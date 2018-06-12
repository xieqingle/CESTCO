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
        mTitleBar.setTitle("在这个类里面你会发现，我还声明了两个方法，我需要的是BarData里的数据，但又不仅仅只需要这个数据，所以我声明了一个类来封装它，其实这个相当于装饰者模式了。Kotlin有更好的方式实现这个模式")
    }

    override fun onClick(v: View?) {
    }
}