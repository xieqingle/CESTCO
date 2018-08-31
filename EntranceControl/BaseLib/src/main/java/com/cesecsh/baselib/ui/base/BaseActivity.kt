package com.cesecsh.baselib.ui.base

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.cesecsh.baselib.common.ActivityManager
import com.cesecsh.baselib.utils.ToastUtils
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
abstract class BaseActivity : RxAppCompatActivity() {
    protected lateinit var mContext: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        ActivityManager.instance.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.instance.finishActivity(this)
    }

    protected fun toast(str: String) {
        ToastUtils.toast(this, str)
    }
}