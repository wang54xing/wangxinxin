<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>教学班组合</title>
    <link type="text/css" rel="stylesheet" href="/static_new/css/new33/timeCombine/course2.css">
    <link type="text/css" rel="stylesheet" href="/static_new/css/new33/timeCombine/style.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script>
        $(function(){
            $('.ahref').click(function(){
                $('#content1').show();
                $('#content2').hide();
            })
        })
    </script>
    <style>
        .cx {
            width: 105px;
            height: 38px;
            background: #2e94db;
            border: 1px solid #2e94db;
            border-radius: 5px;
            color: white;
        }
        div .active1 {
            background: #AFEEEE;
        }
    </style>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<span id="defaultTerm" style="display: none"></span>

<%--<div class="right-pos">
    <div class="ov-hi">
        <div class="td-over">
            &lt;%&ndash;<a href="/newisolatepage/placementSet.do" class="va1">分教学班</a>&ndash;%&gt;
            &lt;%&ndash;<a href="/newisolatepage/stuSign.do" class="vb1">标记学生</a>&ndash;%&gt;
            &lt;%&ndash;<a href="/newisolatepage/stuRelation.do" class="vc1">关联学生</a>&ndash;%&gt;
            <a href="/newisolatepage/fenbanInfoSet.do" class="vd1">信息设置</a>
            &lt;%&ndash;<a href="/newisolatepage/conflict.do" class="ve1">冲突检测</a>&ndash;%&gt;
            <a href="/newisolatepage/danShuangZhou.do" class="vz1">单双周课</a>
            <a href="/newisolatepage/zhuanXiang.do" class="vx1">专项组合</a>
            &lt;%&ndash;<a href="/newisolatepage/jxbcombine.do" class="jxb1">教学班组合</a>&ndash;%&gt;
            <a href="/newisolatepage/jxbTimeCombine.do" class="jxb2">分时段组合</a>
            <a href="/newisolatepage/paiKeZuHe.do" class="vpk1">排课组合</a>
        </div>
    </div>
</div>--%>

<div id="content1" class="content">

    <div class="jxb-tool">
        <input type='hidden' id='gradeId' value='${gradeId}'/>
        <%--<select class="grade">--%>
        <%--</select>--%>
        <%--<script type="text/template" id="grade_temp">--%>
            <%--{{~it:value:index}}--%>
            <%--<option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>--%>
            <%--{{~}}--%>
        <%--</script>--%>
        <select id="jxbType">
            <option value="1">等级教学班</option>
            <option value="2">合格教学班</option>
        </select>
        <%--<a class="bt bt1" id="newZh">新建时段组合</a>--%>
        <%--<a class="bt bt2" id="delZh">删除时段组合</a>--%>
        <%--<a class="bt pkzh">组合排课</a>--%>
        <div style="float: right;margin-right: 86px;">
            <a id="prevStop" class="bt bt2">上一步</a>
            <a class="bt bt1" id="autoTimeComb">自动排课</a>
            <a id="nextStop" class="bt bt2">下一步</a>
        </div>
    </div>

<div class="jxb-zh-content">
<div class="jxb-zh-top" style="width: 1100px;">
    <div id="jxb1" style="margin-top: 75px; text-align: center">
        <img style="margin: 0 auto" src="/static_new/images/pic_blank_3.png">
        <p style="color: #000;font-size: 18px;">请先将学生添加至标签</p>
    </div>
    <div id="jxb2">
        <div class="nav-table">
                <table class="jxb-zh-table ">
                    <thead>
                    <tr>
                        <td width="125">标签名</td>
                        <td width="125">选科组合</td>
                    </tr>
                    </thead>
                </table>
            </div>
        <div class="top-table">
            <table class="jxb-zh-table">
                <thead>
                <tr id="headNameList">
                </tr>
                <script type="text/template" id="headNameList_temp">
                    {{~it:value:index}}
                        {{?index == 0 || index == 1}}
                            <td width="125">{{=value.name}}</td>
                        {{??}}
                            <td width="280">
                                <span class="name">{{=value.name}}</span>
                                <a class="qingkong col-clear" serial="{{=value.serial}}" id="clear">清空该列</a>
                            </td>
                        {{?}}
                    {{~}}
                </script>
                </thead>
            </table>
        </div>
        <div class="left-table" id="jxb3">
            <table class="jxb-zh-table">
                <thead>
                    <tr>
                        <td width="125">标签名</td>
                        <td width="125">选科组合</td>
                    </tr>
                </thead>
                <tbody id="listName">
                </tbody>
                <script type="text/template" id="listName_temp">
                    {{~it:value:index}}
                        <tr>
                            {{~value:tag:it}}
                            {{?it == 0}}
                            <td width="125" height="106" ><span class="watch tag_show" id="bq" tagId="{{=tag.bqid}}" name="{{=tag.name}}">{{=tag.name}}</span></td>
                            {{??}}
                            <td width="125" height="106">{{=tag.name}}</td>
                            {{?}}
                            {{~}}
                        </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
</div>
<div class="jxb-zh-group">
    <table class="jxb-zh-table" id="main_table">
        <thead>
            <tr class="headNameList" id="jxb4">

            </tr>
            <script type="text/template" class="headNameList_temp">
                {{~it:value:index}}
                {{?index == 0 || index == 1}}
                <td width="125"></td>
                {{??}}
                <td width="280" class="hide">
                    <span class="name" hidden></span>
                    &nbsp;
                </td>
                {{?}}
                {{~}}
            </script>
        </thead>
        <tbody id="jxbList">

        </tbody>
        <script type="text/template" id="jxbList_temp">
            {{~it:value:index}}
            <tr>
                <td width="125">&nbsp;</td>
                <td width="125">&nbsp;</td>
                {{~value:jxb:it}}
                    <td width="280">
                        {{?jxb.jxbId == undefined}}
                        <div class="jxb-zh-item jxb">
                            <span class="name nt-jxb jxbcx{{=it}}" idx="{{=it}}" jxbId="{{=jxb.jxbId}}" jxbnm="{{=jxb.jxbName}}" suid="{{=jxb.subId}}">{{=jxb.jxbName}}</span>
                            <div class="gp">
                                <a class="cs selectJxb" serial="{{=jxb.serial}}" tagId="{{=jxb.tagId}}" jxbId="{{=jxb.jxbId}}"></a>
                                <a class="del delJxb" serial="{{=jxb.serial}}" tagId="{{=jxb.tagId}}"></a>
                            </div>
                        </div>
                        {{??}}
                        <div class="jxb-zh-item jxb init">
                            <span class="name nt-jxb jxbcx{{=it}}" idx="{{=it}}" jxbId="{{=jxb.jxbId}}" jxbnm="{{=jxb.jxbName}}" suid="{{=jxb.subId}}">{{=jxb.jxbName}}</span>
                            <div class="gp">
                                <a class="cs selectJxb" serial="{{=jxb.serial}}" tagId="{{=jxb.tagId}}" jxbId="{{=jxb.jxbId}}"></a>
                                <a class="del delJxb" serial="{{=jxb.serial}}" tagId="{{=jxb.tagId}}"></a>
                            </div>
                        </div>
                        {{?}}

                        {{?jxb.teaId == undefined}}
                        <div class="jxb-zh-item teacher">
                            <span class="name">{{=jxb.teaName}}</span>
                            <div class="gp">
                                <a class="cs selectTea" serial="{{=jxb.serial}}"  jxbId="{{=jxb.jxbId}}" subId="{{=jxb.subId}}" teaId="{{=jxb.teaId}}"></a>
                                <a class="del delTea" jxbId="{{=jxb.jxbId}}"></a>
                            </div>
                        </div>
                        {{??}}
                        <div class="jxb-zh-item teacher init">
                            <span class="name">{{=jxb.teaName}}</span>
                            <div class="gp">
                                <a class="cs selectTea" serial="{{=jxb.serial}}"  jxbId="{{=jxb.jxbId}}" subId="{{=jxb.subId}}" teaId="{{=jxb.teaId}}"></a>
                                <a class="del delTea" jxbId="{{=jxb.jxbId}}"></a>
                            </div>
                        </div>
                        {{?}}

                        {{?jxb.rid == undefined}}
                        <div class="jxb-zh-item house">
                            <span class="name">{{=jxb.roomName}}</span>
                            <div class="gp">
                                <a class="cs selectRoom" rid="{{=jxb.rid}}" serial="{{=jxb.serial}}" jxbId="{{=jxb.jxbId}}"></a>
                                <a class="del delRoom" jxbId="{{=jxb.jxbId}}"></a>
                            </div>
                        </div>
                        {{??}}
                        <div class="jxb-zh-item house init">
                            <span class="name">{{=jxb.roomName}}</span>
                            <div class="gp">
                                <a class="cs selectRoom" rid="{{=jxb.rid}}" serial="{{=jxb.serial}}" jxbId="{{=jxb.jxbId}}"></a>
                                <a class="del delRoom" jxbId="{{=jxb.jxbId}}"></a>
                            </div>
                        </div>
                        {{?}}
                    </td>
                {{~}}
            </tr>
            {{~}}
        </script>
    </table>
</div>
</div>
</div>
<div style="display: none;" id="content2" class="content c00" style="width: 1300px">
    <a  class="ahref" style="cursor: pointer">&lt;&nbsp;&nbsp;返回教学班组合</a>
    <span class="ml25" id="gradeName">年级：高二</span>
    <select class="sel1" id="jxbType1">
        <option value="1">等级教学班</option>
        <option value="2">合格教学班</option>
    </select>
    <div class="tab3 mt20">
        <table>
            <thead>
                <tr>
                    <th style="width: 180px;">排课组合名</th>
                    <th style="width: 800px;">教学班列表</th>
                    <th style="width: 120px;">操作</th>
                </tr>
            </thead>
            <tbody id="ZuHeList">
                <%--<tr>--%>
                    <%--<td style="width: 180px;">等级-时段组合1</td>--%>
                    <%--<td style="width: 800px;">--%>
                        <%--<span class="sign-sp">化学D1-吴巧玲</span>--%>
                        <%--<span class="sign-sp">化学D1-吴巧玲</span>--%>
                        <%--<span class="sign-sp">化学D1-吴巧玲</span>--%>
                    <%--</td>--%>
                    <%--<td style="width: 120px;"></td>--%>
                <%--</tr>--%>
            </tbody>

            <script type="text/template" id="ZuHe_temp">
                {{~it:value:index}}
                <tr>
                    <td>{{=value.name}}</td>
                    <td>
                        {{~value.jxbList:obj:itt}}
                        <span class="spw inline inline-jxb sign-sp" ids="{{=obj.id}}" zuHeId="{{=obj.zuHeId}}">{{=obj.jxbName}}-{{=obj.TeaName}}</span>
                        {{~}}
                    </td>
                    <td>
                        <em style="cursor: pointer;"  class="paik" ids="{{=value.id}}" id="paiKe" zuHename = "{{=value.name}}">排课</em>
                    </td>
                </tr>
                {{~}}
            </script>
        </table>
    </div>
</div>
<div id="content3" class="center w1300 clearfix">
    <div class="m2 mt20 mb35">
    <div class="mt20 clearfix w1075">
        <div class="fl w950">
            <a class="cursor Ulprev">&lt;返回排课组合</a>
            <button class="cx fr" id="cancel">撤销</button>
        </div>
        <p class="Ulp fr" id="zuHeName1"></p>
    </div>
    <div>
        <div class="fl w980">
            <table class="tab1 taBb2 mt10 fl">
                <thead>
                <tr>
                    <th>
                        <div class="wd100">课节 / 日</div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="psre1 ke kejie">
                    </td>
                    <script type="text/template" id="ks_temp">
                        {{~it:value:index}}
                        <div class="ke{{=value.serial}} kejiecount"><span class="inline mt12">{{=value.name}}</span><br><em>{{=value.start}}-{{=value.end}}</em>
                        </div>
                        {{~}}
                    </script>
                </tr>
                </tbody>
            </table>
            <div style="width: 831px;overflow-x: auto;float: left;">
                <table class="tab1 mt10" id="tabzuHe">
                    <thead>
                    <tr class="weeks">
                    </tr>
                    <script type="text/template" id="weeks_temp">
                        <th>
                            <div class="zhou">
                                {{~it:value:index}}
                                <div class="zh{{=value.type}} gradeweek">{{=value.name}}</div>
                                {{~}}
                            </div>
                        </th>
                    </script>
                    </thead>
                    <tbody>
                    <tr class="jxbkj">

                    </tr>
                    <script type="text/template" id="kbByTea">
                        <td class="gxao">
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

                    </tbody>
                </table>
            </div>
        </div>
        <div class="fl ml25 mt12">
            <div class="Uld">
                <ul class="nUl" id="jxbs">
                </ul>
                <script type="text/template" id="zuHeJXBListTemp">
                    {{~it:value:index}}
                    <li class="jxbs" ids = "{{=value.id}}">
                        <div class="inline bL fl">
                            <p>{{?value.nickName!=''}}
                                {{=value.nickName}}
                                {{??}}
                                {{=value.name}}
                                {{?}}
                            </p>
                            <p>{{=value.teacherName}}</p>
                            <p>{{=value.classroomName}}</p>
                        </div>
                        <div class="inline wi39">
                            <span class=""><em class="ypks" ypks = {{=value.ypksCount}}>{{=value.ypksCount}}</em>/<em class="zks" zks="{{=value.zksCount}}">{{=value.zksCount}}</em></span>
                        </div>
                    </li>
                    {{~}}
                </script>
            </div>
        </div>
    </div>
</div>

</div>

<div class="dialog dialog-sign">
    <div class="dialog-cover"></div>
    <div class="dialog-content" style="width: 804px;left: 44%;box-shadow: 5px 5px 15px 0px #727272;">
        <div class="dialog-top">
            <span id="jxbTeaName">&nbsp;&nbsp;学生列表</span>
            <a class="close close-dialog" onclick="dgHide()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd" style="margin-left: 28px;">
            <div>
                <span>行政班：
                    <select class="selT" id="cla">
                        <option>全部</option>
                    </select>
                    <script type="text/template" id="class_temp">
                    {{~it:value:index}}
                    {{?value.classId == "*"}}
                        <option value="{{=value.classId}}">{{=value.name}}</option>
                        {{??}}
                        <option value="{{=value.classId}}">{{=value.gname}}（{{=value.xh}}）班</option>
                        {{?}}
                    {{~}}
                    </script>
                </span>
                <span>选科组合：
                    <select class="selT" id="selCom">
                    </select>
                    <script type="text/template" id="content2_temp">
                    {{~it:value:index}}
                    <option value="{{=value.subName}}" subName="{{=value.name}}">{{=value.name}}</option>
                    {{~}}
                    </script>
                </span>
                <span>标签名：
                    <select class="selT" id="tag">
                    </select>
                    <script type="text/template" id="tag_temp">
                    {{~it:value:index}}
                    <option value="{{=value.tagId}}" >{{=value.tagName}}</option>
                    {{~}}
                    </script>
                </span>
            </div>
            <span style="display: inline-block;margin: 15px 0;" id="stuCount"></span>
            <table class="tab3" style="position: absolute">
                <thead>
                    <tr>
                        <th style="width: 90px;">学生姓名</th>
                        <th style="width: 200px;">学号</th>
                        <th style="width: 110px;">行政班</th>
                        <th style="width: 120px;">选科组合</th>
                        <th style="width: 200px;">所属标签名</th>
                    </tr>
                </thead>
            </table>
            <div style="max-height: 300px;overflow-y:auto;width: 755px;margin-bottom: 15px;">
                <table class="tab3">
                    <thead style="color: transparent">
                        <tr>
                            <th style="width: 90px;">学生姓名</th>
                            <th style="width: 200px;">学号</th>
                            <th style="width: 110px;">行政班</th>
                            <th style="width: 120px;">选科组合</th>
                            <th style="width: 200px;">所属标签名</th>
                        </tr>
                    </thead>
                    <tbody id="student">

                    </tbody>
                    <script type="text/template" id="student_temp">
                        {{~it:value:index}}
                            <tr>
                                <td style="width: 90px;">{{=value.userName}}</td>
                                <td style="width: 200px;">{{=value.studyNumber}}</td>
                                <td style="width: 110px;">{{=value.gradeName}}({{=value.classXH}})班</td>
                                <td style="width: 120px;">{{=value.combiname}}</td>
                                <td style="width: 200px;">{{=value.tagName}}</td>
                            </tr>
                        {{~}}
                    </script>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="dialog dialog-tag">
    <div class="dialog-cover"></div>
    <div class="dialog-content">
        <div class="dialog-top">
            <span id="tagName"></span>
            <a class="close close-dialog" onclick="dgHide()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd">
            <div class="jx-gp">
                <table class="jx-tb">
                    <thead><tr><th>姓名</th><th>学籍号</th><th>性别</th><th>行政班</th><th>选科组合</th><th>层级</th></tr></thead>
                    <tbody id="stList">
                    </tbody>
                    <script type="text/template" id="st_temp">
                        {{~it:value:index}}
                            <tr>
                                <td>{{=value.name}}</td>
                                <td>{{=value.sn}}</td>
                                <td>{{=value.sex}}</td>
                                <td>{{=value.clsName}}</td>
                                <td>{{=value.com}}</td>
                                <td>{{=value.lev}}</td>
                            </tr>
                        {{~}}
                    </script>
                </table>
            </div>
            <div class="jx-btn">
                <a onclick="dgHide()">关闭</a>
            </div>
        </div>
    </div>
</div>

<div class="dialog dialog-name">
    <div class="dialog-cover"></div>
    <div class="dialog-content">
        <div class="dialog-top">
            <span>编辑时段组合名称</span>
            <a class="close close-dialog" onclick="dgHide()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd">

            <input type="text" class="jxb-input" placeholder="最多输入10个">
            <div class="jx-btn">
                <a>确定</a>
                <a onclick="dgHide()">关闭</a>
            </div>
        </div>
    </div>
</div>

<div class="dialog dialog-jxb">
    <div class="dialog-cover"></div>
    <div class="dialog-content">
        <div class="dialog-top">
            <span>选择教学班</span>
            <a class="close close-dialog" onclick="dgHide()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd">
            <div class="jxb-group">
                <span>添加教学班：</span>
                <select class="jxb-select" id="selectJxb">

                </select>
                <script type="text/template" id="selectJxb_temp">
                    {{~it:value:index}}
                    <option value="{{=value.id}}">
                        {{?value.nikeName == "" || value.nikeName == null}}
                            {{=value.name}}({{=value.studentCount}}人)
                        {{??}}
                            {{=value.nikeName}}({{=value.studentCount}}人)
                        {{?}}
                    </option>
                    {{~}}
                </script>
            </div>
            <div class="jx-btn">
                <a id="jxb_qd">确定</a>
                <a onclick="dgHide()">关闭</a>
            </div>
        </div>
    </div>
</div>

<div class="z-popup4 popup">
    <div class="popup-top">
        <em class="fl">未填写完整的组合记录</em>
        <i class="close fr" onclick="dgHide()"></i>
    </div>
    <div class="p3Oh">
        <p style="margin-top: -2px;">以下组合中有教学班未设置老师或教室信息，请设置完成后进行下一步操作：</p>
        <div style="margin-top: 25px;" id="list">
            <script type="text/template" id="list_temp">
                {{~it:value:index}}
                <div>
                    <span>{{=value}}</span>
                </div>
                {{~}}
            </script>
        </div>

    </div>
</div>

<div class="dialog dialog-teacher">
    <div class="dialog-cover"></div>
    <div class="dialog-content">
        <div class="dialog-top">
            <span>选择老师</span>
            <a class="close close-dialog" onclick="dgHide()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd">
            <div class="jxb-group">
                <span>添加老师：</span>
                <select class="jxb-select" id="teacher">
                </select>
                <script type="text/template" id="teacher_temp">
                    {{~it:value:index}}
                    <option value="{{=value.id}}">{{=value.name}}</option>
                    {{~}}
                </script>
            </div>
            <div class="jx-btn">
                <a id="tea_qd">确定</a>
                <a onclick="dgHide()">关闭</a>
            </div>
        </div>
    </div>
</div>

<div class="dialog dialog-house">
    <div class="dialog-cover"></div>
    <div class="dialog-content">
        <div class="dialog-top">
            <span>选择教室</span>
            <a class="close close-dialog" onclick="dgHide()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd">
            <div class="jxb-group">
                <span>添加教室：</span>
                <select class="jxb-select" id="room">

                </select>
                <script type="text/template" id="room_temp">
                    {{~it:value:index}}
                    <option value="{{=value.id}}">{{=value.name}}</option>
                    {{~}}
                </script>
            </div>
            <div class="jx-btn">
                <a id="set_room">确定</a>
                <a onclick="dgHide()">关闭</a>
            </div>
        </div>
    </div>
</div>

<div class="dialog dialog-del" >
    <div class="dialog-cover"></div>
    <div class="dialog-content">
        <div class="dialog-top">
            <span>删除组合</span>
            <a class="close close-dialog" onclick="dgHide()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd">
            <div class="jxb-group">
                <span>选择删除组合：</span>
                <select class="jxb-select" id="headNameList1">

                </select>
                <script type="text/template" id="headNameList_temp1">
                    {{~it:value:index}}
                        {{?value.serial != undefined}}
                        <option value="{{=value.serial}}">{{=value.name}}</option>
                        {{?}}
                    {{~}}
                </script>
            </div>
            <div class="jx-btn">
                <a id="del_zuhe">确定</a>
                <a onclick="dgHide()">关闭</a>
            </div>
        </div>
    </div>
</div>

<div class="dialog dialog-clear">
    <div class="dialog-cover"></div>
    <div class="dialog-content">
        <div class="dialog-top">
            <span>清空该列</span>
            <a class="close close-dialog" onclick="dgHide()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd">
            <div class="jxb-group">
                <span>确定是否清空该列？</span>
            </div>
            <div class="jx-btn">
                <a id="qd_clear">确定</a>
                <a onclick="dgHide()">关闭</a>
            </div>
        </div>
    </div>
</div>

<div class="dialog dialog-bj">
    <div class="dialog-cover"></div>
    <div class="dialog-content">
        <div class="dialog-top">
            <span>教学班</span>
            <a class="close close-dialog" onclick="dgHide()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd">

            <div class="jx-tb-group">
                <button id="change_class">调整</button>
                <div class="tb">
                    <table>
                        <thead>
                            <tr>
                                <th>姓名</th>
                                <th>学籍号</th>
                                <th>层级</th>
                                <th>性别</th>
                                <th>行政班</th>
                                <th>选科组合</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody id="student2">

                        </tbody>
                        <script type="text/template" id="student2_temp">
                            {{~it:value:index}}
                            <tr>
                                <td>{{=value.name}}</td>
                                <td  title="{{=value.sn}}">{{=value.sn}}</td>
                                <td>{{=value.lev}}</td>
                                <td>{{=value.sex}}</td>
                                <td>{{=value.grade}}（{{=value.xh}}）班</td>
                                <td>{{=value.conn}}</td>
                                <td userId="{{=value.userId}}" jxbid="{{=value.jxbid}}"><input type="checkbox"></td>
                            </tr>
                            {{~}}
                        </script>
                    </table>
                </div>
            </div>

            <div class="jx-btn">
                <a onclick="dgHide()">确定</a>
                <%--<a onclick="dgHide()">关闭</a>--%>
            </div>
        </div>
    </div>
</div>

<div class="dialog dialog-change">
    <div class="dialog-content">
        <div class="dialog-top">
            <span>教学班调整</span>
            <a class="close close-dialog" onclick="dgHide2()"><img src="/static_new/images/new33/kw_btn.png" /></a>
        </div>
        <div class="dialog-bd">
            <div class="jxb-group">
                <span>现教学班：</span>
                <select class="jxb-select orgJxb">
                </select>
            </div>
            <div class="jxb-group">
                <span>调整至：</span>
                <select class="jxb-select newJxb">
                </select>
            </div>
            <div class="jx-btn">
                <a id="udpStudentByJxb">确定</a>
                <a onclick="dgHide2()">关闭</a>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="/static_new/js/modules/core/0.1.0/index.js"></script>
<div class="bg"></div>
<div class="jhh">
    <div class="jhh-top">数据处理中，请稍等！</div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp"></span>
</div>

<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('jxbAutoTimeCombine', function (jxbAutoTimeCombine) {
        jxbAutoTimeCombine.init();
    });




    function dgHide2() {
        $('.dialog-change').hide()
    }

</script>
</body>
</html>