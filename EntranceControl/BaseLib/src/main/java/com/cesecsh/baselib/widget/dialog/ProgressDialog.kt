package com.cesecsh.baselib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import com.cesecsh.baselib.R
import kotlinx.android.synthetic.main.dialg_progress_layout.*

/**
 * 作者：RockQ on 2018/7/16
 * 邮箱：qingle6616@sina.com
 *
 * msg：等待 dialog 实现2，自定义loadingView，避免实现1
 */
class ProgressDialog private constructor(context: Context) : Dialog(context, R.style.LightProgressDialog) {
    /**
     * 单例Progress dialog
     */
    companion object {
        private lateinit var mDialog: ProgressDialog
        fun create(context: Context): ProgressDialog {
            mDialog = ProgressDialog(context)
            mDialog.setContentView(R.layout.dialg_progress_layout)
            mDialog.setCancelable(true)
            mDialog.setCanceledOnTouchOutside(true)
            mDialog.window.attributes.gravity = Gravity.CENTER
            val lp = mDialog.window.attributes
            lp.dimAmount = 0.2f
            //设置属性
            mDialog.window.attributes = lp
            return mDialog
        }

    }


    fun showLoading() {
        super.show()
        mLoadingView.start()
    }

    fun hideLoading() {
        mLoadingView.stop()
        super.dismiss()
    }
}