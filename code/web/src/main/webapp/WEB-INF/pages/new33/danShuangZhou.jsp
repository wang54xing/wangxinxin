<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>单双周课</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <style>
        .w217 {
            width: 217px;;
        }

        .w218 {
            width: 218px;;
        }

        .ha210 {
            height: 210px;
            width: 455px;
            overflow-y: auto;
        }

        .w240 {
            width: 238px;
        }

        .mtr26 {
            margin: 9px 26px;
        }

        .sel2 {
            width: 145px;
            height: 30px;
            border: 1px solid #9a9a9a;
            border-radius: 5px;
        }

        .w239 {
            width: 238px;;
        }

        .tabSty tr th, .tabSty tr td {
            height: 40px;
            text-align: center;
            border: 1px solid #969696;
        }

        .tabSty tr th {
            background: #ededed;
            color: black;
            border-bottom: none !important;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $(".right-pos a").eq(1).attr("class", "vz2");
            $('.t-down').click(function () {
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top', -91)
            })
            $('.t-top').click(function () {
                $('.td-over').css('margin-top', 0)
            })
        })
        $(function () {
            $('.close,.qx').click(function () {
                $('.bg').hide();
                $('.xz-popup').hide();
            })
        })
    </script>
    <style type="text/css">
    </style>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<span id="defaultTerm"></span>

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
            <%--<a href="/newisolatepage/jxbcombine.do" class="jxb1">教学班组合</a>--%>
            <a href="/newisolatepage/jxbTimeCombine.do" class="jxb1">分时段组合</a>
            <a href="/newisolatepage/paiKeZuHe.do" class="vpk1">排课组合</a>
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
                <span class="fl">学科：</span>

                <div class="d-sty d-ssty fl" id="subject">

                </div>
                <script type="text/template" id="subject_temp">
                    {{~it:value:index}}
                    {{? value.type == 1||value.type1 == 1}}
                    <em val="{{=value.subid}}" class="sp">{{=value.snm}}</em>
                    {{?}}
                    {{~}}
                </script>
            </li>
        </ul>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="mt20 clearfix center fl">
        <select class="sel1 ml0" id="classId">

        </select>
        <script type="text/template" id="class_Item">
            {{~it:value:index}}
            <option value="{{=value.classId}}" class="f4s">{{=value.gname}}（{{=value.xh}}）班</option>
            {{~}}
        </script>
        <script type="text/template" id="sub_Item">
            {{~it:value:index}}
            {{? value.type == 1||value.type1 == 1}}
            <option value="{{=value.subid}}">{{=value.snm}}</option>
            {{?}}
            {{~}}
        </script>
        <button class="chat-bt btn fr">+单双周排课</button>
    </div>
    <div class="mt20 cur-tab cou-tab ccou-tab center fl w1200">
        <table class="tabSty">
            <thead>
            <tr>
                <th width="198">行政班</th>
                <th width="198">单周学科</th>
                <th width="198">双周学科</th>
                <th width="198">教室</th>
                <th width="198">交换单双周学科位置</th>
                <th width="202">操作</th>
            </tr>
            </thead>
        </table>
        <div class="ha410" style="width: 1216px;">
            <table class="tabSty">
                <script type="text/template" id="bd">
                    {{~it:value:index}}
                    <tr>
                        <td width="198">
                            <em class="inline">{{=value.grade}}（{{=value.xh}}）班</em>
                        </td>
                        <td width="198">
                            <em class="inline">
                                <select class="sel2 fwo ft1" subid="{{=value.dan}}" cid="{{=value.cid}}">
                                </select>
                            </em>
                        </td>
                        <td width="198">
                            <em class="inline">
                                <select class="sel2 fwo ft2" subid="{{=value.shuang}}" cid="{{=value.cid}}">
                                </select>
                            </em>
                        </td>
                        <td width="198">
                            <em class="inline">
                                {{=value.room}}
                            </em>
                        </td>
                        <td width="198">
                            <em class="inline jiaohuan ccur" dan="{{=value.danId}}" shuang="{{=value.shuangId}}">
                                交换
                            </em>
                        </td>
                        <td width="202">
                            <em class="inline del ccur" cid="{{=value.cid}}" dan="{{=value.dan}}"
                                shuang="{{=value.shuang}}">删除</em>
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
</div>
<div class="bg"></div>
<div class="popup xz-popup xzz">
    <div class="popup-top">
        <em>选择班级</em>
        <i class="close fr"></i>
    </div>
    <div class="mtr26">
        <table class="tabSty">
            <thead>
            <tr>
                <th width="220">班级</th>
                <th width="220">选择
                    <input type="checkbox" name="" id="" class="gg1">
                    <em>全选</em>
                </th>
            </tr>
            </thead>
        </table>
        <div class="ha210">
            <table class="tabSty" id="clsList">
                <tbody id="csList">

                </tbody>
                <script type="text/template" id="clList">
                    {{~it:value:index}}
                    <tr>
                        <td>
                            <em class="inline w217">{{=value.gname}}（{{=value.xh}}）班</em>
                        </td>
                        <td>
                            <em class="inline w218">
                                <em ids="{{=value.classId}}" class="fks rRight">
                                </em>
                            </em>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
    <div class="popup-btn mt30">
        <button class="qr" id="addDanShuang">确认</button>
        <button class="qx">取消</button>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('danShuangZhou', function (danShuangZhou) {
        danShuangZhou.init();
    });
</script>
</body>
</html>
