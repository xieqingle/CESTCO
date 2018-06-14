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
        val id: Int,
        val username: String,
        val nickname: String,
        val photo: String,
        val recognised: Boolean,
        val token: String,
        val hasPayPassword: Boolean,
        val sites: List<Site>,
        val showWallet: Boolean
) : Parcelable

