package com.cesecsh.usercenter.data.protocol

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */

@Parcelize
data class User(
        val userId: String,
        val name: String,
        val token: String
) : Parcelable

