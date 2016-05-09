package com.baosaas.supervise.util;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间类
 *
 * @Author Ellrien
 * @Date 2015年7月6日下午1:07:41
 * @Version 1.0
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    public static String MS_TO_TIME1 = "dd天HH时mm分ss秒";
    public static String MS_TO_TIME2 = "HH时mm分ss秒";
    public static String MS_TO_TIME3 = "mm分ss秒";
    public static String DATE_FORMAT_STR1 = "yyyy-MM-dd HH:mm:dialog_enter";
    public static String DATE_FORMAT_STR2 = "MM.dd";
    public static String DATE_FORMAT_STR3 = "yyyyMMdd";
    public static String DATE_FORMAT_STR4 = "yyyy/MM/dd HH:mm:dialog_enter";
    public static String DATE_FORMAT_STR5 = "yyyy.MM.dd";
    public static String DATE_FORMAT_STR6 = "yyyy-MM-dd";
    public static String DATE_FORMAT_STR7 = "MM.dd HH:mm:dialog_enter";
    public static String DATE_FORMAT_STR8 = "MM/dd HH:mm:dialog_enter";
    public static String DATE_FORMAT_STR9 = "MM/dd";
    public static String DATE_FORMAT_STR10 = "yyyy/MM/dd";
    public static String DATE_FORMAT_STR11 = "yyyyMMddHHmmss";


    public static String getNewTime() {
        Calendar cal = Calendar.getInstance();
        return (String) DateFormat.format(DATE_FORMAT_STR1, cal);
    }

    public static String getNewTimeStr() {
        Calendar cal = Calendar.getInstance();
        return (String) DateFormat.format(DATE_FORMAT_STR11, cal);
    }

    public static String msToTime(long ms) {
        SimpleDateFormat formatter;
        if (ms >= 24 * 3600 * 1000) {
            ms = ms - 24 * 3600 * 1000;
            formatter = new SimpleDateFormat(MS_TO_TIME1);
        } else if (ms >= 3600 * 1000) {
            formatter = new SimpleDateFormat(MS_TO_TIME2);
        } else {
            formatter = new SimpleDateFormat(MS_TO_TIME3);
        }

        long s = TimeZone.getDefault().getRawOffset();
        String hms = formatter.format(ms - s * 1000);
        return hms;
    }

    public static String parseTime(String dateStr, String format1, String format2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format1);
        Date date = sdf.parse(dateStr);
        SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
        String datestr2 = sdf2.format(date);
        return datestr2;
    }

    public static Date parseTime(String dateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);
    }

    /**
     * 时间戳(毫秒)----日期转为毫秒
     *
     * @return
     */

    public static long timeStampMS(String date) {
        long timePoor = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parseEnd = df.parse(date);
            Date parseStart = df.parse("1970-01-01");
            timePoor = parseEnd.getTime() - parseStart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timePoor;
    }

    public static long timeMMddStampMS(String date) {
        long timePoor = 0;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        try {
            Date parseEnd = df.parse(date);
            Date parseStart = df.parse("1970-01-01");
            timePoor = parseEnd.getTime() - parseStart.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timePoor;
    }

    /**
     * 时间戳(秒)----日期转为秒
     *
     * @return
     */

    public static long timeStampS(String date) {
        long timePoor = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parseEnd = df.parse(date);
            Date parseStart = df.parse("1970-01-01");
            timePoor = parseEnd.getTime() - parseStart.getTime();
            timePoor = timePoor / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timePoor;
    }

    /**
     * 时间戳(毫秒)----毫秒转年月日
     *
     * @param time
     * @return Date类型
     */
    public static Date MSStampyyMMddDate(long time) {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time);
        String longStr = df.format(aLong);
        try {
            date = df.parse(longStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间戳(秒)----秒转年月日
     *
     * @param time
     * @return Date类型
     */
    public static Date SStampyyMMddDate(long time) {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time / 1000);
        String longStr = df.format(aLong);
        try {
            date = df.parse(longStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间戳(毫秒)----毫秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String MSStampyyMMddStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(毫秒)----毫秒转月日
     *
     * @param time
     * @return String类型
     */
    public static String MSStampMMddStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(秒)----秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String SStampyyMMddStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time / 1000);
        String longStr = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(毫秒)----毫秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String MSStampyMdHmmStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:dialog_enter");
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(毫秒)----毫秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String MSStampyMdHmmStrYY(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Long aLong = new Long(time);
        date = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(秒)----秒转年月日
     *
     * @param time
     * @return String类型
     */
    public static String SStampyMdHmmStr(long time) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:dialog_enter");
        Long aLong = new Long(time / 1000);
        String longStr = df.format(aLong);
        return date;
    }

    /**
     * 时间戳(秒)----转年月日时分秒
     *
     * @param time
     * @return String类型
     */
    public static String SSmmHH(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:dialog_enter");
        return formatter.format(time);
    }

}