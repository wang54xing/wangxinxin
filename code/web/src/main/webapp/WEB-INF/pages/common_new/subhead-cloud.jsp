<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/18
  Time: 下午4:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@page import="com.pojo.app.SessionValue"%>



<script type="text/javascript">
    function _loginout(t) {
        $.ajax({
            url: "/user/logout.do",
            type: "post",
            dataType: "json",
            data: {
                'inJson': true
            },
            success: function (data) {
                window.location.href = "${sessionValue.cloudUrl}";
            }
        });
    }
</script>
    <div class="sub-head clearfix">

        <a href="http://yun.k6kt.com/user/gotomainpage.do" class="logo1" style="background: url('../../img/K6KT/LOGO_03.png') 0 0 no-repeat;"></a>
        <div class="div-pt">
            <c:if test="${roles:isEducation(sessionValue.userRole)}">
                <a href="/manageCount/countMain.do?version=90&tag=1" class="head-xiao a-ptwh" target="_blank">平台维护</a>
                <a href="#" class="head-xiao disable-head a-jrx" >校级平台</a>
            </c:if>
            <c:if test="${!roles:isEducation(sessionValue.userRole)}">
                <a href="#" class="head-xiao disable-head a-ptwh">平台维护</a>
                <a href="/user/homepage.do" class="head-xiao a-jrx" target="_blank">校级平台</a>
            </c:if>
        </div>
        <div class="head-right login-head">


            <span style="color: #666;font-size: 12px;margin-right: 18.5px;">您好，${sessionValue.userName}</span>

                <a href="javascript:_loginout();">[退出]</a>

        </div>
    </div>

