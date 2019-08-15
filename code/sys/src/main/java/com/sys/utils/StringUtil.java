package com.sys.utils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;



public class StringUtil {
	//随机串
	public static String getRandom(int length){
    StringBuffer buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
    StringBuffer sb = new StringBuffer(); 
    Random r = new Random(); 
    int range = buffer.length(); 
    for (int i = 0; i < length; i ++) { 
        sb.append(buffer.charAt(r.nextInt(range))); 
    }
    return sb.toString();
	}
	//获取视频文件存储名
	public static String getVideoName(String rowName){
		String lastName = "";
		if (rowName.endsWith(".mp4")){
			lastName = ".mp4";
		} else if (rowName.endsWith(".avi")){
			lastName = ".avi";
		} else if (rowName.endsWith(".asf")){
			lastName = ".asf";
		} else if (rowName.endsWith(".wmv")){
			lastName = ".wmv";
		} else if(rowName.endsWith(".mpg")) {
			lastName = ".mpg";
		} else if(rowName.endsWith(".flv")) {
			lastName = ".flv";
		} else if(rowName.endsWith(".mov")) {
			lastName = ".mov";
		} else if(rowName.endsWith(".3gp")) {
			lastName = ".3gp";
		} else if(rowName.endsWith("rmvb")) {
			lastName = ".rmvb";
		} else if(rowName.endsWith("mkv")) {
			lastName = ".mkv";
		} else {
			return "none";
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");//设置日期格式
		return df.format(new Date())+getRandom(6)+"/"+rowName;
	}

	
	public static <T> List<T> compare(T[] t1, T[] t2) {
		List<T> list1 = Arrays.asList(t1);
		List<T> list2 = new ArrayList<T>();
		for (T t : t2) {
			if (!list1.contains(t)) {
				list2.add(t);
			}
		}
		return list2;
	}

	public static boolean isEmpty(String str) {
		if(str!=null&&!"".equals(str)){
			return true;
		}else{
			return false;
		}
	}

}
