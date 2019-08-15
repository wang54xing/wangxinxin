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
    <title>多步调课</title>
    <meta charset="utf-8">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/table2.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/new33/0.1.0/paike.js"></script>
    <style type="text/css">
        .zbot{
            margin: 30px 0px 25px 34px;
        }
        * {
            -moz-user-select: none;
            -webkit-user-select: none;
        }
        .zh-li{
            overflow-y: auto;
        }
        .classrooms li{
            border-bottom: 1px solid #dfdfdf;
        }
        .ul-main li {
            border-bottom: 1px dashed #d1d1d1;
            line-height: 40px;
            /*height: 40px;*/
            margin: 0;
            width: 1193px;
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
            background:#AFEEEE;
        }

        div.active {
            background: #AFEEEE;
        }

        div.actv {
            /*border: 1px dashed red;*/
            background: #AFEEEE;
        }

        div.active:hover {
            background: #AFEEEE;
        }

        .tag {
            margin-top: 20px;
            margin-bottom: 5px;
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
        .tag div {
            float: right;
            position: relative;
            height: 40px;
            border: 1px solid #F48F49;
            width: 200px;
            border-radius: 3px;
        }
        .tag div span.active {
            color: #fff;
            background: #F48F49;
        }
        .tag div span {
            float: left;
            width: 100px;
            text-align: center;
            line-height: 40px;
            height: 40px;
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
            width: 210px;
            /*position: fixed;*/
            /*top: 100px;*/
            /*right: 50%;*/
            /* min-height: 190px; */
            z-index: 100;
            background: #fff;
            /*overflow: hidden;*/
            /*margin-right: -545px;*/
            margin-top: 12px;
        }

        .kpjxblist {
            width: 208px !important;
        }

        .fixedcont .ul1 {
            z-index: 1;
            width: 160px;
            height: 490px;
            overflow-x: hidden;
            overflow-y: auto;
            cursor: default;
        }

        .fixedcont .ul1 li.over {
            background: #e5e5e5 !important;
            border: 1px solid #ccc !important;
            color: #666;
        }

        .fixedcont .ul1 li {
            min-height: 76px;
            width: 160px;
            margin: 10px 0px 0px 22px;
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
            height: 45px;
            line-height: 45px;
            text-align: center;
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

        .xkbColor{
            background: #AFEEEE!important;
        }

        .fixedcont1{
            width: 305px;
            position: fixed;
            top: 100px;
            right: 50%;
            /* min-height: 190px; */
            z-index: 100;
            background: #fff;
            overflow: hidden;
            margin-right: -545px;
            top: 320px;
        }

        .grayhui{
            background-color: #C0C0C0!important;
        }
        #movebg1 {
            height: 40px;
            background: #fff;
            border: 1px dashed #999;
            line-height: 40px;
            text-align: center;
            cursor: move;
            transition: all .3s;
            overflow: hidden;
        }
        .fixedcont1 .info {
            background: #5851db;
            color: #fff;
            padding: 10px 10px 40px 3px;
        }
       .cont1{
           border: 1px solid #f77737;
           border-top: none;
           height: 585px;
           overflow-x: hidden;
           overflow-y: auto;
       }
       .conTab tr th ,.conTab tr td{
           width: 30px;
           height: 30px;
           border:1px solid #dfdfdf;
           color: #000;
       }
        .conDv{
            margin: 10px 0px 10px 0px;
        }
        .conDv>p{
            margin: 10px 0px;;
        }
        .conTab tr th{
            background:#6495ed!important;
            color: white!important;
        }
        .conTab tr td:nth-child(1){
            background:#6495ed!important;
            color: white!important;
            border:1px solid #fff;
        }
        .zTab tr th{
            width: 153px;
            height: 39px;
            background: #20B2AA;
            color: white;
        }
        .zTab tr td{
            width: 128px;
            height: 50px;
            border:1px solid #dfdfdf;
        }
        .conEm{
            display: block;
            font-size: 32px;
            cursor: pointer;
            float: right;
        }
       .fixedcont .info .p4{
           font-size: 16px;
           height: 30px;
           line-height: 30px;
       }
        #movebg1{
            display: none;
        }
        .conEm{
            display: none;
        }
        .p5{
            display: inline-block;
            float: right;
            margin-top: 6px;
            cursor: pointer;
        }
        .cont1{
            display: none;
        }
        .e9{
            background: #e9e9e9;
        }
        .sel1{
            border: 1px solid #dfdfdf;
            width: 180px;
            height : 40px;
            border-radius: 5px;
            margin: 0px 11px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $('.conEm').click(function(){
                $('.fixedcont1').css('width',130);
                $('.zh-li').css('height',0);
                $(this).hide();
                $('.cont1').hide();
                $('#movebg1').hide();
                $('.p5').show();
            })
            $('.p5').click(function(){
                $('.fixedcont1').css('width',225);
                $('.zh-li').css('height',550);
                $(this).hide();
                $('.cont1').show();
                $('#movebg1').show();
                $('.conEm').show();
            })
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
            $('.ul-main > li > label').click(function () {
                var jsn = $(this).attr('js');
                $(this).addClass('active').siblings('label').removeClass('active')
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

    <script type="text/javascript">
        // window.onload = function () {
        //     var ddd = document.getElementById("movebg1"); // 获取DIV对象
        //     ddd.onmousedown = function (e) {
        //         var event1 = e || window.event;                  // IE、火狐获取事件对象
        //         var eventX = event1.offsetX || event1.layerX;         // 获取鼠标相对于事件源的X轴
        //         var eventY = event1.offsetY || event1.layerY;        // 获取鼠标相对于事件源的Y轴
        //         var flag = true;
        //         this.onmousemove = function (e) {
        //             if (flag) {
        //                 var event2 = e || window.event;
        //                 var eveX = event2.clientX;             // 获取鼠标相对于浏览器x轴的位置
        //                 var eveY = event2.clientY;             // 获取鼠标相对于浏览器Y轴的位置
        //                 $('#move1').css('top', eveY - eventY);
        //                 $('#move1').css('left', eveX - eventX);
        //             }
        //         }
        //         this.onmouseup = function () {
        //             if (flag) {
        //                 flag = false;
        //             }
        //         }
        //     }
        // }

    </script>
    <script type="text/javascript">

    </script>
</head>
<body>
<input id="subjectId" hidden>
<jsp:include page="head.jsp"></jsp:include>
<div class="right-pos">
    <div class="ov-hi">
        <div class="td-over">
            <a class="tdk1" href="/newisolatepage/kstkClass.do">快速调课</a>
            <a class="dbtk2" href="/newisolatepage/dbtkClass.do">多步调课</a>
            <a class="zjtk1" href="/newisolatepage/zjtkClass.do">增减调课</a>
            <a class="dk1" href="/newisolatepage/exchangeClass.do">代课</a>
            <a href="/newisolatepage/exClasslog.do" class="sh1">审核&记录</a>
        </div>
    </div>
</div>
<div class="fixedcont1" style="width: 225px;display: none;">
    <div class="info">
        <p class="p1 p4">
            <label style="float: left;margin-left: 15px;margin-top: 7px;">待调课程</label>
            <em class="conEm" style="cursor: pointer">×</em>
        </p>
    </div>
    <div class="zh-li">
        <ul class="jxbs" style="margin: 10px 0px 0px 10px;">

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
<div class="container clearfix">
    <span id="defaultTerm" style="display: none"></span>

    <div class="clearfix">
        <ul class="ul-main">
            <li class="gaozhong">
            </li>
            <script type="text/template" id="grade_temp">
                {{~it:value:index}}
                <label ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</label>
                {{~}}
            </script>

            <li id="sub" style="display: none">
            </li>
            <script type="text/template" id="subject_temp">
                {{~it:value:index}}
                <label ids="{{=value.subid}}">{{=value.snm}}</label>
                {{~}}
            </script>
            <li hidden>
                <span>课表类型：</span>
                <label id="classroom" class="active">教室课表</label>
                <label id="teacher">教师课表</label>
            </li>
            <li class="jiaoshi">
                <span>教室：</span>
            </li>
            <script type="text/template" id="jiaos">
                {{~it:value:index}}
                <label ids="{{=value.roomId}}" js="{{=value.roomId}}">{{=value.roomName}}</label>
                {{~}}
            </script>
        </ul>
    </div>
    <div class="tag clearfix">
        <%--<button class="b1">编辑</button>--%>
        <div style="display: none;">
            <button class="b5" style="width: 70px;padding: 0">保存</button>
            <button class="b6" style="width: 70px;padding: 0">另存为</button>
            <button class="b4" style="width: 70px;padding: 0">加载</button>
            <button class="b7" style="width: 100px;padding: 0;margin-left: 20px;" hidden>发布</button>
            <button class="b8" hidden>取消发布</button>
            <button class="fb" style="width: 100px;padding: 0;background: #7093bf">发布记录</button>
            <button class="b2" style="width: 100px;padding: 0;margin-left: 40px;">撤销</button>
            <button class="b3" style="width: 102px;padding: 0">清空</button>
            <button class="b99" style="background: #00a3e9">自动排课</button>
            <button class="b10" id="tiaozheng">排课调整</button>
        </div>
        <%--<button class="b9">导出</button>--%>
            <select  class="sel1 weekSel" type="text" id="week">

            </select>
            <%--<em id="char" hidden>至</em>--%>

            <%--<select class="sel1" type="text" id="week1" hidden>--%>

            <%--</select>--%>
            <%--<script type="text/template" id="week_temp">--%>
                <%--{{~it:value:index}}--%>
                <%--<option value="{{=value.serial}}" wid="{{=value.id}}">{{=value.numberWeek}}&nbsp;{{=value.start}}</option>--%>
                <%--{{~}}--%>
            <%--</script>--%>
            <button class="b2" style="width: 100px;padding: 0;margin-left: 20px;">撤销</button>
            <button class="b8">还原课表</button>
            <button class="b5" style="width: 110px;padding: 0;background: #c13584" id="b5">保存并提交</button>
        <div class="fr tk" id="cd">
            <span class="active" tk="1">短期</span>
            <span tk="2">长期</span>
        </div>
    </div>
    <div class="clearfix wtkkb" style="position: relative; display: none;"><span class="tCenter">课表暂未发布！</span></div>
    <div>
        <div class="fl fixedcont">
            <div class="info">
                <p class="p1"><label>待调课程</label>
            </div>
            <div class="cont">
                <ul class="ul1 kpjxblist cx" style="width: 208px;">

                </ul>
                <script type="text/template" id="kpjxblist_temp">
                    {{~it:value:index}}
                        <li clsrmId="{{=value.classroomId}}" tid="{{=value.teacherId}}"
                            jxbType="{{=value.type}}" jxbId="{{=value.id}}">
                            <div class="ls">
                                <p>
                                    <em class="vxk">
                                        {{?value.nickName != ""}}
                                            {{=value.nickName}}
                                        {{??}}
                                            {{=value.name}}
                                        {{?}}
                                    </em>
                                    <em class="vrs">({{=value.studentCount}})</em>

                                </p>
                                <p class="vbq">
                                </p>
                            </div>
                            <span>{{=value.tkKs}}</span>
                        </li>
                    {{~}}
                </script>
            </div>
        </div>
        <div class="w1000 clearfix fr" id="paikeByRoom" style="width: 985px;">
            <div class="clearfix">
                <div class="fl ml10">
                    <div class="f16 c-b">
                        <div style="display: none;">
                            课表-
                            <span id="clsrom"></span>
                            <span id="kuohao">（</span><span id="ypks"></span>
                            <span id="fenge">/</span>
                            <span id="allks"></span>
                        </div>
                        <div class="fr checkfr"  style="display: none;">
                            <label class="one" id="tagcheck" style="display: none;">
                                <%--<input type="checkbox" js='bq' class="one" checked=checked>标签--%>
                            </label>
                            <label id="tid" class="ml140">
                                <input type="checkbox" js='js' class="one" checked=checked>教师
                            </label>
                            <label id="gid">
                                <input type="checkbox" js='js' class="one" checked=checked>教室
                            </label>
                            <label>
                                <input type="checkbox" js='rs' class="two" checked=checked>人数
                            </label>
                            <label class="four">
                                <input type="checkbox" js='xk' class="three" checked=checked>学科
                            </label>
                        </div>
                    </div>
                    <div class="mt10">
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
                                <%--<script type="text/template" id="weeks_temp">--%>
                                    <%--{{~it:value:index}}--%>
                                    <%--<th class="{{=value.roomId}}">--%>

                                        <%--<div class="zhou {{=value.id}}" style="width: 925px;display: inline-block">--%>
                                            <%--{{~value.weekList:value2:index2}}--%>
                                            <%--<div class="zh{{=value2.type}} gradeweek">{{=value2.name}}</div>--%>
                                            <%--{{~}}--%>
                                        <%--</div>--%>
                                    <%--</th>--%>
                                    <%--{{~}}--%>
                                <%--</script>--%>
                                <script type="text/template" id="weeks_temp">
                                    {{~it:value:index}}
                                    <th class="{{=value.roomId}}">
                                        <div class="zhou {{=value.id}}" style="width: 925px;display:inline-block">
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
                                            <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} citt tb-clsrom"
                                                 x="{{=value.x}}" y="{{=value.y}}" ykbid="{{=value.id}}"
                                                 jxbid="{{=value.jxbId}}" ondrop="drop(event,this)"
                                                 ondragover="allowDrop(event)" draggable="false"
                                                 ondragstart="drag(event, this)" type="{{=value.type}}">
                                                {{?value.jxbId!=''}}
                                                    {{?value.type==1}}
                                                    <p class="zb pic"></p>
                                                    {{??value.type==2}}
                                                    <p class="zbl pic"></p>
                                                    {{??value.type==4}}
                                                    <p class="zhuan pic"></p>
                                                    {{??value.type==6}}
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
                                                {{?value.jxbName!=''&& value.jxbName!=null}}
                                                <div class="ls"><p class="whide"
                                                                   title="{{=value.jxbName}}({{=value.studentCount}}人)">
                                                    <em class="vxk">{{=value.jxbName}}</em><em class="vrs">({{=value.studentCount}}人)</em>
                                                </p>
                                                    {{?value.type != 4}}
                                                    <p class="vjs" js='{{=value.teacherId}}' tid="{{=value.teacherId}}">
                                                        {{?value.teacherName!=''&& value.teacherName!=null}}
                                                        {{=value.teacherName}}
                                                        {{?}}
                                                    </p>
                                                    {{?}}
                                                    <p class="vbq" title="{{=value.remarks}}">{{=value.remarks}}</p>
                                                </div>
                                                {{?}}
                                                {{?}}
                                                {{?}}
                                            </div>
                                            {{?}}
                                            {{?value.swType!=0 && value.isSWAndCourse == 1}}
                                            <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} citt tb-clsrom"
                                                 x="{{=value.x}}" y="{{=value.y}}" ykbid="{{=value.id}}"
                                                 jxbid="{{=value.jxbId}}" ondrop="drop(event,this)"
                                                 ondragover="allowDrop(event)" draggable="false"
                                                 ondragstart="drag(event, this)" title="{{=value.remarks}}" type="{{=value.type}}">
                                                <p class="shi pic"></p>
                                                {{?value.jxbId!=''}}
                                                {{?value.type==1}}
                                                <p class="zb pic"></p>
                                                {{??value.type==2}}
                                                <p class="zbl pic"></p>
                                                {{??value.type==4}}
                                                <p class="zhuan pic"></p>
                                                {{??value.type==6}}
                                                <p class="dan pic"></p>
                                                {{?}}
                                                {{?value.njxbName!=''&& value.njxbName!=null}}
                                                <div class="ls"><p class="whide"
                                                                   title="{{=value.jxbName}}(单)({{=value.studentCount}}人)/{{=value.njxbName}}(双)({{=value.nstudentCount}}人)
                                                                   &#10{{=value.teacherName}}(单)/{{=value.nteacherName}}(双)">
                                                    <em class="vxk">{{=value.jxbName}}(单)</em><em class="vrs">({{=value.studentCount}}人)</em>/<em
                                                        class="vxk">{{=value.njxbName}}(双)</em><em class="vrs">({{=value.nstudentCount}}人)</em>
                                                </p></div>
                                                {{??}}
                                                {{?value.jxbName!=''&& value.jxbName!=null}}
                                                <div class="ls"><p class="whide"
                                                                   title="{{=value.jxbName}}({{=value.studentCount}}人)">
                                                    <em class="vxk">{{=value.jxbName}}</em><em class="vrs">({{=value.studentCount}}人)</em>
                                                </p>
                                                    {{?value.type != 4}}
                                                    <p class="vjs" js='{{=value.teacherId}}' tid="{{=value.teacherId}}">
                                                        {{?value.teacherName!=''&& value.teacherName!=null}}
                                                        {{=value.teacherName}}
                                                        {{?}}
                                                    </p>
                                                    {{?}}
                                                    <p class="vbq" title="{{=value.remarks}}">{{=value.remarks}}</p>
                                                </div>
                                                {{?}}
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
                <div class="fr w210 h610 ha" style="height: 650px;width: 200px;">
                    <div class="cont1 clearfix" style="margin-top: 0px;border:none;display: block">

                    </div>
                </div>
            </div>
            <%--<div class="zh-li mt15 clearfix xkb">--%>
            <%--</div>--%>
            <!--专项课-->
            <div class="zDv" style="margin-left: 10px;display: none;">
                <p style="margin: 10px 0 5px;">专项课</p>
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
                    <tbody id="zhuanxiang">

                    </tbody>
                    <script type="text/template" id="zhuanxiang_temp">
                        {{~it:value:index}}
                            <tr>
                                <td>{{=value.zuHeName}}</td>
                                <td>{{=value.jxbName}}</td>
                                <td>{{=value.teaName}}</td>
                                <td>{{=value.roomName}}</td>
                                <td>{{=value.stuCount}}</td>
                            </tr>
                        {{~}}
                    </script>
                </table>
            </div>
            <script type="text/template" id="xkb_temp">
                {{~it:value:index}}
                <div class="conDv">
                    <p>{{=value.teacherName}}</p>
                    <table class="conTab">
                        <thead>
                        <tr>
                            <th></th>
                            <th>一</th>
                            <th>二</th>
                            <th>三</th>
                            <th>四</th>
                            <th>五</th>
                            <th>六</th>
                            <th>日</th>
                        </tr>
                        </thead>
                        <tbody>
                        {{~value.courseRangeDTOList:val2:idx}}
                        <tr>
                            <td>{{=idx+1}}</td>
                            <td class="{{=value.teacherId}}0{{=idx}}"></td>
                            <td class="{{=value.teacherId}}1{{=idx}}"></td>
                            <td class="{{=value.teacherId}}2{{=idx}}"></td>
                            <td class="{{=value.teacherId}}3{{=idx}}"></td>
                            <td class="{{=value.teacherId}}4{{=idx}}"></td>
                            <td class="{{=value.teacherId}}5{{=idx}}"></td>
                            <td class="{{=value.teacherId}}6{{=idx}}"></td>
                        </tr>
                        {{~}}
                        </tbody>
                    </table>
                </div>
                {{~}}
            </script>


        </div>
    </div>
    <!--发布-->
    <div class="bg"></div>
    <%--<div class="popup bf-popup">--%>
        <%--<div class="popup-top">--%>
            <%--<em id="fabName">发布</em>--%>
            <%--<i class="fr close"></i>--%>
        <%--</div>--%>
        <%--<dl class="g-dl">--%>
            <%--<dd class="mt25">--%>
                <%--<span class="mr15">开始周</span>--%>
                <%--<select class="sel2 kaishi">--%>
                <%--</select>--%>
                <%--<script type="text/template" id="week_temp">--%>
                    <%--{{~it:value:index}}--%>
                    <%--<option value="{{=value.serial}}">{{=value.numberWeek}}({{=value.start}})</option>--%>
                    <%--{{~}}--%>
                <%--</script>--%>
            <%--</dd>--%>
            <%--<dd class="mt25">--%>
                <%--<span class="mr15">结束周</span>--%>
                <%--<select class="sel2 jieshu">--%>
                <%--</select>--%>
            <%--</dd>--%>
        <%--</dl>--%>
        <%--<div class="popup-btn mt25">--%>
            <%--<button class="ss stff">确定</button>--%>
            <%--<button class="qxx">取消</button>--%>
        <%--</div>--%>
    <%--</div>--%>
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
        发布中s
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

<div class="z-popup2 popup">
    <div class="popup-top">
        <em class="fl">保存并提交</em>
        <i class="close fr"></i>
    </div>
    <div class="zbot">
        <select class="sel1" type="text" id="week2" disabled>

        </select>
        至
        <select class="sel1" type="text" id="week1">

        </select>
        <script type="text/template" id="week_temp">
            {{~it:value:index}}
            <option value="{{=value.serial}}" wid="{{=value.id}}">{{=value.numberWeek}}&nbsp;{{=value.start}}</option>
            {{~}}
        </script>
    </div>
    <div class="popup-btn mt10" style="margin-bottom: 15px;">
        <button class="ss queding" id="qd">确定</button>
    </div>
</div>
<div class="z-popup3 popup">
    <div class="popup-top">
        <em class="fl">长期调课操作结果</em>
        <i class="close fr"></i>
    </div>
    <div class="p3Oh">
        <div id="weeks"></div>
        <p style="margin-top: 25px;">以下教学周原课节与调课周原课表不一致，未能调课，请使用短期调课设置：</p>
        <div style="margin-top: 25px;" id="list">
            <script type="text/template" id="list_temp">
                {{~it:value:index}}
                <div >
                    <span>{{=value.week}}</span>,
                    <span>{{=value.xy}}</span>,
                    {{?value.jxbName != "" && value.jxbName != null}}
                    <span>{{=value.jxbName}},</span>
                    {{?}}
                    <span>{{=value.classRoomName}}</span>
                </div>
                {{~}}
            </script>
        </div>
    </div>
</div>
<div class="jhh">
    <div class="jhh-top"></div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp"></span>
</div>
</body>
</html>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('dbtkClass', function (dbtkClass) {
        dbtkClass.init();
    });
</script>
