package com.cesecsh.usercenter.data.impl

import com.cesecsh.usercenter.data.UserService
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class UserServiceImpl : UserService {
    // 获取用户协议
    override fun getUserProtocol(): Observable<ResponseBody> {
        return UserRepository.getUserProtocol()
    }

    // 用户注册
    override fun register(phone: String, password: String, verification: String): Observable<ResponseBody> {
        return UserRepository.register(phone, password, verification)
    }

    //用户登陆
    override fun login(userName: String, password: String, pushId: String): Observable<ResponseBody> {
        return UserRepository.login(userName, password, pushId)
    }

    // 密码重置
    override fun resetPassWord(phone: String, password: String?, verification: String): Observable<ResponseBody> {
        return UserRepository.resetPassword(phone, password, verification)
    }

    override fun getVerification(phone: String, template: String): Observable<ResponseBody> {
        return UserRepository.getVerification(phone, template)
    }
}
