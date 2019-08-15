<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/5/18
  Time: 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>排自习课</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/js/fselect/fselect.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/table2.css">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <%--<script type="text/javascript" src="/static/js/fselect/fselect.js"></script>--%>
    <style type="text/css">
        *{
            -moz-user-select: none;
            -webkit-user-select: none;
        }
        .ul-main li{
            border-bottom: 1px dashed #d1d1d1;
            line-height: 40px;
            height: 40px;
            margin: 0;
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
        .tabTh td:first-child{
            background:white!important;
            font-weight: bold;
        }
        .tab1 td:first-child{
            background:#3CB371;
            font-weight: bold;
            color: #fff;
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
        th{
            /*min-width: 100px;*/
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
            width: 120px;
            height: 49px;
            line-height: 20px;
            padding-top: 11px;
        }
        .ul-main{
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
        div.active:hover{
            background: #AFEEEE;
        }
        .tag{
            margin-top: 40px;
            margin-bottom: 20px;
        }
        .tag label.one{
            margin-left: 146px;
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
        }
        .tab2 p{
            line-height: 20px;
            font-size: 12px;
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
            top: 495px;
            right: 0;
            /* min-height: 190px;*/
            z-index: 100;
            background: #fff;
            overflow: hidden;
        }
        .fixedcont .ul1{
            z-index: 1;
            width: 160px;
            height: 358px;
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
            color: #f77737
        }
        .fixedcont .ul2{
            z-index: 1;
            width: 160px;
            height: 358px;
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
            float: left;
            width: 39px;
            color: #666;
        }
        .fixedcont .ul1 li:last-child{
            margin-bottom: 10px;
        }
        .fixedcont .ul1 li>div{
            float: left;
            line-height: 40px;
            text-indent: 15px;
            cursor: pointer;
            height: 66px;
            padding-top: 10px;
            border-right: 1px dashed #999;
            width: 93px;
        }
        .fixedcont .ul1 li>div p{
            height: 20px;
            line-height: 20px;
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
            padding: 10px 12px;
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
            position: relative;
        }
        .tab1 em img{
            position: absolute;
            left: 0px;
            top: 0px;
        }
        .tab2 div.green{
            background:  #90EE90;
            border: 1px dashed #2E8B57;
            color: #333;
        }
        .tab2 div.red{
            background: #FFB6C1;
            border: 1px dashed red;
            color: #333;
            height: 59px;
            width: 99px;
        }
        .h0{
            height: 0px !important;
            border: none !important;
        }
        .mtt10{
            margin-top: -10px;
        }
        .mtt50{
            margin-top: -50px;
        }
        .mtt30{
            margin-top: -30px;
        }
        .mtt20{
            margin-top: -20px;
        }
        .w600{
            width: 600px;
        }
        .mt40{
            margin-top: 40px;
        }
        .mt100{
            margin-top: 100px;
        }
        .w570{
            width: 570px;
        }
        .mt61{
            margin-top: 61px;
        }
        .xz-popup dl dd span,.cj-popup dl dd span{
            min-width: 5em;
            margin-right: 0px;
        }
        .prev,.prevv{
            cursor: pointer;
        }
        .pob{
            position: absolute;
        }
        .w588{
            width: 588px;
        }
        .w456{
            width: 456px;
        }
        .w482{
            width: 482px;
        }
        .w600{
            width: 600px;
        }
        .w617{
            width: 617px;
        }
        .w670{
            width: 670px;
        }
        .w1202{
            width: 1202px;
        }
        .w1185{
            width: 1185px;
        }
        .inline{
            display: inline-block;
        }
        .w199{
            width: 199px;
        }
        .sell1{
            width: 145px;
            height: 28px;
            border: 1px solid #9a9a9a;
            position: absolute;
            margin-top: -14px;
            margin-left: -70px;
        }
        .zhb2{
            display: none;
        }

        .select-group{
            width: 100%;
            margin-top: 20px;
        }
        .select-group .select-item{
            display: inline-block;
            margin-right: 15px;
        }
        .select-group .select-item .sp{
            float: left;
            height: 30px;
            line-height: 30px;
        }
        /*.select-group .select-item .ms{*/
            /*width: 200px;*/
            /*height: 34px;*/
        /*}*/
    </style>
    <script type="text/javascript">
        $(function(){
            $('.set-ul li').click(function(){
                $(this).addClass('ss-cur').siblings().removeClass('ss-cur');
                var tt = $(this).attr('id');
                $("#" + "tab-" + tt).show().siblings().hide();
            })




        })
    </script>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<div class="right-pos">
    <a href="/newisolatepage/pkTable.do" class="ve1">排课</a>
    <a href="/newisolatepage/zixiclass.do" class="zhp2">排自习课</a>
    <a href="/newisolatepage/pkTableAll.do" class="pka1">总课表</a>
    <a href="/newisolatepage/teaCLassTime.do" class="pkc1">教师课表</a>
    <a href="/newisolatepage/classTable.do" class="pkb1">行政班课表</a>
</div>
<div class="container clearfix">
    <div class="main1">
        <ul class="ul-main">
            <li class="xueke">
            </li>
            <script type="text/template" id="grade_temp">
                {{~it:value:index}}
                <label ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</label>
                {{~}}
            </script>
        </ul>
        <div class="container clearfix">
            <div class="fl ml10">
                <div class="clearfix">
                    <div class="w662 clearfix">
                        <div class="f16 c-b cc-in fl">
                            <em class="f16" id="gradenum"></em>
                        </div>
                        <div class="fr mtt10">
                            <button class="class-bt btn fr ml10 f15" id="autoArranage">自动排自习课</button>
                            <button class="tea-bt btn fr ml10 f15" id="exportZiXiBan">导出名单</button>
                            <button class="class-bt btn fr ml10 f15" id="ckzxk">查看自习课</button>
                            <button class="class-bt btn fr ml10 f15 zd-btn" id="findthis">本节自习课</button>
                            <button class="chat-bt btn fr ml10 f15" hidden>排自习课</button>
                        </div>
                    </div>

                    <div class="inline fl w662 pos">
                        <table class="tab1 mt10 inline">
                            <thead>
                            <tr>
                                <th>
                                    <div class="w112">课节 / 日</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody class="t-tbo zz-tm" id="vtitle">

                            </tbody>
                            <script type="text/template" id="vtitle_temp">
                                {{~it:value:index}}
                                <tr>
                                    <td class="psre ke">
                                        <div>{{=value.text}}<br><em>{{=value.time}}</em></div>
                                    </td>
                                </tr>
                                {{~}}
                            </script>
                        </table>
                        <div class="t41 w568 t1">
                            <table class="mt10 inline tabTh">
                                <thead>
                                <tr id="htitle">

                                </tr>
                                <script type="text/template" id="htitle_temp">
                                    {{~it:value:index}}
                                    <th>
                                        <div class="w112">{{=value.text}}</div>
                                    </th>
                                    {{~}}
                                </script>
                                </thead>

                                <tbody class="t-tbo zz-tm h68" id="content">

                                </tbody>
                                <script type="text/template" id="content_temp">
                                    {{~it:value:index}}
                                    <tr>
                                        {{~value:value2:index2}}
                                        {{? value2.zixi==1}}
                                        <td>
                                            <em class="inline zu-cur zbz w100" >全体自习</em>
                                        </td>
                                        {{??}}
                                        <td>
                                            {{? value2.ztime==1}}
                                            <em class="inline zu-cur zbz w100 poss" y="{{=index}}" x="{{=index2}}">
                                                {{=value2.inzixi}}/{{=value2.notin}}
                                                <p class="hzb"></p>
                                            </em>
                                            {{??}}
                                            <em class="inline zu-cur zbz w100 poss" y="{{=index}}" x="{{=index2}}">
                                                {{=value2.inzixi}}/{{=value2.notin}}
                                            </em>
                                            {{?}}
                                        </td>
                                        {{?}}

                                        {{~}}
                                    </tr>
                                    {{~}}
                                </script>
                            </table>
                        </div>
                    </div>
                    <div class="fLi mtt30 ml30 clearfix fl w465">
                        <div class="clearfix">
                            <div class="f16 c-b cc-in fl">
                                <em class="f16" id="kejie"></em>
                            </div>
                            <div class="fr mtt10">
                                <button class="chat-bt btn fr ml10 f15 pk">排本节自习课</button>
                            </div>
                        </div>
                        <div class=' w1200 clearfix' id="none_png" hidden>
                            <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
                        </div>
                        <div class="mt14 pos" hidden id="all_content">
                            <table class="pob">
                                <thead>
                                <tr>
                                    <th width="155">姓名</th>
                                    <th width="115">层级</th>
                                    <th width="115">班級</th>
                                    <th width="115">选科组合</th>
                                </tr>
                                </thead>
                            </table>
                            <div class="fTab h515 w482">
                                <div class="w456">
                                    <table>
                                        <thead>
                                        <tr>
                                            <th width="155" style="height: 32px!important;">姓名</th>
                                            <th width="115" style="height: 32px!important;">层级</th>
                                            <th width="115" style="height: 32px!important;">班級</th>
                                            <th width="115" style="height: 32px!important;">选科组合</th>
                                        </tr>
                                        </thead>
                                        <tbody id="students">

                                        </tbody>
                                        <script type="text/template" id="students_temp">
                                            {{~it:value:index}}
                                            <tr>
                                                <td>
                                                    <em class="inline w115">{{=value.name}}</em>
                                                </td>
                                                <td>
                                                    <em class="inline w115">{{=value.lev}}</em>
                                                </td>
                                                <td>
                                                    <em class="inline w115">{{=value.clsName}}</em>
                                                </td>
                                                <td>
                                                    <em class="inline w115">{{=value.conn}}</em>
                                                </td>
                                            </tr>
                                            {{~}}
                                        </script>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="main2">
        <div>
            <a class="prev">&lt;返回&nbsp;</a>
        </div>
        <ul class="set-ul c-b clearfix mt20">
            <li class="f16 ss-cur" id="cy">成员管理</li>
            <li class="f16" id="zxb">自习班管理</li>
        </ul>
        <div class="clearfix tab">
            <div id="tab-cy" class="lNext w1185 mt20 fLi">
                <div classs="clearfix mt20">
                    <span class="f16" id="kejie2"></span>
                    <%--<select class="kclass" id="klass"></select>--%>
                    <%--<select class="kclass" id="zuhe"></select>--%>
                    <%--<script type="text/template" id="select_set">--%>
                        <%--{{~it:value:index}}--%>
                        <%--<option value="{{=value.name}}">{{=value.name}}</option>--%>
                        <%--{{~}}--%>
                    <%--</script>--%>
                    <button class="class-bt btn fr ml10 f16 tjz mtt10">添加至&gt;&gt;</button>
                </div>

                <div class="select-group">
                    <div class="select-item">
                        <span class="sp">层级：</span>
                        <select class="ms" id="ms1" multiple>
                            </select>
                    </div>
                    <div class="select-item">
                        <span  class="sp">班级：</span>
                        <select class="ms" id="ms2" multiple>

                        </select>
                    </div>
                    <div class="select-item">
                        <span  class="sp">选科组合：</span>
                        <select class="ms" id="ms3" multiple>

                        </select>
                    </div>
                </div>


                <div class="mt20 pos">
                    <table class="pob">
                        <thead>
                        <tr>
                            <th width="240">姓名</th>
                            <th width="240">层级</th>
                            <th width="240">班级</th>
                            <th width="240">选科组合</th>
                            <th width="240">自习班</th>
                            <th width="240">全选&nbsp<input type="checkbox" id="all_select"></th>
                        </tr>
                        </thead>
                    </table>
                    <div class="h515 w1202">
                        <div class="w1185">
                            <table>
                                <thead>
                                <tr>
                                    <th width="240">姓名</th>
                                    <th width="240">层级</th>
                                    <th width="240">班级</th>
                                    <th width="240">选科组合</th>
                                    <th width="240">自习班</th>
                                    <th width="240">选择</th>
                                </tr>
                                </thead>
                                <tbody id="students2">

                                </tbody>
                                <script type="text/template" id="students_temp2">
                                    {{~it:value:index}}
                                    <tr>
                                        <td>
                                            <em class="inline">{{=value.name}}</em>
                                        </td>
                                        <td>
                                            <em class="inline">{{=value.lev}}</em>
                                        </td>
                                        <td>
                                            <em class="inline">{{=value.clsName}}</em>
                                        </td>
                                        <td>
                                            <em class="inline">{{=value.conn}}</em>
                                        </td>
                                        <td>
                                            <em class="inline">{{=value.zixiname}}</em>
                                        </td>
                                        <td>
                                            <em class="inline">
                                                <input type="checkbox" uid="{{=value.id}}">
                                            </em>
                                        </td>
                                    </tr>
                                    {{~}}
                                </script>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tab-zxb" class="lNext w1185 fLi">
                <div class="clearfix mt40">
                    <div class="fr">
                        <button class="auto-bt btn ml10 f16 cj">创建自习班</button>
                        <button class="chat-bt btn ml10 f16" hidden>自动</button>

                    </div>
                </div>
                <div class="mt20 pos">
                    <div class="w1185">
                        <table class="pob">
                            <thead>
                            <tr>
                                <th width="199">自习班名</th>
                                <th width="200">时间</th>
                                <th width="200">学生人数</th>
                                <th width="200">教室</th>
                                <th width="200">教师</th>
                                <th width="200">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="h515 w1202">
                        <div class="w1185">
                            <table>
                                <thead>
                                <tr>
                                    <th width="200">自习班名</th>
                                    <th width="200">时间</th>
                                    <th width="200">学生人数</th>
                                    <th width="200">教室</th>
                                    <th width="200">教师</th>
                                    <th width="200">操作</th>
                                </tr>
                                </thead>
                                <tbody id="zixiban">

                                </tbody>
                                <script type="text/template" id="zixiban_temp">
                                    {{~it:value:index}}
                                    <tr>
                                        <td>
                                            <em class="inline">{{=value.name}}</em>
                                        </td>
                                        <td>
                                            <em class="inline">{{=value.time}}</em>
                                        </td>
                                        <td>
                                            <em class="inline">{{=value.num}}</em>
                                        </td>
                                        <td>
                                                <span class="inline">
                                                    <em class="zhb1">{{=value.roomname}}</em>
                                                    <select class="sell1 zhb2 classroom">

                                                    </select>
                                                </span>
                                        </td>
                                        <td>
                                                <span class="inline">
                                                    <em class="zhb1">{{=value.teaname}}</em>
                                                    <select class="sell1 zhb2 teacher">

                                                    </select>
                                                </span>
                                        </td>
                                        <td>
                                            <em class="inline ccur del" cid="{{=value.id}}">删除</em>
                                            <em class="inline ccur tab-edit" cid="{{=value.id}}"
                                                teaid="{{=value.teaid}}" roomid="{{=value.roomid}}"
                                                roomname="{{=value.roomname}}" teaname="{{=value.teaname}}"
                                                x="{{=value.x}}" y="{{=value.y}}">编辑</em>
                                        </td>
                                    </tr>
                                    {{~}}
                                </script>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="main3">
        <div>
            <a class="prev">&lt;返回&nbsp;</a>
        </div>
        <div class="clearfix">
            <div class="lNext w1185 fLi">
                <div class="mt20 pos">
                    <div class="w1185">
                        <table class="pob">
                            <thead>
                            <tr>
                                <th width="199">自习班名</th>
                                <th width="200">时间</th>
                                <th width="200">学生人数</th>
                                <th width="200">教室</th>
                                <th width="200">教师</th>
                                <th width="200">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="h515 w1202">
                        <div class="w1185">
                            <table>
                                <thead>
                                <tr>
                                    <th width="200">自习班名</th>
                                    <th width="200">时间</th>
                                    <th width="200">学生人数</th>
                                    <th width="200">教室</th>
                                    <th width="200">教师</th>
                                    <th width="200">操作</th>
                                </tr>
                                </thead>
                                <tbody id="zixiban2">

                                </tbody>

                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="main4">
        <div>
            <a class="prev4">&lt;返回&nbsp;</a>
        </div>
        <div class="h515 w1202 fLi" style="margin-top: 20px;">
            <div class="w1185">
                <table>
                    <thead>
                    <tr>
                        <th width="200">自习班名</th>
                        <th width="200">时间</th>
                        <th width="200">学生人数</th>
                        <th width="200">教室</th>
                        <th width="200">教师</th>
                    </tr>
                    </thead>

                    <tbody id="zixiban3">

                    </tbody>
                    <script type="text/template" id="zixiban_temp3">
                        {{~it:value:index}}
                        <tr>
                            <td>
                                <em class="inline">{{=value.name}}</em>
                            </td>
                            <td>
                                <em class="inline">{{=value.time}}</em>
                            </td>
                            <td>
                                <em class="inline">{{=value.num}}</em>
                            </td>
                            <td>
                                <em class="inline">{{=value.roomname}}</em>
                            </td>
                            <td>
                                <em class="inline">{{=value.teaname}}</em>
                            </td>
                        </tr>
                        {{~}}
                    </script>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="bg"></div>
<!--选择自习班-->
<div class="popup xz-popup">
    <div class="popup-top">
        <em>选择自习班</em>
        <i class="fr"></i>
    </div>
    <dl class="popup-dl">
        <dd>
            <span>选择自习班：</span>
            <select class="sel2" type="text" id="select_zixi">

            </select>
            <script type="text/template" id="select_zixi_templ">
                {{~it:value:index}}
                <option value="{{=value.id}}">{{=value.name}}</option>
                {{~}}
            </script>
        </dd>
    </dl>
    <div class="popup-btn mt25">
        <button class="qd" id="addStudentsToZiXi">确定</button>
        <button class="qx">取消</button>
    </div>
</div>

<!--选择自习班-->
<div class="popup cj-popup">
    <div class="popup-top">
        <em>创建自习班</em>
        <i class="fr"></i>
    </div>
    <dl class="popup-dl">
        <dd>
            <span>自习班名称:</span>
            <input class="inp1" type="text" id="zixiname">
        </dd>
        <dd>
            <span>周几:</span>
            <select id="weekday" class="sel11"></select>
            <script type="text/template" id="weekday_temp">
                {{~it:value:index}}
                <option value="{{=value.value}}">{{=value.text}}</option>
                {{~}}
            </script>

        </dd>
        <dd>
            <span>第几节课:</span>
            <select id="course" class="sel11"></select>
            <script type="text/template" id="course_temp">
                {{~it:value:index}}
                <option value="{{=value.value}}">{{=value.text}}</option>
                {{~}}
            </script>
        </dd>
        <dd>
            <span>教室:</span>
            <select id="classroom" class="sel11"></select>
            <script type="text/template" id="classroom_temp">
                {{~it:value:index}}
                <option value="{{=value.roomid}}" ykb="{{=value.ykbid}}">{{=value.roomname}}</option>
                {{~}}
            </script>
        </dd>
        <dd>
            <span>教师:</span>
            <select id="teacher" class="sel11"></select>
            <script type="text/template" id="teacher_temp">
                {{~it:value:index}}
                <option value="{{=value.id}}">{{=value.name}}</option>
                {{~}}
            </script>
        </dd>
    </dl>
    <div class="popup-btn mt25">
        <button class="qd" id="createZiXiBan">确定</button>
        <button class="qx">取消</button>
    </div>
</div>
<!--自动排自习课-->
<div class="popup zd-popup">
    <div class="popup-top">
        <em>自动排自习课</em>
        <i class="fr close"></i>
    </div>
    <dl class="popup-dl">
        <dd class="tLeft">
            <span>教室容量</span>
            <input class="inp20" id="capacity">
        </dd>
        <dd>
            <button class="l1" id="autoArrangeByJXB">按教学班排</button>
        </dd>
        <dd>
            <button class="l2" id="autoArrangeByXZB">按行政班排</button>
        </dd>

    </dl>
</div>
</body>
<div class="jhh">
    <div class="jhh-top">数据处理中，请稍等！</div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp">安排中...</span>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('zixiclass', function (zixiclass) {
        zixiclass.init();
    });


</script>
</html>
