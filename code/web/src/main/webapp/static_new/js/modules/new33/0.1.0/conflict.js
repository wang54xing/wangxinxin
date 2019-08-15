/**
 * Created by albin on 2017/7/25.
 */

define('conflict', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var conflict = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    common = require('common');
    var ctstus = new Map();
    var subid = ""
    var paikeci = "";
    conflict.init = function () {
        conflict.getDefaultTerm();
        conflict.getgrade();
        conflict.getSubjectByType();
        conflict.getConflictCollection();
        initGrade();
        $("body").on("click", "#grade em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            conflict.getSubjectByType();
            conflict.getConflictCollection();
        });
        $("body").on("click", "#type em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            conflict.getSubjectByType();
            conflict.getConflictCollection();
        });
        $("body").on("click", "#subject em", function () {
            if ($(this).hasClass('sp')) {
                if ($('#subject .cur').length == 2) {
                    if ($(this).hasClass('cur')) {
                        $(this).toggleClass('cur');
                    } else {
                        $("#subject em").removeClass('cur')
                        $("#subject em[val=" + subid + "]").addClass('cur');
                        $(this).addClass('cur')
                        subid = $(this).attr("val");
                    }
                } else {
                    $(this).toggleClass('cur');
                    subid = $(this).attr("val");
                }
            } else {
                $(this).addClass('cur').siblings().removeClass('cur');
                subid = $(this).attr("val");
            }
            conflict.getConflictCollection();
        });
        $("body").on("click", "#content .w125", function () {
            conflict.getStudents(ctstus[$(this).attr("jxbs")]);
            console.log(ctstus[$(this).attr("jxbs")]);
            $('.bg').show();
            $('.detail-popup').show();
        });

        $('.close,.qxx').click(function () {
            $('.bg').hide();
            $('.detail-popup').hide();
        })

    }

    conflict.getDefaultTerm = function () {
        common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#defaultTerm").attr("ids", rep.message.xqid);
            paikeci = rep.message.paikeci
        });
    }
    conflict.getgrade = function () {
        var par = {};
        par.xqid = paikeci;
        if(!par.xqid || par.xqid == "" || par.xqid == null){
            common.render({tmpl: $('#grade_temp'), data: [], context: '#grade', overwrite: 1});
            return;
        }
        common.getData('/new33isolateMange/getGradList.do', par, function (rep) {
            common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            // $("#grade em:eq(0)").addClass("cur");
        })
    }

    conflict.getSubjectByType = function () {
        var gid = $("#grade em[class='cur']").attr("val");
        var subType = $("#type em[class='cur']").attr("val");
        if (!gid || !subType || gid == "" || subType == "") {
            return;
        }
        common.getData('/n33_fenbaninfoset/getSubjectByType.do', {"gradeId": gid, "subType": subType}, function (rep) {
            if(rep.length==0||rep.length==1){
                $("#none_png").show();
                $("#all_content").hide();
            }
            else{
                $("#none_png").hide();
                $("#all_content").show();
            }
            common.render({tmpl: $('#subject_temp'), data: rep, context: '#subject', overwrite: 1});
            $("#subject em:eq(0)").addClass("cur");
            $("#subject em:eq(1)").addClass("cur");
            subid = $("#subject em:eq(0)").attr("val");
        })
    }
    conflict.getConflictCollection = function () {
        var gradeId = $("#grade em[class='cur']").attr("val");
        var type = $("#type em[class='cur']").attr("val");
        var subid1 = $("#subject em[class='sp cur']").eq(0).attr("val");
        var subid2 = $("#subject em[class='sp cur']").eq(1).attr("val");
        var subname1 = $("#subject em[class='sp cur']").eq(0).html();
        var subname2 = $("#subject em[class='sp cur']").eq(1).html();
        if (!gradeId || !type || gradeId == "" || type == "" || !subid1 || !subid2 || subid1 == "" || subid2 == "") {
            return;
        }
        $("#ctxk").html(subname1 + "-" + subname2);
        common.getData('/n33_fenbaninfoset/getConflictCollection.do', {
            "gradeId": gradeId,
            "type": type,
            "subid1": subid1,
            "subid2": subid2
        }, function (rep) {
            if(rep.content.length==0){
                $("#none_png").show();
                $("#all_content").hide();
            }
            else{
                $("#none_png").hide();
                $("#all_content").show();
            }
            if (rep.vertical) {
                common.render({tmpl: $('#vertical_temp'), data: rep.vertical, context: '#vertical', overwrite: 1});
            }
            if (rep.horizon) {
                common.render({tmpl: $('#horizon_temp'), data: rep.horizon, context: '#horizon', overwrite: 1});
            }
            if (rep.content) {
                common.render({tmpl: $('#content_temp'), data: rep.content, context: '#content', overwrite: 1});
            }
            if (rep.ctstus) {
                ctstus = rep.ctstus;
            }
        })
    }
    conflict.getStudents = function (ids) {
        var gradeId = $("#grade em[class='cur']").attr("val");
        if (!gradeId || gradeId == "" || !ids) {
            return;
        }
        common.getPostBodyData('/n33_fenbaninfoset/getStudents.do?gradeId=' + gradeId, ids, function (rep) {
            common.render({tmpl: $('#student_temp'), data: rep, context: '#student', overwrite: 1});
        })
    }
    function initGrade(){
        try {
            common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                        if (obj.key == "type") {
                            type = obj.value;
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
    module.exports = conflict;
})