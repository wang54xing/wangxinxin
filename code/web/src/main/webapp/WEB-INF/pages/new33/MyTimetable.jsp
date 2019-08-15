<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/7/31
  Time: 14:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>3+3</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/resetD.css">
    <%--<link rel="stylesheet" href="/static_new/css/new33/course.css">--%>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <style>
        .top-nav>.a5 {
            background: url(/static_new/images/new33/nav5.png) no-repeat center 20px #4740d0;
        }
    </style>
    <script>
        $(function(){
            $('.wucx').click(function(){
                $('.bg').show();
                $('.z-popup').show();
            })
            $('.close,.qx').click(function(){
                $('.bg').hide();
                $('.wu-popup').hide();
                $('.you-popup').hide();
                $(".z-popup").hide();
            })

        })
    </script>
    <style>
        .ban{
            cursor: pointer
        }
    </style>
</head>

<body>
<div>
    <jsp:include page="head.jsp"></jsp:include>
    <span id="defaultTerm" style="display: none"></span>

    <form id="export_form"></form>
    <div class="clearfix center">
        <div class="fl mt30 c00 f18">
            <span id="unm">${sessionValue.userName}</span>
            <span id="keCount"></span>
        </div>
        <div class="fr mt30">
            <script type="text/template" id="week_temp">
                {{~it:value:index}}
                <option value="{{=value.serial}}">{{=value.numberWeek}}{{=value.start}}</option>
                {{~}}
            </script>
            <select class="sel1"  id="week">
                <option></option>
            </select>
            <button class="btn1 ml5" id="printGDG">打印</button>
            <button class="btn2 ml5 b9">导出</button>
            <button class="btn3 ml5">教师无课查询</button>
        </div>
    </div>
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
            <td class="itt">
            </td>
            <td  class="itt">
            </td>
            <td  class="itt">
            </td>
            <td  class="itt">
            </td>
            <td  class="itt">
            </td>
            <td  class="itt">
            </td>
            <td  class="itt">
            </td>
        </tr>
        {{~}}
    </script>
    <div class="center" id="GDGContent">
        <table class="tabSty mt20 mb35">
            <thead>
            <tr>
                <th>
                    <div class="wd100">课节 / 日</div>
                </th>
                <th>
                    <div class="w163">周一</div>
                </th>
                <th>
                    <div class="w163">周二</div>
                </th>
                <th>
                    <div class="w163">周三</div>
                </th>
                <th>
                    <div class="w163">周四</div>
                </th>
                <th>
                    <div class="w163">周五</div>
                </th>
                <th>
                    <div class="w163">周六</div>
                </th>
                <th>
                    <div class="w163">周日</div>
                </th>
            </tr>
            </thead>
            <tbody class="t-tbo" id="tbd">

            </tbody>
        </table>
    </div>
</div>

<div class="bg"></div>
<div class="z-popup popup" hidden>
    <div class="popup-top">
        <em class="fl">选择查询类型</em>
        <i class="close fr"></i>
    </div>
    <div class="zbot">
        <div class="dct">
            <button class="zbtn1" id="byTime">根据时间查无课教师</button>
        </div>
        <div class="dct">
            <button class="zbtn2" id="byTea">根据教师查无课时间</button>
        </div>
    </div>
</div>
<!--教师无课查询-->
<div class="popup wu-popup">
    <div class="popup-top">
        <em class="fl">教师无课查询</em>
        <i class="close fr"></i>
    </div>
    <div class="wuUl">
        <span>年级</span>
        <select class="sel162 mrl10" id="grade">

        </select>
        <script type="text/template" id="gradeList">
            {{~it:value:index}}
            {{? value.jie != ""}}
            <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
            {{?}}
            {{? value.jie == ""}}
            <option value="{{=value.gid}}">{{=value.gnm}}</option>
            {{?}}
            {{~}}
        </script>
        <span>科目</span>
        <select class="sel162 mrl10" id="subject">
        </select>
        <script type="text/template" id="subject_temp">
            {{~it:value:index}}
            <option value="{{=value.subid}}">{{=value.snm}}</option>
            {{~}}
        </script>
        <span>周</span>
        <select class="sel162 mrl10 week1">
        </select>
    </div>
    <div class="wult clearfix">
        <div class="fl">
            <p>选择课节（支持多选）</p>
            <table class="wuTab">
                <thead>
                <tr>
                    <th style="width: 100px;">课节</th>
                    <th>周一</th>
                    <th>周二</th>
                    <th>周三</th>
                    <th>周四</th>
                    <th>周五</th>
                    <th>周六</th>
                    <th>周日</th>
                </tr>
                </thead>
                <tbody id="tbdForSearch">

                </tbody>
                <script type="text/template" id="KeShiListForSearch">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></td>
                        <td class="freeTea">
                            <i class=""></i>
                        </td>
                        <td class="freeTea">
                            <i class=""></i>
                        </td>
                        <td class="freeTea">
                            <i class=""></i>
                        </td>
                        <td class="freeTea">
                            <i class=""></i>
                        </td>
                        <td class="freeTea">
                            <i class=""></i>
                        </td>
                        <td class="freeTea">
                            <i class=""></i>
                        </td>
                        <td class="freeTea">
                            <i class=""></i>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
        <div class="fl" style="margin-left: 25px;">
            <p>无课老师</p>
            <ul class="wuLeft" style="margin-top: 10px" id="tea">

            </ul>
            <script type="text/template" id="freeTea">
                {{~it:value:index}}
                {{? value.subjectStr == ""}}
                <li>{{=value.unm}}</li>
                {{?}}
                {{? value.subjectStr != ""}}
                <li>{{=value.unm}}  （{{=value.subjectStr}}）</li>
                {{?}}
                {{~}}
            </script>
        </div>
    </div>
    <div class="popup-btn mt35 mb35">
        <button class="qd">确定</button>
        <button class="qx">取消</button>
    </div>
</div>


<div class="popup you-popup">
    <div class="popup-top">
        <em class="fl">教师无课查询</em>
        <i class="close fr"></i>
    </div>
    <div class="wuUl">
        <span>年级</span>
        <select class="sel162 mrl10 grade">

        </select>
        <%-- <script type="text/template" id="gradeList1">
             {{~it:value:index}}
             {{? value.jie != ""}}
             <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
             {{?}}
             {{? value.jie == ""}}
             <option value="{{=value.gid}}">{{=value.gnm}}</option>
             {{?}}
             {{~}}
         </script>--%>
        <span>科目</span>
        <select class="sel162 mrl10 subject">
        </select>
        <%--<script type="text/template" id="subject_temp1">
            {{~it:value:index}}
            <option value="{{=value.subid}}">{{=value.snm}}</option>
            {{~}}
        </script>--%>
        <span>周</span>
        <select class="sel162 mrl10 week2">
        </select>
    </div>
    <div class="wult clearfix">

        <div class="fl">
            <p>选择老师（支持多选）</p>
            <ul class="wuLeft" style="margin-top: 10px" id="tbd1">

            </ul>
            <script type="text/template" id="tbdList">
                {{~it:value:index}}
                {{? value.subjectStr == ""}}
                <li class="teacher" ids = "{{=value.uid}}">{{=value.unm}}</li>
                {{?}}
                {{? value.subjectStr != ""}}
                <li class="teacher" ids = "{{=value.uid}}">{{=value.unm}}  （{{=value.subjectStr}}）</li>
                {{?}}
                {{~}}
            </script>
        </div>
        <div class="fl" style="margin-left: 25px;">
            <p>无课课节</p>
            <table class="wTab">
                <thead>
                <tr>
                    <th style="width: 100px;">课节</th>
                    <th>周一</th>
                    <th>周二</th>
                    <th>周三</th>
                    <th>周四</th>
                    <th>周五</th>
                    <th>周六</th>
                    <th>周日</th>
                </tr>
                </thead>
                <tbody class="tbdForSearch">

                </tbody>
                <script type="text/template" id="KeShiListForSearch1">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></td>
                        <td class="freeTea1">
                            <i class="tii1"></i>
                        </td>
                        <td class="freeTea1">
                            <i class="tii1"></i>
                        </td>
                        <td class="freeTea1">
                            <i class="tii1"></i>
                        </td>
                        <td class="freeTea1">
                            <i class="tii1"></i>
                        </td>
                        <td class="freeTea1">
                            <i class="tii1"></i>
                        </td>
                        <td class="freeTea1">
                            <i class="tii1"></i>
                        </td>
                        <td class="freeTea1">
                            <i class="tii1"></i>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
    <div class="popup-btn mt35 mb35">
        <button class="qd">确定</button>
        <button class="qx">取消</button>
    </div>
</div>

<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('MyTimetable', function (MyTimetable) {
        MyTimetable.init();
    });
</script>
</body>
</html>
