<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2017/5/4
  Time: 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.pojo.app.SessionValue"%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>首页</title>
    <link rel="stylesheet" type="text/css" href="/static_new/css/appmanage/reset.css">
    <script type="text/javascript" src="/static/js/askleave/jquery-2.1.1.min.js"></script>
    <script>
        function loginout(t) {
            $.ajax({
                url: "/user/logout.do",
                type: "post",
                dataType: "json",
                data: {
                    'inJson': true
                },
                success: function (data) {
                    window.location.href = "/";
                }
            });
            ssoLoginout();
        }
    </script>
</head>
<body>
<div class="nroll-main">
    <div class="nroll-top">
        <div class="top-main">
            <c:if test="${ not empty sessionValue.schoolLogo }">
                <img class="nroll-top-left" src="${sessionValue.schoolLogo}">
            </c:if>

            <div class="nroll-top-right">
                <%--<button class="nroll-admin">应用管理</button>--%>
                <%--<button class="nroll-style">主体风格</button>--%>
                <%--<img src="/images/desk/nroll-pel.png">--%>
                    <span>欢迎您，${sessionValue.userName}</span>
                    <i onclick="loginout()">[退出]</i>
                    <c:if test="${roles:isStudent(sessionValue.userRole)}">
                        <a href="/stuPersonal/home.do" target="_blank" style="color: white;">[个人中心]</a>
                    </c:if>
                    <c:if test="${!roles:isStudent(sessionValue.userRole)}">
                        <a href="/teaPersonal/home.do" target="_blank" style="color: white;">[个人中心]</a>
                    </c:if>

            </div>
        </div>
    </div>
    <!--主体-->
    <div class="main">
        <div>
            <div class="main-left">

            </div>
            <script type="text/template" id="main-left_templ">
                {{ if(it.message.length>0){ }}
                {{ for (var i = 0, l = it.message.length; i < l; i++) { }}
                {{var obj=it.message[i];}}
                <dl id="page{{=i}}" class="clearfix">
                    <dt>{{=obj.name}}</dt>
                    <dd>
                        <ul>
                            {{~obj.navigationDTOs:value:index}}
                                <li class="appLink" pth="{{=value.link}}" nvid="{{=value.navId}}">
                                    <img src="{{=value.image}}">
                                    <em>{{=value.name}}</em>
                                </li>
                            {{~}}
                            {{if(obj.navigationDTOs.length%6!=0) {}}
                                {{for(var j=0;j<6-obj.navigationDTOs.length%6;j++){}}
                            <li></li>
                            {{}}}
                            {{}}}
                        </ul>
                    </dd>
                </dl>
                {{ } }}
                {{ } }}
            </script>
            <div class="main-right">
                <dl id="appList">

                </dl>
                <script type="text/template" id="appList_templ">
                    {{ if(it.message.length>0){ }}
                    {{ for (var i = 0, l = it.message.length; i < l; i++) { }}
                    {{var obj=it.message[i];}}
                    <dt ><a href="#page{{=i}}" class="nroll-yv {{?i==0}}nroll-y{{?}}">{{=obj.name}}</a></dt>
                    {{?i!=it.message.length-1}}<dd></dd>{{?}}
                    {{ } }}
                    {{ } }}
                </script>
            </div>
        </div>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('nroll');
</script>
</body>
</html>
