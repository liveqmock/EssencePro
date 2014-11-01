package com.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.model.ConfigModel;

public class CmUtil {

    private static long pkId = ClassUtil.obj2long(ConfigModel.MARK.getProperties("pkId"));

    private static Lock lock = new ReentrantLock();

    public synchronized static long getPkId() {
        lock.lock();
        try {
            long lTmp = System.currentTimeMillis();
            if (pkId < lTmp) {
                pkId = lTmp;
            } else {
                pkId++;
            }
            ConfigModel.MARK.writeProperty("pkId", pkId + "");
            return pkId;
        } finally {
            lock.unlock();
        }
    }

    synchronized public static long getPkId(String YYYY_MM_DD) {
        if (YYYY_MM_DD == null) {
            return getPkId();
        }
        long lTmp = System.currentTimeMillis();
        if (getYYYY_MM_DD(new Timestamp(lTmp)).equals(YYYY_MM_DD)) {
            return getPkId();
        }
        if (pkId < lTmp) {
            pkId = lTmp;
        } else {
            pkId++;
        }
        return (pkId + 8 * 60 * 60 * 1000) % (24 * 60 * 60 * 1000) + getTimeStamp(YYYY_MM_DD).getTime();
    }

    public static String getYYYY_MM_DD(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(timestamp);
        int year = g.get(Calendar.YEAR);
        int month = g.get(Calendar.MONTH) + 1;
        int day = g.get(Calendar.DAY_OF_MONTH);
        return year + "-" + (month < 10 ? "0" + month : month + "") + "-" + (day < 10 ? "0" + day : day + "");
    }

    public static String getPreRqByDays(String rqStart, int days) {
        if (rqStart == null || "".equals(rqStart)) {
            return null;
        }
        String rq_str = "";
        try {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");

            long timeLong = (format.parse(rqStart)).getTime();
            int temp = 0;
            if (days <= 24) {
                rq_str = format.format(new java.util.Date(timeLong - 3600 * 24 * 1000 * days));
            } else {
                temp = days - 24;
                rq_str = getPreRqByDays(rqStart, 24);
                rq_str = getPreRqByDays(rq_str, temp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rq_str;
    }

    public static Timestamp getTimeStamp(String YYYY_MM_DD) {
        int year = 1900, month = 0, day = 1;
        try {
            year = Integer.parseInt(YYYY_MM_DD.substring(0, 4));
        } catch (Exception e) {
        }
        try {
            month = Integer.parseInt(YYYY_MM_DD.substring(5, 7)) - 1;
        } catch (Exception e) {
        }
        try {
            day = Integer.parseInt(YYYY_MM_DD.substring(8, 10));
        } catch (Exception e) {
        }
        GregorianCalendar g = new GregorianCalendar(year, month, day);
        return new Timestamp(g.getTime().getTime());
    }

    public static String getToday() {
        GregorianCalendar g = new GregorianCalendar();
        Timestamp today = new Timestamp(g.getTime().getTime());
        return today.toString().substring(0, 10);
    }

}
