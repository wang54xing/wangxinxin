package com.fulaan.user.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.google.gson.Gson;
import com.pojo.app.SessionValue;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserEntry.Binds;
import com.sys.constants.Constant;
import com.sys.utils.HttpClientUtils;
/**
 * 深圳龙华项目用户单点登录
 * @author fourer
 *
 */
@Controller
@RequestMapping("/lh/user")
public class LonghuaUserController extends BaseController {

	private static final Logger logger =Logger.getLogger(LonghuaUserController.class);
	
	private UserService userService =new UserService();
	private SchoolService schoolService=new SchoolService();
	
	/**
	 * 跳转到首页
	 * @param Token
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SessionNeedless
	@RequestMapping("/sso/login")
	public String login (String Token,HttpServletResponse response,HttpServletRequest request) throws Exception
	{
		SessionValue sv =getSessionValue();
		
		if(null!=sv && !sv.isEmpty())
		{
			return "redirect:/user/homepage.do";
		}
		userLogin(Token, response, request);
		return "redirect:/user/homepage.do";
    }

	/**
	 * 跳转到微课
	 * @param Token
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SessionNeedless
	@RequestMapping("/sso/microlesson")
	public String microLesson (String Token,HttpServletResponse response,HttpServletRequest request) throws Exception
	{
		SessionValue sv =getSessionValue();
		if(null!=sv && !sv.isEmpty())
		{
			return "redirect:/microcourse/MicroSource.do";
		}
		userLogin(Token, response, request);
		return "redirect:/microcourse/MicroSource.do";
    }





	private void userLogin(String Token, HttpServletResponse response,
			HttpServletRequest request) throws Exception,
			ClientProtocolException, IOException, JSONException {
		logger.info("longhua Token:"+Token);
		if(StringUtils.isBlank(Token))
		{
			throw new Exception("Token is null");
		}
		//Token ="test-26a9c1ba231a415584aadf4d52708c39";
		Map<String,String> paramMap =new HashMap<String, String>();
		paramMap.put("SERVICE_CODE", "zteict.proxy.user.LoginStatus");
		paramMap.put("CONSUMER_ID", Token);
		
		Gson g =new Gson();
		@SuppressWarnings("deprecation")
		String url="http://lhedu.duoduo001.com/serviceProxy/servlet/?json="+  URLEncoder.encode(g.toJson(paramMap))                       ;
	    String resStr=	HttpClientUtils.get(url);
	    logger.info("longhua resStr:"+resStr);
	    JSONObject resDBO=new JSONObject(resStr);
	    logger.info("longhua resDBO:"+resDBO);
	    JSONObject userInfo= resDBO.getJSONObject("BODY");
	    logger.info("longhua userInfo:"+userInfo);
	    String userId=userInfo.getString("USER_ID");
	    
		//数据库验证
	    UserEntry e=userService.searchUserByUserBind(5,userId);
		if(null==e)
		{
			throw new Exception();
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
		
		SchoolEntry schoolEntry = null;
		try {
			schoolEntry = schoolService.getSchoolEntry(e.getSchoolID(), Constant.FIELDS);
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
		Binds binds=e.getUserBind();
		if(null!=binds)
		{
			//value.setUserType(binds.getType());
		}
		
		if(e.getK6KT() == 1) {//k6kt用户
			value.setSchoolId(e.getSchoolID().toString());
			if (schoolEntry != null && schoolEntry.getLogo() != null) {
				value.setSchoolLogo(schoolEntry.getLogo());
			}
			value.setUserRole(e.getRole());
			value.setAvatar(e.getAvatar());
			value.setUserPermission(e.getPermission());
			value.setUserRemovePermission(e.getRemovePermission());
			value.setExperience(e.getExperiencevalue());
			value.setChatid(e.getChatId());
			value.setSchoolNavs(schoolEntry.getSchoolNavs());
			value.setSchoolName(schoolEntry.getName());
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
       
		try {
			Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY,URLEncoder.encode(e.getUserName(), Constant.UTF_8));
			nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
			nameCookie.setPath(Constant.BASE_PATH);
			response.addCookie(nameCookie);
		} catch (UnsupportedEncodingException e1) {
			logger.error("", e1);
		}
	}
	
	
	
	
	
	
	
	public static void main(String[] args) throws ClientProtocolException, Exception {

		Map<String,String> paramMap =new HashMap<String, String>();
		paramMap.put("SERVICE_CODE", "zteict.proxy.user.LoginStatus");
		paramMap.put("CONSUMER_ID", "test-26a9c1ba231a415584aadf4d52708c39");
		
		Gson g =new Gson();
		String url="http://lhedu.duoduo001.com/serviceProxy/servlet/?json="+  URLEncoder.encode(g.toJson(paramMap))                       ;
	    String userinfo=	HttpClientUtils.get(url);
	
	    JSONObject resDBO=new JSONObject(userinfo);
	    JSONObject userInfo= resDBO.getJSONObject("BODY");
	    String userId=userInfo.getString("USER_ID");
	    System.out.println(userId);
	    
	}
}
