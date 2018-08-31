package com.cesecsh.baselib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：谢青仂 on 2016/8/15
 * 邮箱：qingle6616@sina.com
 */
public class TimeUtils {
    /**
     * <p/>
     * 在工具类中经常使用到工具类的格式化描述，这个主要是一个日期的操作类，所以日志格式主要使用 SimpleDateFormat的定义格式.
     * <p/>
     * 格式的意义如下： 日期和时间模式 <br>
     * 日期和时间格式由日期和时间模式字符串指定。在日期和时间模式字符串中，未加引号的字母 'A' 到 'Z' 和 'a' 到 'z'
     * 被解释为模式字母，用来表示日期或时间字符串元素。文本可以使用单引号 (') 引起来，以免进行解释。"''"
     * 表示单引号。所有其他字符均不解释；只是在格式化时将它们简单复制到输出字符串，或者在分析时与输入字符串进行匹配。
     * <p/>
     * 定义了以下模式字母（所有其他字符 'A' 到 'Z' 和 'a' 到 'z' 都被保留）： <br>
     * <table>
     * <tr>  <td>字母</td>  <td>日期或时间元素</td>  <td>表示</td>  <td>示例</td>  <td> </td></tr>
     * <tr>  <td>G</td>  <td>Era</td>  <td>标志符</td>  <td>Text</td>  <td>AD</td>  <td> </td></tr>
     * <tr> <td>y</td>  <td>年</td>  <td>Year</td>  <td>1996;</td>  <td>96</td>  <td> </td></tr>
     * <tr>  <td>M</td>  <td>年中的月份</td>  <td>Month</td>  <td>July;</td>  <td>Jul;</td>  <td>07 </td></tr>
     * <tr>  <td>w</td>  <td>年中的周数</td>  <td>Number</td>  <td>27</td>  <td> </td></tr>
     * <tr>  <td>W</td>  <td>月份中的周数</td>  <td>Number</td>  <td>2</td>  <td> </td></tr>
     * <tr>  <td>D</td>  <td>年中的天数</td>  <td>Number</td>  <td>189</td>  <td> </td></tr>
     * <tr>  <td>d</td>  <td>月份中的天数</td>  <td>Number</td>  <td>10</td>  <td> </td></tr>
     * <tr>  <td>F</td>  <td>月份中的星期</td>  <td>Number</td>  <td>2</td>  <td> </td></tr>
     * <tr>  <td>E</td>  <td>星期中的天数</td>  <td>Text</td>  <td>Tuesday;</td>  <td>Tue </td></tr>
     * <tr>  <td>a</td>  <td>Am/pm</td>  <td>标记</td>  <td>Text</td>  <td>PM</td>  <td> </td></tr>
     * <tr>  <td>H</td>  <td>一天中的小时数（0-23）</td>  <td>Number</td>  <td>0 </td></tr>
     * <tr>  <td>k</td>  <td>一天中的小时数（1-24）</td>  <td>Number</td>  <td>24</td>  <td> </td></tr>
     * <tr>  <td>K</td>  <td>am/pm</td>  <td>中的小时数（0-11）</td>  <td>Number</td>  <td>0</td>  <td> </td></tr>
     * <tr>  <td>h</td>  <td>am/pm</td>  <td>中的小时数（1-12）</td>  <td>Number</td>  <td>12</td>  <td> </td></tr>
     * <tr>  <td>m</td>  <td>小时中的分钟数</td>  <td>Number</td>  <td>30</td>  <td> </td></tr>
     * <tr>  <td>s</td>  <td>分钟中的秒数</td>  <td>Number</td>  <td>55</td>  <td> </td></tr>
     * <tr>  <td>S</td>  <td>毫秒数</td>  <td>Number</td>  <td>978</td>  <td> </td></tr>
     * <tr>  <td>z</td>  <td>时区</td>  <td>General</td>  <td>time</td>  <td>zone</td>  <td>Pacific</td>  <td>Standard</td> <td>Time;</td>  <td>PST;</td>  <td>GMT-08:00 </td></tr>
     * <tr>  <td>Z</td>  <td>时区</td>  <td>RFC</td>  <td>822</td>  <td>time</td>  <td>zone</td>  <td>-0800</td>  <td> </td></tr>
     * </table>
     * <p/>
     * <p/>
     * <pre>
     *                     yyyy-MM-dd 1969-12-31
     *                     yyyy-MM-dd 1970-01-01
     *               yyyy-MM-dd HH:mm 1969-12-31 16:00
     *               yyyy-MM-dd HH:mm 1970-01-01 00:00
     *              yyyy-MM-dd HH:mmZ 1969-12-31 16:00-0800
     *              yyyy-MM-dd HH:mmZ 1970-01-01 00:00+0000
     *       yyyy-MM-dd HH:mm:ss.SSSZ 1969-12-31 16:00:00.000-0800
     *       yyyy-MM-dd HH:mm:ss.SSSZ 1970-01-01 00:00:00.000+0000
     *     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1969-12-31T16:00:00.000-0800
     *     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1970-01-01T00:00:00.000+0000
     * </pre>
     */
    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DEFAULT_SDF2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    /**
     * 各时间单位与毫秒的倍数
     */
    public static final int UNIT_MSEC = 1;
    public static final int UNIT_SEC = 1000;
    public static final int UNIT_MIN = 60000;
    public static final int UNIT_HOUR = 3600000;
    public static final int UNIT_DAY = 86400000;
    public static final String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String YESTERDAY = "一天前";
    private static final String THE_DAY_YESTERDAY = "前天";


    private TimeUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为yyyy-MM-dd HH:mm:ss
     */
    public static long string2Milliseconds(String time) {
        return string2Milliseconds(time, DEFAULT_SDF);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为yyyy-MM-dd HH:mm
     */
    public static long string2Milliseconds2(String time) {
        return string2Milliseconds(time, DEFAULT_SDF2);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为用户自定义
     */
    public static long string2Milliseconds(String time, SimpleDateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将时间字符串转为Date类型
     * <p>格式为yyyy-MM-dd HH:mm:ss
     */
    public static Date string2Date(String formatDate) {
        return string2Date(formatDate, DEFAULT_SDF);
    }

    /**
     * 将时间字符串转为Date类型
     * <p>格式为用户自定义
     */
    public static Date string2Date(String formatDate, SimpleDateFormat format) {
        return new Date(string2Milliseconds(formatDate, format));
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss
     */
    public static String date2String(Date date) {
        return date2String(date, DEFAULT_SDF);
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为用户自定义
     */
    public static String date2String(Date date, SimpleDateFormat format) {
        return format.format(date);
    }

    /**
     * 将时间戳转为Date类型
     */
    public static Date milliseconds2Date(long milliseconds) {
        return new Date(milliseconds);
    }

    /**
     * 获取两个时间差（单位：unit）
     * <pre>
     * UNIT_MSEC:毫秒
     * UNIT_SEC :秒
     * UNIT_MIN :分
     * UNIT_HOUR:小时
     * UNIT_DAY :天
     * </pre>
     * <p>time1和time2格式都为yyyy-MM-dd HH:mm:ss
     */
    public static long getIntervalTime(String time1, String time2, int unit) {
        return getIntervalTime(time1, time2, unit, DEFAULT_SDF);
    }

    /**
     * 获取两个时间差（单位：unit）
     * <pre>
     * UNIT_MSEC:毫秒
     * UNIT_SEC :秒
     * UNIT_MIN :分
     * UNIT_HOUR:小时
     * UNIT_DAY :天
     * </pre>
     * <p>time1和time2格式都为format
     */
    public static long getIntervalTime(String time1, String time2, int unit, SimpleDateFormat format) {
        return milliseconds2Unit(string2Milliseconds(time1, format)
                - string2Milliseconds(time2, format), unit);
    }

    /**
     * 毫秒时间戳单位转换（单位：unit）
     * <pre>
     * UNIT_MSEC:毫秒
     * UNIT_SEC :秒
     * UNIT_MIN :分
     * UNIT_HOUR:小时
     * UNIT_DAY :天
     * </pre>
     */
    private static long milliseconds2Unit(long milliseconds, int unit) {
        switch (unit) {
            case UNIT_MSEC:
                return Math.abs(milliseconds) / unit;
            case UNIT_SEC:
                return Math.abs(milliseconds) / unit;
            case UNIT_MIN:
                return Math.abs(milliseconds) / unit;
            case UNIT_HOUR:
                return Math.abs(milliseconds) / unit;
            case UNIT_DAY:
                return Math.abs(milliseconds) / unit;
        }
        return -1;
    }

    /**
     * 获取当前时间
     * <p>格式为用户自定义
     */
    public static String getCurTimeString(SimpleDateFormat format) {
        return milliseconds2String(getCurTimeMills(), format);
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为用户自定义
     */
    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }

    /**
     * 获取当前时间
     * <p>单位（毫秒）
     */
    public static long getCurTimeMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <pre>
     * UNIT_MSEC:毫秒
     * UNIT_SEC :秒
     * UNIT_MIN :分
     * UNIT_HOUR:小时
     * UNIT_DAY :天
     * <p>time1和time2格式都为yyyy-MM-dd HH:mm:ss
     */
    public static long getIntervalByNow(String time, int unit) {
        return getIntervalByNow(time, unit, DEFAULT_SDF);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <pre>
     * UNIT_MSEC:毫秒
     * UNIT_SEC :秒
     * UNIT_MIN :分
     * UNIT_HOUR:小时
     * UNIT_DAY :天
     * <p>time1和time2格式都为format
     */
    public static long getIntervalByNow(String time, int unit, SimpleDateFormat format) {
        return getIntervalTime(getCurTimeString(), time, unit, format);
    }

    /**
     * 获取当前时间
     * <p>格式为yyyy-MM-dd HH:mm:ss
     */
    public static String getCurTimeString() {
        return milliseconds2String(getCurTimeMills());
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss
     */
    public static String milliseconds2String(long milliseconds) {
        return milliseconds2String(milliseconds, DEFAULT_SDF);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <pre>
     * UNIT_MSEC:毫秒
     * UNIT_SEC :秒
     * UNIT_MIN :分
     * UNIT_HOUR:小时
     * UNIT_DAY :天
     * <p>time1和time2格式都为format
     */
    public static long getIntervalByNow(Date time, int unit) {
        return getIntervalTime(getCurTimeDate(), time, unit);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <pre>
     * UNIT_MSEC:毫秒
     * UNIT_SEC :秒
     * UNIT_MIN :分
     * UNIT_HOUR:小时
     * UNIT_DAY :天
     * <p>time1和time2格式都为format
     */
    public static long getIntervalByNow(long time, int unit) {
        return milliseconds2Unit(time - getCurTimeMills(), unit);
    }

    /**
     * 获取两个时间差（单位：unit）
     * <pre>
     * UNIT_MSEC:毫秒
     * UNIT_SEC :秒
     * UNIT_MIN :分
     * UNIT_HOUR:小时
     * UNIT_DAY :天
     * </pre>
     * <p>time1和time2都为Date
     */
    public static long getIntervalTime(Date time1, Date time2, int unit) {
        return milliseconds2Unit(date2Milliseconds(time2) - date2Milliseconds(time1), unit);
    }


    /**
     * 获取当前时间
     * <p>Date类型
     */
    public static Date getCurTimeDate() {
        return new Date();
    }

    /**
     * 将Date类型转为时间戳
     */
    public static long date2Milliseconds(Date date) {
        return date.getTime();
    }

    /**
     * 判断闰年
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 获得时间的小时值
     */
    public static int timeStamp2HH(long timeStamp) {
        if (timeStamp <= 0)
            timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.CHINA);
        String format = sdf.format(timeStamp);
        Integer integer = Integer.valueOf(format);
        return integer;
    }

    /**
     * 获得时间的分钟值
     */
    public static int timeStamp2mm(long timeStamp) {
        if (timeStamp <= 0)
            timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("mm", Locale.CHINA);
        String format = sdf.format(timeStamp);
        Integer integer = Integer.valueOf(format);
        return integer;
    }

    /**
     * 时间戳转时间(时分秒   )
     * 显示HH:mm:ss
     */
    public static String time2HHmm(long timeStamp) {
        if (timeStamp <= 0)
            timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(timeStamp);
    }


    /**
     * 根据时间获取星期
     */
    public static String timeStamp2WeekString(long timeStamp) {
        Date date = new Date(timeStamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * 根据时间获取月份
     */
    public static String timeStamp2MonthString(long timeStamp) {
        if (timeStamp <= 0)
            timeStamp = System.currentTimeMillis();
        if (isSameMonth(timeStamp)) {
            return "本";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.CHINA);
            return sdf.format(timeStamp);
        }
    }

    /**
     * 根据时间判断显示日期还是分钟前
     */
    public static String timeStamp2SimpleString(long timeStamp) {
        long delta = getCurTimeMills() - timeStamp;
        if (isSameYear(timeStamp)) {
            if (isSameDay(timeStamp)) {
                if (delta < UNIT_MIN) {
                    long seconds = toSeconds(delta);
                    return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
                }
                if (delta < UNIT_HOUR) {
                    long minutes = toMinutes(delta);
                    return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
                }
                if (delta < UNIT_DAY) {
                    long hours = toHours(delta);
                    return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
                }
            } else {
                if (delta < 2L * UNIT_DAY) {
                    return YESTERDAY;
                }
                if (2L * UNIT_DAY < delta && delta < 3L * UNIT_DAY) {
                    return THE_DAY_YESTERDAY;
                }
                return timeStamp2DayString(timeStamp);

            }

        }
        return timeStamp2DateString(timeStamp);


    }

    public static boolean isSameYear(long timeStamp) {
        Date date = milliseconds2Date(timeStamp);
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        current.setTime(date);
        today.setTime(getCurTimeDate());
        return current.get(Calendar.YEAR) == today.get(Calendar.YEAR);
    }

    public static boolean isSameDay(long timeStamp) {
        Date date = milliseconds2Date(timeStamp);
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        current.setTime(date);
        today.setTime(getCurTimeDate());
        return current.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isSameMonth(long timeStamp) {
        Date date = milliseconds2Date(timeStamp);
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        current.setTime(date);
        today.setTime(getCurTimeDate());
        return current.get(Calendar.MONTH) == today.get(Calendar.MONTH);
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    /**
     * 只获取到日
     * 显示yyyy-MM-dd
     */
    public static String timeStamp2DateString(long timeStamp) {
        if (timeStamp <= 0)
            timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(timeStamp);
    }

    public static String timeStamp2DayString(long timeStamp) {
        if (timeStamp <= 0)
            timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.CHINA);
        return sdf.format(timeStamp);
    }


}
