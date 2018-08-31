package com.cesecsh.ics.ui.activity.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.ics.R
import com.cesecsh.ics.ble.BleStatus
import com.cesecsh.ics.ble.callback.BleListener
import com.cesecsh.ics.ble.service.BleService
import com.cesecsh.ics.data.domain.BluetoothInfo
import com.cesecsh.ics.event.BleStatusEvent
import kotlinx.android.synthetic.main.activity_send_message.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class SendMessageActivity : AppCompatActivity() {
    companion object {
        public const val BLUE_INFO = "blue_info"
        fun launch(context: Context, bluetoothInfo: BluetoothInfo) {
            val intent = Intent(context, SendMessageActivity::class.java)
            intent.putExtra(BLUE_INFO, bluetoothInfo)
            context.startActivity(intent)

        }
    }

    private var mBluetoothInfo: BluetoothInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)
        ScreenFitUtils.auto(this)
        mBluetoothInfo = intent.getParcelableExtra<BluetoothInfo>(BLUE_INFO)
        if (mBluetoothInfo == null) {
            finish()
            return
        }
        mTitleBar.setTitle(mBluetoothInfo!!.bluetoothDevice.name ?: "未知设备")
        mTitleBar.addLeftBackImageButton().setOnClickListener { finish() }
        mBtnSend.setOnClickListener {
            if (mBleService != null && status == BleStatus.STATE_CONNECTED) {
                val toByteArray = "1217592446".toByteArray()
                mBleService!!.send(toByteArray)
            } else toast("设备未连接")
        }
        mTvReceiveMessage.movementMethod = ScrollingMovementMethod.getInstance()
        EventBus.getDefault().register(this@SendMessageActivity)
        doBindService()

    }

    private fun initServiceListener() {
        mBleService!!.setOnDataAvailableListener(object : BleListener.OnDataAvailableListener {
            @SuppressLint("SetTextI18n")
            override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
                runOnUiThread {
                    mTvReceiveMessage.text = "${mTvReceiveMessage.text}\n${SimpleDateFormat("hh:mm:ss", Locale.CHINA).format(System.currentTimeMillis())} 收到消息：${Arrays.toString(characteristic.value)}"
                }

            }

            override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
//                toast("收到消息：" + characteristic.value)
//                mTvReceiveMessage.text = Arrays.toString(characteristic.value)

            }

            override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {


            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        isRunning = true
    }

    /**
     * 绑定服务
     */
    private fun doBindService() {
        val serviceIntent = Intent(this, BleService::class.java)
        bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private var mIsBind: Boolean = false
    /**
     * 解绑服务
     */
    private fun doUnBindService() {
        if (mIsBind) {
            unbindService(mServiceConnection)
            mBleService = null
            mIsBind = false
        }
    }

    private var status = BleStatus.STATE_DISCONNECTED

    private var isConnecting = false
    /**
     * 在当前界面可见的请看下，如果设备断开，重新连接
     */
    private var isRunning = true

    /**
     * 在当前页面不可见时断开连接
     * 以便释放蓝牙占用，给予其他用户使用
     */
    override fun onPause() {
        super.onPause()
        isRunning = false
        if (isConnecting) {
            mBleService?.disconnect()
            isConnecting = false
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun bleStatusEvent(event: BleStatusEvent?) {
        if (event != null) {
            when (event.getStatus()) {
                BleStatus.ACTION_GATT_CONNECTING -> {
                    status = BleStatus.STATE_CONNECTING
                    isConnecting = false
                    mTvStatus.text = "状态：连接中……"
                }
                BleStatus.ACTION_GATT_CONNECTED -> {
                    status = BleStatus.STATE_CONNECTED
                    mTvStatus.text = "状态：已连接"
                    isConnecting = true
                    initServiceListener()
                }
                BleStatus.ACTION_GATT_DISCONNECTING -> {
                    status = BleStatus.STATE_DISCONNECTING
                    isConnecting = false
                    mTvStatus.text = "状态：断开连接中……"
                }
                BleStatus.ACTION_GATT_DISCONNECTED -> {
                    status = BleStatus.STATE_DISCONNECTED
                    isConnecting = false
                    mTvStatus.text = "状态：已断开"
                    if (isRunning) {
                        mBleService!!.connect(mBluetoothInfo!!.bluetoothDevice)
                        mTvStatus.text = "状态：尝试重连中……"
                    }
                }
            }
        }
    }


    private var mBleService: BleService? = null
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            mBleService = (service as BleService.LocalBinder).service
            if (!mBleService!!.initialize()) {
                finish()
            }
            mIsBind = true
            mBleService!!.connect(mBluetoothInfo!!.bluetoothDevice)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mBleService = null
            mIsBind = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        doUnBindService()
        EventBus.getDefault().unregister(this@SendMessageActivity)
    }
}
