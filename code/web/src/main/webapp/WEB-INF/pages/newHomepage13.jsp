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
<html style="overflow-x: hidden">
<head>
    <title>首页</title>
    
    <link rel="stylesheet" href="../../customizedpage/newspage/css/newHomepage.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static/js/newhomepage.js"></script>
</head>
<body>
<%--<div style="width: 100%;height: 100%;position: absolute;z-index: -1">
    <img width="100%" height="100%" src="/img/newPage-1.png">
</div>--%>
<div class="newPage-bg">
    <div class="newPage-main">
        <dl>
            <dt>
                <img src="${sessionValue.schoolLogo}">
                <c:if test="${!roles:isStudentOrParent(sessionValue.userRole)}">
                <a class="teacher-D" href="javascript:;"></a>
                </c:if>
                <span class="newPage-top-right" style="float:right;">
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
                            <img src="${sessionValue.midAvatar}" width="140px;" height="140px" ><br>
                        <em>${sessionValue.userName}</em>
                    </a>
                    <a target="_blank" href="/petbag" class="newPage-left-bottom newPage-shadow">
                        <em>宠物</em>
                        <img id ="petimg" src="" width="75px;" height="75px;" alt="">
                        <div class="div-jyz">经验值：${sessionValue.experience}</div>
                        <%--<i class="lv-bg">lv.1111</i>--%>
                    </a>
                    <a target="_blank" href="http://www.fulaan.com" class="newPage-left-SC newPage-shadow">
                        <img id ="" src="../../customizedpage/newspage/img/newLogoo-12.png" width="250px;" height="123px;" alt="">
                        <img class="newHomepage-N" id ="" src="../../customizedpage/newspage/img/new.png"alt="">
                       <%-- <i class="SC-bg">11</i>--%>
                    </a>
                </div>
                <div class="newPage-right" style="width:800px;height:550px;">

                    <dl>

                        <dd>

                            <!--===================家校互动==========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pageview newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-4-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <a href="/user?version=1&index=2" class="pageview-IV newPage-right-pageview">
                                <img class="newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-4.png">
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
                                <!--================好友圈数量====================-->
                                <%--<span class="bg-yuan ">--%>
                                <%--<p class="bo_logo_I">11</p>--%>
                                <%--</span>--%>
                                <%-- <!--================微校园数量====================-->
                                 <div  class="bg-d-I bg-d">
                                     <!--================微校园字体====================-->
                                     <div class="bg-text text-I">
                                         <i class=""></i>

                                     </div>
                                 </div>
                                 <!--================微家园数量====================-->
                                 <div  class="bg-d-II bg-d">

                                     <!--================微家园字体====================-->
                                     <div class="bg-text text-II">
                                         <i class=""></i>微家园
                                     </div>
                                 </div>
                                 <!--================学校通知数量====================-->
                                 <div  class="bg-d-III bg-d">
                                     <!--================学校通知字体====================-->
                                     <div class="bg-text text-III">
                                         <i class=""></i>

                                     </div>
                                 </div>
                                 <!--================好友圈数量====================-->
                                 <div href="#" class="bg-d-IIII bg-d">

                                     <!--================好友圈字体====================-->
                                     <div class="bg-text text-V">
                                         <i class=""></i>

                                     </div>
                                 </div>--%>
                            </a>
                            <!--===================校园安全==========================-->
                           <%-- <a href="/schoolSecurity/schoolSecurityPage.do?version=30" class="pageview-I newPage-right-pageview">
                                <img src="../../customizedpage/newspage/img/newLogo-1.png">
                            </a>--%>

                            <!--===================微课@翻转课堂==========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pageview newPageHover">
                                <img class="w" src="../../customizedpage/newspage/img/newLogoo-5-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>

                            <c:choose>
                                <c:when test="${(roles:isTeacher(sessionValue.userRole))}">
                                    <a href="/homework/teacher.do?version=5&index=3&type=keqian" class="pageview-II newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-5.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                                    <a href="/homework/student.do?version=5&index=3&type=keqian" class="pageview-II newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-5.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) && roles:isTeacher(sessionValue.userRole)}">
                                    <a href="/testpaper/list.do?version=5&index=3" class="pageview-II newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-5.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole)}">
                                    <a href="/score/header.do?version=5&index=3" class="pageview-II newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-5.png">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:;" class="pageview-II newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-5-H.png">
                                    </a>

                                </c:otherwise>
                            </c:choose>
                         <%--   <!--===================资源中心==========================-->
                            <a href="/cloud/cloudLesson.do?version=61&type=1" class="pageview-II newPage-right-pageview">
                                <img src="../../customizedpage/newspage/img/newLogo-2.png">
                            </a>--%>
                            <!--=====================互动课堂=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-13-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <c:choose>
                                <c:when test="${(roles:isParent(sessionValue.userRole))}">
                                    <a href="javascript:;" class="newPage-right-pagevieww">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-13-H.png">
                                    </a>
                                </c:when>
                                <c:when test="${(roles:isTeacher(sessionValue.userRole))&&(roles:isHeadmaster(sessionValue.userRole))}">
                                    <a href="/interactLesson/lanclass.do?index=4&version=1" class="newPage-right-pagevieww">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-13.png">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/interactLesson/lanclass.do?index=4" class="newPage-right-pagevieww">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-13.png">
                                    </a>
                                </c:otherwise>
                            </c:choose>
                            <!--=====================办公OA=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-14-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                                    <a href="/docflow/documentList.do?type=0&index=5&version=1"  class="newPage-right-pagevieww">
                                        <img class="newpage-WK" src="../../customizedpage/newspage/img/newLogoo-14.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                                    <a href="/docflow/documentList.do?type=0&index=5&version=1"  class="newPage-right-pagevieww">
                                        <img class="newpage-WK" src="../../customizedpage/newspage/img/newLogoo-14.png">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:;"  class="newPage-right-pagevieww">
                                        <img class="newpage-WK" src="../../customizedpage/newspage/img/newLogoo-14-H.png">
                                    </a>

                                </c:otherwise>
                            </c:choose>
                            <!--=====================学生综合素质=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pageview newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-15-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                                    <a href="/growth/list.do?index=6" class="newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-15.png">
                                    </a>
                                </c:when>
                                </c:when>
                                <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                                    <a href="/growth/list.do?index=6" class="newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-15.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isStudent(sessionValue.userRole)}">
                                    <a href="/registration/list.do?index=6"  class="newPage-right-pageview">
                                        <img class="newpage-WK" src="../../customizedpage/newspage/img/newLogoo-15.png">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:;"  class="newPage-right-pageview">
                                        <img class="newpage-WK" src="../../customizedpage/newspage/img/newLogoo-15-H.png">
                                    </a>
                                </c:otherwise>
                            </c:choose>

                            <!--=====================3+3走班教学=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pageview newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-16-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                                    <a href="/zouban/baseConfig.do?index=7&version=1" class="newPage-right-pageview manageZbHref">
                                        <img class="newpage-WK" src="../../customizedpage/newspage/img/newLogoo-16.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                                    <a href="/zouban/teacher.do?index=7&version=4" class="newPage-right-pageview">
                                        <img class="newpage-WK" src="../../customizedpage/newspage/img/newLogoo-16.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isStudent(sessionValue.userRole)}">
                                    <a href="/zouban/student.do?index=7&version=4&tag=2" class="newPage-right-pageview">
                                        <img class="newpage-WK" src="../../customizedpage/newspage/img/newLogoo-16.png">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:void(0);" class="newPage-right-pageview">
                                        <img class="newpage-WK" src="../../customizedpage/newspage/img/newLogoo-16-H.png">
                                    </a>
                                </c:otherwise>
                            </c:choose>
                            <!--=====================微课超市=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-17-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <a href="/emarket?index=8&version=1" class="newPage-right-pagevieww">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-17.png">
                            </a>
                            <!--=====================云资源=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-18-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                                    <a href="/cloud/cloudLesson.do?index=9&version=1&type=1" class="newPage-right-pagevieww">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-18.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                                    <a href="/cloudres/load.do?index=9&version=3" class="newPage-right-pagevieww">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-18.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                                    <a href="/cloud/cloudLesson.do?index=9&version=1&type=1" class="newPage-right-pagevieww">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-18.png">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:;" class="pageview-IV newPage-right-pageview newPageHover" style="display: block!important;width: 100px;;">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-18-H.png">
                                        <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                                    </a>
                                </c:otherwise>

                            </c:choose>

                            <!--=====================兴趣拓展课=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pageview newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-19-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                                    <a href="/myclass/tointerestclass?index=10" class="newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-19.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                                    <a href="/myclass/tointerestclass?index=10" class="newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-19.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                                    <a href="/myclass/toselinclass?index=10" class="newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-19.png">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:;" class="newPage-right-pageview">
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-19-H.png">
                                    </a>
                                </c:otherwise>

                            </c:choose>

                            <!--=====================火热活动=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pageview newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-20-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <a href="/business/fieryactivitylist.do?index=11" class="newPage-right-pageview">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-20.png">
                            </a>
                            <!--=====================群组交流=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-21-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <a href="/chat?index=12" class="newPage-right-pagevieww">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-21.png">
                            </a>
                            <!--=====================阅卷系统=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover" style="display: block">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-22-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <a href="javascript:;" class="newPage-right-pagevieww" style="display: none">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-22-H.png">
                            </a>
                            <!--=====================校园安全监控=========================-->
                            <%--只为复兰大学显示--%>
                            <a href="javascript:;" target="_blank" class="pageview-IV newPage-right-pagevieww newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-23-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <a href="http://139.196.198.137:8081/videoPage3" target="_blank" class="newPage-right-pagevieww">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-23.png">
                            </a>
                            <%--<a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover" style="display: block">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-23-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <a href="/schoolSecurity/schoolSecurityPage.do?index=14" class="newPage-right-pagevieww" style="display: none">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-23.png">
                            </a>--%>
                            <!--=====================直播点播=========================-->
                           <!--  <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover" style="display: block">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-24-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a> -->
                            <a href="http://live.k6kt.com" target="_blank" class="newPage-right-pagevieww" style="display:block ">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-24.png">
                            </a>
                            <!--=====================一卡通=========================-->
                            <a href="/smartCard/myKaoQin.do?&index=20&version=1" class="newPage-right-pagevieww" style="display: block">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-25.png">
                            </a>
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover" style="display: none">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-25-H.png">
                                <%--<img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">--%>
                            </a>
                            <!--=====================教师评价系统=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-26.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>

                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isTeacher(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                                    <a href="/teacherevaluation.do?index=19" class="pageview-IV newPage-right-pagevieww" style="display: block">
                                        <img class="" src="../../customizedpage/newspage/img/newLogoo-26.png">
                                            <%--<img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">--%>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover" style="display: block">
                                        <img class="" src="../../customizedpage/newspage/img/newLogoo-26-H.png">
                                            <%--<img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">--%>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                            <!--=====================平台管理=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-27-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <c:choose>

                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) ||
                                (roles:isTeacher(sessionValue.userRole) && roles:isManager(sessionValue.userRole))}">
                                    <a href="/myschool/managesubject?index=18&version=1&tag=1" class="pageview-VIIII newPage-right-pagevieww" >
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-27.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                                    <a href="/manageCount/schooltotal.do?index=18&version=1&schoolid=${sessionValue.schoolId}" class="pageview-VIIII newPage-right-pagevieww" >
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-27.png">
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:;" class="pageview-VIIII newPage-right-pagevieww" >
                                        <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-27-H.png">
                                    </a>
                                </c:otherwise>
                            </c:choose>
                            <!--=====================APP下载=========================-->
                            <a href="javascript:;" class="pageview-IV newPage-right-pagevieww newPageHover">
                                <img class="" src="../../customizedpage/newspage/img/newLogoo-28-H.png">
                                <img class="newPageHover-img" src="../../customizedpage/newspage/img/newPage-phone.png">
                            </a>
                            <a href="/mobile" class="newPage-right-pagevieww">
                                <img class="newpage-WK newPage-shadow" src="../../customizedpage/newspage/img/newLogoo-28.png">
                            </a>
                            <%--<!--===================学校管理==========================-->
                            <c:choose>
                                <c:when test="${roles:isHeadmaster(sessionValue.userRole) ||
                                (roles:isTeacher(sessionValue.userRole) )}">
                                    <a href="/docflow/documentList.do?type=0&version=51" class="pageview-V newPage-right-pageview">

                                        <img src="../../customizedpage/newspage/img/newLogo-6.png">
                                    </a>
                                </c:when>
                                <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                                    <a href="/student/class?version=52&tag=1" class="pageview-V newPage-right-pageview">

                                        <img src="../../customizedpage/newspage/img/newLogo-6.png">
                                    </a>
                                </c:when>

                                <c:otherwise>
                                    <a href="/myschool/mycoll?version=51" class="pageview-VI newPage-right-pageview" >
                                        <img src="../../customizedpage/newspage/img/newLogo-6.png">
                                    </a>

                                </c:otherwise>
                            </c:choose>--%>

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
<div id="footer" style="font-size: 12px;  clear: both;  overflow: hidden;width: inherit;background-color: #cccccc;opacity: .7;filter: alpha(opacity=70);height: 40px;min-width: 1215px;color:#454545;line-height: 40px;margin-top: 10px;">
    <div style="width: 1000px;margin: 0px auto;">
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
                if(data!=null){if(data.schoolcount>99){
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
