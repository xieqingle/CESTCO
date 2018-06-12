package com.cesecsh.usercenter.ui.activity

import android.os.Bundle
import android.view.View
import com.cesecsh.baselib.ui.BaseMvpActivity
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.presenter.ForgetPasswordPresenter
import com.cesecsh.usercenter.presenter.view.ForgetPasswordView

class ForgetPasswordActivity : BaseMvpActivity<ForgetPasswordPresenter>(), View.OnClickListener, ForgetPasswordView {
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter() {
        mPresenter = ForgetPasswordPresenter()
        mPresenter.mView = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        ScreenFitUtils.auto(this)
    }
}
