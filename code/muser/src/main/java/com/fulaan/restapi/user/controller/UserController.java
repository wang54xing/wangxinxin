package com.fulaan.restapi.user.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.restapi.user.service.UserService;
import com.pojo.app.FieldValuePair;
import com.pojo.app.SessionValue;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;

/**
 * user micro service rest api
 * @author fourer
 *
 */
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {
	
	private static final Logger logger =Logger.getLogger(UserController.class);
	
	private UserService userService=new UserService();
	
	
	
	
	/**
	 * 用户登录
	 * @param loginName
	 * @param passWord
	 * @return
	 */
	@SessionNeedless
	@RequestMapping(value = "/login",method= RequestMethod.POST)
	@ResponseBody
	public RespObj login(@RequestParam String loginName,@RequestParam String pwd,@CookieValue("msui") String cookieValue,HttpServletResponse response)
	{
		boolean isLogined=false;
		if(StringUtils.isNotBlank(cookieValue))
		{
			SessionValue sv=CacheHandler.getMicroUserSessionValue(cookieValue);
			if(null!=sv && !sv.isEmpty())
			{
				isLogined=true;
			}
		}
		if(isLogined)
		{
			logger.info(loginName+" already logined!!");
			return RespObj.SUCCESS;
		}
		
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		if(StringUtils.isBlank(cookieValue) || StringUtils.isBlank(pwd))
		{
			obj.setMessage("loginName and pwd must not be empty");
			return obj;
		}
		logger.info("try login;loginName="+loginName);
		
		UserEntry ue=userService.getUserEntry(loginName);
		if(null==ue)
		{
			obj.setMessage("Can not find User by loginName:"+loginName);
			return obj;
		}
		
		if(!ue.getPassword().equalsIgnoreCase(MD5Utils.getMD5String(pwd)) && !ue.getPassword().equalsIgnoreCase(pwd))
		{
			obj.setMessage("pwd error");
			return obj;
		}
		
		if(Constant.ONE==ue.getUserType()) //有效时间用户 
		{
			long validBeginTime =ue.getValidBeginTime();
			long validTime=ue.getValidTime();
			if(0L==validBeginTime) //第一次登陆
			{
			  userService.update(ue.getID(), "vabt", System.currentTimeMillis());
			}
			else
			{
				if(System.currentTimeMillis()>validBeginTime+validTime*1000)
				{
					obj.setMessage("Time expired");
					return obj;
				}
			}
		}
		
		SessionValue value =new SessionValue();
		value.setId(ue.getID().toString());
		value.setUserName(ue.getUserName());
		value.setSchoolId(ue.getSchoolID().toString());
		
		//放入缓存
		String uuid =UUID.randomUUID().toString();
		//s_key
		CacheHandler.cacheMicroUserSessionValue(uuid, value, Constant.SECONDS_IN_DAY);
		
		try {
			Cookie cookie = new Cookie(Constant.MICRO_USER_COOKIE_USER_KEY,uuid);
			cookie.setMaxAge(Constant.SECONDS_IN_DAY);
			cookie.setPath(Constant.BASE_PATH);
			response.addCookie(cookie);
		} catch (Exception e1) {
			logger.error("", e1);
		}
		
		return RespObj.SUCCESS;
	}
	
	
	/**
	 * 用户退出
	 * @return
	 */
	@RequestMapping(value = "/logout",method= RequestMethod.GET)
	@ResponseBody
	public RespObj logout(HttpServletRequest request,HttpServletResponse response)
	{
		Cookie cookies[] = request.getCookies();
		Cookie c = null;
		for (int i = 0; i < cookies.length; i++) {
			c = cookies[i];
			c.setMaxAge(0);
			if(c.getName().equals(Constant.MICRO_USER_COOKIE_USER_KEY))
			{
				CacheHandler.deleteKey(CacheHandler.MICRO_USER_KEY,
						c.getValue());
			}
		}
		return RespObj.SUCCESS;
	}
	
	
	/**
	 * 删除用户
	 * @return
	 */
	@SessionNeedless
	@RequestMapping(value = "/delete/{userId}",method= RequestMethod.GET)
	@ResponseBody
	public RespObj delete(@PathVariable @ObjectIdType ObjectId userId)
	{
		logger.info("Delete user :"+userId.toString());
		userService.update(userId, "ir", Constant.ONE);
		return RespObj.SUCCESS;
	}
	
	/**
	 * 得到登录者基本信息
	 * @param userId
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/infos") 
	@ResponseBody
	public RespObj getLoginUserInfo(@CookieValue("msui") String cookieValue )
	{
		if(StringUtils.isBlank(cookieValue))
		{
			return new RespObj(Constant.FAILD_CODE, "Can not find user key");
		}
		SessionValue sv=CacheHandler.getMicroUserSessionValue(cookieValue);
		if(null==sv || sv.isEmpty())
		{
			return new RespObj(Constant.FAILD_CODE, "user info is empty");
		}
		
		RespObj obj= new RespObj(Constant.SUCCESS_CODE, sv);
		return obj;
	}
	
	
	/**
	 * 得到用户基本信息
	 * @param userId
	 * @return
	 */
	@RequestMapping("/infos/{userId}")
	@ResponseBody
	public RespObj getUserInfo(@PathVariable @ObjectIdType  ObjectId userId )
	{
		UserEntry ue=userService.getUserEntry(userId, Constant.FIELDS);
		if(null==ue)
		{
			return new RespObj(Constant.FAILD_CODE, "can not find user");
		}
		UserDetailInfoDTO dto =new UserDetailInfoDTO();
		
		try
		{
			dto =new UserDetailInfoDTO(ue);
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		RespObj obj= new RespObj(Constant.SUCCESS_CODE, dto);
		return obj;
	}
	
	
	/**
	 * 用户设置
	 * @param set
	 * @return
	 */
	@RequestMapping("/update/{userId}") 
	@ResponseBody
	public RespObj userUpdate(@PathVariable @ObjectIdType  ObjectId userId,UserSets set)
	{
		if(null==set)
		{
			return new RespObj(Constant.FAILD_CODE, "not user set value");
		}
		List<FieldValuePair> paramList =new ArrayList<FieldValuePair>();
		if(StringUtils.isNotBlank(set.getRole()))
		{
			if(!ValidationUtils.isNumber(set.getRole()))
			{
				return new RespObj(Constant.FAILD_CODE, "role is wrong!the role is a number");
			}
			paramList.add(new FieldValuePair("r", Integer.valueOf(set.getRole())));
		}
		if(StringUtils.isNotBlank(set.getNickName()))
		{
			if(!ValidationUtils.isRequestUserNickName(set.getNickName()))
			{
				return new RespObj(Constant.FAILD_CODE, "nickname is wrong");
			}
			paramList.add(new FieldValuePair("nnm", set.getNickName()));
		}
		
		if(StringUtils.isNotBlank(set.getPassword()))
		{
			try
			{
			   paramList.add(new FieldValuePair("pw", MD5Utils.getMD5(set.getPassword())));
			}catch(Exception ex)
			{
				
			}
		}
		if(StringUtils.isNotBlank(set.getMobile()))
		{
			if(!ValidationUtils.isRequestModile(set.getMobile()))
			{
				return new RespObj(Constant.FAILD_CODE, "mobile is wrong");
			}
			paramList.add(new FieldValuePair("mn", set.getNickName()));
		}
		if(StringUtils.isNotBlank(set.getTelephone()))
		{
			paramList.add(new FieldValuePair("pn", set.getTelephone()));
		}
		if(StringUtils.isNotBlank(set.getEmail()))
		{
			if(!ValidationUtils.isEmail(set.getEmail()))
			{
				return new RespObj(Constant.FAILD_CODE, "email is wrong");
			}
			paramList.add(new FieldValuePair("e", set.getEmail()));
		}
		
		FieldValuePair[] valuePairArr =new FieldValuePair[]{};
		userService.update(userId, paramList.toArray(valuePairArr));
		return RespObj.SUCCESS;
	}
	

	/**
	 * 根据用户Id查询用户信息
	 * @param userIds 用户id,用","连接起来
	 * @param field
	 * @return
	 */
	@RequestMapping(value = "/infos/userIds",method= RequestMethod.POST)
	@ResponseBody
	public RespObj getUserInfo(String userIds,int field)
	{
		if(StringUtils.isBlank(userIds))
		{
			RespObj obj= new RespObj(Constant.FAILD_CODE, "userIds is wrong");
			return obj;
		}
		List<ObjectId> userIdList =MongoUtils.convert(userIds);
		
		List<UserEntry> userEntryList=userService.getUserEntryList(userIdList, field);
		
		List<UserDetailInfoDTO> userDTOList =new ArrayList<UserDetailInfoDTO>();
		
		for(UserEntry ue:userEntryList)
		{
			try
			{
			  userDTOList.add(new UserDetailInfoDTO(ue));
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		
		RespObj obj= new RespObj(Constant.SUCCESS_CODE, userDTOList);
		return obj;
	}
	
	/**
	 * 根据用户Id查询用户信息
	 * @param userIds 用户id,用","连接起来
	 * @param field
	 * @return
	 */
	@RequestMapping(value = "/infos/school",method= RequestMethod.POST)
	@ResponseBody
	public RespObj getUserInfo(String schoolIds ,String roles,String name,int field,int skip,int limit)
	{
		if(StringUtils.isBlank(schoolIds) )
		{
			RespObj obj= new RespObj(Constant.FAILD_CODE, "schoolId are wrong");
			return obj;
		}
		List<ObjectId> schoolidObjs =MongoUtils.convert(schoolIds);
		
		List<UserRole> userRoleList =new ArrayList<UserRole>();
		String[] rolesArr=roles.split(Constant.COMMA);
		for(String r:rolesArr)
		{
			if(!ValidationUtils.isNumber(r))
			{
				return new RespObj(Constant.FAILD_CODE, "roles are wrong");
			}
			UserRole ur=UserRole.getUserRole(Integer.valueOf(r));
			if(null==ur)
			{
				return new RespObj(Constant.FAILD_CODE, "roles are wrong");
			}
			userRoleList.add(ur);
		}
		
		
		
		List<UserEntry> userEntryList=new ArrayList<UserEntry>();
		try {
			userEntryList = userService.getUserEntryList(schoolidObjs, userRoleList,name,skip,limit,field);
		} catch (Exception e) {
			logger.error("", e);
		}
		
		List<UserDetailInfoDTO> userDTOList =new ArrayList<UserDetailInfoDTO>();
		
		for(UserEntry ue:userEntryList)
		{
			try
			{
			  userDTOList.add(new UserDetailInfoDTO(ue));
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		
		RespObj obj= new RespObj(Constant.SUCCESS_CODE, userDTOList);
		return obj;
	}
	
	
	
	
	public static class UserSets
	{
		private String role;
		private String nickName;
		private String password;
		private String mobile;
		private String telephone;
		private String email;
		private String qq;
		private String weixin;
		private String weibo;
		private String lastTime;
		private String sid;
		
		
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getQq() {
			return qq;
		}
		public void setQq(String qq) {
			this.qq = qq;
		}
		public String getWeixin() {
			return weixin;
		}
		public void setWeixin(String weixin) {
			this.weixin = weixin;
		}
		public String getWeibo() {
			return weibo;
		}
		public void setWeibo(String weibo) {
			this.weibo = weibo;
		}
		public String getLastTime() {
			return lastTime;
		}
		public void setLastTime(String lastTime) {
			this.lastTime = lastTime;
		}
		public String getSid() {
			return sid;
		}
		public void setSid(String sid) {
			this.sid = sid;
		}
	}
	
	
}
