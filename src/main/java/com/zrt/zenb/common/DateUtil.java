package com.zrt.zenb.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static String logTag = "DateUtil";

    /**
     * 日期+时间 数据库存储和页面显示格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT1_2 = "MM月dd日 HH:mm";
    public static final String FORMAT1_3 = "MM-dd";
    public static final String FORMAT1_4 = "HH:mm";
    public static final String FORMAT1_4_2 = "HH:mm:ss";
    public static final String FORMAT1_5 = "yyyy-MM-dd HH:mm";
    /**
     * 日期数据库存储和页面显示格式 yyyy-MM-dd
     */
    public static final String FORMAT4 = "yyyy-MM-dd";
    /**
     * yyyy/MM/dd
     */
    public static final String FORMAT4_2 = "yyyy/MM/dd";

    public static final String FORMAT5 = "yyyy年MM月dd日";
    public static final String FORMAT5_2 = "yyyy年";
    public static final String FORMAT5_3 = "MM月dd日";
    public static final String sdf2 = "yyyyMMddHHmmss";
    
    /**
     * 只作为图片存储目录日期格式 yyyyMMdd
     */
    public static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");

    public static Date getStringDate(String dateString, String format) {
        DateFormat formater = new SimpleDateFormat(format);
        Date time = new Date();
        if(null != dateString){
            try {
                time = formater.parse(dateString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time;
    }

    public static String getStringDateConvert(String dateString, String oldFormat, String newFormat) {
        DateFormat formatter = new SimpleDateFormat(oldFormat);
        Date time = null;
        try {
            time = formatter.parse(dateString);
        }
        catch (ParseException e) {

        }
        if(null != time){
            return getDateString(time, newFormat);
        }
        return dateString;
    }

    /**
     *
     * @param date
     * @param type
     * @return
     */
    public static String getDateString(Date date, String type) {
        String result = "";
        if (date != null) {
            SimpleDateFormat dateformat = new SimpleDateFormat(type);
            result = dateformat.format(date);
        }
        return result;
    }

    public static String getDateString(long dateTime, String type) {
    	SimpleDateFormat df = new SimpleDateFormat(type);
    	String result = df.format(dateTime * 1000l);
    	return result;
    }
    
    /**
     *
     * @param date
     * @return
     */
    public static String getDateString(String formatType) {
        String result = "";
        SimpleDateFormat dateformat = new SimpleDateFormat(formatType);
        result = dateformat.format(new Date());
        return result;
    }

    /**
     * 按yyyy-MM-dd HH:mm:ss格式返回当前时间
     * @return
     */
    public static String getDateString() {
        String result = "";
        SimpleDateFormat dateformat = new SimpleDateFormat(FORMAT1);
        result = dateformat.format(new Date());
        return result;
    }

    /**
     *
     * @param start
     * @param end
     * @return
     */
    public static String getTimeStringBetweenSeconds(long start, long end) {
        long last = end - start;
        long hour = last / 3600;
        long min = (last - hour * 3600) / 60;
        long sec = (last - hour * 3600 - min * 60);
        return leftpad(String.valueOf(hour), '0', 2) + ":" + leftpad(String.valueOf(min), '0', 2) + ":" + leftpad(String.valueOf(sec), '0', 2);

    }

    public static String leftpad(String src, char padChar, int size) {
        String dst = nvl(src);
        while (dst.length() < size) {
            dst = padChar + dst;
        }
        return dst;
    }

    public static String nvl(String src) {
        return src != null ? src.trim() : "";
    }

    public static String longTimeToStr(long time){
        Date date = new Date(time);
        return getDateString(date, FORMAT1);
    }

    public static final int minuteSeconds = 60;
    public static final int hourSeconds = 60 * 60;
    public static final int daySeconds = 60 * 60 * 24;
    public static final int yearSeconds = 60 * 60 * 24 * 365;

    public static String timePrewStr(long time){
        long nowTime = System.currentTimeMillis();
        long intervel = nowTime - time;

        long seconds = intervel / 1000;
        // 不到1分钟
        if(seconds <= minuteSeconds){
            return "刚刚";
        }
        // 不到1小时
        else if(seconds < hourSeconds){
            long minute = seconds / minuteSeconds;
            return minute + "分钟前";
        }
        // 不到1天
        else if(seconds < daySeconds){
            long hour = seconds / hourSeconds;
            return hour + "小时前";
        }
        // 不到1年
        else if(seconds < yearSeconds){
            long day = seconds / daySeconds;
            if(day < 30){
                return day + "天前";
            }
            else {
                return day / 30 + "月前";
            }
        }
        else {
            return seconds / yearSeconds + "年前";
        }
    }
}
