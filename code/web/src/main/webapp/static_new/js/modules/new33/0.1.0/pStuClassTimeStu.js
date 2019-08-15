/**
 * Created by albin on 2018/3/23.
 */
define('pStuClassTimeStu', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var pStuClassTimeStu = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var stuId = "";
    var gid = "";
    var deCid = "";
    var deXqid = "";
    pStuClassTimeStu.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getCurrentJXZ.do', {}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial + 1;
                var index2 = rep.serial + 2;
                $("[y="+index+"]").css("background", "#C0C0C0")
                $("[y="+index2+"]").css("background", "#C0C0C0")
            }
        });
    }
    pStuClassTimeStu.getGDSX = function () {
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
    pStuClassTimeStu.GetTeachersSettledPositions = function () {
        var par = {};
        par.studentId = stuId;
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
    pStuClassTimeStu.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        })
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            $("#week option[value=" + wk + "]").prop("selected", true)
        })
    }
    pStuClassTimeStu.getStu = function () {
        var par = {};
        par.ciId = deCid;
        Common.getData('/new33isolateMange/getStuDto.do', par, function (rep) {
            gid = rep.message.gradeId;
            stuId = rep.message.userId;
            $("#userName").html(rep.message.userName);
        });
    }
    pStuClassTimeStu.init = function () {
        $("#week").change(function () {
            pStuClassTimeStu.getStudentList();
            pStuClassTimeStu.getGuDingShiWu()
            pStuClassTimeStu.getGDSX();
            pStuClassTimeStu.GetTeachersSettledPositions();
            pStuClassTimeStu.getCurrentJXZ();
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
        pStuClassTimeStu.getDefaultTerm();
        pStuClassTimeStu.getListBySchoolId();
        pStuClassTimeStu.getListByXq();
        pStuClassTimeStu.getStu()
        pStuClassTimeStu.getChushi();
        pStuClassTimeStu.getGuDingShiWu()
        pStuClassTimeStu.getGDSX();
        pStuClassTimeStu.GetTeachersSettledPositions();
        pStuClassTimeStu.getCurrentJXZ();
    }
    pStuClassTimeStu.getChushi = function () {
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
    pStuClassTimeStu.getListBySchoolId = function () {
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdZKB.do', {"xqid": deXqid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }
    pStuClassTimeStu.getGuDingShiWu = function () {
        $(".itt").css("background", "#FFF")
        $(".itt").html("")
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": deXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                if (obj.sk == 0) {
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
                } else {
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html("自习课")
                }
            })
        })
    }
    pStuClassTimeStu.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deCid = rep.message.paikeci;
            deXqid = rep.message.xqid;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }
    module.exports = pStuClassTimeStu;
})
