/**
 * Created by albin on 2018/3/19.
 */
define('pkTableAll', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var pkTableAll = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    Common = require('common');
    var l3 = 1;
    var l2 = 1;
    var l1 = 1;
    var xqid = ""
    var zhenXqid = ""
    var subid = ""
    var paikeData = "";
    var weekArg = new Array("周一", "周二", "周三", "周四", "周五", "周六", "周日");
    var wk = 1;

    var desc = "";
    pkTableAll.getDesc = function () {
        var par = {};
        par.ciId = xqid;
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

    pkTableAll.initDesc = function(){
        $('.citt').each(function (i,st) {
            if(($(this).text() == null || $(this).text().trim() == "")){
                $(this).text(desc);
            }
        });
    }

    pkTableAll.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            xqid = rep.message.paikeci;
            zhenXqid=rep.message.xqid;
            $("#defaultTerm").attr("ids", rep.message.paikeci);
        });
    }
    pkTableAll.GetPositionsByWeek = function () {
        Common.getData('/paike/GetPositionsByWeek.do', {
            "xqid": zhenXqid,
            "gid": $(".gaozhong .active").attr("ids"),
            "week": wk
        }, function (rep) {
            if (rep.message > 0) {
                $(".tag .b8").show();
                $(".tag .b7").hide();
            } else {
                $(".tag .b7").show();
                $(".tag .b8").hide();
            }
        })
    }
    pkTableAll.getListByXq = function () {
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": zhenXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '.g-dl .sel2', overwrite: 1});
            wk = rep.length;
            var it = 0;
            $(".jieshu option").each(function (is, ot) {
                it = is;
            });
            $(".jieshu option:eq(" + it + ")").attr("selected", true);
        })
    }
    pkTableAll.init = function () {
        pkTableAll.getDefaultTerm();
        pkTableAll.getListByXq();
        $("#jxbType").change(function () {
            pkTableAll.getGradeSubjectList();
        });
        $(".queding").click(function () {
            var par = {};
            par.gid = $(".gaozhong .active").attr("ids");
            par.nid = $("#uu .bul-cur").attr("ids");
            if (par.nid != "" && par.nid != undefined) {
                Common.getData('/paike/getDtoByNid.do', par, function (rep) {
                })
                pkTableAll.getKeBiaoList();
                pkTableAll.getGradeSubjectList();
                pkTableAll.getkpjxb(subid);
                $('.bg').hide();
                $('.bt-popup').hide();
                var len = ($('.zhou label').length - 1) * 100;
                $('th>div').css('width', len + 'px');
            }
        })


        $('.qxx,.close').click(function () {
            $('.bg').hide();
            $('.bf-popup').hide();
            $('.bt-popup').hide();
        })
        $('body').on('click', '.bul li', function () {
            $(this).addClass('bul-cur').siblings().removeClass('bul-cur')
        })
        $(document).ready(function () {
            // var len = $('.weeks th div div').length;
            var len = ($('.zhou label').length - 1) * 100;
            $('th>div').css('width', len + 'px');
        })
        $("body").on("click", ".zhou label", function () {
            if ($(this).hasClass("all")) {
                $(this).addClass('active').siblings('label').removeClass('active');
            } else {
                $('.zhou .all').removeClass('active');
                if ($(this).hasClass("active")) {
                    $(this).removeClass('active');
                } else {
                    $(this).addClass('active');
                }
            }
            var jln = $(this).attr('js');
            var cln = $(this).attr('class');
            var wd = $('.tab2 th>div').width();
            var choose = false;
            $(".zhou label").each(function (i) {
                if ($(this).hasClass("active")) {
                    choose = true;
                }
            })
            if (!choose) {
                jln = 'all';
                z
            }
            $(".zhou label").each(function (i) {
                if (jln == 'all' && $(this).hasClass("all")) {
                    $(this).addClass('active').siblings('label').removeClass('active');
                }
            })
            if (jln == 'all') {
                $('.zhou>div').show();
                $('.gao>div').show();
                var len = ($('.zhou label').length - 1) * 100;
                $('th>div').css('width', len + 'px');
                l2 = 1
            } else {
                if (l2 == 1) {
                    $('tr td div.' + jln).show().siblings('tr td div').hide();
                    $('tr th div.' + jln).show().siblings('tr th div').hide();
                    $('th>div').css('width', '100px');
                    $('tr>td').css('width', '100px');
                    l2 = l2 + 1
                } else {
                    if (cln == 'active') {
                        $('tr td div.' + jln).show();
                        $('tr th div.' + jln).show();
                        $('tr th:first-child').show();
                        $('th>div').css('width', wd + 100)
                    } else {
                        $('tr td div.' + jln).hide();
                        $('tr th div.' + jln).hide();
                        $('tr th:first-child').show();
                        $('th>div').css('width', wd - 100)
                    }
                }
            }
            pkTableAll.getKeBiaoList(2);
        });

        $(".tag .b6").click(function () {
            var par = {};
            par.gid = $(".gaozhong .active").attr("ids");
            layer.prompt({
                formType: 0,
                value: '',
                title: '请输入名字！'
            }, function (value, index, elem) {
                if (value.trim() != "") {
                    par.name = value;
                    layer.close(index);
                    Common.getData('/paike/MingMingKeBiao.do', par, function (rep) {
                        layer.msg("保存成功");
                    })
                } else {
                    layer.msg("请输入名字");
                }
            });
        })
        $(".tag .b4").click(function () {
            $('.bg').show();
            $('.bt-popup').show();
            var par = {};
            par.gid = $(".gaozhong .active").attr("ids");
            Common.getData('/paike/getDtoType.do', par, function (rep) {
                Common.render({tmpl: $('#uu_temp'), data: rep.message, context: '#uu', overwrite: 1});
            })
        })
        $(".tag .b7").click(function () {
            $('.bg').show();
            $('.bf-popup').show();
            $(".stff").attr("iz", 1);
        })
        $(".tag .b8").click(function () {
            $('.bg').show();
            $('.bf-popup').show();
            $(".stff").attr("iz", 2);
        })
        $(".stff").click(function () {
            var par = {};
            par.staWeek = $(".kaishi").val();
            par.endWeek = $(".jieshu").val();
            par.gid = $(".gaozhong .active").attr("ids");
            par.cid=xqid;
            if ($(this).attr("iz") == 1) {
                Common.getData('/paike/faBuKeBiao.do', par, function (rep) {
                    layer.msg("发布成功");
                    pkTableAll.GetPositionsByWeek();
                })
            } else {
                Common.getData('/paike/delZhouKeBiao.do', par, function (rep) {
                    layer.msg("取消成功");
                    pkTableAll.GetPositionsByWeek();
                })
            }
            $('.bg').hide();
            $('.bf-popup').hide();
        })
        $(".b5").click(function () {
            var par = {};
            par.name = "*";
            par.gid = $(".gaozhong .active").attr("ids");
            Common.getData('/paike/MingMingKeBiao.do', par, function (rep) {
                layer.msg("保存成功");
            })
        })
        $(".b1").click(function () {
            if ($('.tab2 .ke div').attr('draggable') == 'true') {
                $(this).text('编辑');
                $('.tab2 .ke div').attr('draggable', false);
                $('.kpjxblist li').attr('draggable', false);
            } else {
                $(this).text('保存');
                $('.tab2 .ke div').attr('draggable', true);
                $('.kpjxblist li').attr('draggable', true);
            }
        })
        $(".b2").click(function () {
            if ($('.actv') == null || $('.actv').length == 0) {
                layer.alert("请选择撤销课！");
            } else {
                var ykbIds = "";
                $('.actv').each(function (i) {
                    ykbIds += $(this).parent().attr('ykbid') + ",";
                });
                pkTableAll.undoYKB(ykbIds);
            }
            var len = ($('.zhou label').length - 1) * 100;
            $('th>div').css('width', len + 'px');
        });

        $(".b3").click(function () {
            layer.confirm('确认清空？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('清空中。。。', {time: 1000});
                pkTableAll.clearKB();
                var len = ($('.zhou label').length - 1) * 100;
                $('th>div').css('width', len + 'px');
            }, function () {
            });
        })
        $('.b9').click(function () {
            var gradeId = "";
            $('.gaozhong label').each(function (i) {
                if ($(this).hasClass("active")) {
                    gradeId = $(this).attr('ids');
                }
            })
            window.location.href = "/paike/exportPKData.do?gradeId=" + gradeId + "&classRoomIds=&weeks=&indexs=";
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
            var ind = $(this).index();
            var jln = $(this).attr('js');
            var cln = $(this).attr('class');
            var choose = false;
            $(".ke label").each(function (i) {
                if ($(this).hasClass("active")) {
                    choose = true;
                }
            })
            if (!choose) {
                jln = 'all';
            }
            $(".ke label").each(function (i) {
                if (jln == 'all' && $(this).hasClass("all")) {
                    $(this).addClass('active').siblings('label').removeClass('active');
                }
            })

            if (jln == 'all') {
                $('.ke>div').show();
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
            pkTableAll.getKeBiaoList(2);
        });
        pkTableAll.getGradeList();
        pkTableAll.getGradeWeekRangeByXqid();
        pkTableAll.GetPositionsByWeek();
        pkTableAll.getListBySchoolId();
        pkTableAll.getRoomEntryListByXqGrade();
        pkTableAll.getKeBiaoList();
        pkTableAll.getGradeSubjectList();
        // pkTableAll.getDesc();
        $("body").on("click", ".subject li", function () {
            $('#subjectId').val($(this).attr('subjectId'));
            subid = $(this).attr('subjectId');
            pkTableAll.getkpjxb($(this).attr('subjectId'));
        });
        $('.gaozhong label').click(function () {
            $('.gaozhong label').each(function (i) {
                $(this).removeClass('active');
            })
            $(this).addClass('active');
            pkTableAll.GetPositionsByWeek();
            pkTableAll.getRoomEntryListByXqGrade();
            pkTableAll.getKeBiaoList();
            pkTableAll.getGradeSubjectList();
        })
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
            var choose = false;
            $(".jiaoshi label").each(function (i) {
                if ($(this).hasClass("active")) {
                    choose = true;
                }
            })
            if (!choose) {
                jln = 'all';
            }
            $(".jiaoshi label").each(function (i) {
                if (jln == 'all' && $(this).hasClass("all")) {
                    $(this).addClass('active').siblings('label').removeClass('active');
                }
            })
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
            pkTableAll.getKeBiaoList(2);
        });

        //收起移动窗
        $('.fixedcont .info .p1 em').click(function () {
            $('.fixedcont .cont,#movebg').addClass('h0');
            $(this).hide();
            $('#move').css('width', '110px');
            $('.fixedcont .info .p2').hide();
            $('.fixedcont .info .p3').show();
        });
        //展开移动窗
        $('.fixedcont .info .p3').click(function () {
            $('.fixedcont .cont,#movebg').removeClass('h0');
            $(this).hide();
            $('#move').css('width', '220px');
            $('.fixedcont .info .p2').show();
            $('.fixedcont .info .p1 em').show();
        });

        //点击课表表格
        $('body').on('click', '.tab2 td>div>div>div', function () {
            if ($(this).parent().hasClass('red')) {

            } else {
                if ($(this).hasClass('actv')) {
                    $(this).removeClass('actv');
                } else {
                    $(this).addClass('actv');
                }
                $(this).toggleClass('active');


            }
        });

        //点击教师名 高亮其所有课程
        $('body').on('click', '.tab2 div>p:nth-child(2)', function () {
            var jsn = $(this).attr('js');
            var cln = $(this).attr('class');
            if ($(this).hasClass('active')) {
                $(this).removeClass('active')
                $('.tab2 div.' + jsn).removeClass('active')
            } else {
                $(this).addClass('active')
                $('.tab2 div.' + jsn).addClass('active')
            }
            return false;
        })
        //拖拽窗口 学科切换
        $('.fixedcont .lt span').click(function () {
            $(this).addClass('active').siblings('li').removeClass('active');
            $('.fixedcont .ul1').hide();
            $('.fixedcont .ul2').show();
        });
        //拖拽窗口 学科切换
        $('.fixedcont .ul2 li').click(function () {
            $(this).addClass('active').siblings('li').removeClass('active');
            $('.fixedcont .ul1').show();
            $('.fixedcont .ul2').hide();
        });
    }
    pkTableAll.getListBySchoolId = function () {
        if (xqid && xqid != null) {
            Common.getData('/courseset/getListBySchoolId.do', {"xqid": xqid}, function (rep) {
                $(".ke").append(" <span>课节 : </span>")
                $(".ke").append(" <label class='active all' js='all' ids='0'>全部</label>")
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '.ke'});
            });
        }
    }

    pkTableAll.getGradeWeekRangeByXqid = function () {
        if (xqid && xqid != null) {
            var par = {};
            var gradeId = "";
            $('.gaozhong label').each(function (i) {
                if ($(this).hasClass("active")) {
                    gradeId = $(this).attr('ids');
                }
            })
            par.gid = gradeId;
            par.xqid = xqid;
            Common.getData('/n33_gradeweekrange/getGradeWeekRangeByXqid.do', par, function (rep) {
                $(".zhou").empty();
                $(".zhou").append("<span>教学日 : </span><label class='active all' js='all' id='0'>全部</label>");
                if (rep != null) {
                    for (var i = rep.start; i <= rep.end; i++) {
                        var name = weekArg[i - 1];
                        $(".zhou").append("<label id='" + i + "' js='zh" + i + "'>" + name + "</label>");
                    }
                }
            });
        }
    }


    pkTableAll.getRoomEntryListByXqGrade = function () {
        if (xqid && xqid != null) {
            var par = {};
            var gradeId = "";
            $('.gaozhong label').each(function (i) {
                if ($(this).hasClass("active")) {
                    gradeId = $(this).attr('ids');
                }
            })
            par.gradeId = gradeId;
            par.xqid = xqid;
            Common.getData('/paike/getRoomEntryListByXqGrade.do', par, function (rep) {
                $(".jiaoshi").empty();
                $(".jiaoshi").append(" <span>教室 : </span>")
                Common.render({tmpl: $('#jiaos'), data: rep.message, context: '.jiaoshi'});
            });
        }
    }

    pkTableAll.undoYKB = function (ykbIds) {
        var par = {};
        par.ykbIds = ykbIds;
        Common.getData('/paike/undoYKB.do', par, function (rep) {
            if (rep.code == '200') {
                layer.msg("撤销成功！");
                pkTableAll.getKeBiaoList();
                pkTableAll.getkpjxb(subid);
            } else {
                layer.msg("撤销失败！")
            }
        });
    }

    pkTableAll.clearKB = function () {
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.xqcid = xqid;
        Common.getData('/paike/clearKB.do', par, function (rep) {
            if (rep.code == '200') {
                layer.msg("清除成功！");
                pkTableAll.getKeBiaoList(1);
            }
        });
    }

    pkTableAll.getGradeList = function () {
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
            $(".gaozhong").append(" <span>年级 : </span>")
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.gaozhong'});
            $(".gaozhong label:eq(0)").addClass("active");
        })
    }

    pkTableAll.getKeBiaoList = function (type) {
        // pkTableAll.getDesc();
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        data.gradeId = gradeId;
        var classRoomIds = "";
        $('.jiaoshi label').each(function (i) {
            if ($(this).hasClass('active')) {
                classRoomIds += $(this).attr('ids') + ",";
            }
        });
        var zouIds = "";
        $('.zhou label').each(function (i) {
            if ($(this).hasClass('active')) {
                zouIds += $(this).attr('id') + ",";
            }
        });
        var keIds = "";
        $('.kj label').each(function (i) {
            if ($(this).hasClass('active')) {
                keIds += $(this).attr('ids') + ",";
            }
        });
        data.classRoomIds = classRoomIds;
        data.weeks = zouIds;
        data.indexs = keIds;
        data.termId = xqid;
        Common.getData('/paike/getKeBiaoList.do', data, function (rep) {
            if (rep.code == '200') {
                if (type != 2) {
                    $('.kejie').empty();
                    $('.clroms').empty();
                    $('.weeks').empty();
                    Common.render({tmpl: $('#ks_temp'), data: rep.message.courseRangeDTOs, context: '.kejie'});
                    Common.render({tmpl: $('#clroms_temp'), data: rep.message.classrooms, context: '.clroms'});
                    Common.render({tmpl: $('#weeks_temp'), data: rep.message.classrooms, context: '.weeks'});
                }
                $('.jxbkj').empty();
                paikeData = rep.message.ykbdto;
                var crIds = new Array();
                $('.jiaoshi label').each(function (i) {
                    if ($(this).hasClass('active')) {
                        crIds[i] = $(this).attr('ids');
                    }
                });

                var zIds = new Array();
                var zIndex = 0;
                $('.zhou label').each(function (i) {
                    if ($(this).hasClass('active')) {
                        zIds[zIndex] = parseInt($(this).attr('id'));
                        zIndex++;
                    }
                });
                var kIndex = 0;
                var kIds = new Array();
                $('.ke label').each(function (i) {
                    if ($(this).hasClass('active')) {
                        kIds[kIndex] = parseInt($(this).attr('ids'));
                        kIndex++;
                    }
                });
                // if (crIds!=null && crIds.length==1 && crIds[0]=="") {
                //     $(".clroms th").each(function(i,obj){
                //         var array=new Array();
                //         $.each(rep.message.ykbdto,function(index,item){
                //             if($(obj).attr("class")==item[0].classroomId){
                //                 for (var j=0;j<item.length;j++) {
                //                     if ((zIds!=null && zIds.length==1 && zIds[0]==0 && kIds!=null && kIds.length==1 && kIds[0]==0)||
                //                         (zIds[0]!=0 && zIds.indexOf(item[j].x+1)!=-1 && kIds[0]==0)||(zIds[0]==0 && kIds[0]!=0 && kIds.indexOf(item[j].y+1)!=-1)||
                //                         (zIds[0]!=0 && zIds.indexOf(item[j].x+1)!=-1 && kIds[0]!=0 && kIds.indexOf(item[j].y+1)!=-1)) {
                //                         array.push(item[j]);
                //                     }
                //                 }
                //             }
                //         })
                //         Common.render({tmpl: $('#jxbkj_temp'), data: array, context: '.jxbkj'});
                //     })
                // } else {
                //     for (var i = 0;i < crIds.length; i++) {
                //         var array=new Array();
                //         $.each(rep.message.ykbdto,function(index,item){
                //             if(crIds[i]==item[0].classroomId){
                //                 for (var j=0;j<item.length;j++) {
                //                     if ((zIds!=null && zIds.length==1 && zIds[0]==0 && kIds!=null && kIds.length==1 && kIds[0]==0)||
                //                         (zIds[0]!=0 && zIds.indexOf(item[j].x+1)!=-1 && kIds[0]==0)||(zIds[0]==0 && kIds[0]!=0 && kIds.indexOf(item[j].y+1)!=-1)||
                //                         (zIds[0]!=0 && zIds.indexOf(item[j].x+1)!=-1 && kIds[0]!=0 && kIds.indexOf(item[j].y+1)!=-1)) {
                //                         array.push(item[j]);
                //                     }
                //                 }
                //
                //             }
                //         })
                //         Common.render({tmpl: $('#jxbkj_temp'), data: array, context: '.jxbkj'});
                //     }
                // }
                if (crIds != null && crIds.length == 1 && crIds[0] == "") {
                    $(".clroms th").each(function (i, obj) {
                        var array = new Array();
                        $.each(rep.message.ykbdto, function (index, item) {
                            if (item != null && $(obj).attr("class") == item[0].classroomId) {
                                array.push(item);
                            }
                        })

                        Common.render({tmpl: $('#jxbkj_temp'), data: array, context: '.jxbkj'});
                    })
                } else {
                    for (var i = 0; i < crIds.length; i++) {
                        var array = new Array();
                        $.each(rep.message.ykbdto, function (index, item) {
                            if (item != null && crIds[i] == item[0].classroomId) {
                                array.push(item);
                            }
                        })
                        if (array != null && array.length != 0) {
                            Common.render({tmpl: $('#jxbkj_temp'), data: array, context: '.jxbkj'});
                        }
                    }
                }
            } else {

            }
        })
        // pkTableAll.initDesc();
    }

    pkTableAll.showTable = function () {
        $('.jxbkj').empty();
        $(".clroms th").each(function (i, obj) {
            var array = new Array();
            $.each(paikeData, function (index, item) {
                if ($(obj).attr("class") == item[0].classroomId) {
                    array.push(item);
                }
            })
            Common.render({tmpl: $('#jxbkj_temp'), data: array, context: '.jxbkj'});
        })
    }

    pkTableAll.getkpjxb = function (subjectId) {
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.subjectId = subjectId;
        par.type = $('#jxbType').val();
        par.termId = xqid;
        Common.getData('/paike/getkpjxb.do', par, function (rep) {
            if (rep.code == '200') {
                $('.subject').hide();
                $('.kpjxblist').show();
                $('.kpjxblist').empty();
                Common.render({tmpl: $('#kpjxblistTemp'), data: rep.message, context: '.kpjxblist'});
            }

        })
    }

    pkTableAll.getGradeSubjectList = function () {
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        data.gid = gradeId;
        data.xqid = xqid;
        data.type = $('#jxbType').val();
        Common.getData('/new33isolateMange/getSubjectByGradeIdType.do', data, function (rep) {
            $('.subject').show();
            $('.kpjxblist').hide();
            $('.subject').empty();
            Common.render({tmpl: $('#subjectTrScript'), data: rep.message, context: '.subject'});
        })
    }

    /**
     * 教学班冲突的格子
     */
        // pkTableAll.getConflictedSettledJXB = function(jxbId) {
        //     var data = {};
        //     data.jxbId = jxbId;
        //     Common.getData('/paike/getConflictedSettledJXB.do',data,function(rep) {
        //         if (rep.code == '200') {
        //
        //         }
        //     });
        // }
    module.exports = pkTableAll;
})