'use strict';
define(['jquery','doT','easing','common',"layer"],function(require,exports,module){
    (function ($) {
        var ms = {
            init: function (obj, args) {
                return (function () {
                    ms.fillHtml(obj, args);
                    ms.bindEvent(obj, args);
                })();
            },
            //填充html
            fillHtml: function (obj, args) {
                return (function () {
                    obj.empty();
                    //上一页
                    if (args.current > 1) {
                        obj.append('<a href="javascript:;" class="prevPage"><上一页</a>');
                    } else {
                        obj.remove('.prevPage');
                        obj.append('<span class="disabled"><上一页</span>');
                    }
                    //中间页码
                    if (args.current != 1 && args.current >= 4 && args.pageCount != 4) {
                        obj.append('<a href="javascript:;" class="tcdNumber">' + 1 + '</a>');
                    }
                    if (args.current - 2 > 2 && args.current <= args.pageCount && args.pageCount > 5) {
                        obj.append('<span>...</span>');
                    }
                    var start = args.current - 2, end = args.current + 2;
                    if ((start > 1 && args.current < 4) || args.current == 1) {
                        end++;
                    }
                    if (args.current > args.pageCount - 4 && args.current >= args.pageCount) {
                        start--;
                    }
                    for (; start <= end; start++) {
                        if (start <= args.pageCount && start >= 1) {
                            if (start != args.current) {
                                obj.append('<a href="javascript:;" class="tcdNumber">' + start + '</a>');
                            } else {
                                obj.append('<span class="current">' + start + '</span>');
                            }
                        }
                    }
                    if (args.current + 2 < args.pageCount - 1 && args.current >= 1 && args.pageCount > 5) {
                        obj.append('<span>...</span>');
                    }
                    if (args.current != args.pageCount && args.current < args.pageCount - 2 && args.pageCount != 4) {
                        obj.append('<a href="javascript:;" class="tcdNumber">' + args.pageCount + '</a>');
                    }
                    //下一页
                    if (args.current < args.pageCount) {
                        obj.append('<a href="javascript:;" class="nextPage">下一页></a>');
                    } else {
                        obj.remove('.nextPage');
                        obj.append('<span class="disabled">下一页></span>');
                    }
                    //跳转页码
                    if (args.turndown == 'true') {
                        obj.append('<span class="countYe">到第<input type="text" maxlength=' + args.pageCount.toString().length + '>页<a href="javascript:;" class="turndown">确定</a><span>');
                    }
                })();
            },
            //绑定事件
            bindEvent: function (obj, args) {
                return (function () {
                    obj.off("click", "a.tcdNumber");
                    obj.one("click", "a.tcdNumber", function () {
                        var current = parseInt($(this).text());
                        ms.fillHtml(obj, {"current": current, "pageCount": args.pageCount, "turndown": args.turndown});
                        if (typeof(args.backFn) == "function") {
                            args.backFn(current);
                        }
                    });
                    //上一页
                    obj.off("click", "a.prevPage");
                    obj.one("click", "a.prevPage", function () {
                        var current = parseInt(obj.children("span.current").text());
                        ms.fillHtml(obj, {
                            "current": current - 1,
                            "pageCount": args.pageCount,
                            "turndown": args.turndown
                        });
                        if (typeof(args.backFn) == "function") {
                            args.backFn(current - 1);
                        }
                    });
                    //下一页
                    obj.off("click", "a.nextPage");
                    obj.one("click", "a.nextPage", function () {
                        var current = parseInt(obj.children("span.current").text());
                        ms.fillHtml(obj, {
                            "current": current + 1,
                            "pageCount": args.pageCount,
                            "turndown": args.turndown
                        });
                        if (typeof(args.backFn) == "function") {
                            args.backFn(current + 1);
                        }
                    });
                    //跳转
                    obj.one("click", "a.turndown", function () {
                        var page = $("span.countYe input").val();
                        if (page > args.pageCount) {
                            layer.alert("您的输入有误，请重新输入！");
                        }
                        ms.fillHtml(obj, {"current": page, "pageCount": args.pageCount, "turndown": args.turndown});
                        /*if(typeof(args.backFn)=="function"){
                         args.backFn(current+1);
                         }*/
                    });
                })();
            }
        }
        $.fn.createPage = function (options) {
            var args = $.extend({
                pageCount: 10,
                current: 1,
                turndown: true,
                backFn: function () {
                }
            }, options);
            ms.init(this, args);
        }
    })(jQuery);
    var teamanage = {},
        Common = require('common');
    require('layer');
    var teamanageData = {};
    teamanage.init = function(){
        //教室导入模板
        $("#addTeacherImp").click(function(){
            window.location.href = "/teacher/addTeacherImp.do;"
        })
        $("#file").change(function () {
            var _this = $(this);
            var nameArr = _this.val().split("\\");
            var filename = nameArr[nameArr.length - 1];
                    $(".file-name").text(filename);
        })
        //导入
        $("#IpuTeacher").click(function(){
            $(".addgrade-alert,.bg").show();
        })
        $(".sure-grade").click(function () {
            var par = {};
            if ($("#file").val() != "") {
                var value = $("#file").val();
                var result = /\.[^\.]+/.exec(value);
                if(result==".xls"||result==".xlsx"){
                    $.ajaxFileUpload({
                        url: '/teacher/import.do',
                        param: par,
                        secureuri: false,
                        fileElementId: 'file',
                        dataType: 'json',
                        success: function (data) {
                            window.location.href = document.URL;
                        },
                        error: function (e) {
                            layer.msg("网络错误，请重试！！");
                        }
                    })
                }else{
                    layer.msg("请使用excel格式导入")
                }
            } else {
                layer.msg("请选择文件")
            }
        })


        teamanage.initYearMonth();
        teamanageData.page = 1;
        teamanageData.pageSize = 10;
        //---------------------教師管理--------------------------------------------------
        teamanage.getSubjectList();
        teamanage.selTeacherList();
        getDutyANDJobLevel();
        $(".step-1 .addTeacher").click(function(){
            $('.tea-manage .edit-nav li').each(function(i) {
                    if (i==0) {
                        $(this).addClass("edit-cur").siblings().removeClass("edit-cur");
                        $('.basic-data').show();
                        $('.per-inf').hide();
                        $('.work-experience').hide();
                    }
            });
            teamanage.clearInput();
            $(".tea-manage .show-con").hide();
            $(".tea-manage .edit-con").show();
        })
        //班级管理编辑导航栏切换
        teamanage.tabCheck("tea-manage","edit-nav","edit-main","div","edit-cur");
        //取消
        $(".btn-esc,.alert-r").click(function(){
            $(".alert-top").parent().hide();
            $(".bg").hide();
        })
        //返回教师管理首页
        $(".tea-manage .path-root").click(function(){
            $(".tea-manage .edit-con").hide();
            $(".tea-manage .show-con").show();
            refresh();
        });
        function refresh(){
            window.location.reload();
        }
        //添加教师经历
        $(".work-experience .con-top .addJob").click(function(){
            $('#workunit').val('');
            $('#startTime').val('');
            $('#endTime').val('');
            $('#jobDetail').val('');
            $('.alert-l').text("添加经历");
            $(".history-alert,.bg").show();
        });
        $('#seachTeacher').click(function() {
            teamanageData.page = 1;
            teamanage.selTeacherList();
        });

        $('.sure-teacherBase').click(function(){
            teamanageData.userName = $('#teacherName').val();
            var subs = "";
            teamanageData.sex = $("input[name='sex']:checked").val();
            $(".sub-list .li-select").each(function(i) {
                subs += $(this).attr('id') + ',';
            });
            teamanageData.subjectIds = subs;
            teamanageData.type = 1;
            if ($('#teacherName').val()=='') {
                layer.alert("请输入姓名！");
            } else {
                teamanage.addTeachInfo();
            }
        });

        $('.teacher-detail').click(function() {
            teamanageData.id = $('#resumeId').val();
            teamanageData.userId = $('#userId').val();
            teamanageData.birth = $('.year-select').val()+"/"+$('.month-select').val();
            teamanageData.birthPlace = $('#origin').val();
            teamanageData.nation = $('#nation').val();
            teamanageData.cardNum = $('#card').val();
            teamanageData.phone = $('#phone').val();
            teamanageData.address = $('#address').val();
            teamanageData.residence = $('#nowAddress').val();
            teamanageData.schoolName = $('#schoolName').val();
            teamanageData.education = $('#education').val();
            teamanageData.major = $('#major').val();
            teamanageData.maritalStatus = $("input[name='if-marry']:checked").val();
            if($('#postionStatus').val()){
            	teamanageData.status = $('#postionStatus').val();
            }
            else{
            	layer.alert("状态不能为空");
            	return;
            }
            teamanageData.type = 2;
            if($('#duty').val()){
            	teamanageData.dutyId = $('#duty').val();
            }
            else{
            	teamanageData.dutyId ="";
            }
            if($('#jobLevelId').val()){
            	teamanageData.jobLevelId = $('#jobLevelId').val();
            }
            else{
            	teamanageData.jobLevelId ="";
            }
            teamanage.addTeachInfo();
        });

        $('.job-sure').click(function() {
            teamanageData.organization = $('#workunit').val();
            teamanageData.startTime = $('#startTime').val();
            teamanageData.endTime = $('#endTime').val();
            teamanageData.detail = $('#jobDetail').val();
            teamanageData.userId = $('#userId').val();
            teamanageData.id = $('#jobId').val();
            teamanage.addTeacherJob();
        });
    }
    //-----------------------通用--------------------------------------------------------------------------------------------------------------------
    teamanage.checkUserName = function() {
        Common.getData('/shoolbase/checkUserName.do', teamanageData,function(rep){
            if (rep.code=='200') {
                if (rep.message!='') {
                    $('#subjectUserId').val(rep.message);
                } else {
                    layer.alert("用户名不存在！");
                    $('#subjectUserName').val('');
                }
            } else {
                layer.alert(rep.message);
            }
        });
    }
    //-------------教师管理--------------------------------------------------------------------------------------------------------------------------
    teamanage.clearInput = function() {
        $('#userId').val("");
        $('#teacherName').val("");
        $("input[name='sex']").eq(0).attr("checked","checked");
        $('#resumeId').val("");
        $("#year-select option:first").prop("selected", 'selected');
        $("#month-select option:first").prop("selected", 'selected');
        $('#origin').val("");
        $('#nation').val("");
        $('#card').val("");
        $('#phone').val("");
        $('#address').val("");
        $('#nowAddress').val("");
        $('#schoolName').val("");
        $('#education').val("");
        $('#major').val("");
        $("#postionStatus option:first").prop("selected", 'selected');
        $("input[name='if-marry']").eq(0).attr("checked","checked");
        $('.sub-list li').removeClass("li-select");
        $('.jobList').html('');
    }
    teamanage.selTeacherList = function() {
        teamanageData.gradeKeyword=$('#gradeKeyword').val();
        Common.getData('/teacher/selTeacherList.do', teamanageData,function(rep){
            if (rep.code=='200') {
                $('.teacherList').html('');
                Common.render({tmpl:$('#teacherList_templ'),data:rep,context:'.teacherList'});
                var totalPage = 0;
                if (rep.message.count % rep.message.pageSize == 0) {
                    totalPage = rep.message.count / rep.message.pageSize;
                } else {
                    totalPage = parseInt(rep.message.count / rep.message.pageSize) + 1;
                }
                $(".pageDiv").createPage({
                    pageCount:totalPage,//总页数
                    current:rep.message.page,//当前页
                    turndown:'false',//是否显示跳转框，显示为true，不现实为false,一定记得加上引号...
                    backFn:function(p){
                        if (teamanageData.page!=p) {
                            teamanageData.page = p;
                            teamanage.selTeacherList();
                        }

                    }
                });
                //教师管理编辑内容
                $(".editTeacher").click(function(){
                    $('.tea-manage .edit-nav li').each(function(i) {
                        if (i==0) {
                            $(this).addClass("edit-cur").siblings().removeClass("edit-cur");
                            $('.basic-data').show();
                            $('.per-inf').hide();
                            $('.work-experience').hide();
                        }
                    });
                    teamanageData.userId = $(this).attr('uid');
                    teamanage.selTeacherInfo();
                });
                $('.delTeacher').click(function() {
                    if (confirm('确认删除！')) {
                        teamanageData.id = $(this).attr('uid');
                        teamanage.delTeacher();
                    }
                });
            } else {
                layer.alert(rep.message);
            }
        });
    }
    teamanage.selTeacherInfo = function() {
        Common.getData('/teacher/selTeacherInfo.do', teamanageData,function(rep){
            if (rep.code=='200') {
                $('#userId').val(rep.message.userId);
                $('#teacherName').val(rep.message.userName);
                if(rep.message.sex==0) {
                    $("input[name='sex']").eq(1).attr("checked","checked");
                } else {
                    $("input[name='sex']").eq(0).attr("checked","checked");
                }
                $('#resumeId').val(rep.message.id);
                $('.year-select').val(rep.message.birth.split("/")[0]);
                $('.month-select').val(rep.message.birth.split("/")[1]);
                $('#origin').val(rep.message.birthPlace);
                $('#nation').val(rep.message.nation);
                $('#card').val(rep.message.cardNum);
                $('#phone').val(rep.message.phone);
                $('#address').val(rep.message.residence);
                $('#nowAddress').val(rep.message.address);
                $('#schoolName').val(rep.message.schoolName);
                $('#education').val(rep.message.education);
                $('#major').val(rep.message.major);
                $('#duty').val(rep.message.dutyId);
                $('#joblevel').val(rep.message.jobLevelId);
                $('#postionStatus').val(rep.message.status);
                $("input[name='if-marry']").eq(rep.message.maritalStatus).attr("checked","checked");
                if (rep.message.subjectIdList!=null && rep.message.subjectIdList.length!=0) {
                    $('.sub-list li').removeClass("li-select");
                    $('.sub-list li').each(function(i) {
                        for (var i=0;i<rep.message.subjectIdList.length;i++) {
                            if ($(this).attr('id')==rep.message.subjectIdList[i]) {
                                $(this).toggleClass("li-select");
                            }
                        }
                    });
                }
                $(".tea-manage .show-con").hide();
                $(".tea-manage .edit-con").show();
            }
        });
    }
    teamanage.delTeacher = function() {
        Common.getData('/teacher/delTeacher.do', teamanageData,function(rep){
            if (rep.code=='200') {
                teamanage.selTeacherList();
            }
        });
    }
    teamanage.addTeachInfo  = function() {
        Common.getData('/teacher/addOrUdpTeacherBase.do', teamanageData,function(rep){
            if (rep.code=='200') {
                $('#resumeId').val(rep.message.resumeId);
                $('#userId').val(rep.message.userId);
                layer.msg("编辑成功！");
            } else {
                layer.alert(rep.message);
            }
        });
    }

    teamanage.addTeacherJob = function() {
        Common.getData('/teacher/addTeacherJob.do', teamanageData,function(rep){
            if (rep.code=='200') {
                $('.history-alert').hide();
                $('.bg').hide();
                teamanage.selTeacherJob();
                layer.msg("编辑成功！");
            } else {
                layer.alert(rep.message);
            }
        });
    }
    teamanage.selTeacherJob = function() {
        teamanageData.userId = $('#userId').val();
        Common.getData('/teacher/selTeacherJob.do', teamanageData,function(rep){
            if (rep.code=='200') {
                $('.jobList').html('');
                Common.render({tmpl:$('#jobList_templ'),data:rep,context:'.jobList'});
                $(".editTeacherJob").click(function(){
                    teamanageData.jobId = $(this).attr('jid');
                    teamanage.getTeacherJob();
                });
                $('.delTeacherJob').click(function() {
                    if (confirm('确认删除！')) {
                        teamanageData.jobId = $(this).attr('jid');
                        teamanage.delTeacherJob();
                    }
                });
            } else {
                layer.alert(rep.message);
            }
        });
    }
    teamanage.delTeacherJob = function() {
        Common.getData('/teacher/delTeacherJob.do', teamanageData,function(rep){
            if (rep.code=='200') {
                teamanage.selTeacherJob();
            }
        });
    }
    teamanage.getTeacherJob = function() {
        Common.getData('/teacher/getTeacherJob.do', teamanageData,function(rep){
            if (rep.code=='200') {
                $('.alert-l').text("编辑经历");
                $('#workunit').val(rep.message.organization);
                $('#startTime').val(rep.message.startTime);
                $('#endTime').val(rep.message.endTime);
                $('#jobDetail').val(rep.message.detail);
                $('#userId').val(rep.message.userId);
                $('#jobId').val(rep.message.id);
                $('.history-alert').show();
                $('.bg').show();
            } else {
                layer.alert(rep.message);
            }
        });
    }
    teamanage.getSubjectList = function() {
        Common.getData('/subject/getSubjectList.do', teamanageData,function(rep){
            $('.sub-list').html('');
            Common.render({tmpl:$('#sub-list_templ'),data:rep,context:'.sub-list'});
            //教师管理-选择学科
            $(".sub-list li").click(function(){
                $(this).toggleClass("li-select");
            });
        });
    }
    //导航栏切换
    teamanage.tabCheck = function(pars,par,wrap,child,className){
        var $li=$("."+pars+" ."+par).find("li");
        var $tab=$("."+pars+" ."+wrap).children(child);
        var len=$li.length;
        $li.click(function(){
            if ($('#userId').val()!='') {
                var $index=$(this).index();
                $(this).addClass(className).siblings().removeClass(className);
                $tab.hide();
                $tab.eq($index).show();
                if ($index==2) {
                    teamanage.selTeacherJob();
                }
            } else {
                layer.alert("请先创建基础信息！");
            }
        })
    }

    teamanage.initYearMonth = function() {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        for (var i = 0; i < 50; i++) {
            var oP = document.createElement("option");
            var oText = document.createTextNode(y);
            oP.appendChild(oText);
            oP.setAttribute("value", y);
            document.getElementById('year-select').appendChild(oP);
            y = y - 1;
        };
        var j = 1;
        for (i = 1; i < 13; i++) {
            var month = document.createElement("option");
            var monthText = document.createTextNode(j);
            month.appendChild(monthText);
            month.setAttribute("value", j);
            if (j == m) {
                month.setAttribute("selected", "selected");
            };
            document.getElementById('month-select').appendChild(month);
            j = j + 1;

        };
    }
    function getDutyANDJobLevel(){
    	 Common.getData('/teacher/dutyandjoblevel.do', null,function(rep){
             if (rep.code=='200') {
            	 var level = rep.message.level;
            	 var duty = rep.message.duty;
            	 Common.render({tmpl:$('#joblevel_templ'),data:level,context:'#joblevel'});
            	 Common.render({tmpl:$('#duty_templ'),data:duty,context:'#duty'});
             } else {
                 layer.alert(rep.message);
             }
         });
    }
    teamanage.init();
});