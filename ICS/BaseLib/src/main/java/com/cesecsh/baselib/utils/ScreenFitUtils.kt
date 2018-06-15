package com.cesecsh.baselib.utils

import android.app.Activity
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * 类名 ScreenFitUtils
 * 创建者：RockQ
 * 实现的主要功能： 适用所有的屏幕适配，利用px设置后，获取相对应的值，再根据百分比进行转换
 *
 *
 * 注：所有的布局的参数都是px单位
 * 创建日期：  2017/5/9
 */

object ScreenFitUtils {
    /**
     * 自动匹配activity
     *
     * @param activity
     */
    fun auto(activity: Activity?) {
        if (activity == null) {
            return
        }
//        val view = activity.getWindow().getDecorView();
        val view = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        auto(view)
    }

    /**
     * 自动匹配 view
     *
     * @param view
     */
    fun auto(view: View?) {
        if (view == null) {
            return
        }
        autoSize(view)
        autoPadding(view)
        autoMargin(view)
        autoTextSize(view)
        if (view is ViewGroup) {
            auto((view as ViewGroup?)!!)
        }
    }

    /**
     * 自动匹配viewGroup
     * 遍历viewGroup并且对其子view进行适配
     *
     * @param viewGroup
     */
    fun auto(viewGroup: ViewGroup) {
        val count = viewGroup.childCount
        for (i in 0 until count) {
            val view = viewGroup.getChildAt(i)
            if (view != null) {
                auto(view)
            }
        }
    }

    /**
     * 适配view的宽高
     *
     * @param view
     */
    fun autoSize(view: View) {
        val lp = view.layoutParams ?: return
        if (lp.width > 0) {
            lp.width = px2percentPx(lp.width)
        }
        if (lp.height > 0) {
            lp.height = px2percentPx(lp.height)
        }
    }

    /**
     * 适配view的padding
     *
     * @param view
     */
    fun autoPadding(view: View) {
        var l = view.paddingLeft
        var t = view.paddingTop
        var r = view.paddingRight
        var b = view.paddingBottom

        l = px2percentPx(l)
        t = px2percentPx(t)
        r = px2percentPx(r)
        b = px2percentPx(b)

        view.setPadding(l, t, r, b)
    }

    /**
     * 适配view的margin
     *
     * @param view
     */
    fun autoMargin(view: View) {
        if (view.layoutParams !is ViewGroup.MarginLayoutParams) {
            return
        }
        val lp = view.layoutParams as ViewGroup.MarginLayoutParams ?: return
        lp.leftMargin = px2percentPx(lp.leftMargin)
        lp.topMargin = px2percentPx(lp.topMargin)
        lp.rightMargin = px2percentPx(lp.rightMargin)
        lp.bottomMargin = px2percentPx(lp.bottomMargin)
    }

    /**
     * 适配字体大小
     *
     * @param view
     */
    fun autoTextSize(view: View) {
        if (view !is TextView) {
            return
        }
        val designPixels = view.textSize.toDouble()
        //        double  display =
        // 设置TextView不包含额外的顶部和底部填充，完全按照设计来
        view.includeFontPadding = false
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, px2percentSp(designPixels.toInt()).toFloat())

    }


    /**
     * 将获取的px根据屏幕的百分比进行适配
     *
     * @param displayValue
     * @return
     */
    fun px2percentPx(displayValue: Int): Int {
        return DensityUtils.dp2px(ResourceUtils.getContext(), displayValue.toFloat())
    }

    /**
     * 将获取的px根据屏幕的百分比进行适配
     *
     * @param displayValue
     * @return
     */
    fun px2percentSp(displayValue: Int): Int {
        return DensityUtils.sp2px(ResourceUtils.getContext(), displayValue.toFloat())
    }
}
