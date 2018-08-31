package com.cesecsh.baselib.widget.view.alpha

import android.content.Context
import android.support.v7.widget.AppCompatImageButton
import android.util.AttributeSet
import android.widget.Toast
import com.cesecsh.baselib.widget.viewHelper.AlphaViewHelper

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
open class AlphaImageButton constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageButton(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        getAlphaViewHelper().onPressedChanged(this, pressed)
    }

    private fun getAlphaViewHelper(): AlphaViewHelper {
        return AlphaViewHelper(this)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        getAlphaViewHelper().onEnabledChanged(this, enabled)
    }

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    fun setChangeAlphaWhenPress(changeAlphaWhenPress: Boolean) {
        getAlphaViewHelper().setChangeAlphaWhenPress(changeAlphaWhenPress)
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    fun setChangeAlphaWhenDisable(changeAlphaWhenDisable: Boolean) {
        getAlphaViewHelper().setChangeAlphaWhenDisable(changeAlphaWhenDisable)
    }


}