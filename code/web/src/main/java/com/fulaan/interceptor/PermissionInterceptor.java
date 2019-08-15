package com.fulaan.interceptor;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fulaan.annotation.UserPermissions;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.pojo.app.SessionValue;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.exceptions.UnLoginException;

public class PermissionInterceptor implements HandlerInterceptor {

	private static String FORMATAT=",{0},";
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		HandlerMethod method =(org.springframework.web.method.HandlerMethod)handler;
		UserPermissions up =method.getMethodAnnotation(UserPermissions.class);
		UserRoles ur =method.getMethodAnnotation(UserRoles.class);
		
		if(null!=up || null!=ur)
		{
			SessionValue sv=(SessionValue)request.getAttribute(BaseController.SESSION_VALUE);
			
			if(null==sv)
				throw new UnLoginException();
			
			if(null!=up)
			{
				String permissionFormat=MessageFormat.format(FORMATAT, up.value());
				
				String removePermission =sv.getUserRemovePermission();
				if(StringUtils.isNotBlank(removePermission))
				{
					if(removePermission.indexOf(permissionFormat)>=0)
					{
						throw new PermissionUnallowedException(handler.toString());
					}
				}
				
				String havePermission =sv.getUserPermission();
				if(StringUtils.isNotBlank(havePermission))
				{
					if(havePermission.indexOf(permissionFormat)>=0)
					{
						return true;
					}
				}
			}
			
			
			if(null!=ur)
			{
				int userRole =sv.getUserRole();
				UserRole[] removeRoles =ur.noValue();
				if(removeRoles.length>Constant.ZERO)
				{
					boolean  isIn=UserRole.isInRoles(userRole, removeRoles);
					if(isIn)
						throw new PermissionUnallowedException(handler.toString());
				}
				UserRole[] allowRoles =ur.value();
				if(allowRoles.length>Constant.ZERO)
				{
					boolean  isIn= UserRole.isInRoles(userRole, allowRoles);
					if(!isIn)
						throw new PermissionUnallowedException(handler.toString());
				}
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	

}
