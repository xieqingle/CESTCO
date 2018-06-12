package com.cesecsh.baselib.ext

import android.widget.Button
import android.widget.EditText
import com.cesecsh.baselib.rx.BaseObserver
import com.cesecsh.baselib.widget.CusTextWatcher
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：定义一些空间的扩展方法
 */
/**
 * 定义按钮根据EditText的内容是否可点击
 */
fun Button.enable(et: EditText, method: () -> Boolean) {
    val btn = this
    et.addTextChangedListener(object : CusTextWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            btn.isEnabled = method()
        }

    })
}

fun <T> Observable<T>.execute(baseObserver: BaseObserver<T>, lifecycleProvider: LifecycleProvider<ActivityEvent>) {
    this
            .subscribeOn(Schedulers.io())
            .compose(lifecycleProvider.bindUntilEvent(ActivityEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(baseObserver)
}

