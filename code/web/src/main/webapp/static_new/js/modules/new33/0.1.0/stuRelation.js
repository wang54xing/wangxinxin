/**
 * Created by albin on 2018/3/30.
 */
define('stuRelation', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var stuRelation = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var subid = new Array();
    var jxbid = "";

    stuRelation.getCheckedSub = function () {
        subid = new Array();
        $("#subject .cls:checked").each(function (i, obj) {
            var val = $(obj).attr("ids");
            subid.push(val);
        })
    }

    stuRelation.getStudentListByNoAdd = function () {
        var par = {};
        par.jxbid = jxbid;
        par.classId = $(".ss-cur").attr("ids");
        par.xqid = deXqid;
        Common.getData('/studentTag/getStudentListByNoAdd.do', par, function (rep) {
            Common.render({tmpl: $('#stList'), data: rep.message, context: '#stuList', overwrite: 1});
            //$("#subject em:eq(0)").addClass("cur");
        })
    }

    stuRelation.addStu = function (uid) {
        var par = {};
        par.jxbid = jxbid;
        par.uid = uid;
        Common.getData('/studentTag/addStu.do', par, function (rep) {
            layer.msg(rep.message);
            stuRelation.getStudentListByNoAdd();
            stuRelation.getJiaoXueBanList();
        })
    }

    stuRelation.delStu = function (uid) {
        var par = {};
        par.jxbid = jxbid;
        par.uid = uid;
        Common.getData('/studentTag/delStu.do', par, function (rep) {
            layer.msg(rep.message)
            stuRelation.getStudentListByIsNotTag();
            stuRelation.getJiaoXueBanList();
        })
    }
    stuRelation.getStudentListByIsNotTag = function () {
        var par = {};
        par.jxbid = jxbid;
        par.classId = $(".ss-cur").attr("ids");
        par.xqid = deXqid;
        Common.getData('/studentTag/getStudentListByIsNotTag.do', par, function (rep) {
            Common.render({tmpl: $('#stList1'), data: rep.message, context: '#stuList', overwrite: 1});
            //$("#subject em:eq(0)").addClass("cur");
        })
    }

    stuRelation.removeTagByBan = function (tagid, jid) {
        var par = {};
        par.jxbid = jid;
        par.tagid = tagid;
        Common.getData('/studentTag/removeTagByBan.do', par, function (rep) {
            stuRelation.getJiaoXueBanList();
            stuRelation.getStudentViewTagList();
        })
    }

    stuRelation.init = function () {
        $("#sosu").click(function(){
            stuRelation.getJiaoXueBanList();
        })

        $("body").on("click", ".yin", function () {
            var dto = {};
            dto.id = $(this).attr("ids");
            Common.getData('/studentTag/updateView.do', dto, function (rep) {
                stuRelation.getTagList();
                stuRelation.getStudentViewTagList();
            });
        })
        $(".hs").click(function () {
            var dto = {};
            dto.xqid = deXqid;
            dto.gradeId = $("#grade .cur").attr("ids");
            Common.getData('/studentTag/updateViewAll.do', dto, function (rep) {
                stuRelation.getTagList();
                stuRelation.getStudentViewTagList();
            })
        })
        $("#xuanze").click(function () {
            var dto = {};
            var ids = new Array();
            $("#tg input:checked").each(function (i, obj) {
                ids.push($(obj).attr("ids"));
            })
            dto.ids = ids;
            Common.getPostBodyData('/studentTag/updateTagView.do', dto, function (rep) {
                stuRelation.getTagList();
                stuRelation.getStudentViewTagList();
                $(".tall").prop("checked", false);
                $(".addxz-popup,.bg").hide();
            });
        })

        $("body").on("click", ".rrow", function () {
            stuRelation.removeTagByBan($(this).attr("ids"), $(this).attr("jxbids"))
        })
        $("body").on("click", "#grade em", function () {
            $("#grade em").removeClass("cur");
            $(this).addClass("cur");
            stuRelation.getClassList();
            stuRelation.getSubjectByType();
            stuRelation.getJiaoXueBanList();
            stuRelation.getTagList();
            stuRelation.getStudentViewTagList();
        })

        $("body").on("click", ".xuanrendea", function () {
            stuRelation.addStu($(this).attr("ids"))
        })
        $("body").on("click", ".ss-li li", function () {
            $(".ss-li li").removeClass("ss-cur");
            $(this).addClass("ss-cur");
            if ($("#quedingren").attr("iz") == 0) {
                stuRelation.getStudentListByNoAdd();
            } else {
                stuRelation.getStudentListByIsNotTag();
            }
        })
        $("body").on("click", ".shanchude", function () {
            stuRelation.delStu($(this).attr("ids"))
        })
        $("#quedingren").click(function () {
            $(".addstu-popup,.bg").hide();
        })
        $("#subType").change(function () {
            $(".app").html("")
            $(".ttall").prop("checked",false)
            subid=new Array();
            stuRelation.getSubjectByType();
            stuRelation.getJiaoXueBanList();
            stuRelation.getStudentViewTagList();
        })
        $("body").on("click", ".add-re", function () {
            $('.isTj').text("添加学生:");
            $('.bg').show();
            $('.addstu-popup').show();
            $("#bnm").html($(this).attr("nm"))
            jxbid = $(this).attr("ids");
            $("#quedingren").attr("iz", 0);
            stuRelation.getStudentListByNoAdd();
        })
        $("body").on("click", ".del-re", function () {
            $('.isTj').text("删除学生:");
            $('.bg').show();
            $('.addstu-popup').show();
            $("#bnm").html($(this).attr("nm"))
            jxbid = $(this).attr("ids");
            $("#quedingren").attr("iz", 1);
            stuRelation.getStudentListByIsNotTag();
        })


        $('body').on('change', '.bl-dv .cls', function () {
            var val = $(this).parent().text();
            if ($(this).is(':checked')) {
                $('.seld .app').append('<em ids=' + val + '>' + val + '</em>')
            } else {
                $("em[ids=" + val + "]").remove()
            }
            stuRelation.getCheckedSub();
            stuRelation.getJiaoXueBanList();
        })
        
        $('.ttall').change(function () {
            if ($(this).is(':checked')) {
                $('.seld .app').empty();
                $('.bl-sp span input').prop("checked", true);
                $("#subject .cls").each(function (i, obj) {
                    var val = $(obj).parent().text();
                    $('.seld .app').append('<em ids=' + val + '>' + val + '</em>')
                })
            } else {
                $('.seld .app').empty();
                $('.bl-sp span input').removeAttr("checked");
            }
            stuRelation.getCheckedSub();
            stuRelation.getJiaoXueBanList();
        })
        stuRelation.getDefaultTerm();
        stuRelation.getGrade();
        stuRelation.getClassList();
        stuRelation.getSubjectByType();
        stuRelation.getJiaoXueBanList();
        stuRelation.getTagList();
        stuRelation.getStudentViewTagList();
    }

    stuRelation.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
        	$("#defaultTerm").text(rep.message.xqnm);
        	deXqid = rep.message.paikeci;
        	$("#defaultTerm").attr("ids",rep.message.xqid);
        });
    }
    stuRelation.getGrade = function () {
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade'});
            $("#grade em:eq(0)").addClass("cur");
        })
    }
    stuRelation.getSubjectByType = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $("#grade .cur").attr("ids");
        par.subType = $("#subType").val();
        Common.getData('/n33_setJXB/getSubjectByType.do', par, function (rep) {
            Common.render({tmpl: $('#sub_temp'), data: rep.message, context: '#subject', overwrite: 1});
            //$("#subject em:eq(0)").addClass("cur");
        })
    }

    stuRelation.getJiaoXueBanList = function () {
        var dto = {};
        dto.xqid = deXqid;
        dto.gid = $("#grade .cur").attr("ids");
        dto.subType = $("#subType").val();
        dto.subid = subid;
        dto.jxbName = "*";
        if ($("#jxbName").val() != "") {
            dto.jxbName = $("#jxbName").val();
        }
        Common.getPostBodyData('/studentTag/getJiaoXueBanList.do', dto, function (rep) {
            Common.render({tmpl: $('#studentList'), data: rep.message, context: '#tbd', overwrite: 1});
        });
    }

    stuRelation.getStudentViewTagList = function () {
        var dto = {};
        dto.xqid = deXqid;
        dto.gradeId = $("#grade .cur").attr("ids");
        Common.getData('/studentTag/getStudentViewTagByGradeId.do', dto, function (rep) {
            Common.render({tmpl: $('#tagList'), data: rep.message, context: '.ul1', overwrite: 1});
        })
    }
    stuRelation.getTagList = function () {
        var dto = {};
        dto.xqid = deXqid;
        dto.gradeId = $("#grade .cur").attr("ids");
        Common.getData('/studentTag/getTagList.do', dto, function (rep) {
            Common.render({tmpl: $('#tglist'), data: rep.message, context: '#tg', overwrite: 1});
            $(".size").html(rep.message.length);
        })
    }
    stuRelation.getClassList = function () {
        var par = {}
        par.xqid = deXqid;
        par.gradeId = $("#grade .cur").attr("ids");
        Common.getData('/new33classManage/getClassList.do', par, function (rep) {
            Common.render({tmpl: $('#classList'), data: rep.message, context: '.ss-li', overwrite: 1});
            $(".ss-li li:eq(0)").addClass("ss-cur")
        })
    }
    module.exports = stuRelation;
})