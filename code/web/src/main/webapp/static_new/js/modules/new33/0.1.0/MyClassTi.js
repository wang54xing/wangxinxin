/**
 * Created by albin on 2018/7/31.
 */
define('MyClassTi', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var MyClassTi = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var deCid = "";
    var teaId = "";
    MyClassTi.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getGradeWeek.do', {"xqid":deXqid}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial + 1;
                var index2 = rep.serial + 2;
                $("[y="+index+"]").css("background", "#C0C0C0")
                $("[y="+index2+"]").css("background", "#C0C0C0")
            }
        });
    }
    MyClassTi.getGDSX = function () {
        var par = {};
        par.xqid = deXqid;
        par.userId = teaId;
        Common.getData('/gdsx/getGDSXBySidAndXqidTea.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var x=obj.y + 1;
                var y=obj.x + 1;
                $(".itt[x=" + x + "][y=" + y+ "]").removeClass("fdd")
                $(".itt[x=" + x + "][y=" + y + "]").removeClass("roomCls")
                $(".itt[x=" + x + "][y=" + y + "]").html(obj.desc)
                $(".itt[x=" + x + "][y=" + y + "]").addClass("fen")
            })
        })
    }
    MyClassTi.addOrUpdateQing = function () {
        var par = {};
        par.xqid = deXqid;
        par.week = $("#week").val();
        $(".greed").each(function (i, obj) {
            if ($(obj).hasClass("fdd")) {
                par.njxbId = $(obj).attr("ids");
                par.nx = $(obj).attr("y") - 1;
                par.ny = $(obj).attr("x") - 1;
            } else {
                par.jxbId = $(obj).attr("ids");
                par.x = $(obj).attr("y") - 1;
                par.y = $(obj).attr("x") - 1;
                par.sta = 1
                par.id = "*";
                //if ($(obj).attr("qid") != undefined && $(obj).attr("qid") != "") {
                //    par.id = $(obj).attr("qid");
                //}
            }
        })
        Common.getPostBodyData('/paike/addOrUpdateShengQing.do', par, function (rep) {
            layer.msg("操作成功!");
            MyClassTi.getFu();
            MyClassTi.getTeaShiWuByXqid();
            MyClassTi.getGuDingShiWu();
            MyClassTi.getGDSX();
            MyClassTi.GetTeachersSettledPositions();
            MyClassTi.getShengQingList();
            MyClassTi.getCurrentJXZ();
        });
    }

    MyClassTi.getShengQingListALL = function () {
        var par = {};
        par.xqid = deXqid;
        Common.getData('/paike/getShengQingListALL.do', par, function (rep) {
            Common.render({tmpl: $('#bdbd'), data: rep.message, context: '#tbdd', overwrite: 1});
        })
    }
    MyClassTi.getShengQingList = function () {
        var par = {};
        par.xqid = deXqid;
        par.week = $("#week").val();
        Common.getData('/paike/getShengQingList.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                if (obj.sta == 1) {
                    $(".itt[x=" + obj.ny + "][y=" + obj.nx + "]").attr("sta", obj.sta)
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("sta", obj.sta)
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("pid", obj.id)
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("shen")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").removeClass("ju")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").find(".juQ").remove();
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").append("<em class='shenQ'></em>")
                }
                if (obj.sta == 2) {
                    if ($(".itt[x=" + obj.ny + "][y=" + obj.nx + "]").attr("ids") != undefined) {
                        $(".itt[x=" + obj.ny + "][y=" + obj.nx + "]").attr("sta", obj.sta)
                        $(".itt[x=" + obj.ny + "][y=" + obj.nx + "]").attr("pid", obj.id)
                        $(".itt[x=" + obj.ny + "][y=" + obj.nx + "]").addClass("tiao")
                        $(".itt[x=" + obj.ny + "][y=" + obj.nx + "]").append("<em class='tiaoQ'></em>")
                    }
                }
                if (obj.sta == 3) {
                    if ($(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("sta") != 1 && $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("ids") != undefined) {
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("sta", obj.sta)
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("pid", obj.id)
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("ju")
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").append("<em class='juQ'></em>")
                    }
                }
            })
        })
    }
    MyClassTi.init = function () {
        $("body").on("click", ".cx", function () {
            var par = {};
            par.id = $(this).attr("ids");
            Common.getData('/paike/QuXiaoShengQing.do', par, function (rep) {
                MyClassTi.getFu();
                MyClassTi.getTeaShiWuByXqid();
                MyClassTi.getGuDingShiWu();
                MyClassTi.getGDSX();
                MyClassTi.GetTeachersSettledPositions();
                MyClassTi.getShengQingList();
                MyClassTi.getShengQingListALL();
                MyClassTi.getCurrentJXZ();
            })
        })
        $(".btn7").click(function () {
            $(".tpopup,.bg").show()
            MyClassTi.getShengQingListALL();
        })
        $(".fr").click(function () {
            $(".tpopup,.bg").hide()
        })
        $(".btn6").click(function () {
            if ($(".greed").length == 2) {
                MyClassTi.addOrUpdateQing();
            } else {
                layer.msg("请选择调课点。")
            }
        })
        MyClassTi.getUserId();
        MyClassTi.getDefaultTerm();
        MyClassTi.getListBySchoolId();
        MyClassTi.getListByXq();
        MyClassTi.getChushi();
        MyClassTi.getFu();
        MyClassTi.getTeaShiWuByXqid();
        MyClassTi.getGuDingShiWu();
        MyClassTi.getGDSX();
        MyClassTi.GetTeachersSettledPositions();
        MyClassTi.getCurrentJXZ();
        MyClassTi.getShengQingList();
        $("#week").change(function () {
            MyClassTi.getFu();
            MyClassTi.getTeaShiWuByXqid();
            MyClassTi.getGuDingShiWu();
            MyClassTi.getGDSX();
            MyClassTi.GetTeachersSettledPositions();
            MyClassTi.getShengQingList();
            MyClassTi.getCurrentJXZ();
        })
        $("body").on("click", ".fdd", function () {
            if ($(this).attr("sta") != 1) {
                if (!$(this).hasClass("greed") && !$(this).hasClass("hui")) {
                    if ($(".greed").length == 1) {
                        $(this).addClass("greed");
                        MyClassTi.jianCeChongTu();
                    }
                } else {
                    $(this).removeClass("greed");
                }
            } else {
                layer.msg("该点已经申请过调课")
            }
        })
        $("body").on("click", ".ban", function () {
            if ($(this).attr("sta") != 1) {
                if ($(this).hasClass("greed")) {
                    $(".roomCls").html("")
                    $(".roomCls").removeAttr("hui");
                    $(".roomCls").removeAttr("ids");
                    $(".roomCls").removeClass("greed")
                    $(".itt").removeClass("greed")
                    $(".itt").removeClass("roomCls");
                    $(".itt").removeClass("hui");
                } else {
                    $(".roomCls").html("")
                    $(".roomCls").removeAttr("hui");
                    $(".itt").removeClass("greed")
                    $(".roomCls").removeClass("greed")
                    $(".roomCls").removeAttr("ids");
                    $(this).addClass("greed");
                    MyClassTi.getRoomKeBiao();
                    MyClassTi.getTeaShiWuByXqid();
                    MyClassTi.getGuDingShiWu()
                    MyClassTi.getGDSX();
                    MyClassTi.getCurrentJXZ();
                }
            } else {
                layer.msg("你已经申请该教学班调课，请等待管理员审批！")
            }
        })

    }
    MyClassTi.jianCeChongTu = function () {
        var par = {};
        par.xqid = deXqid;
        par.week = $("#week").val();
        $(".greed").each(function (i, obj) {
                if($(obj).hasClass("ban")) {
                    par.jxbId1 = $(obj).attr("ids");
                    if (par.jxbId1 == undefined) {
                        par.jxbId1 = "";
                    }
                    par.X1 = $(obj).attr("x");
                    par.Y1 = $(obj).attr("y");
                }else {
                    par.jxbId2 = $(obj).attr("ids");
                    if (par.jxbId2 == undefined) {
                        par.jxbId2 = "";
                    }
                    par.X2 = $(obj).attr("x");
                    par.Y2 = $(obj).attr("y");
                }
        })
        Common.getData('/paike/getShiBuShiKeTiaoKe.do', par, function (rep) {
            if (rep.message == true) {
                layer.msg("可以进行调课申请!");
            } else {
                $(".roomCls").removeClass("greed")
                $(".fdd").removeClass("greed")
                layer.msg("所选教学班无法进行调课!");
            }
        })
    }
    MyClassTi.getRoomKeBiao = function () {
        $(".itt").removeClass("roomCls");
        var par = {};
        par.week = $("#week").val();
        $(".greed").each(function (i, obj) {
            if($(obj).hasClass("ban")) {
                par.jxbId1 = $(obj).attr("ids");
                if (par.jxbId1 == undefined) {
                    par.jxbId1 = "";
                }
                par.x = $(obj).attr("x");
                par.y = $(obj).attr("y");
            }
        })
        par.jxbId = $(".greed").attr("ids");
        Common.getData('/paike/getZkbByJxbId.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                if (obj.sta == 0) {
                    if (!$(".itt[x=" + obj.y + "][y=" + obj.x + "]").hasClass("ban")) {
                        if (!obj.flag) {
                            $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("hui");
                        }
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("roomCls");
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("ids", obj.jxbId)
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.jxbName + "</br>" + obj.gradeName + "</br>" + obj.roomName)
                    }
                } else {
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("sta", 1)
                }
            })

            $('.my').each(function(i) {
                $(this).addClass("hui");
            });
        });
    }
    MyClassTi.getUserId = function () {
        Common.getData('/new33isolateMange/getUserId.do', "", function (rep) {
            teaId = rep.message;
        });
    }
    MyClassTi.GetTeachersSettledPositions = function () {
        var par = {};
        par.teacherId = teaId;
        par.week = $("#week").val();
        if (par.teacherId != "" && par.teacherId != undefined && par.teacherId != null) {
            Common.getData('/paike/GetTeachersSettledPositionsByWeek.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").removeClass("fen")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").removeClass("fdd")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("ban")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("my")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("ids", obj.jxbId)
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.JxbName + "(" + obj.count + ")" + "</br>" + obj.subName + "</br>" + obj.grade + "</br>" + obj.roomName)
                })
            })
        }
    }
    MyClassTi.getFu=function(){
        $(".itt").addClass("bai")
        $(".itt").addClass("fdd")
        $(".itt").attr("sta", 0)
        $(".itt").css("color", "#000")
        $(".itt").html("")
        $(".itt").removeClass("greed")
        $(".itt").removeClass("fen")
        $(".itt").removeClass("roomCls");
        $(".itt").removeClass("ban")
        $(".itt").removeClass("shen")
        $(".itt").removeClass("tiao")
        $(".itt").removeClass("ju")
        $(".itt").removeAttr("ids")
    }
    MyClassTi.getTeaShiWuByXqid = function () {
        var par = {}
        par.xqid = deXqid;
        par.teaId = teaId;
        Common.getData('/new33isolateMange/getTeaShiWuByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").removeClass("roomCls")
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass("fen")
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").removeClass("fdd")
            })
        })
    }
    MyClassTi.getGuDingShiWu = function () {
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": deXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").removeClass("fdd")
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").removeClass("roomCls")
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass("fen")
            })
        })
    }
    MyClassTi.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.xqid;
            deCid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }
    MyClassTi.getListBySchoolId = function () {
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdZKB.do', {"xqid": deXqid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }
    MyClassTi.getChushi = function () {
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
    MyClassTi.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        })
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            $("#week option[value=" + wk + "]").prop("selected", true)
        })
    }
    module.exports = MyClassTi;
})