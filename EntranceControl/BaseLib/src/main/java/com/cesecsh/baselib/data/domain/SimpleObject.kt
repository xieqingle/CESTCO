package com.cesecsh.baselib.data.domain

import com.google.gson.annotations.SerializedName

/**
 * 作者：RockQ on 2018/6/13
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
data class SimpleObject(
        @SerializedName("message")
        val message: String,
        @SerializedName("code")
        val code: String)