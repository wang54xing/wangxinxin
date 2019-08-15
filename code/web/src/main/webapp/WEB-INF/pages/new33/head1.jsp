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
        <%--<c:if test="${roles:isTeacher(sessionValue.userRole)&&!roles:isManager(sessionValue.userRole) &&!roles:isHeadmaster(sessionValue.userRole)&&sessionValue.n33Admin==0}">--%>
            <img src="/static_new/images/new33/logo1.png">
        <%--</c:if>--%>
        <div class="top-nav">
        <a href="/newisolatepage/teaDemand.do" class="a10">教师需求</a>
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