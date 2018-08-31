package com.cesecsh.baselib.rx

import com.cesecsh.baselib.presenter.view.BaseView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：Observer基类，省去OnComplete onError OnSubscribe 方法
 */
abstract class BaseObserver<T>(val mView: BaseView) : Observer<T> {
    private var t: T? = null
    override fun onNext(t: T) {
        this.t = t
    }

    override fun onComplete() {
        mView.hideLoading()
        if (this.t != null && t is ResponseBody ) (t as ResponseBody).close()
    }

    override fun onSubscribe(d: Disposable) {
    }


    override fun onError(e: Throwable) {
        mView.hideLoading()
        ExceptionHelper.handleException(mView, e)
    }
}