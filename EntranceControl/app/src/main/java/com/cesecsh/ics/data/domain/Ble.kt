package com.cesecsh.ics.data.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ble(
        var bleId: String? = null,
        var bleName: String? = null,
        var bluetoothMac: String? = null,
        var isBleShow: Boolean = true,
        var isSelected: Boolean = true
) : Parcelable