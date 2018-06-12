package com.cesecsh.baselib.rx

import com.cesecsh.baselib.presenter.view.BaseView
import com.kotlin.base.rx.BaseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：Observer基类，省去OnComplete onError OnSubscribe 方法
 */
abstract class BaseObserver<T>(val mView: BaseView) : Observer<T> {
    override fun onComplete() {
        mView.hideLoading()
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        mView.hideLoading()
        if (e is BaseException) {
            mView.onError(e.msg)
        }
    }
}