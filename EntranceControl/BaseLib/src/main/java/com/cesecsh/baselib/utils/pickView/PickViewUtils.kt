package com.cesecsh.baselib.utils.pickView

import android.app.Activity
import android.content.Context
import com.bigkoo.pickerview.OptionsPickerView
import com.bigkoo.pickerview.TimePickerView
import com.cesecsh.baselib.helper.KeyboardHelper
import java.util.*

/**
 * 作者：RockQ on 2018/7/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
object PickViewUtils {
    fun getDate(context: Activity, callBack: PickViewDateCallBack) {
        KeyboardHelper.hideSoftInput(context)
        pickViewDate(context, Calendar.getInstance(), callBack)
    }

    fun getDate(context: Activity, calendar: Calendar, callBack: PickViewDateCallBack) {
        KeyboardHelper.hideSoftInput(context)
        pickViewDate(context, calendar, callBack)
    }

    fun pickViewDate(context: Activity, calendar: Calendar, callBack: PickViewDateCallBack) {
        TimePickerView.Builder(context, TimePickerView.OnTimeSelectListener { date, _ -> callBack.onCallBack(date) })
                .setDate(calendar)
                .isCyclic(false)
                .setOutSideCancelable(true)
                .build()
                .show()
    }

    /**
     * 直接显示 数据 没有handler做选择后的消息通知
     *
     * @param context
     * @param list
     * @param view
     * @return
     */
    fun getItem(context: Context, list: List<String>, pos: Int = 0, callBack: PickViewItemCallBack) {
        val optionsPickerView = OptionsPickerView.Builder(context,
                OptionsPickerView.OnOptionsSelectListener { options1, options2, options3, v ->
                    callBack.onCallBack(options1, options2, options3)
                }).setCyclic(false, false, false)
                .setSelectOptions(0)
                .setOutSideCancelable(true)
                .build()
        optionsPickerView.setPicker(list as ArrayList<*>)
        optionsPickerView.setSelectOptions(pos)
        optionsPickerView.show()
    }

}