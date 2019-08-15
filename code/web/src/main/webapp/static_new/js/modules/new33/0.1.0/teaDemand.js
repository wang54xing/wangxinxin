/**
 * Created by albin on 2018/3/30.
 */
define('teaDemand', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var teaDemand = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var par = {};

    var teaId = "";

    var startTime = "";
    var endTime = "";

    teaDemand.init = function () {
        $("body").on("click",".ttc",function () {
            if(!$(this).hasClass("tii") && !$(this).hasClass("tii1") && !$(this).hasClass("tii2") && !$(this).hasClass("tii3")
                && !$(this).hasClass("tii4") && !$(this).hasClass("tii5") && !$(this).hasClass("tii6") && !$(this).hasClass("tii7")
                && !$(this).hasClass("tii8") && !$(this).hasClass("tii9") && !$(this).hasClass("tii10") && !$(this).hasClass("tii11") && !$(this).hasClass("gray") && !$(this).hasClass("swColor")){
                var obj = $(this);
                var flag = false;

                $(".require").each(function (i,st) {
                    if($(this).hasClass("cb5") || $(this).hasClass("c0a") || $(this).hasClass("c27") || $(this).hasClass("cef")){
                        flag = true;
                    }
                });

                var flag2 = true;
                if($("#term").val() != deXqid){
                    layer.alert("只允许提交排课学期的需求");
                    flag2 = false;
                }

                if(flag && flag2){
                    var timestamp=new Date().getTime();
                    if(timestamp > endTime || timestamp < startTime){
                        layer.alert("不在需求申请时间范围内");
                    }else{
                        $('.bg').show();
                        $('.yy-popup').show();
                        $('#qddesc').attr("x",obj.attr("x"));
                        $('#qddesc').attr("y",obj.attr("y"));
                    }
                }else{
                    if(!flag && flag2){
                        layer.alert("请选择需求类型");
                    }
                }
            }
        });

        $('body').on("click","#qddesc",function () {
            var array = new Array();
            var par = {};
            par.xqid = deXqid;
            var map = {};
            map.x = $(this).attr("x");
            map.y = $(this).attr("y");
            map.desc = $("#desc").val();
            if($("#desc").val() != null && $("#desc").val() != ""){
                map.require = "0";
                map.status = $(".select").attr("type");
                var gradeList = new Array();
                $(".gradeCheck").each(function () {
                    if($(this).is(":checked")){
                        gradeList.push($(this).attr("ids"));
                    }
                });
                map.gid = gradeList;
                if(gradeList.length == 0){
                    layer.alert("请选择年级")
                }else{
                    array.push(map);
                    par.teaRulesList = array;
                    Common.getPostBodyData('/teaRules/addTeaRulesByUserId.do', par, function (rep) {
                        if(rep.code == 200){
                            layer.msg("保存成功")
                            $('.bg').hide();
                            $('.yy-popup').hide();
                            teaDemand.getChushi();
                            teaDemand.getTeaRules();
                            teaDemand.getGuDingShiWu();
                            teaDemand.getTeaShiWuByXqid();
                            teaDemand.getGDSX();
                        }else{
                            layer.msg("服务器正忙");
                        }

                    });
                }
            }else{
                layer.alert("请输入需求原因");
            }
        });

        $("body").on("click",".ttd1",function () {
            if($(this).hasClass("tii") || $(this).hasClass("tii2") || $(this).hasClass("tii4") || $(this).hasClass("tii6")){
                layer.alert("该需求已审批，不能删除");
            }else if($(this).hasClass("tii1") || $(this).hasClass("tii3") || $(this).hasClass("tii5") || $(this).hasClass("tii7")){
                layer.alert("该需求已拒绝，不能删除");
            }else if($(this).hasClass("tii8") || $(this).hasClass("tii9") || $(this).hasClass("tii10") || $(this).hasClass("tii11")){
                var map = {};
                map.id = $(this).attr("ids");
                map.xqid = $("#term").val();
                if(map.xqid == deXqid){
                    var gradeList = new Array();
                    $(".gradeCheck").each(function () {
                        if($(this).is(":checked")){
                            gradeList.push($(this).attr("ids"));
                        }
                    });
                    map.gradeList = gradeList;
                    Common.getPostBodyData('/teaRules/deleteTeaRulesByUserId.do', map, function (rep) {
                        teaDemand.getChushi();
                        teaDemand.getTeaRules();
                        teaDemand.getGuDingShiWu();
                        teaDemand.getTeaShiWuByXqid();
                        teaDemand.getGDSX();
                    });
                }else{
                    layer.alert("只能删除排课学期需求")
                }
            }
        });

        /*$("body").on("click","#qddesc",function () {
            var map = {};
            map.x = $(this).attr("x");
            map.y = $(this).attr("y");
            map.desc = $("#desc").val();
            map.require = "0";
            map.status = $(".select").attr("type");
            var gradeList = new Array();
            $(".gradeCheck").each(function () {
                if($(this).is(":checked")){
                    gradeList.push($(this).attr("ids"));
                }
            });
            map.gid = gradeList;
            array.push(map);
            par.teaRulesList = array;
            if(map.status == 1){
                $(".ttd1[x=" + map.x + "][y=" + map.y + "]").html("必须排课");
                $(".ttd1[x=" + map.x + "][y=" + map.y + "]").css("background", "#00a2e8");
            }
            if(map.status == 2){
                $(".ttd1[x=" + map.x + "][y=" + map.y + "]").html("优先排课");
                $(".ttd1[x=" + map.x + "][y=" + map.y + "]").css("background", "#b5e61d");
            }
            if(map.status == 3){
                $(".ttd1[x=" + map.x + "][y=" + map.y + "]").html("不排课");
                $(".ttd1[x=" + map.x + "][y=" + map.y + "]").css("background", "#ff7f27");
            }
            if(map.status == 4){
                $(".ttd1[x=" + map.x + "][y=" + map.y + "]").html("避免排课");
                $(".ttd1[x=" + map.x + "][y=" + map.y + "]").css("background", "#efe4b0");
            }
            $('.bg').hide();
            $('.yy-popup').hide();
        });*/

        /*$("body").on("click","#save",function () {
            if($('.text1').text().indexOf('编辑')!=-1){
                Common.getPostBodyData('/teaRules/addTeaRules.do',par,function (rep) {
                    if(rep.code == 200){
                        layer.msg(rep.message);
                    }
                })
            }
        });*/

        $("#term").change(function () {
            teaDemand.getGrade();
            teaDemand.getChushi();
            teaDemand.getTeaRules();
            teaDemand.getGuDingShiWu();
            teaDemand.getTeaShiWuByXqid();
            teaDemand.getGDSX();
        });

        $("body").on("click",".gradeCheck",function () {
            teaDemand.getChushi();
            teaDemand.getTeaRules();
            teaDemand.getGuDingShiWu();
            teaDemand.getTeaShiWuByXqid();
            teaDemand.getGDSX();
        });
        
        $('body').on("click","#syn",function () {
            teaDemand.syncLastTermTeaRules();
        });

        teaDemand.getDefaultTerm();
        teaDemand.getXueQi();
        teaDemand.getListBySchoolId();
        teaDemand.getChushi();
        teaDemand.getGrade();
        teaDemand.getUserId();
        teaDemand.getTeaShiWuByXqid();
        teaDemand.getGuDingShiWu();
        teaDemand.getGDSX();
        teaDemand.getRequireTime();
    }

    teaDemand.getRequireTime = function () {
        Common.getData('/teaRules/getRequireTime.do', {"xqid":deXqid}, function (rep) {
            startTime = rep.message.startLong;
            endTime = rep.message.endLong;
        });
    }

    teaDemand.getGuDingShiWu = function () {
        var xqid = $('#term').val();
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": xqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".ttd1[x=" + (obj.y - 1) + "][y=" + (obj.x - 1) + "]").html(obj.desc)
                $(".ttd1[x=" + (obj.y - 1) + "][y=" + (obj.x - 1) + "]").addClass("swColor");
            })
        })
    }

    teaDemand.getGDSX = function () {
        var par = {};
        par.xqid = $('#term').val();
        par.userId = teaId;
        Common.getData('/gdsx/getGDSXBySidAndXqidTea.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var x=obj.x;
                var y=obj.y;
                $(".ttd1[x=" + x + "][y=" + y + "]").html(obj.desc);
                $(".ttd1[x=" + x + "][y=" + y + "]").addClass("gray");
            });
        });
    }

    teaDemand.getUserId = function () {
        Common.getData('/new33isolateMange/getUserId.do', "", function (rep) {
            teaId = rep.message;
        });
    }

    teaDemand.syncLastTermTeaRules = function(){
        var xqid = $("#term").val();
        if(xqid && xqid != null){
            Common.getData('/teaRules/syncLastTermTeaRules.do', {"xqid":xqid}, function (rep) {
                teaDemand.getChushi();
                teaDemand.getTeaRules();
                teaDemand.getGuDingShiWu();
                teaDemand.getTeaShiWuByXqid();
                teaDemand.getGDSX();
            });
        }
    }

    teaDemand.getTeaRules = function () {
        var map = {}
        var gradeList = new Array();
        $(".gradeCheck").each(function () {
            if($(this).is(":checked")){
                gradeList.push($(this).attr("ids"));
            }
        });
        map.gradeList = gradeList;
        map.xqid = $("#term").val();
        Common.getPostBodyData('/teaRules/getTeaRules.do',map,function (rep) {
            if(rep.message.length == 0){
                teaDemand.getChushi();
                teaDemand.getGuDingShiWu();
                teaDemand.getTeaShiWuByXqid();
                teaDemand.getGDSX();
            }
            $.each(rep.message,function (i,st) {
                if(st.status == 1 && st.require == 1){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("必须排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }else if(st.status == 1 && st.require == 2){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("必须排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii1");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }else if(st.status == 1 && st.require == 0){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("必须排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii8");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }
                if(st.status == 2 && st.require == 1){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("优先排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii2");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }else if(st.status == 2 && st.require == 2){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("优先排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii3");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }else if(st.status == 2 && st.require == 0){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("优先排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii9");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }
                if(st.status == 3 && st.require == 1){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("不排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii4");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }else if(st.status == 3 && st.require == 2){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("不排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii5");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }else if(st.status == 3 && st.require == 0){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("不排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii10");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }
                if(st.status == 4 && st.require == 1){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("避免排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii6");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }else if(st.status == 4 && st.require == 2){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("避免排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii7");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }else if(st.status == 4 && st.require == 0){
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").html("避免排课");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").addClass("tii11");
                    $(".ttd1[x=" + st.x + "][y=" + st.y + "]").attr("ids",st.id);
                }
            })

        })
    }

    teaDemand.getGrade = function () {
        var xqid = $('#term').val();
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": xqid}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep, context: '#grade', overwrite: 1});
        })
    }

    teaDemand.getTeaShiWuByXqid = function () {
        var par = {}
        par.xqid = $('#term').val();
        par.teaId = teaId;
        Common.getData('/new33isolateMange/getTeaShiWuByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".ttd1[x=" + (obj.y - 1) + "][y=" + (obj.x - 1) + "]").html(obj.desc);
                $(".ttd1[x=" + (obj.y - 1) + "][y=" + (obj.x - 1) + "]").addClass("swColor");
            })
        })
    }

    teaDemand.getChushi = function () {
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
            $(this).removeClass("tii8");
            $(this).removeClass("tii9");
            $(this).removeClass("tii10");
            $(this).removeClass("tii11");
            $(this).removeClass("gray");
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

    teaDemand.getXueQi = function () {
        Common.getData('/new33isolateMange/getTermList.do', {}, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term', overwrite: 1});
                $("#term option[value=" + deXqid + "]").prop("selected", true)
            }
        });
    }

    //获取默认学期和默认排课次
    teaDemand.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.paikexq;
            $("#defaultTerm").attr("ids", rep.message.xqid);
            par.xqid = deXqid;
        });
    }

    teaDemand.getListBySchoolId = function () {
        var xqid = $('#term').val();
        if (xqid && xqid != null) {
            Common.getData('/courseset/getListBySchoolIdZKB.do', {"xqid": xqid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }
    module.exports = teaDemand;
})