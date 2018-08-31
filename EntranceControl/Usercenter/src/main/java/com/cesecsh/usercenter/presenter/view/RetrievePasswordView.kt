package com.cesecsh.usercenter.presenter.view

import com.cesecsh.baselib.presenter.view.BaseView

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
interface RetrievePasswordView : BaseView {
    /**
     * 用户密码重置反馈结果
     */
    fun onRetrieveResult(result: String)

    /**
     * 获取验证码
     */
    fun onVerificationResult(result: String)

    /**
     * 验证码获取失败
     */
    fun onVersificationError()
}