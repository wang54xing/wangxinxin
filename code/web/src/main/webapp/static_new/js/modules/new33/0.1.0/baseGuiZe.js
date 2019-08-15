/**
 * Created by albin on 2018/7/6.
 */
define('baseGuiZe', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var baseGuiZe = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    var ciId = "";
    var TeaList = new Array();
    Common = require('common');
    baseGuiZe.getDefaultCi = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            ciId = rep.message.paikeci;member-select
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term").val(rep.message.paikeci);
        });
    }
    baseGuiZe.getListBySchoolId = function () {
        if (ciId != null && ciId != undefined) {
            Common.getData('/courseset/getListBySchoolId.do', {"xqid": ciId}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#keShi', overwrite: 1});
            });
        }
    }
    baseGuiZe.getChushi = function () {
        var y = 0;
        var x = 0;
        $(".cursor").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            x += 1;
            if (x == 7) {
                y += 1;
                x = 0;
            }
        })
    }
    baseGuiZe.getTeaList = function () {
        var par = {};
        par.xqid = ciId;
        par.gradeId = $("#GradeTp").val();
        var subId = new Array();
        $("#subList .bg5").each(function () {
            subId.push($(this).attr("ids"));
        })
        if (subId.length > 0) {
            par.subList = subId;
        }
        Common.getPostBodyData('/new33isolateMange/getTeaListSub.do', par, function (rep) {
            $("#myList-nav").empty();
            Common.render({tmpl: $('#TeaList_temp'), data: rep.message, context: '#myList', overwrite: 1});
            $('#myList').listnav({
                includeOther: true,
                noMatchText: '',
                prefixes: ['the', 'a']
            })
        })
    }
    baseGuiZe.getGradeList = function () {
        var par = {};
        par.xqid = ciId;
        Common.getData('/new33isolateMange/getGradList.do', par, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.grade', overwrite: 1});
        })
    }
    baseGuiZe.getSubjectByType = function () {
        var par = {};
        par.xqid = ciId;
        par.gradeId = $("#GradeTp").val();
        par.subType = $("#Subtype").val();
        Common.getData('/n33_setJXB/getSubjectByTypeExitZX.do', par, function (rep) {
            Common.render({tmpl: $('#subList_temp'), data: rep.message, context: '#subList', overwrite: 1});
        })
    }


    baseGuiZe.addOrUpdateGuiZe = function () {
        var par = {};
        par.id = $("#addOrUpdateGuiZe").attr("ids");
        par.xqid = ciId;
        par.type = $("#Subtype").val();
        par.gid = $("#GradeTp").val();
        par.status = $(".baseBtn .bg5").attr("ids");
        var subList = new Array();
        $("#subList .bg5").each(function (i, obj) {
            subList.push($(obj).attr("ids"));
        })
        par.sub = subList;
        par.tea = TeaList;
        par.lev = 1;
        if (subList.length > 0) {
            par.lev = 2;
        }
        if (TeaList.length > 0) {
            par.lev = 3;
        }
        var xyList = new Array();
        $(".rrgt").each(function (i, obj) {
            var PaiKeXyDto = {};
            PaiKeXyDto.x = $(obj).parent().attr("x");
            PaiKeXyDto.y = $(obj).parent().attr("y");
            xyList.push(PaiKeXyDto);
        })
        par.xyList = xyList;
        Common.getPostBodyData('/paike/addOrUpdateGuiZe.do', par, function (rep) {
            layer.msg("操作成功!");
            $(".gz-popup,.bg").hide();
            baseGuiZe.getGuiZeList();
        })
    }
    baseGuiZe.getGuiZeList = function () {
        var par = {};
        par.gid = $("#GradeTp").val();
        par.type = $("#Tp").val();
        par.ciId = ciId;
        Common.getData('/paike/getGuiZeList.do', par, function (rep) {
            if(rep.message.length == 0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            Common.render({tmpl: $('#tpL'), data: rep.message, context: '#ListType', overwrite: 1});
        })
    }

    baseGuiZe.init = function () {
        $("body").on("click", ".optt", function () {
            var par = {};
            par.id = $(this).attr("ids");
            TeaList=new Array();
            Common.getData('/paike/getGuiZe.do', par, function (rep) {
                $("#gradeName").html($("#GradeTp option:selected").html())
                $(".gz-popup,.bg").show();
                $("#addOrUpdateGuiZe").attr("ids", rep.message.id);
                //$("#grade option[value=" + rep.message.gid + "]").prop("selected", true);
                $("#Subtype option[value=" + rep.message.type + "]").prop("selected", true);
                baseGuiZe.getSubjectByType();
                $("#teaListCount").val(rep.message.teaStr)
                TeaList = rep.message.tea;
                $.each(rep.message.sub, function (i, obj) {
                    $("#subList .btn1[ids=" + obj + "]").addClass("bg5")
                })
                $(".btn2").removeClass("bg5");
                $(".baseBtn .btn2[ids=" + rep.message.status + "]").addClass("bg5");
                $(".inline").removeClass("rrgt")
                $.each(rep.message.xyList, function (i, obj) {
                    $(".cursor[x=" + obj.x + "][y=" + obj.y + "]").find(".inline").addClass("rrgt")
                })
                //$(".inline").removeClass("rrgt");
                //$("#subList .bg5").removeClass("bg5")
                //$("#teaListCount").val("")
            })
        })

        $("body").on("click",".allow",function () {
            window.location.href = "/newisolatepage/auditDemand.do";
        });

        $("body").on("click", ".opde", function () {
            var par = {};
            par.id = $(this).attr("ids");
            Common.getData('/paike/delGuiZe.do', par, function (rep) {
                baseGuiZe.getGuiZeList();
            })
        })
        $(".sel_all").change(function(){
            if($('.sel_all').is(':checked')){
                $("#myList li").find('i').show();
                $("#myList li").addClass("licur-t5")
            }else{
                $("#myList li").find('i').hide();
                $("#myList li").removeClass("licur-t5")
            }
            $('.ln-no-match').remove();
        })
        $("#Subtype,#grade").change(function () {
            baseGuiZe.getSubjectByType();
            TeaList = new Array();
        })
        $("#GradeTp").change(function () {
            baseGuiZe.getGuiZeList();
        })
        $("#Tp").change(function () {
            baseGuiZe.getGuiZeList();
        })
        $(".newGz").click(function () {
            $(".gz-popup,.bg").show();
            $("#addOrUpdateGuiZe").attr("ids", "*");
            TeaList = new Array();
            $(".inline").removeClass("rrgt");
            $("#subList .bg5").removeClass("bg5")
            $("#teaListCount").val("")
            $("#gradeName").html($("#GradeTp option:selected").html())
        })
        $("#addOrUpdateGuiZe").click(function () {
            baseGuiZe.addOrUpdateGuiZe();
        })
        $("#teaAdd").click(function () {
            TeaList=new Array();
            var teaStr = ""
            $(".licur-t5").each(function (i, obj) {
                TeaList.push($(obj).attr("ids"));
                if (i < 5) {
                    if (i == 0) {
                        teaStr += $(obj).find("span").html();
                    } else {
                        teaStr += "," + $(obj).find("span").html()
                    }
                }
            })
            $("#teaListCount").val(teaStr + ",等" + $(".licur-t5").length + "人");
            $(".adt-popup,.bg").hide();
            $(".gz-popup,.bg").show();
        })
        $(".xzBtn").click(function () {
            $(".gz-popup,.bg").hide();
            $(".adt-popup,.bg").show();
            baseGuiZe.getTeaList();
            baseGuiZe.chuShiTea();
        })
        $("body").on("click", ".pTabfl td", function () {
            $(this).find('em').toggleClass('rrgt');
        })
        $("body").on("click", ".myList li", function () {
            $(this).find('i').toggle('t5');
            $(this).toggleClass("licur-t5")
        })
        $("body").on("click", ".mx button", function () {
            $(this).toggleClass('bg5');
        })
        $("body").on("click", ".baseBtn button", function () {
            $(this).addClass('bg5').siblings().removeClass('bg5');
        })
        baseGuiZe.getDefaultCi();
        baseGuiZe.getListBySchoolId();
        baseGuiZe.getChushi();
        baseGuiZe.getGradeList();
        baseGuiZe.getSubjectByType();
        baseGuiZe.getGuiZeList();

    }
    baseGuiZe.chuShiTea = function () {
        $.each(TeaList, function (i, obj) {
            $(".li-special[ids=" + obj + "]").find('i').toggle('t5');
            $(".li-special[ids=" + obj + "]").toggleClass("licur-t5")
        })
    }
    module.exports = baseGuiZe;
})