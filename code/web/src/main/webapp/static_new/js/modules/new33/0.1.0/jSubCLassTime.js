/**
 * Created by albin on 2018/3/24.
 */
define('jSubCLassTime', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var jSubCLassTime = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var deCid = "";


    var ciId = null;

    var desc = "";
    jSubCLassTime.getDesc = function () {
        var par = {};
        par.ciId = ciId;
        par.gradeId = $(".xueke .active").attr("ids");
        par.type = 3;
        par.kbType = 4;
        if(ciId != null){
            Common.getData('/n33_set/getDESCForNoCourse.do', par, function (rep) {
                desc = rep.message;
            });
        }
    }

    jSubCLassTime.initDesc = function(){
        $('.itt').each(function (i,st) {
            if(($(this).text() == null || $(this).text().trim() == "") && !$(this).hasClass("grayhui")){
                $(this).text(desc);
            }
        });
    }

    jSubCLassTime.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getGradeWeek.do', {"xqid":deXqid}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial + 1;
                var index2 = rep.serial + 2;
                $("[y="+index+"]").addClass("gray")
                $("[y="+index2+"]").addClass("gray")
            }
        });
        jSubCLassTime.initDesc();
    }
    jSubCLassTime.getGDSX = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $(".xueke .active").attr("ids");
        Common.getData('/gdsx/getGDSXByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var x=obj.y + 1;
                var y=obj.x + 1;
                $(".itt[x=" + x + "][y=" + y + "]").html(obj.desc);
                //$(".itt[x=" + x + "][y=" + y + "]").css("background", "#FFB6C1")
                $(".itt[x=" + x + "][y=" + y + "]").addClass("red")
            })
        })
    }
    jSubCLassTime.getJXbList = function () {
        var par = {};
        par.classId = $("#Cls .t-li").attr("ids");
        par.xqid = deXqid;
        par.gradeId = $(".xueke .active").attr("ids");
        par.week = $("#week").val() + "";
        par.x = $("#popup-topName").attr("x") + "";
        par.y = $("#popup-topName").attr("y") + "";
        Common.getData('/paike/GetClassSettledPositionsWeekJxb.do', par, function (rep) {
            Common.render({tmpl: $('#jxbListFo_temp'), data: rep.message, context: '#jxbListFo'});
        })
    }
    jSubCLassTime.getStuList = function () {
        var par = {};
        par.classId = $("#Cls .t-li").attr("ids");
        par.xqid = deXqid;
        par.gradeId = $(".xueke .active").attr("ids");
        par.week = $("#week").val() + "";
        par.x = $("#popup-topName").attr("x") + "";
        par.y = $("#popup-topName").attr("y") + "";
        var jxbIds = new Array();
        $("#jxbListFo .spl.spCur").each(function (i, obj) {
            jxbIds.push($(obj).attr("ids"));
        })
        par.jxbIds =jxbIds;
        Common.getPostBodyData('/paike/GetClassSettledPositionsByWeekXyInfo.do', par, function (rep) {
            Common.render({tmpl: $('#stuBody_temp'), data: rep.message, context: '#stuBody', overwrite: 1});
        })
    }
    jSubCLassTime.getZhuanXiangZuHeXianQing=function(){
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $(".xueke .active").attr("ids");
        $("#zhuanList").empty();
        if(par.gradeId!=""&&par.gradeId!=undefined) {
            Common.getData('/paike/getZhuanXiangZuHeXianQingAll.do', par, function (rep) {
                Common.render({tmpl: $('#Zhuan'), data: rep.message, context: '#zhuanList', overwrite: 1});
            })
        }
    }
    jSubCLassTime.GetTeachersSettledPositions = function () {
        jSubCLassTime.getListBySchoolId();
        jSubCLassTime.getChushi();
        jSubCLassTime.getGDSX();
        jSubCLassTime.getGuDingShiWu();
        $(".itt").each(function (i,st) {
            if(!$(this).hasClass("red")){
                $(this).html("");
            }
        })
        var par = {};
        par.classId = $("#Cls .t-li").attr("ids");
        par.xqid = deXqid;
        par.gradeId = $(".xueke .active").attr("ids");
        par.week = $("#week").val();
        $(".itt").attr("title","")
        if(par.classId!=""&&par.classId!=undefined) {
            Common.getData('/paike/GetClassSettledPositionsByWeek.do', par, function (rep) {
                $("#ban").html($("#Cls .t-li").find("span").html());
//                $(".itt").css("color", "#000");
                $(".itt").removeClass("colorGreen");
                $.each(rep.message, function (i, obj) {
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").css("background", "#FFF")
                    ciId = obj.cid;
                    if (obj.type == 1) {
                        if(obj.teaName != undefined){
                            $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html("<p>" + obj.jList + "(" + obj.count + "人)</p><p>" + obj.teaName + "</p>")
                        }else{
                            $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html("<p>" + obj.jList + "</p><p>" + "</p>")
                        }
                        //$(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.jList + "(" + obj.count + "人)")
                    } else {
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html("走班")
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("title", obj.jList)
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").css("color", "green")
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("colorGreen")
                    }
                })
                if ($('#xq').is(":checked")) {
                    $(".colorGreen").each(function (i, obj) {
                        var title = $(obj).attr("title");
                        $(obj).html(title)
                    })
                } else {
                    $(".colorGreen").each(function (i, obj) {
                        $(obj).html("走班")
                    })
                }
            })
        }
        jSubCLassTime.getDesc();
    }
    jSubCLassTime.init = function () {
        $(".dcBtn").click(function(){
            var par = {};
            par.classId = $("#Cls .t-li").attr("ids");
            par.xqid = deXqid;
            par.name = $("#popup-topName").html();
            par.gradeId = $(".xueke .active").attr("ids");
            par.week = $("#week").val() + "";
            par.x = $("#popup-topName").attr("x") + "";
            par.y = $("#popup-topName").attr("y") + "";
            window.location.href = "/paike/exportJXBinfo.do?classId=" + par.classId + "&xqid=" + par.xqid + "&gradeId=" + par.gradeId + "&week=" + par.week + "&x=" + par.x + "&y=" + par.y + "&name=" + par.name;

        })
        $("#xq").click(function () {
            if ($(this).is(":checked")) {
                $(".colorGreen").each(function (i, obj) {
                    var title = $(obj).attr("title");
                    $(obj).html(title)
                })
            } else {
                $(".colorGreen").each(function (i, obj) {
                    $(obj).html("走班")
                })
            }
        });
        $('body').on('click', '.spl', function () {
            $(this).toggleClass('spCur');
            $('.alSp').removeClass('spCur')
            jSubCLassTime.getStuList()
        })
        $('body').on('click', '.alSp', function () {
            $(this).addClass('spCur').siblings().removeClass('spCur')
            jSubCLassTime.getStuList()
        })
        $('body').on('click', '.colorGreen', function () {
            $('.bg').show();
            $('.jsupopup').show();
            var x = $(this).attr("x");
            var y = $(this).attr("y");
            $("#popup-topName").html($(".t-li").find("span").html() + "周" + y + "第" + x + "节课")
            $("#popup-topName").attr("x", x)
            $("#popup-topName").attr("y", y)
            $("#jxbListFo").empty()
            $("#jxbListFo").append("<span class='spCur alSp'>全部</span>");
            jSubCLassTime.getJXbList();
            jSubCLassTime.getStuList();
        })
        $('.close,.qx').click(function () {
            $('.bg').hide();
            $('.jsupopup').hide();
        })
        $('body').on('click', '#printGDG', function () {
            $('#printGDG').hide();
            var headstr = "<html><head><title></title></head><body>";
            var footstr = "</body>";
            var newstr = $('#GDGContent').html();
            var oldstr = document.body.innerHTML;
            document.body.innerHTML = headstr + newstr + footstr;
            window.print();
            console.log('老页面：' + oldstr);
            console.log('新页面：' + newstr);
            document.body.innerHTML = oldstr;
            $('#printGDG').show()
            return false;
        })
        jSubCLassTime.getDefaultTerm();
        $("#week").change(function () {
            jSubCLassTime.getGuDingShiWu()
            jSubCLassTime.getGDSX();
            jSubCLassTime.GetTeachersSettledPositions();
            jSubCLassTime.getZhuanXiangZuHeXianQing();
            jSubCLassTime.getCurrentJXZ();
            // $("#xq").prop("checked",false)
        })
        $("body").on("click", ".xueke label", function () {
            $(".xueke label").removeClass("active");
            $(this).addClass("active");
            jSubCLassTime.getClassList();
            jSubCLassTime.getGuDingShiWu()
            jSubCLassTime.getGDSX();
            jSubCLassTime.GetTeachersSettledPositions();
            jSubCLassTime.getZhuanXiangZuHeXianQing();
            jSubCLassTime.getCurrentJXZ();
            // $("#xq").prop("checked",false)
        })

        $("body").on("click", ".b9", function () {
            var par = {};
            par.classId = $("#Cls .t-li").attr("ids");
            par.xqid = deXqid;
            par.gradeId = $(".xueke .active").attr("ids");
            par.week = $("#week").val();
            par.className = $(".t-li").find("span").text();
            par.isChecked = $('#xq').is(":checked");
            var flag = isCanExport();
            if (!flag) {
                layer.alert("未发布该周课表，不允许导出课表");
                return;
            }
            window.location.href = "/paike/exportXZBKB.do?classId=" + par.classId + "&xqid=" + par.xqid + "&gradeId=" + par.gradeId + "&week=" + par.week + "&className=" + par.className + "&isChecked=" + par.isChecked;

        });

        $("body").on("click", ".b10", function () {
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $(".xueke .active").attr("ids");
            par.week = $("#week").val();
            par.isChecked = $('#xq').is(":checked");
            var flag = isCanExport();
            if (!flag) {
                layer.alert("未发布该周课表，不允许导出课表");
                return;
            }
            var gradeName = $('.xueke label[class="active"'+']').attr('gnm') + $('.xueke label[class="active"'+']').attr('gJie');
            window.location.href = "/paike/exportZKBDataByClass.do?xqid=" + par.xqid + "&gradeId=" + par.gradeId + "&week=" + par.week + "&isChecked=" + par.isChecked + '&gradeName=' + gradeName;

        });

        jSubCLassTime.getGrade();
        jSubCLassTime.getListByXq();
        jSubCLassTime.getListBySchoolId();
        $("body").on("click", "#Cls li", function () {
            $("#Cls li").removeClass("t-li");
            $(this).addClass("t-li");
            $(".itt").html("")
            jSubCLassTime.getGuDingShiWu()
            jSubCLassTime.getGDSX();
            jSubCLassTime.GetTeachersSettledPositions();
            jSubCLassTime.getZhuanXiangZuHeXianQing();
            jSubCLassTime.getCurrentJXZ();
            // $("#xq").prop("checked",false)
        })
        jSubCLassTime.getChushi();
        jSubCLassTime.getGuDingShiWu();
        jSubCLassTime.getGDSX();
        jSubCLassTime.GetTeachersSettledPositions();
        jSubCLassTime.getZhuanXiangZuHeXianQing();
        jSubCLassTime.getCurrentJXZ();
    }

    function isCanExport() {
        var flag = null;
        var par = {};
        par.week = $("#week").val();
        par.gradeId = $(".xueke .active").attr("ids");
        par.xqid = deXqid;
        Common.getData('/paike/isCanExport.do', par, function (rep) {
            flag = rep.message;
        });
        return flag;
    }


    jSubCLassTime.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.xqid;
            deCid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }

    jSubCLassTime.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        })
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            $("#week option[value=" + wk + "]").prop("selected", true)
        })
    }
    jSubCLassTime.getChushi = function () {
        $(".itt").each(function (i,st) {
            $(this).removeClass("red");
        })
        var y = 1;
        var x = 1;
        $(".itt").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            y += 1;
            if (y == 8) {
                x += 1;
                y = 1;
            }
        })
    }

    jSubCLassTime.getGuDingShiWu = function () {
        //$(".itt").css("background", "#FFF")
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": deXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                //$(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass("red")
            })
        })
    }

    jSubCLassTime.getListBySchoolId = function () {
        var par = {};
        par.xqid = deXqid;
        par.week = $("#week").val();
        par.gradeId = $(".xueke .active").attr("ids");
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdForKB.do', par, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }

    jSubCLassTime.getGrade = function () {
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": deXqid}, function (rep) {
            $(".xueke").append(" <span>年级: </span>")
            Common.render({tmpl: $('#grade_temp'), data: rep, context: '.xueke'});
            $(".xueke label:eq(0)").addClass("active");
            jSubCLassTime.getClassList();
        })
    }

    jSubCLassTime.getClassList = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $(".xueke .active").attr("ids");
        Common.getData('/new33classManage/getClassListZKB.do', par, function (rep) {
            $(".clsSize").html(rep.message.length)
            Common.render({tmpl: $('#class_temp'), data: rep.message, context: '#Cls', overwrite: 1});
            $("#Cls li:eq(0)").addClass("t-li")
        })
    }

    module.exports = jSubCLassTime;
})