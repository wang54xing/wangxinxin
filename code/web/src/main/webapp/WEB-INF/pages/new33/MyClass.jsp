<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/7/31
  Time: 15:37
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
</head>
<body>
<div>
    <input type="hidden" value="${jxbId}" id="jxbId">
    <jsp:include page="head.jsp"></jsp:include>
    <span id="defaultTerm" style="display: none"></span>
    <div class="clearfix center">
        <div class="fl w225 mt30">
            <select class="sel2" disabled id="jxbVal">
            </select>

            <div class="ulSty mt20 f12">
                <ul id="student">
                    <%--<li class="cur">--%>
                        <%--<span class="w4">张三</span>--%>
                        <%--<span>男</span>--%>
                        <%--<span>201800180012</span>--%>
                    <%--</li>--%>
                    <%--<li>--%>
                        <%--<span class="w4">张三</span>--%>
                        <%--<span>男</span>--%>
                        <%--<span>201800180012</span>--%>
                    <%--</li>--%>

                </ul>
                <script type="text/template" id="room_xq">
                    {{~it:value:index}}
                    <li ids="{{=value.userId}}">
                        <span class="w4" style="width:13em;">{{=value.name}}</span>
                        <span>{{=value.sex}}</span>
                        <span class="w110">{{=value.sn}}</span>
                    </li>
                    {{~}}
                </script>
            </div>
        </div>
        <div class="fr w940 mt30">
            <div class="clearfix">
                <select class="sel1 fl" id="week">
                </select>

                <div class="fr">
                    <button class="btn4">学生成绩</button>
                    <button class="btn5 ml5">学生考勤</button>
                </div>
            </div>
            <table class="tabSty1 mt20">
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
            <script type="text/template" id="week_temp">
                {{~it:value:index}}
                <option value="{{=value.serial}}">{{=value.numberWeek}}{{=value.start}}</option>
                {{~}}
            </script>
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
        </div>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('MyClass', function (MyClass) {
        MyClass.init();
    });
</script>
</body>
</html>
