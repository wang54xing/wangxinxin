<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>信息设置</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link href="/static_new/css/jedate.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.charfirst.pinyin.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.listnav.min-2.1.js"></script>
    <script type="text/javascript">
        $(function () {
            $(".right-pos a").eq(0).attr("class", "vd2");
        })
        $(function () {
            $('.hov').click(function () {
            	var type = $('#subType').val();
            	if(type == 3){
            		$('.hiCl').hide();
            	}else{
            		$('.hiCl').show();
            	}
                $('.bg').show();
                $('.scla-popup').show();
            })
            $('.close').click(function () {
                $('.bg').hide();
                $('.detail-popup').hide();
                $('.scla-popup').hide();
            })
            $('.t-down').click(function () {
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top', -91)
            })
            $('.t-top').click(function () {
                $('.td-over').css('margin-top', 0)
            })
            $('.clStu').click(function () {
                $('.bg').show();
                $('.tj-popup').show();
                $("#tab-zxk").empty()
                $("#tab-zb").empty()
                $("#xing").empty()
                $("#sousuokk").attr("cid", "")
                $("#sousuokk").attr("combiname", "")
                $("#sousuokk").attr("userId", "")
                $("#userName").val("")
            })
            $('.feUl li').click(function () {
                $(this).addClass('ulcur').siblings().removeClass('ulcur');
                var tt = $(this).attr('id');
                $('#' + 'tab-' + tt).show().siblings().hide();
            })
        })
    </script>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<span id="defaultTerm" style="display: none"></span>

<div class="right-pos">
    <div class="ov-hi" style="overflow: auto;">
        <div class="td-over">
            <%--<a href="/newisolatepage/placementSet.do" class="va1">分教学班</a>--%>
            <%--<a href="/newisolatepage/stuSign.do" class="vb1">标记学生</a>--%>
            <%--<a href="/newisolatepage/stuRelation.do" class="vc1">关联学生</a>--%>
            <a href="/newisolatepage/fenbanInfoSet.do" class="vd1">信息设置</a>
            <%--<a href="/newisolatepage/conflict.do" class="ve1">冲突检测</a>--%>
            <a href="/newisolatepage/danShuangZhou.do" class="vz1">单双周课</a>
            <a href="/newisolatepage/zhuanXiang.do" class="vx1">专项组合</a>
            <%--<a href="/newisolatepage/jxbcombine.do" class="jxb1">教学班组合</a>--%>
                <a href="/newisolatepage/jxbTimeCombine.do" class="jxb1">分时段组合</a>
            <a href="/newisolatepage/paiKeZuHe.do" class="vpk1">排课组合</a>
        </div>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="ul-sty center mt25 fl">
        <ul class="bg-line">
            <li class="li-sty">
                <span class="fl">年级：</span>

                <div class="d-sty fl" id="grade">

                </div>
                <script type="text/template" id="grade_temp">
                    {{~it:value:index}}
                    <em val="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</em>
                    {{~}}
                </script>
            </li>
            <li class="li-sty">
                <span class="fl">学科：</span>

                <div class="d-sty d-ssty fl" id="subject">

                </div>
                <script type="text/template" id="subject_temp">
                    {{~it:value:index}}
                    <em val="{{=value.subid}}">{{=value.snm}}</em>
                    {{~}}
                </script>
            </li>
        </ul>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="fl">
        <button class="clStu mt35">添加学生</button>
    </div>
    <div class="mt35 fr mr100">
        <select class="sel15 mr30" type="text" id="subType">
            <option value="0">教学班类型</option>
            <option value="1">等级</option>
            <option value="2">合格</option>
            <option value="3">行政</option>
        </select>
        <!-- <button class="class-bt btn fr ml10 f16">保存</button> -->
        <button class="auto-bt bj btn fr ml10" id="detect">计算冲突</button>
        <button class="auto-bt bj btn fr ml10 c2e" id="export">导出</button>
        <button class="auto-bt bj btn fr ml10" id="clearSet">清空设置</button>
        <button class="auto-bt bj btn fr ml10 hov c1a" id="">自动设置
            <!-- <div class="none">仅支持走班教学班自动设置教师、教室。</div> -->
        </button>
        <button class="auto-bt bj btn fr ml10 c2e" id="fbBtn" style="display: none">分班方案</button>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="mt20 cur-tab cou-tab ccou-tab center fl">
        <div class='center w1200 clearfix' id="none_png" hidden>
            <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
        </div>
        <table class="fl tab-jsks votab clstab" id="content" hidden>
            <thead>
            <tr>
                <th width="150">教学班名称</th>
                <th width="170">自定义名称</th>
                <th width="110">教学班类型</th>
                <th width="90">学科</th>
                <th width="90">学生数量</th>
                <th width="155">学生来源</th>
                <th width="230">任课教师(年级带班/总带班数量)</th>
                <th width="195">教室</th>

            </tr>
            </thead>
            <tbody id="jxb">


            </tbody>
            <script type="text/template" id="jxb_temp">
                {{~it:value:index}}
                <tr ids="{{=value.id}}">
                    <td><em style="cursor: pointer" class="cur cInfo" ids="{{=value.id}}" nm="{{=value.name}}">{{=value.name}}</em></td>
                    <td class="hov1">{{=value.nickName}}<img class="lset name" jxbid="{{=value.id}}" nick="{{=value.nickName}}"
                                                src="/static_new/images/new33/edit.png"><div class="none">该名称用于排课和课表页面显示</div></td>
                    {{? value.type==1 }}
                    <td>等级考</td>
                    {{?? value.type==2}}
                    <td>合格考</td>
                    {{?? value.type==3}}
                    <td>行政型</td>
                    {{?? value.type==4}}
                    <td>专项型</td>
                    {{?? value.type==6}}
                    <td>行政型</td>
                    {{?}}
                    <td>{{=value.subjectName}}</td>
                    <td><em style="cursor: pointer" class="cur cInfo" ids="{{=value.id}}" nm="{{=value.name}}">{{=value.studentCount}}</em></td>
                    <%--<td>--%>
                    <%--{{~value.tags:value2:index2}}--%>
                    <%--<em class="block">{{=value2.name}} / {{=value2.num}}人</em>--%>
                    <%--{{~}}--%>
                    <%--</td>--%>
                    <td>
                        {{~value.class_count:value3:index3}}
                        <em class="block">{{=value3.grade}}（{{=value3.xh}}）班 / {{=value3.num}}人</em>
                        {{~}}
                    </td>
                    <td>{{=value.teacherName}}<img class="lset cla" jxbid="{{=value.id}}" eid="{{=value.subjectId}}"
                                                   tid="{{=value.teacherId}}" src="/static_new/images/new33/edit.png">({{=value.thisGradeCarryNum}}/{{=value.carryNum}})
                    </td>
                    <td>{{=value.classroomName}}<img class="lset gra" jxbid="{{=value.id}}" rid="{{=value.classroomId}}"
                                                     src="/static_new/images/new33/edit.png"></td>
                </tr>
                {{~}}
            </script>
        </table>
    </div>
</div>
</div>
<div class="bg"></div>

<!--设置老师-->
<div class="popup setp-popup">
    <div class="popup-top">
        <em>设置老师</em>
        <i class="fr close"></i>
    </div>
    <div class="setp-d">
        <select class="selt" id="teacher">

        </select>
        <script type="text/template" id="teacher_temp">
            {{~it:value:index}}
            <option value="{{=value.id}}">{{=value.name}}</option>
            {{~}}
        </script>
    </div>
    <div class="popup-btn">
        <button class="ss" id="set_teacher">设置</button>
        <button class="qxx">取消</button>
    </div>
</div>

<!--选择教室-->
<div class="popup selp-popup">
    <div class="popup-top">
        <em>选择教室</em>
        <i class="fr close"></i>
    </div>
    <div class="setp-d">
        <select class="selt" id="room">

        </select>
        <script type="text/template" id="room_temp">
            {{~it:value:index}}
            <option value="{{=value.id}}">{{=value.name}}</option>
            {{~}}
        </script>
    </div>
    <div class="popup-btn">
        <button class="ss" id="set_room">设置</button>
        <button class="qxx">取消</button>
    </div>
</div>

<!--设置名称-->
<div class="popup name-popup">
    <div class="popup-top">
        <em>设置名称</em>
        <i class="fr close"></i>
    </div>
    <div class="setp-d">
        <input id="nickname" class="inp1 searchName" type="text"/>
    </div>
    <div class="popup-btn mt25">
        <button class="ss" id="set_name">设置</button>
        <button class="qxx">取消</button>
    </div>
</div>

<div class="popup detail-popup">
    <div class="popup-top">
        <em id="className"></em>
        <i class="fr close"></i>
    </div>
    <div class="ml30 w620 clearfix">
        <button class="btn10 c2e fr mmtt11 ttj">添加</button>
    </div>
    <div class="ss-dv w620 ml30">
        <table>
            <thead>
            <tr>
                <th width="109">姓名</th>
                <th width="130">学籍号</th>
                <th width="50">层级</th>
                <th width="50">性别</th>
                <th width="125">行政班</th>
                <th width="70">选科组合</th>
                <th width="50">操作</th>
            </tr>
            </thead>
        </table>
        <div class="autoo h310 w610">
            <table>
                <tbody id="student">

                </tbody>
                <script type="text/template" id="room_xq">
                    {{~it:value:index}}
                    <tr>
                        <td>
                            <em class="w110 inline">{{=value.name}}</em>
                        </td>
                        <td>
                            <em class="w130 inline whisp" title="{{=value.sn}}">{{=value.sn}}</em>
                        </td>
                        <td>
                            <em class="w50 inline">{{=value.lev}}</em>
                        </td>
                        <td>
                            <em class="w50 inline">{{=value.sex}}</em>
                        </td>
                        <td>
                            <em class="w125 inline">{{=value.grade}}（{{=value.xh}}）班</em>
                        </td>
                        <td>
                            <em class="w70 inline">{{=value.conn}}</em>
                        </td>
                        <td>
                            <em class="w50 inline c6f delStudentJ" userId="{{=value.userId}}" jxbid="{{=value.jxbid}}">删除</em>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
    <div class="popup-btn mt25">
        <button class="qxx" hidden>关闭</button>
    </div>
</div>

<div class="popup ccxx-popup">
    <div class="popup-top">
        <em>查询</em>
        <i class="fr close"></i>
    </div>
    <div class="clearfix">
        <div class="w195 fl ml10">
            <script type="text/template" id="jxbClass">
                {{~it:value:index}}
                <li ids="{{=value.classId}}"><span>{{=value.gname}}({{=value.xh}})班</span></li>
                {{~}}
            </script>
            <ul class="tt-left h350 mt10 classrooms" id="clsUl">
            </ul>
        </div>
        <div class="fl w640 mmtt10" style="position: relative;">
            <dl class="dl">
                <script type="text/template" id="STUClass">
                    {{~it:value:index}}
                    <li class="li-special fuck" ids="{{=value.userId}}">
                        <img src="http://7xiclj.com1.z0.glb.clouddn.com/d_8_1.png">
                        <span>{{=value.userName}}</span>
                        <i class="t5" ids="{{=value.userId}}"></i>
                    </li>
                    {{~}}
                </script>
                <div id="myList-nav" class="member-select"></div>
                <a class="all-select">全选</a>
                <div class="myList">
                    <ul id="myList" class="member-list clearfix">

                    </ul>
                </div>
            </dl>
        </div>
    </div>
    <div class="popup-btn mt10">
        <button class="ss" id="addjxb">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>

<div class="popup scla-popup">
    <div class="popup-top">
        <em>自动关联</em>
        <i class="fr close"></i>
    </div>
    <div class="mt15" id="indexes">
        <input class="ml60" type="checkbox" value="1">教师
        <input class="ml60 hiCl" type="checkbox" value="2" >教室
    </div>
    <div class="popup-btn mt10">
        <button class="ss qxx" id="autoset">确定</button>
    </div>
</div>

<!--分班方案-->
<div class="popup fbf-popup">
    <div class="popup-top">
        <em>走班调整</em>
        <i class="fr fbf-close"></i>
    </div>
    <div class="fbf-content">
        <div class="tb-tool">
            <select id="zbJxbType">
                <option value="1">等级教学班</option>
                <option value="2">合格教学班</option>
            </select>
            <input id="zbJxbId" type="hidden" value="">
            <input id="zbJxbSubId" type="hidden" value="">
            <input id="zbJxbSerial" type="hidden" value="">
            <button class="fr save">保存</button>
            <%--<button class="fr import">导出</button>--%>
        </div>
        <div class="tb-group">
            <table>
                <thead>
                    <tr id="serailHead1">
                    </tr>
                    <script type="text/template" id="serailHead_temp1">
                        {{~it:value:index}}
                            <td colspan="2">
                                <div class="fbf-tb-title">
                                    走班组合时段{{=value.serail}}
                                    {{? value.acClassType==1}}
                                        <a class="fbf-add fbf_add" serail="{{=value.serail}}">+</a>
                                    {{?}}
                                </div>
                            </td>
                        {{~}}
                    </script>
                    <tr id="serailHead2">
                    </tr>
                    <script type="text/template" id="serailHead_temp2">
                        {{~it:value:index}}
                            <td>走班</td>
                            <td>班级</td>
                        {{~}}
                    </script>
                </thead>
                <tbody id="serailDataList">
                </tbody>
                <script type="text/template" id="seraildata_temp">
                    {{~it:value:index}}
                    <tr>
                        {{? value.one!=null }}
                            <td jxbId="{{=value.one.id}}" subId="{{=value.one.subjectId}}" serial="{{=value.one.timeCombSerial}}">
                                <p>{{=value.one.name}}(<a class="fb_cs_stu">{{=value.one.studentCount}}</a>)</p>
                                <p>{{=value.one.teacherName}}
                                    <a class="fb_cs_teacher" tid="{{=value.one.teacherId}}">
                                        <img class="icon-edit" src="/static_new/images/new33/edit.png">
                                    </a>
                                </p>
                                <p>{{=value.one.classroomName}}
                                    <a class="fb_cs_room" rmid="{{=value.one.classroomId}}">
                                        <img class="icon-edit" src="/static_new/images/new33/edit.png">
                                    </a>
                                </p>
                            </tdjxb>
                            <td>
                                {{~value.one.class_count:cls:index3}}
                                    <p>{{=cls.className}} / {{=cls.num}}人</p>
                                {{~}}
                            </td>
                        {{??}}
                            <td>
                            </td>
                            <td>
                            </td>
                        {{?}}
                        {{? value.maxSerial>1 }}
                            {{? value.two!=null }}
                                <td jxbId="{{=value.two.id}}" subId="{{=value.two.subjectId}}" serial="{{=value.two.timeCombSerial}}">
                                    <p>{{=value.two.name}}(<a class="fb_cs_stu">{{=value.two.studentCount}}</a>)</p>
                                    <p>{{=value.two.teacherName}}
                                        <a class="fb_cs_teacher" tid="{{=value.two.teacherId}}">
                                            <img class="icon-edit" src="/static_new/images/new33/edit.png">
                                        </a>
                                    </p>
                                    <p>{{=value.two.classroomName}}
                                        <a class="fb_cs_room" rmid="{{=value.two.classroomId}}">
                                            <img class="icon-edit" src="/static_new/images/new33/edit.png">
                                        </a>
                                    </p>
                                </td>
                                <td>
                                    {{~value.two.class_count:cls:index3}}
                                    <p>{{=cls.className}} / {{=cls.num}}人</p>
                                    {{~}}
                                </td>
                            {{??}}
                                <td>
                                </td>
                                <td>
                                </td>
                            {{?}}
                        {{?}}
                        {{? value.maxSerial>2 }}
                            {{? value.three!=null }}
                                <td jxbId="{{=value.three.id}}" subId="{{=value.three.subjectId}}" serial="{{=value.three.timeCombSerial}}">
                                    <p>{{=value.three.name}}(<a class="fb_cs_stu">{{=value.three.studentCount}}</a>)</p>
                                    <p>{{=value.three.teacherName}}
                                        <a class="fb_cs_teacher" tid="{{=value.three.teacherId}}">
                                            <img class="icon-edit" src="/static_new/images/new33/edit.png">
                                        </a>
                                    </p>
                                    <p>{{=value.three.classroomName}}
                                        <a class="fb_cs_room" rmid="{{=value.three.classroomId}}">
                                            <img class="icon-edit" src="/static_new/images/new33/edit.png">
                                        </a>
                                    </p>
                                </td>
                                <td>
                                    {{~value.three.class_count:cls:index3}}
                                    <p>{{=cls.className}} / {{=cls.num}}人</p>
                                    {{~}}
                                </td>
                            {{??}}
                                <td>
                                </td>
                                <td>
                                </td>
                            {{?}}
                        {{?}}
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
</div>

<!--分班方案-设置老师-->
<div class="popup fbf-setp-popup">
    <div class="popup-top">
        <em>设置老师</em>
        <i class="fr setp-close"></i>
    </div>
    <div class="setp-d">
        <select id="zbJxbTeaList" class="selt">
        </select>
        <script type="text/template" id="zbJxbTeas_tmpl">
            {{~it:value:index}}
            <option value="{{=value.id}}">{{=value.name}}</option>
            {{~}}
        </script>
    </div>
    <div class="popup-btn">
        <button class="ss submit-btn">设置</button>
        <button class="setp-close">取消</button>
    </div>
</div>

<!--分班方案-选择教室-->
<div class="popup fbf-selp-popup">
    <div class="popup-top">
        <em>选择教室</em>
        <i class="fr selp-close"></i>
    </div>
    <div class="setp-d">
        <select id="zbJxbRoomList" class="selt">
        </select>
        <script type="text/template" id="zbJxbRoom_temp">
            {{~it:value:index}}
            <option value="{{=value.id}}">{{=value.name}}</option>
            {{~}}
        </script>
    </div>
    <div class="popup-btn">
        <button class="ss submit-btn">设置</button>
        <button class="selp-close">取消</button>
    </div>
</div>

<!--分班方案-查看学生-->
<div class="popup fbf-detail-popup">
    <div class="popup-top">
        <em>教学班学生</em>
        <i class="fr fbf-detail-close"></i>
    </div>
    <div class="ml30 w620 clearfix">
        <button class="btn10 c2e fr mmtt11 fbf_detail_search">添加</button>
    </div>
    <div class="ss-dv w620 ml30">
        <table>
            <thead>
            <tr>
                <th width="109">姓名</th>
                <th width="130">学籍号</th>
                <th width="50">层级</th>
                <th width="50">性别</th>
                <th width="125">行政班</th>
                <th width="70">选科组合</th>
                <th width="50">操作</th>
            </tr>
            </thead>
        </table>
        <div class="autoo h310 w610">
            <table>
                <tbody id="zbJxbStuList">
                </tbody>
                <script type="text/template" id="zbJxbStuList_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>
                            <em class="w110 inline">{{=value.name}}</em>
                        </td>
                        <td>
                            <em class="w130 inline whisp" title="{{=value.sn}}">{{=value.sn}}</em>
                        </td>
                        <td>
                            <em class="w50 inline">{{=value.lev}}</em>
                        </td>
                        <td>
                            <em class="w50 inline">{{=value.sex}}</em>
                        </td>
                        <td>
                            <em class="w125 inline">{{=value.grade}}（{{=value.xh}}）班</em>
                        </td>
                        <td>
                            <em class="w70 inline">{{=value.conn}}</em>
                        </td>
                        <td>
                            <em class="w50 inline c6f delStudent" userId="{{=value.userId}}" jxbId="{{=value.jxbid}}">删除</em>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
</div>

<!--分班方案-查看学生-添加查询-->
<div class="popup fbf-ccxx-popup">
    <div class="popup-top">
        <em>查询</em>
        <i class="fr fbf-ccxx-close"></i>
    </div>
    <div class="clearfix">
        <div class="w195 fl ml10">
            <script type="text/template" id="zbJxbClassList_temp">
                {{~it:value:index}}
                <li ids="{{=value.classId}}"><span>{{=value.gname}}({{=value.xh}})班</span></li>
                {{~}}
            </script>
            <ul class="tt-left h350 mt10 classrooms" id="zbJxbClassList">
            </ul>
        </div>
        <div class="fl w640 mmtt10" style="position: relative;">
            <dl class="dl">
                <div id="zbJxbClsStus-nav" class="member-select"></div>
                <a class="all-select">全选</a>
                <div class="myList">
                    <ul id="zbJxbClsStus" class="member-list clearfix">

                    </ul>
                </div>
                <script type="text/template" id="zbJxbClsStus_tmpl">
                    {{~it:value:index}}
                    <li class="li-special fuck" ids="{{=value.userId}}">
                        <img src="http://7xiclj.com1.z0.glb.clouddn.com/d_8_1.png">
                        <span>{{=value.userName}}</span>
                        <i class="t5" ids="{{=value.userId}}"></i>
                    </li>
                    {{~}}
                </script>
            </dl>
        </div>
    </div>
    <div class="popup-btn mt10">
        <button class="ss submit-btn">确定</button>
        <button class="fbf-ccxx-close">取消</button>
    </div>
</div>

<!--添加教学班-->
<div class="popup fbf2-setp-popup">
    <div class="popup-top">
        <em>添加教学班</em>
        <i class="fr setp2-close"></i>
    </div>
    <div class="setp-d">
        <select id="noSerialJxbList" class="selt">
        </select>
        <script type="text/template" id="noSerialJxbList_temp">
            {{~it:value:index}}
            <option value="{{=value.id}}" subId="{{=value.subjectId}}">{{=value.name}}</option>
            {{~}}
        </script>
    </div>
    <div class="popup-btn">
        <button class="ss submit-btn">确定</button>
        <button class="setp2-close">取消</button>
    </div>
</div>

<div class="jhh">
    <div class="jhh-top">数据处理中，请稍等！</div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp">冲突计算中...</span>
</div>

<!--添加学生-->
<div class="popup tj-popup">
    <div class="popup-top">
        <em>添加学生</em>
        <i class="fr close"></i>
    </div>
    <ul class="feUl mt10">
        <li class="ulcur" id="xzb" is="1">行政班</li>
        <li id="zb" is="2">走班</li>
        <li id="zxk" is="3">专项课</li>
    </ul>
    <div class="fup">
        <div class="inline pos">
            <input class="inp10" type="text" placeholder="请输入学生姓名" id="userName">
            <i class="search at" id="sousuokk"></i>
        </div>
    </div>
    <div class="ha310">
        <div id="tab-xzb">
            <div class="gUl clearfix">
                <ul id="xing">

                </ul>
            </div>
        </div>
        <script type="text/template" id="XingSub">
            {{~it:value:index}}
            <li class="poss">
                <i></i>
                <i class="ti tii" ids="{{=value.jxbId}}"></i>
                <span title="{{=value.jxbName}}({{=value.jxbCount}})">{{=value.jxbName}}({{=value.jxbCount}})</span>
            </li>
            {{~}}
        </script>

        <script type="text/template" id="subS">
            {{~it:value:index}}
            <div class="ffN">
                <div class="fTop">
                    <span subId="{{=value.subid}}" class="isy">{{=value.snm}}</span>
                </div>
                <div class="gUl clearfix">
                    <ul class="item te{{=value.subid}}" subId="{{=value.subid}}">
                    </ul>
                </div>
            </div>
            {{~}}
        </script>
        <script type="text/template" id="subJxb">
            {{~it:value:index}}
            <li class="poss">
                {{? value.Tui == 1}}
                <i class="tjj"></i>
                <i class="ti tii" isFaBu="{{=value.isFaBu}}" ids="{{=value.jxbId}}"></i>
                {{?}}
                {{? value.Tui ==0}}
                <i></i>
                <i class="ti" isFaBu="{{=value.isFaBu}}" ids="{{=value.jxbId}}"></i>
                {{?}}
                <span title="{{=value.jxbName}}({{=value.jxbCount}})">{{=value.jxbName}}({{=value.jxbCount}})</span>
            </li>
            {{~}}
        </script>
        <script type="text/template" id="ZhuanList">
            {{~it:value:index}}
            <li class="poss">
                <i></i>
                <i class="ti" ids="{{=value.zxId}}"></i>
                <span title="{{=value.jxbName}}({{=value.jxbCount}})">{{=value.jxbName}}({{=value.jxbCount}})</span>
            </li>
            {{~}}
        </script>
        <div id="tab-zb">

        </div>
        <script type="text/template" id="Zhuan">
            {{~it:value:index}}
            <div class="ffN">
                <div class="fTop">
                    <span zxId="{{=value.zxId}}" class="isy tCur">{{=value.name}}</span>
                </div>
                <div class="gUl clearfix">
                    <ul class="items te{{=value.zxId}}" zxId="{{=value.zxId}}">
                    </ul>
                </div>
            </div>
            {{~}}
        </script>
        <div id="tab-zxk">

        </div>
    </div>
    <div class="popup-btn mt10">
        <button class="ss qxx" id="queDing">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>

<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('fenbanInfoSet', function (fenbanInfoSet) {
        fenbanInfoSet.init();
    });
</script>
</body>
</html>
