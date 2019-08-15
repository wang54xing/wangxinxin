<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>重分行政班</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link rel="stylesheet" href="/static/js/fselect/fselect.css">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
</head>
<body>
<div>
	<jsp:include page="head.jsp"></jsp:include>

	<div class="center w1300 clearfix">
		<div class="ul-sty center mt25 fl">
			<ul class="bg-line">
				<li class="li-sty" hidden>
					<span class="fl">学期：</span>
					<input type="hidden" id="timeLoad" startTime="" endTime="">
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
	<div class="center w1300 clearfix" style="margin-bottom: 20px">
		<div class="mt20 center fl">
            <div class="fr inline mt10">
                <button class="ac-bt-4 btn fr f16" onclick="javascript:window.location.href='/newisolatepage/selectResult.do'">完成返回</button>
				<button class="ac-bt-1 btn fr f16 daochu" style="margin-right: 12px;">导出分班结果</button>
                <button class="ac-bt-1 btn fr f16 afresh" style="margin-right: 12px;">重新分班</button>
				<button class="ac-bt-2 btn fr f16 set_rule" style="margin-right: 12px;">设置分班规则</button>
				<button class="ac-bt-3 btn fr f16 choose_ks" style="margin-right: 12px;">选择考试成绩</button>
				<button class="ac-bt-2 btn fr f16" style="margin-right: 12px;" id="delClass">删除班级</button>
				<button class="ac-bt-1 btn fr f16" id='addClassP' style="margin-right: 12px;">新增班级</button>
            </div>
		</div>
	</div>
	<div class="center w1300 clearfix">
		<div class="cou-tab center fl">
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
						<th class="w125">
							<em class="w125" classId="{{=value.classId}}" nm="{{=value.name}}" xh="{{=value.xh}}">
								<em class="showClassName font">{{=value.name}}</em>
								<img class="et edit_name" src="/static_new/images/new33/edit.png">
							</em>
						</th>
						<%--{{?}}--%>
						{{~}}
					</script>
					</thead>
					<tbody id="content3">

					</tbody>
					<script type="text/template" id="content3_temp">
						{{~it:value:index}}
						<tr>
							{{~value.list:value2:index2}}
							{{?value2.type == 1}}
							<td class="itt classItt stu" zuHeCount="{{=index}}" classId="{{=value2.id}}" xkName="{{=value.subName}}" num="{{=value2.num}}/{{=value2.hgnum}}"><em class="w125">{{=value2.num}}/{{=value2.hgnum}}</em></td>
							{{?}}
							{{?value2.type == 2}}
							<td class="itt classItt stu" zuHeCount="{{=index}}" classId="{{=value2.id}}" xkName="{{=value.subName}}" num="{{=value2.num}}"><em class="w125">{{=value2.num}}</em></td>
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


<div class="bg"></div>

<%--选择考试成绩--%>
<div class="popup choose-ks-popup">
	<input type="hidden" id="reportId" value="">
	<div class="popup-top">
		<em>选择考试成绩</em>
		<i class="fr close choose_ks_close"></i>
	</div>
	<div class="choose-ks-group">
		<div class="ks-list" id="report">
			<%--<label><input type="radio" name="ks"><span>高一语文1</span></label>--%>
			<%--<label><input type="radio" name="ks"><span>高一语文1</span></label>--%>
			<%--<label><input type="radio" name="ks"><span>高一语文1</span></label>--%>
		</div>
		<script type="text/template" id="report_temp">
			{{~it:value:index}}
			<%--<em val="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</em>--%>
			<label><input class="selectReport" type="radio" name="ks" value="{{=value.id}}"><span>{{=value.name}}</span></label>
			{{~}}
		</script>
	</div>
	<div class="popup-btn mt10">
		<button class="qd choose_ks_qd">确定</button>
		<button class="qxx choose_ks_close">取消</button>
	</div>
</div>

<%--设置分班规则--%>
<div class="popup set-rule-popup">
	<div class="popup-top">
		<em>设置分班规则</em>
		<i class="fr close set_rule_close"></i>
	</div>
	<div class="rule-group">
		<div class="tb-tool">
			<div class="fl rd-group">
				<p><label><input type="radio" name="afreshType" value="1" checked>3+1+2定二走一</label></p>
				<p><label><input type="radio" name="afreshType" value="2">3+1+2单科优选</label><a class="dk_edit">编辑</a></p>
			</div>
			<div class="fr">
				<button class="all set_all_rule">编辑所有</button>
				<button class="bc">保存</button>
			</div>
		</div>
		<div class="tb-group" style="padding-top: 10px;">
			<table>
				<thead>
					<tr>
						<td>组合设置</td>
						<td width="120">插尖班数量</td>
						<td width="120">插尖班容量</td>
						<td width="120">插尖班浮动人数</td>
						<%--<td width="120">平行班数量</td>--%>
						<%--<td width="120">平行班容量</td>--%>
						<%--<td width="120">平行班浮动人数</td>--%>
					</tr>
				</thead>
				<tbody id="content">

				</tbody>
				<script type="text/template" id="content_temp">
					{{~it:value:index}}
					<tr class="sibb" id='{{=value.id}}' >
						<td class="className"  name="{{=value.subName}}">{{=value.name}}</td>
						<td><input type="number" class="classNumber1" num="{{=value.classNum}}" value='{{=value.classNum}}'></td>
						<td><input type="number" class="classNumber2" num="{{=value.classContain}}" value='{{=value.classContain}}'></td>
						<td><input type="number" class="classNumber3" num="{{=value.floatNum}}" value='{{=value.floatNum}}'></td>
					</tr>
					{{~}}
				</script>
				<%--<tbody>--%>
				<%----%>
					<%--<tr>--%>
						<%--<td>物化(3)</td>--%>
						<%--<td><input type="number"></td>--%>
						<%--<td><input type="number"></td>--%>
						<%--<td><input type="number"></td>--%>
						<%--&lt;%&ndash;<td><input type="number"></td>&ndash;%&gt;--%>
						<%--&lt;%&ndash;<td><input type="number"></td>&ndash;%&gt;--%>
						<%--&lt;%&ndash;<td><input type="number"></td>&ndash;%&gt;--%>
					<%--</tr>--%>
					<%--<tr>--%>
						<%--<td>物化(3)</td>--%>
						<%--<td><input type="number"></td>--%>
						<%--<td><input type="number"></td>--%>
						<%--<td><input type="number"></td>--%>
						<%--&lt;%&ndash;<td><input type="number"></td>&ndash;%&gt;--%>
						<%--&lt;%&ndash;<td><input type="number"></td>&ndash;%&gt;--%>
						<%--&lt;%&ndash;<td><input type="number"></td>&ndash;%&gt;--%>
					<%--</tr>--%>
				<%--</tbody>--%>
			</table>
		</div>
	</div>

</div>

<%--编辑全部规则--%>
<div class="popup set-all-rule-popup">
	<div class="popup-top">
		<em>编辑全部规则</em>
		<i class="fr close set_all_rule_close"></i>
	</div>
	<div class="all-rule-group">
		<div class="cell">
			<span>班级数量</span>
			<input type="number" id="ct1">
		</div>
		<div class="cell">
			<span>班级容量</span>
			<input type="number" id="ct2">
		</div>
		<div class="cell">
			<span>班级浮动人数</span>
			<input type="number" id="ct3">
		</div>
		<%--<div class="cell">--%>
			<%--<span>平行班数量</span>--%>
			<%--<input type="number">--%>
		<%--</div>--%>
		<%--<div class="cell">--%>
			<%--<span>平行班容量</span>--%>
			<%--<input type="number">--%>
		<%--</div>--%>
		<%--<div class="cell">--%>
			<%--<span>平行班浮动人数</span>--%>
			<%--<input type="number">--%>
		<%--</div>--%>
	</div>
	<div class="popup-btn mt10">
		<button class="qd set_all_rule_qd">确定</button>
		<button class="qxx set_all_rule_close">取消</button>
	</div>
</div>

<%--调班--%>
<div class="popup change-class-popup">
	<div class="popup-top">
		<em id="xkName"></em>
		<i class="fr close change_class_close"></i>
	</div>
	<div class="rule-group">
		<div class="tb-tool">
			<div class="search-group">
				<input id="keyword" name="" sore="" type="text" placeholder="请输入学生姓名">
				<a class="bt" id="keyword2"></a>
			</div>
			<button class="change-class-btn change_class">调班</button>
		</div>
		<div class="tb-group">
			<table>
				<thead>
				<tr>
					<td>姓名</td>
					<td>原班级</td>
					<td>现班级</td>
					<td>选科</td>
					<td>层级</td>
					<td>年级排名</td>
					<td>操作</td>
				</tr>
				</thead>

				<tbody id="stuListdan">

				</tbody>

				<script type="text/template" id="stuList_tempdan">
					{{~it:value:index}}
					<tr class="stum" stuId="{{=value.stuId}}" clsId="{{=value.clsId}}">
						<td>{{=value.stuName}}</td>
						<td>{{=value.oldClassName}}</td>
						<td>{{=value.className}}</td>
						<td>{{=value.combineName}}</td>
						<td>{{=value.level}}</td>
						<td>{{=value.num}}</td>
						<td>
							<input type="checkbox" stuId='{{=value.stuId}}' clsId="{{=value.clsId}}" class='stuChange' >
						</td>
						<%--<td><a class="change_class" stuId="{{=value.stuId}}">调班</a></td>--%>
					</tr>
					{{~}}
				</script>

				<%--</tbody>--%>
					<%--<tr>--%>
						<%--<td>张三</td>--%>
						<%--<td>高一（1）</td>--%>
						<%--<td>物生</td>--%>
						<%--<td>1</td>--%>
						<%--<td><a class="change_class">调班</a></td>--%>
					<%--</tr>
				</tbody>--%>
			</table>
		</div>
	</div>

</div>

<%--调整班级--%>
<div class="popup set-change-class-popup">
    <div class="popup-top">
        <em>班级调整</em>
        <i class="fr close set_change_class_close"></i>
    </div>
    <div class="change-class-group">
        <div class="cell" style="display: none">
            <span>现班级：</span>
            <select id="currClassSel"><option id="nowClass" name=""></option></select>
        </div>
        <div class="cell">
            <span>调整至：</span>
            <select id="optionId">

			</select>
			<script type="text/template" id="optionId_tempdan">
				{{~it:value:index}}
				<option value="{{=value.classId}}">{{=value.className}}</option>
				{{~}}
			</script>
        </div>
    </div>
    <div class="popup-btn mt10">
        <button class="qd set_change_class_add">确定</button>
        <button class="qxx set_change_class_close">取消</button>
    </div>
</div>

<%--修改班级名称--%>
<div class="popup class-name-popup">
	<div class="popup-top">
		<em>修改班级序号</em>
		<i class="fr close class_name_close"></i>
	</div>
	<div class="change-class-group">
		<input id="classId" type="hidden">
		<div class="cell">
			<span>班级序号：</span>
			<input id="classXuHao" type="number" value="">
		</div>
	</div>
	<div class="popup-btn mt10">
		<button class="qd">确定</button>
		<button class="qxx">取消</button>
	</div>
</div>

<%--删除班级--%>
<div class="popup class-del-popup">
	<div class="popup-top">
		<em>删除班级</em>
		<i class="fr close class_del_close"></i>
	</div>
	<div class="change-class-group">
		<div class="cell">
			<select id="bjList" multiple>
			</select>
			<script type="text/template" id="bjTempl">
				{{~it:value:index}}
					{{ if(value.noStu == 'true'){ }}
						<option value="{{=value.classId}}">{{=value.name}}</option>
					{{ } }}
				{{~}}
			</script>
		</div>
	</div>
	<div class="popup-btn mt10">
		<button class="qd" id='delLca'>确定</button>
		<button class="qxx">取消</button>
	</div>
</div>


<%--单科优选--%>
<div class="popup dkyx-popup">
	<div class="popup-top">
		<em>单科优选</em>
		<i class="fr close dkyx_close"></i>
	</div>
	<div class="change-class-group">
		<table>
			<thead>
				<tr>
					<td width="28%"></td>
					<td width="35%">班级数量</td>
					<td width="35%">班级容量</td>
				</tr>
			</thead>
			<tbody id="yxSubList">
			</tbody>
			<script type="text/template" id="yxSubList_tmpl">
				{{~it:value:index}}
				<tr class="stum" subId="{{=value.subId}}">
					<td>{{=value.subName}}</td>
					<td>
						<input type="number" class="number s2" value="{{=value.numberStr}}">
					</td>
					<td>
						<input type="number" class="volume s2" value="{{=value.volumeStr}}">
					</td>
				</tr>
				{{~}}
			</script>
		</table>
	</div>
	<div class="popup-btn">
		<button class="dkyx_submit q2">确定</button>
		<button class="dkyx_close">取消</button>
	</div>
</div>


<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
	seajs.use('afreshClass', function (afreshClass) {
		afreshClass.init();
	});
</script>
</body>
</html>
