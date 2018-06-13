package com.cesecsh.ics.ui.activity

import android.os.Bundle
import com.cesecsh.baselib.ui.base.BaseActivity
import com.cesecsh.ics.R
import com.cesecsh.ics.ui.fragment.SplashFragment
import io.reactivex.Observable
import io.reactivex.Observer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val splashFragment = SplashFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mFLSplashLayout, splashFragment).commit()
        Observable.timer(3, TimeUnit.SECONDS).subscribe {
            runOnUiThread {
                supportFragmentManager.beginTransaction().remove(splashFragment).commit()
                mVsMainLayout.inflate()
            }

        }

    }
}
