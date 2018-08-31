package com.cesecsh.ics.data.domain

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者：RockQ on 2018/7/23
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
@Parcelize
class BluetoothInfo constructor(var bluetoothDevice: BluetoothDevice, var rssi: Int) : Parcelable