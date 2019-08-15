package com.fulaan.base.controller;

import com.pojo.app.SessionValue;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author fourer
 * 基础controller,所有controller都由此类派生
 */
@Controller
@RequestMapping("/base")
public class BaseController {
	
	public static final String SESSION_VALUE="sessionValue";
	public static final String YUNCODE_URL ="http://yun.k6kt.com";

	public SessionValue getSessionValue() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();   
		return (SessionValue)request.getAttribute(SESSION_VALUE);
	}
	

	public ObjectId getUserId() {
		SessionValue sv =getSessionValue();
		if(null!=sv && !sv.isEmpty())
		{
			return new ObjectId(sv.getId());
		}
		return null;
	}

	public ObjectId getSchoolId() {
		SessionValue sv =getSessionValue();
		if(null!=sv && !sv.isEmpty())
		{
			return new ObjectId(sv.getSchoolId());
		}
		return null;
	}
	
	
	public String getSchoolVavs()
	{
		return "navs";
	}
	
	
	/**
	 * @param type
	 * @return
	 */
	@RequestMapping("/redirect")
	public String success(int type)
	{
		return "";
	}
	
	
	
	/**
	 * @param 录课神器
	 * @return
	 */
	@RequestMapping("/luke")
	public String luke()
	{
		return "recordd/recordd";
	}
	
	
	

	public String getIP(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
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
