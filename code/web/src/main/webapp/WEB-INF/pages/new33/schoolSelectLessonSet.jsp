<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%--<title class="i18n" name='title'>新高考3+3走班排课系统</title>--%>
    <title class="i18n" name='title'>选科组合设置</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/rome.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>


    <script type="text/javascript">
        $(function () {
            $('.cou-x em').click(function () {
                $('.reset').show();
            })
            $('.d-sty em').click(function () {
                $(this).addClass('cur').siblings().removeClass('cur')
            })
            $('.aSty').click(function(){
                $('.aSty-popup').show();
                $('.bg').show();
            })
            $('.qx,.popup-top i').click(function(){
                $('.aSty-popup').hide();
                $('.bg').hide();
            })

        })


    </script>
    <style>
        .w132{width: 132px!important;}
        .ck-all{
            margin-left: 5px;
            position: relative;
            top: 1px;
        }
        .aSty{
            color: #2e94db;
            margin-bottom: 15px;
        }
        .aSty:hover{
            color: red;
        }
    </style>
</head>
<body>
<div>
    <jsp:include page="head.jsp"></jsp:include>
    <div class="right-pos">
        <a href="/newisolatepage/schoolSelectLessonSet.do" class="b1">选科组合设置</a>
        <a href="/newisolatepage/selectProgress.do" class="xk2">选科进度</a>
        <a href="/newisolatepage/selectResult.do" class="b2">选科结果</a>
        <a href="/newisolatepage/selectClassresult.do" class="d2">班级结果</a>
        <a href="/newisolatepage/compared.do" class="db2">选科结果对比</a>
    </div>
    <div class='center w1300 clearfix'>
        <div class="ul-sty center mt25 fl">
            <ul class="bg-line">
                <li class="li-sty" hidden>
                    <span class="fl">学期：</span>

                    <div class="d-sty fl" id="term">

                    </div>
                    <sc.ript type="text/template" id="term_temp">
                        {{~it:value:index}}
                        <em val="{{=value.ciId}}">{{=value.ciname}}</em>
                        {{~}}
                    </sc.ript>
                </li>
                <li class="li-sty">
                    <span class="fl">年级：</span>

                    <div class="d-sty fl" id="grade">

                    </div>
                    <script type="text/template" id="grade_temp">
                        {{~it:value:index}}
                        <em val="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</em>
                        {{~}}
                    </script>
                </li>
            </ul>
        </div>
    </div>
    <div class="center w1300 clearfix">
        <div class="mt35 center fl">
            <div class="clearfix">
                <a class="fr aSty" style="cursor: pointer">如何重新选科？</a>
            </div>
            <button class="add-bt btn fr f16">设置选科时间</button>
            <button class="kjj-bt btn fr f16">3+1+2快捷键</button>
        </div>
    </div>
    <div class='center w1200 clearfix' id="none_png" hidden>
        <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
    </div>

    <input type="hidden" id="selectId" value=""/>


    <input type="hidden" id="s1" value=""/>
    <input type="hidden" id="s2" value=""/>
    <input type="hidden" id="s3" value=""/>
    <input type="hidden" id="s4" value=""/>
    <input type="hidden" id="s5" value=""/>
    <input type="hidden" id="s6" value=""/>
    <input type="hidden" id="subject_length">

    <div class="center w1300" id="content" hidden>
        <div class="mt20 cou-tab center clearfix fl">
            <table style="width: 1200px;table-layout: fixed;">
                <thead>
                <tr id="tr_subject">


                </tr>
                </thead>
                <tbody id="my_tbody">

                </tbody>
            </table>
        </div>
    </div>
</div>

<script type="text/template" id="tdTmpl122">
    {{ if(it.length>0){ }}
    {{ for (var i = 0, l = it.length; i < l; i++) { }}
    {{var obj=it[i];}}
    <tr>
        <td><em class="w150">{{=obj.name}}</em></td>
        {{ for(var j=1;j<=$("#subject_length").val();j++){ }}


        {{ var s1=jQuery("#s"+j).val(); }}
        {{ if(obj.sub1Str==s1 || obj.sub2Str==s1 || obj.sub3Str==s1 ) { }}
        <td><em class="w150 r-cur"></em></td>
        {{ } else { }}
        <td><em class="w150"></em></td>
        {{ } }}


        {{ } }}

        {{ if(obj.state==1) { }}
        <td><em class="w50"><input id="{{=obj.id}}" type="checkbox" checked="checked"></em></td>
        {{ }else { }}
        <td><em class="w50"><input id="{{=obj.id}}" type="checkbox"></em></td>
        {{ } }}

        {{? obj.num!=null}}
        <td><em style="width: 100px;display: inline-block"><input style="width:60px;height:30px;border:1px solid #dfdfdf;" id="{{=obj.id}}" type="text" value="{{=obj.num}}"></em></td>
        {{??}}
        <td><em style="width: 100px;display: inline-block"><input style="width:60px;height:30px;border:1px solid #dfdfdf;" id="{{=obj.id}}" type="text" ></em></td>
        {{?}}
    </tr>

    {{ } }}
    {{}}}
</script>


<script type="text/template" id="subject">
    <th class="w132">组合</th>
    {{ if(it.length>0){ }}
    {{ for (var i = 0, l = it.length; i < l; i++) { }}
    {{var obj=it[i];}}

    <th class="w132" id="{{=obj.idStr}}">{{=obj.value}}</th>
    {{ } }}
    {{}}}
    <th class="w132">可选<input type="checkbox" class="ck-all"></th>
    <th class="w132">选科人数上限</th>
</script>
<div class="bg"></div>
<div class="popup stu-popup">
    <div class="popup-top">
        <em>开启选科</em>
        <i class="fr"></i>
    </div>
    <dl class="popup-dl">
        <dd>
            <span>选科开始日期：</span>
            <input class="inp1" id="start" type="text">
        </dd>
        <dd>
            <span>选科结束日期：</span>
            <input class="inp1" id="end" type="text">
        </dd>
    </dl>
    <div class="popup-btn mt25">
        <button class="qd" id="update_start">确定</button>
        <button class="qx">取消</button>
    </div>
</div>

<div class="popup aSty-popup">
    <div class="popup-top">
        <em>如何重新选科</em>
        <i class="fr"></i>
    </div>
    <dl class="popup-dl oh395">
        <p>1.新建一次排课<a href="/newisolatepage/setMm.do" style="color: #2e94db;">点击跳转&gt;&gt;</a></p>
        <p>2.选择同步需要的数据源，全选但不勾选学生数据</p>
        <img src="/static_new/images/new33/aSty1.png">
        <p>3.当前排课选择新建的排课次。</p>
        <img width="765" src="/static_new/images/new33/aSty2.png">
        <p>4.进入“学生管理”，学生选科数据为空，则可以重新选科。</p>
        <img width="765" src="/static_new/images/new33/aSty3.png">
    </dl>
    <div class="popup-btn mt25">
        <button class="qx">关闭</button>
    </div>
</div>
</body>


<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('schoolSelectLessonSet', function (schoolSelectLessonSet) {
        schoolSelectLessonSet.init();
    });


</script>
</html>
