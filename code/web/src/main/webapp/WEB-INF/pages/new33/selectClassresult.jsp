<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%--<title class="i18n" name='title'>新高考3+3走班排课系统</title>--%>
	<title class="i18n" name='title'>班级结果</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript">
    	$(function(){
    		$('.cou-x em').click(function(){
    			$('.reset').show();
    		})
    		$('.d-sty em').click(function(){
    			$(this).addClass('cur').siblings().removeClass('cur')
    		})
    	})
    </script>
</head>
<body>
<div>
	<jsp:include page="head.jsp"></jsp:include>
    <div class="right-pos">
        <a href="/newisolatepage/schoolSelectLessonSet.do" class="c1">选科组合设置</a>
        <a href="/newisolatepage/selectProgress.do" class="xk2">选科进度</a>
        <a href="/newisolatepage/selectResult.do" class="b2">选科结果</a>
        <a href="/newisolatepage/selectClassresult.do" class="d1">班级结果</a>
		<a href="/newisolatepage/compared.do" class="db2">选科结果对比</a>
    </div>
	<div class="center w1300 clearfix">
		<div class="ul-sty center mt25 fl">
			<ul class="bg-line">
				<li class="li-sty" hidden>
					<span class="fl">学期：</span>
					<div class="d-sty fl" id="term">

					</div>
					<script type="text/template" id="term_temp">
                             {{~it:value:index}}
									<em val="{{=value.ciId}}">{{=value.ciname}}</em>
							 {{~}}
                   </script>
				</li>
				<li class="li-sty">
					<span class="fl">年级：</span>
					<div class="d-sty fl" id="grade">

					</div>
					<script type="text/template" id="grade_temp">
                             {{~it:value:index}}
									<em val="{{=value.gid}}" grade="{{=value.gnm}}">{{=value.gnm}}({{=value.jie}})</em>
							 {{~}}
                   	</script>
				</li>
				<li class="li-sty">
					<span class="fl" id="Klass">班级：</span>
					<div class="d-sty fl" id="klass">

					</div>
					<script type="text/template" id="klass_temp">
                             {{~it:value:index}}
									<em val="{{=value.classId}}">{{=value.gname}}（{{=value.xh}}）班</em>
							 {{~}}
                   	</script>
				</li>
			</ul>
		</div>
	</div>
	<div class="center w1300">
        <div class="inline pos mt20 mb10 fl">
            <input class="inp10" type="text" placeholder="请输入学生姓名" id="stuName">
            <i class="search at" id="sosu"></i>
        </div>
		<div class="mt20 cou-tab center clearfix fl">
			<table>
				<thead>
					<tr>
						<th class="w260">学生姓名</th>
						<th class="w260">班级</th>
						<th class="w260">学号</th>
						<th class="w260">性别</th>
						<th class="w260">层级</th>
						<th class="w260">所选组合</th>
					</tr>
				</thead>
				<tbody id="student">
					
				</tbody>
				<script type="text/template" id="student_temp">
                             {{~it:value:index}}
									<tr>
										<td>{{=value.name}}</td>
										<td>{{=value.grade}}（{{=value.xh}}）班</td>
										<td>{{=value.sn}}</td>
										<td>{{=value.sex}}</td>
										<td>{{=value.lev}}</td>
										<td>
                                            {{=value.group}}
                                        </td>
									</tr>
							 {{~}}
                   	</script>
			</table>
		</div>
	</div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('selectClassresult', function (selectClassresult) {
	  selectClassresult.init();
  });
</script>
</body>
</html>
