<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>  
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'></title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
    <link rel="stylesheet" href="/static_new/css/t33/reset.css"></link>
    <script type="text/javascript" src="/static_new/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript">
    $(function(){
    	$('.list li').click(function(){
    		$(this).addClass('li-cur').siblings().removeClass("li-cur")
    	})
    	
    	$('.close,.btn-qx').click(function(){
    		$('.bg').hide();
    		$('.gl-popup').hide();
    	})
    })
    </script>
</head>
<body>
	<div class="top">
		<div class="top-center">
			
			<a href="/t33/load/page.do">选科数据</a>
			<a href="/t33/fenban/page.do?tag=1" >教学班管理</a>
		<a href="" class="a-cur">分班检测</a>
		<a href="/t33/fenban/zuhe/page.do">组合教学班</a>
		</div>
	</div>
	<div class="mr1200 mt55">
		<div class="list">
			<ul id="compareUI">
					<c:forEach items="${subcc}" var="sub" >
					 <li class="li-cur" id="${sub.subjectId }">${sub.name}</li>
					</c:forEach>
			</ul>
			<button class="list-btn">分班</button>
		</div>
	
	
	
	
	    <c:forEach items="${subcc}" var="sub" >
			<input id="${sub.subjectId}_c" type="hidden" value="${sub.count}">
	    </c:forEach>
	    
	    
	     <c:forEach items="${jyjjbys}" var="sub" >
			<input id="${sub.subjectIds}" type="hidden" value="${sub.count}">
	    </c:forEach>
	
	<input id="jyjj" type="hidden" value="${jyjj}">
	<div id="container">
	
	
	</div>
	
	
		
	</div>
	<div class="bg"></div>
	<div class="popup gl-popup">
        <div class="popup-top">
            <em class="add-gl">数学1/语文1</em>
            <i class="close"></i>
        </div>
        <div class="popup-div">
	        <table class="popup-tab">
	        	<thead>
	        		<tr>
	        			<th id="s1">数学1</th>
	        			<th id="s3">交集</th>
	        			<th id="s2">语文1</th>
	        		</tr>
	        	</thead>
	        	<tbody>
	        		<tr>
	        			<td id="u1">
	        				<em>11111</em><em>22222</em><em>33333</em><em>44444</em><em>55555</em><em>11111</em><em>22222</em><em>33333</em><em>44444</em><em>55555</em>
	        				<em>11111</em><em>22222</em><em>33333</em><em>44444</em><em>55555</em><em>11111</em><em>22222</em><em>33333</em><em>44444</em><em>55555</em>
	        			</td>
	        			<td id="u3"><em>11111</em></td>
	        			<td id="u2"><em>11111</em></td>
	        		</tr>
	        	</tbody>
	        </table>
        </div>
    </div>
</body>


    <script src="/static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('fenbancheck',function(fenbancheck){
        	fenbancheck.init();
        });
    </script>
</html>
