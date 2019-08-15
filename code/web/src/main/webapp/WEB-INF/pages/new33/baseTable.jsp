<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>课表设置</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/rome.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <style type="text/css">
    	
		.rd-container{
			width: 102px !important;
		}
		.right-pos{
			top: -140px;
		}
    </style>
    <script type="text/javascript">
        $(function(){
            $('.t-down').click(function(){
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top',-182)
            })
            $('.t-top').click(function(){
                $('.td-over').css('margin-top',0)
            })
            $('.set-ul li').click(function(){
                $(this).addClass('ss-cur').siblings().removeClass('ss-cur');
                var tt = $(this).attr('id');
                $('#' + "tab-" + tt).show().siblings().hide();
            })

        })

    </script>
</head>
<body>
<div>
	<jsp:include page="head.jsp"></jsp:include>
	<%@ include file="/WEB-INF/pages/new33/BasicDatasideBar.jsp" %>

    <div class="center w1300 clearfix">
        <div class="mt35 center clearfix fl">
            <ul class="set-ul c-b">
                <li class="f16 ss-cur" id="kb">课表设置</li>
                <li class="f16" id="jx">走班时间</li>
                <li class="f16" id="gd">固定事项</li>
                <%--<li class="f16" id="sw">课表事务</li>--%>
            </ul>
        </div>
    </div>
    <div class="comm">
        <div id="tab-kb">
            <div class="center w1300 clearfix">
                <div class="optcont" style="margin-top: 20px;">
                    <span class="mr30" hidden>学期：</span>
                    <select id="term" hidden>

                    </select>
                    <label class="week">
                        <input type="checkbox" js='six' class="six">周六
                    </label>
                    <label class="week">
                        <input type="checkbox" js='seven' class="seven">周日
                    </label>
                    <script type="text/template" id="term_temp">
                        {{~it:value:index}}
                            <option value="{{=value.ciId}}">{{=value.ciname}}</option>
                        {{~}}
                    </script>
                    <button class="btn-xzkj" id="sync_last" hidden>同步上学期</button>
                    <button class="btn-xzkj xzkj">新增课节</button>
                    <button class="btn-qjsz">区间设置</button>
                </div>
            </div>
            <div class="center w1300 clearfix">
                <div class="mt20 cur-tab cou-tab center fl">
                    <div class='center w1200 clearfix' id="none_png" hidden>
                        <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
                    </div>
                    <table style="width: 1200px" class="votab weektab" id="content" hidden>
                        <thead>
                            <tr>
                                <th width="20%">序号</th>
                                <th width="20%">课节</th>
                                <th width="20%">时间段</th>
                                <%--<th width="20%">附加时间段</th>--%>
                                <th width="20%">区间</th>
                                <th width="20%">操作</th>
                            </tr>
                        </thead>
                        <tbody id="course_list">

                        </tbody>
                        <script type="text/template" id="course_list_temp">
                        {{~it:value:index}}
                        <tr>
                            <td>{{=value.serial}}</td>
                            <td>{{=value.name}}</td>
                            <td>{{=value.start}} - {{=value.end}}</td>
                            <%--<td><a class="set">设置</a></td>--%>
                            <td>{{=value.range}}</td>
                            <td>
                                <img class="opt edit" src="/static_new/images/new33/editt.png" eid="{{=value.id}}"
                                     ename="{{=value.name}}" serial="{{=value.serial}}" start="{{=value.start}}"
                                     end="{{=value.end}}">
                                <img class="opt del" src="/static_new/images/new33/dell.png" eid="{{=value.id}}">
                            </td>
                        </tr>
                        {{~}}
                    </script>
                    </table>
                </div>
            </div>
        </div>

        <div id="tab-jx">
            <div class="optcont">
                <span hidden>学期</span>
                <select hidden id="term2">

                </select>
                <span>年级</span>
                <script type="text/template" id="grade_temp">
                    {{~it:value:index}}
                    <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
                    {{~}}
                </script>
                <select id="grade">
                </select>
            </div>
            <div class="center w1300 clearfix">
                <div class="mt20 cur-tab cou-tab center fl baseSty">
                    <table style="width: 1200px">
                        <thead>
                        <tr>
                            <th width="9%">课节 / 日</th>
                            <th width="13%">周一</th>
                            <th width="13%">周二</th>
                            <th width="13%">周三</th>
                            <th width="13%">周四</th>
                            <th width="13%">周五</th>
                            <th width="13%">周六</th>
                            <th width="13%">周日</th>
                        </tr>
                        </thead>

                        <script type="text/template" id="KeShiList">
                            <%--<div>--%>
                            <%--<p>--%>
                            <%--<em>语文</em>--%>
                            <%--<em>(45)</em>--%>
                            <%--</p>--%>
                            <%--<p>高三</p>--%>
                            <%--</div>--%>
                            {{~it:value:index}}
                                <tr>
                                    <td class="psre ke">
                                        <div>{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></div>
                                    </td>
                                    <td>
                                        <input type="checkbox" class="itt">
                                    </td>
                                    <td>
                                        <input type="checkbox" class="itt">
                                    </td>
                                    <td>
                                        <input type="checkbox" class="itt">
                                    </td>
                                    <td>
                                        <input type="checkbox" class="itt">
                                    </td>
                                    <td>
                                        <input type="checkbox" class="itt">
                                    </td>
                                    <td>
                                        <input type="checkbox" class="itt">
                                    </td>
                                    <td>
                                        <input type="checkbox" class="itt">
                                    </td>
                                </tr>
                            {{~}}
                        </script>

                        <tbody class="t-tbo" id="tbd">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>


        <div id="tab-gd">
            <div class="optcont">
                <div class="fl">
                    <span>年级</span>
                    <select id="gdgrade">

                    </select>
                    <script type="text/template" id="gdgrade_temp">
                        {{~it:value:index}}
                        <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
                        {{~}}
                    </script>
                </div>
                <button class="btn11 fr" class="fr">编辑</button>
            </div>
            <div class="center w1300 clearfix">
                <div class="mt20 cur-tab cou-tab center fl baseSty">
                    <table style="width: 1200px">
                        <thead>
                        <tr>
                            <th width="9%">课节 / 日</th>
                            <th width="13%">周一</th>
                            <th width="13%">周二</th>
                            <th width="13%">周三</th>
                            <th width="13%">周四</th>
                            <th width="13%">周五</th>
                            <th width="13%">周六</th>
                            <th width="13%">周日</th>
                        </tr>
                        </thead>
                        <script type="text/template" id="gdThing_temp">
                            <%--<div>--%>
                            <%--<p>--%>
                            <%--<em>语文</em>--%>
                            <%--<em>(45)</em>--%>
                            <%--</p>--%>
                            <%--<p>高三</p>--%>
                            <%--</div>--%>
                            {{~it:value:index}}
                            <tr>
                                <td class="psre ke">
                                    <div>{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></div>
                                </td>
                                <td <%--class="tEdit"--%>>
                                    <input readonly="true" type="type" class="tp">
                                </td>
                                <td>
                                    <input readonly="true" type="type" class="tp">
                                </td>
                                <td>
                                    <input readonly="true" type="type" class="tp">
                                </td>
                                <td>
                                    <input readonly="true" type="type" class="tp">
                                </td>
                                <td>
                                    <input readonly="true" type="type" class="tp">
                                </td>
                                <td>
                                    <input readonly="true" type="type" class="tp">
                                </td>
                                <td>
                                    <input readonly="true" type="type" class="tp">
                                </td>
                            </tr>
                            {{~}}
                        </script>
                        <tbody class="t-tbo" id="gdThing">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>


        <div id="tab-sw">
            <div class="center w1300 clearfix">
                <div class="optcont" style="margin-top: 20px;">
                    <span class="mr30" hidden>学期：</span>
                    <select>
                        <option>全校</option>
                    </select>
                    <button class="btn-xzkj xzkj">清空所有事件</button>
                </div>
            </div>
            <div class="center w1300 clearfix">
                <div class="mt20 cur-tab cou-tab center fl baseSty">
                    <table style="width: 1200px">
                        <thead>
                        <tr>
                            <th width="9%">课节 / 日</th>
                            <th width="13%">周一</th>
                            <th width="13%">周二</th>
                            <th width="13%">周三</th>
                            <th width="13%">周四</th>
                            <th width="13%">周五</th>
                            <th width="13%">周六</th>
                            <th width="13%">周日</th>
                        </tr>
                        </thead>
                        <tbody class="t-tbo">
                            <tr>
                                <td class="psre ke">
                                    <div></div>
                                </td>
                                <td>
                                    <select class="sel11">
                                        <option></option>
                                    </select>
                                </td>
                                <td>
                                    <select class="sel11">
                                        <option></option>
                                    </select>
                                </td>
                                <td>
                                    <select class="sel11">
                                        <option></option>
                                    </select>
                                </td>
                                <td>
                                    <select class="sel11">
                                        <option></option>
                                    </select>
                                </td>
                                <td>
                                    <select class="sel11">
                                        <option></option>
                                    </select>
                                </td>
                                <td>
                                    <select class="sel11">
                                        <option></option>
                                    </select>
                                </td>
                                <td>
                                    <select class="sel11">
                                        <option></option>
                                    </select>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
	<div class="wind wind-qjsz" id="day_range">
		<div class="d1">课表区间设置<em>×</em></div>
		<ul>
			<li>
				上午:
				<input class="tm" type="text" id="dt1">
				<em>-</em>
				<input class="tm" type="text" id="dt2">
			</li>
			<li>
				下午:
				<input class="tm" type="text" id="dt3">
				<em>-</em>
				<input class="tm" type="text" id="dt4">
			</li>
			<li>
				晚上:
				<input class="tm" type="text" id="dt5">
				<em>-</em>
				<input class="tm" type="text" id="dt6">
			</li>
		</ul>
		<div class="dbtn">
			<button class="btn-ok" id="dayrange_save">确认</button>
			<button class="btn-no">取消</button>
		</div>
	</div>

	<div class="wind wind-xzkj" id="course_range">
		<div class="d1">保存课节<em>×</em></div>
		<ul>
			<li>
				<span>序号</span>
				<input class="inp1" type="text" id="course_serial" onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="请输入数字">
			</li>
			<li>
				<span>课节名称</span>
				<input class="inp1" type="text" id="course_name">
			</li>
			<li>
				<span>时间段</span>
				<input class="inp2" type="text"  id="course_start">
				<em>-</em>
				<input class="inp2" type="text"  id="course_end">
			</li>
		</ul>
		<div class="dbtn">
			<button class="btn-ok" id="course_save">确认</button>
			<button class="btn-no">取消</button>
		</div>
	</div>
	<div class="bg"></div>

    <div class="popup kj-popup">
        <div class="popup-top">
            <em class="fl">课节附加信息</em>
            <i class="fr" onclick="closeKjPopup()"></i>
        </div>
        <div class="kj-gp">
            <div class="ck-gp">
                <label><input type="checkbox" />高一</label>
                <label><input type="checkbox" />高二</label>
                <label><input type="checkbox" />高三</label>
            </div>
            <div class="ck-gp">
                <label><input type="checkbox" />周一</label>
                <label><input type="checkbox" />周二</label>
                <label><input type="checkbox" />周三</label>
                <label><input type="checkbox" />周四</label>
                <label><input type="checkbox" />周五</label>
                <label><input type="checkbox" />周六</label>
                <label><input type="checkbox" />周日</label>
            </div>
            <div class="input-gp">开始时间<input type="text" /></div>
            <div class="input-gp">结束时间<input type="text" /></div>
            <div class="btn-gp">
                <a class="qd">确定</a><a class="qx">取消</a>
            </div>
        </div>

    </div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
  $(".right-pos a").eq(1).attr("class","va2");
  seajs.use('baseTable', function (baseTable) {
	  baseTable.init();
  });

  //附加信息
  $(document).on('click','.set',function () {
      $('.kj-popup').show();
      $('.bg').show()
  });
  function closeKjPopup(){
      $('.kj-popup').hide();
      $('.bg').hide()
  }
</script>
</html>
