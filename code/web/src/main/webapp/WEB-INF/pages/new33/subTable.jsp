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
    <title>学科课表</title>
    <meta charset="utf-8">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/table2.css">
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script>
        $(function () {
            $('.wucx').click(function () {
                $('.bg').show();
                $('.z-popup').show();
            })
            $('.close,.qx').click(function () {
                $('.bg').hide();
                $('.wu-popup').hide();
                $('.you-popup').hide();
                $(".z-popup").hide();
            })
            $('.wuLeft li').click(function () {
                $(this).addClass('wuLi').siblings().removeClass('wuLi')
            })
        })
    </script>
    <style type="text/css">
        * {
            -moz-user-select: none;
            -webkit-user-select: none;
        }

        .ul-main li {
            border-bottom: 1px dashed #d1d1d1;
            line-height: 40px;
            /*height: 40px;*/
            margin: 0;
            width: 1199px;;
        }

        .ul-main > li > div label {
            border-radius: 3px;
            height: 26px;
            margin: 7px 30px 0 0;
            line-height: 26px;
            cursor: pointer;
        }

        .ul-main > li > div label.active {
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
            float: left;
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

        .dc button {
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

        .dc .b9 {
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

        .right-pos {
            top: -60px;
        }

        .w1080 {
            width: 1080px;
        }
        .zDv{
            clear: both;
            width: 100%;
            overflow: hidden;
            padding-top: 20px;
            padding-right: 1px;
        }
        .zDv p{
            margin-bottom: 10px;
        }
        .zDv .zTab{
            width: 100%;
        }
        .zTab tr th{
            background: #20B2AA;
            color: white;
            min-width: inherit;
            height: auto;
            padding: 10px;
        }
        .zTab tr td{
            border: 1px solid #d2d2d2;
            padding: 10px;
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
        })
        
        
    </script>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<span id="defaultTerm" style="display: none"></span>

<form id="export_form"></form>
<div class="right-pos">
    <c:if test="${roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)||roles:isTeacher(sessionValue.userRole)}">
        <c:if test="${roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)}">
            <a href="/newisolatepage/allTable.do" class="pka1">总课表</a>
        </c:if>
        <a href="/newisolatepage/pTeaClassTime.do" class="pkt1">教师课表</a>
        <a href="/newisolatepage/pStuClassTime.do" class="pkc1">学生课表</a>
        <a href="/newisolatepage/jSubCLassTime.do" class="pkb1">行政班课表</a>
        <c:if test="${roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)}">
            <a href="/newisolatepage/subTable.do" class="xkkb2">学科课表</a>
        </c:if>
    </c:if>
</div>
<div class="container clearfix">
    <div class="clearfix">
        <ul class="ul-main">
            <li class="grade">
                <div class="inline w1080 gg">
                </div>
            </li>
            <script type="text/template" id="grade1_temp">
                {{~it:value:index}}
                <label ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</label>
                {{~}}
            </script>
        </ul>

        <ul class="ul-main">
            <li class="xueke">
                <span style="color: white">学科: </span>

                <div class="inline w1080 xuexue">
                </div>
            </li>
            <script type="text/template" id="grade_temp">
                {{~it:value:index}}
                <label ids="{{=value.subid}}">{{=value.snm}}</label>
                {{~}}
            </script>
        </ul>
    </div>
    <div class="container clearfix tophide">
        <p>暂无数据</p>
    </div>
    <div class="container clearfix conhide">
        <div class="fl w225" hidden>
            <div class="f16 c-b">教师列表<em class="c-3c">（<span id="userCount"></span>人）</em></div>
            <%--<script type="text/template" id="userList">--%>
            <%--{{~it:value:index}}--%>
            <%--<li class="cctv" ids="{{=value.id}}" pks="{{=value.pksCount}}" nm="{{=value.name}}">--%>
            <%--<span>{{=value.name}}(课时:{{=value.count}})</span>--%>
            <%--</li>--%>
            <%--{{~}}--%>
            <%--</script>--%>
            <ul class="t-left h588 mt10" id="use" style="margin-top: 14px;">

            </ul>
        </div>
        <div class="fr" id="GDGContent">
            <div class="dc">
                <div><em class="f18 c-b" id="userName" style="display: inline-block"></em><em class="f18 c-b"
                                                                                              id="keCount"
                                                                                              style="display: inline-block"></em>
                </div>
                <button class="b9 fr mtt41 mr5">导出</button>
                <button class="b8 fr mtt41 mr85" id="printGDG">打印</button>
                <button class="b10 fr mtt41 mr170" id='export'>批量导出</button>
                <select class="sel1 fr mtt41 mr286" type="text" id="week">
                </select>
                <script type="text/template" id="week_temp">
                    {{~it:value:index}}
                    <option value="{{=value.serial}}">{{=value.numberWeek}}&nbsp;&nbsp;{{=value.start}}</option>
                    {{~}}
                </script>
            </div>
            <table class="tab1 mt10 ttab">
                <thead>
                <tr>
                    <th>
                        <div class="wd100">课节 / 日</div>
                    </th>
                    <th>
                        <div class="w155">周一</div>
                    </th>
                    <th>
                        <div class="w155">周二</div>
                    </th>
                    <th>
                        <div class="w155">周三</div>
                    </th>
                    <th>
                        <div class="w155">周四</div>
                    </th>
                    <th>
                        <div class="w155">周五</div>
                    </th>
                    <th>
                        <div class="w155">周六</div>
                    </th>
                    <th>
                        <div class="w155">周日</div>
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
                            <div><em class="inline mt45">{{=value.name}}</em><br><em>{{=value.start}}-{{=value.end}}</em></div>
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
                        <td class="itt">
                        </td>
                        <td class="itt">
                        </td>
                    </tr>
                    {{~}}
                </script>
                <tbody class="t-tbo" id="tbd">

                </tbody>
            </table>
        </div>

        <div class="zDv">
            <p>专项课</p>
            <table class="zTab">
                <thead>
                <tr>
                    <th>组合名</th>
                    <th>专项课名称</th>
                    <th>教师</th>
                    <th>教室</th>
                    <th>学生人数</th>
                </tr>
                </thead>
                <script type="text/template" id="Zhuan">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.zxname}}</td>
                        <td>{{=value.name}}</td>
                        <td>{{=value.teaname}}</td>
                        <td>{{=value.roomname}}</td>
                        <td>{{=value.stusize}}</td>
                    </tr>
                    {{~}}
                </script>
                <tbody id="zhuanList">

                </tbody>
            </table>
        </div>

    </div>



</div>


<!--bg-->
<div class="bg"></div>
<!---->
<div id="fuckYou">

</div>
<div class="popup fc-popup" style="width: 255px!important;overflow-x: hidden!important;border: 1px solid #999;">
    <div class="popup-top" style="width: 255px!important;">
        <em class="fl">详情</em>
    </div>
    <div class="ddtt" style="width: 255px!important;border: none!important;line-height:5px;margin: 0px;">
        <%--<span>郭靖</span>--%>
    </div>
</div>

<div class="popup dc-popup">
    <div class="popup-top">
        <em class="fl">批量导出</em>
        <i class="close fr"></i>
    </div>
    <div class="wuUl">
        <span>年级</span>
        <select class="sel162 mrl10" id="grade2">
        </select>

		<script type="text/template" id="grade2Temp">
        	{{~it:value:index}}
               <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
            {{~}}
        </script>
        
        <button class="selectAll" style="margin-left: 100px;">全选/全部取消</button>
       <!--  <span>科目</span>
        <select class="sel162 mrl10 mt10" id="subjectList">

        </select> -->
    </div>
    
    <div>
        <br>
        &nbsp;&nbsp;&nbsp;&nbsp;----------------------------------------------------------------------------------------------
    </div>
    
    <div class="teacherdc" id='subjectList' style="height: 350px">
		
    </div>
    
    <script type="text/template" id="subjectTemp">
        	{{~it:value:index}}
				<label>
            		<input class="dcTea" type="checkbox" subid="{{=value.subid}}">
            		<span value="{{=value.subid}}">{{=value.snm}}</span>
        		</label>
            {{~}}
    </script>
    
   <!-- <script type="text/template" id="teacher">
        {{~it:value:index}}
        <label>
            <input class="dcTea" type="checkbox" ids="{{=value.uid}}">
            <span>{{=value.unm}}</span>
        </label>
        {{~}}
    </script> -->
    <div class="popup-btn mt35 mb35 dcall">
        <button class="dcAll">导出全部</button>
        <button class="qx">取消</button>
    </div>
</div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('subTable', function (subTable) {
        subTable.init();
    });
</script>
</html>