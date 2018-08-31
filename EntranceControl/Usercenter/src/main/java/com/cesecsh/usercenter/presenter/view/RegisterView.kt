package com.cesecsh.usercenter.presenter.view

import com.cesecsh.baselib.presenter.view.BaseView
import com.cesecsh.usercenter.data.protocol.Agreement

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
interface RegisterView : BaseView {
    /**
     * 用户协议
     */
    fun onAgreeementResult(agreement: Agreement)

    /**
     * 用户密码重置反馈结果
     */
    fun onRegisterResult(result: String)

    /**
     * 获取验证码
     */
    fun onVerificationResult(result: String)

    /**
     * 验证码获取失败
     */
    fun onVersificationError()
}