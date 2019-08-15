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
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript">
    	$(function(){
    		
    	})
    </script>
    <style>
        .stu-xkk-popup table tr td, .stu-xkk-popup table tr th{
            width: 120px!important;
        }
        .stu-xk-tb table tr td, .stu-xk-tb table tr th{
            width: 128px;
        }
        .stu-xk-tb {
            width: 100%;
            height: 455px;
            overflow-y: auto;
            padding: 0px 20px;
            box-sizing: border-box;
            margin-top: 20px;
        }
        .cou-tab tr td em{
            cursor: pointer;
        }
        .cou-tab tr td em:hover{
         color: red;
        }
    </style>
</head>
<body>
<div>
	<jsp:include page="head.jsp"></jsp:include>
    <div class="right-pos">
        <a href="/newisolatepage/schoolSelectLessonSet.do" class="c1">选科组合设置</a>
        <a href="/newisolatepage/selectProgress.do" class="xk2">选科进度</a>
        <a href="/newisolatepage/selectResult.do" class="c2">选科结果</a>
        <a href="/newisolatepage/selectClassresult.do" class="d2">班级结果</a>
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
		<div class="mt20 center fl">
			<ul class="ulSty Styno fl">
				<a href="/newisolatepage/selectResult.do">
					<li class="ulcur">表格</li>
				</a>
				<a href="/newisolatepage/selectResultCharts.do">
					<li>图形</li>
				</a>

			</ul>
            <div class="fr inline mt10">
                <button class="load-bt btn fr ml10 f16" id="download">下载模板</button>
                <button class="impor-bt btn fr f16" id="sc">导入</button>
				<button class="impor-bt btn fr f16" id="exportStuSelectResult" style="margin-right: 12px;">导出</button>
				<button id = "afreshId" class="impor-bt btn fr f16" style="margin-right: 12px;background: #6f69d6;border: 1px solid #6f69d6;display: none;" onclick="javascript:window.location.href='/newisolatepage/afreshClass.do'">重分行政班</button>
            </div>
		</div>
	</div>
	<div class="center w1300 clearfix">
		<div class='center w1200 clearfix' id="none_png" hidden>
			<img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
		</div>
		<div class="cur-tab cou-tab center fl" id="all_content" hidden>
			<div class="fl">
				<table>
					<thead>
						<tr>
							<th class="w150" id="header"></th>
						</tr>
					</thead>
					<tbody id="content2">
						
					</tbody>
					<script type="text/template" id="content2_temp">
                             {{~it:value:index}}
							 <tr>
								 <td class="itt allItt" zuHecount="{{=index}}" xkName="{{=value.name}}"><em class="w150">{{=value.name}}</em></td>
							 </tr>
							 {{~}}
                   		</script>
				</table>
			</div>
			<div class="cou-x fl" style="overflow-x: auto">
				<table>
					<thead>
						<tr id="head">
							
						</tr>
						<script type="text/template" id="head_temp">
                             {{~it:value:index}}
                             <%--{{?value.type == 1}}--%>
                             <%--<th class="w125"><em class="w125" classId="{{=value.classId}}">{{=value.name}}<br>{{=value.dh}}</em></th>--%>
							 <%--{{?}}--%>
                             <%--{{?value.type == 2}}--%>
                             <th class="w125"><em class="w125 font" classId="{{=value.classId}}">{{=value.name}}</em></th>
                             <%--{{?}}--%>
                             {{~}}
                   		</script>
					</thead>
					<tbody id="content">
						
					</tbody>
					<script type="text/template" id="content_temp">
						 {{~it:value:index}}
							<tr>
								{{~value.list:value2:index2}}
                                {{?value2.type == 1}}
									<td class="itt classItt" zuHeCount="{{=index}}" classId="{{=value2.id}}"><em class="w125">{{=value2.num}}/{{=value2.hgnum}}</em></td>
                                {{?}}
                                {{?value2.type == 2}}
                                <td class="itt classItt" zuHeCount="{{=index}}" classId="{{=value2.id}}"><em class="w125">{{=value2.num}}</em></td>
                                {{?}}
								{{~}}
							</tr>
						 {{~}}
					</script>
				</table>
			</div>
		</div>
	</div>
</div>

<div class="popup imselect-popup1">
	<div class="popup-top">
		<em>导入选科结果</em>
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

<div class="popup drsb-popup1" style="width: 881px;">
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


<div class="popup stu-xk-popup">
	<div class="popup-top">
		<em id="xkName"></em>
		<i class="fr close"></i>
	</div>
    <div style="position: relative">
	    <div class="stu-xk-tb" style="height:435px;">
		<table style="position: absolute;width: 645px;top: 0px;">
			<thead>
                <tr>
                    <th width="128">姓名</th>
                    <th width="100">班级</th>
                    <th width="204">学号</th>
                    <th width="80">性别</th>
                    <th width="128">层级</th>
                    <th hidden>选科</th>
                </tr>
            </thead>
        </table>
        <table style="width: 645px;">
            <thead style="color: transparent;margin-top: -40px;">
                <tr>
                    <th width="128">姓名</th>
                    <th width="100">班级</th>
                    <th width="204">学号</th>
                    <th width="80">性别</th>
                    <th width="128">层级</th>
                    <th hidden>选科</th>
                </tr>
            </thead>
			<tbody id="stuList">

			</tbody>

			<script type="text/template" id="stuList_temp">
				{{~it:value:index}}
				<tr>
					<td>{{=value.stuName}}</td>
					<td>{{=value.className}}</td>
					<td>{{=value.stuNum}}</td>
					<td>{{=value.sex}}</td>
					<td>{{=value.level}}</td>
					<td hidden>{{=value.combineName}}</td>
				</tr>
				{{~}}
			</script>
		</table>
	</div>
    </div>
</div>


<div class="popup stu-xkk-popup" style="display: none">
    <div class="popup-top">
        <em id="xkNamedan"></em>
        <i class="fr close"></i>
    </div>
    <div class="mt15">
        <select class="sel1" id="sel1">
            <option value="1">等级</option>
            <option value="2">合格</option>
        </select>
    </div>
    <div style="position: relative">
        <div class="stu-xk-tb" style="height:383px;">
            <table style="position: absolute;width: 750px;top: 0px;">
                <thead>
                <tr>
                    <th>姓名</th>
                    <th>班级</th>
                    <th>学号</th>
                    <th>性别</th>
                    <th>层级</th>
                    <th>选科</th>
                </tr>
                </thead>
            </table>
            <table style="width: 750px;">
                <thead style="color: transparent;margin-top: -40px;">
                    <tr>
                        <th>姓名</th>
                        <th>班级</th>
                        <th>学号</th>
                        <th>性别</th>
                        <th>层级</th>
                        <th>选科</th>
                    </tr>
                </thead>
            <tbody id="stuListdan">

            </tbody>

            <script type="text/template" id="stuList_tempdan">
                {{~it:value:index}}
                <tr class="stu" stuId="{{=value.stuId}}">
                    <td>{{=value.stuName}}</td>
                    <td>{{=value.className}}</td>
                    <td>{{=value.stuNum}}</td>
                    <td>{{=value.sex}}</td>
                    <td>{{=value.level}}</td>
                    <td>{{=value.combineName}}</td>
                </tr>
                {{~}}
            </script>
        </table>
           </div>
    </div>
</div>

<div class="jhh" hidden>
	<div class="jhh-top">数据处理中，请稍等！</div>
	<img class="jhhIm" src="/static_new/images/new33/loading1.gif">
	<span class="jhhSp"></span>
</div>

<div class="bg"></div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('selectResult', function (selectResult) {
	  selectResult.init();
  });
</script>
<div class="load">
	<span style="font-size: 18px;margin-left: -60px">正在导入，请稍后...</span>
	<img src="/static_new/images/new33/loading.gif">
</div>
</body>
</html>
