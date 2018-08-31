package com.cesecsh.baselib.utils

import android.os.CountDownTimer
import android.widget.Button
import com.cesecsh.baselib.R
import java.util.*

/**
 * 作者：RockQ on 2018/6/13
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class CountDownTimeHelper(var btnTime: Button, millisInFuture: Int, countDownInterval: Int) :
        CountDownTimer(millisInFuture * 1000L, countDownInterval * 1000L) {
    override fun onFinish() {
        btnTime.setText(R.string.get_verification_again)
        btnTime.isEnabled = true
    }

    override fun onTick(millisUntilFinished: Long) {
        btnTime.isEnabled = false
        btnTime.text = String.format(Locale.CHINA, "%s S", millisUntilFinished / 1000)
    }


}