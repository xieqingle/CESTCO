package com.cesecsh.usercenter.data.impl


import com.cesecsh.baselib.common.BaseConstant
import com.cesecsh.baselib.data.BaseService
import com.cesecsh.baselib.data.RetrofitFactory
import com.cesecsh.usercenter.R.string.username
import io.reactivex.Observable
import okhttp3.ResponseBody


/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：用户相关数据层
 */
object UserRepository {
    //用户注册
    fun getUserProtocol(): Observable<ResponseBody> {
        return RetrofitFactory.instance.create(BaseService::class.java).get(" client/agreement.app")
    }

    //用户注册
    fun register(phone: String, password: String, verification: String): Observable<ResponseBody> {
        val maps = HashMap<String, String>()
        maps.put("username", phone)
        maps.put("password", password)
        maps.put("randCode", verification)
        return RetrofitFactory.instance.create(BaseService::class.java).post("client/regist.app", maps)
    }

    //用户登陆
    fun login(username: String, password: String, pushId: String): Observable<ResponseBody> {
        val maps = HashMap<String, String>()
        maps.put("username", username)
        maps.put("password", password)
        maps.put("JG_REGIST_ID", "1112222")
        return RetrofitFactory.instance.create(BaseService::class.java).post("client/login.app", maps)
    }

    //密码重置
    fun resetPassword(phone: String, password: String?, verification: String): Observable<ResponseBody> {
        val maps = HashMap<String, String>()
        maps.put("username", phone)
        password?.let { maps.put("password", it) }
        maps.put("randCode", verification)
        return RetrofitFactory.instance.create(BaseService::class.java).post("client/password.app", maps)
    }

    fun getVerification(phone: String, template: String): Observable<ResponseBody> {
        val maps = HashMap<String, String>()
        maps.put("tel", phone)
        maps.put("template", template)
        return RetrofitFactory.instance.create(BaseService::class.java).post("alimessage/apply.app", maps)
    }

}