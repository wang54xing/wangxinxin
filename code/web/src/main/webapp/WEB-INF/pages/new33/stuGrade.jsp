<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>班级</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/stucourse.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
        <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <script type="text/javascript">
    	$(function(){
    		$('.d-sty em').click(function(){
    			$(this).addClass('cur').siblings().removeClass('cur');
    		})
    		$('.add').click(function(){
    			$('.bg').show();
    			$('.addg-popup').show();
    		})
    		$('.gg').click(function(){
    			$('.bg').show();
    			$('.editg-popup').show();
    		})
    		$(' .qxx').click(function(){
    			$('.bg').hide();
    			$('.addg-popup').hide();
    			$('.editg-popup').hide();
    		})
    		$('.close').click(function(){
    			$('.bg').hide();
    			$('.addg-popup').hide();
    			$('.editg-popup').hide();
    		})
    	})
    </script>
    <style type="text/css">
     .imghides{
        display: inline-block;
    }
    </style>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
	<div class="right-pos">
		<a href="/newisolatepage/classManage.do" class="h2">班级</a>
		<a href="/newisolatepage/studentManage.do" class="hv1">学生</a>
		<a href="/newisolatepage/jxClass.do" class="jxh">教学班</a>
	</div>
	<div class="center w1300 clearfix">
		<div class="ul-sty center mt25 fl">
			<ul class="bg-line">
				<li class="li-sty" hidden>
					<span class="fl">学期：</span>
					<script type="text/template" id="term_temp">
                        {{~it:value:index}}
                        <em ids="{{=value.ciId}}">{{=value.ciname}}</em>
                        {{~}}
                    </script>
					<div class="d-sty fl" id = "term">
					
					</div>
				</li>
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
			</ul>
		</div>
	</div>
	<div class="center w1300 clearfix">
		<div class="mt20 cur-tab cou-tab center fl">
			<button class="mb20 load-bt f16 ml10 fr download" hidden="true">下载模板</button>
			<button class="mb20 impor-bt f16 ml10 fr sc" hidden="true">导入</button>
			<button class="mb20 gen-btn f16 fr add" hidden="true">新增班级</button>
			<div class="inline pos" hidden="true">
			 	<input class="pos-inp" type="file" id="file" name="file" hidden="true">
			 	<button class="pos-btn btn" hidden="true">选择文件</button>
		 	</div>
		 	<span class="file-name" style="padding-left: 117px;position:relative;top:16px" hidden="true"></span>
			<div class='center w1200 clearfix' id="none_png" hidden>
				<img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
			</div>
			<table style="width: 1200px" class="votab clstab" id="content" hidden>
				<thead>
					<tr>
						<th width="240">班级名称</th>
						<th width="240">年级</th>
						<th width="240">班级序号</th>
						<th width="240">学生数量</th>
						<th width="240" hidden="true">操作</th>
					</tr>
				</thead>
				<script type="text/template" id="classList">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.gname}}（{{=value.xh}}）班</td>
                        <td>{{=value.gname}}</td>
                        <td>{{=value.xh}}</td>
                        <td>{{=value.stuCount}}</td>
                        <td hidden = "true"><img class="opt imghides" src="/static_new/images/new33/edit.png" ids="{{=value.id}}"><img class="opts imghides" src="/static_new/images/new33/dell.png" ids="{{=value.id}}" stuCount = "{{=value.stuCount}}">
                        </td>
                    </tr>
                    {{~}}
                </script>
				<tbody id = "claList">
					
				</tbody>
			</table>
		</div>
	</div>
<div class="bg"></div>

<!--新增班级-->
<div class="popup addg-popup">
	<div class="popup-top">
		<em>新增班级</em>
		<i class="fr close"></i>
	</div>
	<dl class="g-dl">
		<dd class="mt25">
			<span class="mr15">班级名称</span>
			<input class="inp8" type="text">
		</dd>
		<dd class="mt25">
			<div class="inline mr15">
				<span class="mr15">班级序号</span>
				<input class="inp7" placeholder="请输入数字" type="text">
			</div>
		</dd>
	</dl>
	<div class="popup-btn mt25">
		<button class="ss">添加</button>
		<button class="qxx">取消</button>
	</div>
</div>

<!--编辑班级-->
<div class="popup editg-popup">
	<div class="popup-top">
		<em>编辑</em>
		<i class="fr close"></i>
	</div>
	<dl class="g-dl">
		<dd class="mt25">
			<span class="mr15">班级名称</span>
			<input class="inp8 claName" type="text">
		</dd>
		<dd class="mt25">
			<div class="inline mr15">
				<span class="mr15">班级序号</span>
				<input class="inp7 xh" type="text">
			</div>
		</dd>
	</dl>
	<div class="popup-btn mt25">
		<button class="qd">确定</button>
		<button class="qxx">取消</button>
	</div>
</div>
<div class="load">
	<span style="font-size: 18px;margin-left: -60px">正在导入，请稍后...</span>
	<img src="/static_new/images/new33/loading.gif">
</div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('stuGrade', function (stuGrade) {
    	stuGrade.init();
    });
</script>
</html>
