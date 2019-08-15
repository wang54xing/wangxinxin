<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2018/3/9
  Time: 15:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>排课</title>
    <meta charset="utf-8">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/table2.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/new33/0.1.0/paike.js"></script>
    <style type="text/css">
        .tbSty{
            /*width: 1000px;*/
        }
        .spSty{
            display: inline-block;
            min-width: 130px;
        }
        .spSty label{

        }
        .classrooms li {
            border-bottom: 1px solid #dfdfdf;
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
            width: 1193px;
        }
        .xkbColor{
            background: #AFEEEE!important;
        }

        .b1a{
            background: #1ab02f;
            padding: 0px 10px!important;
            height: 30px!important;
            vertical-align: top;
            margin-top: -9px!important;
            float: right;
        }

        .ul-main > li > label {
            border-radius: 3px;
            height: 26px;
            margin: 7px 30px 0 0;
            line-height: 26px;
        }

        /*.ul-main > li >div{*/
        /*width: 1080px;*/
        /*float: left;*/
        /*}*/
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

        .tab1 {
            float: left;
        }

        td > div {
            width: 128px;
            line-height: 30px;
            float: left;
        }

        td > div > div {
            width: 127px;
            height: 60px;
            line-height: 60px;
            border-bottom: 1px solid #dcdcdc;
            border-right: 1px solid #dcdcdc;
        }

        th > div > div {
            min-width: 128px;
            height: 39px;
            float: left;
            line-height: 40px;
        }

        th {
            /*min-width: 100px;*/
            height: 40px;
        }

        .wd80 {
            width: 80px;
        }

        .lth {
            float: right;
        }

        .wd120 {
            width: 120px !important;
            font-weight: bold;
        }

        .psre div {
            float: left;
            width: 120px;
            height: 49px;
            line-height: 20px;
            padding-top: 11px;
            border-bottom: 1px solid #dcdcdc;
        }

        .fontcolor {
            color: #00ff00;
        }

        .liantang {
            background: hotpink;
        }

        .ul-main {
            float: left;
            background: url('/static_new/images/new33/bg-line.png') repeat-y;
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

        div.actv {
            /*border: 1px dashed red;*/
        }

        div.active:hover {
            background: #AFEEEE;
        }

        .tag {
            margin-top: 20px;
            margin-bottom: 20px;
        }

        .tag label.one {
            margin-left: 30px;
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
            font-size: 12px;
            display: inline-block;
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
            top: 100px;
            right: 50%;
            /* min-height: 190px; */
            z-index: 100;
            background: #fff;
            overflow: hidden;
            margin-right: -460px;
        }

        .kpjxblist {
            width: 195px !important;
        }

        .fixedcont .ul1 {
            z-index: 1;
            width: 160px;
            height: 490px;
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
            width: 160px;
            margin: 0 10px 10px 0;
            font-size: 12px;
            background: #fff;
            border: 1px solid #cfa972;
            color: #f77737;
        }

        .fixedcont .ul2 {
            z-index: 1;
            width: 160px;
            height: 490px;
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
            float: right;
            width: 39px;
            color: #666;
        }

        .fixedcont .ul1 li:last-child {
            margin-bottom: 10px;
        }

        .fixedcont .ul1 li > div {
            line-height: 40px;
            text-indent: 15px;
            cursor: pointer;
            min-height: 66px;
            padding-top: 10px;
            border-right: 1px dashed #999;
            width: 120px;
            display: inline-block;
        }

        .fixedcont .ul1 li > div p {
            /*height: 20px;*/
            line-height: 20px;
            font-size: 12px;
        }

        .fixedcont .ul1 li > div p em {
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
            margin-top: 5px;
        }

        .fixedcont .info .p2 select {
            width: 195px;
            height: 36px;
            border-radius: 3px;
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
            padding: 5px 10px 25px 3px;
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

        .tab2 div .red {
            background: #FFB6C1;
            /*border: 1px solid #FFB6C1;*/
            color: #333;
            height: 60px;
            width: 127px;
        }

        .tab2 div .gray1 {
            background: #AAAAAA;
            /*border: 1px solid #AAAAAA;*/
            color: #333;
            height: 60px;
            width: 127px;
        }

        .tab2 div .gray {
            background: #AAAAAA;
            color: #333;
            height: 60px;
            width: 127px;
        }

        .h0 {
            height: 0px !important;
            border: none !important;
        }
        .font {
            color: red;
        }

        .loading-p {
            display: none;
            z-index: 999999999;
        }
        .hongse{
            color: #ff0000 !important;
            width: 127px !important;
            height: 30px !important;
            line-height: 30px !important;
            border-bottom: 1px solid #dcdcdc !important;
            border-right: 1px solid #dcdcdc !important;
        }
        .fuck{
            background: #AAA !important;
            width: 127px !important;
            height: 60px !important;
            line-height: 60px !important;
            border-bottom: 1px solid #dcdcdc !important;
            border-right: 1px solid #dcdcdc !important;
        }
        .roomCls{
            background: #FFF!important;
            width: 127px !important;
            height: 60px !important;
            line-height: 60px !important;
            border-bottom: 1px solid #dcdcdc !important;
            border-right: 1px solid #dcdcdc !important;
        }
        .ban{
            background: #AFEEEE !important;
            width: 128px !important;
            height: 60px !important;
            line-height: 60px !important;
            border-bottom: 1px solid #dcdcdc !important;
            border-right: 1px solid #dcdcdc !important;
        }
        .tab th,.tab td{
            text-align: center;
            border: 1px solid #969696;
            height: 40px;
            width: 238px;
        }
        .tab td{
            display: none;
        }
        .tab tr:nth-child(1) td{
            display:table-cell;
        }
        .tab th{
            background: #ededed;
            color: black;
            border-bottom: none !important;
        }
        .ovDv{
            width: 1215px;
            overflow-y: auto;
            max-height: 180px;
        }
        .moreBtn{
            border: 1px solid #969696;
            /*border-top: none;*/
            width: 1194px;
            text-align: center;
            height: 30px;
        }
        .moreBtn button{
            background: transparent;
            margin-top: 5px;
            color: #388CE9;
            text-decoration: underline;
        }

        .dialog{
            width: 650px;
            min-height: 500px;
            position: fixed;
            top: 30%;
            left: 50%;
            /*margin-left: -325px;;*/
            background-color: #fff;
            overflow-y: auto;
            border: 1px solid #d2d2d2;
            display: none;
            z-index: 999;
        }

        .dialog-title{
            height: 40px;
            background-color: #eee;
            line-height: 40px;
            padding: 0 10px;
            cursor: move;
        }
        .dialog-title .name{
            color: #000;
            font-size: 16px;
            font-weight: bold;
            float: left;
            line-height: 40px;
        }
        .dialog-title .close{
            float: right;
            cursor: pointer;
        }

        .dialog .tb{
            width: 100%;
            overflow-y: auto;
            height: 460px;
            padding: 10px;
            box-sizing: border-box;
        }
        .dialog .tb table{
            width: 100%;
        }
        .dialog .tb table tr th{
            background: #6495ED;
            padding: 10px;
            border: 1px solid #d2d2d2;
            color: #fff;
        }
        .dialog .tb table tr td{
            padding: 10px;
            border: 1px solid #d2d2d2;
            width: auto;
        }

        .dialog .tb table tbody tr td:first-child{
            background: #6495ED;
            color: #fff;
            width: 80px;
        }


    </style>
    <script type="text/javascript">
        $(function () {

            var ddd = document.getElementById("movebar"); // 获取DIV对象
            ddd.onmousedown = function (e) {
                var event1 = e || window.event;                  // IE、火狐获取事件对象
                var eventX = event1.offsetX || event1.layerX;         // 获取鼠标相对于事件源的X轴
                var eventY = event1.offsetY || event1.layerY;        // 获取鼠标相对于事件源的Y轴
                var flag = true;
                this.onmousemove = function (e) {
                    if (flag) {
                        var event2 = e || window.event;
                        var eveX = event2.clientX;             // 获取鼠标相对于浏览器x轴的位置
                        var eveY = event2.clientY;             // 获取鼠标相对于浏览器Y轴的位置
                        $('#moveDialog').css('top', eveY - eventY);
                        $('#moveDialog').css('left', eveX - eventX);
                    }
                }
                this.onmouseup = function () {
                    if (flag) {
                        flag = false;
                    }
                }
            }
            // var ddd = document.getElementById("movebar"); // 获取DIV对象
            // ddd.onmousedown = function (e) {
            //     var event1 = e || window.event;                  // IE、火狐获取事件对象
            //     var eventX = event1.offsetX || event1.layerX;         // 获取鼠标相对于事件源的X轴
            //     var eventY = event1.offsetY || event1.layerY;        // 获取鼠标相对于事件源的Y轴
            //     var flag = true;
            //     this.onmousemove = function (e) {
            //         if (flag) {
            //             var event2 = e || window.event;
            //             var eveX = event2.clientX;             // 获取鼠标相对于浏览器x轴的位置
            //             var eveY = event2.clientY;             // 获取鼠标相对于浏览器Y轴的位置
            //             $('#moveDialog').css('top', eveY - eventY);
            //             $('#moveDialog').css('left', eveX - eventX);
            //         }
            //     }
            //     this.onmouseup = function () {
            //         if (flag) {
            //             flag = false;
            //         }
            //     }
            // }

            var l2 = 1;

            $('.ul-main > li > label').click(function () {
                var jsn = $(this).attr('js');
                if (jsn == 'all') {
                    $(this).addClass('active').siblings('label').removeClass('active')
                } else {
                    $(this).toggleClass('active');
                    $(this).parent().find('.all').removeClass('active');
                }
            });
            var labelNumber = 4;
            $('.checkfr label input').click(function () {
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
            $('.tt-left li span').click(function () {
                $(this).addClass('t-li').parent().siblings().find('span').removeClass('t-li');
            })
            $('.checkfr input').click(function () {
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
</head>
<body>
<input id="subjectId" hidden>
<jsp:include page="head.jsp"></jsp:include>
<div class="right-pos">
    <%--<a href="/newisolatepage/pkTable.do" class="ve2">排课</a>--%>
    <%--<a href="/newisolatepage/zixiclass.do" class="zhp1">排自习课</a>--%>
    <%--<a href="/newisolatepage/pkTableAll.do" class="pka1">总课表</a>--%>
    <%--<a href="/newisolatepage/teaCLassTime.do" class="pkc1">教师课表</a>--%>
    <%--<a href="/newisolatepage/classTable.do" class="pkb1">行政班课表</a>--%>
</div>
<div class="container clearfix">
    <span id="defaultTerm" style="display: none"></span>
    <div class="tab clearfix">
        <table>
            <thead>
                <tr>
                    <th>调整时间</th>
                    <th>原课节时间</th>
                    <th>原课节</th>
                    <th>交换课节时间</th>
                    <th>交换课节</th>
                </tr>
            </thead>
        </table>
        <div class="ovDv">
            <table>
                <tbody id="record">

                </tbody>
                <script type="text/template" id="record_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>
                            <span>{{=value.time}}</span>
                        </td>
                        <td>
                            <span>周{{=value.x + 1}}第{{=value.y + 1}}节</span>
                        </td>
                        <td>
                            {{=value.gradeName}}&nbsp;&nbsp;&nbsp;&nbsp;{{=value.teaName}}
                        </td>
                        <td>
                            <span>周{{=value.ox + 1}}第{{=value.oy + 1}}节</span>
                        </td>
                        <td>
                            <span>{{=value.gradeName}}&nbsp;&nbsp;&nbsp;&nbsp;{{=value.oTeaName}}</span>
                        </td>
                    </tr>
                    {{~}}
                </script>

            </table>
        </div>
        <div class="moreBtn">
            <button>更多</button>
        </div>
    </div>
    <div class="tag" style="color: #000;font-size: 16px;">
        <button class="b5" style="padding: 0px;padding-right: 10px;background: transparent;border: none;;color: #000000;font-size: 16px;">&lt;返回</button>排课调整
        <%--<button class="b6" style="width: 70px;padding: 0">交换</button>--%>
    </div>
    <div class="clearfix">
        <ul class="ul-main">
            <li class="gaozhong">
            </li>
            <script type="text/template" id="grade_temp">
                {{~it:value:index}}
                <label ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</label>
                {{~}}
            </script>

            <li id="sub">
            </li>
            <script type="text/template" id="subject_temp">
                {{~it:value:index}}
                <label ids="{{=value.subid}}">{{=value.snm}}</label>
                {{~}}
            </script>

        </ul>
    </div>

    <div>
        <div class="fl w195">
            <div class="f16 c-b tag">
                <input class="vt5" type="radio" name="na" id="classroom" checked="checked"><em class="mr10">教室</em>
                <input class="vt5" type="radio" name="na" id="teacher" hidden><em class="mr10" hidden>教师</em>
                <%--<input class="vt5" type="radio" name="na" id="tag"><em class="mr10">标签</em>--%>
            </div>
            <ul class="tt-left h588 mt10 classrooms" style="margin-top: 13px;">

            </ul>
            <script type="text/template" id="classrooms_templ">
                {{~it:value:index}}
                <li ids="{{=value.roomId}}">
                    <span class="wha" tname="{{=value.roomName}}">{{=value.roomName}}
                    <label class="wla" ids="{{=value.roomId}}"></label></span>
                    <span class="f12" style="min-height:26px!important;line-height:26px!important;vertical-align: top;margin-top: -10px;">{{=value.className}}</span>
                </li>
                {{~}}
            </script>

            <script type="text/template" id="teacher_templ">
                {{~it:value:index}}
                {{?value.isBanZhuRen!=0}}
                <li ids="{{=value.uid}}">
                    <%--<span tname="{{=value.unm}}" ypks="{{=value.ypks}}" ks="{{=value.ks}}">{{=value.unm}}（{{=value.ypks}}/{{=value.ks}}）<i--%>
                    <%--class="inline bzr"></i></span>--%>
                    <span tname="{{=value.unm}}" ypks="{{=value.ypks}}" ks="{{=value.ks}}">{{=value.unm}}<i
                            class="inline bzr"></i></span>
                </li>
                {{?}}
                {{?value.isBanZhuRen==0}}
                <li ids="{{=value.uid}}">
                    <%--<span tname="{{=value.unm}}" ypks="{{=value.ypks}}" ks="{{=value.ks}}">{{=value.unm}}（{{=value.ypks}}/{{=value.ks}}）</span>--%>
                    <span tname="{{=value.unm}}" ypks="{{=value.ypks}}" ks="{{=value.ks}}">{{=value.unm}}</span>
                </li>
                {{?}}
                {{?value.subjectIds>0}}
                <li ids="{{=value.uid}}">
                    <%--<span tname="{{=value.unm}}" ypks="{{=value.ypks}}" ks="{{=value.ks}}">{{=value.unm}}（{{=value.ypks}}/{{=value.ks}}）<i--%>
                    <%--class="inline kt"></i></span>--%>
                    <span tname="{{=value.unm}}" ypks="{{=value.ypks}}" ks="{{=value.ks}}">{{=value.unm}}<i
                            class="inline kt"></i></span>
                </li>
                {{?}}
                {{~}}
            </script>
            <script type="text/template" id="tag_templ">
                {{~it:value:index}}
                <li ids="{{=value.id}}">
                    <span tname="{{=value.name}}" ypks="{{=value.ypks}}" ks="{{=value.allks}}">{{=value.name}}({{=value.stuCount}})</span>
                </li>
                {{~}}
            </script>
        </div>

        <div class="fl w1000 clearfix" id="paikeByRoom">
            <div class="clearfix">
                <div class="fl ml10">
                    <div class="f16 c-b tag">课表-<span id="clsrom"></span><span id="kuohao">（</span><span id="ypks"></span><span id="fenge">/</span><span id="allks"></span>
                        <button class="b1a ml25" id="back">返回上一步</button>
                        <div class="fr checkfr">
                            <%--<label class="one" id="tagcheck" style="display: none;">--%>
                                <%--&lt;%&ndash;<input type="checkbox" js='bq' class="one" checked=checked>标签&ndash;%&gt;--%>
                            <%--</label>--%>
                            <%--<label id="tid" class="ml140">--%>
                                <%--<input type="checkbox" js='js' class="one" checked=checked>教师--%>
                            <%--</label>--%>
                            <%--<label id="gid">--%>
                                <%--<input type="checkbox" js='js' class="one" checked=checked>教室--%>
                            <%--</label>--%>
                            <%--<label>--%>
                                <%--<input type="checkbox" js='rs' class="two" checked=checked>人数--%>
                            <%--</label>--%>
                            <%--<label class="four">--%>
                                <%--<input type="checkbox" js='xk' class="three" checked=checked>学科--%>
                            <%--</label>--%>
                        </div>
                    </div>
                    <div class="mt10" style="margin-top: -3px;">
                        <table class="tab1">
                            <thead>
                            <tr style="display: none;">
                                <th>
                                    <div class="wd120">班级 / 教室</div>
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
                                    <div class="ke{{=value.serial}} kejiecount">{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em>
                                    </div>
                                    {{~}}
                                </script> 
                            </tr>
                            </tbody>
                        </table>
                        <div style="width: 645px;overflow-x:auto;float: left;background:#f5f5f5;">
                            <table id="MyTable" class="tab2">
                                <thead>
                                <tr class="clroms" style="display: none;">

                                </tr>
                                <script type="text/template" id="clroms_temp">
                                    {{~it:value:index}}
                                    <th class="{{=value.roomId}}" crid="{{=value.id}}">{{=value.roomName}}</th>
                                    {{~}}
                                </script>
                                <tr class="weeks">

                                </tr>
                                <script type="text/template" id="weeks_temp">
                                    {{~it:value:index}}
                                    <th class="{{=value.roomId}}">

                                        <div class="zhou {{=value.id}}" style="width: 925px;display: inline-block">
                                            {{~value.weekList:value2:index2}}
                                            <div class="zh{{=value2.type}} gradeweek">{{=value2.name}}</div>
                                            {{~}}
                                        </div>
                                    </th>
                                    {{~}}
                                </script>
                                </thead>
                                <tbody>
                                <tr class="jxbkj">

                                </tr>

                                <script type="text/template" id="kbByTea">
                                    <td class="gao">
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

                                <script type="text/template" id="jxbkj_temp">
                                    {{ for(var prop in it) { }}
                                    <td class="{{=it[0][0].classroomId}} gao" ids="{{=it[0][0].classroomId}}">
                                        {{for (var i=0;i<7;i++) { }}
                                        <div class="zh{{=i+1}} ke">
                                            {{~it[prop]:value:index}}
                                            {{?value.x==i}}
                                            {{?value.swType==0 && value.isSWAndCourse == 0}}
                                            <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} citt tb-clsrom"
                                                 x="{{=value.x}}" y="{{=value.y}}" ykbid="{{=value.id}}"
                                                 jxbid="{{=value.jxbId}}" ondrop="drop(event,this)"
                                                 ondragover="allowDrop(event)" draggable="false"
                                                 ondragstart="drag(event, this)" title="{{=value.remarks}}" kbType="{{=value.kbType}}">
                                                {{?value.jxbId!=''}}
                                                {{?value.kbType==1}}
                                                <p class="zb pic"></p>
                                                {{??value.kbType==2}}
                                                <p class="zbl pic"></p>
                                                {{??value.kbType==4}}
                                                <p class="zhuan pic"></p>
                                                {{??value.kbType==6}}
                                                <p class="dan pic"></p>
                                                {{?}}
                                                {{?value.njxbName!=''&& value.njxbName!=null}}
                                                <div class="ls"><p class="whide"
                                                                   title="{{=value.jxbName}}(单)({{=value.studentCount}}人)/{{=value.njxbName}}(双)({{=value.nstudentCount}}人)
                                                                   &#10{{=value.teacherName}}(单)/{{=value.nteacherName}}(双)">
                                                    <em class="vxk">{{=value.jxbName}}(单)</em><em class="vrs">({{=value.studentCount}}人)</em>/<em
                                                        class="vxk">{{=value.njxbName}}(双)</em><em class="vrs">({{=value.nstudentCount}}人)</em></br>
                                                    <em class="vxk">{{=value.teacherName}}(单)</em>/<em class="vxk">{{=value.nteacherName}}(双)</em>
                                                </p></div>
                                                {{??}}
                                                <div class="ls"><p class="whide"
                                                                   title="{{=value.jxbName}}({{=value.studentCount}})">
                                                    <em class="vxk">{{=value.jxbName}}</em><em class="vrs">({{=value.studentCount}}人)</em>
                                                </p>

                                                    <p class="vjs" js='{{=value.teacherId}}' tid="{{=value.teacherId}}">
                                                        {{=value.teacherName}}</p>

                                                    <p class="vbq" title="{{=value.remarks}}">{{=value.remarks}}</p>
                                                </div>
                                                {{?}}
                                                {{?}}
                                            </div>
                                            {{?}}
                                            {{?value.swType!=0 && value.isSWAndCourse == 1}}
                                            <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} citt tb-clsrom"
                                                 x="{{=value.x}}" y="{{=value.y}}" ykbid="{{=value.id}}"
                                                 jxbid="{{=value.jxbId}}" ondrop="drop(event,this)"
                                                 ondragover="allowDrop(event)" draggable="false"
                                                 ondragstart="drag(event, this)" title="{{=value.remarks}}" kbType="{{=value.kbType}}">
                                                <p class="shi pic"></p>
                                                {{?value.jxbId!=''}}
                                                {{?value.kbType==1}}
                                                <p class="zb pic"></p>
                                                {{??value.kbType==2}}
                                                <p class="zbl pic"></p>
                                                {{??value.kbType==4}}
                                                <p class="zhuan pic"></p>
                                                {{??value.kbType==6}}
                                                <p class="dan pic"></p>
                                                {{?}}
                                                {{?value.njxbName!=''&& value.njxbName!=null}}
                                                <div class="ls"><p class="whide"
                                                                   title="{{=value.jxbName}}(单)({{=value.studentCount}}人)/{{=value.njxbName}}(双)({{=value.nstudentCount}}人)
                                                                   &#10{{=value.teacherName}}(单)/{{=value.nteacherName}}(双)">
                                                    <em class="vxk">{{=value.jxbName}}(单)</em><em class="vrs">({{=value.studentCount}}人)</em>/<em
                                                        class="vxk">{{=value.njxbName}}(双)</em><em class="vrs">({{=value.nstudentCount}}人)</em></br>
                                                    <em class="vxk">{{=value.teacherName}}(单)</em>/<em class="vxk">{{=value.nteacherName}}(双)</em>
                                                </p></div>
                                                {{??}}
                                                <div class="ls"><p class="whide"
                                                                   title="{{=value.jxbName}}({{=value.studentCount}})">
                                                    <em class="vxk">{{=value.jxbName}}</em><em class="vrs">({{=value.studentCount}}人)</em>
                                                </p>

                                                    <p class="vjs" js='{{=value.teacherId}}' tid="{{=value.teacherId}}">
                                                        {{=value.teacherName}}</p>

                                                    <%--<p class="vbq" title="{{=value.remarks}}">{{=value.remarks}}</p>--%>
                                                </div>
                                                {{?}}
                                                {{?}}
                                            </div>
                                            {{?}}
                                            {{?value.swType ==1 && value.isSWAndCourse == 0}}
                                            <div class="ke{{=value.y+1}} red idx{{=value.x}}{{=value.y}} tb-clsrom" x="{{=value.x}}" y="{{=value.y}}"  ykbid="{{=value.id}}" title="{{=value.remarks}}">
                                                <div>{{=value.remarks}}</div>
                                            </div>
                                            {{?}}
                                            {{?value.swType==2 && value.isSWAndCourse == 0}}
                                            <div class="ke{{=value.y+1}} red idx{{=value.x}}{{=value.y}} tb-clsrom" x="{{=value.x}}" y="{{=value.y}}" ykbid="{{=value.id}}" title="{{=value.remarks}}">
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
                <div class="fr w210 zh-li h610 ha mt30">

                    <div class="zh-li mt15 clearfix xkb">
                    </div>
                    <script type="text/template" id="xkb_temp">
                        {{~it:value:index}}
                        <div class="inline fl w185 ml5 tea">
                            <span class="mt10 inline" tid = "{{=value.teacherId}}" tname = "{{=value.teacherName}}">{{=value.teacherName}}</span>
                            <table class="zzh-tab fl inline mt5">
                                <thead>
                                <tr>
                                    <th width="23"><em class="inline w23"></em></th>
                                    <th width="23"><em class="inline w23">一</em></th>
                                    <th width="23"><em class="inline w23">二</em></th>
                                    <th width="23"><em class="inline w23">三</em></th>
                                    <th width="23"><em class="inline w23">四</em></th>
                                    <th width="23"><em class="inline w23">五</em></th>
                                    <th width="23"><em class="inline w23">六</em></th>
                                    <th width="23"><em class="inline w23">日</em></th>
                                </tr>
                                </thead>
                                <tbody>
                                {{~value.courseRangeDTOList:val2:idx}}
                                <tr>
                                    <td>{{=idx+1}}</td>
                                    <td style="cursor: pointer" class="{{=value.teacherId}}0{{=idx}} caonima" index="0" y="{{=idx}}"></td>
                                    <td style="cursor: pointer" class="{{=value.teacherId}}1{{=idx}} caonima" index="1" y="{{=idx}}"></td>
                                    <td style="cursor: pointer" class="{{=value.teacherId}}2{{=idx}} caonima" index="2" y="{{=idx}}"></td>
                                    <td style="cursor: pointer" class="{{=value.teacherId}}3{{=idx}} caonima" index="3" y="{{=idx}}"></td>
                                    <td style="cursor: pointer" class="{{=value.teacherId}}4{{=idx}} caonima" index="4" y="{{=idx}}"></td>
                                    <td style="cursor: pointer" class="{{=value.teacherId}}5{{=idx}} caonima" index="5" y="{{=idx}}"></td>
                                    <td style="cursor: pointer" class="{{=value.teacherId}}6{{=idx}} caonima" index="6" y="{{=idx}}"></td>
                                </tr>
                                {{~}}
                                </tbody>
                            </table>
                        </div>
                        {{~}}
                    </script>
                    <ul class="jxbs" style="display: none">

                    </ul>
                    <script type="text/template" id="jxbs_templ">
                        {{~it:value:index}}
                        <li>
                            <em class="f12 ibk c50">
                                {{?value.nickName!=''}}
                                {{=value.nickName}}
                                {{??}}
                                {{=value.name}}
                                {{?}}
                                ({{=value.studentCount}})</em>
                            <em class="f12 ibk">{{=value.teacherName}}</em>
                            <em class="f12 ibk">{{=value.classroomName}}</em>
                            <em class="f12 ibk whide" title="{{=value.remarks}}">{{=value.remarks}}</em>
                        </li>
                        {{~}}

                    </script>
                </div>
            </div>



        </div>
    </div>
    <!--发布-->
    <div class="bg"></div>
    <div class="popup bf-popup">
        <div class="popup-top">
            <em id="fabName">发布</em>
            <i class="fr close"></i>
        </div>
        <dl class="g-dl">
            <dd class="mt25">
                <span class="mr15">开始周</span>
                <select class="sel2 kaishi">
                </select>
                <script type="text/template" id="week_temp">
                    {{~it:value:index}}
                    <option value="{{=value.serial}}">{{=value.numberWeek}}({{=value.start}})</option>
                    {{~}}
                </script>
            </dd>
            <dd class="mt25">
                <span class="mr15">结束周</span>
                <select class="sel2 jieshu">
                </select>
            </dd>
        </dl>
        <div class="popup-btn mt25">
            <button class="ss stff">确定</button>
            <button class="qxx">取消</button>
        </div>
    </div>
    <div class="popup bt-popup">
        <div class="popup-top">
            <em>加载</em>
            <i class="fr close"></i>
        </div>
        <div class="mmtt15 bul">
            <script type="text/template" id="uu_temp">
                {{~it:value:index}}
                <li ids="{{=value.id}}">{{=value.name}}</li>
                {{~}}
            </script>
            <ul id="uu">

            </ul>
        </div>
        <div class="popup-btn mt25">
            <button class="ss queding">确定</button>
            <button class="qxx">取消</button>
        </div>
    </div>
    <div class="popup loading-p">
        发布中
        <img src="/static_new/images/new33/loading.gif">
    </div>
    <div class="jhh">
        <div class="jhh-top">数据处理中，请稍等！</div>
        <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
        <span class="jhhSp"></span>
    </div>
</div>
<!--发布-->
<div class="popup fb-popup">
    <div class="popup-top">
        <em>课表发布记录</em>
        <i class="fr close"></i>
    </div>
    <div class="cou-tab mtr32">
        <table>
            <thead>
            <tr>
                <th class="w260">发布日期</th>
                <th class="w175">作用周</th>
                <th class="w135">状态</th>
                <th class="w115">操作</th>
            </tr>
            </thead>
        </table>
        <div class="ha715">
            <table>
                <tbody class="fblog">

                </tbody>
                <script type="text/template" id="fblogTmpl">
                    {{~it:value:index}}
                    <tr>
                        <td class="w260">{{=value.time}}</td>
                        <td class="w175">{{=value.week}}</td>
                        <td class="w135">已发布</td>
                        <td class="w115">
                            <%--<em class="cursor c6f">取消</em>--%>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
    <div class="popup-btn mt10">
        <button class="ss queding">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>
<div class="z-popup popup">
    <div class="popup-top">
        <em class="fl">清空</em>
        <i class="close fr"></i>
    </div>
    <div class="zbot">
        <div class="dct">
            <button class="zbtn1" id="clearByCid">清空当前教室课表</button>
        </div>
        <div class="dct">
            <button class="zbtn2" id="clearAll">清空所有教室课表</button>
        </div>
    </div>
</div>
<div class="z-popup1 popup" hidden>
    <div class="popup-top">
        <em class="fl">清空</em>
        <i class="close fr"></i>
    </div>
    <div class="zbot">
        <div class="dct">
            <button class="zbtn1" id="clearByTea">清空当前教师课表</button>
        </div>
        <div class="dct">
            <button class="zbtn2" id="clearAllTea">清空所有教师课表</button>
        </div>
    </div>
</div>


<div class="dialog" id="moveDialog">
    <div class="dialog-title" id="movebar"><span class="name"></span><a class="close dclose">关闭</a></div>
    <div class="tb">
       <div class="tbSty">
         <table>
        <thead>
            <tr><th width="30"></th><th>周一</th><th>周二</th><th>周三</th><th>周四</th><th>周五</th><th>周六</th><th>周日</th></tr>
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
                <td>
                    {{=index + 1}}
                </td>
                <td class="itt1">
                </td>
                <td  class="itt1">
                </td>
                <td  class="itt1">
                </td>
                <td  class="itt1">
                </td>
                <td  class="itt1">
                </td>
                <td  class="itt1">
                </td>
                <td  class="itt1">
                </td>
            </tr>
            {{~}}
        </script>

        <tbody id="tbd">

        </tbody>

        <%--<tbody>--%>
            <%--<tr><td>1</td>--%>
                <%--<td><p>操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
            <%--</tr>--%>
            <%--<tr><td>2</td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
            <%--</tr>--%>
            <%--<tr><td>3</td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
                <%--<td><p>教室：操场1</p><p>教学班：高二(1,3,4,7)体育</p></td>--%>
            <%--</tr>--%>
        <%--</tbody>--%>
    </table>
       </div>
    </div>
</div>


</body>
</html>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('pkTableTiaoke', function (pkTableTiaoke) {
        pkTableTiaoke.init();
    });
</script>
