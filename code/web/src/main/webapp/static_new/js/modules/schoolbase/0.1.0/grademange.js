'use strict';
define(['jquery','doT','easing','common','select2',"layer"],function(require,exports,module){
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
    var gradeManage = {},
        Common = require('common');
    require('select2');
    require('layer');
    var gradeManageData = {};
    gradeManage.init = function(){
        gradeManageData.page = 1;
        gradeManageData.pageSize = 10;

        //----------------------年級管理-----------------------------------------------------------------------------------
        gradeManage.getGradeList();
        gradeManage.getSchoolPeriod();
        //年级管理 添加教师
        $(".grade-manage .add-teacher").click(function(){
            $('#sgtkeyword').val("");
            $("#subjectInfo option:first").prop("selected", 'selected');
            $('#gradeNm').text($('#gradeNameT').text());
            gradeManage.getSubjectList();
            gradeManageData.keyword = $('#sgtkeyword').val();
            gradeManageData.gradeId = $('#gradeIdT').val();
            gradeManageData.subjectId = $('#subjectInfo').val();
            gradeManage.selTeachersByGradeSubject();
            $(".addteacher-alert,.bg").show();

        });
        $("#histroy_grade").click(function(){
        	if($("#histroy_grade").html()=="更多历史年级"){
        		Common.getData('/schoolGrade/selHistroyGradeList.do', gradeManageData,function(rep){
                    $('.gradeList').html('');
                    Common.render({tmpl:$('#gradeList_templ2'),data:rep,context:'.gradeList'});
                   

                   
                    $('.td-position .grade-ck').hover(function(){
                        $(this).parent().find(".hide-tea").show();
                    },function(){
                        $(this).parent().find(".hide-tea").hide();
                    })
                    $('.add-grade').hide();
                    $('.grade-top-lt').hide();
                    $("#histroy_grade").html("更多当前年级");
                });
        	}
        	else{
        		Common.getData('/schoolGrade/selGradeList.do', gradeManageData,function(rep){
                    $('.gradeList').html('');
                    Common.render({tmpl:$('#gradeList_templ'),data:rep,context:'.gradeList'});
                    $('.gtlist2').html('');
                    Common.render({tmpl:$('#gtlist2_templ'),data:rep,context:'.gtlist2'});

                    $('.editGrade').click(function() {
                        gradeManageData.gradeId = $(this).attr('gid');
                        gradeManage.getGrade();
                    });
                    $('.td-position .grade-ck').hover(function(){
                        $(this).parent().find(".hide-tea").show();
                    },function(){
                        $(this).parent().find(".hide-tea").hide();
                    })
                    //年级管理 编辑
                    $(".grade-manage .grade-edit").click(function(){
                        gradeManage.getGradeTeacher($(this).attr('gid'),$(this).attr('gnm'));
                    })
                    $('.add-grade').show();
                    $('.grade-top-lt').show();
                    $("#histroy_grade").html("更多历史年级");
                });
        	}
        	 
        });
        $('.addGT').click(function() {
            var userArg = "";
            var subjectArg = "";
            $('.checkGt').each(function() {
                if ($(this).is(':checked')) {
                    userArg += $(this).attr('uid') +",";
                    subjectArg += $(this).attr('suid') +",";
                }
            });
            if (userArg!=''&& subjectArg!='') {
                gradeManageData.id = $('#gradeIdT').val();
                gradeManageData.userIds = userArg;
                gradeManageData.subjectIds = subjectArg;
                gradeManage.addGradeTeacher();
            }
        });
        //取消
        $(".btn-esc,.alert-r").click(function(){
            $(".alert-top").parent().hide();
            $(".bg").hide();
        })
        $('#subjectInfo').change(function() {
            gradeManageData.keyword = $('#sgtkeyword').val();
            gradeManageData.gradeId = $('#gradeIdT').val();
            gradeManageData.subjectId = $('#subjectInfo').val();
            gradeManage.selTeachersByGradeSubject();
        });
        //返回年级管理首页
        $('.grade-manage .return-div img').click(function(){
            gradeManage.getGradeList();
            $(".hide-edit").hide();
            $(".show-list").show();
        })
        $('#gseach').click(function() {
            gradeManageData.keyword = $('#sgtkeyword').val();
            gradeManageData.gradeId = $('#gradeIdT').val();
            gradeManageData.subjectId = $('#subjectInfo').val();
            gradeManage.selTeachersByGradeSubject();
        });
        $('#gseach2').click(function() {
            gradeManageData.page = 1;
            gradeManage.getGradeTeacher($('#gradeIdT').val(),$('#gradeNameT').text());
        });
        //添加年级
        $('.add-grade').click(function(){
            $('.gradeTitle').text("添加年级");
            $('#period').text('');
            $('#gradeId').val('');
            $('#gradeName').val('');
            $('#gradeDate').val('');
            $("#graduateDate_div").hide();
            $('#gradeCount').val(0);
            $('#gradeCount').attr('disabled',false);
            gradeManage.getSelectTeacherList($('.edit-grade-teacher'),function() {
                $('.edit-grade-teacher').select2("destroy");
                initSelect($('.edit-grade-teacher'));
            });
            $(".addgrade-alert,.bg").show();
            
        });
        $('.group-qx').click(function(){
            $('.stu-add').hide();
            $('.grade-edit-popup').hide();
            $('.term-add').hide();
            $('.educa-del-popup').hide();
            $('.educa-add-popup').hide()
            $('.bg').hide();
        });
        $('.stu-add-top i').click(function(){
            $('.stu-add').hide();
            $('.grade-edit-popup').hide();
            $('.term-add').hide();
            $('.educa-del-popup').hide();
            $('.educa-add-popup').hide();
            $('.bg').hide();
        });
        $('.sure-grade').click(function() {
            gradeManageData.gradeName = $('#gradeName').val();
            gradeManageData.admissionDate = $('#gradeDate').val();
            if ($('#gradeId').val()!='') {
            	gradeManageData.graduateDate = $('#graduateDate').val();
            } else {
            	gradeManageData.graduateDate = graduateDate();
            }
           
            gradeManageData.classCount = $('#gradeCount').val();
            gradeManageData.type = $('#sel_period').val();
            //gradeManageData.gradeUserName = $('#gradeUserName').val();
            gradeManageData.gradeUserId = $('.edit-grade-teacher').select2("val");
            if ($('#gradeName').val()=='') {
                layer.alert("请输入年级名称！");
                return;
            }
            if ($('#sel_period').val()=='') {
                layer.alert("请选择学段！");
                return;
            }
            if ($('#gradeDate').val()=='') {
                layer.alert("请输入入学时间！");
                return;
            }
            if (gradeManageData.graduateDate=='') {
                layer.alert("请输入毕业时间！");
                return;
            }
            if ($('#gradeCount').val()=='') {
                layer.alert("请输入班级总数！");
                return;
            }
            if ($('#gradeId').val()!='') {
                gradeManageData.id = $('#gradeId').val();
                gradeManage.updateGrade();
            } else {
                gradeManage.addGrade();
            }
        });
        $('.group-qd').click(function() {
            var bol = false;
            var gids = "";
            var gnms = "";
            $('.gtlist2 tr').each(function(i) {
                if ($(this).find('input').val()=='') {
                    bol = true;
                }
                gids += $(this).attr('gid')+",";
                gnms += $(this).find('input').val()+",";
            });
            if (!bol) {
                gradeManageData.gradeIds = gids;
                gradeManageData.gradeNames = gnms;
                gradeManage.updateGrades();
            } else {
                layer.alert("年级名称不为空！");
            }
        });
        //$('#gradeUserName').blur(function() {
        //    if ($(this).val()!='') {
        //        gradeManageData.userName = $('#gradeUserName').val();
        //        gradeManage.checkUserName();
        //    }
        //});

        /*********设置年级名称***********/
        $('.grade-top-lt').click(function(){
            $('.grade-edit-popup').show();
            $('.bg').show();
        });
       
    }
    //-----------------------通用--------------------------------------------------------------------------------------------------------------------
    gradeManage.checkUserName = function() {
        Common.getData('/shoolbase/checkUserName.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                if (rep.message!=null) {
                    $('#gradeUserId').val(rep.message);
                } else {
                    layer.alert("用户名不存在！");
                    $('#gradeUserName').val('');
                }
            } else {
                layer.alert(rep.message);
            }
        });
    }

    //-------------年级管理--------------------------------------------------------------------------------------------------------------------------
    gradeManage.addGradeTeacher = function() {
        Common.getData('/schoolGrade/addGradeTeacher.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                $(".addteacher-alert,.bg").hide();
                gradeManage.getGradeTeacher($('#gradeIdT').val(),$('#gradeNameT').text());
            }
        });
    }
    gradeManage.updateGrades = function() {
        Common.getData('/schoolGrade/updateGrades.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                $('.grade-edit-popup').hide();
                $('.bg').hide();
                gradeManage.getGradeList();
            }
        });
    }
    gradeManage.selTeachersByGradeSubject = function() {
        Common.getData('/schoolGrade/selTeachersByGradeSubject.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                $('.gtlist').html('');
                Common.render({tmpl:$('#gtlist_templ'),data:rep,context:'.gtlist'});
            }
        });
    }
    gradeManage.addGrade = function() {
        Common.getData('/schoolGrade/addGrade.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                $('.addgrade-alert').hide();
                $('.bg').hide();
                gradeManage.getGradeList();
            }
        });
    }
    gradeManage.updateGrade = function() {
        Common.getData('/schoolGrade/updateGrade.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                $('.addgrade-alert').hide();
                $('.bg').hide();
                gradeManage.getGradeList();
            }
        });
    }
    gradeManage.getGradeList = function() {
        Common.getData('/schoolGrade/selGradeList.do', gradeManageData,function(rep){
            $('.gradeList').html('');
            Common.render({tmpl:$('#gradeList_templ'),data:rep,context:'.gradeList'});
            $('.gtlist2').html('');
            Common.render({tmpl:$('#gtlist2_templ'),data:rep,context:'.gtlist2'});

            $('.editGrade').click(function() {
                gradeManageData.gradeId = $(this).attr('gid');
                gradeManage.getGrade();
            });
            $('.td-position .grade-ck').hover(function(){
                $(this).parent().find(".hide-tea").show();
            },function(){
                $(this).parent().find(".hide-tea").hide();
            })
            //年级管理 编辑
            $(".grade-manage .grade-edit").click(function(){
                gradeManage.getGradeTeacher($(this).attr('gid'),$(this).attr('gnm'));
            })
        });
    }
    gradeManage.getGradeTeacher = function(id,name) {
        gradeManageData.id = id;
        gradeManageData.keyword = $('#gtkeyword').val();
        Common.getData('/schoolGrade/getGradeTeacher.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                if (name.indexOf("教师")==-1) {
                    $('#gradeNameT').text(name+"教师");
                }
                $('#gradeIdT').val(id);
                $('.gradeTeacher').html('');
                Common.render({tmpl:$('#gradeTeacher_templ'),data:rep,context:'.gradeTeacher'});
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
                        if (gradeManageData.page!=p) {
                            gradeManageData.page = p;
                            gradeManage.getGradeTeacher(id,name);
                        }

                    }
                });
                $(".show-list").hide();
                $(".hide-edit").show();
                $('.grade-head').click(function() {
                    gradeManageData.id = $('#gradeIdT').val();
                    gradeManage.setGradeLeader($(this).attr('uid'));
                });
                $('.table-del').click(function() {
                    if (confirm('确认删除年级老师！')) {
                        gradeManage.delGradeTeacher($(this).attr('id'));
                    }
                });
            }
        });
    }
    gradeManage.delGradeTeacher = function(id) {
        gradeManageData.id = id;
        Common.getData('/schoolGrade/delGradeTeacher.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                gradeManage.getGradeTeacher($('#gradeIdT').val(),$('#gradeNameT').text());
            } else {
                layer.alert(rep.message);
            }
        });
    }
    gradeManage.setGradeLeader = function(uid) {
        gradeManageData.gradeUserId = uid;
        Common.getData('/schoolGrade/setGradeLeader.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                gradeManage.getGradeTeacher($('#gradeIdT').val(),$('#gradeNameT').text());
            } else {
                layer.alert(rep.message);
            }
        });
    }
    gradeManage.getGrade = function() {
        Common.getData('/schoolGrade/selGradeInfo.do', gradeManageData,function(rep){
            if (rep.code=='200') {
                $('.gradeTitle').text("编辑年级");
                $('#period').text(rep.message.type);
                if(rep.message.type!=null){
                	 $('#sel_period').find("option[text='"+rep.message.type+"']").attr("selected",true);
                }
                else{
                	$('#sel_period').val("");
                }
                $('#gradeCount').attr('disabled',true);
                $('#gradeId').val(rep.message.id);
                $('#gradeName').val(rep.message.gradeName);
                $('#gradeDate').val(rep.message.admissionDate);
                $("#graduateDate_div").show();//毕业日期可编辑
                $('#graduateDate').val(rep.message.graduateDate);
                $('#gradeCount').val(rep.message.classCount);
                //$('#gradeUserName').val(rep.message.gradeUserName);
                gradeManage.getSelectTeacherList($('.edit-grade-teacher'),function() {
                    //年级组长控制
                    var childs=$('.edit-grade-teacher').children();
                    for(var i=0;i<childs.length;i++){
                        if(childs[i].value==rep.message.gradeUserId){
                            childs[i].selected='selected';
                        }
                    }
                    $('.edit-grade-teacher').select2("destroy");
                    initSelect($('.edit-grade-teacher'));
                });
                $('.addgrade-alert').show();
                $('.bg').show();
            }
        });
    }



    //导航栏切换
    gradeManage.tabCheck = function(pars,par,wrap,child,className){
        var $li=$("."+pars+" ."+par).find("li");
        var $tab=$("."+pars+" ."+wrap).children(child);
        var len=$li.length;
        $li.click(function(){
            var $index=$(this).index();
            $(this).addClass(className).siblings().removeClass(className);
            $tab.hide();
            $tab.eq($index).show();
        })
    }
    gradeManage.getSubjectList = function() {
        Common.getData('/subject/getSubjectList.do', gradeManageData,function(rep){
            $('#subjectInfo').html('');
            Common.render({tmpl:$('#subjectInfo_templ'),data:rep,context:'#subjectInfo'});
        });
    }

    gradeManage.getSelectTeacherList = function(target,callback) {
        target.empty();
        target.append('<option value="0">请选择...</option>');
        Common.getData('/teacher/selTeachersBySchoolId.do', gradeManageData,function(rep){
            var obj = eval('(' + rep.message + ')');
            for (var i = 0; i < obj.message.length; i++) {
                var content = '';
                content += '<option value=' + obj.message[i].userId + '>' + obj.message[i].userName + '</option>';
                target.append(content);
            }
            callback();
        });

    }
    // 获得当前学校学段信息
    gradeManage.getSchoolPeriod = function() {
        
        Common.getData('/shoolbase/getCurrentSchool.do', null,function(resp){
            if(resp.code==200){
            	var periods = resp.message.periods;
            	for(var i in periods){
            		if(periods[i].period=="primary"||periods[i].period=="小学"){
            			periods[i].name="小学";
                        periods[i].period="primary";
            		}
            		if(periods[i].period=="junior"||periods[i].period=="初中"){
            			periods[i].name="初中";
                        periods[i].period="junior";
            		}
            		if(periods[i].period=="senior"||periods[i].period=="高中"){
            			periods[i].name="高中";
                        periods[i].period="senior";
            		}
            	}
            	Common.render({tmpl:$('#sel_period_templ'),data:periods,context:'#sel_period'});
            }
            
        });

    }
   
    function graduateDate(){
    	var year = $('#sel_period>option:selected').attr("year");
    	var adYear =  $('#gradeDate').val().split("-")[0];
    	var grYear = parseInt(adYear)+parseInt(year);
    	return grYear+"-06-30";
    }
    // 初始化select2
    function initSelect(target) {
        target.select2({
            width: '250px',
            containerCss: {
                'margin-left': '0px',
                'font-family': 'sans-serif'
            },
            dropdownCss: {
                'font-size': '14px',
                'font-family': 'sans-serif'
            }
        });
    }
    
    exports.gradeManage = gradeManage;
    
    gradeManage.init();
});