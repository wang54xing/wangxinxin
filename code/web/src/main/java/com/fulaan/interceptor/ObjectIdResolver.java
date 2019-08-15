package com.fulaan.interceptor;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fulaan.annotation.ObjectIdType;
import com.sys.exceptions.IllegalParamException;

public class ObjectIdResolver implements HandlerMethodArgumentResolver {

	
	public static Class<?> CLASS_TYPE=null;
	
	static
	{
		try
		{
		  CLASS_TYPE=Class.forName("org.bson.types.ObjectId");
		}catch(Exception ex){}
	}
	
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		
		ObjectIdType type=	parameter.getParameterAnnotation(ObjectIdType.class);
		if(null!=type && CLASS_TYPE.equals(parameter.getParameterType()) )
		{
			return true;
		}
		
		return false;
	}
	

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		
		ObjectIdType type=	parameter.getParameterAnnotation(ObjectIdType.class);
		String field =parameter.getParameterName();
		if(StringUtils.isNotBlank(type.field()))
		{
			field=type.field();
		}
	
		String fieldValue =webRequest.getParameter(field);
		
		
		if(!type.isRequire())
		{
			if(null==fieldValue)
			{
				return null;
			}
			if(null==fieldValue || !ObjectId.isValid(fieldValue))
			{
				return null;
			}
			return new ObjectId(fieldValue);
		}
		if(null==fieldValue || !ObjectId.isValid(fieldValue))
		{
			throw new IllegalParamException();
		}
		return new ObjectId(fieldValue);
	}


}
