/**
 * Created by albin on 2018/3/19.
 */
define('jxClass', ['jquery', 'doT', 'common', 'Rome', 'pagination','fselect', 'layer'], function (require, exports, module) {
    var jxClass = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('fselect');
    Common = require('common');

    var ciId = "";
    var zhenXqid = "";
    var gradeId = "";
    var classId = "";

    jxClass.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            ciId = rep.message.paikeci;
            zhenXqid=rep.message.xqid;
            $("#defaultTerm").attr("ids", rep.message.paikeci);
        });
    }

    jxClass.init = function () {
        jxClass.getDefaultTerm();
        jxClass.getGradeList();
        jxClass.getClassList();
        jxClass.getSubjectZouBan();
        jxClass.getStuJXB();
        initGrade();

        $("body").on("click", "#grade em", function () {
            $("#grade em").removeClass("cur");
            $(this).addClass("cur");
            jxClass.getClassList();
            jxClass.getSubjectZouBan();
            jxClass.getStuJXB();
        })

        $("body").on("click", "#cla em", function () {
            $("#cla em").removeClass("cur");
            $(this).addClass("cur");
            jxClass.getStuJXB();
        })
    }

    jxClass.getStuJXB = function () {
        var par = {};
        par.xqid = ciId;
        par.gid = $("#grade .cur").attr("ids");
        par.classId = $("#cla .cur").attr("ids");
        if(!ciId || ciId == "" || ciId == null || !par.gid || par.gid == "" || par.gid == null || !par.classId || par.classId == "" || par.classId == null){
            Common.render({tmpl: $('#jxb_temp'), data: [], context: '#jxb', overwrite: 1});
            return;
        }
        Common.getData('/studentJXB/getStuJXB.do', par, function (rep) {
            Common.render({tmpl: $('#jxb_temp'), data: rep.message, context: '#jxb', overwrite: 1});
        })
    }

    $("body").on("click", "#dcByClass", function () {
        var par = {};
        par.gid = $("#grade .cur").attr("ids");
        par.classId = $("#cla .cur").attr("ids");
        window.location.href="/studentJXB/exportByClass.do?xqid=" + ciId + "&gid=" + par.gid + "&classId=" + par.classId;
    })

    $("body").on("click", "#dcByGrade", function () {
        var par = {};
        par.gid = $("#grade .cur").attr("ids");
        setTimeout(function () {
            $(".jhh").show();
            $('.bg').show();
            window.location.href="/studentJXB/exportByGrade.do?xqid=" + ciId + "&gid=" + par.gid;
        },1000);
        c = setInterval(getStatusDC, 500);
    })


    jxClass.getSubjectZouBan = function () {
        var par = {};
        par.xqid = ciId;
        par.gid = $("#grade .cur").attr("ids");
        if(!ciId || ciId == "" || ciId == null || !par.gid || par.gid == "" || par.gid == null){
            Common.render({tmpl: $('#zouban_temp'), data: [], context: '#zouban', overwrite: 1});
            return;
        }
        Common.getData('/studentJXB/getSubjectZouBan.do', par, function (rep) {
            Common.render({tmpl: $('#zouban_temp'), data: rep.message, context: '#zouban', overwrite: 1});
        })
    }

    jxClass.getClassList = function () {
        var par = {};
        par.xqid = ciId;
        par.gradeId = $("#grade .cur").attr("ids");
        if(!par.xqid || par.xqid == "" || par.xqid == null || !par.gradeId || par.gradeId == "" || par.gradeId == null){
            Common.render({tmpl: $('#class_temp'), data: [], context: '#cla', overwrite: 1});
            return;
        }
        Common.getData('/new33classManage/getClassList.do', par, function (rep) {
            Common.render({tmpl: $('#class_temp'), data: rep.message, context: '#cla', overwrite: 1});
            //$("#cla em:eq(0)").addClass("cur");
        })

        var hgt = $('#cla').height();
        $("#Klass").parent().css('height',hgt);
        $("#Klass").css('height',hgt);
        $("#Klass").css('line-height',hgt+'px');
    }


    jxClass.getGradeList = function () {
        if(!ciId || ciId == "" || ciId == null){
            Common.render({tmpl: $('#grade_temp'), data: [], context: '#grade', overwrite: 1});
            return;
        }
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": ciId}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            // $(".xueke label:eq(0)").addClass("active");
        })
    }

    function getStatusDC() {
        $.ajax({
            type: "GET",
            url: '/studentJXB/getStatusDC.do',
            async: false,
            cache: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {
                if (rep.st == -1) {
                    $('.bg').hide();
                    $(".jhh").hide();
                    window.clearInterval(c);
                    layer.msg("导出学生教学班信息成功");
                    $(".jhhSp").text("");
                } else {
                    $(".jhhSp").text("正在导出" + rep.className);
                }
            }
        });
    }

    function initGrade(){
        try {
            Common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                        if (obj.key == "classId") {
                            classId = obj.value;
                        }
                    })
                    $("#grade em").each(function () {
                        if(gradeId==$(this).attr("ids")){
                            $(this).click();
                        }
                    });
                    $("#cla em").each(function () {
                        if(classId==$(this).attr("ids")){
                            $(this).click();
                        }
                    });
                } else {

                }
            });
        } catch (x) {
            console.log(x);
        }
    }
    function getCiIdIsFaBu() {
        var flag = null;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId":ciId}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }

    module.exports = jxClass;
})