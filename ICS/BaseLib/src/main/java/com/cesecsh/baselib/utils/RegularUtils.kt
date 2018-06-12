package com.cesecsh.baselib.utils

import android.text.TextUtils
import java.util.regex.Pattern


/**
 * 作者：RockQ on 2018/6/12
 * 邮箱：qingle6616@sina.com
 *
 * msg： 正则表达式utils
 * 邮箱、电话号码，座机等信息的匹配
 * 网址等
 */
object RegularUtils {

    /**
     * 验证手机号（简单）
     */
    private val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"
    /**
     * 验证车牌号
     */
    private val REGEX_CAR_NUM = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘" + "晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$"
    /**
     * 验证手机号（精确）
     *
     *
     *
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     *
     * 联通：130、131、132、145、155、156、175、176、185、186
     *
     * 电信：133、153、173、177、180、181、189
     *
     * 全球星：1349
     *
     * 虚拟运营商：170 199
     */
    private val REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,9])|(15[0-3,5-9])|(17[0-9])|(18[0-9])|(19[0-9])|(147))\\d{8}$"
    /**
     * 验证座机号,正确格式：xxx/xxxx-xxxxxxx/xxxxxxxx/
     */
    private val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"
    /**
     * 验证邮箱
     */
    private val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
    /**
     * 验证url
     */
    private val REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?"
    /**
     * 验证汉字
     */
    private val REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$"
    /**
     * 验证用户名,取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    private val REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$"
    /**
     * 验证密码，取值范围是a-z A-Z 0-9
     */
    private val REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$"
    /**
     * 验证IP地址
     */
    private val REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"

    //If u want more please visit http://toutiao.com/i6231678548520731137/

    /**
     * @param string 待验证文本
     * @return 是否符合手机号（简单）格式
     */
    fun isMobileSimple(string: String): Boolean {
        return isMatch(REGEX_MOBILE_SIMPLE, string)
    }

    /**
     * @param string 待验证文本
     * @return 是否符合手机号（精确）格式
     */
    fun isMobileExact(string: String): Boolean {
        return isMatch(REGEX_MOBILE_EXACT, string)
    }

    /**
     * @param string 待验证文本
     * @return 是否符合座机号码格式
     */
    fun isTel(string: String): Boolean {
        return isMatch(REGEX_TEL, string)
    }

    /**
     * @param string 待验证文本
     * @return 是否符合邮箱格式
     */
    fun isEmail(string: String): Boolean {
        return isMatch(REGEX_EMAIL, string)
    }

    /**
     * @param string 待验证文本
     * @return 是否符合网址格式
     */
    fun isURL(string: String): Boolean {
        return isMatch(REGEX_URL, string)
    }

    /**
     * @param string 待验证文本
     * @return 是否符合汉字
     */
    fun isChz(string: String): Boolean {
        return isMatch(REGEX_CHZ, string)
    }

    /**
     * @param string 待验证文本
     * @return 是否符合用户名
     */
    fun isUsername(string: String): Boolean {
        return isMatch(REGEX_USERNAME, string)
    }

    /**
     * @param string 待验证的文本
     * @return 是否符合文本
     */
    fun isPassword(string: String): Boolean {
        return isMatch(REGEX_PASSWORD, string)
    }

    /**
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    fun isMatch(regex: String, string: String): Boolean {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string)
    }

    /**
     * 验证车牌号是否符合规范
     * @param inputCarNumber
     * @return
     */
    fun isCarNumber(inputCarNumber: String): Boolean {
        return isMatch(REGEX_CAR_NUM, inputCarNumber)
    }

}