package com.cesecsh.baselib.data

import com.cesecsh.baselib.data.domain.NormalObject
import com.cesecsh.baselib.data.domain.SimpleObject
import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
object JsonUtils {
    /**
     * json -> 简单实体类
     *
     * @param jsonStr :json字符串
     * @param clazz : 转换的对象
     */
    fun <T> json2NormalObject(jsonStr: String, clazz: Class<*>): NormalObject<T> {
        val gson = Gson()
        val objectType = type(NormalObject::class.java, clazz)
        return gson.fromJson(jsonStr, objectType)

    }

    fun json2SimpleObject(jsonStr: String): SimpleObject {
        val gson = Gson()
        return gson.fromJson(jsonStr, SimpleObject::class.java)

    }

    /**
     * 类型整合
     */
    private fun type(raw: Class<*>, vararg args: Type): ParameterizedType {

        return object : ParameterizedType {
            override fun getOwnerType(): Type? {
                return null
            }

            override fun getActualTypeArguments(): Array<out Type> {
                return args
            }

            override fun getRawType(): Type {
                return raw
            }
        }
    }

}
