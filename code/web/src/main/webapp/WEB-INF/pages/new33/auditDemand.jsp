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

            $('.gd').click(function(){
                if($(this).hasClass('j')){
                    $(this).removeClass('j');
                    $(this).parent().css('height','40');
                    $(this).css('height','40');
                    $(this).text('更多...')
                }else{
                    $(this).addClass('j')
                    var h = $(this).siblings('div').height();
                    $(this).parent().css('height',h);
                    $(this).css('height',h);
                    $(this).text('收缩')
                }
            })
            $('.bc1').click(function(){
                $('.m1').show();
                $('.m2').hide();
                $('.sel1').show();
            })

            $('.prev').click(function(){
                $('.mm2').hide();
                $('.mm1').show();
            })
            $('.table-cell2').click(function(){
                $('.bg').show();
                $('.yy-popup').show();
            })
            $('.qxx,.yy-popup .close').click(function(){
                $('.bg').hide();
                $('.yy-popup').hide();
            })

            $('.pkType').click(function(){
                if(!$(this).hasClass("select")){
                    $(this).addClass("select").siblings().removeClass("select");
                }else{
                    $(this).removeClass("select");
                }

                if($(this).attr("type") == 2){
                    $(this).toggleClass("cb5");
                    $(".pkType").each(function (i,st) {
                        $(this).removeClass("c0a");
                        $(this).removeClass("c27");
                        $(this).removeClass("cef");
                    });
                }
                if($(this).attr("type") == 1){
                    $(this).toggleClass("c0a");
                    $(".pkType").each(function (i,st) {
                        $(this).removeClass("cb5");
                        $(this).removeClass("c27");
                        $(this).removeClass("cef");
                    });
                }
                if($(this).attr("type") == 3){
                    $(this).toggleClass("c27");
                    $(".pkType").each(function (i,st) {
                        $(this).removeClass("cb5");
                        $(this).removeClass("c0a");
                        $(this).removeClass("cef");
                    });
                }
                if($(this).attr("type") == 4){
                    $(this).toggleClass("cef");
                    $(".pkType").each(function (i,st) {
                        $(this).removeClass("cb5");
                        $(this).removeClass("c0a");
                        $(this).removeClass("c27");
                    });
                }
            })

        })
    </script>
    <style>
        .fixed-table-box{
            width: 730px;
            margin: 0px auto;
        }
        .fixed-table-box>.fixed-table_body-wraper{/*内容了表格主体内容有纵向滚动条*/
            max-height: 331px;
        }

        .fixed-table_fixed>.fixed-table_body-wraper{/*为了让两侧固定列能够同步表格主体内容滚动*/
            max-height: 331px;
        }
        .w-100{
            width: 100px;
        }
        .w-150{
            width: 150px;
        }
        .w-120{
            width: 120px;
        }
        .w-300{
            width: 300px;
        }
        .w-100{
            width: 100px;
        }
        .btns{
            text-align: center;
        }
        .btns button{
            padding: 10px 20px;
        }
        .fixed-table-box .table-cell{
            text-align: center;
            padding-left: 0px;
            cursor: pointer;
        }
        .fixed-table-box .table-cell1{
            height: 60px;
            text-align: center;
            padding-left: 0px;
        }
        .fixed-table-box .tbc{
            background: #3cb371;
            color: white!important;
            padding: 5px 0px;
            text-align: center;
        }
        .fixed-table-box .tbc .table-cell{
            cursor: default;
        }
    </style>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>

<style>
    .top-nav > .a7 {
        background: url(/static_new/images/new33/nav7.png) no-repeat center 20px #4740d0;
    }
</style>
<span id="defaultTerm" style="display: none"></span>
<div>
    <div class="clearfix center mb35">
        <div class="mm1">
            <div class="clearfix mt30">
                <ul class="bg-line">
                    <li class="li-sty">
                        <span class="fl">学期：</span>
                        <div class="d-sty fl" id="term">

                        </div>
                        <script type="text/template" id="term_temp">
                            {{~it:value:index}}
                            <em class = "" ids="{{=value.id}}">{{=value.xqnm}}</em>
                            {{~}}
                        </script>
                        <em class="cursor c58 gd">更多...</em>
                    </li>
                    <li class="li-sty">
                        <span class="fl">年级：</span>
                        <div class="d-sty fl" id="grade">

                        </div>
                        <script type="text/template" id="grade_temp">
                            {{~it:value:index}}
                            <em class = "" ids="{{=value.gid}}">{{=value.gnm}}（{{=value.jie}}）</em>
                            {{~}}
                        </script>
                        <em class="cursor c58 gd"></em>
                    </li>
                    <li class="li-sty">
                        <span class="fl">学科：</span>
                        <div class="d-sty fl" id="subject">
                        </div>
                        <script type="text/template" id="subject_temp">
                            {{~it:value:index}}
                            <em class = "" ids="{{=value.subid}}">{{=value.snm}}</em>
                            {{~}}
                        </script>
                        <em class="cursor c58 gd">更多...</em>
                    </li>
                </ul>
            </div>
            <div class="fl w195 mt30">
                <div class="pos">
                    <input class="inp1" type="text" id="teaName">
                    <i class="search pob tr1 cursor" id="search"></i>
                </div>
                <div class="ulSty mt20 f12">
                    <ul id="tea">

                    </ul>
                    <script type="text/template" id="tea_temp">
                        {{~it:value:index}}
                            <li class="" ids = "{{=value.uid}}">
                                <span class="w4" style="width: 14em">{{=value.unm}}</span>
                            </li>
                        {{~}}
                    </script>
                </div>
            </div>
            <div class="fr w1090 mt30">
                <div class="clearfix">
                    <div class="fl">
                        <button class="btn bc1">列表模式</button>
                        <button class="btn bc2 ml5">课表模式</button>
                        <button class="btn bc3 ml5">导出</button>
                        <button class="btn bc4 ml5">打印</button>
                    </div>
                    <div class="fr">
                        <select class="sel1 fl" id="status">
                            <option value="0">全部类型</option>
                            <option value="1">必须排课</option>
                            <option value="2">优先排课</option>
                            <option value="3">不排课</option>
                            <option value="4">避免排课</option>
                        </select>
                        <button class="btn bc5 ml5 newAdd">新增需求</button>
                    </div>
                </div>
                <div class="mt23 d2">
                    <div class="m1">
                        <table class="tab">
                            <thead>
                            <tr>
                                <th><span class="inline w130">教师姓名</span></th>
                                <th><span class="inline w100">性别</span></th>
                                <th><span class="inline w140">排课时间</span></th>
                                <th><span class="inline w130">要求</span></th>
                                <th><span class="inline w300">描述&原因</span></th>
                                <th><span class="inline w115">审批状态</span></th>
                                <th><span class="inline w170">操作</span></th>
                            </tr>
                            </thead>
                        </table>
                        <div class="w1110 h585">
                            <table class="tab w1092">
                                <tbody id="teaRulesList">
                                </tbody>
                                <script type="text/template" id="teaRulesList_temp">
                                    {{~it:value:index}}
                                    {{~value.teaRulesList:teaRules:it}}
                                    {{? it == 0}}
                                    <tr>
                                        <td rowspan="{{=value.size}}" class="w130">
                                            <span class="inline w130">{{=value.teaName}}</span>
                                        </td>

                                        <td rowspan="{{=value.size}}" class="w100">
                                            <span class="inline w100">{{=value.sexStr}}</span>
                                        </td>

                                        <td class="w140">
                                            <span class="inline w140">周{{=teaRules.x + 1}}第{{=teaRules.y + 1}}节</span>
                                        </td>
                                        <td class="130">
                                            <span class="inline w130">{{=teaRules.statusDesc}}</span>
                                        </td>

                                        <td class="w300 pos">
                                            <span class="inline w300">
                                                <span class="inline w265 oh">{{=teaRules.desc}}</span>
                                                <i class="c58 cursor oh inline i1">
                                                    详情
                                                     <div class="pob d3 t40">{{=teaRules.desc}}</div>
                                                </i>
                                            </span>
                                        </td>
                                        {{?teaRules.require == 0}}
                                        <td class="w115">
                                            <span class="inline w115">未审批</span>
                                        </td>
                                        {{?}}
                                        {{?teaRules.require == 1}}
                                        <td class="w115">
                                            <span class="inline w115">已同意</span>
                                        </td>
                                        {{?}}
                                        {{?teaRules.require == 2}}
                                        <td class="w115">
                                            <span class="inline w115">已拒绝</span>
                                        </td>
                                        {{?}}
                                        <td class="170">
                                            <span class="inline w170">
                                                <em class="cursor c4e agree" require="{{=teaRules.require}}" ids="{{=value.id}}" eachId = {{=teaRules.id}}>同意</em>
                                                <em class="cursor cf1 ml10 refuse" require="{{=teaRules.require}}" ids="{{=value.id}}" eachId = {{=teaRules.id}}>拒绝</em>
                                            </span>
                                        </td>
                                    </tr>
                                    {{?}}
                                    {{? it != 0}}
                                    <tr>
                                        <td class="w140">
                                            <span class="inline w140">周{{=teaRules.x + 1}}第{{=teaRules.y + 1}}节</span>
                                        </td>
                                        <td class="130">
                                            <span class="inline w130">{{=teaRules.statusDesc}}</span>
                                        </td>

                                        <td class="415 pos">
                                            <span class="inline w300">
                                                <span class="inline w265 oh">{{=teaRules.desc}}</span>
                                                <i class="c58 cursor oh inline i1">
                                                    详情
                                                     <div class="pob d3 t40">{{=teaRules.desc}}</div>
                                                </i>
                                            </span>
                                        </td>
                                        {{?teaRules.require == 0}}
                                        <td class="w115">
                                            <span class="inline w115">未审批</span>
                                        </td>
                                        {{?}}
                                        {{?teaRules.require == 1}}
                                        <td class="w115">
                                            <span class="inline w115">已同意</span>
                                        </td>
                                        {{?}}
                                        {{?teaRules.require == 2}}
                                        <td class="w115">
                                            <span class="inline w115">已拒绝</span>
                                        </td>
                                        {{?}}
                                        <td class="170">
                                            <span class="inline w170">
                                                <em class="cursor c4e agree" require="{{=teaRules.require}}" ids="{{=value.id}}" eachId = {{=teaRules.id}}>同意</em>
                                                <em class="cursor cf1 ml10 refuse" require="{{=teaRules.require}}" ids="{{=value.id}}" eachId = {{=teaRules.id}}>拒绝</em>
                                            </span>
                                        </td>
                                    </tr>
                                    {{?}}
                                    {{~}}
                                    {{~}}
                                </script>
                            </table>
                        </div>
                    </div>
                    <div class="m2">
                        <table class="tabSty1 mt1 fl">
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
                            <tbody id="kbModel">

                            </tbody>
                            <script type="text/template" id="kbModel_temp">
                                {{~it:value:index}}
                                <tr>
                                    <td>
                                        <em>{{=value.name}}</em><br> <em>{{=value.start}}-{{=value.end}}</em>
                                    </td>
                                    <td class="kbms"></td>
                                    <td class="kbms"></td>
                                    <td class="kbms"></td>
                                    <td class="kbms"></td>
                                    <td class="kbms"></td>
                                    <td class="kbms"></td>
                                    <td class="kbms"></td>
                                </tr>
                                {{~}}
                            </script>
                        </table>
                        <dl class="fr d2" id="xyRules">
                        </dl>
                        <script type="text/template" id="xyRules_temp">
                            {{~it:value:index}}
                            {{~value.teaRulesList:teaRules:it}}
                            <dd class='{{=teaRules.color}}'><span class="block">{{=value.teaName}}</span><span class="block">{{=teaRules.statusDesc}}</span></dd>
                            {{~}}
                            {{~}}
                        </script>
                    </div>
                </div>
            </div>
        </div>
        <div class="mm2">
            <div class="center3">
                <div class="clearfix">
                    <div class="mt20 c00 ml10 cursor prev">
                        &lt;返回
                    </div>
                    <div class="fl w195 mt20 ml10">
                        <select class="sel2" id="subject1">
                        </select>

                        <script type="text/template" id="subject_temp1">
                            {{~it:value:index}}
                            <option value="{{=value.subid}}">{{=value.snm}}</option>
                            {{~}}
                        </script>
                        <div class="ulSty mt20 f12 h371">
                            <ul id="tea1">

                            </ul>
                            <script type="text/template" id="tea_temp1">
                                {{~it:value:index}}
                                    <li class="" ids = "{{=value.uid}}">
                                        <span class="w4" style="width: 14em">{{=value.unm}}</span>
                                    </li>
                                {{~}}
                            </script>
                        </div>
                    </div>
                    <div class="fr w760">
                        <div class="clearfix mt20">
                            <span class="f20">李静</span>
                            <div class="fr">
                                <button class="btn bc6 pkType" type="2">优先排课</button>
                                <button class="btn bc7 pkType" type="1">必须排课</button>
                                <button class="btn bc8 pkType" type="3">不排课</button>
                                <button class="btn bc50 mr25 pkType" type="4">避免排课</button>
                            </div>
                        </div>
                        <div class="mt20">
                            <label id="grade1">
                                <%--<input type="checkbox">dsfa--%>
                            </label>
                            <script type="text/template" id="grade_temp1">
                                {{~it:value:index}}
                                <input type="checkbox" ids="{{=value.gid}}" class="gradeCheck"><span>{{=value.gnm}}({{=value.jie}})</span>&nbsp;&nbsp;
                                {{~}}
                            </script>
                        </div>
                        <div class="w730 mr25 oh mt20">
                            <div class="fixed-table-box row-col-fixed">
                                <!-- 表头 start -->
                                <div class="fixed-table_header-wraper w710">
                                    <table class="fixed-table_header" cellspacing="0" cellpadding="0" border="0">
                                        <thead>
                                        <tr>
                                            <th class="w-100 tbc" data-fixed="true"><div class="table-cell">课节/日期</div></th>
                                            <th class="w-120 tbc"><div class="table-cell">周一</div></th>
                                            <th class="w-120 tbc"><div class="table-cell">周二</div></th>
                                            <th class="w-120 tbc"><div class="table-cell">周三</div></th>
                                            <th class="w-120 tbc"><div class="table-cell">周四</div></th>
                                            <th class="w-120 tbc"><div class="table-cell">周五</div></th>
                                            <th class="w-120 tbc"><div class="table-cell">周六</div></th>
                                            <th class="w-120 tbc"><div class="table-cell">周日</div></th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                                <!-- 表头 end -->
                                <!-- 表格内容 start -->
                                <div class="fixed-table_body-wraper">
                                    <table class="fixed-table_body" cellspacing="0" cellpadding="0" border="0">
                                        <tbody id="tbd">

                                        </tbody>
                                        <script type="text/template" id="KeShiList">
                                            {{~it:value:index}}
                                            <tr>
                                                <td class="w-100 tbc">
                                                    <div class="table-cell table-cell1 tbc">
                                                        <em class="block">{{=value.name}}<em class="block">{{=value.start}}-{{=value.end}}</em></em>
                                                    </div>
                                                </td>
                                                <td class="w-120 ttd1 ttc"><div class="table-cell table-cell2 textCenter"></div></td>
                                                <td class="w-120 ttd1 ttc"><div class="table-cell table-cell2 textCenter"></div></td>
                                                <td class="w-120 ttd1 ttc"><div class="table-cell table-cell2 textCenter"></div></td>
                                                <td class="w-120 ttd1 ttc"><div class="table-cell table-cell2 textCenter"></div></td>
                                                <td class="w-120 ttd1 ttc"><div class="table-cell table-cell2 textCenter"></div></td>
                                                <td class="w-120 ttd1 ttc"><div class="table-cell table-cell2 textCenter"></div></td>
                                                <td class="w-120 ttd1 ttc"><div class="table-cell table-cell2 textCenter"></div></td>
                                            </tr>
                                            {{~}}
                                        </script>
                                    </table>
                                </div>
                                <!-- 表格内容 end -->

                                <!-- 固定列 start -->
                                <div class="fixed-table_fixed fixed-table_fixed-left">
                                    <div class="fixed-table_header-wraper">
                                        <table class="fixed-table_header" cellspacing="0" cellpadding="0" border="0">
                                            <thead>
                                            <tr>
                                                <th class="w-100 tbc"><div class="table-cell">课节/日期</div></th>
                                            </tr>
                                            </thead>
                                        </table>
                                    </div>

                                    <div class="fixed-table_body-wraper">
                                        <table class="fixed-table_body" cellspacing="0" cellpadding="0" border="0">
                                            <tbody id="tbd1">

                                            </tbody>
                                            <script type="text/template" id="KeShiList1">
                                                {{~it:value:index}}
                                                <tr>
                                                    <td class="w-100 tbc">
                                                        <div class="table-cell table-cell1 tbc">
                                                            <em class="block">{{=value.name}}<em class="block">{{=value.start}}-{{=value.end}}</em></em>
                                                        </div>
                                                    </td>
                                                </tr>
                                                {{~}}
                                            </script>
                                        </table>
                                    </div>
                                </div>
                                <!-- 固定列 end -->
                                <div class="fixed-table-box_fixed-right-patch" style="width: 20px; height: 39px;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
<script>
    //初始化FixedTable
    $(".fixed-table-box").fixedTable();
</script>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('auditDemand', function (auditDemand) {
        auditDemand.init();
    });
</script>
</body>
</html>
