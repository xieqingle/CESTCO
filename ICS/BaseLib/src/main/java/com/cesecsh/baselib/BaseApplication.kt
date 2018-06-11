package com.cesecsh.baselib

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 */
class BaseApplication : Application() {
    /**
     * 全局伴生对象
     */
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}