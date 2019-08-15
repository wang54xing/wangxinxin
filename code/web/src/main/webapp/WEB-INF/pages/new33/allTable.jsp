<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/3/24
  Time: 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>总课表</title>
    <meta charset="utf-8">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/table2.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <style type="text/css">
        .xkkb1{
            background: url('/static_new/images/new33/xkkb1.png') no-repeat #fff 32px 20px;
            border:1px solid #dfdfdf;
            color: #8c44de;
        }
        * {
            -moz-user-select: none;
            -webkit-user-select: none;
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

        .tab1 td {
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

        .gray {
            background: #C0C0C0;
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
            width: 99px;
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

        .wd100 {
            width: 100px;
        }

        .lth {
            float: right;
        }

        .wd120 {
            width: 120px !important;
            font-weight: bold;
        }

        .psre div {
            width: 120px;
            height: 49px;
            line-height: 20px;
            padding-top: 11px;
            border-bottom: 1px solid #dcdcdc;
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

        .jiaoshi div label {
            border-radius: 3px;
            height: 26px;
            margin: 7px 30px 0 0;
            line-height: 26px;
            float: left;
            cursor: pointer;
        }

        .ul-main > li > div label.active {
            color: #388CE9;
            font-weight: bold;
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

        .tab2 p em {
            font-size: 12px;;
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

        .tab2 div .red {
            background: #FFB6C1;
            border: 1px solid #FFB6C1;
            color: #333;
            height: 59px;
            width: 99px;
        }

        .tab2 div .gray1 {
            background: #CCC;
            border: 1px solid #CCC;
            color: #333;
            height: 59px;
            width: 99px;
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
    </style>
    <script type="text/javascript">
        $(function () {
            var l1 = 1;
            var l2 = 1;
            var l3 = 1;


            $('.ul-main > li > label').click(function () {
                var jsn = $(this).attr('js');
                if (jsn == 'all') {
                    $(this).addClass('active').siblings('label').removeClass('active')
                }
                else if ($(this).parent().hasClass('grade')) {
                    $(this).addClass('active').siblings().removeClass('active')
                }
                else {
                    $(this).toggleClass('active');
                    $(this).parent().find('.all').removeClass('active');
                }
            });

            $('body').on('click', '.tab2 td>div>div>div', function () {
                if ($(this).parent().hasClass('red')) {

                } else {
                    $(this).toggleClass('active');
                }
            });

            $('.fixedcont .cont .lt span').click(function () {
                $(this).addClass('active').siblings('span').removeClass('active');
                $('.fixedcont .ul1').hide();
                $('.fixedcont .ul2').show();
            })

            //收起移动窗
            $('.fixedcont .info .p1 em').click(function () {
                $('.fixedcont .cont,#movebg').addClass('h0');
                $(this).hide();
                $('#move').css('width', '110px');
                $('.fixedcont .info .p2').hide();
                $('.fixedcont .info .p3').show();
            });
            //展开移动窗
            $('.fixedcont .info .p3').click(function () {
                $('.fixedcont .cont,#movebg').removeClass('h0');
                $(this).hide();
                $('#move').css('width', '220px');
                $('.fixedcont .info .p2').show();
                $('.fixedcont .info .p1 em').show();
            });

            //点击教师名 高亮其所有课程
            $('body').on('click', '.tab2 div>p:nth-child(2)', function () {
                var jsn = $(this).attr('js');
                var cln = $(this).attr('class');
                if ($(this).hasClass('active')) {
                    $(this).removeClass('active')
                    $('.tab2 div.' + jsn).removeClass('active')
                } else {
                    $(this).addClass('active')
                    $('.tab2 div.' + jsn).addClass('active')
                }
                return false;
            })

            var labelNumber = 4;
            $('.tag label input').click(function () {
                var jsn = $(this).attr('js');
                if ($(this).is(':checked')) {
                    $('.tab2 .v' + jsn).show();
                    labelNumber++
                } else {
                    labelNumber--;
                    if (labelNumber < 1) {
                        layer.msg('请至少选择一个标签显示！')
                        $(this).prop('checked', 'checked')
                        labelNumber = 1
                    } else {
                        $('.tab2 .v' + jsn).hide();
                    }
                }
            });


            //拖拽窗口 学科切换
            $('.fixedcont .ul2 li').click(function () {
                $(this).addClass('active').siblings('li').removeClass('active');
                $('.fixedcont .ul1').show();
                $('.fixedcont .ul2').hide();
            })

            $('.gaozhong label').click(function () {
                var jln = $(this).attr('js');
                var cln = $(this).attr('class');
                if (jln == 'all') {
                    $('tr td').show();
                    $('tr th').show();
                    l1 = 1;
                } else {
                    if (l1 == 1) {
                        if (cln == 'active') {
                            $('tr td.' + jln).show().siblings('tr td').hide();
                            $('tr th.' + jln).show().siblings('tr th').hide();
                            l1 = l1 + 1
                        }
                    } else {
                        if (cln == 'active') {
                            $('tr td.' + jln).show();
                            $('tr th.' + jln).show();
                        } else {
                            $('tr td.' + jln).hide();
                            $('tr th.' + jln).hide();
                        }
                    }
                }
            });


            $('.zhou label').click(function () {
                var jln = $(this).attr('js');
                var cln = $(this).attr('class');
                var wd = $('.tab2 th>div').width();
                if (jln == 'all') {
                    $('.zhou>div').show();
                    $('.gao>div').show();
                    $('th>div').css('width', '700px')
                    l2 = 1
                } else {
                    if (l2 == 1) {
                        $('tr td div.' + jln).show().siblings('tr td div').hide();
                        $('tr th div.' + jln).show().siblings('tr th div').hide();
                        $('th>div').css('width', '100px')
                        l2 = l2 + 1
                    } else {
                        if (cln == 'active') {
                            $('tr td div.' + jln).show();
                            $('tr th div.' + jln).show();
                            $('tr th:first-child').show();
                            $('th>div').css('width', wd + 100)
                        } else {
                            $('tr td div.' + jln).hide();
                            $('tr th div.' + jln).hide();
                            $('tr th:first-child').show();
                            $('th>div').css('width', wd - 100)
                        }
                    }
                }
            });


            $('.tag input').click(function () {
                var jsn = $(this).attr('js')
                if ($(this).is(':checked') == true) {
                    $('div.' + jsn).addClass('active')
                } else {
                    $('div.' + jsn).removeClass('active')
                }
            });

            function init() {

            }


        })
    </script>

    <%--<script type="text/javascript">--%>

    <%--window.onload = function () {--%>

    <%--var dd = document.getElementById("movebg"); // 获取DIV对象--%>
    <%--dd.onmousedown = function (e) {--%>
    <%--var event1 = e || window.event;                  // IE、火狐获取事件对象--%>
    <%--var eventX = event1.offsetX || event1.layerX;         // 获取鼠标相对于事件源的X轴--%>
    <%--var eventY = event1.offsetY || event1.layerY;        // 获取鼠标相对于事件源的Y轴--%>
    <%--var flag = true;--%>
    <%--this.onmousemove = function (e) {--%>
    <%--if (flag) {--%>
    <%--var event2 = e || window.event;--%>
    <%--var eveX = event2.clientX;             // 获取鼠标相对于浏览器x轴的位置--%>
    <%--var eveY = event2.clientY;             // 获取鼠标相对于浏览器Y轴的位置--%>
    <%--$('#move').css('top', eveY - eventY);--%>
    <%--$('#move').css('left', eveX - eventX);--%>
    <%--}--%>
    <%--}--%>
    <%--this.onmouseup = function () {--%>
    <%--if (flag) {--%>
    <%--flag = false;--%>
    <%--}--%>
    <%--}--%>
    <%--}--%>
    <%--}--%>
    <%--</script>--%>
    <script type="text/javascript">
        // function allowDrop(ev){
        //     ev.preventDefault();
        // }

        // var srcdiv = null;
        // function drag(ev,divdom){
        //     srcdiv=divdom;
        //     ev.dataTransfer.setData("text/html",divdom.innerHTML);
        // }

        // function drop(ev,divdom){
        //     ev.preventDefault();
        //     if(srcdiv != divdom){
        //         srcdiv.innerHTML = divdom.innerHTML;
        //         divdom.innerHTML=ev.dataTransfer.getData("text/html");
        //     }
        // }
        // function removed(divdom){
        //     divdom.parentNode.removeChild(divdom)
        // }

    </script>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<span id="defaultTerm" style="display: none"></span>

<form id="export_form"></form>
<div class="right-pos">
    <c:if test="${roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)}">
        <a href="/newisolatepage/allTable.do" class="pka2">总课表</a>
        <a href="/newisolatepage/pTeaClassTime.do" class="pkt1">教师课表</a>
        <a href="/newisolatepage/pStuClassTime.do" class="pkc1">学生课表</a>
        <a href="/newisolatepage/jSubCLassTime.do" class="pkb1">行政班课表</a>
        <a href="/newisolatepage/subTable.do" class="xkkb1">学科课表</a>
    </c:if>
</div>
<div class="container clearfix">
    <ul class="ul-main">
        <li class="gaozhong">
        </li>
        <script type="text/template" id="grade_temp">
            {{~it:value:index}}
            <label ids="{{=value.gid}}" nm="{{=value.gnm}}({{=value.jie}})">{{=value.gnm}}({{=value.jie}})</label>
            {{~}}
        </script>

        <script type="text/template" id="jiaos">
            <div style="width: 1080px;float: left">
                <label class='active all' js='all'>全部</label>

                {{~it:value:index}}
                <label ids="{{=value.roomId}}" js="{{=value.roomId}}">{{=value.roomName}}</label>
                {{~}}
            </div>
        </script>
        <li class="jiaoshi">
        </li>
        <li class="zhou jxr">
            <span>教学日 : </span>
            <label class="active all" js='all'>全部</label>
            <label js="zh1">周一</label>
            <label js="zh2">周二</label>
            <label js="zh3">周三</label>
            <label js="zh4">周四</label>
            <label js="zh5">周五</label>
            <label js="zh6">周六</label>
            <label js="zh7">周日</label>
        </li>
        <script type="text/template" id="KeShiList">
            {{~it:value:index}}
            <label ids="{{=value.id}}" js="ke{{=index+1}}" time="{{=value.start}}-{{=value.end}}"><em
                    style="display:none">{{=value.start}}-{{=value.end}}</em>{{=value.name}}</label>
            {{~}}
        </script>
        <li class="ke kj">
        </li>
    </ul>
    <div class="tag fr" style="width: 1190px;">
        <span class="c-b f18" style="margin-right: 285px;"><em id="kbName" class="f18 mr10"></em>年级总课表</span>
        <label class="one" style="display: none">
            <input type="checkbox" js='bq' class="one" checked=checked>标签
        </label>
        <label style="margin-left: 146">
            <input type="checkbox" js='js' class="one" checked=checked>教师
        </label>
        <label>
            <input type="checkbox" js='rs' class="two" checked=checked>人数
        </label>
        <label class="four">
            <input type="checkbox" js='xk' class="three" checked=checked>学科
        </label>
        <select class="sel1 fr mtt41 mr90" type="text" id="week">
        </select>
        <script type="text/template" id="week_temp">
            {{~it:value:index}}
            <option value="{{=value.serial}}">{{=value.numberWeek}}{{=value.start}}</option>
            {{~}}
        </script>
        <button class="b8" hidden="true">打印</button>
        <button class="b9">导出</button>

    </div>
    <div>
        <div>

        </div>
        <div class="fr">
            <table class="tab1">
                <thead>
                <tr>
                    <th>
                        <div class="wd120">教室 / 班级</div>
                    </th>
                </tr>
                <tr>
                    <th>
                        <div class="wd120">课节 / 日</div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="psre ke kejie">

                    </td>
                    <script type="text/template" id="ks_temp">
                        {{~it:value:index}}
                        <div class="ke{{=value.serial}}" count="{{=index+1}}" ids="{{=value.id}}">
                            {{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></div>
                        {{~}}
                    </script>
                </tr>
                </tbody>
            </table>
            <div style="width: 1076px;overflow-x:auto;float: left;background:#f5f5f5;">
                <table id="MyTable" class="tab2">
                    <thead>
                    <tr class="clroms">

                    </tr>
                    <script type="text/template" id="clroms_temp">
                        {{~it:value:index}}
                        {{?value.className != ""}}
                        <th class="{{=value.roomId}}" crid="{{=value.id}}"><p>{{=value.roomName}}</p><p>（{{=value.className}}）</p></th>
                        {{?}}
                        {{?value.className == "" || value.className == null}}
                        <th class="{{=value.roomId}}" crid="{{=value.id}}">{{=value.roomName}}</th>
                        {{?}}
                        {{~}}
                    </script>
                    <tr class="weeks">

                    </tr>
                    <script type="text/template" id="weeks_temp">
                        {{~it:value:index}}
                        <th class="{{=value.roomId}}">
                            <div class="zhou {{=value.id}}" style="width: 500px;">
                                <div class="zh1">周一</div>
                                <div class="zh2">周二</div>
                                <div class="zh3">周三</div>
                                <div class="zh4">周四</div>
                                <div class="zh5">周五</div>
                                <div class="zh6">周六</div>
                                <div class="zh7">周日</div>
                            </div>
                        </th>
                        {{~}}
                    </script>
                    </thead>
                    <tbody>
                    <tr class="jxbkj">

                    </tr>
                    <script type="text/template" id="jxbkj_temp">
                        {{ for(var prop in it) { }}
                        <td class="{{=it[0][0].classroomId}} gao" ids="{{=it[0][0].classroomId}}">
                            {{for (var i=0;i<7;i++) { }}
                            <div class="zh{{=i+1}} ke">
                                {{~it[prop]:value:index}}
                                {{?value.x==i}}
                                {{?value.swType==0 && value.isSWAndCourse == 0}}
                                <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} citt" ykbid="{{=value.id}}"
                                     jxbid="{{=value.jxbId}}" ondrop="drop(event,this)" ondragover="allowDrop(event)"
                                     draggable="false" ondragstart="drag(event, this)">
                                    {{?value.jxbId!=null}}
                                    <%--<p class="vbq">WD-123</p>--%>
                                    {{?value.jxbId!=''}}
                                    <div class="ls">
                                        <p style="height: 40px;margin-bottom: 4px;line-height: 14px;"><em title="{{=value.jxbName}}" style="margin-top:3px;width: 100px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;display: inline-block;height: 20px;" class="vxk">{{=value.jxbName}}</em><em class="vrs">({{=value.studentCount}}人)</em>
                                        </p>
                                        {{?value.teacherName!=null}}
                                        <p class="vjs" js='ls'>{{=value.teacherName}}</p>
                                        {{?}}
                                    </div>
                                    {{?}}
                                    {{?}}
                                </div>
                                {{?}}
                                {{?value.swType!=0 && value.isSWAndCourse == 1}}
                                <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} citt" ykbid="{{=value.id}}"
                                     jxbid="{{=value.jxbId}}" ondrop="drop(event,this)" ondragover="allowDrop(event)"
                                     draggable="false" ondragstart="drag(event, this)">
                                    {{?value.jxbId!=null}}
                                    <%--<p class="vbq">WD-123</p>--%>
                                    {{?value.jxbId!=''}}
                                    <div class="ls" title="{{=value.remarks}}">
                                        <p class="shi1 pic"></p>
                                        <p><em class="vxk">{{=value.jxbName}}</em><em class="vrs">({{=value.studentCount}}人)</em>
                                        </p>
                                        {{?value.teacherName!=null}}
                                        <p class="vjs" js='ls'>{{=value.teacherName}}</p>
                                        {{?}}
                                    </div>
                                    {{?}}
                                    {{?}}
                                </div>
                                {{?}}
                                {{?value.swType==1 && value.isSWAndCourse == 0}}
                                <div class="ke{{=value.y+1}} red citt" ykbid="{{=value.id}}">
                                    <div>{{=value.remarks}}</div>
                                </div>
                                {{?}}
                                {{?value.swType==2 && value.isSWAndCourse == 0}}
                                <div class="ke{{=value.y+1}} red citt" ykbid="{{=value.id}}">
                                    <div>{{=value.remarks}}</div>
                                </div>
                                {{?}}
                                {{?}}
                                {{~}}
                            </div>
                            {{}}}
                        </td>
                        {{ } }}
                    </script>
                    </tbody>
                </table>
            </div>
        </div>
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


</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('allTable', function (allTable) {
        allTable.init();
    });
</script>

</html>