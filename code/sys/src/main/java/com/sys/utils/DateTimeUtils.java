package com.sys.utils;

import com.sys.constants.Constant;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeUtils {

	public static final String CHINESE_LONGDATETIME = "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String CHINESE_DATETIME_MINUTE = "yyyy年MM月dd日 HH时mm分";
	public static final String DATE_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
	public static final String DATE_YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm";
	public static final String DATE_YYYY_MM_DD_HH_MM_SS_H = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_YYYY_MM_DD_HH_MM_A = "yyyy-MM-dd HH:mm";
    public static final String DATE_YYYY_MM_DD_B = "yyyy.MM.dd";
	public static final String DATE_YYYY_MM_DD_HH_MM_H = "MM月dd日  HH:mm";
	public static final String DATE_HH_MM = "HH:mm";
    public static final String DATE_MM_DD_HH_MM = "MM/dd HH:mm";
    public static final String DATE_HH_MM_SS = "HH:mm:ss";
	public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_YYYY_MM = "yyyy-MM";
    public static final String CHINESE_DATE = "yyyy年MM月dd日";
    public static final String CHINESE_DATE_MM_DD = "MM月dd日";
    public static final String DATE_YEAE = "yyyy";
    public static final String DATE_HH = "HH";
    public static final String DATE_MM = "MM";
    public static final String DATE_DD = "dd";
    public static final String DATE_YYYY_MM_DD_N = "yyyy/MM/dd";
    public static final String FORMAT="EEEE";
    public static final String[] weekDays = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期日"};
	
    /**
     * 
     * @param time
     * @param format 日期格式
     * @return
     */
	public static String convert(long time,String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format,Locale.CHINA);
		String dateString = formatter.format(new Date(time));
		return dateString;
	}
	
	

	/**
	 * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStrLong(Date dateDate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(dateDate);
		return dateString;
	}
	
	
	/**
	 * 得到一天23：59：59分时间
	 * @param time
	 * @return
	 */
	public static Long getDayMaxTime(long time)
	{
		
		String timeStr=dateToStrLong(new Date(time),DateTimeUtils.DATE_YYYY_MM_DD)+" 23:59:59";
		Date date=stringToDate(timeStr,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
		return date.getTime();

	}
	
	
	
	public static Date stringToDate(String str, String formatType) {
		SimpleDateFormat format = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
		}
		return date;
	}

    public static Date longToDate(long time, String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            String str=getLongToStrTimeTwo(time);
            date = format.parse(str);
        } catch (ParseException e) {
        }
        return date;
    }

    public static Date longToDate2(long time, String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            String str=getLongToStrTimeSEVEN(time);
            date = format.parse(str);
        } catch (ParseException e) {
        }
        return date;
    }


	
	/**
	 * 得到周的最大和最小时间
	 * @return
	 */
	public static Long[] getWeekMinAndMaxTime(String date) throws Exception {
		Long[] times = new Long[2];

		Calendar cal = Calendar.getInstance();
		
		if(StringUtils.isBlank(date))
		{
			date =convert(System.currentTimeMillis(),DATE_YYYY_MM_DD);
		}
		Date d =stringToDate(date,DATE_YYYY_MM_DD);
		cal.setTime(d);
		
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		times[0] = cal.getTime().getTime();

		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		cal.add(Calendar.WEEK_OF_YEAR, 1);

		//times[1] = cal.getTime().getTime();
		times[1] = cal.getTime().getTime()+Constant.MS_IN_DAY-1;

		return times;
	}
	
	
	
	/**
	 * 得到月的最大和最小时间
	 * @return
	 */
	public static Long[] getMonthMinAndMaxTime(String date) throws Exception {
		Long[] times = new Long[2];

		Calendar cal = Calendar.getInstance();
	
		if(StringUtils.isBlank(date))
		{
			date =convert(System.currentTimeMillis(),DATE_YYYY_MM_DD);
		}
		Date d =stringToDate(date,DATE_YYYY_MM_DD);
		cal.setTime(d);
		
		
		
		cal.set(Calendar.DAY_OF_MONTH, cal     
	            .getActualMinimum(Calendar.DAY_OF_MONTH));

		times[0] = cal.getTime().getTime();

		cal.set(Calendar.DAY_OF_MONTH, cal     
	            .getActualMaximum(Calendar.DAY_OF_MONTH));

		times[1] = cal.getTime().getTime()+Constant.MS_IN_DAY-1;
		
		return times;
	}
	
	
	
	/**
	 * 得到一天00：00：00分时间
	 * @param time
	 * @return
	 */
	public static Long getDayMinTime(long time)
	{
		
		String timeStr=dateToStrLong(new Date(time),DATE_YYYY_MM_DD)+" 00:00:00";
		Date date=stringToDate(timeStr,DATE_YYYY_MM_DD_HH_MM_SS_H);
		return date.getTime();

	}
	
	

	/**
	 * 是不是工作日
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isWorkDate(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(time));
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w != Constant.ZERO && w != Constant.SIX;
	}

    /**
     * 获取日期是周几
     * @param date
     * @return
     */
    public static int somedayIsWeekDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return week; //星期一时week=1,week=0是星期天
    }

    /**
     * 取年份
     * @return
     */
    public int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 取月份 1～12，自然月
     * @return
     */
    public int getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1; // 月份要加1，因为java是从0~11
    }

    /**
     * 取号 1～31，自然日
     * @return
     */
    public int getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE); // 月份要加1，因为java是从0~11
    }

    /**
     * 获取当前时间yyyy年MM月DD日
     * @return
     */
    public static String getChineseDate()
    {
        String timeStr=dateToStrLong(new Date(),CHINESE_DATE);
        return timeStr;
    }

    public static String getCurrWeekDate() {
        String weekStr = getWeekOfDate(new Date());
        return weekStr;
    }

    public static int getCurrWeekIndex(){
        Integer[] weekDays = {6, 0, 1, 2, 3, 4, 5};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];
    }

    /**
     * 获取当前时间format
     * @return
     */
    public static String getCurrDate(String format)
    {
        String timeStr=dateToStrLong(new Date(),format);
        return timeStr;

    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrDate()
    {
        String timeStr=dateToStrLong(new Date(),DATE_YYYY_MM_DD);
        return timeStr;

    }
    /**
     * 日期比较
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(long DATE1, long DATE2) {
        if (DATE1 > DATE2) {
            return 1;
        } else if (DATE1 < DATE2) {
            return -1;
        } else {
            return 0;
        }
    }
    /**
     * 日期比较
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date2(String DATE1, String DATE2) {
        Date dt1 = stringToDate(DATE1, DATE_YYYY_MM_DD);
        Date dt2 = stringToDate(DATE2, DATE_YYYY_MM_DD);
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 日期比较
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(String DATE1, Date DATE2) {
        Date dt1 = stringToDate(DATE1, DATE_YYYY_MM_DD_HH_MM);
        if (dt1.getTime() > DATE2.getTime()) {
            return 1;
        } else if (dt1.getTime() < DATE2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }


    public static Date getNextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获得指定日期的后一天
     * @param someday
     * @return
     */
    public static Date getPrevDay(Date someday,int value){
        Calendar c = Calendar.getInstance();
        c.setTime(someday);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day+value);
        Date date = c.getTime();
        return date;
    }

    /**
     * 获得指定日期的年月
     * @param someday
     * @return
     */
    public static String getYearMonth(Date someday){
        Calendar c = Calendar.getInstance();
        c.setTime(someday);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-1);
        Date date = c.getTime();
        String timeStr=dateToStrLong(date,DATE_YYYY_MM);
        return timeStr;
    }


    /**
     * 获得指定日期的后一天
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay){
        Calendar c = Calendar.getInstance();
        Date date=null;
        date =stringToDate(specifiedDay,DATE_YYYY_MM_DD);
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day+1);

        String dayAfter=dateToStrLong(c.getTime(),DATE_YYYY_MM_DD);
        return dayAfter;
    }

    // 获得当前周- 周一的日期
    public static  String getCurrentMonday(String date) {
        Calendar c = Calendar.getInstance();
        c.setTime(stringToDate(date,DATE_YYYY_MM_DD));
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            weekday = -6;
        } else {
            weekday = 2 - weekday;
        }
        c.add(Calendar.DATE, weekday);
        return dateToStrLong(c.getTime(), DATE_YYYY_MM_DD);
    }

    // 获得当前周- 周日  的日期
    public static String getPreviousSunday(String date) {
        Calendar c = Calendar.getInstance();
        c.setTime(stringToDate(date,DATE_YYYY_MM_DD));
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            weekday = -6;
        } else {
            weekday = 2 - weekday;
        }
        c.add(Calendar.DATE, 6 + weekday);
        return dateToStrLong(c.getTime(), DATE_YYYY_MM_DD);
    }

    // 获得当前周- 周几的日期
    public static  String getCurrentDay(String date,int index) {
        Calendar c = Calendar.getInstance();
        c.setTime(stringToDate(date,DATE_YYYY_MM_DD));
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            weekday = -6;
        } else {
            weekday = 2 - weekday;
        }
        c.add(Calendar.DATE, index - 1 + weekday);
        return dateToStrLong(c.getTime(), DATE_YYYY_MM_DD);
    }

    // 获得当前月--开始日期
    public static String getMinMonthDate(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToDate(date,DATE_YYYY_MM_DD));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateToStrLong(calendar.getTime(), DATE_YYYY_MM_DD);
    }


    // 获得当前月--结束日期
    public static String getMaxMonthDate(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToDate(date, DATE_YYYY_MM_DD));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateToStrLong(calendar.getTime(), DATE_YYYY_MM_DD);
    }

    // 获得当前月--开始日期
    public static String getMinMonthDate(String date,String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToDate(date,format));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateToStrLong(calendar.getTime(), DATE_YYYY_MM_DD);
    }


    // 获得当前月--结束日期
    public static String getMaxMonthDate(String date,String format){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToDate(date, format));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateToStrLong(calendar.getTime(), DATE_YYYY_MM_DD);
    }

    /**
     * 得到某一天时间戳
     * @param time
     * @return
     */
    public static Long getStrToLongTime(String time)
    {
        Date date=stringToDate(time,DATE_YYYY_MM_DD_HH_MM_SS_H);
        return date.getTime();
    }

    /**
     * 得到某一天时间戳
     * @param time
     * @return
     */
    public static Long getStrToLongTime(String time,String format)
    {
        Date date=stringToDate(time,format);
        return date.getTime();
    }

    public static String getLongToStrTime(long time,String format)
    {
        String timeStr=dateToStrLong(new Date(time),format);
        return timeStr;
    }

    public static String getLongToStrTime(long time)
    {
        String timeStr=dateToStrLong(new Date(time),DATE_YYYY_MM_DD);
        return timeStr;
    }

    public static String getLongToStrTimeTwo(long time)
    {
        String timeStr=dateToStrLong(new Date(time),DATE_YYYY_MM_DD_HH_MM_SS_H);
        return timeStr;
    }

    public static String getLongToStrTimeThree(long time)
    {
        String timeStr=dateToStrLong(new Date(time),DATE_HH_MM_SS);
        return timeStr;
    }

    public static String getLongToStrTimeFour(long time)
    {
        String timeStr=dateToStrLong(new Date(time),DATE_HH);
        return timeStr;
    }

    public static String getLongToStrTimeFive(long time)
    {
        String timeStr=dateToStrLong(new Date(time),DATE_YYYY_MM_DD_HH_MM);
        return timeStr;
    }

    public static String getLongToStrTimeSIX(long time)
    {
        String timeStr=dateToStrLong(new Date(time),DATE_HH_MM);
        return timeStr;
    }

    public static String getLongToStrTimeSEVEN(long time)
    {
        String timeStr=dateToStrLong(new Date(time),"yyyy/MM/dd");
        return timeStr;
    }
    public static String getDateToStrTime(Date time)
    {
        String timeStr=dateToStrLong(time,DATE_YYYY_MM_DD);
        return timeStr;
    }

    public static long getDate(Date time,int exp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.MONTH, exp);
        return calendar.getTime().getTime();
    }

    //整数(秒数)转换为时分秒格式(xx:xx:xx)
    public static String getDuration(int durationSeconds){
        int hours = durationSeconds /(60*60);
        int leftSeconds = durationSeconds % (60*60);
        int minutes = leftSeconds / 60;
        int seconds = leftSeconds % 60;

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(addZeroPrefix(hours));
        sBuffer.append(": ");
        sBuffer.append(addZeroPrefix(minutes));
        sBuffer.append(": ");
        sBuffer.append(addZeroPrefix(seconds));

        return sBuffer.toString();
    }

    public static String addZeroPrefix(int number){
        if(number < 10){
            return "0"+number;
        }else{
            return ""+number;
        }

    }

    public static String getCreateTime(long time) {
        long lasttime=new Date().getTime()-time;
        lasttime/=1000;
        String timeStr="";
        if(lasttime/(3600*24)>=1)
        {
            timeStr+= (int)Math.floor(lasttime/(3600*24))+"天前";
        }
        else if(lasttime/3600>=1)
        {
            timeStr+= (int)Math.floor(lasttime/3600)+"小时前";
        }
        else if(lasttime/60>=1)
        {
            timeStr+= (int)Math.floor(lasttime/60)+"分钟前";
        }
        else
        {
            if(lasttime>0)
            {
                timeStr+= (int)Math.floor(lasttime/60)+"秒前";
            }
            else
            {
                timeStr+="刚刚";
            }
        }
        return timeStr;
    }

    /**
     * 给日期字符串拼接时分秒
     * @param dateTime 日期
     * @param type 1：表示开始时间，2：表示结束时间
     * @return
     */
    public static String handleTime(String dateTime, int type){
        if(type==1) {
            //判断查询条件的开始时间是否为空
            dateTime = dateTime == null ? "" : dateTime;
            if (!"".equals(dateTime)) {//不等于空时，拼接" 00:00:00"
                dateTime = dateTime + " 00:00:00";
            } else {//等于空时，设置默认时间
                dateTime = "2014-09-01 00:00:00";
            }
        }
        if(type==2){
            //判断查询条件的结束时间是否为空
            dateTime=dateTime==null?"":dateTime;
            if(!"".equals(dateTime)){//不等于空时，拼接" 23:59:59"
                dateTime=dateTime+" 23:59:59";
            }else{//等于空时，设置默认时间
                dateTime=getCurrDate()+" 23:59:59";
            }
        }
        return dateTime;
    }


    /**
     * 产生一个date1到date2的时间list
     * @param date1
     * @param date2
     * @return
     */
    public static List<String> getUseTimeArea(String date1, String date2){
        List<String> list2 = new ArrayList<String>();
        if(date1==null||"".equals(date1)){
            date1="2014-09-01";
        }
        if(date2==null||"".equals(date2)){
            date2=getCurrDate();
        }
        while (compare_date2(date1,date2)<1) {
            list2.add(date1.substring(0,10));
            date1 = getSpecifiedDayAfter(date1);
        }
        return list2;
    }

    /**
     * 获取两个日期的天数
     * @param fDate
     * @param oDate
     * @return
     */
    public static int daysOfTwo(Date fDate, Date oDate) {

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(fDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(oDate);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;

    }

    /**
     * 取得当前日期所在月的最后一天
     * @param sDate1
     * @return
     */
    public   static   Date   getLastDayOfMonth(Date   sDate1)   {
        Calendar   cDay1   =   Calendar.getInstance();
        cDay1.setTime(sDate1);
        final   int   lastDay   =   cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date   lastDate   =   cDay1.getTime();
        lastDate.setDate(lastDay);
        return   lastDate;
    }

    /**
     * 取得当前日期所在月的第一天
     * @param sDate1
     * @return
     */
    public   static   Date   getFirstDayOfMonth(Date   sDate1)   {
        Calendar   cDay1   =   Calendar.getInstance();
        cDay1.setTime(sDate1);
        final   int   lastDay   =   cDay1.getActualMinimum(Calendar.DAY_OF_MONTH);
        Date   lastDate   =   cDay1.getTime();
        lastDate.setDate(lastDay);
        return   lastDate;
    }


    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek() + 6); // Saturday
        return calendar.getTime();
    }

    /**
     *
     *
     * @param date
     * @return
     */
    public static List<String> getWeekDays(Date date) {
        List<String> dates = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        for (int i=0;i<7;i++) {
            calendar.set(Calendar.DAY_OF_WEEK,
                    calendar.getFirstDayOfWeek() + i);
            dates.add(convert(calendar.getTimeInMillis(),CHINESE_DATE));
            System.out.println(convert(calendar.getTimeInMillis(),CHINESE_DATE));
        }
        return dates;
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static int getWeekByDate(Date dt) {
        Integer[] weekDays = {7, 1, 2,3,4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     *
     *
     * @param date
     * @return
     */
    public static List<Long> getWeekDays2(Date date) {
        List<Long> dates = new ArrayList<Long>();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        for (int i=0;i<7;i++) {
            calendar.set(Calendar.DAY_OF_WEEK,
                    calendar.getFirstDayOfWeek() + i);
            dates.add(calendar.getTimeInMillis());
            System.out.println(convert(calendar.getTimeInMillis(),DATE_YYYY_MM_DD));
        }
        return dates;
    }

    public static void main(String[] args) {
        //getWeekDays(new Date());

	    System.out.println(DateTimeUtils.getLongToStrTime(-1500000l, "HH:mm"));


	}
}
