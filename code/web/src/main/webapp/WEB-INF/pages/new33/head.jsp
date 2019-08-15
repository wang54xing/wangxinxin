<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<script type="text/javascript">
    $(function () {
        $('.top-nav a').each(function (i) {
            $(this).removeClass("active");
        });
        $('.a' + $('#type').val()).addClass("active");
    })
</script>
<style>
    .head-cont {
        position: relative;
    }

    #defaultTerm {
        float: none;
        margin-top: 0px;
        color: white;
        position: absolute;
        left: 0px;
        bottom: 3px;
    }
</style>

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


    function ssoLoginout() {
        var logoutURL = "http://ah.sso.cycore.cn/sso/logout";

        $.ajax({
            url: logoutURL,
            type: "GET",
            dataType: 'jsonp',
            jsonp: "callback",
            crossDomain: true,
            cache: false,
            success: function (html) {

            },
            error: function (data) {

            }
        });
    }
</script>
<input id="type" value="${type}" hidden>

<div class="header">
    <div class="head-cont">
        <c:if test="${roles:isStudent(sessionValue.userRole)||roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)||sessionValue.n33Admin==1}">
            <img src="/static_new/images/new33/logo.png">
        </c:if>
        <c:if test="${roles:isTeacher(sessionValue.userRole)&&!roles:isManager(sessionValue.userRole) &&!roles:isHeadmaster(sessionValue.userRole)&&sessionValue.n33Admin==0}">
            <img src="/static_new/images/new33/logo1.png">
        </c:if>
        <div class="top-nav">
            <c:if test="${roles:isStudent(sessionValue.userRole)}">
                <a href="/newisolatepage/stuselect.do" class="a1">学生选科</a>
                <a href="/newisolatepage/pStuClassTimeStu.do" class="a5">查看课表</a>
            </c:if>
            <c:if test="${roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)||sessionValue.n33Admin==1}">
                <a href="/newisolatepage/selectResult.do" class="a1">学生选科</a>
                <a href="/newisolatepage/studentManage.do" class="a8">学生管理</a>
                <a href="/newisolatepage/placementSet.do" class="a9">分班管理</a>
                <a href="/newisolatepage/fenbanInfoSet.do" class="a2">教学班管理</a>
                <a href="/newisolatepage/pkTable.do" class="a3">排课</a>
                <a href="/newisolatepage/kstkClass.do" class="a4">调课</a>
                <a href="/newisolatepage/pTeaClassTime.do" class="a5">查看课表</a>
                <a href="/newisolatepage/classpage.do" class="a7">基础数据</a>
                <a href="/newisolatepage/set.do" class="a6">设置</a>
            </c:if>
            <c:if test="${roles:isTeacher(sessionValue.userRole)&&!roles:isManager(sessionValue.userRole) &&!roles:isHeadmaster(sessionValue.userRole)&&sessionValue.n33Admin==0}">
                <%--<a href="/newisolatepage/MyClass.do" class="a1">我的班級</a>--%>
                <a href="/newisolatepage/MyTimetable.do" class="a5">查看课表</a>
                <a href="/newisolatepage/MyClassTi.do" class="a3">调代课</a>
                <%--<a href="/newisolatepage/teaDemand.do" class="a10">教师需求</a>--%>
            </c:if>
        </div>
        <span class="newPage-top-right fr"
              style="position:absolute;right:0px;line-height: 24px;padding-top: 20px;text-align: right;font-size: 14px;color: white;margin-top: 16px;">
            <i>${sessionValue.userName}</i>
            <input type="hidden" value="${sessionValue.userRole}" id="roleuser">
            <span id="fz_out" style="cursor: pointer;font-size:small; " onclick="loginout();"
                  class="login-dis"> [退出]</span>
            <br/>
            <i id="qx"></i>
        </span>
        <span id="defaultTerm"></span>
    </div>
</div>