package com.cesecsh.ics.ui.fragment

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cesecsh.baselib.ui.base.BaseFragment
import com.cesecsh.ics.R

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
//        activity!!.startActivity<LoginActivity>()
        if (view != null) {
            val pvhA = PropertyValuesHolder.ofFloat("alpha", 1f, 0.7f, 0.1f)
            ObjectAnimator.ofPropertyValuesHolder(view, pvhA).setDuration(3000).start()
        }
        return view
    }
}