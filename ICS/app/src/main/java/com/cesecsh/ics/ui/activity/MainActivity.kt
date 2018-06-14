package com.cesecsh.ics.ui.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.cesecsh.baselib.common.BaseConstant
import com.cesecsh.baselib.ui.base.BaseActivity
import com.cesecsh.ics.R
import com.cesecsh.ics.ui.fragment.SplashFragment
import com.cesecsh.usercenter.IS_LOGIN
import com.cesecsh.usercenter.USER_INFO
import com.cesecsh.usercenter.ui.activity.LoginActivity
import com.kotlin.base.utils.AppPrefsUtils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_layout.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

@Route(path = BaseConstant.MAIN_PATH)
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isLogin = intent.getBooleanExtra("Login", false)
        if (!isLogin) {
            val splashFragment = SplashFragment()
            supportFragmentManager.beginTransaction().replace(R.id.mFLSplashLayout, splashFragment).commit()
            Observable.timer(3, TimeUnit.SECONDS).subscribe {
                runOnUiThread {
                    if (AppPrefsUtils.getBoolean(IS_LOGIN)) {
                        supportFragmentManager.beginTransaction().remove(splashFragment).commit()
                        initMainView()
                    } else {
                        startActivity<LoginActivity>()
                        finish()
                    }
                }
            }
        } else {
            initMainView()
        }


    }

    private fun initMainView() {
        mVsMainLayout.inflate()
        textView.setOnClickListener {
            toast("删除成功")
            AppPrefsUtils.remove(IS_LOGIN)
            AppPrefsUtils.remove(USER_INFO)
        }
    }
}
