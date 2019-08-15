/**
 * Created by albin on 2017/5/10.
 */
define('publisher', ['jquery', 'doT', 'common', 'pagination', "layer"], function (require, exports, module) {
    var publisher = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    common = require('common');
    publisher.init = function () {
        //编辑教材
        $("body").on("click", ".table-edit", function () {
            $(".bg,.add-alert").fadeIn();
            $("#schoolType option").attr("selected", false)
            $("#schoolType option[value=" + $(this).attr("d") + "]").attr("selected", true);
            $("#grade").empty();
            $("#subject").empty();
            publisher.loadType(-1, 0);
            $("#jiaoname").val($(this).parent().parent().find(".xxname").text());
            $("#jzsj option").attr("selected", false)
            $("#subject option").attr("selected", false)
            $("#grade option").attr("selected", false)
            $("#jzsj option[value=" + $(this).attr("y") + "]").attr("selected", true);
            $("#subject option[value=" + $(this).attr("s") + "]").attr("selected", true);
            $("#grade option[value=" + $(this).attr("g") + "]").attr("selected", true);
            $("#jcqd").attr("ids", $(this).parent().parent().attr("ids"));
        })
        //删除教材
        $("body").on("click", ".table-del", function () {
            var par = {};
            par.id = $(this).parent().parent().attr("ids");
            layer.confirm('确实要删除这个教材吗？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('删除中。。。', {time: 1000});
                publisher.getdel(par);
            }, function () {
            });
        })
        //右侧导航
        publisher.loadTypeDh(-1, 0);
        $("#xzxd").change(function () {
            $("#qbnj").empty();
            $("#qbxk").empty();
            $("<option value='-1'>全部学科</option>").appendTo("#qbxk");
            $("<option value='-1'>全部年级</option>").appendTo("#qbnj");
            publisher.loadTypeDh(-1, 0);
            publisher.getjiaoList($(".active").attr("ids"), 1)
        })
        $("#jss").click(function () {
            publisher.getjiaoList($(".active").attr("ids"), 1)
        })
        $("#qbnj").change(function () {
            $("#qbxk").empty();
            $("<option value='-1'>全部学科</option>").appendTo("#qbxk");
            publisher.loadTypeDh($(this).val(), 1);
            publisher.getjiaoList($(".active").attr("ids"), 1)
        })
        $("#qbxk").change(function () {
            publisher.getjiaoList($(".active").attr("ids"), 1)
        })
        $("#cbsj").change(function () {
            publisher.getjiaoList($(".active").attr("ids"), 1)
        })
        //左侧列表
        $("body").on("click", ".publisher-list>dd", function () {
            $(this).addClass("active").siblings().removeClass("active");
            $("#xzxd option").attr("selected",false);
            $("#xzxd option:eq(0)").attr("selected",true);
            $("#cbsj option").attr("selected",false);
            $("#cbsj option:eq(0)").attr("selected",true);
            $("#qbnj").empty();
            $("#qbxk").empty();
            $("<option value='-1'>全部学科</option>").appendTo("#qbxk");
            $("<option value='-1'>全部年级</option>").appendTo("#qbnj");
            publisher.loadTypeDh(-1, 0);
            publisher.getjiaoList($(this).attr("ids"), 1);
        })

        $("body").on("click", ".add-textbooks", function () {
            $(".bg,.add-alert").fadeIn();
            $("#jiaoname").val("")
            $("#jcqd").attr("ids", "*");
            $("#jzsj option").attr("selected", false)
            $("#schoolType option").attr("selected", false)
            $("#jzsj option:eq(0)").attr("selected", true);
            $("#schoolType option:eq(0)").attr("selected", true);
            $("#grade").empty();
            $("#subject").empty();
            publisher.loadType(-1, 0);
        })
        $("#jcqd").click(function () {
            publisher.addJiao();
        })
        $("#ban-sure").click(function () {
            publisher.addBan();
        })
        $("#cbss").click(function () {
            publisher.getBanList(1);
        })
        $("#schoolType").change(function () {
            var sub = $("#subject").val();
            $("#grade").empty();
            $("#subject").empty();
            publisher.loadType(-1, 0);
            $("#subject option").each(function () {
                if ($(this).val() == sub) {
                    $("#subject option").attr("selected", false);
                    $(this).attr("selected", true);
                }
            })
        })
        $("#grade").change(function () {
            var sub = $("#subject").val();
            $("#subject").empty();
            publisher.loadType($(this).val(), 1);
            $("#subject option").each(function () {
                if ($(this).val() == sub) {
                    $("#subject option").attr("selected", false);
                    $(this).attr("selected", true);
                }
            })
        })
        $("body").on("click", ".sccb", function () {
            var par = {};
            par.id = $(this).parent().attr("ids");
            layer.confirm('确实要删除这个出版社吗？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('删除中。。。', {time: 1000});
                publisher.getBanDelete(par);
            }, function () {
            });
        })
        publisher.getBanList(1);
        publisher.getjiaoList($(".active").attr("ids"), 1)
    }
    publisher.addBan = function () {
        var p = {};
        p.name = $("#itemName").val();
        p.type = 1;
        if (p.name != "") {
            common.getData("/teacher/dictionary/add/teacherdictionary.do", p, function (res) {
                $(".addpublisher-alert,.bg").hide();
                publisher.getBanList(1);
                publisher.getjiaoList($(".active").attr("ids"), 1)
            })
        } else {
            layer.msg("请将信息填写完整！")
        }
    }

    publisher.addJiao = function () {
        var p = {};
        p.name = $("#jiaoname").val();
        p.dictionaryID = $(".active").attr("ids");
        p.year = $("#jzsj").val();
        p.subject = $("#subject").val();
        p.sty = $("#schoolType").val();
        p.grade = $("#grade").val();
        p.type = 2;
        if (p.name != "") {
            if ($("#jcqd").attr("ids") == "*") {
                common.getData("/teacher/dictionary/add/teacherBook.do", p, function (res) {
                    $(".bg,.add-alert").hide();
                    publisher.getjiaoList($(".active").attr("ids"), 1)
                })
            } else {
                p.dictionaryID = $("#jcqd").attr("ids");
                common.getData("/teacher/dictionary/up/teacherBook.do", p, function (res) {
                    $(".bg,.add-alert").hide();
                    publisher.getjiaoList($(".active").attr("ids"), 1)
                })
            }
        } else {
            layer.msg("请将信息填写完整！")
        }
    }
    publisher.getjiaoList = function (dictionaryId, page) {
        $("#tb").empty();
        var p = {};
        if ($("#jzssname").val() != "") {
            p.name = $("#jzssname").val();
        }
        p.page = page;
        p.dictionaryId = dictionaryId;
        p.sty = $("#xzxd").val();
        p.year = $("#cbsj").val();
        p.subject = $("#qbxk").val();
        p.grade = $("#qbnj").val();
        p.pagesize=20;
        common.getData("/teacher/dictionary/book/slist.do", p, function (res) {
            common.render({tmpl: $('#tblist'), data: res.message.list, context: '#tb'});
            $('#jedat').jqPaginator({
                totalPages: Math.ceil(res.message.count / 20) == 0 ? 1 : Math.ceil(res.message.count / 20),//总页数
                visiblePages: 5,//分多少页
                currentPage: parseInt(page),//当前页数
                first: '<li class="first"><a href="javascript:void(0);">首<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                onPageChange: function (n) { //回调函数
                    if (n != page) {
                        page = n;
                        publisher.getjiaoList(dictionaryId, page);
                    }
                }
            });
        })
    }
    publisher.getdel = function (par) {
        common.getData("/teacher/dictionary/remove.do", par, function (res) {
        })
        publisher.getjiaoList($(".active").attr("ids"), 1)
    }
    publisher.getBanDelete = function (par) {
        common.getData("/teacher/dictionary/del/teacherDri.do", par, function (res) {
            publisher.getBanList(1);
            publisher.getjiaoList($(".active").attr("ids"), 1)
        })
    }
    publisher.loadType = function (grade, i) {
        var param = {};
        param.schoolType = $("#schoolType").val();
        param.subject = grade
        common.getData("/cloud/infoss.do", param, function (res) {
            if (i == 0) {
                common.render({tmpl: $('#cloudLesson_grade'), data: res.grade, context: '#grade'});
            }
            common.render({tmpl: $('#cloudLesson_grade'), data: res.subject, context: '#subject'});
        })
    }

    publisher.loadTypeDh = function (grade, i) {
        var param = {};
        param.schoolType = $("#xzxd").val();
        if (param.schoolType == -1) {
            param.schoolType = 2;
        }
        param.subject = grade
        common.getData("/cloud/infoss.do", param, function (res) {
            if (i == 0) {
                common.render({tmpl: $('#cloudLesson_grade'), data: res.grade, context: '#qbnj'});
            }
            common.render({tmpl: $('#cloudLesson_grade'), data: res.subject, context: '#qbxk'});
        })
    }
    publisher.getBanList = function (page) {
        $("#cbs").empty();
        $("<dt><h3 class='fl'>出版社</h3><span class='fr add-btn add-publish'>添加出版社</span></dt>").appendTo("#cbs");
        var p = {};
        p.type = 1;
        p.page = page;
        p.parentId = null;
        if ($("#cbname").val() != "") {
            p.name = $("#cbname").val();
        }
        p.psize=10;
        common.getData("/teacher/dictionary/list.do", p, function (res) {
            common.render({tmpl: $('#publisher'), data: res.message.list, context: '#cbs'});
            $("#cbs dd:eq(0)").addClass("active")
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
                        publisher.getBanList(page);
                        publisher.getjiaoList($("#cbs dd:eq(0)").attr("ids"), 1)
                    }
                }
            });
        })
    }
    module.exports = publisher;
})