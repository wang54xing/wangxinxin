package com.fulaan.interceptor;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.pojo.app.SessionValue;
import com.sys.constants.Constant;
import com.sys.exceptions.UnLoginException;


public class SessionValueInterceptor implements HandlerInterceptor {

	private static final Logger logger =Logger.getLogger(SessionValueInterceptor.class);
	
	public static final String sso_homepage="/user/homepage.do?type=sso";
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
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
	
	
	
	

}
