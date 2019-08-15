<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/3/29
  Time: 9:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>标记学生</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $('body').on('click','.qqx,.wdd-popup .close',function(){
                $('.wdd-popup').hide();
                $('.wd-popup').show();
            })
            $('.text span').click(function () {
                $(this).toggleClass('sign-spp')
            })
            $('.qxx,.close,#queque').click(function () {
                $('.bg').hide();
                $('.addb-popup').hide();
                $('.wd-popup').hide();
            })
            $('.t-down').click(function () {
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top', -91)
            })
            $('.t-top').click(function () {
                $('.td-over').css('margin-top', 0)
            })
            $('.rrow').click(function () {
                $(this).parent().hide();
            })

        })
        $(function () {
            $(".right-pos a").eq(1).attr("class", "vb2");
        })
    </script>
    <style type="text/css">
        .cou-tab tr .sing-td {
            display: inline-block;
            width: 100px;
            height: 40px;
            line-height: 40px;
            border: 1px dashed #6f69d6 !important;
            border-top: none !important;
        }
    </style>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<span id="defaultTerm" style="display: none"></span>

<div class="right-pos">
    <div class="ov-hi">
        <div class="td-over">
            <a href="/newisolatepage/placementSet.do" class="va1">分教学班</a>
            <a href="/newisolatepage/stuSign.do" class="vb1">标记学生</a>
            <a href="/newisolatepage/jxbRelation.do" class="vc1">关联教学班</a>
            <%--<a href="/newisolatepage/stuRelation.do" class="vc1">关联学生</a>--%>
            <%--<a href="/newisolatepage/fenbanInfoSet.do" class="vd1">信息设置</a>--%>
            <a href="/newisolatepage/conflict.do" class="ve1">冲突检测</a>
            <%--<a href="/newisolatepage/danShuangZhou.do" class="vz1">单双周课</a>--%>
        </div>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="optcont sign mt20">
        <script type="text/template" id="grade_temp">
            {{~it:value:index}}
            <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
            {{~}}
        </script>
        <script type="text/template" id="content2_temp">
            {{~it:value:index}}
            <option value="{{=value.name}}" subName="{{=value.subName}}">{{=value.name}}</option>
            {{~}}
        </script>
        <select id="grade">
        </select>
        <select id="ZuHeType" style="display: none;">
            <option value="3">三科</option>
            <option value="2">双科</option>
            <%--<option value="1">单科</option>--%>
        </select>
        <select id="subList">
        </select>
        <select id="ceng">
            <option value="-1">全部</option>
            <option value="0">无</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
        </select>
        <select id="duanCiSel">
        </select>

        <div class="fr inline mr125">
            <label class="ml10 mr20">
                <input class="vtl13 fei0" type="checkbox" checked>组合人数非0
            </label>
            <%--<label class="">--%>
                <%--<input class="vtl13 weibiao" type="checkbox">未标记学生--%>
            <%--</label>--%>
            <%--<label class="ml10 mr20">--%>
                <%--<input class="vtl13 biaole" type="checkbox">已标记学生--%>
            <%--</label>--%>
            <button class="zd-bt btn fr f16 ml10" id="zdbq">自动标签</button>
            <button class="zd-bt btn fr f16 ml10" id="qkbq">清除标签</button>
            <button class="bt-hh auto-btt btn fr f16 ml20">创建标签</button>
        </div>
    </div>
    <div class="center w1300 clearfix">
        <div class="mt20 cur-tab cou-tab couu-tab center fl w1185">
            <div class='center w1200 clearfix' id="none_png" hidden>
                <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
            </div>
            <table class="fl tab-jsks votab clstab" id="content" hidden>
                <thead>
                <tr>
                    <th width="130">行政班</th>
                    <th width="100">班级人数</th>
                    <th width="130">组合</th>
                    <th width="100">层级</th>
                    <th width="100">组合人数</th>
                    <th width="100">已标人数</th>
                    <th width="100">未标人数</th>
                    <th width="405">标签</th>
                </tr>
                </thead>
            </table>
            <div class="stuTab">
            <table class="fl tab-jsks votab clstab" width="1174">
                <script type="text/template" id="studentListDing">
                    {{~it:value:index}}
                    {{~value.subjectNameList:obj:it}}
                    {{? it == 0}}
                    <tr>
                        <td width="130" rowspan="{{=value.size}}">{{=value.grade}}（{{=value.xh}}）班</td>
                        <td width="100" rowspan="{{=value.size}}">{{=value.classCount}}</td>
                        <td width="130">{{=obj.subName}}</td>
                        {{? obj.lev == 0}}
                        <td width="100" class="tt4"></td>
                        {{?}}
                        {{? obj.lev != 0}}
                        <td width="100" class="tt4">{{=obj.lev}}</td>
                        {{?}}
                        <td width="100">
                            {{=obj.subjectStuCount}}
                        </td>
                        <td width="100">
                            {{=obj.subjectStuBiaoCount}}
                        </td>
                        <td width="100" class="sing-td cursor lastTr" cid="{{=value.classId}}" cnm="{{=value.name}}">
                            {{=obj.subjectWeiBiaoCount}}
                        </td>
                        <td class="text tt6"  width="405">
                            {{~obj.tagList:wt:itt}}
							<span class="sign-sp wd" name="{{=wt.name}}" ids="{{=wt.id}}">
								<em>{{=wt.name}}</em>
								<i class="rrow cursor" ids="{{=wt.id}}"></i>
							</span>
                            {{~}}
                        </td>
                        <td class="userLit" hidden width="0">
                            {{~obj.student:wt:itt}}
                            <span ids="{{=wt.id}}" bj="{{=obj.bj}}"></span>
                            {{~}}
                        </td>
                    </tr>
                    {{?}}
                    {{? it != 0}}
                    <tr>
                        <td width="130">{{=obj.subName}}</td>
                        {{? obj.lev == 0}}
                        <td width="100" class="tt4"></td>
                        {{?}}
                        {{? obj.lev != 0}}
                        <td width="100" class="tt4">{{=obj.lev}}</td>
                        {{?}}
                        <td width="100">
                            {{=obj.subjectStuCount}}
                        </td>
                        <td width="100">
                            {{=obj.subjectStuBiaoCount}}
                        </td>
                        <td width="100" class="sing-td cursor lastTr" cid="{{=value.classId}}" cnm="{{=value.name}}">
                            {{=obj.subjectWeiBiaoCount}}
                        </td>
                        <td width="405" class="text tt6">
                            {{~obj.tagList:wt:itt}}
							<span class="sign-sp wd"  name="{{=wt.name}}"ids="{{=wt.id}}">
								<em>{{=wt.name}}</em>
								<i class="rrow cursor" ids="{{=wt.id}}"></i>
							</span>
                            {{~}}
                        </td>
                        <td class="userLit" hidden width="0">
                            {{~obj.student:wt:itt}}
                            <span ids="{{=wt.id}}" bj="{{=obj.bj}}"></span>
                            {{~}}
                        </td>
                    </tr>
                    {{?}}
                    {{~}}
                    {{~}}
                </script>

                <script type="text/template" id="studentList">
                    {{~it:value:index}}
                    <tr>
                        <td width="130">{{=value.grade}}（{{=value.xh}}）班</td>
                        <td width="100">{{=value.classCount}}</td>
                        <td width="130">{{=value.subjectName}}</td>
                        {{? value.lev == 0}}
                        <td width="100" class="tt4"></td>
                        {{?}}
                        {{? value.lev != 0}}
                        <td width="100" class="tt4">{{=value.lev}}</td>
                        {{?}}
                        <td width="100">
                            {{=value.subjectStuCount}}
                        </td>
                        <td width="100">
                            {{=value.subjectStuBiaoCount}}
                        </td>
                        <td width="100" class="sing-td cursor lastTr" cid="{{=value.classId}}" cnm="{{=value.name}}">
                            {{=value.subjectWeiBiaoCount}}
                        </td>
                        <td width="405" class="text tt6">
                            {{~value.tagList:obj:itt}}
							<span class="sign-sp wd" name="{{=obj.name}}" ids="{{=obj.id}}">
								<em>{{=obj.name}}</em>
								<i class="rrow cursor" ids="{{=obj.id}}"></i>
							</span>
                            {{~}}
                        </td>
                        <td class="userLit" hidden width="0">
                            {{~value.student:obj:itt}}
                            <span ids="{{=obj.id}}" bj="{{=obj.bj}}"></span>
                            {{~}}
                        </td>
                    </tr>
                    {{~}}
                </script>
                <tbody id="tbd">

                </tbody>
            </table>
            </div>
        </div>
    </div>
</div>
<div class="bg"></div>

<!--创建标签-->
<div class="popup addb-popup">
    <div class="popup-top">
        <em>创建标签</em>
        <i class="fr close"></i>
    </div>
    <dl class="g-dl">
        <dd class="mt25">
            <span class="mr15">标签名称</span>
            <input class="inp8 inp-v" type="text">
        </dd>
    </dl>
    <div class="popup-btn mt25">
        <button class="ss">添加</button>
        <button class="qxx">取消</button>
    </div>
</div>


<div class="popup wd-popup">
    <div class="popup-top">
        <em id="tagName"></em>
        <i class="fr close"></i>
    </div>
    <div class="mt10">
        <button class="xzBtn">新增</button>
    </div>
    <div class="mmr30">
        <table class="cou-tab">
            <thead>
            <tr>
                <th width="110">姓名</th>
                <th width="180">学籍号</th>
                <th width="50">性别</th>
                <th width="130">行政班</th>
                <th width="90">选科组合</th>
                <th width="45">层级</th>
                <th width="45">释放</th>
            </tr>
            </thead>
        </table>
        <div class="w675 ha290">
            <table class="cou-tab">
                <script type="text/template" id="st_temp">
                    {{~it:value:index}}
                    <tr>
                        <td width="110">{{=value.name}}</td>
                        <td width="180"><em class="whisp w180" title="{{=value.sn}}">{{=value.sn}}</em></td>
                        <td width="50">{{=value.sex}}</td>
                        <td width="130">{{=value.clsName}}</td>
                        <td width="90">{{=value.com}}</td>
                        <td width="45">{{=value.lev}}</td>
                        <td width="45">
                            <input class="stuList" type="checkbox" checked ids="{{=value.id}}" tgid="{{=value.tgid}}">
                        </td>
                    </tr>
                    {{~}}
                </script>
                <tbody id="stList">

                </tbody>
            </table>
        </div>
    </div>
    <div class="popup-btn mt25 mb35">
        <button class="sss" id="queque">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>


<div class="popup wdd-popup">
    <div class="popup-top">
        <em id="">新增</em>
        <i class="fr close"></i>
    </div>
    <div class="mmr30 w705">
        <table class="cou-tab">
            <thead>
            <tr>
                <th width="110">姓名</th>
                <th width="180">学籍号</th>
                <th width="50">层级</th>
                <th width="50">性别</th>
                <th width="130">行政班</th>
                <th width="90">选科组合</th>
                <th width="90">操作</th>
            </tr>
            </thead>
        </table>
        <div class="w705 ha290">
            <table class="cou-tab">
                <tbody id="adTagStu">

                </tbody>
                <script type="text/template" id="addStuTag">
                    {{~it:value:index}}
                    <tr>
                        <td width="110">{{=value.name}}</td>
                        <td width="180">{{=value.sn}}</td>
                        <td width="50">{{=value.lev}}</td>
                        <td width="50">{{=value.sex}}</td>
                        <td width="130">{{=value.clsName}}</td>
                        <td width="90">{{=value.com}}</td>
                        <td width="90">
                            <input class="addSt" type="checkbox"  ids="{{=value.id}}">
                        </td>
                    </tr>
                    {{~}}
                </script>
            </table>
        </div>
    </div>
    <div class="popup-btn mt25 mb35">
        <button class="addStuIsTag ss">确定</button>
        <button class="qqx">取消</button>
    </div>
</div>
<div class="jhh">
    <div class="jhh-top">数据处理中，请稍等！</div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp"></span>
</div>
</body>
</html>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('stuSign', function (stuSign) {
        stuSign.init();
    });
</script>
