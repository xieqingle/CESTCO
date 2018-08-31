package com.cesecsh.ics.event

import com.cesecsh.ics.data.domain.BluetoothInfo

/**
 * 作者：RockQ on 2018/7/25
 * 邮箱：qingle6616@sina.com
 *
 * msg：ble  扫描到事件
 */
class BleFindEvent(private var bluetoothInfo: BluetoothInfo) {


    public fun setBluetoothInfo(bluetoothInfo: BluetoothInfo) {
        this.bluetoothInfo = bluetoothInfo
    }

    public fun getBluetoothInfo(): BluetoothInfo {
        return this.bluetoothInfo
    }

}