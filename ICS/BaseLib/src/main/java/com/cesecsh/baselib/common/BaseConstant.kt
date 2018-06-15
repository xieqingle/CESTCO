package com.cesecsh.baselib.common

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * msg：常量
 */
class BaseConstant {
    companion object {
        //Token Key
        const val KEY_SP_TOKEN = "token"
        //SP表名
        const val TABLE_PREFS = "ics"
        const val SIGNIN = "REGIST"// 用户注册模板
        const val RETRIEVE = "EDIT_PASSWORD"// 密码修改模版
        const val DATABASE_NAME = "ics-db"
        const val TRADE_RETRIEVE = "EDIT_PAY_PASSWORD"// 修改支付密码模版

        //ARoute 路径,主Activity
        const val PATH_MAIN = "/app/main"
        //ARoute 路径 登陆Activity
        const val PATH_LOGIN = "/userCenter/login"
    }
}