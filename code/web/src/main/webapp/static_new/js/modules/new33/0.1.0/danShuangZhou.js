/**
 * Created by albin on 2017/7/25.
 */

define('danShuangZhou', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var danShuangZhou = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    common = require('common');
    var subid = ""
    var deXqid = ""
    var ClsList = new Array();
    var isFaBu = true;
    danShuangZhou.getDefaultTerm = function () {
        common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#defaultTerm").attr("ids", rep.message.paikeci);
            deXqid = rep.message.paikeci;
        });
    }

    danShuangZhou.getSubjectByType = function () {
        var par = {};
        par.xqid = $("#defaultTerm").attr("ids");
        par.gradeId = $("#grade .cur").attr("val");
        common.getData('/new33isolateMange/getSubjectByGradeId.do', par, function (rep) {
            common.render({tmpl: $('#subject_temp'), data: rep.message, context: '#subject', overwrite: 1});
            $("#subject em:eq(0)").addClass("cur");
            $("#subject em:eq(1)").addClass("cur");
            subid = $("#subject em:eq(1)").attr("val");
        })
    }

    danShuangZhou.getSubjectByType1 = function () {
        var par = {};
        par.xqid = $("#defaultTerm").attr("ids");
        par.gradeId = $("#grade .cur").attr("val");
        common.getData('/new33isolateMange/getSubjectByGradeId.do', par, function (rep) {
            common.render({tmpl: $('#sub_Item'), data: rep.message, context: '.fwo', overwrite: 1});
        })
    }
    danShuangZhou.chu = function () {
        $(".fwo").each(function () {
            var id = $(this).attr("subid");
            $(this).find("option[value=" + id + "]").prop("selected", true);
        })
    }

    danShuangZhou.getDanShuang = function (dan, shuang, cid) {
        var par = {};
        par.dan = dan;
        par.shuang = shuang;
        par.cid = cid;
        par.xqid = $("#defaultTerm").attr("ids");
        common.getData('/studentTag/updateDanShuang.do', par, function (rep) {
            if (rep.message != "ok") {
                layer.msg(rep.message)
                danShuangZhou.getClassListDanShuangZhou();
                danShuangZhou.getSubjectByType1();
                danShuangZhou.chu();
            }
        })
    }
    danShuangZhou.init = function () {
        $("body").on("click", ".jiaohuan", function () {
            if (isFaBu == false) {
                var par = {};
                par.dan = $(this).attr("dan");
                par.shuang = $(this).attr("shuang");
                layer.confirm('确认要交换位置么？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    common.getData('/studentTag/HuanDanShuang.do', par, function (rep) {
                        layer.msg('操作成功', {time: 1000});
                        danShuangZhou.getClassListDanShuangZhou();
                        danShuangZhou.getSubjectByType1();
                        danShuangZhou.chu();
                    });
                }, function () {
                });
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("#classId").change(function () {
            danShuangZhou.getClassListDanShuangZhou();
            danShuangZhou.getSubjectByType1();
            danShuangZhou.chu();
            if (isFaBu != false) {
                $(".sel2").prop("disabled", "disabled")
            }
        })
        $("body").on("click", ".del", function () {
            if (isFaBu == false) {
                var par = {};
                par.dan = $(this).attr("dan");
                par.shuang = $(this).attr("shuang");
                par.cid = $(this).attr("cid");
                par.xqid = $("#defaultTerm").attr("ids");
                common.getData('/studentTag/removeDanShuang.do', par, function (rep) {
                    danShuangZhou.getClassListDanShuangZhou();
                    danShuangZhou.getSubjectByType1();
                    danShuangZhou.chu();
                })
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        /*$('document').ready(function(){
            var len = $('#clsList td').length/2;
            $('#clsList td .w218>em').each(function(){
                var cun = $(this).find('.fks').hasClass('dRight');
                cun++;
            })
        })*/
        $('body').on('click','.gg1',function(){
            $(this).toggleClass('dRight');
            if($(this).is(':checked')){
                $('#clsList .fks').addClass('dRight').removeClass('rRight')
                var map = {};
                map.xqid = $("#defaultTerm").attr("ids");
                var cidList = new Array();
                $("#clsList .dRight").each(function () {
                    cidList.push($(this).attr("ids"));
                })
                map.cidList = cidList;
                map.subid1 = $("#subject .cur:eq(0)").attr("val");
                map.subid2 = $("#subject .cur:eq(1)").attr("val");
                common.getPostBodyData('/studentTag/setClassListDanShuangZhou1.do', map, function (rep) {
                    if (rep.message != "ok") {
                        layer.msg(rep.message);
                        $('#clsList .fks').removeClass('dRight').addClass('rRight')
                        $(".gg1").prop('checked',false);
                    }
                });
            }else{
                $('#clsList .fks').removeClass('dRight').addClass('rRight')
            }
        })
        $("body").on("change", ".fwo", function () {
            var dan = "";
            var shuang = "";
            if ($(this).hasClass("ft1")) {
                var ft2 = $(this).parents("tr").find(".ft2").val();
                if (ft2 != $(this).val()) {
                    dan = $(this).val();
                    shuang = ft2;
                }
            } else {
                var ft1 = $(this).parents("tr").find(".ft1").val();
                if (ft1 != $(this).val()) {
                    dan = ft1;
                    shuang = $(this).val();
                }
            }
            if (dan != "" && shuang != "") {
                danShuangZhou.getDanShuang(dan, shuang, $(this).attr("cid"))
            } else {
                layer.msg("同一学科不发建立单双周");
                danShuangZhou.getClassListDanShuangZhou();
                danShuangZhou.getSubjectByType1();
                danShuangZhou.chu();
            }
        })

        $("#addDanShuang").click(function () {
            danShuangZhou.setClassListDanShuangZhou();
        })
        $('.chat-bt').click(function () {
            if (isFaBu == false) {
                if ($('#subject .cur').length == 2) {
                    $('.bg').show();
                    $('.xz-popup').show();
                    $(".fks").removeClass("dRight")
                    $(".fks").addClass("rRight")
                    $.each(ClsList, function (i, obj) {
                        $(".fks[ids=" + obj + "]").addClass("dRight")
                        $(".fks[ids=" + obj + "]").removeClass("rRight")
                    })
                } else {
                    layer.msg("请选择两门学科！")
                }
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })

        $("body").on("click", ".dRight", function () {
            var bf = true;
            var ids = $(this).attr("ids");
            $.each(ClsList, function (i, obj) {
                if (obj == ids) {
                    bf = false;
                }
            })
            if (bf) {
                $(this).removeClass("dRight")
                $(this).addClass("rRight")
            } else {
                layer.msg("已经创建的单双周无法直接移除,请删除单双周后重试。")
            }
        })

        $("body").on("click", ".rRight", function () {
            var cid = $(this).attr("ids");
            $(this).addClass("dRight")
            var a = 1;
            if ($(this).has("rRight")) {
                var map = {};
                map.xqid = $("#defaultTerm").attr("ids");
                var cidList = new Array();
                cidList.push(cid);
                map.cidList = cidList;
                map.subid1 = $("#subject .cur:eq(0)").attr("val");
                map.subid2 = $("#subject .cur:eq(1)").attr("val");
                common.getPostBodyData('/studentTag/setClassListDanShuangZhou1.do', map, function (rep) {
                    if (rep.message != "ok") {
                        layer.msg(rep.message);
                        a = 0;
                    }
                });
            }
            if (a == 1) {
                $(this).removeClass("rRight")
            } else {
                $(this).removeClass("dRight")
            }
        })
        danShuangZhou.getDefaultTerm();
        danShuangZhou.getgrade();
        danShuangZhou.getSubjectByType();
        danShuangZhou.getClassList();
        danShuangZhou.getClassListDanShuangZhou();
        danShuangZhou.getSubjectByType1();
        danShuangZhou.chu();
        $("body").on("click", "#grade em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            danShuangZhou.getSubjectByType();
            danShuangZhou.getClassList();
            danShuangZhou.getClassListDanShuangZhou();
            danShuangZhou.getSubjectByType1();
            danShuangZhou.chu();
            if (isFaBu != false) {
                $(".sel2").prop("disabled", "disabled")
            }
        });
        $("body").on("click", "#subject em", function () {
            if ($(this).hasClass('sp')) {
                if ($('#subject .cur').length == 2) {
                    if ($(this).hasClass('cur')) {
                        $(this).addClass('cur');
                    } else {
                        $("#subject em").removeClass('cur')
                        $("#subject em[val=" + subid + "]").addClass('cur');
                        $(this).addClass('cur')
                        subid = $(this).attr("val");
                    }
                } else {
                    $(this).addClass('cur');
                    subid = $(this).attr("val");
                }
            } else {
                $(this).addClass('cur').siblings().removeClass('cur');
                subid = $(this).attr("val");
            }
            danShuangZhou.getClassListDanShuangZhou();
            danShuangZhou.getSubjectByType1();
            danShuangZhou.chu();
            if (isFaBu != false) {
                $(".sel2").prop("disabled", "disabled")
            }
        });
        danShuangZhou.getIsFaBu();
        if (isFaBu != false) {
            $(".sel2").prop("disabled", "disabled")
        }
    }
    danShuangZhou.getIsFaBu = function () {
        var par = {};
        par.ciId = deXqid;
        common.getData('/new33isolateMange/getCiIdIsFaBu.do', par, function (rep) {
            isFaBu = rep.message;
        });
    }
    danShuangZhou.getgrade = function () {
        common.getData('/new33isolateMange/getGradList.do', {"xqid": deXqid}, function (rep) {
            common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            $("#grade em:eq(0)").addClass("cur");
        })
    }

    danShuangZhou.getClassList = function () {
        var par = {};
        par.xqid = $("#defaultTerm").attr("ids");
        par.gradeId = $("#grade .cur").attr("val");
        $("#classId").empty();
        common.getData('/new33classManage/getClassList.do', par, function (rep) {
            $("#classId").append("<option value='*'>全部</option>")
            common.render({tmpl: $('#class_Item'), data: rep.message, context: '#classId'});
            common.render({tmpl: $('#clList'), data: rep.message, context: '#csList', overwrite: 1});
            //$("#grade em:eq(0)").addClass("cur");
        })
    }
    danShuangZhou.setClassListDanShuangZhou = function () {
        var map = {};
        map.xqid = $("#defaultTerm").attr("ids");
        var cidList = new Array();
        $("#clsList .dRight").each(function () {
            cidList.push($(this).attr("ids"));
        })
        map.cidList = cidList;
        map.subid1 = $("#subject .cur:eq(0)").attr("val");
        map.subid2 = $("#subject .cur:eq(1)").attr("val");
        if (cidList.length > 0) {
            common.getPostBodyData('/studentTag/setClassListDanShuangZhou.do', map, function (rep) {
                if (rep.message != "ok") {
                    layer.msg(rep.message);
                } else {
                    $(".xz-popup,.bg").hide();
                    danShuangZhou.getClassListDanShuangZhou();
                    danShuangZhou.getSubjectByType1();
                    danShuangZhou.chu();
                    layer.msg("创建成功!");
                }
            });
        }
    }
    danShuangZhou.getClassListDanShuangZhou = function () {
        var map = {};
        map.xqid = $("#defaultTerm").attr("ids");
        var cidList = new Array();
        if ($("#classId").val() == "*") {
            $(".f4s").each(function () {
                cidList.push($(this).attr("value"))
            })
        } else {
            cidList.push($("#classId").val())
        }
        map.cidList = cidList;
        var subList = new Array();
        $("#subject .cur").each(function () {
            subList.push($(this).attr("val"))
        })
        map.subList = subList;
        map.gid = $("#grade .cur").attr("val");
        ClsList = new Array();
        common.getPostBodyData('/studentTag/getClassListDanShuangZhou.do', map, function (rep) {
            common.render({tmpl: $('#bd'), data: rep.message, context: '#tbd', overwrite: 1});
            $.each(rep.message, function (i, obj) {
                var b = false;
                var c = false;
                if (obj.dan == $("#subject .cur:eq(0)").attr("val") || obj.dan == $("#subject .cur:eq(1)").attr("val")) {
                    b = true;
                }
                if (obj.shuang == $("#subject .cur:eq(0)").attr("val") || obj.shuang == $("#subject .cur:eq(1)").attr("val")) {
                    c = true;
                }
                if (b == true && c == true) {
                    ClsList.push(obj.cid);
                }
            });
        })
    }
    module.exports = danShuangZhou;
})
