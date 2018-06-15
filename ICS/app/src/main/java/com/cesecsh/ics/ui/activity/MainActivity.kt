package com.cesecsh.ics.ui.activity

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cesecsh.baselib.common.BaseConstant
import com.cesecsh.baselib.ui.base.BaseActivity
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.ics.R
import com.cesecsh.ics.ui.fragment.SplashFragment
import com.cesecsh.usercenter.IS_LOGIN
import com.cesecsh.usercenter.LOGIN_SUCCESS
import com.cesecsh.usercenter.USER_INFO
import com.kotlin.base.utils.AppPrefsUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_layout.*
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

@Route(path = BaseConstant.PATH_MAIN)
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = View.inflate(this, R.layout.activity_main, null)
        setContentView(inflate)
        val isLogin = intent.getBooleanExtra(LOGIN_SUCCESS, false)
        val splashFragment = SplashFragment()
        if (!isLogin) {
            supportFragmentManager.beginTransaction().replace(R.id.mFLSplashLayout, splashFragment).commit()
            Observable.timer(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (AppPrefsUtils.getBoolean(IS_LOGIN)) {
                            supportFragmentManager.beginTransaction().remove(splashFragment).commit()
                            initMainView()
                        } else {
                            ARouter.getInstance().build(BaseConstant.PATH_LOGIN).navigation()
                            finish()
                        }

                    }
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.mFLSplashLayout, splashFragment).commit()
            initMainView()
        }


    }

    private fun initMainView() {
        mVsMainLayout.inflate()
        ScreenFitUtils.auto(this)
        mTitleBar.setTitle("首页")
        textView.setOnClickListener {
            toast("删除成功")
            AppPrefsUtils.remove(IS_LOGIN)
            AppPrefsUtils.remove(USER_INFO)
        }
    }
}
