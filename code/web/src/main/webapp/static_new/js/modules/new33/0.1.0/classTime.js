/**
 * Created by albin on 2018/3/7.
 */
define('classTime', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var classTime = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var SubjectList = new Array();
    var isFaBu = true;
    classTime.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            deXqid = rep.message.paikeci;
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term").val(rep.message.paikeci);
        });
    }
    classTime.addSubjectList = function () {
        var dtos = new Array();
        $(".gra-sp .ssp-cur").each(function () {
            var dto = {};
            dto.xqid = deXqid;
            dto.gid = $("#grade").val();
            dto.type = $(this).attr("type");
            dto.type1 = $(this).attr("type1");
            dto.isZouBan = $(this).attr("isZouBan");
            dto.time = $(this).attr("time");
            dto.dTime = $(this).attr("dtime");
            dto.snm = $(this).attr("subjectName");
            dto.subid = $(this).attr("subjectId");
            dto.id = $(this).attr("ids");
            dto.dan = $(this).attr("dan");
            dtos.push(dto);
        })
        Common.getPostBodyData('/new33isolateMange/addSubjectList.do?gid='+$("#grade").val(), dtos, function (rep) {
            classTime.getGradeSubjectList();
            $('.gra-popup,.bg').hide();
        });
    }
    classTime.updateSubject = function () {
        var dtos = new Array();
        $("#subjectList tr").each(function () {
            var dto = {};
            dto.xqid = deXqid;
            dto.gid = $("#grade").val();
            dto.type = $(this).attr("type");
            dto.type1 = 0;
            dto.time = $(this).find(".inp").val();
            dto.dTime = 0;
            dto.isZouBan = $(this).attr("isZouBan");
            dto.snm = $(this).attr("subjectName");
            dto.subid = $(this).attr("subjectId");
            dto.id = $(this).attr("ids");
            dto.dan = $(this).attr("dan");
            dtos.push(dto);
        })
        Common.getPostBodyData('/new33isolateMange/addSubjectList.do?gid='+$("#grade").val(), dtos, function (rep) {
            classTime.getGradeSubjectList();
        });
    }
    classTime.getIsFaBu = function () {
        var par = {};
        par.ciId = deXqid;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', par, function (rep) {
            isFaBu = rep.message;
        });
    }
    classTime.init = function () {
        classTime.getDefaultTerm();
        classTime.getIsFaBu();
        $("body").on("click", ".ckbo", function () {
            var par = {};
            par.id = $(this).attr("ids");
            par.ty = $(this).attr("ty");
            par.type = 0;
            if ($(this).is(":checked")) {
                par.type = 1;
            }
            Common.getData('/new33isolateMange/updateDanShuangZhou.do', par, function (rep) {
                classTime.getGradeSubjectList();
            })
        })
        $("body").on("click", ".ckbod", function () {
            var par = {};
            par.id = $(this).attr("ids");
            par.dan = 0;
            if ($(this).is(":checked")) {
                par.dan = 1;
            }
            Common.getData('/new33isolateMange/updateZhuanDan.do', par, function (rep) {
                classTime.getGradeSubjectList();
            })
        })
        $("#grade").change(function () {
            classTime.getGradeSubjectList();
            classTime.getTeacherLimitEntryByXqid();
            if (isFaBu == false) {
            } else {
                $(".ckbo,.ckbod").prop("disabled", "disabled")
            }
        })
        $("#term").change(function () {
            classTime.getGradeList();
            classTime.getGradeSubjectList();
            classTime.getTeacherLimitEntryByXqid();
        })

        $('.optcont .btn-ok').click(function () {
            classTime.updateSubject();
            $('.cltmtab .fsr').hide();
            $('.cltmtab span').show();
            $(".btn-ok").hide();
            $(".btn-edit").show();
        })

        $('body').on('click', '.optcont .btn-edit', function () {
            if (isFaBu == false) {
                $(this).hide();
                $('.optcont .btn-ok').show();
                $('.cltmtab span').map(function () {
                    var val = $(this).text()
                    if (val === '-') {
                        val = ''
                    }
                    $(this).next().val(val)
                    $('.ckbo,.ckbod').attr("disabled", !this.checked);
                })
                $('.cltmtab td>input[type=text]').show();
                $('.cltmtab td>span').hide();
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        });

        $('.btn-Sele').click(function () {
            if (isFaBu == false) {
                $('.bg').show();
                $('.gra-popup').show();
                $(".gra-sp span").removeClass("ssp-cur")
                $(".gra-sp span").attr("ids", "*");
                $(".gra-sp span").attr("time", "0");
                $.each(SubjectList, function (i, obj) {
                    $(".gra-sp span[subjectid=" + obj.subid + "]").addClass("ssp-cur");
                    $(".gra-sp span[subjectid=" + obj.subid + "]").attr("ids", obj.id);
                    $(".gra-sp span[subjectid=" + obj.subid + "]").attr("dan", obj.dan);
                    $(".gra-sp span[subjectid=" + obj.subid + "]").attr("time", obj.time);
                    $(".gra-sp span[subjectid=" + obj.subid + "]").attr("dtime", obj.dtime);
                    $(".gra-sp span[subjectid=" + obj.subid + "]").attr("isZouBan", obj.isZouBan);
                    $(".gra-sp span[subjectid=" + obj.subid + "]").attr("type", obj.type);
                    $(".gra-sp span[subjectid=" + obj.subid + "]").attr("type1", obj.type1);
                })
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        classTime.getIsolateSubjectList();
        $(".qd").click(function () {
            classTime.addSubjectList();
        })
        //classTime.getTermList();
        classTime.getGradeList();
        initGrade();
        classTime.getGradeSubjectList();
        classTime.getTeacherLimitEntryByXqid();
        $("#limithour").change(function () {
            classTime.saveLimitHour();
        })

        $("body").on("click", "#zouban", function () {
            if (isFaBu == false) {
                classTime.getSubjectCanZouBan();
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        });

        $("body").on("click", ".zoubanSub span", function () {
            if ($(this).hasClass("sp-curr")) {
                $(this).removeClass("sp-curr");
            } else {
                $(this).addClass("sp-curr");
            }
        });

        $("body").on("click", "#qdzouban", function () {

            var array = new Array();
            $(".zoubanSub span").each(function () {
                var par = {};
                par.xqid = deXqid;
                par.subId = $(this).attr("subjectId");
                par.gid = $("#grade").val();
                if ($(this).hasClass("sp-curr")) {
                    par.isZouBan = "1";
                } else {
                    par.isZouBan = "0";
                }
                array.push(par);
            });
            Common.getPostBodyData('/n33_set/setIsZouBanList.do', array, function (rep) {
                classTime.getGradeSubjectList();
            });
            $(".zou-popup").hide();
            $(".bg").hide();
        });

        if (isFaBu == false) {
        } else {
            $(".ckbo,.ckbod").prop("disabled", "disabled")
        }
    }

    classTime.getSubjectCanZouBan = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $("#grade").val();
        Common.getData('/n33_set/getSubjectCanZouBan.do', par, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#zoubanSubject'), data: rep.message, context: '.zoubanSub', overwrite: 1});
            }
        });
        $('.bg').show();
        $('.zou-popup').show();
    }

    classTime.getTermList = function () {
        Common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term', overwrite: 1});
            }
        });
    }
    classTime.getGradeList = function () {
        var xqid = deXqid;
        if (xqid != "" && xqid != undefined) {
            Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
                Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            })
        }
    }

    classTime.getGradeSubjectList = function () {
        SubjectList = new Array();
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $("#grade").val();
        if (par.xqid != "" && par.xqid != undefined && par.gradeId != "" && par.gradeId != undefined) {
            Common.getData('/new33isolateMange/getSubjectByGradeId.do', par, function (rep) {
                if(rep.message.length==0){
                    $("#none_png").show();
                    $("#content").hide();
                }
                else{
                    $("#none_png").hide();
                    $("#content").show();
                }
                Common.render({tmpl: $('#subjectTrScript'), data: rep.message, context: '#subjectList', overwrite: 1});
                $.each(rep.message, function (i, obj) {
                    var dto = {};
                    dto.id = obj.id;
                    dto.subid = obj.subid
                    dto.time = obj.time
                    dto.dtime = obj.dTime
                    dto.type = obj.type
                    dto.type1 = obj.type1
                    dto.isZouBan = obj.isZouBan
                    dto.dan = obj.dan;
                    SubjectList.push(dto);
                })
            })
        }
    }

    classTime.getIsolateSubjectList = function () {
        Common.getData('/new33isolateMange/selSubjectList.do', {}, function (rep) {
             Common.render({tmpl: $('#subjectSpanScript'), data: rep.message, context: '.gra-sp', overwrite: 1});
        })

    }
    classTime.getTeacherLimitEntryByXqid = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $("#grade").val();
        Common.getData('/n33_teacherlimithour/getTeacherLimitEntryByXqid.do', par, function (rep) {
            $("#limithour").val(rep.hour);
            $("#limithour").attr("lid", rep.id);
        })
    }
    classTime.saveLimitHour = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $("#grade").val();
        par.hour = $("#limithour").val();
        if ($("#limithour").attr("lid") && $("#limithour").attr("lid") != null && $("#limithour").attr("lid") != "undefined") {
            par.id = $("#limithour").attr("lid");
        }
        Common.getPostBodyData('/n33_teacherlimithour/addLimitEntry.do', par, function (rep) {
            classTime.getTeacherLimitEntryByXqid();
        })
    }

    function initGrade() {
        try {
            Common.userData("n33", function (res) {
                var gradeId = "";
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                    });
                    if(gradeId != ""){
                        $("#grade").val(gradeId).change();
                    }
                } else {

                }
            });
        } catch (x) {

        }
    }

    module.exports = classTime;
})