<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>教室设置</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
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
    		$(".right-pos a").eq(0).attr("class","jss2");

    	})

		function chooseClrm(){
			var windclrm = document.getElementsByClassName('wind-new-clrm')[0];
			var bg = document.getElementsByClassName('bg')[0];
			windclrm.style.display='block'
			bg.style.display='block'
		}
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
	<div class="mt20"></div>
	</div>
	<div class="optcont">
		<span hidden>学期</span>
		<select id="term" hidden>
			
		</select>
		 <script type="text/template" id="term_temp">
			{{~it:value:index}}
				<option value="{{=value.ciId}}" termId="{{=value.termId}}">{{=value.ciname}}</option>
			{{~}}
		 </script>
		<span>年级</span>
		<select id="grade">
			
		</select>
		<script type="text/template" id="grade_temp">
			{{~it:value:index}}
				<option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
			{{~}}
		 </script>
		<button class="btn-classroom-sel" id="select_classroom">教室选择</button>
	</div>
	<div class="center w1300 clearfix">
		<div class="mt20 cur-tab cou-tab center fl">
			<div class='center w1200 clearfix' id="none_png" hidden>
				<img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
			</div>
			<table style="width: 1200px" class="votab clstab" id="content" hidden>
				<thead>
					<tr>
						<th width="20%">教室</th>
						<th width="5%">容量</th>
						<th width="10%">类型</th>
						<th width="15%">班级</th>
						<th width="15%">已排课时/计划课时</th>
						<th width="15%">备注</th>
						<th width="10%">可排课</th>
						<th width="20%">操作</th>
					</tr>
				</thead>
				<tbody id="classlist">
					
				</tbody>
				<script type="text/template" id="classlist_temp">
				{{~it:value:index}}
					<tr>
						<td>{{=value.roomName}}</td>
						<td>{{=value.capacity}}</td>
						{{? value.type==1}}
						<td>教室</td>
						{{?? value.type==2}}
						<td>操场</td>
						{{?? value.type==3}}
						<td>功能教室</td>
						{{?}}
						{{? value.gradeName == null}}
						<td><img class="lset" src="/static_new/images/new33/lset.png" eid="{{=value.id}}" cid = "{{=value.classId}}"></td>
						{{?}}
						{{? value.gradeName != null}}
						<td>{{=value.gradeName}}（{{=value.classNumber}}）班<img class="lset" src="/static_new/images/new33/lset.png" eid="{{=value.id}}" cid = "{{=value.classId}}"></td>
						{{?}}
						<td>{{=value.arrangedTime}}/{{=value.classTime}}</td>
						<td>{{=value.description}}</td>
						{{? value.arrange==1}}
							<td><input type='checkbox' class="arranged" checked eid="{{=value.id}}" ></td>
						{{??}}
							<td><input type='checkbox' eid="{{=value.id}}" class="arranged"></td>
						{{?}}
						<td><img class="opt edit" eid="{{=value.id}}" desc="{{=value.description}}" cap="{{=value.capacity}}" type="{{=value.type}}"
								 src="/static_new/images/new33/editt.png"><img class="opt del" eid="{{=value.id}}" src="/static_new/images/new33/dell.png"></td>
					</tr>
				{{~}}
		 		</script>
			</table>
		</div>
	</div>
</div>

	<div class="wind wind-new-tea" id="class_select_dialog">
		<div class="d1">设置<em>×</em></div>
		<ul>
			<li>
				<span>班级</span>
				<select id="class_select">
					
				</select>
				 <script type="text/template" id="class_select_templ">
					{{~it:value:index}}
						{{?value.classId == ""}}
						<option value="{{=value.classId}}">无</option>
						{{?}}
						{{?value.classId != ""}}
						<option value="{{=value.classId}}">{{=value.gname}}（{{=value.xh}}）班</option>
						{{?}}
					{{~}}
				  </script>
			</li>
		</ul>
		<div class="dbtn">
			<button class="btn-ok" id="class_select_save">确认</button>
			<button class="btn-no">取消</button>
		</div>
	</div>




<div class="wind" id="select_classroom_dialog">
	<div class="d1">设置教室<em>×</em></div>
	<ul class="ul-jxl clearfix" id="loop">
		
	</ul>
	<script type="text/template" id="loopTmpl">
		{{~it:value:index}}
			<li>
				<img src="/static_new/images/new33/department.png" alt="教学楼" class="loop" eid="{{=value.id}}">
				{{=value.name}}
				<span></span>
			</li>			
		{{~}}
	</script>
	<span id="tip" style="display: none">该楼暂无教室数据，请去基础数据添加</span>
	<ul class="ul-js clearfix" id="roomlist">
		
	</ul>
	<script type="text/template" id="roomlist_templ">
		{{~it:value:index}}
			<li name="{{=value.loopId}}" style="display:none"><label><input type="checkbox" value="{{=value.id}}" ename="{{=value.number}}" class="check_room" >{{=value.number}}</label></li>
		{{~}}
	</script>
	<div class="dbtn">
		<button class="btn-ok" id="update_classroom">确认</button>
		<button class="btn-no">取消</button>
	</div>
</div>
<div class="wind wind-new-tea" id="edit_dialog">
	<div class="d1">编辑<em>×</em></div>
	<ul>
		<li>
				<span>备注</span>
				<input type="text" id="description">
		</li>
		<li>
			<span>教室容量</span>
			<input type="text" id="capacity">
		</li>
		<li>
			<span>类型</span>
			<select id="room_type">
				<option value="1">教室</option>
				<option value="2">操场</option>
				<option value="3">功能教室</option>
			</select>
		</li>
	</ul>
	
	<div class="dbtn">
		<button class="btn-ok" id="edit_save">确认</button>
		<button class="btn-no">取消</button>
	</div>
</div>
	<div class="bg"></div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('baseClassroom', function (baseClassroom) {
	  baseClassroom.init();
  });
</script>
</html>
