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
    <style rel="stylesheet">
        @font-face {
            font-family: font;
            src: url('/font/font.ttf');
        }

    </style>
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
            <div class="store-main-right-bg" id="news">
                <em></em>
                <div>
                    <ul>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-WYY.png" class="store-right-TT" width="45px" height="45px">
                                    <div>
                                        <em class="store-people store-P">王晔阳25</em>
                                        <em class="store-DD store-P">学生</em>
                                    </div>
                                </dt>
                                <dt>
                                    <p>
                                        今天我去了上海自然博物馆，那儿的东西都令我感觉新奇，有许多没见到过的史前生物，我还看了两部四维电影，《宇宙大爆炸》和《逃出白垩纪》，让人身临其境！
                                    </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-29.png"><img src="/img/K6KT/main-page/store-T-30.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-WHT.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">王菡童25</em>
                                    <em class="store-DD store-P">学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                    <%--<i class="store-P">#温暖圣诞#</i>--%>
                                        寒假，我帮爸爸妈妈晾衣服、收衣服、叠衣服。
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-31.png"><img src="/img/K6KT/main-page/store-T-32.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-JZR.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">井悦融</em>
                                    <em class="store-DD store-P">学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p> 我的新年愿望是祖国更加富强、民主。 </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-33.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-WXY.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">王欣盈</em>
                                    <em class="store-DD store-P">学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p> 今天我和妈妈外婆去了自然博物馆，我终于看到了会动会咆哮的大恐龙模型，还有很多恐龙化石，各类动物的标本。通过介绍宣传片我了解了人类进化的大致过程。
                                经过自然博物馆的一日游，我学到了很多以前不了解的知识，我喜欢探索大自然的奥秘，很开心！ </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-34.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-YEC.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">俞儿畅14</em>
                                    <em class="store-DD store-P">学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>我描绘的中国梦，是未来世界自由之梦！</p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-35.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-HCM.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">胡晟铭15</em>
                                    <em class="store-DD store-P">学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>我的新年“红包”是每天为家人洗一次碗，别看洗碗容易，要坚持每天做到可是一件难事，有好几次我要放弃了，最后还是坚持下来，开学了我可能没有时间给家里洗碗，但我还是会做些力所能及的家务，比如倒垃圾...哈哈</dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-36.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-YYD.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">叶予多27</em>
                                    <em class="store-DD store-P">学生</em>
                                </div>
                                </dt>
                                <dt>
                                    <p>
                                     敲敲背，亲亲脸，陪着外公过生日！这是我给外公的“红包”。
                                    </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-37.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-FT.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">丰韬4</em>
                                    <em class="store-DD store-P">学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>今年过年，我带着自己用心写的春联回到了湖南老家，爷爷开心的将春联贴在了自家的大门上，我还在菜地里帮奶奶摘小野葱，奶奶说这种小野葱炒土鸡蛋好吃又营养。
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-38.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-DJ.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">董俊</em>
                                    <em class="store-DD store-P">阜阳三中&nbsp;&nbsp;老师</em>
                                </div>
                                </dt>
                                <dt>
                                <p>光鲜亮丽
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-12.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-PZL.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">蒲子岭</em>
                                    <em class="store-DD store-P">阜阳三中&nbsp;&nbsp;老师</em>
                                </div>
                                </dt>
                                <dt>
                                <p>大三中的桂花树……
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-13.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-WS.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">汪帅1</em>
                                    <em class="store-DD store-P">阜阳三中&nbsp;&nbsp;老师</em>
                                </div>
                                </dt>
                                <dt>
                                <p>三中红地毯
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-14.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-WZA.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">王澳哲</em>
                                    <em class="store-DD store-P">淮南师范附属小学&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>我不仅参加学校的运动会，还具体负责了一项学校工作呢，妈妈给我说我是：礼仪先生，哈哈……我喜欢!
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-15.png"> <img src="/img/K6KT/main-page/store-T-16.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-ZJQ.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">周佳琪1</em>
                                    <em class="store-DD store-P">淮南师范附属小学&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>参观安徽省地质博物馆
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-17.png"> <img src="/img/K6KT/main-page/store-T-18.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-LSS.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">龙双双</em>
                                    <em class="store-DD store-P">六安寿县安丰高级中学&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                <i class="store-p">#城市秋色#</i>
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-19.png"> <img src="/img/K6KT/main-page/store-T-20.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-CPH.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">陈鹏翰</em>
                                    <em class="store-DD store-P">蚌埠第二实验小学&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                    <i class="store-p">#温暖圣诞#</i>
                                我的圣诞大餐
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-21.png"> <img src="/img/K6KT/main-page/store-T-22.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-ZWJ.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">张文静37</em>
                                    <em class="store-DD store-P">马鞍山二十一中&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                    <i class="store-p">#温暖圣诞#</i>
                                最是浪漫圣诞雪，丝丝凉意都快乐。
                                祝大家圣诞快乐。
                                </p>
                                </dt>
                                <dd>
                                 <%--   <img src="/img/K6KT/main-page/store-T-21.png"> <img src="/img/K6KT/main-page/store-T-22.png">--%>
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-LZ.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">李征2</em>
                                    <em class="store-DD store-P">马鞍山二十一中&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                老师，您是辛勤的园丁，培育了我们这些种子，使我们生根发芽，茁壮成长！您是未来的建筑师，一砖一瓦的辛勤工作！您是点燃的蜡烛，照亮着我们，却唯独没有照亮自己。今天，我们用真诚的祝福感谢您！老师，您辛苦了！祝老师桃李满天下！
                                </p>
                                </dt>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-LYY.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">刘玥盈</em>
                                    <em class="store-DD store-P">安庆华中路第二小学&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                1可以先做一些试卷。
                                2可以家长出几道有关语文、数学和英语的难题。
                                3认真复习。
                                4可以让家长出几道口算题做做。
                                5看看有益的广告，能帮助你的品德，也能帮助你的行为和良好习惯！
                                提示：【请不要作弊哦！】
                                </p>
                                </dt>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-LXJ.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">李晓军tzzx</em>
                                    <em class="store-DD store-P">唐镇中学&nbsp;&nbsp;老师</em>
                                </div>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-23.png"> <img src="/img/K6KT/main-page/store-T-24.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-ZY.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">周一tzzx</em>
                                    <em class="store-DD store-P">唐镇中学&nbsp;&nbsp;老师</em>
                                </div>
                                </dt>
                                <dt>
                                    <p>
                                        六一，快乐的大家庭！
                                    </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-25.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-ZQ.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">张倩tzzx</em>
                                    <em class="store-DD store-P">唐镇中学&nbsp;&nbsp;老师</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                初三3中队班级海报
                            </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-26.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-DYR.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">丁怡然1</em>
                                    <em class="store-DD store-P">来安县实验小学&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                圣诞的雪花，轻舞飞扬。圣诞的花树，诗意芬芳。圣诞的天空，写满吉祥。圣诞的钟声，不同凡响。圣诞的祝福，喜气洋洋。祝你圣诞快乐，平安吉祥！
                                </p>
                                </dt>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-ZY%20(2).png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">朱越7</em>
                                    <em class="store-DD store-P">淮南第二中学&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                    <i class="store-P">#温暖圣诞#</i>
                                    圣诞节到了，开心！
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-27.png">
                                </dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>
                                    <img src="/img/K6KT/main-page/store-YYQ.png" class="store-right-TT" width="45px" height="45px">
                                <div>
                                    <em class="store-people store-P">叶沅琦</em>
                                    <em class="store-DD store-P">广州市东风东路小学&nbsp;&nbsp;学生</em>
                                </div>
                                </dt>
                                <dt>
                                <p>
                                《世界著名连环漫画:父与子全集(彩色珍藏版)》讲述了父与子是德国幽默大师埃·奥·卜劳恩的作品，创作于1934—1937年，现已风靡世界。父与子的情节简单，在漫画中，父子的情感被刻画的淋漓尽致。《世界著名连环漫画:父与子全集(彩色珍藏版)》在原著的基础上进行中英文标注，帮助小读者以及父母朋友们更形象地了解内容。是亲子互动的最佳读本。
                                </p>
                                </dt>
                                <dd>
                                    <img src="/img/K6KT/main-page/store-T-28.png">
                                </dd>
                            </dl>
                        </li>
                    </ul>
                </div>
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