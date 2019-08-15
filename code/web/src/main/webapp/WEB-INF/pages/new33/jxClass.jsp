<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>教学班</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/stucourse.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <script type="text/javascript">
        $(document).on('click','.d-sty em',function(){
            $(this).addClass('cur').siblings().removeClass('cur')
        })
    </script>
    <style type="text/css">
        .jhh{
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            width: 420px;
            height: 200px;
            margin-left: -200px;
            margin-top: -100px;
            background: white;
            z-index: 19891018!important;
        }
    </style>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
	<div class="right-pos">
		<a href="/newisolatepage/classManage.do" class="hv2">班级</a>
		<a href="/newisolatepage/studentManage.do" class="hv1">学生</a>
		<a href="/newisolatepage/jxClass.do" class="jxh2">教学班</a>
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
					<div class="d-sty fl" id = "grade">

					</div>
				</li>
				<li class="li-sty">
					<span class="fl" id = "Klass">班级：</span>
					<script type="text/template" id="class_temp">
						{{~it:value:index}}
						<em ids="{{=value.classId}}">{{=value.gname}}（{{=value.xh}}）班</em>
						{{~}}
					</script>
					<div class="d-sty fl" id = "cla">
					</div>
				</li>
			</ul>
		</div>
	</div>
	<div class="center w1300 clearfix">
		<div class="mt20 cur-tab cou-tab center fl">
			<button class="mb20 gen-btn f15 ml10 fr " id="dcByGrade">导出年级</button>
			<button class="mb20 impor-bt f15 ml10 fr " id="dcByClass">导出本班</button>
			<div class="inline pos">
				<input class="inp10" type="text" placeholder="请输入学生姓名" id="userName">
				<i class="search at" id="sosu"></i>
			</div>
			<table style="width: 1200px" class="votab clstab">
				<thead>
					<tr id="zouban">
						<%--<th width="150">学生姓名</th>--%>
						<%--<th width="100">性别</th>--%>
						<%--<th width="150">学籍号</th>--%>
						<%--<th width="120">物理</th>--%>
						<%--<th width="120">化学</th>--%>
						<%--<th width="120">生物</th>--%>
						<%--<th width="120">历史</th>--%>
						<%--<th width="120">政治</th>--%>
						<%--<th width="120">地理</th>--%>
					</tr>
					<script type="text/template" id="zouban_temp">
						{{~it:value:index}}
							<th width="120">{{=value}}</th>
						{{~}}
					</script>
				</thead>
				<tbody id="jxb">
					<%--<tr>--%>
						<%--<td>张三</td>--%>
						<%--<td>男</td>--%>
						<%--<td>20916565</td>--%>
						<%--<td>物理1</td>--%>
						<%--<td>化学1</td>--%>
						<%--<td>生物1</td>--%>
						<%--<td>历史1</td>--%>
						<%--<td>政治1</td>--%>
						<%--<td>地理1</td>--%>
					<%--</tr>--%>
				</tbody>
				<script type="text/template" id="jxb_temp">
					{{~it:value:index}}
					<tr>
						<td>{{=value.stuName}}</td>
						<td>{{=value.sexStr}}</td>
						<td>{{=value.stuNum}}</td>
						{{~value.jxbNames:jxb:it}}
						<td>{{=jxb}}</td>
						{{~}}
					</tr>
					{{~}}
				</script>
			</table>
		</div>
	</div>
</div>
<div class="jhh">
    <div class="jhh-top">数据处理中，请稍等！</div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp"></span>
</div>

</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('jxClass', function (jxClass) {
        jxClass.init();
    });
</script>
</html>
