<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/3/6
  Time: 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>学科&课时</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
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
            $(".right-pos a").eq(3).attr("class", "ve2");
            $('.cou-x em').click(function () {
                $('.reset').show();
            });
            $('.d-sty em').click(function () {
                $(this).addClass('cur').siblings().removeClass('cur');
            });

            $('.gra-popup i,.qx').click(function () {
                $('.bg').hide();
                $('.gra-popup').hide();
                $('.zou-popup').hide();
            })

            $('.zou-popup i,.qx').click(function () {
                $('.bg').hide();
                $('.gra-popup').hide();
                $('.zou-popup').hide();
            })
            $("body").on("click", ".gra-sp span", function () {
                $(this).toggleClass('ssp-cur')
            })
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
        <div class="mt35 center fl mt20">
        </div>
    </div>
    <div class="optcont">
        <%--<select id="term">--%>

        <%--</select>--%>
        <%--<script type="text/template" id="term_temp">--%>
            <%--{{~it:value:index}}--%>
            <%--<option value="{{=value.ciId}}" termId="{{=value.termId}}">{{=value.ciname}}</option>--%>
            <%--{{~}}--%>
        <%--</script>--%>
            <span>年级</span>
        <select id="grade">

        </select>
        <script type="text/template" id="grade_temp">
            {{~it:value:index}}
            <option value="{{=value.gid}}">{{=value.gnm}}（{{=value.jie}}）</option>
            {{~}}
        </script>
        教师最大周课时:<input class="inp10 ml10" id="limithour" type="text"/>
            <button class="btn-Sele">选择学科</button>
            <button class="btn-zouban mr10 zbxk" id="zouban">走班学科</button>
            <button class="btn-edit mr10 bianji">编辑</button>
            <button class="btn-ok mr10">完成</button>
    </div>
    <div class="center w1300 clearfix">
        <div class="mt20 cur-tab cou-tab center fl">
            <div class='center w1200 clearfix' id="none_png" hidden>
                <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
            </div>
            <table style="width: 1200px" class="votab cltmtab" id="content" hidden>
                <thead>
                <tr>
                    <th width="20%">学科</th>
                    <th width="20%">类型</th>
                    <th width="20%">课时</th>
                    <th width="20%">是否为单双周</th>
                    <th width="20%">是否为专项课</th>
                </tr>
                </thead>
                <tbody id="subjectList">

                </tbody>
                <script type="text/template" id="subjectTrScript">
                    {{~it:value:index}}
                    {{? value.isZouBan == 1}}
                    <tr subjectName="{{=value.snm}}" subjectId="{{=value.subid}}" ids="{{=value.id}}"
                        isZouBan="{{=value.isZouBan}}" type="{{=value.type}}" dan="{{=value.dan}}">
                        <td>{{=value.snm}}</td>
                        <td>合格</td>
                        <td>
                            <span>{{=value.time}}</span>
                            <input type="text" value="{{=value.time}}" class="inp fsr">
                        </td>
                        {{? value.type == 1}}
                        <td>
                            <input type="checkbox" checked="checked" class="ckbo" ty="0" ids="{{=value.id}}">
                        </td>
                        {{?}}
                        {{? value.type == 0}}
                        <td>
                            <input type="checkbox" class="ckbo" ty="0" ids="{{=value.id}}">
                        </td>
                        {{?}}
                        {{? value.dan == 1}}
                        <td>
                            <input type="checkbox" checked="checked" class="ckbod" ids="{{=value.id}}">
                        </td>
                        {{?}}
                        {{? value.dan == 0}}
                        <td>
                            <input type="checkbox" class="ckbod" ids="{{=value.id}}">
                        </td>
                        {{?}}
                    </tr>
                    <tr subjectName="{{=value.snm}}" subjectId="{{=value.subid}}" ids="{{=value.id}}"
                        isZouBan="{{=value.isZouBan}}" type="{{=value.type1}}" dan="{{=value.dan}}">
                        <td>{{=value.snm}}</td>
                        <td>等级</td>
                        <td>
                            <span>{{=value.dTime}}</span>
                            <input type="text" value="{{=value.dTime}}" class="inp fsr">
                        </td>
                        {{? value.type1 == 1}}
                        <td>
                            <input type="checkbox" checked="checked" class="ckbo" ty="1" ids="{{=value.id}}">
                        </td>
                        {{?}}
                        {{? value.type1 == 0}}
                        <td>
                            <input type="checkbox" class="ckbo" ty="1" ids="{{=value.id}}">
                        </td>
                        {{?}}
                        {{? value.dan == 1}}
                        <td>
                            <input type="checkbox" checked="checked" class="ckbod" ids="{{=value.id}}">
                        </td>
                        {{?}}
                        {{? value.dan == 0}}
                        <td>
                            <input type="checkbox" class="ckbod" ids="{{=value.id}}">
                        </td>
                        {{?}}
                    </tr>
                    {{?}}
                    {{? value.isZouBan == 0}}
                    <tr subjectName="{{=value.snm}}" subjectId="{{=value.subid}}" ids="{{=value.id}}"
                        isZouBan="{{=value.isZouBan}}" type="{{=value.type}}" dan="{{=value.dan}}">
                        <td>{{=value.snm}}</td>
                        <td>非走班</td>
                        <td>
                            <span>{{=value.time}}</span>
                            <input type="text" value="{{=value.time}}" class="inp fsr">
                        </td>
                        {{? value.type == 1}}
                        <td>
                            <input type="checkbox" checked="checked" class="ckbo" ty="2" ids="{{=value.id}}">
                        </td>
                        {{?}}
                        {{? value.type == 0}}
                        <td>
                            <input type="checkbox" class="ckbo" ty="2" ids="{{=value.id}}">
                        </td>
                        {{?}}
                        {{? value.dan == 1}}
                        <td>
                            <input type="checkbox" checked="checked" class="ckbod" ids="{{=value.id}}">
                        </td>
                        {{?}}
                        {{? value.dan == 0}}
                        <td>
                            <input type="checkbox" class="ckbod" ids="{{=value.id}}">
                        </td>
                        {{?}}
                    </tr>
                    {{?}}
                    {{~}}
                </script>
            </table>
        </div>
    </div>
</div>

<div class="bg"></div>
<div class="popup gra-popup">
    <div class="popup-top">
        <em class="fl">选择学科</em>
        <i class="fr"></i>
    </div>
    <div class="popup-li">
        <script type="text/template" id="subjectSpanScript">
            {{~it:value:index}}
            <span subjectName="{{=value.subjectName}}" subjectId="{{=value.id}}" dan="0" isZouBan="0" ids="*" time="0" type="0"
                  type1="0"
                  dtime="0">{{=value.subjectName}}</span>
            {{~}}
        </script>
            <div class="gra-sp">
            </div>
        <%--<div class="gra-d">--%>
            <%--<table class="tabSty">--%>
                <%--<thead>--%>
                    <%--<tr>--%>
                        <%--<th width="160">学科</th>--%>
                        <%--<th width="160">学科</th>--%>
                        <%--<th width="160">学科</th>--%>
                    <%--</tr>--%>
                <%--</thead>--%>
            <%--</table>--%>
            <%--<div class="gra-sp">--%>
                <%--<table class="tabSty">--%>
                    <%--<tbody>--%>
                        <%--<tr>--%>
                            <%--<td width="160">学科</td>--%>
                            <%--<td width="160">--%>
                                <%--<input type="checkbox">--%>
                            <%--</td>--%>
                            <%--<td width="160">--%>
                                <%--<input type="checkbox">--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                    <%--</tbody>--%>
                <%--</table>--%>
            <%--</div>--%>
    </div>
        <div class="popup-btn mtb20">
            <button class="qd">确定</button>
            <button class="qx">取消</button>
        </div>
</div>

<div class="popup zou-popup">
    <div class="popup-top">
        <em class="fl">走班学科</em>
        <i class="fr close"></i>
    </div>
    <div class="popup-li">
        <script type="text/template" id="zoubanSubject">
            {{~it:value:index}}
            {{?value.isZouBan == 1}}
            <span class="sp-curr" subjectName="{{=value.snm}}" dan="{{=value.dan}}" subjectId="{{=value.subid}}" isZouBan={{=value.isZouBan}}>{{=value.snm}}</span>
            {{?}}
            {{?value.isZouBan != 1}}
            <span subjectName="{{=value.snm}}" subjectId="{{=value.subid}}" dan="{{=value.dan}}" isZouBan={{=value.isZouBan}}>{{=value.snm}}</span>
            {{?}}
            {{~}}
        </script>
        <div class="zoubanSub">
        </div>
    </div>
    <div class="popup-btn mtb20">
        <button class="qdzouban" id="qdzouban">确定</button>
        <button class="qx">取消</button>
    </div>
</div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('classTime', function (classTime) {
        classTime.init();
    });
</script>
</body>
</html>
