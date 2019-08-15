/**
 * Created by albin on 2018/3/30.
 */
define('jxbAutoTimeCombine', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var jxbAutoTimeCombine = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var paikeci = "";
    var zuHeId = "";
    var zuHeName = "";
    var userIds = "";
    jxbAutoTimeCombine.init = function () {
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
        $(document).on('click','.nt-jxb',function (e) {
            $('.orgJxb').empty();
            $('.newJxb').empty();
            var htm = "";
            htm = '<option value="'+$(this).attr('jxbId')+'">'+$(this).attr('jxbnm')+'</option>';
            $('.orgJxb').append(htm);
            var idx = $(this).attr('idx');
            var subid = $(this).attr('suid');
            var jxbIdArg = [];
            jxbIdArg.push($(this).attr('jxbId'));
            var htm2 = "";
            $('.jxbcx'+idx).each(function(i) {
                if ($(this).attr('suid')==subid && jxbIdArg.indexOf($(this).attr('jxbId'))==-1) {
                    htm2 += '<option value="'+$(this).attr('jxbId')+'">'+$(this).attr('jxbnm')+'</option>';
                    jxbIdArg.push($(this).attr('jxbId'));
                }
            })
            $('.newJxb').append(htm2);
            jxbAutoTimeCombine.getJxbStudent2($(this).attr('jxbId'));
            $('.dialog-bj').show();
        });

        $('#udpStudentByJxb').click(function() {
            var par = {};
            par.studentIds = userIds;
            par.orgJxbId = $('.orgJxb').val();
            par.newJxbId = $('.newJxb').val();
            if ($('.newJxb').val()=="" || $('.newJxb').val()==null) {
                layer.alert("调整教学班不能为空！");
                return;
            }
            Common.getData('/studentJXB/udpStudentByJxb.do', par, function (rep) {
                if (rep.code == '200') {
                    layer.alert("调班成功！");
                    $('.dialog').hide();
                    jxbAutoTimeCombine.getTimeCombine();
                } else {
                    layer.alert("调班失败！");
                }
            })
        })
        $('#change_class').click(function () {
            userIds = "";
            $('#student2').find(':checkbox').each(function(){
                if ($(this).is(":checked")) {
                    userIds += $(this).parent().attr('userId')+",";
                }
            });
            if (userIds=="") {
                layer.alert("请先选择学生！");
            } else {
                $('.dialog-change').show()
            }
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
            par.gradeId = $('#gradeId').val();
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
            par.gradeId = $('#gradeId').val();
            Common.getData('/timeCombine/saveJXB.do', par, function (rep) {
                jxbAutoTimeCombine.getTimeCombineHeadNameList();
                jxbAutoTimeCombine.getTimeCombine();
            });
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
            par.gradeId = $('#gradeId').val();
            Common.getData('/timeCombine/delJXB.do', par, function (rep) {
                jxbAutoTimeCombine.getTimeCombineHeadNameList();
                jxbAutoTimeCombine.getTimeCombine();
            })
        })

        /*$('body').on("change",".grade",function () {
            jxbAutoTimeCombine.initTimeCombine();
            jxbAutoTimeCombine.getTimeCombineHeadNameList();
            jxbAutoTimeCombine.getTimeCombine();
        });*/

        $('body').on('change','#jxbType',function () {
            jxbAutoTimeCombine.getTimeCombineHeadNameList();
            jxbAutoTimeCombine.getTimeCombine();
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
                    jxbAutoTimeCombine.getTeacherList(subId,serial);
                    $("#teacher").val(tid);
                },function () {
                });
            }else{
                jxbAutoTimeCombine.getTeacherList($(this).attr("subId"),$(this).attr("serial"));
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
            jxbAutoTimeCombine.setJxbTeacher($("#tea_qd").attr("jxbId"));
            $('.bg').hide();
            $('.dialog-teacher').hide();
        });

        $('body').on("click",".delTea",function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            jxbAutoTimeCombine.delJxbTeacher($(this).attr("jxbId"));
        });

        $('body').on("click",".delRoom",function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            jxbAutoTimeCombine.delJxbRoom($(this).attr("jxbId"));
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
                    jxbAutoTimeCombine.getClassRoomList(serial);
                    $("#room").val(rid);
                    $('.bg').show();
                    $('.dialog-house').show();
                },function () {
                });
            }else{
                jxbAutoTimeCombine.getClassRoomList($(this).attr('serial'));
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
            jxbAutoTimeCombine.setJxbRoom($("#set_room").attr("jxbid"));
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
            par.gradeId = $('#gradeId').val();
            par.type = $('#jxbType').val();
            par.serial = $(this).attr("serial");
            Common.getData('/timeCombine/clearColumn.do',par, function (rep) {
                if (rep.code == 200) {
                    jxbAutoTimeCombine.getTimeCombineHeadNameList();
                    jxbAutoTimeCombine.getTimeCombine();
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
            jxbAutoTimeCombine.getTimeCombineHeadNameList();
        });

        $('body').on("click",'#del_zuhe',function () {
            $('.bg').hide();
            $('.dialog-del').hide();
            var par = {};
            par.ciId = paikeci;
            par.gradeId = $('#gradeId').val();
            par.type = $('#jxbType').val();
            par.serial = $('#headNameList1').val();
            Common.getData('/timeCombine/delZuHe.do',par, function (rep) {
                if (rep.code == 200) {
                    jxbAutoTimeCombine.getTimeCombineHeadNameList();
                    jxbAutoTimeCombine.getTimeCombine();
                    $('#jxb1').hide();
                    $('#jxb2').show();
                }
            })
        });

        $('body').on('click','.pkzh',function () {
           // var name = $(".grade option[value="+$('#gradeId').val()+"]").text();
            //$('#gradeName').text('年级：' + name);
            var par = {};
            par.ciId = paikeci;
            par.gradeId = $('#gradeId').val();
            var flag = true;
            var list = null;
            Common.getData('/timeCombine/checkZuHeData.do',par, function (rep) {
                if (rep.code == 200) {
                    flag = rep.message.flag;
                    list = rep.message.list;
                }
            })
            if(flag){
                jxbAutoTimeCombine.initZuHeList();
                jxbAutoTimeCombine.getList();
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
                jxbAutoTimeCombine.getGradeWeek();
                jxbAutoTimeCombine.getListBySchoolIdTime();
                jxbAutoTimeCombine.ChuShi();
                jxbAutoTimeCombine.getZouBanTimeList();
                jxbAutoTimeCombine.getKbByGroup(zuHeName,ids);
                jxbAutoTimeCombine.getZuHeConflictedSettledJXB(ids);
                jxbAutoTimeCombine.getZuHeJXBList(zuHeName,ids);
                $('#content2').hide();
                $('#content3').show();
                $(".bg").hide();
                $(".jhh").hide();
            },500);

        })

        $('body').on("click",'#newZh',function () {
            var par = {};
            par.ciId = paikeci;
            par.gradeId = $('#gradeId').val();
            par.type = $('#jxbType').val();
            Common.getData('/timeCombine/addZuHe.do',par, function (rep) {
                if (rep.code == 200) {
                    jxbAutoTimeCombine.getTimeCombineHeadNameList();
                    jxbAutoTimeCombine.getTimeCombine();
                }
            })
        });

        $('body').on("click",'#autoTimeComb',function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            jxbAutoTimeCombine.autoTimeCombSaveJXB();
        });

        $('body').on('change','#jxbType1',function () {
            jxbAutoTimeCombine.getList();
        });

        var jxbId = "";
        $('body').on('click','.sign-sp',function () {
            $('#jxbTeaName').text($(this).text());
            jxbId = $(this).attr('ids');
            jxbAutoTimeCombine.getClassList();
            jxbAutoTimeCombine.getStuSelectResultByClassForZhuanXiang();
            jxbAutoTimeCombine.getTagName();
            jxbAutoTimeCombine.getJxbStudentCount(jxbId);
            jxbAutoTimeCombine.getJxbStudent(jxbId);
            $('.dialog-sign').show();
        });

        $('body').on("change","#cla",function () {
            jxbAutoTimeCombine.getJxbStudent(jxbId);
        });
        $('body').on("change","#selCom",function () {
            jxbAutoTimeCombine.getJxbStudent(jxbId);
        });
        $('body').on("change","#tag",function () {
            jxbAutoTimeCombine.getJxbStudent(jxbId);
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
                    jxbAutoTimeCombine.saveZuHeJXBByJXBID($(this).attr("ids"));
                }else if($(this).find(".has").length == 0){
                    jxbAutoTimeCombine.clearZuHeJXBByJXBID($(this).attr("ids"));
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
            par.gradeId = $('#gradeId').val();
            Common.getData('/n33PaikeZuHe/clearKeBiaoByGroup.do', par, function (rep) {
                if (rep.code == '200') {
                    layer.msg("撤销成功！");
                    jxbAutoTimeCombine.getKbByGroup(zuHeName,zuHeId);
                    jxbAutoTimeCombine.getZuHeJXBList(zuHeName,zuHeId);
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
                par.gradeId = $('#gradeId').val();
                par.type = $("#jxbType1").val();
                par.x = $(this).attr("x");
                par.y = $(this).attr("y");
                Common.getData('/n33PaikeZuHe/saveKeBiaoByGroup.do', par, function (rep) {
                    if (rep.code == '200') {
                        layer.msg("放入成功！");
                        jxbAutoTimeCombine.getKbByGroup(zuHeName,zuHeId);
                        jxbAutoTimeCombine.getZuHeJXBList(zuHeName,zuHeId);
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
                    jxbAutoTimeCombine.getXYJXBList();
                }else{
                    $(".jxbs").each(function () {
                        $(this).find("div").removeClass("has");
                    });
                }

            }
        });

        $('#prevStop').click(function () {
            Common.goTo('/newisolatepage/pkTable.do');
        });

        $('#nextStop').click(function () {
            var gradeId = $('#gradeId').val();
            Common.goTo('/newisolatepage/autoTimeCombKeBiao.do?gradeId='+gradeId);
        });

        jxbAutoTimeCombine.getDefaultTerm();
        //jxbAutoTimeCombine.getGrade();
        jxbAutoTimeCombine.initTimeCombine();
        jxbAutoTimeCombine.getTimeCombineHeadNameList();
        jxbAutoTimeCombine.getTimeCombine();
    };
    jxbAutoTimeCombine.getJxbStudent2 = function (jxbId) {
        var dto = {};
        dto.id = jxbId;
        dto.gid = $('#gradeId').val();
        Common.getData('/n33_fenbaninfoset/getjxbStudentByJxbId.do', dto, function (rep) {
            Common.render({tmpl: $('#student2_temp'), data: rep.message, context: '#student2', overwrite: 1});
            // $("#student li:eq(0)").addClass("cur")
        })
    }
    jxbAutoTimeCombine.autoTimeCombSaveJXB = function(){
        var index = layer.load(2);
        var par = {};
        par.gradeId = $('#gradeId').val();
        par.ciId = paikeci;
        setTimeout(function(){
            Common.getPostData('/timeCombine/autoTimeCombSaveJXB.do', par, function (rep) {
                layer.close(index);
                if(rep.code==200) {
                    if (rep.message == "ok") {
                        layer.msg("创建成功");
                        jxbAutoTimeCombine.getTimeCombineHeadNameList();
                        jxbAutoTimeCombine.getTimeCombine();
                    } else {
                        layer.msg(rep.message)
                    }
                }
            });
        }, "100");
    };

    jxbAutoTimeCombine.clearZuHeJXBByJXBID = function (ids){
        zuHeId = $(".active1").attr("zuHeId");
        zuHeName = $(".active1").attr("zuHename");
        var par = {};
        par.x = $(".active1").attr("x");
        par.y = $(".active1").attr("y");
        par.zuHeId = $(".active1").attr("zuHeId");
        par.jxbId = ids;
        par.xqid = paikeci;
        par.gradeId = $('#gradeId').val();
        Common.getData('/n33PaikeZuHe/clearZuHeJXBByJXBID.do', par, function (rep) {
            layer.msg(rep.message);
            jxbAutoTimeCombine.getZuHeJXBList(zuHeName,zuHeId);
            jxbAutoTimeCombine.getXYJXBList();
            var flag = true;
            $(".jxbs").each(function () {
                if($(this).find(".has").length == 0){
                    flag = false;
                }
            });
            if(flag){
                jxbAutoTimeCombine.getKbByGroup(zuHeName,zuHeId);
            }
        });
    }

    jxbAutoTimeCombine.saveZuHeJXBByJXBID = function (ids){
        zuHeId = $(".active1").attr("zuHeId");
        zuHeName = $(".active1").attr("zuHename");
        var par = {};
        par.x = $(".active1").attr("x");
        par.y = $(".active1").attr("y");
        par.zuHeId = $(".active1").attr("zuHeId");
        par.jxbId = ids;
        par.xqid = paikeci;
        par.gradeId = $('#gradeId').val();
        Common.getData('/n33PaikeZuHe/saveZuHeJXBByJXBID.do', par, function (rep) {
            layer.msg(rep.message);
            //paiKeZuHe.getKbByGroup(zuHeName,zuHeId);
            jxbAutoTimeCombine.getZuHeJXBList(zuHeName,zuHeId);
            jxbAutoTimeCombine.getXYJXBList();
        });
    }

    jxbAutoTimeCombine.getXYJXBList = function(){
        var par = {};
        par.x = $(".active1").attr("x");
        par.y = $(".active1").attr("y");
        par.zuHeId = $(".active1").attr("zuHeId");
        par.xqid = paikeci;
        par.gradeId = $('#gradeId').val();
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

    jxbAutoTimeCombine.getZuHeJXBList = function(name,ids){
        var map = {};
        map.zuHeId = ids;
        map.xqid = paikeci;
        map.gradeId = $('#gradeId').val();
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

    jxbAutoTimeCombine.getZuHeConflictedSettledJXB = function(ids){
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

    jxbAutoTimeCombine.getKbByGroup = function(name,ids){
        $(".itt").each(function (i,st) {
            $(st).removeClass("active1");
            if($(st).find(".ls").length > 0){
                $(this).html("");
            }

        });
        var par = {};
        par.zuHeId = ids;
        par.xqid = paikeci;
        par.gradeId = $('#gradeId').val();
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

    jxbAutoTimeCombine.getZouBanTimeList = function () {
        var par = {}
        par.xqid = paikeci;
        par.gid = $('#gradeId').val();
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

    jxbAutoTimeCombine.ChuShi = function () {
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

    jxbAutoTimeCombine.getListBySchoolIdTime = function () {
        var xqid = paikeci;
        if (xqid && xqid != null) {
            Common.getData('/courseset/getListBySchoolId.do', {"xqid": xqid}, function (rep) {
                Common.render({tmpl: $('#ks_temp'), data: rep, context: '.kejie', overwrite: 1});

            });
        }
    }

    jxbAutoTimeCombine.getGradeWeek = function () {
        var par = {};
        par.ciId = paikeci;
        par.gradeId = $('#gradeId').val();
        if(par.ciId != null && par.ciId != "" && par.gradeId != null && par.gradeId != ""){
            Common.getData('/n33PaikeZuHe/getGradeWeek.do', par, function (rep) {
                Common.render({tmpl: $('#weeks_temp'), data: rep.message, context: '.weeks', overwrite: 1});

            });
        }
    }

    jxbAutoTimeCombine.getJxbStudent = function (jxbId) {
        var par = {};
        par.jxbId = jxbId;
        par.classId = $('#cla').val();
        par.combinName = $('#selCom').val();
        par.tagId = $('#tag').val();
        Common.getData('/timeCombine/getJxbStudent.do', par, function (rep) {
            Common.render({tmpl: $('#student_temp'), data: rep.message, context: '#student', overwrite: 1});
        })
    }

    jxbAutoTimeCombine.getJxbStudentCount = function (jxbId) {
        Common.getData('/timeCombine/getJxbStudentCount.do', {"jxbId":jxbId}, function (rep) {
            $('#stuCount').text("人数：" + rep.message);
        })
    }

    jxbAutoTimeCombine.getTagName = function () {
        var par = {};
        par.gradeId = $('#gradeId').val();
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

    jxbAutoTimeCombine.getStuSelectResultByClassForZhuanXiang = function () {
        var gradeId = $('#gradeId').val();
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

    jxbAutoTimeCombine.getClassList = function () {
        var par = {};
        par.xqid = paikeci;
        par.gradeId = $('#gradeId').val();
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

    jxbAutoTimeCombine.getList = function () {
        var par = {};
        par.gradeId = $('#gradeId').val();
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

    jxbAutoTimeCombine.initZuHeList = function () {
        var par = {};
        par.gradeId = $('#gradeId').val();
        par.ciId = paikeci;
        Common.getData('/n33PaikeZuHe/initZuHeList.do', par, function (rep) {

        })
    }

    jxbAutoTimeCombine.setJxbRoom = function (jxbid) {
        var roomId = $("#room").val();
        Common.getData('/n33_fenbaninfoset/setJxbRoom.do', {"jxbId": jxbid, "roomId": roomId}, function (rep) {
            if (rep.code == 200) {
                jxbAutoTimeCombine.getTimeCombineHeadNameList();
                jxbAutoTimeCombine.getTimeCombine();
            }
        })
    }

    jxbAutoTimeCombine.getClassRoomList = function (serial) {
        var par = {};
        par.gradeId = $('#gradeId').val();
        par.type = $('#jxbType').val();
        par.serial = serial;
        Common.getData('/timeCombine/getClassRoomList.do', par, function (rep) {
            Common.render({tmpl: $('#room_temp'), data: rep, context: '#room', overwrite: 1});
        })
    }

    jxbAutoTimeCombine.delJxbTeacher = function (jxbId) {
        var par = {};
        par.jxbId = jxbId;
        Common.getData('/timeCombine/delJxbTeacher.do', par, function (rep) {
            jxbAutoTimeCombine.getTimeCombineHeadNameList();
            jxbAutoTimeCombine.getTimeCombine();
        })
    }

    jxbAutoTimeCombine.delJxbRoom = function (jxbId) {
        var par = {};
        par.jxbId = jxbId;
        Common.getData('/timeCombine/delJxbRoom.do', par, function (rep) {
            jxbAutoTimeCombine.getTimeCombineHeadNameList();
            jxbAutoTimeCombine.getTimeCombine();
        })
    }

    jxbAutoTimeCombine.getTeacherList = function (subjectId,serial) {
        var gradeId = $('#gradeId').val();
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

    jxbAutoTimeCombine.setJxbTeacher = function (jxbid) {
        var teacherId = $("#teacher").val();
        Common.getData('/n33_fenbaninfoset/setJxbTeacher.do', {"jxbId": jxbid, "teacherId": teacherId}, function (rep) {
            if (rep.code == 200) {
                jxbAutoTimeCombine.getTimeCombineHeadNameList();
                jxbAutoTimeCombine.getTimeCombine();
            }
        })
    }

    jxbAutoTimeCombine.getTimeCombine = function () {
        var par = {};
        par.ciId = paikeci;
        par.gradeId = $('#gradeId').val();
        par.type = $('#jxbType').val();
        Common.getPostData('/timeCombine/getTimeCombine.do', par, function (rep) {
            Common.render({tmpl: $('#listName_temp'), data: rep.message.listName, context: '#listName',overwrite : 1});
            Common.render({tmpl: $('#jxbList_temp'), data: rep.message.jxbList, context: '#jxbList',overwrite : 1});
        });
    }

    jxbAutoTimeCombine.getTimeCombineHeadNameList = function () {
        var par = {};
        par.ciId = paikeci;
        par.gradeId = $('#gradeId').val();
        par.type = $('#jxbType').val();
        Common.getPostData('/timeCombine/getTimeCombineHeadNameList.do', par, function (rep) {
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

    jxbAutoTimeCombine.initTimeCombine = function () {
        var par = {};
        par.ciId = paikeci;
        par.gradeId = $('#gradeId').val();
        Common.getPostData('/timeCombine/initTimeCombine.do', par, function (rep) {

        });
    }

    //获取默认学期和默认排课次
    jxbAutoTimeCombine.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            paikeci = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.paikeci);
        });
    }

    //获取年级
    /*jxbAutoTimeCombine.getGrade = function () {
        if(paikeci != "" && paikeci != undefined && paikeci != null){
            Common.getData('/new33isolateMange/getGradList.do', {"xqid": paikeci}, function (rep) {
                Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.grade',overwrite : 1});
                //$("#grade em:eq(0)").addClass("cur");
            })
        }
    }*/

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

    module.exports = jxbAutoTimeCombine;
})