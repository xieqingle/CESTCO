package com.cesecsh.baselib.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cesecsh.baselib.presenter.BasePresenter
import com.cesecsh.baselib.presenter.view.BaseView
import com.cesecsh.baselib.widget.dialog.ProgressDialog

/**
 * 作者：RockQ on 2018/6/13
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
abstract class BaseMvpFragment<T : BasePresenter<*>> : BaseFragment(), BaseView {
    /**
     * MVP p层
     */
    lateinit var mPresenter: T
    /**
     * 显示延迟对话框
     */
    lateinit var mProgressDialog: ProgressDialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initPresenter()
        mProgressDialog = ProgressDialog.create(activity!!)
        mPresenter.context = activity!!
        mPresenter.mProvider = this
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    abstract fun initPresenter()
}