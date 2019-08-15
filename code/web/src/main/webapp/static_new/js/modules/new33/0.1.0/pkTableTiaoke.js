/**
 * Created by albin on 2018/3/19.
 */
define('pkTableTiaoke', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var pkTableTiaoke = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    Common = require('common');
    var xqid = ""
    var zhenXqid = ""
    var wk = 1;
    var zlength = 5;
    var paikeData = "";
    var pkflg = ""
    pkTableTiaoke.getSubjectList = function () {
        var gid = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gid = $(this).attr('ids');
            }
        })
        Common.getData('/new33isolateMange/getIsolateSubjectListByXqAndGrade.do', {
            "xqid": xqid,
            "gid": gid
        }, function (rep) {
            $("#sub").empty();
            if ($("#classroom").is(":checked") || $("#tag").is(":checked")) {
                $("#sub").hide();
            } else if ($("#teacher").is(":checked")) {
                $("#sub").show();
                $("#sub").append(" <span>学科 : </span>")
            }

            Common.render({tmpl: $('#subject_temp'), data: rep.message, context: '#sub'});
            $("#sub label:eq(0)").addClass("active");
        })
    }
    pkTableTiaoke.getTeacherList = function (type) {
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        var subId = "";
        $('#sub label').each(function (i) {
            if ($(this).hasClass("active")) {
                subId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.xqid = xqid;
        par.subId = subId;
        var tid = "";
        if (type == 1) {
            tid = $(".classrooms .t-li").attr("ids");
        }
        if (par.gradeId != null && par.gradeId != "" && par.gradeId != undefined && par.xqid != "" && par.xqid != undefined && par.xqid != null && par.subId != undefined && par.subId != "" && par.subId != null) {
            Common.getData('/new33isolateMange/getTeaAndSubjectByGradeId1.do', par, function (rep) {
                $(".classrooms").empty();
                Common.render({tmpl: $('#teacher_templ'), data: rep.message, context: '.classrooms'});
                if (type == 1) {
                    $(".classrooms li").each(function () {
                        if ($(this).attr("ids") == tid) {
                            $(this).addClass("t-li");
                        }
                    });
                } else {
                    $(".classrooms li:eq(0)").addClass("t-li");
                    $('#clsrom').text($(".classrooms li:eq(0)").find("span").attr("tname"));
                }
                // var ypks = $(".classrooms .t-li").find("span").attr("ypks");
                // var ks = $(".classrooms .t-li").find("span").attr("ks");
                // $("#ypks").text(ypks);
                // $("#allks").text(ks);
            });
        } else {
            $(".classrooms").empty();
            Common.render({tmpl: $('#teacher_templ'), data: [], context: '.classrooms'});
        }
    }
    pkTableTiaoke.ChuShiByTea = function () {
        var array = new Array();
        var y = 0;
        var x = 0;
        var count = 0;
        $(".jxbkj").empty();
        $(".kejiecount").each(function (j, jt) {
            count++;
        })
        $(".gradeweek").each(function (s, ot) {
            $(".kejiecount").each(function (i, st) {
                var map = {};
                map.x = x;
                map.y = y;
                array.push(map);
                y += 1;
                if (y == count) {
                    x += 1;
                    y = 0;
                }
            })
        })
        Common.render({tmpl: $('#kbByTea'), data: array, context: '.jxbkj'});
        var a = 0;
        var b = 0;
        $(".itt").each(function (s, ot) {
            $(this).attr("x", a);
            $(this).attr("y", b);
            b += 1;
            if (b == count) {
                a += 1;
                b = 0;
            }
        })
    }
    pkTableTiaoke.getGuDingShiWu = function () {
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": zhenXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                if (!$(".idx" + (obj.y - 1) + (obj.x - 1)).hasClass("red")) {
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).html(obj.desc);
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).addClass("red")
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title", obj.desc);
                } else {
                    var string = $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title");
                    if (string || string == null) {
                        string = obj.desc;
                    } else {
                        string += ",";
                        string += obj.desc;
                    }
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title", string);
                }
            })
        })
    }
    pkTableTiaoke.getGDSX = function () {
        var par = {};
        par.xqid = zhenXqid;
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gid = gradeId;

        var teacherId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                teacherId = $(this).attr('ids');
            }
        });
        par.teaId = teacherId;
        //用于查询老师
        par.ciId = xqid;
        Common.getData('/gdsx/getGDSXBySidAndXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                if ($(".idx" + (obj.x) + (obj.y)).hasClass("red")) {
                    var string = $(".idx" + (obj.x) + (obj.y)).attr("title");
                    if (string || string == null) {
                        string = obj.desc;
                    } else {
                        string += ",";
                        string += obj.desc;
                    }
                    $(".idx" + (obj.x) + (obj.y)).attr("title", string);
                    $(".idx" + (obj.x) + (obj.y)).addClass("red")
                } else {
                    $(".idx" + (obj.x) + (obj.y)).attr("title", obj.desc);
                    $(".idx" + (obj.x) + (obj.y)).text(obj.desc);
                    $(".idx" + (obj.x) + (obj.y)).addClass("red")
                }

            })
        })
    }
    pkTableTiaoke.getTeaShiWuByXqid = function () {
        var par = {}
        par.xqid = zhenXqid;
        var teacherId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                teacherId = $(this).attr('ids');
            }
        });
        par.teaId = teacherId;
        Common.getData('/new33isolateMange/getTeaShiWuByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                if (!$(".idx" + (obj.y - 1) + (obj.x - 1)).hasClass("red")) {
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).html(obj.desc);
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).addClass("red")
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title", obj.desc);
                } else {
                    var string = $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title");
                    if (string || string == null) {
                        string = obj.desc;
                    } else {
                        string += ",";
                        string += obj.desc;
                    }
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title", string);
                }
            })
        })
    }
    pkTableTiaoke.GetTeachersSettledPositions = function () {
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gid = gradeId;
        var teacherId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                teacherId = $(this).attr('ids');
            }
        });
        par.teacherId = teacherId;
        if (par.teacherId != undefined && par.teacherId != null && par.teacherId != "" && par.gid != undefined && par.gid != "" && par.gid != null) {
            Common.getData('/paike/GetTeachersSettledPositionsByGrade.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    var str = "";
                    if ($(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").hasClass("red")) {
                        $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").html("");
                        $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").removeClass("red");
                        str = "<p class='shi pic'></p>";
                    }
                    if (obj.type == 1) {
                        str += "<p class='zb pic'></p>";
                    } else if (obj.type == 2) {
                        str += "<p class='zbl pic'></p>";
                    } else if (obj.type == 4) {
                        str += "<p class='zhuan pic'></p>";
                    } else if (obj.type == 6) {
                        str += "<p class='dan pic'></p>";
                    }
                    str += "<div class='ls'><p class='whide' title=" + obj.JxbName + obj.count + "><em class='vxk'>" + obj.JxbName + "</em><em class='vrs'>(" + obj.count + ")</em></p><p>" + obj.subName + "</p><p><em class='vjs'>" + obj.roomName + "</em></p></div>";
                    $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").html(str);
                    $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").attr("ykbId", obj.ykbId);
                    $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").attr("jxbId", obj.jxbId);
                    $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").attr("kbType", obj.type);
                })
                pkTableTiaoke.getZouBanTimeList(2);
            })
        }
    }
    pkTableTiaoke.getZouBanTimeList = function (type) {
        var par = {}
        var gid = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gid = $(this).attr('ids');
            }
        })

        par.xqid = xqid;
        par.gid = gid;
        if (par.xqid != "" && par.xqid != undefined && par.xqid != null && par.gid != "" && par.gid != undefined && par.gid != null) {
            Common.getData('/courseset/getZouBanTime.do', par, function (rep) {
                var dom = $(".itt");
                if (type == 1) {
                    dom = $(".citt");
                }
                $.each(rep.message, function (i, obj) {
                    if (obj.type == 0) {
                        dom.each(function (o, st) {
                            if ($(this).attr("x") == (obj.x - 1) && $(this).attr("y") == (obj.y - 1)) {
                                if ($(this).find("p").length == 0 && !$(this).hasClass("red")) {
                                    $(this).append('<p class="hzb"></p>');
                                }
                            }
                        });
                    }
                });
            });
        }
    }
    // pkTableTiaoke.getZkbByJxbIdYKB = function (jxbid) {
    //     $('.tab2 td>div>div').addClass("fuck")
    //     var par = {};
    //     par.jxbId = jxbid;
    //     Common.getData('/paike/getZkbByJxbIdYKB.do', par, function (rep) {
    //         $.each(rep.message, function (i, obj) {
    //             if (obj.sta == 0) {
    //                 var x = obj.y - 1;
    //                 var y = obj.x - 1;
    //                 if ($("#classroom").is(":checked")) {
    //                     if (!$(".tb-clsrom[x=" + y + "][y=" + x + "]").hasClass("ban")) {
    //                         $(".tb-clsrom[x=" + y + "][y=" + x + "]").addClass("roomCls")
    //                     }
    //                 } else {
    //                     if (!$(".tb-clsrom[x=" + y + "][y=" + x + "]").hasClass("ban")) {
    //                         $(".tb-clsrom[x=" + y + "][y=" + x + "]").addClass("roomCls")
    //                         if ($(".tb-clsrom[x=" + y + "][y=" + x + "]").attr("ykbid") != "" && $(".tb-clsrom[x=" + y + "][y=" + x + "]").attr("ykbid") != undefined) {
    //                         } else {
    //                             $(".tb-clsrom[x=" + y + "][y=" + x + "]").addClass("nmgb");
    //                             $(".tb-clsrom[x=" + y + "][y=" + x + "]").attr("ykbid", obj.ykbId)
    //                             $(".tb-clsrom[x=" + y + "][y=" + x + "]").append("<p class='pic'></p>")
    //                             $(".tb-clsrom[x=" + y + "][y=" + x + "]").append("<div class='ls'><p class='whide' title='" + obj.jxbName + "'><em class='vxk'>" + obj.jxbName + "</em><em class='vrs'></em></p><p></p><p><em class='vjs'>" + obj.roomName + "</em></p></div>")
    //                         }
    //                     }
    //                 }
    //             }
    //         })
    //     });
    // }

    pkTableTiaoke.getAllCTXY = function (jxbid,x,y) {
        var par = {};
        par.jxbId = jxbid;
        var classRoomId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                classRoomId = $(this).attr('ids');
            }
        });
        par.classRoomId = classRoomId;
        par.x = x;
        par.y = y;
        var teacherId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                teacherId = $(this).attr('ids');
            }
        });
        par.tid = teacherId;
        if($("#classroom").is(":checked")){
            par.type = 1;
        }
        $(".tb-clsrom").each(function () {
            $(this).attr("title","");
        })
        Common.getData('/paike/getAllCTXY.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var x = obj.x;
                var y = obj.y;
                if ($("#classroom").is(":checked")) {
                    if (!$(".tb-clsrom[x=" + x + "][y=" + y + "]").hasClass("ban")) {
                        $(".tb-clsrom[x=" + x + "][y=" + y + "]").addClass("fuck");
                        $(".tb-clsrom[x=" + x + "][y=" + y + "]").attr("title",obj.msg);
                    }
                }else{
                    if(!$(".itt[x=" +x+ "][y=" +y + "]").hasClass("ban")){
                            $(".itt[x=" +x+ "][y=" +y + "]").addClass("fuck");
                        $(".itt[x=" +x+ "][y=" +y + "]").attr("title",obj.msg);
                    }
                }
            })
        })
    }

    //源课表格子所有课
    pkTableTiaoke.getJXBById = function (ykbId, x, y) {
        var par = {};
        par.ykbId = ykbId;
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        Common.getData('/paike/getJXBById.do', par, function (rep) {
            $('.jxbs').empty();
            Common.render({tmpl: $('#jxbs_templ'), data: rep.message.jxbdtos, context: '.jxbs'});
            $('.xkb').empty();
            if (rep.message.teas != null && rep.message.teas.length != 0) {
                Common.render({tmpl: $('#xkb_temp'), data: rep.message.teas, context: '.xkb'});
            }
            if (rep.message.teakbs != null && rep.message.teakbs.length != 0) {
                $.each(rep.message.teakbs, function (key, value) {
                    if (value != null && value.length != 0) {
                        for (var i = 0; i < value.length; i++) {
                            if(x == value[i].x && y == value[i].y){
                                var cls = key + value[i].x + value[i].y;
                                $('.' + cls + '').addClass("xkbColor");
                            }
                            var cls = key + value[i].x + value[i].y;
                            $('.' + cls + '').text(value[i].subjectName.charAt(0));
                            if($('.' + cls + '').attr("title") != "" && $('.' + cls + '').attr("title") != undefined){
                                var string = $('.' + cls + '').attr("title");
                                string += "\r\n";
                                string += "教学班：" + value[i].jxbName;
                                string += "\r\n";
                                string += "教室：" + value[i].classRoomName;
                                $('.' + cls + '').attr("title",string);
                            }else{
                                $('.' + cls + '').attr("title","教学班：" + value[i].jxbName + "\r\n" + "教室：" + value[i].classRoomName);
                            }

                        }
                    }
                })
            }
            // $.each(rep.message.jxbdtos, function (fck, fuck) {
            //     $(".caonima[index=" + x + "][y=" + y + "]").attr("title", "教学班:" + fuck.name + ",老师:" + fuck.teacherName)
            //     $(".caonima[index=" + x + "][y=" + y + "]").attr("teacherName", fuck.teacherName)
            //     $(".caonima[index=" + x + "][y=" + y + "]").attr("jxbName", fuck.name)
            // })
        });
    }
    pkTableTiaoke.getJXBByXY = function (x, y) {
        var par = {};
        par.x = x;
        par.y = y;
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.xqid = xqid;
        Common.getData('/paike/getJXBByXY.do', par, function (rep) {
            $('.jxbs').empty();
            Common.render({tmpl: $('#jxbs_templ'), data: rep.message.jxbdtos, context: '.jxbs'});
            if (rep.message.teas != null && rep.message.teas.length != 0) {
                $('.xkb').empty();
                Common.render({tmpl: $('#xkb_temp'), data: rep.message.teas, context: '.xkb'});
            }
            if (rep.message.teakbs != null && rep.message.teakbs.length != 0) {
                $.each(rep.message.teakbs, function (key, value) {
                    if (value != null && value.length != 0) {
                        for (var i = 0; i < value.length; i++) {
                            if(x == value[i].x && y == value[i].y){
                                var cls = key + value[i].x + value[i].y;
                                $('.' + cls + '').addClass("xkbColor");
                            }
                            var cls = key + value[i].x + value[i].y;
                            $('.' + cls + '').text(value[i].subjectName.charAt(0));
                            if($('.' + cls + '').attr("title") != "" && $('.' + cls + '').attr("title") != undefined){
                                var string = $('.' + cls + '').attr("title");
                                string += "\r\n";
                                string += "教学班：" + value[i].jxbName;
                                string += "\r\n";
                                string += "教室：" + value[i].classRoomName;
                                $('.' + cls + '').attr("title",string);
                            }else{
                                $('.' + cls + '').attr("title","教学班：" + value[i].jxbName + "\r\n" + "教室：" + value[i].classRoomName);
                            }

                        }
                    }
                })
            }
        });
    }
    pkTableTiaoke.checkYKB = function () {
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.termId = xqid;
        Common.getData('/paike/checkYKB.do', par, function (rep) {
        });
    }

    pkTableTiaoke.init = function () {
        $("body").on("click", "#sub label", function () {
            $('#sub label').each(function (i) {
                $(this).removeClass('active');
            })
            $(this).addClass('active');
            $(".itt").html("")
            pkTableTiaoke.getTeacherList();
            pkTableTiaoke.ChuShiByTea();
            pkTableTiaoke.getGuDingShiWu();
            pkTableTiaoke.getGDSX();
            pkTableTiaoke.getTeaShiWuByXqid();
            pkTableTiaoke.GetTeachersSettledPositions();
        });
        $("body").on("click", ".gaozhong label", function () {
            $('.gaozhong label').removeClass('active');
            $(this).addClass('active');
            pkTableTiaoke.checkYKB();
            if($("#teacher").is(":checked")) {
                pkTableTiaoke.getSubjectList();
                pkTableTiaoke.getTeacherList();
                pkTableTiaoke.ChuShiByTea();
                pkTableTiaoke.getGuDingShiWu();
                pkTableTiaoke.getGDSX();
                pkTableTiaoke.getTeaShiWuByXqid();
                pkTableTiaoke.GetTeachersSettledPositions();
            }else{
                pkTableTiaoke.getGradeWeekRangeByXqid();
                pkTableTiaoke.getRoomEntryListByXqGrade();
                pkTableTiaoke.getKeBiaoList();
                pkTableTiaoke.getRecord();
            }
        })
        $('body').on('click', '.classrooms li', function () {
            $(this).addClass('t-li').siblings().removeClass('t-li');
            if ($("#classroom").is(":checked")) {
                //pkTableTiaoke.getGradeWeekRangeByXqid();
                //pkTableTiaoke.getRoomEntryListByXqGrade();
                pkTableTiaoke.getKeBiaoList();
                pkTableTiaoke.getRecord();
            }else{
                pkTableTiaoke.ChuShiByTea();
                pkTableTiaoke.getGuDingShiWu();
                pkTableTiaoke.getGDSX();
                pkTableTiaoke.getTeaShiWuByXqid();
                pkTableTiaoke.GetTeachersSettledPositions();
            }
        })
        $(".b5").click(function () {
            window.location.href="/newisolatepage/pkTable.do"
        })
        $(".b6").click(function () {
            if ($(".ban").length == 2) {
                var par = {};
                $(".ban").each(function (i, obj) {
                    if (i == 0) {
                        par.ybkId = $(obj).attr("ykbId")
                    } else {
                        par.ybkId1 = $(obj).attr("ykbId");
                    }
                })
                Common.getData('/paike/tiaokeLuo.do', par, function (rep) {
                    if($("#teacher").is(":checked")) {
                        //pkTableTiaoke.getSubjectList();
                        //pkTableTiaoke.getTeacherList();
                        pkTableTiaoke.ChuShiByTea();
                        pkTableTiaoke.getGuDingShiWu();
                        pkTableTiaoke.getGDSX();
                        pkTableTiaoke.getTeaShiWuByXqid();
                        pkTableTiaoke.GetTeachersSettledPositions();
                    }else{
                        pkTableTiaoke.getGradeWeekRangeByXqid();
                        //pkTableTiaoke.getRoomEntryListByXqGrade();
                        pkTableTiaoke.getKeBiaoList();
                    }
                    layer.msg("操作成功！!");
                    //$('.tab2 td>div>div').removeClass("ban")
                    //$('.tab2 td>div>div').removeClass("roomCls")
                    //$('.tab2 td>div>div').removeClass("active")
                    //$('.tab2 td>div>div').removeClass("actv")
                    //$(".nmgb").empty();
                    //$(".nmgb").attr("ykbid", "");
                    //$('.tab2 td>div>div').removeClass("nmgb")
                    //$(".caonima").attr("teacherName", "")
                    //$(".caonima").attr("title", "")
                    //$(".caonima").attr("jxbName", "")
                })
            } else {
                layer.msg("请选择交换点。")
            }
        })
        $('body').on('click','.moreBtn button',function(){
            if($(this).text().indexOf('更多')!=-1){
                $('.tab tr td').css('display','table-cell');
                $(this).text('收起')
            }else{
                $(this).text('更多')
                $('.tab tr td').css('display','none');
                $('.tab tr:eq(1) td').css('display','table-cell');
            }
        })

        //点击课表表格
        $('body').on('click', '.tab2 td>div>div', function () {
            var wc = $(this);
            console.log($(".ban").length)
            if ($(".ban").length == 1) {
                var par = {};
                par.ybkId1 = $(this).attr("ykbId");
                $(".ban").each(function (i, obj) {
                    par.ybkId = $(obj).attr("ykbId")
                })
                par.x = $(this).attr("x");
                par.y = $(this).attr("y");
                par.ciId = xqid;

                if ($(this).hasClass("ban")) {
                    $(this).removeClass("ban");
                } else {

                    console.log($(this).hasClass("fuck"));

                    if(!$(this).hasClass("fuck")){
                        Common.getData('/paike/getShiBuShiKeTiaoKeYKB.do', par, function (rep) {
                            if($("#teacher").is(":checked")) {
                                //pkTableTiaoke.getSubjectList();
                                //pkTableTiaoke.getTeacherList();
                                pkTableTiaoke.ChuShiByTea();
                                pkTableTiaoke.getGuDingShiWu();
                                pkTableTiaoke.getGDSX();
                                pkTableTiaoke.getTeaShiWuByXqid();
                                pkTableTiaoke.GetTeachersSettledPositions();
                            }else{
                                pkTableTiaoke.getGradeWeekRangeByXqid();
                                //pkTableTiaoke.getRoomEntryListByXqGrade();
                                pkTableTiaoke.getKeBiaoList();
                                pkTableTiaoke.getRecord();
                                $('.xkb').empty();
                            }
                        })
                        $('.moreBtn button').text('更多')

                    }
                }
            } else {
                $('.tab2 td>div>div').removeClass("fuck")
                $('.tab2 td>div>div').removeClass("ban")
                $('.tab2 td>div>div').removeClass("roomCls")
                $(".nmgb").empty();
                $(".nmgb").attr("ykbid", "");
                $('.tab2 td>div>div').removeClass("nmgb")
                if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                    if ($(this).hasClass('red')) {

                    } else {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass('actv');
                            $(this).removeClass('active');
                            $('.jxbs').empty();
                            $('.xkb').empty();
                        } else {
                            $('.tab2 td>div>div').each(function (i) {
                                $(this).removeClass('actv');
                                $(this).removeClass('active');
                            });
                            if (!pkflg && $(this).attr("kbtype") != 0 && $(this).attr("kbtype") != undefined && $(this).attr("kbtype") != null) {
                                if ($(this).hasClass('actv')) {
                                    $(this).removeClass('actv');
                                } else {
                                    $(this).addClass("ban")
                                    $(this).addClass('actv');
                                    pkTableTiaoke.getJXBById($(this).attr("ykbId"), $(this).attr("x"), $(this).attr("y"));
                                    if ($(this).attr("jxbid") != undefined && $(this).attr("jxbid") != "") {
                                        pkTableTiaoke.getAllCTXY($(this).attr("jxbid"),$(this).attr("x"),$(this).attr("y"));
                                    }
                                }
                                $(this).toggleClass('active');
                            } else if (!pkflg && ($(this).attr("kbtype") == 0 || $(this).attr("kbtype") == undefined || $(this).attr("kbtype") == null)) {
                                if ($(this).hasClass('actv')) {
                                    $(this).removeClass('actv');
                                } else {
                                    $(this).addClass('actv');
                                    pkTableTiaoke.getJXBByXY($(this).attr("x"), $(this).attr("y"));
                                }
                                $(this).toggleClass('active');
                            }
                        }
                    }
                } else {
                    if ($(this).hasClass('red')) {

                    } else {
                        if ($(this).hasClass('active')) {
                            $(this).removeClass('actv');
                            $(this).removeClass('active');
                            $('.jxbs').empty();
                            $('.xkb').empty();
                        } else {
                            $('.tab2 td>div>div').each(function (i) {
                                $(this).removeClass('actv');
                                $(this).removeClass('active');
                            });
                            if (!pkflg && $(this).attr("kbtype") != 0 && $(this).attr("kbtype") != undefined && $(this).attr("kbtype") != null) {
                                if ($(this).hasClass('actv')) {
                                    $(this).removeClass('actv');
                                } else {
                                    $(this).addClass("ban")
                                    $(this).addClass('actv');
                                    pkTableTiaoke.getJXBById($(this).attr('ykbid'), $(this).attr("x"), $(this).attr("y"));
                                    if ($(this).attr("jxbid") != undefined && $(this).attr("jxbid") != "") {
                                        pkTableTiaoke.getAllCTXY($(this).attr("jxbid"),$(this).attr("x"),$(this).attr("y"));
                                    }
                                }
                                $(this).toggleClass('active');
                            }else if(!pkflg && ($(this).attr("kbtype") == 0 || $(this).attr("kbtype") == undefined || $(this).attr("kbtype") == null)){
                                if ($(this).hasClass('actv')) {
                                    $(this).removeClass('actv');
                                } else {
                                    $(this).addClass('actv');
                                    pkTableTiaoke.getJXBByXY($(this).attr("x"), $(this).attr("y"));
                                }
                                $(this).toggleClass('active');
                            }
                        }
                    }
                }
                // $(".caonima[index=" + $(this).attr("x") + "][y=" + $(this).attr("y") + "]").addClass("hongse")
            }
        });


        $("body").on("click", "#classroom", function () {
            pkTableTiaoke.getSubjectList();
            pkTableTiaoke.getRoomEntryListByXqGrade();
            pkTableTiaoke.getKeBiaoList();
            var len = zlength * 128;
            $('th>div').css('width', len + 'px');
            $('.fixedcont .ul1').css('width', '160');
            $('#tid').show();
            $('#gid').removeClass("one")
            $('#tagcheck').hide();
            $('#gid').hide();
            $('#tid').removeClass("one")
            $('#kuohao').text("（");
            $('#fenge').text("/");
        })
        //按照教师排课
        $("body").on("click", "#teacher", function () {
            $(".itt").html("")
            pkTableTiaoke.getSubjectList();
            pkTableTiaoke.getTeacherList();
            pkTableTiaoke.ChuShiByTea();
            pkTableTiaoke.getGuDingShiWu();
            pkTableTiaoke.getGDSX();
            pkTableTiaoke.getTeaShiWuByXqid();
            pkTableTiaoke.GetTeachersSettledPositions();
            var len = zlength * 128;
            $('th>div').css('width', len + 'px');
            $('.fixedcont .ul1').css('width', '180');
            $('#tid').hide();
            $('#tagcheck').hide();
            $('#gid').show();
            $('#gid').addClass("one")
            $('#clsrom').text($(this).find("span").attr("tname"));
            $('#ypks').text("");
            $('#allks').text("");
            $('#kuohao').text("");
            $('#fenge').text("");
        })

        $('body').on("click","#back",function () {
            pkTableTiaoke.cancelRecord();
            $('.xkb').empty();
        });

        pkTableTiaoke.getDefaultTerm();
        pkTableTiaoke.getListByXq();
        pkTableTiaoke.getGradeList();
        pkTableTiaoke.getGradeWeekRangeByXqid();
        pkTableTiaoke.getRoomEntryListByXqGrade();
        pkTableTiaoke.getKeBiaoList();
        pkTableTiaoke.getRecord();
        initGrade();
    }

    pkTableTiaoke.cancelRecord = function (){
        var par = {};
        par.xqid = xqid;
        if(par.xqid == null || par.xqid == undefined || par.xqid == ""){

        }else{
            Common.getData('/paike/cancelRecord.do', par, function (rep) {
                pkTableTiaoke.getKeBiaoList();
                pkTableTiaoke.getRecord();
            })
        }
    }

    pkTableTiaoke.getRecord = function(){
        var par = {};
        par.xqid = xqid;

        if(par.xqid == null || par.xqid == undefined || par.xqid == ""){

        }else{
            Common.getData('/paike/getRecord.do', par, function (rep) {
                Common.render({tmpl: $('#record_temp'), data: rep.message, context: '#record',overwrite:1});
            })
        }
    }

    pkTableTiaoke.getKeBiaoList = function (type) {
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        data.gradeId = gradeId;
        var classRoomId = "";

        var keIds = "";
        $('.kj label').each(function (i) {
            if ($(this).hasClass('active')) {
                keIds += $(this).attr('ids') + ",";
            }
        });
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                classRoomId = $(this).attr('ids');
            }
        });
        data.classRoomId = classRoomId;
        data.indexs = keIds;
        data.termId = xqid;
        //$("#paikeByTea").hide();
        //$("#paikeByRoom").show();
        if(data.gradeId != ""){
            Common.getData('/paike/getKeBiaoListByCrmid.do', data, function (rep) {
                if (rep.code == '200') {
                    $('#clsrom').text($(this).find("span").attr("tname"));
                    $('#ypks').text(rep.message.ypCnt);
                    $('#allks').text(rep.message.allCnt + "课时)");
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
                    $('.classrooms li').each(function (i) {
                        if ($(this).hasClass("t-li")) {
                            crIds[i] = $(this).attr('ids');
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
        }
    }
    pkTableTiaoke.getRoomEntryListByXqGrade = function () {
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
            if(par.gradeId != ""){
                Common.getData('/paike/getRoomEntryListByXqGrade.do', par, function (rep) {
                    if (rep.code == '200') {
                        $(".classrooms").empty();
                        Common.render({tmpl: $('#classrooms_templ'), data: rep.message, context: '.classrooms'});
                        $(".classrooms li:eq(0)").addClass("t-li");
                        $('#clsrom').text($(".classrooms li:eq(0)").find("span").text());
                    }
                });
            }
        }
    }
    pkTableTiaoke.getGradeWeekRangeByXqid = function () {
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
            if(par.gid != ""){
                Common.getData('/n33_gradeweekrange/getGradeWeekRangeByXqid.do', par, function (rep) {
                    if (rep != null) {
                        zlength = rep.end;
                    }
                });
            }
        }
    }
    pkTableTiaoke.getGradeList = function () {
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
            $(".gaozhong").append(" <span>年级 : </span>")
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.gaozhong'});
            // $(".gaozhong label:eq(0)").addClass("active");
            // pkTableTiaoke.initGrade();
            //pkTableTiaoke.initGrade();
        })
    }
    pkTableTiaoke.getListByXq = function () {
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": zhenXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '.g-dl .sel2', overwrite: 1});
            //处理开始周
            wk = rep.length;
            var it = 0;
            $(".jieshu option").each(function (is, ot) {
                it = is;
            });
            $(".jieshu option:eq(" + it + ")").attr("selected", true);
        })
    }
    pkTableTiaoke.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            xqid = rep.message.paikeci;
            zhenXqid = rep.message.paikexq;
            $("#defaultTerm").attr("ids", rep.message.paikeci);
        });
    }


    function initGrade(){
        try {
            Common.userData("n33", function (res) {
                var gradeId = "";
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                    })

                    $(".gaozhong label").each(function () {
                        if (gradeId == $(this).attr("ids")) {
                            $(this).click();
                        }
                    });
                } else {

                }
            });
        } catch (x) {

        }
    }

    //点击老师 相同名字变成红色
    $('body').on('click','.ls',function(){
        var text = $(this).find('.vjs').text();
        $('.ls').each(function(){
            if($(this).find('.vjs').text()==text){
                $(this).find('.vjs').addClass('font');
            }else{
                $(this).find('.vjs').removeClass('font')
            }
        })
    })

    //新增课表弹窗
    $('body').on('click','.tea',function(){
        var tid = $(this).find("span").attr("tid");
        var tname = $(this).find("span").attr("tname");
        $('.name').text(tname);
        pkTableTiaoke.getListBySchoolId();
        pkTableTiaoke.getChushi();
        pkTableTiaoke.GetTeachersSettledPositions(tid);
        dialogShow()
    });
    $('.dclose').click(dialogClose);

    pkTableTiaoke.getListBySchoolId = function () {
        if (xqid && xqid != null) {
            Common.getData('/courseset/getListBySchoolId.do', {"xqid": xqid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }

    pkTableTiaoke.getChushi = function () {
        var y = 1;
        var x = 1;
        $(".itt1").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            y += 1;
            if (y == 8) {
                x += 1;
                y = 1;
            }
        })
    }

    pkTableTiaoke.GetTeachersSettledPositions = function (tid) {
        var par = {};
        par.teacherId = tid;
        Common.getData('/paike/GetTeachersSettledPositions.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                if($(".itt1[x=" + obj.y + "][y=" + obj.x + "]").html() != "" && $(".itt1[x=" + obj.y + "][y=" + obj.x + "]").html() != null && $(".itt1[x=" + obj.y + "][y=" + obj.x + "]").html() != undefined){
                    var string = $(".itt1[x=" + obj.y + "][y=" + obj.x + "]").html();
                    string += '<span class="spSty"> <label>'+obj.JxbName + '</label></span>' + '<span class="spSty"><label>'+ obj.gradeName  + '</label></span>' + '<span class="spSty"><label>' +obj.roomName+'</label></span>';
                    $(".itt1[x=" + obj.y + "][y=" + obj.x + "]").html(string);
                }else{
                    $(".itt1[x=" + obj.y + "][y=" + obj.x + "]").html('<span class="spSty"> <label>'+obj.JxbName + '</label></span>' + '<span class="spSty"><label>'+ obj.gradeName  + '</label></span>' + '<span class="spSty"><label>' +obj.roomName+'</label></span>')
                }
            })
        })
    }


    function dialogShow(){
        // $('.bg').show();
        $('.dialog').show()
    }
    function dialogClose(){
        // $('.bg').hide();
        $('.dialog').hide()
    }


    module.exports = pkTableTiaoke;
})