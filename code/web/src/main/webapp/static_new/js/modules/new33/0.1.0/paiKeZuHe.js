/**
 * Created by albin on 2018/7/10.
 */
define('paiKeZuHe', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var paiKeZuHe = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var ciId = "";
    var bf = true;
    var cls = true;
    var zuHeId = "";
    var zuHeName = "";
    var c;
    paiKeZuHe.getDefaultCi = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            ciId = rep.message.paikeci;
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term").val(rep.message.paikeci);
        });
    }
    paiKeZuHe.getGradeList = function () {
        var par = {};
        par.xqid = ciId;
        Common.getData('/new33isolateMange/getGradList.do', par, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.grade', overwrite: 1});
        })
    }
    paiKeZuHe.getVirturlClass = function () {
        var par = {};
        par.ciId = ciId;
        par.gradeId = $(".grade").val();
        par.type = $(".jxbType").val();
        Common.getData('/n33PaikeZuHe/getJXBList.do.do', par, function (rep) {
            Common.render({tmpl: $('#viList'), data: rep.message, context: '#ulList', overwrite: 1});
        })
    }
    paiKeZuHe.getJxbListIsChongTu = function (jxbId,st) {
        var par = {}
        var jxbIds = new Array();
        var jxbIds1 = new Array();
        //alert($(st).hasClass("rrgtH"));
        $(".tfCur").each(function (i, obj) {
            if(jxbId != $(obj).find("i").attr("ids")){
                jxbIds1.push($(obj).find("i").attr("ids"));
            }
        })
        if ($(".ra1:checked").length > 0) {
            var tr = $(".ra1:checked").parent().parent();
            $(tr).find(".inline-jxb").each(function (i, obj) {
                jxbIds.push($(obj).attr("ids"));
            })
        }
        $.each(jxbIds1,function (i,st) {
            jxbIds.push(st);
        });
        par.jxbIds = jxbIds;
        par.jxbId = jxbId;
        bf = true;
        if(!$(st).find("i").hasClass("rrgtH") && !$(st).hasClass("tfCur")){
            layer.alert("该教学班与其他教学班存在冲突，无法选择");
            bf = false;
            return;
        }
        if (cls == false) {
            if (par.jxbIds.length > 1) {
                Common.getPostBodyData('/virtualClass/getJxbListIsChongTu.do', par, function (rep) {
                    bf = rep.message;
                })
            }
        }
        if(!$(st).hasClass("tfCur")){
            jxbIds.push(jxbId);
        }
        if (bf == true) {
           paiKeZuHe.getGuiZeClass(jxbIds)
        }
    }
    paiKeZuHe.getGuiZeClass = function (jxbIds) {
        $(".tf-ul i").removeClass("rrgtH")
        var par = {};
        par.ciId = ciId;
        par.gradeId = $(".grade").val();
        par.type = $(".jxbType").val() + "";
        par.jxbIds = jxbIds;
        Common.getPostBodyData('/n33PaikeZuHe/getNoCTJXB.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                if(!$(".tf-ul i[ids=" + obj + "]").hasClass("rrgt")){
                    $(".tf-ul i[ids=" + obj + "]").addClass("rrgtH");
                }
            });
        });
    }
    function getStatusPK() {
        $.ajax({
            type: "GET",
            url: '/n33PaikeZuHe/getStatus.do',
            async: false,
            cache: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {
                if (rep.st == -1) {
                    paiKeZuHe.getVirturlClass();
                    paiKeZuHe.getList();
                    $('.bg').hide();
                    $(".jhh").hide();
                    window.clearInterval(c);
                    layer.msg("自动生成组合成功");
                } else {
                    $(".jhhSp").text("正在自动生成中，请稍候...");
                }
            }
        });
    }
    paiKeZuHe.init = function () {
        $("#ziDongZuhe").click(function(){
            var par={};
            par.ciId = ciId;
            par.gradeId = $(".grade").val();
            par.type = $(".jxbType").val();
            $(".jhh").show();
            $('.bg').show();
            $(".jhhSp").text("正在自动生成中，请稍候...");
            $.ajax({
                type: "post",
                data: JSON.stringify(par),
                url: '/n33PaikeZuHe/ziDongChuangJianZuHe.do?ciId=' + par.ciId + "&gradeId=" +   par.gradeId + "&type=" +  par.type,
                async: true,
                cache: false,
                contentType: 'application/json',
                success: function (rep) {
                    if (rep.code == 500) {
                        paiKeZuHe.getVirturlClass();
                        paiKeZuHe.getList();
                        window.clearInterval(c);
                        $(".jhh").hide();
                        $('.bg').hide();
                        layer.alert("自动生成组合发生异常");
                    }
                }
            });
            c = setInterval(getStatusPK, 2000);
        })
        $("body").on("click", ".ra1", function () {
            var obj = $(this);
            var map = {};
            map.zuHeId = $(this).attr("ids");
            map.xqid = ciId;
            map.gradeId = $(".grade").val();
            var flag = true;
            Common.getData('/n33PaikeZuHe/getYkbEntries.do', map, function (rep) {
                if(rep.message.length > 0){
                    layer.alert("该组合已排课，修改组合前请撤销组合排课");
                    obj.prop("checked",false);
                    flag = false;
                }
            });
            if(!flag){
                return;
            }
            $(".jhh").show();
            $('.bg').show();
            $(".jhhSp").text("正在检测冲突，请稍候...");
            setTimeout(function () {
                var jxbIds1 = new Array();
                $(".tfCur").each(function (i,st) {
                    $(this).removeClass("tfCur");
                });

                var jxbIds = new Array();
                if ($(".ra1:checked").length > 0) {
                    var tr = $(".ra1:checked").parent().parent();
                    $(tr).find(".inline-jxb").each(function (i, obj) {
                        jxbIds.push($(obj).attr("ids"));
                    })
                }
                $(".jhh").hide();
                $('.bg').hide();
                paiKeZuHe.getGuiZeClass(jxbIds);
            },500)
        })
        $(".grade,.jxbType").change(function () {
            paiKeZuHe.getVirturlClass();
            paiKeZuHe.getList();
            paiKeZuHe.checkYKB();
        })
        $("#addDto").click(function () {
            $('.bg').hide();
            $('.addP-popup').hide();
            paiKeZuHe.addZuHe();
        })
        $("body").on("click", ".delZu", function () {
            var par = {}
            par.id = $(this).attr("ids");
            Common.getData('/n33PaikeZuHe/delEntry.do', par, function (rep) {
                paiKeZuHe.getList();
            })
        })

        $("body").on("click", ".rrow", function () {
            var par = {}
            par.jxbId = $(this).attr("ids");
            par.id = $(this).attr("zuHeId");
            Common.getData('/n33PaikeZuHe/delJxbById.do', par, function (rep) {
                paiKeZuHe.getList();
            })
        })

        $("body").on("click", ".tf-ul li", function () {
            var flag1 = false;
            $(".ra1").each(function () {
                if($(this).is(':checked')){
                    flag1 = true;
                }
            });

            if(!flag1){
                layer.alert("请选择排课组合");
                return;
            }

            if($(this).find("i").hasClass("rrgt")){
                layer.alert("该教学班已经存在于某一个组合中，无法选择");
                return;
            }

            if(!$(this).find("i").hasClass("rrgtH") && !$(this).hasClass("tfCur")){
                layer.alert("该教学班与其他教学班存在冲突，无法选择");
                return;
            }

            var flag = false;
            $(".ra1").each(function () {
                if($(this).is(':checked')){
                    flag = true;
                }
            });
            if(flag){
                $(".jhh").show();
                $('.bg').show();
                $(".jhhSp").text("正在检测冲突，请稍候...");
                var obj = $(this);
                setTimeout(function () {
                    bf = true;
                    cls = obj.find('i').hasClass("rrgtH");
                    var jxbId = obj.find("i").attr("ids");
                    paiKeZuHe.getJxbListIsChongTu(jxbId,obj);
                    if (bf == false) {
                        layer.alert("该教学班与其他教学班冲突，无法选择");
                    } else {
                        if (cls) {
                            obj.find('i').removeClass("rrgtH");
                        }
                        obj.toggleClass('tfCur');
                    }
                    $(".jhh").hide();
                    $('.bg').hide();
                },500)
            }else{
                layer.alert("请选择排课组合");
            }
        })
        $('body').on('click', '.ra1', function () {
            if ($(this).is(':checked')) {
                $(this).addClass("raCk").parent().parent().siblings().find('input').removeClass('raCk')
            }
        })
        $("body").on("click", ".cctv", function () {
            $(this).find('i').toggleClass('rr-t').toggleClass('rr-d');
            $(this).siblings('ul').toggle();
            $(this).addClass('tf-li').parent().siblings().find('li').removeClass('tf-li')
        })

        $("body").on("click", "#update", function () {
            if ($(".raCk").attr("ids") == undefined) {
                layer.alert("请选中需要添加的组合");
                return;
            } else {
                paiKeZuHe.updateDto();
            }
            $(".tfCur").each(function () {
                $(this).removeClass("tfCur");
            });

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
            par.xqid = ciId;
            par.gradeId = $(".grade").val();

            Common.getData('/n33PaikeZuHe/clearKeBiaoByGroup.do', par, function (rep) {
                if (rep.code == '200') {
                    layer.msg("撤销成功！");
                    paiKeZuHe.getKbByGroup(zuHeName,zuHeId);
                    paiKeZuHe.getZuHeJXBList(zuHeName,zuHeId);
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
                par.xqid = ciId;
                par.gradeId = $(".grade").val();
                par.type = $(".jxbType").val();
                par.x = $(this).attr("x");
                par.y = $(this).attr("y");
                Common.getData('/n33PaikeZuHe/saveKeBiaoByGroup.do', par, function (rep) {
                    if (rep.code == '200') {
                        layer.msg("放入成功！");
                        paiKeZuHe.getKbByGroup(zuHeName,zuHeId);
                        paiKeZuHe.getZuHeJXBList(zuHeName,zuHeId);
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
                    paiKeZuHe.getXYJXBList();
                }else{
                    $(".jxbs").each(function () {
                        $(this).find("div").removeClass("has");
                    });
                }

            }

        });

        $("body").on("click", "#paiKe" ,function () {
            $(".jhh").show();
            $('.bg').show();
            $(".jhhSp").text("正在检测冲突，请稍候...");
            var id = $(this).attr("ids");
            var zuHeName = $(this).attr("zuHename");

            setTimeout(function () {
                var ids = id;
                paiKeZuHe.getListBySchoolIdTime();
                paiKeZuHe.getGradeWeek();
                paiKeZuHe.ChuShi();
                paiKeZuHe.getZouBanTimeList();
                paiKeZuHe.getKbByGroup(zuHeName,id);
                paiKeZuHe.getZuHeConflictedSettledJXB(ids);
                paiKeZuHe.getZuHeJXBList(zuHeName,id);
                $(".bg").hide();
                $(".jhh").hide();
            },500)
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
                    paiKeZuHe.saveZuHeJXBByJXBID($(this).attr("ids"));
                }else if($(this).find(".has").length == 0){
                    paiKeZuHe.clearZuHeJXBByJXBID($(this).attr("ids"));
                }
            }
        });

        paiKeZuHe.getDefaultCi();
        paiKeZuHe.getGradeList();
        initGrade();
        paiKeZuHe.getVirturlClass();
        paiKeZuHe.getList();
        paiKeZuHe.checkYKB();
    }

    paiKeZuHe.saveZuHeJXBByJXBID = function (ids){
        zuHeId = $(".active1").attr("zuHeId");
        zuHeName = $(".active1").attr("zuHename");
        var par = {};
        par.x = $(".active1").attr("x");
        par.y = $(".active1").attr("y");
        par.zuHeId = $(".active1").attr("zuHeId");
        par.jxbId = ids;
        par.xqid = ciId;
        par.gradeId = $(".grade").val();
        Common.getData('/n33PaikeZuHe/saveZuHeJXBByJXBID.do', par, function (rep) {
            layer.msg(rep.message);
            //paiKeZuHe.getKbByGroup(zuHeName,zuHeId);
            paiKeZuHe.getZuHeJXBList(zuHeName,zuHeId);
            paiKeZuHe.getXYJXBList();
        });
    }

    paiKeZuHe.clearZuHeJXBByJXBID = function (ids){
        zuHeId = $(".active1").attr("zuHeId");
        zuHeName = $(".active1").attr("zuHename");
        var par = {};
        par.x = $(".active1").attr("x");
        par.y = $(".active1").attr("y");
        par.zuHeId = $(".active1").attr("zuHeId");
        par.jxbId = ids;
        par.xqid = ciId;
        par.gradeId = $(".grade").val();
        Common.getData('/n33PaikeZuHe/clearZuHeJXBByJXBID.do', par, function (rep) {
            layer.msg(rep.message);
            paiKeZuHe.getZuHeJXBList(zuHeName,zuHeId);
            paiKeZuHe.getXYJXBList();
            var flag = true;
            $(".jxbs").each(function () {
                if($(this).find(".has").length == 0){
                    flag = false;
                }
            });
            if(flag){
                paiKeZuHe.getKbByGroup(zuHeName,zuHeId);
            }
        });
    }

    paiKeZuHe.getXYJXBList = function(){
        var par = {};
        par.x = $(".active1").attr("x");
        par.y = $(".active1").attr("y");
        par.zuHeId = $(".active1").attr("zuHeId");
        par.xqid = ciId;
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

    paiKeZuHe.checkYKB = function () {
        var par = {};
        par.gradeId = $(".grade").val();
        par.termId = ciId;
        Common.getData('/paike/checkYKB.do', par, function (rep) {
        });
    }

    paiKeZuHe.getKbByGroup = function(name,ids){
        $(".itt").each(function (i,st) {
            $(st).removeClass("active1");
            if($(st).find(".ls").length > 0){
                $(this).html("");
            }

        });
        var par = {};
        par.zuHeId = ids;
        par.xqid = ciId;
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

    paiKeZuHe.getZuHeJXBList = function(name,ids){
        var map = {};
        map.zuHeId = ids;
        map.xqid = ciId;
        map.gradeId = $(".grade").val();
        map.type = $(".jxbType").val();
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

    paiKeZuHe.getZuHeConflictedSettledJXB = function(ids){
        var par = {};
        par.zuHeId = ids;
        par.xqid = ciId;
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

    paiKeZuHe.updateDto = function () {
        var map = {};
        var array = new Array();
        map.id = $(".raCk").attr("ids");
        $(".tfCur").each(function () {
            array.push($(this).find("i").attr("ids"));
        })
        map.jxbIds = array;
        Common.getPostBodyData('/n33PaikeZuHe/updateDto.do', map, function (rep) {
            layer.msg("操作完成");
            paiKeZuHe.getList();
            $(".ra1[ids="+map.id+"]").prop("checked",true)
            $(".ra1[ids="+map.id+"]").addClass("raCk")
        })
    }

    paiKeZuHe.addZuHe = function () {
        var dto = {};
        dto.name = $("#zuHeName").val();
        dto.termId = ciId;
        dto.gradeId = $(".grade").val();
        dto.type = $(".jxbType").val();
        dto.jxbIds = new Array();
        Common.getPostBodyData('/n33PaikeZuHe/addDto.do', dto, function (rep) {
            layer.msg("操作完成");
            paiKeZuHe.getList();
        })
    }

    paiKeZuHe.getList = function () {
        var par = {};
        par.gradeId = $(".grade").val();
        par.zuHeType = $(".jxbType").val();
        par.ciId = ciId;
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
            $(".jxbId").each(function (i,st) {
                $(st).removeClass("rrgt");
                $.each(rep.message,function (r,at) {
                    $(at.jxbIds).each(function (o,rt) {
                        if($(st).attr("ids") == rt){
                            $(st).addClass("rrgt");
                        }
                    });
                });
            });

        })
    }

    paiKeZuHe.getListBySchoolIdTime = function () {
        var xqid = ciId;
        if (xqid && xqid != null) {
            Common.getData('/courseset/getListBySchoolId.do', {"xqid": xqid}, function (rep) {
                Common.render({tmpl: $('#ks_temp'), data: rep, context: '.kejie', overwrite: 1});

            });
        }
    }

    paiKeZuHe.getGradeWeek = function () {
        var par = {};
        par.ciId = ciId;
        par.gradeId = $(".grade").val();
        if(par.ciId != null && par.ciId != "" && par.gradeId != null && par.gradeId != ""){
            Common.getData('/n33PaikeZuHe/getGradeWeek.do', par, function (rep) {
                Common.render({tmpl: $('#weeks_temp'), data: rep.message, context: '.weeks', overwrite: 1});
            });
        }
    }

    paiKeZuHe.ChuShi = function () {
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

    paiKeZuHe.getZouBanTimeList = function () {
        var par = {}
        par.xqid = ciId;
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

    function initGrade(){
        try {
            Common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            $(".grade").val(obj.value);
                            $(".grade").change();
                        }
                    })

                } else {

                }
            });
        } catch (x) {

        }
    }


    module.exports = paiKeZuHe;
})