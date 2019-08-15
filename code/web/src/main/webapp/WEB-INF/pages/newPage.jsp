<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2017/1/6
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.pojo.app.SessionValue"%>
<html>
<head>
    <title>首页</title>
  <meta charset="utf-8">
  <meta id="i18n_pagename" content="index-common">
  <meta name="viewport" content="width=device-width">
  <meta name="keywords" content="" />
  <meta name="description" content=""/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <link rel="stylesheet" href="/static_new/css/reset-new.css"/>
  <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
  <script type="text/javascript" src="/static/js/roll.js"></script>
  <script type="text/javascript">
    $(function() {
      $.ajax({
        url: "/navs/citie.do",
        type: "post",
        data:{'type':1},
        success: function (data) {
          if (data!=null &&data.length!=0) {
            $('#navs').html();
            for(var i=0;i<data.length;i++) {
              $('#navs').append("<li class='appLink' pth='"+data[i].link+"'><img src='"+data[i].image+"'><em>"+data[i].name+"</em></li>");
            }
            $('.appLink').click(function() {
              window.open($(this).attr('pth'));
            });
          }
        }
      });
    });

  </script>
</head>
<body>
<div class="roll-main">
  <div class="roll-top">
    <div class="roll-top-info">
      <div class="head-main clearfix">
        <%--<img src="${sessionValue.schoolLogo}">--%>
        <c:if test="${ not empty sessionValue.schoolLogo }">
          <img src="${sessionValue.schoolLogo}" class="fl">
        </c:if>
        <%--<div class="roll-top-info-r">
          &lt;%&ndash;<img src="/images/roll-2.jpg">&ndash;%&gt;
            <img src="${sessionValue.midAvatar}" style="width: 40px;height: 40px;">
          <select>
            <option>人员1</option>
            <option>人员2</option>
          </select>--%>
          <div class="folder-out fr">
            <span>欢迎您，${sessionValue.userName}</span>
            <i onclick="loginout()">[退出]</i>
          </div>
      </div>
      </div>
    </div>
  </div>
  <div class="roll-info">
    <ul>
      <li>
        <div class="roll-info-list-b1">
          <img src="/images/desk/2.jpg">
        </div>
      </li>
      <li>
        <div class="roll-info-list">
          <div>
            <ul id="navs">
            </ul>
          </div>
        </div>
      </li>
    </ul>
  </div>
  <div class="roll-bottom">
    <img src="/images/desk/roll-4.jpg">
  </div>
</div>
<script>
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
</script>
</body>
</html>
