<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>排课规则设置</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
	<link rel="stylesheet" href="/static/css/zTreeStyle/zTreeStyle.css">
	<link rel="stylesheet" href="/static_new/css/new33/course.css">

    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="/static/js/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="/static/js/jquery.listnav.min-2.1.js"></script>
	<script type="text/javascript" src="/static/js/jquery.charfirst.pinyin.js"></script>
	<%--<script type="text/javascript" src="/static_new/js/modules/new33/0.1.0/pkRuleSet.js"></script>--%>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<script>

</script>
<input type='hidden' id='grade' value='${gradeId}'/>
<input type='hidden' id='cxq' value=''/>
<div class="center w1300 clearfix">
	<div class="mt35 center fl" style="position: relative;">
		<ul class="set-ul c-b set-ul2">
			<li>排课规则设置</li>
		</ul>
		<button class="add-bt btn rule-edit">编辑</button>
	</div>
</div>

<div class="center w1300 comm">
	<div class="mt20 cou-tab center clearfix fl">
		<div class="pk-gz-group">
			<div class="pk-gz-title">
				<ul class="gz-list gz_list"><li class="active">学科设置</li><li id='teacherSet'>教师设置</li><li>走班设置</li><li id='otherSet'>其他设置</li></ul>
			</div>
			<div class="gz-xk-content gz_ct gz_ct_1">

				<div class="xk_group">
					<%--<div class="lf">--%>
						<%--<div class="top"><span>学科列表</span><a class="ck_xk_all">全选</a></div>--%>
						<%--<ul class="xk-list xk-1 ck_xk" id="subject1">--%>
						<%--</ul>--%>
						<%--<script type="text/template" id="subject_temp1">--%>
							<%--{{~it:value:index}}--%>
								<%--<li><span class="ck" subId="{{=value.subid}}"></span><span class="name">{{=value.snm}}</span></li>--%>
							<%--{{~}}--%>
						<%--</script>--%>
					<%--</div>--%>
					<div class="rt">
						<p>排课规则：</p>
						<ul class="xk-list">
							<li>
								<span class="ck fp_ck" type="1" id="subjectPro"></span><span class="name" >学科进度一致</span>
								<span class="fp-set" id="su-fp-set">
									<a class="open_xk_1">编辑</a>
								</span>
							</li>
							<li>
								<span class="ck fp_ck" type="2" id="WeekKs"></span>
								<span class="name">周课时平均,日课时集中</span>
								<span class="fp-set" id="we-fp-set">
									<a class="open_xk_2">编辑</a>
								</span>
							</li>
							<li>
								<span class="ck fp_ck" type="3" id="AmPm"></span><span class="name">上下午课时分配</span>
								<span class="fp-set" id="ap-fp-set">
									<a class="open_xk_3">编辑</a>
								</span>
							</li>
							<li>
								<span class="ck hc_ck" type="4" id="mutexSubject"></span><span class="name">学科互斥（不在同一天上课）</span>
								<span class="hc-set" id="mu-hc-set"><a class="open_xk_hc">编辑</a></span>
							</li>
						</ul>
					</div>
				</div>

				<div class="xk_hc_group" style="display: none;margin: 25px;">
					<div class="hc-title">
						<p>学科互斥</p>
						<div class="bt">
							<a id="back_xk_hc">返回</a>
							<a id="new_xk_hc">新建互斥组</a>
						</div>
					</div>
					<table class="hc-tb">
						<thead>
						<tr>
							<td width="25%">互斥组</td>
							<td width="25%">学科一</td>
							<td width="25%">学科二</td>
							<td width="25%">操作</td>
						</tr>
						</thead>
						<tbody id="mutexSubject1">

						</tbody>
						<script type="text/template" id="mutexSubject_temp">
							{{~it:value:index}}
							<tr>
								<td>{{=index + 1}}</td>
								<td>{{=value.subName1}}</td>
								<td>{{=value.subName2}}</td>
								<td><a mutexSubId='{{=value.id}}' class='del'>删除</a></td>
							</tr>
							{{~}}
						</script>
					</table>
				</div>

			</div>
			<div class="gz-js-content gz_ct gz_ct_2">
				<div class="rt tc_group">
					<p style="margin-bottom: 10px;">排课规则：</p>
					<ul class="xk-list">
						<li>
							<span class="ck tc_zr_ck"></span><span class="name">教师周任课天数</span>
							<span class="tc-zr-set">
								<a class="tc_ts_edit">编辑</a><a>详情</a>
							</span>
						</li>
						<li>
							<span class="ck tc_lk_ck"></span><span class="name">教师日最大连课节数</span>
							<span class="tc-lk-set">
								<a class="tc_lk_edit">编辑</a><a>详情</a>
							</span>
						</li>
						<li><span class="ck tc_kb_kb"></span><span class="name">教师跨班连堂</span></li>
						<li>
							<span class="ck tc_kb_ck"></span><span class="name">教师跨班进度一致</span>
							<span class="tc-kb-set">
								<a class="tc_kb_edit">编辑</a><a>详情</a>
							</span>
						</li>
						<li>
							<span class="ck tc_gz_ck"></span><span class="name">教师规则（必须，优先，拒绝，避免）</span>
							<span class="tc-gz-set">
								<a href="/newisolatepage/pkTcRule.do">编辑</a><a>详情</a>
							</span>
						</li>
						<li>
							<span class="ck tc_hc_ck"></span><span class="name">教师互斥（不在同一时间上课）</span>
							<span class="tc-hc-set">
								<a class="open_tc_hc">编辑</a><a>详情</a>
							</span>
						</li>
					</ul>
				</div>

				<div class="tc_hc_group" style="display: none;">
					<div class="hc-title">
						<p>教师互斥</p>
						<div class="bt">
							<a id="back_tc_hc">返回</a>
							<a id="new_tc_hc">新建互斥组</a>
						</div>
					</div>
					<table class="hc-tb">
						<thead>
						<tr>
							<td width="25%">互斥组</td>
							<td width="25%">教师一</td>
							<td width="25%">教师二</td>
							<td width="25%">操作</td>
						</tr>
						</thead>
						<tbody id='nutexList'>
						
						
						</tbody>
						
						<script type="text/template" id="nutexTempl">
                			{{~it:value:index}}
                    	    	<tr>
									<td>{{=value.mutexNum}}</td>
									<td>{{=value.teaName0}}</td>
									<td>{{=value.teaName1}}</td>
									<td><a nutexId='{{=value.id}}' id='delNuyex'>删除</a></td>
								</tr>
                    		{{~}}
                		</script>
					</table>
				</div>

			</div>
			<div class="gz-js-content gz_ct gz_ct_3">
				<div class="rt">
					<p style="margin-bottom: 10px;">排课规则：</p>
					<ul class="xk-list">
						<li><span class="ck" id="ZouBanPro" type="1"></span><span class="name">走班学科轮次一致</span></li>
						<li>
							<span class="ck zb_sx_ck" id="ZouBanCount" type="2"></span>
							<span class="name">走班教室数量</span>
							<span class="zb-sx-set"><input type="number" id="ZouBanCountNum"></span>
						</li>
						<li>
							<span class="ck zb_jxb_ck" id="jxbStuCount" type="3"></span>
							<span class="name">教学班人数上限</span>
							<span class="zb-jxb-set"><input type="number" id="jxbStuCountNum"></span>
						</li>
						<li>
                            <span class="ck zb_fd_ck" id="roomStuCount" type="4"></span>
                            <span class="name">走班教室上下浮动人数上限</span>
                            <span class="zb-fd-set"><input type="number" id="roomStuCountNum"></span>
                        </li>

					</ul>
				</div>
			</div>
			<div class="gz-js-content gz_ct gz_ct_4">
				<div class="rt">
					<p style="margin-bottom: 10px;">排课规则：</p>
					<ul class="xk-list">
						<li>
							<span class="ck qt_xk_ck" id='qtxkck'></span><span class="name">选择可排学科(默认系统识别)</span>
							<span class="qt-xk-set">
								<a class="qt_xk_edit">编辑</a><a>详情</a>
							</span>
						</li>
						<li>
							<span class="ck qt_gz_ck"></span><span class="name">选择可排格子(默认系统识别)</span>
							<span class="qt-gz-set">
								<a class="qt_gz_edit">编辑</a><a>详情</a>
							</span>
						</li>
						<li><span class="ck qt-lt-set"></span><span class="name">连堂处理(大课间与午餐不作为连堂课)</span></li>
						<!-- <li>
							<span class="ck qt_js_ck"></span><span class="name">多功能教室数量设置</span>
							<span class="qt-js-set">
								<a class="qt_js_edit">编辑</a><a>详情</a>
							</span>
						</li> 课表质量检测时做 -->

						<li>
							<span class="ck qt_fd_ck"></span><span class="name">分段设置</span>
							<span class="qt-fd-set">
								<a class="qt_fd_edit">编辑</a><a>详情</a>
							</span>
						</li>

					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="bg"></div>

<%--选择老师周任课天数--%>
<div class="popup ts-popup">
	<div class="popup-top">
		<em class="fl">选择老师周任课天数</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="popup-content">
		<div class="popup-search">
			<input type="text" placeholder="教师搜索">
			<a id='searchTea'></a>
		</div>
		<div class="ct">
			<p class="top">备选教师<a>全选</a></p>
			<div class="list">
				<select multiple class="tc_bx" id='subTeaList'>
				
				</select>
				
				<script type="text/template" id="subTeaTempl">
                	{{~it:value:index}}
                        <option teaId='{{=value.userId}}' >{{=value.teaName}}</option>
                    {{~}}
                </script>
			</div>
		</div>
		<a class="jump tc_add jsw_add"><img src="/static_new/images/new33/icon_jump.png"></a>
		<div class="rt">
			<p class="top">已选教师<select id='weekCourse'><option>1</option><option>2</option><option>3</option><option>4</option><option>5</option></select></p>
			<div class="list">
				<ul class="tc_yx" id='cTeaList'>
					
				</ul>
				
				<script type="text/template" id="cTeaTempl">
                	{{~it:value:index}}
						<li teaId='{{=value.userId}}'>{{=value.teaName}}<a class="tc_remove">&times;</a></li>
                    {{~}}
                </script>
			</div>
		</div>
	</div>
	<div class="popup-btn">
		<button class="qd" id='jztQd' >确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>

<%--教师日最大连课节数--%>
<div class="popup lk-popup">
	<div class="popup-top">
		<em class="fl">教师日最大连课节数</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="popup-content">
		<div class="popup-search">
			<input type="text" placeholder="教师搜索" >
			<a id='searchDTea'></a>
		</div>
		<div class="ct">
			<p class="top">备选教师<a>全选</a></p>
			<div class="list">
				<select multiple class="lk_bx" id='dTeaList' >
				
				</select>
				
				<script type="text/template" id="dTeaTempl">
                	{{~it:value:index}}
						<option teaId='{{=value.userId}}'>{{=value.teaName}}</option>
                    {{~}}
                </script>
				
			</div>
		</div>
		<a class="jump lk_add"><img src="/static_new/images/new33/icon_jump.png"></a>
		<div class="rt">
			<p class="top">已选教师<select id='courseNum'><option>1</option><option>2</option><option>3</option><option>4</option><option>5</option></select></p>
			<div class="list">
				<ul class="lk_yx" id='dCTeaList'>
					
				</ul>
				
				<script type="text/template" id="dCTeaTempl">
                	{{~it:value:index}}
						<li teaId='{{=value.userId}}'>{{=value.teaName}}<a class="lk_remove">&times;</a></li>
                    {{~}}
                </script>
			</div>
		</div>
	</div>
	<div class="popup-btn">
		<button class="qd">确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>

<%--教师跨班进度一致--%>
<div class="popup kb-popup">
	<div class="popup-top">
		<em class="fl">选择教师跨班进度一致</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="popup-content">
		<div class="popup-search">
			<input type="text"  placeholder="教师搜索">
			<a id='searchTStep'></a>
		</div>
		<div class="ct">
			<p class="top">备选教师<a>全选</a></p>
			<div class="list">
				<select multiple class="kb_bx" id='stepTeaList'>
					
				</select>
				
				<script type="text/template" id="stepTeaTempl">
                	{{~it:value:index}}
						<option teaId='{{=value.userId}}'>{{=value.teaName}}</option>
                    {{~}}
                </script>
                
			</div>
		</div>
		<a class="jump tc_add step_add"><img src="/static_new/images/new33/icon_jump.png"></a>
		<div class="rt">
			<p class="top">已选教师</p>
			<div class="list">
				<ul class="kb_yx" id='stepCteaList'>
					
				</ul>
				
				<script type="text/template" id="stepCteaTempl">
                	{{~it:value:index}}
						<li teaId='{{=value.userId}}' >{{=value.teaName}}<a class="kb_remove">&times;</a></li>
                    {{~}}
                </script>
				
			</div>
		</div>
	</div>
	<div class="popup-btn">
		<button class="qd" id='stepQd'>确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>

<%--选择可排学科--%>
<div class="popup qt-xk-popup">
	<div class="popup-top">
		<em class="fl">选择可排学科</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="popup-content">
		<div class="ct">
			<p class="top">所有学科<a>全选</a></p>
			<div class="list">
				<select multiple class="xk_bx" id='xkbxList'>
				
				</select>
				
				<script type="text/template" id="xkbxTempl">
                	{{~it:value:index}}
						<option subId='{{=value.subid}}'>{{=value.snm}}</option>
                    {{~}}
                </script>
			</div>
		</div>
		<a class="jump tc_add kpxk"><img src="/static_new/images/new33/icon_jump.png"></a>
		<div class="rt">
			<p class="top">可排学科</p>
			<div class="list">
				<ul class="xk_yx" id='xkyxList'>
					
				</ul>
				
				<script type="text/template" id="xkyxTempl">
                	{{~it:value:index}}
						<li subId='{{=value.subId}}'>{{=value.subName}}<a class="xk_remove">&times;</a></li>
                    {{~}}
                </script>
			</div>
		</div>
	</div>
	<div class="popup-btn">
		<button class="qd" id='osubQd'>确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>

<%--选择可排格子--%>
<div class="popup qt-gz-popup">
	<div class="popup-top">
		<em class="fl">选择可排格子</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="qt-gz-content">
		<table class="tb gz_tb">
			<thead>
				<tr>
					<td width="60">课节/日</td>
					<td width="60">周一</td>
					<td width="60">周二</td>
					<td width="60">周三</td>
					<td width="60">周四</td>
					<td width="60">周五</td>
					<td width="60">周六</td>
					<td width="60">周日</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>第一节课</td>
					<td class="cursor gz" x='1' y='1'><span></span></td>
					<td class="cursor gz" x='2' y='1'><span></span></td>
					<td class="cursor gz" x='3' y='1'><span></span></td>
					<td class="cursor gz" x='4' y='1'><span></span></td>
					<td class="cursor gz" x='5' y='1'><span></span></td>
					<td class="cursor gz" x='6' y='1'><span></span></td>
					<td class="cursor gz" x='7' y='1'><span></span></td>
				</tr>
				<tr>
					<td>第二节课</td>
					<td class="cursor gz" x='1' y='2'><span></span></td>
					<td class="cursor gz" x='2' y='2'><span></span></td>
					<td class="cursor gz" x='3' y='2'><span></span></td>
					<td class="cursor gz" x='4' y='2'><span></span></td>
					<td class="cursor gz" x='5' y='2'><span></span></td>
					<td class="cursor gz" x='6' y='2'><span></span></td>
					<td class="cursor gz" x='7' y='2'><span></span></td>
				</tr>
				<tr>
					<td>第三节课</td>
					<td class="cursor gz" x='1' y='3'><span></span></td>
					<td class="cursor gz" x='2' y='3'><span></span></td>
					<td class="cursor gz" x='3' y='3'><span></span></td>
					<td class="cursor gz" x='4' y='3'><span></span></td>
					<td class="cursor gz" x='5' y='3'><span></span></td>
					<td class="cursor gz" x='6' y='3'><span></span></td>
					<td class="cursor gz" x='7' y='3'><span></span></td>
				</tr>
				<tr>
					<td>第四节课</td>
					<td class="cursor gz" x='1' y='4'><span></span></td>
					<td class="cursor gz" x='2' y='4'><span></span></td>
					<td class="cursor gz" x='3' y='4'><span></span></td>
					<td class="cursor gz" x='4' y='4'><span></span></td>
					<td class="cursor gz" x='5' y='4'><span></span></td>
					<td class="cursor gz" x='6' y='4'><span></span></td>
					<td class="cursor gz" x='7' y='4'><span></span></td>
				</tr>
				<tr>
					<td>第五节课</td>
					<td class="cursor gz" x='1' y='5'><span></span></td>
					<td class="cursor gz" x='2' y='5'><span></span></td>
					<td class="cursor gz" x='3' y='5'><span></span></td>
					<td class="cursor gz" x='4' y='5'><span></span></td>
					<td class="cursor gz" x='5' y='5'><span></span></td>
					<td class="cursor gz" x='6' y='5'><span></span></td>
					<td class="cursor gz" x='7' y='5'><span></span></td>
				</tr>
				<tr>
					<td>第六节课</td>
					<td class="cursor gz" x='1' y='6'><span></span></td>
					<td class="cursor gz" x='2' y='6'><span></span></td>
					<td class="cursor gz" x='3' y='6'><span></span></td>
					<td class="cursor gz" x='4' y='6'><span></span></td>
					<td class="cursor gz" x='5' y='6'><span></span></td>
					<td class="cursor gz" x='6' y='6'><span></span></td>
					<td class="cursor gz" x='7' y='6'><span></span></td>
				</tr>
				<tr>
					<td>第七节课</td>
					<td class="cursor gz" x='1' y='7'><span></span></td>
					<td class="cursor gz" x='2' y='7'><span></span></td>
					<td class="cursor gz" x='3' y='7'><span></span></td>
					<td class="cursor gz" x='4' y='7'><span></span></td>
					<td class="cursor gz" x='5' y='7'><span></span></td>
					<td class="cursor gz" x='6' y='7'><span></span></td>
					<td class="cursor gz" x='7' y='7'><span></span></td>
				</tr>
				<tr>
					<td>第八节课</td>
					<td class="cursor gz" x='1' y='8'><span></span></td>
					<td class="cursor gz" x='2' y='8'><span></span></td>
					<td class="cursor gz" x='3' y='8'><span></span></td>
					<td class="cursor gz" x='4' y='8'><span></span></td>
					<td class="cursor gz" x='5' y='8'><span></span></td>
					<td class="cursor gz" x='6' y='8'><span></span></td>
					<td class="cursor gz" x='7' y='8'><span></span></td>
				</tr>
			</tbody>
		</table>
		<div class="btn">
			<!-- <button class="xz">点击选择可排格子</button> -->
			<button>点击选择不可排格子</button>
		</div>
	</div>

	<div class="popup-btn">
		<button class="qd">确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>

<%--多功能教室数量设置--%>
<div class="popup qt-js-popup">
	<div class="popup-top">
		<em class="fl">多功能教室数量设置</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="qt-js-content">
		<div class="group">
			<span>类型：</span>
			<select><option>1</option></select>
		</div>
		<div class="group">
			<span>容量：</span>
			<select><option>1</option></select>
		</div>
		<div class="group">
			<span>数量：</span>
			<select><option>1</option></select>
		</div>
	</div>
	<div class="popup-btn">
		<button class="qd">确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>

<%--其他设置-分段设置--%>
<div class="popup qt-fd-popup">
	<div class="popup-top">
		<em class="fl">分段</em>
		<i class="fr close"></i>
	</div>
	<div class="qt-fd-content">
		<div class="top">
			分段数量：
			<select id="selFenDuan">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
			</select>
		</div>
		<table class="tb">
			<thead>
				<tr>
					<td>分段</td>
					<td>班级</td>
					<td>编辑</td>
				</tr>
			</thead>
			<tbody id="fenDuanList">
			</tbody>
			<script type="text/template" id="fenDuanList_temp">
				{{~it:value:index}}
					{{?value.clsCount>0}}
						<tr>
							<td rowspan="{{=value.clsCount}}">{{=value.fenDuanName}}</td>
							<td>{{=value.firstClass.showSlassName}}</td>
							<td><a class="fd_cs" setId="{{=value.firstClass.id}}">调整</a></td>
						</tr>
						{{~value.residueList:cls:idx}}
							<tr>
								<td>{{=cls.showSlassName}}</td>
								<td><a class="fd_cs" setId="{{=cls.id}}">调整</a></td>
							</tr>
						{{~}}
					{{??}}
						<tr>
							<td>{{=value.fenDuanName}}</td>
							<td></td>
							<td></td>
						</tr>
					{{?}}
				{{~}}
			</script>
		</table>
	</div>
</div>
<%--其他设置-分段设置-调整--%>
<div class="popup qt-fd-tz-popup">
	<div class="popup-top">
		<em class="fl">选择分段</em>
		<i class="fr close"></i>
	</div>
	<div class="qt-fd-tz-content">
		<select id="selNewFenDuan" class="fd-cs">
		</select>
	</div>
	<div class="popup-btn">
		<button class="qd">确定</button>
		<button class="qxx">取消</button>
	</div>
</div>

<%--新建互斥组--%>
<div class="popup hc-popup">
	<div class="popup-top">
		<em class="fl">新建互斥组</em>
		<i class="fr fft" onclick="popClose()"></i>
	</div>
	<div class="t-center clearfix">
		<!-- <ul id="treeDemo" class="ztree fl"></ul> -->
		<div class="teacher-list-group">
			<div id="myList-nav" class="member-select">
			</div>
			<div class="myList">
				<ul id="myList" class="member-list clearfix allTeaList">
					
				</ul>
				
				<script type="text/template" id="allTeaTempl">
                	{{~it:value:index}}
						<li class="li-special" teaId='{{=value.userId}}'>
							<img src="http://7xiclj.com1.z0.glb.clouddn.com/d_8_1.png">
							<span>{{=value.teaName}}</span>
							<em></em>
						</li>
                    {{~}}
            	</script>
				
			</div>
			<p class="hc-number">已选择：<span id="hcTeaherNum">0</span>/2名老师</p>
		</div>
	</div>
	<div class="popup-btn mt45">
		<button class="qd queDingRen">确定</button>
		<button class="qxx qss" onclick="popClose()">取消</button>
	</div>
</div>


<%--选择学科--%>
<div class="popup xk-popup xk_popup_1">
	<div class="popup-top">
		<em class="fl">选择学科</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="popup-content">
		<div class="ct">
			<p class="top">备选学科</p>
			<div class="list">
				<select multiple class="xk_bx_1" id="subject1">
				</select>
				<script type="text/template" id="subject_temp1">
				{{~it:value:index}}
				<option subId="{{=value.subid}}">{{=value.snm}}</option>
				{{~}}
				</script>
			</div>
		</div>
		<a class="jump xk_add_1" id="subject"><img src="/static_new/images/new33/icon_jump.png"></a>
		<div class="rt">
			<p class="top">已选学科</p>
			<div class="list">
				<ul class="xk_yx_1 select-sub1">
					<%--<li>123<a class="xk_remove_1">&times;</a></li>--%>
				</ul>
			</div>
		</div>
	</div>
	<div class="popup-btn">
		<button class="qd" id="qdsubject1">确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>


<%--选择学科--%>
<div class="popup xk-popup xk_popup_2">
	<div class="popup-top">
		<em class="fl">选择学科</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="popup-content">
		<div class="ct">
			<p class="top">备选学科</p>
			<div class="list">
				<select multiple class="xk_bx_2" id="subject2">

				</select>
                <script type="text/template" id="subject_temp2">
                    {{~it:value:index}}
                    <option subId="{{=value.subid}}">{{=value.snm}}</option>
                    {{~}}
                </script>
			</div>
		</div>
		<a class="jump xk_add_2"><img src="/static_new/images/new33/icon_jump.png"></a>
		<div class="rt">
			<p class="top">已选学科</p>
			<div class="list">
				<ul class="xk_yx_2 select-sub1">

				</ul>
			</div>
		</div>
	</div>
	<div class="popup-btn">
		<button class="qd" id="qdsubject2">确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>

<%--选择学科--%>
<div class="popup xk-popup xk_popup_3 p3">
	<div class="popup-top">
		<em class="fl">选择学科</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="popup-content">
		<div class="xk-date">
			<em>上午课时</em>
			<select id="amCount">
                <option>1</option>
                <option>2</option>
                <option>3</option>
                <option>4</option>
                <option>5</option>
            </select>
			<em>下午课时</em>
			<select id="pmCount">
                <option>1</option>
                <option>2</option>
                <option>3</option>
                <option>4</option>
                <option>5</option>
            </select>
		</div>

		<div class="ct">
			<p class="top">备选学科</p>
			<div class="list">
				<select multiple class="xk_bx_3" id="subject3">

				</select>
                <script type="text/template" id="subject_temp3">
                    {{~it:value:index}}
                    <option subId="{{=value.subid}}">{{=value.snm}}</option>
                    {{~}}
                </script>
			</div>
		</div>
		<a class="jump xk_add_3"><img src="/static_new/images/new33/icon_jump.png"></a>
		<div class="rt">
			<p class="top">已选学科</p>
			<div class="list">
				<ul class="xk_yx_3 select-sub1">
					<%--<li>123<a class="xk_remove_3">&times;</a></li>--%>
				</ul>
			</div>
		</div>
	</div>
	<div class="popup-btn">
		<button class="qd" id="qdsubject3">确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>

<%--学科互斥--%>
<div class="popup xk-popup xk_popup_4">
	<div class="popup-top">
		<em class="fl">新建学科互斥组</em>
		<i class="fr" onclick="popClose()"></i>
	</div>
	<div class="popup-content">
		<div class="ct">
			<p class="top">备选学科</p>
			<div class="list">
				<select multiple class="xk_bx_4" id="subject4">

				</select>
                <script type="text/template" id="subject_temp4">
                    {{~it:value:index}}
                    <option subId="{{=value.subid}}">{{=value.snm}}</option>
                    {{~}}
                </script>
			</div>
		</div>
		<a class="jump xk_add_4"><img src="/static_new/images/new33/icon_jump.png"></a>
		<div class="rt">
			<p class="top">互斥学科</p>
			<div class="list">
				<ul class="xk_yx_4 select-sub1">
					<%--<li>123<a class="xk_remove_4">&times;</a></li>--%>
				</ul>
			</div>
		</div>
	</div>
	<div class="popup-btn">
		<button class="qd" id="qdsubject4">确定</button>
		<button class="qxx" onclick="popClose()">取消</button>
	</div>
</div>


<script type="text/javascript">

	//排课规则  1
	$('.open_xk_1').click(function () {
	    $('.bg').show();
		$('.xk_popup_1').show()
    })
    $('.xk_add_1').click(function () {
        $.each($(".xk_bx_1 option:selected"),function (i,item) {
            var flag = true;
            $('.select-sub1 li').each(function (o,st) {
				if($(item).attr('subId') == $(st).attr("subId")){
                    flag = false;
				}
            });
            if(flag){
                $('.xk_yx_1').append('<li subId=' + $(item).attr('subId') + '>'+$(item).text()+'<a class="xk_remove_1">&times;</a></li>')
			}else{
                layer.alert("学科不可重复加入");
			}
        })
    })
    $(document).on('click','.xk_remove_1',function (e) {
        $(this).parent().remove()
    });

    //排课规则  2
    $('.open_xk_2').click(function () {
        $('.bg').show();
        $('.xk_popup_2').show()
    })
    $('.xk_add_2').click(function () {
        $.each($(".xk_bx_2 option:selected"),function (i,item) {
            var flag = true;
            $('.select-sub1 li').each(function (o,st) {
                if($(item).attr('subId') == $(st).attr("subId")){
                    flag = false;
                }
            });
            if(flag){
                $('.xk_yx_2').append('<li subId=' + $(item).attr('subId') + '>'+$(item).text()+'<a class="xk_remove_2">&times;</a></li>')
            }else{
                layer.alert("学科不可重复加入");
            }
        })
    })
    $(document).on('click','.xk_remove_2',function (e) {
        $(this).parent().remove()
    });

    //排课规则  3
    $('.open_xk_3').click(function () {
        $('.bg').show();
        $('.xk_popup_3').show()
    })
    $('.xk_add_3').click(function () {
        $.each($(".xk_bx_3 option:selected"),function (i,item) {
            var flag = true;
            $('.select-sub1 li').each(function (o,st) {
                if($(item).attr('subId') == $(st).attr("subId")){
                    flag = false;
                }
            });
            if(flag){
                $('.xk_yx_3').append('<li subId=' + $(item).attr('subId') + '>'+$(item).text()+'<a class="xk_remove_3">&times;</a></li>')
            }else{
                layer.alert("学科不可重复加入");
            }
        })
    })
    $(document).on('click','.xk_remove_3',function (e) {
        $(this).parent().remove()
    });

    //新建互斥学科组
    $('#new_xk_hc').click(function () {
        $('.bg').show();
        $('.xk_popup_4').show()
    })
    $('.xk_add_4').click(function () {
        $.each($(".xk_bx_4 option:selected"),function (i,item) {
            var flag = true;
            $('.select-sub1 li').each(function (o,st) {
                if($(item).attr('subId') == $(st).attr("subId")){
                    flag = false;
                }
            });
            if(flag){
                if($('.select-sub1 li').length >= 2){
                    layer.alert("学科数量不能大于2科");
                }else{
                    $('.xk_yx_4').append('<li subId=' + $(item).attr('subId') + '>'+$(item).text()+'<a class="xk_remove_4">&times;</a></li>')
                }
            }else{
                layer.alert("学科不可重复加入");
            }
        })
    })
    $(document).on('click','.xk_remove_4',function (e) {
        $(this).parent().remove()
    });




    //排课规则
    $('.gz_list li').click(function () {
        var index=$(this).index()+1;
        $('.gz_ct_'+index).show().siblings('.gz_ct').hide();
        $(this).addClass('active').siblings().removeClass('active')
    });

    $(document).on('click','.ck_xk li',function () {
        $(this).addClass('active').siblings().removeClass('active')
    });

    // $(document).on('click','.xk-list .ck',function (e) {
    //     e.stopPropagation();
    //     $(this).toggleClass('active');
    // });
    $(document).on('click','.xk-list .ck',function (e) {
        e.stopPropagation();
        $(this).toggleClass('active');
    });

	//上下午课时分配
    $('.fp_ck').click(function () {
        var m=$(this).hasClass('active');
        var other=$(this).siblings('.fp-set');
        if(m){
            other.hide()
        }else{
            other.show()
        }
    });

    //学科互斥（不在同一天上课）
    $('.hc_ck').click(function () {
        var m=$(this).hasClass('active');
        var other=$(this).siblings('.hc-set');
        if(m){
            other.hide()
        }else{
            other.show()
        }
    });
    //---打开学科互斥表格
    $('.open_xk_hc').click(function () {
        $('.xk_group').hide();
        $('.xk_hc_group').show()
    })
    //---关闭教师互斥表格
    $('#back_xk_hc').click(function () {
        $('.xk_group').show();
        $('.xk_hc_group').hide()
    });

    //走班教室上下浮动人数
    $('.zb_fd_ck').click(function () {
        var m=$(this).hasClass('active');
        var other=$(this).siblings('.zb-fd-set');
        if(m){
            other.hide()
        }else{
            other.show()
        }
    });
    //走班教室上限数量
    $('.zb_sx_ck').click(function () {
        var m=$(this).hasClass('active');
        var other=$(this).siblings('.zb-sx-set');
        if(m){
            other.hide()
        }else{
            other.show()
        }
    })
	//教学班人数上限
    $('.zb_jxb_ck').click(function () {
        var m=$(this).hasClass('active');
        var other=$(this).siblings('.zb-jxb-set');
        if(m){
            other.hide()
        }else{
            other.show()
        }
    })

    //学科全选和取消
    $('.ck_xk_all').click(function () {
        var tp=1;
        if($(this).text()=='取消'){
            $(this).text('全选');
            tp=2;
        }else{
            $(this).text('取消');
            tp=1;
        }
        $.each($('.ck_xk .ck'),function (i,item) {
            if(tp==1){
                $(item).addClass('active')
            }else{
                $(item).removeClass('active')
            }
        })
    })

	
    	
		
    	
		

    

    
    
    //--弹窗学科选择
    $(document).on('click','.kb_xk li',function (e) {
        $(this).toggleClass('active');
    });
    

    

    
	
	


	
    
    


    
    
    


	



	//-----------------教师日最大连课节数
    
    //--弹窗学科选择
    $(document).on('click','.lk_xk li',function (e) {
        $(this).toggleClass('active');
    });
    /* //--弹窗教师选择
    $('.lk_add').click(function () {
        $.each($(".lk_bx option:selected"),function (i,item) {
            $('.lk_yx').append('<li>'+$(item).text()+'<a class="lk_remove">&times;</a></li>')
        })
    })
    //--弹窗删除已选教师
    $(document).on('click','.lk_remove',function (e) {
        $(this).parent().remove()
    }); */
    //-------------------------------------//



    //新建互斥组--选择教师

    /* $('#myList').listnav({
        includeOther: true,
        noMatchText: '',
        prefixes: ['the', 'a']
    });
    $("#myList-nav").append(" <div class='demo-search'> <input type='text'id='name'> <button id='sou'>搜索</button> </div>") */
/*     var zNodes = [
        {name:"高一", open:true, children:[
                {name:"语文"}, {name:"数学"}]},
    ];
    var setting = {
        callback : {
            onClick : zTreeOnClick
        }
	};
    function zTreeOnClick(event, treeId, treeNode) {
        console.log(treeNode.tId + ", " + treeNode.name);
    }
    $.fn.zTree.init($("#treeDemo"),setting, zNodes);
 */
    //弹窗关闭
    function popClose(){
        $('.popup').hide();
        $('.bg').hide();
    }

</script>
</body>
</html>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('pkRuleSet', function (pkRuleSet) {
    	pkRuleSet.init();
    });
</script>
