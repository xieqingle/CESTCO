package com.cesecsh.usercenter.data.impl


import com.cesecsh.baselib.data.BaseService
import com.cesecsh.baselib.data.RetrofitFactory
import io.reactivex.Observable
import okhttp3.ResponseBody


/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：用户相关数据层
 */
class UserRepository {
    fun login(username: String, password: String, pushId: String): Observable<ResponseBody> {
        val maps = HashMap<String, Any>()
        maps.put("username", username)
        maps.put("password", password)
        maps.put("JG_REGIST_ID", "1112222")
        return RetrofitFactory.instance.create(BaseService::class.java).post("client/login.app", maps)
    }

}