package com.cesecsh.usercenter.data.protocol

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者：RockQ on 2018/6/13
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
@Parcelize
data class Agreement(var id: Long, var agreementUrl: String) : Parcelable