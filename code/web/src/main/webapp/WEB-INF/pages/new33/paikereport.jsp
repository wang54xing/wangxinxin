<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/3/24
  Time: 9:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>排课报告</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
    <link rel="stylesheet" href="/static_new/css/new33/reset.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static/plugins/echarts/echarts-all.js"></script>

</head>
<body ciId="${ciId}">
<div class="mb35">
    <div class="header">
        <div class="head-cont clearfix">
            <p style="font-size: 32px;display: inline-block;color: white;font-family: KaiTi;width: 1090px;text-align: center;margin-top: 26px;" id="title"></p>
            <button class="dc-btn fr mt25" hidden>导出数据</button>
        </div>
    </div>
    <div class="center w1200">
        <div class="mt25 clearfix">
            <div class="fr">
                年级
                <select class="sel1 ml15" id="grade">

                </select>
                <script type="text/template" id="grade_temp">
                    {{~it:value:index}}
                    <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
                    {{~}}
                </script>
            </div>
        </div>
        <%--<div class="mt25">
            <p class="tx">选科结果</p>
        </div>
        <div class="mt25">
            <p class="tx">学生选科完成度</p>
        </div>--%>
        <div class="mt25">
            <p class="tx">教学班及课时</p>
            <span class="block f18 mt20 c00">整体</span>
            <div class="ha1200">
                <div id="main1" style="width: 1200px;height:400px;"></div>
            </div>
            <div class="ha1200">
                <div id="main2" style="width: 1200px;height:400px;"></div>
            </div>
            <div class="ha1200">
                <div id="main3" style="width: 1200px;height:400px;"></div>
            </div>
            <div class="ha1200">
                <div id="main4" style="width: 1200px;height:400px;"></div>
            </div>
            <table class="tabSty mt20">
                <thead>
                <th width="220">学科</th>
                <th width="180">类型</th>
                <th width="160">教学班数量</th>
                <th width="160">总人数</th>
                <th width="160">人数/每班</th>
                <th width="160">课时</th>
                <th width="150">总课时</th>
                </thead>
                <tbody id="zhengti1">

                </tbody>
                <script type="text/template" id="zhengti1_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.subname}}</td>
                        <td>{{=value.type}}</td>
                        <td>{{=value.jxbnum}}</td>
                        <td>{{=value.stunum}}</td>
                        <td>{{=value.stunumjxbnum}}</td>
                        <td>{{=value.ksnum}}</td>
                        <td>{{=value.zongks}}</td>
                    </tr>
                    {{~}}
                </script>
            </table>
            <table class="tabSty mt30">
                <thead>
                <th width="400">类型</th>
                <th width="400">教学班数量</th>
                <th width="400">课时总数</th>
                </thead>
                <tbody id="zhengti2">
                <tr>
                    <td>非走班</td>
                    <td name="feizoubannum"></td>
                    <td name="feizoubanhour"></td>
                </tr>
                <tr>
                    <td>走班</td>
                    <td name="zoubannum"></td>
                    <td name="zoubanhour"></td>
                </tr>
                <tr>
                    <td>自习课</td>
                    <td name="zixinum"></td>
                    <td name="zixinum"></td>
                </tr>
                </tbody>
            </table>
            <span class="block f18 mt20 c00">走班整体</span>
            <table class="tabSty mt30">
                <thead>
                <th width="240">走班总数量</th>
                <th width="240">教师数量</th>
                <th width="240">走班数/教师</th>
                <th width="240">总课时</th>
                <th width="240">课时总数</th>
                </thead>
                <tbody id="zouban1">
                <tr>
                    <td name="zoubannum"></td>
                    <td name="teanum"></td>
                    <td name="ratio"></td>
                    <td></td>
                    <td name="zoubanhour"></td>
                </tr>
                </tbody>
            </table>
            <table class="tabSty mt30">
                <thead>
                <th width="200">学科</th>
                <th width="200">走班总数量</th>
                <th width="200">教师数量</th>
                <th width="200">跨头教师数量</th>
                <th width="200">走班数/教师</th>
                <th width="200">总课时</th>
                <th width="200">课时/教师</th>
                </thead>
                <tbody id="zouban2">

                </tbody>
                <script type="text/template" id="zouban2_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.subname}}</td>
                        <td>{{=value.zoubannum}}</td>
                        <td>{{=value.teanum}}</td>
                        <td>{{=value.kuatou}}</td>
                        <td>{{=value.zoubannumteanum}}</td>
                        <td>{{=value.zoubanhour}}</td>
                        <td>{{=value.zoubanhourteanum}}</td>
                    </tr>
                    {{~}}
                </script>
            </table>
            <table class="tabSty mt30">
                <thead>
                <th width="200">学科</th>
                <th width="200">类型</th>
                <th width="200">总人数</th>
                <th width="200">班级最多人数</th>
                <th width="200">班级最少人数</th>
                <th width="200">班级平均人数</th>
                </thead>
                <tbody id="zouban3">

                </tbody>
                <script type="text/template" id="zouban3_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.subname}}</td>
                        <td>{{=value.type}}</td>
                        <td>{{=value.stunum}}</td>
                        <td>{{=value.maxnum}}</td>
                        <td>{{=value.minnum}}</td>
                        <td>{{=value.avgnum}}</td>
                    </tr>
                    {{~}}
                </script>

            </table>
            <table class="tabSty mt30">
                <thead>
                <th width="200">组合</th>
                <th width="200">人数</th>
                </thead>
                <tbody id="select_result">

                </tbody>
                <script type="text/template" id="select_result_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.name}}</td>
                        <td>{{=value.sum}}</td>
                    </tr>
                    {{~}}
                </script>

            </table>

            <table class="tabSty mt30">
                <thead>
                <th width="400">组合</th>
                <th width="400">等级考人数</th>
                <th width="400">合格考人数</th>
                </thead>
                <tbody id="select_result2">

                </tbody>
                <script type="text/template" id="select_result2_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.subname}}</td>
                        <td>{{=value.sum}}</td>
                        <td>{{=value.hegenum}}</td>
                    </tr>
                    {{~}}
                </script>

            </table>
        </div>
        <div class="mt25">
            <p class="tx">个人事务</p>
            <table class="tabSty mt20">
                <thead>
                <th width="300">学科</th>
                <th width="300">教师人数</th>
                <th width="300">事务数量</th>
                <th width="300">事务数量/教师</th>
                </thead>
                <tbody id="shiwu">

                </tbody>
                <script type="text/template" id="shiwu_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.subname}}</td>
                        <td>{{=value.teanum}}</td>
                        <td>{{=value.swnum}}</td>
                        <td>{{=value.swnumteanum}}</td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
        <div class="mt25">
            <p class="tx">课表</p>
            <table class="tabSty mt20">
                <thead>
                <th width="300">可用总课节数量</th>
                <th width="300">上午可用课节数</th>
                <th width="300">下午可用课时数</th>
                <th width="300">晚上可用课时数</th>
                </thead>
                <tbody id="kebiao1">
                <tr>
                    <td name="keyong"></td>
                    <td name="swkeyong"></td>
                    <td name="xwkeyong"></td>
                    <td name="wskeyong"></td>
                </tr>
                </tbody>
            </table>
            <table class="tabSty mt20">
                <thead>
                <th width="400">时间段</th>
                <th width="400">非走班课时数/占比</th>
                <th width="400">走班课时数/占比</th>
                </thead>
                <tbody id="kebiao2">
                <tr>
                    <td>上午</td>
                    <td name="zoubanshangwu"></td>
                    <td name="feizoubanshangwu"></td>
                </tr>
                <tr>
                    <td>下午</td>
                    <td name="zoubanxiawu"></td>
                    <td name="feizoubanxiawu"></td>
                </tr>
                <tr>
                    <td>晚上</td>
                    <td name="zoubanwanshang"></td>
                    <td name="feizoubanwanshang"></td>
                </tr>
                </tbody>
            </table>

            <table class="tabSty mt30">
                <thead>
                <th width="250">教室名称</th>
                <th width="250">所属行政班</th>
                <th width="250">使用班级</th>
                <th width="250"> 教室课表占用课时</th>
                <th width="250"> 教室课表占用百分比</th>
                </thead>
                <tbody id="roomkebiao">

                </tbody>
                <script type="text/template" id="roomkebiao_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.name}}</td>
                        <td>{{=value.classname}}</td>
                        <td>
                            {{~value.source:value2:index2}}
                                <span class="inline ml5">{{=value2}}</span>
                            {{~}}
                        </td>
                        <td>{{=value.arrangedTime}}</td>
                        <td>{{=value.ratio}}</td>
                    </tr>
                    {{~}}
                </script>

            </table>
        </div>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('paikereport', function (paikereport) {
        paikereport.init();
    });
</script>
</body>
</html>
