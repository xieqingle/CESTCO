package com.cesecsh.baselib.data.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
@Parcelize
data class NormalObject<T : Parcelable>(
        @SerializedName("msg")
        val msg: String,
        @SerializedName("code")
        val code: String,
        @SerializedName("data")
        val obj: T? = null) : Parcelable