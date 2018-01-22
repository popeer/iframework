package com.chanjet.chanapp.qa.iFramework.common.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by haijia on 12/6/17.
 */
public final class DateUtil {
    public DateUtil() {
    }

    public static String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(new Date());
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss SSS");
    }

    public static String format(Date date, String format) {
        return (new SimpleDateFormat(format)).format(date);
    }

    public static String format(long time) {
        return format(new Date(time));
    }

    public static String format(long time, String format) {
        return format(new Date(time), format);
    }

    public static String formatDelta(long from, long to) {
        long l = to - from;
        long day = l / 86400000L;
        long hour = l / 3600000L - day * 24L;
        long min = l / 60000L - day * 24L * 60L - hour * 60L;
        long s = l / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L;
        long sss = l - day * 24L * 60L * 60L * 1000L - hour * 60L * 60L * 1000L - min * 60L * 1000L - s * 1000L;
        return day + " " + hour + ":" + min + ":" + s + " " + sss;
    }

    public static String formatDelta(Date from, Date to) {
        return formatDelta(from.getTime(), to.getTime());
    }

    public static long getTimestampBeforDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, days);
        return calendar.getTimeInMillis();
    }

    public static long getTimestampBeforDays(long timestamp, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(5, days);
        return calendar.getTimeInMillis();
    }

    public static int getDaysBetweenTwoTimestamp(long curTimestamp, long agoTimestamp) {
        long cur = curTimestamp;
        long ago = agoTimestamp;
        boolean exchange = false;
        if(agoTimestamp > curTimestamp) {
            cur = agoTimestamp;
            ago = curTimestamp;
            exchange = true;
        }

        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(cur);
        Calendar agoCalendar = Calendar.getInstance();
        agoCalendar.setTimeInMillis(ago);
        int days = 0;
        int curYear = curCalendar.get(1);
        int agoYear = agoCalendar.get(1);
        int curDayOfYear = curCalendar.get(6);
        int agoDayOfYear = agoCalendar.get(6);
        if(curYear == agoYear) {
            return curDayOfYear - agoDayOfYear;
        } else {
            for(int i = agoYear; i < curYear; ++i) {
                if((i % 4 != 0 || i % 100 == 0) && i % 400 != 0) {
                    days += 365;
                } else {
                    days += 366;
                }
            }

            days += curDayOfYear - agoDayOfYear;
            if(exchange) {
                days = -days;
            }

            return days;
        }
    }
}
