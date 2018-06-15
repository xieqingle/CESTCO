package com.cesecsh.usercenter.ui.activity

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cesecsh.baselib.common.BaseConstant
import com.cesecsh.baselib.ext.enable
import com.cesecsh.baselib.ext.tarnsPasswordVisible
import com.cesecsh.baselib.ui.base.BaseMvpActivity
import com.cesecsh.baselib.utils.RegularUtils
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.usercenter.LOGIN_SUCCESS
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.data.protocol.User
import com.cesecsh.usercenter.presenter.LoginPresenter
import com.cesecsh.usercenter.presenter.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 */
@Route(path = BaseConstant.PATH_LOGIN)
class LoginActivity : BaseMvpActivity<LoginPresenter>(), LoginView, View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mBtnLogin -> {
                if (!RegularUtils.isMobileExact(mEtPhone.text.toString())) {
                    toast(getString(R.string.please_input_right_phone))
                    return
                }
                mPresenter.login(mEtPhone.text.toString(), mEtPassword.text.toString(), "222222")
            }
            R.id.mBtnSignIn -> {
                startActivity<RegisterActivity>()
            }
            R.id.tvForgetPassword -> {
                startActivity<RetrievePasswordActivity>()
            }
        }
    }


    override fun showLoginResult(user: User) {
        ARouter.getInstance().build(BaseConstant.PATH_MAIN)
                .withBoolean(LOGIN_SUCCESS, true)
                .navigation()
        finish()

    }

    override fun initPresenter() {
        mPresenter = LoginPresenter()
        mPresenter.mView = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ScreenFitUtils.auto(this)
        mBtnLogin.enable(mEtPhone) { isBtnEnable() }
        mBtnLogin.enable(mEtPassword) { isBtnEnable() }
        mBtnShowPassword.tarnsPasswordVisible(mEtPassword)
        mBtnLogin.setOnClickListener(this)
        mBtnSignIn.setOnClickListener(this)
        tvForgetPassword.setOnClickListener(this)
    }

    private fun isBtnEnable(): Boolean {
        return mEtPassword.text.isNullOrEmpty().not()
                && mEtPhone.text.isNullOrEmpty().not()

    }
}
