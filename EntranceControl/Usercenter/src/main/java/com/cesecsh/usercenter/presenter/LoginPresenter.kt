package com.cesecsh.usercenter.presenter

import com.cesecsh.baselib.common.DataResource
import com.cesecsh.baselib.data.JsonUtils
import com.cesecsh.baselib.ext.execute
import com.cesecsh.baselib.presenter.BasePresenter
import com.cesecsh.baselib.rx.BaseObserver
import com.cesecsh.usercenter.data.UserService
import com.cesecsh.usercenter.data.impl.UserServiceImpl
import com.cesecsh.usercenter.data.protocol.User
import com.cesecsh.usercenter.presenter.view.LoginView
import com.kotlin.base.rx.BaseException
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
                        val result = t.string()
                        val normalObject = JsonUtils.json2NormalObject<User>(result, User::class.java)
                        val user = normalObject.obj
                        if (user != null) {
                            AppPrefsUtils.putString(DataResource.userName, username)
                            AppPrefsUtils.putBoolean(DataResource.IS_LOGIN, true)
                            AppPrefsUtils.putString(DataResource.userInfo, result)
                            AppPrefsUtils.putString(DataResource.token, user.token)
                            AppPrefsUtils.putString(DataResource.userId, user.userId)
                            (mView as LoginView).showLoginResult(user)
                        } else onError(BaseException(Integer.valueOf(normalObject.code), normalObject.msg))
                    }
                }, mProvider)
    }
}