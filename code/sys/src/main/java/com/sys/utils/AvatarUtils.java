package com.sys.utils;

import java.text.MessageFormat;


import org.apache.commons.lang.StringUtils;

import com.sys.constants.Constant;
import com.sys.props.Resources;

/**
 * 头像辅助类
 * @author fourer
 *
 */

public class AvatarUtils {



	public static final String MIN_AVATAR_FORMAT =Resources.getProperty("min.avatar.format");
	public static final String MIDDLE_AVATAR_FORMAT =Resources.getProperty("mid.avatar.format");
	public static final String MAX_AVATAR_FORMAT =Resources.getProperty("max.avatar.format");

	
	public static final String USER_AVATAR_FORMAT =Resources.getProperty("max.avatar.format");
	public static final String DEFAULT_AVATOR_FORMAT="http://7xiclj.com1.z0.glb.clouddn.com/d_{0}_{1}.png";
	public static final String DEFAULT_FORMAT="head-default-head.jpg";
	
	public static final int DEFAULT_ROLE=1;
	public static final int DEFAULT_SEX=1;
	
	/**
	 * 得到头像
	 * @param avatar
	 * @param type 1小的  2中型 3 大的
	 * @return
	 */
	public static String getAvatar(String avatar, int type)
	{

		if(Constant.ONE==type)
		{
			return MessageFormat.format(MIN_AVATAR_FORMAT, avatar);
		}
		if(Constant.TWO==type)
		{
			return MessageFormat.format(MIDDLE_AVATAR_FORMAT, avatar);
		}
		return MessageFormat.format(MAX_AVATAR_FORMAT, avatar);
	}
	
	/**
	 * 得到头像
	 * @param avatar
	 * role 1学生 2 老师 4家长 8领导;默认是学生
	 * sex  1男 0女 ;默认是男
	 * @return
	 */
	public static String getAvatar(String avatar,int role,int sex)
	{
		if(StringUtils.isNotBlank(avatar) && !avatar.equals(DEFAULT_FORMAT))
		{
			return MessageFormat.format(USER_AVATAR_FORMAT, avatar);
		}
		else
		{
			int defrole=Constant.ONE;
			
			if(isHeadMaster(role))
			{
				defrole=Constant.EIGHT;
			}
			else if(isTeacher(role))
			{
				defrole=Constant.TWO;
			}
			else if(isParent(role))
			{
				defrole=Constant.FOUR;
			}
			
			int defSex=Constant.ONE;
			if(sex!=Constant.ONE)
				defSex=Constant.ZERO;
			
			return MessageFormat.format(DEFAULT_AVATOR_FORMAT, defrole,defSex);
		}
	}
	
	
	private static boolean isHeadMaster(int role)
	{
		return role!= Constant.ONE && role!= Constant.TWO   && role!= Constant.FOUR;
	}
	private static boolean isParent(int role)
	{
		return role== Constant.FOUR;
	}
	private static boolean isTeacher(int role)
	{
		return role== Constant.TWO;
	}
}
