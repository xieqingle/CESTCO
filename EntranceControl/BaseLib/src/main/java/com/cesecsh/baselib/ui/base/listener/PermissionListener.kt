package com.cesecsh.baselib.ui.base.listener

/**
 * 作者：RockQ on 2018/7/13
 * 邮箱：qingle6616@sina.com
 *
 * msg：权限请求回调
 */
interface PermissionListener {
    fun onGranted()
    fun onDenied(deniPermissions: List<String>)

}