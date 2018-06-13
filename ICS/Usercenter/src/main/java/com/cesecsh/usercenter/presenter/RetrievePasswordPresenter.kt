package com.cesecsh.usercenter.presenter

import android.text.Editable
import android.widget.Button
import com.cesecsh.baselib.common.BaseConstant
import com.cesecsh.baselib.common.CodeState
import com.cesecsh.baselib.data.JsonUtils
import com.cesecsh.baselib.ext.execute
import com.cesecsh.baselib.presenter.BasePresenter
import com.cesecsh.baselib.rx.BaseObserver
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.data.UserService
import com.cesecsh.usercenter.data.impl.UserServiceImpl
import com.cesecsh.usercenter.data.protocol.User
import com.cesecsh.usercenter.presenter.view.LoginView
import com.cesecsh.usercenter.presenter.view.RetrievePasswordView
import okhttp3.ResponseBody

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class RetrievePasswordPresenter : BasePresenter<RetrievePasswordView>() {
    private val userService: UserService = UserServiceImpl()

    fun resetPassword(phone: String, password: String, verification: String) {
        userService.resetPassWord(phone, password, verification)
                .execute(object : BaseObserver<ResponseBody>(mView) {
                    override fun onNext(t: ResponseBody) {
                        val simpleObject = JsonUtils.json2SimpleObject(t.string())
                        if (Integer.valueOf(simpleObject.code) != CodeState.SUCCESS)
                            (mView as RetrievePasswordView).onRetrieveResult(context.getString(R.string.password_change_success))
                    }
                }, mProvider)
    }

    fun getVerification(phone: String) {
        userService.getVerification(phone, BaseConstant.RETRIEVE)
                .execute(object : BaseObserver<ResponseBody>(mView) {
                    override fun onNext(t: ResponseBody) {
                        val simpleObject = JsonUtils.json2SimpleObject(t.string())
                        if (Integer.valueOf(simpleObject.code) != CodeState.SUCCESS)
                            (mView as RetrievePasswordView).onVerificationResult(context.getString(R.string.verification_send_success))
                        else (mView as RetrievePasswordView).onVersificationError()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        (mView as RetrievePasswordView).onVersificationError()
                    }
                }, mProvider)
    }
}