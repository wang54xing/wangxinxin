/**
 * Created by albin on 2017/7/25.
 */

define('selectProgress', ['jquery', 'doT', 'common','Rome', 'pagination', 'layer'], function (require, exports, module) {
    var selectProgress = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    common = require('common');
    var ciId = "";

    selectProgress.init=function(){
        getDefaultCi();
        getgrade(ciId);

        initGrade();


        $("body").on("click","#grade em",function(){
            $(this).addClass('cur').siblings().removeClass('cur');
            getGradeProgress();
        });
        $('.gg-popup i,.qx').click(function(){
            $('.bg').hide();
            $('.gg-popup').hide();
            $('.add-popup').hide();
        })
        $('.cou-x em').click(function(){
            $('.reset').show();
        })
        $('.d-sty em').click(function(){
            $(this).addClass('cur').siblings().removeClass('cur')
        })
        $('.chat-00').click(function(){
            getClassList();
            getStudentsByClassId();
            $('.sul1').hide();
            $('.att').hide();
            $('.sul2').show();
        })
        $('.prev').click(function(){
            $('.sul1').show();
            $('.att').show();
            $('.sul2').hide();
        })
        $("#klass").change(function () {
            getStudentsByClassId();
        });
        $("body").on("click", "#student .gg", function () {
            var ids = $(this).attr("ids");
            getStudentDto(ids);
            getAllCombineName();
            var flag = getCiIdIsFaBu();
            if(flag){
                if($(".inp1").val() != "" && $(".inp1").val() != undefined && $(".inp1").val() != null){
                    layer.alert("课表已发布，不允许修改选科组合");
                    return;
                }
            }
            $('.bg').show();
            $('.gg-popup').show();

        })
        $("body").on("click", ".qd", function () {
            $('.gg-popup,.bg').hide();
            $('.gg-popup,.bg').hide();
            updateStudent();

        })
        $("#sosu").click(function () {
            getStudentsByName();
        });
        $("#export").click(function () {
            window.location.href = "/n33_slectprogress/export.do?ciId="+ciId+"&gradeId="+$("#grade em[class='cur']").attr("val");
        });
    }

     function getgrade(xqid){
        common.getData('/new33isolateMange/getGradList.do', {"xqid":xqid}, function (rep) {
            common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade',overwrite:1});

        })
    }

     function getDefaultCi() {
        common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            ciId = rep.message.paikeci;
        });
    }
    function getGradeProgress(){
        common.getData('/n33_slectprogress/getGradeProgress.do', {ciId:ciId,gradeId: $("#grade em[class='cur']").attr("val")}, function (rep) {
            common.render({tmpl: $('#select_progress_temp'), data: rep.list, context: '#select_progress',overwrite:1});
        });
    }
    function getClassList() {
        common.getData('/new33classManage/getClassList.do', {xqid:ciId,gradeId: $("#grade em[class='cur']").attr("val")}, function (rep) {
            rep.message.unshift({"className":"年级","classId":"grade"});
            common.render({tmpl: $('#klass_temp'), data: rep.message, context: '#klass',overwrite:1});
        });
    }
    function getStudentsByClassId(){
        common.getData('/n33_slectprogress/getStudentsByClassId.do', {ciId:ciId,gradeId: $("#grade em[class='cur']").attr("val"),"classId":$("#klass").val()}, function (rep) {
            common.render({tmpl: $('#student_temp'), data: rep, context: '#student',overwrite:1});
        });
    }
    function getStudentsByName(){
        var name = $("#stuName").val();
        common.getData('/n33_slectprogress/getStudentsByName.do', {ciId:ciId,name: name,gradeId: $("#grade em[class='cur']").attr("val")}, function (rep) {
            common.render({tmpl: $('#student_temp'), data: rep, context: '#student',overwrite:1});
        });
    }
    function getStudentDto(ids) {
        var par = {};
        par.id = ids;
        common.getData('/IsolateStudent/getStudent.do', par, function (rep) {
            $(".inp1").val(rep.message.combiname);
            $(".qd").attr("ids",rep.message.id);
        });
    }
    function getAllCombineName() {
        var par = {};
        par.termId = ciId;
        par.gradeId =$("#grade em[class='cur']").attr("val");
        if(par.termId != "" && par.termId != undefined && par.termId != null  && par.gradeId != "" && par.gradeId != undefined && par.gradeId != null){
            common.getData('/IsolateStudent/getSelectLessonsList.do', par, function (rep) {
                common.render({tmpl: $('#sel_temp'), data: rep.message, context: '#sel', overwrite: 1});
                $.each(rep.message, function (i, obj) {
                    $("#sel option:eq("+i+")").attr("subjectId1", obj.subjectId1Str);
                    $("#sel option:eq("+i+")").attr("subjectId2", obj.subjectId2Str);
                    $("#sel option:eq("+i+")").attr("subjectId3", obj.subjectId3Str);
                    $("#sel option:eq("+i+")").attr("name", obj.name);
                })
            });
        }else{
            common.render({tmpl: $('#sel_temp'), data:[], context: '#sel', overwrite: 1});
        }
    }

    function updateStudent() {
        var par = {};
        par.id = $(".qd").attr("ids");
        var value=$("#sel").val();
        par.subjectId1 = $("#sel option[value="+value+"]").attr("subjectId1");
        par.subjectId2 = $("#sel option[value="+value+"]").attr("subjectId2");
        par.subjectId3 = $("#sel option[value="+value+"]").attr("subjectId3");
        par.name = $("#sel option[value="+value+"]").attr("name");
        common.getData('/IsolateStudent/updateStudent.do', par, function (rep) {

        });
        getStudentsByClassId();
    }
    function getCiIdIsFaBu() {
        var flag = null;
        var xqid = ciId;
        common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId":xqid}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }
    function initGrade(){
        try {
            common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }


                    })

                    $("#grade em").each(function () {
                        if(gradeId==$(this).attr("val")){
                            $(this).click();
                        }
                    });

                } else {

                }
            });
        } catch (x) {

        }
    }
    module.exports = selectProgress;
})