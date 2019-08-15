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
    	$('body').one('click','.btn-su',function(){
    		
    		
    		var num = $('.inp2').val();
    		for(var i =1; i <= num; i ++){
    			$('.com-tab table thead tr').append('<th><em>教室'+i+'</em></th>');
    			$('.com-tab table tbody tr').append('<td><em class="chat edit">更改</em></td>')
    		}
    		//$('.com-tab table thead tr').append('<th><em class="last-th">组合总人数/年级总人数</th></em>');
    		//$('.com-tab table tbody tr').append('<td>432/456</td>')
    	})
    	
    	$('body').on('click','.class-btn',function(){
    		
    		
    		var ht="<table>";
    		var nu = $('.inp2').val();
    		
    		for(var i =1; i <= nu; i ++){
    			ht+='<thead><tr><th><em>教室'+i+'</em></th></tr></thead><tbody><tr><td><em class="chat edit">更改</em></td></tr></tbody>';;
    		}
    		var ht="</table>";
    		
    		$('.com').append(ht);
    	})
    	$('.close,.qx').click(function(){
    		$(".gl-popup").hide();
    		$('.bg').hide();
    	})
    	$('.lis li').click(function(){
    		$(this).addClass('li-cur').siblings().removeClass('li-cur')
    	})
    	$('.lit li').click(function(){
    		$(this).addClass('li-cur').siblings().removeClass('li-cur')
    	})
    })
    </script>
</head>
<body>
	<div class="top">
		<div class="top-center">
			<a href="/t33/load/page.do">选科数据</a>
			<a href="/t33/fenban/page.do?tag=1">教学班管理</a>
			<a href="/t33/fenban/check/page.do?tag=1">分班检测</a>
			<a href="/t33/fenban/zuhe/page.do" class="a-cur">组合教学班</a>
		</div>
	</div>
	<div class="mr1200 mt55">
		<div class="com-top mt30 clearfix">
			<div class="top-left fl">
				<span>教室数量：</span>
				<input class="inp2" type="text">
				<button class="btn-dr mr20 btn-su">确定</button>
			</div>
			<button class="btn-dr fr class-btn">+教学班组合</button>
		</div>
		<div class="com">
			<div class="com-tab mt30 w1200">
				<table>
					<thead>
						<tr></tr>
					</thead>
					<tbody>
						<tr></tr>
					</tbody>
				</table>
			</div>
		</div>
		
		
		
		
		
		
		
		<!-- 
		
		<div class="com-tab mt30 w1200">
				<table>
					<thead>
						<tr>
							<th>教室1</th>
							<th>教室2</th>
						</tr>
					</thead>
					
					
					
					
					<tbody>
						<tr>
							<td><em class="chat edit">更改</em></td>
						</tr>
					</tbody>
				</table>
			</div>
		
		
		
		
		 -->
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
    </div>
    
    <c:forEach items="${subcc}" var="sub" >
			<input id="${sub.subjectId}_c" type="hidden" value="${sub.count}">
	</c:forEach>
	
	<input id="jyjj" type="hidden" value="${jyjj}">
	
    <div class="bg"></div>
    <div class="popup gl-popup">
        <div class="popup-top">
            <em class="add-gl">数学1/语文1</em>
            <i class="close"></i>
        </div>
        <div class="list list-sta com-list mm30">
			<div class="lis">
				<span>学科：</span>
				<ul id="subjectss" class="clearfix">
					<c:forEach items="${subcc}" var="sub" >
					   <li class="li-cur" id="${sub.subjectId }">${sub.name}</li>
					</c:forEach>
				</ul>
			</div>
			<div class="lit">
				<span>教学班：</span>
				<ul id="class_count">
		
				</ul>
			</div>
		</div>
		<div class="com-bottom mt30">
			<button class="qdaad">确定</button>
			<button class="qx">取消</button>
		</div>
    </div>
</body>

    <script src="/static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('fenbanzuhe',function(fenbanzuhe){
        	fenbanzuhe.init();
        });
    </script>
</html>
