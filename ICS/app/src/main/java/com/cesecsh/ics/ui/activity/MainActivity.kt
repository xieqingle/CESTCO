package com.cesecsh.ics.ui.activity

import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_layout.*
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

@Route(path = BaseConstant.PATH_MAIN)
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isLogin = intent.getBooleanExtra(LOGIN_SUCCESS, false)
        if (!isLogin) {
            val splashFragment = SplashFragment()
            supportFragmentManager.beginTransaction().replace(R.id.mFLSplashLayout, splashFragment).commit()
            Observable.timer(3, TimeUnit.SECONDS).subscribe {
                runOnUiThread {
                    if (AppPrefsUtils.getBoolean(IS_LOGIN)) {
                        supportFragmentManager.beginTransaction().remove(splashFragment).commit()
                        initMainView()
                    } else {
//                        startActivity<LoginActivity>()
                        ARouter.getInstance().build(BaseConstant.PATH_LOGIN).navigation()
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
        mTitleBar.setTitle("首页")
//        mTitleBar.addLeftBackImageButton().setOnClickListener{finish()}
//        mTitleBar.setCenterView()
        textView.setOnClickListener {
            toast("删除成功")
            AppPrefsUtils.remove(IS_LOGIN)
            AppPrefsUtils.remove(USER_INFO)
        }
    }
}
