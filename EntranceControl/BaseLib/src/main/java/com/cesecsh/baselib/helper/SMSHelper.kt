package com.cesecsh.baselib.helper

import android.support.v4.content.ContextCompat.startActivity
import android.R.attr.phoneNumber
import android.content.Context
import android.content.Intent
import android.net.Uri


/**
 * 作者：RockQ on 2018/7/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
object SMSHelper {
    fun sendSmsTo(context: Context, msg: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:")
        intent.putExtra("sms_body", msg)
        context.startActivity(intent)
    }

    fun sendSmsTo(context: Context, phone: String, msg: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:$phone")
        intent.putExtra("sms_body", msg)
        context.startActivity(intent)
    }

}