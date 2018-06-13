package com.cesecsh.usercenter.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import com.cesecsh.baselib.ext.enable
import com.cesecsh.baselib.ext.isEnableByPhone
import com.cesecsh.baselib.ext.tarnsPasswordVisible
import com.cesecsh.baselib.ui.BaseMvpActivity
import com.cesecsh.baselib.ui.activity.WebViewActivity
import com.cesecsh.baselib.utils.*
import com.cesecsh.usercenter.R
import com.cesecsh.usercenter.data.protocol.Agreement
import com.cesecsh.usercenter.presenter.RegisterPresenter
import com.cesecsh.usercenter.presenter.view.RegisterView
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：用户注册界面
 */
class RegisterActivity : BaseMvpActivity<RegisterPresenter>(), View.OnClickListener, RegisterView {
    private var agreement: Agreement? = null
    override fun onAgreeementResult(agreement: Agreement) {
        this.agreement = agreement
    }

    override fun onRegisterResult(result: String) {
        toast(result)
    }

    override fun onVerificationResult(result: String) {
        toast(result)
        finish()
    }

    override fun onVersificationError() {
        countDownTimer.cancel()
        mBtnGetVerification.setText(R.string.get_verification_again)
        mBtnGetVerification.isEnabled = true
    }

    override fun initPresenter() {
        mPresenter = RegisterPresenter()
        mPresenter.mView = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        ScreenFitUtils.auto(this)
        initView()
    }

    private fun initView() {
        mTitleBar.addLeftBackImageButton().setOnClickListener { finish() }
        mTitleBar.setTitle(getString(R.string.register))
        mBtnSubmit.enable(mEtPhone, { isBtnEnable() })
        mBtnSubmit.enable(mEtPassword, { isBtnEnable() })
        mBtnSubmit.enable(mEtConfirmPassword, { isBtnEnable() })
        mBtnSubmit.enable(mEtVerification, { isBtnEnable() })
        mBtnGetVerification.enable(mEtPhone, { isGetVerificationBtnEnable() })
        mBtnGetVerification.isEnableByPhone(mEtPhone)
        mBtnShowPassword.tarnsPasswordVisible(mEtPassword)
        mBtnShowConfirmPassword.tarnsPasswordVisible(mEtConfirmPassword)
        mBtnGetVerification.isEnableByPhone(mEtPhone)
        mBtnGetVerification.setOnClickListener(this)
        mBtnSubmit.setOnClickListener(this)
        mTvProtocol.setOnClickListener(this)
        mPresenter.getUserProtocol()
    }

    private fun isGetVerificationBtnEnable(): Boolean {
        return mEtPhone.text.isNullOrEmpty().not()
                && RegularUtils.isMobileExact(mEtPhone.text.toString())
    }

    private fun isBtnEnable(): Boolean {
        return mEtPhone.text.isNullOrEmpty().not()
                && mEtPassword.text.isNullOrEmpty().not()
                && mEtConfirmPassword.text.isNullOrEmpty().not()
                && mEtVerification.text.isNullOrEmpty().not()
    }

    private lateinit var countDownTimer: CountDownTimer

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mBtnGetVerification -> {
                AlertDialog.Builder(this)
                        .setTitle(R.string.sms_send)
                        .setMessage(getString(R.string.send_sms_warning) + mEtPhone.text)
                        .setPositiveButton(getString(R.string.confirm), { _, _ ->
                            countDownTimer = CountDownTimeHelper(mBtnGetVerification, ResourceUtils.getInteger(R.integer.countDownTime), 1).start()
                            mPresenter.getVerification(mEtPhone.text.toString())
                        })
                        .create()
                        .show()
            }
            R.id.mBtnSubmit -> {
                if (!TextUtils.equals(mEtPassword.text, mEtConfirmPassword.text)) {
                    toast(getString(R.string.twice_password_match_faliure))
                    return
                }
                if (RegularUtils.isPassword(mEtPassword.text.toString())) {
                    toast(getString(R.string.passwordLimit))
                    return
                }
                mPresenter.register(mEtPhone.text.toString(), mEtPassword.text.toString(), mEtVerification.text.toString())

            }
            R.id.mTvProtocol -> {
                if (agreement != null && !StringUtils.isNullOrEmpty(agreement!!.agreementUrl)) {
                    WebViewActivity.launch(mPresenter.context, agreement!!.agreementUrl, "用户使用协议")
                }
            }
        }

    }
}