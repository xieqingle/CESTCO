package com.cesecsh.ics.ui.activity.entrance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import cn.cestco.dialoglib.bottomSheet.BottomSheetDialog
import com.cesecsh.baselib.ext.enable
import com.cesecsh.baselib.ui.base.BaseActivity
import com.cesecsh.baselib.utils.*
import com.cesecsh.baselib.utils.pickView.PickViewDateCallBack
import com.cesecsh.baselib.utils.pickView.PickViewUtils
import com.cesecsh.ics.R
import com.cesecsh.ics.data.domain.Ble
import com.cesecsh.ics.data.domain.EntranceControl
import kotlinx.android.synthetic.main.activity_authorization_visitor.*
import java.util.*
import kotlin.collections.ArrayList

class AuthorizationVisitorActivity : BaseActivity() {
    private var mEntranceControls: ArrayList<Ble>? = null
    private var mSelectEntranceControls: ArrayList<Ble>? = null
    private lateinit var mEntranceNames: ArrayList<String>
    //    private var mCurrentEntrancePos = 0
    private val SELECTED_ADDRESS_CODE = 0x0101
    private var mCurrentPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization_visitor)
        ScreenFitUtils.auto(this)
        mTitleBar.setTitle(getString(R.string.authorization_visitor))
        mTvAuthorArriveTime.text = TimeUtils.milliseconds2String(System.currentTimeMillis())
        mTitleBar.addLeftBackImageButton().setOnClickListener { finish() }
        mEntranceControls = intent.getParcelableArrayListExtra<Ble>("mEntranceControls")
        mCurrentPos = intent.getIntExtra("pos", 0)
        if (mEntranceControls == null || mEntranceControls!!.isEmpty()) {
            finish()
            return
        }
        mEntranceNames = ArrayList()
        for (mEntranceControl in mEntranceControls!!) {
            mEntranceNames.add(mEntranceControl.bleName!!)
        }
        mMvAddress.setRightText(mEntranceControls!!.get(mCurrentPos).bleName!!)
        initAddress()
        initListener()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                SELECTED_ADDRESS_CODE -> {
                    mSelectEntranceControls = data.getParcelableArrayListExtra(EntranceControlSelectActivity.SELECTED_ENTRANCE_CONTROLS)
                    initAddress()
                }
            }

        }
    }

    private var arriveTime = System.currentTimeMillis();
    private fun initListener() {
        mLlAddressName.setOnClickListener {
            EntranceControlSelectActivity.launch(mContext, mEntranceControls, mSelectEntranceControls, SELECTED_ADDRESS_CODE)
//            PickViewUtils.getItem(mContext, mEntranceNames, mCurrentEntrancePos, object : PickViewItemCallBack {
//                override fun onCallBack(options1: Int, options2: Int, options3: Int) {
//                    mCurrentEntrancePos = options1
//                    initAddress()
//                }
//            })
        }
        mLlAuthorTime.setOnClickListener {
            PickViewUtils.getDate(
                    mContext,
                    object : PickViewDateCallBack {
                        override fun onCallBack(selectDate: Date) {
                            arriveTime = selectDate.time
                            mTvAuthorArriveTime.text = TimeUtils.date2String(selectDate)
                        }
                    }
            )
        }
        mMvVisitAim.setOnClickListener {
            showDialogOfVisitAim()
        }
        mBtnGetQrCode.enable(mEtAuthorName) { isEtEnable() }
        mBtnGetQrCode.enable(mEtPhone) { isEtEnable() }
    }

    private var arriveAim = -1

    private fun showDialogOfVisitAim() {

        BottomSheetDialog.BottomListSheetBuilder(this)
                .addItem("工作联系")
                .addItem("销售推广")
                .addItem("面试")
                .addItem("其它")
                .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                    dialog.dismiss()
                    mMvVisitAim.setRightText(tag)
                    arriveAim = position
                    if (position == 3) {
                        mEtVisitAim.visibility = View.VISIBLE
                    } else mEtVisitAim.visibility = View.GONE

                }
                .build()
                .show()
    }


    private fun initAddress() {
        if (mSelectEntranceControls == null || mSelectEntranceControls!!.isEmpty()) {
            mTvAddress.text = getString(R.string.please_select_address)
            mTvAddress.gravity = Gravity.END
        } else {
            var str = ""
            mTvAddress.gravity = Gravity.START
            for (i in 0 until mSelectEntranceControls!!.size) {
                val entranceControl = mSelectEntranceControls!![i]
                str += if (i == mSelectEntranceControls!!.size - 1) {
                    "${i + 1}. ${entranceControl.bleName}"
                } else
                    "${i + 1}. ${entranceControl.bleName}\n"

            }
            if (!StringUtils.isNullOrEmpty(str)) {
                mTvAddress.text = str
            }
        }


    }

    private fun isEtEnable(): Boolean {
        return mEtAuthorName.text.isNullOrEmpty().not()
                && mEtPhone.text.isNullOrEmpty().not()
                && RegularUtils.isMobileExact(mEtPhone.text.toString())
    }

    fun getQRCode(view: View) {
//        var mainService: MainService = MainServiceImpl()
        if (arriveAim < 0) {
            ToastUtils.toast(this@AuthorizationVisitorActivity, "请选择来访目的")
            return
        }
        val url = "http://115.238.41.78:8083/ic/admin/manager/door/visitor/code.web?address=${mEntranceControls!!.get(mCurrentPos).bleName}&phone=${mEtPhone.text.toString()}&time=$arriveTime"
        VisitorQRCodeActivity.launch(this@AuthorizationVisitorActivity, url, "访客二维码", mEtPhone.text.toString())
//        mainService
//                .getQrAddress(mEtPhone.text.toString(), arriveTime, mEntranceControls!!.get(mCurrentPos).name)
//                .compose(this.bindUntilEvent(ActivityEvent.STOP))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : Observer<ResponseBody> {
//                    override fun onComplete() {
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                    }
//
//                    override fun onNext(t: ResponseBody) {
//                        println(t.string().toString())
////                        VisitorQRCodeActivity.launch(this@AuthorizationVisitorActivity, "http://115.238.41.78:8083/ic/estate/visitor/code.web?url=/upload/qrcode/qrcode.png&visitorId=155", "访客二维码", mEtPhone.text.toString())
//
//                    }
//
//                    override fun onError(e: Throwable) {
//                        println(e.message)
//                    }
//                })

    }
}
