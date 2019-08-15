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
    <title>K6KT-快乐课堂</title>
    <link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <%--<link rel="stylesheet" type="text/css" href="/static/css/.css"/>--%>
    <link rel="stylesheet" type="text/css" href="/static/css/nivo-slider.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
  
    <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/bjqs-1.3.min.js"></script>
    <script type="text/javascript" src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
    <script src="/static_new/js/modules/core/0.1.0/doT.min.js"></script>
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
        		  response.sendRedirect("/user/homepage.do");
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
        $(function() {
            var $this = $("#news");
            var scrollTimer;
            $this.hover(function() {
                clearInterval(scrollTimer);
            }, function() {
                scrollTimer = setInterval(function() {
                    scrollNews($this);
                }, 2000);
            }).trigger("mouseleave");

            function scrollNews(obj) {
                var $self = obj.find("ul");
                var lineHeight = $self.find("li:first").height();
                $self.animate({
                    "marginTop": -lineHeight + "px"
                }, 600, function() {
                    $self.css({
                        marginTop: 0
                    }).find("li:first").appendTo($self);
                })
            }
        })
        var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
        document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3F4bb2ddb93121ee50b935f35817841195' type='text/javascript'%3E%3C/script%3E"));

        $(window).load(function () {
            $('#my-slideshow').bjqs({
                'height' :217,
                'width' : 1920,
                'animspeed' : 3000,
                responsive : true,
                showcontrols : true,
                centercontrols : true,
                usecaptions : false,
                showmarkers : true
            });
        });

        var  $c=function(array){var nArray = [];for (var i=0;i<array.length;i++) nArray.push(array[i]);return nArray;};
        Array.prototype.each=function(func){
            for(var i=0,l=this.length;i<l;i++) {func(this[i],i);};
        };
        document .getElementsByClassName=function(cn){
            var hasClass=function(w,Name){
                var hasClass = false;
                w.className.split(' ').each(function(s){
                    if (s == Name) hasClass = true;
                });
                return hasClass;
            };
            var elems =document.getElementsByTagName("*")||document.all;
            var elemList = [];
            $c(elems).each(function(e){
                if(hasClass(e,cn)){elemList.push(e);}
            })
            return $c(elemList);
        };
        function change_bg(obj){
            var a=document.getElementsByClassName("nav")[0].getElementsByTagName("a");
            for(var i=0;i<a.length;i++){a[i].className="";}
            obj.className="current";
        }
        function initializelink() {
            //var fulaan = document.getElementById('fulaan-link');
            var business = document.getElementById('business-link');
            var winWidth = window.innerWidth;
            if(winWidth > 1900) {
                //fulaan.style.left=(winWidth-winWidth*0.9)+'px';
                //fulaan.style.top='164px';
                business.style.top='266px';
            } else {
                //fulaan.style.left=(winWidth-winWidth*0.85)+'px';
                //fulaan.style.top=winWidth*0.1+'px';
                business.style.top=winWidth*0.17+'px';;
            }
        }
        $(function() {
            initializelink();
            window.onresize = function () {
                initializelink();
            }
            $('.bjqs-controls.v-centered li a').html('');
        });
    </script>
    <%--加载分类模板--%>
    <%--<script>
        $.get("/mall/categories.do",function(data){
                    var categorys = data.goodsCategories;
                    var template = doT.template($('#categoryTemplate').text());
                    $('#categories').html(template(categorys));
                }
        );
    </script>--%>
</head>

<div id="content_main_container">
<div id="play_I"
     style="width: 745px;height: 425px;position: fixed;top: 50%;left: 50%;margin-left: -365px;margin-top:-207px;z-index: 999;display: none;background-color: rgba(255, 255, 255, 0.5);box-shadow: 0 0 10px #666;">
    <div id='sewise-div' class="video-player-container" style="height: 100%">
        <script type="text/javascript" src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
    </div>
    <div>
        <a style="display: none;position: absolute;top: 1%;left: 98%;color: #666666;z-index: 999"
           onclick="closeMoviee()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></a>
    </div>
</div>
<script type="text/javascript">
    SewisePlayer.setup({
        server: "vod",
        type: "m3u8",
        skin: "vodFlowPlayer",
        logo: "none",
        lang: "zh_CN",
        topbardisplay: 'enable',
        videourl: ''
    });
    var isFlash = false;
    function playMovie(url) {
        try {
            SewisePlayer.toPlay(url, "", 0, true);
        } catch (e) {
            playerReady.videoURL = url;
            isFlash = true;
        }
        $("#sewise-div").fadeIn();
        $("#play_I").fadeIn();
        $(".close-dialog").fadeIn();
    }



    function playerReady(name){
        if(isFlash){
            SewisePlayer.toPlay(playerReady.videoURL, "", 0, true);
        }
    }
    function closeMoviee() {
        var $player_container = $(".close-dialog");
        $player_container.fadeOut();
        $("#sewise-div").fadeOut();
        $("#play_I").fadeOut();
        /* $("#sewise-div").hide();
         $("#play_I").hide();*/
        window.location.reload();
    }
</script>
<body style="background: #ffffff">
<%--<%@ include file="/WEB-INF/pages/common/ypxxhead.jsp" %>--%>

<div id="intro-player">
    <div id="player_div">

    </div>
    <span onclick="closeMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span>
</div>
<div class="login-bar" style="margin: 0 auto;">
    <div class='title-bar-container' style="height:80px;overflow: visible">
        <%--<img class="title-logo" src="/img/K6KT/main-page/store-logo.png">--%>
        <img class="title-logo" src="/img/K6KT/main-page/ebusiness-logo-I.png">
        <!--申请试用-->
        <%--<a id="teacher-test" href="/application-use">教师申请试用</a>--%>
        <a class="login-password" href="/user/findPwd.do">忘记密码</a>
        <div class="unlogin">
           <%-- <a class="login-btm" href="javascript:;">注册商城用户</a>--%>
            <a class="login-btn" href="javascript:;">登 录</a>
                <span id="veryCodeSpan" style="display: none" class="very-code">
                    <input id="veryCode" class="input-verycode" name="veryCode" type="text"/>
                    <img id="imgObj" alt="" src=""/>
                    <a href="javascript:;" onclick="changeImg()">看不清楚？换一张</a>
                </span>
            <input id="input-first" class="input-password" type="password" placeholder="密码" tabindex="2">
            <input id="input-last" class="input-account" type="text" placeholder="用户名/邮箱/手机号" tabindex="1" value="<%=userName%>">



            <div id="tips-msg">
                <span class="verycode-error"></span>
                <span class="password-error">密码错误</span>
                <!--a class="forget-pass" href='#'>忘记密码？</a-->
                <span class="username-error">用户名不存在</span>
            </div>
        </div>
        <div class="login" hidden="true" style="width: 700px;position: relative">
            <a href="/user/homepage.do" style="width: 130px;position: absolute;left: 0px;top: 25px;" id="home"></a>
            <em style="position: relative;right: -465px;">欢迎您，</em>
            <em id="nm" style="position: relative;right: -455px;"></em>
            <em id="nmm" onclick="logout()" style="position: relative;right: -450px;">[退出]</em>
        </div>
    </div>
</div>
<div class="main-container" >
    <div class="main-content-container">
        <div class='content-container' >
            <img class="text-1" src="/img/K6KT/main-page/index_banner1.png"/>

            <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/570daeeb0cf2372af0f4b7bf.mp4.m3u8')">
                <div class="player-hand">
                    <div></div>
                   <%-- <a onclick="playMovie('http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/570daeeb0cf2372af0f4b7bf.mp4.m3u8')">
                        【观看演示视频】 </a>
                    </span>--%>
                </div>
            </a>
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
                    <%--<img src="/img/K6KT/main-page/screen-2.png"/>--%>
                </div>
            </div>
        </div>
    </div>
</div>
<%--<div class="all-main">
<div class='slide-container'>
    <table style='width:100%'>
        <tr>
            <td style='vertical-align: middle;text-align: center;'>
                <div id="my-slideshow">
                    <ul class="bjqs">
                        <li>
                            <a id='keji' href="#" target="_blank"></a>
                            <img src="/img/K6KT/main-page/store-XS.jpg" width="100%" height="100%" />
                        </li>
                        <li>
                            <a href="#"></a>
                            <img src="/img/K6KT/main-page/store-BN.jpg"width="100%" height="100%"  />
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
    </table>
</div>
</div>--%>
<div class="clearfix" style="height:18px;"></div>
<div class="store-main">
    <div class="store-mar">

        <div class="store-main-left">
            <div class="store-main-left-top"></div>
            <%--<!--===================名校风采====================-->
            <div class="store-news">
                <h1></h1>
                <div>
                    <ul>
                        <li class="store-li-I">
                            <img src="/img/K6KT/main-page/store-news-1.jpg" width="150" height="150">
                            <div class="store-info">
                                <em>上海奉贤区智慧教育推进，复兰科技助力</em>
                                <p>12月30日，由上海市奉贤区教育学院主办，肖塘中学承办的奉贤区初中学段智慧课堂展示活动拉开帷幕。奉贤区教育局副局长万国良、奉贤区教育学院院长蒋东标、奉贤区教师进修学院副院长金红卫、奉贤区教师进修学院教育培训管理中心副主任黄建龙、肖塘中学校长卜琴华出席了活动。</p>
                                &lt;%&ndash;<a href="javascript:;"> Read&nbsp;More</a>&ndash;%&gt;
                            </div>
                        </li>
                        <li class="store-li-II">
                            <img src="/img/K6KT/main-page/store-news-2.jpg" width="150" height="150">
                            <div class="store-info">
                                <em>复兰科技选拔MIT”中国实验室China Lab“项目成员</em>
                                <p>2015年12月18日，上海复兰科技基础教育部主任汤杰琴女士，受复旦大学国际MBA项目邀请和曾成桦教授共同参与2015-2016年度China Lab项目精英选拔。此次复兰的咨询需求是“Chines K12 quality education O2O community ”，围绕这个主题，</p>
                                &lt;%&ndash;<a href="javascript:;"> Read&nbsp;More</a>&ndash;%&gt;
                            </div>
                        </li>
                    </ul>
                </div>
            </div>--%>

            <a class="store-guide-banner" href="http://www.fulaan.com" target="_blank"></a>

            <!--======================精品导购========================-->
            <div class="store-guide" style="width:1000px;height:800px;display: none" >
                <h1 id="mallindex" onclick="window.open('/mall/integrate.do')"></h1>
                <div>
                    <ul id="categories">
                        <li>
                            <a href="/mall?categoryId=56eb6a1d0cf234ce7e479c24" target="_blank"><img src="/img/K6KT/main-page/store-st.png"></a>
                        </li>
                        <li>
                            <a href="/mall?categoryId=56eb6a0b0cf234ce7e479c1c" target="_blank"><img src="/img/K6KT/main-page/store-book.png"></a>
                        </li>
                        <li>
                            <a href="/mall?categoryId=56eb6a2d0cf234ce7e479c26" target="_blank"><img src="/img/K6KT/main-page/store-safe.png"></a>
                        </li>
                        <li>
                            <a href="/mall?categoryId=56eb6a350cf234ce7e479c27" target="_blank"><img src="/img/K6KT/main-page/store-state.png"></a>
                        </li>
                        <li>
                            <a href="/mall?categoryId=56eb6a440cf234ce7e479c29" target="_blank"><img src="/img/K6KT/main-page/store-toy.png"></a>
                        </li>
                    </ul>
                    <%--<script id="categoryTemplate" type="application/template">
                        {{~it:value:index}}
                        {{?value.image != ""}}
                        <li>
                            <a href="/mall?categoryId={{=value.id}}" target="_blank"><img src="{{=value.image}}"></a>
                        </li>
                        {{?}}
                        {{~}}
                    </script>--%>
                </div>
                <%--<a href="/mall">加载更多...</a>--%>
            </div>
        </div>
        <div class="store-main-right">
            <div class="store-main-right-bg" id="news" style="width: 275px;">
                <img src="/static_new/images/youhong-right.jpg">
            </div>
        </div>
    </div>
</div>
<!--=============底部版权=================-->
<div class="wind-banner-hot">
    <div class="div-hot-x"></div>
</div>
<div class="store-foott" style="min-width: 1350px;">
    <div class="store-foott-main">
        <div class="store-foott-left">
            <span>版权所有：上海复兰信息科技有限公司</span><a target="_blank" href="http://www.fulaan-tech.com">www.fulaan-tech.com</a>
                   <span>
                       <a href="/aboutus/k6kt">关于我们</a>
                       <a href="/contactus/k6kt">联系我们</a>
                       <a href="/service/k6kt">服务条款 </a>
                       <a href="/privacy/k6kt">隐私保护 </a>
                       <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank">在线客服</a>
                       <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' style="position: relative;top: 5px;"><img src="/img/QQService.png"></a>
                   </span>
            <span>沪ICP备14004857号</span>
        </div>
        <div class="store-foott-right">
            <div>
                <img src="/img/K6KT/main-page/store-phone.png">
                    <span>
                        <i>关注我们：</i>
                        <a target="_blank" href='http://weibo.com/FulaanTechnology'><img src="/img/K6KT/main-page/store-WEB.png"></a>
                        <a target="_blank" href='http://t.qq.com/FulaanTechnology'><img src="/img/K6KT/main-page/store-WEBI.png"></a>
                        <a><img src="/img/K6KT/main-page/store-WEX.png"></a>
                    </span>
            </div>
            <img src="/img/K6KT/main-page/store-WEII.jpg">
        </div>
    </div>
</div>

<!-- 页尾 -->
<%--<%@ include file="/WEB-INF/pag../common_new/foot.jsp" %>--%>
<!--=================弹出框==================-->
<!--=================背景层==================-->
<div class="bg"></div>
<div class="store-bg">
    <dl>
        <dt>
            <em>提示</em><i id="close">X</i>
        </dt>
        <dl>
            <span class="store-DL">你已登录“<em id="name">siri</em>”账号，请选择去向：</span>
        </dl>
        <dl>
            <img src="" id="avatar">
        </dl>
        <dl>
            <span>
                <a href="javascript:;">
                    <em id="stay">留在当前页面</em>
                </a>
                <em class="store-HO">或</em>
                <a href="javascript:;">
                    <em id="go">前往校级平台</em>
                </a>
            </span>
        </dl>
    </dl>
</div>
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

    function changeImg(){
        var imgSrc = $("#imgObj");
        var src = "verify/verifyCode.do";
        //var src =imgSrc.attr("src");
        imgSrc.attr("src",chgUrl(src));
    }
    //时间戳
    //为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳
    function chgUrl(url){
        var timestamp = (new Date()).valueOf();
        url = url.substring(0,20);
        if((url.indexOf("&")>=0)){
            url = url + "×tamp=" + timestamp;
        }else{
            url = url + "?timestamp=" + timestamp;
        }
        return url;
    }
</script>
  <script type='text/javascript' src='/static/js/k6kt-sso.js'></script>
</html>