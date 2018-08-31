package com.cesecsh.usercenter.ui.activity

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cesecsh.baselib.common.ARouterPath
import com.cesecsh.baselib.common.DataResource
import com.cesecsh.baselib.ext.enable
import com.cesecsh.baselib.ext.tarnsPasswordVisible
import com.cesecsh.baselib.helper.StatusBarHelper
import com.cesecsh.baselib.ui.base.BaseMvpActivity
import com.cesecsh.baselib.utils.RegularUtils
import com.cesecsh.baselib.utils.StringUtils
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.data.protocol.User
import com.cesecsh.usercenter.presenter.LoginPresenter
import com.cesecsh.usercenter.presenter.view.LoginView
import com.kotlin.base.utils.AppPrefsUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 */
@Route(path = ARouterPath.PATH_LOGIN)
class LoginActivity : BaseMvpActivity<LoginPresenter>(), LoginView, View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mBtnLogin -> {
                if (!RegularUtils.isMobileExact(mEtPhone.text.toString())) {
                    toast(getString(R.string.please_input_right_phone))
                    return
                }
                mPresenter.login(mEtPhone.text.toString(), mEtPassword.text.toString(), "")
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
        ARouter.getInstance().build(ARouterPath.PATH_MAIN)
                .navigation()
        finish()

    }

    override fun initPresenterAndView() {
        mPresenter = LoginPresenter()
        mPresenter.mView = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        ScreenFitUtils.auto(this)
        StatusBarHelper.translucent(this)

        val userName = AppPrefsUtils.getString(DataResource.userName)
        if (!StringUtils.isNullOrEmpty(userName)) {
            mEtPhone.setText(userName)
        }
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
