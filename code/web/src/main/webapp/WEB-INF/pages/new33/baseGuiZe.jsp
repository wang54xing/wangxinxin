<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/3/13
  Time: 10:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>排课规则</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
    <link rel="stylesheet" href="/static/css/demo.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/zTreeStyle/zTreeStyle.css">
    <link rel="stylesheet" href="/static_new/css/jedate.css">
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.charfirst.pinyin.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.listnav.min-2.1.js"></script>
    <script type="text/javascript">
        $(function () {
            $(function () {
                $(".right-pos a").eq(6).attr("class", "gz2");
            })
            $('.t-down').click(function () {
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top', -182)
            })
            $('.t-top').click(function () {
                $('.td-over').css('margin-top', 0)
            })
            $('.t-set').click(function () {
                $('.bg').show();
                $('.set-popup').show();
            })
            $('.fft,.qss').click(function () {
                $('.qua-popup').hide();
                $('.sww-popup').show();
            })
            $('.quanbudou,.ffr').click(function () {
                $('.set-popup').hide();
                $('.sww-popup').show();
            })
            $("body").on("click", ".s-cur", function () {
                $(this).find('em').toggleClass('set-cur')
            })
            $(".adt-popup .qqxx,.adt-popup .cc").click(function () {
                $(".adt-popup,.bg").hide();
                $(".gz-popup,.bg").show();
            })
            $(".gz-popup .qx,.gz-popup .close").click(function () {
                $(".gz-popup,.bg").hide();
            })
        })
    </script>
    <style>
        .td-over {
            margin-top: -182px;
        }
    </style>
</head>
<body>
<div>
    <jsp:include page="head.jsp"></jsp:include>
    <%@ include file="/WEB-INF/pages/new33/BasicDatasideBar.jsp" %>
    <div class="comment">
        <div id="com-col">
            <div class="center w1300 clearfix">
                <div class="mt35 center fl">
                    <select class="fl sel6 mr30 grade" id="GradeTp">
                    </select>
                    <select class="fl sel6 mr30" type="text" id="Tp">
                        <option value="-1">对象类型</option>
                        <option value="3">行政型</option>
                        <option value="1">走班型</option>
                    </select>
                    <button class="allow fr">教师排课需求</button>
                    <button class="newGz fr">+新增规则</button>
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
                            <th width="240">对象</th>
                            <th width="240">对象类型</th>
                            <th width="360">时间区域</th>
                            <th width="120">规则</th>
                            <th width="240">操作</th>
                        </tr>
                        </thead>
                    </table>
                    <div class="ha420">
                        <table class="votab">
                            <script type="text/template" id="tpL">
                                {{~it:value:index}}
                                <tr>
                                    <td><em class="w238">{{=value.str}}</em></td>
                                    <td><em class="w239">{{=value.type}}</em></td>
                                    <td><em class="w358">{{=value.day}}</em></td>
                                    <td><em class="w120">{{=value.sta}}</em></td>
                                    <td>
                                        <em class="w239">
                                            <img class="opt optt" src="/static_new/images/new33/editt.png" ids="{{=value.id}}">
                                            <img class="opt opde" src="/static_new/images/new33/dell.png" ids="{{=value.id}}">
                                        </em>
                                    </td>
                                </tr>
                                {{~}}
                            </script>
                            <tbody id="ListType">

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="bg"></div>
    <div class="popup gz-popup">
        <div class="popup-top">
            <em class="fl">设置排课规则一<span id="gradeName"></span></em>
            <i class="close fr"></i>
        </div>
        <div class="mlt15 clearfix">
            <div class="pTabfl fl">
                <table>
                    <thead>
                    <tr>
                        <th><em class="inline w60">课节/日</em></th>
                        <th><em class="inline w45">周一</em></th>
                        <th><em class="inline w45">周二</em></th>
                        <th><em class="inline w45">周三</em></th>
                        <th><em class="inline w45">周四</em></th>
                        <th><em class="inline w45">周五</em></th>
                        <th><em class="inline w45">周六</em></th>
                        <th><em class="inline w45">周日</em></th>
                    </tr>
                    </thead>
                </table>
                <div class="pTabfl ha370">
                    <table>
                        <tbody id="keShi">

                        </tbody>
                        <script type="text/template" id="KeShiList">
                            {{~it:value:index}}
                            <tr>
                                <td><em class="inline w60">{{=value.name}}</em></td>
                                <td class="cursor"><em class="inline w45 rg"></em></td>
                                <td class="cursor"><em class="inline w45 rg"></em></td>
                                <td class="cursor"><em class="inline w45 rg"></em></td>
                                <td class="cursor"><em class="inline w45 rg"></em></td>
                                <td class="cursor"><em class="inline w45 rg"></em></td>
                                <td class="cursor"><em class="inline w45 rg"></em></td>
                                <td class="cursor"><em class="inline w45 rg"></em></td>
                            </tr>
                            {{~}}
                        </script>
                    </table>
                </div>
            </div>
            <div class="pTabrt fr">
                <script type="text/template" id="grade_temp">
                    {{~it:value:index}}
                    <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
                    {{~}}
                </script>
                <div class="fr" style="margin-right: 200px;margin-top: 7px"><em style="color:red">(学科非必选)</em></div>
                <%--<select class="sel1 mlt0 grade" id="grade">--%>
                <%--</select>--%>
                <select class="sel1 mlt0" id="Subtype">
                    <option value="3">行政型</option>
                    <option value="1">走班型</option>
                </select>
                <script type="text/template" id="subList_temp">
                    {{~it:value:index}}
                    <button class="btn1" ids="{{=value.subid}}">{{=value.snm}}</button>
                    {{~}}
                </script>
                    <div class="mt10 mx" id="subList">
                    </div>
                    <div style="margin-top: 20px;">
                        选择教师
                </div>
                <div class="mt10 pos">
                    <input class="sel3 l0" id="teaListCount" type="text" disabled placeholder="选择老师">
                    <button class="xzBtn r5">选择</button>
                </div>
                <div class="mt65" style="margin-top: 80px;">
                    选择规则
                </div>
                <div class="mt10 baseBtn">
                    <button class="btn2 bg5" ids="2">优先</button>
                    <button class="btn2" ids="1">必须</button>
                    <button class="btn2" ids="3">拒绝</button>
                    <button class="btn2" ids="4">避免</button>
                </div>
            </div>
        </div>
        <div class="popup-bot mt30">
            <button class="qd" id="addOrUpdateGuiZe" ids="*">确定</button>
            <button class="qx">取消</button>
        </div>
    </div>
</div>


<div class="popup adt-popup">
    <div class="popup-top">
        <em class="fl ml20">选择教师</em>
        <i class="fr cc"></i>
    </div>
    <dl class="dl">
        <div class="clearfix">
            <div style="background:#ededed;">
                <div id="myList-nav" class="member-select" style="width: 530px;display: inline-block"></div>
                <span style="display: inline-block;vertical-align: top;margin-top: 8px;">
                    <input type="checkbox" class="sel_all">全选
                </span>
            </div>
            <div class="myList">
                <ul id="myList" class="member-list clearfix">

                </ul>
                <script type="text/template" id="TeaList_temp">
                    {{~it:value:index}}
                    <li class="li-special" ids="{{=value.uid}}">
                        <img src="http://7xiclj.com1.z0.glb.clouddn.com/d_2_1.png">
                        <span>{{=value.unm}}</span>
                        <i class="r-icon t5"></i>
                    </li>
                    {{~}}
                </script>
            </div>

        </div>
    </dl>
    <div class="popup-bot mt30">
        <button class="qd" id="teaAdd">确定</button>
        <button class="qqxx">取消</button>
    </div>
</div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('baseGuiZe', function (baseGuiZe) {
        baseGuiZe.init();
    });
</script>
</html>
