/**
 * Created by albin on 2018/3/28.
 */
define('placementSet', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var placementSet = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var gradeId = "";
    var subjectId = "";
    var subType2 = "";
    placementSet.init = function () {
        $("#tbks").click(function(){
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $("#grade .cur").attr("ids");
            Common.getData('/n33_setJXB/updateJXBKsList.do', par, function (rep) {
                layer.msg("操作成功");
                placementSet.getAllJXB();
                placementSet.getCount();
            })
        })
        $("#cz").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布，不允许创建");
                return;
            }
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $("#grade .cur").attr("ids");
            Common.getData('/n33_setJXB/reXingZengBan.do', par, function (rep) {
                layer.msg("操作成功");
                placementSet.getAllJXB();
                placementSet.getCount();
            })
        })

        $("#tb").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布，不允许创建");
                return;
            }
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $("#grade .cur").attr("ids");
            $(".jhh").show();
            $('.bg').show();
            $(".imstu-popup1").hide();
            $(".jhhSp").text("正在同步行政班学生，请稍候...");
            setTimeout(function () {
                Common.getData('/studentTag/TongBuStuByClass.do', par, function (rep) {
                    layer.msg("操作成功");
                    placementSet.getAllJXB();
                    placementSet.getCount();
                })
                $(".bg").hide();
                $(".jhh").hide();
            },500);
        })

        $("body").on("click", '.bj', function () {
            if ($(this).text().indexOf('编辑') != -1) {
                $(".nnm").removeAttr('disabled', 'disabled');
                $(".rl").removeAttr('disabled', 'disabled');
                $(".jxbks").removeAttr('disabled', 'disabled');
                $(this).text('保存');
                $(this).removeClass('auto-bt').addClass('class-bt');
            } else {
                $(".nnm").attr('disabled', 'disabled');
                $(".rl").attr('disabled', 'disabled');
                $(".jxbks").attr('disabled', 'disabled');
                $(this).text('编辑');
                $(this).removeClass('class-bt').addClass('auto-bt');
            }
        });

        $("body").on("click", ".class-bt", function () {
            $(this).removeClass('class-bt').addClass('auto-bt');
            placementSet.updateJXB();
            placementSet.getAllJXB();
            placementSet.getCount();
        })

        $("body").on("click", "#grade em", function () {
            if ($(".bj").text().indexOf('保存') != -1) {
                $(".nnm").attr('disabled', 'disabled');
                $(".rl").attr('disabled', 'disabled');
                $(".bj").text('编辑');
                $(".bj").removeClass('class-bt').addClass('auto-bt');
            }

            $("#grade em").removeClass("cur");
            $(this).addClass("cur");
            placementSet.getSubjectByType(1);
            placementSet.createJXBIsNotZouBan();
            placementSet.getAllJXB();
            placementSet.getCount();
        });

        $("body").on("click", "#subject em", function () {
            if ($(".bj").text().indexOf('保存') != -1) {
                $(".nnm").attr('disabled', 'disabled');
                $(".rl").attr('disabled', 'disabled');
                $(".bj").text('编辑');
                $(".bj").removeClass('class-bt').addClass('auto-bt');
            }

            $("#subject em").removeClass("cur")
            $(this).addClass("cur");
            placementSet.getAllJXB();
            placementSet.getCount();
        })

        $("body").on("click", "#sc", function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布，不允许导入");
                return;
            }
            $(".bg").show();
            $(".imjxb-popup1").show();
        });

        $("body").on("click", ".download", function () {
            window.location.href = "/paike/exportTemplate.do";
        })

        $("body").on("click", "#qddr", function () {
            var par = {};
            par.xqid = deXqid;
            if (!par.xqid || par.xqid == "" || par.xqid == null) {
                layer.alert("请选择一个学期");
                return;
            }
            if ($("#file").val() != "") {
                var value = $("#file").val();
                var result = /\.[^\.]+/.exec(value);
                if (result == ".xls" || result == ".xlsx") {
                    $(".jhh").show();
                    $('.bg').show();
                    $(".imjxb-popup1").hide();
                    $(".jhhSp").text("正在导入教学班，请稍候...");
                    $.ajaxFileUpload({
                        url: '/paike/importJXBExcel.do',
                        param: par,
                        secureuri: false,
                        async: false,
                        fileElementId: 'file',
                        dataType: 'json',
                        success: function (data) {
                            $(".jhh").hide();
                            $('.bg').hide();
                            placementSet.getAllJXB();
                            placementSet.getCount();
                            if (data.message.length > 0) {
                                Common.render({
                                    tmpl: $('#errorList'),
                                    data: data.message,
                                    context: '#stuErrorList',
                                    overwrite: 1
                                });
                                $(".bg").show();
                                $(".drsb-popup1").show();
                            } else {
                                layer.msg("导入成功");
                            }
                            var file = $("#file")
                            file.after(file.clone().val(""));
                            file.remove();
                            $(".file-name").html("");
                        },
                        error: function (e) {
                            var file = $("#file")
                            file.after(file.clone().val(""));
                            file.remove();
                            $(".file-name").html("");
                            $(".jhh").hide();
                            $('.bg').hide();
                            layer.msg("网络错误，请重试！！");
                        }
                    })
                } else {
                    var file = $("#file")
                    file.after(file.clone().val(""));
                    file.remove();
                    $(".file-name").html("");
                    layer.msg("请使用excel格式导入");
                }
            } else {
                var file = $("#file")
                file.after(file.clone().val(""));
                file.remove();
                $(".file-name").html("");
                layer.msg("请选择文件");
            }
        });

        $("body").on("change", "#file", function () {
            var _this = $(this);
            var nameArr = _this.val().split("\\");
            var filename = nameArr[nameArr.length - 1];
            $(".file-name").html(filename);
        });

        $("body").on("click", ".opts", function () {
            var par = {};
            par.id = $(this).attr("ids");
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布，不允许删除");
                return;
            }
            Common.getData('/n33_setJXB/getStuCount.do', par, function (rep) {
                if (rep.message > 0) {
                    layer.confirm('该教学班存在学生，确认删除？', {
                        btn: ['确定', '取消'] //按钮
                    }, function () {
                        Common.getData('/n33_setJXB/removeJXB.do', par, function (rep) {
                            layer.msg(rep.message);
                            placementSet.getAllJXB();
                            placementSet.getCount();
                        });
                    }, function () {
                    });
                } else {
                    layer.confirm('确认删除？', {
                        btn: ['确定', '取消'] //按钮
                    }, function () {
                        Common.getData('/n33_setJXB/removeJXB.do', par, function (rep) {
                            layer.msg(rep.message);
                            placementSet.getAllJXB();
                            placementSet.getCount();
                        });
                    }, function () {
                    });
                }
                $('.bj').text('编辑');
                $('.bj').removeClass('class-bt').addClass('auto-bt')
            });
        });

        $("body").on("click", ".pos", function () {
            var par = {};
            par.xqid = deXqid;
            par.gid = $("#grade .cur").attr("ids");
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布，不允许创建教学班");
                return;
            }
            if (par.xqid != "" && par.xqid != undefined && par.xqid != null && par.gid != "" && par.gid != undefined && par.gid != null) {
                layer.prompt({
                    formType: 0,
                    value: '',
                    title: '请输入班级容量！'
                }, function (value, index, elem) {
                    if (value.trim() != "") {
                        par.rl = value;
                        layer.close(index);
                        Common.getData('/n33_setJXB/createJXB.do', par, function (rep) {
                            layer.msg(rep.message);
                            placementSet.getAllJXB();
                            placementSet.getCount();
                        })
                    } else {
                        layer.msg("请输入班级容量！");
                    }
                });
            } else {
                layer.msg("无法自动创建教学班，请检查默认学期和年级");
            }
        });

        $("#subType").change(function () {
            placementSet.getSubjectByType(2);
            placementSet.getAllJXB();
            placementSet.getCount();
        });

        $("body").on("click", ".xj", function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布，不允许新建教学班");
                return;
            }
            $('.bg').show();
            $('.addjxb-popup1').show();
        })

        $("body").on("click", "#qd", function () {
            if ($("#inp-nnm").val() == "") {
                $('.bg').show();
                $('.addjxb-popup1').show();
                layer.msg("请输入名称！");
            } else {
                placementSet.addJXB();
                placementSet.getAllJXB();
                placementSet.getCount();
                $('.inp-nnm').val("");
                $('.inp-rl').val("");
            }
        })


        placementSet.getDefaultTerm();
        placementSet.getGradeList();
        placementSet.getSubjectByType(1);
        placementSet.getAllJXB();
        placementSet.getCount();
        initGrade();

    }

    placementSet.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            deXqid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }


    placementSet.addJXB = function () {
        var par = {};
        par.name = $("#inp-nnm").val();
        par.nickName = $("#inp-nnm").val();
        par.subjectId = $("#subject .cur").attr("ids");
        par.gradeId = $("#grade .cur").attr("ids");
        par.termId = deXqid;
        par.type = $("#addSubType").val();
        par.rl = $("#inp-rl").val();
        if (par.termId != "" && par.termId != null && par.termId != undefined && par.subjectId != "" && par.subjectId != null && par.subjectId != undefined && par.gradeId != "" && par.gradeId != null && par.gradeId != undefined) {
            Common.getPostBodyData('/n33_setJXB/addJXBDto.do', par, function (rep) {
                layer.msg(rep.message);
                if (rep.message == "增加成功") {
                    $('.bg').hide();
                    $('.addjxb-popup1').hide();
                }
            });
        } else {
            layer.msg("增加教学班失败,请检查是否选中年级和学科");
        }

    }

    placementSet.getCount = function () {
        var par = {};
        par.gid = $("#grade .cur").attr("ids");
        par.xqid = deXqid;
        par.subid = $("#subject .cur").attr("ids");
        if (!par.xqid || par.xqid == "" || par.xqid == null || !par.gid || par.gid == "" || par.gid == null || par.subid == "" || !par.subid || par.subid == null) {
            return;
        }
        Common.getData('/n33_setJXB/getCount.do', par, function (rep) {
            if (rep.message.isZouBan == 1) {
                $("#count").text(rep.message.subjectName + "：等级考(" + rep.message.studentSubjectCount + "人)/合格考(" + rep.message.feiCount + "人)");
            } else {
                $("#count").text(rep.message.subjectName + "：" + rep.message.stuCount + "人");
            }

        });
    }

    placementSet.updateJXB = function () {
        var map = {};
        var gid = $("#grade .cur").attr("ids");
        var dtos = new Array();
        $(".jxbTr").each(function () {
            var par = {};
            par.id = $(this).attr("ids");
            par.nnm = $(this).find(".nnm").val();
            par.rl = $(this).find(".rl").val();
            par.jxbks = $(this).find(".jxbks").val();
            dtos.push(par);
        })
        map.gid = gid;
        map.xqid = deXqid;
        map.dtos = dtos;
        if (map.xqid == "" || !map.xqid || map.xqid == null || !map.gid || map.gid == "" || map.gid == null) {
            return;
        }
        Common.getPostBodyData('/n33_setJXB/updateJXBList.do', map, function (rep) {

        });
    }

    placementSet.createJXBIsNotZouBan = function () {
        var par = {};
        par.gid = $("#grade .cur").attr("ids");
        par.xqid = deXqid;
        if (!par.xqid || par.xqid == "" || par.xqid == null || !par.gid || par.gid == "" || par.gid == null) {
            layer.msg("自动创建非走班教学班失败");
            return;
        }
        Common.getData('/n33_setJXB/createJXBIsNotZouBan.do', par, function (rep) {
        });
    }

    placementSet.getAllJXB = function () {
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $("#grade .cur").attr("ids");
        par.subjectId = $("#subject .cur").attr("ids");
        par.subType2 = $("#subType").val();
        if (!par.xqid || par.xqid == "" || par.xqid == null || !par.gradeId || par.gradeId == "" || par.gradeId == null || par.subjectId == "" || !par.subjectId || par.subjectId == null) {
            Common.render({tmpl: $('#jxbList'), data: [], context: '#jxb', overwrite: 1});
            return;
        }
        Common.getData('/n33_setJXB/getAllJXBList.do', par, function (rep) {
            if(rep.message.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            Common.render({tmpl: $('#jxbList'), data: rep.message, context: '#jxb', overwrite: 1});

        })
    }

    placementSet.getSubjectByType = function (type) {
        var oldSubId = "";
        if (type == 2) {
            oldSubId = $("#subject .cur").attr("ids");
        }
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $("#grade .cur").attr("ids");
        par.subType = $("#subType").val();
        if (!par.xqid || par.xqid == "" || par.xqid == null || !par.gradeId || par.gradeId == "" || par.gradeId == null) {
            Common.render({tmpl: $('#subject_temp'), data: [], context: '#subject', overwrite: 1});
            return;
        }
        Common.getData('/n33_setJXB/getSubjectByTypeExitZX.do', par, function (rep) {
            Common.render({tmpl: $('#subject_temp'), data: rep.message, context: '#subject', overwrite: 1});
            var flag = true;
            if (type == 2) {
                $("#subject em").each(function () {
                    if ($(this).attr("ids") == oldSubId) {
                        flag = false;
                        $(this).addClass("cur");
                    }
                });
                if (flag) {
                    $("#subject em:eq(0)").addClass("cur");
                }
            } else {
                //$("#subject em:eq(0)").addClass("cur");
            }
        });
    }

    placementSet.getGradeList = function () {
        var par = {};
        par.xqid = deXqid;
        if (!par.xqid || par.xqid == "" || par.xqid == null) {
            Common.render({tmpl: $('#grade_temp'), data: [], context: '#grade', overwrite: 1});
            return;
        }
        Common.getData('/new33isolateMange/getGradList.do', par, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            //$("#grade em:eq(0)").addClass("cur");
        })
    }

    function initGrade() {
        try {
            Common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }

                        if (obj.key == "subjectId") {
                            subjectId = obj.value;
                        }

                        if (obj.key == "subType2") {
                            subType2 = obj.value;
                        }
                    })
                    $("#grade em").each(function () {
                        if (gradeId == $(this).attr("ids")) {
                            $(this).click();
                        }
                    });

                    $("#subType").val(subType2).change();

                    $("#subject em").each(function () {
                        if (subjectId == $(this).attr("ids")) {
                            $(this).click();
                        }
                    });

                    if ($("#subType").val() == null) {
                        $("#subType").val(0);
                        $("#subType").change();
                    }
                } else {

                }
            });
        } catch (x) {

        }
    }

    function getCiIdIsFaBu() {
        var flag = null;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId": deXqid}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }

    module.exports = placementSet;
})