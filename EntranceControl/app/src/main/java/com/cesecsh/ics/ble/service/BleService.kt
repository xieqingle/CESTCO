/*
 * Copyright 2015 Junk Chen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cesecsh.ics.ble.service

import android.Manifest
import android.app.Service
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.util.Log
import com.cesecsh.baselib.helper.LoggerHelper
import com.cesecsh.ics.ble.BleStatus
import com.cesecsh.ics.ble.GattAttributes
import com.cesecsh.ics.ble.JdyType
import com.cesecsh.ics.ble.JdyUtils
import com.cesecsh.ics.ble.callback.BleListener
import com.cesecsh.ics.data.domain.BluetoothInfo
import com.cesecsh.ics.event.BleFindEvent
import com.cesecsh.ics.event.BleStatusEvent
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by 上海中电
 * on 2016/9/1
 */
class BleService : Service(), BleListener {

    //Member fields
    private var mBluetoothManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private val mScanLeDeviceList = ArrayList<BluetoothDevice>()
    /**
     * If Ble is scaning return true, if not return false.
     *
     * @return ble whether scanning
     */
    var isScanning: Boolean = false
        private set
    var isConnect: Boolean = false
        private set
    private var mBluetoothDeviceAddress: String? = null
    private var mConnState = BleStatus.STATE_DISCONNECTED
    private val mScanPeriod: Long = 0

    private var mOnLeScanListener: BleListener.OnLeScanListener? = null
    private var mOnConnectionStateChangeListener: BleListener.OnConnectionStateChangeListener? = null
    private var mOnServicesDiscoveredListener: BleListener.OnServicesDiscoveredListener? = null
    private var mOnDataAvailableListener: BleListener.OnDataAvailableListener? = null
    private var mOnReadRemoteRssiListener: BleListener.OnReadRemoteRssiListener? = null
    private var mOnMtuChangedListener: BleListener.OnMtuChangedListener? = null

    private val mBinder = LocalBinder()

    companion object {
        //Debug
        private val TAG = BleService::class.java.name
        // Stop scanning after 10 seconds.
        private const val SCAN_PERIOD = (10 * 1000).toLong()
        private var instance: BleService? = null

        fun getInstance(): BleService {
            instance = BleService()
            return instance!!
        }

        var Service_uuid = "0000FFE0-0000-1000-8000-00805F9B34FB"
        var Characteristic_uuid_TX = "0000ffe1-0000-1000-8000-00805f9b34fb"
        var Characteristic_uuid_FUNCTION = "0000ffe2-0000-1000-8000-00805f9b34fb"
    }

    /**
     * Check for your device to support Ble
     *
     * @return true is support    false is not support
     */
    val isSupportBle: Boolean
        get() = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    /**
     * Return true if Bluetooth is currently enabled and ready for use.
     *
     * @return true if the local adapter is turned on
     */
    val isEnableBluetooth: Boolean get() = mBluetoothAdapter != null && mBluetoothAdapter!!.isEnabled

    val connectDevice: BluetoothDevice?
        get() = if (bluetoothGatt == null) null else bluetoothGatt!!.device

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after `BluetoothGatt#discoverServices()` completes successfully.
     *
     * @return A `List` of supported services.
     */
    val supportedGattServices: List<BluetoothGattService>?
        get() = if (bluetoothGatt == null) null else bluetoothGatt!!.services

    val connectDevices: List<BluetoothDevice>?
        get() = if (mBluetoothManager == null) null else mBluetoothManager!!.getConnectedDevices(BluetoothProfile.GATT)

    /**
     * Device scan callback.
     *
     *
     * Use mScanCallback if Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP,
     * else use mLeScanCallback.
     */
    private var mScanCallback: ScanCallback? = null
    private var mLeScanCallback: BluetoothAdapter.LeScanCallback? = null

    /**
     * Implements callback methods for GATT events that the app cares about.  For example,
     * connection change and services discovered.
     */
    private val mGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (mOnConnectionStateChangeListener != null) {
                mOnConnectionStateChangeListener!!.onConnectionStateChange(gatt, status, newState)
            }
            val intentAction: String
            val address = gatt.device.address
            LoggerHelper.d("bleState>>>>>>>>$newState >>> address >>>>$address")
            when (newState) {
                BluetoothProfile.STATE_DISCONNECTED -> {//断开连接
                    intentAction = BleStatus.ACTION_GATT_DISCONNECTED
                    isConnect = false
                    mConnState = BleStatus.STATE_DISCONNECTED
                    EventBus.getDefault().post(BleStatusEvent(intentAction, address))
//                    broadcastUpdate(intentAction, address)
                    close()
                }
                BluetoothProfile.STATE_CONNECTING -> {// 连接中
                    isConnect = false
                    intentAction = BleStatus.ACTION_GATT_CONNECTING
                    mConnState = BleStatus.STATE_CONNECTING
                    EventBus.getDefault().post(BleStatusEvent(intentAction, address))
//                    broadcastUpdate(intentAction, address)
                }
                BluetoothProfile.STATE_CONNECTED -> {// 连接成功
                    intentAction = BleStatus.ACTION_GATT_CONNECTED
                    isConnect = true
                    mConnState = BleStatus.STATE_CONNECTED
                    EventBus.getDefault().post(BleStatusEvent(intentAction, address))
                    gatt.discoverServices()
//                    broadcastUpdate(intentAction, address)
                    // Attempts to discover services after successful connection.
                }
                BluetoothProfile.STATE_DISCONNECTING -> {// 断开连接中
                    isConnect = false
                    intentAction = BleStatus.ACTION_GATT_DISCONNECTING
                    mConnState = BleStatus.STATE_DISCONNECTING
                    EventBus.getDefault().post(BleStatusEvent(intentAction, address))
//                    broadcastUpdate(intentAction, address)
                }
            }
        }

        // New services discovered
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (mOnServicesDiscoveredListener != null) {
                mOnServicesDiscoveredListener!!.onServicesDiscovered(gatt, status)
            }
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = bluetoothGatt!!.getService(UUID.fromString(Service_uuid))
                if (service != null) {
                    val gg: BluetoothGattCharacteristic = service.getCharacteristic(UUID.fromString(Characteristic_uuid_TX))
                    gatt.setCharacteristicNotification(gg, true)
                    val dsc = gg.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                    val bytes = byteArrayOf(0x01, 0x00)
                    dsc.value = bytes
                    val success = gatt.writeDescriptor(dsc)
                    if (success) {
                        for (descriptor in gg.descriptors) {
                            if (descriptor != null) {
                                if ((gg.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
                                } else if ((gg.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                                    descriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                                }
                                gatt.writeDescriptor(descriptor)
                            }
                        }
                    }

                }
            } else {
                Log.w(TAG, "onServicesDiscovered received: $status")
            }
        }

        // Result of a characteristic read operation
        override fun onCharacteristicRead(gatt: BluetoothGatt,
                                          characteristic: BluetoothGattCharacteristic, status: Int) {
            if (mOnDataAvailableListener != null) {
                mOnDataAvailableListener!!.onCharacteristicRead(gatt, characteristic, status)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            val address = gatt.device.address
            for (i in 0 until characteristic.value.size) {
                //TODO
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt,
                                             characteristic: BluetoothGattCharacteristic) {
            if (mOnDataAvailableListener != null) {
                mOnDataAvailableListener!!.onCharacteristicChanged(gatt, characteristic)
            }
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            if (mOnDataAvailableListener != null) {
                mOnDataAvailableListener!!.onDescriptorRead(gatt, descriptor, status)
            }
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
            if (mOnReadRemoteRssiListener != null) {
                mOnReadRemoteRssiListener!!.onReadRemoteRssi(gatt, rssi, status)
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            if (mOnMtuChangedListener != null) {
                mOnMtuChangedListener!!.onMtuChanged(gatt, mtu, status)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        close()
        instance = null
        return super.onUnbind(intent)
    }

    fun send(g: String): Int {
        var ic = 0
        val bytes = g.toByteArray()//getBytesByString( g );//  hex2byte(g.toString().getBytes());
        val length = bytes.size
        val data_len_20 = length / 20
        val data_len_0 = length % 20
        var i = 0
        if (data_len_20 > 0) {
            while (i < data_len_20) {
                val da = ByteArray(20)
                for (h in 0..19) {
                    da[h] = bytes[20 * i + h]
                    //Log.d("20*i+h"," len = " + 20*i+h );
                }
                val gg: BluetoothGattCharacteristic = bluetoothGatt!!.getService(UUID.fromString(Service_uuid)).getCharacteristic(UUID.fromString(Characteristic_uuid_TX))
                gg.value = da
                bluetoothGatt!!.writeCharacteristic(gg)
                ic += 20
                i++
            }

        }
        if (data_len_0 > 0) {
            val da = ByteArray(data_len_0)
            //               System.arraycopy(bytes, 20 * i, da, 0, data_len_0);
            for (h in 0 until data_len_0) {
                da[h] = bytes[20 * i + h]
            }
            val gg: BluetoothGattCharacteristic = bluetoothGatt!!.getService(UUID.fromString(Service_uuid)).getCharacteristic(UUID.fromString(Characteristic_uuid_TX))
            gg.value = da
            bluetoothGatt!!.writeCharacteristic(gg)
            ic += data_len_0
        }
        return ic
    }

    fun send(writeBytes: ByteArray) {
        //协议：以0A结尾
        val suffixBytes = byteArrayOf(0x0d,0x0A)
        val bytes = Arrays.copyOf(writeBytes, writeBytes.size + suffixBytes.size)//数组扩容
        System.arraycopy(suffixBytes, 0, bytes, bytes.size - suffixBytes.size, suffixBytes.size)
        val length = bytes.size
        val groupCount = length / 20
        val dataRemainLength = length % 20
        var i = 0
        if (groupCount > 0) {
            while (i < groupCount) {
                val da = ByteArray(20)
                System.arraycopy(bytes, 20 * i, da, 0, dataRemainLength)
                val gg: BluetoothGattCharacteristic = bluetoothGatt!!.getService(UUID.fromString(Service_uuid)).getCharacteristic(UUID.fromString(Characteristic_uuid_TX))
                gg.value = da
                bluetoothGatt!!.writeCharacteristic(gg)
                i++
            }

        }
        if (dataRemainLength > 0) {
            val da = ByteArray(dataRemainLength)
            System.arraycopy(bytes, 20 * i, da, 0, dataRemainLength)
            val service = bluetoothGatt!!.getService(UUID.fromString(Service_uuid))
            if (service != null) {
                val gg: BluetoothGattCharacteristic = service.getCharacteristic(UUID.fromString(Characteristic_uuid_TX))
                gg.value = da
                bluetoothGatt!!.writeCharacteristic(gg)
            }
        }
    }

    inner class LocalBinder : Binder() {
        val service: BleService
            get() = this@BleService
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return If return true, the initialization is successful.
     */
    fun initialize(): Boolean {
        //For API level 18 and above, get a reference to BluetoothAdapter through BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if (mBluetoothManager == null) {
                return false
            }
        }
        mBluetoothAdapter = mBluetoothManager!!.adapter
        if (mBluetoothAdapter == null) {
            return false
        }
        return true
    }

    /**
     * Turn on or off the local Bluetooth adapter;do not use without explicit
     * user action to turn on Bluetooth.
     *
     * @param enable if open ble
     * @return if ble is open return true
     */
    fun enableBluetooth(enable: Boolean): Boolean {
        return if (enable) {
            if (!mBluetoothAdapter!!.isEnabled) {
                mBluetoothAdapter!!.enable()
            } else true
        } else {
            if (mBluetoothAdapter!!.isEnabled) {
                mBluetoothAdapter!!.disable()
            } else false
        }
    }

    /**
     * Scan Ble device.
     *
     * @param enable     If true, start scan ble device.False stop scan.
     * @param scanPeriod scan ble period time
     */
    fun scanLeDevice(enable: Boolean, scanPeriod: Long) {
        if (!isEnableBluetooth) {
            return
        }
        if (enable) {
            if (isScanning) return
            if (mBluetoothAdapter == null) return
            Handler().postDelayed({
                isScanning = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val bluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner
                    bluetoothLeScanner?.stopScan(mScanCallback)
                } else {
                    mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
                }
//                broadcastUpdate(BleStatus.ACTION_SCAN_FINISHED)
                mScanLeDeviceList.clear()
            }, scanPeriod)
            mScanLeDeviceList.clear()
            isScanning = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter!!.bluetoothLeScanner.startScan(mScanCallback)
            } else {
                mBluetoothAdapter!!.startLeScan(mLeScanCallback)
            }
        } else {
            isScanning = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothAdapter!!.bluetoothLeScanner.stopScan(mScanCallback)
            } else {
                mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
            }
            mScanLeDeviceList.clear()
        }
    }

    /**
     * Scan Ble device.
     *
     * @param enable If true, start scan ble device.False stop scan.
     */
    fun scanLeDevice(enable: Boolean) {
        this.scanLeDevice(enable, SCAN_PERIOD)
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the BluetoothGattCallback#onConnectionStateChange.
     */
    fun connect(address: String?): Boolean {
        if (isScanning) scanLeDevice(false)
        close()
        if (mBluetoothAdapter == null || address == null) {
            return false
        }
        //Previously connected device.  Try to reconnect.
        if (bluetoothGatt != null && mBluetoothDeviceAddress != null
                && address == mBluetoothDeviceAddress) {
            if (bluetoothGatt!!.connect()) {
                mConnState = BleStatus.STATE_CONNECTING
                return true
            } else {
                return false
            }
        }
        val device = mBluetoothAdapter!!.getRemoteDevice(address) ?: return false
        //We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        bluetoothGatt = device.connectGatt(this, false, mGattCallback)
        mBluetoothDeviceAddress = address
        mConnState = BleStatus.STATE_CONNECTING
        return true
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the BluetoothGattCallback#onConnectionStateChange.
     */
    fun connect(device: BluetoothDevice): Boolean {
        return connect(device.address)
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the BluetoothGattCallback#onConnectionStateChange.
     */
    fun disconnect() {
        if (mBluetoothAdapter == null || bluetoothGatt == null) {
            return
        }
        bluetoothGatt!!.disconnect()
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    fun close() {
        isConnect = false
        if (bluetoothGatt == null) {
            return
        }
        bluetoothGatt!!.close()
        bluetoothGatt = null
    }

    /**
     * Request a read on a given `BluetoothGattCharacteristic`. The read result is reported
     * asynchronously through the BluetoothGattCallback#onCharacteristicRead.
     *
     * @param characteristic The characteristic to read from.
     */
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        if (mBluetoothAdapter == null || bluetoothGatt == null) {
            return
        }
        bluetoothGatt!!.readCharacteristic(characteristic)

    }

    /**
     * Request a read on a given `BluetoothGattCharacteristic`, specific service UUID
     * and characteristic UUID. The read result is reported asynchronously through the
     * `BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt,
     * android.bluetooth.BluetoothGattCharacteristic, int)` callback.
     *
     * @param serviceUUID        remote device service uuid
     * @param characteristicUUID remote device characteristic uuid
     */
    fun readCharacteristic(serviceUUID: String, characteristicUUID: String) {
        if (bluetoothGatt != null) {
            val service = bluetoothGatt!!.getService(UUID.fromString(serviceUUID))
            val characteristic = service.getCharacteristic(UUID.fromString(characteristicUUID))
            bluetoothGatt!!.readCharacteristic(characteristic)
        }
    }

    fun readCharacteristic(address: String, serviceUUID: String, characteristicUUID: String) {
        if (bluetoothGatt != null) {
            val service = bluetoothGatt!!.getService(UUID.fromString(serviceUUID))
            val characteristic = service.getCharacteristic(UUID.fromString(characteristicUUID))
            bluetoothGatt!!.readCharacteristic(characteristic)
        }
    }

    /**
     * Write data to characteristic, and send to remote bluetooth le device.
     *
     * @param serviceUUID        remote device service uuid
     * @param characteristicUUID remote device characteristic uuid
     * @param value              Send to remote ble device data.
     */
    fun writeCharacteristic(serviceUUID: String, characteristicUUID: String, value: String) {
        if (bluetoothGatt != null) {
            val service = bluetoothGatt!!.getService(UUID.fromString(serviceUUID))
            val characteristic = service.getCharacteristic(UUID.fromString(characteristicUUID))
            characteristic.setValue(value)
            bluetoothGatt!!.writeCharacteristic(characteristic)
        }
    }

    fun writeCharacteristic(serviceUUID: String, characteristicUUID: String, value: ByteArray): Boolean {
        if (bluetoothGatt != null) {
            val service = bluetoothGatt!!.getService(UUID.fromString(serviceUUID))
            val characteristic = service.getCharacteristic(UUID.fromString(characteristicUUID))
            characteristic.value = value
            return bluetoothGatt!!.writeCharacteristic(characteristic)
        }
        return false
    }

    /**
     * Write value to characteristic, and send to remote bluetooth le device.
     *
     * @param characteristic remote device characteristic
     * @param value          New value for this characteristic
     * @return if write success return true
     */
    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, value: String): Boolean {
        return writeCharacteristic(characteristic, value.toByteArray())
    }

    /**
     * Writes a given characteristic and its values to the associated remote device.
     *
     * @param characteristic remote device characteristic
     * @param value          New value for this characteristic
     * @return if write success return true
     */
    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, value: ByteArray): Boolean {
        if (bluetoothGatt != null) {
            characteristic.value = value
            return bluetoothGatt!!.writeCharacteristic(characteristic)
        }
        return false
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic,
                                      enabled: Boolean) {
        if (mBluetoothAdapter == null || bluetoothGatt == null) {
            return
        }
        bluetoothGatt!!.setCharacteristicNotification(characteristic, enabled)

        val descriptor = characteristic.getDescriptor(
                UUID.fromString(GattAttributes.DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION))
        descriptor.value = if (enabled)
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        else
            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        bluetoothGatt!!.writeDescriptor(descriptor)
    }

    fun setCharacteristicNotification(serviceUUID: String, characteristicUUID: String,
                                      enabled: Boolean) {
        if (mBluetoothAdapter == null || bluetoothGatt == null) {
            return
        }
        val service = bluetoothGatt!!.getService(UUID.fromString(serviceUUID))
        val characteristic = service.getCharacteristic(UUID.fromString(characteristicUUID))

        bluetoothGatt!!.setCharacteristicNotification(characteristic, enabled)

        val descriptor = characteristic.getDescriptor(
                UUID.fromString(GattAttributes.DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION))
        descriptor.value = if (enabled)
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        else
            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        bluetoothGatt!!.writeDescriptor(descriptor)
    }

    /**
     * Reads the value for a given descriptor from the associated remote device.
     *
     *
     *
     * Once the read operation has been completed, the
     * [BluetoothGattCallback.onDescriptorRead] callback is
     * triggered, signaling the result of the operation.
     *
     *
     *
     * Requires [android.Manifest.permission.BLUETOOTH] permission.
     *
     * @param descriptor Descriptor value to read from the remote device
     * @return true, if the read operation was initiated successfully
     */
    fun readDescriptor(descriptor: BluetoothGattDescriptor): Boolean {
        if (bluetoothGatt == null) {
            return false
        }
        return bluetoothGatt!!.readDescriptor(descriptor)
    }

    /**
     * Reads the value for a given descriptor from the associated remote device.
     *
     * @param serviceUUID        remote device service uuid
     * @param characteristicUUID remote device characteristic uuid
     * @param descriptorUUID     remote device descriptor uuid
     * @return true, if the read operation was initiated successfully
     */
    fun readDescriptor(serviceUUID: String, characteristicUUID: String,
                       descriptorUUID: String): Boolean {
        if (bluetoothGatt == null) {
            return false
        }
        //        try {
        val service = bluetoothGatt!!.getService(UUID.fromString(serviceUUID))
        val characteristic = service.getCharacteristic(UUID.fromString(characteristicUUID))
        val descriptor = characteristic.getDescriptor(UUID.fromString(descriptorUUID))
        return bluetoothGatt!!.readDescriptor(descriptor)
        //        } catch (Exception e) {
        //            return false;
        //        }
    }

    /**
     * Read the RSSI for a connected remote device.
     *
     *
     *
     * The [BluetoothGattCallback.onReadRemoteRssi] callback will be
     * invoked when the RSSI value has been read.
     *
     *
     *
     * Requires [android.Manifest.permission.BLUETOOTH] permission.
     *
     * @return true, if the RSSI value has been requested successfully
     */
    fun readRemoteRssi(): Boolean {
        return if (bluetoothGatt == null) false else bluetoothGatt!!.readRemoteRssi()
    }

    /**
     * Request an MTU size used for a given connection.
     *
     *
     *
     * When performing a write request operation (write without response),
     * the data sent is truncated to the MTU size. This function may be used
     * to request a larger MTU size to be able to send more data at once.
     *
     *
     *
     * A [BluetoothGattCallback.onMtuChanged] callback will indicate
     * whether this operation was successful.
     *
     *
     *
     * Requires [Manifest.permission.BLUETOOTH] permission.
     *
     * @param mtu mtu
     * @return true, if the new MTU value has been requested successfully
     */
    fun requestMtu(mtu: Int): Boolean {
        if (bluetoothGatt == null) return false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Android API level >= 21
            bluetoothGatt!!.requestMtu(mtu)
        } else {
            false
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mScanCallback = object : ScanCallback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    if (mScanLeDeviceList.contains(result.device)) return
                    mScanLeDeviceList.add(result.device)
                    if (mOnLeScanListener != null) {
                        mOnLeScanListener!!.onLeScan(result.device, result.rssi, result.scanRecord!!.bytes)
                    }
                    val bluetoothInfo = BluetoothInfo(result.device, result.rssi)
                    if (JdyUtils.spiltJdy(result.scanRecord!!.bytes) == JdyType.JDY)
                        EventBus.getDefault().post(BleFindEvent(bluetoothInfo))
                }
            }
        } else {
            mLeScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
                if (device == null || mScanLeDeviceList.contains(device)) return@LeScanCallback
                mScanLeDeviceList.add(device)
                if (mOnLeScanListener != null) {
                    mOnLeScanListener!!.onLeScan(device, rssi, scanRecord)
                }
                val bluetoothInfo = BluetoothInfo(device, rssi)

                if (JdyUtils.spiltJdy(scanRecord) == JdyType.JDY)
                    EventBus.getDefault().post(BleFindEvent(bluetoothInfo))
            }
        }
    }


    fun setOnLeScanListener(onLeScanListener: BleListener.OnLeScanListener) {
        mOnLeScanListener = onLeScanListener
    }

    fun setOnConnectListener(l: BleListener.OnConnectionStateChangeListener) {
        mOnConnectionStateChangeListener = l
    }

    fun setOnServicesDiscoveredListener(l: BleListener.OnServicesDiscoveredListener) {
        mOnServicesDiscoveredListener = l
    }

    fun setOnDataAvailableListener(l: BleListener.OnDataAvailableListener) {
        mOnDataAvailableListener = l
    }

    fun setOnReadRemoteRssiListener(l: BleListener.OnReadRemoteRssiListener) {
        mOnReadRemoteRssiListener = l
    }

    fun setOnMtuChangedListener(l: BleListener.OnMtuChangedListener) {
        mOnMtuChangedListener = l
    }


}