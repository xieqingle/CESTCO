package com.cesecsh.baselib.ui

import android.app.Activity
import android.os.Bundle
import com.cesecsh.baselib.common.ActivityManager
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
abstract class BaseActivity : RxAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.instance.addActivity(this)
        initPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.instance.finishActivity(this)
    }

    abstract fun initPresenter()
}