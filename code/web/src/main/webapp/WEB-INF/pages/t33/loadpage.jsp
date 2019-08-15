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
    	$('.a-upload input').change(function(){
    		var name = $(':file').val();
	    	$("#file-name").text(name);
    	})
    })
    </script>
</head>
<body>
	<div class="top">
		<div class="top-center">
	        <a href="/t33/load/page.do" class="a-cur">选科数据</a>
			<a href="/t33/fenban/page.do?tag=1" >教学班管理</a>
			<a href="/t33/fenban/check/page.do?tag=1">分班检测</a>
			<a href="/t33/fenban/zuhe/page.do">组合教学班</a>
			
		</div>
	</div>
	<div class="mr1200 mt55">
		<!--选科统计-->
		<div class="tj tj-tab">
			<p>导入数据</p>
			<a href="javascript:;" class="a-upload">
			    <input type="file" name="" id="">选择
			</a>
			<span id="file-name"></span>
			<button class="btn-dr">导入</button>
			<p>选科统计</p>
			<div class="btn-l">
			    <form id="fenbanForm" action="/t33/fenban/page.do" method="post">
			    
			      <input type="hidden" name="wuli" id="wuli"/>
			      <input type="hidden" name="dili" id="dili"/>
			      <input type="hidden" name="zhengzhi" id="zhengzhi"/>
			      <input type="hidden" name="huaxue" id="huaxue"/>
			      <input type="hidden" name="lishi" id="lishi"/>
			      <input type="hidden" name="shengwu" id="shengwu"/>
			      <input type="hidden" name="tag" value="1"/>
			    
			    </form>
				<input class="" type="radio" name="ra" checked="checked"><span>行政班优先</span>
				<input class="" type="radio" name="ra"><span>组合优先</span>
				<button class="btn-dr" id="createClass">生成教学班</button>
				<button class="btn-dr set-btn" id="mySet">设定</button>
			</div>
			<div class="tab">
				<table>
					<thead>
						<tr>
							<th><em>学科</em></th>
							<th><em>选科人数</em></th>
							<th><em>教学班级规模上限</em></th>
							<th><em>班级数量</em></th>
						</tr>
					</thead>
					<tbody id="subject_tr">
			
					</tbody>
				</table>
			</div>
			
					<script id="subject_script" type="text/x-dot-template">
						{{~it:value:index}}
                           <tr id="{{=value.subjectIdStr}}">
							 <td>{{=value.subjectName}}</td>
							 <td class="totCount">{{=value.count}}</td>
							 <td class="perCount">
								<input type="text" class="inp1">
							 </td>
							 <td class="classCount"></td>

						    </tr>
						{{~}}
					  </script>
		</div>
		<!--选科数据-->
		<div class="tj">
		
			<p>选科数据</p>
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
    
    <!-- 设定 -->
    <div id="myForm" class="popup set-popup" style="display:none">
        <div class="popup-top">
            <em class="add-gl">学校/年级</em>
            <i class="close"></i>
        </div>
        <dl>
        	<dd>
	            <span>选择学校：</span>
	            <select id="se_school">
	            	<option value="59533a22b0fbeb15307a7566">蚌埠一中</option>
	            	<option value="58bf9e9eb0fbeb67fcd550ba">晋元高中</option>
	            </select>
            </dd>
            <dd>
           	    <span>选择年级：</span>
	            <select id="se_grade">
	            	<option value="59533a22b0fbeb15307a7568">高一</option>
	            </select>
            </dd>
            <dd>
           	    <span>分班人数：</span>
	            <input type="text" id="persionCount"/>
            </dd>
  
        </dl>
        <div class="set-bottom">
        	<button class="qd" >确定</button>
        	<button id="cancel">取消</button>
        </div>
    </div>
</body>

    <script src="/static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
      
        
        
        seajs.use('t33',function(t33){
        	t33.init();
        });
    </script>
</html>
