package com.cesecsh.baselib.presenter.view

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg ：MVP中视图回调 基类
 */
interface BaseStatusView : BaseView {
    /**
     * 显示加载中
     */
    override fun showLoading()

    /**
     * 隐藏加载布局
     */
    override fun hideLoading()

    /**
     * 显示错误
     * @param text 显示的文本
     */
    override fun onError(text: String)

    /**
     * 显示空内容布局
     */
    fun showEmptyContent()

    /**
     * 显示网络异常布局
     */
    fun showNetWorkError()

    /**
     * 显示内容
     */
    fun showContent()

    /**
     * 显示错误布局
     */
    fun showError()

}
