package com.fulaan.interceptor;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fulaan.annotation.UserDataCollection;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.pojo.app.SessionValue;
import com.sys.constants.Constant;

/**
 * 
 * 用户参数收集
 * @author fourer
 *
 */
public class UserDataCollectionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		HandlerMethod method =(org.springframework.web.method.HandlerMethod)handler;
		UserDataCollection s =method.getMethodAnnotation(UserDataCollection.class);
		
		if(null!=s)
		{
			boolean isNeed=false;
			SessionValue sv=(SessionValue)request.getAttribute(BaseController.SESSION_VALUE);
			String key=MessageFormat.format(CacheHandler.K6KT2_USER_DATA_COLLECTION, sv.getId(),s.pageTag());
			
			Map<String,String> cacheMap= CacheHandler.getMapValue(key);
			
			if(null==cacheMap)
			{
				cacheMap=new HashMap<String, String>();
			}
			
			Map<String,MethodParameter> paramMap=buildParamMap(method);
			
			if(!paramMap.isEmpty())
			{
				for(String str:s.params())
				{
					if(paramMap.containsKey(str))
					{
						MethodParameter mp=paramMap.get(str);
						
						String paramValue=request.getParameter(str);
						
						RequestParam rp=	mp.getParameterAnnotation(RequestParam.class);
						
						String cacheValue=cacheMap.get(str);
						
						String def=null;
						if(null!=rp)
						{
							def=rp.defaultValue();
						}
						boolean isShouleChche=isShouldCache(cacheValue, paramValue, def);
						
						
						if(s.isAllCollection()) //如果要求参数一起收集的 话，
						{
							if(!isShouleChche)
							{
								return true;
							}
							else
							{
								isNeed=true;
								cacheMap.put(str, paramValue);
							}
						}
						else
						{
							if(isShouleChche)
							{
								isNeed=true;
								cacheMap.put(str, paramValue);
							}
						}
					}
				}
			}
			
			if(isNeed)
			{
			    CacheHandler.cache(key, cacheMap, Constant.SECONDS_IN_DAY*365);
			}
		}
		return true;
		
	}
	
	/**
	 * 是不是应该缓存
	 * @param cacheValue
	 * @param paramValue
	 * @param defaultValue
	 * @return
	 */
	private boolean isShouldCache(String cacheValue,String paramValue,String defaultValue)
	{
		if(StringUtils.isBlank(paramValue))
		{
			return false;
		}
		if(StringUtils.isNotBlank(defaultValue))
		{
			if(paramValue.equals(defaultValue))
			{
				//return false;
			}
		}
		
		if(StringUtils.isNotBlank(cacheValue))
		{
			if(cacheValue.equals(paramValue))
			{
				//return false;
			}
		}
		
		return true;
	}
	
	private Map<String,MethodParameter> buildParamMap(HandlerMethod method)
	{
		Map<String,MethodParameter> map =new HashMap<String, MethodParameter>();
		
		MethodParameter[] params=method.getMethodParameters();
		
		if(null!=params)
		{
			for(MethodParameter par:params)
			{
				if(StringUtils.isNotBlank(par.getParameterName()))
				{
				  map.put(par.getParameterName(), par);
				}
			}
		}
		
		return map;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
