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
    <title class="i18n" name='title'>事务设置</title>
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
    <script type="text/javascript" src="/static/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="/static/js/jquery.listnav.min-2.1.js"></script>
    <script type="text/javascript" src="/static/js/jquery.charfirst.pinyin.js"></script>
    <script type="text/javascript">
        $(function () {
            $(function () {
                $(".right-pos a").eq(5).attr("class", "vs2");

            })
            $('.t-down').click(function(){
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top',-182 )
            })
            $('.t-top').click(function(){
                $('.td-over').css('margin-top',0)
            })
            $('.t-set').click(function () {
                $('.bg').show();
                $('.set-popup').show();
            })

            $('.sww-popup .qx,.sww-popup .fr').click(function () {
                $('.bg').hide();
                $('.sww-popup').hide();
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
            $('.xz-li li').click(function () {
                $(this).addClass('xz-bg').siblings().removeClass('xz-bg');
                $(this).find('i').toggleClass('xz-cur');
            })
        })
    </script>
    <style>
        .td-over{
            margin-top: -91px;
        }
    </style>
</head>
<body>
<div>
    <jsp:include page="head.jsp"></jsp:include>
    <%@ include file="/WEB-INF/pages/new33/BasicDatasideBar.jsp" %>
    <div class="center w1300 clearfix">
        <div class="ul-sty center mt25 fl">
            <ul class="bg-line fl">
                <%--<li class="li-sty">--%>
                    <%--<span class="fl">学期:</span>--%>
                    <%--<script type="text/template" id="term_temp">--%>
                        <%--{{~it:value:index}}--%>
                        <%--<em ids="{{=value.id}}">{{=value.xqnm}}</em>--%>
                        <%--{{~}}--%>
                    <%--</script>--%>
                    <%--<div class="d-sty fl" id="term">--%>
                    <%--</div>--%>
                <%--</li>--%>
                <li class="li-sty">
                    <span class="fl">类型:</span>
                    <script type="text/template" id="sw_temp">
                        {{~it:value:index}}
                        <em ids="{{=value.id}}">{{=value.desc}}</em>
                        {{~}}
                    </script>
                    <div class="d-sty fl" id="swType">
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="comment">
        <div id="com-col">
            <div class="center w1300 clearfix">
                <div class="mt35 center fl"><span class="f18 c-b c-bb" style="line-height: 42px;">校级事务</span>
                    <button class="sel-tea">添加事务</button>
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
                                <th width="300">事务简述</th>
                                <th width="640">相关对象</th>
                                <th width="100">事务级别</th>
                                <th width="160">时间设置</th>
                                <th width="160">操作</th>
                            </tr>
                        </thead>
                    </table>
                        <script type="text/template" id="sw">
                            {{~it:value:index}}
                            <tr>
                                <td><em class="w262">{{=value.desc}}</em></td>
                                <td title="{{=value.userName}}"><em class="w559">{{=value.teaName}}</em><a class="detail" ids="{{=value.id}}">详情</a></td>
                                <td><em class="w89">{{=value.level}}</em></td>
                                <td class="pos">
                                    <div class="inline w142">
                                        <span class="">{{=value.xy}}</span>
                                    </div>
                                </td>
                                <td>
                                    <em class="w142">
                                        <img class="opt optt" ids="{{=value.id}}" src="/static_new/images/new33/editt.png">
                                        <img class="opt opde" ids="{{=value.id}}" src="/static_new/images/new33/dell.png">
                                    </em>
                                </td>
                            </tr>
                            {{~}}
                        </script>
                        <div>
                            <table class="votab">
                                <tbody id="swTbd">

                                </tbody>
                            </table>
                        </div>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="bg"></div>
    <!--新增事务-->
    <div class="popup sww-popup">
        <div class="popup-top">
            <em class="fl"><em id="xb">新增</em>事务</em>
            <i class="fr"></i>
        </div>
        <dl class="popup-dl sw-dl c-b clearfix">
            <dd>
                <span>事务类型</span>
                <select class="sel4" id="swTy">
                </select>
                <script type="text/template" id="swtemp">
                    {{~it:value:index}}
                    <option value="{{=value.id}}">{{=value.desc}}</option>
                    {{~}}
                </script>
               <input class="ml10" type="checkbox" id="stuKe"> 学生自习
            </dd>
            <dd>
                <span>事务简述</span>
                <input type="text" class="inp4">
            </dd>
            <dd>
                <span>事务级别</span>
                <select class="inp9">
                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                    <option>4</option>
                    <option>5</option>
                </select>
            </dd>
            <dd>
                <span>相关对象</span>

                <div class="inline pos">
                    <div class="inline w300 sww tongxun" property="全体人员事务"></div>
                    <button class="t-btn t-tx">通讯录</button>
                </div>
            </dd>
            <dd>
                <span>时间设置</span>

                <div class="inline pos">
                    <div class="inline w300 sww shezhi"></div>
                    <button class="t-btn t-set">设置</button>
                </div>
            </dd>
        </dl>
        <div class="popup-btn mt45">
            <button class="qd" id="baocunShiWu" ids="*">确认</button>
            <button class="qx">取消</button>
        </div>
    </div>
    <div class="popup set-popup">
        <div class="popup-top">
            <em class="fl">设置</em>
            <i class="fr close ffr"></i>
        </div>
        <div class="set-dl cou-tab" style="margin: 20px">

            <div class="ha518" style="height: 425px">
                <table>
                    <thead>
                    <tr>
                        <th width="66"><em class="w66 inline"></em></th>
                        <th width="64"><em class="w64 inline">周一</em></th>
                        <th width="64"><em class="w64 inline">周二</em></th>
                        <th width="64"><em class="w64 inline">周三</em></th>
                        <th width="64"><em class="w64 inline">周四</em></th>
                        <th width="64"><em class="w64 inline">周五</em></th>
                        <th width="64"><em class="w64 inline">周六</em></th>
                        <th width="64"><em class="w64 inline">周日</em></th>
                    </tr>
                    </thead>
                    <tbody id="keShi">

                    </tbody>
                </table>
            </div>
                <script type="text/template" id="KeShiList">
                    {{~it:value:index}}
                    <tr>
                        <td><em class="w66 inline">{{=value.name}}</em></td>
                        <td class="s-cur">
                            <em class="w64 inline"></em>
                        </td>
                        <td class="s-cur">
                            <em class="w64 inline"></em>
                        </td>
                        <td class="s-cur">
                            <em class="w64 inline"></em>
                        </td>
                        <td class="s-cur">
                            <em class="w64 inline"></em>
                        </td>
                        <td class="s-cur">
                            <em class="w64 inline"></em>
                        </td>
                        <td class="s-cur">
                            <em class="w64 inline"></em>
                        </td>
                        <td class="s-cur">
                            <em class="w64 inline"></em>
                        </td>
                    </tr>
                    {{~}}
                </script>

            </table>
        </div>
        <div class="popup-btn mt25">
            <button class="qd qdIndex">确认</button>
            <button class="qxx quanbudou">取消</button>
        </div>
    </div>

    <div class="popup qua-popup">
        <div class="popup-top">
            <em class="fl">通讯录</em>
            <i class="fr fft"></i>
        </div>
        <div class="t-center clearfix">
            <ul id="treeDemo" class="ztree fl"></ul>
            <div class="fr t-right">
                <div id="myList-nav" class="member-select">
                </div>
                <div class="myList">
                    <ul id="myList" class="member-list clearfix">

                    </ul>
                    <script type="text/template" id="TeaLi">
                        {{~it:value:index}}
                        <li class="li-special" uid="{{=value.uid}}" ids="{{=value.id}}" nm="{{=value.unm}}">
                            <img src="http://7xiclj.com1.z0.glb.clouddn.com/d_8_1.png">
                            <span>{{=value.unm}}</span>
                            <em></em>
                        </li>
                        {{~}}
                    </script>
                </div>
            </div>
        </div>
        <div class="popup-btn mt45">
            <button class="qd queDingRen">确定</button>
            <button class="qxx qss">取消</button>
        </div>
    </div>

    <div class="popup tc-popup">
        <div class="popup-top">
            <em class="fl">事务教师列表</em>
            <i class="fr" onclick="closeTcPopup()"></i>
        </div>
        <script type="text/template" id="SWTEALIST">
            {{~it:value:index}}
            <tr><td>{{=value.unm}}</td><td>{{=value.sexStr}}</td><td>{{=value.gradeStr}}</td><td>{{=value.subjectStr}}</td></tr>
            {{~}}
        </script>
        <div class="tb-gp">
            <table>
                <thead>
                <tr><th>姓名</th><th>性别</th><th>年级</th><th>学科</th></tr>
                </thead>
                <tbody id="swta">

                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('baseAffair', function (baseAffair) {
        baseAffair.init();
    });


    function closeTcPopup(){
        $('.tc-popup').hide();
        $('.bg').hide()
    }
</script>
</html>
