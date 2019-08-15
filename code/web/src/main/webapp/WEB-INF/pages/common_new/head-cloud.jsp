<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/18
  Time: 下午4:58
  To change this template use File | Settings | File Templates.
--%>
<%--<script type="text/javascript" src="../../../static/js/jquery-1.11.1.min.js"></script>--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.pojo.app.SessionValue" %>
<%--<%
    SessionValue sv =  new BaseController().getSessionValue();
    String schoolName = sv.getSchoolName();
%>--%>
<script type="text/javascript">
    $(function(){
        $(".head-nav>a").click(function(){
            $(this).addClass("cur-a").siblings().removeClass("cur-a");
        });
        $('.head-nav span:first-child').css('border-left','none');
        $('.head-span a:first-child').css('cursor','default');
    });
</script>
<link href="../../../static_new/css/cloudreset.css" rel="stylesheet">
<link href="../../../static_new/css/reset.css" rel="stylesheet">
<div id="head" class="head" style="background: #fff;height: 192px;">
    <!--#subhead-->
    <!--=================================引入子头部============================================-->
    <%@ include file="subhead-cloud.jsp" %>
    <div class="head-but" style="margin-top: 10px;">
        <div class="head-nav"><%--
            <span class="head-span">
    <c:choose>
        <c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isHeadmaster(sessionValue.userRole)}">
            <a href="/microlesson/micropage.do?version=1b&a=10000" target="_blank">微课大赛</a>
            <a href="/score/teacher.do?version=17&a=10000" target="_blank">成绩分析</a>
            <a href="/registration/list.do?version=5g&a=10000" target="_blank">成长档案</a>
            <a href="/docflow/documentList.do?type=0&version=51&a=10000" target="_blank">公文流转</a>
            <a href="/manageCount/schooltotal.do?version=88&a=10000&schoolid=${sessionValue.schoolId}" target="_blank">管理统计</a>
        </c:when>
        <c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isManager(sessionValue.userRole)}">
            <a href="/microlesson/micropage.do?version=1b&a=10000" target="_blank">微课大赛</a>
            <a href="/score/teacher.do?version=17&a=10000" target="_blank">成绩分析</a>
            <a href="/registration/list.do?version=5g&a=10000" target="_blank">成长档案</a>
            <a href="/docflow/documentList.do?type=0&version=51&a=10000" target="_blank">公文流转</a>
            <a href="/manageCount/schooltotal.do?version=88&a=10000&schoolid=${sessionValue.schoolId}" target="_blank">管理统计</a>
        </c:when>
        <c:when test="${roles:isTeacher(sessionValue.userRole)}">
            <a href="/microlesson/micropage.do?version=1b&a=10000" target="_blank">微课大赛</a>
            <a href="/score/teacher.do?version=17&a=10000" target="_blank">成绩分析</a>
            <a href="/registration/list.do?version=5g&a=10000" target="_blank">成长档案</a>
            <a href="/docflow/documentList.do?type=0&version=51&a=10000" target="_blank">公文流转</a>
            <a href="/manageCount/schooltotal.do?version=88&a=10000&schoolid=${sessionValue.schoolId}" target="_blank">管理统计</a>
        </c:when>
        <c:when test="${roles:isStudent(sessionValue.userRole)}">
            <a href="#">微课大赛</a>
            <a href="/score/student.do?version=17&a=10000" target="_blank">成绩分析</a>
            <a href="/registration/list.do?version=53&tag=1&a=10000" target="_blank">成长档案</a>
            <a href="#">公文流转</a>
            <a href="#">管理统计</a>
        </c:when>
        <c:when test="${roles:isParent(sessionValue.userRole)}">
            <a href="#">微课大赛</a>
            <a href="/score/student.do?version=17&a=10000" target="_blank">成绩分析</a>
            <a href="#">成长档案</a>
            <a href="#">公文流转</a>
            <a href="#">管理统计</a>
        </c:when>
        <c:when test="${roles:isHeadmaster(sessionValue.userRole)}">
            <a href="/microlesson/micropage.do?version=1b&a=10000" target="_blank">微课大赛</a>
            <a href="#">成绩分析</a>
            <a href="/registration/list.do?version=5g&a=10000" target="_blank">成长档案</a>
            <a href="/docflow/documentList.do?type=0&version=51&a=10000" target="_blank">公文流转</a>
            <a href="/manageCount/schooltotal.do?version=88&a=10000&schoolid=${sessionValue.schoolId}" target="_blank">管理统计</a>
        </c:when>
        <c:when test="${roles:isManagerOnly(sessionValue.userRole)}">
            <a href="#">微课大赛</a>
            <a href="#">成绩分析</a>
            <a href="#">成长档案</a>
            <a href="/docflow/documentList.do?type=0&version=51&a=10000" target="_blank">公文流转</a>
            <a href="#">管理统计</a>
        </c:when>
        <c:when test="${roles:isEducation(sessionValue.userRole)}">
            <a href="/microlesson/micropage.do?version=1b&a=10000" target="_blank">微课大赛</a>
            <a href="/score/educationBureau.do?version=17&a=10000" target="_blank">成绩分析</a>
            <a href="/registration/list.do?version=8a&tag=1&a=10000" target="_blank">成长档案</a>
            <a href="/docflow/documentList.do?type=0&version=51&a=10000" target="_blank">公文流转</a>
            <a href="/manageCount/countMain.do?version=90&tag=1&a=10000" target="_blank">管理统计</a>
        </c:when>
        <c:otherwise>
            <a href="#">微课大赛</a>
            <a href="#">成绩分析</a>
            <a href="#">成长档案</a>
            <a href="#">公文流转</a>
            <a href="#">管理统计</a>
        </c:otherwise>
    </c:choose>
            </span>
            <c:if test="${sessionValue.schoolName=='复兰大学'}">
            <span class="head-span">
                <a href="http://yun.k6kt.com/videoconference/list.do" target="_blank">视频会议</a>
                <a href="http://yun.k6kt.com/interactLesson/lanclass.do" target="_blank">互动课堂</a>
                &lt;%&ndash;<a href="${sessionValue.cloudUrl}videoconference/list.do">视频会议</a>
                <a href="${sessionValue.cloudUrl}interactLesson/lanclass.do">互动课堂</a>&ndash;%&gt;
                <a href="http://yun.k6kt.com/onlineClass/gotoFristPage.do" target="_blank" style="width: 81px;">远程同步课堂</a>
                <a href="http://www.fulaan.com/courses" target="_blank" style="width: 44px;">精品课</a>
                <a href="http://yun.k6kt.com/onlineCourses/list.do" target="_blank">网络授课</a>
            </span>
            </c:if>
            <span class="head-span span-b">
                <c:choose>
                    <c:when test="${fn.contains(sessionValue.cloudUrl,'midong')}">
                        <a href="http://midong.k6kt.com/videoAir/index.do?type=1" target="_blank">操场直播</a>
                        <a href="http://midong.k6kt.com/videoAir/index.do?type=2" target="_blank">课堂直播</a>
                        <a href="http://midong.k6kt.com/videoAir/index.do?type=3" target="_blank">活动直播</a>
                        <a href="http://midong.k6kt.com/videoAir/index.do?type=4" target="_blank">安全直播</a>
                        <a href="http://midong.k6kt.com/videoAir/index.do?type=5" target="_blank">会议直播</a>
                    </c:when>
                    <c:otherwise>
                        <a href="http://yun.k6kt.com/videoAir/index.do?type=1" target="_blank">操场直播</a>
                        <a href="http://yun.k6kt.com/videoAir/index.do?type=2" target="_blank">课堂直播</a>
                        <a href="http://yun.k6kt.com/videoAir/index.do?type=3" target="_blank">活动直播</a>
                        <a href="http://yun.k6kt.com/videoAir/index.do?type=4" target="_blank">安全直播</a>
                        <a href="http://yun.k6kt.com/videoAir/index.do?type=5" target="_blank">会议直播</a>
                    </c:otherwise>
                </c:choose>
            </span>
            <span class="head-span">
                <c:choose>
                    <c:when test="${fn.contains(sessionValue.cloudUrl,'midong')}">
                        <a href="http://midong.k6kt.com/school/gotoschoollist.do" target="_blank">学校空间</a>
                        <a href="http://midong.k6kt.com/school/gotoclasslist.do" target="_blank">班级空间</a>
                        <a href="http://midong.k6kt.com/school/gotoparentlist.do" target="_blank">家长空间</a>
                        <a href="http://midong.k6kt.com/school/gototeacherlist.do" target="_blank">老师空间</a>
                        <a href="http://midong.k6kt.com/school/gotostudentlist.do" target="_blank">学生空间</a>
                    </c:when>
                    <c:otherwise>
                        <a href="http://yun.k6kt.com/school/gotoschoollist.do" target="_blank">学校空间</a>
                        <a href="http://yun.k6kt.com/school/gotoclasslist.do" target="_blank">班级空间</a>
                        <a href="http://yun.k6kt.com/school/gotoparentlist.do" target="_blank">家长空间</a>
                        <a href="http://yun.k6kt.com/school/gototeacherlist.do" target="_blank">老师空间</a>
                        <a href="http://yun.k6kt.com/school/gotostudentlist.do" target="_blank">学生空间</a>
                    </c:otherwise>
                </c:choose>
                &lt;%&ndash;<a href="${sessionValue.cloudUrl}school/gotoschoollist.do">学校空间</a>
                <a href="${sessionValue.cloudUrl}school/gotoclasslist.do">班级空间</a>
                <a href="${sessionValue.cloudUrl}school/gotoparentlist.do">家长空间</a>
                <a href="${sessionValue.cloudUrl}school/gototeacherlist.do">老师空间</a>
                <a href="${sessionValue.cloudUrl}school/gotostudentlist.do">学生空间</a>&ndash;%&gt;
            </span>
            <span class="head-span">
                <c:choose>
                    <c:when test="${fn.contains(sessionValue.cloudUrl,'midong')}">
                        <a href="http://midong.k6kt.com/preparation/listPage.do" target="_blank">集体备课</a>
                        <a href="http://midong.k6kt.com/project/listPage.do" target="_blank">课题研究</a>
                        <a href="http://midong.k6kt.com/research1/listIndex.do" target="_blank">教研专题</a>
                        <a href="http://midong.k6kt.com/reviewcourse/list.do" target="_blank">评课议课</a>
                        <a href="http://midong.k6kt.com/achievement/list.do" target="_blank">科研成果</a>
                        </c:when>
                    <c:otherwise>
                        <a href="http://yun.k6kt.com/preparation/listPage.do" target="_blank">集体备课</a>
                        <a href="http://yun.k6kt.com/project/listPage.do" target="_blank">课题研究</a>
                        <a href="http://yun.k6kt.com/research1/listIndex.do" target="_blank">教研专题</a>
                        <a href="http://yun.k6kt.com/reviewcourse/list.do" target="_blank">评课议课</a>
                        <a href="http://yun.k6kt.com/achievement/list.do" target="_blank">科研成果</a>
                    </c:otherwise>
                </c:choose>
                &lt;%&ndash;<a href="${sessionValue.cloudUrl}preparation/listPage.do">集体备课</a>
                        <a href="${sessionValue.cloudUrl}project/listPage.do">课题研究</a>
                        <a href="${sessionValue.cloudUrl}research1/listIndex.do">教研专题</a>
                        <a href="${sessionValue.cloudUrl}reviewcourse/list.do">评课议课</a>
                        <a href="${sessionValue.cloudUrl}achievement/list.do">科研成果</a>&ndash;%&gt;
            </span>
            <span class="head-span span-b">
                <c:choose>
                    <c:when test="${fn.contains(sessionValue.cloudUrl,'midong')}">
                        <a href="http://midong.k6kt.com/courseware/cloudList.do?urlType=tbzy" target="_blank">同步资源</a>
                        <a href="http://midong.k6kt.com/itempool/frontList.do" target="_blank">题库资源</a>
                        <a href="http://midong.k6kt.com/courseware/cloudList.do?urlType=slzy" target="_blank">实录资源</a>
                        <a href="http://midong.k6kt.com/courseware/cloudList.do?urlType=zhzy" target="_blank">综合资源</a>
                        <a href="http://midong.k6kt.com/courseware/cloudList.do?urlType=wkzy" target="_blank">微课资源</a>
                    </c:when>
                    <c:otherwise>
                        <a href="http://yun.k6kt.com/courseware/cloudList.do?urlType=tbzy" target="_blank">同步资源</a>
                        <a href="http://yun.k6kt.com/itempool/frontList.do" target="_blank">题库资源</a>
                        <a href="http://yun.k6kt.com/courseware/cloudList.do?urlType=slzy" target="_blank">实录资源</a>
                        <a href="http://yun.k6kt.com/courseware/cloudList.do?urlType=zhzy" target="_blank">综合资源</a>
                        <a href="http://yun.k6kt.com/courseware/cloudList.do?urlType=wkzy" target="_blank">微课资源</a>
                    </c:otherwise>
                </c:choose>
                &lt;%&ndash;<a href=${sessionValue.cloudUrl}courseware/cloudList.do?urlType=tbzy">同步资源</a>
                <a href="${sessionValue.cloudUrl}itemstore/frontList.do">题库资源</a>
                <a href="${sessionValue.cloudUrl}courseware/cloudList.do?urlType=slzy">实录资源</a>
                <a href="${sessionValue.cloudUrl}courseware/cloudList.do?urlType=zhzy">综合资源</a>
                <a href="${sessionValue.cloudUrl}courseware/cloudList.do?urlType=wkzy">微课资源</a>&ndash;%&gt;
            </span>--%>
            <span class="head-span">
                <a class="head-nav-JC">基础应用</a>
                <a target="_blank" href="${sessionValue.cloudUrl}school/gotonewslist.do?type=1">新闻通知</a>
                <a target="_blank" href="${sessionValue.cloudUrl}school/gotonewslist.do?type=2">活动评选</a>

                <c:choose>
                    <c:when test="${roles:isNotStudentAndParent(sessionValue.userRole)}">
                        <a href="#">公文流转</a>
                    </c:when>
                    <c:otherwise>
                        <a target="_blank" href="/docflow/documentList.do?type=0&version=51&tag=4&a=10000">公文流转</a>
                    </c:otherwise>
                </c:choose>
                <a target="_blank" href="${sessionValue.cloudUrl}videoconference/list.do">视频会议</a>
                <c:choose>
                    <c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isHeadmaster(sessionValue.userRole)}">
                        <a href="/manageCount/schooltotal.do?version=88&a=10000&schoolid=${sessionValue.schoolId}" target="_blank">应用排行</a>
                    </c:when>
                    <c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isManager(sessionValue.userRole)}">
                        <a href="/manageCount/schooltotal.do?version=88&a=10000&schoolid=${sessionValue.schoolId}" target="_blank">应用排行</a>
                    </c:when>
                    <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                        <a href="/manageCount/schooltotal.do?version=88&a=10000&schoolid=${sessionValue.schoolId}" target="_blank">应用排行</a>
                    </c:when>
                    <c:when test="${roles:isHeadmaster(sessionValue.userRole)}">
                        <a href="/manageCount/schooltotal.do?version=88&a=10000&schoolid=${sessionValue.schoolId}" target="_blank">应用排行</a>
                    </c:when>
                    <c:when test="${roles:isStudent(sessionValue.userRole)||roles:isParent(sessionValue.userRole)||roles:isManagerOnly(sessionValue.userRole)}">
                        <a href="#">应用排行</a>
                    </c:when>
                    <c:when test="${roles:isEducation(sessionValue.userRole)}">
                        <a href="/manageCount/countMain.do?version=90&tag=1&a=10000" target="_blank">应用排行</a>
                    </c:when>
                    <c:otherwise>
                        <a href="#">应用排行</a>
                    </c:otherwise>
                </c:choose>
            </span>
             <span class="head-span head-bor">
                <a class="head-nav-ZY">资源应用</a>
                <a target="_blank" href="${sessionValue.cloudUrl}/cloud/cloudLesson.do">微课资源</a>
                <a target="_blank" href="${sessionValue.cloudUrl}/itempool/frontList.do?flag=tcv">题库资源</a>
                <a target="_blank" href="${sessionValue.cloudUrl}/cloudres/cloudList.do">教案课件</a>
                <a target="_blank" href="${sessionValue.cloudUrl}/school/network.do">专题教育</a>
                 <c:choose>
                     <c:when test="${roles:isStudentOrParent(sessionValue.userRole)||roles:isManagerOnly(sessionValue.userRole)}">
                         <a href="#">微课大赛</a>
                     </c:when>
                     <c:otherwise>
                         <a target="_blank" href="/microlesson/micropage.do?version=1b&tag=1&a=10000">微课大赛</a>
                     </c:otherwise>
                 </c:choose>

            </span>
             <span class="head-span">
                <a class="head-nav-YC">远程交互</a>
                <a target="_blank" href="${sessionValue.cloudUrl}videoAir/index.do?type=1">直播点播</a>
                <a target="_blank" href="${sessionValue.cloudUrl}interactLesson/lanclass.do">互动课堂</a>
                <a target="_blank" href="${sessionValue.cloudUrl}onlineClass/gotoFristPage.do">远程同步课堂</a>
                <a target="_blank" href="${sessionValue.cloudUrl}courseware/mailClassroomList.do?urlType=tbzy">专递课堂</a>
                <a target="_blank" href="${sessionValue.cloudUrl}onlineCourses/list.do">网络授课</a>
            </span>
             <span class="head-span head-bor">
                <a class="head-nav-JX">教学教研</a>
                <a target="_blank" href="/registration/list.do?version=5g&tag=3&a=10000">成长档案</a>
                <c:choose>
                    <c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isHeadmaster(sessionValue.userRole)}">
                        <a href="/score/teacher.do?version=17&a=10000" target="_blank">成绩分析</a>
                    </c:when>
                    <c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isManager(sessionValue.userRole)}">
                        <a href="/score/teacher.do?version=17&a=10000" target="_blank">成绩分析</a>
                    </c:when>
                    <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                        <a href="/score/teacher.do?version=17&a=10000" target="_blank">成绩分析</a>
                    </c:when>
                    <c:when test="${roles:isStudent(sessionValue.userRole)}">
                        <a href="/score/student.do?version=17&a=10000" target="_blank">成绩分析</a>
                    </c:when>
                    <c:when test="${roles:isParent(sessionValue.userRole)}">
                        <a href="/score/student.do?version=17&a=10000" target="_blank">成绩分析</a>
                    </c:when>
                    <c:when test="${roles:isHeadmaster(sessionValue.userRole)}">
                        <a href="#">成绩分析</a>
                    </c:when>
                    <c:when test="${roles:isManagerOnly(sessionValue.userRole)}">
                        <a href="#">成绩分析</a>
                    </c:when>
                    <c:when test="${roles:isEducation(sessionValue.userRole)}">
                        <a href="/score/educationBureau.do?version=17&a=10000" target="_blank">成绩分析</a>
                    </c:when>
                    </c:choose>
                <a target="_blank" href="${sessionValue.cloudUrl}preparation/listPage.do">集体备课</a>
                <a target="_blank" href="${sessionValue.cloudUrl}reviewcourse/list.do">评课议课</a>
                <a target="_blank" href="${sessionValue.cloudUrl}research1/listIndex.do">课题研究</a>
            </span>
             <span class="head-span">
                <a class="head-nav-KJ">空间风采</a>
                <a target="_blank" href="${sessionValue.cloudUrl}school/gotoschoollist.do">学校空间</a>
                <a target="_blank" href="${sessionValue.cloudUrl}school/gotoclasslist.do">班级空间</a>
                <a target="_blank" href="${sessionValue.cloudUrl}school/gototeacherlist.do">老师空间</a>
                <a target="_blank" href="${sessionValue.cloudUrl}school/gotostudentlist.do">学生空间</a>
                <a target="_blank" href="${sessionValue.cloudUrl}school/gotoparentlist.do">家长空间</a>
            </span>
        </div>
        <div class="warning">
            贵校未采购该功能，请联系：400-820-6735
        </div>
    </div>

</div>
<%--voyage_wu微课大赛首页--%>

