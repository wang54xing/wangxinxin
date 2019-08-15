/**
 * Created by albin on 2018/3/19.
 */
define('dbtkClass', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var dbtkClass = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    Common = require('common');

    var deXqid = "";
    var deCid = "";
    var pkflg = false;
    var tkType = 1;

    var isSubmit = "";
    var yweek = "";


    dbtkClass.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.xqid;
            deCid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }

    dbtkClass.getGradeList = function () {
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": deXqid}, function (rep) {
            $(".gaozhong").append(" <span>年级 : </span>")
            Common.render({tmpl: $('#grade_temp'), data: rep, context: '.gaozhong'});
            $(".gaozhong label:eq(0)").addClass("active");
        })
    }

    dbtkClass.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        })
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week1', overwrite: 1});
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week2', overwrite: 1});
            $("#week option[value=" + wk + "]").prop("selected", true)
            $("#week1 option[value=" + (wk + 1) + "]").prop("selected", true)
            yweek = $('#week').val();
        })
    }

    dbtkClass.getRoomEntryListByXqGrade = function () {
        $(".jiaoshi").empty();
        if (deXqid && deXqid != null) {
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $(".gaozhong .active").attr("ids");
            Common.getData('/paike/getRoomEntryListByXqGradeAndTerm.do', par, function (rep) {
                $(".jiaoshi").append(" <span>教室 : </span>")
                Common.render({tmpl: $('#jiaos'), data: rep.message, context: '.jiaoshi'});
                $(".jiaoshi label:eq(0)").addClass("active");
            });
        }
    }

    dbtkClass.initN33_ZKBTemp = function () {
        var week = $("#week").val();
        if (deXqid && deXqid != null && week && week != null) {
            var par = {};
            par.week = week;
            par.termId = deXqid;
            Common.getData('/n33_zkbtemp/initN33_ZKBTemp.do', par, function (rep) {
            });
        }
    }



    function getStatusPK() {
        $.ajax({
            type: "GET",
            url: '/n33_zkbtemp/getStatusSave.do',
            async: false,
            cache: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {
                if (rep.st == -1) {
                    $('.bg').hide();
                    $(".jhh").hide();
                    window.clearInterval(c);
                    layer.msg("保存成功");
                } else {
                    $(".jhhSp").text("正在保存中，请稍后...");
                }
            }
        });
    }

    dbtkClass.getKeBiaoList = function (type) {
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        data.gradeId = gradeId;
        var classRoomId = "";
        $('.jiaoshi label').each(function (i) {
            if ($(this).hasClass("active")) {
                classRoomId = $(this).attr('ids');
            }
        });
        data.classRoomId = classRoomId;
        data.termId = deXqid;
        data.week = $('#week').val();
        Common.getData('/n33_zkbtemp/getKeBiaoListTemp.do', data, function (rep) {
            if (rep.code == '200') {
                if(rep.message!=null && rep.message.ykbdto!=null && rep.message.ykbdto.length!=0){
                    $('.wtkkb').hide();
                    $('.tag').show();
                    $('#paikeByRoom').show();
                    $('.fl').show();
                    $('.cont1').empty();
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
                        if ($(this).hasClass("active")) {
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
                }else{
                    $('.tag').hide();
                    $('.wtkkb').show();
                    $('.fl').hide();
                    $('#paikeByRoom').hide();
                }
            } else {

            }
            dbtkClass.getCurrentJXZ();
        })
    }

    dbtkClass.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getGradeWeek.do', {"xqid": deXqid}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial;
                var index2 = rep.serial + 1;
                $(".citt[x=" + index + "]").addClass("gray")
                $(".citt[x=" + index2 + "]").addClass("gray")
            }
        });
    }

    dbtkClass.reloadKB_Temp = function (type) {
        var par = {};
        par.termId = deXqid;
        par.week = $('#week').val();
        par.type = $('#cd .active').attr("tk");
        Common.getData('/n33_zkbtemp/reloadKB_Temp.do', par, function (rep) {
            if(rep.code == 200){
                dbtkClass.initN33_ZKBTemp();
                dbtkClass.getKeBiaoList();
                dbtkClass.getCurrentJXZ();
                dbtkClass.getJXBList();
                if(type == 1){

                }else{
                    layer.msg("课表已还原")
                }
            }else{
                layer.msg("网络错误")
            }

        });
    }

    dbtkClass.initIsSubmit = function () {
        var par = {};
        par.termId = deXqid;
        par.week = $('#week').val();
        var tk = $('#cd .active').attr("tk");
        par.tk = tk;
        Common.getData('/n33_zkbtemp/initIsSubmit.do', par, function (rep) {

        });
    }

    dbtkClass.getCount = function (yweek,tk) {
        var par = {};
        par.termId = deXqid;
        par.week = yweek;
        par.tk = tk;
        Common.getData('/n33_zkbtemp/getCount.do', par, function (rep) {
            isSubmit = rep.message;
        });
    }

    dbtkClass.updateCount = function (submit) {
        var par = {};
        par.termId = deXqid;
        par.week = $('#week').val();
        var tk = $('#cd .active').attr("tk");
        par.tk = tk;
        par.submit = submit;
        Common.getData('/n33_zkbtemp/updateCount.do', par, function (rep) {
            isSubmit = rep.message;
        });
    }

    dbtkClass.judgeIsCanSave = function () {
        var par = {};
        par.termId = deXqid;
        par.week = $('#week').val();
        par.type = $('#cd .active').attr("tk");
        Common.getData('/n33_zkbtemp/judgeIsCanSave.do', par, function (rep) {
            if(par.type == 1){
                if(rep.message){
                    dbtkClass.updateCount(true);
                    dbtkClass.saveKeBiao();
                }else{
                    layer.alert("存在待调课程，不允许提交")
                }
            }else{
                if(rep.message){
                    $('#week2').val($('#week').val());
                    $('.z-popup2').show();
                    $('.bg').show();
                }else{
                    layer.alert("存在待调课程，不允许提交")
                }
            }
        });
    }


    dbtkClass.init = function () {
        dbtkClass.getDefaultTerm();
        dbtkClass.getGradeList();
        dbtkClass.getRoomEntryListByXqGrade();
        dbtkClass.getListByXq();
        dbtkClass.initN33_ZKBTemp();
        dbtkClass.getKeBiaoList();
        dbtkClass.getCurrentJXZ();
        dbtkClass.getJXBList();
        dbtkClass.initIsSubmit();
        dbtkClass.getCount(yweek,$('#cd .active').attr('tk'));
        $('.qxx,.close').click(function () {
            $('.bg').hide();
            $('.bf-popup').hide();
            $('.bt-popup').hide();
            $('.fb-popup').hide();
            $('.z-popup').hide();
            $('.z-popup1').hide();
            $('.z-popup2').hide();
            $('.z-popup3').hide();
        });

        $('body').on("click",'.b8',function () {
            pkflg = false;
            dbtkClass.updateCount(true);
            dbtkClass.reloadKB_Temp();
            $('.cont').removeClass('e9');
        })
        $('body').on("click",'.b5',function () {
            if(tkType == 1){
                dbtkClass.judgeIsCanSave();
            }else{
                dbtkClass.judgeIsCanSave();
            }
            $('.cont').removeClass('e9');
        })



        $('body').on("click",".cx",function () {
            $('.cont').removeClass('e9');
            $('.cont1').empty();
            if ($('.actv') == null || $('.actv').length == 0) {
                layer.alert("请选择撤销课！");
            } else {
                var zkbId = $('.actv').attr("ykbId");
                if(zkbId == undefined || zkbId == ""){
                    layer.alert("请选择撤销课！");
                }else{
                    dbtkClass.updateCount(false);
                    dbtkClass.undoYKB(zkbId);
                }

            }
        });


        $('body').on("click",'.gaozhong label',function () {
            $('.gaozhong label').each(function (i) {
                $(this).removeClass('active');
                $('.cont').removeClass('e9');
            })
            $(this).addClass('active');
            dbtkClass.getRoomEntryListByXqGrade();
            dbtkClass.getListByXq();
            dbtkClass.getKeBiaoList();
            dbtkClass.getCurrentJXZ();
            dbtkClass.getJXBList();
        })

        $('body').on("click",'.jiaoshi label',function () {
            $('.jiaoshi label').each(function (i) {
                $(this).removeClass('active');
                $('.cont').removeClass('e9');
            })
            $(this).addClass('active');
            dbtkClass.getListByXq();
            dbtkClass.getKeBiaoList();
            dbtkClass.getCurrentJXZ();
            dbtkClass.getJXBList();
        })

        $("#week").change(function () {
            dbtkClass.getCount(yweek,$('#cd .active').attr('tk'));
            if(isSubmit){
                var wk = $('#week').val();
                var week = parseInt(wk);
                week += 1;
                $('#week1').val(week);
                if($('#week1').val() == null){
                    $('#week1').val(wk);
                }
                dbtkClass.reloadKB_Temp(1);
                dbtkClass.getKeBiaoList();
                dbtkClass.getCurrentJXZ();
                dbtkClass.getJXBList();
                $('.cont').removeClass('e9');
                yweek = $('#week').val();
            }else{
                $('#week').val(yweek);
                layer.alert("请执行保存提交操作或还原课表至初始状态后进行切换");
            }
        })

        $("#week1").change(function () {
            var wk = $('#week').val();
            var wk1 = $('#week1').val();
            var week = parseInt(wk);
            var week1 = parseInt(wk1);
            var changeWeek = week + 1;
            if(week > week1){
                layer.alert("起始周不能大于结束周");
                $('#week1').val(changeWeek)
            }
        })
        $('body').on("click","#cd span",function () {
            dbtkClass.getCount(yweek,$('#cd .active').attr('tk'));
            if(isSubmit){
                if($('.cx li').length == 0){
                    $(this).addClass("active").siblings().removeClass("active");
                    if($(this).attr("tk") == 2){
                        tkType = 2;
                        layer.msg("已调为长期调课");
                        dbtkClass.getJXBList();
                        dbtkClass.initIsSubmit();
                    }else{
                        tkType = 1;
                        layer.msg("已调为短期调课");
                        dbtkClass.initIsSubmit();
                    }
                }
            }else{
                layer.alert("请执行保存提交操作或还原课表至初始状态后进行切换");
            }
        });

        //点击课表表格
        $('body').on('click', '.tab2 td>div>div', function () {
            if ($(this).hasClass('red')) {

            } else {
                if ($(this).hasClass('actv')) {
                    $(this).removeClass('actv');
                    $('.jxbs').empty();
                    $('.xkb').empty();
                    $('.cont1').empty();
                    $('.cont').removeClass('e9');
                } else {
                    $('.tab2 td>div>div').each(function (i) {
                        $(this).removeClass('actv');
                    });
                    if (!pkflg && $(this).attr("type") != 0 && $(this).attr("type") != undefined && $(this).attr("type") != null) {
                        if ($(this).hasClass('actv')) {
                            $(this).removeClass('actv');
                            $('.cont1').empty();
                            $('.cont').removeClass('e9');
                        } else {
                            $(this).addClass('actv');
                            $('.cont').addClass('e9');
                            dbtkClass.getJXBById($(this).attr('ykbid'),$(this).attr("x"),$(this).attr("y"));
                        }
                    }else if(!pkflg && ($(this).attr("type") == 0 || $(this).attr("type") == undefined || $(this).attr("type") == null)){
                        if ($(this).hasClass('actv')) {
                            $(this).removeClass('actv');
                            $('.cont').removeClass('e9');
                            $('.cont1').empty();
                        } else {
                            $(this).addClass('actv');
                            $('.cont').addClass('e9');
                            dbtkClass.getJXBByXY($(this).attr("x"),$(this).attr("y"));
                        }
                        // if(colorFlag){
                        //     $('.cont').toggleClass('e9');
                        // }
                        // $(this).toggleClass('active');
                    }
                }

            }
        });

        $('body').on("click",'.b2',function () {
            $('.cont').removeClass('e9');
            if($('.actv').length == 0 || $('.actv') == null){
                layer.alert("请选择撤销课")
            }else{
                var zkbId = $('.actv').attr("ykbId");
                dbtkClass.updateCount(false);
                dbtkClass.undoYKB(zkbId);
                // if($('#cd active').attr("tk") == 2){
                //     alert(1)
                // }
            }
        });


        $('body').on('click', '.kpjxblist li', function (e) {
            $('.cont').removeClass('e9');
            e.stopPropagation();
            if ($(this).hasClass('jxbbac')) {
                $(this).removeClass('jxbbac');
                pkflg = false;
                dbtkClass.getKeBiaoList(2);
                dbtkClass.getZouBanTimeList(1);
            } else {
                $(this).addClass('jxbbac').siblings().removeClass('jxbbac');
                dbtkClass.getKeBiaoList(2);
                var classRoomId = "";
                $('.classrooms li').each(function (i) {
                    if ($(this).hasClass("t-li")) {
                        classRoomId = $(this).attr('ids');
                    }
                });
                jxbId = $(this).attr('jxbid');
                var tid = $(this).attr('tid');
                $('.tb-clsrom .vjs').each(function (i) {
                    if (tid == $(this).attr('tid')) {
                        $(this).addClass("fontcolor");
                    } else {
                        $(this).removeClass("fontcolor");
                    }
                });
                dbtkClass.getConflictedSettledJXB($(this).attr('jxbid'));
            }
        });

        $('body').on('click', '.tb-clsrom', function () {
            if (pkflg) {
                $('.cont').removeClass('e9');
                if (!$(this).hasClass("gray")) {
                    var obj = $(this);
                    if(obj.hasClass("red")){
                        layer.confirm('排课时间存在事务，是否排课？', {
                            btn: ['确定', '取消'] //按钮
                        }, function () {
                            dbtkClass.saveZKB_Temp(obj.attr('ykbid'), jxbId);
                        },function () {
                        });
                    }else{
                        dbtkClass.updateCount(false);
                        dbtkClass.saveZKB_Temp(obj.attr('ykbid'), jxbId);
                    }
                }
            }
        });

        $('body').on("click",'#qd',function () {
            $('.z-popup2').hide();
            $(".jhh").show();
            $('.bg').show();
            $(".jhhSp").text("正在保存中，请稍候...");
            setTimeout(function () {
                dbtkClass.updateCount(true);
                dbtkClass.saveKeBiao();
                c = setInterval(getStatusPK, 100);
            })
            $('.cont').removeClass('e9');
        })
    }

    dbtkClass.saveKeBiao = function () {
        var week = $("#week").val();
        if (deXqid && deXqid != null && week && week != null) {
            var par = {};
            par.week = week;
            par.termId = deXqid;
            par.tkType = tkType;
            par.eWeek = $('#week1').val();
            Common.getData('/n33_zkbtemp/saveKeBiao.do', par, function (rep) {
                layer.msg(rep.message.msg)
                if(tkType == 2){
                    if(rep.message.list.length > 0){
                        $('.z-popup3').show();
                        $('.bg').show();
                        $('#weeks').text("第" + rep.message.canNotTKString + "周调课未能成功");
                        Common.render({tmpl: $('#list_temp'), data: rep.message.list, context: '#list'});
                    }
                }
            });
        }
    }

    dbtkClass.undoYKB = function (zkbId) {
        var par = {};
        par.zkbId = zkbId;
        par.type = $('#cd .active').attr("tk");
        Common.getData('/n33_zkbtemp/undoZKB_Temp.do', par, function (rep) {
            if (rep.code == '200') {
                layer.msg("撤销成功！");
                $('.jxbs').empty();
                $('.xkb').empty();
                $('.cont1').empty();
                dbtkClass.getKeBiaoList();
                dbtkClass.getCurrentJXZ();
                dbtkClass.getJXBList();
            } else {
                layer.msg("撤销失败！");
            }
        });
    }

    dbtkClass.getJXBList = function () {
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        var classRoomId = "";
        $('.jiaoshi label').each(function (i) {
            if ($(this).hasClass("active")) {
                classRoomId = $(this).attr('ids');
            }
        });
        par.classRoomId = classRoomId;
        par.week = $('#week').val();
        par.zhenXqid = deXqid;
        par.type = $('#cd .active').attr("tk");
        Common.getData('/n33_zkbtemp/getJXBList.do', par, function (rep) {
            Common.render({tmpl: $('#kpjxblist_temp'), data: rep.message, context: '.kpjxblist',overwrite:1});
        });
    }

    dbtkClass.saveZKB_Temp = function (zkbId, jxbId) {
        var data = {};
        data.zkbId = zkbId;
        data.jxbId = jxbId;
        data.week = $('#week').val();
        data.type = $('#cd .active').attr("tk");
        Common.getData('/n33_zkbtemp/saveZKB_Temp.do', data, function (rep) {
            if (rep.code == '200') {
                layer.msg("放入成功！");
                dbtkClass.getKeBiaoList();
                dbtkClass.getCurrentJXZ();
                dbtkClass.getJXBList();
                pkflg = false;
                colorFlag = false;
                jxbId = "";
            }
        });
    }

    dbtkClass.getConflictedSettledJXB = function (jxbId) {
        // dbtkClass.lianTangs(jxbId);
        var data = {};
        data.jxbId = jxbId;
        data.zhenXqid = deXqid;
        var classRoomId = "";
        $('.jiaoshi label').each(function (i) {
            if ($(this).hasClass("active")) {
                classRoomId = $(this).attr('ids');
            }
        });
        data.classRoomId = classRoomId;
        data.week = $('#week').val();
        Common.getData('/n33_zkbtemp/getConflictedSettledJXBByRoomId.do', data, function (rep) {
            if (rep.code == '200') {
                $('.jxbs').empty();
                $('.xkb').empty();
                $('.cont1').empty();
                pkflg = true;
                var sws = rep.message.swcts;
                if (sws != null && sws.length != 0) {
                    for (var i = 0; i < sws.length; i++) {
                        var dt = "idx" + (sws[i].y - 1) + (sws[i].x - 1);
                        if($("." + dt + " .ls").length == 0){
                            $("." + dt).addClass("red");
                        }
                        var string = $("." + dt).attr("title");
                        if(!string || string == null){
                            string = sws[i].desc;
                        }else if(string || string != null){
                            string += "\r\n";
                            string += sws[i].desc;
                        }
                        $("." + dt).attr("title",string);
                    }
                }
                var gdsx = rep.message.gdsxs;
                if (gdsx != null && gdsx.length != 0) {
                    for (var i = 0; i < gdsx.length; i++) {
                        var dt = "idx" + gdsx[i].x + gdsx[i].y;
                        if($("." + dt + " .ls").length == 0){
                            $("." + dt).addClass("red");
                            $("." + dt).text(gdsx[i].desc);
                        }
                        var string = $("." + dt).attr("title");
                        if(!string || string == null){
                            string = gdsx[i].desc;
                        }else if(string || string != null){
                            string += "\r\n";
                            string += gdsx[i].desc;
                        }
                        $("." + dt).attr("title",string);
                    }
                }
                if (rep.message.jxbcts != null && rep.message.jxbcts.length != 0) {
                    for (var j = 0; j < rep.message.jxbcts.length; j++) {
                        var string = "";
                        var dt = "idx" + (rep.message.jxbcts[j].x) + (rep.message.jxbcts[j].y);
                        //if (!$("." + rep.message.jxbcts[j].classroomId + " ." + dt).hasClass('red')) {
                        if($("." + rep.message.jxbcts[j].classroomId + " ." + dt).hasClass('red')){
                            $("." + rep.message.jxbcts[j].classroomId + " ." + dt).removeClass('red');
                            string = $("." + rep.message.jxbcts[j].classroomId + " ." + dt).attr("title");
                            $("." + rep.message.jxbcts[j].classroomId + " ." + dt).text("");
                        }
                        $("." + rep.message.jxbcts[j].classroomId + " ." + dt).addClass("gray");
                        if ($("." + rep.message.jxbcts[j].classroomId + " ." + dt + " p").hasClass('pic')) {
                            // $("."+rep.message.jxbcts[j].classroomId+" ."+dt +" .ls").text(rep.message.jxbcts[j].remarks);
                        } else {
                                if(rep.message.jxbcts[j].ctString != null){
                                    $.each(rep.message.jxbcts[j].ctString,function (i,st) {
                                        if(string == ""){
                                            string += st;
                                        }else{
                                            string += "\r\n";
                                            string += st;
                                        }
                                    });
                                }
                                $("." + rep.message.jxbcts[j].classroomId + " ." + dt).attr("title",string);

                        }
                        //}
                    }
                }
                dbtkClass.getZouBanTimeList(1,jxbId);
            }
        });
    }

    dbtkClass.getZouBanTimeList = function (type,jxbId) {
        var par = {}
        var gid = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gid = $(this).attr('ids');
            }
        })
        par.jxbId = jxbId;
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
                                if (($(this).find("p").length == 0 && !$(this).hasClass("red")) || $(this).attr("kbType") == 3) {
                                    $(this).append('<p class="hzb"></p>');
                                }
                            }
                        });
                    }
                });

            });
        }
    }



    //源课表格子所有课(根据xy)
    dbtkClass.getJXBByXY = function (x, y) {
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
        par.week = $("#week").val();
        par.xqid = deXqid;
        Common.getData('/dbtk/getJXBByXY.do', par, function (rep) {
            $('.jxbs').empty();
            Common.render({tmpl: $('#jxbs_templ'), data: rep.message.jxbdtos, context: '.jxbs'});
            if (rep.message.teas != null && rep.message.teas.length != 0) {
                $('.cont1').empty();
                Common.render({tmpl: $('#xkb_temp'), data: rep.message.teas, context: '.cont1'});
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
                                string += "教室：" + value[i].roomName;
                                $('.' + cls + '').attr("title",string);
                            }else{
                                $('.' + cls + '').attr("title","教学班：" + value[i].jxbName + "\r\n" + "教室：" + value[i].roomName);
                            }
                        }
                    }
                })
            }
        });
    }

    dbtkClass.getJXBById = function (ykbId,x,y) {
        var par = {};
        par.zkbId = ykbId;
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.week = $("#week").val();
        par.gradeId = gradeId;
        Common.getData('/dbtk/getJXBById.do', par, function (rep) {
            $('.jxbs').empty();
            Common.render({tmpl: $('#jxbs_templ'), data: rep.message.jxbdtos, context: '.jxbs'});
            $('.cont1').empty();
            if (rep.message.teas != null && rep.message.teas.length != 0) {
                Common.render({tmpl: $('#xkb_temp'), data: rep.message.teas, context: '.cont1'});
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
                                string += "教室：" + value[i].roomName;
                                $('.' + cls + '').attr("title",string);
                            }else{
                                $('.' + cls + '').attr("title","教学班：" + value[i].jxbName + "\r\n" + "教室：" + value[i].roomName);
                            }
                        }
                    }


                })
            }
        });
    }



    dbtkClass.initGrade = function () {
        try {
            Common.getData('/udc/data.do', {"pageTag":"n33"}, function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                    })

                    $(".gaozhong label").each(function () {
                        if (gradeId == $(this).attr("ids")) {
                            $(this).addClass("active");
                        }
                    });
                } else {

                }
            });
        } catch (x) {
            console.log(x);
        }
    }

    module.exports = dbtkClass;
})