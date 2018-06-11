package com.cesecsh.usercenter.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cesecsh.baselib.ui.BaseActivity
import com.cesecsh.baselib.ui.BaseMvpActivity
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.presenter.LoginPresenter
import com.cesecsh.usercenter.presenter.view.LoginView

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 */
class LoginActivity : BaseMvpActivity<LoginPresenter>(), LoginView {


    override fun showLoginResult(str: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter() {
        mPresenter = LoginPresenter()
    }

    override fun initView() {
        mView = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ScreenFitUtils.auto(this)
    }
}
