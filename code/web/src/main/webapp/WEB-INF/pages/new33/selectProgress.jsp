<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%--<title class="i18n" name='title'>新高考3+3走班排课系统</title>--%>
    <title class="i18n" name='title'>选科进度</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
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
        <a href="/newisolatepage/selectProgress.do" class="xk1">选科进度</a>
		<a href="/newisolatepage/selectResult.do" class="b2">选科结果</a>
		<a href="/newisolatepage/selectClassresult.do" class="d2">班级结果</a>
        <a href="/newisolatepage/compared.do" class="db2">选科结果对比</a>
	</div>
	<div class="center w1300 clearfix att">
		<div class="ul-sty center mt25 fl">
			<ul class="bg-line">
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
			</ul>
		</div>
	</div>
	<div class="center w1300">
        <div class="sul1">
            <div class="center fl mt20">
                <button class="impor-bt btn fr f16" id="export">导出</button>
                <button class="chat-00 btn3 fr mr20">未选科名单</button>
            </div>
            <div class="center fl mt20" id="select_progress">

            </div>
            <script type="text/template" id="select_progress_temp">
                {{~it:value:index}}
                <div class="sult ml30">
                    <span>{{=value.name}}</span>
                    <div class="inline w1000 {{=value.color}}">
                        <div class="nw tr" style="width:{{=value.width}}px">
                            <em>{{=value.ratio}}%</em>
                        </div>
                    </div>
                </div>
                {{~}}
            </script>
        </div>
        <div class="sul2">
            <div class="center fl">
                <a class="sula">
                    <em class="inline prev">&lt;返回</em>--选科人员名单
                </a>
                <select class="sel1 fr mt14" id="klass">

                </select>
                <script type="text/template" id="klass_temp">
                    {{~it:value:index}}
                    <option value="{{=value.classId}}">{{=value.className}}</option>
                    {{~}}
                </script>
                <div class="inline pos mt20 mb10 fr">
                    <input class="inp10" type="text" placeholder="请输入学生姓名" id="stuName">
                    <i class="search at" id="sosu"></i>
                </div>
            </div>
            <div class="mt20 cou-tab center clearfix fl">
                <table>
                    <thead>
                        <tr>
                            <th class="w260">学生姓名</th>
                            <th class="w260">班级</th>
                            <th class="w260">学号</th>
                            <th class="w260">层级</th>
                            <th class="w260">性别</th>
                            <th class="w260">操作</th>
                        </tr>
                    </thead>
                    <tbody id="student">

                    </tbody>
                    <script type="text/template" id="student_temp">
                                 {{~it:value:index}}
                                        <tr>
                                            <td>{{=value.nm}}</td>
                                            <td>{{=value.bj}}</td>
                                            <td>{{=value.sn}}</td>
                                            <td>{{=value.lev}}</td>
                                            <td>{{=value.sex}}</td>
                                            <td>
                                                {{=value.cbn}}<img class="inline opt gg imghide" src="/static_new/images/new33/edit.png" ids="{{=value.id}}">
                                            </td>
                                        </tr>
                                 {{~}}
                        </script>
                </table>
            </div>
        </div>
	</div>
</div>
<div class="bg"></div>
<!--更改选科-->
<div class="popup gg-popup">
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
    </dl>
    <div class="popup-btn mt25">
        <button class="qd">确定</button>
        <button class="qx">取消</button>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('selectProgress', function (selectProgress) {
      selectProgress.init();
  });
</script>
</body>
</html>
