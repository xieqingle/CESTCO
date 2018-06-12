package com.cesecsh.baselib.data

import io.reactivex.Observable
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.http.DELETE
import retrofit2.http.QueryMap
import retrofit2.http.GET
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.FieldMap


/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：服务访问 service
 */
interface BaseService {
    @POST
    @FormUrlEncoded
    fun post(@Url url: String, @FieldMap requestMaps: Map<String, String>): Observable<ResponseBody>

    @POST
    @FormUrlEncoded
    fun post(@Url url: String): Observable<ResponseBody>

    @GET
    operator fun get(@Url url: String, @QueryMap requestMaps: Map<String, Any>): Observable<ResponseBody>

    @GET
    operator fun get(@Url url: String): Observable<ResponseBody>

    @DELETE
    fun delete(@Url url: String, @QueryMap requestMaps: Map<String, Any>): Observable<ResponseBody>

    @DELETE
    fun delete(@Url url: String): Observable<ResponseBody>
}