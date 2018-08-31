package com.cesecsh.ics.ui.activity.ble

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.ics.R
import com.cesecsh.ics.ble.service.BleService
import com.cesecsh.ics.data.domain.BluetoothInfo
import com.cesecsh.ics.event.BleFindEvent
import kotlinx.android.synthetic.main.activity_search_ble.*
import kotlinx.android.synthetic.main.item_device.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast


class SearchBleActivity : AppCompatActivity() {
    private lateinit var mBleDeviceInfos: ArrayList<BluetoothInfo>
    private lateinit var mAdapter: DeviceAdapter
    private var isRefresh: Boolean = false
    private var mBleService: BleService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_ble)
        ScreenFitUtils.auto(this)
        mBleDeviceInfos = ArrayList()
        mAdapter = DeviceAdapter()
        EventBus.getDefault().register(this@SearchBleActivity)
        mRvDevice.adapter = mAdapter
        mRvDevice.layoutManager = LinearLayoutManager(this@SearchBleActivity)
        mSmartRefresh.setEnableLoadMore(false)
        mTitleBar.setTitle(getString(R.string.debug_mode))
        doBindService()
        mSmartRefresh.setOnRefreshListener {
            if (!mBleService!!.isScanning) {
                mBleService?.scanLeDevice(true)
                mAdapter.notifyDataSetChanged()
                isRefresh = true
                mSmartRefresh.finishRefresh(3000)
            } else {
                mSmartRefresh.finishRefresh()
                isRefresh = false
            }
        }
        mTitleBar.addLeftBackImageButton().setOnClickListener { finish() }
        mTitleBar.addRightTextButton(getString(R.string.search), R.id.rightViewId).setOnClickListener {
            if (isRefresh) {
                mSmartRefresh.finishRefresh()
                toast(getString(R.string.refreshing))
            } else {
                mSmartRefresh.autoRefresh()
            }
        }
//        mAuthorizationVisitor.setOnClickListener {
//            if (isRefresh) {
//                mSmartRefresh.finishRefresh()
//                toast(getString(R.string.refreshing))
//            } else {
//                mSmartRefresh.autoRefresh()
//            }
//        }

    }


    /**
     * 绑定服务
     */
    private fun doBindService() {
        val serviceIntent = Intent(this, BleService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private var mIsBind: Boolean = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mBleService = (service as BleService.LocalBinder).service
            mIsBind = true
            if (mBleService != null) {
                if (mBleService!!.initialize()) {
                    if (mBleService!!.enableBluetooth(true)) {
                        verifyIfRequestPermission()
                        mBleService!!.scanLeDevice(true)
                    }
                } else {
                    Toast.makeText(this@SearchBleActivity, getString(R.string.device_not_support_ble), Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mBleService = null
            mIsBind = false
        }
    }
    private val REQUEST_CODE_ACCESS_COARSE_LOCATION = 1
    private fun verifyIfRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(this, getString(R.string.search_ble_need_access_location), Toast.LENGTH_SHORT).show()
                } else {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_CODE_ACCESS_COARSE_LOCATION)
                }
            } else {
                mBleService?.scanLeDevice(true)
            }
        } else {
            mBleService?.scanLeDevice(true)
        }
    }

    /**
     * 解绑服务
     */
    private fun doUnBindService() {
        if (mIsBind) {
            unbindService(serviceConnection)
            mBleService = null
            mIsBind = false
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun bleFindEvent(event: BleFindEvent) {
        if (mBleDeviceInfos.isEmpty()) {
            mBleDeviceInfos.add(event.getBluetoothInfo())
        } else {
            mBleDeviceInfos.forEach { it ->
                if (!TextUtils.equals(it.bluetoothDevice.address, event.getBluetoothInfo().bluetoothDevice.address)) {
                    mBleDeviceInfos.add(event.getBluetoothInfo())
                } else {
                    mBleDeviceInfos.remove(it)
                    mBleDeviceInfos.add(event.getBluetoothInfo())
                }

            }

        }
//        mAdapter.notifyItemInserted(mBleDeviceInfos.size - 1)
        mAdapter.notifyDataSetChanged()
        if (isRefresh) isRefresh = false

    }

    override fun onDestroy() {
        super.onDestroy()
        doUnBindService()
        EventBus.getDefault().unregister(this@SearchBleActivity)
    }

    inner class DeviceAdapter : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@SearchBleActivity).inflate(R.layout.item_device, parent, false)
            ScreenFitUtils.auto(view)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mBleDeviceInfos.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val bluetoothInfo = mBleDeviceInfos.get(position)
            holder.itemView.mTvName.text = "设备名称：${bluetoothInfo.bluetoothDevice.name ?: "未知"}"
            holder.itemView.mTvMac.text = bluetoothInfo.bluetoothDevice.address
            holder.itemView.mTvRssi.text = bluetoothInfo.rssi.toString()
            holder.itemView.setOnClickListener { SendMessageActivity.launch(this@SearchBleActivity, bluetoothInfo) }


        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}

