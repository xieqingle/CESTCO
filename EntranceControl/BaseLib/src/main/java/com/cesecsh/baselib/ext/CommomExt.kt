package com.cesecsh.baselib.ext

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import com.cesecsh.baselib.R
import com.cesecsh.baselib.rx.BaseObserver
import com.cesecsh.baselib.utils.RegularUtils
import com.cesecsh.baselib.widget.CusTextWatcher
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
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


/**
 * 按钮是否可点击根据是否符合手机
 * @param et 手机号码输入
 */
fun Button.isEnableByPhone(et: EditText) {
    val btn = this
    val text = btn.text
    et.addTextChangedListener(object : CusTextWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            if (!RegularUtils.isMobileExact(s.toString())) {
                btn.text = context.getString(R.string.phone_error)
            } else btn.text = text
        }
    })
}

/**
 * 封装访问网络基础库
 */
fun <T> Observable<T>.execute(baseObserver: BaseObserver<T>, lifecycleProvider: LifecycleProvider<*>) {
    this.subscribeOn(Schedulers.io())
            .compose(lifecycleProvider.bindToLifecycle())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(baseObserver)
}

fun Button.tarnsPasswordVisible(et: EditText) {
    this.setOnClickListener {
        if (et.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
            et.transformationMethod = PasswordTransformationMethod.getInstance()
            this.setBackgroundResource(R.mipmap.password_hide)
        } else {
            et.transformationMethod = HideReturnsTransformationMethod.getInstance()
            this.setBackgroundResource(R.mipmap.password_show)
        }
    }

}

