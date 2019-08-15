<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%--<title class="i18n" name='title'>新高考3+3走班排课系统</title>--%>
    <title class="i18n" name='title'>冲突检测</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript">
    	
    	
        $(function(){
        	$(".right-pos a").eq(3).attr("class","ve2");
            $('.t-down').click(function () {
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top', -91)
            })
            $('.t-top').click(function () {
                $('.td-over').css('margin-top', 0)
            })
        })
    </script>
</head>
<body>
<div>
    <jsp:include page="head.jsp"></jsp:include>
    <span id = "defaultTerm" style="display: none"></span>
    <div class="right-pos">
        <div class="ov-hi">
            <div class="td-over">
                <a href="/newisolatepage/placementSet.do" class="va1">分教学班</a>
             	<a href="/newisolatepage/stuSign.do" class="vb1">标记学生</a>
                <a href="/newisolatepage/jxbRelation.do" class="vc1">关联教学班</a>
             	<%--<a href="/newisolatepage/stuRelation.do" class="vc1">关联学生</a>--%>
             	<%--<a href="/newisolatepage/fenbanInfoSet.do" class="vd1">信息设置</a>--%>
             	<a href="/newisolatepage/conflict.do" class="ve1">冲突检测</a>
                <%--<a href="/newisolatepage/danShuangZhou.do" class="vz1">单双周课</a>--%>
            </div>
        </div>
    </div>
    <div class="center w1300 clearfix">
        <div class="ul-sty center mt25 fl">
            <ul class="bg-line">
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
                <li class="li-sty">
                    <span class="fl">分类：</span>
                    <div class="d-sty fl" id="type">
                        <em val="1" class = "cur">等级班</em>
                        <em val="2">合格班</em>
                        <em val="3">行政班</em>
                        <em val="4">专项班</em>
                    </div>
                </li>
                <li class="li-sty">
                    <span class="fl">学科：</span>
                    <div class="d-sty s-c fl" id="subject">

                    </div>
                    <script type="text/template" id="subject_temp">
                        {{~it:value:index}}
                            <em class="sp" val="{{=value.subid}}">{{=value.snm}}</em>
                        {{~}}
                    </script>
                    <label class="lab">请勾选任意两门学科</label>
                </li>
            </ul>
        </div>
    </div>
    <div class="center w1300 clearfix">
        <div class="mt20 center fl">
            <span class="f16">冲突检测：<em class="cur" id="ctxk"></em></span>
        </div>
    </div>
    <div class='center w1200 clearfix' id="none_png" hidden>
        <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
    </div>
    <div class="center w1300 clearfix"  id="all_content" hidden>
        <div class="mt20 curr-tab cou-tab center fl" >
            <div class="fl">
                <table>
                    <thead>
                    <tr>
                        <th class="w150">教学班/教学班</th>
                    </tr>
                    </thead>
                    <tbody id="vertical">

                    </tbody>
                    <script type="text/template" id="vertical_temp">
                        {{~it:value:index}}
                        <tr>
                            <td><em class="w150">{{=value.name}}</em></td>
                        </tr>
                        {{~}}
                    </script>
                </table>
            </div>
            <div class="cou-xx fl">
                <table>
                    <thead>
                    <tr id="horizon">

                    </tr>
                    <script type="text/template" id="horizon_temp">
                        {{~it:value:index}}
                        <th class="w125">{{=value.name}}</th>
                        {{~}}
                    </script>
                    </thead>
                    <tbody id="content">

                    </tbody>
                    <script type="text/template" id="content_temp">
                        {{~it:value:index}}
                            <tr>
                                {{~value:value2:index2}}
                                    {{? value2.num==0}}
                                        <td><em class="w125" jxbs="{{=value2.jxbs}}">{{=value2.num}}</em></td>
                                    {{??}}
                                        <td><em class="w125 cur de-cur" jxbs="{{=value2.jxbs}}">{{=value2.num}}</em></td>
                                    {{?}}
                                {{~}}
                            </tr>
                        {{~}}
                    </script>
                </table>
            </div>
        </div>
    </div>

</div>
<div class="bg"></div>

<div class="popup detail-popup">
    <div class="popup-top">
        <em>详情</em>
        <i class="fr close"></i>
    </div>
    <div class="ss-dv w620 ml30">
        <table>
            <thead>
            <tr>
                <th width="112">姓名</th>
                <th width="50">层级</th>
                <th width="130">学籍号</th>
                <th width="50">性别</th>
                <th width="112">行政班</th>
                <th width="111">选科组合</th>
            </tr>
            </thead>
        </table>
        <div class="autoo h310 w586">
            <table>
                <tbody id="student">

                </tbody>
                <script type="text/template" id="student_temp">
                    {{~it:value:index}}
                        <tr>
                            <td><em class="Ww100 inline">{{=value.name}}</em></td>
                            <td><em class="w50 inline">{{=value.lev}}</em></td>
                            <td><em class="w130 inline whisp" title="{{=value.sn}}">{{=value.sn}}</em></td>
                            <td><em class="w50 inline">{{=value.sex}}</em></td>
                            <td><em class="Ww100 inline">{{=value.grade}}（{{=value.xh}}）班</em></td>
                            <td><em class="Ww100 inline">{{=value.group}}</em></td>
                        </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
    <div class="popup-btn mt25">
        <button class="qxx">关闭</button>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('conflict', function (conflict) {
        conflict.init();
    });
</script>
</body>
</html>
