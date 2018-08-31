package com.cesecsh.usercenter.data.impl


import com.cesecsh.baselib.common.DataResource
import com.cesecsh.baselib.data.BaseService
import com.cesecsh.baselib.data.RetrofitFactory
import com.kotlin.base.utils.AppPrefsUtils
import io.reactivex.Observable
import okhttp3.ResponseBody


/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：用户相关数据层
 */
object MainRepository {
    fun getQrAddress(): Observable<ResponseBody> {
        val maps = HashMap<String, String>()
        maps["userId"] = AppPrefsUtils.getString(DataResource.userId)
        return RetrofitFactory.instance.create(BaseService::class.java).get("employee/updateQrCode", maps)
    }

    fun getDoors(): Observable<ResponseBody> {
        val maps = HashMap<String, String>()
        maps["userId"] = AppPrefsUtils.getString(DataResource.userId)
        return RetrofitFactory.instance.create(BaseService::class.java).get("employee/getDevicesAndOpenCode", maps)
    }

}