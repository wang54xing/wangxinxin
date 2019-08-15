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
	<script type="text/javascript" src="/static/plugins/echarts/echarts.js"></script>
    <script type="text/javascript">
    	$(function(){
    	})
    </script>
</head>
<body>
<div>
	<jsp:include page="head.jsp"></jsp:include>
	<div class="right-pos">
		<a href="/newisolatepage/schoolSelectLessonSet.do" class="c1">选科组合设置</a>
		<a href="/newisolatepage/selectProgress.do" class="xk2">选科进度</a>
		<a href="/newisolatepage/selectResult.do" class="c2">选科结果</a>
		<a href="/newisolatepage/selectClassresult.do" class="d2">班级结果</a>
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
									<em val="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</em>
							 {{~}}
                   	</script>
				</li>
				<li class="li-sty">
					<span class="fl">组合：</span>
					<div class="d-sty fl" id="type">
						<em val="3">三科</em>
						<em val="2">双科</em>
						<em val="1">单科</em>
					</div>
				</li>
			</ul>
		</div>
	</div>

	<div class="center w1300 clearfix">
		<div class="mt20 cur-tab cou-tab center fl">
			<div class="center w1300 clearfix">
				<div class="center fl">
					<ul class="ulSty Styno fl">
						<a href="/newisolatepage/selectResult.do">
							<li>表格</li>
						</a>
                        <a href="/newisolatepage/selectResultCharts.do">
                            <li class="ulcur">图形</li>
                        </a>

					</ul>
                    <select class="sel1 fr" id="classList" style="margin-top: 8px;"></select>
                    <script type="text/template" id="classList_temp">
                        {{~it:value:index}}
						{{? value.class != null}}
						<option value="{{=value.classId}}">{{=value.class}}</option>
						{{?}}
						{{? value.class == null}}
						<option value="{{=value.classId}}">{{=value.gname}}（{{=value.xh}}）班</option>
						{{?}}
                        {{~}}
                    </script>
				</div>
			</div>
			<div id="main1" class="center" style="width:1200px;height:485px;"></div>
		</div>
	</div>
</div>
<div class="bg"></div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('selectResultCharts', function (selectResultCharts) {
      selectResultCharts.init();
  });
</script>
<div class="load">
	<span style="font-size: 18px;margin-left: -60px">正在导入，请稍后...</span>
	<img src="/static_new/images/new33/loading.gif">
</div>
</body>
</html>
