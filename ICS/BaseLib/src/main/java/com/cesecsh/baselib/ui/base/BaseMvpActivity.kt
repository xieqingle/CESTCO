package com.cesecsh.baselib.ui.base

import android.os.Bundle
import com.cesecsh.baselib.presenter.BasePresenter
import com.cesecsh.baselib.presenter.view.BaseView
import com.cesecsh.baselib.widget.ProgressLoading
import org.jetbrains.anko.toast

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
abstract class BaseMvpActivity<T : BasePresenter<*>> : BaseActivity(), BaseView {
    /**
     * MVP P层
     */
    lateinit var mPresenter: T
    /**
     * 显示等待对话框
     */
    lateinit var mProgressLoading: ProgressLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
        mProgressLoading = ProgressLoading.create(this)
        mPresenter.mProvider = this
        mPresenter.context = this
    }

    override fun showLoading() {
        mProgressLoading.showLoading()
    }

    override fun hideLoading() {
        mProgressLoading.hideLoading()
    }

    override fun onError(text: String) {
        toast(text)
    }
    abstract fun initPresenter()
}