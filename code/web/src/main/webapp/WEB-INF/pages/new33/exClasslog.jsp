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
    <title>审核&记录</title>
    <meta charset="utf-8">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/table2.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/echarts/0.1.0/echarts.js"></script>

    <%--<script type="text/javascript" src="/static_new/js/modules/new33/0.1.0/paike.js"></script>--%>
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
            width: 1199px;
        }
        .ul-main > li > label{
            border-radius: 3px;
            height: 26px;
            margin: 7px 30px 0 0;
            line-height: 26px;
        }
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
        div.act{
            background: #AFEEEE;
        }
        div.active:hover{
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
            height: 572px;
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
            <a class="dbtk1" href="/newisolatepage/dbtkClass.do" style="display: block">多步调课</a>
            <a class="zjtk1" href="/newisolatepage/zjtkClass.do"style="display: block">增减调课</a>
            <a class="dk1" href="/newisolatepage/exchangeClass.do" style="display: block">代课</a>
            <a href="/newisolatepage/exClasslog.do" class="sh2">审核&记录</a>
        </div>
    </div>
</div>
<jsp:include page="head.jsp"></jsp:include>
<span id = "defaultTerm" style="display: none"></span>
<div class="container clearfix">
    <div class="mm1">
        <div class="clearfix tile">
             <ul class="ul-main">
            <li class="gaozhong">
            </li>
            <script type="text/template" id="grade_temp">
                {{~it:value:index}}
                <label ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</label>
                {{~}}
            </script>
        </ul>
        </div>
        <div class="center w1300 clearfix">
            <div class="mt20 cur-tab cou-tab center fl">
                <div class="clearfix">
                    <div class="inline pos">
                        <input class="inp10" type="text" placeholder="请输入姓名" id="userName">
                        <i class="search at"></i>
                    </div>
                    <div class="fr">
                        <button class="b8 tkbg dkreport">调课报告</button>
                        <button class="b9 ml10 export">导出</button>
                    </div>
                </div>
                <div class='center w1200 clearfix' id="none_png" hidden>
                    <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
                </div>
                <table style="width: 1200px;margin-top: 10px;" class="votab clstab" id="content" hidden>
                    <thead>
                        <tr>
                            <th>调/代课申请人</th>
                            <th>课节</th>
                            <th>调/代课交换教师</th>
                            <th>交换课节</th>
                            <th>处理日期</th>
                            <th>类型</th>
                            <th>作用期</th>
                            <th>作用周</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody class="tkLog">

                    </tbody>
                    <script type="text/template" id="tkLog_temp">
                        {{~it:value:index}}
                        <tr>
                            <td>
                                {{?value.teacherName != null && value.teacherName != ""}}
                                    {{=value.teacherName}}
                                {{??}}
                                    -
                                {{?}}
                            </td>

                            <td>
                                {{?value.jxbName != null}}
                                    {{=value.jxbName}}</br>{{=value.keJie}}
                                {{??}}
                                -
                                {{?}}
                            </td>
                            <td>
                                {{?value.newTeacherName != null && value.newTeacherName != ""}}
                                {{=value.newTeacherName}}
                                {{??}}
                                -
                                {{?}}
                            </td>
                            <td>
                                {{?value.newJxbName != null}}
                                {{=value.newJxbName}}</br>{{=value.newKeJie}}
                                {{??}}
                                -
                                {{?}}
                            <td>{{=value.time}}</td>
                            <td>{{?value.type==0}}调课{{??value.type==1}}代课{{??value.type==2}}增课{{??value.type==3}}减课{{?}}</td>
                            <td>{{?value.xcty==0}}短期{{??}}长期{{?}}</td>
                            <td>{{=value.week}}</td>
                            <td {{?value.cimType==2}} style="color: red;" {{??value.cimType==1}} style="color: green;" {{?}}>
                                {{?value.cimType==0}}
                                <em class="c6f agree" lid="{{=value.id}}">同意</em>
                                <em class="c6f refuse" lid="{{=value.id}}">拒绝</em>
                                {{??value.cimType==2}}
                                已拒绝
                                {{??}}
                                已通过
                                {{?}}
                            </td>
                        </tr>
                        {{~}}
                    </script>
                </table>
                <div class="page-list pageDiv">

                </div>

            </div>
        </div>
    </div>
    <div class="mm2">
        <div>
            <a class="prevK">&lt;返回</a>
        </div>
        <p class="tx mt15">调课数量/代课数量</p>
        <div id="D1" style="width: 1200px;height:400px;"></div>
        <p class="tx">短期数量/长期数量</p>
        <div id="D2" style="width: 1200px;height:400px;"></div>
        <div>
            <ul class="qUl">
                <li id="qb" class="active">全部</li>
                <li id="cq">长期</li>
                <li id="dq">短期</li>
            </ul>
            <div id="tab-qb">
                <p class="tx">学科调课总数量</p>
                <div id="D3" style="width: 1200px;height:400px;"></div>
            </div>
            <div id="tab-cq">
                <p class="tx">学生调课总数量</p>
                <div id="D4" style="width: 1200px;height:400px;"></div>
            </div>
            <div id="tab-dq">
                <p class="tx">学生调课总数量</p>
                <div id="D5" style="width: 1200px;height:400px;"></div>
            </div>
        </div>
        <p class="tx">调课比例</p>
        <div id="D6" style="width: 1200px;height:400px;"></div>
        <p class="tx">调课数量</p>
        <div id="D7" style="width: 1200px;height:400px;"></div>
    </div>
</div>
<div class="bg"></div>
</body>
</html>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<script>
    seajs.use('excCourseLog', function (excCourseLog) {
        excCourseLog.init();
    });
</script>