package com.cesecsh.ics.ble.callback

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor


interface BleListener {
    interface OnLeScanListener {

        fun onLeScan(device: BluetoothDevice, rssi: Int, scanRecord: ByteArray)
    }

    interface OnConnectionStateChangeListener {

        fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int)
    }

    interface OnServicesDiscoveredListener {

        fun onServicesDiscovered(gatt: BluetoothGatt, status: Int)
    }

    interface OnDataAvailableListener {

        fun onCharacteristicRead(gatt: BluetoothGatt,
                                 characteristic: BluetoothGattCharacteristic, status: Int)

        fun onCharacteristicChanged(gatt: BluetoothGatt,
                                    characteristic: BluetoothGattCharacteristic)


        fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int)
    }

    interface OnReadRemoteRssiListener {

        fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int)
    }

    interface OnMtuChangedListener {

        fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int)
    }
}
