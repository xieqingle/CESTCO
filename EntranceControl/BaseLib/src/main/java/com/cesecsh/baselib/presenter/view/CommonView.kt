package com.cesecsh.baselib.presenter.view

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：通用的view，返回字符串进行处理
 */
interface CommonView : BaseView {
    fun onResult(result: String)
}