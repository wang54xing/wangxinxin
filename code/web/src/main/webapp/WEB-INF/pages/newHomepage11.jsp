<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-17
  Time: 下午2:59
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@page import="com.pojo.app.SessionValue"%>
<html>
<head>
    <title>首页</title>
    <link rel="stylesheet" href="../../customizedpage/newspage/css/newHomepage.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static/js/newhomepage.js"></script>
</head>
<body>

<div class="newPage-bg">
    <div class="newPage-main">
        <dl>
            <dt>

                <c:if test="${ not empty sessionValue.schoolLogo }">
                    <img src="${sessionValue.schoolLogo}">
                </c:if>

                <c:if test="${ not empty bindSchoolName }">
                    <label class="newPage-LABEL">${bindSchoolName}</label>
                </c:if>

                <c:if test="${!roles:isStudentOrParent(sessionValue.userRole)}">
                    <a class="teacher-D" href="javascript:;"></a>
                </c:if>
                
                
                
                <span class="newPage-top-right">
                    <a href="/message"><em class="">
                        私信
                        <div class="bg-yuan sixin" style="display:none;"><i class="bg-ss" id="lettercount" ></i></div>
                    </em></a>
                    <a  href="/basic"><em>个人中心</em></a>
                    <i>欢迎${sessionValue.userName}的到来！</i>
                    <span id="fz_out" style="cursor: pointer;font-size:small; " onclick="loginout();"> [退出]</span>
                </span>
            </dt>

            <!--========================链接部分=======================-->
            <dd>
                <div class="newPage-left">
                    <a  href="/basic" class="newPage-left-top curson-c newPage-shadow">
                        <img src="${sessionValue.midAvatar}" width="140px;" height="140px" /><br>
                        <em>${sessionValue.userName}</em>
                        <em>经验值${sessionValue.experience}</em>
                    </a>
                    <a target="_blank" href="/petbag" class="newPage-left-bottom newPage   -shadow">
                        <em>宠物</em>
                        <img id ="petimg" src="" width="75px;" height="75px;" alt="">
                        <%--<i class="lv-bg">lv.1111</i>--%>
                    </a>
                    <%--  <a target="_blank" href="/mall" class="newPage-left-SC newPage-shadow">
                          <img id ="" src="../../customizedpage/newspage/img/newLogoo-12.png" width="250px;" height="123px;" alt="">
                          <img class="newHomepage-N" id ="" src="../../customizedpage/newspage/img/new.png"alt="">
                         &lt;%&ndash; <i class="SC-bg">11</i>&ndash;%&gt;
                      </a>--%>
                </div>
                <div class="newPage-right">
                    <dl>
                        <dd>
                            <!--===================家校互动==========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pageview newPageHover">
                                <img class="" src="/customizedpage/newspage/img/newLogoo-4-H.png">
                            </a>
                            <a href="/user?version=1&index=2" class="pageview-IV newPage-right-pageview">
                                <img class="newPage-shadow" src="/customizedpage/newspage/img/newLogoo-4.png">
                                <!--================微校园数量====================-->
                                <span class="bg-yuan bo-l" >
                                    <p class="bo_logo_I" id="wxycount"></p>
                                </span>
                                <!--================微家园数量====================-->
                                 <span class="bg-yuan bo-ll" >
                                    <p class="bo_logo_I" id="wjycount"></p>
                                </span>
                                <!--================学校通知数量====================-->
                                 <span class="bg-yuan bo-lll" style="display: none">
                                    <p class="bo_logo_I" id="noticecount"></p>
                                </span>
                            </a>

                            <!--===================微课@翻转课堂==========================-->
                            <c:if test="${roles:isStudentOrParent(sessionValue.userRole)}">
                            <a  href="/homework/student.do?version=5&index=3&type=keqian"class="newPage-right-pageview">
                                <img class="w" src="/customizedpage/newspage/img/new-logoo-101.jpg">
                            </a>
                            </c:if>
                            <c:if test="${roles:isTeacher(sessionValue.userRole)}">
                                <a  href="/homework/teacher.do?version=5&index=3&type=keqian"class="newPage-right-pageview">
                                    <img class="w" src="/customizedpage/newspage/img/new-logoo-101.jpg">
                                </a>
                            </c:if>

                            <!--=====================火热活动=========================-->
                            <a href="/business/fieryactivitylist.do?index=11" class="newPage-right-pageview">
                                <img class="newpage-WK newPage-shadow" src="/customizedpage/newspage/img/new-logoo-100.jpg">
                            </a>


                            <!--=====================平台管理=========================-->
                            

              <%--              <a href="/myschool/managesubject?index=18&version=1&tag=1" class="newPage-right-pageview" >
                                <img class="" src="/customizedpage/newspage/img/new-logoo-102.jpg">
                            </a>

                            
                            
                            
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover" style="width: 255px;">
                                <img class="" src="/customizedpage/newspage/img/newLogoo-103-H.jpg">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>--%>
                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) ||
                                (roles:isTeacher(sessionValue.userRole) && roles:isManager(sessionValue.userRole))}">
                                    <a href="/myschool/managesubject?index=18&version=1&tag=1" class="pageview-VIIII newPage-right-pagevieww"  style="width: 255px;">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/new-logoo-102.jpg">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                                    <a href="/manageCount/schooltotal.do?index=18&version=1&schoolid=${sessionValue.schoolId}" class="pageview-VIIII newPage-right-pagevieww"  style="width: 255px;">
                                        <img class="newpage-WK newPage-shadow" src="/customizedpage/newspage/img/new-logoo-102.jpg">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover"  style="width: 255px;display: inline-block">
                                        <img class="newpage-WK newPage-shadow" src="/customizedpage/newspage/img/newLogoo-103-H.png">
                                        <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                                    </a>
                                </c:otherwise>
                            </c:choose>
                            
                            
                            
                            
                            
                            <!--=====================云资源=========================-->
                            <a href="/cloud/cloudLesson.do?index=9&version=1&type=1" class="newPage-right-pageview">
                                <img class="" src="/customizedpage/newspage/img/new-logoo-103.jpg">
                            </a>
                            <!--=====================APP下载=========================-->
                            <a href="/mobile" class="newPage-right-pagevieww">
                                <img class="newpage-WK newPage-shadow" src="/customizedpage/newspage/img/new-logoo-104.jpg">
                            </a>

                        </dd>
                    </dl>
                    <div class="newPage-FL">
                        <!--微校园-->
                        <a href="/user?version=1&index=2" class="newPage-FL-A newPage-AA">
                            <div class="bg-text text-I">
                                <i class=""></i>
                                微校园
                            </div>
                        </a>
                        <!--微家园-->
                        <a href="/user?version=2&index=2" class="newPage-FL-B newPage-AA">
                            <div class="bg-text text-II">
                                <i class=""></i>
                                微家园
                            </div>
                        </a>
                        <!--学校通知-->
                        <a href="/notice/index.do?version=3&index=2&page=1" class="newPage-FL-C newPage-AA">
                            <div class="bg-text text-III">
                                <i class="">

                                </i>
                                学校通知
                            </div>
                        </a>
                        <!--好友圈-->
                        <a href="/activity/activityMain?version=4&index=2" class="newPage-FL-D newPage-AA">
                            <div class="bg-text text-V">
                                <i class=""></i>
                                好友圈
                            </div>
                        </a>
                    </div>
                </div>
            </dd>
        </dl>
    </div>
</div>
<div id="footer" style="font-size: 12px;  clear: both;  overflow: hidden;width: inherit;background-color: #cccccc;opacity: .7;filter: alpha(opacity=70);height: 40px;min-width: 1215px;color:#454545;line-height: 40px;">
    <div style="width: 1000px;margin: 0 auto;">
        <span style="float: left;">版权所有：上海复兰信息科技有限公司          <a style="color: #454545" href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
        <span style="float: right">
            <a style="color: #454545" href="/aboutus/k6kt">关于我们</a>  |
            <a style="color: #454545" href="/contactus/k6kt">联系我们</a>   |
            <a style="color: #454545" href="/service/k6kt">服务条款 </a> |
            <a style="color: #454545" href="/privacy/k6kt">隐私保护 </a> |
            <a style="color: #454545" href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank">
                <img style="vertical-align: middle" src="/img/QQService.png">
            </a>
        </span>
    </div>
</div>
<!--============================教师节活动弹出框start============================-->
<div class="bg"></div>
<div class="teacher-main">
    <div class="teacher-info">
        <img src="/customizedpage/newspage/img/teacher-popup.png">
        <!--关闭按钮-->
        <em class="teacher-info-em"></em>
        <!--免费领取按钮-->
        <em class="teacher-info-cl"></em>
    </div>
    <div class="teacher-bottom">
        <img class="teacher-ph" src="/customizedpage/newspage/img/teacher-ph.png">
        <input class="teacher-inp" placeholder="输入手机号">
        <div class="teacher-yz">
            <input placeholder="输入验证码" id="vific">
            <span class="teacher-hq">获取验证码</span>
        </div>
        <em class="teacher-go">GO</em>
    </div>
</div>
<!--============================教师节活动弹出框end============================-->
<!--============================学生弹出框start===============================-->
<div class="student-main" style="display: none;">
    <div>
        <img src="/customizedpage/newspage/img/teacher-popupT.png" width="80%">
        <!--论坛君-->
        <a class="student-main-I"></a>
        <!--大赛君-->
        <a class="student-main-II"></a>
        <!--www.fulaan.com-->
        <a class="student-main-III"></a>
        <!--关闭按钮-->
        <a class="student-main-IV"></a>
    </div>
</div>
<!--============================学生弹出框end===============================-->
</body>
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
    function getPetImage() {
        $.ajax({
            url: "/pet/selectedPet.do",
            type: "post",
            success: function (data) {
                if(data.petInfo!=null){
                    $("#petimg").attr("src",data.petInfo.petimage)
                }else{
                    $("#petimg").attr("src","/img/egg.png");
                }
            }
        });
    }

    function getMicroblogcount() {
        $.ajax({
            url: "/homeschool/getMicoblog.do",
            type: "get",
            success: function (data) {
                if(data!=null){
                    if(data.schoolcount>99){
                        $("#wxycount").html("99+");
                    }else {
                        $("#wxycount").html(data.schoolcount);
                    }
                    if(data.homecount>99){
                        $("#wjycount").html("99+");
                    }else {
                        $("#wjycount").html(data.homecount);
                    }
                }else{

                }
            }
        });
    }

    function getNoticecount() {
        $.ajax({
            url: "/notice/unread/count.do",
            type: "get",
            success: function (data) {
                if(data!=null){
                    if(data.message>99) {
                        $("#noticecount").html("99+");
                    }
                    else
                    {
                        $("#noticecount").html(data.message);
                    }
                }else{

                }
            }
        });
    }
    function getLettercount() {
        $.ajax({
            url: "/letter/count.do",
            type: "get",
            success: function (data) {
                if(data!=null){
                    if(data>99) {
                        $("#lettercount").html("99+");
                    }
                    else
                    {
                        $("#lettercount").html(data);
                    }
                }else{

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


    getPetImage();
    getMicroblogcount();
    //getNoticecount();
    //getLettercount();
</script>
</html>
