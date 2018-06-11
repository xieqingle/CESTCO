package com.cesecsh.baselib.utils

import android.view.View
import com.cesecsh.baselib.BaseApplication
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.support.annotation.*


/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：资源相关的类
 */
object ResourceUtils {
    fun getContext() = BaseApplication.context
    /**
     * 获取视图
     */
    fun inflate(@LayoutRes layoutRes: Int) = View.inflate(getContext(), layoutRes, null)!!

    /**
     * 获取整形
     */
    fun getInteger(@IntegerRes id: Int) = getContext().resources.getInteger(id)

    /**
     *  获取字符串
     *  @param id
     */
    fun getString(@StringRes id: Int): String {
        return getContext().resources.getString(id)
    }

    /**
     *  获取字符串数组
     *  @param id
     */
    fun getStringArray(@ArrayRes id: Int): Array<String> {
        return getContext().resources.getStringArray(id)
    }

    /**
     *  获取图片
     *  @param id
     */
    fun getDrawable(@DrawableRes id: Int): Drawable {
        return getContext().resources.getDrawable(id)
    }

    /**
     *   获取颜色
     *  @param id
     */
    fun getColor(@ColorRes id: Int): Int {
        return getContext().resources.getColor(id)
    }

    /**
     *   根据id获取颜色的状态选择器
     *  @param id
     */
    fun getColorStateList(@ColorRes id: Int): ColorStateList {
        return getContext().resources.getColorStateList(id)
    }

    /**
     *   获取尺寸
     *  @param id
     */
    fun getDimen(@DimenRes id: Int): Int {
        return getContext().resources.getDimensionPixelSize(id)// 返回具体像素值
    }


}