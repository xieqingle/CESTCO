package com.cesecsh.baselib.ui.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cesecsh.baselib.R
import com.cesecsh.baselib.common.*
import com.cesecsh.baselib.ui.base.BaseActivity
import com.kotlin.base.utils.AppPrefsUtils

@Route(path = ARouterPath.PATH_START_PAGE)
class StartPageActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startpage)
//        直接使用fragment tag 会出现沉浸式失败，以及会闪一下的原因
        supportFragmentManager.beginTransaction().remove(SplashFragment()).commit()
//        Handler().postDelayed({
//            runOnUiThread {
        if (AppPrefsUtils.getBoolean(DataResource.IS_LOGIN)) {
            ARouter.getInstance()
                    .build(ARouterPath.PATH_MAIN)
                    .withTransition(R.anim.scale_enter, R.anim.slide_still)
                    .navigation()
        } else {
            ARouter.getInstance()
                    .build(ARouterPath.PATH_LOGIN)
                    .withTransition(R.anim.scale_enter, R.anim.slide_still)
                    .navigation()
        }
        finish()
//            }
//        }, 2000)

    }
}
