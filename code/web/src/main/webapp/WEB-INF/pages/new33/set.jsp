<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>设置</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
	<link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
	<link rel="stylesheet" href="/static_new/css/rome.css">
    <link rel="stylesheet" href="/static/css/zTreeStyle/zTreeStyle.css">
    <link rel="stylesheet" href="/static_new/css/new33/course.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/rome.js"></script>

    <script type="text/javascript" src="/static/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="/static/js/jquery.listnav.min-2.1.js"></script>
    <script type="text/javascript" src="/static/js/jquery.charfirst.pinyin.js"></script>

    <script type="text/javascript">
    	$(function(){
            rome(time1);
            rome(time2);
    		$('.set-ul li').click(function(){
    			$(this).addClass('ss-cur').siblings().removeClass('ss-cur');
    			var tt = $(this).attr('id');
    			$("#" + "com-" + tt).show().siblings().hide();
    		})
    		$('.swlx').click(function(){
    			$('.bg').show();
    			$('.sw-popup').show();
    		})
    		$('.gly').click(function(){
    			$('.bg').show();
    			$('.ssw-popup').show();
    		})
            $(".newpk").click(function(){
                $("#description").val("");

                $("#grade input").each(function () {
                    $(this).prop("checked",false);
                });
                $("#savePaikeci").attr("pid","");
                $('.bg').show();
                $('.newpk-popup').show();
            })
            $('.chat-bt').click(function(){
                $('.bg').show();
                $('.xj-popup').show();
            })

    		$('.qx,.close').click(function(){
    			$('.bg').hide();
    			$('.sw-popup').hide();
    			$('.ssw-popup').hide();
    			$('.addn-popup').hide();
                $('.newpk-popup').hide();
                $('.xj-popup').hide();
                $('.tbb-popup').hide();
    		})



            //排课规则
            $('.gz_list li').click(function () {
                var index=$(this).index()+1;
                $('.gz_ct_'+index).show().siblings('.gz_ct').hide();
                $(this).addClass('active').siblings().removeClass('active')
            })

            $(document).on('click','.ck_xk li',function () {
                $(this).addClass('active').siblings().removeClass('active')
                console.log(1)
            })
            $(document).on('click','.xk-list .ck',function (e) {
                e.stopPropagation();
                console.log(123)
                $(this).toggleClass('active')
            })


            //同班连课
            $('.tblk').click(function () {
                if($(this).hasClass('active')){
                    $('.lk').hide()
                }else{
                    $('.lk').show()
                }
            })

            //学科全选和取消
            $('.ck_xk_all').click(function () {
                var tp=1;
                if($(this).text()=='取消'){
                    $(this).text('全选');
                    tp=2;
                }else{
                    $(this).text('取消');
                    tp=1;
                }
                $.each($('.ck_xk .ck'),function (i,item) {
                    if(tp==1){
                        $(item).addClass('active')
                    }else{
                        $(item).removeClass('active')
                    }
                })
            })

            //新建互斥组--选择教师

            $('#new_hc').click(function () {
                $('.hc-popup').show();
                $('.bg').show();
            })

            $('#myList').listnav({
                includeOther: true,
                noMatchText: '',
                prefixes: ['the', 'a']
            });
            $("#myList-nav").append(" <div class='demo-search'> <input type='text'id='name'> <button id='sou'>搜索</button> </div>")
            var zNodes = [
                {name:"高一", open:true, children:[
                        {name:"语文"}, {name:"数学"}]},
            ];
            var setting = {};
            $.fn.zTree.init($("#treeDemo"),setting, zNodes);

            var num=0;
            $("body").on("click", "#myList li", function () {
                if ($(this).find("em").hasClass("spe-cur")) {
                    $(this).find('em').removeClass('spe-cur');
                    num--;
                }else{
                    if(num<2){
                        $(this).find('em').addClass('spe-cur');
                        num++;
                    }
                }
                $('#hcTeaherNum').text(num);
            })

    	})
    </script>
    
    <style>
    .imghides,.del{
        display: inline-block;
        cursor: pointer;
    }
    </style>
</head>
<body>
<div>

	<jsp:include page="head.jsp"></jsp:include>
	<span id = "defaultTerm" style="display: none;"></span>
	<div class="center w1300 clearfix">
		<div class="mt35 center fl">
			<ul class="set-ul c-b">
				<c:if test="${roles:isManager(sessionValue.userRole) ||roles:isHeadmaster(sessionValue.userRole)}">
                <li class="f16 ss-cur" id="qx">权限</li>
				</c:if>
				<li class="f16" id="sw">事务类型</li>
                <li class="f16" id="gl">学期管理</li>
                <li class="f16" id="pk">排课学期</li>
                <li class="f16" id="sj">排课需求</li>
				<li class="f16" id="sz">排课设置</li>
				<!-- <li class="f16" id="gz">排课规则</li> -->
			</ul>
		</div>
	</div>
	<div class="center w1300 comm">
		<!--事务-->
		<div id="com-sw" class="mt20 cou-tab center clearfix fl">
			<div class="center fl">
				<button class="add-bt btn fr f16 swlx">+事务类型</button>
			</div>
			<table class="wauto">
				<thead>
					<tr>
						<th class="w260">事务类型</th>
						<th class="w260">操作</th>
					</tr>
				</thead>
				<script type="text/template" id="SWLXList">
                    {{~it:value:index}}
                    <tr>
                        <td><em class = "w260">{{=value.desc}}</em></td>
                        <td><img class="opt imghides" src="/static_new/images/new33/edit.png" type="{{=value.desc}}" ids="{{=value.id}}"><img class="opts imghides" src="/static_new/images/new33/dell.png" ids="{{=value.id}}">
                        </td>
                    </tr>
                    {{~}}
                </script>
				<tbody id = "SWList">
					
				</tbody>
			</table>
		</div>
		<!--权限-->
		<div id="com-qx" class="mt20 cou-tab center clearfix fl">
			<div class="center clearfix">
				<button class="add-bt btn fr f16 gly">+管理员</button>
			</div>
			<table class="mt25">
				<thead>
					<tr>
						<th class="w399">管理员</th>
						<th class="w399">联系方式</th>
						<th class="w399">操作</th>
					</tr>
				</thead>
				<script type="text/template" id="manageList">
                    {{~it:value:index}}
                    <tr>
                        <td><em class = "w260">{{=value.userName}}</em></td>
						<td>{{=value.contactWay}}</td>
                        <td><img class="opts imghides" id = "opts" src="/static_new/images/new33/dell.png" ids="{{=value.id}}">
                        </td>
                    </tr>
                    {{~}}
                </script>
				<tbody id = "maList">
					
				</tbody>
			</table>
		</div>
        <!--学期管理-->
        <%--<div id="com-gl" class="mt20 cou-tab center clearfix fl">
            <ul>
                <li>
                    <span>学年</span>
                    <select id="start_year">
                        <option value="2013">2013</option>
                        <option value="2014">2014</option>
                        <option value="2015">2015</option>
                        <option value="2016">2016</option>
                        <option value="2017">2017</option>
                    </select>
                    <em>-</em>
                    <select id="end_year" disabled="true">
                        <option value="2014">2014</option>
                        <option value="2015">2015</option>
                        <option value="2016">2016</option>
                    </select>
                </li>
                <li>
                    <span>第一学期</span>
                    <input type="text" id="dt1" data-rome-id="0">
                    <em>-</em>
                    <input type="text" id="dt2" data-rome-id="1">
                </li>
                <li>
                    <span>第二学期</span>
                    <input type="text" id="dt3" data-rome-id="2">
                    <em>-</em>
                    <input type="text" id="dt4" data-rome-id="3">
                </li>
            </ul>
        </div>--%>
		<div id="com-gl" class="mt20 cou-tab center clearfix fl">
            <div class="mt20 clearfix center">
                <div class="clearfix">
                    <button class="chat-bt btn fl">新建学期</button>

                </div>
                <table class="mt20">
                    <thead>
                    <tr>
                        <th class="w247">学年</th>
                        <th class="w247">学年名称</th>
                        <th class="w301">第一学期</th>
                        <th class="w301">第二学期</th>
                        <th class="w100">操作</th>
                    </tr>
                    </thead>
                    <tbody id="term3">
					</tbody>
					<script type="text/template" id="term2_temp">
						{{~it:value:index}}
						<tr>
							<td>
								<em class="w247">{{=value.year}}-{{=value.year+1}}</em>
							</td>
							<td>
								{{? value.sy==null}}
									<em class="w247"></em>
								{{??}}
									<em class="w247">{{=value.sy}}</em>
								{{?}}
							</td>
							<td>
								<em class="w301">{{=value.term1}}</em>
							</td>
							<td>
								<em class="w301">{{=value.term2}}</em>
							</td>
							<td>
								{{? value.isfabu==1}}
								<em class="w100  ccur2" >编辑</em>
								{{??}}
								<em class="w100 edit ccur" year="{{=value.year}}">编辑</em>
								{{?}}
							</td>
						</tr>
						{{~}}
					</script>
                </table>
            </div>
			<%--<div class="dbtn mt35">--%>
				<%--<button class="btn-ok" id="save_term">确认</button>--%>
			<%--</div>--%>
		</div>
		<!--走班学科-->
		<div id="com-zb" class="mt20 cou-tab center clearfix fl">
			<div class="center">
				<span class="c-b f18 mr182 mt10 inline">设置走班学科</span>
			</div>
			
			<table class="mt25">
				<thead>
					<tr>
						<th class="w299">学科</th>
						<th class="w299">高一</th>
						<th class="w299">高二</th>
						<th class="w299">高三</th>
					</tr>
				</thead>
				<script type="text/template" id="subjectList">
                    {{~it:value:index}}
                    <tr>
                        <td><em>{{=value.subjectName}}</em></td>
						<td>
							{{? value.gradeOne==1}}
                      		 <input class = "sel2" type="checkbox" checked = "checked" subId = {{=value.subjectId}} gid = {{=value.gidOne}}>
                        	{{?}}
							{{? value.gradeOne==0}}
                      		 <input class = "sel2" type="checkbox" subId = {{=value.subjectId}} gid = {{=value.gidOne}}>
                        	{{?}}
							{{? value.gradeOne != 0 && value.gradeOne != 1}}
                      		 <input class = "sel2" type="checkbox" subId = {{=value.subjectId}} gid = {{=value.gidOne}} disabled>
                        	{{?}}
						</td>
                        <td>
							{{? value.gradeTwo==1}}
                      		 <input class = "sel3" type="checkbox" checked = "checked" subId = {{=value.subjectId}} gid = {{=value.gidTwo}}>
                        	{{?}}
							{{? value.gradeTwo==0}}
                      		 <input class = "sel3" type="checkbox" subId = {{=value.subjectId}} gid = {{=value.gidTwo}}>
                        	{{?}}
							{{? value.gradeTwo != 0 && value.gradeTwo != 1}}
                      		 <input class = "sel3" type="checkbox" subId = {{=value.subjectId}} gid = {{=value.gidTwo}} disabled>
                        	{{?}}
						</td>
                        <td>
							{{? value.gradeThree==1}}
                      		 <input class = "sel5" type="checkbox" checked = "checked" subId = {{=value.subjectId}} gid = {{=value.gidThree}}>
                        	{{?}}
							{{? value.gradeThree==0}}
                      		 <input class = "sel5" type="checkbox"  subId = {{=value.subjectId}} gid = {{=value.gidThree}}>
                        	{{?}}
							{{? value.gradeThree != 0 && value.gradeThree != 1}}
                      		 <input class = "sel5" type="checkbox" subId = {{=value.subjectId}} gid = {{=value.gidThree}} disabled>
                        	{{?}}
						</td>
                    </tr>
                    {{~}}
                </script>
				<tbody id = "subList">
					
				</tbody>
			</table>
		</div>
        <!--排课-->
        <div id="com-pk" class="mt20 cou-tab center clearfix fl">
            <div class="center clearfix">
				<span class="fl f16 c-b">
					排课学期:<select class = "sel1 mr15" type = "text" id="term2">

                    </select>
				</span>
				<span class="fl f16 c-b ">
					当前学期:
					<script type="text/template" id="term_temp">
						{{~it:value:index}}
						<option value="{{=value.id}}">{{=value.xqnm}}</option>
						{{~}}
		 			</script>
					<select class = "sel1" type = "text" id="term">

					</select>
				</span>
				<button class="add-bt btn fr f16 newpk">+新建排课</button>
            </div>
            <table class="mt25">
                <thead>
                    <tr>
                        <th width="90">排课次第</th>
                        <th width="455">排课说明</th>
                        <th width="200">日期</th>
						<th width="180">年级</th>
                        <th width="89">当前排课</th>
                        <th width="89">同步</th>
                        <th width="89">操作</th>
                    </tr>
                </thead>
                <tbody id="paikeci">

                </tbody>
				<script type="text/template" id="paikeci_templ">
					{{~it:value:index}}
					<tr sn="{{=value.serial}}">
						<td>
							<em>第{{=value.serial}}次</em>
						</td>
						<td>
							{{=value.desc}}
						</td>
						<td>
							{{=value.date}}
						</td>
						<td>
							{{=value.gradeStr}}
						</td>
						<td>
							<input type="radio" name="current_paike" value="{{=value.ciId}}">
						</td>
						<td>
							{{? value.isfabu}}
								<span class="ccur2 " pid="{{=value.ciId}}">同步</span>
							{{??}}
								<span class="ccur tbb" pid="{{=value.ciId}}">同步</span>
							{{?}}
						</td>
						<td>
                            <a class="ccur" target="_blank" href="/newisolatepage/paikereport.do?ciId={{=value.ciId}}">
                                <img class="inline" src="/static_new/images/new33/bgg.png">
                            </a>
							{{? value.isfabu}}

							{{??}}
							<img pid="{{=value.ciId}}" desc="{{=value.desc}}" grades="{{=value.gradeIds}}" class="opts imghides edit mr10" src="/static_new/images/new33/edit.png">
							{{?}}

							{{? value.isfabu}}

							{{??}}
							<img pid="{{=value.ciId}}" class="opts imghides del" src="/static_new/images/new33/dell.png">
							{{?}}

						</td>
					</tr>
					{{~}}
				</script>
            </table>
        </div>
        <!--排课需求-->
        <div id="com-sj" class="mt20 cou-tab center clearfix fl" hidden>
            <div class="center clearfix">
                <dl class="mt35">
                    <dt class="dt1 c00 f18">本学期需求录入时间</dt>
                    <dd class="mt35 c00">
                        <span>开始日期</span><input class="dtime" type="text" id="time1" data-rome-id="0">
                        <span>结束日期</span><input class="dtime" type="text" id="time2" data-rome-id="1">
                        <button class="btnQd" id="qd">确定</button>
                    </dd>
                </dl>
            </div>
        </div>
		<!--排课设置-->
		<div id="com-sz" class="mt20 cou-tab center clearfix fl" hidden>
			<div class="center clearfix">
				<span class="fl f16 c-b block clearfix mb20">
					年级：
					<select class="sel1 mr15" type="text" id="gradeTurnOff">

					</select>
					<script type="text/template" id="gradeTurnOff_temp">
                    {{~it:value:index}}
					<option value="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</option>
					{{~}}
                </script>
				</span>
				<dl class="mt35" style="clear: both">
					<dt class="dt1 c00 f18 mt30">重分行政班</dt>
					<dd class="mt20">
						<span class="f16 mr10">
							<input id="afreshSet" type="checkbox">重分行政班模式
						</span>
					</dd>
					<dt class="dt1 c00 f18 mt30">走班格</dt>
					<dd class="mt20">
						<span class="f16 mr10">
							<input type="checkbox" id="allow">允许走班格放置非走班教学班
						</span>
					</dd>
                    <dt class="dt1 c00 f18 mt30">课表无课格子显示</dt>
                    <dd class="mt20">
                        <span class="f16 mr10">
                            课表类型：<select class="sel1 mr15" type="text" style="margin-left: 0" id="kbType">
								<option value="1">教室课表</option>
								<option value="2">教师课表</option>
								<option value="3">学生课表</option>
								<option value="4">行政班课表</option>
								<option value="5">学科课表</option>
						</select>
                        </span>
                    </dd>
                    <dd class="mt20">
						<span class="f16 mr10">
							编辑文本：<input type="text" class="inpT" placeholder="默认显示空白" id="desc" disabled="disabled">
                            <button class="btnQd" id="descQD">编辑</button>
						</span>
                    </dd>
				</dl>
				<%--<dl class="mt35" style="clear: both">--%>
					<%--<dt class="dt1 c00 f18 mt30">调代课</dt>--%>
					<%--<dd class="mt20">--%>
						<%--<span class="f16 mr10">--%>
							<%--<input type="checkbox" id="tdk">允许当天可以调代课--%>
						<%--</span>--%>
					<%--</dd>--%>
				<%--</dl>--%>
			</div>
		</div>
		<!--排课规则-->
		<div id="com-gz" class="mt20 cou-tab center clearfix fl" style="display: none;">
			<div class="pk-gz-group">
				<div class="pk-gz-title">
					<ul class="gz-list gz_list"><li class="active">学科设置</li><li>教师设置</li></ul>
				</div>
				<div class="gz-xk-content gz_ct gz_ct_1">
					<div class="lf">
						<div class="top"><span>学科列表</span><a class="ck_xk_all">全选</a></div>
						<ul class="xk-list xk-1 ck_xk">
							<li><span class="ck"></span><span class="name">语文（课时：5）</span></li>
                            <li><span class="ck"></span><span class="name">语文（课时：5）</span></li>
						</ul>
					</div>
					<div class="rt">
						<p>排课规则：</p>
						<ul class="xk-list">
							<li><span class="ck"></span><span class="name">学科进度一致</span></li>
							<li><span class="ck"></span><span class="name">周课时平均</span></li>
							<li><span class="ck"></span><span class="name">跨班进度一致</span></li>
							<li class="tblk"><span class="ck"></span><span class="name">同班连课</span></li>
						</ul>
						<div class="lk">
							<span>同班最大连课数</span>
							<select><option>1</option></select>
						</div>
					</div>
				</div>
				<div class="gz-js-content gz_ct gz_ct_2">
					<div class="hc-title">
						<p>教师互斥</p>
						<a id="new_hc">新建互斥组</a>
					</div>
					<table class="hc-tb">
						<thead>
							<tr>
								<td width="25%">互斥组</td>
								<td width="25%">教师一</td>
								<td width="25%">教师二</td>
								<td width="25%">操作</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>1</td>
								<td>教师一</td>
								<td>教师二</td>
								<td><a>删除</a></td>
							</tr>
							<tr>
								<td>2</td>
								<td>教师一</td>
								<td>教师二</td>
								<td><a>删除</a></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>

	</div>
</div>
<div class="bg"></div>
<!--事务类型-->
<div class="popup sw-popup">
	<div class="popup-top">
		<em>事务类型</em>
		<i class="fr close"></i>
	</div>
	<dl class="popup-dl">
		<dd>
			<span>事务类型：</span>
			<input class="inp1" id = "swType" type="text">
		</dd>
	</dl>
	<div class="popup-btn mt25">
		<button class="qd" id = "swqd" ids = "*">确定</button>
		<button class="qx">取消</button>
	</div>
</div>

<div class="popup addn-popup">
	<div class="popup-top">
		<em>添加管理员</em>
		<i class="fr close"></i>
	</div>
	<div class="addn-dv">
		<table>
			<thead>
				<tr>
					<th width="112">管理员</th>
					<th width="130">联系方式</th>
					<th width="130">选择</th>
				</tr>
			</thead>
			<script type="text/template" id="managerList">
                    {{~it:value:index}}
                    <tr userId = {{=value.userId}} ids = "*">
                        <td>{{=value.userName}}</td>
						<td class = "tel">{{=value.contactWay}}</td>
                        <td class="ad">
						<i></i>
						</td>	
                    </tr>
                    {{~}}
             </script>
			<tbody id = "manaList">
				
			</tbody>
		</table>
	</div>
	<div class="popup-btn mt25">
		<button class="qd" id = "tj" ids = "*">添加</button>
		<button class="qx">取消</button>
	</div>
</div>

<!--管理员-->
<div class="popup ssw-popup">
	<div class="popup-top">
		<em>增加管理员</em>
		<i class="fr close"></i>
	</div>
	<dl class="popup-dl">
		<dd>
			<span>管理员：</span>
			<input class="inp1" type="text" id = "mname">
		</dd>
	</dl>
	<div class="popup-btn mt25">
		<button class="qd" id = "mnqd">确定</button>
		<button class="qx">取消</button>
	</div>
</div>

<!--管理员-->
<div class="popup newpk-popup">
    <div class="popup-top">
        <em>排课</em>
        <i class="fr close"></i>
    </div>
    <dl class="popup-dl">
        <dd>
			<p>排课作用年级</p>
			<ul id="grade" style="margin: 8px 0">

			</ul>
			<span>排课说明</span>
			<script type="text/template" id="grade_temp">
				{{~it:value:index}}
				<input class="vt4" type="checkbox" value="{{=value.gid}}">
				<em class="mr10">{{=value.gnm}}({{=value.jie}}) </em>
				{{~}}
			</script>
        </dd>
        <dd>
            <textarea class="text2 mtt13" id="description"></textarea>
        </dd>
    </dl>
    <div class="popup-btn mt25">
        <button class="qd" id="savePaikeci">确定</button>
        <button class="qx">取消</button>
    </div>
</div>

<!--新建学期-->
<div class="popup xj-popup">
    <div class="popup-top">
        <em class="fl">新建学期</em>
        <i class="close fr"></i>
    </div>
    <div class="term-add-info">
        <dl>
            <dd>
                <em>学年</em>
                <select id="start_year">

                </select>
				<script type="text/template" id="start_year_temp">
					{{~it:value:index}}
					<option value="{{=value.val}}">{{=value.val}}</option>
					{{~}}
				</script>
                <i>-</i>
                <select id="end_year" disabled="true">

                </select>
				<script type="text/template" id="end_year_temp">
					{{~it:value:index}}
					<option value="{{=value.val}}">{{=value.val}}</option>
					{{~}}
				</script>
            </dd>
            <dd>
                <em>学年名称</em>
                <input type="text" id="yearName" style="width: 287px;">
            </dd>
            <dd>
                <em>第一学期</em>
                <input type="text" class="Wdate"  id="dt1">
                <i>-</i>
                <input type="text" class="Wdate"  id="dt2">
            </dd>
            <dd>
                <em>第二学期</em>
                <input type="text" class="Wdate"  id="dt3">
                <i>-</i>
                <input type="text" class="Wdate"  id="dt4">
            </dd>
        </dl>
    </div>
    <div class="popup-btn mt35">
        <button class="qd" id="save_term">确定</button>
        <button class="qx">取消</button>
    </div>
</div>
<!--同步-->
<div class="popup tbb-popup">
    <div class="popup-top">
        <em class="fl">同步</em>
        <i class="fr close"></i>
    </div>
    <dl class="popup-dl">
        <dd>
            <span>同步数据源</span>
            <select class="inp1" id="synSource">

            </select>
			<script type="text/template" id="synSource_temp">
				{{~it:value:index}}
				<option value="{{=value.ciId}}">{{=value.ciname}}</option>
				{{~}}
			</script>
        </dd>
        <dd>
			<span>
				<input class="vt4" type="checkbox" value="1" id="all">
				<em class="">全选</em>
			</span>
            <ul style="margin: 8px 0" id="types">
				<span>
					<input class="vt4" type="checkbox" value="1">
					<em class="">教室数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="2">
					<em class="">课表结构 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="3">
					<em class="">教师数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="4">
					<em class="">课时数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="5">
					<em class="">自习课数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="6">
					<em class="">教学班 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="7">
					<em class="">班级数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="8">
					<em class="">走班时间 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="9">
					<em class="">年级数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="10">
					<em class="">学生(选科)数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="11">
					<em class="">排课数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="12">
					<em class="">教学日数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="13">
					<em class="">选科组合数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="14">
					<em class="">拼班数据 </em>
				</span>
				<span>
					<input class="vt4" type="checkbox" value="15">
					<em class="">时段组合数据 </em>
				</span>
            </ul>
        </dd>
    </dl>
    <div class="popup-btn mt35 mb35">
        <button class="qd" id="synData">确定</button>
        <button class="qx">取消</button>
    </div>
</div>
<div class="popup load-popup">
	<p id="loadtext"></p>
	<img src="/static_new/images/new33/nloading.gif">
</div>
<div class="jhh">
	<div class="jhh-top">数据处理中，请稍等！</div>
	<img class="jhhIm" src="/static_new/images/new33/loading1.gif">
	<span class="jhhSp"></span>
</div>



<%--新建互斥组--%>
<div class="popup hc-popup">
    <div class="popup-top">
        <em class="fl">新建互斥组</em>
        <i class="fr fft" onclick="hcClose()"></i>
    </div>
    <div class="t-center clearfix">
        <ul id="treeDemo" class="ztree fl"></ul>
        <div class="fr t-right">
            <div id="myList-nav" class="member-select">
            </div>
            <div class="myList">
                <ul id="myList" class="member-list clearfix">
                    <li class="li-special">
                        <img src="http://7xiclj.com1.z0.glb.clouddn.com/d_8_1.png">
                        <span>张三</span>
                        <em></em>
                    </li>
                    <li class="li-special">
                        <img src="http://7xiclj.com1.z0.glb.clouddn.com/d_8_1.png">
                        <span>张三</span>
                        <em></em>
                    </li>
                    <li class="li-special">
                        <img src="http://7xiclj.com1.z0.glb.clouddn.com/d_8_1.png">
                        <span>张三</span>
                        <em></em>
                    </li>
                </ul>
            </div>
            <p class="hc-number">已选择：<span id="hcTeaherNum">0</span>/2名老师</p>
        </div>
    </div>
    <div class="popup-btn mt45">
        <button class="qd queDingRen">确定</button>
        <button class="qxx qss" onclick="hcClose()">取消</button>
    </div>
</div>


</body>

<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('set', function (set) {
    	set.init();
    });

    //互斥弹窗关闭
    function hcClose(){
        $('.hc-popup').hide();
        $('.bg').hide();
        dt=0;
        $('#hcTeaherNum').text(0)
    }

</script>
</html>
