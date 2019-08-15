<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/8/1
  Time: 10:15
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
    <script type="text/javascript">
        $(function () {
            $('.ulSty1 li').click(function () {
                $(this).addClass('liCur').siblings().removeClass('liCur');
                var tt = $(this).attr('id');
                $('#' + "tab-" + tt).show().siblings().hide();
            })
        })
    </script>
</head>
<body>
<div>
    <style>
        .bai{
            background: #FFF!important;
        }
        .roomCls {
            /*background: #AFEECC!important;;*/
        }
        .hui {
            background: #AAA !important;
        }
        .greed {
            background: #AFEEEE!important;;
        }
        .fen{
            background: #FFB6C1!important;;
        }
    </style>
    <jsp:include page="head.jsp"></jsp:include>
    <span id="defaultTerm" style="display: none"></span>

    <div class="clearfix center">
        <div class="fl mt30 c00 f18">
            <button class="btn6 ml5">申请调课</button>
            <button class="btn7 ml5">调课记录</button>
        </div>
        <div class="fr mt30">
            <script type="text/template" id="week_temp">
                {{~it:value:index}}
                <option value="{{=value.serial}}">{{=value.numberWeek}}{{=value.start}}</option>
                {{~}}
            </script>
            <select class="sel1" id="week">
                <option></option>
            </select>
            <select class="sel1 ml5">
                <option>调课</option>
            </select>
            <ul class="ulSty1 ml5">
                <li id="dq" class="liCur">短期</li>
                <li id="cq">长期</li>
            </ul>
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
            <td class="itt fdd" sta="0">
            </td>
            <td class="itt fdd" sta="0">
            </td>
            <td class="itt fdd" sta="0">
            </td>
            <td class="itt fdd" sta="0">
            </td>
            <td class="itt fdd" sta="0">
            </td>
            <td class="itt fdd" sta="0">
            </td>
            <td class="itt fdd" sta="0">
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
<div class="popup tpopup" hidden>
    <div class="popup-top">
        <em class="fl">调课记录</em>
        <i class="fr"></i>
    </div>
    <div class="ss-dv w610 ml30">
        <table>
            <thead>
            <tr>
                <th>调课班级</th>
                <th>调课时间</th>
                <th>被调班级</th>
                <th>被调时间</th>
                <th>调课交换老师</th>
                <th>作用周</th>
                <th>类型</th>
                <th>调课信息</th>
                <th>操作</th>
            </tr>
            </thead>
        </table>
        <div class="autoo h310 w610">
            <table>
                <script type="text/template" id="bdbd">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.jxbName}}</td>
                        <td>周{{=value.x}}第{{=value.y}}节</td>
                        <td>{{=value.njxbName}}</td>
                        <td>周{{=value.nx}}第{{=value.ny}}节</td>
                        <td>{{=value.teaName}}</td>
                        <td>{{=value.week}}</td>
                        <td>调课</td>
                        <td>{{=value.st}}</td>
                        {{? value.sta ==1}}
                        <td class="cx" ids="{{=value.id}}"><em class="c6f">撤销</em></td>
                        {{?}}
                        {{? value.sta !=1}}
                        <td></td>
                        {{?}}
                    </tr>
                    {{~}}
                </script>
                <tbody id="tbdd">
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('MyClassTi', function (MyClassTi) {
        MyClassTi.init();
    });
</script>
</html>
