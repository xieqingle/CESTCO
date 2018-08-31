package com.cesecsh.ics.event

import com.cesecsh.ics.data.domain.BluetoothInfo

/**
 * 作者：RockQ on 2018/7/25
 * 邮箱：qingle6616@sina.com
 *
 * msg：ble 断开连接事件
 */
class BleStatusEvent {
    private var status: String? = ""
    private var address: String? = ""

    constructor(status: String?, address: String?) {
        this.status = status
        this.address = address
    }


    public fun setStatus(status: String) {
        this.status = status

    }

    public fun getAddress(): String? {
        return this.address
    }

    public fun setAddress(status: String) {
        this.address = status

    }

    public fun getStatus(): String? {
        return this.status
    }


}