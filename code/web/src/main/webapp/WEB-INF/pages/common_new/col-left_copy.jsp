<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@page import="com.pojo.app.SessionValue" %>
<input type="hidden" id="versionValue" value="<%=request.getParameter("version") %>"/>
<input type="hidden" id="index_Value" value="<%=request.getParameter("index") %>"/>

<div class="col-left">

    <!--.user-info-->
    <div class="user-info">
        <img src="${sessionValue.midAvatar}"/>
        <em>${sessionValue.userName}</em>
        <span>经验值${sessionValue.experience}</span>
    </div>
    <!--/.user-info-->

    <!--.left-nav-->
   
   
   <jsp:include page="/WEB-INF/pages/common_new/nav_test.jsp" ></jsp:include>
   
   <!--  
    <c:choose>
        <c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isHeadmaster(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/teacher_headmaster_nav.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isManager(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/teacher_manager_nav.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isTeacher(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/teacher_nav.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isStudent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/student_nav.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/parent_nav.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isHeadmaster(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/headmaster_nav.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isManagerOnly(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/manager_nav.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isEducation(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/navs/education_nav.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isDoorKeeper(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/doorkeeper.jsp" ></jsp:include>
        </c:when>
         <c:when test="${roles:isDormManager(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/dormmanager.jsp" ></jsp:include>
        </c:when>
        <c:when test="${roles:isFunctionRoomManager(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/function_room_manager.jsp" ></jsp:include>
        </c:when>
        
        
        
        <c:otherwise>

        </c:otherwise>
    </c:choose>
  -->
    <!--/.left-nav-->

    <!--.wcal-->
    <div class="left-cal" id="calId"></div>
    <!--/.wcal-->

    <!--.orange-col-->
    <c:choose>
        <c:when test="${!roles:isHeadmaster(sessionValue.userRole)||roles:isTeacher(sessionValue.userRole)}">
            <div class="orange-col" id="top5Container">
                <div class="col-head">
                    <h3>同学排行</h3>
                </div>
                <ul class="col-main paihan" id="top5studentcontainner">
                        <%--<li class="clearfix">--%>
                        <%--<img src="http://placehold.it/45x45" />--%>
                        <%--<span>xinxin</span>--%>
                        <%--<em>经验值<i>238</i></em>--%>
                        <%--</li>--%>
                </ul>
            </div>
        </c:when>
    </c:choose>


    <!--/.orange-col-->

</div>
<script id="top5studenttemp" type="text/template">
    {{ for (var i = 0, l = it.length; i < l; i++) { }}
    <li class="clearfix">
        <img src="{{=it[i].imgUrl}}"/>
        <span>{{=it[i].userName}}</span>
        <em>经验值<i>{{=it[i].experienceValue}}</i></em>
    </li>
    {{}}}
</script>

