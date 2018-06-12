package com.cesecsh.usercenter.ui.activity

import android.os.Bundle
import android.view.View
import com.cesecsh.baselib.ext.enable
import com.cesecsh.baselib.ui.BaseMvpActivity
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.presenter.RegisterPresenter
import com.cesecsh.usercenter.presenter.view.RegisterView
import kotlinx.android.synthetic.main.activity_register.*

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：用户注册界面
 */
class RegisterActivity : BaseMvpActivity<RegisterPresenter>(), View.OnClickListener, RegisterView {
    override fun initPresenter() {
        mPresenter = RegisterPresenter()
        mPresenter.mView = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        ScreenFitUtils.auto(this)
        initView()
    }

    private fun initView() {
        mTitleBar.addLeftBackImageButton().setOnClickListener { finish() }
        mTitleBar.setTitle("注册")
        mBtnSubmit.enable(mEtPhone, { isBtnEnable() })
        mBtnSubmit.enable(mEtPassword, { isBtnEnable() })
        mBtnSubmit.enable(mEtConfirmPassword, { isBtnEnable() })
        mBtnSubmit.enable(mEtVerification, { isBtnEnable() })
    }

    private fun isBtnEnable(): Boolean {
        return mEtPhone.text.isNullOrEmpty().not()
                && mEtPassword.text.isNullOrEmpty().not()
                && mEtConfirmPassword.text.isNullOrEmpty().not()
                && mEtVerification.text.isNullOrEmpty().not()
    }

    override fun onClick(v: View?) {
    }
}