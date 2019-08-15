package com.fulaan.cache;

import com.pojo.app.SessionValue;
import org.bson.types.ObjectId;

import java.text.MessageFormat;
import java.util.Map;

/**
 * 缓存处理类
 * @author fourer
 *
 */
public final class CacheHandler {

	//存储session 
	public static final String CACHE_SESSION_KEY="s_{0}";
	//存储string  
	public static final String CACHE_STRING_KEY="str_{0}";
	//学生错题集数据缓存 ed_用户ID_排序
	public static final String CACHE_ERRORITEM_DATA="ed_{0}_{1}";
	//学生错题集开始数缓存  er_用户ID_开始数
	public static final String CACHE_ERRORITEM_BEGIN="er_{0}_{1}";
	//用户信件数量
	public static final String CACHE_USER_LETTER_COUNT="lc_{0}";
	//用户日历；某月哪天有事件
	public static final String CACHE_USER_CALENDAR="cal_{0}_{1}";
	//用户缓存的Key
	public static final String CACHE_USER_KEY="ck_{0}";
	//作业筛选项   hw_用户ID
	public static final String CACHE_HWSECTION_KEY="hw_{0}";
    //云资源默认加载
	public static final String CACHE_CLOUDRESOURCE_DEFAULT_KEY="cld_def";
	//管理统计key   ms_查询条件
	public static final String CACHE_SCHOOL_MANAGER_KEY="sm_{0}";
	//验证码缓存的Key
	public static final String CACHE_VALIDATE_CODE="vc_{0}";
	//用户IP地址缓存的Key
	public static final String CACHE_USER_IP="ip_{0}";
	//用户是第一次登录key   fl_查询条件
	public static final String CACHE_USER_FIRST_LOGIN="fl_{0}";
	//学生或学生家长用户每隔5分钟才可以再发布校园安全   ssk_用户id
	public static final String CACHE_PUBLISH_SCHOOL_SECURITY="pss_{0}";
	//用户IP地址登录次数缓存的Key
	public static final String CACHE_USER_LOGIN_COUNT="ulc_{0}";
	//用户IP地址登录次数缓存的Key
	public static final String CACHE_TEACHER_INTERACT_LESSON="til_{0}";
	//资源字典表  资源节点id
	public static final String CACHE_RESDICTIONARY="resd_{0}";
	//用户购物车
	public static final String CACHE_ESHOPPING_CARS="car_{0}";
	//短信验证
	public static final String CACHE_SHORTMESSAGE="smc_{0}";
	//已经发送短信验证的手机
	public static final String CACHE_MOBILE="smk_{0}";
	//sso ticket
	public static final String USER_SSO_TICKET="sso_{0}";
	//jsessionId_email
	public static final String SESSIONID_EMALL="jsi_{0}";
	//学校总人数
	public static final String SCHOOL_USER_COUNT="st_{0}_{1}";
	//存储用户key和IP的对应关系
	public static final String CACHE_USER_KEY_IP="uip_{0}";
	//存储学校角色导航;k_学校id_用户角色
	public static final String K6KT_SCHOOL_ROLE_NAVS="kt_{0}_{1}";
	//存储学校角色磁贴;k_学校id_用户角色
	public static final String K6KT_SCHOOL_ROLE_CITIE="kt:ct:{0}:{1}";
	//存储学校角色磁贴;k_学校id_用户角色
	public static final String K6KT_SCHOOL_ROLE_APPCITIE="kt:app:ct:{0}:{1}";

	//拓展课角色
	public static final String CACHE_EXPAND_ROLE_DEFAULT_KEY="expand_role_{0}";
	
	//app 存储用户科目
	public static final String K6KT2_USER_SUBJECT_LIST_KEY="k2:u:s:{0}";
	//app 用户收集参数key 0用户 1页面tag
	public static final String K6KT2_USER_DATA_COLLECTION="k2:{0}:{1}";
	//存储学生开始考试时间
	public static final String K6KT_STU_EXAM_BEGIN_TIME="kt:exb:{0}:{1}";
	//学生正在答题
	public static final String K6KT_STU_EXAM_KEY="K2:i:{0}";
	
	//教室预定
	public static final String K6KT2_CLASSROOM_RESERVE="k2:{0}";

	//学生手环账户
	public static final String K6KT2_BRACELET_ACCOUNT_KEY="K2:bc:{0}";

	// 成绩分析学生个人成绩  0考试id 1学籍类型
	public static final String K6KT2_CJANALYZE_PERSONAL = "K2:cp:{0}:{1}";
	// 成绩分析分析结果 0：参数id
	public static final String K6KT2_CJANALYZE_RESULT = "k2:cr:{0}";
	/**
	 * 得到session
	 * @param userId
	 * @return
	 */
	public static SessionValue getSessionValue(String userId)
	{
		String key=MessageFormat.format(CACHE_SESSION_KEY, userId);
		Map<String,String> m=RedisUtils.getMap(key);
		return new SessionValue(m);
	}
	
	
	/**
	 * 存储SessionValue
	 * @param userKey
	 * @param sv
	 * @param timeouts 单位秒
	 */
	public static void cacheSessionValue(String userKey,SessionValue sv,int timeouts)
	{
		String key=MessageFormat.format(CACHE_SESSION_KEY, userKey);
		RedisUtils.cacheMap(key, sv.getMap(), timeouts);
	}
	
	
	/**
	 * 存储UserKey
	 * @param userId
	 * @param sv
	 * @param timeouts 单位秒
	 */
	public static void cacheUserKey(String userId,String userKey,int timeouts)
	{
		String key=MessageFormat.format(CACHE_USER_KEY, userId);
		RedisUtils.cacheString(key, userKey, timeouts);
	}
	
	
	
	/**
	 * 得到userKey
	 * @param userId
	 * @return
	 */
	public static String getUserKey(String userId)
	{
		String key=MessageFormat.format(CACHE_USER_KEY, userId);
		return RedisUtils.getString(key);
	}
	
	

	/**
	 * caceh maps
	 * @param key
	 * @param value
	 * @param timeouts
	 */
	public static void cache(String key,Map<String,String> value,int timeouts)
	{
		RedisUtils.cacheMap(key, value, timeouts);
	}
	
	
	/**
	 * 存储
	 * @param key
	 * @param value
	 * @param timeouts 单位秒
	 */
	public static void cache(String key,String value,int timeouts)
	{
		RedisUtils.cacheString(key, value, timeouts);
	}
	
	/**
	 * 缓存bytes
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public static void cache(String key,byte[] value,int seconds)
	{
		RedisUtils.cacheBytes(key, value, seconds);
	}

	/**
	 * 得到String key
	 * @param format  类似于str_{0}
	 * @param data
	 * @return
	 */
	public static String getKeyString(String format,Object... data)
	{
		String key=MessageFormat.format(format, data);
		return key;
	}
	
	/**
	 * 得到String value
	 * @param key
	 * @return
	 */
	public static String getStringValue(String key)
	{
		return RedisUtils.getString(key);
	}
	
	
	/**
	 * 得到Map value
	 * @param key
	 * @return
	 */
	public static Map<String,String> getMapValue(String key)
	{
		return RedisUtils.getMap(key);
	}
	
	/**
	 * 得到byte[]
	 * @param key
	 * @return
	 */
	public static  byte[] getBytesValue(String key)
	{
		return RedisUtils.getBytes(key);
	}
	
	
	/**
	 * 删除key
	 * @param key
	 */
	public static void deleteKey(String format,Object... data)
	{
		String key=MessageFormat.format(format, data);
		RedisUtils.deleteKey(key);
	}
	
	/**
	 * 删除key
	 * @param key
	 */
	public static void deleteKey(String key)
	{
		RedisUtils.deleteKey(key);
	}
	
	
	
	
	public static void main(String[] args) {

		
		System.out.println(CacheHandler.getKeyString(CacheHandler.CACHE_ERRORITEM_DATA,new ObjectId(),2));
		
	}
	
}
