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
    <title>总课表</title>
    <meta charset="utf-8">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/table2.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/new33/0.1.0/paikeAll.js"></script>
    <style type="text/css">
        *{
            -moz-user-select: none;
            -webkit-user-select: none;
        }
        .ul-main li{
            border-bottom: 1px dashed #d1d1d1;
            line-height: 40px;
            /*height: 40px;*/
            margin: 0;
            width: 1193px;
        }

        .ul-main > li > label{
            border-radius: 3px;
            height: 26px;
            margin: 7px 30px 0 0;
            line-height: 26px;
        }
        /*.ul-main > li >div{*/
            /*width: 1080px;*/
            /*float: left;*/
        /*}*/
        .ul-main > li > label.active{
            color: #388CE9;
            font-weight: bold;
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
            width: 100px;
            height: 60px;
            line-height: 60px;
            border-bottom: 1px solid #dcdcdc;
            border-right: 1px solid #dcdcdc;
        }
        th>div>div{
            min-width: 100px;
            height: 39px;
            float: left;
            line-height: 40px;
        }
        th{
            min-width: 100px;
            height: 40px;
        }
        .wd80{
            width: 80px;
        }
        .lth{
            float: right;
        }
        .wd120{
            width: 120px !important;
            font-weight: bold;
        }
        .psre div{
            float: left;
            width: 120px;
            height: 49px;
            line-height: 20px;
            padding-top: 11px;
            border-bottom: 1px solid #dcdcdc;
        }
        .ul-main{
            float: left;
            background: url('/static_new/images/new33/bg-line.png') repeat-y;
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
        div.actv{
            border: 1px dashed red;
        }
        div.active:hover{
            background: #AFEEEE;
        }
        .tag{
            margin-top: 10px;
            margin-bottom: 5px;
        }
        .tag label.one{
            margin-left: 8px;
        }
        .tag label.four{
            margin-right: 30px;
        }
        .tag label input{
            position: relative;
            top: 1px;
            left: -5px;
        }
        .tag label{
            margin-right: 15px;
            cursor: pointer;
            margin-top: 10px;
            display: inline-block;
        }
        .tab2 p{
            line-height: 20px;
            font-size: 12px;
        }
        .tab2 p em{
            font-size: 12px;
            display: inline-block;
        }
        .tab2 span{
            display: none;
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
        .fixedcont{
            width: 220px;
            position: fixed;
            top: 320px;
            right: 0;
            /* min-height: 190px;*/
            z-index: 100;
            background: #fff;
            overflow: hidden;
        }
        .vtt10{
            vertical-align: top;
            margin-bottom: 10px;
        }
        .fixedcont .ul1{
            z-index: 1;
            width: 160px;
            height: 490px;
            overflow-x: hidden;
            overflow-y: auto;
            cursor: default;
            float: right;
        }
        .fixedcont .ul1 li.over{
            background: #e5e5e5 !important;
            border: 1px solid #ccc !important;
            color: #666;
        }
        .fixedcont .ul1 li{
            min-height: 76px;
            width: 134px;
            margin: 0 10px 10px 0;
            font-size: 12px;
            background: #fff;
            border: 1px solid #cfa972;
            color: #f77737;
        }
        .fixedcont .ul2{
            z-index: 1;
            width: 160px;
            height: 490px;
            overflow-x: hidden;
            overflow-y: auto;
            cursor: default;
            float: right;
        }
        .fixedcont .ul2 li{
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
        .fixedcont .ul2 li.active{
            width: 134px;
            background: #f77727;
            border: 1px solid #aaa;
            color: #fff;
        }
        .fixedcont .ul1 li span{
            line-height: 76px;
            text-align: center;
            float: right;
            width: 39px;
            color: #666;
        }
        .fixedcont .ul1 li:last-child{
            margin-bottom: 10px;
        }
        .fixedcont .ul1 li>div{
            line-height: 40px;
            text-indent: 15px;
            cursor: pointer;
            min-height: 66px;
            padding-top: 10px;
            border-right: 1px dashed #999;
            width: 93px;
            display: inline-block;
        }
        .fixedcont .ul1 li>div p{
            /*height: 20px;*/
            line-height: 20px;
            font-size: 12px;
        }
        .fixedcont .ul1 li>div p em{
            font-size: 12px;
        }
        .fixedcont .info .p1{
            font-size: 16px;
            height: 30px;
            line-height: 30px;
        }
        .fixedcont .info .p1 em{
            float: right;
            font-size: 32px;
            cursor: pointer;
            display: none;
        }
        .fixedcont .info .p2{
            font-size: 12px;
            height: 26px;
            line-height: 26px;
            display: none;
            margin-top: 5px;
        }
        .fixedcont .info .p2 select{
            width: 195px;
            height: 36px;
            border-radius: 3px;
        }
        .fwb{
            font-weight: bold;
        }
        .fixedcont .info .p3{
            font-size: 14px;
            height: 30px;
            cursor: pointer;
            text-align: center;
            line-height: 30px;
        }
        .fixedcont .info{
            background: #5851db;
            color: #fff;
            padding: 5px 12px 25px 12px;
        }
        .fixedcont .cont{
            border: 1px solid #f77737;
            border-top: none;
            /* padding-top: 10px;*/
            transition: all .3s;
            overflow: hidden;
        }
        .fixedcont .cont .lt{
            float: left;
            width: 46px;

        }
        .fixedcont .cont .lt span.active{
            background: #f77737 !important;
            color: #fff !important;
        }
        .fixedcont .cont .lt span{
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
        #movebg{
            height: 40px;
            background: #fff;
            border: 1px dashed #999;
            line-height: 40px;
            text-align: center;
            cursor: move;
            transition: all .3s;
            overflow: hidden;
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
        /*.tab2 div.red{
            background: #FFB6C1;
            border: 1px dashed red;
            color: #333;
            height: 59px;
            width: 99px;
        }*/

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


        .tab2 div.gray{
            background: #AAAAAA;
            color: #333;
            height: 60px;
            width: 99px;
        }

        .h0{
            height: 0px !important;
            border: none !important;
        }
    </style>
    <script type="text/javascript">
        $(function(){

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

            function init(){

            }


        })
    </script>

    <script type="text/javascript">

        window.onload=function() {

            var dd = document.getElementById("movebg"); // 获取DIV对象
            dd.onmousedown=function(e) {
                var event1 = e || window.event;                  // IE、火狐获取事件对象
                var eventX = event1.offsetX || event1.layerX;         // 获取鼠标相对于事件源的X轴
                var eventY = event1.offsetY || event1.layerY;        // 获取鼠标相对于事件源的Y轴
                var flag = true;
                this.onmousemove=function(e) {
                    if (flag) {
                        var event2 = e || window.event;
                        var eveX = event2.clientX;             // 获取鼠标相对于浏览器x轴的位置
                        var eveY = event2.clientY;             // 获取鼠标相对于浏览器Y轴的位置
                        $('#move').css('top',eveY - eventY);
                        $('#move').css('left',eveX - eventX);
                    }
                }
                this.onmouseup=function() {
                    if (flag) {
                        flag = false;
                    }
                }
            }
        }
    </script>
    <script type="text/javascript">

    </script>
</head>
<body>
<input id="subjectId" hidden>
<jsp:include page="head.jsp"></jsp:include>
<div class="right-pos">
    <a href="/newisolatepage/pkTable.do" class="ve1">排课</a>
    <a href="/newisolatepage/zixiclass.do" class="zhp1">排自习课</a>
    <a href="/newisolatepage/pkTableAll.do" class="pka2">总课表</a>
    <a href="/newisolatepage/teaCLassTime.do" class="pkc1">教师课表</a>
    <a href="/newisolatepage/classTable.do" class="pkb1">行政班课表</a>
</div>
<%--<div class="fixedcont" id="move" style="width: 110px;">--%>
    <%--<div id="movebg" class="h0">点击可移动</div>--%>
    <%--<div class="info">--%>
        <%--<p class="p1">待排教学班<em>×</em></p>--%>
        <%--<p class="p2">--%>
            <%--<select id="jxbType">--%>
                <%--<option value="0">选择教学班类型</option>--%>
                <%--<option value="1">等级</option>--%>
                <%--<option value="2">合格</option>--%>
                <%--<option value="3">行政</option>--%>
                <%--<option value="4">专项</option>--%>
            <%--</select>--%>
        <%--</p>--%>
        <%--<p class="p3">展开 > </p>--%>
    <%--</div>--%>
    <%--<div class="cont clearfix h0">--%>
        <%--<div class="lt">--%>
            <%--<span class="active">学<br>科</span>--%>
            <%--&lt;%&ndash;<span>标<br>签</span>&ndash;%&gt;--%>
        <%--</div>--%>
        <%--<ul class="ul2 subject">--%>

        <%--</ul>--%>
        <%--<script type="text/template" id="subjectTrScript">--%>
            <%--{{~it:value:index}}--%>
                <%--<li subjectName="{{=value.snm}}" subjectId="{{=value.subid}}" ids="{{=value.id}}">{{=value.snm}}</li>--%>
            <%--{{~}}--%>
        <%--</script>--%>
        <%--<ul class="ul1 kpjxblist" style="display: none;">--%>

        <%--</ul>--%>
        <%--<script type="text/template" id="kpjxblistTemp">--%>
            <%--{{~it:value:index}}--%>
                <%--{{?value.ypksCount!=value.zksCount}}--%>
            <%--<li  draggable="true" ondrop="drop(event,this)" ondragover="allowDrop(event)" ondragstart="drag(event, this)" clsrmId="{{=value.classroomId}}" jxbType="{{=value.type}}" jxbId="{{=value.id}}" ypksCnt="{{=value.ypksCount}}" zksCnt="{{=value.zksCount}}"><div class="ls"><p><em class="vxk">{{=value.name}}</em><em class="vrs">({{=value.studentCount}})</em></p><p class="vjs" js='{{=value.teacherId}}'>{{=value.teacherName}}</p><p class="vbq">{{=value.remarks}}</p></div><span>{{=value.ypksCount}}/{{=value.zksCount}}</span></li>--%>
                <%--{{?}}--%>
            <%--{{~}}--%>
        <%--</script>--%>
    <%--</div>--%>
<%--</div>--%>
<div class="container clearfix">
    <span id = "defaultTerm" style="display: none"></span>
    <div class="clearfix">
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
                <label ids="{{=index+1}}" js="ke{{=index+1}}">{{=value.name}}</label>
            {{~}}
        </script>
        <script type="text/template" id="jiaos">
                <label class='active all' js='all' ids="">全部</label>
                {{~it:value:index}}
                <label ids="{{=value.roomId}}" js="{{=value.roomId}}">{{=value.roomName}}</label>
                {{~}}
        </script>

        <li class="jiaoshi">
        </li>
        <li class="zhou">
        </li>
        <li class="ke kj">
            <%--<div class="inline"></div>--%>
        </li>
    </ul>
    </div>
    <div class="tag">
        <%--<button class="b1">编辑</button>--%>
        <%--<button class="b2">撤销</button>--%>
        <%--<button class="b3">清空</button>--%>
        <%--<button class="b4">加载</button>--%>
        <%--<button class="b5">保存</button>--%>
        <%--<button class="b6">另存为</button>--%>
        <%--<button class="b7" hidden>发布</button>--%>
        <%--<button class="b8" hidden>取消发布</button>--%>
        <label class="one" style="display: none;">
            <input type="checkbox" js='bq' class="one" checked=checked>标签
        </label>
        <label class="ml8">
            <input type="checkbox" js='js' class="one" checked=checked>教师
        </label>
        <label>
            <input type="checkbox" js='rs' class="two" checked=checked>人数
        </label>
        <label class="four">
            <input type="checkbox" js='xk' class="three" checked=checked>学科
        </label>
        <%--<button class="b8">自动排课</button>--%>
        <%--<button class="b9 fr vtt10">导出</button>--%>

    </div>
    <div>
        <div class="fr">
            <table class="tab1">
                <thead>
                <tr>
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
                        <div class="ke{{=value.serial}}">{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></div>
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
                        <th class="{{=value.roomId}}" crid="{{=value.id}}">{{=value.roomName}}</th>
                        {{~}}
                    </script>
                    <tr class="weeks">

                    </tr>
                    <script type="text/template" id="weeks_temp">
                    {{~it:value:index}}
                    <th class="{{=value.roomId}}">

                    <div class="zhou {{=value.id}}" style="width: 500px;">
                        {{~value.weekList:value2:index2}}
                        <div class="zh{{=value2.type}}">{{=value2.name}}</div>
                        {{~}}
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
                                        {{?value.swType==0}}
                                        <div class="ke{{=value.y+1}} idx{{=value.x}}{{=value.y}} citt" ykbid="{{=value.id}}" jxbid="{{=value.jxbId}}" ondrop="drop(event,this)" ondragover="allowDrop(event)" draggable="false" ondragstart="drag(event, this)">
                                            {{?value.jxbId!=''}}
                                                {{?value.njxbName!=''&& value.njxbName!=null}}
                                                <div class="ls"><p class="whide" title="{{=value.jxbName}}({{=value.studentCount}})"><em class="vxk">{{=value.jxbName}}</em><em class="vrs">({{=value.studentCount}})</em></p><p><em class="vxk">{{=value.njxbName}}</em><em class="vrs">({{=value.nstudentCount}})</em></p></div>
                                                {{??}}
                                                <div class="ls"><p class="whide" title="{{=value.jxbName}}({{=value.studentCount}})"><em class="vxk">{{=value.jxbName}}</em><em class="vrs">({{=value.studentCount}})</em></p><p class="vjs" js='{{=value.teacherId}}'>{{=value.teacherName}}</p><p class="vbq">{{=value.remarks}}</p></div>
                                                {{?}}
                                            {{?}}
                                        </div>
                                        {{?}}
                                        {{?value.swType==1}}
                                            <div class="ke{{=value.y+1}} red" ykbid="{{=value.id}}"><div>{{=value.remarks}}</div></div>
                                        {{?}}
                                        {{?value.swType==2}}
                                        <div class="ke{{=value.y+1}} gray1" ykbid="{{=value.id}}"><div>{{=value.remarks}}</div></div>
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
</div>
<!--发布-->
<div class="bg"></div>
<div class="popup bf-popup">
    <div class="popup-top">
        <em>发布</em>
        <i class="fr close"></i>
    </div>
    <dl class="g-dl">
        <dd class="mt25">
            <span class="mr15">开始周</span>
            <select class="sel2 kaishi">
            </select>
            <script type="text/template" id="week_temp">
                {{~it:value:index}}
                <option value="{{=value.serial}}">{{=value.numberWeek}}</option>
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
</body>
</html>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('pkTableAll', function (pkTableAll) {
        pkTableAll.init();
    });
</script>
