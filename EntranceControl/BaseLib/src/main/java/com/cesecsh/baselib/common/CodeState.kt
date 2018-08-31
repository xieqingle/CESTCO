package com.cesecsh.baselib.common

import android.util.SparseArray

/**
 * Created by 上海中电
 * on 2016/10/27
 * 返回码状态封装
 */

object CodeState {
    /**
     * 数据加载成功
     */
    const val SUCCESS = 200
    /**
     *  数据过时，需要重新加载
     */
    const val DATA_OVERDUE = 205
    /**
     *  参数错误
     */
    const val PARAM_ERROR = 404
    /**
     * 权限不足
     */
    const val PERMISSION_DENY = 405
    /**
     * 登录过期
     */
    const val LOGIN_OVERDUE = 406
    /**
     * 服务器错误
     */
    const val SERVER_ERROR = 500
    var messages: SparseArray<String>? = null
    var mCurrentCode = SUCCESS

    fun getMessage(codeState: Int): String {
        getData()
        return messages!!.get(codeState)
    }

    fun getData() {
        if (messages == null) {
            messages = SparseArray()
            messages!!.put(SUCCESS, "数据加载成功")
            messages!!.put(PARAM_ERROR, "参数填写错误")
            messages!!.put(PERMISSION_DENY, "您的权限不足")
            messages!!.put(LOGIN_OVERDUE, "您的登录过期")
            messages!!.put(SERVER_ERROR, "操作失败，请重试")
        }
    }
}
