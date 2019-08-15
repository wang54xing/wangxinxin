<%@page import="com.fulaan.cache.CacheHandler"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.bson.types.ObjectId"%>
<%@page import="com.pojo.app.SessionValue"%>
<%@ page import="java.net.URLDecoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html style="background:white">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <meta property="qc:admins" content="223027642741216375" />
    <meta name="renderer" content="webkit">
    <title>智慧校园-中国专业的K12教育学习平台</title>
    <link rel="stylesheet" type="text/css" href="/static_new/css/index/wall.css.css" />
    <link rel="stylesheet" type="text/css" href="/static_new/css/index/style.css" />
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/skin/layer.css"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
        <%

        String ui="";
        String userName = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("momcallme".equals(cookie.getName())) {
                    userName = URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
                if ("ui".equals(cookie.getName())){
                	ui=cookie.getValue();
                }
            }
        }

        if(StringUtils.isNotBlank(ui))
        {
        	SessionValue sv=CacheHandler.getSessionValue(ui);
        	if(null!=sv)
        	{
        		String uid=sv.getId();
        		if(null!= uid &&     ObjectId.isValid(uid) && sv.getK6kt()==1)
        		{
        		  response.sendRedirect("/newisolatepage/selectResult.do");
        		}
                if(null!= uid && ObjectId.isValid(uid) && sv.getK6kt()==0)
                {
                  //  response.sendRedirect("www.fulaan.com");
                }
        	}
        }
    %>
    <script>
        var _hmt = _hmt || [];
        (function() {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?0a755e8f7dd7a784eafc0ef0288e0dff";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>

    <body>
    <div class="login-box">
        <div class="login-top">
            <img src="/static_new/images/index/logo.png" alt="智慧校园" />
            <p>中国K12教育学习专业平台</p>
        </div>
        <div class="login-input">
            <div><input type="text" placeholder="账号" tabindex="1" id="input-last" class="input-account"/><span class="username-error">用户名不存在</span></div>
            <div><input type="password" placeholder="密码" id="input-first" class="input-password" tabindex="2"/><span class="password-error">密码输入错误</span></div>
        </div>

        <button class="btn login-btn">登录</button>

        <%--<div class="login-bot">--%>
            <%--<div>--%>
                <%--<span class="check">√</span><span class="txt">记住密码</span>--%>
            <%--</div>--%>
            <%--<a>忘记密码?</a>--%>
        <%--</div>--%>
    </div>
    <img style="visibility: hidden;" src="/static_new/images/index/del.png" alt="" id="img" />
    <div id="wall-bg">
        <span></span>
        <canvas width="1920" height="901" style="width: 100%; height: 100%;"></canvas>
    </div>

    <!-- <div class="bg"></div> -->
    </body>
<script type="text/javascript" src="/static_new/js/modules/index/three.min.js"></script>
<script type="text/javascript" src="/static_new/js/modules/index/wallbgcanvas.js"></script>
<script type="text/javascript">

    var flag = true;
    $('.check').click(function(){
        if( flag == true ){
            $(this).css('background','#ddd');
            flag = false;
        }else{
            $(this).css('background','#3cbe64');
            flag = true;
        }

    })

    console.log(windowHalfX,windowHalfY);
</script>
  <script type='text/javascript' src='/static/js/k6kt-sso.js'></script>
</html>