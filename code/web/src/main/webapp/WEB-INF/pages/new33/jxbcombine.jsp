<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>教学班组合</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $(".right-pos a").eq(3).attr("class", "jxb2");
        })
        $(function () {
            $('.hov').click(function () {
                $('.bg').show();
                $('.scla-popup').show();
            })
            $('.close').click(function () {
                $('.bg').hide();
                $('.detail-popup').hide();
                $('.scla-popup').hide();
            })
            $('.t-down').click(function () {
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top', -91)
            })
            $('.t-top').click(function () {
                $('.td-over').css('margin-top', 0)
            })
            $('.clStu').click(function () {
                $('.bg').show();
                $('.tj-popup').show();
                $("#tab-zxk").empty()
                $("#tab-zb").empty()
                $("#xing").empty()
                $("#sousuokk").attr("cid", "")
                $("#sousuokk").attr("combiname", "")
                $("#sousuokk").attr("userId", "")
                $("#userName").val("")
            })
            $('.feUl li').click(function () {
                $(this).addClass('ulcur').siblings().removeClass('ulcur');
                var tt = $(this).attr('id');
                $('#' + 'tab-' + tt).show().siblings().hide();
            })
        })
    </script>
    <style type="text/css">
        .dialog {
            position: absolute;
            width: 400px;
            height: 246px;
            background-color: #fff;
            border: 1px solid #d2d2d2;
            display: none;
        }

        .dialog tr th {
            background: #ededed !important;
            color: black;
            border-bottom: none !important;
        }

        .dialog tr th, .dialog tr td {
            text-align: center;
            border: 1px solid #969696;
            height: 40px;
            background: #fff;
        }
        .box-s{
            box-shadow: #969696 0px 0px 12px 3px;
        }
    </style>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<span id="defaultTerm" style="display: none"></span>

<div class="right-pos">
    <div class="ov-hi">
        <div class="td-over">
            <%--<a href="/newisolatepage/placementSet.do" class="va1">分教学班</a>--%>
            <%--<a href="/newisolatepage/stuSign.do" class="vb1">标记学生</a>--%>
            <%--<a href="/newisolatepage/stuRelation.do" class="vc1">关联学生</a>--%>
            <a href="/newisolatepage/fenbanInfoSet.do" class="vd1">信息设置</a>
            <%--<a href="/newisolatepage/conflict.do" class="ve1">冲突检测</a>--%>
            <a href="/newisolatepage/danShuangZhou.do" class="vz1">单双周课</a>
            <a href="/newisolatepage/zhuanXiang.do" class="vx1">专项组合</a>
            <a href="/newisolatepage/jxbcombine.do" class="jxb1">教学班组合</a>
                <a href="/newisolatepage/jxbTimeCombine.do" class="jxb1">分时段组合</a>
            <a href="/newisolatepage/paiKeZuHe.do" class="vpk1">排课组合</a>
        </div>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="ma1 fl">
        <div class="mt20 clearfix center fl">
            <button class="b9 fr mtt41 mr133 mr10">导出</button>
            <button class="b11 fr mtt41 mr133 mr10" id="save">计算冲突&保存数据</button>
            <select class="sel1 ml0 grade" style="width: 201px">
            </select>
            <script type="text/template" id="grade_temp">
                {{~it:value:index}}
                <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
                {{~}}
            </script>
            <select class="sel1 ml0 type" style="width: 150px" id="type">
                <option value="1">等级</option>
                <option value="2">合格</option>
                <option value="3">行政</option>
            </select>
        </div>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="mt20 cur-tab cou-tab ccou-tab center fl">
        <div class='center w1200 clearfix' id="none_png" hidden>
            <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
        </div>
        <table class="fl tab-jsks votab clstab" id="content" hidden>
            <thead>
            <tr>
                <th width="90" rowspan="2">教学班组合</th>
                <th width="120" rowspan="2">学生来源</th>
                <th width="" colspan="3">教学班信息</th>
                <th width="" colspan="3">相交教学班信息</th>
                <th width="100">操作</th>
            </tr>
            <tr>
                <th width="210">教学班<em id="count"></em></th>
                <th width="130">教室<em></em></th>
                <th width="130">教师<em></em></th>
                <th width="130">教学班<em id="count1"></em></th>
                <th width="130">教室<em></em></th>
                <th width="130">教师<em></em></th>
                <th width="110"><em></em></th>
            </tr>
            </thead>
            <tbody id="jxbCombine">

            </tbody>
            <script type="text/template" id="jxbCombineList">
                {{~it:value:index}}
                <tr>
                    {{~value:jxb:it}}
                    {{?jxb.zuHeName != undefined}}
                    <td class="tdName xjJxb" rowspan={{=jxb.rowspan}} ids="{{=jxb.id}}" zuHeName="{{=jxb.zuHeName}}"
                        count="{{=jxb.count}}">
                        {{=jxb.name}}
                        {{?jxb.stuCount != undefined}}
                        （{{=jxb.stuCount}}人）
                        {{?}}
                    </td>
                    {{?}}
                    {{?jxb.zuHeName == undefined}}
                    <td class="tdName" rowspan={{=jxb.rowspan}} ids="{{=jxb.id}}" roomId="{{=jxb.roomId}}"
                        teaId="{{=jxb.teaId}}" subId="{{=jxb.subId}}" jxbid="{{=jxb.jxbId}}" count="{{=jxb.count}}">
                        {{=jxb.name}}
                        {{?jxb.stuCount != undefined}}
                        （{{=jxb.stuCount}}人）
                        {{?}}
                    </td>
                    {{?}}
                    {{~}}
                    <td></td>
                </tr>
                {{~}}
            </script>
        </table>
    </div>
</div>
</div>

<!--设置老师-->
<div class="popup setp-popup">
    <div class="popup-top">
        <em>设置老师</em>
        <i class="fr close"></i>
    </div>
    <div class="setp-d">
        <select class="selt" id="teacher">

        </select>
        <script type="text/template" id="teacher_temp">
            {{~it:value:index}}
            <option value="{{=value.id}}">{{=value.name}}</option>
            {{~}}
        </script>
    </div>
    <div class="popup-btn">
        <button class="ss" id="set_teacher">设置</button>
        <button class="qxx">取消</button>
    </div>
</div>

<div class="popup selp-popup">
    <div class="popup-top">
        <em>选择教室</em>
        <i class="fr close"></i>
    </div>
    <div class="setp-d">
        <select class="selt" id="room">

        </select>
        <script type="text/template" id="room_temp">
            {{~it:value:index}}
            <option value="{{=value.id}}">{{=value.name}}</option>
            {{~}}
        </script>
    </div>
    <div class="popup-btn">
        <button class="ss" id="set_room">设置</button>
        <button class="qxx">取消</button>
    </div>
</div>

<div class="jhh">
    <div class="jhh-top">数据处理中，请稍等！</div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp">计算冲突并保存数据...</span>
</div>
<div class="dialog">
    <div class="box-s" style="border:1px solid #dfdfdf;border-radius: 8px;width: 450px;height: 410px;background: #FFFFE0;">
            <p style="margin: 15px 0px 0px 20px;color: black;" id="name"></p>
            <div style="margin: 15px 0px 0px 20px">
                <table>
                    <thead>
                    <tr>
                        <th width="133">教学班</th>
                        <th width="133">教室</th>
                        <th width="134">教师</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="jxb" style="overflow-y: auto;height: 300px;width: 425px;margin: 0px 0px 0px 20px">
                <table>
                    <script type="text/template" id="jxb_temp">
                        {{~it:value:index}}
                        <tr>
                            {{?value.nickName != ""}}
                            <td width="133">{{=value.nickName}}</td>
                            {{?}}
                            {{?value.nickName == ""}}
                            <td width="133">{{=value.name}}</td>
                            {{?}}
                            <td width="133">{{=value.classroomName}}</td>
                            <td width="134">{{=value.teacherName}}</td>
                        </tr>
                        {{~}}
                    </script>
                    <tbody id="jxb">

                    </tbody>
                </table>
            </div>
    </div>
</div>

<div class="bg"></div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('jxbcombine', function (jxbcombine) {
        jxbcombine.init();
    });
    //var index='';
    // $(document).on('click','.xjJxb',function (e) {
    //
    //
    // })
</script>
</body>
</html>
