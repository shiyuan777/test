package com.jixunkeji.utils.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 *
 * @author Administrator
 */
public class DateUtil extends cn.hutool.core.date.DateUtil {


    private static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");


    public DateUtil() {
    }

    private static class DateUtilHolder {
        static DateUtil instance = new DateUtil();
    }

    public static DateUtil getInstance() {
        return DateUtilHolder.instance;
    }

    private static Date getDate(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static Date addHours(Date date, int hours) {
        return getDate(date, Calendar.HOUR, hours);
    }

    public static Date addHours(int hours) {
        return getDate(null, Calendar.HOUR, hours);
    }

    public static Date addMins(int mins) {
        return getDate(null, Calendar.MINUTE, mins);
    }

    public static Date addMins(Date date, int mins) {
        return getDate(date, Calendar.MINUTE, mins);
    }

    public static Date addSecond(int seconds) {
        return getDate(null, Calendar.SECOND, seconds);
    }

    public static Date addSecond(Date date, int seconds) {
        return getDate(date, Calendar.SECOND, seconds);
    }

    public static Date addDays(Date date, int days) {
        return getDate(date, Calendar.DAY_OF_MONTH, days);
    }

    public static long getSecondsBetween(Date fromDate, Date toDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);

        Calendar anotherCalendar = Calendar.getInstance();
        anotherCalendar.setTime(toDate);
        return (anotherCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000;
    }


    public static String getDatePoor(Date endDate, Date nowDate, int minutes) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();

        diff = diff + minutes * 60000;
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        //   return day + "天" + hour + "小时" + min + "分钟";

        if (day > 0) {
            return day + "天" + hour + "小时" + min + "分钟";
        }
        if (hour > 0) {
            return hour + "小时" + min + "分钟";
        } else {
            return min + "分钟";
        }
    }

    /**
     * 判断两个日期是否同一天
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean isSameDay(Date first, Date second) {
        Date range[] = getDayPeriod(first);
        return second.after(range[0]) && second.before(range[1]);
    }

    /**
     * 计算一天的起始时间和结束时间.
     *
     * @param date
     * @return
     */
    public static Date[] getDayPeriod(Date date) {
        if (date == null) {
            return null;
        }
        Date[] dtary = new Date[2];
        dtary[0] = getDayMinTime(date);
        dtary[1] = getDayMaxTime(date);
        return dtary;
    }

    /**
     * 获得指定日期的最小时间
     *
     * @param date
     * @return
     */
    public static Date getDayMinTime(Date date) {
        return getSpecifiedTime(date, 0, 0, 0);
    }


    public static Date getHourOfTime(Date date) {
        return getSpecifiedTime(date, date.getHours(), 0, 0);
    }

    /**
     * 获得指定日期的指定时、分、秒日期
     *
     * @param date
     * @param hours
     * @param minutes
     * @param seconds
     * @return
     */
    public static Date getSpecifiedTime(Date date, int hours, int minutes, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, seconds);
        return c.getTime();
    }

    /**
     * 获得指定日期的最大时间
     *
     * @param date
     * @return
     */
    public static Date getDayMaxTime(Date date) {
        return getSpecifiedTime(date, 23, 59, 59);
    }

    /**
     * 获取第二天凌晨时间
     *
     * @param date
     * @return
     */
    public static Date getNextZeroDay(Date date) {
        return getSpecifiedTime(date, 24, 00, 00);
    }

    /**
     * super add in 2015-07-10 获取两日期相差几天
     **/
    public static int getBetweenDay(Date date1, Date date2) {
        Calendar d1 = new GregorianCalendar();
        d1.setTime(date1);
        Calendar d2 = new GregorianCalendar();
        d2.setTime(date2);
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            // d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 获取两个时间的时间差（分）
     *
     * @param date1, date2
     * @return
     */
    public static int getMins(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60));
    }

    /**
     * 格式化到天
     *
     * @param date
     * @return
     */
    public static String formatToDay(Date date) {
        String pattern = "yyyy/MM/dd";
        return formate(date, pattern);
    }

    private static String formate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化时间 yyyy/MM/dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        String pattern = "yyyy/MM/dd HH:mm:ss";
        return formate(date, pattern);
    }


    /**
     * 将时间转换为时间戳
     *
     * @param s
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }


    /**
     * 将时间戳转换为时间
     *
     * @param s
     * @return
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    public static String stampToDateStr(Long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDateStrHms(Long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDateStrHm(Long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }


    public static Date stampToDate(Long s) {
        Date date = null;
        try {
            date = yyyyMMddHHmmss.parse(stampToDateStr(s));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*
     * 判断当前时间是周几
     *
     * */
    public static boolean WhatDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        boolean isOk = false;
        Date d = new Date();
        int hour = 0;
        if (dayOfWeek == 2) {
            hour = d.getHours();
            if (hour >= 9) {
                isOk = true;
            }
            return isOk;
        } else if (dayOfWeek == 3) {
            hour = d.getHours();
            if (hour <= 18) {
                isOk = true;
            }
            return isOk;
        } else {
            return isOk;
        }

        /*switch (dayOfWeek) {
            case 1:
                System.out.println("星期日");
                break;
            case 2:
                System.out.println("星期一");
                break;
            case 3:
                System.out.println("星期二");
                break;
            case 4:
                System.out.println("星期三");
                break;
            case 5:
                System.out.println("星期四");
                break;
            case 6:
                System.out.println("星期五");
                break;
            case 7:
                System.out.println("星期六");
                break;

        }*/


    }


    /**
     * 判断 一个时间点 是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   需要验证的时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }
}
