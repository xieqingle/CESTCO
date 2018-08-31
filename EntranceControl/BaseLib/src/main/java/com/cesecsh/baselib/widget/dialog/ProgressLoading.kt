package com.cesecsh.baselib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.widget.ImageView
import com.cesecsh.baselib.R
import org.jetbrains.anko.find

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：等待 dialog 实现1，有部分机型显示不出旋转
 */
class ProgressLoading(context: Context, theme: Int) : Dialog(context, theme) {
    companion object {

        private lateinit var mDialog: ProgressLoading
        private var animDrawable: AnimationDrawable? = null
        /**
         * 加载对话框
         */
        fun create(context: Context): ProgressLoading {
            mDialog = ProgressLoading(context, R.style.LightProgressDialog)
            //设置布局
            mDialog.setContentView(R.layout.progress_dialog)
            mDialog.setCancelable(true)
            mDialog.setCanceledOnTouchOutside(false)
            mDialog.window.attributes.gravity = Gravity.CENTER

            val lp = mDialog.window.attributes
            lp.dimAmount = 0.2f
            //设置属性
            mDialog.window.attributes = lp

            //获取动画视图
            val loadingView = mDialog.find<ImageView>(R.id.iv_loading)
            animDrawable = loadingView.background as AnimationDrawable

            return mDialog
        }


    }

    /**
     * 显示对话框，动画开始
     */
    fun showLoading() {
        super.show()
        animDrawable?.start()
    }

    /**
     * 隐藏对话框，动画消失
     */
    fun hideLoading() {
        super.dismiss()
        animDrawable?.stop()
    }
}