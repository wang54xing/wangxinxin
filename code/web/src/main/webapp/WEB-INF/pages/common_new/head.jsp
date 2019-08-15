<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/18
  Time: 下午4:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@page import="com.pojo.app.SessionValue"%>
<%--<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>--%>
<script>
    var _hmt = _hmt || [];
    (function() {
        var hm = document.createElement("script");
        hm.src = "//hm.baidu.com/hm.js?0a755e8f7dd7a784eafc0ef0288e0dff";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
</script>


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
                window.location.href = "/";
            }
        });
        ssoLoginout();
    }
    
    function ssoLoginout () {
        var logoutURL = "http://221.214.55.21:6603/dsssoserver/logout";
        
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
<div id="head">

    <div class="sub-head clearfix">


        <div class="head-right">
            <a href="/message"  class="user-msg">私信</a>
                <span class="user-cener">
                    <a href="javascript:;">个人中心</a>
                    <ul style="display:none;">
                        <li><a href="/message">我的私信</a></li>
                        <li><a href="/basic">个人设置</a></li>
                        <li><a href="/personal/userhelp">用户手册</a></li>
                        <li><a href="/friendcircle/friendlist">我的好友</a></li>
                    </ul>
                </span>
            <span>欢迎您，${sessionValue.userName} [ <a href="javascript:_loginout();">退出</a> ]</span>
        </div>
        <img alt="" src="${sessionValue.schoolLogo}">
    </div>

</div>
