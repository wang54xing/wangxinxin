package com.fulaan.utils;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.phprpc.PHPRPC_Client;

public class PHPRPCUtils {

	//phprpc api  address
	public static final String phprpcAddress="http://www.ahedu.cn/EduResource/api/thirdUserApi.php";
	
	
	
	/**
	 * 得到用户基本数据
	 * @param userName
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	public  static JSONObject getUserInfo(String userName) throws UnsupportedEncodingException, JSONException
	{
		PHPRPC_Client client = new PHPRPC_Client(phprpcAddress);
		Object o=client.invoke("GetUserInfo", new Object[]{"login_name",userName});
		String ss =new String((byte[])o, "utf-8");
		String output=decodeUnicode(ss);
		JSONObject  jsono =new JSONObject(output);
		return jsono;
	}
	
	
	/**
	 *  获取用户所在的学校
	 * @param schoolId
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	public  static JSONObject GetSchool(String schoolId) throws UnsupportedEncodingException, JSONException
	{
		PHPRPC_Client client = new PHPRPC_Client(phprpcAddress);
		Object o=client.invoke("GetSchool", new Object[]{schoolId});
		String ss =new String((byte[])o, "utf-8");
		String output=decodeUnicode(ss);
		JSONObject  jsono =new JSONObject(output);
		return jsono;
	}
	
	
	/**
	 *  5.1.5 获取区域信息
	 * @param areaId
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	public  static JSONObject GetArea(String areaId) throws UnsupportedEncodingException, JSONException
	{
		PHPRPC_Client client = new PHPRPC_Client(phprpcAddress);
		Object o=client.invoke("GetArea", new Object[]{areaId});
		String ss =new String((byte[])o, "utf-8");
		String output=decodeUnicode(ss);
		JSONObject  jsono =new JSONObject(output);
		return jsono;
	}
	
	/**
	 * todo
	 *  5.1.2 获取用户所在的学校（GetUserSchool）
	 * @param userName
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	public  static JSONObject GetUserSchool(int skip,int limit,String userId) throws UnsupportedEncodingException, JSONException
	{
		PHPRPC_Client client = new PHPRPC_Client(phprpcAddress);
		Object o=client.invoke("GetUserSchool", new Object[]{skip,limit,userId});
		String ss =new String((byte[])o, "utf-8");
		String output=decodeUnicode(ss);
		JSONObject  jsono =new JSONObject(output);
		return jsono;
	}
	
	
	
	
	/**
	 * todo
	 * 获取用户所在的机构（GetUserOrg）
	 * @param userName
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	public  static JSONObject GetUserOrg(int skip,int limit,String userId) throws UnsupportedEncodingException, JSONException
	{
		PHPRPC_Client client = new PHPRPC_Client(phprpcAddress);
		Object o=client.invoke("GetUserOrg", new Object[]{skip,limit,userId});
		String ss =new String((byte[])o, "utf-8");
		String output=decodeUnicode(ss);
		JSONObject  jsono =new JSONObject(output);
		return jsono;
	}
	
	
	
	

	
	
	
	public static void main(String[] args) throws UnsupportedEncodingException, JSONException {
		
		System.out.println(PHPRPCUtils.getUserInfo("meng_xh"));
		System.out.println(PHPRPCUtils.getUserInfo("yaoxia"));
		//System.out.println(PHPRPCUtils.getUserInfo("lvdi_1124"));
//		
//		System.out.println(PHPRPCUtils.GetSchool("2134000001000633392"));
//		
//		System.out.println(PHPRPCUtils.GetArea("1124"));
		
		
		//System.out.println(PHPRPCUtils.GetUserSchool(0,10,"2134000017000480616"));
		//System.out.println(PHPRPCUtils.GetUserOrg(0,10,"2134000017000480616"));
	}
	
	
	
	  public static String decodeUnicode(String theString) {      
	        
	        char aChar;      
	       
	         int len = theString.length();      
	       
	        StringBuffer outBuffer = new StringBuffer(len);      
	       
	        for (int x = 0; x < len;) {      
	       
	         aChar = theString.charAt(x++);      
	       
	         if (aChar == '\\') {      
	       
	          aChar = theString.charAt(x++);      
	       
	          if (aChar == 'u') {      
	       
	           // Read the xxxx      
	       
	           int value = 0;      
	       
	           for (int i = 0; i < 4; i++) {      
	       
	            aChar = theString.charAt(x++);      
	       
	            switch (aChar) {      
	       
	            case '0':      
	       
	            case '1':      
	       
	            case '2':      
	       
	            case '3':      
	       
	           case '4':      
	       
	            case '5':      
	       
	             case '6':      
	              case '7':      
	              case '8':      
	              case '9':      
	               value = (value << 4) + aChar - '0';      
	               break;      
	              case 'a':      
	              case 'b':      
	              case 'c':      
	              case 'd':      
	              case 'e':      
	              case 'f':      
	               value = (value << 4) + 10 + aChar - 'a';      
	              break;      
	              case 'A':      
	              case 'B':      
	              case 'C':      
	              case 'D':      
	              case 'E':      
	              case 'F':      
	               value = (value << 4) + 10 + aChar - 'A';      
	               break;      
	              default:      
	               throw new IllegalArgumentException(      
	                 "Malformed   \\uxxxx   encoding.");      
	              }      
	       
	            }      
	             outBuffer.append((char) value);      
	            } else {      
	             if (aChar == 't')      
	              aChar = '\t';      
	             else if (aChar == 'r')      
	              aChar = '\r';      
	       
	             else if (aChar == 'n')      
	       
	              aChar = '\n';      
	       
	             else if (aChar == 'f')      
	       
	              aChar = '\f';      
	       
	             outBuffer.append(aChar);      
	       
	            }      
	       
	           } else     
	       
	           outBuffer.append(aChar);      
	       
	          }      
	       
	          return outBuffer.toString();      
	       
	         }     
}
