/**
 * Created by albin on 2018/7/31.
 */
define('MyTimetable', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var MyTimetable = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var deCid = "";
    var teaId = "";
    var ksCount = "";
    MyTimetable.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getGradeWeek.do', {"xqid":deXqid}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial + 1;
                var index2 = rep.serial + 2;
                $("[y="+index+"]").css("background", "#C0C0C0")
                $("[y="+index2+"]").css("background", "#C0C0C0")
            }
        });
    }
    MyTimetable.init = function () {
        $("body").on("click", ".ban", function () {
            if($(this).attr("ids")!=undefined) {
                window.location.href = "/newisolatepage/MyClass.do?jxbId=" + $(this).attr("ids");
            }
        })
        MyTimetable.getUserId();
        MyTimetable.getDefaultTerm();
        MyTimetable.getListBySchoolId();
        MyTimetable.getListByXq();
        MyTimetable.getChushi();
        MyTimetable.getTeaShiWuByXqid();
        MyTimetable.getGuDingShiWu();
        MyTimetable.getGDSX();
        MyTimetable.GetTeachersSettledPositions();
        MyTimetable.getCurrentJXZ();
        $("#week").change(function () {
            MyTimetable.getTeaShiWuByXqid();
            MyTimetable.getGuDingShiWu();
            MyTimetable.getGDSX();
            MyTimetable.GetTeachersSettledPositions();
            MyTimetable.getCurrentJXZ();
        })
        $("body").on("click", ".b9", function () {
            var par = {};
            par.teacherId = teaId;
            par.week = $("#week").val();
            par.userName = $("#unm").html();
            window.location.href = "/paike/exportTeaKB.do?teacherId=" + par.teacherId + "&week=" + par.week + "&userName=" + par.userName;
        });
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
    }
    MyTimetable.getUserId = function () {
        Common.getData('/new33isolateMange/getUserId.do', "", function (rep) {
            teaId = rep.message;
        });
    }

    MyTimetable.getGDSX = function () {
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
                $(".itt[x=" + x + "][y=" + y + "]").css("background", "#FFB6C1")
            })
        })
    }

    MyTimetable.GetTeachersSettledPositions = function () {
        var par = {};
        par.teacherId = teaId;
        par.week = $("#week").val();
        if (par.teacherId != "" && par.teacherId != undefined && par.teacherId != null) {
            Common.getData('/paike/GetTeachersSettledPositionsByWeek.do', par, function (rep) {
                ksCount = rep.message.length;
                MyTimetable.getPks();
                $.each(rep.message, function (i, obj) {
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").css("background", "#FFF")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("ban")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("ids", obj.jxbId)
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.JxbName + "(" + obj.count + ")" + "</br>" + obj.subName + "</br>" + obj.grade + "</br>" + obj.roomName)
                })
            })
        }
    }
    MyTimetable.getPks = function () {
        $("#keCount").html("-课时数:" + ksCount)
    }
    MyTimetable.getTeaShiWuByXqid = function () {
        $(".itt").css("color", "#000")
        $(".itt").css("background", "#FFF")
        $(".itt").html("")
        $(".itt").removeClass("ban")
        $(".itt").removeAttr("ids")
        var par = {}
        par.xqid = deXqid;
        par.teaId = teaId;
        Common.getData('/new33isolateMange/getTeaShiWuByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
            })
        })
    }
    MyTimetable.getGuDingShiWu = function () {
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": deXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
            })
        })
    }
    MyTimetable.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.xqid;
            deCid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }
    MyTimetable.getListBySchoolId = function () {
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdZKB.do', {"xqid": deXqid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }
    MyTimetable.getChushi = function () {
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
    MyTimetable.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        })
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            $("#week option[value=" + wk + "]").prop("selected", true)
        })
    }
    module.exports = MyTimetable;
})