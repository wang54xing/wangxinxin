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
    var studentManage = {},
        Common = require('common');
    require('layer');
    var studentManageData = {};
    studentManage.init = function(){

        $("body").on("click",".CodeStu",function(){
            var par={};
            par.id=$(this).attr("ids");
            layer.prompt({
                formType: 0,
                value: '',
                title: '请输入学籍号'
            }, function (value, index, elem) {
                if (value.trim() != "") {
                    par.StuCode = value;
                    layer.close(index);
                    Common.getData('/student/updateStuCode.do', par, function (rep) {
                        studentManage.selStudentListByClassId($('#classId').val());
                    })
                } else {
                    layer.msg("请输入名字")
                }
            });
        })
        studentManageData.page = 1;
        studentManageData.pageSize = 10;

        //--------------------------學生---------------------------------------------------------------------------------
        studentManage.getGradeList();
        studentManage.getClassesByGrade();
        studentManage.getMembershipTypeList();
        $('#studentGrades').click(function() {
            studentManageData.page = 1;
            studentManage.getClassesByGrade();
        });
        //添加学生
        $('.stu-tb button').click(function(){
            $('#studentText').text("添加学生");
            $("input[name='rena']").eq(0).click();
            $('#stuName').val('');
            $('#studentCode').val('');
            $('#membership').val(1);
            $('#stuDate').val('');
            $('#studentId').val('');
            $('#stuGrade').text($('#studentGrades option:selected').text());
            $('.stu-add').show();
            $('.bg').show();
        })
        $('.group-qx').click(function(){
            $('.stu-add').hide();
            $('.grade-edit-popup').hide();
            $('.term-add').hide();
            $('.educa-del-popup').hide();
            $('.educa-add-popup').hide();
            $('.bg').hide();
        })
        $('.stu-add-top i').click(function(){
            $('.stu-add').hide();
            $('.grade-edit-popup').hide();
            $('.term-add').hide();
            $('.educa-del-popup').hide();
            $('.educa-add-popup').hide();
            $('.bg').hide();
        });
        $('.add-user').click(function() {
            studentManageData.userName = $('#stuName').val();
            if ($('#stuName').val()=='') {
                alert("请输入学生姓名！");
                return;
            }
            studentManageData.sex = $("input[name='rena']:checked").val();
            studentManageData.studentCode = $('#studentCode').val();
            studentManageData.membership=$('#membership').val();
            studentManageData.intake = $('#stuDate').val();
            studentManageData.classId = $('.classList3').val();
            studentManageData.orgClassId = $('#classId').val();
            studentManageData.userId = $('#studentId').val();
            studentManage.addUdpStudent();
        });

        $('#stuSearch').click(function() {
            studentManageData.page = 1;
            studentManage.selStudentListByClassId($('#classId').val());
        });
    }
    //-----------------------通用--------------------------------------------------------------------------------------------------------------------
    studentManage.checkUserName = function() {
        Common.getData('/shoolbase/checkUserName.do', studentManageData,function(rep){
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

    //-------------年级管理--------------------------------------------------------------------------------------------------------------------------

    studentManage.getGradeList = function() {
        Common.getData('/schoolGrade/selGradeList.do', studentManageData,function(rep){
            $('#studentGrades').html('');
            Common.render({tmpl:$('#studentGrades_templ'),data:rep,context:'#studentGrades'});
        });
    }

    //-------------------班级-----------------------------------------------------------------------------------------------------------------------------------
    studentManage.getClassesByGrade = function() {
        if ($('#studentGrades').val()!=null) {
            studentManageData.gradeId = $('#studentGrades').val();
            var gradeNum = $('#studentGrades option:selected').attr('gnm');
            console.log(gradeNum)
            Common.getData('/schoolClass/getClassesByGrade.do', studentManageData,function(rep){
                if (rep.code=='200') {
                    $('.classList2').html('');
                    Common.render({tmpl:$('#classList2_templ'),data:rep,context:'.classList2'});
                    $('.classList3').html('');
                    Common.render({tmpl:$('#classList3_templ'),data:rep,context:'.classList3'});
                    $('#addStu').hide();
                    $('.studentList').html('');
                    if (rep.message.length>0) {
                        studentManage.selStudentListByClassId($('#classId').val());
                        if (!isNaN(gradeNum) && Number(gradeNum) != -1) {
                        	$('#addStu').show();
                        }
                    }
                    $('.stu-list li').click(function(){
                        $(this).addClass('stu-li-ho').siblings().removeClass('stu-li-ho');
                        $('#classId').val($(this).attr("cid"));
                        studentManageData.page = 1;
                        studentManage.selStudentListByClassId($(this).attr("cid"));
                    })
                } else {
                    layer.alert(rep.message);
                }
            });
        }
    }

    studentManage.getMembershipTypeList = function() {
        Common.getData('/student/getMembershipTypeList.do', {},function(rep){
            if (rep.code=='200') {
                $('#membership').html('');
                Common.render({tmpl:$('#typeList_templ'),data:rep,context:'#membership'});
            } else {
                layer.alert(rep.message);
            }
        });
    }



    //-----------------学生-----------------------------------------------------------------------------------------------------------------------------
    studentManage.addUdpStudent = function() {
        Common.getData('/student/addUdpStudent.do', studentManageData,function(rep){
            if (rep.code=='200') {
                $('.stu-add').hide();
                $('.bg').hide();
                studentManage.selStudentListByClassId($('#classId').val());
            } else {
                layer.alert(rep.message);
            }
        });
    }

    studentManage.selStudentListByClassId = function(id) {
        if (id!='undefined') {
            studentManageData.classId = id;
            studentManageData.keyword = $('#keyword').val();
            studentManageData.pageSize=10000;
            Common.getData('/student/selStudentListByClassId.do', studentManageData,function(rep){
                if (rep.code=='200') {
                    $('.studentList').html('');
                    Common.render({tmpl:$('#studentList_templ'),data:rep,context:'.studentList'});
                    //var totalPage = 0;
                    //if (rep.message.count % rep.message.pageSize == 0) {
                    //    totalPage = rep.message.count / rep.message.pageSize;
                    //} else {
                    //    totalPage = parseInt(rep.message.count / rep.message.pageSize) + 1;
                    //}
                    //$(".pageDiv").createPage({
                    //    pageCount:totalPage,//总页数
                    //    current:rep.message.page,//当前页
                    //    turndown:'false',//是否显示跳转框，显示为true，不现实为false,一定记得加上引号...
                    //    backFn:function(p){
                    //        if (studentManageData.page!=p) {
                    //            studentManageData.page = p;
                    //            studentManage.selStudentListByClassId(id);
                    //        }
                    //
                    //    }
                    //});
                    $('.table-edit').click(function() {
                        studentManage.getStudent($(this).attr('uid'),$(this).attr('cid'));
                    });
                    $('.del-student').click(function() {
                        if (confirm('确认删除！')) {
                            studentManage.delStudent($(this).attr('uid'),$(this).attr('cid'));
                        }
                    });
                } else {
                    layer.alert(rep.message);
                }
            });
        }
    }
    studentManage.getStudent = function(id,cid) {
        studentManageData.userId = id;
        studentManageData.classId = cid;
        Common.getData('/student/selStudentInfo.do', studentManageData,function(rep){
            if (rep.code=='200') {
                $('#studentText').text("编辑学生");
                $('#stuGrade').text($('#studentGrades option:selected').text());
                $('#stuName').val(rep.message.nickName);
                if(rep.message.sex==0) {
                    //$("input[name='rena']").eq(1).attr("checked","checked");
                    $("input[name='rena']").eq(1).click();
                } else {
                    //$("input[name='rena']").eq(0).attr("checked","checked");
                    $("input[name='rena']").eq(0).click();
                }
                $('#studentCode').val(rep.message.studentCode);
                $('#membership').val(rep.message.membership);
                $('#stuDate').val(rep.message.intake);
                $('.classList3').val(rep.message.classId);
                $('#studentId').val(rep.message.userId);
                $('.stu-add').show();
                $('.bg').show();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    studentManage.delStudent = function(id,cid) {
        studentManageData.userId = id;
        studentManageData.classId = cid;
        Common.getData('/student/delStudent.do', studentManageData,function(rep){
            if (rep.code=='200') {
                studentManage.selStudentListByClassId($('#classId').val());
            } else {
                layer.alert(rep.message);
            }
        });
    }


    studentManage.init();
});