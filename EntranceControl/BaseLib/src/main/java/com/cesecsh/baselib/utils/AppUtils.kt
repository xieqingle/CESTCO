package com.cesecsh.baselib.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo


/**
 * 作者：RockQ on 2018/6/20
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
object AppUtils {
    /**
     * 判断当前 app 是否是 debug 版本
     */
    fun isAPKInDebug(context: Context): Boolean {
        return try {
            (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 检测其他应用是否处于 debug 模式。
     */
    fun isAPKInDebug(context: Context, packageName: String): Boolean {
        return try {
            val pkgInfo = context.packageManager.getPackageInfo(packageName, 1)
            if (pkgInfo != null) pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            else false
        } catch (e: Exception) {
            false
        }


    }
}