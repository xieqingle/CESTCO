package com.cesecsh.usercenter.presenter

import com.cesecsh.baselib.common.BaseConstant
import com.cesecsh.baselib.common.CodeState
import com.cesecsh.baselib.data.JsonUtils
import com.cesecsh.baselib.ext.execute
import com.cesecsh.baselib.presenter.BasePresenter
import com.cesecsh.baselib.rx.BaseObserver
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.data.UserService
import com.cesecsh.usercenter.data.impl.UserServiceImpl
import com.cesecsh.usercenter.data.protocol.Agreement
import com.cesecsh.usercenter.presenter.view.RegisterView
import okhttp3.ResponseBody

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class RegisterPresenter : BasePresenter<RegisterView>() {
    private val userService: UserService = UserServiceImpl()
    /**
     * 获取验证码
     */
    fun getVerification(phone: String) {
        userService.getVerification(phone, BaseConstant.SIGN_IN)
                .execute(object : BaseObserver<ResponseBody>(mView) {
                    override fun onNext(t: ResponseBody) {
                        val simpleObject = JsonUtils.json2SimpleObject(t.string())
                        if (Integer.valueOf(simpleObject.code) != CodeState.SUCCESS)
                            (mView as RegisterView).onVerificationResult(context.getString(R.string.verification_send_success))
                        else (mView as RegisterView).onVersificationError()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        (mView as RegisterView).onVersificationError()
                    }
                }, mProvider)
    }

    fun register(phone: String, password: String, randCode: String) {
        userService.register(phone, password, randCode)
                .execute(object : BaseObserver<ResponseBody>(mView) {
                    override fun onNext(t: ResponseBody) {
                        val simpleObject = JsonUtils.json2SimpleObject(t.string())
                        if (Integer.valueOf(simpleObject.code) != CodeState.SUCCESS)
                            (mView as RegisterView).onRegisterResult(context.getString(R.string.register_success))
                    }

                }, mProvider)
    }

    fun getUserProtocol() {
        userService.getUserProtocol()
                .execute(object : BaseObserver<ResponseBody>(mView) {
                    override fun onNext(t: ResponseBody) {
                        val normalObject = JsonUtils.json2NormalObject<Agreement>(t.string(), Agreement::class.java)
                        if (normalObject.obj != null) {
                            (mView as RegisterView).onAgreeementResult(normalObject.obj!!)
                        }

                    }
                }, mProvider)
    }
}