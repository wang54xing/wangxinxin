<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2018/3/29
  Time: 10:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>增减调课</title>
    <meta charset="utf-8">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/table2.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <%--<script type="text/javascript" src="/static_new/js/modules/new33/0.1.0/paike.js"></script>--%>
    <style type="text/css">
        *{
            -moz-user-select: none;
            -webkit-user-select: none;
        }
        .wind-new-tea ul li{
            height: auto;
        }
        .ul-main li{
            border-bottom: 1px dashed #d1d1d1;
            line-height: 40px;
            /*height: 40px;*/
            margin: 0;
            width: 1199px;
        }
        .ul-main > li > label{
            border-radius: 3px;
            height: 26px;
            margin: 7px 30px 0 0;
            line-height: 26px;
        }
        .ul-main > li > label.active{
            color: #6f69d6!important;
            font-weight: bold;
            background: transparent!important;
        }
        .tab1 td{
            background:#3CB371;
            font-weight: bold;
            color: #fff;
        }
        td.ke{
            width: 120px;
        }
        .tab1 th{
            background:#3CB371 !important;
            font-weight: bold;
            color: #fff;
        }
        .tab1{
            float: left;
        }
        td>div{
            width: 100px;
            line-height: 30px;
            float: left;
        }
        td>div>div{
            width: 99px;
            height: 60px;
            line-height: 60px;
            border-bottom: 1px solid #dcdcdc;
            border-right: 1px solid #dcdcdc;
        }
        th>div>div{
            min-width: 100px;
            height: 40px;
            float: left;
            line-height: 40px;
        }
        .ke em{
            font-size: 12px;
        }
        th{
            min-width: 100px;
            height: 40px;
        }
        .gray{
            background:#C0C0C0;
        }
        .wd100{
            width: 100px;
        }
        .lth{
            float: right;
        }
        .wd120{
            width: 120px !important;
            font-weight: bold;
        }
        .psre div{
            width: 120px;
            height: 49px;
            line-height: 20px;
            padding-top: 11px;
            border-bottom: 1px solid #dcdcdc;
        }
        .ul-main{
            float: left;
            background: url(/static_new/images/new33/bg-line.png) repeat-y;
        }
        .ul-main > li > span{
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
        td>div>div>div{
            height: 60px;
            line-height: 60px;
        }
        td>div>div{
            cursor: pointer;
            position: relative;
        }
        td>div>div:hover{
            background: #f3f3f3;
        }
        div.active{
            background: #AFEEEE;
        }
        div.active2{
            background: #AFEEEE;
        }
        div.act{
            background: #AFEEEE;
        }
        div.active:hover{
            background: #AFEEEE;
        }
        div.active2:hover{
            background: #AFEEEE;
        }
        .tag{
            margin-top: 40px;
            margin-bottom: 20px;
            display: none;
        }
        .tag div{
            float:right;
            position: relative;
            height: 40px;
            border: 1px solid #F48F49;
            width: 200px;
            border-radius: 3px;
        }
        .tag div span.active{
            color: #fff;
            background: #F48F49;
        }
        .tag div span{
            float: left;
            width: 100px;
            text-align: center;
            line-height: 40px;
            height: 40px;
            cursor: pointer;
        }
        .tag button{
            height: 41px;
            padding: 0 23px;
            border-radius: 4px;
            color: #fff;
            margin-right: 10px;
        }
        .tag .b1{ background: #405DE6; }
        .tag .b2{ background: #1ab02f; }
        .tag .b3{ background: #9196aa; }
        .tag .b4{ background: #6f69d6; }
        .tag .b5{ background: #1ab02f; }
        .tag .b6{ background: #f77737; }
        .tag .b7{ background: #c13584; }
        .tag .b8{ background: #f48f49; }
        .tag .b9{ background: #2e94db; margin-right: 0 !important;}
        .fwb{
            font-weight: bold;
        }
        .container{
            padding-right: 100px;
        }
        .tab1 th{
            border: 1px solid #f1f1f1;
        }
        .tab2 tr th:first-child,
        .tab2 tr td:first-child{
            border-left: none !important;
        }
        .tab1 em{
            font-size: 12px;
        }
        .tab2 div.green{
            background:  #90EE90;
            border: 1px dashed #2E8B57;
            color: #333;
        }
        .tab2 div.red{
            background:  #FFB6C1;
            border: 1px dashed red;
            color: #333;
            height: 59px;
        }
        .h0{
            height: 0px !important;
            border: none !important;
        }
        .tabbg{
            position: absolute;
            width: 100%;
            height: 560px;
            top: 0;
            left: 0;
            z-index: 1;
            opacity: 0;
        }
        #exchange{
            display: none;
        }
        #daike{
            display: none;
        }
        #back{
            display: none;
        }
        .tab2 div .red {
            background: #FFB6C1;
            border: 1px solid #FFB6C1;
            color: #333;
            height: 59px;
            width: 99px;
        }

        .tab2 div .gray1 {
            background: #AAAAAA;
            border: 1px solid #AAAAAA;
            color: #333;
            height: 59px;
            width: 99px;
        }
        .wind-new-tea{
            width: 580px;
        }
        .wind-new-tea ul li .sel12{
            height: 46px;
            border-radius: 4px;
            text-indent: 5px;
            width: 75px;
            border: 1px solid #aaa;
            margin-left: 5px;
        }
       .tab3 tr th {
           background: #ededed;
           color: black;
           border-bottom: none!important;
       }
       .tab3 tr th, .tab3 tr td {
           text-align: center;
           border: 1px solid #969696;
           height: 40px;
           width: 54px;
           min-width: 54px;
        }
        .tab3 tr td:first-child{
            cursor: default;
        }
        .tab3 tr td{
            cursor: pointer;
        }
        .tab3 tr td span{
            display: inline-block;
            width: 100%;
            height: 100%;
        }
        .active{
            background: #AFEEEE;
        }
        .active2{
            background: #AFEEEE;
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
            height: 40px;
            border-radius: 5px;
            margin: 0px;
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
            top: 225px;
            box-shadow: -1px -2px 20px 0px rgba(0, 0, 0, 0.4)
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
        .cou-tab{
            padding: 15px 0px 0px 25px;
            width: 100%;
            box-sizing: border-box;
        }
        .emSty label{
            display: inline-block;
            margin: 0px 2px;
        }
    </style>
    <script type="text/javascript">
        $(function(){

            $('.dbtk-popup i,.qxx').click(function(){
                $(".bg").hide();
                $(".dbtk-popup").hide();
            })
            $('.conEm').click(function(){
                $('.fixedcont1').css('width',130);
                $('.zh-li').css({
                    'height':0,
                    'border':'none'
                });
                $(this).hide();
                $('.cont1').hide();
                $('#movebg1').hide();
                $('.p5').show();
            })

            var l2 = 1;

            $('.ul-main > li > label').click(function(){
                var jsn = $(this).attr('js');
                if(jsn =='all'){
                    $(this).addClass('active').siblings('label').removeClass('active')
                }else{
                    $(this).toggleClass('active');
                    $(this).parent().find('.all').removeClass('active');
                }
            });
            var labelNumber = 4;
            $('.tag label input').click(function(){
                var jsn = $(this).attr('js');
                if($(this).is(':checked')){
                    $('.tab2 .v'+jsn).show();
                    labelNumber++
                }else{
                    labelNumber--;
                    if(labelNumber<1){
                        layer.msg('请至少选择一个标签显示！')
                        $(this).prop('checked','checked')
                        labelNumber =1
                    }else{
                        $('.tab2 .v'+jsn).hide();
                    }
                }
            });

            $('.tag input').click(function(){
                var jsn = $(this).attr('js')
                if($(this).is(':checked')==true){
                    $('div.'+jsn).addClass('active')
                }else{
                    $('div.'+jsn).removeClass('active')
                }
            });
            $('body').on('click','.tab3 td span',function(){
                $(this).toggleClass('active');
            })
            function init(){

            }


        })
    </script>
    <script type="text/javascript">
        window.onload = function () {
            var ddd = document.getElementById("movebg1"); // 获取DIV对象
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
                        $('#move1').css('top', eveY - eventY);
                        $('#move1').css('left', eveX - eventX);
                    }
                }
                this.onmouseup = function () {
                    if (flag) {
                        flag = false;
                    }
                }
            }
        }

    </script>
    <script type="text/javascript">
        $(function(){
            var l1 = 1;
            var l2 = 1;
            var l3 = 1;

            $('.ul-main > li > label').click(function(){
                var jsn = $(this).attr('js');
                if(jsn =='all'){
                    $(this).addClass('active').siblings('label').removeClass('active')
                }else{
                    $(this).toggleClass('active');
                    $(this).parent().find('.all').removeClass('active');
                }
            });


        });
        function allowDrop(ev){
            ev.preventDefault();
        }

        var srcdiv = null;
        function drag(ev,divdom){
            srcdiv=divdom;
            ev.dataTransfer.setData("text/html",divdom.innerHTML);
        }

        function drop(ev,divdom){
            ev.preventDefault();
            if(srcdiv != divdom){
                srcdiv.innerHTML = divdom.innerHTML;
                divdom.innerHTML=ev.dataTransfer.getData("text/html");
            }
        }
        function removed(divdom){
            divdom.parentNode.removeChild(divdom)
        }
        $('.t-down').click(function(){
            $('.td-over').scrollTop();
            $('.td-over').css('margin-top',-91)
        })
        $('.t-top').click(function(){
            $('.td-over').css('margin-top',0)
        })
    </script>
</head>
<body>
<div class="right-pos">
    <div class="ov-hi">
        <div class="td-over">
            <a class="tdk1" href="/newisolatepage/kstkClass.do">快速调课</a>
            <a class="dbtk1" href="/newisolatepage/dbtkClass.do" style="display: block;">多步调课</a>
            <a class="zjtk2" href="/newisolatepage/zjtkClass.do">增减调课</a>
            <a class="dk1" href="/newisolatepage/exchangeClass.do">代课</a>
            <a href="/newisolatepage/exClasslog.do" class="sh1">审核&记录</a>
        </div>
    </div>
</div>
<jsp:include page="head.jsp"></jsp:include>
<div class="fixedcont1" id="move1" style="width: 130px;border:1px solid #dfdfdf;">
    <div id="movebg1">点击可移动</div>
    <div class="info">
        <p class="p1 p4">
            <label style="float: left;margin-left: 15px;margin-top: 7px;">待增课节</label>
            <em class="conEm" style="cursor: pointer">×</em>
        </p>
        <p class="p5">展开 ></p>
    </div>
    <div class="zh-li" style="height: 0px;">
        <div class="cou-tab">
            <table>
                <thead>
                    <tr>
                        <th>教学周</th>
                        <th style="width:150;min-width: 150px;">课节时间</th>
                        <th style="width:150;min-width: 150px;">教学班</th>
                        <th>教师</th>
                    </tr>
                </thead>
                <tbody class="zjkj2">

                </tbody>
                <script type="text/template" id="zjkj_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.week}}</td>
                        <td>{{=value.keji}}</td>
                        <td>{{=value.jxbName}}</td>
                        <td>{{=value.teacherName}}</td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>

</div>
<span id = "defaultTerm" style="display: none"></span>
<div class="container clearfix">
    <div class="clearfix tile">
         <ul class="ul-main">
        <li class="gaozhong">
        </li>
        <script type="text/template" id="grade_temp">
            {{~it:value:index}}
            <label ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</label>
            {{~}}
        </script>
        <script type="text/template" id="KeShiList">
            {{~it:value:index}}
            <label ids="{{=value.id}}" js="ke{{=index+1}}">{{=value.name}}</label>
            {{~}}
        </script>
        <script type="text/template" id="jiaos">
            {{~it:value:index}}
            <label ids="{{=value.roomId}}" js="{{=value.roomId}}">{{=value.roomName}}</label>
            {{~}}
        </script>
        <li class="jiaoshi">
        </li>
         <li class="zhou jxr" style="display: none">
             <span>教学日 : </span>
             <label class="active all" js='all' id = "0">全部</label>
             <label js="zh1" id = "1">周一</label>
             <label js="zh2" id = "2">周二</label>
             <label js="zh3" id = "3">周三</label>
             <label js="zh4" id = "4">周四</label>
             <label js="zh5" id = "5">周五</label>
             <label js="zh6" id = "6">周六</label>
             <label js="zh7" id = "7">周日</label>
         </li>
        <li class="ke" style="display: none">

        </li>
    </ul>
    </div>
    <div class="tag clearfix dhhd" style="margin-top: 20px;margin-bottom: 15px;">
        <span id="name" style="float:left;font-size: 20px;color: #000;"></span>
        <select class="fl sel6 mr30" type="text" id="week" style="height: 42px;margin: -5px 0px 0px 0px;">

        </select>
        <script type="text/template" id="week_temp">
            {{~it:value:index}}
            <option value="{{=value.serial}}" wid="{{=value.id}}">{{=value.numberWeek}}&nbsp;{{=value.start}}</option>
            {{~}}
        </script>
        <span style="color:red;margin-left: 10px;margin-top: 5px;display: inline-block;">请点击课表格子进行操作</span>
        <button class="btn2 fr hhd1" style="width: 100px;padding: 0;background: #c13584;margin-right: 150px;" hidden>删除课节</button>
        <button class="btn1 fr zjkj hhd2" style="width: 100px;padding: 0;margin-left: 20px;background:#1ab02f;margin-right: 150px;" hidden>增加课节</button>
        <div style="display: none;">
            <%--<button class="b1" id="edit">编辑</button>--%>
            <%--<button class="b2" id="back">撤销</button>--%>
            <%--<button class="b4" id="exchange">交换并发布</button>--%>
            <%--<button class="b5" id="daike">代课</button>--%>
            <%--<button class="b7" id="publish">发布</button>--%>
            <div class="fr tk">
                <%--<span class="active" tk="1">短期</span>--%>
                <%--<span tk="2">长期</span>--%>
            </div>
            <%--<label>--%>
                <%--<input type="checkbox" js='bq'  checked=checked>标签--%>
            <%--</label>--%>
            <label>
                <input type="checkbox" js='js'  checked=checked>教师
            </label>
            <label>
                <input type="checkbox" js='rs'  checked=checked>人数
            </label>
            <label>
                <input type="checkbox" js='xk' checked=checked>学科
            </label>
            <select class="fr sel6 mr30" type="text" id="tksel" style="height: 42px;">
                <option value="1">调课</option>
                <option value="2">代课</option>
            </select>
        </div>
    </div>
    <div class="clearfix tkkb" style="position: relative; display: none;">
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
                    <div class="ke{{=value.serial}}">{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></div>
                    {{~}}
                </script>
            </tr>
            </tbody>
        </table>
        <div style="width: 1076px;overflow-x:auto;overflow-y: hidden;float: left;background:#f5f5f5">
            <table id="MyTable" class="tab2">
                <thead>
                <tr class="clroms">

                </tr>
                <script type="text/template" id="clroms_temp">
                    {{~it:value:index}}
                    {{?value.className != ""}}
                    <th class="{{=value.roomId}}" crid="{{=value.id}}">{{=value.roomName}}（{{=value.className}}）</th>
                    {{?}}
                    {{?value.className == ""}}
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
                    <td class="{{=it[0][0].classroomId}} gao" ids = "{{=it[0][0].classroomId}}">
                        {{for (var i=0;i<7;i++) { }}
                        <div class="zh{{=i+1}} ke">
                            {{~it[prop]:value:index}}
                            {{?value.x==i}}
                            {{?value.swType==0 && value.isSWAndCourse == 0}}
                            <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} week{{=value.x}}" crmid="{{=value.classroomId}}" subid="{{=value.subjectId}}" ykbid="{{=value.id}}" jxbtp="{{=value.jxbType}}" jxbid="{{=value.jxbId}}"
                                 <%--ondrop="drop(event,this)" ondragover="allowDrop(event)" draggable="true" ondragstart="drag(event, this)"--%>
                                 disabled="true">
                                {{?value.jxbId!=null}}
                                {{?value.type==1}}
                                <p class="zb pic"></p>
                                {{??value.type==2}}
                                <p class="zbl pic"></p>
                                {{??value.type==4}}
                                <p class="zhuan pic"></p>
                                {{??value.type==6}}
                                <p class="dan pic"></p>
                                {{?}}
                                <div class="ls"><p><em class="vxk" title="{{=value.jxbName}}" style="display: block;width: 100px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">{{=value.jxbName}}</em><em class="vrs" style="display: block">({{=value.studentCount}}人)</em></p>
                                    {{?value.teacherName != null}}
                                    <p class="vjs" js='ls'>{{=value.teacherName}}</p>
                                    {{?}}
                                    <p class="vbq">{{=value.remarks}}</p></div>
                                {{?}}
                            </div>
                            {{?}}
                            {{?value.swType!=0 && value.isSWAndCourse == 1}}
                            <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} week{{=value.x}}" crmid="{{=value.classroomId}}" subid="{{=value.subjectId}}" ykbid="{{=value.id}}" jxbtp="{{=value.jxbType}}" jxbid="{{=value.jxbId}}"
                            <%--ondrop="drop(event,this)" ondragover="allowDrop(event)" draggable="true" ondragstart="drag(event, this)"--%>
                                 disabled="true">
                                <p class="shi1 pic"></p>
                                {{?value.jxbId!=null}}
                                {{?value.type==1}}
                                <p class="zb pic"></p>
                                {{??value.type==2}}
                                <p class="zbl pic"></p>
                                {{??value.type==4}}
                                <p class="zhuan pic"></p>
                                {{??value.type==6}}
                                <p class="dan pic"></p>
                                {{?}}
                                <div class="ls"><p><em class="vxk" title="{{=value.jxbName}}" style="display: block;width: 100px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">{{=value.jxbName}}</em><em class="vrs" style="display: block">({{=value.studentCount}}人)</em></p>
                                    {{?value.teacherName != null}}
                                    <p class="vjs" js='ls'>{{=value.teacherName}}</p>
                                    {{?}}
                                    <p class="vbq">{{=value.remarks}}</p></div>
                                {{?}}
                            </div>
                            {{?}}
                            {{?value.swType==1 && value.isSWAndCourse == 0}}
                            <div class="ke{{=value.y+1}} red" ykbid="{{=value.id}}"><div>{{=value.remarks}}</div></div>
                            {{?}}
                            {{?value.swType==2 && value.isSWAndCourse == 0}}
                            <div class="ke{{=value.y+1}} red" ykbid="{{=value.id}}"><div>{{=value.remarks}}</div></div>
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
    <div class="clearfix wtkkb" style="position: relative; display: none;"><span class="tCenter">课表暂未发布！</span></div>
</div>

<div class="wind wind-new-tea">
    <input id="dktype" hidden>
    <div class="popup-top">
        <em class="d1">长期调课</em>
        <i class="fr close"></i>
    </div>
    <%--<div >长期调课<em>×</em></div>--%>
    <ul style="padding: 0">
        <li style=" display: none;" class="tktime">
            <span>调整时间</span>
            <select name="" id="week2" style="width: 180px;">
            </select>
            <em style="display: inline-block;width:75px;">至</em>
            <select name="" id="week3" style="width: 180px;">
            </select>
        </li>
        <%--<li class="thwerk">--%>
            <%--<div style="text-align: left;margin: 0px 0px 15px 39px;">选择需代课课节</div>--%>
            <%--<table class="tab3" style="margin: 0 0 0 110px">--%>
                <%--<thead>--%>
                    <%--<tr>--%>
                        <%--<th></th>--%>
                        <%--<th>一</th>--%>
                        <%--<th>二</th>--%>
                        <%--<th>三</th>--%>
                        <%--<th>四</th>--%>
                        <%--<th>五</th>--%>
                        <%--<th>六</th>--%>
                        <%--<th>七</th>--%>
                    <%--</tr>--%>
                <%--</thead>--%>
                <%--<tbody>--%>
                    <%--<tr>--%>
                        <%--<td>1</td>--%>
                        <%--<td>--%>
                            <%--<span class="active">英</span>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--<span>1</span>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--<span></span>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--<span></span>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--<span></span>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--<span></span>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--<span></span>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                <%--</tbody>--%>
            <%--</table>--%>
        <%--</li>--%>
        <li>
            <%--<label style="display: none;" class="xkcls">--%>
                <%--<span>学科</span>--%>
                <%--<select style="width: 180px;">--%>
                    <%--<option>英语</option>--%>
                <%--</select>--%>
            <%--</label>--%>
            <label style=" display: none;" class="dkcls">
                <span>代课老师</span>
                <select name="" id="dkTeacherId" style="width: 180px;">
                </select>
                <script type="text/template" id="teacher_temp">
                    {{~it:value:index}}
                    <option value="{{=value.id}}">{{=value.name}}</option>
                    {{~}}
                </script>
            </label>
        </li>
    </ul>
    <div class="dbtn">
        <button class="btn-ok">确定</button>
        <button class="btn-no">取消</button>
    </div>
</div>
<div class="bg"></div>
<!--==增加课节==-->
<div class="popup dbtk-popup">
    <div class="popup-top">
        <em class="emSty">增加课节：
            <%--<label>高一（7）班</label><label>第14周</label><label>周一第1节</label>--%>
        </em>
        <i class="fr close"></i>
    </div>
    <div style="margin: 40px 50px;">
        <span>选择教学班</span>
        <select class="sel20">

        </select>
        <script type="text/template" id="sel20_temp">
            {{~it:value:index}}
            <option value="{{=value.jxbId}}">{{=value.jxbName}} {{=value.teacherName}}</option>
            {{~}}
        </script>
    </div>
    <div class="popup-btn mt10">
        <button class="ss qxx" id="queDing">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>
</body>
</html>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('zjtkClass', function (zjtkClass) {
        zjtkClass.init();
    });
</script>