/**
 * Created by albin on 2018/3/30.
 */
define('jxbTimeCombine', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var jxbTimeCombine = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var paikeci = "";
    var zuHeId = "";
    var zuHeName = "";

    jxbTimeCombine.init = function () {
        $('body').on('click', '#bq', function () {
            var tagId = $(this).attr("tagId");
            $('.bg').show();
            $('.dialog-tag').show();
            $("#tagName").html($(this).attr("name"))
            var par = {};
            par.id = tagId;
            Common.getData('/studentTag/getStuSign.do', par, function (rep) {
                Common.render({tmpl: $('#st_temp'), data: rep.message, context: '#stList', overwrite: 1});
            })
        })

        $('body').on('click', '.selectJxb', function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            // e.stopPropagation();
            $('.dialog-jxb').show();
            var jxbId = $(this).attr('jxbId');
            var par = {};
            par.ciId = paikeci;
            par.tagId = $(this).attr('tagId');
            par.gradeId = $('.grade').val();
            par.type = $('#jxbType').val();
            $('#jxb_qd').attr('serial',$(this).attr('serial'));
            $('#jxb_qd').attr('tagId',$(this).attr('tagId'));
            par.serial = $(this).attr('serial');
            Common.getData('/timeCombine/querySelectJXB.do', par, function (rep) {
                Common.render({tmpl: $('#selectJxb_temp'), data: rep.message, context: '#selectJxb', overwrite: 1});
                $('#selectJxb').val(jxbId);
            })
        })

        $('body').on('click', '#jxb_qd', function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            // e.stopPropagation();
            $('.dialog-jxb').hide();
            $('.bg').hide();
            var par = {};
            par.ciId = paikeci;
            par.tagId = $(this).attr('tagId');
            par.serial = $(this).attr('serial');
            par.type = $('#jxbType').val();
            par.jxbId = $('#selectJxb').val();
            par.gradeId = $('.grade').val();
            Common.getData('/timeCombine/saveJXB.do', par, function (rep) {
                jxbTimeCombine.getTimeCombineHeadNameList();
                jxbTimeCombine.getTimeCombine();
            })
        })

        $('body').on('click', '.delJxb', function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            // e.stopPropagation();
            var par = {};
            par.ciId = paikeci;
            par.tagId = $(this).attr('tagId');
            par.serial = $(this).attr('serial');
            par.type = $('#jxbType').val();
            par.gradeId = $('.grade').val();
            Common.getData('/timeCombine/delJXB.do', par, function (rep) {
                jxbTimeCombine.getTimeCombineHeadNameList();
                jxbTimeCombine.getTimeCombine();
            })
        })

        $('body').on("change",".grade",function () {
            jxbTimeCombine.initTimeCombine();
            jxbTimeCombine.getTimeCombineHeadNameList();
            jxbTimeCombine.getTimeCombine();
        });

        $('body').on('change','#jxbType',function () {
            jxbTimeCombine.getTimeCombineHeadNameList();
            jxbTimeCombine.getTimeCombine();
        })

        // 教师设置
        $("body").on("click", ".selectTea", function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            if($(this).attr("jxbId") == "undefined"){
                layer.msg("请先设置教学班");
                return;
            }
            $("#tea_qd").attr("jxbId", $(this).attr("jxbId"));
            if($(this).attr("teaId") != "undefined"){
                var tid = $(this).attr("teaId");
                var subId = $(this).attr("subId");
                var serial = $(this).attr("serial");
                layer.confirm('修改教师会撤销该教学班课表，确定修改？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    $("#teacher").val(tid);
                    $('.layui-layer').hide();
                    $('.layui-layer-shade').hide();
                    $('.bg').show();
                    $('.dialog-teacher').show();
                    jxbTimeCombine.getTeacherList(subId,serial);
                    $("#teacher").val(tid);
                },function () {
                });
            }else{
                jxbTimeCombine.getTeacherList($(this).attr("subId"),$(this).attr("serial"));
                $('.bg').show();
                $('.dialog-teacher').show();
            }
        });

        $('body').on("click","#tea_qd",function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            jxbTimeCombine.setJxbTeacher($("#tea_qd").attr("jxbId"));
            $('.bg').hide();
            $('.dialog-teacher').hide();
        });

        $('body').on("click",".delTea",function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            jxbTimeCombine.delJxbTeacher($(this).attr("jxbId"));
        });

        $('body').on("click",".delRoom",function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            jxbTimeCombine.delJxbRoom($(this).attr("jxbId"));
        });

        // 教室设置
        $("body").on("click", ".selectRoom", function () {
            if($(this).attr("jxbId") == "undefined"){
                layer.msg("请先设置教学班");
                return;
            }
            $("#set_room").attr("jxbId", $(this).attr("jxbid"));
            if($(this).attr("rid") != "undefined"){
                var rid = $(this).attr("rid");
                var serial = $(this).attr('serial');
                layer.confirm('修改教室会撤销该教学班课表，确定修改？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    $('.layui-layer').hide();
                    $('.layui-layer-shade').hide();
                    jxbTimeCombine.getClassRoomList(serial);
                    $("#room").val(rid);
                    $('.bg').show();
                    $('.dialog-house').show();
                },function () {
                });
            }else{
                jxbTimeCombine.getClassRoomList($(this).attr('serial'));
                $("#room").val($(this).attr("rid"));
                $('.bg').show();
                $('.dialog-house').show();
            }
        });

        $("#set_room").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            jxbTimeCombine.setJxbRoom($("#set_room").attr("jxbid"));
            $('.bg').hide();
            $('.dialog-house').hide();
        });

        $('body').on("click",'#clear',function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            $('.dialog-clear').show();
            $('#qd_clear').attr("serial",$(this).attr('serial'))
        });

        $('body').on("click",'#qd_clear',function () {
            $('.bg').hide();
            $('.dialog-clear').hide();
            var par = {};
            par.ciId = paikeci;
            par.gradeId = $('.grade').val();
            par.type = $('#jxbType').val();
            par.serial = $(this).attr("serial");
            Common.getData('/timeCombine/clearColumn.do',par, function (rep) {
                if (rep.code == 200) {
                    jxbTimeCombine.getTimeCombineHeadNameList();
                    jxbTimeCombine.getTimeCombine();
                }
            })
        });

        $('body').on("click",'#delZh',function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            $('.dialog-del').show();
            jxbTimeCombine.getTimeCombineHeadNameList();
        });

        $('body').on("click",'#del_zuhe',function () {
            $('.bg').hide();
            $('.dialog-del').hide();
            var par = {};
            par.ciId = paikeci;
            par.gradeId = $('.grade').val();
            par.type = $('#jxbType').val();
            par.serial = $('#headNameList1').val();
            Common.getData('/timeCombine/delZuHe.do',par, function (rep) {
                if (rep.code == 200) {
                    jxbTimeCombine.getTimeCombineHeadNameList();
                    jxbTimeCombine.getTimeCombine();
                    $('#jxb1').hide();
                    $('#jxb2').show();
                }
            })
        });

        $('body').on('click','.pkzh',function () {
            var name = $(".grade option[value="+$('.grade').val()+"]").text();
            $('#gradeName').text('年级：' + name);
            var par = {};
            par.ciId = paikeci;
            par.gradeId = $('.grade').val();
            var flag = true;
            var list = null;
            Common.getData('/timeCombine/checkZuHeData.do',par, function (rep) {
                if (rep.code == 200) {
                    flag = rep.message.flag;
                    list = rep.message.list;
                }
            })
            if(flag){
                jxbTimeCombine.initZuHeList();
                jxbTimeCombine.getList();
                $('#content1').hide();
                $('#content2').show();
            }else{
                $('.bg').show();
                $('.z-popup4').show();
                Common.render({tmpl: $('#list_temp'), data: list, context: '#list', overwrite: 1});
            }
        });

        $('.Ulprev').click(function(){
            $('#content2').show();
            $('#content3').hide();
        })

        $('body').on('click','.paik',function(){
            $(".jhh").show();
            $('.bg').show();
            $(".jhhSp").text("正在检测冲突，请稍候...");
            var id = $(this).attr("ids");
            var zuHeName = $(this).attr("zuHename");
            setTimeout(function () {
                var ids = id;
                jxbTimeCombine.getGradeWeek();
                jxbTimeCombine.getListBySchoolIdTime();
                jxbTimeCombine.ChuShi();
                jxbTimeCombine.getZouBanTimeList();
                jxbTimeCombine.getKbByGroup(zuHeName,ids);
                jxbTimeCombine.getZuHeConflictedSettledJXB(ids);
                jxbTimeCombine.getZuHeJXBList(zuHeName,ids);
                $('#content2').hide();
                $('#content3').show();
                $(".bg").hide();
                $(".jhh").hide();
            },500);

        })

        $('body').on("click",'#newZh',function () {
            var par = {};
            par.ciId = paikeci;
            par.gradeId = $('.grade').val();
            par.type = $('#jxbType').val();
            Common.getData('/timeCombine/addZuHe.do',par, function (rep) {
                if (rep.code == 200) {
                    jxbTimeCombine.getTimeCombineHeadNameList();
                    jxbTimeCombine.getTimeCombine();
                }
            })
        });

        $('body').on('change','#jxbType1',function () {
            jxbTimeCombine.getList();
        });

        var jxbId = "";
        $('body').on('click','.sign-sp',function () {
            $('#jxbTeaName').text($(this).text());
            jxbId = $(this).attr('ids');
            jxbTimeCombine.getClassList();
            jxbTimeCombine.getStuSelectResultByClassForZhuanXiang();
            jxbTimeCombine.getTagName();
            jxbTimeCombine.getJxbStudentCount(jxbId);
            jxbTimeCombine.getJxbStudent(jxbId);
            $('.dialog-sign').show();
        });

        $('body').on("change","#cla",function () {
            jxbTimeCombine.getJxbStudent(jxbId);
        });
        $('body').on("change","#selCom",function () {
            jxbTimeCombine.getJxbStudent(jxbId);
        });
        $('body').on("change","#tag",function () {
            jxbTimeCombine.getJxbStudent(jxbId);
        });

        $("body").on("click",".jxbs",function () {
            zuHeName = $(".active1").attr("zuHename");
            zuHeId = $(".active1").attr("ids");
            if($(".active1").length == 0){
                layer.alert("请选中组合");
                return;
            }else{
                if($(this).find(".has").length > 0 && $(this).find(".ypks").attr("ypks") >= $(this).find(".zks").attr("zks")){
                    layer.alert("该教学班课时已满");
                    return;
                }else if($(this).find(".has").length > 0 && $(this).find(".ypks").attr("ypks") < $(this).find(".zks").attr("zks")) {
                    jxbTimeCombine.saveZuHeJXBByJXBID($(this).attr("ids"));
                }else if($(this).find(".has").length == 0){
                    jxbTimeCombine.clearZuHeJXBByJXBID($(this).attr("ids"));
                }
            }
        });

        $("body").on("click","#cancel",function () {
            if($(".active1").length == 0){
                layer.alert("请选择撤销的组合");
                return;
            }
            var par = {};
            $('.active1').each(function (i) {
                $(this).text("");
                zuHeId = $(this).attr("ids");
                zuHeName = $(this).attr("zuHename");
                par.x = $(this).attr("x");
                par.y = $(this).attr("y");
            });
            par.zuHeId = zuHeId;
            par.xqid = paikeci;
            par.gradeId = $(".grade").val();
            Common.getData('/n33PaikeZuHe/clearKeBiaoByGroup.do', par, function (rep) {
                if (rep.code == '200') {
                    layer.msg("撤销成功！");
                    jxbTimeCombine.getKbByGroup(zuHeName,zuHeId);
                    jxbTimeCombine.getZuHeJXBList(zuHeName,zuHeId);
                    $('.active1').each(function (i) {
                        $(this).removeClass('active1')
                    });
                }
            });
        });

        $("body").on("click",".itt",function () {
            var pkFlag = false;
            $(".jxbs").each(function () {
                if(!$(this).find("div").hasClass("gray") && !$(this).find("div").hasClass("red")){
                    pkFlag = true;
                }
            });
            if(!pkFlag && !$(this).hasClass("gray") && $(this).find(".ls").length == 0){
                layer.alert("该组合教学班课时已满");
                return;
            }

            if(!$(this).hasClass("gray") && $(this).find(".ls").length == 0 && !$(this).hasClass("red")){
                var par = {};
                zuHeId = $(this).attr("ids");
                zuHeName = $(this).attr("zuHename");
                par.zuHeId = $(this).attr("ids");
                par.xqid = paikeci;
                par.gradeId = $(".grade").val();
                par.type = $("#jxbType1").val();
                par.x = $(this).attr("x");
                par.y = $(this).attr("y");
                Common.getData('/n33PaikeZuHe/saveKeBiaoByGroup.do', par, function (rep) {
                    if (rep.code == '200') {
                        layer.msg("放入成功！");
                        jxbTimeCombine.getKbByGroup(zuHeName,zuHeId);
                        jxbTimeCombine.getZuHeJXBList(zuHeName,zuHeId);
                    }
                });
            }else if($(this).find(".ls").length > 0 && !$(this).hasClass('red') && !$(this).hasClass("gray")){
                var flag = true;
                if(!$(this).hasClass("active1")){
                    flag = false;
                }
                $('.itt').each(function (i) {
                    $(this).removeClass('active1');
                });
                if(flag){
                    $(this).removeClass("active1");
                }else{
                    $(this).addClass("active1");
                }

                if($(this).hasClass("active1")){
                    $(".jxbs").each(function () {
                        $(this).find("div").removeClass("has");
                    });
                    jxbTimeCombine.getXYJXBList();
                }else{
                    $(".jxbs").each(function () {
                        $(this).find("div").removeClass("has");
                    });
                }

            }

        });


        jxbTimeCombine.getDefaultTerm();
        jxbTimeCombine.getGrade();
        jxbTimeCombine.initTimeCombine();
        jxbTimeCombine.getTimeCombineHeadNameList();
        jxbTimeCombine.getTimeCombine();
    }

    jxbTimeCombine.clearZuHeJXBByJXBID = function (ids){
        zuHeId = $(".active1").attr("zuHeId");
        zuHeName = $(".active1").attr("zuHename");
        var par = {};
        par.x = $(".active1").attr("x");
        par.y = $(".active1").attr("y");
        par.zuHeId = $(".active1").attr("zuHeId");
        par.jxbId = ids;
        par.xqid = paikeci;
        par.gradeId = $(".grade").val();
        Common.getData('/n33PaikeZuHe/clearZuHeJXBByJXBID.do', par, function (rep) {
            layer.msg(rep.message);
            jxbTimeCombine.getZuHeJXBList(zuHeName,zuHeId);
            jxbTimeCombine.getXYJXBList();
            var flag = true;
            $(".jxbs").each(function () {
                if($(this).find(".has").length == 0){
                    flag = false;
                }
            });
            if(flag){
                jxbTimeCombine.getKbByGroup(zuHeName,zuHeId);
            }
        });
    }

    jxbTimeCombine.saveZuHeJXBByJXBID = function (ids){
        zuHeId = $(".active1").attr("zuHeId");
        zuHeName = $(".active1").attr("zuHename");
        var par = {};
        par.x = $(".active1").attr("x");
        par.y = $(".active1").attr("y");
        par.zuHeId = $(".active1").attr("zuHeId");
        par.jxbId = ids;
        par.xqid = paikeci;
        par.gradeId = $(".grade").val();
        Common.getData('/n33PaikeZuHe/saveZuHeJXBByJXBID.do', par, function (rep) {
            layer.msg(rep.message);
            //paiKeZuHe.getKbByGroup(zuHeName,zuHeId);
            jxbTimeCombine.getZuHeJXBList(zuHeName,zuHeId);
            jxbTimeCombine.getXYJXBList();
        });
    }

    jxbTimeCombine.getXYJXBList = function(){
        var par = {};
        par.x = $(".active1").attr("x");
        par.y = $(".active1").attr("y");
        par.zuHeId = $(".active1").attr("zuHeId");
        par.xqid = paikeci;
        par.gradeId = $(".grade").val();
        Common.getData('/n33PaikeZuHe/getXYJXBList.do', par, function (rep) {
            $(rep.message).each(function (i,st) {
                $(".jxbs").each(function (o,at) {
                    if(st == $(at).attr("ids")){
                        $(this).find("div").addClass("has");
                    }
                })
            });
        });
    }

    jxbTimeCombine.getZuHeJXBList = function(name,ids){
        var map = {};
        map.zuHeId = ids;
        map.xqid = paikeci;
        map.gradeId = $(".grade").val();
        map.type = $("#jxbType1").val();
        Common.getData('/n33PaikeZuHe/getZuHeJXBList.do', map, function (rep) {
            Common.render({tmpl: $('#zuHeJXBListTemp'), data: rep.message, context: '#jxbs', overwrite: 1});
            $("#zuHeName1").text(name + "(" + rep.message.length + ")");
            $(".jxbs").each(function () {
                if($(this).find(".ypks").attr("ypks") >= $(this).find(".zks").attr("zks")){
                    $(this).find("div").addClass("gray");
                }
            });
        });
    }

    jxbTimeCombine.getZuHeConflictedSettledJXB = function(ids){
        var par = {};
        par.zuHeId = ids;
        par.xqid = paikeci;
        Common.getData('/n33PaikeZuHe/getZuHeConflictedSettledJXB.do', par, function (rep) {
            if (rep.code == '200') {
                $('#jxbs').empty();
                var gdsw = rep.message.gdsws;
                if (gdsw != null && gdsw.length != 0) {
                    for (var i = 0; i < gdsw.length; i++) {
                        var dt = "idx" + (gdsw[i].y - 1) + (gdsw[i].x - 1);
                        $("." + dt).addClass("red");
                        $("." + dt).text("全校事务");
                    }
                }

                var sws = rep.message.swcts;
                if (sws != null && sws.length != 0) {
                    for (var i = 0; i < sws.length; i++) {
                        var dt = "idx" + (sws[i].y - 1) + (sws[i].x - 1);
                        $("." + dt).addClass("red");
                        $("." + dt).text(sws[i].desc);
                    }
                }
                if (rep.message.jxbcts != null && rep.message.jxbcts.length != 0) {
                    for (var j = 0; j < rep.message.jxbcts.length; j++) {
                        var dt = "idx" + (rep.message.jxbcts[j].x) + (rep.message.jxbcts[j].y);
                        if (!$("." + dt).hasClass('red') && $(" ." + dt + " .ls").length == 0) {
                            var string = "";
                            $.each(rep.message.jxbcts[j].ctString,function (i,st) {
                                string += st;
                                string += "\r\n";
                            })
                            $("." + dt).attr('title',string);
                            $("." + dt).addClass("gray");
                            if ($("." + dt + " p").hasClass('pic')) {

                            } else {
                                if ($(" ." + dt + " .ls").length == 0) {
                                    $(" ." + dt).text(rep.message.jxbcts[j].remarks);
                                }
                            }
                        }
                    }
                }

            }
        });
    }

    jxbTimeCombine.getKbByGroup = function(name,ids){
        $(".itt").each(function (i,st) {
            $(st).removeClass("active1");
            if($(st).find(".ls").length > 0){
                $(this).html("");
            }

        });
        var par = {};
        par.zuHeId = ids;
        par.xqid = paikeci;
        par.gradeId = $(".grade").val();
        Common.getData('/n33PaikeZuHe/getKbByGroup.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var str = "";
                str += "<div class='ls'>" + name + "</div>"
                $(".itt[x=" + (obj.x) + "][y=" + (obj.y) + "]").html(str);
                $(".itt[x=" + (obj.x) + "][y=" + (obj.y) + "]").attr("zuHeId", obj.zuHeId);
            });
            $(".itt").each(function (i,st) {
                $(this).attr("ids",ids);
                $(this).attr("zuHename",name)
            });
        });
    }

    jxbTimeCombine.getZouBanTimeList = function () {
        var par = {}
        par.xqid = paikeci;
        par.gid = $(".grade").val();
        if (par.xqid != "" && par.xqid != undefined && par.xqid != null && par.gid != "" && par.gid != undefined && par.gid != null) {
            Common.getData('/courseset/getZouBanTime.do', par, function (rep) {
                var dom = $(".itt");
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

    jxbTimeCombine.ChuShi = function () {
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

    jxbTimeCombine.getListBySchoolIdTime = function () {
        var xqid = paikeci;
        if (xqid && xqid != null) {
            Common.getData('/courseset/getListBySchoolId.do', {"xqid": xqid}, function (rep) {
                Common.render({tmpl: $('#ks_temp'), data: rep, context: '.kejie', overwrite: 1});

            });
        }
    }

    jxbTimeCombine.getGradeWeek = function () {
        var par = {};
        par.ciId = paikeci;
        par.gradeId = $(".grade").val();
        if(par.ciId != null && par.ciId != "" && par.gradeId != null && par.gradeId != ""){
            Common.getData('/n33PaikeZuHe/getGradeWeek.do', par, function (rep) {
                Common.render({tmpl: $('#weeks_temp'), data: rep.message, context: '.weeks', overwrite: 1});

            });
        }
    }

    jxbTimeCombine.getJxbStudent = function (jxbId) {
        var par = {};
        par.jxbId = jxbId;
        par.classId = $('#cla').val();
        par.combinName = $('#selCom').val();
        par.tagId = $('#tag').val();
        Common.getData('/timeCombine/getJxbStudent.do', par, function (rep) {
            Common.render({tmpl: $('#student_temp'), data: rep.message, context: '#student', overwrite: 1});
        })
    }

    jxbTimeCombine.getJxbStudentCount = function (jxbId) {
        Common.getData('/timeCombine/getJxbStudentCount.do', {"jxbId":jxbId}, function (rep) {
            $('#stuCount').text("人数：" + rep.message);
        })
    }

    jxbTimeCombine.getTagName = function () {
        var par = {};
        par.gradeId = $('.grade').val();
        par.ciId = paikeci;
        Common.getData('/timeCombine/getTagName.do', par, function (rep) {
            var array = new Array();
            var map = {};
            map.tagId = "*";
            map.tagName = "全部";
            array.push(map)
            $.each(rep.message,function (i,st) {
                array.push(st)
            })
            Common.render({tmpl: $('#tag_temp'), data: array, context: '#tag', overwrite: 1});
        })
    }

    jxbTimeCombine.getStuSelectResultByClassForZhuanXiang = function () {
        var gradeId = $(".grade").val();
        var type = 3;
        if (!gradeId || gradeId == "" || !type || type == "") {
            return;
        }
        Common.getData('/new33school/set/getStuSelectResultByClassForZhuanXiang.do', {
            "xqid": paikeci,
            "gradeId": gradeId,
            "type": type
        }, function (rep) {
            var array = new Array();
            var map = {};
            map.name = "全部";
            map.subName = "*";
            array.push(map)
            $.each(rep.content,function (i,st) {
                array.push(st)
            })
            Common.render({tmpl: $('#content2_temp'), data: array, context: '#selCom', overwrite: 1});
        })
    }




    jxbTimeCombine.getClassList = function () {
        var par = {};
        par.xqid = paikeci;
        par.gradeId = $(".grade").val();
        if(!par.xqid || par.xqid == "" || par.xqid == null || !par.gradeId || par.gradeId == "" || par.gradeId == null){
            Common.render({tmpl: $('#class_temp'), data: [], context: '#cla', overwrite: 1});
            return;
        }
        Common.getData('/new33classManage/getClassList.do', par, function (rep) {
            var array = new Array();
            var map = {};
            map.classId = "*";
            map.name = "全部";
            array.push(map)
            $.each(rep.message,function (i,st) {
                array.push(st)
            })
            Common.render({tmpl: $('#class_temp'), data:array, context: '#cla', overwrite: 1});
        })
    }

    jxbTimeCombine.getList = function () {
        var par = {};
        par.gradeId = $(".grade").val();
        par.zuHeType = $("#jxbType1").val();
        par.ciId = paikeci;
        Common.getData('/n33PaikeZuHe/getPaiKeZuHeList.do', par, function (rep) {
            if(rep.message.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            Common.render({tmpl: $('#ZuHe_temp'), data: rep.message, context: '#ZuHeList', overwrite: 1});

        })
    }

    jxbTimeCombine.initZuHeList = function () {
        var par = {};
        par.gradeId = $('.grade').val();
        par.ciId = paikeci;
        Common.getData('/n33PaikeZuHe/initZuHeList.do', par, function (rep) {

        })
    }

    jxbTimeCombine.setJxbRoom = function (jxbid) {
        var roomId = $("#room").val();
        Common.getData('/n33_fenbaninfoset/setJxbRoom.do', {"jxbId": jxbid, "roomId": roomId}, function (rep) {
            if (rep.code == 200) {
                jxbTimeCombine.getTimeCombineHeadNameList();
                jxbTimeCombine.getTimeCombine();
            }
        })
    }

    jxbTimeCombine.getClassRoomList = function (serial) {
        var par = {};
        par.gradeId = $('.grade').val();
        par.type = $('#jxbType').val();
        par.serial = serial;
        Common.getData('/timeCombine/getClassRoomList.do', par, function (rep) {
            Common.render({tmpl: $('#room_temp'), data: rep, context: '#room', overwrite: 1});
        })
    }

    jxbTimeCombine.delJxbTeacher = function (jxbId) {
        var par = {};
        par.jxbId = jxbId;
        Common.getData('/timeCombine/delJxbTeacher.do', par, function (rep) {
            jxbTimeCombine.getTimeCombineHeadNameList();
            jxbTimeCombine.getTimeCombine();
        })
    }

    jxbTimeCombine.delJxbRoom = function (jxbId) {
        var par = {};
        par.jxbId = jxbId;
        Common.getData('/timeCombine/delJxbRoom.do', par, function (rep) {
            jxbTimeCombine.getTimeCombineHeadNameList();
            jxbTimeCombine.getTimeCombine();
        })
    }

    jxbTimeCombine.getTeacherList = function (subjectId,serial) {
        var gradeId = $('.grade').val();
        var par = {};
        par.gradeId = gradeId;
        par.subjectId = subjectId;
        par.serial = serial;
        par.ciId = paikeci;
        par.type = $('#jxbType').val();
        Common.getData('/timeCombine/getTeacherList.do', par, function (rep) {
            Common.render({tmpl: $('#teacher_temp'), data: rep, context: '#teacher', overwrite: 1});
        })
    }

    jxbTimeCombine.setJxbTeacher = function (jxbid) {
        var teacherId = $("#teacher").val();
        Common.getData('/n33_fenbaninfoset/setJxbTeacher.do', {"jxbId": jxbid, "teacherId": teacherId}, function (rep) {
            if (rep.code == 200) {
                jxbTimeCombine.getTimeCombineHeadNameList();
                jxbTimeCombine.getTimeCombine();
            }
        })
    }

    jxbTimeCombine.getTimeCombine = function () {
        var par = {};
        par.ciId = paikeci;
        par.gradeId = $('.grade').val();
        par.type = $('#jxbType').val();
        Common.getData('/timeCombine/getTimeCombine.do', par, function (rep) {
            Common.render({tmpl: $('#listName_temp'), data: rep.message.listName, context: '#listName',overwrite : 1});
            Common.render({tmpl: $('#jxbList_temp'), data: rep.message.jxbList, context: '#jxbList',overwrite : 1});
        });
    }

    jxbTimeCombine.getTimeCombineHeadNameList = function () {
        var par = {};
        par.ciId = paikeci;
        par.gradeId = $('.grade').val();
        par.type = $('#jxbType').val();
        Common.getData('/timeCombine/getTimeCombineHeadNameList.do', par, function (rep) {
            if(rep.message != null && rep.message.length != 0){
                Common.render({tmpl: $('#headNameList_temp'), data: rep.message, context: '#headNameList',overwrite : 1});
                Common.render({tmpl: $('.headNameList_temp'), data: rep.message, context: '.headNameList',overwrite : 1});
                Common.render({tmpl: $('#headNameList_temp1'), data: rep.message, context: '#headNameList1',overwrite : 1});
                $('#jxb1').hide();
                $('#jxb2').show();
                $('#jxb3').show();
                $('#jxb4').show();
            }else{
                $('#jxb1').show();
                $('#jxb2').hide();
                $('#jxb3').hide();
                $('#jxb4').hide();
            }

        });
    }

    jxbTimeCombine.initTimeCombine = function () {
        var par = {};
        par.ciId = paikeci;
        par.gradeId = $('.grade').val();
        Common.getData('/timeCombine/initTimeCombine.do', par, function (rep) {

        });
    }

    //获取默认学期和默认排课次
    jxbTimeCombine.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            paikeci = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.paikeci);
        });
    }

    //获取年级
    jxbTimeCombine.getGrade = function () {
        if(paikeci != "" && paikeci != undefined && paikeci != null){
            Common.getData('/new33isolateMange/getGradList.do', {"xqid": paikeci}, function (rep) {
                Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.grade',overwrite : 1});
                //$("#grade em:eq(0)").addClass("cur");
            })
        }
    }

    function getCiIdIsFaBu() {
        var flag = null;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId": paikeci}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }

    $('body').on('click','.jxb-zh-item > span',function () {
        var name = $(this).text();
        if($(this).parent().hasClass('active')){
            $.each($('#main_table .jxb-zh-item'),function (i,item) {
                $(item).removeClass('active');
            })
        }else{
            $.each($('#main_table .jxb-zh-item'),function (i,item) {
                $(item).removeClass('active');
                if ($(item).find('.name').text() == name) {
                    $(item).addClass('active');
                }
            })
        }
    })

    module.exports = jxbTimeCombine;
})