package com.cesecsh.baselib.common

import android.app.Activity
import android.content.Context
import android.view.View
import com.cesecsh.baselib.utils.ToastUtils

/**
 * Created by 上海中电
 * on 2016/10/25
 * 基础View，一般情况下所有的布局View继承该View
 */

abstract class BaseViewPage(var mContext: Activity) {
    var mRootView: View// 菜单详情页根布局


    init {
        mRootView = initView()
    }

    // 初始化布局,必须子类实现
    abstract fun initView(): View

    // 初始化数据
    open fun initData() {}

    protected fun toast(content: String) {
        ToastUtils.toast(mContext, content)
    }

    protected fun toast(resId: Int) {
        ToastUtils.toast(mContext, mContext.resources.getString(resId))
    }

    protected fun toast(redId: Int, type: Int) {
        ToastUtils.toast(mContext, mContext.resources.getString(redId), type)

    }

    protected fun toast(content: String, type: Int) {
        ToastUtils.toast(mContext, content, type)
    }
}
