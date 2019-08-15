/**
 * Created by albin on 2018/3/7.
 */
define('compared', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer','fselect'], function (require, exports, module) {
    var compared = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    require('fselect');
    Common = require('common');
    var paikeci = "";
    var gradeId = "";
    var classId = "";

    compared.init = function () {
        $("body").on("click", "#grade em", function () {
            $("#grade em").removeClass("cur");
            $(this).addClass("cur");
            compared.getClassList();
            compared.getDifferenceXQ();
            compared.getDifference();
        });

        $("body").on("click", "#cla em", function () {
            $("#cla em").removeClass("cur");
            $(this).addClass("cur");
            compared.getDifferenceXQ();
            compared.getDifference();
        });

        $('body').on("change","#time",function () {
            compared.getDifferenceXQ();
            compared.getDifference();

        });

        $('.xk_light').change(function () {
            compared.selectStudent($(this).val());
        });

        compared.getDefaultCi();
        compared.getGradeList();
        compared.getClassList();
        compared.getAllTermTimesList();
        compared.getDifference();
        initGrade();
    }

    compared.selectStudent = function(type) {
        if(type == 1) {
            $('.record').each(function () {
                $(this).show();
            })
        }else if(type == 2){
            $('.record').each(function () {
                $(this).show();
            })
            $('.record').each(function () {
                if($(this).attr("type") == 1){
                    $(this).hide();
                }
            })
        }else if(type == 3){
            $('.record').each(function () {
                $(this).show();
            })
            $('.record').each(function () {
                if($(this).attr("type") == 2){
                    $(this).hide();
                }
            })
        }
    }


    compared.getDifferenceXQ = function(){
        var par = {};
        par.ciId1 = paikeci;
        par.ciId2 = $('#time').val();
        Common.getData('/IsolateStudent/getDifferenceXQ.do', par, function (rep) {
            var ciId1Name_xqname = rep.message.ciId1Name.xqname;
            var ciId1Name_ciName = rep.message.ciId1Name.ciName;
            var ciId2Name_xqname = rep.message.ciId2Name.xqname;
            var ciId2Name_ciName = rep.message.ciId2Name.ciName;
            $('#ciId1Name').html(ciId1Name_xqname + '<br>' + ciId1Name_ciName);
            $('#ciId2Name').html(ciId2Name_xqname + '<br>' + ciId2Name_ciName);
        });
    }

    compared.getDifference = function (){
        var par = {};
        par.ciId1 = paikeci;
        par.ciId2 = $('#time').val();
        par.classId = $("#cla .cur").attr("ids");
        if(!par.ciId1 || par.ciId1 == "" || par.ciId1 == null || !par.ciId2 || par.ciId2 == "" || par.ciId2 == null || !par.classId || par.classId == "" || par.classId == null){
            Common.render({tmpl: $('#different_temp'), data: [], context: '#different', overwrite: 1});
            return;
        }
        Common.getData('/IsolateStudent/getDifference.do', par, function (rep) {
            Common.render({tmpl: $('#different_temp'), data: rep.message, context: '#different', overwrite: 1});
            compared.selectStudent($('.xk_light').val());
        });
    }

    compared.getDefaultCi = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $('#ciId1Name').text(rep.message.paikeciname)
            paikeci = rep.message.paikeci;
        });
    }

    compared.getClassList = function () {
        var par = {};
        par.xqid = paikeci;
        par.gradeId = $("#grade .cur").attr("ids");
        if(!par.xqid || par.xqid == "" || par.xqid == null || !par.gradeId || par.gradeId == "" || par.gradeId == null){
            Common.render({tmpl: $('#class_temp'), data: [], context: '#cla', overwrite: 1});
            return;
        }
        Common.getData('/new33classManage/getClassList.do', par, function (rep) {
            Common.render({tmpl: $('#class_temp'), data: rep.message, context: '#cla', overwrite: 1});
            $("#cla em:eq(0)").addClass("cur");
        })

        var hgt = $('#cla').height();
        $("#Klass").parent().css('height',hgt);
        $("#Klass").css('height',hgt);
        $("#Klass").css('line-height',hgt+'px');
    }

    compared.getAllTermTimesList = function(){
        Common.getData('/new33isolateMange/getAllTermTimesList.do', {}, function (rep) {
            var array = new Array();
            $.each(rep.message,function (i,st) {
                if(paikeci != st.ciId){
                    array.push(st)
                }
            })
            Common.render({tmpl: $('#times_temp'), data: array, context: '#time', overwrite: 1});

        })
    }


    compared.getGradeList = function () {
        var xqid = paikeci;
        if(!xqid || xqid == "" || xqid == null){
        	Common.render({tmpl: $('#grade_temp'), data: [], context: '#grade', overwrite: 1});
        	return;
        }
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            $("#grade em:eq(0)").addClass("cur");
        })
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

        }
    }

    module.exports = compared;
})