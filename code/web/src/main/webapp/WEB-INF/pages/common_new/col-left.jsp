<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<link rel="stylesheet" type="text/css" href="/static_new/css/reset.css"/>
<script type="text/javascript" src="/static_new/js/modules/calendar/0.1.0/WdatePicker.js"></script>
<%--<script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>--%>
<%@page import="com.pojo.app.SessionValue" %>
<input type="hidden" id="versionValue" value="<%=request.getParameter("version") %>"/>
<input type="hidden" id="index_Value" value="<%=request.getParameter("index") %>"/>
<div class="col-left">

    <!--.user-info-->
    <div class="user-info">
        <img src="${sessionValue.midAvatar}"/>
        <em>${sessionValue.userName}</em>
        <span>经验值${sessionValue.experience}</span>
    </div>
    <!--/.user-info-->

    <!--.left-nav-->
   
   
   <%--<jsp:include page="/WEB-INF/pages/common_new/nav_test.jsp" ></jsp:include>--%>


    <%--<c:choose>--%>
        <%--<c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isHeadmaster(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/teacher_headmaster_nav.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%--<c:when test="${roles:isTeacher(sessionValue.userRole) && roles:isManager(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/teacher_manager_nav.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%--<c:when test="${roles:isTeacher(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/teacher_nav.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%--<c:when test="${roles:isStudent(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/student_nav.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%--<c:when test="${roles:isParent(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/parent_nav.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%--<c:when test="${roles:isHeadmaster(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/headmaster_nav.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%--<c:when test="${roles:isManagerOnly(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/manager_nav.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%--<c:when test="${roles:isEducation(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/navs/education_nav.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%--<c:when test="${roles:isDoorKeeper(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/doorkeeper.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
         <%--<c:when test="${roles:isDormManager(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/dormmanager.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%--<c:when test="${roles:isFunctionRoomManager(sessionValue.userRole)}">--%>
            <%--<jsp:include page="/WEB-INF/pages/common_new/${sessionValue.schoolNavs}/function_room_manager.jsp" ></jsp:include>--%>
        <%--</c:when>--%>
        <%----%>
        <%----%>
        <%----%>
        <%--<c:otherwise>--%>

        <%--</c:otherwise>--%>
    <%--</c:choose>--%>

    <%--new nav--%>
    <ul class="left-nav">
        <%--<li class="current">--%>
            <%--<img src="/static_new/images/navv_left_2.png">--%>
            <%--<span><a href="###">模块一</a></span>--%>
            <%--<s class="iconfont">&#xe607;</s>--%>
        <%--</li>--%>
        <%--<dl style="display: block">--%>
            <%--<a href="###">--%>
                <%--<dt>条目一</dt>--%>
            <%--</a>--%>
            <%--<a href="###" class="">--%>
                <%--<dt class='red-in'>条目二</dt>--%>
            <%--</a>--%>
            <%--<a href="###">--%>
                <%--<dt>条目三</dt>--%>
            <%--</a>--%>
        <%--</dl>--%>


    <%--<li class="er">--%>
        <%--<img src="/static_new/images/navv_left_2.png">--%>
        <%--<span><a href="###">模块二</a></span>--%>
        <%--<s class="iconfont">&#xe61c;</s>--%>
    <%--</li>--%>
    <%--<dl>--%>
        <%--<a href="###">--%>
            <%--<dt>条目一</dt>--%>
        <%--</a>--%>
        <%--<a href="###">--%>
            <%--<dt>条目二</dt>--%>
        <%--</a>--%>
        <%--<a href="###">--%>
            <%--<dt>条目三</dt>--%>
        <%--</a>--%>
    <%--</dl>--%>
    <%--<li class="er">--%>
        <%--<img src="/static_new/images/navv_left_2.png">--%>
        <%--<span><a href="###">模块三</a></span>--%>
        <%--<s class="iconfont">&#xe61c;</s>--%>
    <%--</li>--%>
        <%--<dl>--%>
            <%--<a href="###">--%>
                <%--<dt>条目一</dt>--%>
            <%--</a>--%>
            <%--<a href="###">--%>
                <%--<dt>条目二</dt>--%>
            <%--</a>--%>
            <%--<a href="###">--%>
                <%--<dt>条目三</dt>--%>
            <%--</a>--%>
        <%--</dl>--%>
    </ul>
    <!--/.left-nav-->
    <!--.wcal-->
    <div class="left-cal" id="calId"></div>
    <!--/.wcal-->

    <!--.orange-col-->
    <c:choose>
        <c:when test="${!roles:isHeadmaster(sessionValue.userRole)||roles:isTeacher(sessionValue.userRole)}">
            <div class="orange-col" id="top5Container">
                <div class="col-head">
                    <h3>同学排行</h3>
                </div>
                <ul class="col-main paihan" id="top5studentcontainner">
                        <%--<li class="clearfix">--%>
                        <%--<img src="http://placehold.it/45x45" />--%>
                        <%--<span>xinxin</span>--%>
                        <%--<em>经验值<i>238</i></em>--%>
                        <%--</li>--%>
                </ul>
            </div>
        </c:when>
    </c:choose>


    <!--/.orange-col-->

</div>
<%--<script id="top5studenttemp" type="text/template">--%>
    <%--{{ for (var i = 0, l = it.length; i < l; i++) { }}--%>
    <%--<li class="clearfix">--%>
        <%--<img src="{{=it[i].imgUrl}}"/>--%>
        <%--<span>{{=it[i].userName}}</span>--%>
        <%--<em>经验值<i>{{=it[i].experienceValue}}</i></em>--%>
    <%--</li>--%>
    <%--{{}}}--%>
<%--</script>--%>
<script type="text/javascript">
    $(function(){
        cal('calId');
        leftNavSel();
        /*右侧头部*/
        $(".user-cener").hover(function() {
            $(this).children('ul').css({
                "display": 'block'
            });
        }, function() {
            $(this).children('ul').css({
                "display": 'none'
            });
        });
    });
    function cal(id){
        WdatePicker({
            eCont:id,
            onpicked:function(dp){
                location.href = '/calendar/day?date='+dp.cal.getDateStr()+'#eightclock';
            }
        })
    };

    function leftNavSel(){
        $.ajax({
            url: "/school/navs/load.do",
            type: "post",
            dataType: "json",
            data: {},
            success: function (rep) {
                if (rep != null && rep.length != 0) {
                    $('.left-nav').empty();
//                    $('.left-nav').append("<li class='homepage-left-one copy-two' id='nav_left_1'><img src='/static_new/images/navv_left_16.png'> <span><a href='/user/homepage.do?index=1'>首页</a></span></li>");
                    for (var i = 0; i < rep.length; i++) {
                        var con = "";

                        if (rep[i].dto.link) {
                            con = "<li class='pre' id='" + rep[i].dto.id + "'><img src='" + rep[i].dto.image + "'> <span><a href='" + rep[i].dto.link + "'>" + rep[i].dto.name + "</a></span></li><dl>";
                        }
                        else {
                            con = "<li class='er' id='" + rep[i].dto.id + "'><img src='" + rep[i].dto.image + "'> <span><a href='#'>" + rep[i].dto.name + "</a></span><s class='iconfont'>&#xe61c;</s></li><dl>";
                        }

                        if (rep[i].list != null && rep[i].list.length != 0) {
                            for (var j = 0; j < rep[i].list.length; j++) {
                                if (rep[i].list[j].name == "党建管理") {
                                    con += "<a href='" + rep[i].list[j].link + "' id='" + rep[i].list[j].id + "' target='_blank'><dt>" + rep[i].list[j].name + "</dt></a>";
                                } else {
                                    con += "<a href='" + rep[i].list[j].link + "' id='" + rep[i].list[j].id + "'><dt>" + rep[i].list[j].name + "</dt></a>";
                                }
                            }
                        }
                        con += "</dl>";
                        $('.left-nav').append(con);
                    }
                    var version = $("#versionValue").val();
                    var versionIdx =jQuery("#index_Value").val();

                    $("#nav_left_" + versionIdx).removeClass('er');
                    $("#nav_left_" + versionIdx).addClass('current');

                    if (versionIdx && version)
                    {
                        jQuery("#version_" +versionIdx+ "_"+version).css("color", "red");
                    }

                    $("#nav_left_" + versionIdx).next("dl").slideDown(400);

                    /*new*/

                    $(".left-nav li.er").click(function(event) {
                        $(this).toggleClass('re').siblings().removeClass('re').next("dl").slideUp(400);
                        $(".left-nav li.er").children('s').html("&#xe61c;");
                        $(".left-nav li.re").children('s').html("&#xe607;");
                        $('.left-nav li.current').addClass('ll');
                        $(".left-nav li.ll").children('s').html("&#xe61c;");
                        $(this).next("dl").slideToggle(400);
                    });
                    $(".left-nav li.current").click(function(event) {
                        $(".left-nav li.re").children('s').html("&#xe61c;");
                        $(this).toggleClass('ll').siblings().removeClass('re').next("dl").slideUp(400);
                        $(this).children('s').html("&#xe607;");
                        $(".ll").children('s').html("&#xe61c;");
                        $(this).next("dl").slideToggle(400);
                    });
                    /*new*/

                    $(".left-nav li#pre").click(function(event) {
                        $(this).addClass('current').siblings().removeClass('current');
                        $(".left-nav dl a").css({"color": '#666'});
                    });
                }
            }
        });
        //

        //私信数目
        $.ajax({
            url: "/letter/count.do",
            type: "post",
            dataType: "json",
            data: {},
            success: function (resp) {
                if (resp) {
                    if (resp > 0) {
                        $('a.user-msg').html('<i>' + resp + '</i>私信');
                    } else {
                        $('a.user-msg').html('私信');
                    }
                }
            }
        });


        //同学排行
        $.ajax({
            url: "/user/studenttopfive",
            type: "post",
            dataType: "json",
            data: {
            },
            success: function (resp) {
                if(resp.length==0)
                {
                    jQuery("#top5Container").hide();
                }
                else
                {
                    if (resp!=null && resp.length!=0) {
                        $('#top5studentcontainner').empty();
                        var con = "";
                        for (var i=0;i<resp.length;i++) {
                            con += '<li class="clearfix"><img src="'+resp[i].imgUrl+'"/><span>'+resp[i].userName+'</span><em>经验值<i>'+resp[i].experienceValue+'</i></em></li>';
                        }
                        $('#top5studentcontainner').append(con);
                    }
//                    Common.render({
//                        tmpl: '#top5studenttemp',
//                        data: resp,
//                        context: '#top5studentcontainner',
//                        overwrite: 1
//                    });
                }
            }
        });
    }
</script>
<%--<!-- initialize seajs Library -->--%>
<%--<script src="/static_new/js/sea.js"></script>--%>
<%--<!-- Custom js -->--%>
<%--<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>--%>
<%--<script>--%>
    <%--seajs.use('/static_new/js/modules/core/0.1.0/newCommon');--%>
<%--</script>--%>



