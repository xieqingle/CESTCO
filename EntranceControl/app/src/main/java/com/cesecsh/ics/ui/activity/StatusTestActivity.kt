package com.cesecsh.ics.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cesecsh.baselib.widget.statusLayout.StatusFrameLayout
import com.cesecsh.ics.R
import kotlinx.android.synthetic.main.activity_status_test.*

class StatusTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_test)
//        ScreenFitUtils.auto(this)
        mSflRoot.showLoading()

        mSflRoot.setOnClickListener {
            when (mSflRoot.getStatus()) {
                StatusFrameLayout.STATUS_LOADING -> mSflRoot.showEmpty()
                StatusFrameLayout.STATUS_EMPTY -> mSflRoot.showError()
                StatusFrameLayout.STATUS_ERROR -> mSflRoot.showNetWorkError()
                StatusFrameLayout.STATUS_NETWORK_ERROR -> mSflRoot.showContent()
                StatusFrameLayout.STATUS_CONTENT -> mSflRoot.showLoading()
            }
        }

    }
}
