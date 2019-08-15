'use strict';
define(['jquery', 'doT', 'easing', 'common', 'uploadify', 'zTree', 'zTreeCore', 'zTreeExedit', 'zTreeExcheck', "layer", "orgchart"], function (require, exports, module) {
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
    var edumanage = {},
        Common = require('common');
    require('layer');
    require('zTree');
    require('zTreeCore');
    require('zTreeExedit');
    require('zTreeExcheck');
    require('orgchart');
    var pid = "*";
    var ps;
    var nnm;
    var ct;
    var teamanageData = {};
    var edumanageData = {};
    var roleParam = {};
    edumanageData.page = 1;
    edumanageData.pageSize = 10;
    teamanageData.pageSize = 10;
    teamanageData.keyword = "keyword"
    /*var setting = {
     data: {
     simpleData: {
     enable: true
     }
     }
     };*/

    var setting = {
        view: {
            selectedMulti: false,
            dblClickExpand: true
        },
        edit: {
            enable: true,
            showRemoveBtn: false,
            showRenameBtn: false
        },
        data: {
            keep: {
                parent: true,
                leaf: false
            },
            simpleData: {
                enable: true
            }
        },
        callback: {}
    };
    edumanage.getGroupUsersByIdents = function () {
        var selDate = {};
        selDate.gid = pid;
        selDate.userName = "";
        selDate.identName = $("input[name='identNames']:checked").val();
        Common.getData('/groupMge/getRoleUsersByIdent.do', selDate, function (rep) {
            $("#myLists").html("");
            if (rep.code == '200') {
                Common.render({tmpl: $('#j-tmpls'), data: rep.message, context: '#myLists'});
            }
        });
    };
    edumanage.selTeacherInfo = function () {
        Common.getData('/teacher/selStaffInfo.do', teamanageData, function (rep) {
            if (rep.code == '200') {
                $('#userId').val(rep.message.userId);
                $('#teacherName').val(rep.message.userName);
                if (rep.message.sex == 0) {
                    $("input[name='sex']").eq(1).attr("checked", "checked");
                } else {
                    $("input[name='sex']").eq(0).attr("checked", "checked");
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
                $('#postionStatus').val(rep.message.status);
                $("input[name='if-marry']").eq(rep.message.maritalStatus).attr("checked", "checked");
                $(".tea-manage .show-con").hide();
                $(".tea-manage .edit-con").show();
            }
        });
    }
    edumanage.selTeacherList = function () {
        teamanageData.gradeKeyword = $('#gradeKeyword').val();
        Common.getData('/teacher/selStaffList.do', teamanageData, function (rep) {
            if (rep.code == '200') {
                $('.teacherList').html('');
                Common.render({tmpl: $('#teacherList_templ'), data: rep, context: '.teacherList'});
                var totalPage = 0;
                if (rep.message.count % rep.message.pageSize == 0) {
                    totalPage = rep.message.count / rep.message.pageSize;
                } else {
                    totalPage = parseInt(rep.message.count / rep.message.pageSize) + 1;
                }
                $(".pageDiv2").createPage({
                    pageCount: totalPage,//总页数
                    current: rep.message.page,//当前页
                    turndown: 'false',//是否显示跳转框，显示为true，不现实为false,一定记得加上引号...
                    backFn: function (p) {
                        if (teamanageData.page != p) {
                            teamanageData.page = p;
                            edumanage.selTeacherList();
                        }

                    }
                });
                //教师管理编辑内容
                $(".editTeacher").click(function () {
                    $('.tea-manage .edit-nav li').each(function (i) {
                        if (i == 0) {
                            $(this).addClass("edit-cur").siblings().removeClass("edit-cur");
                            $('.basic-data').show();
                            $('.per-inf').hide();
                            $('.work-experience').hide();
                        }
                    });
                    teamanageData.userId = $(this).attr('uid');
                    edumanage.selTeacherInfo();
                });
                $('.delTeacher').click(function () {
                    teamanageData.id = $(this).attr('uid');
                    layer.confirm('确定要删除吗', {
                        btn: ['确定', '取消'] //按钮
                    }, function () {
                        layer.msg('删除中。。。', {time: 1000});
                        edumanage.delTeacher();
                    }, function () {
                    });
                });
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.delTeacher = function () {
        Common.getData('/teacher/delStaff.do', teamanageData, function (rep) {
            if (rep.code == '200') {
                edumanage.selTeacherList();
            }
        });
    }
    edumanage.addTeacherJob = function () {
        Common.getData('/teacher/addTeacherJob.do', teamanageData, function (rep) {
            if (rep.code == '200') {
                $('.history-alert').hide();
                $('.bg').hide();
                edumanage.selTeacherJob();
                layer.msg("编辑成功！");
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.selTeacherJob = function () {
        teamanageData.userId = $('#userId').val();
        Common.getData('/teacher/selTeacherJob.do', teamanageData, function (rep) {
            if (rep.code == '200') {
                $('.jobList').html('');
                Common.render({tmpl: $('#jobList_templ'), data: rep, context: '.jobList'});
                $(".editTeacherJob").click(function () {
                    teamanageData.jobId = $(this).attr('jid');
                    edumanage.getTeacherJob();
                });
                $('.delTeacherJob').click(function () {
                    teamanageData.jobId = $(this).attr('jid');
                    layer.confirm('确定要删除吗', {
                        btn: ['确定', '取消'] //按钮
                    }, function () {
                        layer.msg('删除中。。。', {time: 1000});
                        edumanage.delTeacherJob();
                    }, function () {
                    });
                });
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.delTeacherJob = function () {
        Common.getData('/teacher/delTeacherJob.do', teamanageData, function (rep) {
            if (rep.code == '200') {
                edumanage.selTeacherJob();
            }
        });
    }
    edumanage.getTeacherJob = function () {
        Common.getData('/teacher/getTeacherJob.do', teamanageData, function (rep) {
            if (rep.code == '200') {
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
    edumanage.addTeachInfo = function () {
        Common.getData('/teacher/addStaffBase.do', teamanageData, function (rep) {
            if (rep.code == '200') {
                $('#resumeId').val(rep.message.resumeId);
                $('#userId').val(rep.message.userId);
                layer.msg("编辑成功！");
                teamanageData.page = 1;
                edumanage.selTeacherList();
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.clearInput = function () {
        $('#userId').val("");
        $('#teacherName').val("");
        $("input[name='sex']").eq(0).attr("checked", "checked");
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
        $("input[name='if-marry']").eq(0).attr("checked", "checked");
        $('.sub-list li').removeClass("li-select");
        $('.jobList').html('');
    }
    edumanage.getRolesBySchool = function () {
        var par = {}
        par.name = $("#searchRoleName").val();
        par.role=$("#strole").val();
        if (par.name == "") {
            par.name = "*";
        }
        Common.getData('/roleMge/getRolesBySchool.do', par, function (rep) {
            $("#roleList").empty();
            Common.render({tmpl: $('#j-tmpl'), data: rep.message, context: '#roleList'});
        })
    }
    edumanage.getRolesBySchoolRole = function () {
        var par = {}

        Common.getData('/roleMge/getRolesBySchool.do', par, function (rep) {
            var dts = new Array();
            $.each(rep.message, function (i, obj) {
                if (dts.length > 0) {
                    var pd = true;
                    $.each(dts, function (it, fa) {
                        if (fa == obj.rName) {
                            pd = false;
                        }
                    })
                    if (pd) {
                        dts.push(obj.rName)
                    }
                } else {
                    dts.push(obj.rName)
                }
            })
            Common.render({tmpl: $('#bret'), data: dts, context: '#strole'});

        })
    }
    edumanage.editUserRoleInGroup = function (dietParam) {

        Common.getPostBodyData('/groupMge/addOrEditUserRoleInGroup.do', dietParam, function (rep) {
            edumanage.getRolesBySchool();
        });
    }
    edumanage.init = function () {
        $("#strole").change(function(){
            edumanage.getRolesBySchool();
        })
        edumanage.getRolesBySchoolRole();
        $("body").on("click", ".shan", function () {
            var dietParam = {};
            dietParam.groupId = $(this).attr("groupid");
            dietParam.userId = $(this).attr("uid");
            var roleInfos = [];
            dietParam.roleInfos = roleInfos;
            layer.confirm('确定要删除么?', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('删除中。。。', {time: 1000});
                edumanage.editUserRoleInGroup(dietParam);
            }, function () {
            });
        })
        //角色
        edumanage.getRolesBySchool();


        $("#seachTeacher").click(function () {
            teamanageData.page = 1;
            edumanage.selTeacherList();
        })
        edumanage.getSchoolLoopList();
        $('.job-sure').click(function () {
            teamanageData.organization = $('#workunit').val();
            teamanageData.startTime = $('#startTime').val();
            teamanageData.endTime = $('#endTime').val();
            teamanageData.detail = $('#jobDetail').val();
            teamanageData.userId = $('#userId').val();
            teamanageData.id = $('#jobId').val();
            edumanage.addTeacherJob();
        });
        //添加教师经历
        $(".work-experience .con-top .addJob").click(function () {
            $('#workunit').val('');
            $('#startTime').val('');
            $('#endTime').val('');
            $('#jobDetail').val('');
            $('.alert-l').text("添加经历");
            $(".history-alert,.bg").show();
        });
        $(".step-1 .addTeacher").click(function () {
            $('.tea-manage .edit-nav li').each(function (i) {
                if (i == 0) {
                    $(this).addClass("edit-cur").siblings().removeClass("edit-cur");
                    $('.basic-data').show();
                    $('.per-inf').hide();
                    $('.work-experience').hide();
                }
            });
            edumanage.clearInput();
            $(".tea-manage .show-con").hide();
            $(".tea-manage .edit-con").show();
        })
        $('.teacher-detail').click(function () {
            teamanageData.id = $('#resumeId').val();
            teamanageData.userId = $('#userId').val();
            teamanageData.birth = $('.year-select').val() + "/" + $('.month-select').val();
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
            if ($('#postionStatus').val()) {
                teamanageData.status = $('#postionStatus').val();
            }
            else {
                layer.alert("状态不能为空");
                return;
            }
            teamanageData.type = 2;
            edumanage.addTeachInfo();
        });
        //职工管理
        teamanageData.page = 1;
        edumanage.selTeacherList();
        $('.sure-teacherBase').click(function () {
            teamanageData.userName = $('#teacherName').val();
            teamanageData.sex = $("input[name='sex']:checked").val();
            teamanageData.type = 1;
            if ($('#teacherName').val() == '') {
                layer.alert("请输入姓名！");
            } else {
                edumanage.addTeachInfo();
            }
        });
        //职工结束

        $(".addjwc-alert .btn-sure").click(function () {
            $(".addjwc-alert,.bg").hide();
        })
        $('body').on('click', '.opti .sp1', function () {
            $("#xxdmz").html(nnm)
            $('.addjwc-alert,.bg').fadeIn();
            var par = {};
            par.gid = pid;
            $(".ul-jwc").empty();
            Common.getData('/groupMge/getUsersByGid.do', par, function (rep) {
                if (rep.code != 500) {
                    $("#tdrs").html(rep.message.length);
                    if (rep.message.length > 0) {
                        Common.render({tmpl: $('#j-tmplss'), data: rep.message, context: '.ul-jwc'});
                    } else {
                        $(".ul-jwc").append("<div style='text-align: center'>请添加人员</div>")
                    }
                }
            });
        });

        $('.edit-nav>ul>li').click(function () {
            var ind = $(this).index() + 1;
            if ($('#userId').val() != '') {
                $(this).addClass('edit-cur').siblings('li').removeClass('edit-cur');
                $('.edit-main>div:nth-child(' + ind + ')').show().siblings('div').hide();
                if (ind == 3) {
                    edumanage.selTeacherJob();
                }
            } else {
                layer.alert("请先创建基础信息！");
            }
        })
        $('body').on('click', '.ul-jwc li', function () {
            $(this).toggleClass('sel-bg');
        });

        $("input[name='identName']").change(function () {
            edumanage.getGroupUsersByIdent();
        });
        $("input[name='identNames']").change(function () {
            edumanage.getGroupUsersByIdents();
        });

        $(".set-access .set-bct").click(function () {
            var par = {};
            par.gId = pid;
            var st = "";
            $(".group-rg").each(function () {
                st += $(this).attr("ids") + ",";
            })
            par.users = st;
            Common.getData('/groupMge/addRoleUsersByIdent.do', par, function (rep) {
                $(".set-access").hide();
                $(".addjwc-alert").show();
                var par = {};
                par.gid = pid;
                $(".ul-jwc").empty();
                Common.getData('/groupMge/getUsersByGid.do', par, function (rep) {
                    if (rep.code != 500) {
                        $("#tdrs").html(rep.message.length);
                        if (rep.message.length > 0) {
                            Common.render({tmpl: $('#j-tmplss'), data: rep.message, context: '.ul-jwc'});
                        } else {
                            $(".ul-jwc").append("<span>请添加人员</span>")
                        }
                    }
                });
            });
        })
        $("#addren").click(function () {
            edumanage.getGroupUsersByIdents();
            $("#myLists-nav").empty();
            $('#myLists').listnav({
                includeOther: true,
                noMatchText: '',
                prefixes: ['the', 'a']
            });
            $(".set-access").show();
            $(".addjwc-alert").hide();
        })
        $("#delren").click(function () {
            var par = {};
            par.groupId = pid;
            var us = "";
            $(".sel-bg").each(function () {
                us += $(this).attr("ids") + ",";
            })
            par.userIds = us;
            if (us != "") {
                layer.confirm('确定要删除吗', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    layer.msg('删除中。。。', {time: 1000});
                    Common.getData('/groupMge/delUserFromGroups.do', par, function (rep) {
                        var tt = {};
                        tt.gid = pid;
                        $(".ul-jwc").empty();
                        Common.getData('/groupMge/getUsersByGid.do', tt, function (rep) {
                            if (rep.code != 500) {
                                $("#tdrs").html(rep.message.length);
                                if (rep.message.length > 0) {
                                    Common.render({tmpl: $('#j-tmplss'), data: rep.message, context: '.ul-jwc'});
                                } else {
                                    $(".ul-jwc").append("<span>请添加人员</span>")
                                }
                            }
                        });
                    })
                }, function () {
                });
            } else {
                layer.msg("请选择删除的人！")
            }
        })
        $(".set-qxt,.set-popup-f i").click(function () {
            $(".set-access").hide();
            $(".addjwc-alert").show();
        })

        $('body').on('blur', '.inputs', function () {
            var nn = $(this).next();
            var val = $(this).val();
            nn.text(val)
            $('.node input ').remove();
            var map = {};
            map.id = pid;
            map.nm = val;
            Common.getPostBodyData('/shoolbase/upTissue.do', map, function (rep) {
            });
        })
        edumanage.getTissue();
        $('#btn-delete-nodes').on('click', function () {


            var $node = $('#selected-node').data('node');
            if (!$node) {
                layer.msg('请在组织架构中选择一个单位');
                return;
            } else {
                var countt = 0;
                var par = {};
                par.gid = pid
                Common.getData('/groupMge/getUsersByGid.do', par, function (rep) {
                    if (rep.code != 500) {
                        countt = rep.message.length;
                    }
                });
                var map = {};
                map.id = pid;
                if (ps != "null") {
                    if (ct > 0) {
                        if (countt > 0) {
                            layer.confirm('《' + nnm + '》这个目录下有用户确定要删除吗?', {
                                btn: ['确定', '取消'] //按钮
                            }, function () {
                                layer.msg('删除中。。。', {time: 1000});
                                Common.getPostBodyData('/shoolbase/delTissue.do', map, function (rep) {
                                    edumanage.getTissue();
                                })
                            }, function () {
                            });
                        } else {
                            layer.confirm('《' + nnm + '》这个目录下有子集目录确定要删除吗?', {
                                btn: ['确定', '取消'] //按钮
                            }, function () {
                                layer.msg('删除中。。。', {time: 1000});
                                Common.getPostBodyData('/shoolbase/delTissue.do', map, function (rep) {
                                    edumanage.getTissue();
                                })
                            }, function () {
                            });
                        }
                    } else {
                        if (countt > 0) {
                            layer.confirm('《' + nnm + '》这个目录下有用户确定要删除吗?', {
                                btn: ['确定', '取消'] //按钮
                            }, function () {
                                layer.msg('删除中。。。', {time: 1000});
                                Common.getPostBodyData('/shoolbase/delTissue.do', map, function (rep) {
                                    edumanage.getTissue();
                                })
                            }, function () {
                            });
                        } else {
                            layer.confirm('确定要删除吗', {
                                btn: ['确定', '取消'] //按钮
                            }, function () {
                                layer.msg('删除中。。。', {time: 1000});
                                Common.getPostBodyData('/shoolbase/delTissue.do', map, function (rep) {
                                    edumanage.getTissue();
                                })
                            }, function () {
                            });
                        }
                    }
                } else {
                    layer.msg('第一级目录不能删除！');
                }
            }
        });
        $('#btn-add-siblings-nodes').on('click', function () {
            var $node = $('#selected-node').data('node');
            if (!$node) {
                layer.msg('请在组织架构中选择一个单位');
                return;
            } else {
                if (ps != "null") {
                    layer.open({
                        title: '添加同级单位',
                        content: '<ul id="new-nodelist"><li><input placeholder="输入单位名称" type="text" class="new-node"></li></ul>',
                        btn: ['确定', '取消'],
                        btnAlign: 'c',
                        yes: function (index) {
                            var nodeVals = $(".new-node").val();
                            if (nodeVals != "" || nodeVals != undefined) {
                                var map = {};
                                map.pid = pid;
                                map.nm = nodeVals;
                                Common.getPostBodyData('/shoolbase/addTissues.do', map, function (rep) {
                                    edumanage.getTissue();
                                });
                                layer.close(index)
                            } else {
                                layer.msg("请为新组织添加名称");
                            }
                        }
                    })
                } else {
                    layer.msg("第一级目录不能增加同级目录");
                }
            }
        });
        $('#btn-add-child-nodes').on('click', function () {
            var $node = $('#selected-node').data('node');
            if (!$node) {
                layer.msg('请在组织架构中选择一个单位');
                return;
            } else {
                layer.open({
                    title: '添加子级单位',
                    content: '<ul id="new-nodelist"><li><input placeholder="输入单位名称" type="text" class="new-node"></li></ul>',
                    btn: ['确定', '取消'],
                    btnAlign: 'c',
                    yes: function (index) {
                        var nodeVals = $(".new-node").val();
                        if (nodeVals != "" || nodeVals != undefined) {
                            var map = {};
                            map.pid = pid;
                            map.nm = nodeVals;
                            Common.getPostBodyData('/shoolbase/addTissue.do', map, function (rep) {
                                edumanage.getTissue();
                            });
                            layer.close(index)
                        } else {
                            layer.msg("请输入子级名称");
                        }
                    }
                })
            }
        });


        //----------------------学校信息START------------------------------------
        edumanage.imgUpload();
        edumanage.selSchoolList();
        $(".newAdd").click(function () {
            edumanage.getClassTypeList();
            edumanage.clearSchoolInput();
            $(".tea-manage .show-con").hide();
            $(".tea-manage .edit-con").show();
        });
        $(".newAdd2").click(function () {
            $(".bg").show();
            $(".jslxsz-alert").show();
            edumanage.getClassTypeList();
        });
        $(".newAdd3").click(function () {
            $('.clsType').append("<li><input type='text' ctid='' class='typeName'><span class='closeType'>×</span></li>");
            $('.typeName').blur(function () {
                edumanageData.id = $(this).attr('ctid');
                edumanageData.name = $(this).val();
                if ($.trim($(this).val()) != '') {
                    if ($(this).attr('ctid') == '') {
                        edumanage.addClassType();
                    } else {
                        edumanage.updClassType();
                    }
                }
            });
            $('.closeType').click(function () {
                edumanageData.id = $(this).attr('ctid');
                edumanage.delClassType();
            });
        });


        //返回学校信息管理首页
        $(".tea-manage .path-root").click(function () {
            $(".tea-manage .edit-con").hide();
            $(".tea-manage .show-con").show();
        });
        $('#school-sure').click(function () {
            edumanageData.name = $('#schoolName').val();
            edumanageData.url = $('#schoolUrl').val();
            edumanageData.introduce = $('#schoolInfo').val();
            edumanageData.schoolMotto = $('#schoolMotto').val();
            edumanageData.primary = $('#primary').val();
            edumanageData.middle = $('#middle').val();
            edumanageData.high = $('#high').val();
            edumanageData.detailedAddress = $('#schoolAdress').val();
            edumanageData.schoolLogo = $('#schoolLogo').attr('src');
            if ($('#schoolName').val() == '') {
                layer.alert("请输入学校名称！");
                return;
            }
            if ($('#primary').val() == '0' && $('#middle').val() == '0' && $('#high').val() == '0') {
                layer.alert("请输入年级设置！");
                return;
            }
            if ($('#schoolId').val() != '') {
                edumanage.updateSchool();
            } else {
                edumanage.addSchool();
            }

        });

        $('#submit-schoolName').click(function () {
            edumanage.selSchoolList();
        });
        //----------------------学校信息END--------------------------------------

        //----------------------角色管理START--------------------------------------
        roleParam.pageSize = 15;
        $("#roleMge").click(function () {
            roleParam.page = 1;
            roleParam.name = "";
            edumanage.getRoles();
        });

        $("#searchRoleNameBtn").click(function () {
            edumanage.getRoles();
        });

        $('.educa-add').click(function () {
            edumanage.getIdentities();
            $('.popup-addr').show();
            $('.bg').show();
        })
        //添加角色弹窗
        $(".popup-addr .btn-r-esc,.popup-addr .group-i").click(function () {
            edumanage.closeRoleWin();
        });

        $('.btn-r-sure').click(function () {
            edumanage.addOrEditRole();
        });

        //角色管理---管理
        $('body').on('click', '.subject-manage .edit-grade', function () {
            var id = $(this).parent().parent().attr("id");
            $("#hRoleId").val(id);
            var name = $(this).parent().parent().attr("name");
            $("#roleNameTitle").text(name);
            edumanage.loadZTree();
            edumanage.pageLoad();
            $(".educa-add-popup,.bg").show();
        });
        //限定人数---其他
        if ($('.popup-addr-bn1 input').is(":checked")) {
            $(".popup-addr-bn1").parent().parent().next("dd").show();
        }
        $('.popup-addr-li input').change(function () {
            if ($('.popup-addr-bn1 input').is(":checked")) {
                $(".popup-addr-bn1").parent().parent().next("dd").show();
            }
            else {
                $(".popup-addr-bn1").parent().parent().next("dd").hide();
            }
        });
        edumanage.tabCheck("educa-add-popup", "ul-select", "select-con", "div", "select-cur");

        $('.group-qx, .group-ru-qx').click(function () {
            $('.stu-add').hide();
            $('.grade-edit-popup').hide();
            $('.term-add').hide();
            $('.educa-del-popup').hide();
            $('.educa-add-popup').hide();
            $('.stu-edit').hide();
            $('.bg').hide();
        });

        $('.stu-add-top i').click(function () {
            $('.stu-add').hide();
            $('.grade-edit-popup').hide();
            $('.term-add').hide();
            $('.educa-del-popup').hide();
            $('.educa-add-popup').hide();
            $(".stu-edit").hide();
            $('.bg').hide();
        });

        $('body').on('change', '.cf', function () {
            var cf = $(this).parent().siblings().find('input');
            $(this).prop('checked') ? cf.prop('checked', true) : cf.prop('checked', false);
        });

        $('body').on('click', '.cff', function () {
            if (!$(this).is(":checked")) {
                $(".cf").removeAttr("checked");
            }
        });

        $('body').on('click', '.del-role', function () {
            var id = $(this).parent().parent().attr("id");
            edumanage.delRoleById(id);
        });
        $('body').on('click', ".myList li", function () {
            if ($(this).hasClass('group-rgg')) {
                $(this).addClass('group-rgg')
            } else {
                $(this).toggleClass('group-rg');
            }

        });

        $(".group-ru-qd").click(function () {
            edumanage.addUserRolesInGroup();
        });

        //----------------------角色管理END--------------------------------------


        //----------------------学校信息END--------------------------------------

        //----------------------教室管理START--------------------------------
        //左侧导航栏切换
        edumanage.tabCheck("content", "left-nav", "right-con", "div", "li-cur");
        $(".btn-esc,.alert-r").click(function () {
            $(".alert-top").parent().hide();
            $(".bg").hide();
        });
        $('#loop-sure').click(function () {
            edumanageData.name = $('#loopName').val();
            edumanageData.number = $('#loopNumber').val();
            edumanageData.count = $('#loopCount').val();
            if ($('#loopNumber').val() == '') {
                layer.alert("教学楼楼号不为空！");
                return;
            }
            if ($('#loopName').val() == '') {
                layer.alert("教学楼名称不为空！");
                return;
            }

            if ($('#loopCount').val() == '' || $('#loopCount').val() < 0) {
                layer.alert("教学楼楼层必须大于0！");
                return;
            }
            Common.getData('/clsroom/addSchoolLoop.do', edumanageData, function (rep) {
                if (rep.code == '200') {
                    $(".alert-top").parent().hide();
                    $(".bg").hide();
                    edumanage.getSchoolLoopList();
                } else {
                    layer.alert("添加失败！");
                }
            });
        });
        $('.cr-sure').click(function () {
            edumanageData.loopId = $('#teachLoopId').val();
            edumanageData.loop = $('#crLoop').val();
            edumanageData.number = $('#crNumber').val();
            edumanageData.count = $('#crCount').val();
            edumanageData.type = $('#crType').val();
            edumanageData.remark = $('#crRemark').val();
            if ($('#crNumber').val() == '') {
                layer.alert("教室编号不为空！");
                return;
            }
            if ($('#croomId').val() != '') {
                edumanage.updClassRoom($('#croomId').val());
            } else {
                edumanage.addClassRoom();
            }

        });

        //教室管理---添加教学楼
        $(".step-addjsl .newAdd").click(function () {
            $('#loopName').val('');
            $('#loopNumber').val('');
            $('#loopCount').val('');
            $(".bg,.addjxl-alert").show();
        });
        //教室管理---添加教室
        $(".step-addjs .newAdd").click(function () {
            edumanage.clearClassRoom();
            $('.add-classRoom').text("添加教室");
            $(".bg,.addjs-alert").show();
        });

        //----------------------教室管理END--------------------------------
    };
    //导航栏切换
    edumanage.tabCheck = function (pars, par, wrap, child, className) {
        var $li = $("." + pars + " ." + par).find("li");
        var $tab = $("." + pars + " ." + wrap).children(child);
        var len = $li.length;
        $li.click(function () {
            var $index = $(this).index();
            $(this).addClass(className).siblings().removeClass(className);
            if (par == "ul-select") {
                edumanage.getGroupUsers();
            } else {
                $tab.hide();
            }
            if ($index == 0) {
                edumanage.selSchoolList();
            } else if ($index == 2) {
                edumanage.getSchoolLoopList();
                edumanage.getClassTypeList();
            }
            $tab.eq($index).show();
        })
    }
    //-----------------------------学校信息--------------------------------------------------------------------------------------------------------------------------------
    edumanage.addSchool = function () {
        Common.getData('/shoolbase/addSchool.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                $(".tea-manage .edit-con").hide();
                $(".tea-manage .show-con").show();
                edumanage.selSchoolList();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    edumanage.updateSchool = function () {
        Common.getData('/shoolbase/updateSchool.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                $(".tea-manage .edit-con").hide();
                $(".tea-manage .show-con").show();
                edumanage.selSchoolList();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    edumanage.delSchool = function (id) {
        edumanageData.schoolId = id;
        Common.getData('/shoolbase/delSchool.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                edumanage.selSchoolList();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    edumanage.getSchool = function (id) {
        edumanageData.id = id;
        edumanage.clearSchoolInput();
        Common.getData('/shoolbase/getSchool.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                $('#schoolId').val(rep.message.id);
                $('#schoolParentId').val(rep.message.parentId);
                $('#schoolName').val(rep.message.name);
                $('#schoolUrl').val(rep.message.url);
                $('#schoolInfo').val(rep.message.introduce);
                $('#schoolMotto').val(rep.message.schoolMotto);
                if (rep.message.periods != null && rep.message.periods.length != 0) {
                    for (var i = 0; i < rep.message.periods.length; i++) {
                        if (rep.message.periods[i].period == '小学' || rep.message.periods[i].period == 'primary') {
                            $('#primary').val(rep.message.periods[i].year);
                        } else if (rep.message.periods[i].period == '初中' || rep.message.periods[i].period == 'junior') {
                            $('#middle').val(rep.message.periods[i].year);
                        } else if (rep.message.periods[i].period == '高中' || rep.message.periods[i].period == 'senior') {
                            $('#high').val(rep.message.periods[i].year);
                        }
                    }
                }


                $('#schoolAdress').val(rep.message.detailedAddress);
                $('#schoolLogo').attr('src', rep.message.schoolLogo);
                $(".tea-manage .edit-con").show();
                $(".tea-manage .show-con").hide();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    edumanage.selSchoolList = function () {
        edumanageData.keyword = $('#keyword').val();
        Common.getData('/shoolbase/selSchoolList.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                $('.schoolList').html('');
                Common.render({tmpl: $('#schoolList_templ'), data: rep, context: '.schoolList'});
                $('.table-edit').click(function () {
                    edumanage.getSchool($(this).attr('sid'));
                });
                $('.educa-del').click(function () {
                    var sid = $(this).attr('sid');
                    layer.confirm('确定要删除吗', {
                        btn: ['确定', '取消'] //按钮
                    }, function () {
                        layer.msg('删除中。。。', {time: 1000});
                        edumanage.delSchool(sid);
                    }, function () {
                    });
                });
            } else {
                layer.alert(rep.message);
            }
        });
    }

    edumanage.clearSchoolInput = function () {
        $('#schoolId').val("");
        $('#schoolParentId').val("");
        $('#schoolName').val("");
        $('#schoolUrl').val("");
        $('#schoolInfo').val("");
        $('#schoolMotto').val("");
        $("#primary option:first").prop("selected", 'selected');
        $("#middle option:first").prop("selected", 'selected');
        $("#high option:first").prop("selected", 'selected');
        $('#schoolAdress').val("");
        $('#schoolLogo').attr('src', "");
    }
    //---------------------------------教室---------------------------------------------------------------------------------------------------------------
    edumanage.updClassType = function () {
        Common.getData('/clsroom/updClassType.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                edumanage.getClassTypeList();
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.addClassType = function () {
        Common.getData('/clsroom/addClassType.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                edumanage.getClassTypeList();
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.delClassType = function () {
        Common.getData('/clsroom/delClassType.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                edumanage.getClassTypeList();
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.getClassTypeList = function () {
        Common.getData('/clsroom/getClassTypeList.do', edumanageData, function (rep) {
            $('.clsType').empty();
            $('#crType').empty();
            var json = eval('(' + rep.message + ')');
            if (json.length != 0) {
                Common.render({tmpl: $('#classTypeList_templ'), data: json, context: '.clsType'});
                Common.render({tmpl: $('#classTypeList2_templ'), data: json, context: '#crType'});
            }

            $('.typeName').blur(function () {
                edumanageData.id = $(this).attr('ctid');
                edumanageData.name = $(this).val();
                if ($.trim($(this).val()) != '') {
                    if ($(this).attr('ctid') == '') {
                        edumanage.addClassType();
                    } else {
                        edumanage.updClassType();
                    }
                }
            });
            $('.closeType').click(function () {
                edumanageData.id = $(this).attr('ctid');
                edumanage.delClassType();
            });
        });
    }
    edumanage.getSchoolLoopList = function () {
        Common.getData('/clsroom/getSchoolLoopList.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                $('.looplist').html('');
                if (rep.message == null || rep.message.length == 0) {
                    $('.looplist').hide();
                } else {
                    $('.looplist').show();
                }
                Common.render({tmpl: $('#looplist_templ'), data: rep, context: '.looplist'});
                //教室管理---选择教学楼
                $(".jsl-select ul li").click(function () {
                    $(this).addClass("liselect").siblings().removeClass("liselect");
                    $('#teachLoopId').val($(this).attr('lid'));
                    $('.step-addjs .newAdd').show();
                    var cnt = $(this).attr('lcnt');
                    $('#crLoop').empty();
                    for (var i = 0; i < cnt; i++) {
                        $('#crLoop').append("<option>" + (i + 1) + "</option>");
                    }

                    edumanage.getClassRoomList($(this).attr('lid'));
                });
            } else {
                layer.alert(rep.message);
            }
        });
    }

    edumanage.getClassRoomList = function (id) {
        edumanageData.loopId = id;
        Common.getData('/clsroom/getClassRoomList.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                $('.classRommList').html('');
                Common.render({tmpl: $('#classRommList_templ'), data: rep, context: '.classRommList'});
                var totalPage = 0;
                if (rep.message.count % rep.message.pageSize == 0) {
                    totalPage = rep.message.count / rep.message.pageSize;
                } else {
                    totalPage = parseInt(rep.message.count / rep.message.pageSize) + 1;
                }
                $(".pageDiv").createPage({
                    pageCount: totalPage,//总页数
                    current: rep.message.page,//当前页
                    turndown: 'false',//是否显示跳转框，显示为true，不现实为false,一定记得加上引号...
                    backFn: function (p) {
                        if (edumanageData.page != p) {
                            edumanageData.page = p;
                            edumanage.getClassRoomList(id);
                        }

                    }
                });
                $('.cr-edit').click(function () {
                    edumanage.getClassTypeList();
                    edumanage.getClassRoom($(this).attr('crid'));
                });
                $('.cr-del').click(function () {
                    var cid = $(this).attr('crid');
                    layer.confirm('确定要删除吗', {
                        btn: ['确定', '取消'] //按钮
                    }, function () {
                        layer.msg('删除中。。。', {time: 1000});
                        edumanage.delClassRoom(cid);
                    }, function () {
                    });
                });
            } else {
                layer.alert("查询失败！");
            }
        });
    }

    edumanage.getClassRoom = function (id) {
        edumanageData.id = id;
        Common.getData('/clsroom/getClassRoom.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                $('.add-classRoom').text("编辑教室");
                edumanage.clearClassRoom();
                $('#croomId').val(id);
                $('#crLoop').val(rep.message.loop);
                $('#crNumber').val(rep.message.number);
                $('#crCount').val(rep.message.count);
                $('#crType').val(rep.message.type);
                $('#crRemark').val(rep.message.remark);
                $(".bg,.addjs-alert").show();
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.delClassRoom = function (id) {
        edumanageData.id = id;
        Common.getData('/clsroom/delClassRoom.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                edumanage.getClassRoomList($('#teachLoopId').val());
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.updClassRoom = function (id) {
        edumanageData.id = id;
        Common.getData('/clsroom/updClassRoom.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                $(".bg,.addjs-alert").hide();
                edumanage.getClassRoomList($('#teachLoopId').val());
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.addClassRoom = function (id) {
        Common.getData('/clsroom/addClassRoom.do', edumanageData, function (rep) {
            if (rep.code == '200') {
                $(".bg,.addjs-alert").hide();
                edumanage.getClassRoomList($('#teachLoopId').val());
            } else {
                layer.alert(rep.message);
            }
        });
    }
    edumanage.clearClassRoom = function () {
        $('#croomId').val('');
        $('#crLoop').val('');
        $('#crNumber').val('');
        $('#crCount').val('');
        $('#crRemark').val('');
        $("#crLoop option:first").prop("selected", 'selected');
        $('#crType option:first').prop("selected", 'selected');
    }


//---------------------------------角色start---------------------------------------------------------------------------------------------------------------
    /**
     * 查询角色集合
     */
    edumanage.getRoles = function () {
        edumanage.getRolesBySchool();
        //Common.getData('/roleMge/getSchoolRoles.do', roleParam, function (rep) {
        //    if (rep.code == 200) {
        //        //rep.message.count;
        //        //rep.message.page;
        //        //rep.message.pageSize;
        //        $("#roleList").html("");
        //        Common.render({tmpl: $('#j-tmpl'), data: rep.message.rows, context: '#roleList'});
        //        var totalPage = 0;
        //        if (rep.message.count % rep.message.pageSize == 0) {
        //            totalPage = rep.message.count / rep.message.pageSize;
        //        } else {
        //            totalPage = parseInt(rep.message.count / rep.message.pageSize) + 1;
        //        }
        //        $(".pageDiv2").createPage({
        //            pageCount: totalPage,//总页数
        //            current: rep.message.page,//当前页
        //            turndown: 'false',//是否显示跳转框，显示为true，不现实为false,一定记得加上引号...
        //            backFn: function (p) {
        //                if (edumanageData.page != p) {
        //                    edumanageData.page = p;
        //                    edumanage.getClassRoomList(id);
        //                }
        //
        //            }
        //        });
        //    }
        //});
    };

    edumanage.getIdentities = function () {
        Common.getData('/groupMge/getIdentities.do', {}, function (rep) {
            if (rep.code == '200') {
                var html = '<span><input class="cf" name="cff" type="checkbox"><em>所有</em></span>';
                var idenList = rep.message;
                $.each(idenList, function (i, item) {
                    html += '<span><input class="cff" name="cff" value="' + item.key + '" type="checkbox"><em>' + item.value + '</em></span>';
                });
                $(".popup-addr-bn").html(html);
            }
        });
    }

    /**
     * 删除角色
     */
    edumanage.delRoleById = function (id) {
        if (confirm("您确定删除该角色吗?")) {
            var delParam = {};
            delParam.id = id;
            Common.getPostData('/roleMge/delRoleById.do', delParam, function (rep) {
                if (rep.code == 200) {
                    layer.alert(rep.message);
                    edumanage.getRoles();
                } else {
                    layer.alert(rep.message);
                }
            });
        }
    };

    /**
     * 添加或修改角色
     */
    edumanage.addOrEditRole = function () {
        var addParam = {};
        addParam.id = $("#roleId").val();
        //addParam.groupTypeId = $("#gtLid").val();
        //addParam.category = category;
        addParam.name = $("#roleName").val();
        addParam.desc = $("#roleDesc").val();
        //addParam.schoolId = $("#schoolId").val();
        var identities = [];
        $("input[class='cff']").each(function () {
            var checked = $(this).is(":checked");
            if (checked) {
                var identity = $(this).val();
                identities.push(identity);
            }
        });
        addParam.identities = identities;
        $("input[name='df']").each(function () {
            var checked = $(this).is(":checked");
            if (checked) {
                if ($(this).val() == "") {
                    addParam.maxNum = -1;
                }
                if ($(this).val() == "1") {
                    addParam.maxNum = 1;
                }
                if ($(this).val() == "other") {
                    addParam.maxNum = $("#otherNum").val();
                }
            }
        });
        Common.getPostBodyData('/roleMge/addOrEditSchoolRole.do', addParam, function (rep) {
            var json = eval('(' + rep + ')');
            if (json.code == 200) {
                //var dto=json.message.dto;
                roleParam.page = 1;
                //roleParam.name="";
                edumanage.getRoles();
                edumanage.closeRoleWin();
            } else {
                layer.alert(rep.message);
            }
        });
    };

    /**
     * 关闭角色弹窗
     */
    edumanage.closeRoleWin = function () {
        $("#roleId").val("");
        $("#roleName").val("");
        $("#roleDesc").val("");
        $("input[name='cff']").removeAttr("checked");
        $("input:radio[name='df'][value='']").prop("checked", true);
        $('.popup-addr-bn2').css('display', 'none');
        $("#otherNum").val("");
        $('.popup-addr').hide();
        $('.bg').hide();
    };


    var zTree, nodes, treeNode;
    var zNodes = [];
    /**
     * 加载zTree
     */
    edumanage.loadZTree = function () {
        zNodes = [];
        var selParam = {};
        Common.getPostData('/groupMge/getGroupsStr.do', selParam, function (rep) {
            if (rep.code == 200) {
                var list = rep.message;
                $.each(list, function (i, item) {
                    var obj;
                    if (item.parentId == "") {
                        obj = {id: item.id, pId: item.parentId, name: item.name, gTLid: item.groupTypeId, open: true};
                    } else {
                        obj = {id: item.id, pId: item.parentId, name: item.name, gTLid: item.groupTypeId};
                    }
                    zNodes.push(obj);
                });
            } else {
                layer.alert(rep.message);
            }
        });
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        zTree = $.fn.zTree.getZTreeObj("treeDemo");
    };

    var groupUsersParam = {};
    edumanage.pageLoad = function () {
        /*$('body').on('click', '.myList li', function(){
         $(this).toggleClass('group-rg')
         });*/

        $('body').on('click', '.node_name', function () {
            nodes = zTree.getSelectedNodes();
            treeNode = nodes[0];
            if (treeNode) {
                groupUsersParam.groupId = treeNode.id;
                groupUsersParam.groupTypeId = treeNode.gTLid;
                edumanage.getGroupUsers();
            }
        });

        zTree = $.fn.zTree.getZTreeObj("treeDemo");
        nodes = zTree.getNodes();
        treeNode = nodes[0];
        if (treeNode) {
            groupUsersParam.groupId = treeNode.id;
            groupUsersParam.groupTypeId = treeNode.gTLid;
            edumanage.getGroupUsers();
        }
    };

    edumanage.getGroupUsers = function () {
        groupUsersParam.roleId = $("#hRoleId").val();
        groupUsersParam.type = $(".ul-select .select-cur").attr("id");
        Common.getPostData('/groupMge/getGroupUserInfos.do', groupUsersParam, function (rep) {
            if (rep.code == 200) {
                $("#myList").html("");
                $("#myList-nav").html("");
                Common.render({tmpl: $('#j-tmpl2'), data: rep.message, context: '#myList'});
                $('#myList').listnav({
                    includeOther: true,
                    noMatchText: '',
                    prefixes: ['the', 'a']
                });
            }
        });
    };

    /*
     * 上传学校头像
     * */
    edumanage.imgUpload = function () {
        /*
         * 点击附件按钮
         * */
        $('#image-upload').click(function (event) {
            Common.fileUpload('#image-upload', '/commonupload/upload.do', '#picuploadLoading', function (e, response) {
                var result = response.result;
                if (result.code == '200') {
                    var url = result.message[0].path;
                    $('#schoolLogo').attr('src', url);
                }
            });
        });
    }

    edumanage.addUserRolesInGroup = function () {
        var dietParam = {};
        var userIds = [];
        $("#myList li").each(function (i, item) {
            if ($(item).hasClass("group-rg")) {
                var uid = $(item).attr("uid");
                userIds.push(uid);
            }
        });
        dietParam.userIds = userIds;
        dietParam.roleId = $("#hRoleId").val();
        Common.getPostBodyData('/groupMge/addUserRolesInGroup.do', dietParam, function (rep) {
            var json = eval('(' + rep + ')');
            if (json.code == 200) {
                layer.alert(json.message);
                edumanage.getGroupUsers();
                edumanage.getRoles();
            } else {
                layer.alert(json.message);
            }
        });
    }
    //---------------------------------组织结构------------------------------------------------------------------------------------------------------------
    //开始创建
    edumanage.addTissue = function () {
        var map = {};
        map.pid = "*";
        map.nm = "学校组织结构";
        Common.getPostBodyData('/shoolbase/addTissue.do', map, function (rep) {
            edumanage.getTissue();
        });
    }
    edumanage.getTissue = function () {
        Common.getData('/shoolbase/infos.do', {}, function (rep) {
            $("#chart-container").empty();
            if (rep.length > 0) {
                $('#chart-container').orgchart({
                    'data': "/shoolbase/infost.do",
                    'exportButton': true,
                    'exportFilename': 'SportsChart',
                    'parentNodeSymbol': 'fa-th-large',
                    'nodeTitle': 'name',
                    'nodeId': 'id',
                    'createNode': function ($node, data) {
                        $node.on('click', function (event) {
                            if (!$(event.target).is('.edge')) {
                                ps = data.pid;
                                pid = data.id;
                                nnm = data.name;
                                ct = data.count;
                                $('#selected-node').val(data.name).data('node', $node);
                            }
                        });
                    },
                    'pan': true,
                    'zoom': true
                })
            } else {
                edumanage.addTissue();
            }
        });
    }

//---------------------------------角色end---------------------------------------------------------------------------------------------------------------
    edumanage.init();
});