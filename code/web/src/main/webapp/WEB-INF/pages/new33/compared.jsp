<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>选科结果</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link rel="stylesheet" href="/static/js/fselect/fselect.css">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
	<style>
		.fs-wrap {
			float: left;
		}
		.fs-label-wrap {
			border: 1px solid #969696;
		}
		.fs-label-wrap .fs-label {
			padding: 10px 22px 10px 8px;
		}
		.compared-top .ssEl1{
			vertical-align: top;
			margin: -6px 0 15px 10px;
			padding: 0px;
		}
	</style>
</head>
<body>
<div>
	<jsp:include page="head.jsp"></jsp:include>
    <div class="right-pos">
        <a href="/newisolatepage/schoolSelectLessonSet.do" class="c1">选科组合设置</a>
        <a href="/newisolatepage/selectProgress.do" class="xk2">选科进度</a>
        <a href="/newisolatepage/selectResult.do" class="b2">选科结果</a>
        <a href="/newisolatepage/selectClassresult.do" class="d2">班级结果</a>
		<a href="/newisolatepage/compared.do" class="db1">选科结果对比</a>
    </div>
	<div class="center w1300 clearfix">
		<div class="ul-sty center mt25 fl">
			<ul class="bg-line">
				<li class="li-sty">
					<span class="fl">年级：</span>
					<script type="text/template" id="grade_temp">
						{{~it:value:index}}
						<em ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</em>
						{{~}}
					</script>
					<div class="d-sty fl" id="grade">
						<em class="cur"></em>
					</div>
				</li>
				<li class="li-sty">
					<span class="fl" id = "Klass">班级：</span>
					<script type="text/template" id="class_temp">
						{{~it:value:index}}
						<em ids="{{=value.classId}}">{{=value.gname}}（{{=value.xh}}）班</em>
						{{~}}
					</script>
					<div class="d-sty fl" id="cla">
					</div>
				</li>
			</ul>
		</div>
	</div>

	<div class="center w1300 clearfix">
		<div class="compared-top">
			<span class="fl">选择需对比学科数据：</span>
			<script type="text/template" id="times_temp">
				{{~it:value:index}}
				<option value="{{=value.ciId}}">{{=value.ciName}}</option>
				{{~}}
			</script>

			<select id="time" class="ssEl1">
			</select>

			<select class="xk_light ssEl1"><option value="1">查看全部学生</option><option value="2">只查看有差异学科学生</option><option value="3">只查看无差异学科学生</option></select>
			<%--<button>查询</button>--%>
		</div>

		<table class="compared-tb">
			<thead>
				<tr>
					<th width="10%">学生姓名</th>
					<th width="10%">班级</th>
					<th width="10%">学号</th>
					<th width="10%">性别</th>
					<th width="16%" id="ciId1Name">请选择选科数据</th>
					<th width="16%" id="ciId2Name">请选择选科数据</th>
					<th width="10%">差异学科</th>
					<th>备注信息</th>
				</tr>
			</thead>
			<script type="text/template" id="different_temp">
				{{~it:value:index}}
				<tr type="{{=value.flag}}" class="record">
					<td>{{=value.name}}</td>
					<td>{{=value.className}}</td>
					<td>{{=value.studentNumber}}</td>
					<td>{{=value.sexStr}}</td>
					<td>{{=value.combineName1}}</td>
					<td>{{=value.combineName2}}</td>
					<td>{{=value.different}}</td>
					<td>{{=value.remark}}</td>
				</tr>
				{{~}}
			</script>
			<tbody id="different">

			</tbody>
		</table>


	</div>
</div>

<script>
</script>
</body>

<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('compared', function (compared) {
        compared.init();
    });
</script>
</html>
