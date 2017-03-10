package com.caro.smartmodule.utils;

import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * formatType格式:yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
 * 可根据情况自由组合
 *
 * @author caro 注意:unix时间单位是秒
 *         <p>
 *         useage :eg: long times = Long.parseLong(WAB.getCreatetime())*1000;
 *         Date d = DateTools.longToDate(times,"yyyy-MM-dd HH:mm:ss"); String
 *         time =DateTools.dateToString(d,"yyyy-MM-dd HH:mm:ss");
 */
public class DateUtil {


    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳(毫秒)
     */
    public static long getCurrentTimeMillis() {
        long timecurrentTimeMillis = System.currentTimeMillis();

        return timecurrentTimeMillis;
    }

    /**
     * 获取当前时间
     *
     * @param type   日期时间格式
     * @param locale 地区默认为 Locale.CHINA
     * @return 按照格式返回当前时间
     */
    public String getCurrentTime(String type, Locale locale) {
        if (locale == null) {
            locale = Locale.CHINA;
        }
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(type, locale);
        return sdf.format(curDate);
    }

    /**
     * 日期格式转换
     *
     * @param date   待转换日期
     * @param type   格式
     * @param locale 地区 默认为 Locale.CHINA
     * @return 日期
     */
    public String formatDate(String date, String type, Locale locale) {
        String fmDate = "";
        if (date != null) {
            if (locale == null) {
                locale = Locale.CHINA;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(type, locale);
            fmDate = sdf.format(new Date(Long.parseLong(date)));
        }
        return fmDate;
    }

    /**
     * 比较时间
     *
     * @return true courseTime 大于当前时间
     */
    public boolean compareTime(String curTime, String courseTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.CHINA);
        boolean boo = true;
        try {
            try {
                boo = sdf.parse(courseTime).getTime()
                        - sdf.parse(curTime).getTime() > 0;
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return boo;
    }

    /**
     * date类型转换为String类型 formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * data Date类型的时间
     *
     * @param data
     * @param formatType
     * @return
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /**
     * long类型转换为String类型
     *
     * @param currentTime currentTime要转换的long类型的时间
     * @param formatType  formatType要转换的string类型的时间格式
     * @return
     * @throws ParseException
     */
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * @param timeType   timeType:unix 时间（type 1 秒）和标准时间（type 2 毫秒）
     * @param strTime    strTime的时间格式必须要与formatType的时间格式相同
     * @param formatType formatType要转换的格式yyyy-MM-dd       HH:mm:ss//yyyy年MM月dd日 yyyy/MM/dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static String stringDataToString(int timeType, String strTime, String formatType)
            throws ParseException {
        long times = 0;
        switch (timeType) {
            case 1:
                times = Long.parseLong(strTime) * 1000;
                break;
            case 2:
                times = Long.parseLong(strTime);
                break;
        }
        Date d = longToDate(times, formatType);
        String time = dateToString(d, formatType);

        return time;
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * long转换为Date类型
     *
     * @param currentTime currentTime要转换的long类型的时间
     * @param formatType  formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @return
     * @throws ParseException
     */
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    /**
     * string类型转换为long类型 strTime要转换的String类型的时间 formatType时间格式
     * strTime的时间格式和formatType的时间格式必须相同
     *
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */

    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * date类型转换为long类型 date要转换的date类型的时间
     *
     * @param date
     * @return
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * unix 秒数转换为天数 1day = 86400s
     *
     * @param times
     * @return
     */
    public static int longToday(long times) {
        return (int) times / 86400;
    }

    /**
     * 时间戳转换成日期格式
     *
     * @param pattern 格式
     * @param time    时间戳 单位毫秒
     * @return
     */
    public static String getTimePattern(String pattern, long time) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }


    /**
     * 获取24小时制的时间格式
     *
     * @return
     */
    public static String getTime() {
        Calendar now;
        SimpleDateFormat fmt;

        now = Calendar.getInstance();
        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(now.getTime());
    }


    //------------http://zzc1684.iteye.com/blog/2169639------------------
    //类名称:isWeekSame
    //包含方法:isSameDate
    //作者:lzueclipse
    //时间:2005-11-13
    //方法名称：isSameDate(String date1,String date2)
    //功能描述：判断date1和date2是否在同一周
    //输入参数：date1,date2
    //输出参数：
    //返 回 值：false 或 true
    //其它说明：主要用到Calendar类中的一些方法
    //--------------------------------------------------------------------
    public static boolean isSameDate(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        //subYear==0,说明是同一年
        if (subYear == 0) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        //例子:cal1是"2005-1-1"，cal2是"2004-12-25"
        //java对"2004-12-25"处理成第52周
        // "2004-12-26"它处理成了第1周，和"2005-1-1"相同了
        //大家可以查一下自己的日历
        //处理的比较好
        //说明:java的一月用"0"标识，那么12月用"11"
        else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        //例子:cal1是"2004-12-31"，cal2是"2005-1-1"
        else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;

        }
        return false;
    }

    /******************Android小工具之智能格式化时间**********************************
     * http://www.jianshu.com/p/c819918240b3
     * 看着别人的应用的时间显示着“刚刚”、“2分钟之前”、“下午 2:00”是不是很羡慕？但是常常因为项目时间紧凑，
     * 没空去扣这些细节的你一定很想要一个这样的小工具啦，这里就送大家一个这样的格式化时间小工具：
     */

    /**
     *
     * @param date
     * @return
     */

    public static String formatDateTime(Date date) {
        String text;
        long dateTime = date.getTime();
        if (isSameDay(dateTime)) {
            Calendar calendar = GregorianCalendar.getInstance();
            if (inOneMinute(dateTime, calendar.getTimeInMillis())) {
                return "刚刚";
            } else if (inOneHour(dateTime, calendar.getTimeInMillis())) {
                return String.format("%d分钟之前", Math.abs(dateTime - calendar.getTimeInMillis()) / 60000);
            } else {
                calendar.setTime(date);
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                if (hourOfDay > 17) {
                    text = "晚上 hh:mm";
                } else if (hourOfDay >= 0 && hourOfDay <= 6) {
                    text = "凌晨 hh:mm";
                } else if (hourOfDay > 11 && hourOfDay <= 17) {
                    text = "下午 hh:mm";
                } else {
                    text = "上午 hh:mm";
                }
            }
        } else if (isYesterday(dateTime)) {
            text = "昨天 HH:mm";
        } else if (isSameYear(dateTime)) {
            text = "M月d日 HH:mm";
        } else {
            text = "yyyy-M-d HH:mm";
        }

        // 注意，如果使用android.text.format.DateFormat这个工具类，在API 17之前它只支持adEhkMmszy
        return new SimpleDateFormat(text, Locale.CHINA).format(date);
    }

    private static boolean inOneMinute(long time1, long time2) {
        return Math.abs(time1 - time2) < 60000;
    }

    private static boolean inOneHour(long time1, long time2) {
        return Math.abs(time1 - time2) < 3600000;
    }

    private static boolean isSameDay(long time) {
        long startTime = floorDay(Calendar.getInstance()).getTimeInMillis();
        long endTime = ceilDay(Calendar.getInstance()).getTimeInMillis();
        return time > startTime && time < endTime;
    }

    private static boolean isYesterday(long time) {
        Calendar startCal;
        startCal = floorDay(Calendar.getInstance());
        startCal.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = startCal.getTimeInMillis();

        Calendar endCal;
        endCal = ceilDay(Calendar.getInstance());
        endCal.add(Calendar.DAY_OF_MONTH, -1);
        long endTime = endCal.getTimeInMillis();
        return time > startTime && time < endTime;
    }

    private static boolean isSameYear(long time) {
        Calendar startCal;
        startCal = floorDay(Calendar.getInstance());
        startCal.set(Calendar.MONTH, Calendar.JANUARY);
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        return time >= startCal.getTimeInMillis();
    }

    private static Calendar floorDay(Calendar startCal) {
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        return startCal;
    }

    private static Calendar ceilDay(Calendar endCal) {
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        return endCal;
    }
}
