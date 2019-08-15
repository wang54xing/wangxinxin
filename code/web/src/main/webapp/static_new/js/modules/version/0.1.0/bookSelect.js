/**
 * Created by albin on 2017/5/10.
 */
define('bookSelect', ['jquery', 'doT', 'common', 'pagination', "layer"], function (require, exports, module) {
    var bookSelect = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    common = require('common');
    bookSelect.init = function () {
        //右侧导航
        bookSelect.loadTypeDh(-1, 0);
        $("#xzxd").change(function () {
            $("#qbnj").empty();
            $("#qbxk").empty();
            $("<option value='-1'>全部学科</option>").appendTo("#qbxk");
            $("<option value='-1'>全部年级</option>").appendTo("#qbnj");
            bookSelect.loadTypeDh(-1, 0);
            bookSelect.getjiaoList($(".active").attr("ids"), 1)
        })
        $("#jss").click(function () {
            bookSelect.getjiaoList($(".active").attr("ids"), 1)
        })
        $("#qbnj").change(function () {
            $("#qbxk").empty();
            $("<option value='-1'>全部学科</option>").appendTo("#qbxk");
            bookSelect.loadTypeDh($(this).val(), 1);
            bookSelect.getjiaoList($(".active").attr("ids"), 1)
        })
        $("#qbxk").change(function () {
            bookSelect.getjiaoList($(".active").attr("ids"), 1)
        })
        $("#cbsj").change(function () {
            bookSelect.getjiaoList($(".active").attr("ids"), 1)
        })
        $("#cbss").click(function () {
            bookSelect.getBanList(1);
        })

        //左侧列表
        $("body").on("click", ".publisher-list>dd", function () {
            $(this).addClass("active").siblings().removeClass("active");
            $("#xzxd option").attr("selected", false);
            $("#xzxd option:eq(0)").attr("selected", true);
            $("#cbsj option").attr("selected", false);
            $("#cbsj option:eq(0)").attr("selected", true);
            $("#qbnj").empty();
            $("#qbxk").empty();
            $("<option value='-1'>全部学科</option>").appendTo("#qbxk");
            $("<option value='-1'>全部年级</option>").appendTo("#qbnj");
            bookSelect.loadTypeDh(-1, 0);
            bookSelect.getjiaoList($(this).attr("ids"), 1);
        })

        $("body").on("click", ".fxkko", function () {
            var par = {};
            par.dictionaryID = $(".active").attr("ids");
            par.cls = $(this).parent().parent().parent().attr("ids");
            if ($(this).is(':checked')) {
                par.zt = 0;
            } else {
                par.zt = 1;
            }
            bookSelect.addBan(par);
        })

        bookSelect.getBanList(1);
        bookSelect.getjiaoList($(".active").attr("ids"), 1)
    }


    bookSelect.addBan = function (par) {
        common.getData("/teacher/dictionary/add/BookSid.do", par, function (res) {
        })
    }
    bookSelect.getjiaoList = function (dictionaryId, page) {
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
        p.pagesize = 20;
        common.getData("/teacher/dictionary/book/list.do", p, function (res) {
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
                        bookSelect.getjiaoList(dictionaryId, page);
                    }
                }
            });
        })
    }
    bookSelect.loadType = function (grade, i) {
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

    bookSelect.loadTypeDh = function (grade, i) {
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
    bookSelect.getBanList = function (page) {
        $("#cbs").empty();
        $("<dt><h3 class='fl'>出版社</h3></dt>").appendTo("#cbs");
        var p = {};
        p.type = 1;
        p.page = page;
        p.parentId = null;
        if ($("#cbname").val() != "") {
            p.name = $("#cbname").val();
        }
        p.psize = 10;
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
                        bookSelect.getBanList(page);
                        bookSelect.getjiaoList($("#cbs dd:eq(0)").attr("ids"), 1)
                    }
                }
            });
        })
    }
    module.exports = bookSelect;
})