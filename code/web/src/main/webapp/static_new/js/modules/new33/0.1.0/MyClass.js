/**
 * Created by albin on 2018/7/31.
 */
define('MyClass', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var MyClass = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var deCid = "";
    var gid = ""
    MyClass.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getGradeWeek.do', {"xqid":deXqid}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial + 1;
                var index2 = rep.serial + 2;
                $("[y="+index+"]").css("background", "#C0C0C0")
                $("[y="+index2+"]").css("background", "#C0C0C0")
            }
        });
    }
    MyClass.getGDSX = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = gid;
        Common.getData('/gdsx/getGDSXByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var x=obj.y + 1;
                var y=obj.x + 1;
                $(".itt[x=" + x + "][y=" + y + "]").html(obj.desc)
                $(".itt[x=" + x + "][y=" + y + "]").css("background", "#FFB6C1")
            })
        })
    }
    MyClass.init = function () {
        MyClass.getDefaultTerm();
        MyClass.getListBySchoolId();
        MyClass.getListByXq();
        MyClass.getTeaJXBList();
        MyClass.getJxbStudent();
        MyClass.getChushi();
        MyClass.getGuDingShiWu();
        MyClass.getGDSX();
        MyClass.GetTeachersSettledPositions()
        MyClass.getCurrentJXZ();
        $("body").on("click", "#student li", function () {
            $("#student li").removeClass("cur");
            $(this).addClass("cur");
            MyClass.getGuDingShiWu();
            MyClass.getGDSX();
            MyClass.GetTeachersSettledPositions()
            MyClass.getCurrentJXZ();
        })
        $("#week").change(function(){
            MyClass.getGuDingShiWu();
            MyClass.getGDSX();
            MyClass.GetTeachersSettledPositions()
            MyClass.getCurrentJXZ();
        })
    }
    MyClass.getTeaJXBList = function () {
        Common.getData('/paike/getJxb.do', {"jxbId": $("#jxbId").val()}, function (rep) {
            $("#jxbVal").append("<option>" + rep.message.name + "</option>")
            gid = rep.message.gradeId;
        })
    }
    MyClass.GetTeachersSettledPositions = function () {
        var par = {};
        par.studentId = $("#student .cur").attr("ids");
        par.week = $("#week").val();
        par.gid = gid;
        if (par.studentId != "" && par.studentId != undefined) {
            Common.getData('/paike/GetStudentSettledPositionsByWeek.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").css("background", "#FFF")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.JxbName + "</br>" + obj.teaName + "</br>" + obj.subName + "</br>" + obj.roomName)
                })
            })
        }
    }
    MyClass.getJxbStudent = function () {
        var dto = {};
        dto.id = $("#jxbId").val();
        dto.gid = gid;
        Common.getData('/n33_fenbaninfoset/getjxbStudentByJxbId.do', dto, function (rep) {
            Common.render({tmpl: $('#room_xq'), data: rep.message, context: '#student', overwrite: 1});
            $("#student li:eq(0)").addClass("cur")
        })
    }

    MyClass.getGuDingShiWu = function () {
        $(".itt").css("background", "#FFF")
        $(".itt").html("")
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": deXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
            })
        })
    }
    MyClass.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.xqid;
            deCid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }
    MyClass.getListBySchoolId = function () {
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdZKB.do', {"xqid": deXqid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }
    MyClass.getChushi = function () {
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
    MyClass.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        })
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            $("#week option[value=" + wk + "]").prop("selected", true)
        })
    }
    module.exports = MyClass;
})