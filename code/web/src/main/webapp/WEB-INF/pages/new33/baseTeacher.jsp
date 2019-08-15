    <%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/3/8
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>教师设置</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $('.t-down').click(function(){
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top',-182)
            })
            $('.t-top').click(function(){
                $('.td-over').css('margin-top',0)
            })
        	$(".right-pos a").eq(4).attr("class","vd2");
            $('.wind .d1 em,.wind .dbtn .btn-no').click(function () {
                $('.wind-new-tea,.bg').fadeOut();
            })
            $('.cou-x em').click(function () {
                $('.reset').show();
            });
            $('.d-sty em').click(function () {
                $(this).addClass('cur').siblings().removeClass('cur');
            });

            $('.qx').click(function () {
                $('.ciee-popup').hide();
                $('.bg').hide();
            });

            $('.close').click(function () {
                $('.ciee-popup').hide();
                $('.bg').hide();
            });
        })
    </script>
    <style type="text/css">
        .right-pos {
            top: -140px;
        }
    </style>
</head>
    <body>
<div>
    <jsp:include page="head.jsp"></jsp:include>
    <%@ include file="/WEB-INF/pages/new33/BasicDatasideBar.jsp" %>
    <div class="center w1300 clearfix">
        <div class="ul-sty center mt25 fl">
            <ul class="bg-line">
                <%--<li class="li-sty">--%>
                    <%--<span class="fl">学期 :</span>--%>
                    <%--<script type="text/template" id="term_temp">--%>
                        <%--{{~it:value:index}}--%>
                        <%--<em ids="{{=value.ciId}}" termId="{{=value.termId}}">{{=value.ciname}}</em>--%>
                        <%--{{~}}--%>
                    <%--</script>--%>
                    <%--<div class="d-sty fl" id="term">--%>
                    <%--</div>--%>
                <%--</li>--%>
                <li class="li-sty">
                    <span class="fl">年级 :</span>
                    <script type="text/template" id="grade_temp">
                        {{~it:value:index}}
                        <em ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</em>
                        {{~}}
                    </script>
                    <script type="text/template" id="subject_temp">
                        {{~it:value:index}}
                        <em ids="{{=value.subid}}">{{=value.snm}}</em>
                        {{~}}
                    </script>
                    <div class="d-sty fl" id="grade">
                    </div>
                </li>
                <li class="li-sty">
                    <span class="fl">学科 : </span>

                    <div class="d-sty fl" id="subject">

                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="center w1300 clearfix">
        <div class="mt35 center fl"><span class="fl" style="line-height: 42px;">教师设置</span>
            <div class="pos fl ml20">
                <input class="p-inp inp10" type="text" placeholder="请输入老师名称" id="teaName">
                <button id="sousuo">搜索</button>
            </div>
            <button class="new-tea">新增教师</button>
            <button class="sel-tea">选择教师</button>
            <button class="ks-tea">教师课时</button>
        </div>
    </div>
    <div class="center w1300 clearfix">
        <div class="mt20 cur-tab cou-tab center fl">
            <div class='center w1200 clearfix' id="none_png" hidden>
                <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
            </div>
            <table style="width: 1200px" class="votab" id="content" hidden>
                <thead>
                <tr>
                    <th width="20%">教师姓名</th>
                    <th width="10%">性别</th>
                    <th width="10%">年级</th>
                    <th width="10%">学科</th>
                    <th width="10%">是否是班主任</th>
                    <th width="20%" hidden>本年级带班数量/总带班数量</th>
                    <th width="10%"hidden>课时</th>
                    <th width="10%">操作</th>
                </tr>
                </thead>
                <script type="text/template" id="tbdList">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.unm}}</td>
                        <td>{{=value.sexStr}}</td>
                        <td>{{=value.gradeStr}}</td>
                        <td>{{=value.subjectStr}}</td>
                        <td>{{=value.isBanName}}</td>
                        <td hidden>{{=value.gradeJXBCount}}/{{=value.jxbCount}}</td>
                        <td hidden>{{=value.ks}}</td>
                        <td><img class="opt" src="/static_new/images/new33/edit.png" ids="{{=value.id}}"><img class="opts" src="/static_new/images/new33/dell.png" ids="{{=value.id}}">
                        </td>
                    </tr>
                    {{~}}
                </script>
                <tbody id="tbd">

                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="wind wind-new-tea">
    <div class="d1">新增/编辑教师<em>×</em></div>
    <ul>
        <li>
            <span>教师姓名</span>
            <input type="text" id="name">
        </li>
        <li>
            <span>教师性别</span>
            <select id="sex">
                <option value="1">男</option>
                <option value="0">女</option>
            </select>
        </li>
        <li>
            <span>年级</span>
            <span style="display: inline-block;width: 288px;text-align: left;" id="checkedGra"></span>
        </li>
        <script type="text/template" id="gradeCheck">
            {{~it:value:index}}
            <label><input type="checkbox" gid="{{=value.gid}}" class="ck">{{=value.gnm}}</label>
            {{~}}
        </script>
        <script type="text/template" id="subjectOpt">
            {{~it:value:index}}
            <option value="{{=value.subid}}">{{=value.snm}}</option>
            {{~}}
        </script>
        <li>
            <span>学科</span>
            <select id="subjectList" multiple>
            </select>
        </li>
        <li>
            <span>班主任</span>
            <select id="banZhuRen">
                <option value="0">不是</option>
                <option value="1">是</option>
            </select>
        </li>
        <li hidden>
            <span hidden>带班数量</span>
            <input type="text" id="classNum" hidden value="0">
        </li>
    </ul>
    <div class="dbtn">
        <button class="btn-ok" uid="*" ids="*">确认</button>
        <button class="btn-no">取消</button>
    </div>
</div>
<div class="bg"></div>
</body>
<div class="popup cie-popup">
    <div class="popup-top">
        <em class="fl">选择教师</em>
        <i class="fr"></i>
    </div>
    <div class="ml40">
        <div class="mt10">
            <span class="w160 c-b">学科</span>
            <span class="w160 c-b ml50">备选教师</span>
            <span class="w160 c-b ml63">已选教师</span>
        </div>
        <div class="mt10">
            <div class="w160 h245 bor-line auto inline xz-li">
                <ul id="subList">

                </ul>
                <script type="text/template" id="subLi">
                    {{~it:value:index}}
                    <li  ids="{{=value.subid}}">{{=value.snm}}</li>
                    {{~}}
                </script>
            </div>
            <div class="w160 h245 bor-line auto inline xz-li ml50">
                <script type="text/template" id="userList">
                    {{~it:value:index}}
                    <li><span class="whSp" title="{{=value.unm}} &nbsp;{{=value.subjectName}}">{{=value.unm}} &nbsp;{{=value.subjectName}}</span><i  sex="{{=value.sex}}" subjectStr="{{=value.subjectStr}}" ids="{{=value.id}}" isBanZhuRen="{{=value.isBanZhuRen}}" classNum="{{=value.classNum}}" uid="{{=value.uid}}" unm="{{=value.unm}}" sub="{{=value.sub}}"class="fft ad-red_x"></i></li>
                    {{~}}
                </script>
                <script type="text/template" id="userLists">
                    {{~it:value:index}}
                    <li><span class="whSp" title="{{=value.unm}}&nbsp;{{=value.subjectName}}">{{=value.unm}}&nbsp;{{=value.subjectName}}</span><i  sex="{{=value.sex}}" subjectStr="{{=value.subjectName}}" ids="{{=value.id}}" isBanZhuRen="0" classNum="0" uid="{{=value.uid}}" unm="{{=value.unm}}" sub="{{=value.sub}}" class="fft"></i></li>
                    {{~}}
                </script>
                <ul id="useSub">
                </ul>
            </div>
            <img class="inline v-top" src="/static_new/images/new33/jt.png">
            <div class="w160 h245 bor-line auto inline xz-li">
                <ul id="yiXuan">

                </ul>
            </div>
        </div>


    </div>
    <div class="popup-btn mt20">
        <button class="qd">确认</button>
        <button class="qx">取消</button>
    </div>
</div>
<div class="popup ciee-popup" style="display: none">
    <div class="popup-top">
        <em class="fl">教师课时（<label id="gradeName"></label><label id="subjectName"></label>）</em>
        <i class="fr close"></i>
    </div>
    <div class="ml40">
        <div class="clearfix">
            <div class="pos fl mt15 ml415">
                <input class="p-inp inp10" type="text" placeholder="请输入老师姓名" id="teaKsName">
                <button class="sousuo" id="teaKs">搜索</button>
            </div>
        </div>
        <div class="mt55">
            <table class="cou-tab">
                <thead>
                    <tr>
                        <th width="100">教师姓名</th>
                        <th width="80">性别</th>
                        <th width="120">年级</th>
                        <th width="280">学科</th>
                        <th width="80">总课时</th>
                    </tr>
                </thead>
            </table>
            <div class="oh2">
                <table class="cou-tab">
                    <script type="text/template" id="ksList_temp">
                        {{~it:value:index}}
                        <tr>
                            <td width="100">{{=value.unm}}</td>
                            <td width="80">{{=value.sexStr}}</td>
                            <td width="120">{{=value.gradeStr}}</td>
                            <td width="280">{{=value.subjectStr}}</td>
                            <td width="80">{{=value.ks}}</td>
                        </tr>
                        {{~}}
                    </script>
                    <tbody id="ksList">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="popup-btn mt20" style="margin-bottom: 15px;">
        <button class="qx">取消</button>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('baseTeacher', function (baseTeacher) {
        baseTeacher.init();
    });
</script>
</html>
