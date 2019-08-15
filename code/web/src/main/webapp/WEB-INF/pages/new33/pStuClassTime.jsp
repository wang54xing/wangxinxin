<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/3/23
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>学生课表</title>
    <meta charset="utf-8">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/table2.css">
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <style type="text/css">
        * {
            -moz-user-select: none;
            -webkit-user-select: none;
        }
        .right-pos{
            top: -95px;
        }
        .ul-main li {
            border-bottom: 1px dashed #d1d1d1;
            line-height: 40px;
            /*height: 40px;*/
            margin: 0;
            width: 1199px;
        }

        .ul-main > li > label {
            border-radius: 3px;
            height: 26px;
            margin: 7px 30px 0 0;
            line-height: 26px;
        }

        .ul-main > li > label.active {
            color: #388CE9;
            font-weight: bold;
        }

        .tab1 td:first-child {
            background: #3CB371;
            font-weight: bold;
            color: #fff;
        }

        td.ke {
            width: 120px;
        }

        .tab1 th {
            background: #3CB371 !important;
            font-weight: bold;
            color: #fff;
        }

        .tab1 {
            float: left;
        }

        td > div {
            width: 100px;
            line-height: 30px;
            float: left;
        }

        td > div > div {
            width: 100px;
            height: 60px;
            line-height: 60px;
            border-bottom: 1px solid #dcdcdc;
            border-right: 1px solid #dcdcdc;
        }

        th > div > div {
            min-width: 100px;
            height: 40px;
            float: left;
            line-height: 40px;
        }

        th {
            min-width: 100px;
            height: 40px;
        }

        .wd80 {
            width: 80px;
        }

        .lth {
            float: right;
        }

        .wd100 {
            width: 100px !important;
            font-weight: bold;
        }

        .psre div {
            width: 120px;
            height: 49px;
            line-height: 20px;
            padding-top: 11px;
        }

        .ul-main {
            background: url(/static_new/images/new33/bg-line.png) repeat-y;
        }

        .ul-main > li > span {
            color: #fff;
            text-indent: 19px;
            margin-right: 25px;
        }

        /* table{
            margin-top: 10px;
        } */
        /*  td>div>div em{
            display: block;
            position: absolute;
            width: 100px;
            height: 12px;
            line-height: 12px;
            bottom: 0;
            left: 0;
            color: #FF8C00;
            font-size: 12px;
        } */
        td > div > div > div {
            height: 60px;
            line-height: 60px;
        }

        td > div > div {
            cursor: pointer;
            position: relative;
        }

        td > div > div:hover {
            background: #f3f3f3;
        }

        div.active {
            background: #AFEEEE;
        }

        div.active:hover {
            background: #AFEEEE;
        }

        .tag {
            margin-top: 40px;
            margin-bottom: 20px;
        }

        .tag label.one {
            margin-left: 146px;
        }

        .tag label.four {
            margin-right: 30px;
        }

        .tag label input {
            position: relative;
            top: 1px;
            left: -5px;
        }

        .tag label {
            margin-right: 15px;
            cursor: pointer;
        }

        .tab2 p {
            line-height: 20px;
            font-size: 12px;
        }

        .tab2 span {
            display: none;
        }

        .tag button {
            height: 41px;
            padding: 0 23px;
            border-radius: 4px;
            color: #fff;
            margin-right: 10px;
        }

        .tag .b1 {
            background: #405DE6;
        }

        .tag .b2 {
            background: #1ab02f;
        }

        .tag .b3 {
            background: #9196aa;
        }

        .tag .b4 {
            background: #6f69d6;
        }

        .tag .b5 {
            background: #1ab02f;
        }

        .tag .b6 {
            background: #f77737;
        }

        .tag .b7 {
            background: #c13584;
        }

        .tag .b8 {
            background: #f48f49;
        }

        .tag .b9 {
            background: #2e94db;
            margin-right: 0 !important;
        }

        .fixedcont {
            width: 220px;
            position: fixed;
            top: 495px;
            right: 0;
            /* min-height: 190px;*/
            z-index: 100;
            background: #fff;
            overflow: hidden;
        }

        .fixedcont .ul1 {
            z-index: 1;
            width: 160px;
            height: 358px;
            overflow-x: hidden;
            overflow-y: auto;
            cursor: default;
            float: right;
        }

        .fixedcont .ul1 li.over {
            background: #e5e5e5 !important;
            border: 1px solid #ccc !important;
            color: #666;
        }

        .fixedcont .ul1 li {
            min-height: 76px;
            width: 134px;
            margin: 0 10px 10px 0;
            font-size: 12px;
            background: #fff;
            border: 1px solid #cfa972;
            color: #f77737
        }

        .fixedcont .ul2 {
            z-index: 1;
            width: 160px;
            height: 358px;
            overflow-x: hidden;
            overflow-y: auto;
            cursor: default;
            float: right;
        }

        .fixedcont .ul2 li {
            height: 46px;
            width: 134px;
            line-height: 46px;
            margin: 0 10px 10px 0;
            font-size: 14px;
            text-align: center;
            background: #fff;
            border: 1px solid #cfa972;
            color: #f77737;
            cursor: pointer;
        }

        .fixedcont .ul2 li.active {
            width: 134px;
            background: #f77727;
            border: 1px solid #aaa;
            color: #fff;
        }

        .fixedcont .ul1 li span {
            line-height: 76px;
            text-align: center;
            float: left;
            width: 39px;
            color: #666;
        }

        .fixedcont .ul1 li:last-child {
            margin-bottom: 10px;
        }

        .fixedcont .ul1 li > div {
            float: left;
            line-height: 40px;
            text-indent: 15px;
            cursor: pointer;
            height: 66px;
            padding-top: 10px;
            border-right: 1px dashed #999;
            width: 93px;
        }

        .fixedcont .ul1 li > div p {
            height: 20px;
            line-height: 20px;
            font-size: 12px;
        }

        .fixedcont .info .p1 {
            font-size: 16px;
            height: 30px;
            line-height: 30px;
        }

        .fixedcont .info .p1 em {
            float: right;
            font-size: 32px;
            cursor: pointer;
            display: none;
        }

        .fixedcont .info .p2 {
            font-size: 12px;
            height: 26px;
            line-height: 26px;
            display: none;
        }

        .fwb {
            font-weight: bold;
        }

        .fixedcont .info .p3 {
            font-size: 14px;
            height: 30px;
            cursor: pointer;
            text-align: center;
            line-height: 30px;
        }

        .fixedcont .info {
            background: #5851db;
            color: #fff;
            padding: 10px 12px;
        }

        .fixedcont .cont {
            border: 1px solid #f77737;
            border-top: none;
            /* padding-top: 10px;*/
            transition: all .3s;
            overflow: hidden;
        }

        .fixedcont .cont .lt {
            float: left;
            width: 46px;

        }

        .fixedcont .cont .lt span.active {
            background: #f77737 !important;
            color: #fff !important;
        }

        .fixedcont .cont .lt span {
            display: block;
            width: 45px;
            height: 95px;
            line-height: 35px;
            padding-top: 20px;
            text-align: center;
            border: 1px solid #f77737;
            margin-bottom: 10px;
            border-left: none;
            color: #f77737;
            cursor: pointer;
            font-size: 14px;
        }

        #movebg {
            height: 40px;
            background: #fff;
            border: 1px dashed #999;
            line-height: 40px;
            text-align: center;
            cursor: move;
            transition: all .3s;
            overflow: hidden;
        }

        .container {
            padding-right: 100px;
        }

        .tab1 th {
            border: 1px solid #f1f1f1;
        }

        .tab2 tr th:first-child,
        .tab2 tr td:first-child {
            border-left: none !important;
        }

        .tab1 em {
            font-size: 12px;
        }

        .tab2 div.green {
            background: #90EE90;
            border: 1px dashed #2E8B57;
            color: #333;
        }

        .tab2 div.red {
            background: #FFB6C1;
            border: 1px dashed red;
            color: #333;
            height: 59px;
            width: 99px;
        }

        .h0 {
            height: 0px !important;
            border: none !important;
        }
        .shi{
            background: url("/static_new/images/new33/shi.png")no-repeat right 0;
            background-size: 18px;
        }
        .dcallstu button {
            width: 78px!important;
            height: 33px!important;
            border-radius: 3px!important;
            margin: 300px 10px!important;
        }
        .gray{
            background-color: #C0C0C0!important;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $(".t-left li").click(function () {
                $(this).addClass('t-li').siblings().removeClass('t-li');
            })
            $('.xueke label').click(function () {
                $(this).addClass('active').siblings('label').removeClass('active')
            })

            $('.close,.qx').click(function(){
                $('.bg').hide();
                $('.dc-popup').hide();
            })
        })

    </script>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<span id = "defaultTerm" style="display: none"></span>
<form id = "export_form"></form>
<div class="right-pos">
    <c:if test="${roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)||roles:isTeacher(sessionValue.userRole)}">
        <c:if test="${roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)}">
            <a href="/newisolatepage/allTable.do" class="pka1">总课表</a>
        </c:if>
        <a href="/newisolatepage/pTeaClassTime.do" class="pkt1">教师课表</a>
        <a href="/newisolatepage/pStuClassTime.do" class="pkc2">学生课表</a>
        <a href="/newisolatepage/jSubCLassTime.do" class="pkb1">行政班课表</a>
    </c:if>
    <c:if test="${roles:isStudent(sessionValue.userRole)}">
        <a href="/newisolatepage/pStuClassTime.do" class="pkc2">学生课表</a>
        <%--<a href="/newisolatepage/jSubCLassTime.do" class="pkb1">行政班课表</a>--%>
    </c:if>
    <c:if test="${roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)}">
        <a href="/newisolatepage/subTable.do" class="xkkb1">学科课表</a>
    </c:if>
</div>
<div class="container clearfix">
    <ul class="ul-main clearfix" style="margin-bottom: 50px;">
        <li class="xueke">
        </li>
        <li class="cls">
        </li>
        <script type="text/template" id="grade_temp">
            {{~it:value:index}}
            <label ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</label>
            {{~}}
        </script>
        <script type="text/template" id="class_temp">
            {{~it:value:index}}
            <label ids="{{=value.classId}}" gName="{{=value.gname}}" cName="{{=value.xh}}">{{=value.gname}}（{{=value.xh}}）班</label>
            {{~}}
        </script>
    </ul>
    <div class="container clearfix">
        <div class="fl w225">
            <div class="f16 c-b">学生列表<em class="c-3c">（<span id="userCount"></span>名）</em></div>
            <script type="text/template" id="userList">
                {{~it:value:index}}
                <li class="cctv" ids="{{=value.userId}}" nm="{{=value.userName}}">
                    <span>{{=value.userName}}</span>
                </li>
                {{~}}
            </script>
            <ul class="t-left h671 mt10" id="use">

            </ul>
        </div>
       	<div class="fr w940 mr10 clearfix" id="GDGContent">
            <div class = "dc clearfix">
                <div class="f18 c-b" id="userName"></div>
                <button class="b9 fr mtt41">导出</button>
		        <button class="b8 fr mtt41 mr90" id="printGDG">打印</button>
                <button class="b10 fr mtt41 mr182">批量导出</button>
		        <select class="sel1 fr mtt41" type="text" id="week" style="margin-right: 300px;">
                </select>
                <script type="text/template" id="week_temp">
                    {{~it:value:index}}
                    <option value="{{=value.serial}}">{{=value.numberWeek}}{{=value.start}}</option>
                    {{~}}
                </script>
            </div>
            <table class="tab1 mt10" style="margin-top: 5px;">
                <thead>
                <tr>
                    <th>
                        <div class="wd100">课节 / 日</div>
                    </th>
                    <th>
                        <div class="w163">周一</div>
                    </th>
                    <th>
                        <div class="w163">周二</div>
                    </th>
                    <th>
                        <div class="w163">周三</div>
                    </th>
                    <th>
                        <div class="w163">周四</div>
                    </th>
                    <th>
                        <div class="w163">周五</div>
                    </th>
                    <th>
                        <div class="w163">周六</div>
                    </th>
                    <th>
                        <div class="w163">周日</div>
                    </th>
                </tr>
                </thead>
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
                            <div>{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></div>
                        </td>
                        <td class="itt">
                        </td>
                        <td class="itt">
                        </td>
                        <td class="itt">
                        </td>
                        <td class="itt">
                        </td>
                        <td class="itt">
                        </td>
                        <td  class="itt">
                        </td>
                        <td  class="itt">
                        </td>
                    </tr>
                    {{~}}
                </script>
                <tbody class="t-tbo" id="tbd">

                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="bg"></div>

<div class="popup dc-popup">
    <div class="popup-top">
        <em class="fl">批量导出</em>
        <i class="close fr"></i>
    </div>
    <div class="wuUl">
        <span>年级</span>
        <select class="sel162 mrl10" id="grade2">
        </select>

        <script type="text/template" id="gradeList">
            {{~it:value:index}}
            {{? value.jie != ""}}
            <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
            {{?}}
            {{? value.jie == ""}}
            <option value="{{=value.gid}}">{{=value.gnm}}</option>
            {{?}}
            {{~}}
        </script>

        <span>班级</span>
        <select class="sel162 mrl10 mt10" id="classList">

        </select>

        <script type="text/template" id="class">
            {{~it:value:index}}
            {{? value.xh != ""}}
            <option value="{{=value.classId}}">{{=value.gname}}({{=value.xh}})班</option>
            {{?}}
            {{? value.xh == ""}}
            <option value="{{=value.classId}}">{{=value.gname}}</option>
            {{?}}
            {{~}}
        </script>
    </div>
    <div class="wult clearfix">
        <div class="fl">
            <button class="selectAll">全选/全部取消</button>
        </div>
    </div>
    <div>
        <br>
        &nbsp;&nbsp;&nbsp;&nbsp;----------------------------------------------------------------------------------------------
    </div>
    <div class="studentdc" id="studentList">

    </div>
    <script type="text/template" id="student">
        {{~it:value:index}}
        <label>
            <input class="dcStu" type="checkbox" ids="{{=value.userId}}">
            <span>{{=value.userName}}</span>
        </label>
        {{~}}
    </script>
    <div class="popup-btn mt35 mb35 dcall">
        <button class="dcAll">导出</button>
        <button class="qx">取消</button>
    </div>
</div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('pStuClassTime', function (pStuClassTime) {
        pStuClassTime.init();
    });
</script>
</html>