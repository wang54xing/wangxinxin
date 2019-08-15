/**
 * Created by albin on 2017/5/11.
 */
define('chapterDirectory', ['jquery', 'doT', 'common', 'pagination', "layer"], function (require, exports, module) {
    var chapterDirectory = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    common = require('common');
    var sbt = -1;
    var gst = -1;
    var zs = 0;
    chapterDirectory.init = function () {
        try {
            common.userData("Cai", function (res) {
                var dd = -1;
                zs = 1;
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "subject") {
                            sbt = obj.value;
                        }
                        if (obj.key == "grade") {
                            gst = obj.value;
                        }
                        if (obj.key == "sty") {
                            $("#schoolType option[value=" + obj.value + "]").attr("selected", true);
                            $("#grade").empty();
                            $("#subject").empty();
                            $("<option value='-1'>全部学科</option>").appendTo("#subject");
                            $("<option value='-1'>全部年级</option>").appendTo("#grade");
                            chapterDirectory.getType(-1, 0)
                        }
                        if (obj.key == "did") {
                            dd = obj.value;
                        }
                        if (obj.key = "year") {
                            $("#jzsj option[value=" + obj.value + "]").attr("selected", true);
                        }
                    })
                    $("#subject option[value=" + sbt + "]").attr("selected", true);
                    $("#grade option[value=" + gst + "]").attr("selected", true);
                    chapterDirectory.getTypeCB();
                    $("#cbs option[value=" + dd + "]").attr("selected", true);
                    chapterDirectory.getJiaoList(1);
                    if ($(".active").attr("ids") != undefined) {
                        chapterDirectory.gets($(".active").attr("ids"));
                    }
                } if(res.message.length<=0) {
                    chapterDirectory.getTypeCB();
                    chapterDirectory.getType(-1, 0)
                    chapterDirectory.getJiaoList(1);
                    chapterDirectory.get();
                }
                //alert(res.code);
            });
        } catch (x) {

        }


        $(".view-textbooks").click(function () {
            var id = $("#names").attr("ids");
            var name = $("#names").html();
            var is=$(".active").attr("is");
            window.location.href = '/teacher/dictionary/chapterDetail.do?id=' + id + "&name=" + name+"&is="+is;
        })

        $("body").on("click", ".chapter-delate", function () {
            var p = {};
            p.id = $(this).attr("ids");
            layer.confirm('确实要删除吗？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('删除中。。。', {time: 1000});
                common.getData("/teacher/dictionary/del/teacherDri.do", p, function (res) {
                    chapterDirectory.get();
                })
            }, function () {
            });
        })

        $("body").on("click", ".chapter-edit", function () {
            if ($(this).attr("s") == 0) {
                $("#tj").html("编辑小节");
                $("#tjname").html("小节名称");
                $("#tjnr").attr("placeholder", "请输入小节名称");
            } else {
                $("#tj").html("编辑章节");
                $("#tjname").html("章节名称");
                $("#tjnr").attr("placeholder", "请输入章节名称");
            }
            $("#tjnr").val($(this).attr("name"));
            $("#bjid").val($(this).attr("ids"));
            $(".addnodule-alert,.bg").fadeIn();
        })


        //添加小节
        $("body").on("click", ".chapter-add", function () {
            $("#tj").html("添加小节");
            $("#tjname").html("小节名称");
            $("#tjnr").attr("placeholder", "请输入小节名称");
            $("#names").attr("tids", $(this).attr("ids"));
            $("#bjid").val("*");
            $("#tjnr").val("");
            $(".addnodule-alert,.bg").fadeIn();
        })
        //添加章节
        $("body").on("click", ".add-chapter>span", function () {
            $("#tj").html("添加章节");
            $("#tjname").html("章节名称");
            $("#tjnr").attr("placeholder", "请输入章节名称");
            $("#bjid").val("*");
            $("#tjnr").val("");
            $(".addnodule-alert,.bg").fadeIn();
        })
        $(".btn-sure").click(function () {
            var p = {};
            p.name = $("#tjnr").val();
            if ($("#bjid").val() == "*") {
                if (p.name != "") {
                    if ($("#tj").html() == "添加章节") {
                        p.type = 3;
                        p.parentId = $("#names").attr("ids");
                    } else {
                        p.type = 4;
                        p.parentId = $("#names").attr("tids");
                    }
                    chapterDirectory.add(p);
                } else {
                    layer.msg("请将信息填写完整!");
                }
            } else {
                p.dictionaryID = $("#bjid").val();
                p.year = 0;
                p.subject = 0;
                p.sty = 0;
                p.grade = 0;
                p.type = 2;
                if (p.name != "") {
                    chapterDirectory.up(p);
                } else {
                    layer.msg("请将信息填写完整!");
                }
            }
        })
        //左侧列表
        $("body").on("click", "#ac>dd", function () {
            $(this).addClass("active").siblings().removeClass("active");
            $("#names").html($(this).find(".textbooks-name").html());
            $("#names").attr("ids", $(this).attr("ids"));
            if($(this).attr("is")==0){
                $(".edit-btn").hide();
            }else{
                $(".edit-btn").show();
            }
            chapterDirectory.get();
        })
        $("#schoolType").change(function () {
            var sub = $("#subject").val();
            $("#grade").empty();
            $("#subject").empty();
            $("<option value='-1'>全部学科</option>").appendTo("#subject");
            $("<option value='-1'>全部年级</option>").appendTo("#grade");
            chapterDirectory.getType(-1, 0);
            $("#subject option").each(function () {
                if ($(this).val() == sub) {
                    $("#subject option").attr("selected", false);
                    $(this).attr("selected", true);
                }
            })
            chapterDirectory.getJiaoList(1);
            chapterDirectory.get();

        })
        $("#cbs").change(function () {
            chapterDirectory.getJiaoList(1);
            chapterDirectory.get();
        })
        $("#subject").change(function () {
            chapterDirectory.getJiaoList(1);
            chapterDirectory.get();
        })
        $("#jzsj").change(function () {
            chapterDirectory.getJiaoList(1);
            chapterDirectory.get();
        })
        $("#ss").click(function () {
            chapterDirectory.getJiaoList(1);
            chapterDirectory.get();
        })
        $("#grade").change(function () {
            var sub = $("#subject").val();
            $("#subject").empty();
            $("<option value='-1'>全部学科</option>").appendTo("#subject");
            chapterDirectory.getType($(this).val(), 1);
            $("#subject option").each(function () {
                if ($(this).val() == sub) {
                    $("#subject option").attr("selected", false);
                    $(this).attr("selected", true);
                }
            })
            chapterDirectory.getJiaoList(1);
            chapterDirectory.get();

        })
        //if (zs == 0) {
        //
        //}
    }
    chapterDirectory.getTypeCB = function () {
        $("#cbs").empty();
        $('<option value="-1">出版社</option>').appendTo("#cbs");
        var p = {};
        p.type = 1;
        p.page = 1;
        p.psize = 100;
        common.getData("/teacher/dictionary/slist.do", p, function (res) {
            common.render({tmpl: $('#chuban'), data: res.message.list, context: '#cbs'});
        })
    }

    chapterDirectory.getType = function (grade, i) {
        var param = {};
        param.schoolType = $("#schoolType").val();
        if (param.schoolType == -1) {
            param.schoolType = 2;
        }
        param.subject = grade
        common.getData("/cloud/infoss.do", param, function (res) {
            if (i == 0) {
                common.render({tmpl: $('#cloudLesson_grade'), data: res.grade, context: '#grade'});
            }
            common.render({tmpl: $('#cloudLesson_grade'), data: res.subject, context: '#subject'});
        })
    }
    chapterDirectory.add = function (par) {
        common.getData("/teacher/dictionary/add/teacherdictionary.do", par, function (res) {
            $(".addnodule-alert,.bg").hide();
            chapterDirectory.get();
        })
    }

    chapterDirectory.up = function (par) {
        common.getData("/teacher/dictionary/up/teacherBook.do", par, function (res) {
            $(".addnodule-alert,.bg").hide();
            chapterDirectory.get();
        })
    }
    chapterDirectory.get = function () {
        $(".detail-main").empty();
        var p = {};
        p.dictionaryId = $("#names").attr("ids");
        if(p.dictionaryId!=""){
        common.getData("/teacher/dictionary/materialList.do", p, function (res) {
            common.render({tmpl: $('#text'), data: res.message, context: '.detail-main'});
            $.each(res.message, function (i, obj) {
                var id = "#tt_" + obj.id;
                common.render({tmpl: $('#textxj'), data: obj.list, context: id});
            })
            $(" <div class='add-chapter'><span>+添加章节</span></div>").appendTo(".detail-main");
            if ($(".edit-btn").html() == "保存") {
                $(".add-chapter").show();
            }
        })
        }
    }
    chapterDirectory.gets = function (id) {
        $(".detail-main").empty();
        var p = {};
        p.dictionaryId = id;
        common.getData("/teacher/dictionary/materialList.do", p, function (res) {
            common.render({tmpl: $('#text'), data: res.message, context: '.detail-main'});
            $.each(res.message, function (i, obj) {
                var id = "#tt_" + obj.id;
                common.render({tmpl: $('#textxj'), data: obj.list, context: id});
            })
            $(" <div class='add-chapter'><span>+添加章节</span></div>").appendTo(".detail-main");
            if ($(".edit-btn").html() == "保存") {
                $(".add-chapter").show();
            }
        })
    }

    chapterDirectory.getJiaoList = function (page) {
        $("#names").html("");
        $("#names").attr("ids", "");
        $("#ac").empty();
        var p = {};
        if ($("#jzssname").val() != "") {
            p.name = $("#jzssname").val();
        }
        p.page = page;
        if ($("#cbs").val() != -1) {
            p.dictionaryId = $("#cbs").val();
            p.did = $("#cbs").val();
        } else {
            p.did = "-1";
        }
        p.sty = $("#schoolType").val();
        p.year = $("#jzsj").val();
        p.subject = $("#subject").val();
        p.grade = $("#grade").val();
        p.pagesize = 10;
        common.getData("/teacher/dictionary/book/slist.do", p, function (res) {
            if(res.message.list.length>0){
            common.render({tmpl: $('#action'), data: res.message.list, context: '#ac'});
            $("#ac dd:eq(0)").addClass("active");
            $("#names").html($("#ac dd:eq(0)").find(".textbooks-name").html());
            $("#names").attr("ids", $("#ac dd:eq(0)").attr("ids"));
            if( $("#ac dd:eq(0)").attr("is")==0){
                $(".edit-btn").hide();
            }else{
                $(".edit-btn").show();
            }
            }
            $('.page-turn').jqPaginator({
                totalPages: Math.ceil(res.message.count / 10) == 0 ? 1 : Math.ceil(res.message.count / 10),//总页数
                visiblePages: 4,//分多少页
                currentPage: parseInt(page),//当前页数
                first: '<li class="first"><a href="javascript:void(0);">首<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                onPageChange: function (n) { //回调函数
                    if (n != page) {
                        page = n;
                        chapterDirectory.getJiaoList(page);
                        chapterDirectory.get();
                    }
                }
            });
        })
    }
    module.exports = chapterDirectory;
})