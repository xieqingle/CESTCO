package com.cesecsh.baselib.ui.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.cesecsh.baselib.R
import com.cesecsh.baselib.common.ActivityManager
import com.cesecsh.baselib.presenter.BasePresenter
import com.cesecsh.baselib.ui.base.listener.PermissionListener
import java.util.ArrayList

/**
 * 作者：RockQ on 2018/7/13
 * 邮箱：qingle6616@sina.com
 *
 * msg： 基于 MVP 模式的权限请求
 */
abstract class BasePermissionActivity<T : BasePresenter<*>> : BaseMvpActivity<T>() {
    /**
     * 请求回调
     */
    private lateinit var mListener: PermissionListener

    /**
     * 权限请求吗
     */
    private val PERMISSION_REQUEST_CODE = 5421

    private val PACKAGE_URL_SCHEME = "package:" // 方案

    fun requestPermission(listener: PermissionListener, vararg requestPermissions: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mListener = listener
            val permissions = checkPermissionGranted(*requestPermissions)
            if (permissions != null && !permissions.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
            } else {
                listener.onGranted()
            }
        } else {
            listener.onGranted()
        }
    }

    /**
     * 权限请求结果处理
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val deniedPermissions = ArrayList<String>()
                for (i in grantResults.indices) {
                    val grantResult = grantResults[i]
                    val permission = permissions[i]
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permission)
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    mListener.onGranted()
                } else {
                    mListener.onDenied(deniedPermissions)
                    showMissingPermissionDialog()
                }
            }
        }
    }

    /**
     * 检车所有的权限是否都允许
     */
    protected fun checkPermissionGranted(vararg requestPermissions: String): List<String>? {
        if (requestPermissions == null || requestPermissions.isEmpty()) return null
        val permissions = ArrayList<String>()
        for (requestPermission in requestPermissions) {
            if (ContextCompat.checkSelfPermission(this, requestPermission) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(requestPermission)
            }
        }
        return permissions
    }

    /**
     *   显示缺失权限提示
     */
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(ActivityManager.instance.getTopActivity())
        builder.setTitle(R.string.help)
        builder.setMessage(R.string.string_permission_help_text)
        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.quit) { dialog, which -> ActivityManager.instance.finishAllActivity() }
                .setPositiveButton(R.string.settings) { _, _ -> startAppSettings() }
        builder.setCancelable(false)
        builder.show()
    }


    // 启动应用的设置
    protected fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(PACKAGE_URL_SCHEME + packageName)
        startActivity(intent)
    }
}