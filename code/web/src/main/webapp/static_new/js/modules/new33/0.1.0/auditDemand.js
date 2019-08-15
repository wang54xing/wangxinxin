/**
 * Created by albin on 2018/3/30.
 */
define('auditDemand', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var auditDemand = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var par = {};
    auditDemand.init = function () {

        $("body").on("click", ".ttc", function () {
            if (!$(this).hasClass("cb5") && !$(this).hasClass("c0a") && !$(this).hasClass("c27") && !$(this).hasClass("cef")) {
                var obj = $(this);
                var flag = false;

                $(".pkType").each(function (i, st) {
                    if ($(this).hasClass("cb5") || $(this).hasClass("c0a") || $(this).hasClass("c27") || $(this).hasClass("cef")) {
                        flag = true;
                    }
                });

                var flag2 = true;
                if ($("#term .cur").attr("ids") != deXqid) {
                    layer.alert("只允许提交排课学期的需求");
                    flag2 = false;
                }

                if (flag && flag2) {
                    $('.bg').show();
                    $('.yy-popup').show();
                    $('#qddesc').attr("x", obj.attr("x"));
                    $('#qddesc').attr("y", obj.attr("y"));
                } else {
                    if (!flag && flag2) {
                        layer.alert("请选择需求类型");
                    }
                }
            }
        });

        $("body").on("click", "#qddesc", function () {
            var array = new Array();
            var par = {};
            par.xqid = deXqid;
            par.teaId = $(".teaCur").attr("ids");
            var map = {};
            map.x = $(this).attr("x");
            map.y = $(this).attr("y");
            map.desc = $("#desc").val();
            if($("#desc").val() != null && $("#desc").val() != ""){
                map.require = "1";
                map.status = $(".select").attr("type");
                var gradeList = new Array();
                $(".gradeCheck").each(function () {
                    if ($(this).is(":checked")) {
                        gradeList.push($(this).attr("ids"));
                    }
                });
                map.gid = gradeList;
                if (gradeList.length == 0) {
                    layer.alert("请选择年级");
                }else{
                    array.push(map);
                    par.teaRulesList = array;
                    Common.getPostBodyData('/teaRules/addTeaRulesByTeaId.do', par, function (rep) {
                        $('.bg').hide();
                        $('.yy-popup').hide();
                        auditDemand.getChushi();
                        auditDemand.getTeaRulesByTeaId();
                    });
                }
            }else{
                layer.alert("请输入需求原因");
            }

        });

        $("body").on("click", ".ttd1", function () {
            if ($(this).hasClass("cb5") || $(this).hasClass("c0a") || $(this).hasClass("c27") || $(this).hasClass("cef")) {
                var map = {};
                map.id = $(this).attr("ids");
                map.xqid = $("#term .cur").attr("ids");
                map.teaId = $(".teaCur").attr("ids");
                var gradeList = new Array();
                $(".gradeCheck").each(function () {
                    if ($(this).is(":checked")) {
                        gradeList.push($(this).attr("ids"));
                    }
                });
                map.gradeList = gradeList;
                Common.getPostBodyData('/teaRules/deleteTeaRules.do', map, function (rep) {
                    auditDemand.getChushi();
                    auditDemand.getTeaRulesByTeaId();
                });
            }
        })

        $("body").on("click", "#search", function () {
            auditDemand.getTea();
        });

        $('body').on("click", "#term em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            auditDemand.getGrade();
            auditDemand.getSubjectList();
            auditDemand.getTea();
            auditDemand.getKBMSChushi();
            auditDemand.getGuDingShiWu();
            auditDemand.getTeaRules();
        });

        $('.newAdd').click(function () {
            $('.mm1').hide();
            $('.mm2').show();
            auditDemand.getTeaBySubject();
        })

        $('body').on("click", "#grade em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            auditDemand.getSubjectList();
            auditDemand.getTea();
            auditDemand.getKBMSChushi();
            auditDemand.getGuDingShiWu();
            auditDemand.getTeaRules();
        });

        $('body').on("click", "#subject em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            auditDemand.getTea();
            auditDemand.getKBMSChushi();
            auditDemand.getGuDingShiWu();
            auditDemand.getTeaRules();
        });

        $('body').on("click", "#tea li", function () {
            //$(this).addClass('cur').siblings().removeClass('cur');
            $(this).toggleClass('curr');
            auditDemand.getKBMSChushi();
            auditDemand.getGuDingShiWu();
            auditDemand.getTeaRules();
        });

        $('body').on("click", "#tea1 li", function () {
            //$(this).addClass('cur').siblings().removeClass('cur');
            $(this).toggleClass('teaCur').siblings().removeClass("teaCur");
            var gradeList = new Array();
            $(".gradeCheck").each(function () {
                if ($(this).is(":checked")) {
                    gradeList.push($(this).attr("ids"));
                }
            });
            if (gradeList.length == 0) {
                return;
            } else {
                auditDemand.getTeaRulesByTeaId();
            }
        });

        $('#status').on("change", function () {
            //$(this).addClass('cur').siblings().removeClass('cur');
            auditDemand.getTeaRules();
        });

        $('#subject1').on("change", function () {
            //$(this).addClass('cur').siblings().removeClass('cur');
            auditDemand.getTeaBySubject();
            var gradeList = new Array();
            $(".gradeCheck").each(function () {
                if ($(this).is(":checked")) {
                    gradeList.push($(this).attr("ids"));
                }
            });
            if (gradeList.length == 0) {
                return;
            } else {
                auditDemand.getTeaRulesByTeaId();
            }
        });

        $('body').on("click", ".agree", function () {
            var ruleId = $(this).attr("ids");
            var eachId = $(this).attr("eachId");
            if($(this).attr("require") != 0){
                if($(this).attr("require") == 1){
                    layer.alert("该需求已同意，不允许修改");
                }else if($(this).attr("require") == 2){
                    layer.alert("该需求已拒绝，不允许修改");
                }
            }else{
                auditDemand.agreeRules(ruleId, eachId);
                auditDemand.getTeaRules();
            }
        });

        $('body').on("click", ".refuse", function () {
            var ruleId = $(this).attr("ids");
            var eachId = $(this).attr("eachId");
            if($(this).attr("require") != 0){
                if($(this).attr("require") == 1){
                    layer.alert("该需求已同意，不允许修改");
                }else if($(this).attr("require") == 2){
                    layer.alert("该需求已拒绝，不允许修改");
                }
            }else{
                auditDemand.refuseRules(ruleId, eachId);
                auditDemand.getTeaRules();
            }
        });

        $("body").on("click", ".gradeCheck", function () {
            auditDemand.getChushi();
            auditDemand.getTeaRulesByTeaId();
        });

        $('.bc2').click(function () {
            $('.m2').show();
            $('.m1').hide();
            $('.sel1').hide();
            auditDemand.getKBMSChushi();
            auditDemand.getGuDingShiWu();
            auditDemand.getTeaRules();
        })

        $('body').on("click",".kbms",function () {
            if($(this).hasClass("bacColor") || !$(this).hasClass("count")){
                $('.kbms').each(function () {
                    $(this).removeClass("bacColor");
                });
                $("#xyRules dd").each(function () {
                    $(this).html("");
                    $(this).removeClass("c27");
                    $(this).removeClass("cb5");
                    $(this).removeClass("c0a");
                    $(this).removeClass("cef");
                });
            }else{
                $('.kbms').each(function () {
                    $(this).removeClass("bacColor");
                });
                $(this).addClass("bacColor");
                var x = $(this).attr("x");
                var y = $(this).attr("y");
                auditDemand.getTeaRulesByXy(x,y);
            }

        });

        auditDemand.getDefaultTerm();
        auditDemand.getXueQi();
        auditDemand.getGrade();
        auditDemand.getSubjectList();
        auditDemand.getTea();
        auditDemand.getListBySchoolId();
        auditDemand.getChushi();
        auditDemand.getKBMSChushi();
        auditDemand.getTeaRules();
    }

    auditDemand.getGuDingShiWu = function () {
        var xqid = $("#term .cur").attr("ids");
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": xqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".kbms[x=" + (obj.y - 1) + "][y=" + (obj.x - 1) + "]").html(obj.desc)
                $(".kbms[x=" + (obj.y - 1) + "][y=" + (obj.x - 1) + "]").addClass("swColor");
            })
        })
    }

    auditDemand.getChushi = function () {
        var x = 0;
        var y = 0;
        $(".ttd1").each(function (s, ot) {
            $(this).removeClass("tii");
            $(this).removeClass("tii1");
            $(this).removeClass("tii2");
            $(this).removeClass("tii3");
            $(this).removeClass("tii4");
            $(this).removeClass("tii5");
            $(this).removeClass("tii6");
            $(this).removeClass("tii7");
            $(this).removeClass("c27");
            $(this).removeClass("cb5");
            $(this).removeClass("c0a");
            $(this).removeClass("cef");
            $(this).html("");
            $(this).attr("x", x);
            $(this).attr("y", y);
            x += 1;
            if (x == 7) {
                y += 1;
                x = 0;
            }
        })
    }

    auditDemand.getTeaRulesByTeaId = function () {
        var map = {}
        var gradeList = new Array();
        $(".gradeCheck").each(function () {
            if ($(this).is(":checked")) {
                gradeList.push($(this).attr("ids"));
            }
        });
        map.gradeList = gradeList;
        map.xqid = $("#term .cur").attr("ids");
        map.teaId = $("#tea1 .teaCur").attr("ids");
        Common.getPostBodyData('/teaRules/getTeaRulesByTeaId.do', map, function (rep) {
            if (rep.message.length == 0) {
                auditDemand.getChushi();
            }
            $.each(rep.message, function (i, st) {
                if (st.status == 1) {
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("必须排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("c0a");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids", st.id);
                    //$(".ttd1[x=" + st.x + "][y=" + st.y + "]").css("background", "#00a2e8","text-align","center");
                }
                if (st.status == 2) {
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("优先排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("cb5");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids", st.id);
                    // $(".ttd1[x=" + st.x + "][y=" + st.y + "]").css("background", "#b5e61d","text-align","center");
                }
                if (st.status == 3) {
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("不排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("c27");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids", st.id);
                    //$(".ttd1[x=" + st.x + "][y=" + st.y + "]").css("background", "#ff7f27","text-align","center");
                }
                if (st.status == 4) {
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("避免排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("cef");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids", st.id);
                    //$(".ttd1[x=" + st.x + "][y=" + st.y + "]").css("background", "#efe4b0","text-align","center");
                }
            })

        })
    }

    auditDemand.getListBySchoolId = function () {
        var xqid = $("#term .cur").attr("ids");
        if (xqid && xqid != null) {
            Common.getData('/courseset/getListBySchoolIdZKB.do', {"xqid": xqid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
                Common.render({tmpl: $('#KeShiList1'), data: rep, context: '#tbd1', overwrite: 1});
                Common.render({tmpl: $('#kbModel_temp'), data: rep, context: '#kbModel', overwrite: 1});
            });
        }
    }

    auditDemand.agreeRules = function (ruleId, eachId) {
        var par = {};
        par.ruleId = ruleId;
        par.eachId = eachId;
        Common.getData('/teaRules/agreeRules.do', par, function (rep) {
            if (rep.code == 200) {
                layer.msg("已同意");
            }
        })
    }

    auditDemand.refuseRules = function (ruleId, eachId) {
        var par = {};
        par.ruleId = ruleId;
        par.eachId = eachId;
        Common.getData('/teaRules/refuseRules.do', par, function (rep) {
            if (rep.code == 200) {
                layer.msg("已拒绝");
            }
        })
    }

    auditDemand.getTeaRulesByXy = function(x,y){
        var par = {};
        par.x = x;
        par.y = y;
        var teaList = new Array();
        $('.curr').each(function (i, st) {
            teaList.push($(st).attr("ids"))
        });
        par.teaList = teaList;
        par.xqid = $("#term .cur").attr("ids");
        Common.getPostBodyData('/teaRules/getTeaRulesByXy.do', par, function (rep) {
            Common.render({tmpl: $('#xyRules_temp'), data: rep.message, context: '#xyRules', overwrite: 1});
        });
    }

    auditDemand.getTeaRules = function () {
        var par = {};
        var teaList = new Array();
        $('.curr').each(function (i, st) {
            teaList.push($(st).attr("ids"))
        });
        par.teaList = teaList;
        par.xqid = $("#term .cur").attr("ids");
        par.status = $('#status').val();
        Common.getPostBodyData('/teaRules/getTeaRulesList.do', par, function (rep) {
            Common.render({tmpl: $('#teaRulesList_temp'), data: rep.message, context: '#teaRulesList', overwrite: 1});
            $('.kbms').each(function (o, st) {
                var count = 0;
                var status = -1;
                var tea = "";
                $.each(rep.message, function (i, ot) {
                    $.each(ot.teaRulesList, function (k, kt) {
                        if ($(st).attr("x") == kt.x && $(st).attr("y") == kt.y) {
                            status = kt.status;
                            tea = ot.teaName;
                            count++;
                        }
                    });
                });
                if (count > 1) {
                    $(st).html(count);
                    $(st).addClass("count");
                } else if (count == 1) {
                    if (status == 1) {
                        $(st).html("<em class='block'>" + tea + "</em>" + "<em class='block'>(必须排课)</em>");
                        $(st).addClass("c0a");
                        $(st).attr("ids", st.id);
                    }
                    if (status == 2) {
                        $(st).html("<em class='block'>" + tea + "</em>" + "<em class='block'>(优先排课)</em>");
                        $(st).addClass("cb5");
                        $(st).attr("ids", st.id);
                        // $(".ttd1[x=" + st.x + "][y=" + st.y + "]").css("background", "#b5e61d","text-align","center");
                    }
                    if (status == 3) {
                        $(st).html("<em class='block'>" + tea + "</em>" + "<em class='block'>(不排课)</em>");
                        $(st).addClass("c27");
                        $(st).attr("ids", st.id);
                        //$(".ttd1[x=" + st.x + "][y=" + st.y + "]").css("background", "#ff7f27","text-align","center");
                    }
                    if (status == 4) {
                        $(st).html("<em class='block'>" + tea + "</em>" + "<em class='block'>(避免排课)</em>");
                        $(st).addClass("cef");
                        $(st).attr("ids", st.id);
                        //$(".ttd1[x=" + st.x + "][y=" + st.y + "]").css("background", "#efe4b0","text-align","center");
                    }
                }
            });
        });
    }

    auditDemand.getTea = function () {
        var par = {};
        par.xqid = $("#term .cur").attr("ids");
        par.gid = $("#grade .cur").attr("ids");
        par.subId = $("#subject .cur").attr("ids");
        if ($("#teaName").val() == "") {
            par.name = "*";
        } else {
            par.name = $("#teaName").val();
        }
        Common.getData('/teaRules/getTea.do', par, function (rep) {
            Common.render({tmpl: $('#tea_temp'), data: rep.message, context: '#tea', overwrite: 1});
            $('#tea li:eq(0)').addClass("curr");
        })
    }

    auditDemand.getTeaBySubject = function () {
        var par = {};
        par.xqid = $("#term .cur").attr("ids");
        par.gid = $("#grade .cur").attr("ids");
        par.subId = $("#subject1").val();
        if ($("#teaName").val() == "") {
            par.name = "*";
        } else {
            par.name = $("#teaName").val();
        }
        Common.getData('/teaRules/getTea.do', par, function (rep) {
            Common.render({tmpl: $('#tea_temp1'), data: rep.message, context: '#tea1', overwrite: 1});
            $("#tea1 li:eq(0)").addClass("teaCur");
        })
    }

    auditDemand.getSubjectList = function () {
        var par = {};
        par.xqid = $("#term .cur").attr("ids");
        par.gid = $("#grade .cur").attr("ids");
        Common.getData('/teaRules/getSubjectByXQIDAndGid.do', par, function (rep) {
            Common.render({tmpl: $('#subject_temp'), data: rep.message, context: '#subject', overwrite: 1});
            Common.render({tmpl: $('#subject_temp1'), data: rep.message, context: '#subject1', overwrite: 1});
            $("#subject em:eq(0)").addClass("cur");
        })
    }

    auditDemand.getGrade = function () {
        var xqid = $("#term .cur").attr("ids");
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": xqid}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep, context: '#grade', overwrite: 1});
            Common.render({tmpl: $('#grade_temp1'), data: rep, context: '#grade1', overwrite: 1});
            $("#grade em:eq(0)").addClass("cur");
        })
    }

    /* auditDemand.getChushi = function () {
         var x = 0;
         var y = 0;
         $(".ttd1").each(function (s, ot) {

             $(this).html("");
             $(this).attr("x", x);
             $(this).attr("y", y);
             x += 1;
             if (x == 7) {
                 y += 1;
                 x = 0;
             }
         })
     }*/

    auditDemand.getKBMSChushi = function () {
        var x = 0;
        var y = 0;
        $(".kbms").each(function (s, ot) {
            $(this).removeClass("c27");
            $(this).removeClass("cb5");
            $(this).removeClass("c0a");
            $(this).removeClass("cef");
            $(this).removeClass("count");
            $(this).removeClass("swColor");
            $(this).html("");
            $(this).attr("x", x);
            $(this).attr("y", y);
            x += 1;
            if (x == 7) {
                y += 1;
                x = 0;
            }
        })
    }


    auditDemand.getXueQi = function () {
        Common.getData('/new33isolateMange/getTermList.do', {}, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term', overwrite: 1});
                $("#term em[ids=" + deXqid + "]").addClass("cur");
            }
        });
    }

    //获取默认学期和默认排课次
    auditDemand.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.paikexq;
            $("#defaultTerm").attr("ids", rep.message.xqid);
            par.xqid = deXqid;
        });
    }

    module.exports = auditDemand;
})