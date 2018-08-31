package com.cesecsh.baselib.presenter.view

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg ：MVP中视图回调 基类
 */
interface BaseView {
    /**
     * 显示加载中
     */
    fun showLoading()

    /**
     * 隐藏加载布局
     */
    fun hideLoading()

    /**
     * 显示错误
     * @param text 显示的文本
     */
    fun onError(text: String)
}
