package com.cesecsh.ics.widget

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cesecsh.baselib.common.BaseViewPage
import com.cesecsh.baselib.utils.ResourceUtils
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.ics.R
import com.cesecsh.ics.data.domain.Ble
import com.cesecsh.ics.event.BleSendCardIdEvent
import com.cesecsh.ics.event.UpdateQrCodeUrlEvent
import org.greenrobot.eventbus.EventBus


/**
 * 作者：RockQ on 2018/7/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class EntranceView(context: Activity) : BaseViewPage(context), SensorEventListener {


    private lateinit var mTvAddress: TextView
    private var entrance: Ble? = null
    private lateinit var mImgQrCode: ImageView
    private lateinit var mTvTitle: TextView
    fun setEntrance(entrance: Ble) {
        this.entrance = entrance
    }

    override fun initView(): View {
        val inflate = View.inflate(mContext, R.layout.item_entrance_control, null)
        ScreenFitUtils.auto(inflate)
        mTvAddress = inflate.findViewById(R.id.mTvAddress)
        mTvTitle = inflate.findViewById(R.id.mTvTitle)
        mImgQrCode = inflate.findViewById(R.id.mImgQrCode)
        return inflate
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        if (entrance == null) {
            return
        }
        mTvAddress.text = "地址：${entrance!!.bleName}"
        mImgQrCode.setOnClickListener {
            if (!isBluetooth)
                EventBus.getDefault().post(UpdateQrCodeUrlEvent())
        }
    }

    private var isBluetooth: Boolean = false
    fun setQrCode(url: String) {
        Glide.with(mContext)
                .load(url)
                .placeholder(R.mipmap.placehold_qrcode)
                .error(R.mipmap.placehold_qrcode)
                .into(mImgQrCode)
        mTvTitle.text = ResourceUtils.getString(R.string.scan_open_door)
        isBluetooth = false
    }

    public fun setShark() {
        Glide.with(mContext)
                .load(R.mipmap.shark_demo)
                .placeholder(R.mipmap.shark_demo)
                .error(R.mipmap.shark_demo)
                .into(mImgQrCode)
        mTvTitle.text = ResourceUtils.getString(R.string.shark_open_door)
        isBluetooth = true
    }

    /**
     * 检测的时间间隔
     */
    private val UPDATE_INTERVAL = 500
    /**
     * 上一次检测的时间
     */
    private var mLastUpdateTime: Long = 0

    private var mShartRange = 17
    /**
     * 摇一摇，加速度感应反馈
     */
    override fun onSensorChanged(event: SensorEvent) {
        //获取传感器类型
        val sensorType = event.sensor.type
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        val values = event.values
        //如果传感器类型为加速度传感器，则判断是否为摇一摇
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            if (Math.abs(values[0]) > mShartRange || Math.abs(values[1]) > mShartRange || Math
                            .abs(values[2]) > mShartRange) {
                val currentTime = System.currentTimeMillis()
                val diffTime = currentTime - mLastUpdateTime
                if (diffTime < UPDATE_INTERVAL) {
                    return
                }
                mLastUpdateTime = currentTime
                if (isBluetooth) {
                    shakeImgGrab()
                    EventBus.getDefault().post(BleSendCardIdEvent())
                }
            }
        }

    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    /**
     *摇一摇动画
     */
    private fun shakeImgGrab() {
        val vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator?.vibrate(200L)
        val pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        )

        val pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        )

        val pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * 1),
                Keyframe.ofFloat(.2f, -3f * 1),
                Keyframe.ofFloat(.3f, 3f * 1),
                Keyframe.ofFloat(.4f, -3f * 1),
                Keyframe.ofFloat(.5f, 3f * 1),
                Keyframe.ofFloat(.6f, -3f * 1),
                Keyframe.ofFloat(.7f, 3f * 1),
                Keyframe.ofFloat(.8f, -3f * 1),
                Keyframe.ofFloat(.9f, 3f * 1),
                Keyframe.ofFloat(1f, 0f)
        )

        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mImgQrCode, pvhScaleX, pvhScaleY, pvhRotate).setDuration(1000)
        objectAnimator.start()
    }
}