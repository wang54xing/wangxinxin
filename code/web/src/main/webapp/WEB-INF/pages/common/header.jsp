<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
<link href="/static/css/font-awesome.min.css" rel="stylesheet">
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
    getPrivateLetterCount();
    //setInterval("getPrivateLetterCount()", 60000);//定时刷新未读私信数
    function getPrivateLetterCount() {
        $.ajax({
            url: "/letter/count.do",
            type: "post",
            data: {
                'inJson': true
            },
            success: function (data) {
                //$("#sl").html(data.messageCount);
                if (data > 0) {
                    $('#message').css('background', 'url(/img/newmessage.png) no-repeat 5px 0px');
                    $("#sl").html(data);
                    jQuery("#xyd").html(data);
                } else {
                    $('#message').css('background', 'url(/img/nomessage.png) no-repeat 10px 10px');
                    jQuery("#xyd").hide();
                }
            }
        });
    }

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


<div class="homepage-top" style="font-size: 15px;background-color: #fda616;position: relative;color: white;line-height: 30px;overflow: visible">
  <div class="homepage-top-main">
    <div class="homepage-top-left" style="height: 50px; margin-top: -10px;">

          <img alt="" style="max-height: 50px;margin-right:5px;vertical-align: initial;width:auto;height: 48px;" src="${sessionValue.schoolLogo}">

    </div>
    <div class="homepage-top-right">

        <i class="fa fa-envelope"></i>
        <span class=" homepage-i" onclick="location.href='/message'">私信</span>
        <i class="fa fa-gear homepage-i" style="cursor: default;text-decoration: none"></i>
        <span class=" homepage-i" id="zhongxin">个人中心</span>
        <div class="homepage-hove"style="z-index: 99999999999999">
            <div class="homepage-hove-I" >
                <ul>
                    <li><a href="/message">我的私信</a></li>
                    <li><a href="/basic">个人设置</a></li>
                    <li><a href="/personal/userhelp">用户手册</a></li>
                    <li><a href="/friendcircle/getFriendList.do?type=0">我的好友</a></li>
                </ul>
            </div>
        </div>
        <span class="homepage-message" style="position: relative;left: -130px" id="xyd"></span>

          <span>欢迎您，${sessionValue.userName} <span id="fz_out" style="cursor: pointer;font-size:small; " onclick="loginout();"> [退出]</span></span>
      </div>
    </div>
</div>