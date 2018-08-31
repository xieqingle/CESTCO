package com.cesecsh.usercenter.data

import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：用户数据操作接口
 */
interface MainService {
    fun getQrAddress(): Observable<ResponseBody>
    fun getDoors(): Observable<ResponseBody>
}