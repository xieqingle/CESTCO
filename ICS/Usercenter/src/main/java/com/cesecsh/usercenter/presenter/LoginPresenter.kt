package com.cesecsh.usercenter.presenter

import com.cesecsh.baselib.data.JsonUtils
import com.cesecsh.baselib.ext.execute
import com.cesecsh.baselib.presenter.BasePresenter
import com.cesecsh.baselib.rx.BaseObserver
import com.cesecsh.usercenter.IS_LOGIN
import com.cesecsh.usercenter.USER_INFO
import com.cesecsh.usercenter.data.UserService
import com.cesecsh.usercenter.data.impl.UserServiceImpl
import com.cesecsh.usercenter.data.protocol.User
import com.cesecsh.usercenter.presenter.view.LoginView
import com.kotlin.base.utils.AppPrefsUtils
import okhttp3.ResponseBody

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class LoginPresenter : BasePresenter<LoginView>() {
    private var userService: UserService = UserServiceImpl()
    fun login(username: String, password: String, pushId: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService
                .login(username, password, pushId)
                .execute(object : BaseObserver<ResponseBody>(mView) {
                    override fun onNext(t: ResponseBody) {
                        val normalObject = JsonUtils.json2NormalObject<User>(t.string(), User::class.java)
                        val user = normalObject.obj
                        if (user != null) {
                            AppPrefsUtils.putString(USER_INFO, t.string())
                            AppPrefsUtils.putBoolean(IS_LOGIN, true)
                            (mView as LoginView).showLoginResult(user)
                        } else mView.onError(normalObject.message)
                    }
                }, mProvider)
    }
}