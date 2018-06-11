package com.cesecsh.usercenter.data.impl

import com.cesecsh.usercenter.data.UserService

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class UserServiceImpl : UserService {

    override fun login(userName: String, password: String, pushId: String) {
        UserRepository().login(userName, password, pushId)
    }
}
