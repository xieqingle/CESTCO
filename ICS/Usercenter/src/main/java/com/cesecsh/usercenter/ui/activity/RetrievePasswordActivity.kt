package com.cesecsh.usercenter.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.cesecsh.baselib.ext.enable
import com.cesecsh.baselib.ext.isEnableByPhone
import com.cesecsh.baselib.ui.base.BaseMvpActivity
import com.cesecsh.baselib.utils.RegularUtils
import com.cesecsh.baselib.utils.ScreenFitUtils
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.presenter.RetrievePasswordPresenter
import com.cesecsh.usercenter.presenter.view.RetrievePasswordView
import kotlinx.android.synthetic.main.activity_retrieve_password.*
import org.jetbrains.anko.toast
import android.content.DialogInterface
import android.os.CountDownTimer
import com.cesecsh.baselib.ext.tarnsPasswordVisible
import com.cesecsh.baselib.utils.CountDownTimeHelper
import com.cesecsh.baselib.utils.ResourceUtils


/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：密码重置
 */
class RetrievePasswordActivity : BaseMvpActivity<RetrievePasswordPresenter>(), View.OnClickListener, RetrievePasswordView {
    override fun onVersificationError() {
        countDownTimer.cancel()
        mBtnGetVerification.setText(R.string.get_verification_again)
        mBtnGetVerification.isEnabled = true
    }

    override fun onVerificationResult(result: String) {
        toast(result)
    }

    override fun onRetrieveResult(result: String) {
        toast(result)
        finish()
        TODO("缺少RxBus自动登陆逻辑处理")

    }

    private lateinit var countDownTimer: CountDownTimer

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mBtnGetVerification -> {
                AlertDialog.Builder(this)
                        .setTitle(R.string.sms_send)
                        .setMessage(getString(R.string.send_sms_warning) + mEtPhone.text)
                        .setPositiveButton(getString(R.string.confirm), DialogInterface.OnClickListener { dialog, which ->
                            countDownTimer = CountDownTimeHelper(mBtnGetVerification, ResourceUtils.getInteger(R.integer.countDownTime), 1).start()
                            mPresenter.getVerification(mEtPhone.text.toString())
                        })
                        .create()
                        .show()
            }
            R.id.mBtnSubmit -> {
                mPresenter.resetPassword(mEtPhone.text.toString(), mEtPassword.text.toString(), mEtVerification.text.toString())
            }

        }
    }

    override fun initPresenter() {
        mPresenter = RetrievePasswordPresenter()
        mPresenter.mView = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrieve_password)
        ScreenFitUtils.auto(this)
        initView()
        mBtnSubmit.enable(mEtPhone, { isBtnEnable() })
        mBtnSubmit.enable(mEtVerification, { isBtnEnable() })
        mBtnSubmit.enable(mEtPassword, { isBtnEnable() })
        mBtnSubmit.setOnClickListener(this)
        mBtnGetVerification.enable(mEtPhone, { isGetVerificationBtnEnable() })
        mBtnShowPassword.tarnsPasswordVisible(mEtPassword)
        mBtnGetVerification.setOnClickListener(this)
        mBtnGetVerification.isEnableByPhone(mEtPhone)

    }

    private fun isGetVerificationBtnEnable(): Boolean {
        return mEtPhone.text.isNullOrEmpty().not()
                && RegularUtils.isMobileExact(mEtPhone.text.toString())
    }

    private fun isBtnEnable(): Boolean {
        return mEtPhone.text.isNullOrEmpty().not()
                && mEtVerification.text.isNullOrEmpty().not()
                && mEtPassword.text.isNullOrEmpty().not()
    }

    private fun initView() {
        mTitleBar.addLeftBackImageButton().setOnClickListener { finish() }
        mTitleBar.setTitle(getString(R.string.password_retrieve))

    }
}
