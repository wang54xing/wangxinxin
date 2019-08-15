package com.sys.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 对名字处理；张三2变成张三
 * @author fourer
 *
 */
public class NameShowUtils {

	private static final String k6kt_help="k6kt小助手";
	
	private static final String regex="[0-9]+(?=[^0-9]*$)";
	
	private static final String regex1="[0-9]+";
	
	public static String showName(String showName)
	{
		if(StringUtils.isBlank(showName))
			return showName;
		Pattern pattern = Pattern.compile("\\d+$");
		Matcher matcher = pattern.matcher(showName);
		if(!matcher.find())
		{
			return showName;
		}
		if(showName.matches(regex1))
		{
			return showName;
		}
		
		if(showName.toLowerCase().equals(k6kt_help))
			return showName;
		return showName.replaceAll(regex, "");
	}
	
	
	public static void main(String[] args) {
//		System.out.println(NameShowUtils.showName("张三"));
//		System.out.println(NameShowUtils.showName("张三2"));
//		System.out.println(NameShowUtils.showName("123张三2"));
//		System.out.println(NameShowUtils.showName("k6kt小助手"));
//		System.out.println(NameShowUtils.showName("k6kt小助手2"));
//		System.out.println(NameShowUtils.showName("123"));
//		System.out.println(NameShowUtils.showName("123爸爸"));

		System.out.println(ValidationUtils.isRequestModile("13248365750"));
		System.out.println(ValidationUtils.isRequestModile("13548365758"));
		System.out.println(ValidationUtils.isRequestModile("13248365752"));
		System.out.println(ValidationUtils.isRequestModile("13248365753"));
		System.out.println(ValidationUtils.isRequestModile("13248365754"));
		System.out.println(ValidationUtils.isRequestModile("13248365755"));
	}
}
