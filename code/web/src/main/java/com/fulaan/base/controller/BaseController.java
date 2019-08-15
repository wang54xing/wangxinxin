package com.fulaan.base.controller;

import java.util.List;

import com.db.new33.isolate.N33_DefaultTermDao;
import com.db.new33.isolate.TermDao;
import com.pojo.app.SessionValue;
import com.pojo.new33.isolate.N33_DefaultTermEntry;
import com.pojo.new33.isolate.TermEntry;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
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
	private TermDao termDao = new TermDao();
	private N33_DefaultTermDao defaultTermDao = new N33_DefaultTermDao();
	

	public SessionValue getSessionValue() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();   
		return (SessionValue)request.getAttribute(SESSION_VALUE);
	}
	
	/**
	 * 获取默认的学期id
	 * @return
	 */
	public final ObjectId getDefauleTermId(){
		N33_DefaultTermEntry defaultTerm = defaultTermDao.findDefaultTermEntryBySchoolId(getSchoolId());
		N33_DefaultTermEntry defaultTermEntry = new N33_DefaultTermEntry();
		if(defaultTerm == null){
			long current = System.currentTimeMillis();
			TermEntry entry = termDao.getTermByTime(getSchoolId(), current);
			if(entry == null) {
				List<TermEntry> list = termDao.getIsolateTermEntrysBySid(getSchoolId());
				if(list.size() > 1){
					long min = Math.min(Math.abs(list.get(0).getStartTime()-current), Math.abs(list.get(0).getEndTime()-current));
					for (TermEntry termEntry : list) {
						if(min > Math.min(Math.abs(termEntry.getStartTime()-current), Math.abs(termEntry.getEndTime()-current))){
							min = Math.min(Math.abs(termEntry.getStartTime()-current), Math.abs(termEntry.getEndTime()-current));
							entry = termEntry;
						}
					}
				}else if (list.size() == 1){
					entry = list.get(0);
				}
				if(entry != null){
					defaultTermEntry.setXqid(entry.getID());
					defaultTermEntry.setSid(getSchoolId());
					defaultTermEntry.setPaikeXqid(entry.getID());
					defaultTermEntry.setPaikeci(entry.getPaiKeTimes().get(0).getID());
					defaultTerm = defaultTermEntry;
					defaultTermDao.addDefaultTerm(defaultTerm);
				}
			}

		}
		if(defaultTerm != null){
			return defaultTerm.getXqid();
		}else{
			return null;
		}
	}

	public final N33_DefaultTermEntry getDefaultPaiKeTerm(){
		N33_DefaultTermEntry defaultTerm = defaultTermDao.findDefaultTermEntryBySchoolId(getSchoolId());
		if(defaultTerm == null){
			long current = System.currentTimeMillis();
			TermEntry entry = termDao.getTermByTime(getSchoolId(), current);
			if(entry == null) {
				List<TermEntry> list = termDao.getIsolateTermEntrysBySid(getSchoolId());
				if(list.size() > 1){
					long min = Math.min(Math.abs(list.get(0).getStartTime()-current), Math.abs(list.get(0).getEndTime()-current));
					for (TermEntry termEntry : list) {
						if(min > Math.min(Math.abs(termEntry.getStartTime()-current), Math.abs(termEntry.getEndTime()-current))){
							min = Math.min(Math.abs(termEntry.getStartTime()-current), Math.abs(termEntry.getEndTime()-current));
							entry = termEntry;
						}
					}
				}else if (list.size() == 1){
					entry = list.get(0);
				}
			}
			if(entry != null){
				N33_DefaultTermEntry defaultTermEntry = new N33_DefaultTermEntry();
				defaultTermEntry.setXqid(entry.getID());
				defaultTermEntry.setSid(getSchoolId());
				defaultTermEntry.setPaikeXqid(entry.getID());
				defaultTermEntry.setPaikeci(entry.getPaiKeTimes().get(0).getID());
				defaultTerm = defaultTermEntry;
				defaultTermDao.addDefaultTerm(defaultTerm);
			}
		}
		return defaultTerm;
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
	
	
	public String getCookieUserKeyValue(HttpServletRequest request)
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
}
