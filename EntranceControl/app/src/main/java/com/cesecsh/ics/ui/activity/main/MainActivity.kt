package com.cesecsh.ics.ui.activity.main

import android.Manifest
import android.app.Activity
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.cesecsh.baselib.common.ARouterPath
import com.cesecsh.baselib.common.ActivityManager
import com.cesecsh.baselib.common.DataResource
import com.cesecsh.baselib.data.BaseUrls
import com.cesecsh.baselib.helper.LoggerHelper
import com.cesecsh.baselib.ui.base.BaseMvpActivity
import com.cesecsh.baselib.utils.ResourceUtils
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.baselib.widget.CusPageChangeListener
import com.cesecsh.baselib.widget.callBack.OnItemClickListener
import com.cesecsh.baselib.widget.statusLayout.OnRetryClickListener
import com.cesecsh.ics.R
import com.cesecsh.ics.ble.BleStatus
import com.cesecsh.ics.ble.service.BleService
import com.cesecsh.ics.data.domain.Ble
import com.cesecsh.ics.data.domain.BluetoothInfo
import com.cesecsh.ics.data.domain.EntranceControl
import com.cesecsh.ics.event.BleFindEvent
import com.cesecsh.ics.event.BleSendCardIdEvent
import com.cesecsh.ics.event.BleStatusEvent
import com.cesecsh.ics.event.UpdateQrCodeUrlEvent
import com.cesecsh.ics.presenter.MainPresenter
import com.cesecsh.ics.presenter.view.MainView
import com.cesecsh.ics.ui.activity.ble.SearchBleActivity
import com.cesecsh.ics.ui.activity.entrance.AuthorizationVisitorActivity
import com.cesecsh.ics.ui.activity.main.adapter.DoorAdapter
import com.cesecsh.ics.ui.adapter.BaseViewPagerAdapter
import com.cesecsh.ics.widget.EntranceView
import com.kotlin.base.utils.AppPrefsUtils
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * msg:主界面
 * @author RockQ
 */
@Route(path = ARouterPath.PATH_MAIN)
class MainActivity : BaseMvpActivity<MainPresenter>(), MainView, View.OnClickListener {
    override fun updateQrUrl(QRCodeUrl: String) {
        this.mEntranceControl!!.QRCodeUrl = QRCodeUrl
        changeDrawable(mBleDoors.get(mCurrentEntrancePos).isBleShow)
    }

    /**
     * 显示门禁刷新
     */
    override fun showDoors(mEntranceControl: EntranceControl) {
        setData(mEntranceControl)
    }

    /**
     * 显示为空的时候的布局，并且关闭菜单
     */
    override fun showEmptyContent() {
        mSflRoot.showEmpty()
//        mDlMenus.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    /**
     * 显示网络为空的布局，并且关闭菜单
     */
    override fun showNetWorkError() {
        mSflRoot.showNetWorkError()
//        mDlMenus.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    /**
     * 显示内容，并且允许打开菜单
     */
    override fun showContent() {
        mSflRoot.showContent()
//        mDlMenus.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    }

    /**
     * 显示加载中布局，并且关闭菜单
     */
    override fun showLoading() {
        mSflRoot.showLoading()
//        mDlMenus.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    /**
     * 显示错误，，并且关闭菜单
     */
    override fun showError() {
        mSflRoot.showError()
//        mDlMenus.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    /**
     * 错误信息，调用加载失败布局
     */
    override fun onError(text: String) {
        super.onError(text)
        showError()
    }

    override fun initPresenterAndView() {
        mPresenter = MainPresenter()
        mPresenter.mView = this
    }

    /**
     * 门禁名称
     */
    private lateinit var mBleDoors: ArrayList<Ble>
    /**
     * 门禁 View集合
     */
    private lateinit var mEntranceControlViews: ArrayList<EntranceView>
    /**
     * 当前账户门禁
     */
    private var mEntranceControl: EntranceControl? = null
    /**
     * 当前页的下标
     */
    internal var mCurrentEntrancePos: Int = -1
    /**
     * 请求蓝牙开启
     */
    private val REQUEST_CODE_BLUETOOTH_ON = 1313
    /**
     * 蓝牙扫描时间
     */
    private val BLUETOOTH_DISCOVERABLE_DURATION = 60
    /**
     * 当前蓝牙是否在连接
     */
    private var isConnecting = false
    /**
     * 当前门禁页下标
     */
    private var currentPage = 0

    /**
     * 传感器
     */
    private var mManager: SensorManager? = null
    /**
     * adapter 门禁页
     */
    private lateinit var mEntranceViewApdater: BaseViewPagerAdapter

    /**
     * 蓝牙是否开启
     */
    private var isBluetoothOpen: Boolean = false
    /**
     * 默认测试蓝牙地址
     */
    val mDefaultMac = "18:93:D7:77:63:51"
//    private val mDefaultMac = "58:7A:62:26:AB:EE"
    /**
     * 默认门禁卡的卡号
     */
    private val mDefaultCardNo = "1217592446"
    /**
     * 蓝牙控制服务
     */
    private var mBleService: BleService? = null
    /**
     * 蓝牙开启附近位置权限，
     * 蓝牙需要位置服务才能扫描到附近设备
     */
    private val REQUEST_CODE_ACCESS_COARSE_LOCATION = 1
    /**
     * 蓝牙设备集合
     */
    private lateinit var mBleDevices: ArrayList<BluetoothInfo>
    /**
     * 门禁菜单列表
     */
    private lateinit var mDoorAdapter: DoorAdapter
    /**
     * 是否绑定好服务
     */
    private var mIsBind: Boolean = false
    /**
     * 是否显示蓝牙设备为空对话框
     */
    private var isShowBleNullDialog = false
    /**
     * 服务连接
     */
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
                    Toast.makeText(this@MainActivity, "你的设备不具备蓝牙功能!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mBleService = null
            mIsBind = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = View.inflate(this, R.layout.activity_main, null)
        setContentView(inflate)
        initMainView()
    }

    private val mPeriodTime = 120000L
    /**
     * 初始化主界面
     */
    private fun initMainView() {
        //填充主界面
        mVsMainLayout.inflate()
        //屏幕适配
        ScreenFitUtils.auto(this)
        // 初始化标题栏
        iniTitleBar()
        // 初始化集合
        initArrays()
        mManager = getSystemService(Service.SENSOR_SERVICE) as SensorManager?
//        progressDialog = ProgressDialog.create(this)
        initBlueTooth()
        mRvDoors.layoutManager = LinearLayoutManager(this)
        EventBus.getDefault().register(this@MainActivity)
        mDoorAdapter = DoorAdapter(this)
        mRvDoors.adapter = mDoorAdapter
        initEntrance()
        initListener()
        doBindService()
        mPresenter.getDoors()
        startTimer()
    }

    private var mTimer: Timer? = null
    private fun startTimer() {
        if (mTimer == null) mTimer = Timer()
        mTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mPresenter.updateQrCodeUrl()
            }
        }, mPeriodTime, mPeriodTime)
    }


    private fun setData(entrance: EntranceControl) {
        this.mEntranceControl = entrance
        this.mBleDoors.clear()
        this.mEntranceControlViews.clear()
        this.mBleDoors.addAll(mEntranceControl!!.bles)
        if (mBleDoors.size > 0) {
            for (mBleDoor in mBleDoors) {
                val entranceView = EntranceView(this)
                entranceView.setEntrance(mBleDoor)
                mEntranceControlViews.add(entranceView)
                mBleDoor.isBleShow = isBluetoothOpen
            }
            mDoorAdapter.setDoors(mBleDoors)
            mEntranceViewApdater.notifyDataSetChanged()
            mDoorAdapter.notifyDataSetChanged()
            mVpEntranceControls.offscreenPageLimit = mBleDoors.size
            mCurrentEntrancePos = if (mCurrentEntrancePos < 0) 0 else mCurrentEntrancePos
            setCurrentEntrance()

        }

    }

    /**
     * 初始化集合
     */
    private fun initArrays() {
        mBleDoors = ArrayList()
        mEntranceControlViews = ArrayList()
        mBleDevices = ArrayList()
    }

    /**
     * 初始化titleBar
     */

    private fun iniTitleBar() {
        setSupportActionBar(mToolBar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, mDlMenus, mToolBar, R.string.open, R.string.close) {}
        mDlMenus.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    /**
     * 初始化监听器
     */
    private fun initListener() {
        mDoorAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                mVpEntranceControls.currentItem = position
                if (mDlMenus.isDrawerOpen(Gravity.START))
                    mDlMenus.closeDrawer(Gravity.START)
            }

        })
        mAuthorizationVisitor.setOnClickListener {
            startActivity<AuthorizationVisitorActivity>(
                    "mEntranceControls" to mBleDoors,
                    "pos" to currentPage)
        }
        mTvQrCode.setOnClickListener(this)
        mTvBluetooth.setOnClickListener(this)
        mVpEntranceControls.addOnPageChangeListener(object : CusPageChangeListener() {
            override fun onPageSelected(position: Int) {
                mManager?.unregisterListener(mEntranceControlViews.get(mCurrentEntrancePos))
                mCurrentEntrancePos = position
                currentPage = position
                setCurrentEntrance()
                connectBleDevice(position)
            }
        })
        mTvLoginOut.setOnClickListener {
            QMUIDialog.MessageDialogBuilder(mContext)
                    .setTitle(getString(R.string.login_out))
                    .setMessage(getString(R.string.login_out_confirm))
                    .addAction(getString(R.string.cancel)) { dialog, index -> dialog.dismiss() }
                    .addAction(0, getString(R.string.exit), QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, _ ->
                        dialog.dismiss()
                        AppPrefsUtils.putBoolean(DataResource.IS_LOGIN, false)
                        ARouter.getInstance()
                                .build(ARouterPath.PATH_LOGIN)
                                .navigation()
                        finish()
                    }
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show()
        }

        mSflRoot.setOnRetryClickListener(object : OnRetryClickListener {
            override fun onRetryClick(view: View) {
                mPresenter.getDoors()
            }
        })
    }

    /**
     * 绑定服务
     */
    private fun doBindService() {
        val serviceIntent = Intent(this, BleService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    /**
     * 判断蓝牙是否开启，如果没有开启，则开启蓝牙
     */
    private fun initBlueTooth() {
        val openBluetoothBySystemIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        openBluetoothBySystemIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BLUETOOTH_DISCOVERABLE_DURATION)
        startActivityForResult(openBluetoothBySystemIntent, REQUEST_CODE_BLUETOOTH_ON)
    }

    /**
     * 初始化门禁
     */
    private fun initEntrance() {
        mEntranceViewApdater = BaseViewPagerAdapter(mEntranceControlViews)
        mVpEntranceControls.adapter = mEntranceViewApdater
    }

    /**
     * 显示门禁显示以及注册 传感器 SensorManager
     */
    private fun setCurrentEntrance() {
        if (mCurrentEntrancePos < 0) return
        changeDrawable(mBleDoors.get(mCurrentEntrancePos).isBleShow)
        mManager!!.registerListener(mEntranceControlViews.get(mCurrentEntrancePos), mManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    /**
     * 选择结果处理
     */
    private fun changeDrawable(isCanBluetooth: Boolean) {
        mBleDoors.get(mCurrentEntrancePos).isBleShow = isCanBluetooth
        if (isCanBluetooth) {
            var drawable = ResourceUtils.getDrawable(R.mipmap.qrcode_normal)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mTvQrCode.setCompoundDrawables(null, drawable, null, null)
            drawable = ResourceUtils.getDrawable(R.mipmap.bluetooth)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mTvBluetooth.setCompoundDrawables(null, drawable, null, null)
            mEntranceControlViews[mCurrentEntrancePos].setShark()
        } else {
            var drawable = ResourceUtils.getDrawable(R.mipmap.qrcode)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mTvQrCode.setCompoundDrawables(null, drawable, null, null)
            drawable = ResourceUtils.getDrawable(R.mipmap.bluetooth_normal)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mTvBluetooth.setCompoundDrawables(null, drawable, null, null)
            mEntranceControlViews[mCurrentEntrancePos].setQrCode(BaseUrls.base_url + mEntranceControl!!.QRCodeUrl)
        }

    }

    /**
     * 蓝牙开启，是否开启提示显示，以及如果蓝牙可用，将所有的门禁第一选项改为蓝牙开门
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE_BLUETOOTH_ON == requestCode) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    for (mEntranceControl in mBleDoors) {
                        mEntranceControl.isBleShow = true
                    }
                    isBluetoothOpen = true
                    setCurrentEntrance()
                }
                Activity.RESULT_CANCELED -> {
                    for (mEntranceControl in mBleDoors) {
                        mEntranceControl.isBleShow = false
                    }
                    isBluetoothOpen = false
                }

            }

        }
    }

    /**
     * 蓝牙设备扫描到后的界面显示
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun bleFindEvent(event: BleFindEvent?) {
        if (event != null) {
            var bluetoothInfo = event.getBluetoothInfo().bluetoothDevice
            val name = bluetoothInfo.name
            var uuid = bluetoothInfo.uuids
            val type = bluetoothInfo.type
            if (mBleDevices.isEmpty()) {
                mBleDevices.add(event.getBluetoothInfo())
            } else {
                if (!mBleDevices.contains(event.getBluetoothInfo())) {
                    mBleDevices.add(event.getBluetoothInfo())
                }
            }
            if (!isConnecting)
                connectBleDevice(currentPage)
        }
    }

    /**
     * 蓝牙状态显示时间回调
     *  BleStatus.ACTION_GATT_CONNECTING ->连接中
     *  BleStatus.ACTION_GATT_CONNECTED ->已连接
     *  BleStatus.ACTION_GATT_DISCONNECTING-> 断开连接中
     *  BleStatus.ACTION_GATT_DISCONNECTED->已断开
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun bleStatusEvent(event: BleStatusEvent?) {
        if (event != null) {
            when (event.getStatus()) {
                BleStatus.ACTION_GATT_CONNECTING -> {
//                    toast("连接中……")
                    LoggerHelper.d("连接中……")
                }
                BleStatus.ACTION_GATT_CONNECTED -> {
//                    toast("已连接")
                    LoggerHelper.d("已连接……")
                }
                BleStatus.ACTION_GATT_DISCONNECTING -> {
//                    toast("断开连接中……")
                    LoggerHelper.d("断开连接中……")
                }
                BleStatus.ACTION_GATT_DISCONNECTED -> {
//                    toast("已断开")
                    LoggerHelper.d("已断开……")
                }
            }
        }
    }

    /**
     * 连接蓝牙
     * 在 onRestart() 搜索到设备 当前页改变 以及 摇一摇开门时
     *连接蓝夜的逻辑
     */
    fun connectBleDevice(position: Int) {
        if (mBleDevices.size > 0 && mBleDoors.size > 0) {
            var isContain = false
            val ble = mBleDoors.get(position)
            LoggerHelper.d("状态：当前页的mac地址为……${ble.bluetoothMac}")
//            判断当前页的蓝牙门禁是否在连接范围之内，如果在连接，如果不在，则断开
            for (bluetoothInfo in mBleDevices) {
                val address = bluetoothInfo.bluetoothDevice.address.replace(":", "")
                if (!isConnecting && TextUtils.equals(address, ble.bluetoothMac)) {
                    LoggerHelper.d("状态：正在连接……")
                    isContain = true
                    mBleService!!.connect(bluetoothInfo.bluetoothDevice)
                    isConnecting = true
                }
            }
            if (!isContain) {
                mBleService?.scanLeDevice(true)
                if (isConnecting) {
                    mBleService!!.disconnect()
                    isConnecting = false
                }
            }
        } else {
            mBleService!!.connect(mDefaultMac)
        }
    }


    /**
     * 请求权限
     */
    private fun verifyIfRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(this, "只有允许访问位置才能搜索到蓝牙设备", Toast.LENGTH_SHORT).show()
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
    public fun bleFindEvent(event: BleSendCardIdEvent?) {
        if (event != null) {
            if (mEntranceControl == null) return
            if (isConnecting) {
                mBleService!!.send(mEntranceControl!!.bleNo.toByteArray())
            } else {
                connectBleDevice(currentPage)
                if (!isShowBleNullDialog) {
                    val qmuiDialog = QMUIDialog.MessageDialogBuilder(mContext)
                            .setTitle("没有扫描到设备")
                            .setMessage("需要打开调试模式吗？")
                            .addAction("取消") { dialog, index -> dialog.dismiss() }
                            .addAction(0, "打开", QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, index ->
                                dialog.dismiss()
                                startActivity<SearchBleActivity>()
                            }
                            .create(com.qmuiteam.qmui.R.style.QMUI_Dialog)
                    qmuiDialog.show()
                    qmuiDialog.setOnDismissListener { isShowBleNullDialog = false }
                    isShowBleNullDialog = true
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun updateQrCodeUrlEvent(event: UpdateQrCodeUrlEvent?) {
        if (event != null) {
            mPresenter.updateQrCodeUrl()
            cancelTimer()
            startTimer()
        }
    }

    /**
     * view 点击处理
     * 1、展示蓝牙还是二维码
     */
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mTvQrCode -> {
                changeDrawable(false)
            }
            R.id.mTvBluetooth -> {
                if (isBluetoothOpen) {
                    changeDrawable(true)
                } else {
                    initBlueTooth()
                }
            }


        }

    }

    /**
     * 点击返回的时间，用于双击退出操作
     */

    private var exitTime: Long = 0


    /**
     * 返回键的使用
     * 如果DrawLayout打开则先关闭
     */
    override fun onBackPressed() = if (mDlMenus.isDrawerOpen(GravityCompat.START)) {
        mDlMenus.closeDrawer(GravityCompat.START)
    } else {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast(getString(R.string.exit_app_if_double_back))
            exitTime = System.currentTimeMillis()
        } else {
            ActivityManager.instance.exitApp(this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        connectBleDevice(currentPage)
        setCurrentEntrance()
        startTimer()
    }

    /**
     * 在当前页面不可见时断开连接
     * 以便释放蓝牙占用，给予其他用户使用
     */
    override fun onPause() {
        super.onPause()
        disconnectBle()
        unregisterSensor()
        cancelTimer()
    }

    /**
     * 销毁定时器
     */
    private fun cancelTimer() {
        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }
    }

    /**
     * 销毁加速度监听
     */
    private fun unregisterSensor() {
        if (mCurrentEntrancePos > -1 && mEntranceControlViews.size > 0)
            mManager?.unregisterListener(mEntranceControlViews.get(mCurrentEntrancePos))
    }

    /**
     * 断开连接
     */
    private fun disconnectBle() {
        if (isConnecting) {
            mBleService?.disconnect()
            isConnecting = false
        }
    }

    /**
     * 销毁取消监听
     */
    override fun onDestroy() {
        super.onDestroy()
        doUnBindService()
        EventBus.getDefault().unregister(this@MainActivity)
    }

}

