package com.cesecsh.baselib.data

import com.cesecsh.baselib.BuildConfig
import com.cesecsh.baselib.common.BaseConstant
import com.kotlin.base.utils.AppPrefsUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
class RetrofitFactory private constructor() {
    companion object {
        val instance: RetrofitFactory by lazy { RetrofitFactory() }
    }

    /**
     * retrofit对象
     */
    private val retrofit: Retrofit
    /**
     * 拦截器
     */
    private val interceptor: Interceptor

    init {
        interceptor = Interceptor { chain ->
            val request = chain.request()
                    .newBuilder()
                    .addHeader("Content_Type", "application/json")
                    .addHeader("charset", "UTF-8")
                    .addHeader("token", AppPrefsUtils.getString(BaseConstant.KEY_SP_TOKEN))
                    .build()

            chain.proceed(request)
        }
        //Retrofit实例化
        retrofit = Retrofit.Builder()
                .baseUrl(BaseUrls.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(initClient()!!)
                .build()
    }

    private fun initClient(): OkHttpClient? {
        return OkHttpClient
                .Builder()
                .addInterceptor(initInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
    }

    private fun initInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

}