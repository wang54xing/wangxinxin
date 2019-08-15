<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>关联教学班</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <link rel="stylesheet" href="/static_new/css/new33/reset1.css">
    <%--<link rel="stylesheet" href="/static_new/css/new33/reset1.css">--%>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $('.pbUl li').click(function () {
                $(this).find('.ti').toggleClass('none')
            })
            $('.close,.qx').click(function () {
                $('.bg').hide();
                $('.pb-popup').hide();
                $('.xz-popup').hide();
                $('.sz-popup').hide();
                $('.szz-popup').hide();
            })
//            $(".tabSty input[type='checkbox']").click(function() {
//                var flag;
//                var tr = $(this).parent().parent();  　　　// 当前行
//                var trIndex = tr.index();    　　　　　 // 当前行索引
//                var firstTD = tr.children().eq(0);     // 当前行第一个td
//                var twoTD = tr.children().eq(1);        // 当前行第二个td
//
//                var firstTDText = firstTD.text(); // 获取此行第一列内容
//                var rowspan = firstTD.attr("rowspan");
//                var twoTDText = twoTD.text(); // 获取此行第二列内容
//                var rowspan = firstTD.attr("rowspan");
//                // 判断当前行第一列是否包含 rowspan 属性
//                if ( typeof(firstTD.attr("rowspan")) != "undefined" && typeof(twoTD.attr("rowspan")) == "undefined") {
//                    if (rowspan != 1) {
//                        // 将td插入到下一行
//                        tr.next().children().eq(0).before("<td rowspan="+(rowspan-1)+">"+firstTDText+"</td>");
//                    }
//                } else if(typeof(twoTD.attr("rowspan")) != "undefined" && typeof(firstTD.attr("rowspan")) == "undefined" ){
//                    if (rowspan != 1) {
//                        // 将td插入到下一行
//                        tr.next().children().eq(1).before("<td rowspan="+(rowspan-1)+">"+twoTDText+"</td>");
//                    }
//                } else if(typeof(firstTD.attr("rowspan")) != "undefined" && typeof(twoTD.attr("rowspan")) != "undefined"){
//                    if (rowspan != 1) {
//                        // 将td插入到下一行
//                        tr.next().children().eq(0).before("<td rowspan="+(rowspan-1)+">"+firstTDText+"</td>");
//                        tr.next().children().eq(1).before("<td rowspan="+(rowspan-1)+">"+twoTDText+"</td>");
//
//                    }
//                }
//                else {
//
//                    // 带有 rowspan 属性的行的索引
//                    var rowspanIndex = 0;
//
//                    $(".tabSty tr td[rowspan]").each(function(i) {
//
//                        var rowspan = $(".tabSty tr td[rowspan]").eq(i).attr("rowspan");
//                        if (parseInt(rowspan) >= trIndex) return false;
//                        trIndex = trIndex - parseInt(rowspan);
//                        rowspanIndex += 1;
//                    });
//
//                    var rowspan = $(".tabSty tr td[rowspan]").eq(rowspanIndex).attr("rowspan");
//                    $(".tabSty tr td[rowspan]").eq(rowspanIndex).attr("rowspan",rowspan-1);
//                }
//                tr.hide();
//            })
        })
    </script>
</head>
<body>
<div>
    <jsp:include page="head.jsp"></jsp:include>
    <span id="defaultTerm" style="display: none"></span>
    <div class="right-pos">
        <div class="ov-hi">
            <div class="td-over">
                <a href="/newisolatepage/placementSet.do" class="va1">分教学班</a>
                <a href="/newisolatepage/stuSign.do" class="vb1">标记学生</a>
                <a href="/newisolatepage/jxbRelation.do" class="vc2">关联教学班</a>
                <%--<a href="/newisolatepage/stuRelation.do" class="vc1">关联学生</a>--%>
                <%--<a href="/newisolatepage/fenbanInfoSet.do" class="vd1">信息设置</a>--%>
                <a href="/newisolatepage/conflict.do" class="ve1">冲突检测</a>
                <%--<a href="/newisolatepage/danShuangZhou.do" class="vz1">单双周课</a>--%>
            </div>
        </div>
    </div>
    <div class="center w1300 clearfix">
        <div class="mt20 cur-tab cou-tab center fl">
		<span>
			<select class="sel1 ml10 jxbType" id = "type">
                <option value="0">等级</option>
                <option value="1">合格</option>
            </select>
		</span>
            <select id="grade" class="sel1 ml10">

            </select>
            <script type="text/template" id="grade_temp">
                {{~it:value:index}}
                <option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
                {{~}}
            </script>
            <div class="fr yc">
                <input type="checkbox" class="vt15 hide" id="isHide">完成隐藏
                <button class="clear clearbtn fr mr20" id="clear" style="background: #1ab02f">清空教学班</button>
                <button hidden class="chatt-bt btn fr mr20">计算冲突</button>
                <button class="btn b1 ml10" style="border: 1px solid #f77737;" id="shiFang">拼班释放</button>
                <button class="btn b2" id="pinban" style="border: none;">拼班</button>
                <button class="btn b3" id="auto" style="width:102px;">自动拼班</button>
                <button class="btn b3" id="autojxb" style="width:102px;padding: 0px">自动设置教学班</button>
            </div>
        </div>
    </div>
    <div class="center w1300 clearfix">
        <div class="mt20 cur-tab cou-tab center fl mb35">
            <div class='center w1200 clearfix' id="none_png" hidden>
                <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
            </div>
            <table class="tabSty"  id="content" hidden>
                <thead>
                <tr>
                    <th width="180">标签名</th>
                    <th width="200">选科组合</th>
                    <%--<th width="100">组合名</th>--%>
                    <th width="180">学生来源</th>
                    <th width="100">选科名</th>
                    <th width="110">学科人数</th>
                    <th width="280">教学班</th>
                    <%--<th width="200" hidden>教室</th>--%>
                    <%--<th width="120" hidden>教师</th>--%>
                    <th width="120">完成</th>
                </tr>
                </thead>
            </table>
            <div class="hha200">
                <table>
                <tbody id="jxbRelation">
                <%--<tr>
                    <td rowspan="3" class="eq">物化生</td>
                    <td rowspan="3" class="eq1">高一（1）/32人</td>
                    <td>物理</td>
                    <td>57</td>
                    <td>物理D1<em class="xzf c6f ptr">选择</em></td>
                    <td>高一（2）<em class="sz c6f ptr">设置</em></td>
                    <td>张三丰<em class="szz c6f ptr">设置</em></td>
                    <td>
                        <input class="rr" type="checkbox">
                    </td>
                </tr>
                <tr>
                    <td>物理</td>
                    <td>57</td>
                    <td>物理D1<em class="xzf c6f ptr">选择</em></td>
                    <td>高一（2）<em class="sz c6f ptr">设置</em></td>
                    <td>张三丰<em class="szz c6f ptr">设置</em></td>
                    <td>
                        <input class="rr" type="checkbox">
                    </td>
                </tr>--%>

                </tbody>
                <script type="text/template" id="jxbRelationList">
                    {{~it:value:index}}
                    {{~value.subjectList:subject:it}}
                    {{? it == 0}}
                    <tr>
                        <td width="180" class="eq" rowspan="{{=value.size}}" ids="{{=value.id}}">
                            {{~value.bqName:tag:itt}}
                            {{=tag}}</br>
                            {{~}}
                        </td>
                        <td width="200" class="eq1" rowspan="{{=value.size}}" ids="{{=value.id}}">
                            {{~value.tagName:tag:itt}}
                            {{=tag.name}}（{{=tag.count}}）</br>
                            {{~}}
                        </td>
                        <%--<td width="100" rowspan="{{=value.size}}">--%>
                            <%--{{=value.combineName}}--%>
                        <%--</td>--%>
                        <td width="180" rowspan="{{=value.size}}">
                            {{~value.stuSource:stuSource:itt}}
                            {{=stuSource.sourceClass}}（{{=stuSource.sourceStuCount}}人）</br>
                            {{~}}
                        </td>
                        <td width="100" class="hcur">{{=subject.subName}}</td>
                        <td width="110">{{=subject.stuCount}}</td>


                        {{?subject.jxbId != ""}}
                        <td width="280" class="bcur" jxbId = "{{=subject.jxbId}}">{{=subject.jxbName}}（{{=subject.jxbStuCount}}）<em class="xzf c6f ptr" subId = "{{=subject.subjectId}}" jxbId = "{{=subject.jxbId}}" ids = "{{=value.id}}">&nbsp;&nbsp;选择</em><em class="qk c6f ptr" subId = "{{=subject.subjectId}}" jxbId = "{{=subject.jxbId}}" ids = "{{=value.id}}">&nbsp;&nbsp;清空</em></td>
                        {{?}}
                        {{?subject.jxbId == ""}}
                        <td width="280" class="bcur" jxbId = "{{=subject.jxbId}}">{{=subject.jxbName}}<em class="xzf c6f ptr" subId = "{{=subject.subjectId}}" jxbId = "{{=subject.jxbId}}" ids = "{{=value.id}}">&nbsp;&nbsp;选择</em><em class="qk c6f ptr" subId = "{{=subject.subjectId}}" jxbId = "{{=subject.jxbId}}" ids = "{{=value.id}}">&nbsp;&nbsp;清空</em></td>
                        {{?}}

                        <%--<td hidden>--%>
                            <%--{{=subject.classRoom}}<em class="sz c6f ptr classRoom" jxbId = "{{=subject.jxbId}}" roomId = "{{=subject.classRoomId}}">&nbsp;&nbsp;设置</em>--%>
                        <%--</td>--%>
                        <%--<td hidden>--%>
                            <%--{{=subject.teaName}}<em class="szz c6f ptr teaName" jxbId = "{{=subject.jxbId}}" subId = "{{=subject.subjectId}}" tid = "{{=subject.teaId}}">&nbsp;&nbsp;设置</em>--%>
                        <%--</td>--%>
                        {{? subject.isFinish == 0}}
                        <td width="120">
                            <input class="rr finish" type="checkbox" ids="{{=value.id}}" subId = "{{=subject.subjectId}}">
                        </td>
                        {{?}}
                        {{? subject.isFinish == 1}}
                        <td width="120">
                            <input class="rr finish" type="checkbox" checked = "checked" ids="{{=value.id}}" subId = "{{=subject.subjectId}}">
                        </td>
                        {{?}}
                    </tr>
                    {{?}}
                    {{? it != 0}}
                    <tr>
                        <td width="100" class="hcur">{{=subject.subName}}</td>
                        <td width="110">{{=subject.stuCount}}</td>
                        {{?subject.jxbId != ""}}
                        <td width="280" class="bcur" jxbId = "{{=subject.jxbId}}">{{=subject.jxbName}}（{{=subject.jxbStuCount}}）<em class="xzf c6f ptr" subId = "{{=subject.subjectId}}" jxbId = "{{=subject.jxbId}}" ids = "{{=value.id}}">&nbsp;&nbsp;选择</em><em class="qk c6f ptr" subId = "{{=subject.subjectId}}" jxbId = "{{=subject.jxbId}}" ids = "{{=value.id}}">&nbsp;&nbsp;清空</em></td>
                        {{?}}
                        {{?subject.jxbId == ""}}
                        <td width="280" class="bcur" jxbId = "{{=subject.jxbId}}">{{=subject.jxbName}}<em class="xzf c6f ptr" subId = "{{=subject.subjectId}}" jxbId = "{{=subject.jxbId}}" ids = "{{=value.id}}">&nbsp;&nbsp;选择</em><em class="qk c6f ptr" subId = "{{=subject.subjectId}}" jxbId = "{{=subject.jxbId}}" ids = "{{=value.id}}">&nbsp;&nbsp;清空</em></td>
                        {{?}}
                        <%--<td hidden>--%>
                            <%--{{=subject.classRoom}}<em class="sz c6f ptr classRoom" jxbId = "{{=subject.jxbId}}" roomId = "{{=subject.classRoomId}}">&nbsp;&nbsp;设置</em>--%>
                        <%--</td>--%>
                        <%--<td hidden>--%>
                            <%--{{=subject.teaName}}<em class="szz c6f ptr teaName" jxbId = "{{=subject.jxbId}}" subId = "{{=subject.subjectId}}" tid = "{{=subject.teaId}}">&nbsp;&nbsp;设置</em>--%>
                        <%--</td>--%>
                        {{? subject.isFinish == 0}}
                        <td width="120">
                            <input class="rr finish" type="checkbox" ids="{{=value.id}}" subId = "{{=subject.subjectId}}">
                        </td>
                        {{?}}
                        {{? subject.isFinish == 1}}
                        <td width="120">
                            <input class="rr finish" type="checkbox" checked = "checked" ids="{{=value.id}}" subId = "{{=subject.subjectId}}">
                        </td>
                        {{?}}
                    </tr>
                    {{?}}
                    {{~}}
                    {{~}}
                </script>
            </table>
            </div>
        </div>
    </div>
    <div class="bg"></div>
    <div class="popup pb-popup">
        <div class="popup-top">
            <em class="fl">拼班</em>
            <i class="fr close"></i>
        </div>
        <div class="pbUl clearfix">
            <ul id="xuniban">
            </ul>
            <script type="text/template" id="xunibanList">
                {{~it:value:index}}
                    <li ids = {{=value.id}}>
                        <span class="">{{=value.bqName}}</span>
                        <span class="c25">{{=value.name}}（{{=value.stuCount}}）</span>
                        <i isBigger="{{=value.isBigger}}" class="ti"></i>
                        <i id="tuijian"></i>
                    </li>
                {{~}}
            </script>
        </div>
        <div class="popup-btn mt20 mb35">
            <button class="qd" id="qdpuzzle">确定</button>
            <button class="qx">取消</button>
        </div>
    </div>
    <div class="popup xz-popup">
        <div class="popup-top">
            <em>选择教学班</em>
            <i class="fr close"></i>
        </div>
        <div class="setp-d">
            <select class="selt" id="jxb">
            </select>
            <script type="text/template" id="jxb_temp">
                {{~it:value:index}}
                <option value="{{=value.id}}">{{=value.name}}({{=value.studentCount}})</option>
                {{~}}
            </script>

        </div>
        <div class="popup-btn">
            <button class="qd" id="qdjxb">确定</button>
            <button class="qx">取消</button>
        </div>
    </div>
    <div class="popup sz-popup">
        <div class="popup-top">
            <em>选择教室</em>
            <i class="fr close"></i>
        </div>
        <div class="setp-d">
            <select class="selt" id="room">
                <option></option>
            </select>
            <script type="text/template" id="room_temp">
                {{~it:value:index}}
                <option value="{{=value.id}}">{{=value.name}}</option>
                {{~}}
            </script>
        </div>
        <div class="popup-btn">
            <button class="qd" id = "qdclass">确定</button>
            <button class="qx">取消</button>
        </div>
    </div>
    <div class="popup szz-popup">
        <div class="popup-top">
            <em>选择教师</em>
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
            <button class="qd" id="qdtea">确定</button>
            <button class="qx">取消</button>
        </div>
    </div>
</div>

<div class="jhh">
    <div class="jhh-top">数据处理中，请稍等！</div>
    <img class="jhhIm" src="/static_new/images/new33/loading1.gif">
    <span class="jhhSp"></span>
</div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('jxbRelation', function (jxbRelation) {
        jxbRelation.init();
    });
</script>
</html>
