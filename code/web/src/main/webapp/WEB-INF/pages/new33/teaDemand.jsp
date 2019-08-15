<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>3+3</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/teaDemand/resetD.css"></link>
    <link rel="stylesheet" href="/static_new/css/new33/teaDemand/fixed-table.css"></link>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/fixed-table.js"></script>
    <script type="text/javascript">
        $(function(){
            $('.text1').click(function(){
                if($(this).text().indexOf('编辑')!=-1){
                    $(this).text('保存')
                }else{
                    $(this).text('编辑')
                }
            })
            $('.close,.qxx').click(function(){
                $('.bg').hide();
                $('.yy-popup').hide();
            })
            $('.require').click(function(){
                if(!$(this).hasClass("select")){
                    $(this).addClass("select").siblings().removeClass("select");
                }else{
                    $(this).removeClass("select");
                }

                if($(this).attr("type") == 2){
                    $(this).toggleClass("cb5");
                    $(".require").each(function (i,st) {
                        $(this).removeClass("c0a");
                        $(this).removeClass("c27");
                        $(this).removeClass("cef");
                    });
                }
                if($(this).attr("type") == 1){
                    $(this).toggleClass("c0a");
                    $(".require").each(function (i,st) {
                        $(this).removeClass("cb5");
                        $(this).removeClass("c27");
                        $(this).removeClass("cef");
                    });
                }
                if($(this).attr("type") == 3){
                    $(this).toggleClass("c27");
                    $(".require").each(function (i,st) {
                        $(this).removeClass("cb5");
                        $(this).removeClass("c0a");
                        $(this).removeClass("cef");
                    });
                }
                if($(this).attr("type") == 4){
                    $(this).toggleClass("cef");
                    $(".require").each(function (i,st) {
                        $(this).removeClass("cb5");
                        $(this).removeClass("c0a");
                        $(this).removeClass("c27");
                    });
                }
            })
        })
    </script>
    <style>
        .top-nav {
            float: left;
            margin-left: 150px;
        }
    </style>
</head>
<body>

<jsp:include page="head1.jsp"></jsp:include>
<span id="defaultTerm" style="display: none"></span>
<div>
    <div class="clearfix center2 mb35">
        <div class="mt20 clearfix">
            <button class="btn bc6 require" type="2">优先排课</button>
            <button class="btn bc7 require" type="1">必须排课</button>
            <button class="btn bc8 require" type="3">不排课</button>
            <button class="btn mr25 bc50 require" type="4">避免排课</button>
            <select class="sel1 ml30" id="term">
            </select>
            <script type="text/template" id="term_temp">
                {{~it:value:index}}
                <option value="{{=value.id}}">{{=value.xqnm}}</option>
                {{~}}
            </script>
            <button class="btn bc10" id="syn">同上学期</button>
            <button class="btn bc11 text1 fr" id="save" hidden>编辑</button>
        </div>
        <div class="mt20">
            <label id="grade">
                <%--<input type="checkbox">dsfa--%>
            </label>
            <script type="text/template" id="grade_temp">
                {{~it:value:index}}
                <input type="checkbox" ids="{{=value.gid}}" class="gradeCheck"><span>{{=value.gnm}}({{=value.jie}})</span>&nbsp;&nbsp;
                {{~}}
            </script>
        </div>
        <div>
            <table class="tabSty1 mt20 fl">
                <thead>
                <tr>
                    <th>课节/日</th>
                    <th>周一</th>
                    <th>周二</th>
                    <th>周三</th>
                    <th>周四</th>
                    <th>周五</th>
                    <th>周六</th>
                    <th>周日</th>
                </tr>
                </thead>
                <tbody id="tbd">
                </tbody>
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
                            <em>{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></em>
                        </td>
                        <td class="ttd1 ttc"></td>
                        <td class="ttd1 ttc"></td>
                        <td class="ttd1 ttc"></td>
                        <td class="ttd1 ttc"></td>
                        <td class="ttd1 ttc"></td>
                        <td class="ttd1 ttc"></td>
                        <td class="ttd1 ttc"></td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
</div>
<div class="bg"></div>
<div class="popup yy-popup">
    <div class="popup-top">
        <em class="fl">原因</em>
        <i class="close fr"></i>
    </div>
    <div class="yyp">
        <p>排课需求描述</p>
        <textarea id="desc"></textarea>
    </div>
    <div class="popup-btn mt15">
        <button class="qd" id="qddesc">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('teaDemand', function (teaDemand) {
        teaDemand.init();
    });
</script>
</body>
</html>
