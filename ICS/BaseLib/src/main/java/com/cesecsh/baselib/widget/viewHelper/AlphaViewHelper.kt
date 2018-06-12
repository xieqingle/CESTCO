package com.cesecsh.baselib.widget.viewHelper

import android.support.annotation.NonNull
import android.view.View
import com.cesecsh.baselib.R
import java.lang.ref.WeakReference


/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：按钮按下透明或者是否可点击透明
 */
class AlphaViewHelper constructor(target: View, val mPressedAlpha: Float = 0.5F, val mDisabledAlpha: Float = 0.5f) {
    private var mTarget: WeakReference<View>? = null

    /**
     * 设置是否要在 press 时改变透明度
     */
    private var mChangeAlphaWhenPress = true

    /**
     * 设置是否要在 disabled 时改变透明度
     */
    private var mChangeAlphaWhenDisable = true

    private val mNormalAlpha = 1f

    init {
        mTarget = WeakReference(target)
    }


    /**
     * @param current the view to be handled, maybe not equal to target view
     * @param pressed
     */
    fun onPressedChanged(current: View, pressed: Boolean) {
        val target = mTarget!!.get() ?: return
        if (current.isEnabled()) {
            target.alpha = if (mChangeAlphaWhenPress && pressed && current.isClickable) mPressedAlpha else mNormalAlpha
        } else {
            if (mChangeAlphaWhenDisable) {
                target.alpha = mDisabledAlpha
            }
        }
    }

    /**
     * @param current the view to be handled, maybe not  equal to target view
     * @param enabled
     */
    fun onEnabledChanged(current: View, enabled: Boolean) {
        val target = mTarget!!.get() ?: return
        val alphaForIsEnable: Float
        if (mChangeAlphaWhenDisable) {
            alphaForIsEnable = if (enabled) mNormalAlpha else mDisabledAlpha
        } else {
            alphaForIsEnable = mNormalAlpha
        }
        if (current !== target && target.isEnabled !== enabled) {
            target.setEnabled(enabled)
        }
        target.setAlpha(alphaForIsEnable)
    }

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    fun setChangeAlphaWhenPress(changeAlphaWhenPress: Boolean) {
        mChangeAlphaWhenPress = changeAlphaWhenPress
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    fun setChangeAlphaWhenDisable(changeAlphaWhenDisable: Boolean) {
        mChangeAlphaWhenDisable = changeAlphaWhenDisable
        val target = mTarget!!.get()
        if (target != null) {
            onEnabledChanged(target, target!!.isEnabled())
        }

    }
}