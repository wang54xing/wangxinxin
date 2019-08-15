<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>学生</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/stucourse.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <style>

    .imghide{
        display: inline-block;
    }
    </style>
    <script type="text/javascript">
    	$(function(){
    		
    		$('.d-sty em').click(function(){
    			$(this).addClass('cur').siblings().removeClass('cur')
    		})
    		$('.gg').click(function(){
    			$('.bg').show();
    			$('.gg-popup').show();
    		})
    		$('.gg-popup i,.qx').click(function(){
    			$('.bg').hide();
    			$('.gg-popup').hide();
    			$('.add-popup').hide();
    		})
    		$('.close,.qxx').click(function(){
    			$('.bg').hide();
    			$('.addn-popup').hide();
    			$('.add-popup').hide();
    			$(".imstu-popup1").hide();
    			$(".drsb-popup1").hide();
                $(".gs-popup").hide();
                $(".gp-popup").hide();
    		})
    		$('.add').click(function(){
    			$('.bg').show();
    			$('.add-popup').show();
    		})
    
    		$('.ad i').click(function(){
    			$(this).toggleClass('ad-cur')
    		})
    		/* $('.gge span').click(function(){
    			if($(this).text().indexOf('更改')!=-1){
    				$(this).parent().find('option').removeAttr('disabled','disabled')
    				$(this).text('确定')
    			}else{
					$(this).parent().find('option').attr('disabled','disabled')
    				$(this).text('更改')
    			}
    		}) */
    	})
    </script>
    <style type="text/css">
    </style>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
	<div class="right-pos">
		<a href="/newisolatepage/classManage.do" class="hv2">班级</a>
		<a href="/newisolatepage/studentManage.do" class="h1">学生</a>
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
			<button class="mb20 load-bt f15 ml10 fr download" hidden>下载模板</button>
			<button class="mb20 impor-bt f15 ml10 fr tongbu">同步学生&班级</button>
			<button class="mb20 gen-btn f15 ml10 fr add">添加学生</button>
			<%--<button class="mb20 gen-btn2 f15 ml10 fr">同步重行政班学生</button>--%>
		 	<div class="inline pos">
            	<input class="inp10" type="text" placeholder="请输入学生姓名" id="userName">
            	<i class="search at" id="sosu"></i>
		 	</div>
			<div class='center w1200 clearfix' id="none_png" hidden>
				<img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
			</div>
			<table style="width: 1200px" class="votab clstab" id="content" hidden>
				<thead>
					<tr>
						<th width="200">学生姓名</th>
						<th width="200">学籍号</th>
						<th width="150">性别</th>
						<th width="200">选科组合</th>
						<th width="150">学生层级</th>
						<th width="150">操作</th>
					</tr>
				</thead>
				<script type="text/template" id="studentList">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.userName}}</td>
                        <td>{{=value.studyNumber}}</td>
                        <td>{{=value.sexStr}}</td>
                        <td>{{=value.combiname}}<img class="opt gg imghide"  src="/static_new/images/new33/edit.png" ids="{{=value.id}}"></td>
						<td>
							<em class="gge">
                        	<select class = "levelSel" type = "text" ids = {{=value.id}}>
								{{? value.level==0}}
                      		 		<option  value="0" selected = "selected">无</option>
                        		{{?}}
								{{? value.level!=0}}
                      		 		<option  value="0">无</option>
                        		{{?}}

								{{? value.level==1}}
                      		 		<option  value="1" selected = "selected">1</option>
                        		{{?}}
								{{? value.level!=1}}
                      		 		<option  value="1">1</option>
                        		{{?}}

								{{? value.level==2}}
                      		 		<option  value="2" selected = "selected">2</option>
                        		{{?}}
								{{? value.level!=2}}
                      		 		<option  value="2">2</option>
                        		{{?}}

								{{? value.level==3}}
                      		 		<option  value="3" selected = "selected">3</option>
                        		{{?}}
								{{? value.level!=3}}
                      		 		<option  value="3">3</option>
                        		{{?}}

								{{? value.level==4}}
                      		 		<option  value="4" selected = "selected">4</option>
                        		{{?}}
								{{? value.level!=4}}
                      		 		<option  value="4">4</option>
                        		{{?}}

								{{? value.level==5}}
                      		 		<option  value="5" selected = "selected">5</option>
                        		{{?}}
								{{? value.level!=5}}
                      		 		<option  value="5" >5</option>
                        		{{?}}
							</select>
							</em>
						</td>
                        <td><img class="opts imghided" src="/static_new/images/new33/dell.png" ids="{{=value.id}}">
                        </td>
                    </tr>
                    {{~}}
                </script>
				
				<tbody id = "stuList">
					
				</tbody>
			</table>
		</div>
	</div>
</div>
<div class="bg"></div>
<!--更改选科-->
<div class="popup gg-popup" style="height: 300px;">
	<div class="popup-top">
		<em>更改选科</em>
		<i class="fr close"></i>
	</div>
	<dl class="popup-dll">
		<dd>
			<span>原始选科：</span>
			<input class="inp1" type="text" disabled="disabled">
		</dd>
		<dd>
			<span>变更为：</span>
			<script type="text/template" id="sel_temp">
				{{~it:value:index}}
					<option value="{{=value.id}}">{{=value.name}}</option>
				{{~}}
		 	</script>
			<select class = "sel2" id="sel">
				
			</select>
		</dd>
        <dd>
            <input style="vertical-align: top;margin-top: 4px;margin-right: 5px;" type="checkbox" id="uJXB">同时将该学生加入更改选科后教学班
        </dd>
	</dl>
	<div class="popup-btn mt25">
		<button class="qd">确定</button>
		<button class="qx">取消</button>
	</div>
</div>
<!--选科说明-->
<div class="popup gp-popup" style="height: 300px;display: none">
	<div class="popup-top">
		<em>加入教学班成功</em>
		<i class="fr close"></i>
	</div>
	<dl class="popup-dll">
		<dd><span id="stuName1"></span></dd>
		<dd>原所在教学班（该学生已移出这些教学班）</dd>
		<dd id="oJxbName"></dd>

		<dd class="mt20">加入教学班：</dd>
		<dd id="nJxbName"></dd>
	</dl>
</div>
<div class="popup gs-popup" style="height: 300px;width:400px;display: none">
	<div class="popup-top">
		<em>加入教学班失败</em>
		<i class="fr close"></i>
	</div>
	<dl class="popup-dll">
		<dd id="name"><span id="stuName"></span>未能加入更改选科后教学班，请尝试手动设置</dd>
		<dd id="reason">可能原因：未能找到<span id="combineName"></span>组合学生对应教学班</dd>
		<dd>该学生已移除原教学班</dd>
		<dd id="oJxbName1"></dd>
	</dl>
</div>
<!--添加学生-->
<div class="popup add-popup">
	<div class="popup-top">
		<em>添加学生</em>
		<i class="fr close"></i>
	</div>
	<dl class="popup-dll">
		<dd>
			<p class="mt40">请输入学生姓名或学籍号</p>
		</dd>
		<dd>
			<input class="inp3" type="text" >
		</dd>
	</dl>
	<div class="popup-btn mt25">
		<button class="ss">搜索</button>
		<button class="qx">取消</button>
	</div>
</div>
<div class="popup addn-popup">
	<div class="popup-top">
		<em>添加学生</em>
		<i class="fr close"></i>
	</div>
	<div class="addn-dv">
		<table>
			<thead>
				<tr>
					<th width="112">学生姓名</th>
					<th width="130">学籍号</th>
					<th width="90">性别</th>
					<th width="80">选择</th>
				</tr>
			</thead>
			<script type="text/template" id="student2List">
                    {{~it:value:index}}
                    <tr userId = {{=value.userId}}>
                        <td>{{=value.userName}}</td>
                        <td>{{=value.studyNumber}}</td>
                        <td>{{=value.sexStr}}</td>
                        <td class="ad">
						<i></i>
						</td>	
                    </tr>
                    {{~}}
             </script>
			<tbody id = "stu2List">
				
			</tbody>
		</table>
	</div>
	<div class="popup-btn mt25">
		<button class="tj" ids = "*">添加</button>
		<button class="qxx">取消</button>
	</div>
</div>

<div class="popup imstu-popup1">
	<div class="popup-top">
		<em>导入学生</em>
		<i class="fr close"></i>
	</div>
	<div class="posc">
		<div class="inline pos1">
			<input class="pos-inp" type="file" id="file" name="file">
			<button class="pos-btn btn">选择文件</button>
		</div>
		<span class="file-name"></span>
	</div>
	<div class="popup-btn mt10">
		<button class="qd" id = "qddr">确定</button>
		<button class="qxx">取消</button>
	</div>
</div>

<div class="popup drsb-popup1">
	<div class="popup-top">
		<em>导入失败学生信息</em>
		<i class="fr close sx"></i>
	</div>
	<div class="cou-tab mmt25 poss">
		<table class="pob">
			<thead>
			<tr>
				<th width="140">姓名</th>
				<th width="200">学号</th>
				<th width="200">学籍类型</th>
				<th width="120">性别</th>
				<th width="160">年级</th>
				<th width="140">班级序号</th>
				<th width="140">层级</th>
				<%--<th width="80">教学班类型</th>--%>
			</tr>
			</thead>
		</table>
		<div class="w1126">
			<div class="w1106">
				<table>
					<thead>
					<tr>
						<th width="140">姓名</th>
						<th width="200">学号</th>
						<th width="200">学籍类型</th>
						<th width="120">性别</th>
						<th width="160">年级</th>
						<th width="140">班级序号</th>
						<th width="140">层级</th>
						<%--<th width="80">教学班类型</th>--%>
					</tr>
					</thead>

					<script type="text/template" id="errorList">
						{{~it:value:index}}
						<tr>
							<td>{{=value.userName}}</td>
							<td>{{=value.stuNum}}</td>
							<td>{{=value.stuRegisterType}}</td>
							<td>{{=value.sex}}</td>
							<td>{{=value.grade}}</td>
							<td>{{=value.classXH}}</td>
							<td>{{=value.level}}</td>
							<%--<td>{{=value.}}</td>--%>
						</tr>
						{{~}}
					</script>
					<tbody id="stuErrorList">

					</tbody>
				</table>
			</div>
		</div>

	</div>
</div>

<%--<div class="load">
	<span style="font-size: 18px;margin-left: -60px">正在导入，请稍后...</span>
	<img src="/static_new/images/new33/loading.gif">
</div>--%>
<div class="jhh" hidden>
	<div class="jhh-top">数据处理中，请稍等！</div>
	<img class="jhhIm" src="/static_new/images/new33/loading1.gif">
	<span class="jhhSp"></span>
</div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('stuAdmini', function (stuAdmini) {
    	stuAdmini.init();
    });
</script>
</html>
