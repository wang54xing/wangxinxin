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

        div .act1 {
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
<div class="center w1300 clearfix">
    <div class="mt20 mb35">
        <input type='hidden' id='gradeId' value='${gradeId}'/>
        <div class="mt20 clearfix w1075">
            <div class="fl w950 sd-btn">
                <button class="cx fr cx1" id="nextStop">下一步排非走班</button>
                <button class="cx fr cx2" id="prevStop">返回上一步</button>
                <button id="cancel" class="cx fr">撤销</button>
                <button id="autoPaiKe" class="cx fr cx3">自动排课</button>
            </div>
            <p class="Ulp fr">时段(<span id="finishTimeComb">0</span>/<span id="totalTimeComb">0</span>)</p>
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
                        </tr>
                        <script type="text/template" id="kbByTea">
                            <td class="gxao">
                                {{for (var i=0;i<7;i++) { }}
                                <div class="zh{{=i + 1}} ke">
                                    {{~it:value:index}}
                                        {{?value.x==i}}
                                            <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} itt tb-clsrom"
                                                 x="{{=value.x}}" y="{{=value.y}}">
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
                    <ul id="timeCombList" class="nUl">
                    </ul>
                    <script type="text/template" id="timeCombList_templ">
                        {{~it:value:index}}
                            <li ty="{{=value.type}}" nm="{{=value.name}}" srl="{{=value.serial}}" {{?value.isFinish==1}}class="finish"{{?}}>
                                <div class="inline bL fl sd70">
                                    <p>{{=value.name}}</p>
                                </div>
                                <div class="inline wi39">
                                    <span>(<em class="finishKeShi">{{=value.finishKeShi}}</em>/<em class="maxKeShi">{{=value.maxKeShi}}</em>)</span>
                                </div>
                            </li>
                        {{~}}
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('autoTimeCombKeBiao', function (autoTimeCombKeBiao) {
        autoTimeCombKeBiao.init();
    });
</script>
</body>
</html>
