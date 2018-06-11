package com.cesecsh.usercenter.presenter

import com.cesecsh.baselib.presenter.BasePresenter
import com.cesecsh.baselib.presenter.view.BaseView
import com.cesecsh.baselib.presenter.view.CommonView
import com.cesecsh.usercenter.data.UserService
import com.cesecsh.usercenter.data.impl.UserServiceImpl

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class LoginPresenter : BasePresenter<CommonView>() {
    private var userService: UserService

    init {
        userService = UserServiceImpl()
    }

    fun login(username: String, password: String, pushId: String) {
        userService.login(username, password, pushId)
    }
}