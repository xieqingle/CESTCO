package com.cesecsh.ics.presenter

import android.text.TextUtils
import com.cesecsh.baselib.data.JsonUtils
import com.cesecsh.baselib.ext.execute
import com.cesecsh.baselib.presenter.BasePresenter
import com.cesecsh.baselib.rx.BaseObserver
import com.cesecsh.ics.data.domain.EntranceControl
import com.cesecsh.ics.presenter.view.MainView
import com.cesecsh.usercenter.data.impl.MainServiceImpl
import com.kotlin.base.rx.BaseException
import okhttp3.ResponseBody

/**
 * 作者：RockQ on 2018/7/17
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class MainPresenter : BasePresenter<MainView>() {
    private val mainService = MainServiceImpl()
    fun getDoors() {
        if (!checkNetWork()) {
            mView.showNetWorkError()
            return
        }
        mView.showLoading()
        mainService.getDoors()
                .execute(object : BaseObserver<ResponseBody>(mView as MainView) {
                    override fun onNext(t: ResponseBody) {
                        val result = t.string()
                        var normalObject = JsonUtils.json2NormalObject<EntranceControl>(result, EntranceControl::class.java)
                        if (TextUtils.equals(normalObject.code, "200")) {
                            if (normalObject.obj == null) {
                                (mView as MainView).showEmptyContent()
                            } else {
                                (mView as MainView).showDoors(normalObject.obj!!)
                                mView.showContent()
                            }
                        } else
                            onError(BaseException(Integer.valueOf(normalObject.code), normalObject.msg))

                    }
                }, mProvider)
    }

    fun updateQrCodeUrl() {
        mainService.getQrAddress()
                .execute(object : BaseObserver<ResponseBody>(mView as MainView) {
                    override fun onNext(t: ResponseBody) {
                        val result = t.string()
                        var normalObject = JsonUtils.json2NormalObject<EntranceControl>(jsonStr = result, clazz = EntranceControl::class.java)
                        if (TextUtils.equals(normalObject.code, "200") && normalObject.obj != null) {
                            (mView as MainView).updateQrUrl(normalObject.obj!!.QRCodeUrl)
                            mView.showContent()
                        } else {
                            onError(BaseException(Integer.valueOf(normalObject.code), normalObject.msg))
                        }
                    }
                }, mProvider)
    }


}