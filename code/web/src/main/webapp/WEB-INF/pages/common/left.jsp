<%@ taglib prefix="cc" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
<cc:if test="${!fnn:endsWith(pageContext.request.requestURL,'/edit-news.jsp')}">
    <%--该js会与新闻编辑页面产生兼容性问题 因此需要判断引入--%>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
</cc:if>
<script src="/static/js/common/top5student.js"></script>
<script type="text/javascript">
    //$(function () {
    //        alert("run");
    //        $('.homepage-left-one').click(function (event) {
    //            alert("click");
    //
    //            if ($(this).children(".hocu").prop('className').indexOf('fa-chevron-left') > 0) {
    //                $(this).children(".hocu").addClass("fa-chevron-down").removeClass('fa-chevron-left');
    //                $(this).addClass("copy-one").removeClass("copy-two");
    //
    //            } else {
    //                $(this).children(".hocu").addClass("fa-chevron-left").removeClass('fa-chevron-down');
    //                $(this).addClass("copy-two").removeClass("copy-one");
    //            }
    //            $(this).next("div").toggle();
    //        });
    //});
    //    //$(function () {
    //        $(".homepage-left-one").click(function () {
    //
    //        });
    //    //});


    var recommendRandom = 2;
    function recommendFriend(page) {
        var pageChoose = 0;
        if (page == -1)
            pageChoose = recommendRandom;
        $.ajax({
            url: '/friendcircle/recommendedFriends.do',
            type: 'post',
            dataType: "json",
            data: {
                userId: '${wbUserInfo.id}',
                page: pageChoose,
                pageSize: 5
            },
            success: function (result) {
                var htm = '';
                result = result.rows;
                for (var i = 0; i < result.length; i++) {
                    var role = getRole(result[i].role);
                    htm += '<li><img class="fc_stu_img" target-id="' + result[i].id + '" role="' + result[i].role + '" ' +
                    'src="' + result[i].imgUrl + '" style="width:48px;height:48px;"><div class="mingzi" style="overflow: visible;"><a><span style="display: inline-block;-ms-word-break:break-all;word-break:break-all;' +
                    'width: 8em;color:#656565;font-weight: initial">' + result[i].userName + '  ' + role + '</span>' +
                    '<div class="center_left_ziti" style="overflow: visible;"><span>'
                    if (result[i].cityName != null) {
                        htm += result[i].cityName;
                    }
                    if (result[i].schoolName4WB != null) {
                        htm += '  ' + result[i].schoolName4WB;
                    }
                    htm += '</span><div><span>';
                    if (result[i].mainClassName != null) {
                        htm += result[i].mainClassName;
                    }
                    htm += '</span></div></div></a></div><div class="center_left_haoyou"> ' +
                    '<a href="javascript:addFriend(\'' + result[i].id + '\');"><span>+好友</span></a></div><div style="clear: both"></div></li>'
                }
                var sk = $("#center_left_li").find("ul").empty();
                $(sk).append(htm);
                recommendRandom += 1;
            }
        });
    }
    function getRole(role) {
        var STUDENT = 1//"学生")
        var TEACHER = 1 << 1// "老师"),
        var PARENT = 1 << 2//,"家长"),
        var HEADMASTER = 1 << 3//"校长"),
        var LEADER_CLASS = 1 << 4//,"班主任"),
        var K6KT_HELPER = 1 << 5//,"K6KT小助手"),
        var ADMIN = 1 << 6//,"管理员"),
        var LEADER_OF_GRADE = 1 << 7//"年级组长"),
        var LEADER_OF_SUBJECT = 1 << 8//,"学科组长"),
        var EDUCATION = 1 << 9//,"教育局"),
        var roles = "";
        if ((role & STUDENT) == STUDENT) {
            roles = "学生";
        } else if ((role & TEACHER) == TEACHER) {
            roles = "老师";
        } else if ((role & HEADMASTER) == HEADMASTER) {
            roles = "校领导";
        } else if ((role & EDUCATION) == EDUCATION) {
            roles = "教育局";
        } else {
            roles = "家长";
        }
        return roles;
    }
    function addFriend(friendId) {
        $.ajax({
            url: '/friendcircle/addFriendApply.do',
            type: 'post',
            dataType: "json",
            data: {

                userId: '${wbUserInfo.id}',
                friendId: friendId
            },
            success: function (result) {
                if (result) {
                    alert("好友申请已发出");
                    recommendFriend();
                } else {
                    alert("添加失败");
                }
            }
        });
    }
    function go2search() {
        var url = '/friendcircle/friendSearch.do';
        var keywords = $("#keywords").val();
        keywords = encodeURI(encodeURI(keywords));
        url += '?keyWords=' + keywords;
        window.location.href = url;
    }
    function go2href(url) {
        window.location.href = url;
    }
    $(function () {
//点击返回头部效果
        $(".homepage-upward").click(function () {
            $("html,body").animate({scrollTop: 0});
        });


        /*点击高亮代码*/

        var chatValue = jQuery("#chatValue").val();
        var showtypeValue = jQuery("#showtypeValue").val();
        var version = jQuery("#versionValue").val();
        var index_Value = jQuery("#index_Value").val();


        if (index_Value && version) {
            var v = index_Value;
            jQuery("#nav_left_" + v).css("backgroundColor", "#FF8A00");
            jQuery("#nav_left_" + v).css("background-image", "url('/img/JXHD.png')");
            jQuery("#nav_left_" + v).css("background-position", "211px 5px");
            jQuery("#nav_left_" + v).css("background-repeat", "no-repeat");
            jQuery("#nav_left_" + v).find("span").css("color", "white");
            jQuery("#nav_left_" + v).find("i").css("color", "white");
            jQuery("#nav_left_" + v + " + div.homepage-left-two").show();
            jQuery("#version_" + index_Value + "_" + version).css("color", "red");
        }
        else {
            // jQuery("#version"+version).css("color","red");
            jQuery("#nav_left_" + v).css("backgroundColor", "#FF8A00");
            jQuery("#nav_left_" + v).css("background-image", "url('/img/JXHD.png')");
            jQuery("#nav_left_" + v).css("background-position", "211px 5px");
            jQuery("#nav_left_" + v).css("background-repeat", "no-repeat");
        }


    });
</script>
<input type="hidden" id="versionValue" value="<%=request.getParameter("version") %>"/>
<input type="hidden" id="index_Value" value="<%=request.getParameter("index") %>"/>


<input type="hidden" id="chatValue" value="<%=request.getParameter("chat") %>"/>
<input type="hidden" id="showtypeValue" value="<%=request.getParameter("showtype") %>"/>


<link href="/static/css/homepage.css" type="text/css" rel="stylesheet">

<div class="homepage-left" style="z-index:1;overflow: visible">
    <!--=============================左边悬浮====================================-->
    <div class="homepage-momery" style="display:none;">
        <div class="homepage-momery-top" onclick="location.href = '/user?showtype=3'">
            <img src="/img/activity/homepage-training.png">
        </div>
        <div class="homepage-momery-top" onclick="location.href = '/business/reverse/user/userHelp.jsp'">
            <img src="/img/activity/homepage-user.png">
        </div>
        <div class="homepage-upward">
            <img src="/img/activity/homepage-arrow.png" title="回到顶部">
        </div>
    </div>

    <!--=========================上边部分===============================-->
    <div class="homepage-left-head">
        <img src="${sessionValue.midAvatar}">

        <div>${sessionValue.userName}</div>
        <div class="homepage-left-JY">
            <span class="homepage-left-JYZ">经验值${sessionValue.experience}</span>
        </div>
    </div>

    <!--=================尖角====================-->
    <!--.left-nav-->
    <%--<cc:choose>
     <cc:when test="${roles:isTeacher(sessionValue.userRole) && roles:isHeadmaster(sessionValue.userRole)}">
       <%@ include file="navs/teacher_headmaster_nav.jsp" %>
     </cc:when>
     <cc:when test="${roles:isTeacher(sessionValue.userRole) && roles:isManager(sessionValue.userRole)}">
       <%@ include file="navs/teacher_manager_nav.jsp" %>
     </cc:when>
     <cc:when test="${roles:isTeacher(sessionValue.userRole)}">
       <%@ include file="navs/teacher_nav.jsp" %>
     </cc:when>
     <cc:when test="${roles:isStudent(sessionValue.userRole)}">
       <%@ include file="navs/student_nav.jsp" %>
     </cc:when>
     <cc:when test="${roles:isParent(sessionValue.userRole)}">
       <%@ include file="navs/parent_nav.jsp" %>
     </cc:when>
     <cc:when test="${roles:isHeadmaster(sessionValue.userRole)}">
       <%@ include file="navs/headmaster_nav.jsp" %>
     </cc:when>
     <cc:when test="${roles:isManagerOnly(sessionValue.userRole)}">
       <%@ include file="navs/manager_nav.jsp" %>
     </cc:when>
     <cc:when test="${roles:isEducation(sessionValue.userRole)}">
       <%@ include file="navs/education_nav.jsp" %>
     </cc:when>
     
     <c:when test="${roles:isDoorKeeper(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/doorkeeper.jsp" ></jsp:include>
        </c:when>
         <c:when test="${roles:isDormManager(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/dormmanager.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isFunctionRoomManager(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/function_room_manager.jsp" ></jsp:include>
        </c:when>
        
        
        
     <cc:otherwise>
      
     </cc:otherwise>
    </cc:choose>--%>
    <cc:choose>
        <cc:when test="${roles:isTeacher(sessionValue.userRole) && roles:isHeadmaster(sessionValue.userRole)}">
            <jsp:include
                    page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/teacher_headmaster_nav.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isTeacher(sessionValue.userRole) && roles:isManager(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/teacher_manager_nav.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isTeacher(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/teacher_nav.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isStudent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/student_nav.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/parent_nav.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isHeadmaster(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/headmaster_nav.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isManagerOnly(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/manager_nav.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isEducation(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/navs/education_nav.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isDoorKeeper(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/doorkeeper.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isDormManager(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/dormmanager.jsp"></jsp:include>
        </cc:when>
        <cc:when test="${roles:isFunctionRoomManager(sessionValue.userRole)}">
            <jsp:include
                    page="/WEB-INF/pages/common/${sessionValue.schoolNavs}/function_room_manager.jsp"></jsp:include>
        </cc:when>


        <cc:otherwise>

        </cc:otherwise>
    </cc:choose>
    <!--/.left-nav-->


    <%--朋友圈--%>
    <div id="friendcircle" style="float: left;overflow: visible;display:none">
        <div id="center_left_info" style="overflow: visible;">
            <div id="center_left_info_I" style="overflow: visible;">
                <img onclick="go2href('/friendcircle/getFriendList.do?type=1')" src="/img/activity/friend-request.png">
                <span>${friendApplyCount}</span>
                <span style="margin-top: -4px">好友申请</span>
            </div>
            <div id="centent_left_info_M" style="overflow: visible;">
                <img onclick="go2href('/friendcircle/getFriendList.do?type=0')" src="/img/activity/myfriend.png">
                <span>我的好友</span>
            </div>
            <div id="center_left_info_C" style="overflow: visible;">
                <img onclick="go2href('/message')" src="/img/activity/events.png">
                <span>活动邀请</span>
            </div>
        </div>
        <div style="background-color:#F07474;width: 220px;height: 30px;font-size: 12px;">
            <div style="color: #ffffff;font-weight: bold;padding:5px 0 0 10px;">添加好友，去参加他/她发起的活动吧！</div>
        </div>
        <div id="center_left_info_IN">
            <input type="text" id="keywords" placeholder="搜索好友" onkeydown="if(event.keyCode==13){go2search();}">
            <img onclick="go2search()" src="/img/activity/fdjing.png">
        </div>

        <div class="center_left">
            <div id="center_left_top">
                <div class="info-card"></div>
                <span>推荐好友</span>
                <a href="javascript:recommendFriend(-1)"><span id="changefriend" class="change"
                                                               style="padding-left: 60px;">
                <img style="position: relative;top:4px;" src="/img/activity/xuanzhuang_2.png">换一批</span></a>
            </div>
            <div id="center_left_li">
                <%--<ul style="padding-left: 10px;"></ul>--%>
                <ul></ul>
            </div>
        </div>
        <div class="center_left" style="display: none">
            <div id="center_left_bottom">
                <span>热门活动</span>
                <a href="javascript:changeActivity()">
                <span id="changeActivity" class="change" style="position: relative;left: 70px;top:-10px;">
                    <img style="position: relative;top:4px;" src="/img/activity/xuanzhuang_2.png">换一批
                </span>
                </a>
            </div>
            <div id="center_left_bottom_li">
            </div>

        </div>
    </div>

    <div class="homepage-left-middle">
        <div id="divCalendar" style="padding-top:20px;"></div>
        <form id="calendarForm" action="/calendar/day" target="_blank">
            <input type="hidden" id="date" name="date">
        </form>
        <script>
            var currentRes;
            WdatePicker({
                eCont: 'divCalendar', skin: 'twoer', onpicked: function (dp) {
                    //jQuery("#date").val(dp.cal.getP('y')+"-"+dp.cal.getP('M'));
                    jQuery("#date").val(dp.cal.getNewDateStr());
                    jQuery("#calendarForm").submit();
                    showTag();
                }, ychanged: function (dp) {
                    currentRes = "";
                    load(dp.cal.getNewDateStr());
                },
                Mchanged: function (dp) {
                    currentRes = "";
                    load(dp.cal.getNewDateStr());
                }
            })

            function load(date) {
                $("iframe").css('width', 220)
                $("iframe").contents().find(".WotherDay").css("background", "");
                try {
                    $.ajax({
                        url: '/calendar/month/data1.do?date=' + date,
                        contentType: 'application/json',
                        success: function (res) {
                            currentRes = res;
                            showTag();
                        }
                    });
                } catch (x) {
                }
            }
            function showTag() {
                if (currentRes) {
                    var currentResArr = currentRes.split(",");
                    $("iframe").contents().find(".Wday,.Wwday,.Wselday").each(function () {
                        jQuery(this).css("background", "");
                        var tag = jQuery(this).text();
                        if (tag) {
                            if (jQuery.inArray(tag, currentResArr) >= 0) {
                                jQuery(this).css("background", "url('../images/triangle.png')no-repeat");
                                jQuery(this).css("background-position", " 100% 0");
                                jQuery(this).unbind("onmouseout");
                                jQuery(this).unbind("onmouseover");
                            }
                        }
                    });
                }
            }
            $(document).ready(function () {
                setTimeout(function () {
                    var d = new Date();
                    load(d.getFullYear() + "-" + (d.getMonth() + 1) + "-01");
                }, 2000);
            });
        </script>
    </div>
    <!--============================同学排行=======================================-->
    <c:choose>
        <c:when test="${!roles:isHeadmaster(sessionValue.userRole)||roles:isTeacher(sessionValue.userRole)}">
            <div class="homepage-left-bottom" style="display:none">
                <div class="homepage-left-bottom-top">
                    <span>同学排行</span>
                </div>
                <div class="homepage-left-bottom-text">
                    <ul id="version_student_sort">
                    </ul>
                </div>
            </div>
        </c:when>
    </c:choose>
</div>
<script type="text/javascript">
    try {
        $('.homepage-left-one').click(function (event) {
            if ($(this).children(".hocu") && $(this).children(".hocu").prop('className') && $(this).children(".hocu").prop('className').indexOf('fa-chevron-left') > 0) {
                $(this).children(".hocu").addClass("fa-chevron-down").removeClass('fa-chevron-left');
                $(this).addClass("copy-one").removeClass("copy-two");
            } else {
                $(this).children(".hocu").addClass("fa-chevron-left").removeClass('fa-chevron-down');
                $(this).addClass("copy-two").removeClass("copy-one");
            }
            $(this).next("div").toggle();
        });
    } catch (x) {

    }
</script>
