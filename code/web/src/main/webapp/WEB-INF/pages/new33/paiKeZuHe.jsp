<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/5/9
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>排课组合</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <style>

        .tf-ul li .ovh{
            width: 135px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow:ellipsis;
        }
        .quedingren {
            cursor: not-allowed;
        }

        .w220 {
            width: 220px;;
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

        .mtr23 {
            margin: 9px 23px;
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

        .w480 {
            width: 480px;
        }

        .w239 {
            width: 238px;;
        }

        .w294 {
            width: 294px;;
        }

        .w199 {
            width: 199px;
        }

        .w197 {
            width: 197px;
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

        .loading-p {
            display: none;
            z-index: 999999999;
            background: transparent;
        }

        div .active1 {
            background: #AFEEEE;
        }

        .innp1 {
            width: 180px;
            height: 28px;
        }

        .bor {
            border: 1px solid #dfdfdf;
        }
        .cx{
            width: 105px;
            height: 38px;
            background: #2e94db;
            border: 1px solid #2e94db;
            border-radius: 5px;
            color: white;
        }
    </style>
    <script type="text/javascript">
        $(function () {
//            $(".right-pos a").eq(4).attr("class", "vpk2");
            $('.cctv').click(function () {
                $(this).find('i').toggleClass('rr-t').toggleClass('rr-d');
                $(this).siblings('ul').toggle();
                $(this).addClass('tf-li').parent().siblings().find('li').removeClass('tf-li')
            })
            $(".chat-ff").click(function () {
                $("#zuHeName").val("")
                $('.bg').show();
                $('.addP-popup').show();
            })
            $('.popup-top i,.qx').click(function () {
                $('.bg').hide();
                $('.addP-popup').hide();
            })
            $('body').on('click','.paik',function(){
                $('.m1').hide();
                $('.m2').show();
            })
            $('.Ulprev').click(function(){
                $('.m1').show();
                $('.m2').hide();
                $(".ra1").each(function () {
                    $(this).prop("checked",false);
                });
            })
        })
    </script>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
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
               <%--<a href="/newisolatepage/conflict.do" class="ve1">冲突检测</a>--%>
            <a href="/newisolatepage/paiKeZuHe.do" class="vpk2">排课组合</a>
        </div>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="m1 fl m1" id="GDGContent">
        <div class="mt20 clearfix center fl">
            <select class="sel1 ml0 jxbType" style="width: 201px" hidden>
                <%--<option value="1">等级型</option>--%>
                <%--<option value="2">合格型</option>--%>
                <option value="3">行政型</option>
            </select>

            <div class="inline w970 fr">
                <button class="chat-bt btn fl" id="update">添加至&gt;&gt;</button>
                <button class="chat-ff btn3 fr">+新建排课组合</button>
                <button class="chat-00 btn3 fr mr20" id="ziDongZuhe">自动构建排课组合</button>
                <select class="fr sel1 mr20 grade">
                </select>
                <script type="text/template" id="grade_temp">
                    {{~it:value:index}}
                    <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
                    {{~}}
                </script>
            </div>
        </div>
        <div class="mt20 cur-tab cou-tab ccou-tab center fl">
            <div class="fl w200 tf-left h588" id="ulList" >

            </div>
            <script type="text/template" id="viList">
                {{~it:value:index}}
                <ul class="ul-list">
                    <li class="cctv">
                        <span style="font-weight: bold">{{=value.name}}</span>
                        <i class="rr-t"></i>
                    </li>
                    <ul class="tf-ul">
                        {{~value.jxbList:obj:itt}}
                        {{?obj.zuHeRepeatFlag ==true}}
                        <li>
                            <em class="cfBiao"></em>
                            <span class="ovh" title="{{=obj.name}}-{{=obj.teacherName}}">{{=obj.name}}-{{=obj.teacherName}}</span>
                            <i class="jxbId" ids="{{=obj.id}}"></i>
                        </li>
                        {{?}}
                        {{?obj.zuHeRepeatFlag !=true}}
                        <li>
                            <span class="ovh" title="{{=obj.name}}-{{=obj.teacherName}}">{{=obj.name}}-{{=obj.teacherName}}</span>
                            <i class="jxbId" ids="{{=obj.id}}"></i>
                        </li>
                        {{?}}
                        {{~}}
                    </ul>
                </ul>
                {{~}}
            </script>
            <div class='center w1200 clearfix' id="none_png" hidden>
                <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
            </div>
            <div class="cou-tab w970 fr" id="content" hidden>
                <table>
                    <thead>
                    <tr>
                        <th width="120">排课组合名</th>
                        <th width="660">教学班列表</th>
                        <th width="70">选中</th>
                        <th width="120">操作</th>
                    </tr>
                    </thead>
                    <script type="text/template" id="ZuHe_temp">
                        {{~it:value:index}}
                        <tr>
                            <td>{{=value.name}}</td>
                            <td>
                                {{~value.jxbList:obj:itt}}
                                <span class="spw inline inline-jxb" ids="{{=obj.id}}" zuHeId="{{=obj.zuHeId}}">{{=obj.jxbName}}-{{=obj.TeaName}}<i class="inline rrow" ids="{{=obj.id}}" zuHeId="{{=obj.zuHeId}}"></i></span>
                                {{~}}
                            </td>
                            <td>
                                <input name="na" class="ra1" type="radio" ids = "{{=value.id}}">
                            </td>
                            <td>
                                <em style="cursor: pointer"  class="paik" ids="{{=value.id}}" id="paiKe" zuHename = "{{=value.name}}">排课</em>
                                <em style="cursor: pointer" ids="{{=value.id}}" class="delZu">删除</em>
                            </td>
                        </tr>
                        {{~}}
                    </script>
                    <tbody id="ZuHeList">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="m2 mt20 mb35">
        <div class="mt20 clearfix w1075">
            <div class="fl w950">
                <a class="cursor Ulprev">&lt;返回</a>
                <button class="cx fr" id="cancel">撤销</button>
            </div>
            <p class="Ulp fr" id="zuHeName1"></p>
        </div>
        <div>
            <div class="fl w980">
                <table class="tab1 taBb2 mt10 fl">
                    <thead>
                        <tr>
                            <th>
                                <div class="wd100">课节 / 日</div>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="psre1 ke kejie">
                            </td>
                            <script type="text/template" id="ks_temp">
                                {{~it:value:index}}
                                <div class="ke{{=value.serial}} kejiecount"><span class="inline mt12">{{=value.name}}</span><br><em>{{=value.start}}-{{=value.end}}</em>
                                </div>
                                {{~}}
                            </script>
                        </tr>
                    </tbody>
                </table>
                <div style="width: 831px;overflow-x: auto;float: left;">
                 <table class="tab1 mt10" id="tabzuHe">
                    <thead>
                        <tr class="weeks">
                        </tr>
                        <script type="text/template" id="weeks_temp">
                            <th>
                                <div class="zhou">
                                {{~it:value:index}}
                                    <div class="zh{{=value.type}} gradeweek">{{=value.name}}</div>
                                {{~}}
                                </div>
                            </th>
                        </script>
                    </thead>
                    <tbody>
                        <tr class="jxbkj">
                            <%--<td>
                                <em class="inline hzb"></em>
                                <span class="c49">组合1</span>
                            </td>
                            <td class="cc3">
                                <span class="red">午餐</span>
                            </td>
                            <td></td>
                            <td class="cc3"></td>
                            <td></td>
                            <td class="cc3"></td>
                            <td></td>--%>
                        </tr>
                        <script type="text/template" id="kbByTea">
                            <td class="gxao">
                                {{for (var i=0;i<7;i++) { }}
                                <div class="zh{{=i + 1}} ke">
                                    {{~it:value:index}}
                                    {{?value.x==i}}
                                    <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} itt tb-clsrom"
                                         ondrop="drop(event,this)" ondragover="allowDrop(event)"
                                         draggable="false" ondragstart="drag(event, this)">
                                    </div>
                                    {{?}}
                                    {{~}}
                                </div>
                                {{}}}
                            </td>
                        </script>

                    </tbody>
                </table>
                </div>
            </div>
            <div class="fl ml25 mt12">
                <div class="Uld">
                    <ul class="nUl" id="jxbs">
                    </ul>
                    <script type="text/template" id="zuHeJXBListTemp">
                        {{~it:value:index}}
                        <li class="jxbs" ids = "{{=value.id}}">
                            <div class="inline bL fl">
                                <p>{{?value.nickName!=''}}
                                    {{=value.nickName}}
                                    {{??}}
                                    {{=value.name}}
                                    {{?}}
                                </p>
                                <p>{{=value.teacherName}}</p>
                                <p>{{=value.classroomName}}</p>
                            </div>
                            <div class="inline wi39">
                                <span class=""><em class="ypks" ypks = {{=value.ypksCount}}>{{=value.ypksCount}}</em>/<em class="zks" zks="{{=value.zksCount}}">{{=value.zksCount}}</em></span>
                            </div>
                        </li>
                        {{~}}
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="bg"></div>
<!--新增排课组合-->
<div class="popup addP-popup">
    <div class="popup-top">
        <em class="fl">新增排课组合</em>
        <i class="fr"></i>
    </div>
    <div class="mmt55">
        <input class="inp30" type="text" id="zuHeName" placeholder="请输入组合名">
    </div>
    <div class="popup-btn">
        <button class="qd" id="addDto">确定</button>
        <button class="qx">取消</button>
    </div>
</div>
<div class="jhh">
    <div class="jhh-top">数据处理中，请稍等！</div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp"></span>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('paiKeZuHe', function (paiKeZuHe) {
        paiKeZuHe.init();
    });
</script>
</body>
</html>
