package com.cesecsh.baselib.data

import com.google.gson.annotations.SerializedName

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */

data class NormalObject<T>(
        @SerializedName("message")
        val message: String,
        @SerializedName("code")
        val code: String,
        @SerializedName("obj")
        val obj: T? = null)