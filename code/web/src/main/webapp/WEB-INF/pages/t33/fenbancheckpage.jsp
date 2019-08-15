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
			<a href="/t33/fenban/page.do?tag=1">教学班管理</a>
			<a href="/t33/fenban/check/page.do?tag=1"  class="a-cur">分班检测</a>
			<a href="/t33/fenban/zuhe/page.do">组合教学班</a>
		</div>
	</div>

	<div class="mr1200 mt55">
		 <div class="popup-top">
            <a href="index-dc.html" class="prev-lt">&lt;返回<em class="add-gl"></em></a>
        </div>
        <input id="idTag" name="idTag" value="${idtag}" type="hidden"/>
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
	        	
	        			</td>
	        			<td id="u3"></td>
	        			<td id="u2"></td>
	        		</tr>
	        	</tbody>
	        </table>
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
							<th><em>操作</em></th>
						</tr>
					</thead>
					<tbody id="student_tr">
						
					</tbody>
				</table>
			</div>
		</div>
		
    </div>
</body>



    <script src="/static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('fenbancheckpage',function(fenbancheckpage){
        	fenbancheckpage.init();
        });
    </script>
</html>
