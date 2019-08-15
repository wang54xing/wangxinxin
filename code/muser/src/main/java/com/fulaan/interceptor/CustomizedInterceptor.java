package com.fulaan.interceptor;

import com.fulaan.annotation.Customized;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.cache.CacheHandler;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by admin on 2016/8/16.
 */
public class CustomizedInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
        HandlerMethod method =(org.springframework.web.method.HandlerMethod)arg2;

        Customized s =method.getMethodAnnotation(Customized.class);
        String ui=getCookieUserKeyValue(arg0);
        if(null!=s)
        {
//            Object object=arg0.getSession().getAttribute("login");
            if(StringUtils.isNotBlank(ui)){

                Map<String,String> map=CacheHandler.getMapValue(ui);
                if(null==map ||map.isEmpty()){
                    arg1.sendRedirect("/customized/hubei/hubeiLogin.do");
                    return false;
                }
//            }
//            if(null!=object){
//                Boolean flag=Boolean.valueOf(object.toString());
//                Map<String,String> map= CacheHandler.getMapValue("");
//                if(!flag){
//                    arg1.sendRedirect("/customized/hubei/hubeiLogin.do");
//                    return false;
//                }
            }else{
                arg1.sendRedirect("/customized/hubei/hubeiLogin.do");
                return false;
            }
        }
        return true;
    }

    private String getCookieUserKeyValue(HttpServletRequest request)
    {
        Cookie[] cookies=request.getCookies();
        if(null!=cookies)
        {
            for(Cookie cookie:cookies)
            {
                if(cookie.getName().equals(Constant.COOKIE_PROVINCE_LOGIN))
                {
                    return cookie.getValue();
                }
            }
        }
        return Constant.EMPTY;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
