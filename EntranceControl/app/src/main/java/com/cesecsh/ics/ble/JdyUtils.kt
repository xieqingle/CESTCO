package com.cesecsh.ics.ble

/**
 * 作者：RockQ on 2018/8/21
 * 邮箱：qingle6616@sina.com
 *
 * msg：
 */
object JdyUtils {
    fun spiltJdy(p: ByteArray): JdyType? {
        if (p == null || p.isEmpty()) return JdyType.JDY
        val dev_VID = 0x88.toByte()
        if (p.size != 62) return null
        var str: String

        val m1 = (p[18 + 2] + 1 xor 0x11).toByte()////透传模块密码位判断
        str = String.format("%02x", m1)

        val m2 = (p[17 + 2] + 1 xor 0x22).toByte()//透传模块密码位判断
        str = String.format("%02x", m2)
        //Log.d( "out_2","="+ str);

        var ib1_major = 0
        var ib1_minor = 0
        if (p[52] == 0xff.toByte()) {
            if (p[53] == 0xff.toByte()) ib1_major = 1
        }
        if (p[54] == 0xff.toByte()) {
            if (p[55] == 0xff.toByte()) ib1_minor = 1
        }
        if (p[5] == 0xe0.toByte() && p[6] == 0xff.toByte() && p[11] == m1 && p[12] == m2 && dev_VID === p[19 - 6])
        //JDY
        {
            val writeBytes = ByteArray(4)
            writeBytes[0] = p[19 - 6]
            writeBytes[1] = p[20 - 6]
            if (p[20 - 6] == 0xa0.toByte())
                return JdyType.JDY//透传
            else if (p[20 - 6] == 0xa5.toByte())
                return JdyType.JDY_AMQ//按摩器
            else if (p[20 - 6] == 0xb1.toByte())
                return JdyType.JDY_LED1// LED灯
            else if (p[20 - 6] == 0xb2.toByte())
                return JdyType.JDY_LED2// LED灯
            else if (p[20 - 6] == 0xc4.toByte())
                return JdyType.JDY_KG// 开关控制
            else if (p[20 - 6] == 0xc5.toByte()) return JdyType.JDY_KG1// 开关控制
            return JdyType.JDY
        } else return if (p[44] == 0x10.toByte() && p[45] == 0x16.toByte() && (ib1_major == 1 || ib1_minor == 1)) {
            JdyType.sensor_temp
        } else if (p[3] == 0x1a.toByte() && p[4] == 0xff.toByte()
        ) {
                       JdyType.JDY_iBeacon
        } else {
            JdyType.UNKW
        }
    }

//                if( p[57]==(byte)0xe0 ){return JdyType.JDY_iBeacon;}//iBeacon模式
//                else if( p[57]==(byte)0xe1 ){return JdyType.sensor_temp;}////温度传感器
//                else if( p[57]==(byte)0xe2 ){return JdyType.sensor_humid;}////湿度传感器
//                else if( p[57]==(byte)0xe3 ){return JdyType.sensor_temp_humid;}////湿湿度传感器
//                else if( p[57]==(byte)0xe4 ){return JdyType.sensor_fanxiangji;}////芳香机香水用量显示仪
//                else if( p[57]==(byte)0xe5 ){return JdyType.sensor_zhilanshuibiao;}////智能水表传感器，抄表仪
//                else if( p[57]==(byte)0xe6 ){return JdyType.sensor_dianyabiao;}////电压传感器
//                else if( p[57]==(byte)0xe7 ){return JdyType.sensor_dianliu;}////电流传感器
//                else if( p[57]==(byte)0xe8 ){return JdyType.sensor_zhonglian;}////称重传感器
//                else if( p[57]==(byte)0xe9 ){return JdyType.sensor_pm2_5;}////PM2.5传感器
}