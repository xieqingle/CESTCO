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
data class Site(
        val id: Int,
        val name: String,
        val domain: String,
        val recognised: Boolean
) : Parcelable