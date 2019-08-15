/**
 * Created by albin on 2018/3/23.
 */
define('teaCLassTime', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
        var teaCLassTime = {};
        require('jquery');
        require('doT');
        require('pagination');
        require('layer');
        require('Rome');
        Common = require('common');
        var deXqid = "";
        var Xqid = "";

        var desc = "";

        teaCLassTime.getDESCForNoCourse = function () {
            var par = {};
            par.ciId = deXqid;
            par.gradeId = $("#gradeTurnOff").val();
            par.type = 3;
            par.kbType = $('#kbType').val();
            Common.getData('/n33_set/getDESCForNoCourse.do', par, function (rep) {
                $('#desc').val(rep.message)
            });
        }

        teaCLassTime.getGDSX = function () {
            var par = {};
            par.xqid = Xqid;
            par.userId = $(".t-li").attr("ids");
            Common.getData('/gdsx/getGDSXBySidAndXqidTea.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    var x=obj.y + 1;
                    var y=obj.x + 1;
                    $(".itt[x=" + x + "][y=" + y + "]").html(obj.desc)
                    $(".itt[x=" + x + "][y=" + y + "]").css("background", "#ccc")
                })
            })
        }
        teaCLassTime.GetTeachersSettledPositions = function () {
            var par = {};
            par.teacherId = $(".t-li").attr("ids");
            $(".itt").attr("jxbId", "*");
            $(".itt").css("background", "#FFF")
            Common.getData('/paike/GetTeachersSettledPositions.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("jxbId", obj.jxbId)
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.JxbName + "(" + obj.count + ")" + "</br>" + obj.subName + "</br>" + obj.gradeName + "</br>" + obj.roomName)
                })
            })
        }
        teaCLassTime.init = function () {
            teaCLassTime.getDefaultTerm();
            teaCLassTime.getListBySchoolId();
            teaCLassTime.getSubjectList();
            teaCLassTime.getUserList();
            teaCLassTime.getChushi();
            teaCLassTime.GetTeachersSettledPositions();
            teaCLassTime.getTeaShiWuByXqid();
            teaCLassTime.getGuDingShiWu();
            teaCLassTime.getGDSX();
            $("body").on("click", ".itt", function () {
                $('.itt').each(function (i) {
                    $(this).removeClass("active");
                });
                var jxbId = $(this).attr('jxbId');
                if (jxbId != '*') {
                    $('.itt').each(function (i) {
                        if (jxbId == $(this).attr('jxbId')) {
                            $(this).addClass("active");
                        }
                    });
                }

            });
            $("body").on("click", "#use li", function () {
                $("#use li").removeClass("t-li");
                $(this).addClass("t-li");
                teaCLassTime.getPks();
                $(".itt").html("")
                teaCLassTime.GetTeachersSettledPositions();
                teaCLassTime.getTeaShiWuByXqid();
                teaCLassTime.getGuDingShiWu()
                teaCLassTime.getGDSX();
            })


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
            $("body").on("click", ".xueke label", function () {
                $(".xueke label").removeClass("active");
                $(this).addClass("active");
                $("#usPk").html("")
                $("#userName").html("")
                teaCLassTime.getUserList();
                $(".itt").html("")
                teaCLassTime.GetTeachersSettledPositions();
                teaCLassTime.getTeaShiWuByXqid();
                teaCLassTime.getGuDingShiWu()
                teaCLassTime.getGDSX();
            })
        }

        teaCLassTime.getDefaultTerm = function () {
            Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
                $("#defaultTerm").text(rep.message.paikeciname);
                deXqid = rep.message.paikeci;
                Xqid = rep.message.paikexq;
                $("#defaultTerm").attr("ids", rep.message.xqid);
            });
        }

        teaCLassTime.getChushi = function () {
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

        teaCLassTime.getGuDingShiWu = function () {
            Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": Xqid}, function (rep) {
                $.each(rep.message, function (i, obj) {
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
                })
            })
        }
        teaCLassTime.getTeaShiWuByXqid = function () {
            var par = {}
            par.xqid = Xqid;
            par.teaId = $(".t-li").attr("ids");
            Common.getData('/new33isolateMange/getTeaShiWuByXqid.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                    $(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
                })
            })
        }
        teaCLassTime.getSubjectList = function () {
            Common.getData('/new33isolateMange/getIsolateSubjectListByXq.do', {"xqid": deXqid}, function (rep) {
                $(".xueke").append(" <span>学科: </span>")
                Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.xueke'});
                $(".xueke label:eq(0)").addClass("active");
            })
        }
        teaCLassTime.getUserList = function () {
            var par = {};
            par.subid = $(".xueke .active").attr("ids");
            Common.getData('/paike/getTeaListBySubjectIdIsTeaTable.do', par, function (rep) {
                $("#userCount").html(rep.message.length);
                Common.render({tmpl: $('#userList'), data: rep.message, context: '#use', overwrite: 1});
                $("#use li:eq(0)").addClass("t-li");
                teaCLassTime.getPks();
            })
        }

        teaCLassTime.getListBySchoolId = function () {
            if (deXqid && deXqid != null) {
                Common.getData('/courseset/getListBySchoolId.do', {"xqid": deXqid}, function (rep) {
                    Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
                });
            }
        }
        teaCLassTime.getPks = function () {
            $("#usPk").html($(".t-li").attr("pks"))
            $("#userName").html($(".t-li").attr("nm"))
        }
        module.exports = teaCLassTime;
    }
)