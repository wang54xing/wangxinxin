package com.fulaan.utils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wang_xinxin on 2016/7/4.
 */
public class WeekUtil {
    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        // 定义输出日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd EEE");

        Date currentDate = new Date();

        // 比如今天是2012-12-25
        List<Date> days = dateToWeek(currentDate);
        System.out.println("今天的日期: " + sdf.format(currentDate));
        for (Date date : days) {
            System.out.println(sdf.format(date));
        }
    }

    /**
     * 根据日期获得所在周的日期
     * @param mdate
     * @return
     */
    @SuppressWarnings("deprecation")
    public static List<Date> dateToWeek(Date mdate) {
        int b = mdate.getDay();
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        Long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int a = 0; a < 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a, fdate);
        }
        return list;
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek()); // Sunday
        return calendar.getTime();
    }


    /**
     * 根据日期获得所在周的日期
     * @param mdate
     * @return
     */
    @SuppressWarnings("deprecation")
    public static List<Date> dateToWeek(Date mdate,int count) {
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        Long fTime = mdate.getTime();
        for (int a = 0; a < count; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a, fdate);
        }
        return list;
    }
}
