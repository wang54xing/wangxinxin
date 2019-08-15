<%--
  Created by IntelliJ IDEA.
  User: albin
  Date: 2018/3/30
  Time: 9:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>教室设置</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/new33/course2.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/layer/layer.js"></script>
    <!--[if IE]>
    <style>
        .w150{
            width: 149px!important;
        }
        .w700{
            width: 693px!important;
        }
    </style>
    <![end if]-->
    <style type="text/css">
        .fixedcont {
            width: 290px;
            position: fixed;
            top: 0px;
            right: 0px;
            /* min-height: 190px;*/
            z-index: 100;
            background: #fff;
            border-left: 5px solid #928ee8;
        }

        .fixedcont .ul1 {
            z-index: 1;
            width: 100%;
            height: 600px;
            overflow-x: hidden;
            overflow-y: auto;
            cursor: default;
            float: right;
        }

        .fixedcont .ul1 li.over {
            background: #e5e5e5 !important;
            border: 1px solid #ccc !important;
            color: #666;
        }

        .fixedcont .ul1 li {
            min-height: 85px;
            width: 99%;
            margin: 5px 10px 5px 0;
            font-size: 12px;
            background: #fff;
            color: #f77737;
            float: left;
            padding: 10px 0px;
            border: 1px solid transparent;
            cursor: pointer;
        }

        .fixedcont .ul2 {
            z-index: 1;
            width: 160px;
            height: 358px;
            overflow-x: hidden;
            overflow-y: auto;
            cursor: default;
            float: right;
        }

        .fixedcont .ul2 li {
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

        .fixedcont .ul2 li.active {
            width: 134px;
            background: #f77727;
            border: 1px solid #aaa;
            color: #fff;
        }

        .fixedcont .ul1 li span {
            text-align: center;
            /* float: left;*/
            width: 39px;
            color: #666;
        }

        .fixedcont .ul1 li:last-child {
            margin-bottom: 10px;
        }

        .fixedcont .ul1 li > div {
            float: left;
            /* line-height: 40px;
             text-indent: 15px;
             cursor: pointer;
             height: 66px;
             padding-top: 10px;
             width: 245px;*/
        }

        .fixedcont .ul1 li > div p {
            /* height: 20px;*/
            line-height: 20px;
            font-size: 12px;
            margin-left: 30px;
        }

        .fixedcont .info .p1 {
            font-size: 16px;
            height: 30px;
            line-height: 30px;
            margin-bottom: 12px;
        }

        .fixedcont .info .p1 em {
            float: right;
            font-size: 32px;
            cursor: pointer;
            display: none;
        }

        .fixedcont .info .p2 {
            font-size: 12px;
            height: 26px;
            line-height: 26px;
            display: inline-block;
            height: 57px;
            width: 290px;
            margin-left: -12px;
            background: #f0f0f0;
        }

        .fwb {
            font-weight: bold;
        }

        .fixedcont .info .p3 {
            font-size: 14px;
            height: 30px;
            cursor: pointer;
            text-align: center;
            line-height: 30px;
        }

        .fixedcont .info {
            background: #ffffff;
            color: black;
            padding: 10px 12px 0px 12px;
        }

        .fixedcont .cont {
            /*border: 1px solid #f77737;*/
            border-top: none;
            /* padding-top: 10px;*/
            transition: all .3s;
            overflow: hidden;
            background: #f0f0f0;
        }

        .fixedcont .cont .lt {
            float: left;
            width: 46px;

        }

        .fixedcont .cont .lt span.active {
            background: #f77737 !important;
            color: #fff !important;
        }

        .fixedcont .cont .lt span {
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

        #movebg {
            height: 40px;
            background: #fff;
            border: 1px dashed #999;
            line-height: 40px;
            text-align: center;
            cursor: move;
            transition: all .3s;
            overflow: hidden;
        }

        .container {
            padding-right: 100px;
        }

        .tab1 th {
            border: 1px solid #f1f1f1;
        }

        .tab2 tr th:first-child,
        .tab2 tr td:first-child {
            border-left: none !important;
        }

        .tab1 em {
            font-size: 12px;
        }

        .tab2 div.green {
            background: #90EE90;
            border: 1px dashed #2E8B57;
            color: #333;
        }

        .tab2 div.red {
            background: #FFB6C1;
            border: 1px dashed red;
            color: #333;
            height: 59px;
            width: 99px;
        }

        .h0 {
            height: 0px !important;
            border: none !important;
        }
        
        .cou-tab{
        	
        }
    </style>
    <script type="text/javascript">
        $(function () {
            var h = $(window).height();
            $('.fixedcont').css('height', h);
            $('.ul1').css('height', h - 155);
            $('.rrow').click(function () {
                $(this).parent().hide();
            })
            $('.seld').click(function () {
                $('.bl-dv').show();
            })
            $('.t-down').click(function () {
                $('.td-over').scrollTop();
                $('.td-over').css('margin-top', -91)
            })
            $('.t-top').click(function () {
                $('.td-over').css('margin-top', 0)
            })
            $('.ss-li li').click(function () {
                $(this).addClass('ss-cur').siblings().removeClass('ss-cur')
            })
            $('.re-sy').click(function () {
                $('.bg').show();
                $('.addxz-popup').show();
            })
            $('.qxx,.close').click(function () {
                $('.bg').hide();
                $('.addxz-popup').hide();
                $('.addstu-popup').hide();
                $('.delstu-popup').hide();
            })
            <!--全选-->
            $('.tall').change(function () {
                if ($(this).is(':checked')) {
                    $('.xz-dl span input').prop("checked", true);
                } else {
                    $('.xz-dl span em').removeClass('c-ff');
                    $('.xz-dl span input').removeAttr("checked");
                }
            })

            $('body').on('click', '.qd-cl', function () {
                $(this).parent().hide();
            })


            $('.xz-dl span input').change(function () {
                if ($(this).is(':checked')) {
                    $(this).siblings('em').addClass('c-ff')
                } else {
                    $(this).siblings('em').removeClass('c-ff')
                }
            })
            $('.yc').click(function () {
                $(this).parent().parent().parent().hide();
            })
            $('body').on('click','.zk',function(){
                if ($(this).text().indexOf('[展开]') != -1) {
                    $(this).siblings('.c-h').addClass('c-class');
                    $(this).text('[收起]');
                } else {
                    $(this).text('[展开]');
                    $(this).siblings('.c-h').removeClass('c-class')
                }
            })
            $('.d-sty em').click(function () {
                $(this).addClass('cur').siblings().removeClass('cur')
            })
            $('.text span').click(function () {
                $(this).toggleClass('sign-spp')
            });
            $('body').on('click', '.sing-td', function () {
                var tt = $(this).siblings('.text');
                $(this).addClass('td-cur').parent().siblings('tr').find('.sing-td').removeClass('td-cur');
                if ($(this).hasClass('td-cur')) {
                    $('.bt-hh').removeClass('auto-btt').addClass('auto-bt')
                } else {
                    $('.bt-hh').removeClass('auto-bt').addClass('auto-btt')
                }
                $('.auto-bt').click(function () {
                    $('.bg').show();
                    $('.addb-popup').show()
                })
            })

        })
    </script>
    <script type="text/javascript">
        $(function () {
            var l1 = 1;
            var l2 = 1;
            var l3 = 1;


            $('.ul-main > li > label').click(function () {
                var jsn = $(this).attr('js');
                if (jsn == 'all') {
                    $(this).addClass('active').siblings('label').removeClass('active')
                } else {
                    $(this).toggleClass('active');
                    $(this).parent().find('.all').removeClass('active');
                }
            });

            $('body').on('click', '.tab2 td>div>div>div', function () {
                if ($(this).parent().hasClass('red')) {

                } else {
                    $(this).toggleClass('active');
                }
            });

            $('.fixedcont .cont .lt span').click(function () {
                $(this).addClass('active').siblings('span').removeClass('active');
                $('.fixedcont .ul1').hide();
                $('.fixedcont .ul2').show();
            })

            //收起移动窗
            $('.fixedcont .info .p1 em ,.rowa').click(function () {
                $('.fixedcont .cont,#movebg').addClass('h0');
                $('.em-close').hide();
                $('.rowa').hide()
                $('#move').css('width', '110px');
                $('.fixedcont .info .p2').hide();
                $('.fixedcont .info .p3').show();
                $('.fixedcont').css('height', 85);
            });
            //展开移动窗
            $('.fixedcont .info .p3').click(function () {
                var h = $(window).height();
                $('.fixedcont .cont,#movebg').removeClass('h0');
                $(this).hide();
                $('#move').css('width', '290px');
//                $('#move').css('background', '#5851db');
                $('.fixedcont .info .p2').show();
                $('.fixedcont .info .p1 em').show();
                $('.rowa').show();
                $('.fixedcont').css('height',h);
            });

            //点击教师名 高亮其所有课程
            $('body').on('click', '.tab2 div>p:nth-child(2)', function () {
                var jsn = $(this).attr('js');
                var cln = $(this).attr('class');
                if ($(this).hasClass('active')) {
                    $(this).removeClass('active')
                    $('.tab2 div.' + jsn).removeClass('active')
                } else {
                    $(this).addClass('active')
                    $('.tab2 div.' + jsn).addClass('active')
                }
                return false;
            })

            var labelNumber = 4;
            $('.tag label input').click(function () {
                var jsn = $(this).attr('js');
                if ($(this).is(':checked')) {
                    $('.tab2 .v' + jsn).show();
                    labelNumber++
                } else {
                    labelNumber--;
                    if (labelNumber < 1) {
                        layer.msg('请至少选择一个标签显示！')
                        $(this).prop('checked', 'checked')
                        labelNumber = 1
                    } else {
                        $('.tab2 .v' + jsn).hide();
                    }
                }
            });


            //拖拽窗口 学科切换
            $('.fixedcont .ul2 li').click(function () {
                $(this).addClass('active').siblings('li').removeClass('active');
                $('.fixedcont .ul1').show();
                $('.fixedcont .ul2').hide();
            })

            $('.gaozhong label').click(function () {
                var jln = $(this).attr('js');
                var cln = $(this).attr('class');
                if (jln == 'all') {
                    $('tr td').show();
                    $('tr th').show();
                    l1 = 1;
                } else {
                    if (l1 == 1) {
                        if (cln == 'active') {
                            $('tr td.' + jln).show().siblings('tr td').hide();
                            $('tr th.' + jln).show().siblings('tr th').hide();
                            l1 = l1 + 1
                        }
                    } else {
                        if (cln == 'active') {
                            $('tr td.' + jln).show();
                            $('tr th.' + jln).show();
                        } else {
                            $('tr td.' + jln).hide();
                            $('tr th.' + jln).hide();
                        }
                    }
                }
            });


            $('.zhou label').click(function () {
                var jln = $(this).attr('js');
                var cln = $(this).attr('class');
                var wd = $('.tab2 th>div').width();
                if (jln == 'all') {
                    $('.zhou>div').show();
                    $('.gao>div').show();
                    $('th>div').css('width', '500px')
                    l2 = 1
                } else {
                    if (l2 == 1) {
                        $('tr td div.' + jln).show().siblings('tr td div').hide();
                        $('tr th div.' + jln).show().siblings('tr th div').hide();
                        $('th>div').css('width', '100px')
                        l2 = l2 + 1
                    } else {
                        if (cln == 'active') {
                            $('tr td div.' + jln).show();
                            $('tr th div.' + jln).show();
                            $('tr th:first-child').show();
                            $('th>div').css('width', wd + 100)
                        } else {
                            $('tr td div.' + jln).hide();
                            $('tr th div.' + jln).hide();
                            $('tr th:first-child').show();
                            $('th>div').css('width', wd - 100)
                        }
                    }
                }
            });

            $('.ke label').click(function () {
                var ind = $(this).index();
                var jln = $(this).attr('js');
                var cln = $(this).attr('class');
                if (jln == 'all') {
                    $('.ke>div').show();
                    l3 = 1;
                } else {
                    if (l3 == 1) {
                        $('.ke>div.' + jln).show().siblings('.ke>div').hide();
                        l3 = l3 + 1
                    } else {
                        if (cln == 'active') {
                            $('.ke>div.' + jln).show();
                        } else {
                            $('.ke>div.' + jln).hide();
                        }
                    }
                }
            });

            $('.tag input').click(function () {
                var jsn = $(this).attr('js')
                if ($(this).is(':checked') == true) {
                    $('div.' + jsn).addClass('active')
                } else {
                    $('div.' + jsn).removeClass('active')
                }
            });

            function init() {

            }


        })
    </script>

    <script type="text/javascript">

        window.onload = function () {

            var dd = document.getElementById("movebg"); // 获取DIV对象
            dd.onmousedown = function (e) {
                var event1 = e || window.event;                  // IE、火狐获取事件对象
                var eventX = event1.offsetX || event1.layerX;         // 获取鼠标相对于事件源的X轴
                var eventY = event1.offsetY || event1.layerY;        // 获取鼠标相对于事件源的Y轴
                var flag = true;
                this.onmousemove = function (e) {
                    if (flag) {
                        var event2 = e || window.event;
                        var eveX = event2.clientX;             // 获取鼠标相对于浏览器x轴的位置
                        var eveY = event2.clientY;             // 获取鼠标相对于浏览器Y轴的位置
                        $('#move').css('top', eveY - eventY);
                        $('#move').css('left', eveX - eventX);
                    }
                }
                this.onmouseup = function () {
                    if (flag) {
                        flag = false;
                    }
                }
            }
        }
    </script>
    <script type="text/javascript">
        function allowDrop(ev) {
            ev.preventDefault();
        }

        var srcdiv = null;
        function drag(ev, divdom) {
            srcdiv = divdom;
            ev.dataTransfer.setData("text/html", divdom.innerHTML);
        }

        function drop(ev, divdom) {
            ev.preventDefault();
            if (srcdiv != divdom) {
                addStuByTag($(divdom).attr('jxbids'), $(srcdiv).attr('id'),  $(srcdiv).attr('name'),  $(srcdiv).attr('chushiName'), $(divdom).attr('name'), $(divdom).attr('count'),  $(srcdiv).attr('count'))
            }
        }
        function removed(divdom) {
//            var id = $('.ul1 li .vxk').attr('id')
//            alert(id)
//            //  divdom.parentNode.removeChild(divdom)
        }

        function addStuByTag(jxbid, tagid, name, chushiName, xinName, Ycount, Zcoung) {
            var data = {};
            data.tagid = tagid;
            data.jxbId = jxbid;
            var zong = parseInt(Ycount) + parseInt(Zcoung);
            $.ajax({
                url: "/studentTag/addJxbStudentByTagId.do",
                type: "get",
                dataType: "json",
                async: true,
                data: data,
                success: function (rep) {
                    if (rep.code == '200') {
                        layer.msg(rep.message);
                        if (rep.message == "添加成功") {
                            $("td[jxbids=" + jxbid + "]>em").append("<span class='sign-sp ml10'>" + name + "<i class='rrow cursor'  jxbid='" + jxbid + "' ids='" + tagid + "'></i></span>")
                            chushiName += xinName + ",";
                            $("[gdda=" + tagid + "]").html(chushiName)
                            $("[ddff=" + jxbid + "]").html(zong)
                            $("td[jxbids=" + jxbid + "]").attr("count",zong)
                        }
                    }
                },
                error: function () {
                    console.log('saveKeBiaoInfo error');
                }
            });
        }
	
        $(function(){
        	$(".right-pos a").eq(3).attr("class","vc2");
        })
    </script>
    <style type="text/css">
    </style>
</head>
<body>
<jsp:include page="head.jsp"></jsp:include>
<span id = "defaultTerm" style="display: none;"></span>
<div class="right-pos">
    <div class="ov-hi">
        <div class="td-over">
             <a href="/newisolatepage/placementSet.do" class="va1">分教学班</a>
             <a href="/newisolatepage/stuSign.do" class="vb1">标记学生</a>
             <a href="/newisolatepage/jxbRelation.do" class="vc1">关联教学班</a>
             <a href="/newisolatepage/stuRelation.do" class="vc1">关联学生</a>
             <%--<a href="/newisolatepage/fenbanInfoSet.do" class="vd1">信息设置</a>--%>
             <a href="/newisolatepage/conflict.do" class="ve1">冲突检测</a>
            <%--<a href="/newisolatepage/danShuangZhou.do" class="vz1">单双周课</a>--%>
        </div>
    </div>
</div>
<div class="fixedcont" id="move" style="width: 290px;">
    <div class="rowa"><em>收</em><em>起</em></div>
    <div id="movebg">点击可移动</div>
    <div class="info">
        <p class="p1">关联学生<em class="em-close" style="display:inline-block;">×</em></p>

        <div class="p2" style="display:inline-block">
            <div class="re-sy">
                全部标签（<em class="size"></em>）
                <i class="rig"></i>
            </div>
            <em class="hs">显示隐藏</em>
        </div>
        <p class="p3" style="display:none">展开 > </p>
    </div>
    <div class="cont clearfix">
        <script type="text/template" id="tagList">
            {{~it:value:index}}
            <li ondrop="drop(event,this)" ondragover="allowDrop(event)" ondragstart="drag(event, this)"
                ondragend="removed(this)" draggable="true" id="{{=value.id}}" count="{{=value.stuCount}}" name="{{=value.name}}" chushiName="{{=value.jiaoXueBanStr}}">
                <div class="zs w290">
                    <p>
                        <em class="vxk f12 c-7b w6">学生标签:</em>
                        <em class="vrs f12 c-h">{{=value.name}}</em>
                        <span class="hhs pt3 yc yin" ids="{{=value.id}}">隐藏</span>
                    </p>

                    <p>
                        <em class="vjs f12 c-7b w6" js='zs'>学生数量:</em>
                        <em class="vrs f12 c-b c-h">{{=value.stuCount}}</em>
                    </p>

                    <p><em class="vbq f12 c-7b w6">学生来源：</em><em
                            class="vrs f12 c-b c-h">{{=value.classListStr}}</em><span
                            class="hhs h20 pt20 zk">[展开]</span></p>

                    <p><em class="vbq f12 c-7b w6">关联教学班:</em><em
                            class="vrb f12 c-b c-h" gdda="{{=value.id}}">{{=value.jiaoXueBanStr}}</em><span
                            class="hhs h20 zk">[展开]</span></p></div>
            </li>
            {{~}}
        </script>
        <ul class="ul1">

        </ul>
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
                    <em ids="{{=value.gid}}">{{=value.gnm}}({{=value.jie}})</em>
                    {{~}}
                </script>
            </li>
        </ul>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="optcont sign mt20">
    	<div class = "pos">
        <select style="height:40px" id="subType">
            <option value="2">合格型</option>
            <option value="1">等级型</option>
            <option value="3">行政型</option>
            <option value="4">专项型</option>
        </select>
		</div>
        <div class="pos seld">
            <div class="app"></div>
            <div class="bl-dv">
                    <span class="block">
                        <input class="ttall" type="checkbox">全选
                    </span>
                <script type="text/template" id="sub_temp">
                    {{~it:value:index}}
                    <span><input type="checkbox" class="cls" ids="{{=value.subid}}">{{=value.snm}}</span>
                    {{~}}
                </script>
                <div class="bl-sp" id="subject">

                </div>
                <button class="qd-cl">确定</button>
            </div>
        </div>
        <div class="inline pos">
            <input class="inp10" type="text" placeholder="查找教学班" id="jxbName">
            <i class="search at" id="sosu"></i>
        </div>
        <button class="chat-btt btn fr mr100 f16 pos">
            自动关联
            <div class="pla-dvv">仅支持“物理、化学、生物、历史、地理、政治、”等走班学科自动关联</div>
        </button>
    </div>
</div>
<div class="center w1300 clearfix">
    <div class="mt20 cur-tab cou-tab center fl">
        <table class="fl tab-jsks votab clstab">
            <thead>
            <tr>
                <th width="150">教学班名称</th>
                <th width="110">教学班类型</th>
                <th width="110">学生数量</th>
                <th width="700">学生标签</th>
                <th width="135">学生管理</th>
            </tr>
            </thead>
         </table>
            <script type="text/template" id="studentList">
                {{~it:value:index}}
                <tr>
                    <td><em class="inline w150">{{=value.name}}</em></td>
                    <td><em class="inline w110">{{=value.typeName}}</em></td>
                    <td><em  class="inline w110" ddff="{{=value.id}}">{{=value.stuCount}}</em></td>
                    <td class="text" jxbids="{{=value.id}}" name="{{=value.name}}" count="{{=value.stuCount}}"
                        ondragover="allowDrop(event)"
                        ondrop="drop(event,this)">
						<em class="inline w700">
                        {{~value.tagList:obj:itt}}
						
							<span class="sign-sp ml10">
								<em>{{=obj.name}}({{=obj.count}})</em>
								<i class="rrow cursor" ids="{{=obj.id}}" jxbids="{{=value.id}}"></i>
							</span>
						
                        {{~}}
						</em>
                    </td>
                    <td>
						<em class="inline w135">
                        	<em class="ccur add-re" ids="{{=value.id}}" nm="{{=value.name}}">添加</em>
                       	    <em class="ccur del-re" ids="{{=value.id}}" nm="{{=value.name}}">删除</em>
						</em>
                    </td>
                </tr>
                {{~}}
            </script>
        <div class='ha410'>
	        <table class="fl tab-jsks votab clstab">
				<tbody id="tbd">
				</tbody>
			</table>
		</div>
    </div>
</div>
</div>
<div class="bg"></div>
<!--选择标签-->
<div class="popup addxz-popup">
    <div class="popup-top">
        <em>选择标签</em>
        <i class="fr close"></i>
    </div>
    <div class="bt-li">
        <input class="tall" type="checkbox">全选（<em class="size"></em>）
    </div>
    <dl class="xz-dl" id="tg">

    </dl>
    <script type="text/template" id="tglist">
        {{~it:value:index}}
        <span>
                {{? value.view==0}}
                       	<input type="checkbox" ids="{{=value.id}}" checked><em class="f12">{{=value.name}}({{=value.stuCount}})</em>
               {{?}}
                 {{? value.view==1}}
                       	<input type="checkbox" ids="{{=value.id}}"><em
                class="f12">{{=value.name}}({{=value.stuCount}})</em>
               {{?}}
		</span>
        {{~}}
    </script>
    <div class="popup-btn mt25">
        <button class="ss" id="xuanze">添加</button>
        <button class="qxx">取消</button>
    </div>
</div>
<!--添加学生-->
<div class="popup addstu-popup">
    <div class="popup-top">
        <em class = "isTj"></em><em id="bnm"></em>
        <i class="fr close"></i>
    </div>
    <div class="clearfix">
        <script type="text/template" id="classList">
            {{~it:value:index}}
            <li ids="{{=value.classId}}">{{=value.gname}}（{{=value.xh}}）班</li>
            {{~}}
        </script>
        <ul class="fl ss-li">

        </ul>
        <div class="fl ss-dv">
            <table>
                <thead>
                <tr>
                    <th width="100">姓名</th>
                    <th width="150">学号</th>
                    <th width="50">性别</th>
                    <th width="80">学科组合</th>
                    <th width="100">选择</th>
                </tr>
                </thead>
            </table>
            <div class="autoo h310 w503">
                <table>
                    <script type="text/template" id="stList">
                        {{~it:value:index}}
                        <tr>
                            <td><em class="f12 w100 inline">{{=value.userName}}</em></td>
                            <td><em class="f12 w151 inline">{{=value.studyNumber}}</em></td>
                            <td><em class="f12 w50 inline">{{=value.sexStr}}</em></td>
                            <td><em class="f12 w79 inline">{{=value.combiname}}</em></td>
                            <td>
                                <em class="f12 w100 inline">
                                    <input type="checkbox" class="xuanrendea" ids="{{=value.userId}}">
                                </em>
                            </td>
                        </tr>
                        {{~}}
                    </script>
                    <script type="text/template" id="stList1">
                        {{~it:value:index}}
                        <tr>
                            <td><em class="w100 inline">{{=value.userName}}</em></td>
                            <td><em class="w151 inline">{{=value.studyNumber}}</em></td>
                            <td><em class="w50 inline">{{=value.sexStr}}</em></td>
                            <td><em class="w79 inline">{{=value.combiname}}</em></td>
                            <td>
                                <em class="w100 inline">
                                    <input type="checkbox" class="shanchude" ids="{{=value.userId}}">
                                </em>
                            </td>
                        </tr>
                        {{~}}
                    </script>
                    <tbody id="stuList">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="popup-btn mt25">
        <button class="ss" id="quedingren" iz="0">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>
<!--移除学生-->
<div class="popup delstu-popup">
    <div class="popup-top">
        <em>移除学生</em>
        <i class="fr close"></i>
    </div>
    <div class="clearfix">
        <div class="ss-dv w620 ml20">
            <table>
                <thead>
                <tr>
                    <th width="98">姓名</th>
                    <th width="123">学籍</th>
                    <th width="50">性别</th>
                    <th width="100">行政班</th>
                    <th width="125">学科标签</th>
                    <th width="100">选择</th>
                </tr>
                </thead>
            </table>
            <div class="autoo h310 w620">
                <table>
                    <tbody>
                    <tr>
                        <td><em class="w98 inline">张三丰</em></td>
                        <td><em class="w123 inline">上海</em></td>
                        <td><em class="w50 inline">男</em></td>
                        <td><em class="w100 inline"></em></td>
                        <td><em class="w125 inline"></em></td>
                        <td>
                            <em class="w99 inline">
                                <input type="checkbox">
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td><em class="w98 inline">张三丰</em></td>
                        <td><em class="w123 inline">2018001</em></td>
                        <td><em class="w50 inline">男</em></td>
                        <td><em class="w100 inline"></em></td>
                        <td><em class="w125 inline"></em></td>
                        <td>
                            <em class="w99 inline">
                                <input type="checkbox">
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td><em class="w98 inline">张三丰</em></td>
                        <td><em class="w123 inline">2018001</em></td>
                        <td><em class="w50 inline">男</em></td>
                        <td><em class="w100 inline"></em></td>
                        <td><em class="w125 inline"></em></td>
                        <td>
                            <em class="w99 inline">
                                <input type="checkbox">
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td><em class="w98 inline">张三丰</em></td>
                        <td><em class="w123 inline">2018001</em></td>
                        <td><em class="w50 inline">男</em></td>
                        <td><em class="w100 inline"></em></td>
                        <td><em class="w125 inline"></em></td>
                        <td>
                            <em class="w99 inline">
                                <input type="checkbox">
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td><em class="w98 inline">张三丰</em></td>
                        <td><em class="w123 inline">2018001</em></td>
                        <td><em class="w50 inline">男</em></td>
                        <td><em class="w100 inline"></em></td>
                        <td><em class="w125 inline"></em></td>
                        <td>
                            <em class="w99 inline">
                                <input type="checkbox">
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td><em class="w98 inline">张三丰</em></td>
                        <td><em class="w123 inline">2018001</em></td>
                        <td><em class="w50 inline">男</em></td>
                        <td><em class="w100 inline"></em></td>
                        <td><em class="w125 inline"></em></td>
                        <td>
                            <em class="w99 inline">
                                <input type="checkbox">
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td><em class="w98 inline">张三丰</em></td>
                        <td><em class="w123 inline">2018001</em></td>
                        <td><em class="w50 inline">男</em></td>
                        <td><em class="w100 inline"></em></td>
                        <td><em class="w125 inline"></em></td>
                        <td>
                            <em class="w99 inline">
                                <input type="checkbox">
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td><em class="w98 inline">张三丰</em></td>
                        <td><em class="w123 inline">2018001</em></td>
                        <td><em class="w50 inline">男</em></td>
                        <td><em class="w100 inline"></em></td>
                        <td><em class="w125 inline"></em></td>
                        <td>
                            <em class="w99 inline">
                                <input type="checkbox">
                            </em>
                        </td>
                    </tr>
                    <tr>
                        <td><em class="w98 inline">张三丰</em></td>
                        <td><em class="w123 inline">2018001</em></td>
                        <td><em class="w50 inline">男</em></td>
                        <td><em class="w100 inline"></em></td>
                        <td><em class="w125 inline"></em></td>
                        <td>
                            <em class="w99 inline">
                                <input type="checkbox">
                            </em>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="popup-btn mt25">
        <button class="ss">确定</button>
        <button class="qxx">取消</button>
    </div>
</div>
</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('stuRelation', function (stuRelation) {
        stuRelation.init();
    });
</script>
</html>
