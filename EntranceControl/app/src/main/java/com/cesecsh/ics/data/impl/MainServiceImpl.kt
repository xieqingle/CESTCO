package com.cesecsh.usercenter.data.impl

import com.cesecsh.usercenter.data.MainService
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class MainServiceImpl : MainService {
    override fun getDoors(): Observable<ResponseBody> {
        return MainRepository.getDoors()
    }

    override fun getQrAddress(): Observable<ResponseBody> {
        return MainRepository.getQrAddress()
    }

}
