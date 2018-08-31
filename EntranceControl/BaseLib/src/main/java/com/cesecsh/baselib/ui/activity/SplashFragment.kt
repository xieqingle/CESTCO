package com.cesecsh.baselib.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cesecsh.baselib.R
import com.cesecsh.baselib.helper.StatusBarHelper
import com.cesecsh.baselib.ui.base.BaseFragment

/**
 * 作者：RockQ on 2018/6/13
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class SplashFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        StatusBarHelper.translucent(activity)
//        if (view != null) {
//            val pvhA = PropertyValuesHolder.ofFloat("alpha", 1f, 0.7f, 0.1f)
//            ObjectAnimator.ofPropertyValuesHolder(view, pvhA).setDuration(2000).start()
//        }
        return view
    }
}