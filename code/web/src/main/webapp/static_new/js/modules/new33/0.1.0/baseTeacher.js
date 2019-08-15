/**
 * Created by albin on 2018/3/7.
 */
define('baseTeacher', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer', 'select2'], function (require, exports, module) {
    var baseTeacher = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    require('select2');
    Common = require('common');
    var deXqid = "";
    var isFaBu = true;
    var teaList = new Array();
    baseTeacher.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            deXqid = rep.message.paikeci;
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term").val(rep.message.paikeci);
        });
    }
    baseTeacher.addTea = function (dto) {
        dto.unm = $("#name").val();
        dto.isBanZhuRen = $("#banZhuRen").val();
        dto.classNum = $("#classNum").val();
        dto.sex = $("#sex").val();
        dto.xqid = deXqid;
        var gradeIds = new Array();
        $(".ck:checked").each(function () {
            gradeIds.push($(this).attr("gid"));
        })
        var memberIds = $('#subjectList').select2('val');
        if(memberIds.length==0){
            layer.msg("请选择学科！", {time: 2000, zIndex : 9999});
            return false;
        }
        dto.gradeIds = gradeIds;
        dto.subjectIds = memberIds;
        if (dto.unm == "") {
            layer.msg('请输入老师姓名');
            return;
        }
        if (dto.gradeIds.length == 0) {
            layer.msg('请选择年级');
            return;
        }
        Common.getPostBodyData('/new33isolateMange/addTeaDto.do', dto, function (rep) {
            //classTime.getGradeSubjectList();
            $('.wind-new-tea,.bg').hide();
            $("#name").val("")
            $(".ck").prop("checked", false);
        })
        baseTeacher.getTeaList();
        baseTeacher.getTeaListd();
    }

    baseTeacher.getTea = function (id) {
        $(".wind-new-tea,.bg").show();
        var par = {};
        par.id = id;
        par.gid = $("#grade .cur").attr("ids");
        Common.getData('/new33isolateMange/getTea.do', par, function (rep) {
            $("#name").val(rep.message.unm)
            $("#sex option[value=" + rep.message.sex + "]").prop("selected", true);
            $("#banZhuRen option[value=" + rep.message.isBanZhuRen + "]").prop("selected", true);
            $("#subjectList").val(rep.message.subjectIds).trigger('change')
            //$("#subjectList").val(rep.message.subjectIds)
            $.each(rep.message.gradeIds, function (i, obj) {
                $(".ck[gid=" + obj + "]").prop("checked", true);
            })
            $("#classNum").val(rep.message.classNum)
            $(".btn-ok").attr("ids", rep.message.id)
            $(".btn-ok").attr("uid", rep.message.uid)
        })
    }
    baseTeacher.addTeaList = function () {
        var list = new Array();
        $.each(teaList, function (i, obj) {
            var dto = {};
            dto.id = obj.id;
            dto.uid = obj.uid;
            dto.unm = obj.unm;
            dto.sex = obj.sex;
            dto.isBanZhuRen = obj.isBanZhuRen;
            dto.classNum = obj.classNum;
            dto.xqid = deXqid;
            dto.gradeIds = obj.gradeIds;
            dto.subjectIds = obj.subjectIds;
            list.push(dto);
        })
        Common.getPostBodyData('/new33isolateMange/addTeaDtoList.do?gid=' + $("#grade .cur").attr("ids"), list, function (rep) {
            $('.cie-popup,.bg').hide();
        })
        baseTeacher.getTeaList();
        baseTeacher.getTeaListd();
    }
    baseTeacher.getIsFaBu = function () {
        var par = {};
        par.ciId = deXqid;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', par, function (rep) {
            isFaBu = rep.message;
        });
    }

    baseTeacher.init = function () {
        $('.popup-top i,.qx').click(function () {
            $('.bg').hide();
            $('.cie-popup').hide();
            baseTeacher.getTeaList();
            baseTeacher.getTeaListd();
        })
        $("body").on("click", ".ad-red_x", function () {
            var userId = $(this).attr("uid");
            $("#useSub").append("<li>" + $(this).attr("unm") + "&nbsp;" + $(this).attr("subjectStr") + "<i class='fft'sex=" + $(this).attr("sex") + "  subjectstr=" + $(this).attr("subjectStr") + " classnum=" + $(this).attr("classnum") + " isbanzhuren=" + $(this).attr("isbanzhuren") + " ids=" + $(this).attr("ids") + " uid=" + $(this).attr("uid") + " sub=" + $(this).attr("sub") + " unm=" + $(this).attr("unm") + "></i></li>")
            $(this).parent().remove()
            var sub = $(this).attr("sub");
            var teList = new Array();
            $.each(teaList, function (i, obj) {
                if (obj.uid != userId) {
                    teList.push(obj);
                } else {
                    var subList = new Array();
                    $.each(obj.subjectIds, function (dt, dd) {
                        if (dd != sub) {
                            subList.push(dd);
                        }
                    })
                    obj.subjectIds = subList;
                    if (obj.subjectIds.length > 0) {
                        teList.push(obj)
                    }
                }
            })
            teaList = teList;
        })
        baseTeacher.getDefaultTerm();
        baseTeacher.getIsFaBu();
        $("#sousuo").click(function () {
            baseTeacher.getTeaList();
            baseTeacher.getTeaListd();
        })
        $(".qd").click(function () {
            baseTeacher.addTeaList();
        })

        $("body").on("click", "#subList li", function () {
            $(this).addClass('xz-bg').siblings().removeClass('xz-bg');
            $(this).find('i').toggleClass('xz-cur');
            baseTeacher.getXuanList();
            baseTeacher.getSubjectList();
        })
        $("body").on("click", "#useSub .fft", function () {
            var bf = true;
            var sub = $(this).attr("sub");
            var ui = $(this).attr("uid")
            $.each(teaList, function (itt, ddt) {
                if (ddt.uid == ui) {
                    ddt.subjectIds.push(sub)
                    bf = false;
                }
            })
            $("#yiXuan").append("<li>" + $(this).attr("unm") + "&nbsp;" + $(this).attr("subjectstr") + "<i class='ad-red_x fft' sex=" + $(this).attr("sex") + " subjectstr=" + $(this).attr("subjectstr") + " classnum=" + $(this).attr("classnum") + " isbanzhuren=" + $(this).attr("isbanzhuren") + " ids=" + $(this).attr("ids") + " uid=" + $(this).attr("uid") + " sub=" + $(this).attr("sub") + " unm=" + $(this).attr("unm") + "></i></li>")
            $(this).parent().remove()
            if (bf) {
                var dto = {};
                dto.id = $(this).attr("ids");
                dto.uid = $(this).attr("uid");
                dto.unm = $(this).attr("unm");
                dto.sex = $(this).attr("sex");
                dto.isBanZhuRen = $(this).attr("isBanZhuRen");
                dto.classNum = $(this).attr("classNum");
                dto.xqid = deXqid;
                var gradeIds = new Array();
                gradeIds.push($("#grade .cur").attr("ids"));
                dto.gradeIds = gradeIds;
                var subjectIds = new Array();
                subjectIds.push($(this).attr("sub"));
                dto.subjectIds = subjectIds;
                teaList.push(dto);
            }
        })
        $('.sel-tea').click(function () {
            if (isFaBu == false) {
                $('.bg').show();
                $('.cie-popup').show();
                $("#useSub").empty();
                baseTeacher.getXuanList();
                baseTeacher.getSubjectList();
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })

        $('.new-tea').click(function () {
            if (isFaBu == false) {
                $('.wind-new-tea,.bg').fadeIn();
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("body").on("click", ".opt", function () {
            if (isFaBu == false) {
                baseTeacher.getTea($(this).attr("ids"));
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("body").on("click", ".opts", function () {
            if (isFaBu == false) {
                var par = {};
                par.id = $(this).attr("ids");
                layer.confirm('确认删除？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    Common.getData('/new33isolateMange/deleteTea.do', par, function (rep) {
                        layer.msg(rep.message);
                        baseTeacher.getTeaList();
                        baseTeacher.getTeaListd();
                    });
                }, function () {
                });
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })

        $(".btn-ok").click(function () {
            var dto = {};
            dto.id = $(this).attr("ids");
            dto.uid = $(this).attr("uid");
            baseTeacher.addTea(dto);
        })
        $("body").on("click", "#subject em", function () {
            $("#subject em").removeClass("cur")
            $(this).addClass("cur")
            baseTeacher.getTeaList();
            baseTeacher.getTeaListd();
        })
        $("body").on("click", "#grade em", function () {
            $("#grade em").removeClass("cur")
            $(this).addClass("cur")
            baseTeacher.getGradeSubjectList();
            baseTeacher.getTeaList();
            baseTeacher.getTeaListd();
        })
        $("body").on("click", "#term em", function () {
            $("#term em").removeClass("cur")
            $(this).addClass("cur")
            baseTeacher.getGradeList();
            baseTeacher.getGradeSubjectList();
            baseTeacher.getTeaList();
            baseTeacher.getTeaListd();
        })

        $('body').on("click",".ks-tea",function () {
            $('#gradeName').text("年级:" + $('#grade .cur').text());
            $('#subjectName').text("  学科:" + $('#subject .cur').text());
            baseTeacher.getTeaListForKS();
            $('.ciee-popup').show();
            $('.bg').show();
        });
        $('body').on("click","#teaKs",function () {
            baseTeacher.getTeaListForKS();
        });

        //baseTeacher.getTermList();
        baseTeacher.getGradeList();
        baseTeacher.getGradeSubjectList();
        baseTeacher.getTeaList();
        baseTeacher.getTeaListd();
    }
    baseTeacher.getTermList = function () {
        Common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term', overwrite: 1});
                $("#term em:eq(0)").addClass("cur");
            }
        });
    }
    baseTeacher.getGradeList = function () {
        var xqid = deXqid;
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            $("#grade em:eq(0)").addClass("cur");
            $("#checkedGra").empty();
            Common.render({tmpl: $('#gradeCheck'), data: rep.message, context: '#checkedGra'});
        })
    }

    baseTeacher.getGradeSubjectList = function () {
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $("#grade .cur").attr("ids");
        $("#subject").empty();
        $("#subjectList").empty();
        $("#subList").empty()
        if (par.gradeId != undefined) {
            Common.getData('/new33isolateMange/getSubjectByGradeId.do', par, function (rep) {
                $("#subject").append("<em ids='*'>全部</em>")
                Common.render({tmpl: $('#subject_temp'), data: rep.message, context: '#subject'});
                $("#subject em:eq(0)").addClass("cur");
                Common.render({tmpl: $('#subjectOpt'), data: rep.message, context: '#subjectList'});
                $('#subjectList').select2("destroy");
                initSelect($('#subjectList'));
                Common.render({tmpl: $('#subLi'), data: rep.message, context: '#subList'});
                $("#subList li:eq(0)").addClass("xz-bg");
            })
        }
    }
    function initSelect(target) {
        target.select2({
            width: '270px',
            containerCss: {
                'margin-left': '10px',
                'font-family': 'sans-serif'
            },
            dropdownCss: {
                'font-size': '14px',
                'font-family': 'sans-serif'
            }
        });
    }
    baseTeacher.getTeaList = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $("#grade .cur").attr("ids");
        par.subid = $("#subject .cur").attr("ids");
        par.name = "*";
        if ($("#teaName").val() != "") {
            par.name = $("#teaName").val();
        }
        Common.getData('/new33isolateMange/getTeaList.do', par, function (rep) {
            if(rep.message.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            if (par.name == "*") {
                teaList = rep.message;
            }
            Common.render({tmpl: $('#tbdList'), data: rep.message, context: '#tbd', overwrite: 1});
        })
    }

    baseTeacher.getTeaListForKS = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $("#grade .cur").attr("ids");
        par.subid = $("#subject .cur").attr("ids");
        par.name = "*";
        if ($("#teaKsName").val() != "") {
            par.name = $("#teaKsName").val();
        }
        Common.getData('/new33isolateMange/getTeaListKS.do', par, function (rep) {
            Common.render({tmpl: $('#ksList_temp'), data: rep.message, context: '#ksList', overwrite: 1});
        })
    }

    baseTeacher.getTeaListd = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $("#grade .cur").attr("ids");
        par.subid = "*";
        par.name = "*";
        Common.getData('/new33isolateMange/getTeaList.do', par, function (rep) {
            teaList = rep.message;
        })
    }
    baseTeacher.getXuanList = function () {
        $("#yiXuan").empty();
    }
    baseTeacher.getSubjectList = function () {
        var par = {};
        par.subid = $(".xz-bg").attr("ids");
        $("#useSub").empty();
        var list = new Array();
        var list1 = new Array();
        Common.getData('/new33isolateMange/get2SubjectList.do', par, function (rep) {
            $.each(rep.message.userIds, function (i, obj) {
                var item = 1;
                var dto = {};
                dto.uid = obj;
                dto.id = "*";
                dto.unm = rep.message.userName[i];
                dto.sub = rep.message.id;
                dto.subjectName = rep.message.subjectName;
                dto.subjectStr = $(".xz-bg").html();
                $.each(teaList, function (itt, ddt) {
                    var bf = false;
                    $.each(ddt.subjectIds, function (df, dd) {
                        if (dd == rep.message.id) {
                            bf = true;
                        }
                    })
                    if (obj == ddt.uid && bf) {
                        dto.isBanZhuRen = ddt.isBanZhuRen;
                        dto.classNum = ddt.classNum;
                        dto.sex = ddt.sex;
                        item = 0;
                    }
                })
                if (item == 1) {
                    dto.sex = rep.message.sex[i];
                    list.push(dto)
                } else {
                    list1.push(dto);
                }
            })
        })
        par.xqid = deXqid;
        par.gid = $("#grade .cur").attr("ids");
        Common.getData('/new33isolateMange/getTeaList.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var item = 0;
                $.each(list1, function (itt, ddt) {
                    if (obj.uid == ddt.uid) {
                        item = 1;
                    }
                })
                if (item == 0) {
                    obj.sub = $(".xz-bg").attr("ids");
                    obj.subjectName = $(".xz-bg").html();
                    list1.push(obj);
                }
            })
        })
        if (list.length > 0) {
            Common.render({tmpl: $('#userLists'), data: list, context: '#useSub', overwrite: 1});
        }
        if (list1.length > 0) {
            Common.render({tmpl: $('#userList'), data: list1, context: '#yiXuan', overwrite: 1});
        }
    }
    module.exports = baseTeacher;
})