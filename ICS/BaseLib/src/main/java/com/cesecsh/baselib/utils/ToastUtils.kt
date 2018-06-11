package com.cesecsh.baselib.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.cesecsh.baselib.R

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：弹出吐司
 */
object ToastUtils {
    private var message: String? = null
    private var instance: ToastUtils? = null
    val TYPE_SUCCESS = 0 // 操作成功后的toast
    val TYPE_WARNING = 1 // 警示类toast
    val TYPE_DEFAULT = 2 // 默认toast
    private var mToast: Toast? = null

    /**
     * 居中的文字toast
     *
     * @param context
     * @param str
     */
    fun toast(context: Context, str: String) {
        val mView = ResourceUtils.inflate(R.layout.toast_custom_layout)
        val text = mView.findViewById(R.id.toast_text) as TextView
        val iv = mView.findViewById(R.id.toast_iv) as ImageView
        iv.visibility = View.GONE

        // 连续按只以最后一次为起点显示2s,持续时间不再按次数累加
        text.text = str
        if (mToast == null || str != message) {
            mToast = Toast(ResourceUtils.getContext())
            mToast!!.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            mToast!!.duration = Toast.LENGTH_SHORT
            mToast!!.view = mView
            message = str
        }
        mToast!!.show()

    }

    /**
     * 显示Toast
     *
     * @param context
     * @param str
     */

    fun toast(context: Context, str: String, type: Int) {
        val mView = ResourceUtils.inflate(R.layout.toast_custom_layout)
        val text = mView.findViewById(R.id.toast_text) as TextView
        val iv = mView.findViewById(R.id.toast_iv) as ImageView

        if (type == TYPE_DEFAULT) {
            iv.visibility = View.GONE
        } else if (type == TYPE_SUCCESS) {
            iv.setImageResource(R.drawable.collection_success)
        } else if (type == TYPE_WARNING) {
            iv.setImageResource(R.drawable.warning)
        }
        text.text = str

        // 连续按只以最后一次为起点显示2s,持续时间不再按次数累加
        if (mToast == null || str != message) {
            mToast = Toast(ResourceUtils.getContext())
            mToast!!.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            mToast!!.duration = Toast.LENGTH_SHORT
            mToast!!.view = mView
            message = str
        }
        mToast!!.show()

    }

    /**
     * 收藏、取消收藏
     *
     * @param context
     * @param str
     * @param type
     * @param iconRes
     */
    fun toast(context: Context, str: String, type: Int, iconRes: Int) {
        val mView = ResourceUtils.inflate(R.layout.toast_custom_layout)
        val text = mView.findViewById(R.id.toast_text) as TextView
        val iv = mView.findViewById(R.id.toast_iv) as ImageView

        iv.setImageResource(iconRes)
        text.text = str

        // 连续按只以最后一次为起点显示2s,持续时间不再按次数累加
        if (mToast == null || str != message) {
            mToast = Toast(ResourceUtils.getContext())
            mToast!!.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            mToast!!.duration = Toast.LENGTH_SHORT
            mToast!!.view = mView
            message = str
        }
        mToast!!.show()
    }
}
