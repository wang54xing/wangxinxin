package com.fulaan.user.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Collator;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jasig.cas.client.rest.CASRestful;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.log.service.LogService;
import com.fulaan.myschool.controller.ExcelStudentRecord;
import com.fulaan.myschool.controller.ExcelTeacherRecord;
import com.fulaan.myschool.controller.ExcelTeacherRecord.GradeAndClass;
import com.fulaan.myschool.controller.UserManagerController;
import com.fulaan.school.service.SchoolService;
import com.fulaan.schoolbase.service.SchoolBaseService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.PHPRPCUtils;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.Platform;
import com.pojo.app.RegionEntry;
import com.pojo.app.SessionValue;
import com.pojo.log.LogType;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.school.SchoolType;
import com.pojo.school.SubjectType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.LoginLog;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.UnBindException;
import com.sys.exceptions.UnLoginException;
import com.sys.mails.MailUtils;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.HttpClientUtils;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;

/**
 * 用户controller,处理用户请求，比如登录
 * @author fourer
 *
 */

@Controller
@RequestMapping("/user")
public class
		UserController  extends BaseController implements ApplicationContextAware{
	private static final Logger logger =Logger.getLogger(UserController.class);
	private static final Logger loginLogger =Logger.getLogger("LOGIN");

	
	
	private UserService userService =new UserService();

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private LogService logService;
	
	@Autowired
	private SchoolBaseService schoolBaseService;
	
	
	public final static String cas_url = Resources.getProperty("ssoservice.cas.url","http://ah.sso.cycore.cn/sso");
	
	
	public final static String service_url = Resources.getProperty("ssoservice.service.url","http://www.k6kt.com/user/sso/login.do");
	
	
	
	public final static String cas_login_url = Resources.getProperty("ssoservice.cas.login.url","http://ah.sso.cycore.cn/sso/login");
	
	//找回密码邮箱链接
	public final static String linkFormat=Resources.getProperty("domain","http://www.k6kt.com")+"/user/email/callback.do?ukId={0}&email={1}&token={2}";
	
	//k6kt单点登录URl
	private final static String K6KT_SSO_URL="http://www.mysso.com";
	
	/**
	 * 学校自定制磁贴
	 */
	public static Set<String> schoolCustomCiteSet =new HashSet<String>();
	
	static
	{
		schoolCustomCiteSet.add("navs11");
		schoolCustomCiteSet.add("navs22");
		schoolCustomCiteSet.add("navs26");
	}
	/**
	 * 用户登录
	 * @param name
	 * @param pwd
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/login")
	@ResponseBody
	public RespObj login (String name,String pwd,String veryCode,HttpServletResponse response,HttpServletRequest request)
	{
		RespObj faild =new RespObj(Constant.FAILD_CODE);
		String ip = getIP(request);

        //判断用户登录平台，只对PC登录用户进行密码输入错误3次，需要验证码
		String client = request.getHeader("User-Agent");
		Platform pf = null;
		if (client.contains("iOS")) {
			pf = Platform.IOS;
		} else if (client.contains("Android")){
			pf = Platform.Android;
		} else {
			pf = Platform.PC;
		}

		logger.info("try login;the name="+name+";pwd="+pwd);
		
		//数据库验证
		UserEntry e = getUserEntry(name);
		if(null==e)
		{
			faild.setMessage("accountError");
			return faild;
		}

		
		if(!ValidationUtils.isRequestPassword(pwd)|| (!e.getPassword().equalsIgnoreCase(MD5Utils.getMD5String(pwd)) && !e.getPassword().equalsIgnoreCase(pwd)) )
		{

			if(!ValidationUtils.isRequestPassword(pwd))
			{
				faild.setMessage("密码非法");
				return faild;
			}
			if(!e.getPassword().equalsIgnoreCase(MD5Utils.getMD5String(pwd))&& !e.getPassword().equalsIgnoreCase(pwd))
			{
				faild.setMessage("密码错误");
				return faild;
			}
		}

		try
		{
			if(0l==e.getLastActiveDate()){
				String params=e.getID().toString()+0l;
				String flkey= CacheHandler.getKeyString(CacheHandler.CACHE_USER_FIRST_LOGIN,params);
				CacheHandler.cache(flkey,Constant.USER_FIRST_LOGIN, Constant.SESSION_FIVE_MINUTE);
			}
			//更新最后登录日期
			userService.updateLastActiveDate(e.getID());
		}catch(Exception ex)
		{
			logger.error("",ex);
		}
		if(Constant.ONE==e.getUserType()) //有效时间用户 
		{
			long validBeginTime =e.getValidBeginTime();
			long validTime=e.getValidTime();
			if(0L==validBeginTime) //第一次登陆
			{
				try {
					userService.update(e.getID(), "vabt", System.currentTimeMillis());
				} catch (IllegalParamException e1) {
					
				};
			}
			else
			{
				if(System.currentTimeMillis()>validBeginTime+validTime*1000)
				{
					faild.setMessage("该用户已经失效");
					return faild;
				}
			}
		}
		Map<String,String> schoolMap = new HashMap<String,String>();
//		SchoolEntry schoolEntry = null;
		try {
//			schoolEntry = schoolService.getSchoolEntryByUserId(e.getID());
			String result = schoolBaseService.getSchool(e.getSchoolID());
			JSONObject dataJson = new JSONObject(result);
			JSONObject row = dataJson.getJSONObject("message");
			schoolMap.put("id",row.getString("id"));
			schoolMap.put("name",row.getString("name"));
			schoolMap.put("logo",row.getString("schoolLogo"));
			if (row.getInt("status")==1) {
				faild.setMessage("该学校已停用，请联系复兰管理员！");
				return faild;
			}
		}
		catch (Exception ie){
			logger.error("Can not find school for user:"+e);
		}

		//处理SessionValue
		SessionValue value =new SessionValue();
		value.setId(e.getID().toString());
		value.setUserName(e.getUserName());
		value.setRealName(e.getNickName());
		value.setSex(e.getSex());
		value.setK6kt(e.getK6KT());
		if(e.getK6KT() == 1) {//k6kt用户
			value.setSchoolId(e.getSchoolID().toString());
			value.setSchoolLogo(schoolMap.get("logo"));
			value.setUserRole(e.getRole());
			value.setAvatar(e.getAvatar());
			value.setUserPermission(e.getPermission());
			value.setUserRemovePermission(e.getRemovePermission());
			value.setExperience(e.getExperiencevalue());
			value.setChatid(e.getChatId());
//			value.setSchoolNavs(schoolEntry.getSchoolNavs());
			value.setSchoolName(schoolMap.get("name"));
			value.setCoupon(e.getCoupon());
		}
		
		request.setAttribute(BaseController.SESSION_VALUE, value);
		
		//放入缓存
		ObjectId cacheUserKey =new ObjectId();
		//ck_key
		CacheHandler.cacheUserKey(e.getID().toString(), cacheUserKey.toString(), Constant.SECONDS_IN_DAY);
		//s_key
		CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
		//处理cookie
		Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY,cacheUserKey.toString());
		userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
		userKeycookie.setPath(Constant.BASE_PATH);
		response.addCookie(userKeycookie);
        //存储用户key和IP的对应关系,加强sso安全
		String ipKey=CacheHandler.getKeyString(CacheHandler.CACHE_USER_KEY_IP, cacheUserKey.toString());
		CacheHandler.cache(ipKey, ip, Constant.SECONDS_IN_DAY);
		Cookie couCookie = new Cookie("coupon","false");
		couCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
		couCookie.setPath(Constant.BASE_PATH);
		response.addCookie(couCookie);

		try {
			Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY,URLEncoder.encode(name, Constant.UTF_8));
			nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
			nameCookie.setPath(Constant.BASE_PATH);
			response.addCookie(nameCookie);
		} catch (UnsupportedEncodingException e1) {
			logger.error("", e1);
		}
		try
		{
			
//			RegionEntry region=schoolService.getRegionEntry(dto.getRegionId());
			//获取客户端信息
			LoginLog loginLog =new LoginLog();
			loginLog.setIpAddr(ip+e.getUserName());
			loginLog.setPlatform(pf.getName());
			loginLog.setUserId(e.getID().toString());
			loginLog.setUserName(e.getUserName());
			if(e.getK6KT() == 1) {//k6kt用户
				loginLog.setRole(e.getRole());
				loginLog.setSchoolId(schoolMap.get("id"));
				loginLog.setSchoolName(schoolMap.get("name"));
//				loginLog.setCity(region.getName());
			}

			loginLogger.info(loginLog);
			logService.insertLog(e,pf,LogType.CLICK_LOGIN,"login.do");
		}catch(Exception ex)
		{
		}
	
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		respObj.setMessage(value);
		return respObj;
	}
	
	
	/**
	 * 用户登录
	 * @param name
	 * @param pwd
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/test/login")
	public String testLogin ( @RequestParam(required=false,defaultValue="siri") String name, @RequestParam(required=false,defaultValue="123456") String pwd,String veryCode,HttpServletResponse response,HttpServletRequest request)
	{
		RespObj faild =new RespObj(Constant.FAILD_CODE);
		String ip = getIP(request);

        //判断用户登录平台，只对PC登录用户进行密码输入错误3次，需要验证码
		String client = request.getHeader("User-Agent");
		Platform pf = null;
		if (client.contains("iOS")) {
			pf = Platform.IOS;
		} else if (client.contains("Android")){
			pf = Platform.Android;
		} else {
			pf = Platform.PC;
		}

		logger.info("try login;the name="+name+";pwd="+pwd);
		
		//数据库验证
		UserEntry e = getUserEntry(name);
		

		try
		{
			if(0l==e.getLastActiveDate()){
				String params=e.getID().toString()+0l;
				String flkey= CacheHandler.getKeyString(CacheHandler.CACHE_USER_FIRST_LOGIN,params);
				CacheHandler.cache(flkey,Constant.USER_FIRST_LOGIN, Constant.SESSION_FIVE_MINUTE);
			}
			//更新最后登录日期
			userService.updateLastActiveDate(e.getID());
		}catch(Exception ex)
		{
			logger.error("",ex);
		}
		if(Constant.ONE==e.getUserType()) //有效时间用户 
		{
			long validBeginTime =e.getValidBeginTime();
			long validTime=e.getValidTime();
			if(0L==validBeginTime) //第一次登陆
			{
				try {
					userService.update(e.getID(), "vabt", System.currentTimeMillis());
				} catch (IllegalParamException e1) {
					
				};
			}
			else
			{
				if(System.currentTimeMillis()>validBeginTime+validTime*1000)
				{
					faild.setMessage("该用户已经失效");
					//return faild;
				}
			}
		}

//		SchoolEntry schoolEntry = null;
//		com.fulaan.schoolbase.dto.SchoolDTO dto = null;
		Map<String,String> schoolMap = new HashMap<String,String>();
		try {
//			schoolEntry = schoolService.getSchoolEntryByUserId(e.getID());
			String result = schoolBaseService.getSchool(e.getSchoolID());
			JSONObject dataJson = new JSONObject(result);
			JSONObject row = dataJson.getJSONObject("message");
			schoolMap.put("id",row.getString("id"));
			schoolMap.put("name",row.getString("name"));
			schoolMap.put("logo",row.getString("schoolLogo"));
		}
		catch (Exception ie){
			logger.error("Can not find school for user:"+e);
		}

		//处理SessionValue
		SessionValue value =new SessionValue();
		value.setId(e.getID().toString());
		value.setUserName(e.getUserName());
		value.setRealName(e.getNickName());
		value.setSex(e.getSex());
		value.setK6kt(e.getK6KT());
		if(e.getK6KT() == 1) {//k6kt用户
			value.setSchoolId(e.getSchoolID().toString());
			value.setSchoolLogo(schoolMap.get("logo"));
			value.setUserRole(e.getRole());
			value.setAvatar(e.getAvatar());
			value.setUserPermission(e.getPermission());
			value.setUserRemovePermission(e.getRemovePermission());
			value.setExperience(e.getExperiencevalue());
			value.setChatid(e.getChatId());
//			value.setSchoolNavs(schoolEntry.getSchoolNavs());
//			if(null!=dto)
//			{
			  value.setSchoolName(schoolMap.get("name"));
//			}
			value.setCoupon(e.getCoupon());
		}
		
		request.setAttribute(BaseController.SESSION_VALUE, value);
		
		//放入缓存
		ObjectId cacheUserKey =new ObjectId();
		//ck_key
		CacheHandler.cacheUserKey(e.getID().toString(), cacheUserKey.toString(), Constant.SECONDS_IN_DAY);
		//s_key
		CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
		//处理cookie
		Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY,cacheUserKey.toString());
		userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
		userKeycookie.setPath(Constant.BASE_PATH);
		response.addCookie(userKeycookie);
        //存储用户key和IP的对应关系,加强sso安全
		String ipKey=CacheHandler.getKeyString(CacheHandler.CACHE_USER_KEY_IP, cacheUserKey.toString());
		CacheHandler.cache(ipKey, ip, Constant.SECONDS_IN_DAY);
		Cookie couCookie = new Cookie("coupon","false");
		couCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
		couCookie.setPath(Constant.BASE_PATH);
		response.addCookie(couCookie);

		try {
			Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY,URLEncoder.encode(name, Constant.UTF_8));
			nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
			nameCookie.setPath(Constant.BASE_PATH);
			response.addCookie(nameCookie);
		} catch (UnsupportedEncodingException e1) {
			logger.error("", e1);
		}
		try
		{
			
//			RegionEntry region=schoolService.getRegionEntry(dto.getRegionId());
			//获取客户端信息
			LoginLog loginLog =new LoginLog();
			loginLog.setIpAddr(ip+e.getUserName());
			loginLog.setPlatform(pf.getName());
			loginLog.setUserId(e.getID().toString());
			loginLog.setUserName(e.getUserName());
			if(e.getK6KT() == 1) {//k6kt用户
				loginLog.setRole(e.getRole());
				loginLog.setSchoolId(schoolMap.get("id"));
				loginLog.setSchoolName(schoolMap.get("name"));
//				loginLog.setCity(region.getName());
			}

			loginLogger.info(loginLog);
			logService.insertLog(e,pf,LogType.CLICK_LOGIN,"login.do");
		}catch(Exception ex)
		{
		}
	
		return "redirect:/user/homepage.do";
	}
	
	/**
	 * 通过sso登录
	 * @param response
	 * @param request
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@ResponseBody
	@SessionNeedless
	@RequestMapping("/k6kt/sso/login")
	public RespObj k6ktssoLogin(HttpServletResponse response,HttpServletRequest request) throws ClientProtocolException, IOException
	{
		String token=request.getParameter("token");
		String cookieValue =request.getParameter("cValue");
		
		logger.info("k6kt.sso.login token="+token);
		logger.info("k6kt.sso.login cookieValue="+cookieValue);
		
		SessionValue value =new SessionValue();
		
		boolean isLogin =false;
		//放入缓存
		String cacheUserKey =new ObjectId().toString();
		if(StringUtils.isBlank(cookieValue))
		{
			String uid=  HttpClientUtils.get(K6KT_SSO_URL+"/sso/simUserInfo.do?ssoKey="+request.getParameter("token"));
		    UserEntry e = userService.searchUserId(new ObjectId(uid));
			//		SchoolEntry schoolEntry = null;
//			com.fulaan.schoolbase.dto.SchoolDTO dto = null;
			Map<String,String> schoolMap = new HashMap<String,String>();
			try {
//			schoolEntry = schoolService.getSchoolEntryByUserId(e.getID());
				String result = schoolBaseService.getSchool(e.getSchoolID());
				JSONObject dataJson = new JSONObject(result);
				JSONObject row = dataJson.getJSONObject("message");
				schoolMap.put("id",row.getString("id"));
				schoolMap.put("name",row.getString("name"));
				schoolMap.put("logo",row.getString("schoolLogo"));
			} catch (Exception ie){
					logger.error("Can not find school for user:"+e);
			}
			
			//处理SessionValue
			
			value.setId(e.getID().toString());
			value.setUserName(e.getUserName());
			value.setRealName(e.getNickName());
			value.setK6kt(e.getK6KT());
			if(e.getK6KT() == 1) {//k6kt用户
				value.setSchoolId(e.getSchoolID().toString());
				value.setSchoolLogo(schoolMap.get("logo"));
				value.setUserRole(e.getRole());
				value.setAvatar(e.getAvatar());
				value.setUserPermission(e.getPermission());
				value.setUserRemovePermission(e.getRemovePermission());
				value.setExperience(e.getExperiencevalue());
				value.setChatid(e.getChatId());
//				value.setSchoolNavs(schoolEntry.getSchoolNavs());
				value.setSchoolName(schoolMap.get("name"));
			}
			isLogin=true;
		}
		else
		{
			value =CacheHandler.getSessionValue(cookieValue);
			if(null!=value && !value.isEmpty())
			{
				cacheUserKey=cookieValue;
				String ip = getIP(request);
				//存储用户key和IP的对应关系,加强sso安全
				String ipKey=CacheHandler.getKeyString(CacheHandler.CACHE_USER_KEY_IP, cacheUserKey.toString());
				String cacheIP=CacheHandler.getStringValue(ipKey);
				
//				if(ip.equalsIgnoreCase(cacheIP))
//				{
					isLogin=true;
//				}
			}
		}
		
		
	
		if(isLogin)
		{
			//ck_key
			CacheHandler.cacheUserKey(value.getId(), cacheUserKey, Constant.SECONDS_IN_DAY);
			//s_key
			CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
			//处理cookie
			Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY,cacheUserKey.toString());
			userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
			userKeycookie.setPath(Constant.BASE_PATH);
			response.addCookie(userKeycookie);
			
			try {
				Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY,URLEncoder.encode(value.getUserName(), Constant.UTF_8));
				nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
				nameCookie.setPath(Constant.BASE_PATH);
				response.addCookie(nameCookie);
			} catch (UnsupportedEncodingException e1) {
				logger.error("", e1);
			}
		}
		
		return isLogin?RespObj.SUCCESS:RespObj.FAILD;
	}
	
	
	/**
	 * 用户登出
	 * @param request
	 * @param response
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/logout")
	@ResponseBody
	public RespObj logout(HttpServletRequest request,HttpServletResponse response )
	{
		SessionValue sv =getSessionValue();
		if (null != sv) {
			try
			{
	            //sso logout
				CASRestful casrest = new CASRestful(request, cas_url, service_url);
				String ssoTicket=CacheHandler.getStringValue(CacheHandler.getKeyString(CacheHandler.USER_SSO_TICKET, sv.getId()));
				if(StringUtils.isNotBlank(ssoTicket))
				{
				 casrest.logout(ssoTicket);
				}
		
			}catch(Exception ex)
			{
				loginLogger.error("", ex);
			}
			
			

			String yearMonth=DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM);
			CacheHandler.deleteKey(CacheHandler.CACHE_USER_CALENDAR,sv.getId(),yearMonth);

			logger.info("try loginout;the ui=" + sv.getId());
			logger.info("delete session value for user:" + sv.getId());

			Cookie cookies[] = request.getCookies();
			Cookie c = null;
			for (int i = 0; i < cookies.length; i++) {
				c = cookies[i];
				c.setMaxAge(0);
				if(c.getName().equals(Constant.COOKIE_USER_KEY))
				{
					CacheHandler.deleteKey(CacheHandler.CACHE_SESSION_KEY,
							c.getValue());
				}
			}
		}
		
		return RespObj.SUCCESS;
	}

	

	/**
	 * 得到用户基本信息
	 * @return
	 */
	@RequestMapping("/info")
	@ResponseBody
	public UserDetailInfoDTO getUserDetailInfoDTO()
	{
		try
		{
			UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getSessionValue().getId());
			if(StringUtils.isNotBlank(getSessionValue().getSchoolId()))
			{
				SchoolEntry se= schoolService.getSchoolEntry(new ObjectId(getSessionValue().getSchoolId()),new BasicDBObject("nm",1).append("logo", 1).append("mods", 1));
				if(null!=se)
				{
					userInfoDTO4WB.setSchoolName(se.getName());
					userInfoDTO4WB.setSchoolLogo(se.getLogo());
					userInfoDTO4WB.setModuleStr(se.getModuleStr());
				}
			}
			return userInfoDTO4WB;
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		return new UserDetailInfoDTO();
	}
	
	
	/**
	 * sso 回调登录
	 * @param request
	 * @param response
	 * @return
	 * @throws UnLoginException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws IllegalParamException 
	 * @throws UnBindException 
	 * @throws ServletException 
	 */
	@SessionNeedless
	@RequestMapping("/sso/login")
	public String ssoLogin ( HttpServletRequest request,HttpServletResponse response,String nextpage) throws UnLoginException, ClientProtocolException, IOException, IllegalParamException, UnBindException, ServletException
	{
		String service = new String(service_url);
		if (StringUtils.isNotBlank(nextpage)) {
			service = service + "?nextpage=" + nextpage;
		}
		CASRestful casrest = new CASRestful(request, cas_url, service);
		String st = "";
		if(null!=request.getParameter("ticket"))
		{
			st=request.getParameter("ticket").toString();
		}
		if(StringUtils.isBlank(st))
		{
			throw new UnBindException("sso login cannot find ticket!!!!");
		}
		
		logger.info("get ticket="+st);
		casrest.authenticateWithST(st);
		logger.info("sso user name："+casrest.getCurrentUser());
		if(StringUtils.isBlank(casrest.getCurrentUser()))
		{
			throw new  UnBindException("sso login faild");
		}
		
		String name=casrest.getCurrentUser().toLowerCase();
		
		UserEntry e=userService.getUserEntryByBindName(name,1);
		 
		
		/**
		 *  -------------自动创建用户逻辑--------------------
		 */
		if(null==e)
		{
			try {
				e= handleSSOLogin(name, request);
			} catch (Exception e1) {
				throw new UnBindException(e1.getLocalizedMessage());
			}
		}
		/**
		 *  -------------自动创建用户逻辑--------------------
		 */
		
		/**
//		
		if(null!= request.getParameter("nextpage")) //用户绑定 
		{
		   String loginedIdStr=request.getParameter("nextpage").toString();
		   if(ObjectId.isValid(loginedIdStr)) //已经登录了
		   {
			   if(null!=e)
			   {
				   logger.error("BIND ERROR;"+ name+" already binded,Can not bind to user:"+getUserId());
			   }
			   else
			   {
				   userService.update(new ObjectId(loginedIdStr), "bnm", name);
			   }
			   return "redirect:/user/homepage.do";
		   }
		}
		**/
		
		if(null==e) //没有绑定用户
		{
			throw new  UnBindException("un bind k6kt user!!!");
		}

		try
		{
			if(0l==e.getLastActiveDate()){
				String params=e.getID().toString()+0l;
				String flkey= CacheHandler.getKeyString(CacheHandler.CACHE_USER_FIRST_LOGIN,params);
				CacheHandler.cache(flkey,Constant.USER_FIRST_LOGIN, Constant.SESSION_FIVE_MINUTE);
			}
			//更新最后登录日期
			userService.updateLastActiveDate(e.getID());
		}catch(Exception ex)
		{
			logger.error("",ex);
		}
		
		
		if(Constant.ONE==e.getUserType()) //有效时间用户 
		{
			long validBeginTime =e.getValidBeginTime();
			long validTime=e.getValidTime();
			if(0L==validBeginTime) //第一次登陆
			{
				try {
					userService.update(e.getID(), "vabt", System.currentTimeMillis());
				} catch (IllegalParamException e1) {
					
				};
			}
			else
			{
				if(System.currentTimeMillis()>validBeginTime+validTime*1000)
				{
					throw new UnLoginException();
				}
			}
		}
		Map<String,String> schoolMap = new HashMap<String,String>();
//		SchoolEntry schoolEntry = null;
//		com.fulaan.schoolbase.dto.SchoolDTO dto = null;
		try {
//			schoolEntry = schoolService.getSchoolEntryByUserId(e.getID());
			String result = schoolBaseService.getSchool(e.getSchoolID());
			JSONObject dataJson = new JSONObject(result);
			JSONObject row = dataJson.getJSONObject("message");
			schoolMap.put("id",row.getString("id"));
			schoolMap.put("name",row.getString("name"));
			schoolMap.put("logo",row.getString("schoolLogo"));
		}
		catch (Exception ie){
			logger.error("Can not find school for user:"+e);
		}

	
		//处理SessionValue
		SessionValue value =new SessionValue();
		value.setId(e.getID().toString());
		value.setUserName(e.getUserName());
		value.setRealName(e.getNickName());
		value.setK6kt(e.getK6KT());
		value.setSso();
		if(e.getK6KT() == 1) {//k6kt用户
			value.setSchoolId(e.getSchoolID().toString());
			value.setSchoolLogo(schoolMap.get("logo"));

			value.setUserRole(e.getRole());
			value.setAvatar(e.getAvatar());
			value.setUserPermission(e.getPermission());
			value.setUserRemovePermission(e.getRemovePermission());
			value.setExperience(e.getExperiencevalue());
			value.setChatid(e.getChatId());
//			value.setSchoolNavs(schoolEntry.getSchoolNavs());
			value.setSchoolName(schoolMap.get("name"));
		}
		//放入缓存
		ObjectId cacheUserKey =new ObjectId();
		//ck_key
		CacheHandler.cacheUserKey(e.getID().toString(), cacheUserKey.toString(), Constant.SECONDS_IN_DAY);
		//s_key
		CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
		//处理cookie
		Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY,cacheUserKey.toString());
		userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
		userKeycookie.setPath(Constant.BASE_PATH);
		response.addCookie(userKeycookie);

		request.setAttribute(BaseController.SESSION_VALUE, value);
		
		//将ticket存入缓存
		CacheHandler.cache(CacheHandler.getKeyString(CacheHandler.USER_SSO_TICKET, e.getID().toString()), st, Constant.SECONDS_IN_DAY*2);

		try {
			Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY,URLEncoder.encode(name, Constant.UTF_8));
			nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
			nameCookie.setPath(Constant.BASE_PATH);
			response.addCookie(nameCookie);
		} catch (UnsupportedEncodingException e1) {
			logger.error("", e1);
		}
		
		return "redirect:/user/homepage.do";
	}

	/**
	 * sso 登录 跳转
	 * @param request
	 * @param response
	 * @param callBack
	 * @throws UnLoginException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@SessionNeedless
	@RequestMapping("/sso/redirect")
	public void ssoLoginRedirect(HttpServletRequest request,
			HttpServletResponse response, String callBack)
			throws UnLoginException, ClientProtocolException, IOException {
		String login_url = new String(cas_login_url);
		String service = new String(service_url);
		
		ObjectId uid =getUserId();
		if (null!=uid) {
			service = service + "?nextpage=" + uid.toString();
		}
		login_url = login_url + "?service="+ URLEncoder.encode(service, "UTF-8");
		logger.info("sso loginaa begin redirect!!!");
		logger.info(login_url);
		response.sendRedirect(login_url + "&redirect=true");
	}

	/**
	 *
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/school/teacher")
	@ResponseBody
	public List<IdValuePairDTO> getSchoolTeacher()
	{
		List<IdValuePairDTO> retList =new ArrayList<IdValuePairDTO>();
		List<UserEntry> list=	userService.getTeacherEntryBySchoolId(new ObjectId(getSessionValue().getSchoolId()), new BasicDBObject("nm",1));
		for(UserEntry e:list)
		{
			IdValuePairDTO dto =new IdValuePairDTO(e.getID(), e.getUserName());
			retList.add(dto);
		}
		return retList;
	}

	/**
	 * 好友根据名字排序
	 * add by miaoqiang
	 * @param list
	 * @return
	 */
	public List<UserDetailInfoDTO> sortList(List<UserDetailInfoDTO> list)
	{

		Collections.sort(list,new Comparator<UserDetailInfoDTO>(){
			public int compare(UserDetailInfoDTO arg0,UserDetailInfoDTO arg1)
			{
				Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
				if (cmp.compare(arg0.getUserName(), arg1.getUserName())>0){
					return 1;
				}else if (cmp.compare(arg0.getUserName(), arg1.getUserName())<0){
					return -1;
				}
				return 0;
			}
		});
		return list;
	}
	/**得到用户所在的班级
	 * @return
	 */
	@RequestMapping("classes")
	@ResponseBody
	public List<ClassInfoDTO> getUserClasses() {
		return userService.getClassDTOList(getUserId(),getSessionValue().getUserRole());
	}
	
	
	/**
	 * 更新用户基本信息
	 * @param userLoginName
	 * @param mobile
	 * @param valiCode
	 * @param cacheKeyId
	 * @param email
	 * @param sex
	 * @return
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/update/basic")
	@ResponseBody
	public RespObj updateUserBasicInfos(@RequestParam (defaultValue="") String userLoginName, @RequestParam (defaultValue="") String mobile,String valiCode, String cacheKeyId,String email,Integer sex) throws IllegalParamException
	{
		
		UserEntry ue =userService.searchUserId(getUserId());
		RespObj ret=new RespObj(Constant.FAILD_CODE);
		
		UserEntry e;
		if(!ue.getLoginName().equals(userLoginName))
		{
			if(StringUtils.isNotBlank(userLoginName) )
			{
				if(ValidationUtils.isNumber(userLoginName))
				{
					ret.setMessage("登录名不能是数字");
					return ret;
				}
				if(ValidationUtils.isEmail(userLoginName))
				{
					ret.setMessage("登录名不能是邮箱");
					return ret;
				}
				
				e=userService.searchUserByUserName(userLoginName);
				if(null!=e)
				{
					ret.setMessage("名字重复");
					return ret;
				}
				
				e=userService.searchUserByUserLoginName(userLoginName);
				if(null!=e)
				{
							ret.setMessage("名字重复");
							return ret;
				}
				
				userService.update(ue.getID(), "logn", userLoginName.toLowerCase());
				
			}
		}
		
		
		
		if(!ue.getMobileNumber().equals(mobile))
		{
			if(StringUtils.isNotBlank(mobile))
			{
				if(!ValidationUtils.isRequestModile(mobile))
				{
					ret.setMessage("手机错误");
					return ret;
				}
				
				e=userService.searchUserByMobile(mobile);
				if(null!=e)
				{
					ret.setMessage("手机号码被占用");
					return ret;
				}
				    
				String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
			    String value=CacheHandler.getStringValue(cacheKey);
			    if(StringUtils.isBlank(value)) {
			            ret.setMessage("验证码失效，请重新获取");
			            return ret;
			    }
			    
			    String[] cache = value.split(",");
			    if(!cache[0].equals(valiCode)){
			            ret.setMessage("注册失败：手机号码与验证码不匹配");
			            return ret;
			    }
				userService.update(ue.getID(), "mn", mobile.toLowerCase());
			}
		}
		
		
		
		
		if(!ue.getEmail().equals(email))
		{
			if(StringUtils.isNotBlank(email))
			{
				if(!ValidationUtils.isEmail(email))
				{
					ret.setMessage("邮箱错误");
					return ret;
				}
				
				e=userService.searchUserByEmail(email);
				if(null!=e)
			    {
						ret.setMessage("邮箱重复");
						return ret;
			    }
				
				
				userService.update(ue.getID(), "e", email.toLowerCase());
			}
		}
		
		
		if(sex!=Constant.NEGATIVE_ONE)
		{
			userService.update(ue.getID(), "sex", sex);
		}
		return RespObj.SUCCESS;
	}

	@RequestMapping("/userpage")
	public String userpage(Map<String,Object> model,HttpServletResponse res) {
		String url = "homepage/homepage";
		model.put("classInfoList",
				userService.getClassDTOList(getUserId(),getSessionValue().getUserRole()));
		if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
			url="homepage/homepage";
			return url;
		} else if (UserRole.isTeacher(getSessionValue().getUserRole())) {
			url="homepage/homepage";
			return url;
		} else if (UserRole.isHeadmaster(getSessionValue().getUserRole())) {
			url="homepage/homepage";
			return url;
		}
		else if (UserRole.isDoorKeeper(getSessionValue().getUserRole())) {
				url="homepage/homepage";
				return url;
		}else if (UserRole.isFunctionRoomManager(getSessionValue().getUserRole())) {
				url="homepage/homepage";
				return url;
		}else if (UserRole.isDormManager(getSessionValue().getUserRole())) {
				url="homepage/homepage";
				return url;
		}
		
		
		try {
			res.sendRedirect("/myschool/managesubject?version=55&tag=1");
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}


	@RequestMapping("/homepage")
	public String homepage(Map<String,Object> model,HttpServletResponse res) throws IOException {
		
		if(getSessionValue().getUserRole()==UserRole.EDUCATION.getRole() ||  getSessionValue().getUserRole()==UserRole.CHECKMANAGE.getRole())
		{
			res.sendRedirect("/manageCount/countMain.do?version=3&index=25");
			return null;
		}		
		
		String url = "homeschool/headmaster";
		try {
			String params=getUserId().toString()+0l;
			String key= CacheHandler.getKeyString(CacheHandler.CACHE_USER_FIRST_LOGIN,params);
			String firstlogin = CacheHandler.getStringValue(key);
//			if (firstlogin!=null&&Constant.USER_FIRST_LOGIN.equals(firstlogin)) {
//				res.sendRedirect("/password");
//				return null;
//			}
		}catch(Exception ex)
		{
			logger.error("",ex);
		}
		
		
		
		UserEntry user=userService.searchUserId(getUserId());
		SessionValue sv = getSessionValue();
		sv.setExperience(user.getExperiencevalue());
		int validityTime = Constant.SECONDS_IN_DAY+(int)((user.getLastActiveDate()-System.currentTimeMillis())/1000);
		String userKey =CacheHandler.getUserKey(sv.getId());
		//Map<String,String> map=CacheHandler.getMapValue(userKey);
		CacheHandler.cacheSessionValue(userKey, sv, validityTime);
//		SchoolEntry se =schoolService.getSchoolEntry(new ObjectId(getSessionValue().getSchoolId()), new BasicDBObject("bindId",1).append("nm", 1));
//		if(StringUtils.isNotBlank(se.getBindId()))
//		{
//		   model.put("bindSchoolName", se.getName());
//		}


//		model.put("classInfoList",
//				userService.getClassDTOList(getUserId(),getSessionValue().getUserRole()));
		try
		{
			 String citieStr =sv.getSchoolNavs();
			 if(schoolCustomCiteSet.contains(citieStr))
			 {
				 return "application/nroll";
			 }
			 
		}catch(Exception ex)
		{
			
		}


		if(StringUtils.isNotBlank(getSessionValue().getSchoolNavs()))
		{
			int navIndex=0;
			try
			{
				 navIndex=Integer.parseInt(getSessionValue().getSchoolNavs().replace("navs",""));
				
				if(navIndex>=31)// 导航大于31，都是自定制导航
				{
					 return "application/nroll";
				}
			}catch(Exception ex)
			{
				
			}
			
			
			url="application/nroll";
			if(navIndex==3)
			{
				int role=getSessionValue().getUserRole();
				if(UserRole.isManagerOnly(role)){
					try {
						res.sendRedirect("/myschool/managesubject?index=18&version=1&tag=1");
					} catch (IOException e) {
						logger.error("", e);
					}
					return null;
				}
				if(UserRole.isDoorKeeper(role)||UserRole.isDormManager(role)){
					try {
						res.sendRedirect("/user?version=91&index=6");
					} catch (IOException e) {
						logger.error("", e);
					}
					return null;
				}
				return url;
			}
			//导航5~9纯管理员直接不进入磁铁页面
			else if(UserRole.isManagerOnly(getSessionValue().getUserRole()) )
			{
//				try {
//					res.sendRedirect("/user/homepage.do");
//				} catch (IOException e) {
//					logger.error("", e);
//				}
//				return null;
			}
			return url;
		}

		if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
			url="newPage";
			return url;
		} else if (UserRole.isTeacher(getSessionValue().getUserRole())) {
			url="newPage";
			return url;
		} else if (UserRole.isHeadmaster(getSessionValue().getUserRole())) {
			url="newPage";
			return url;
		} else if (UserRole.isDoorKeeper(getSessionValue().getUserRole())) {
			url="homepage/homepage";
			return url;
		}else if (UserRole.isFunctionRoomManager(getSessionValue().getUserRole())) {
			url="homepage/homepage";
			return url;
		} else if (UserRole.isDormManager(getSessionValue().getUserRole())) {
			url="homepage/homepage";
			return url;
		}


		if (UserRole.isEducation(getSessionValue().getUserRole())  ||  getSessionValue().getUserRole()==UserRole.CHECKMANAGE.getRole() ) {
			try
			{
				res.sendRedirect("/manageCount/countMain.do?version=90&tag=1");
				return null;
			}catch(Exception ex)
			{
			}
		}

		try {
			res.sendRedirect("/myschool/managesubject?version=55&tag=1");
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

	@RequestMapping("/userSchoolYearExpManage")
	public String userSchoolYearExpManage() {
		String url = "";
		if (UserRole.isManager(getSessionValue().getUserRole())) {
			url="experienceHistory/userSchoolYearExp";
		}
		return url;
	}

	@RequestMapping("/userSchoolYearExpImport")
	@ResponseBody
	public boolean userSchoolYearExpImport(){
		if (UserRole.isManager(getSessionValue().getUserRole())) {
			// userService.userSchoolYearExpImport();
		}
		return true;
	}

	@RequestMapping("/updSchoolHomeDate")
	public void updSchoolHomeDate() {
		userService.updSchoolHomeDate(getUserId());
	}

	@RequestMapping("/updFamilyHomeDate")
	public void updFamilyHomeDate() {
		userService.updFamilyHomeDate(getUserId());
	}

	/**
	 * k6kt增加经验值
	 * @param name
	 */
	@RequestMapping("/updateExp")
	public void updateExp(String name) {
		userService.updateExp(name);
	}
	
	/**
	 * 找回密码
	 * @param name
	 */
	@SessionNeedless
	@RequestMapping("/findPwd")
	public String findPwd(String name) {
		return "user/userAccount";
	}
	
	
	/**
	 * 找回密码第一次验证
	 * @param username
	 * @param vCode
	 * @return
	 */
	@SessionNeedless
	@ResponseBody
	@RequestMapping("/check/first")
	public RespObj checkFirst(String username,String vCode,HttpServletRequest request)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		
		if(StringUtils.isBlank(username) || StringUtils.isBlank(vCode))
		{
			obj.setMessage("用户名或验证码为空");
			return obj;
		}
		
		UserEntry e=userService.searchUserByUserName(username.toLowerCase());
		if(null==e)
		{
			obj.setMessage("找不到用户");
			return obj;
		}
		
		String cookieValue=getCookieUserKeyValue(request,Constant.COOKIE_VALIDATE_CODE);
		
		String key=   CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, cookieValue);
		
		String rightCode= CacheHandler.getStringValue(key);
		
		if(StringUtils.isBlank(rightCode))
		{
			obj.setMessage("验证码失效");
			return obj;
		}
		
		if(!rightCode.equalsIgnoreCase(vCode))
		{
			obj.setMessage("验证码错误");
			return obj;
		}
		
		obj =new RespObj(Constant.SUCCESS_CODE);
		int type; 
		
		if(UserRole.isStudent(e.getRole()))
		{
			type=0;
		}
		else if(UserRole.isParent(e.getRole()))
		{
			type=1;
		}
		else if(UserRole.TEACHER.getRole()==e.getRole())
		{
			type=2;
		}
		else
		{
			type=3;
		}
		obj.setMessage(type);
		return obj;
	}
	
	
	

	
	/**
	 * 移动端找回密码第一次验证
	 * @param username
	 * @return
	 */
	@SessionNeedless
	@ResponseBody
	@RequestMapping("/mobile/check/first")
	public RespObj mobileCheckFirst(String username,HttpServletRequest request)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		
		if(StringUtils.isBlank(username) )
		{
			obj.setMessage("用户名或验证码为空");
			return obj;
		}
		
		UserEntry e=userService.searchUserByUserName(username.toLowerCase());
		if(null==e)
		{
			obj.setMessage("找不到用户");
			return obj;
		}
		
		String cookieValue=getCookieUserKeyValue(request,Constant.COOKIE_VALIDATE_CODE);
		
		String key=   CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, cookieValue);
		
		String rightCode= CacheHandler.getStringValue(key);
		
		if(StringUtils.isBlank(rightCode))
		{
			//obj.setMessage("验证码失效");
			//return obj;
		}
		
		
		obj =new RespObj(Constant.SUCCESS_CODE);
		int type; 
		
		if(UserRole.isStudent(e.getRole()))
		{
			type=0;
		}
		else if(UserRole.isParent(e.getRole()))
		{
			type=1;
		}
		else if(UserRole.TEACHER.getRole()==e.getRole())
		{
			type=2;
		}
		else
		{
			type=3;
		}
		obj.setMessage(type);
		return obj;
	}
	
	

	/**
	 * 重设密码
	 * @param username
	 * @param vCode
	 * @return
	 */
	@SessionNeedless
	@ResponseBody
	@RequestMapping("/resetPwd")
	public RespObj reSetPwd(String username,String pwd,String pwdAgain,String  vCode, HttpServletRequest request)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		
		String cacheTag=CacheHandler.getStringValue(getCookieUserKeyValue(request,"JSESSIONID"));
		
		if(StringUtils.isBlank(cacheTag) || !"1".equals(cacheTag))
		{
			obj.setMessage("参数错误");
			return obj;
		}
		
		if(StringUtils.isBlank(pwd) || StringUtils.isBlank(pwdAgain) || !pwd.equals(pwdAgain))
		{
			obj.setMessage("参数错误");
			return obj;
		}
		
		UserEntry e=userService.searchUserByUserName(username.toLowerCase());
		if(null==e)
		{
			obj.setMessage("找不到用户");
			return obj;
		}
		boolean isCheckRole =false;
		if(UserRole.isParent(e.getRole())  || UserRole.isStudent(e.getRole()) || e.getRole()==UserRole.TEACHER.getRole())
		{
			isCheckRole=true;
		}
		
		if(!isCheckRole)
		{
			obj.setMessage("身份错误");
			return obj;
		}
		
		String cookieValue=getCookieUserKeyValue(request,Constant.COOKIE_VALIDATE_CODE);
		
		String key=   CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, cookieValue);
		
		String rightCode= CacheHandler.getStringValue(key);
	
		if(StringUtils.isNotBlank(vCode) && !rightCode.equalsIgnoreCase(vCode))
		{
			obj.setMessage("验证码错误");
			return obj;
		}
		
		try {
			userService.update(e.getID(), "pw", MD5Utils.getMD5(pwdAgain));
		} catch (IllegalParamException e1) {
	       logger.error("", e1);
		} catch (Exception e1) {
		   logger.error("", e1);
		}
		
		return RespObj.SUCCESS;
	}
	
	
	/**
	 * 用户更新基本信息
	 * @param userName
	 * @param mobile
	 * @param valiCode
	 * @param cacheKeyId
	 * @param clientType 客户端类型 0是网页 1是手机
	 * @return
	 * @throws IllegalParamException
	 */
	@SessionNeedless
	@RequestMapping("/update/basic2")
	@ResponseBody
	public RespObj updateUserBasicInfos2( String userName, String mobile,String valiCode, String cacheKeyId, @RequestParam(required=false,defaultValue="0") Integer clientType,  HttpServletRequest req) throws IllegalParamException
	{
		RespObj ret=new RespObj(Constant.FAILD_CODE);
		if(StringUtils.isBlank(userName) || StringUtils.isBlank(mobile) || StringUtils.isBlank(valiCode)  )
		{
			ret.setMessage("请正确输入手机和邮箱");
			return ret;
		}
		
		UserEntry ue=userService.searchUserByUserName(userName);
		
		if(null==ue)
		{
			ret.setMessage("用户名错误");
			return ret;
		}
		
		
		if(!ValidationUtils.isRequestModile(mobile))
		{
			ret.setMessage("手机非法");
			return ret;
		}
		
		UserEntry mobileEntry=userService.searchUserByMobile(mobile);
		
		if(null!=mobileEntry && !mobileEntry.getUserName().toLowerCase().equals(ue.getUserName()))
		{
			ret.setMessage("此手机已经被占用");
			return ret;
		}
		
		
		String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
	    String value=CacheHandler.getStringValue(cacheKey);
	    if(StringUtils.isBlank(value)) {
	            ret.setMessage("验证码失效，请重新获取");
	            return ret;
	    }
	    
	    String[] cache = value.split(",");
	    if(!cache[0].equals(valiCode)){
	            ret.setMessage("注册失败：手机号码与验证码不匹配");
	            return ret;
	    }
	    
	    
	    String jsessionIdNow=getCookieUserKeyValue(req,"JSESSIONID");
	    
	    if(clientType==0)
	    {
			
			//jsi_{JSESSIONID}=email
			cacheKey=CacheHandler.getKeyString(CacheHandler.SESSIONID_EMALL, jsessionIdNow);
			String emailCache=CacheHandler.getStringValue(cacheKey);
			
			if(StringUtils.isBlank(emailCache) )
			{
				ret.setMessage("邮箱验证错误");
				return ret;
			}
			UserEntry emailUser=userService.searchUserByEmail(emailCache);
			if(null!=emailUser && !emailUser.getUserName().equalsIgnoreCase(userName))
		    {
				ret.setMessage("邮箱已经被登记使用");
				return ret;
		    }
			userService.update(ue.getID(), "e", emailCache.toLowerCase());
	    }
		userService.update(ue.getID(), "mn", mobile.toLowerCase());
		CacheHandler.cache(jsessionIdNow, "1", Constant.SECONDS_IN_HOUR);
		
		return RespObj.SUCCESS;
	}

	/**
	 * 更新用户基本信息
	 * @param mobile
	 * @param valiCode
	 * @param cacheKeyId
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/update/basic3")
	@ResponseBody
	public RespObj updateUserBasicInfos3(@RequestParam (defaultValue="") String mobile,String valiCode, String cacheKeyId) throws IllegalParamException
	{

		UserEntry ue =userService.searchUserId(getUserId());
		RespObj ret=new RespObj(Constant.FAILD_CODE);

		UserEntry e;

		if(!ue.getMobileNumber().equals(mobile))
		{
			if(StringUtils.isNotBlank(mobile))
			{
				if(!ValidationUtils.isRequestModile(mobile))
				{
					ret.setMessage("手机错误");
					return ret;
				}

				e=userService.searchUserByMobile(mobile);
				if(null!=e)
				{
					ret.setMessage("手机号码被占用");
					return ret;
				}

				String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
				String value=CacheHandler.getStringValue(cacheKey);
				if(StringUtils.isBlank(value)) {
					ret.setMessage("验证码失效，请重新获取");
					return ret;
				}

				String[] cache = value.split(",");
				if(!cache[0].equals(valiCode)){
					ret.setMessage("注册失败：手机号码与验证码不匹配");
					return ret;
				}
				userService.update(ue.getID(), "mn", mobile.toLowerCase());
			}
		}
		return RespObj.SUCCESS;
	}
	
    /**
     * 
     * @param email
     * @param req
     * @return
     * @throws IllegalParamException
     */
	@SessionNeedless
	@RequestMapping("/email")
	@ResponseBody
	public RespObj sendEmail( String email,HttpServletRequest req) throws IllegalParamException
	{
		RespObj ret=new RespObj(Constant.FAILD_CODE);
		
		if(!ValidationUtils.isEmail(email))
		{
			ret.setMessage("邮箱错误");
			return ret;
		}
		
		ObjectId ukId=new ObjectId();
		//jsi_{ukId}=JSESSIONID
		String cacheKey=CacheHandler.getKeyString(CacheHandler.SESSIONID_EMALL, ukId.toString());
		
		String jsessionId=getCookieUserKeyValue(req,"JSESSIONID");
		CacheHandler.cache(cacheKey,jsessionId, Constant.SECONDS_IN_HOUR);
		
		
		try {
			String templateContent =FileUtils.readFileToString(new File(req.getServletContext().getRealPath("/email/findPwd.html")),"utf-8");
		    String link=MessageFormat.format(linkFormat, ukId.toString(),email,UUID.randomUUID().toString()) ;
			String emailContent=MessageFormat.format(templateContent, link);
			MailUtils sendMail=new MailUtils();
	        sendMail.sendMail("用户邮箱验证",email,emailContent);
		} catch (Exception e1) {
			logger.error("", e1);
			ret.setMessage("邮箱错误");
			return ret;
		}
		
		return RespObj.SUCCESS;
	}
	
	
	/**
	 * 邮件认证用户回调
	 * @param email
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@SessionNeedless
	@RequestMapping("/email/callback")
	public String emailCall( String ukId,String email,Map<String,Object> model,HttpServletRequest req) throws Exception
	{
		if(null==ukId || !ObjectId.isValid(ukId))
		{
			//ret.setMessage("参数错误");
			//return ret;
			
			throw new Exception();
		}
		
		if(!ValidationUtils.isEmail(email))
		{
			//ret.setMessage("邮箱错误");
			//return ret;
			throw new Exception();
		}
		
		
		String cacheKey=CacheHandler.getKeyString(CacheHandler.SESSIONID_EMALL, ukId.toString());

		String jsessionId=CacheHandler.getStringValue(cacheKey);
		
//		String jsessionIdNow=getCookieUserKeyValue(req,"JSESSIONID");
//		
//		if(!jsessionId.equalsIgnoreCase(jsessionIdNow))
//		{
//			//ret.setMessage("参数错误");
//			//return ret;
//			//throw new Exception();
//		}
		
		//jsi_{JSESSIONID}=email
		cacheKey=CacheHandler.getKeyString(CacheHandler.SESSIONID_EMALL, jsessionId);
		CacheHandler.cache(cacheKey,email, Constant.SECONDS_IN_HOUR);
		
		model.put("email", email);
		return "user/emailsuccess";
	}
	
	
	
	
	public UserEntry handleSSOLogin(String userName,HttpServletRequest request) throws Exception
	{
		JSONObject userInfo =PHPRPCUtils.getUserInfo(userName);
		logger.info("sso userInfo:"+userInfo);
		JSONObject schoolInfo=PHPRPCUtils.GetSchool(userInfo.getString("schoolId"));
		logger.info("sso schoolInfo:"+schoolInfo);
		
		String schoolName =schoolInfo.getString("schoolName");
		SchoolEntry seEntry=schoolService.getSchoolEntry(schoolName, Constant.EMPTY);
		
		if(null==seEntry) //没有此学校
		{
			Map<String, RegionEntry> regionMap=userService.getRegionEntryMap();
			String cityId =schoolInfo.getString("cityId");
			JSONObject areaInfo=PHPRPCUtils.GetArea(cityId);
			
			logger.info("sso areaInfo:"+areaInfo);
			String areaName =areaInfo.getString("areaName");
			
			ObjectId regionId=new ObjectId("55934c13f6f28b7261c19c3d"); //默认安徽省
			for(Map.Entry<String, RegionEntry> entry:regionMap.entrySet())
			{
				if(entry.getValue().getName().replace("市", "").equals(areaName.replace("市", "")) )
				{
					regionId=new ObjectId(entry.getKey());
					break;
				}
			}
			
			int schoolType=SchoolType.KINDERGARENER.getType();
			
			if(userInfo.getString("stage").equals(SchoolType.PRIMARY.getName()))
			{
				schoolType=SchoolType.PRIMARY.getType();
			}
			if(userInfo.getString("stage").equals(SchoolType.JUNIOR.getName()))
			{
				schoolType=SchoolType.JUNIOR.getType();
			}
			if(userInfo.getString("stage").equals(SchoolType.SENIOR.getName()))
			{
				schoolType=SchoolType.SENIOR.getType();
			}
			
			String time =String.valueOf(System.currentTimeMillis());
			
			String pwd=time.substring(time.length()-6);
			
			
			
			seEntry =new SchoolEntry(schoolType, schoolName, regionId, pwd);
			seEntry.setSchoolNavs(11);
			seEntry.setBindId(userInfo.getString("schoolId"));
			seEntry.setModuleStr("1,2,4,5,7,");
			schoolService.addSchoolEntry(seEntry);
			
		}
		
		int sex=1;
		if("0".equals(userInfo.getString("gender")))
		{
			sex=0;
		}
		
		
		int gradeCode=-1;
		Object objgrade =userInfo.get("grade");
		Object objclassName =userInfo.get("className");
		
		//零年级、一年级~九年级、高一、高二、高三、已毕业
        
		String grade="";
		String className="";
		if(objgrade!=org.json.JSONObject.NULL && objclassName!=org.json.JSONObject.NULL)
		{
			grade=objgrade.toString();
			className=objclassName.toString();
			
			if(StringUtils.isNotBlank(grade) && StringUtils.isNotBlank(className))
			{
			  if(grade.equals("零年级") || grade.equals("一年级") )
			  {
				  gradeCode=1;
			  }
			  else if( grade.equals("二年级") )
			  {
				  gradeCode=2;
			  }
			  else if( grade.equals("三年级") )
			  {
				  gradeCode=3;
			  }
			  else if( grade.equals("四年级") )
			  {
				  gradeCode=4;
			  }
			  else if( grade.equals("五年级") )
			  {
				  gradeCode=5;
			  }
			  else if( grade.equals("六年级") )
			  {
				  gradeCode=6;
			  }
			  else if( grade.equals("七年级") )
			  {
				  gradeCode=7;
			  }
			  else if( grade.equals("八年级") )
			  {
				  gradeCode=8;
			  }
			  else if( grade.equals("九年级") )
			  {
				  gradeCode=9;
			  }
			  else if( grade.equals("高一") )
			  {
				  gradeCode=10;
			  }
			  else if( grade.equals("高二") )
			  {
				  gradeCode=11;
			  }
			  else if( grade.equals("高三") )
			  {
				  gradeCode=12;
			  }
			  else if( grade.equals("已毕业") )
			  {
				  gradeCode=17;
			  }
			}
		}
		
		
		//语文 数学 英语 品德与生活 品德与社会 思想品德 思想政治 历史 地理 物理 化学 生物 音乐 体育与健康 美术 信息技术 通用技术 幼儿教育 科学综合实践活动   心理健康 教育特殊教育
		Object objsubject =userInfo.get("subject");
		SubjectType subjectType =null;
		if(objsubject!=org.json.JSONObject.NULL)
		{
			String subject=objsubject.toString();
			subjectType =SubjectType.getSubjectType(subject);
			if(null==subjectType)
			{
				if(subject.equals("品德与生活") || subject.equals("品德与社会") || subject.equals("思想政治") || subject.equals("思想品德"))
				{
					subjectType=SubjectType.POLITICS;
				}
				else if(subject.equals("体育与健康") || subject.equals("美术") )
				{
					subjectType=SubjectType.MUSIC_SPORT_ART;
				}
				else if(subject.equals("信息技术") || subject.equals("通用技术") )
				{
					subjectType=SubjectType.COMPUTER;
				}
				else
					subjectType=SubjectType.OTHERS;
			}
		}
		
		
		List<ExcelTeacherRecord> teachers =new ArrayList<ExcelTeacherRecord>();
		List<ExcelStudentRecord> students=new ArrayList<ExcelStudentRecord>();
		
		Object realUserName =userInfo.get("userName");
		
		String dbName=(realUserName==org.json.JSONObject.NULL)?userName:realUserName.toString();
		
		String userType=userInfo.getString("userType");
		//用户名，权限 ，性别
		if(userType.equals("1") || userType.equals("2") ) //老师
		{
			ExcelTeacherRecord record =new ExcelTeacherRecord(dbName, "", sex, UserRole.TEACHER.getRole(), "");
			
			if(gradeCode>-1 && null!=subjectType)
			{
				GradeAndClass gac=new GradeAndClass(subjectType.getName(), grade, gradeCode, className, false, false, false, false);
				record.setGradeAndClassList(Arrays.asList(gac));
			}
			teachers.add(record);
		}
		else //学生  （用户名，权限 ，性别）
		{
			 ExcelStudentRecord excelStudentRecord=new ExcelStudentRecord();
             excelStudentRecord.setSex(sex);
             excelStudentRecord.setStudentName(dbName);
             
             if(gradeCode>-1 && null!=subjectType)
             {
            	 excelStudentRecord.setClassName(className);
            	 excelStudentRecord.setGradeCode(gradeCode);
             }
             students.add(excelStudentRecord);
		}
		
		 Map<String,Object> returnMap=new HashMap<String, Object>();
		 returnMap.put("teacherList",teachers);
         returnMap.put("studentList",students);
		
         
         UserManagerController uc= (UserManagerController)applicationContext.getBean("userManagerController");
         
 		 //确保学校下有该年级
 		 if(gradeCode>-1)
 		 {
 			 Grade g=uc.isContainGrade(seEntry.getGradeList(), gradeCode);
 			 if(null==g) //增加年级
 			 {
 				  g =new Grade(grade,gradeCode, null,new ObjectId());
	  			  schoolService.addGrade(seEntry.getID().toString(), g);
 			 }
 			seEntry=schoolService.getSchoolEntry(seEntry.getID(), Constant.FIELDS);
 			 
 		 }
         List<ObjectId> uList =new ArrayList<ObjectId>();
         uc.handleExcelMapData(returnMap,seEntry,uList);
        
         userService.update(uList.get(0), "bnm", userName);
         
         
         UserEntry checkUser =getUserEntry(dbName);
         if(null==checkUser)
         {
        	 userService.update(uList.get(0), "logn", dbName);
         }
         
         UserEntry ue=userService.searchUserId(uList.get(0));
         
         return ue;
	}
	
	
	
	
	private String getCookieUserKeyValue(HttpServletRequest request,String cookieName)
	{
		Cookie[] cookies=request.getCookies();
		if(null!=cookies)
		{
			for(Cookie cookie:cookies)
			{
				if(cookie.getName().equals(cookieName))
				{
					return cookie.getValue();
				}
			}
		}
		return Constant.EMPTY;
	}
	
	
	
	private UserEntry getUserEntry(String name) {
		UserEntry e=userService.searchUserByUserName(name);
			
		if(null==e && ValidationUtils.isRequestModile(name) )
		{
			e=userService.searchUserByMobile(name);
		}
		if(null==e && ValidationUtils.isEmail(name) )
		{
			e=userService.searchUserByEmail(name);
		}
		if(null==e)
		{
			e=userService.searchUserByUserLoginName(name);
		}
		if (null==e) {
			e = userService.searchUserByJNB(name);
		}
		if (null==e) {
			e = userService.searchUserByStuCode(name);
		}
		if(null==e)
		{
			e=userService.searchUserBySid(name);
		}
		return e;
	}


	private String getIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private  ApplicationContext applicationContext;  
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	@SessionNeedless
	@RequestMapping("/check")
	@ResponseBody
	public Map check() throws ParseException {
		Map map = new HashMap();
		String beginTime=new String("2016-09-03 00:00:01");
		String endTime=new String("2016-09-10 23:59:59");
		SimpleDateFormat sdf=new SimpleDateFormat(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
		Date bt=sdf.parse(beginTime);
		Date et=sdf.parse(endTime);
		Date date = new Date();
		boolean flag = false;
		int role = 2;
		if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
			role = 1;
			flag = true;
		}
		if (
//				bt.before(date) &&
				date.before(et) && !UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
			UserEntry e=userService.searchUserId(getUserId());
			if (e.getCoupon()==0) {
				flag = true;
			}
		}
		map.put("role",role);
		map.put("flag",flag);
		return map;
	}

	/**
	 * 查询教职工
	 * @return
     */
	@RequestMapping("/getStaffUsers")
	@ResponseBody
	public String getStaffUsers() {
		return userService.getStaffUsers(getSchoolId());
	}


	/**
	 *查询个人信息
	 * @return
	 */
	@RequestMapping("/getUserInfo")
	@ResponseBody
	public String getUserById() {
		return userService.getUserById(getUserId());
	}

	/**
	 * 修改个人信息
	 * @param userName
	 * @param telphone
	 * @param email
     * @return
     */
	@RequestMapping("/updateUserInfo")
	@ResponseBody
	public String updateUserInfo(String userName,String telphone,String email,int sex) {
		return userService.updateUserInfo(getUserId(),userName,telphone,email,sex);
	}

	/**
	 * 修改密码
	 * @param orgpwd
	 * @param pwd
     * @return
     */
	@RequestMapping("/updatePassword")
	@ResponseBody
	public String updatePassword(String orgpwd,String pwd) {
		return userService.updatePassword(getUserId(),orgpwd,pwd);
	}


	/**
	 *
	 * @param keyword
	 * @return
     */
	@RequestMapping("/getUsersByUserName")
	@ResponseBody
	public String getUsersByUserName(String keyword) {
		return userService.getUsersByUserName(getSchoolId(),keyword);
	}
}
