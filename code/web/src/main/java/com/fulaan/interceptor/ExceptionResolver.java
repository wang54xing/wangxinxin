package com.fulaan.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fulaan.base.controller.BaseController;
import com.google.gson.Gson;
import com.sys.constants.Constant;
import com.sys.exceptions.UnBindException;
import com.sys.exceptions.UnLoginException;
import com.sys.utils.RespObj;

/**
 * 统一处理系统异常
 * @author fourer
 *
 */
public class ExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger =Logger.getLogger(ExceptionResolver.class);
	private static final String ERROR_JSON_STR;
	
	static
	{
		RespObj FAILD=new RespObj(Constant.FAILD_CODE,"服务器太忙了，稍后在试下吧");
		ERROR_JSON_STR=new Gson().toJson(FAILD);
	}
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		org.springframework.web.method.HandlerMethod method =(org.springframework.web.method.HandlerMethod)handler;
		ResponseBody rb=method.getMethodAnnotation(ResponseBody.class);
		
		
		if(!(ex instanceof UnLoginException))
		{
			logger.error("user="+request.getAttribute(BaseController.SESSION_VALUE));
			logger.error("method="+method);
		    logger.error("", ex);
		}
		
		if(null!=rb)
		{
			response.setCharacterEncoding(Constant.UTF_8);
		    response.setContentType("application/json; charset=utf-8");  
		    PrintWriter out = null;  
		    try {  
		        out = response.getWriter();  
		        out.append(ERROR_JSON_STR);  
		    } catch (Exception e) {  
		    	
		    } finally {  
		        if (out != null) {  
		            out.close();  
		        }  
		    }
		}
		else
		{
			if(ex instanceof UnLoginException)
			{
				try {                             
				    request.getRequestDispatcher(Constant.BASE_PATH).forward(request, response);
					return null;
				} catch (Exception e) {
				}
			}
			
			if(ex instanceof UnBindException)
			{
				try {                             
				    request.getRequestDispatcher("/ah").forward(request, response);
					return null;
				} catch (Exception e) {
				}
			}
			
			return new ModelAndView("error/error");
		}
		return null;
	}

}
