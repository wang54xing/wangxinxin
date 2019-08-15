/**
 * Created by albin on 2018/3/23.
 */
define('pTeaClassTime', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var pTeaClassTime = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var deCid = "";
    var ksCount = 0;
    var ciId = null;

    var desc = "";
    pTeaClassTime.getDesc = function () {
        var par = {};
        par.ciId = ciId;
        par.gradeId = $(".grade .active").attr("ids");
        par.type = 3;
        par.kbType = 2;
        if(ciId != null){
            Common.getData('/n33_set/getDESCForNoCourse.do', par, function (rep) {
                desc = rep.message;
            });
        }
    }

    pTeaClassTime.initDesc = function(){
        $('.itt').each(function (i,st) {
            if(($(this).text() == null || $(this).text().trim() == "") && !$(this).hasClass("grayhui")){
                $(this).text(desc);
            }
        });
    }

    pTeaClassTime.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getGradeWeek.do', {"xqid": deXqid}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial + 1;
                var index2 = rep.serial + 2;
                $(".itt[y=" + index + "]").addClass("grayhui")
                $(".itt[y=" + index2 + "]").addClass("grayhui")
                $(".freeTea[x=" + (index - 1) + "]").addClass("grayhui")
                $(".freeTea[x=" + (index2 - 1) + "]").addClass("grayhui")
                $(".freeTea1[x=" + (index - 1) + "]").addClass("grayhui")
                $(".freeTea1[x=" + (index2 - 1) + "]").addClass("grayhui")
            }
        });
        pTeaClassTime.initDesc();
    }
    pTeaClassTime.getGDSX = function () {
        var par = {};
        par.xqid = deXqid;
        par.userId = $(".t-li").attr("ids");
        Common.getData('/gdsx/getGDSXBySidAndXqidTea.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var x = obj.y + 1;
                var y = obj.x + 1;
                if ($(".itt[x=" + x + "][y=" + y + "]").text() == "") {
                    $(".itt[x=" + x + "][y=" + y + "]").html(obj.desc);
                    $(".itt[x=" + x + "][y=" + y + "]").addClass("swRed");
                } else {
                    $(".itt[x=" + x + "][y=" + y + "]").attr("title", obj.desc);
                    $(".itt[x=" + x + "][y=" + y + "]").addClass('shi');
                }
                //$(".itt[x=" + x + "][y=" + y + "]").css("background", "#ccc")
            })
        })
    }

    function getStatusDC() {
        $.ajax({
            type: "GET",
            url: '/paike/getStatusDC.do',
            async: false,
            cache: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {
                if (rep.st == -1) {
                    $('.bg').hide();
                    $(".jhh").hide();
                    window.clearInterval(c);
                } else {
                    $(".jhhSp").text("正在导出所选老师课表，请稍候...");
                }
            }
        });
    }

    pTeaClassTime.GetTeachersSettledPositions = function () {
        pTeaClassTime.getListBySchoolIdForKB();
        pTeaClassTime.getChushi();
        pTeaClassTime.getGDSX();
        pTeaClassTime.getTeaShiWuByXqid();
        pTeaClassTime.getGuDingShiWu();
        $(".itt").removeClass("swRed");
        $(".itt").removeClass("shi");
        $(".itt").html("")
        var par = {};
        par.teacherId = $(".t-li").attr("ids");
        par.week = $("#week").val();
        if (par.teacherId != "" && par.teacherId != undefined && par.teacherId != null) {
            Common.getData('/paike/GetTeachersSettledPositionsByWeek.do', par, function (rep) {
                ksCount = rep.message.length;
                pTeaClassTime.getPks();
                $.each(rep.message, function (i, obj) {
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.JxbName + "(" + obj.count + "人)" + "</br>" + obj.subName + "</br>" + obj.grade + "</br>" + obj.roomName)
                    ciId = obj.cid;
                })
            })
        }
        pTeaClassTime.getDesc();
    }
    pTeaClassTime.init = function () {
        $('body').on('click', '#printGDG', function () {
            $('#printGDG').hide();
            var headstr = "<html><head><title></title></head><body>";
            var footstr = "</body>";
            var newstr = $('#GDGContent').html();
            var oldstr = document.body.innerHTML;
            document.body.innerHTML = headstr + newstr + footstr;
            window.print();
            console.log('老页面：' + oldstr);
            console.log('新页面：' + newstr);
            document.body.innerHTML = oldstr;
            $('#printGDG').show()
            return false;
        })

        $('body').on("click",".b10",function () {
            $(".jhh").show();
            $('.bg').show();
            $(".jhh-top").text("批量导出");
            $(".jhhSp").text("正在查询老师，请稍候...");
            setTimeout(function () {
                pTeaClassTime.getGradeList();
                pTeaClassTime.getSubjectListForExport();
                pTeaClassTime.getTeaListForExport();
                $(".bg").hide();
                $(".jhh").hide();
                $('.dc-popup').show();
                $('.bg').show();
            },500);
        });

        $('body').on("click",".selectAll",function () {
            var flag = false;
            $('.dcTea').each(function (i,st) {
                if(!$(this).is(":checked")){
                    flag = true;
                }
            });
            if(flag){
                $('.dcTea').each(function (i,st) {
                    $(this).prop("checked",true);
                });
            }else{
                $('.dcTea').each(function (i,st) {
                    $(this).prop("checked",false);
                });
            }
        });

        $("body").on("change", "#grade2", function () {
            pTeaClassTime.getSubjectListForExport();
            pTeaClassTime.getTeaListForExport();
        });

        $("body").on("change", "#subject2", function () {
            pTeaClassTime.getTeaListForExport();
        });

        $("body").on("click", "#wkcx", function () {
            pTeaClassTime.getGradeList();
        });

        $("body").on("change", "#grade", function () {
            pTeaClassTime.getSubjectListByTime();
            pTeaClassTime.getFreeTeacher();
        });

        $("body").on("change", ".grade", function () {
            pTeaClassTime.getSubjectListByTea();
            pTeaClassTime.getTeaList();
        });

        $("body").on("change", ".subject", function () {
            pTeaClassTime.getTeaList();
        });

        $("body").on("change", "#subject", function () {
            pTeaClassTime.getFreeTeacher();
        });

        $("body").on("change", ".week", function () {
            pTeaClassTime.getFreeTeacher();
        });

        $("body").on("click", ".wuTab td", function () {
            //$(this).find('i').addClass('tii').parents().siblings().find('i').removeClass('tii')
            if (!$(this).hasClass("grayhui")) {
                if ($(this).find('i').hasClass('tii')) {
                    $(this).find('i').removeClass('tii');
                } else {
                    $(this).find('i').addClass('tii');
                }
                pTeaClassTime.getFreeTeacher();
            }
        });

        $("#week").change(function () {
            var tid = $(".t-li").attr("ids");
            pTeaClassTime.getUserList();
            $('#use li').each(function (i, st) {
                $(this).removeClass("t-li");
                if (tid == $(st).attr("ids")) {
                    $(st).addClass("t-li");
                }
            });
            pTeaClassTime.GetTeachersSettledPositions();
            pTeaClassTime.getGDSX();
            pTeaClassTime.getTeaShiWuByXqid();
            pTeaClassTime.getGuDingShiWu()
            pTeaClassTime.getCurrentJXZ();
        })
        pTeaClassTime.getDefaultTerm();
        pTeaClassTime.getGrade();
        pTeaClassTime.getListByXq();
        pTeaClassTime.getListBySchoolId();
        pTeaClassTime.getListBySchoolIdForKB();
        pTeaClassTime.getSubjectList();
        pTeaClassTime.getUserList();
        pTeaClassTime.getChushi();
        pTeaClassTime.GetTeachersSettledPositions();
        pTeaClassTime.getGDSX();
        pTeaClassTime.getTeaShiWuByXqid();
        pTeaClassTime.getGuDingShiWu();
        pTeaClassTime.getChushiForWuKeSearch();
        pTeaClassTime.getChushiForWuKeSearch1();
        pTeaClassTime.getCurrentJXZ();
        $("body").on("click", "#use li", function () {
            $("#use li").removeClass("t-li");
            $(this).addClass("t-li");
            pTeaClassTime.getPks();
            $(".itt").html("")
            pTeaClassTime.GetTeachersSettledPositions();
            pTeaClassTime.getGDSX();
            pTeaClassTime.getTeaShiWuByXqid();
            pTeaClassTime.getGuDingShiWu();
            pTeaClassTime.getCurrentJXZ();
        })

        $("body").on("click", ".b9", function () {
            var par = {};
            par.teacherId = $(".t-li").attr("ids");
            par.week = $("#week").val();
            par.userName = $(".t-li").find("span").text();
            window.location.href = "/paike/exportTeaKB.do?teacherId=" + par.teacherId + "&week=" + par.week + "&userName=" + par.userName;
        });
        // $("body").on("click", ".b9", function () {
        //         // var par = {};
        //         // par.teacherId = $(".t-li").attr("ids");
        //         // par.week = $("#week").val();
        //         // par.userName = $(".t-li").find("span").text();
        //         window.location.href = "/paike/exportTeaKB1.do";
        // });

        $('body').on("click",".dcAll",function () {
            var map = {};
            var teacherIds = new Array();
            $('.dcTea').each(function (i,st) {
                if($(this).is(":checked")){
                    teacherIds.push($(this).attr("ids"))
                }
            });
            map.teaIds = teacherIds;
            map.week = $("#week").val();
            map.xqid = deXqid;
            map.gradeId = $('#grade2').val();
            map.subId = $('#subject2').val();
            $('.dc-popup').hide();
            $(".jhh").show();
            $('.bg').show();
            $(".jhhSp").text("正在导出所选老师课表，请稍候...");
            setTimeout(function () {
                $("#export_form").attr("action", "/paike/exportTeaKBByGroup.do");
                $("#export_form").html("");
                $("#export_form").append("<input  type='hidden' name='dataMap' +  value='" + JSON.stringify(map) + "'/>");
                $("#export_form").submit();
            },500);
            c = setInterval(getStatusDC, 2000);
        });

        $("body").on("click", ".xueke label", function () {
            $(".xueke label").removeClass("active");
            $(this).addClass("active");
            $("#usPk").html("")
            $("#userName").html("")
            $("#keCount").html("")
            pTeaClassTime.getUserList();
            $(".itt").html("")
            pTeaClassTime.GetTeachersSettledPositions();
            pTeaClassTime.getGDSX();
            pTeaClassTime.getTeaShiWuByXqid();
            pTeaClassTime.getGuDingShiWu()
            pTeaClassTime.getCurrentJXZ();
        });

        $("body").on("click", ".grade label", function () {
            $(this).addClass('active').siblings().removeClass('active');
            pTeaClassTime.getSubjectList();
            $("#usPk").html("");
            $("#userName").html("");
            $("#keCount").html("");
            pTeaClassTime.getUserList();
            $(".itt").html("");
            pTeaClassTime.GetTeachersSettledPositions();
            pTeaClassTime.getGDSX();
            pTeaClassTime.getTeaShiWuByXqid();
            pTeaClassTime.getGuDingShiWu()
            pTeaClassTime.getCurrentJXZ();
        });

        $("body").on("click", "#byTime", function () {
            $(".z-popup").hide();
            $(".wu-popup").show();
            pTeaClassTime.getSubjectListByTime();
        });

        $("body").on("click", "#byTea", function () {
            pTeaClassTime.getSubjectListByTea();
            $(".z-popup").hide();
            $(".you-popup").show();
            pTeaClassTime.getTeaList();
            pTeaClassTime.initWuKe();
        });

        $("body").on("click", ".teacher", function () {
            $(this).toggleClass("t-li");
            pTeaClassTime.initWuKe();
            pTeaClassTime.getFreeKeJie();
        });
    }

    pTeaClassTime.initWuKe = function () {
        $(".freeTea1").each(function (i, st) {
            if (!$(this).hasClass("grayhui")) {
                $(this).find("i").addClass("tii1");
            }
        });
    }

    pTeaClassTime.getFreeKeJie = function () {
        var map = {};
        var teaList = new Array();
        $("#tbd1 .t-li").each(function (i, st) {
            teaList.push($(this).attr("ids"));
        });

        map.xqid = deXqid;
        map.week = $(".week2").val();
        map.teaIds = teaList;

        Common.getPostBodyData('/paike/getTeaIdsKPXy.do', map, function (rep) {
            $.each(rep.message, function (s, ot) {
                $(".freeTea1").each(function (i, st) {
                    if ((ot.x == $(st).attr("x") && ot.y == $(st).attr("y")) || $(st).hasClass("grayhui")) {
                        $(st).find("i").removeClass("tii1");
                    }
                });
            })
        });
    }

    pTeaClassTime.getFreeTeacher = function () {
        var map = {};
        var xyList = new Array();
        $(".tii").each(function (i, st) {
            var xyMap = {};
            xyMap.x = $(st).parent().attr("x");
            xyMap.y = $(st).parent().attr("y");
            xyList.push(xyMap);
        });

        /*if(xyList.length == 0){
         //Common.render({tmpl: $('#freeTea'), data: [], context: '#tea'});
         return;
         }*/
        map.xqid = deXqid;
        map.xyList = xyList;
        map.subId = $("#subject").val();
        map.gradeId = $("#grade").val();
        map.week = $(".week1").val();

        Common.getPostBodyData('/paike/getFreeTeacher.do', map, function (rep) {
            //Common.render({tmpl: $('#freeTea'), data: [], context: '#tea'});
            Common.render({tmpl: $('#freeTea'), data: rep.message, context: '#tea', overwrite: 1});
        });
    }

    pTeaClassTime.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.xqid;
            deCid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }

    pTeaClassTime.getChushi = function () {
        var y = 1;
        var x = 1;
        $(".itt").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            y += 1;
            if (y == 8) {
                x += 1;
                y = 1;
            }
        })
    }

    pTeaClassTime.getChushiForWuKeSearch = function () {
        var y = 0;
        var x = 0;
        $(".freeTea").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            x += 1;
            if (x == 7) {
                y += 1;
                x = 0;
            }
        })
    }

    pTeaClassTime.getChushiForWuKeSearch1 = function () {
        var y = 0;
        var x = 0;
        $(".freeTea1").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            x += 1;
            if (x == 7) {
                y += 1;
                x = 0;
            }
        })
    }

    pTeaClassTime.getGuDingShiWu = function () {
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": deXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                var string = "";
                if ($(".itt[x=" + obj.x + "][y=" + obj.y + "]").text() == "" && !$(".itt[x=" + obj.x + "][y=" + obj.y + "]").hasClass("swRed")) {
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc);
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass("swRed");
                } else {
                    if ($(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title") != undefined) {
                        string = $(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title");
                        string += "\r\n";
                        string += obj.desc;
                    } else {
                        $(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title", obj.desc);
                        $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass('shi');
                    }
                }
                //$(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
            })
        })
    }
    pTeaClassTime.getTeaShiWuByXqid = function () {
        var par = {}
        par.xqid = deXqid;
        par.teaId = $(".t-li").attr("ids");
        Common.getData('/new33isolateMange/getTeaShiWuByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var string = "";
                if ($(".itt[x=" + obj.x + "][y=" + obj.y + "]").text() == "" && !$(".itt[x=" + obj.x + "][y=" + obj.y + "]").hasClass("swRed")) {
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc);
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass("swRed");
                } else {
                    if ($(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title") != undefined) {
                        string = $(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title");
                        string += "\r\n";
                        string += obj.desc;
                    } else {
                        $(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title", obj.desc);
                        $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass('shi');
                    }
                }
                //$(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass("swRed");
                //$(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
            })
        })
    }
    pTeaClassTime.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        })
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            Common.render({tmpl: $('#week_temp'), data: rep, context: '.week', overwrite: 1});
            Common.render({tmpl: $('#week_temp'), data: rep, context: '.week1', overwrite: 1});
            Common.render({tmpl: $('#week_temp'), data: rep, context: '.week2', overwrite: 1});
            $(".week option[value=" + wk + "]").prop("selected", true)
            $("#week option[value=" + wk + "]").prop("selected", true)
            $(".week1 option[value=" + wk + "]").prop("selected", true)
            $(".week2 option[value=" + wk + "]").prop("selected", true)
        })
    }

    pTeaClassTime.getGrade = function () {
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": deXqid}, function (rep) {
            $(".grade").append(" <span>年级: </span>")
            Common.render({tmpl: $('#grade1_temp'), data: rep, context: '.grade'});
            $(".grade label:eq(0)").addClass("active");
        })
    }

    pTeaClassTime.getSubjectList = function () {
        var par = {};
        par.gradeId = $(".grade .active").attr("ids");
        par.xqid = deXqid;
        Common.getData('/new33isolateMange/getIsolateSubjectListByZKBGrade.do', par, function (rep) {
            /* $(".xueke").append(" <span>学科: </span>");*/
            Common.render({tmpl: $('#subject2_temp'), data: rep.message, context: '.xueke', overwrite: 1});
            $(".xueke label:eq(0)").addClass("active");
            var subjectList = new Array();
            var par = {};
            par.subid = "*";
            par.snm = "全部";
            subjectList.push(par);
            $.each(rep.message, function (i, st) {
                subjectList.push(st);
            });
            Common.render({tmpl: $('#subject_temp'), data: subjectList, context: '.subject'});
        })
    }

    pTeaClassTime.getSubjectListByTime = function () {
        var par = {};
        par.gradeId = $("#grade").val();
        par.xqid = deXqid;
        Common.getData('/new33isolateMange/getIsolateSubjectListByZKBGrade.do', par, function (rep) {
            var subjectList = new Array();
            var par = {};
            par.subid = "*";
            par.snm = "全部";
            subjectList.push(par);
            $.each(rep.message, function (i, st) {
                subjectList.push(st);
            });
            Common.render({tmpl: $('#subject_temp'), data: subjectList, context: '#subject',overwrite:1});
        })
    }

    pTeaClassTime.getSubjectListForExport = function () {
        var par = {};
        par.gradeId = $("#grade2").val();
        par.xqid = deXqid;
        Common.getData('/new33isolateMange/getIsolateSubjectListByZKBGrade.do', par, function (rep) {
            var subjectList = new Array();
            var par = {};
            par.subid = "*";
            par.snm = "全部";
            subjectList.push(par);
            $.each(rep.message, function (i, st) {
                subjectList.push(st);
            });
            Common.render({tmpl: $('#subject_temp'), data: subjectList, context: '#subject2',overwrite:1});
        })
    }

    pTeaClassTime.getSubjectListByTea = function () {
        var par = {};
        par.gradeId = $("#grade1").val();
        par.xqid = deXqid;
        Common.getData('/new33isolateMange/getIsolateSubjectListByZKBGrade.do', par, function (rep) {
            var subjectList = new Array();
            var par = {};
            par.subid = "*";
            par.snm = "全部";
            subjectList.push(par);
            $.each(rep.message, function (i, st) {
                subjectList.push(st);
            });
            Common.render({tmpl: $('#subject_temp'), data: subjectList, context: '.subject',overwrite:1});
        })
    }

    pTeaClassTime.getUserList = function () {
        var par = {};
        par.gid = $(".grade .active").attr("ids");
        par.subid = $(".xueke .active").attr("ids");
        par.week = $("#week").val();
        if (par.subid != undefined && par.subid != "" && par.subid != null && par.week != undefined && par.week != "" && par.week != null && par.gid != "" && par.gid != null && par.gid != undefined) {
            Common.getData('/paike/getTeaListBySubjectIdIsTeaTableWeek.do', par, function (rep) {
                $("#userCount").html(rep.message.length);
                Common.render({tmpl: $('#userList'), data: rep.message, context: '#use', overwrite: 1});
                if (rep.message.length > 0) {
                    $("#use li:eq(0)").addClass("t-li");
                }
            })
        }
    }

    pTeaClassTime.getGradeList = function () {
        var xqid = deXqid;
        if (!xqid || xqid == "" || xqid == null) {
            Common.render({tmpl: $('#gradeList'), data: [], context: '#grade', overwrite: 1});
            return;
        }
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": deXqid}, function (rep) {
            var gradeList = new Array();
            var par = {};
            par.gid = "*";
            par.gnm = "全部";
            par.jie = "";
            gradeList.push(par)
            $.each(rep, function (i, obj) {
                gradeList.push(obj);
            });
            Common.render({tmpl: $('#gradeList'), data: gradeList, context: '#grade', overwrite: 1});
            //Common.render({tmpl: $('#gradeList'), data: gradeList, context: '.grade', overwrite: 1});
            Common.render({tmpl: $('#gradeList'), data: gradeList, context: '#grade1', overwrite: 1});
            Common.render({tmpl: $('#gradeList'), data: gradeList, context: '#grade2', overwrite: 1});
            //$("#grade em:eq(0)").addClass("cur");
        })
    }

    pTeaClassTime.getListBySchoolId = function () {
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdZKB.do', {"xqid": deXqid}, function (rep) {
                // Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
                Common.render({tmpl: $('#KeShiListForSearch'), data: rep, context: '#tbdForSearch', overwrite: 1});
                Common.render({tmpl: $('#KeShiListForSearch1'), data: rep, context: '.tbdForSearch', overwrite: 1});
            });
        }
    }

    pTeaClassTime.getListBySchoolIdForKB = function () {
        var par = {};
        par.xqid = deXqid;
        par.week = $("#week").val();
        par.gradeId = $(".grade .active").attr("ids");
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdForKB.do', par, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }

    pTeaClassTime.getTeaList = function () {
        var par = {};
        par.xqid = deCid;
        par.gid = $("#grade1").val();
        par.subid = $(".subject").val();
        Common.getData('/new33isolateMange/getTeaList.do', par, function (rep) {
            Common.render({tmpl: $('#tbdList'), data: rep.message, context: '#tbd1', overwrite: 1});
        })
    }

    pTeaClassTime.getTeaListForExport = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $("#grade2").val();
        par.subid = $("#subject2").val();
        par.week =  $("#week").val();
        Common.getData('/new33isolateMange/getTeaListByXQID.do', par, function (rep) {
            Common.render({tmpl: $('#teacher'), data: rep.message, context: '#teacherList', overwrite: 1});
        })
    }

    pTeaClassTime.getPks = function () {
        $("#usPk").html($(".t-li").attr("pks"))
        $("#userName").html($(".t-li").attr("nm"))
        $("#keCount").html("-课时数:" + ksCount)
    }
    module.exports = pTeaClassTime;
})