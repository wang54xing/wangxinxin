<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/3/28
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>分教学班</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <style>
        .pos {
             top: 0px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $('.dd-sty em').click(function () {
                $(this).addClass('cur').siblings().removeClass('cur')
            })
             $('.dd-sty em').click(function () {
                $(this).addClass('cur').siblings().removeClass('cur')
            })
            $('.d-ssty em').click(function () {
                $(this).toggleClass('cur')
            })
            $('.t-down').click(function () {
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top', -91)
            })
            $('.t-top').click(function () {
                $('.td-over').css('margin-top', 0)
            })
            $('.tea-bt').click(function () {
                $('.bg').show();
                $('.addjxb-popup1').show();
            })
            $('.close,.qxx').click(function () {
                $('.bg').hide();
                $('.addjxb-popup1').hide();
                $(".imjxb-popup1").hide();
                $(".drsb-popup1").hide();
            })
        })
        
       $(function(){
        	$(".right-pos a").eq(0).attr("class","va2");
        })
    </script>
    <style type="text/css">
    </style>
</head>
<body>
<div>
    <jsp:include page="head.jsp"></jsp:include>
    <span id = "defaultTerm" style="display: none"></span>
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
        <div class="ul-sty center mt25 fl">
            <ul class="bg-line">
                <li class="li-sty">
                    <span class="fl">年级：</span>
					<script type="text/template" id="grade_temp">
                        {{~it:value:index}}
                        <em ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</em>
                        {{~}}
                    </script>
                    <div class="d-sty dd-sty fl" id = "grade">
                     
                    </div>
                </li>
                <li class="li-sty">
                    <span class="fl">学科：</span>
					<script type="text/template" id="subject_temp">
                        {{~it:value:index}}
                        <em ids="{{=value.subid}}">{{=value.snm}}</em>
                        {{~}}
                    </script>
                    <div class="d-sty d-ssty fl" id = "subject">
                        
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="center w1300 clearfix">
        <div class="mt20 fl">
            <span class="f16 vt20" id = "count"></span>
        </div>
        <div class="mt20 fr mr100">
            <select class="sel15 mr30" type="text" id = "subType">
                <option value = "0">教学班类型</option>
                <option value = "1">等级</option>
                <option value = "2">合格</option>
                <option value = "3">行政</option>
                <option value = "4">专项</option>
            </select>

            <div class="fr">
                <button class="bj btn fr ml10 f14 auto-bt">编辑</button>
            </div>
            <button class="chat-bt btn fr ml10 f14 xj">新建教学班</button>
            <button class="chat-bt btn fr ml10 f14 pos">
                               自动创建
                <div class="pla-dv">仅支持走班学科教学班自动创建</div>
            </button>
			<button class="impor-bt btn fr f14 sc ml10" id="sc">导入教学班</button>
            <button class="mb20 load-bt1 f14 ml10 fr download">下载模板</button>
            <button class="chat-bt btn fr ml10 f14 posdd" id="cz">重建教学班
                <div class="pla-dv">新增学科生成行政型教学班</div>
            </button>
            <button class="chat-bt btn fr ml10 f14 posdd" id="tb" style="border: #1ab02f;background: #1ab02f!important;">同步学生
                <div class="pla-dv">新增学生添加至所有行政教学班</div>
            </button>
            <button class="chat-bt btn fr ml10 f14 posdd" id="tbks" style="border: #058296;background: #058296!important;">同步学科课时
                <div class="pla-dv">所有教学班课时与学科课时同步一致</div>
            </button>
        </div>
    </div>
    <div class='center w1200 clearfix' id="none_png" hidden>
        <img style="width:330px;margin: 30px auto" src="/static_new/images/new33/noneJ.png">
    </div>
    <div class="center w1300 clearfix" id="content">
        <div class="cur-tab cou-tab center fl">
            <table style="width: 1200px" class="votab clstab">
                <thead>
                <tr>
                    <th width="240">教学班</th>
                    <th width="240">自定义名称</th>
                    <th width="240">类型</th>
                    <th width="240">教学班容量</th>
                    <th width="240">教学班课时</th>
                    <th width="240">操作</th>
                </tr>
                </thead>
                <script type="text/template" id="jxbList">
                    {{~it:value:index}}
                    <tr ids = {{=value.id}} class = "jxbTr">
                        <td>{{=value.name}}</td>
						<td class="hov2">
							<span class="ml25">
								<input class="inp11 nnm" type="text" value="{{=value.nickName}}" disabled="disabled">
							</span>
                            <div class="none">该名称用于排课和课表页面显示</div>
						</td>
                        <td>
							{{? value.type==1}}
                                等级考
                        	{{?}}
							{{? value.type==2}}
                                合格考
                        	{{?}}
							{{? value.type==3}}
                                行政型
                        	{{?}}
							{{? value.type==4}}
                                专项型
                        	{{?}}
                            {{? value.type==6}}
                                行政型
                            {{?}}
						</td>
						<td>
							<span class="ml25">
								<input class="inp11 rl" type="text" value="{{=value.rl}}" disabled="disabled">
							</span>
						</td>
                        <td>
                            <span class="ml25">
								<input class="inp11 jxbks" type="text" value="{{=value.jxbks}}" disabled="disabled">
							</span>
                        </td>
                        <td><img class="opts imghided" src="/static_new/images/new33/dell.png" ids="{{=value.id}}">
                        </td>
                    </tr>
                    {{~}}
                </script>
                <tbody id = "jxb">
        
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="bg"></div>
<!--教室容量-->
<div class="popup addjxb-popup1">
	<div class="popup-top">
		<em>添加教学班</em>
		<i class="fr close"></i>
	</div>
	<dl class="popup-dl">
		<dd>
			<span>自定义名称：</span>
			<input class="inp1 inp-nnm" type="text" id = "inp-nnm">
		</dd>
		<dd>
			<span>教学班容量：</span>
			<input class="inp1 inp-rl" type="text" id = "inp-rl">
		</dd>
		<dd>
			<span>教学班类型：</span>
			<select class="sel5 mr30" type="text" id = "addSubType">
	            <option value = "1">等级</option>
	            <option value = "2">合格</option>
	            <option value = "3">行政</option>
	            <%--<option value = "4">专项</option>--%>
	        </select>
        </dd>
	</dl>
	<div class="popup-btn mt25">
		<button class="qd" id = "qd">确定</button>
		<button class="qxx">取消</button>
	</div>
</div>

<div class="popup imjxb-popup1">
    <div class="popup-top">
        <em>导入教学班</em>
        <i class="fr close"></i>
    </div>
    <div class="posc">
        <div class="inline pos1">
            <input class="pos-inp" type="file" id="file" name="file">
            <button class="pos-btn btn">选择文件</button>
        </div>
        <span class="file-name"></span>
    </div>
    <div class="popup-btn mt10">
        <button class="qd" id = "qddr">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>




<div class="popup drsb-popup1">
    <div class="popup-top">
        <em>导入失败学生信息</em>
        <i class="fr close sx"></i>
    </div>
    <div class="cou-tab mmt25 poss">
        <table class="pob">
            <thead>
                <tr>
                    <th width="171">学号</th>
                    <th width="120">学生姓名</th>
                    <th width="160">教学班名</th>
                    <th width="120">行政班名</th>
                    <th width="110">年级</th>
                    <th width="120">学科</th>
                    <th width="140">授课老师</th>
                    <th width="160">失败原因</th>
                    <%--<th width="80">教学班类型</th>--%>
                </tr>
            </thead>
        </table>
        <div class="w1126">
            <div class="w1106">
                <table>
                    <thead>
                        <tr>
                            <th width="170">学号</th>
                            <th width="120">学生姓名</th>
                            <th width="160">教学班名</th>
                            <th width="120">行政班名</th>
                            <th width="110">年级</th>
                            <th width="120">学科</th>
                            <th width="140">授课老师</th>
                            <th width="160">失败原因</th>
                            <%--<th width="80">教学班类型</th>--%>
                        </tr>
                    </thead>

                    <script type="text/template" id="errorList">
                        {{~it:value:index}}
                            <tr>
                                <td>{{=value.stuNum}}</td>
                                <td>{{=value.stuName}}</td>
                                <td>{{=value.jxbName}}</td>
                                <td>{{=value.className}}</td>
                                <td>{{=value.grade}}</td>
                                <td>{{=value.subject}}</td>
                                <td>{{=value.teaName}}</td>
                                <td>{{=value.errorMessage}}</td>
                                <%--<td>{{=value.}}</td>--%>
                            </tr>
                        {{~}}
                    </script>
                    <tbody id="stuErrorList">

                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>
<%--<div class="load">
	<span style="font-size: 18px;margin-left: -60px">正在导入，请稍后...</span>
	<img src="/static_new/images/new33/loading.gif">
</div>--%>
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
    seajs.use('placementSet', function (placementSet) {
        placementSet.init();
    });
</script>
