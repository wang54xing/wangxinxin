<%@page import="com.fulaan.cache.CacheHandler"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.bson.types.ObjectId"%>
<%@page import="com.pojo.app.SessionValue"%>
<%@ page import="java.net.URLDecoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <meta name="renderer" content="webkit">
    <title>K6KT-快乐课堂</title>
    <link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/nivo-slider.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
    <script type="text/javascript" src="/static/js/jquery.nivo.slider.js"></script>
    <script type="text/javascript" src="/static/plugins/bjqs-1.3.min.js"></script>
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
        		if(null!= uid &&     ObjectId.isValid(uid))
        		{
        		  response.sendRedirect("/user");
        		}
        	}
        }
    %>
    <script type="text/javascript">
        $(function () {
            var strCookie = document.cookie;
            var arrCookie = strCookie.split("; ");
            var closeflag = false;
            for (var i = 0; i < arrCookie.length; i++) {
                var arr = arrCookie[i].split("=");
                if ("closesheep" == arr[0]) {
                    closeflag = true;
                }
            }
            if (!closeflag) {
                document.getElementById('light').style.display = 'block';
            }
        });

        function closesheepdiv() {
            document.cookie = "closesheep=true";
            $('#light').hide();
        }
    </script>

    <script language="javascript" type="text/javascript">

        $(window).load(function () {
            $('#my-slideshow').bjqs({
                'height': 150,
                'width': 850,
                'animspeed': 3500,
                responsive: true,
                showcontrols: true,
                centercontrols: true,
                usecaptions: false,
                showmarkers: true
            });
        });

        var $c = function (array) {
            var nArray = [];
            for (var i = 0; i < array.length; i++) nArray.push(array[i]);
            return nArray;
        };
        Array.prototype.each = function (func) {
            for (var i = 0, l = this.length; i < l; i++) {
                func(this[i], i);
            }
        };
        document.getElementsByClassName = function (cn) {
            var hasClass = function (w, Name) {
                var hasClass = false;
                if(w.className) {
                    w.className.split(' ').each(function (s) {
                        if (s == Name) hasClass = true;
                    });
                }
                return hasClass;
            };
            var elems = document.getElementsByTagName("*") || document.all;
            var elemList = [];
            $c(elems).each(function (e) {
                if (hasClass(e, cn)) {
                    elemList.push(e);
                }
            })
            return $c(elemList);
        };
        function change_bg(obj) {
            var a = document.getElementsByClassName("nav")[0].getElementsByTagName("a");
            for (var i = 0; i < a.length; i++) {
                a[i].className = "";
            }
            obj.className = "current";
        }
        function initializelink() {
            //var fulaan = document.getElementById('fulaan-link');
            var business = document.getElementById('business-link');
            var winWidth = window.innerWidth;
            if (winWidth > 1500) {
                //fulaan.style.left=(winWidth-winWidth*0.9)+'px';
                //fulaan.style.top='164px';
                //business.style.top = '266px';
            } else {
                //fulaan.style.left=(winWidth-winWidth*0.85)+'px';
                //fulaan.style.top=winWidth*0.1+'px';
                //business.style.top = winWidth * 0.17 + 'px';
                ;
            }
        }
        $(function () {
            initializelink();
            window.onresize = function () {
                initializelink();
            }
            $('.bjqs-controls.v-centered li a').html('');
        });
    </script>
</head>
<body>
<%@ include file="/WEB-INF/pages/common/ypxxhead.jsp" %>

<div id="intro-player">
    <div id="player_div">

    </div>
    <span onclick="closeMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span>
</div>

<div class="main-container">
    <div class="login-bar" style="margin: 0 auto;">
        <div class='title-bar-container' style="height:105px;overflow: hidden">
            <img class="title-logo" src="/img/K6KT/main-page/logo.png">
            <!--申请试用-->
            <%--<a id="teacher-test" href="/application-use">教师申请试用</a>--%>
            <a class="login-btn" href="javascript:;">登 录</a>
            <input id="input-first" class="input-password" type="password" placeholder="密码" tabindex="2">
            <input id="input-last" class="input-account" type="text" placeholder="用户名" tabindex="1"
                   value="<%=userName%>">

            <div id="tips-msg">
                <span class="password-error">密码错误</span>
                <!--a class="forget-pass" href='#'>忘记密码？</a-->
                <span class="username-error">用户名不存在</span>
            </div>
        </div>
    </div>
    <div class="main-content-container">
        <div class='content-container'>
            <img class="text-1" src="/img/K6KT/main-page/text-1.png"/>
            <img class="text-2" src="/img/K6KT/main-page/text-2.png"/>

            <a onclick="playMovie()">
                <div class="player-hand">
                    <div></div>
                </div>
            </a>

            <div class="teacher-apply" onclick="go2appuse()">
                教师申请试用
            </div>
            <div class="app-link">
                <img src="/img/K6KT/iphone.png"/>

                <div>
                    <div>手机客户端 APP</div>
                    <div>
                        <span><img src="/img/K6KT/ios.png"/></span>
                        <span>|</span>
                        <span><img src="/img/K6KT/android.png"/></span>
                        <span><a href="/mobile">点击下载 ></a></span>
                    </div>
                </div>
            </div>

            <div class="monitor-div">
                <div class="carousel nivoSlider" id='slider'>
                    <img src="/img/K6KT/main-page/screen-1.png"/>
                    <!--img src="/img/K6KT/main-page/screen-2.png"/ -->
                </div>
            </div>
        </div>

    </div>
    <div style="height: 30px;text-align: center;line-height: 30px;;width: 100%;background-color:#EAE9DC;position: relative;top:-6px;border-bottom: 1px solid #C0C0C0;font-family: 'Microsoft YaHei';font-weight: bold;color: #5a5a5a">
        <!-- <p>欢迎北京大学入驻复兰大学K6KT-翻转课堂</p> -->
    </div>
    <div style="clear: both"></div>
    <div class="new-schoolelogo" id="my-slideshow">
        <div style="line-height: 150px;">
            <ul class="bjqs" id="new-all">
                <li width="850" height="100">
                    <a href=""></a><img src="/img/K6KT/main-page/01.png" alt="1"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_2.png" alt="2"
                                        style="width: 200px;height: 100px !important;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_3.png" alt="3"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_4.png" alt="4"
                                        style="width: 200px;height: 100px !important;;"/>
                </li>
                <li>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_5.png" alt="1"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_6.png" alt="2"
                                        style="width: 200px;height: 100px !important;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_1%20.png" alt="3"
                                        style="width: 200px;height: 100px  !important;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_8.png" alt="4"
                                        style="width: 200px;height: 100px !important;;"/>
                </li>
                <li>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_9.png" alt="1"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_115.png" alt="3"
                                        style="width: 200px;height: 100px !important;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_11.png" alt="3"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_12.png" alt="4"
                                        style="width: 200px;height: 100px !important;;"/>
                </li>
                <li>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_13.png" alt="1"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_14.png" alt="2"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_15.png" alt="3"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_16.png" alt="4"
                                        style="width: 200px;height: 100px !important;;"/>
                </li>
                <li>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_17.png" alt="1"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_18).png" alt="2"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_19.png" alt="3"
                                        style="width: 200px;height: 100px !important;;"/>
                    <a href=""></a><img src="/img/K6KT/main-page/newmain_logo_20.png" alt="4"
                                        style="width: 200px;height: 100px !important;;"/>
                </li>
            </ul>
        </div>
    </div>
</div>
<!-- 页尾 -->
<%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>

<div id="light" class="white_content" style="display: none">

    <a onclick="closesheepdiv()" class="close_but"></a>
</div>
<!-- 页尾 -->
</body>
<script>
    function go2appuse(){
        window.location.href="/customizedpage/application.jsp";
    }
</script>
<script>
    $(function () {
        var h = $(window).height()
        var w = $(window).width();
        if (h < 700) {
            $("#II").css({height: 276, width: 401, marginLeft: -200, marginTop: -138})
        }
    })

</script>
</html>