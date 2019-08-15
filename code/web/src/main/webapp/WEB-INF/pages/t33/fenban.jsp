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
    	$('.chat').click(function(){
    		$('.bg').show();
    		$('.gl-popup').show();
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
			<a href="/t33/fenban/page.do?tag=1" class="a-cur">教学班管理</a>
			<a href="/t33/fenban/check/page.do?tag=1">分班检测</a>
			<a href="/t33/fenban/zuhe/page.do">组合教学班</a>
		</div>
	</div>
	
	<c:forEach items="${subcc}" var="sub" >
			<input id="${sub.subjectId}_c" type="hidden" value="${sub.count}">
	</c:forEach>
	
	
	<div class="mr1200 mt55">
		<div class="list list-sta">
			<div>
				<span>学科：</span>
				<ul class="clearfix">
					<c:forEach items="${subcc}" var="sub" >
					 <li class="li-cur" id="${sub.subjectId }">${sub.name}</li>
					</c:forEach>
				</ul>
			</div>
			<div>
				<span>教学班：</span>
				<ul id="class_count">
					<li class="li-cur">物理1</li>
					<li>物理2</li>
					<li>物理3</li>
					<li>物理4</li>
					<li>物理5</li>
					<li>物理6</li>
				</ul>
				<button class="list-btn">重命名</button>
			</div>
		</div>


		<div class="tj">
		
			<p>物理1</p>
			<div class="tab">
				<table>
					<thead>
						<tr>
							<th><em>姓名</em></th>
							<th><em>行政班</em></th>
							<th><em>组合</em></th>
							<th><em>物理</em></th>
							<th><em>化学</em></th>
							<th><em>生物</em></th>
							<th><em>历史</em></th>
							<th><em>地理</em></th>
							<th><em>政治</em></th>
						</tr>
					</thead>
					<tbody id="student_tr">
						
					</tbody>
				</table>
			</div>
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
	        			<th>数学1</th>
	        			<th>交集</th>
	        			<th>语文1</th>
	        		</tr>
	        	</thead>
	        	<tbody>
	        		<tr>
	        			<td>
	        				<em>11111</em><em>22222</em><em>33333</em><em>44444</em><em>55555</em><em>11111</em><em>22222</em><em>33333</em><em>44444</em><em>55555</em>
	        				<em>11111</em><em>22222</em><em>33333</em><em>44444</em><em>55555</em><em>11111</em><em>22222</em><em>33333</em><em>44444</em><em>55555</em>
	        			</td>
	        			<td><em>11111</em></td>
	        			<td><em>11111</em></td>
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
        seajs.use('fenban',function(fenban){
        	fenban.init();
        });
    </script>
</html>
