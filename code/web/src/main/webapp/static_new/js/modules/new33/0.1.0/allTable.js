/**
 * Created by albin on 2018/3/19.
 */
define('allTable', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var allTable = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    Common = require('common');
    var l3 = 1;
    var l1 = 1;
    var deXqid = "";

    var deCid = "";
    var classRoomList = new Array();
    var jxrList = new Array();
    var kjList = new Array();
    var gradeList = new Array();
    var weekList = new Array();
    var param = {};

    var ciId = null;

    var desc = "";
    allTable.getDesc = function () {
        var par = {};
        par.ciId = ciId;
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.type = 3;
        par.kbType = 1;
        Common.getData('/n33_set/getDESCForNoCourse.do', par, function (rep) {
            desc = rep.message;
        });
    }

    allTable.initDesc = function(){
        $('.citt').each(function (i,st) {
            if(($(this).text() == null || $(this).text().trim() == "") && !$(this).hasClass("gray")){
                $(this).text(desc);
            }
        });
    }

    allTable.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getGradeWeek.do', {"xqid" : deXqid}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial + 1;
                var index2 = rep.serial + 2;
                $(".zh"+index).find("div").addClass("gray")
                $(".zh"+index2).find("div").addClass("gray")
            }
        });
        allTable.initDesc();
    }
    allTable.init = function () {
        $("#week").change(function () {
            allTable.getKeBiaoList();
            allTable.getZhuanXiangZuHeXianQing();
            allTable.getCurrentJXZ();
        })
        allTable.getDefaultTerm();
        allTable.getListByXq();
        allTable.getGradeList();
        allTable.getListBySchoolId();
        allTable.getRoomEntryListByXqGrade();
        allTable.getKeBiaoList();
        allTable.getZhuanXiangZuHeXianQing();
        allTable.getCurrentJXZ();
        var len = $('.jxbkj .gao:eq(0) .ke:eq(0)>div').length;
        $('.kejie>div').length = len;
        $('.kejie>div').each(function(){
            var count = $(this).attr('count')
            if(count>len){
                $(this).hide();
            }
        })

        $('.gaozhong label').click(function () {
            $('.gaozhong label').each(function (i) {
                $(this).removeClass('active');
            })
            $(this).addClass('active');
            allTable.getRoomEntryListByXqGrade();
            allTable.getKeBiaoList();
            allTable.getZhuanXiangZuHeXianQing();
            allTable.getCurrentJXZ();
            var len = $('.jxbkj .gao:eq(0) .ke:eq(0)>div').length;
            $('.kejie>div').length = len;
            $('.kejie>div').each(function(){
                var count = $(this).attr('count')
                if(count>len){
                    $(this).hide();
                }
            })
        })

        $("body").on("click", ".b9", function () {
            var flag = isCanExport();
            if (!flag) {
                layer.alert("该周课表未发布，不允许导出课表")
                return;
            }
            allTable.exportZKB();
            $("#export_form").attr("action", "/paike/exportZKBData.do");
            $("#export_form").html("");
            $("#export_form").append("<input  type='hidden' name='dataMap' +  value='" + JSON.stringify(param) + "'/>");
            $("#export_form").submit();
        });
        $(document).ready(function () {
            var len = ($('.zhou label').length - 1) * 100;
            $('th>div').css('width', len + 'px');
        })
        $("body").on("click", ".ke label", function () {
            if ($(this).hasClass("all")) {
                $(this).addClass('active').siblings('label').removeClass('active');
            } else {
                $('.ke .all').removeClass('active');
                if ($(this).hasClass("active")) {
                    $(this).removeClass('active');
                } else {
                    $(this).addClass('active');
                }
            }

            var jln = $(this).attr('js');
            var cln = $(this).attr('class');
            if (jln == 'all') {
                $('.ke>div').show();
                var len = ($('.zhou label').length - 1) * 100;
                $('th>div').css('width', len + 'px');
                l3 = 1;
            } else {
                if (l3 == 1) {
                    $('.ke>div.' + jln).show().siblings('.ke>div').hide();
                    l3 = l3 + 1
                } else {
                    if (cln == 'active') {
                        $('.ke>div.' + jln).show();
                    } else {
                        $('.ke>div.' + jln).hide();
                    }
                }
            }
        });

        $("body").on("click", ".jiaoshi label", function () {
            if ($(this).hasClass("all")) {
                $(this).addClass('active').siblings('label').removeClass('active');
            } else {
                $('.jiaoshi .all').removeClass('active');
                if ($(this).hasClass("active")) {
                    $(this).removeClass('active');
                } else {
                    $(this).addClass('active');
                }
            }

            var jln = $(this).attr('js');
            var cln = $(this).attr('class');
            if (jln == 'all') {
                $('tr td').show();
                $('tr th').show();
                l1 = 1;
            } else {
                if (l1 == 1) {
                    if (cln == 'active') {
                        $('tr td.' + jln).show().siblings('tr td').hide();
                        $('tr th.' + jln).show().siblings('tr th').hide();
                        l1 = l1 + 1
                    }
                } else {
                    if (cln == 'active') {
                        $('tr td.' + jln).show();
                        $('tr th.' + jln).show();
                    } else {
                        $('tr td.' + jln).hide();
                        $('tr th.' + jln).hide();
                    }
                }
            }
        });
    }

    allTable.exportZKB = function () {
        classRoomList = new Array();
        jxrList = new Array();
        kjList = new Array();
        gradeList = new Array();
        weekList = new Array();
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
                gradeList.push(gradeId);
            }
        })
        param.gradeList = gradeList;

        var par = {};
        weekList.push($("#week").val());
        param.weekList = weekList;

        if ($(".jiaoshi .active").attr("js") == 'all') {
            $(".jiaoshi label").each(function () {
                if ($(this).attr("js") != 'all') {
                    classRoomList.push($(this).attr("ids"));
                }
            });
        } else {
            $(".jiaoshi .active").each(function () {
                classRoomList.push($(this).attr("ids"));
            });
        }
        param.classRoomList = classRoomList;

        if ($(".jxr .active").attr("js") == 'all') {
            $(".jxr label").each(function () {
                if ($(this).attr("js") != 'all') {
                    jxrList.push($(this).text());
                }
            });
        } else {
            $(".jxr .active").each(function () {
                jxrList.push($(this).text());
            });
        }
        param.jxrList = jxrList;

        if ($(".kj .active").attr("js") == 'all') {
            $(".kj label").each(function () {
                if ($(this).attr("js") != 'all') {
                    kjList.push($(this).attr("ids"));
                }
            });
        } else {
            $(".kj .active").each(function () {
                kjList.push($(this).attr("ids"));
            });
        }
        param.kjList = kjList;

    }

    allTable.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.xqid;
            deCid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }
    allTable.getListBySchoolId = function () {
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdZKB.do', {"xqid": deXqid}, function (rep) {
                $(".kj").append(" <span>课节 : </span>")
                $(".kj").append(" <label class='active all' js='all'>全部</label>")
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '.kj'});
            });
        }
    }

    allTable.getRoomEntryListByXqGrade = function () {
        $(".jiaoshi").empty();
        if (deXqid && deXqid != null) {
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $(".gaozhong .active").attr("ids");
            Common.getData('/paike/getRoomEntryListByXqGradeAndTerm.do', par, function (rep) {
                $(".jiaoshi").append(" <span>教室 : </span>")
                Common.render({tmpl: $('#jiaos'), data: rep.message, context: '.jiaoshi'});
            });
        }
    }

    allTable.getGradeList = function () {
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": deXqid}, function (rep) {
            $(".gaozhong").append(" <span>年级 : </span>")
            Common.render({tmpl: $('#grade_temp'), data: rep, context: '.gaozhong'});
            $(".gaozhong label:eq(0)").addClass("active");
        })
    }
    allTable.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        })
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            $("#week option[value=" + wk + "]").prop("selected", true)
        })
    }

    function isCanExport() {
        var flag = null;
        var par = {};
        par.week = $("#week").val();
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.xqid = deXqid;
        Common.getData('/paike/isCanExport.do', par, function (rep) {
            flag = rep.message;
        });
        return flag;
    }

    allTable.getZhuanXiangZuHeXianQing=function(){
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $(".gaozhong .active").attr("ids");
        $("#zhuanList").empty();
        if(par.gradeId!=""&&par.gradeId!=undefined) {
            Common.getData('/paike/getZhuanXiangZuHeXianQingAll.do', par, function (rep) {
                Common.render({tmpl: $('#Zhuan'), data: rep.message, context: '#zhuanList', overwrite: 1});
            })
        }
    }
    allTable.getKeBiaoList = function () {
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        data.gradeId = gradeId;
        data.classRoomIds = '';
        data.weeks = '';
        data.indexs = '';
        data.week = $("#week").val();
        $("#kbName").html($(".gaozhong .active").attr("nm"))
        Common.getData('/paike/getKeBiaoListZhou.do', data, function (rep) {
            $(".jxbkj").empty();
            if (rep.code == '200'&&rep.message!=null) {
                // alert(rep.message);
                $('.kejie').empty();
                $('.clroms').empty();
                $('.weeks').empty();
                Common.render({tmpl: $('#ks_temp'), data: rep.message.courseRangeDTOs, context: '.kejie'});
                Common.render({tmpl: $('#clroms_temp'), data: rep.message.classrooms, context: '.clroms'});
                Common.render({tmpl: $('#weeks_temp'), data: rep.message.classrooms, context: '.weeks'});
                $(".clroms th").each(function (i, obj) {
                    var array = new Array();
                    $.each(rep.message.ykbdto, function (index, item) {
                        if ($(obj).attr("class") == item[0].classroomId) {
                            ciId = item[0].cid;
                            array.push(item);
                        }
                    })
                    Common.render({tmpl: $('#jxbkj_temp'), data: array, context: '.jxbkj'});
                })
                var len = ($('.zhou label').length - 1) * 100;
                $('th>div').css('width', len + 'px');
            } else {

            }
        })
        allTable.getDesc();
    }
    module.exports = allTable;
})