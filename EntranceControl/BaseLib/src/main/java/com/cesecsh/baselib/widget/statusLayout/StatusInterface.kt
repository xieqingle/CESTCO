package com.cesecsh.baselib.widget.statusLayout

/**
 * 作者：RockQ on 2018/8/15
 * 邮箱：qingle6616@sina.com
 *
 * msg：状态接口
 */
interface StatusInterface {
    /**
     * 显示内容
     */
    fun showContent()

    /**
     * 显示错误
     */
    fun showError()

    /**
     * 显示加载中
     */
    fun showLoading()

    /**
     * 显示网络异常
     */
    fun showNetWorkError()

    /**
     * 显示空布局
     */
    fun showEmpty()
}