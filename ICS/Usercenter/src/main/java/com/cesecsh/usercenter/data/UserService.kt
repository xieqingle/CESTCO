package com.cesecsh.usercenter.data

import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：用户数据操作接口
 */
interface UserService {
    //用户登陆
    fun login(userName: String, password: String, pushId: String): Observable<ResponseBody>

    //密码重置
    fun resetPassWord(phone: String, password: String?, verification: String): Observable<ResponseBody>

    //获取验证码
    fun getVerification(phone: String, template: String): Observable<ResponseBody>

    //用户注册
    fun register(phone: String, password: String, verification: String): Observable<ResponseBody>

    // 获取用户权限
    fun getUserProtocol(): Observable<ResponseBody>
}