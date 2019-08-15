<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/5/9
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>专项组合</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" href="/static_new/css/new33/course2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <style>
        .quedingren {
            cursor: not-allowed;
        }

        .w220 {
            width: 220px;;
        }

        .w218 {
            width: 218px;;
        }

        .ha210 {
            height: 210px;
            width: 455px;
            overflow-y: auto;
        }
        .zzQ{
            margin-left: 20px;
            vertical-align: top;
            margin-top: 5px;
        }
        .w240 {
            width: 238px;
        }

        .mtr23 {
            margin: 9px 23px;
        }

        .mtr26 {
            margin: 9px 26px;
        }

        /*.sel2 {*/
            /*width: 145px;*/
            /*height: 30px;*/
            /*border: 1px solid #9a9a9a;*/
            /*border-radius: 5px;*/
        /*}*/

        .w480 {
            width: 480px;
        }

        .w239 {
            width: 238px;;
        }

        .w294 {
            width: 294px;;
        }

        .w199 {
            width: 199px;
        }

        .w197 {
            width: 197px;
        }

        .tabSty tr th, .tabSty tr td {
            height: 40px;
            text-align: center;
            border: 1px solid #969696;
        }

        .tabSty tr th {
            background: #ededed;
            color: black;
            border-bottom: none !important;
        }

        .loading-p {
            display: none;
            z-index: 999999999;
            background: transparent;
        }

        .innp1 {
            width: 180px;
            height: 28px;
        }

        .bor {
            border: 1px solid #dfdfdf;
        }
        .w198{
            width: 198px;
        }
        .w199{
            width: 199px;
        }
        .w398{
            width: 398px;;
        }
        .c6f{
            color: #6f69d6!important;
        }
        .prev{
            color: #2e94db;
        }
        .prev:hover{
            color: red;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $(".right-pos a").eq(2).attr("class", "vx2");
            $('.qx,.close,.qxx').click(function () {
                $('.bg').hide();
                $('.man-popup').hide();
                $('.xzz-popup').hide();
                $('.mnew-popup').hide();
                $('.txz-popup').hide();
            })
            $('.ss-li li').click(function () {
                $(this).addClass('ss-cur').siblings().removeClass('ss-cur')
            })

            $('.prev').click(function () {
                $('.ma1').show();
                $('.ma2').hide();
            })
            $('.zxx-btt').click(function(){
                $('.imselect-popup1').show();
                $('.bg').show();
            })
        })
    </script>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<div class="right-pos">
    <div class="ov-hi">
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
    <div class="ma1 fl" id="GDGContent">
        <div class="mt20 clearfix center fl">
            <select class="sel1 ml0" id="grade">
                <option>选择年级</option>
            </select>
            <script type="text/template" id="grade_temp">
                {{~it:value:index}}
                <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
                {{~}}
            </script>
            <select class="sel1 ml0 subject" id="subject">
            </select>
            <script type="text/template" id="subject_temp">
                {{~it:value:index}}
                {{? value.dan == 1}}
                <option value="{{=value.subid}}">{{=value.snm}}</option>
                {{?}}
                {{~}}
            </script>
            <button class="chat-bt btn fr">新建组合</button>
            <button class="chatt-bt btn fr mr20">计算冲突</button>
            <%--<button class="b8 fr mtt41 fr mr20" id="printGDG" style="">打印</button>--%>
        </div>
        <div class='center w1200 clearfix' id="none_png" hidden>
            <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
        </div>
        <div class="mt20 cur-tab cou-tab ccou-tab center fl" id="content" hidden>
            <table class="tabSty">
                <thead>
                <tr>
                    <th class="w200">组合名</th>
                    <th class="w200">学科</th>
                    <th class="w200">课时</th>
                    <th class="w400">行政班</th>
                    <th class="w200">操作</th>
                </tr>
                </thead>
            </table>
            <div class="ha410">
                <script type="text/template" id="tb_temp">
                    {{~it:value:index}}
                    <tr>
                        <td class="w198">
                            <em class="inline w198">
                                <input style="color: #666" readonly="readonly" class="innp1" value="{{=value.name}}({{=value.count}})">
                            </em>
                        </td>
                        <td class="w199">
                            <em class="inline w199">{{=value.subName}}</em>
                        </td>
                        <td class="w199">
                            <em class="inline w199">{{=value.jxbks}}<em style="margin-left: 15px;" class="bjks c6f" ks="{{=value.jxbks}}" ids="{{=value.id}}">编辑</em></em>
                        </td>
                        <td class="w398">
                            <em class="inline w398">
                                {{=value.clsName}}
                            </em>
                        </td>
                        <td class="w200">
                            <em class="inline w200 del">
                                <i class="ccur bj" ids="{{=value.id}}" nm="{{=value.name}}">编辑</i>
                                <i class="ccur manage" ids="{{=value.id}}" nm="{{=value.name}}"
                                   subId="{{=value.subId}}">管理</i>
                                <i class="ccur delZu" ids="{{=value.id}}">删除</i>
                            </em>
                        </td>
                    </tr>
                    {{~}}
                </script>
                <table class="tabSty" id="tb" style="table-layout:fixed">

                </table>
            </div>
        </div>
    </div>
    <div class="ma2">
        <div class="mt20 clearfix center fl">
            <a class="fl cursor c-b prev">&lt;返回&nbsp;<em id="zhm"></em></a>
            <button class="zx-bt btn fr ml25">+专项教学班</button>
            <button class="zxx-bt btn fr ml25 download">下载模板</button>
            <button class="zxx-btt btn fr">导入</button>
        </div>
        <div class="mt20 cur-tab cou-tab ccou-tab center fl">
            <table class="tabSty mt20">
                <thead>
                <tr>
                    <th class="w300">教学班名</th>
                    <th class="w200">组合名</th>
                    <th class="w200">学生</th>
                    <th class="w200">教室</th>
                    <th class="w200">教师</th>
                    <th class="w100">操作</th>
                </tr>
                </thead>
            </table>
            <div class="ha410">
                <script type="text/template" id="zh_temp">
                    {{~it:value:index}}
                    <tr>
                        <td>
                            <em class="inline w294">{{=value.name}}</em>
                        </td>
                        <td>
                            <em class="inline w197">{{=value.zh}}</em>
                        </td>
                        <td>
                            <em class="inline w197">
                                {{=value.size}}
                                <i class="ccur gl" ids="{{=value.id}}" nm="{{=value.name}}">选择</i>
                            </em>
                        </td>
                        <td>
                            <em class="inline w197">{{=value.room}}</em>
                        </td>
                        <td>
                            <em class="inline w197">{{=value.tea}}</em>
                        </td>
                        <td>
                            <em class="inline ww111 del">
                                <i class="ccur edt" tea="{{=value.teaId}}" ids="{{=value.id}}" room="{{=value.roomId}}"
                                   nm="{{=value.name}}">编辑</i>
                                <i class="ccur delR" ids="{{=value.id}}">删除</i>
                            </em>
                        </td>
                    </tr>
                    {{~}}
                </script>
                <table class="tabSty" id="zh">

                </table>
            </div>
        </div>
    </div>
</div>
<div class="bg"></div>
<div class="popup man-popup">
    <div class="popup-top">
        <em class="fl">新建组合:<em id="sunName"></em></em>
        <i class="close fr"></i>
    </div>
    <dl class="popup-dl">
        <dd>
            <span class="w3">组合名</span>
            <input class="inp1 inp-nnm" type="text" id="zhName">
        </dd>
        <dd>
            <span class="w3">学科</span>
            <select class="sel1 ml0 subject" id="subject1">
            </select>
            <span class="xRed">
                (*需勾选专项学科)
            </span>
        </dd>
        <dd class="poss">
            <span class="w3">班级</span>
            <input class="inp1 inp-nnm" type="text" disabled id="clasNameList">
            <button class="r15 xz">选择</button>
        </dd>
    </dl>
    <div class="popup-btn mt30">
        <button class="qd" id="addJxb">确定</button>
        <button class="qx">取消</button>
    </div>
</div>

<div class="popup xzz-popup">
    <div class="popup-top">
        <em>选择班级</em>
        <i class="close fr"></i>
    </div>
    <div class="mtr26">
        <table class="tabSty">
            <thead>
            <tr>
                <th width="220">班级</th>
                <th width="220">选择<input class="zzQ" type="checkbox" id="quan">全选</th>
            </tr>
            </thead>
        </table>
        <div class="ha210">
            <table class="tabSty" id="clsList">
                <tbody id="csList">

                </tbody>
                <script type="text/template" id="clList">
                    {{~it:value:index}}
                    <tr>
                        <td>
                            <em class="inline w217">{{=value.gname}}（{{=value.xh}}）</em>
                        </td>
                        <td>
                            <em class="inline w218">
                                <em ids="{{=value.classId}}" nm="{{=value.gname}}（{{=value.className}}）"
                                    class="fks rRight">
                                </em>
                            </em>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
    <div class="popup-btn mt30">
        <button class="qr">确认</button>
        <button class="qx">取消</button>
    </div>
</div>

<div class="popup mnew-popup">
    <div class="popup-top">
        <em class="fl">新建专项教学班</em>
        <i class="close fr"></i>
    </div>
    <dl class="popup-dl">
        <dd>
            <span class="w4">教学班名</span>
            <input class="inp1 inp-nnm" type="text" id="jbName">
        </dd>
        <dd>
            <span class="w4">教室</span>
            <select class="sel11 inp-nnm" type="text" id="room">
            </select>
            <script type="text/template" id="room_temp">
                {{~it:value:index}}
                <option value="{{=value.id}}">{{=value.name}}</option>
                {{~}}
            </script>
        </dd>
        <dd>
            <span class="w4">教师</span>
            <script type="text/template" id="teacher_temp">
                {{~it:value:index}}
                <option value="{{=value.id}}">{{=value.name}}</option>
                {{~}}
            </script>
            <select class="sel11 inp-nnm" type="text" id="teacher">

            </select>
        </dd>
    </dl>
    <div class="popup-btn mt30">
        <button class="qd" id="addZhuan" ids="*">确定</button>
        <button class="qx">取消</button>
    </div>
</div>

<div class="popup txz-popup">
    <div class="popup-top">
        <em class="isTj">添加学生:</em><em id="bnm"></em>
        <i class="fr close"></i>
    </div>
    <div class="clearfix">
        <script type="text/template" id="cs_temp">
            {{~it:value:index}}
            <li ids="{{=value.classId}}">{{=value.gname}}({{=value.className}})</li>
            {{~}}
        </script>
        <ul class="fl ss-li" id="cl">
        </ul>
        <div class="fl ss-dv">
            <div>
                层级：
                <select class="sel4" id="selLevel">
                    <option value="-1">全部</option>
                    <option value="0">无</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>

                </select>
                性别：
                <select class="sel4" id="selSex">
                    <option value="-1">全部</option>
                    <option value="1">男</option>
                    <option value="0">女</option>
                </select>
                学科组合：
                <select class="sel4" id="selCom">

                </select>
                <script type="text/template" id="content2_temp">
                    {{~it:value:index}}
                    <option value="{{=value.subName}}" subName="{{=value.name}}">{{=value.name}}</option>
                    {{~}}
                </script>
            </div>
            <table class="mt15">
                <thead>
                <tr>
                    <th width="100">姓名</th>
                    <th width="100">学号</th>
                    <th width="50">层级</th>
                    <th width="50">性别</th>
                    <th width="80">学科组合</th>
                    <th width="100">选择<input class="vt54" type="checkbox" id="stuCk"></th>
                </tr>
                </thead>
            </table>
            <div class="autoo h310 w503">
                <table class="zx">
                    <script type="text/template" id="studentList">
                        {{~it:value:index}}
                        <tr>
                            <td>
                                <em class="f12 w100 inline ln40">{{=value.userName}}</em>
                            </td>
                            <td>
                                <em class="f12 w100 inline whisp" title="{{=value.studyNumber}}">{{=value.studyNumber}}</em>
                            </td>
                            {{? value.level == 0}}
                            <td>
                                <em class="f12 w50 inline">无</em>
                            </td>
                            {{?}}
                            {{? value.level != 0}}
                            <td>
                                <em class="f12 w50 inline">{{=value.level}}</em>
                            </td>
                            {{?}}
                            <td>
                                <em class="f12 w50 inline">{{=value.sexStr}}</em>
                            </td>
                            <td>
                                <em class="f12 w79 inline">{{=value.combiname}}</em>
                            </td>
                            <td>
                                <em class="f12 w100 inline">
                                    <input type="checkbox" class="xuanrendea mt13" ids="{{=value.userId}}">
                                </em>
                            </td>
                        </tr>
                        {{~}}
                    </script>
                    <tbody id="stuList">

                </table>
            </div>
        </div>
    </div>
    <div class="popup-btn mt40">
        <button class="ss" id="quedingren" iz="0">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>
<div class="popup loading-p">
    加载中
    <img src="/static_new/images/new33/loading.gif">
</div>
<div class="jhh">
    <div class="jhh-top">数据处理中，请稍等！</div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp"></span>
</div>

<div class="popup imselect-popup1">
    <div class="popup-top">
        <em>导入选科结果</em>
        <i class="fr close"></i>
    </div>
    <div class="posc mt30">
        <div class="inline pos1">
            <input class="pos-inp" type="file" id="file" name="file">
            <button class="pos-btn btn">选择文件</button>
        </div>
        <span class="file-name"></span>
    </div>
    <div class="popup-btn mt10">
        <button class="qd" id="qddr">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('zhuanXiang', function (zhuanXiang) {
        zhuanXiang.init();
    });
</script>
</body>
</html>
