/*
 * Copyright (c) 2013 zljysoft. 中联佳裕信息技术（北京）有限公司 版权所有 
 * http://www.zljysoft.com
 * File Name：DateUtil.java
 * Comments: 日期操作工具类
 * Author: 
 * Create Date:2013-4-8 上午11:13:29 
 * Modified By: 
 * Modified Date: 
 * Why & What is modified: 
 * version: V1.0 
 */
package com.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

import org.apache.log4j.Logger;

/**
 * 日期操作工具类
 * 
 * @date 2013-4-8 上午11:13:29
 * @version V1.0
 */

public class DateUtil {

    private static final Logger log = LoggerUtil.getLogger();

    /** 日期格式 yyyy-MM-dd */
    public static final String DateFormat1 = "yyyy-MM-dd";
    public static final String DateFormat2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DateFormat3 = "yyyy-MM-dd HH:mm";

    private final static SimpleDateFormat IE_GMT_SDF = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.UK);
    private final static SimpleDateFormat GMT_SDF = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z", Locale.UK);
    static {
        IE_GMT_SDF.setTimeZone(new SimpleTimeZone(0, "GMT"));
        GMT_SDF.setTimeZone(new SimpleTimeZone(0, "GMT"));
    }

    /**
     * 将给定的日期按照 yyyy-MM-dd 格式化后返回字符串
     * 
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        if (null == date) {
            return "";
        }
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String alldateToString(Date date) {
        if (null == date) {
            return "";
        }
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * 将给定的日期按照 pattern 格式化后返回字符串
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if (null == date) {
            return "";
        }
        Format formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    /**
     * 取得当前服务器的时间 按照yyyy-MM-dd 格式化后返回字符串
     * 
     * @return
     */
    public static String getCurrentDate() {
        return dateToString(new Date());
    }

    /**
     * 取得当前服务器的时间 按照yyyy-MM-dd 格式化后返回字符串
     * 
     * @return
     */
    public static String getCurrentDateAndTime() {
        return dateToString(new Date(), "yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 计算并返回给定年月的最后一天
     */
    public static String lastDayOfMonth(int year, int month) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR, year);
        gc.set(Calendar.MONTH, month - 1);
        int maxDate = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        gc.set(Calendar.DATE, maxDate);
        return dateToString(gc.getTime());
    }

    /**
     * 计算并返回日期中的星期几
     */

    public static int weekOfDate(java.util.Date d1) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d1);
        return gc.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 字符串转日期
     * 
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date stringToDate(String dateStr, String formatStr) {
        DateFormat df = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 时间戳字符串转日期
     * 
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static String timestampToDateStr(String dateStr, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(new Date(Long.parseLong(dateStr)));
    }

    /**
     * 
     * @author : 玄承勇
     * @datetime: 2014-5-1 下午9:01:40
     */
    public static Date timestampToDate(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 格式化日期，如果不是两位，拼‘0’
     */
    public static String getTwoDate(int rq) {
        String temp = "" + rq;
        if (rq > 0 && rq < 10)
            temp = "0" + rq;
        return temp;
    }

    /**
     * 计算并返回两个日期之间的秒数 2006年1月1日
     */
    public static int subSecond(java.util.Date d1, java.util.Date d2) {

        long mss = d2.getTime() - d1.getTime();
        long ss = mss / 1000;

        return (int) ss;
    }

    /**
     * 计算并返回两个日期之间的天数
     */

    public static int subDate(java.util.Date d1, java.util.Date d2) {
        long mss = d2.getTime() - d1.getTime();
        long ss = mss / 1000;
        long ms = ss / 60;
        long hs = ms / 60;
        long ds = hs / 24;
        return (int) ds;
    }

    /**
     * 计算并返回两个日期之间的分钟数
     */

    public static int subDateMine(java.util.Date d1, java.util.Date d2) {
        long mss = d2.getTime() - d1.getTime();
        long ss = mss / 1000;
        long ms = ss / 60;
        long hs = ms / 60;
        long ds = hs / 24;
        return (int) ms;
    }

    // 从服务器上取得当前日期
    // 格式：2002年04月25日 星期四
    public static String get_current_date() {
        String[] week = new String[7];
        week[0] = "日";
        week[1] = "一";
        week[2] = "二";
        week[3] = "三";
        week[4] = "四";
        week[5] = "五";
        week[6] = "六";
        java.util.Date d1 = new java.util.Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d1);
        return gc.get(Calendar.YEAR) + "年" + (gc.get(Calendar.MONTH) + 1) + "月" + gc.get(Calendar.DAY_OF_MONTH) + "日 星期"
                + week[gc.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY];
    }

    // 获取当前的时间 HH:mm——几点几分。
    public static String stringOfTime() {
        return stringOfTime(new java.util.Date());
    }

    // 把Date类型转换为时间字符串，格式：HH:mm
    public static String stringOfTime(java.util.Date date) {
        Format formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    /**
     * 当前日期字符串,获取当前的时间格式为yyyy-MM-dd HH:mm:ss
     */
    public static String stringOfDateTime() {
        return stringOfDateTime(new java.util.Date());
    }

    // 把date转换为日期字符串。格式为yyyy-MM-dd HH:mm:ss
    public static String stringOfDateTime(java.util.Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    public static String stringDateTime() {
        return stringDateTime(new java.util.Date());
    }

    public static String stringDateTime(java.util.Date date) {
        Format formatter = new SimpleDateFormat("HHmmss");
        return formatter.format(date);
    }

    /**
     * 当前日期字符串，格式为：yyyy年M月d日 H时m分s秒
     */

    public static String stringOfCnDateTime() {
        return stringOfCnDateTime(new java.util.Date());
    }

    public static String stringOfCnDateTime(java.util.Date date) {
        Format formatter = new SimpleDateFormat("yyyy年M月d日 H时m分s秒");
        return formatter.format(date);
    }

    /**
     * 当前日期字符串
     */
    public static String stringOfCnDate() {
        return stringOfCnDate(new java.util.Date());
    }

    public static String stringOfCnDate(java.util.Date date) {
        Format formatter = new SimpleDateFormat("yyyy年M月d日");
        return formatter.format(date);
    }

    /**
     * 当前日期字符串yyyy-MM-dd
     */
    public static String stringOfDate() {
        return stringOfDate(new java.util.Date());
    }

    public static String stringOfDate(java.util.Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 当前日期字符串yyyy-M-d
     */
    public static String stringOfDate2() {
        return stringOfDate2(new java.util.Date());
    }

    public static String stringOfDate2(java.util.Date date) {
        Format formatter = new SimpleDateFormat("yyyy-M-d");
        return formatter.format(date);
    }

    /**
     * 计算并返回给定年月的最后一天
     */
    public static String lastDateOfMonth(int year, int month) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR, year);
        gc.set(Calendar.MONTH, month - 1);
        int maxDate = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        gc.set(Calendar.DATE, maxDate);
        return stringOfDate(gc.getTime());
    }

    // 将日期字符串转换为日期变量,如果有问题,返回当前日期
    public static java.util.Date stringToDateTime(String str) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return (java.util.Date) formatter.parse(str);
        } catch (ParseException e) {
            return new java.util.Date();
        }
    }

    public static String amorPmTime() {

        return amorPmTime(new java.util.Date());
    }

    public static String amorPmTime(java.util.Date d1) {

        SimpleDateFormat formatter2 = new SimpleDateFormat(" a hh:mm");
        return formatter2.format(d1);
    }

    /**
     * 返回日期中的星期几
     */
    public static String weekOfDate() {
        String[] week = new String[7];
        week[0] = "日";
        week[1] = "一";
        week[2] = "二";
        week[3] = "三";
        week[4] = "四";
        week[5] = "五";
        week[6] = "六";
        return week[weekOfDate(new java.util.Date())];
    }

    /**
     * 计算并返回日期中的日
     */
    public static int dayOfDate(java.util.Date d1) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d1);
        return gc.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 计算并返回日期中的月
     */
    public static int monthOfDate(java.util.Date d1) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d1);
        return gc.get(Calendar.MONTH) + 1;
    }

    /**
     * 计算并返回日期中的年
     */
    public static int yearOfDate(java.util.Date d1) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d1);
        return gc.get(Calendar.YEAR);
    }

    /**
     * 计算并返回日期中的时
     */
    public static int hourOfDate(java.util.Date d1) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d1);
        return gc.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 计算并返回日期中的分
     */
    public static int minuteOfDate(java.util.Date d1) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d1);
        return gc.get(Calendar.MINUTE);
    }

    /**
     * 计算并返回日期中的秒
     */
    public static int secondOfDate(java.util.Date d1) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d1);
        return gc.get(Calendar.SECOND);
    }

    /**
     * 计算数月后的日期
     */
    public static java.util.Date addDateByMonth(java.util.Date d, int mcount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.MONTH, mcount);
        gc.add(Calendar.DATE, -1);
        return new java.util.Date(gc.getTime().getTime());
    }

    /**
     * 计算数日后的日期
     */
    public static java.util.Date addDateByDay(java.util.Date d, int dcount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.DATE, dcount);
        return new java.util.Date(gc.getTime().getTime());
    }

    public static java.util.Date addDateByMinute(java.util.Date d, int mcount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.MINUTE, mcount);
        return gc.getTime();
    }

    /**
     * 计算数秒后的日期
     */
    public static java.util.Date addDateBySecond(java.util.Date d, int scount) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.SECOND, scount);
        return gc.getTime();
    }

    /**
     * zhurx 20040224 输入的字符转换为时间类型
     */
    public static java.sql.Date isTime(String shijian) {
        java.sql.Date time = null;
        try {
            time = java.sql.Date.valueOf(shijian);
            return time;
        } catch (IllegalArgumentException myException) {
            return time;
        }
    }

    /**
     * 20040306 输入的字符为yyyy-MM-dd HH:mm:ss类型 转换为：java.util.Date
     */

    public static java.util.Date isDateTime(String datestr) {
        java.util.Date rdatetime = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            rdatetime = formatter.parse(datestr, pos);
            return rdatetime;
        } catch (IllegalArgumentException myException) {
            return rdatetime;
        }
    }

    public static java.util.Date getDate() {
        return new java.util.Date();
    }

    /**
     * 取diff天之前或之后的日期
     * 
     * @param diff
     *            时间跨度
     * @param aorb
     *            "a"之前，"b"之后
     * @return
     */
    public static String dateOfSomeDayDiff(int diff, String aorb) {
        String dd = "";
        Date date = null;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        long day = (long) diff * 24 * 60 * 60 * 1000;
        long current = System.currentTimeMillis();
        if ("a".equalsIgnoreCase(aorb))
            date = new Date(current + day);
        if ("b".equalsIgnoreCase(aorb))
            date = new Date(current - day);
        return dd = ft.format(date);
    }

    // 日期差的天数
    public static long getQuot(String time1, String time2) {
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date1.getTime() - date2.getTime();
            quot = quot / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return quot;
    }

    public static int date2int(String dateStr) {
        int dateInt;
        try {
            dateInt = Integer.parseInt(dateStr.replace("-", ""));
        } catch (Exception e) {
            log.info("日期转换出错：'" + dateStr + "'");
            dateInt = 0;
        }
        return dateInt;
    }

    public static long date2long(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            log.info("日期转换出错：'" + dateStr + "'");
            return 0;
        }
    }

    /**
     * 
     * @description : 获取从startY年开始，往后的len年组成是数组Year
     * @author : 玄承勇
     * @datetime: 2014-5-2 上午10:15:35
     * @param:
     * @param:
     * @return:String[]
     */
    public static String[] getYearList(int startY, int len) {
        String[] list = new String[len];
        for (int i = startY; i < len + startY; i++)
            list[i - startY] = "" + (i + 1);
        return list;
    }

    /**
     * 
     * @description : 获取从当前月开始，往后的len月组成是数组月
     * @author : 玄承勇
     * @datetime: 2014-5-2 上午10:18:33
     * @param:
     * @param:
     * @return ： String[]
     */
    public static String[] getMonDayList(int len) {
        String[] list = new String[len];
        for (int i = 0; i < len; i++) {
            if (i < 9)
                list[i] = "0" + (1 + i);
            else
                list[i] = "" + (1 + i);
        }
        return list;
    }

    public static String getNowIeGMT(Date date) {
        synchronized (IE_GMT_SDF) {
            return IE_GMT_SDF.format(date);
        }
    }

    public static Date gmt2Date(String gmtDate) {
        try {
            if (gmtDate.matches("\\d+.*")) {
                synchronized (GMT_SDF) {
                    return GMT_SDF.parse(gmtDate);
                }
            } else if (gmtDate.matches("\\w+.*")) {
                synchronized (IE_GMT_SDF) {
                    return IE_GMT_SDF.parse(gmtDate);
                }
            }
        } catch (ParseException e) {
            log.error("dateStr:'" + gmtDate + "'", e);
        }
        return null;
    }

    public static String getNowDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getDateString(Date date) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return dateStr;
    }

    public static String getNowDateTime(String formatStr) {
        return new SimpleDateFormat(formatStr).format(new Date());
    }

    public static String getNowDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getDateTimeString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String getDateStr(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getMonthFirstDate() {
        return new SimpleDateFormat("yyyy-MM-01 00:00:00").format(new Date());
    }

    public static Date str2date(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            log.warn("日期转换错误，dateStr‘" + dateStr + "’，" + e.getMessage());
            return null;
        }
    }

    public static Date str2dateTime(String dateStr) {
        return str2dateTime(dateStr, null);
    }

    public static Date str2dateTime(String dateStr, Date defaultDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        } catch (Exception e) {
            log.warn("日期转换错误，dateStr‘" + dateStr + "’，" + e.getMessage());
            return defaultDate;
        }
    }

    public static Date getLongStringToDate(String dateStr) throws ParseException {
        Date retDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        return retDate;
    }

    public static boolean isNextDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().equals("")) {
            return false;
        }
        Date retDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        return retDate.getTime() > new Date().getTime();
    }

    public static Date addDate(Date date, int addNum, int addUnit) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(addUnit, addNum);
        return c.getTime();
    }

    public static Timestamp getLongStringToTimestamp(String dateStr) {
        Timestamp time = Timestamp.valueOf(dateStr);
        return time;
    }

    public static final boolean isSameMonth(Date early, Date late) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        return calst.get(Calendar.MONTH) == caled.get(Calendar.MONTH);
    }

    public static String getDh(long id) {
        String dh = "";
        String dh1 = id + "";
        if (dh1.length() == 13) {
            dh = dh1.substring(2, 5) + "-" + dh1.substring(5, 9) + "-" + dh1.substring(9, 13);
        } else {
            dh = dh1;
        }
        return dh;
    }

    public static final int daysBetween(Date early, Date late) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        diffDates(calst, caled, early, late);
        // 得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
        return days;
    }

    // 得到两个日期相差的小时数
    public static final int hoursBetween(Date early, Date late) {
        return minutesBetween(early, late) / 60;
    }

    // 得到两个日期相差的分钟数
    public static final int minutesBetween(Date early, Date late) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        int minutes = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 60;
        return minutes;
    }

    // 得到两个日期相差的毫秒数
    public static final long timeBetween(Date early, Date late) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        return caled.getTime().getTime() - calst.getTime().getTime();
    }

    private static void diffDates(Calendar calst, Calendar caled, Date early, Date late) {
        calst.setTime(early);
        caled.setTime(late);
        // 设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
    }

    /**
     * 将毫秒数转换为时间表示1000ms==》1s 格式：12天20小时38分钟31秒111毫秒
     * 
     * @Title formatMill
     * @author 玄承勇
     * @date 2013-12-17 下午2:29:03
     * @param time
     * @return String
     */
    public static String millFormat(double time) {
        String[] sizearr = { "毫秒", "秒", "分钟", "小时", "天" };
        int[] steps = { 1000, 60, 60, 24, 30 };
        String result = "";
        double sized = (double) time;
        for (int i = 0; i < steps.length; i++) {
            double rem = sized % steps[i];
            double value = sized / steps[i];
            if (rem != 0 || time == 0) {
                result = (int) rem + sizearr[i] + result;
                if (value > 1) {
                    sized /= steps[i];
                } else {
                    break;
                }
            } else {
                if (value >= steps[i + 1]) {
                    sized /= steps[i];
                } else {
                    result = (int) value + sizearr[i + 1] + result;
                    break;
                }
            }
        }
        return result;
    }

}
