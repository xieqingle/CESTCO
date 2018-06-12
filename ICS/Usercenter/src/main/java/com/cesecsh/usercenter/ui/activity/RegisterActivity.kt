package com.cesecsh.usercenter.ui.activity

import android.os.Bundle
import android.view.View
import com.cesecsh.baselib.ui.BaseMvpActivity
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.R.id.mTitleBar
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
    }

    override fun onClick(v: View?) {
    }
}