'use strict';
define(['jquery','doT','easing','common',"layer"],function(require,exports,module){
    (function ($) {
        var ms = {
            init: function (obj, args) {
                return (function () {
                    ms.fillHtml(obj, args);
                    ms.bindEvent(obj, args);
                })();
            },
            //填充html
            fillHtml: function (obj, args) {
                return (function () {
                    obj.empty();
                    //上一页
                    if (args.current > 1) {
                        obj.append('<a href="javascript:;" class="prevPage"><上一页</a>');
                    } else {
                        obj.remove('.prevPage');
                        obj.append('<span class="disabled"><上一页</span>');
                    }
                    //中间页码
                    if (args.current != 1 && args.current >= 4 && args.pageCount != 4) {
                        obj.append('<a href="javascript:;" class="tcdNumber">' + 1 + '</a>');
                    }
                    if (args.current - 2 > 2 && args.current <= args.pageCount && args.pageCount > 5) {
                        obj.append('<span>...</span>');
                    }
                    var start = args.current - 2, end = args.current + 2;
                    if ((start > 1 && args.current < 4) || args.current == 1) {
                        end++;
                    }
                    if (args.current > args.pageCount - 4 && args.current >= args.pageCount) {
                        start--;
                    }
                    for (; start <= end; start++) {
                        if (start <= args.pageCount && start >= 1) {
                            if (start != args.current) {
                                obj.append('<a href="javascript:;" class="tcdNumber">' + start + '</a>');
                            } else {
                                obj.append('<span class="current">' + start + '</span>');
                            }
                        }
                    }
                    if (args.current + 2 < args.pageCount - 1 && args.current >= 1 && args.pageCount > 5) {
                        obj.append('<span>...</span>');
                    }
                    if (args.current != args.pageCount && args.current < args.pageCount - 2 && args.pageCount != 4) {
                        obj.append('<a href="javascript:;" class="tcdNumber">' + args.pageCount + '</a>');
                    }
                    //下一页
                    if (args.current < args.pageCount) {
                        obj.append('<a href="javascript:;" class="nextPage">下一页></a>');
                    } else {
                        obj.remove('.nextPage');
                        obj.append('<span class="disabled">下一页></span>');
                    }
                    //跳转页码
                    if (args.turndown == 'true') {
                        obj.append('<span class="countYe">到第<input type="text" maxlength=' + args.pageCount.toString().length + '>页<a href="javascript:;" class="turndown">确定</a><span>');
                    }
                })();
            },
            //绑定事件
            bindEvent: function (obj, args) {
                return (function () {
                    obj.off("click", "a.tcdNumber");
                    obj.one("click", "a.tcdNumber", function () {
                        var current = parseInt($(this).text());
                        ms.fillHtml(obj, {"current": current, "pageCount": args.pageCount, "turndown": args.turndown});
                        if (typeof(args.backFn) == "function") {
                            args.backFn(current);
                        }
                    });
                    //上一页
                    obj.off("click", "a.prevPage");
                    obj.one("click", "a.prevPage", function () {
                        var current = parseInt(obj.children("span.current").text());
                        ms.fillHtml(obj, {
                            "current": current - 1,
                            "pageCount": args.pageCount,
                            "turndown": args.turndown
                        });
                        if (typeof(args.backFn) == "function") {
                            args.backFn(current - 1);
                        }
                    });
                    //下一页
                    obj.off("click", "a.nextPage");
                    obj.one("click", "a.nextPage", function () {
                        var current = parseInt(obj.children("span.current").text());
                        ms.fillHtml(obj, {
                            "current": current + 1,
                            "pageCount": args.pageCount,
                            "turndown": args.turndown
                        });
                        if (typeof(args.backFn) == "function") {
                            args.backFn(current + 1);
                        }
                    });
                    //跳转
                    obj.one("click", "a.turndown", function () {
                        var page = $("span.countYe input").val();
                        if (page > args.pageCount) {
                            layer.alert("您的输入有误，请重新输入！");
                        }
                        ms.fillHtml(obj, {"current": page, "pageCount": args.pageCount, "turndown": args.turndown});
                        /*if(typeof(args.backFn)=="function"){
                         args.backFn(current+1);
                         }*/
                    });
                })();
            }
        }
        $.fn.createPage = function (options) {
            var args = $.extend({
                pageCount: 10,
                current: 1,
                turndown: true,
                backFn: function () {
                }
            }, options);
            ms.init(this, args);
        }
    })(jQuery);
    var termMange = {},
        Common = require('common');
    require('layer');
    var termMangeData = {};
    termMange.init = function() {
        termMangeData.page = 1;
        termMangeData.pageSize = 10;
        //-----------------------学期------------------------------------------
        termMange.getTermList();
        /**********添加学年**************/
        $('.term-top-bu').click(function () {
            $('#termText').text("添加学年");
            $('#termId').val("");
            $("#termStartYear option:first").prop("selected", 'selected');
            $("#termEndYear option:first").prop("selected", 'selected');
            $('#termOSTime').val("");
            $('#termOETime').val("");
            $('#termTSTime').val("");
            $('#termTETime').val("");
            $('.term-add').show();
            $('.bg').show();
        })
        $('.group-qx').click(function () {
            $('.stu-add').hide();
            $('.grade-edit-popup').hide();
            $('.term-add').hide();
            $('.educa-del-popup').hide();
            $('.educa-add-popup').hide()
            $('.bg').hide();
        });

        $('.term-sure').click(function () {
            termMangeData.termName = $('#termNm').val();
            termMangeData.startYear = $('#termStartYear').val();
            termMangeData.endYear = $('#termEndYear').val();
            if ($('#termNm').val()=='') {
                layer.alert("学年名称不能为空！");
                return;
            }
            if ($('#termEndYear').val()-$('#termStartYear').val()!=1) {
                layer.alert("学年开始与结束相差一年！");
                return;
            }
            termMangeData.startDate = $('#termOSTime').val();
            termMangeData.endDate = $('#termOETime').val();
            termMangeData.startDate2 = $('#termTSTime').val();
            termMangeData.endDate2 = $('#termTETime').val();
            if($('#termOSTime').val()==''||$('#termOETime').val()==''||$('#termTSTime').val()==''||$('#termTETime').val()=='') {
                layer.alert('日期不为空！');
                return;
            }
            termMangeData.id = $('#termId').val();
            termMange.addOrUdpTerm();
        });

        $('#termSearch').click(function () {
            termMange.getTermList();
        });
    }


    //----------------学期---------------------------------------------------------------------------------------------------------------------------------
    termMange.addOrUdpTerm = function() {
        Common.getData('/term/addOrUdpTerm.do', termMangeData,function(rep){
            if (rep.code=='200') {
                $('.term-add').hide();
                $('.bg').hide();
                termMange.getTermList();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    termMange.delTerm = function(id) {
        termMangeData.id = id;
        Common.getData('/term/delTerm.do', termMangeData,function(rep){
            if (rep.code=='200') {
                termMange.getTermList();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    termMange.getTerm = function(id) {
        termMangeData.id = id;
        Common.getData('/term/getTerm.do', termMangeData,function(rep){
            if (rep.code=='200') {
                $('#termText').text("编辑学年");
                $('#termId').val(id);
                $('#termNm').val(rep.message.termName);
                $('#termStartYear').val(rep.message.startYear);
                $('#termEndYear').val(rep.message.endYear);
                $('#termOSTime').val(rep.message.startDate);
                $('#termOETime').val(rep.message.endDate);
                $('#termTSTime').val(rep.message.startDate2);
                $('#termTETime').val(rep.message.endDate2);
                $('.term-add').show();
                $('.bg').show();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    termMange.getTermList = function() {
        termMangeData.keyword = $('#termKeyword').val();
        Common.getData('/term/getTermList.do', termMangeData,function(rep){
            if (rep.code=='200') {
                $('.termList').html('');
                Common.render({tmpl:$('#termList_templ'),data:rep,context:'.termList'});
                var totalPage = 0;
                if (rep.message.total % rep.message.pageSize == 0) {
                    totalPage = rep.message.total / rep.message.pageSize;
                } else {
                    totalPage = parseInt(rep.message.total / rep.message.pageSize) + 1;
                }
                $(".pageDiv").createPage({
                    pageCount:totalPage,//总页数
                    current:rep.message.page,//当前页
                    turndown:'false',//是否显示跳转框，显示为true，不现实为false,一定记得加上引号...
                    backFn:function(p){
                        if (termMangeData.page!=p) {
                            termMangeData.page = p;
                            termMange.getTermList();
                        }

                    }
                });
                $('.term-edit').click(function() {
                    termMange.getTerm($(this).attr('tid'));
                });
                $('.term-del').click(function() {
                    if (confirm('确认删除！')) {
                        termMange.delTerm($(this).attr('tid'));
                    }
                });
            } else {
                layer.alert(rep.message);
            }
        });
    }

    termMange.init();
});