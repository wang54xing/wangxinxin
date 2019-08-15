<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>新高考3+3走班排课系统</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
     <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
   <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
</head>
<body>
<div>
	<jsp:include page="head.jsp"></jsp:include>
	<div class="mt45 center b-line com">
		<span>学科组合</span>
		<em class="fr" id="cannot_select"></em>
	</div>
	<div class="mt25 center com-sp" id="subject">
		
	</div>
	<script type="text/template" id="subject_temp">
             {{~it:value:index}}
					<span name="{{=value.name}}" sub1="{{=value.sub1}}" sub2="{{=value.sub2}}" sub3="{{=value.sub3}}" >{{=value.name}}<em></em></span>
			 {{~}}
    </script>
	<div class="mt25 center t-center">
		<button class="submit">提交</button>
	</div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('stuselect', function (stuselect) {
	  stuselect.init();
  });
</script>
</body>
</html>
