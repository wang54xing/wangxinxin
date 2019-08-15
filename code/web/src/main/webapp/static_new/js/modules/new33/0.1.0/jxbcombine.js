/**
 * Created by albin on 2018/3/30.
 */
define('jxbcombine', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var jxbcombine = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var gradeId = "";

    jxbcombine.init = function () {
        $("body").on("change", ".grade", function () {
            jxbcombine.getJXBZuHe();
            initJxbCount();
            initJxbCount1();
            $('.tdName').each(function(){
                if($(this).attr("ids") != "undefined"){
                    $(this).css('cursor','pointer');
                }
            });
        });

        $(".type").change(function () {
            jxbcombine.getJXBZuHe();
            initJxbCount();
            initJxbCount1();
            $('.tdName').each(function(){
                if($(this).attr("ids") != "undefined"){
                    $(this).css('cursor','pointer');
                }
            });
        });

        $('.qxx,.close').click(function () {
            $('.bg').hide();
            $('.setp-popup').hide();
            $('.selp-popup').hide();
            $('.name-popup').hide();
            $('.tj-popup').hide();
        })

        $('body').on('click','#jxbCombine tr td',function(){
            var te = $(this).text();
            if($(this).attr("zuHeName") == undefined){
                $(".xjJxb").each(function (i,st) {
                    $(st).css("color","");
                });
            }
            if($(this).attr('ids') != "undefined"){
                $('.tdName').each(function(){
                    if($(this).text()==te){
                        $(this).toggleClass('trCur');
                    }else{
                        $(this).removeClass('trCur')
                    }
                });
            }
        });

        $("body").on("click","#save",function () {
            jxbcombine.saveJXBZuHe();
            jxbcombine.getJXBZuHe();
        });

        $("body").on("click",".xjJxb",function(){
            var par = {};
            var gradeId = $(".grade").val();
            par.name = $(this).attr("zuHeName");
            par.jxbId = $(this).attr("ids");
            par.ciId = deXqid;
            par.gradeId = gradeId;
            par.type = $(".type").val();
            $(".xjJxb").each(function (i,st) {
                $(st).css("color","");
            });
            Common.getData('/n33PaikeZuHe/getZuHeNoCTJXB.do', par, function (rep) {
                $(".xjJxb").each(function (i,st) {
                    $.each(rep.message,function (s,ot) {
                        $.each(ot.jxbList,function (t,rt) {
                            if($(st).attr("zuHeName") == ot.name && $(st).attr("ids") == rt){
                                $(st).css("color","#66CD00");
                            }
                        });
                    })
                });
            });
        });

        $('body').on("mouseover",".dialog",function () {
            $('.dialog').show();
        });

        //var index='';
        $('body').on("mouseover",".xjJxb",function (e) {
            var string = $(this).text();
            string += ":   相交教学班";
            $('#name').text(string)
            var par = {};
            par.jxbId = $(this).attr("ids");
            par.gradeId = $(".grade").val();
            par.ciId = deXqid;
            Common.getData('/n33PaikeZuHe/getBuFenCTJXB.do', par, function (rep) {
                Common.render({tmpl: $('#jxb_temp'), data: rep.message, context: '#jxb',overwrite : 1});
            });
            var x=e.clientX;
            var y=e.clientY - 142;
            var x2=e.offsetX + 1;
            var y2=e.offsetY + 1;
            //var scroll=$(document).scrollTop();
           // alert(scroll)
            $('.dialog').css({'margin-left':' 170px','position':'fixed','left':' 50%','top':' 50%','margin-top:':'-220px','margin-top':'-220px'}).show();
        });

        $('body').on("mouseout",".xjJxb",function () {
            $('.dialog').hide();
        });

        $("#set_teacher").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            jxbcombine.setJxbTeacher($("#set_teacher").attr("jxbid"));
            jxbcombine.getJXBZuHe();
            $('.bg').hide();
            $('.setp-popup').hide();
            $('.selp-popup').hide();
        });

        $("#set_room").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            jxbcombine.setJxbRoom($("#set_room").attr("jxbid"));
            jxbcombine.getJXBZuHe();
            $('.bg').hide();
            $('.setp-popup').hide();
            $('.selp-popup').hide();
        });

        $("body").on("click",".editTea",function () {
            $("#set_teacher").attr("jxbid", $(this).parent().attr("jxbid"));
            jxbcombine.getTeacherList($(this).parent().attr("subId"));
            $("#teacher").val($(this).parent().attr("teaId"));
            $('.bg').show();
            $('.setp-popup').show();
        });

        $("body").on("click",".editRoom",function () {
            $("#set_room").attr("jxbid", $(this).parent().attr("jxbid"));
            jxbcombine.getClassRoomList();
            $("#room").val($(this).parent().attr("roomid"));
            $('.bg').show();
            $('.selp-popup').show();
        });

        $("body").on("click",".b9",function () {
            var gradeId = $(".grade").val();
            if(deXqid != "" && deXqid != undefined && deXqid != null && gradeId != "" && gradeId != null && gradeId != undefined) {
                var par = {};
                par.ciId = deXqid;
                par.gradeId = gradeId;
                par.type = $(".type").val();
                par.count = $("#count").text();
                par.count1 = $("#count1").text();
                window.location.href = "/n33PaikeZuHe/exportZuHe.do?ciId=" + par.ciId + "&gradeId=" + par.gradeId + "&type=" + par.type + "&count=" + par.count + "&count1=" + par.count1;
            }
        });
        jxbcombine.getDefaultTerm();
        jxbcombine.getGrade();
        initGrade();
       // jxbcombine.getJXBZuHe();
        initJxbCount();
        initJxbCount1();
    }

    jxbcombine.detactConfliction = function () {
        var gradeId = $(".grade").val();
        var cid = deXqid;
        $(".jhh").show();
        setTimeout(function () {
            Common.getData('/n33_fenbaninfoset/detactConfliction.do', {"gradeId": gradeId, "cid": cid}, function (rep) {
                $(".jhh").hide();
                layer.msg("保存成功!");
            })
        }, 100)

    }

    jxbcombine.setJxbRoom = function (jxbid) {
        var roomId = $("#room").val();
        Common.getData('/n33_fenbaninfoset/setJxbRoom.do', {"jxbId": jxbid, "roomId": roomId}, function (rep) {

        })
    }

    jxbcombine.getClassRoomList = function () {
        var gradeId = $(".grade").val();
        Common.getData('/n33_fenbaninfoset/getClassRoomList.do', {"gradeId": gradeId}, function (rep) {
            Common.render({tmpl: $('#room_temp'), data: rep, context: '#room', overwrite: 1});
        })
    }

    jxbcombine.setJxbTeacher = function (jxbid) {
        var teacherId = $("#teacher").val();
        Common.getData('/n33_fenbaninfoset/setJxbTeacher.do', {"jxbId": jxbid, "teacherId": teacherId}, function (rep) {

        })
    }
    $(document).ready(function(){
        $('.tdName').each(function(){
            if($(this).attr("ids") != "undefined"){
                $(this).css('cursor','pointer');
            }
        });
    })

    jxbcombine.getTeacherList = function (subjectId) {
        var gradeId = $(".grade").val();
        Common.getData('/n33_fenbaninfoset/getTeacherList.do', {
            "gradeId": gradeId,
            "subjectId": subjectId
        }, function (rep) {
            Common.render({tmpl: $('#teacher_temp'), data: rep, context: '#teacher', overwrite: 1});
        })
    }

    //获取默认学期和默认排课次
    jxbcombine.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            deXqid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.paikeci);
        });
    }

    //获取年级
    jxbcombine.getGrade = function () {
        if(deXqid != "" && deXqid != undefined && deXqid != null){
            Common.getData('/new33isolateMange/getGradList.do', {"xqid": deXqid}, function (rep) {
                Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.grade',overwrite : 1});
                //$("#grade em:eq(0)").addClass("cur");
            })
        }
    }

    jxbcombine.getJXBZuHe = function () {
        var gradeId = $(".grade").val();
        if(deXqid != "" && deXqid != undefined && deXqid != null && gradeId != "" && gradeId != null && gradeId != undefined){
            var par = {};
            par.ciId = deXqid;
            par.gradeId = gradeId;
            par.zuHeType = $(".type").val();
            Common.getData('/n33PaikeZuHe/getJXBZuHeList.do', par, function (rep) {
                if(rep.message.length==0){
                    $("#none_png").show();
                    $("#content").hide();
                }
                else{
                    $("#none_png").hide();
                    $("#content").show();
                }
                Common.render({tmpl: $('#jxbCombineList'), data: rep.message, context: '#jxbCombine',overwrite : 1});
            })
            $('#jxbCombine td').each(function(){
                if($(this).attr('roomid') !='undefined' && $(this).attr('teaid')=='undefined'){
                    var roomId = $(this).attr("roomid");
                    $(this).append('<img class="lset name editRoom" src="/static_new/images/new33/edit.png">');
                }else if($(this).attr('roomid') =='undefined' && $(this).attr('teaid') !='undefined'){
                    var teaId = $(this).attr("teaid");
                    $(this).append('<img class="lset name editTea" src="/static_new/images/new33/edit.png">');
                }
            })
        }
    }

    jxbcombine.saveJXBZuHe = function () {
        var gradeId = $(".grade").val();
        if(deXqid != "" && deXqid != undefined && deXqid != null && gradeId != "" && gradeId != null && gradeId != undefined){
            var par = {};
            par.ciId = deXqid;
            par.gradeId = gradeId;
            par.type = $(".type").val();

            layer.confirm('此操作会删除原数据，是否执行？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                $(".layui-layer").hide();
                Common.getData('/n33PaikeZuHe/saveJXBZuHeList.do', par, function (rep) {
                    jxbcombine.detactConfliction();
                    //stuAdmini.getSchoolSelects();
                });
            },function () {
            });
        }
    }

    function initGrade(){
        try {
            Common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                        if(obj.key == "zuHeType"){
                            $(".type").val(obj.value);
                            $(".type").change();
                        }
                    })
                    $(".grade").val(gradeId);
                    $(".grade").change();
                } else {

                }
            });
        } catch (x) {

        }
    }

    function initJxbCount(){
        var count = 0;
        /*$("#jxbCombine tr td:nth-child(2)").each(function (s,ot) {
            if($(this).text() != ""){
                count ++;
            }
        });*/
        $(".tdName").each(function (s,ot) {
            if($(this).attr("ids") != "undefined" && $(this).attr("rowspan") != "undefined"){
                count ++;
            }
        });
        $("#count").text("（" + count + "）");
    }

    function initJxbCount1(){
        var array = new Array();
        $(".tdName").each(function (s,ot) {
            if($(ot).attr("rowspan") == "undefined" && $(ot).text() != "-" && $(ot).attr("ids") != "undefined"){
                var flag = true;
                $.each(array,function (i,st) {
                    if(st == $(ot).text()){
                        flag = false;
                    }
                });
                if(flag){
                    array.push($(ot).text());
                }
            }
        });
        $("#count1").text("（" + array.length + "）");
    }

    function getCiIdIsFaBu() {
        var flag = null;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId": deXqid}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }

    module.exports = jxbcombine;
})