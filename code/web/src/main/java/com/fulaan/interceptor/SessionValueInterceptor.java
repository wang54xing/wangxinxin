package com.fulaan.interceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.db.user.UserDao;
import com.fulaan.school.service.SchoolService;
import com.fulaan.schoolbase.service.SchoolBaseService;
import com.fulaan.user.controller.ChaoXInSSOController;
import com.fulaan.user.service.UserService;
import com.google.common.base.Splitter;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.LoginLog;
import com.sys.exceptions.IllegalParamException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.log.SimpleLogHandler;
import com.pojo.app.ModuleApps;
import com.pojo.app.Platform;
import com.pojo.app.SessionValue;
import com.pojo.log.SimpleLogEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.UnLoginException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class SessionValueInterceptor implements HandlerInterceptor {

	private static final Logger logger =Logger.getLogger(SessionValueInterceptor.class);
	
	public static final String sso_homepage="/user/homepage.do?type=sso";
	
	public static final String sso_gaoxin="/user/homepage.do?type=gaoxin";

	public static final String sso_chaoxin = "type=chaoxin";

	public static final String appkey_chaoxin = "4D18II5TQR5C15QJ";

	public UserDao userDao = new UserDao();

	private UserService userService = new UserService();

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private SchoolBaseService schoolBaseService;

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		Object obj =arg0.getAttribute(BaseController.SESSION_VALUE);
		if(null!=obj && obj instanceof SessionValue)
		{
				SessionValue sv=(SessionValue)obj;
				if(null!=sv && !sv.isEmpty())
				{
				  buildSimpleLog(sv,arg0);
				}
		}
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		HandlerMethod method =(org.springframework.web.method.HandlerMethod)arg2;
		
		SessionNeedless s =method.getMethodAnnotation(SessionNeedless.class);
		String ui =getCookieUserKeyValue(arg0);
		if(null!=s)
		{
			if(StringUtils.isNotBlank(ui))
			{
				SessionValue sv=CacheHandler.getSessionValue(ui);
				if(null!=sv && !sv.isEmpty())
				{
					arg0.setAttribute(BaseController.SESSION_VALUE, sv);
				}
			}
			return true;
		}
		else
		{
			if(StringUtils.isNotBlank(ui))
			{
				SessionValue sv=CacheHandler.getSessionValue(ui);
				if(null!=sv && !sv.isEmpty())
				{
					arg0.setAttribute(BaseController.SESSION_VALUE, sv);
					return true;
				}
			}
			String requestURL=getFullURL(arg0);
			
			if(StringUtils.isNotBlank(requestURL) && requestURL.equalsIgnoreCase(sso_homepage))
			{
			  arg1.sendRedirect("/user/sso/redirect.do");
			}
			else if(StringUtils.isNotBlank(requestURL) && requestURL.equalsIgnoreCase(sso_gaoxin))
			{
			  String str=java.net.URLEncoder.encode("http://www.jngxqdyzx.com:8081/gxschool/sso/login.do", "UTF-8"); 
			  String sss="http://www.hschool.cc/oauth/authorize?client_id=fulan&response_type=code&scope=read+write&redirect_uri="+str;
			  arg1.sendRedirect(sss);
			}
			else if(StringUtils.isNotBlank(requestURL) && requestURL.contains("code=")&&requestURL.contains("state=") && requestURL.contains("appId=")&&requestURL.contains("appKey=")&&requestURL.contains("uid="))
			{
				String params = requestURL.substring(requestURL.indexOf("?") + 1, requestURL.length());
				Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
				String uidStr = split.get("uid");
				UserEntry userEntry = userDao.searchUserByUserBind(9,uidStr);
				login(userEntry,arg0,arg1);
				arg1.sendRedirect(requestURL);
			}
			else if(StringUtils.isNotBlank(requestURL) && requestURL.contains(sso_chaoxin)&&requestURL.contains("uid="))
			{
				String uidStr = requestURL.substring(requestURL.indexOf("uid=")+4,requestURL.length());
				UserEntry userEntry = userDao.searchUserByUserBind(9,uidStr);
				login(userEntry,arg0,arg1);
				arg1.sendRedirect(requestURL);
			}
			else if(StringUtils.isNotBlank(requestURL) && requestURL.contains(sso_chaoxin))
			{

				HttpSession session = arg0.getSession();
				session.setAttribute("cxurl", requestURL);
				arg1.sendRedirect("http://suzhi.jichu.chaoxing.com/front/getAppCode?client_secret="+appkey_chaoxin
						+"&redirect_uri=http://218.244.151.12:8083/chaoxin/sso/"+ ChaoXInSSOController.url_type.get(requestURL)+"/login.do");
			}
			else             
			{
                logger.info(method);
                throw new UnLoginException();
			}
			return false;
		}
		
	}
	
	
	
	private String getCookieUserKeyValue(HttpServletRequest request)
	{
		Cookie[] cookies=request.getCookies();
		if(null!=cookies)
		{
			for(Cookie cookie:cookies)
			{
				if(cookie.getName().equals(Constant.COOKIE_USER_KEY))
				{
					return cookie.getValue();
				}
			}
		}
		return Constant.EMPTY;
	}
	
	
	private String getFullURL(HttpServletRequest arg0)
	{
		String path=arg0.getRequestURI();
		String queryString=arg0.getQueryString();
		if(StringUtils.isNotBlank(queryString))
		{
			return path+"?"+queryString;
		}
		return path;
	}
	
	
	private void buildSimpleLog(SessionValue sv,HttpServletRequest request)
	{
		try
		{
			String url=request.getRequestURI();
			ModuleApps app=getModuleApps(url);
			if(null!=app)
			{
		      SimpleLogEntry e=new SimpleLogEntry(new ObjectId(sv.getId()),sv.getUserRole(),new ObjectId(sv.getSchoolId()) , getPf(request).getType(),app.getIndex(),url );
		      SimpleLogHandler.getSimpleLogHandler().put(e);
			}
		}catch(Exception ex)
		{
			
		} 
	}
	
	private Platform getPf(HttpServletRequest request)
	{
		String client = request.getHeader("User-Agent");
		Platform pf = null;
		if (client.contains("iOS")) {
			pf = Platform.IOS;
		} else if (client.contains("Android")){
			pf = Platform.Android;
		} else {
			pf = Platform.PC;
		}
		return pf;
	}
	private ModuleApps getModuleApps(String url)
	{
		for(ModuleApps app:ModuleApps.values())
		{
			if(url.startsWith(app.getBeginUrl()))
			{
				return app;
			}
		}
		return null;
	}

	public void login(UserEntry e,HttpServletRequest request, HttpServletResponse response) throws UnLoginException {
		String ip = getIP(request);
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
					throw new  UnLoginException("该用户已经失效");
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
			Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY,URLEncoder.encode(e.getUserName(), Constant.UTF_8));
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
//            loginLog.setPlatform(pf.getName());
			loginLog.setUserId(e.getID().toString());
			loginLog.setUserName(e.getUserName());
			if(e.getK6KT() == 1) {//k6kt用户
				loginLog.setRole(e.getRole());
				loginLog.setSchoolId(schoolMap.get("id"));
				loginLog.setSchoolName(schoolMap.get("name"));
//				loginLog.setCity(region.getName());
			}

		}catch(Exception ex)
		{
		}

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

}
