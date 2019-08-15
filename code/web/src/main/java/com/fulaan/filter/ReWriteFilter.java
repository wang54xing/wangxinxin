package com.fulaan.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by Hao on 2015/4/24.
 */
public class ReWriteFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /** 头像控件上传请求，拦截刀对应的controller
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String a=servletRequest.getParameter("a");
        if("rectavatar".equals(a)){
            servletRequest.getRequestDispatcher("/personal/avataredit.do").forward(servletRequest,servletResponse);
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
