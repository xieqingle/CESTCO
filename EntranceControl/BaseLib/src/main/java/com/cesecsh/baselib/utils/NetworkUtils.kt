package com.cesecsh.baselib.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.text.TextUtils

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Enumeration

/**
 * 作者：RockQ on 2018/6/11
 * 邮箱：qingle6616@sina.com
 *
 * 和网络相关utils
 */


class NetworkUtils private constructor() {
    init {
        throw UnsupportedOperationException("你不能直接创建我，我是单例")
    }

    object Constants {
        // Unknown network class
        const val NETWORK_CLASS_UNKNOWN = 0
        // wifi network
        const val NETWORK_WIFI = 1
        // "2G" networks
        const val NETWORK_CLASS_2_G = 2
        // "3G" networks
        const val NETWORK_CLASS_3_G = 3
        // "4G" networks
        const val NETWORK_CLASS_4_G = 4
    }

    companion object {

        /**
         * 打开网络设置界面
         *
         *
         * 3.0以下打开设置界面
         */
        fun openWirelessSettings(context: Context) {
            if (android.os.Build.VERSION.SDK_INT > 10) {
                context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
            } else {
                context.startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS))
            }
        }

        /**
         * 判断是否网络连接
         *
         *
         * 需添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
         */
        fun isConnected(context: Context): Boolean {
            val cm = context
                    .getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            return info != null && info.isConnected

            //另外一种实现模式
            // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            //        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //        if (connectivityManager == null) {
            //            return false;
            //        } else {
            //            // 获取NetworkInfo对象
            //            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            //            if (networkInfo != null && networkInfo.length > 0) {
            //                for (int i = 0; i < networkInfo.length; i++) {
            //                    System.out.println(i + "===状态===" + networkInfo[i].getState());
            //                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
            //                    // 判断当前网络状态是否为连接状态
            //                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
            //                        return true;
            //                    }
            //                }
            //            }
            //        }
        }

        /**
         * 判断wifi是否连接状态
         *
         *
         * 需添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
         */

        fun isWifiConnected(context: Context): Boolean {
            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm != null && cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
        }


        /*
            检测3G是否连接
         */
        fun is3gConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE
        }

        /**
         * 获取移动网络运营商名称
         *
         *
         * 如中国联通、中国移动、中国电信
         */
        fun getNetworkOperatorName(context: Context): String? {
            val tm = context
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm?.networkOperatorName
        }

        /**
         * 获取移动终端类型
         * <pre>
         * PHONE_TYPE_NONE  : 0 手机制式未知
         * PHONE_TYPE_GSM   : 1 手机制式为GSM，移动和联通
         * PHONE_TYPE_CDMA  : 2 手机制式为CDMA，电信
         * PHONE_TYPE_SIP   : 3
         * <pre></pre>
        </pre> */
        fun getPhoneType(context: Context): Int {
            val tm = context
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm?.phoneType ?: -1
        }

        /**
         * 获取手机连接的网络类型(2G,3G,4G)
         *
         *
         * 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
         */
        fun getNetworkTpye(context: Context): Int {
            val tm = context
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            when (tm.networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> return Constants.NETWORK_CLASS_2_G
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> return Constants.NETWORK_CLASS_3_G
                TelephonyManager.NETWORK_TYPE_LTE -> return Constants.NETWORK_CLASS_4_G
                else -> return Constants.NETWORK_CLASS_UNKNOWN
            }
        }

        /**
         * 获取当前手机的网络类型(WIFI,2G,3G,4G)
         *
         *
         * 需添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
         *
         *
         * 需要用到上面的方法
         */
        fun getCurNetworkType(context: Context): Int {
            var netWorkType = Constants.NETWORK_CLASS_UNKNOWN
            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                val type = networkInfo.type
                if (type == ConnectivityManager.TYPE_WIFI) {
                    netWorkType = Constants.NETWORK_WIFI
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    netWorkType = getNetworkTpye(context)
                }
            }
            return netWorkType
        }

        /**
         * 获取ip
         *
         *
         * 建立静态常量，以便在访问网络的时候经常访问网络获取ip
         */
        private var ip: String? = ""

        fun getIp(): String? {
            try {
                val en = NetworkInterface
                        .getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf
                            .inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                            return inetAddress.getHostAddress().toString()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        /**
         * 获取IP地址
         * @return
         */
        /**
         * 获取IP地址
         * @return
         */
        fun getIpAddress(): String? {
            if (!TextUtils.isEmpty(ip)) {
                return ip
            } else {
                ip = getIp()
                return ip
            }
        }
    }

}
