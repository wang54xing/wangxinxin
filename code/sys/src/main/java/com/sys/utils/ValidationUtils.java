package com.sys.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.sys.constants.Constant;

/**
 * 检验输入参数
 * @author fourer
 *
 */
public class ValidationUtils {

	/**
	 * 检验课程名字是否合法
	 * @param name
	 * @return
	 */
	public static boolean isRequestLessonName(String name)
	{
		return true;
	}
	
	
	
	/**
	 * 检验课程附件名字是否合法
	 * @param name
	 * @return
	 */
	public static boolean isRequestLessonWareName(String name)
	{
		return true;
	}
	
	
	/**
	 * 检验课程介绍是否合法
	 * @param name
	 * @return
	 */
	public static boolean isRequestLessonIntro(String intro)
	{
		return true;
	}
	
	
	/**
	 * 是不是合法的用户名称
	 * @param name
	 * @return
	 */
	public static boolean isRequestUserName(String name)
	{
		if(StringUtils.isBlank(name))
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * 是不是合法的用户名称
	 * @param name
	 * @return
	 */
	public static boolean isRequestUserNickName(String name)
	{
		if(StringUtils.isBlank(name))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 是不是合法的用户密码
	 * @param name
	 * @return
	 */
	public static boolean isRequestPassword(String pwd)
	{
		if(StringUtils.isBlank(pwd))
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * 是不是合法的作业名
	 * @param name
	 * @return
	 */
	public static boolean isRequestHomeWorkName(String name )
	{
		return true;
	}
	
	
	
	/**
	 * 是不是合法的通知名
	 * @param name
	 * @return
	 */
	public static boolean isRequestNoticeName(String name )
	{
		return true;
	}
	
	
	/**
	 * 是不是合法的作业内容
	 * @param name
	 * @return
	 */
	public static boolean isRequestHomeWorkContent(String content )
	{
		return true;
	}
	
	
	

	/**
	 * 是不是合法的通知内容
	 * @param name
	 * @return
	 */
	public static boolean isRequestNoticeContent(String content )
	{
		return true;
	}
	
	
	
	/**
	 * 是不是合法的考试名称
	 * @param name
	 * @return
	 */
	public static boolean isRequestTestPaper(String name )
	{
		if(StringUtils.isBlank(name))
			return false;
		if(name.length()>Constant.TEN*2)
			return false;
		return true;
	}
	
	
	
	/**
	 * 是不是合法的商品评论
	 * @param name
	 * @return
	 */
	public static boolean isRequestGoodsComment(String comment )
	{
		return true;
	}
	
	
	
	/**
	 * 是不是合法的课程评论
	 * @param name
	 * @return
	 */
	public static boolean isRequestLessonComment(String comment )
	{
		return true;
	}
	
	
	/**
	 * 是不是合法的视频名称
	 * @param name
	 * @return
	 */
	public static boolean isRequestVideoName(String name )
	{
		return true;
	}
	
	/**
	 * 是不是合法的学生练习名称
	 * @param name
	 * @return
	 */
	public static boolean isRequestStudentExerciseName(String name )
	{
		return true;
	}
	
	
	/**
	 * 是不是合法的手机
	 * @param mobile
	 * @return
	 */
	public static boolean isRequestModile(String mobile)
	{
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$"); 
		Matcher m = p.matcher(mobile); 
		return m.matches();
	}
	
	/**
	 * 是不是数字
	 * @param num
	 * @return
	 */
	public static boolean isNumber(String num)
	{
        Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(num); 
		return m.matches();
	}
	
	
	/**
	 * 是不是数字
	 * @param num
	 * @return
	 */
	public static boolean isEmail(String num)
	{
		String check = "^.+@.+\\..+$"; 
        Pattern p = Pattern.compile(check);  
		Matcher m = p.matcher(num); 
		return m.matches();
	}

}
