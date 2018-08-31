package com.cesecsh.ics.ui.activity.entrance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.cesecsh.baselib.ui.base.BaseMvpActivity
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.baselib.utils.ToastUtils
import com.cesecsh.baselib.widget.callBack.OnItemClickListener
import com.cesecsh.ics.R
import com.cesecsh.ics.data.domain.Ble
import com.cesecsh.ics.presenter.entrace.EntranceControlSelectPresenter
import com.cesecsh.ics.presenter.view.EntranceControlSelectView
import com.cesecsh.ics.ui.activity.entrance.adapter.EntranceControlSelectedAdapter
import kotlinx.android.synthetic.main.activity_entrance_control_select.*

class EntranceControlSelectActivity : BaseMvpActivity<EntranceControlSelectPresenter>(), EntranceControlSelectView {

    private lateinit var mEntranceControls: ArrayList<Ble>
    private var mSelectEntranceControls: ArrayList<Ble>? = null

    companion object {
        const val ENTRANCE_CONTROLS = "entranceControls"
        const val SELECTED_ENTRANCE_CONTROLS = "selectedEntranceControls"

        fun launch(context: Activity, entranceControls: ArrayList<Ble>?, selectedEntranceControls: ArrayList<Ble>?, requestCode: Int) {
            if (entranceControls == null || entranceControls.isEmpty()) {
                ToastUtils.toast(context, "无地址可选")
                return
            }

            val intent = Intent(context, EntranceControlSelectActivity::class.java)
            intent.putExtra(ENTRANCE_CONTROLS, entranceControls)
            if (selectedEntranceControls != null && !selectedEntranceControls.isEmpty())
                intent.putExtra(SELECTED_ENTRANCE_CONTROLS, selectedEntranceControls)

            context.startActivityForResult(intent, requestCode)

        }

    }

    override fun initPresenterAndView() {
        mPresenter = EntranceControlSelectPresenter()
        mPresenter.mView = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance_control_select)
        mEntranceControls = intent.getParcelableArrayListExtra<Ble>(ENTRANCE_CONTROLS)
        mSelectEntranceControls = intent.getParcelableArrayListExtra<Ble>(SELECTED_ENTRANCE_CONTROLS)
        if (mSelectEntranceControls == null) mSelectEntranceControls = ArrayList()
        else {
            for (mSelectEntranceControl in mSelectEntranceControls!!) {
                for (mEntranceControl in mEntranceControls) {
                    if (TextUtils.equals(mSelectEntranceControl.bluetoothMac, mEntranceControl.bleName)) {
                        mEntranceControl.isSelected = mSelectEntranceControl.isSelected
                    }
                }
            }
        }
        initView()
        initListener()
    }

    private fun initListener() {
        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val entranceControl = mEntranceControls.get(position)
                entranceControl.isSelected = !entranceControl.isSelected
                mAdapter.notifyItemChanged(position)
            }

        })
    }

    private lateinit var mAdapter: EntranceControlSelectedAdapter

    private fun initView() {
        ScreenFitUtils.auto(this)
        mTitleBar.setTitle(getString(R.string.entrance_control_select))
        mTitleBar.addLeftBackImageButton().setOnClickListener { finish() }
        mTitleBar.addRightTextButton(getString(R.string.submit), R.id.rightViewId).setOnClickListener { submit() }
        mRvEntranceControls.layoutManager = LinearLayoutManager(this)
        mAdapter = EntranceControlSelectedAdapter(mContext)
        mAdapter.setEntranceControls(mEntranceControls)
        mRvEntranceControls.adapter = mAdapter
    }

    private fun submit() {
        mSelectEntranceControls!!.clear()
        mEntranceControls.forEach { if (it.isSelected) mSelectEntranceControls!!.add(it) }
//        if (mSelectEntranceControls!!.isEmpty()) {
//            toast(getString(R.string.please_select_address))
//            return
//        }
        val intent = Intent()
        intent.putExtra(SELECTED_ENTRANCE_CONTROLS, mSelectEntranceControls);
        this.setResult(android.app.Activity.RESULT_OK, intent)
        finish()

    }
}
