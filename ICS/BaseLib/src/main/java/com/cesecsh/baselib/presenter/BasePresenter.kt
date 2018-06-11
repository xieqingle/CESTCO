package com.cesecsh.baselib.presenter

import android.content.Context
import com.cesecsh.baselib.R
import com.cesecsh.baselib.utils.NetworkUtils
import com.cesecsh.baselib.presenter.view.BaseView
import com.cesecsh.baselib.utils.ResourceUtils

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：MVP中P层 基类
 */
open class BasePresenter<T : BaseView> {

    lateinit var mView: T

    lateinit var context: Context

    /*
        检查网络是否可用
     */
    fun checkNetWork(): Boolean {
        if (NetworkUtils.isConnected(context)) {
            return true
        }
        mView.onError(ResourceUtils.getString(R.string.network_is_not_connect))
        return false
    }
}
