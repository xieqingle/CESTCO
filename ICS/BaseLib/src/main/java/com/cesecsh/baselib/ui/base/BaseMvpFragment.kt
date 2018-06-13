package com.cesecsh.baselib.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 作者：RockQ on 2018/6/13
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
abstract class BaseMvpFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        initPresenter()
    }

    abstract fun initPresenter()
}