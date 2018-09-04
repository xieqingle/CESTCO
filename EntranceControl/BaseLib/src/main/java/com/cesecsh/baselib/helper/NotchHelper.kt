package com.cesecsh.baselib.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.WindowManager

object NotchHelper {

    private val TAG = "QMUINotchHelper"

    private val NOTCH_IN_SCREEN_VOIO = 0x00000020
    private val MIUI_NOTCH = "ro.miui.notch"
    private var sHasNotch: Boolean? = null
    private var sRotation0SafeInset: Rect? = null
    private var sRotation90SafeInset: Rect? = null
    private var sRotation180SafeInset: Rect? = null
    private var sRotation270SafeInset: Rect? = null
    private var sNotchSizeInHawei: IntArray? = null
    private var sHuaweiIsNotchSetToShow: Boolean? = null
    private var sXiaomiIsNotchSetToShow: Boolean? = null

    @JvmStatic
    val isNotchOfficialSupport: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    @JvmStatic
    fun getSafeInsetTop(activity: Activity): Int {
        return if (!hasNotch(activity)) {
            0
        } else getSafeInsetRect(activity).top
    }

    @JvmStatic
    @SuppressLint("NewApi")
    fun hasNotch(activity: Activity): Boolean {
        if (sHasNotch == null) {
            if (isNotchOfficialSupport) {
                val window = activity.window ?: return false
                val decorView = window.decorView ?: return false
                val displayCutout = decorView.rootWindowInsets.displayCutout
                sHasNotch = displayCutout != null
            } else {
                sHasNotch = has3rdNotch(activity)
            }
        }
        return sHasNotch!!
    }

    @JvmStatic
    @SuppressLint("NewApi")
    private fun getSafeInsetRect(activity: Activity): Rect {
        if (isNotchOfficialSupport) {
            val rect = Rect()
            val displayCutout = activity.window.decorView
                    .rootWindowInsets.displayCutout
            if (displayCutout != null) {
                rect.set(displayCutout.safeInsetLeft, displayCutout.safeInsetTop,
                        displayCutout.safeInsetRight, displayCutout.safeInsetBottom)
            }
            return rect
        }
        return get3rdSafeInsetRect(activity)
    }

    @JvmStatic
    fun has3rdNotch(context: Context): Boolean {
        if (DeviceHelper.isHuawei) {
            return hasNotchInHuawei(context)
        } else if (DeviceHelper.isVivo) {
            return hasNotchInVivo(context)
        } else if (DeviceHelper.isOppo) {
            return hasNotchInOppo(context)
        } else if (DeviceHelper.isXiaomi) {
            return hasNotchInXiaomi(context)
        }
        return false
    }

    @JvmStatic
    private fun get3rdSafeInsetRect(context: Context): Rect {
        // 全面屏设置项更改
        if (DeviceHelper.isHuawei) {
            val isHuaweiNotchSetToShow = DisplayHelper.huaweiIsNotchSetToShowInSetting(context)
            if (sHuaweiIsNotchSetToShow != null && sHuaweiIsNotchSetToShow != isHuaweiNotchSetToShow) {
                clearLandscapeRectInfo()
            }
            sHuaweiIsNotchSetToShow = isHuaweiNotchSetToShow
        } else if (DeviceHelper.isXiaomi) {
            val isXiaomiNotchSetToShow = DisplayHelper.xiaomiIsNotchSetToShowInSetting(context)
            if (sXiaomiIsNotchSetToShow != null && sXiaomiIsNotchSetToShow != isXiaomiNotchSetToShow) {
                clearAllRectInfo()
            }
            sXiaomiIsNotchSetToShow = isXiaomiNotchSetToShow
        }
        val screenRotation = getScreenRotation(context)
        if (screenRotation == Surface.ROTATION_90) {
            if (sRotation90SafeInset == null) {
                sRotation90SafeInset = getRectInfoRotation90(context)
            }
            return sRotation90SafeInset as Rect
        } else if (screenRotation == Surface.ROTATION_180) {
            if (sRotation180SafeInset == null) {
                sRotation180SafeInset = getRectInfoRotation180(context)
            }
            return sRotation180SafeInset as Rect
        } else if (screenRotation == Surface.ROTATION_270) {
            if (sRotation270SafeInset == null) {
                sRotation270SafeInset = getRectInfoRotation270(context)
            }
            return sRotation270SafeInset as Rect
        } else {
            if (sRotation0SafeInset == null) {
                sRotation0SafeInset = getRectInfoRotation0(context)
            }
            return sRotation0SafeInset as Rect
        }
    }

    @JvmStatic

    fun hasNotchInHuawei(context: Context): Boolean {
        var hasNotch = false
        try {
            val cl = context.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            hasNotch = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.i(TAG, "hasNotchInHuawei ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e(TAG, "hasNotchInHuawei NoSuchMethodException")
        } catch (e: Exception) {
            Log.e(TAG, "hasNotchInHuawei Exception")
        }

        return hasNotch
    }

    @JvmStatic
    fun hasNotchInVivo(context: Context): Boolean {
        var ret = false
        try {
            val cl = context.classLoader
            val ftFeature = cl.loadClass("android.util.FtFeature")
            val methods = ftFeature.declaredMethods
            if (methods != null) {
                for (i in methods.indices) {
                    val method = methods[i]
                    if (method.name.equals("isFeatureSupport", ignoreCase = true)) {
                        ret = method.invoke(ftFeature, NOTCH_IN_SCREEN_VOIO) as Boolean
                        break
                    }
                }
            }
        } catch (e: ClassNotFoundException) {
            Log.i(TAG, "hasNotchInVivo ClassNotFoundException")
        } catch (e: Exception) {
            Log.e(TAG, "hasNotchInVivo Exception")
        }

        return ret
    }

    @JvmStatic
    fun hasNotchInOppo(context: Context): Boolean {
        return context.packageManager
                .hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    @JvmStatic
    fun hasNotchInXiaomi(context: Context): Boolean {
        try {
            @SuppressLint("PrivateApi") val spClass = Class.forName("android.os.SystemProperties")
            val getMethod = spClass.getDeclaredMethod("getInt", String::class.java, Int::class.java)
            val hasNotch = getMethod.invoke(null, MIUI_NOTCH, 0) as Int
            return hasNotch == 1
        } catch (ignore: Exception) {
        }

        return false
    }

    @JvmStatic
    private fun clearLandscapeRectInfo() {
        sRotation90SafeInset = null
        sRotation270SafeInset = null
    }

    @JvmStatic
    private fun clearAllRectInfo() {
        sRotation0SafeInset = null
        sRotation90SafeInset = null
        sRotation180SafeInset = null
        sRotation270SafeInset = null
    }

    /**
     * this method is private, because we do not need to handle tablet
     *
     * @param context
     * @return
     */
    @JvmStatic
    private fun getScreenRotation(context: Context): Int {
        val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                ?: return Surface.ROTATION_0
        val display = w.defaultDisplay ?: return Surface.ROTATION_0

        return display.rotation
    }

    @JvmStatic
    private fun getRectInfoRotation90(context: Context): Rect {
        val rect = Rect()
        if (DeviceHelper.isVivo) {
            rect.left = getNotchHeightInVivo(context)
            rect.right = 0
        } else if (DeviceHelper.isOppo) {
            rect.left = StatusBarHelper.getStatusbarHeight(context)
            rect.right = 0
        } else if (DeviceHelper.isHuawei) {
            if (sHuaweiIsNotchSetToShow!!) {
                rect.left = getNotchSizeInHuawei(context)[1]
            } else {
                rect.left = 0
            }
            rect.right = 0
        } else if (DeviceHelper.isXiaomi) {
            if (sXiaomiIsNotchSetToShow!!) {
                rect.left = getNotchHeightInXiaomi(context)
            } else {
                rect.left = 0
            }
            rect.right = 0
        }
        return rect
    }

    @JvmStatic
    private fun getRectInfoRotation180(context: Context): Rect {
        val rect = Rect()
        if (DeviceHelper.isVivo) {
            rect.top = 0
            rect.bottom = getNotchHeightInVivo(context)
        } else if (DeviceHelper.isOppo) {
            rect.top = 0
            rect.bottom = StatusBarHelper.getStatusbarHeight(context)
        } else if (DeviceHelper.isHuawei) {
            val notchSize = getNotchSizeInHuawei(context)
            rect.top = 0
            rect.bottom = notchSize[1]
        } else if (DeviceHelper.isXiaomi) {
            rect.top = 0
            rect.bottom = getNotchHeightInXiaomi(context)
        }
        return rect
    }

    @JvmStatic
    private fun getRectInfoRotation270(context: Context): Rect {
        val rect = Rect()
        if (DeviceHelper.isVivo) {
            rect.right = getNotchHeightInVivo(context)
            rect.left = 0
        } else if (DeviceHelper.isOppo) {
            rect.right = StatusBarHelper.getStatusbarHeight(context)
            rect.left = 0
        } else if (DeviceHelper.isHuawei) {
            if (sHuaweiIsNotchSetToShow!!) {
                rect.right = getNotchSizeInHuawei(context)[1]
            } else {
                rect.right = 0
            }
            rect.left = 0
        } else if (DeviceHelper.isXiaomi) {
            if (sXiaomiIsNotchSetToShow!!) {
                rect.right = getNotchHeightInXiaomi(context)
            } else {
                rect.right = 0
            }
            rect.left = 0
        }
        return rect
    }

    @JvmStatic
    private fun getRectInfoRotation0(context: Context): Rect {
        val rect = Rect()
        if (DeviceHelper.isVivo) {
            // TODO vivo 显示与亮度-第三方应用显示比例
            rect.top = getNotchHeightInVivo(context)
            rect.bottom = 0
        } else if (DeviceHelper.isOppo) {
            // TODO OPPO 设置-显示-应用全屏显示-凹形区域显示控制
            rect.top = StatusBarHelper.getStatusbarHeight(context)
            rect.bottom = 0
        } else if (DeviceHelper.isHuawei) {
            val notchSize = getNotchSizeInHuawei(context)
            rect.top = notchSize[1]
            rect.bottom = 0
        } else if (DeviceHelper.isXiaomi) {
            rect.top = getNotchHeightInXiaomi(context)
            rect.bottom = 0
        }
        return rect
    }

    @JvmStatic
    fun getNotchHeightInVivo(context: Context): Int {
        return DisplayHelper.dp2px(context, 27)
    }

    @JvmStatic
    fun getNotchSizeInHuawei(context: Context): IntArray {
        if (sNotchSizeInHawei == null) {
            sNotchSizeInHawei = intArrayOf(0, 0)
            try {
                val cl = context.classLoader
                val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
                val get = HwNotchSizeUtil.getMethod("getNotchSize")
                sNotchSizeInHawei = get.invoke(HwNotchSizeUtil) as IntArray
            } catch (e: ClassNotFoundException) {
                Log.e(TAG, "getNotchSizeInHuawei ClassNotFoundException")
            } catch (e: NoSuchMethodException) {
                Log.e(TAG, "getNotchSizeInHuawei NoSuchMethodException")
            } catch (e: Exception) {
                Log.e(TAG, "getNotchSizeInHuawei Exception")
            }

        }
        return sNotchSizeInHawei as IntArray
    }

    @JvmStatic
    fun getNotchHeightInXiaomi(context: Context): Int {
        val resourceId = context.resources.getIdentifier("notch_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else -1
    }

    @JvmStatic
    fun getSafeInsetBottom(activity: Activity): Int {
        return if (!hasNotch(activity)) {
            0
        } else getSafeInsetRect(activity).bottom
    }

    @JvmStatic
    fun getSafeInsetLeft(activity: Activity): Int {
        return if (!hasNotch(activity)) {
            0
        } else getSafeInsetRect(activity).left
    }

    @JvmStatic
    fun getSafeInsetRight(activity: Activity): Int {
        return if (!hasNotch(activity)) {
            0
        } else getSafeInsetRect(activity).right
    }

    @JvmStatic
    fun getSafeInsetTop(view: View): Int {
        return if (!hasNotch(view)) {
            0
        } else getSafeInsetRect(view).top
    }

    @JvmStatic
    @SuppressLint("NewApi")
    fun hasNotch(view: View): Boolean {
        if (sHasNotch == null) {
            if (isNotchOfficialSupport) {
                val windowInsets = view.rootWindowInsets
                if (windowInsets != null) {
                    val displayCutout = windowInsets.displayCutout
                    sHasNotch = displayCutout != null
                } else {
                    // view not attached
                    return false
                }

            } else {
                sHasNotch = has3rdNotch(view.context)
            }
        }
        return sHasNotch!!
    }

    @JvmStatic
    @SuppressLint("NewApi")
    private fun getSafeInsetRect(view: View): Rect {
        if (isNotchOfficialSupport) {
            val rect = Rect()
            val displayCutout = view.rootWindowInsets.displayCutout
            if (displayCutout != null) {
                rect.set(displayCutout.safeInsetLeft, displayCutout.safeInsetTop,
                        displayCutout.safeInsetRight, displayCutout.safeInsetBottom)
            }
            return rect
        }
        return get3rdSafeInsetRect(view.context)
    }

    @JvmStatic
    fun getSafeInsetBottom(view: View): Int {
        return if (!hasNotch(view)) {
            0
        } else getSafeInsetRect(view).bottom
    }

    @JvmStatic
    fun getSafeInsetLeft(view: View): Int {
        return if (!hasNotch(view)) {
            0
        } else getSafeInsetRect(view).left
    }

    @JvmStatic
    fun getSafeInsetRight(view: View): Int {
        return if (!hasNotch(view)) {
            0
        } else getSafeInsetRect(view).right
    }

    @JvmStatic
    private fun clearPortraitRectInfo() {
        sRotation0SafeInset = null
        sRotation180SafeInset = null
    }

    @JvmStatic
    fun getNotchWidthInXiaomi(context: Context): Int {
        val resourceId = context.resources.getIdentifier("notch_width", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else -1
    }

    @JvmStatic
    fun getNotchWidthInVivo(context: Context): Int {
        return DisplayHelper.dp2px(context, 100)
    }

}
