package com.hkzr.wlwd.ui.utils;

import android.annotation.SuppressLint;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description 时间格式化工具类
 * @Date 2014-10-30 上午10:48:33
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public final static String FORMAT_YEAR = "yyyy";
    public final static String FORMAT_MONTH_DAY = "MM月dd日";
    public final static String FORMAT_YEAR_MONTH_DAY = "yyyy年MM月dd日";

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_TIME2 = "HH:mm:ss";

    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日  hh:mm";

    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm";
    public final static String FORMAT_DATE2_TIME = "yyyy-MM-dd HH:mm:ss";

    public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";
    public final static String FORMAT_DATE2 = "yyyy.MM.dd";

    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 日
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟


    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTime(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;
        if (timestamp == 0f) {
            timeGap = 49;
        }
        String timeStr = null;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {
            timeStr = timeGap / MINUTE + "分钟前";
        } else {
            timeStr = "刚刚";
        }
        return timeStr;
    }

    public static String getSigninData() {
        Time t = new Time();
        t.setToNow();
        int lastmonth = t.month + 1;
        final String str = t.year + "年" + lastmonth + "月" + t.monthDay + "日";
        return null;
    }


    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    /**
     * 获取当前系统时间戳
     */
    public static String getTimestamp() {
        long currentTime = System.currentTimeMillis();
//        long timeGetTime = new Date().getTime();
//        long timeSeconds = System.currentTimeMillis();
//        long timeMillis = Calendar.getInstance().getTimeInMillis();
        return currentTime + "";
    }

    /**
     * Date类型转换为String类型
     *
     * @param data       Date类型的时间
     * @param formatType 要转换的string类型的时间格式
     * @return String
     */
    public static String date2String(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /**
     * String类型转换为String类型
     *
     * @param data       String
     * @param formatType 要转换的string类型的时间格式
     * @return String
     */
    public static String String2String(String data, String formatType) {
        return new SimpleDateFormat(formatType).format(new Date(data));
    }

    /**
     * long类型转换为String类型
     *
     * @param currentTime 要转换的long类型的时间
     * @param formatType  要转换的string类型的时间格式
     * @return String
     */
    public static String long2String(long currentTime, String formatType) {
        String strTime = "";
        Date date = long2Date(currentTime, formatType);// long类型转成Date类型
        strTime = date2String(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * String类型转换为Date类型
     *
     * @param strTime    要转换的String类型的时间
     * @param formatType 要转换的格式
     * @return Date类型的时间
     */
    public static Date string2Date(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;

        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String showString2Date(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        String showTime = null;
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        try {
            date = formatter.parse(strTime);
            android.util.Log.e("histime", strTime + "=======" + date.getDate() + "+++" + date.getHours() + "+++" + date.getMinutes()
                    + "??????" + day + "===" + hour + "???/" + minute);
            if (date.getDate() < day) {
                showTime = day - date.getDate() + "天前";
            } else {
                if (date.getHours() < hour) {
                    showTime = hour - date.getHours() + "小时前";
                } else {
                    showTime = minute - date.getMinutes() + "分钟前";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return showTime;
    }


    public String parseTime2(String time) {
        String formattedTime = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // SimpleDateFormat的parse(String time)方法将String转换为Date
            Date date = simpleDateFormat.parse(time);
            android.util.Log.e("myTime", time + "==" + date.getMinutes() + "==" + date.getHours() + "==" + date.getDay() + "==");
            simpleDateFormat = new SimpleDateFormat("MM-dd");
            // SimpleDateFormat的format(Date date)方法将Date转换为String
            formattedTime = simpleDateFormat.format(date);
            return formattedTime;
        } catch (Exception e) {

        }
        return formattedTime;

    }

    /**
     * long转换为Date类型
     *
     * @param currentTime
     * @param formatType
     * @return
     */
    public static Date long2Date(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = date2String(dateOld, formatType); // 把date类型的时间转换为string
        Date date = string2Date(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    /**
     * string类型转换为long类型
     *
     * @param strTime
     * @param formatType
     * @return
     */
    public static long string2Long(String strTime, String formatType) {
        Date date = string2Date(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = date2Long(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * date类型转换为long类型
     *
     * @param date
     * @return
     */
    public static long date2Long(Date date) {
        return date.getTime();
    }

    /**
     * 获取yy-MM-dd HH:mm:ss格式的时间字符串
     *
     * @return
     */
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 将年月日分开
     *
     * @param time
     * @return
     */
    public static int[] getYearMonthTime(String time) {
        int[] t = new int[3];
        int first = time.indexOf("-");
        int last = time.lastIndexOf("-");
        t[0] = Integer.parseInt(time.substring(0, first));
        t[1] = Integer.parseInt(time.substring(first + 1, last));
        t[2] = Integer.parseInt(time.substring(last + 1, time.length()));
        return t;
    }

    /**
     * 获取yy-MM-dd格式的日期字符串
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        return format.format(new Date());
    }

    /**
     * 获取yyyy年MM月dd日格式的日期字符串
     *
     * @return
     */
    public static String getDate2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(new Date());
    }


    /**
     * 日期逻辑
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static String timeLogic(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        Date date = string2Date(dateStr, FORMAT_DATE_TIME);
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long time = (now - past) / 1000;

        StringBuffer sb = new StringBuffer();
        if (time > 0 && time < 60) { // 1小时内
            return sb.append(time + "秒前").toString();
        } else if (time > 60 && time < 3600) {
            return sb.append(time / 60 + "分钟前").toString();
        } else if (time >= 3600 && time < 3600 * 24) {
            return sb.append(time / 3600 + "小时前").toString();
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            return sb.append("昨天").toString()
                    + TimeUtil.date2String(date, FORMAT_TIME);
        } else if (time >= 3600 * 48 && time < 3600 * 72) {
            return sb.append("前天").toString()
                    + TimeUtil.date2String(date, FORMAT_TIME);
        } else if (time >= 3600 * 72) {
            return TimeUtil.date2String(date, FORMAT_DATE_TIME);
        }
        return TimeUtil.date2String(date, FORMAT_DATE_TIME);
    }

    /**
     * 把毫秒转化成日期
     *
     * @param dateFormat(日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @param millSec(毫秒数)
     * @return
     */
    public static String transferLongToDate(Long millSec, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }


    public static String getweek(String data) {

        Calendar c = Calendar.getInstance(java.util.Locale.CHINA);
        String[] sp = null;
        if (data.contains(" ") || data.contains(":")) {
            sp = data.split(" ")[0].split("-");
        } else {
            sp = data.split("-");
        }
        c.set(Calendar.YEAR, Integer.parseInt(sp[0]));
        c.set(Calendar.MONTH, Integer.parseInt(sp[1]) - 1);
        c.set(Calendar.DATE, Integer.parseInt(sp[2]));

        int wd = c.get(Calendar.DAY_OF_WEEK);
        String x = "";
        switch (wd) {
            case 1:
                x = "日";
                break;
            case 2:
                x = "一";
                break;
            case 3:
                x = "二";
                break;
            case 4:
                x = "三";
                break;
            case 5:
                x = "四";
                break;
            case 6:
                x = "五";
                break;
            case 7:
                x = "六";
                break;
        }
        return x;
    }

}