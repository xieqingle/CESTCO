package com.cesecsh.ics.data.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者：RockQ on 2018/7/13
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
@Parcelize
data class EntranceControl(var QRCodeUrl: String,
                           var bles: ArrayList<Ble> = ArrayList(),
                           var bleNo: String,
                           var canBluetooth: Boolean = false) : Parcelable