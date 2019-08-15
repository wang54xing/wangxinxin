<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>教学周设置</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
    <link rel="stylesheet" href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css">
    <link rel="stylesheet" href="/static_new/css/rome.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript">
    	$(function(){
            $('.t-down').click(function(){
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top',-182)
            })
            $('.t-top').click(function(){
                $('.td-over').css('margin-top',0)
            })
    		$(".right-pos a").eq(2).attr("class","jzz2");
    	})
    </script>
    <style type="text/css">
		.right-pos{
			top: -140px;
		}
    </style>
</head>
<body>
<div>
	<jsp:include page="head.jsp"></jsp:include>
	<%@ include file="/WEB-INF/pages/new33/BasicDatasideBar.jsp" %>
	

	<div class="center w1300 clearfix">
		<div class="optcont" style="margin-top: 20px;">
			<span>学期</span>
			<select id="term">
			
			</select>
		 	<script type="text/template" id="term_temp">
				{{~it:value:index}}
					<option value="{{=value.id}}">{{=value.xqnm}}</option>
				{{~}}
		 	</script>
		</div>
		<%--<div class="vonav">--%>
			<%--<button class="btn-xqsz">学期设置</button>--%>
		<%--</div>--%>
	</div>
	<div class="center w1300 clearfix">
		<div class="mt20 cur-tab cou-tab center fl">
			<div class='center w1200 clearfix' id="none_png" hidden>
				<img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
			</div>
			<table style="width: 1200px" class="votab weektab" hidden id="content">
				<thead>
					<tr>
						<th width="9%">序号</th>
						<th width="13%">周一</th>
						<th width="13%">周二</th>
						<th width="13%">周三</th>
						<th width="13%">周四</th>
						<th width="13%">周五</th>
						<th width="13%">周六</th>
						<th width="13%">周日</th>
					</tr>
				</thead>
				<tbody id="week">
					
				</tbody>
				<script type="text/template" id="week_temp">
				{{~it:value:index}}
					<tr>
						<td>{{=value.numberWeek}}</td>
						{{~value.weekdays:value2:index2}}
						<td>{{=value2.date}}</td>
						{{~}}
					</tr>
				{{~}}
		 		</script>
			</table>
		</div>
	</div>
</div>

	<div class="wind wind-xqsz">
		<div class="d1">学期设置<em>×</em></div>
		<ul>
			<li>
				<span>学年</span>
				<select id="start_year">
					
				</select>
				<script type="text/template" id="start_year_temp">
				{{~it:value:index}}
					<option value="{{=value.val}}">{{=value.val}}</option>
				{{~}}
		 		</script>
				<em>-</em>
				<select id="end_year" disabled="true">
					
				</select>
				<script type="text/template" id="end_year_temp">
				{{~it:value:index}}
					<option value="{{=value.val}}">{{=value.val}}</option>
				{{~}}
		 		</script>
			</li>
			<li>
				<span>第一学期</span>
				<input type="text" id="dt1">
				<em>-</em>
				<input type="text" id="dt2">
			</li>
			<li>
				<span>第二学期</span>
				<input type="text" id="dt3">
				<em>-</em>
				<input type="text" id="dt4">
			</li>
		</ul>
		<div class="dbtn">
			<button class="btn-ok" id="save_term">确认</button>
			<button class="btn-no">取消</button>
		</div>
	</div>
	<div class="bg"></div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('baseWeek', function (baseWeek) {
	  baseWeek.init();
  });
</script>
</html>
