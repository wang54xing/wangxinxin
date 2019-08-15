<%@page import="com.fulaan.base.controller.BaseController"%>
<%@page import="com.pojo.app.SessionValue"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%--<div style="position: relative; overflow: visible;">
 &lt;%&ndash; <div class="fun-button" onclick="fastnewlessonDialog()">
      <img src="/img/K6KT/recorder-luke.png"/>
  </div>
  <div class="funn-button" onclick="fastnewlessonDialog();">
      <img src="/img/K6KT/recorder-beike.png"/>
  </div>&ndash;%&gt;

    <span class="fast_container">
     	<c:choose>
            <c:when test="${dir=='cloud'}">
                <div class="" onclick="fastnewlessonDialog()">
                    <img src="/img/K6KT/recorder-luke.png"/>
                </div>
                <div class="" onclick="fastnewlessonDialog();">
                    <img src="/img/K6KT/recorder-beike.png"/>
                </div>
            &lt;%&ndash;    <span class="fast_load"><img src="/img/cloud_up.png"><span><a onclick="fastnewlessonDialog();">快速上传至备课空间</a></span></span>&ndash;%&gt;
              &lt;%&ndash;  <span class="fast_load"><img src="/img/cloud_down.png"><span><a href="/upload/resources/云课程清单.zip">下载云课程清单</a></span></span>&ndash;%&gt;
                <div class="" onclick="fastnewlessonDialog();">
                    <a href="/upload/resources/云课程清单.zip">
                        <img src="/img/K6KT/recorder-qingdan.jpg"/>
                     </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="" onclick="fastnewlessonDialog()">
                    <img src="/img/K6KT/recorder-luke.png"/>
                </div>
                <div class="" onclick="fastnewlessonDialog();">
                    <img src="/img/K6KT/recorder-beike.png"/>
                </div>
                &lt;%&ndash;<span class="fast_load" style="padding-right: 20px; text-align:right;"><img src="/img/cloud_up.png"><span><a onclick="fastnewlessonDialog();">快速上传至备课空间</a></span></span>&ndash;%&gt;
            </c:otherwise>
        </c:choose>
     </span>
</div>--%>


<style>
  #howtomakevk:hover {
    text-decoration: underline;
  }

  .fast_container {
      position: absolute;
      top: 0px;
      left: 765px;
      text-align: center;
      color: #FFA800;
      font-size: 14px;
      width: 80px;
      padding: 0px 0px 10px;
      cursor: pointer;
  }
  .fast_container>div{
      line-height: 20px;;
  }
</style>

<jsp:include page="right.jsp"></jsp:include>
<div class="cloud-tabs" style="margin-top: 20px;">

  <c:set var="dir" value="${param.dir}"/>
  
  
  <!-- 
  <c:choose>
    <c:when test="${sessionScope.isVote}">
      <%--<a href="/cloudClass" class="title_a"><span class="${dir=="cloud"?"active-l":"cloud-tile "} ">云课程</span></a>--%>
      <a href="/teacher/course" class="title_a"><span
              class="${dir=="teacher"?"active":""} cloud-tile ">上传微课</span></a>
      <a href="/vote/course" class="title_a"><span
              class="${dir=="voteCourse"?"active":""} cloud-tile ">微课评比</span></a>
      <a href="/school/course" class="title_a"><span class="${dir=="school"?"active":"cloud-tile"}  ">校本资源</span></a>
      <c:if test="${isVoteMaster}">
        <a href="/vote/result" class="title_a"><span
                class="${dir=="voteResult"?"active":""} cloud-tile ">评比结果</span></a>
      </c:if>
    </c:when>
    <c:otherwise>
	   
	    <c:choose>
        <c:when test="${not empty param.teacherId }">
          <a href="/teacher/course/${param.teacherId}" class="title_a"><span
                  class="cloud-tile active">备课空间</span></a>
        </c:when>
        <c:otherwise>
          <%--<a href="/cloudres/load.do" class="title_a"><span class=" ${dir=="cloud"?"active-l":"cloud-tile"}">云课程</span></a>--%>
          <a href="/lesson/teacher.do" class="title_a"><span
                  class=" ${dir=="teacher"?"active":"cloud-tile"}">备课空间</span></a>

          <a href="/lesson/school.do" class="title_a"><span
                  class=" ${dir=="school"?"active":"cloud-tile"}">校本资源</span></a>
          <a href="/lesson/league.do" class="title_a"><span
                  class=" ${dir=="league"?"active":"cloud-tile"}">联盟资源</span></a>
   
        </c:otherwise>
      </c:choose>
      
    </c:otherwise>
  </c:choose>
   -->
  
  
  
  
     <%--<span class="fast_container">
     	<c:choose>
          <c:when test="${dir=='cloud'}">

            <span class="fast_load"><img src="/img/cloud_up.png"><span><a onclick="fastnewlessonDialog();">快速上传至备课空间</a></span></span>
            <span class="fast_load"><img src="/img/cloud_down.png"><span><a href="/upload/resources/云课程清单.zip">下载云课程清单</a></span></span>

          </c:when>
          <c:otherwise>
            <span class="fast_load" style="padding-right: 20px; text-align:right;"><img src="/img/cloud_up.png"><span><a onclick="fastnewlessonDialog();">快速上传至备课空间</a></span></span>
          </c:otherwise>
        </c:choose>
     </span>--%>
    <div style="position: relative; overflow: visible;">
        <%-- <div class="fun-button" onclick="fastnewlessonDialog()">
             <img src="/img/K6KT/recorder-luke.png"/>
         </div>
         <div class="funn-button" onclick="fastnewlessonDialog();">
             <img src="/img/K6KT/recorder-beike.png"/>
         </div>--%>

    <span class="fast_container">
     	<c:choose>
            <c:when test="${dir=='cloud'}">
                <div class="" onclick="fastnewlessonDialog()">
                    <img src="/img/K6KT/recorder-luke.png"/>
                </div>
                <div class="" onclick="fastnewlessonDialog();">
                    <img src="/img/K6KT/recorder-beike.png"/>
                </div>
                <%--    <span class="fast_load"><img src="/img/cloud_up.png"><span><a onclick="fastnewlessonDialog();">快速上传至备课空间</a></span></span>--%>
                <%--  <span class="fast_load"><img src="/img/cloud_down.png"><span><a href="/upload/resources/云课程清单.zip">下载云课程清单</a></span></span>--%>
                <div class="" onclick="fastnewlessonDialog();" >
                    <a href="/upload/resources/云课程清单.zip" style="display: inline-block;height: 90px;">
                        <img src="/img/K6KT/recorder-qingdan.jpg"/>
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="" onclick="fastnewlessonDialog()">
                    <img src="/img/K6KT/recorder-luke.png"/>
                </div>
                <div class="" onclick="fastnewlessonDialog();">
                    <img src="/img/K6KT/recorder-beike.png"/>
                </div>
                <%--<span class="fast_load" style="padding-right: 20px; text-align:right;"><img src="/img/cloud_up.png"><span><a onclick="fastnewlessonDialog();">快速上传至备课空间</a></span></span>--%>
            </c:otherwise>
        </c:choose>
     </span>
    </div>
</div>