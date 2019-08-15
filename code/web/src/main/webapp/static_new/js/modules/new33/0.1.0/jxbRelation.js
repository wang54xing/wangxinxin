/**
 * Created by albin on 2018/3/30.
 */
define('jxbRelation', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var jxbRelation = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var gradeId = "";
    var zouBanType = "";

    function getStatusSetJXB() {
        $.ajax({
            type: "GET",
            url: '/virtualClass/getStatusSetJXB.do',
            async: false,
            cache: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {
                if (rep.st == -1) {
                    $('.bg').hide();
                    $(".jhh").hide();
                    window.clearInterval(c);
                    jxbRelation.getVirturlClass();
                    layer.msg("设置教学班完成");
                } else {
                    $(".jhhSp").text("正在设置教学班，请稍候...");
                }
            }
        });
    }

    jxbRelation.init = function () {
        $("body").on("click", ".eq", function () {
            if($(this).hasClass("tdCur")){
                $(".eq").removeClass("tdCur");
            }else{
                $(".eq").removeClass("tdCur");
                $(this).toggleClass('tdCur');
            }
        })
        $("#shiFang").click(function () {
            if(flag){
                layer.alert("课表已发布，无法释放拼班数据");
                return;
            }
            var par={}
            par.id = $(".tdCur").attr("ids");

            if (par.id != undefined && par.id != "") {
                layer.confirm('是否释放拼班？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    Common.getData('/virtualClass/shiFangXuNiBan.do.do', par, function (rep) {
                        if (rep.code == 200) {
                            layer.msg("释放成功")
                            jxbRelation.getVirturlClass();
                        } else {
                            layer.msg("服务器异常")
                        }
                    });
                }, function () {
                });
            }else{
                layer.msg("请选择标签")
            }
        })

        $("body").on("change", "#grade", function () {
            jxbRelation.initVirturlClass();
            jxbRelation.getVirturlClass();
        });

        $(".jxbType").change(function () {
            jxbRelation.getVirturlClass();
        });

        jxbRelation.getDefaultTerm();
        var flag = getCiIdIsFaBu();
        jxbRelation.getGrade();
        jxbRelation.initVirturlClass();
        //jxbRelation.getVirturlClass();
        initGrade();

        $("body").on("click","#auto",function(){
            if(flag){
                layer.alert("课表已发布，无法自动拼班");
                return;
            }
            $(".jhh").show();
            $(".bg").show();
            $(".jhhSp").text("拼班中,请稍候...");
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $("#grade").val();
            setTimeout(function () {
                Common.getData('/virtualClass/AutoFenBanBySubject.do', par, function (rep) {
                    if(rep.code == 200){
                        layer.msg("操作成功")
                        jxbRelation.getVirturlClass();
                    }else{
                        layer.msg("服务器异常")
                    }
                });
                $(".jhh").hide();
                $(".bg").hide();
            },500);
        });

        $("body").on("click","#autojxb",function(){
            if(flag){
                layer.alert("课表已发布，无法自动设置教学班");
                return;
            }
            $(".jhh").show();
            $(".bg").show();
            $(".jhhSp").text("正在设置教学班，请稍候...");
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $("#grade").val();
            setTimeout(function () {
                $.ajax({
                    type: "GET",
                    data: JSON.stringify(par),
                    url: '/virtualClass/AutoSetJXB.do?gradeId=' + par.gradeId + "&xqid=" + par.xqid,
                    async: true,
                    cache: false,
                    contentType: 'application/json',
                    success: function (rep) {
                        if (rep.code == 500) {
                            window.clearInterval(c);
                            $(".jhh").hide();
                            layer.alert("服务器异常");
                        }
                    }
                });
                c = setInterval(getStatusSetJXB, 500);
            },500);
        });

        $("body").on("change", "#isHide", function () {
            jxbRelation.getVirturlClass();
        });

        $("body").on("change", ".finish", function () {
            var par = {};
            if ($(this).is(":checked")) {
                par.isFinish = 1;
            } else {
                par.isFinish = 0;
            }
            par.id = $(this).attr("ids");
            par.subId = $(this).attr("subId");
            par.xqid = deXqid;
            Common.getData('/virtualClass/finishFenBan.do', par, function (rep) {
                jxbRelation.getVirturlClass();
            })
        });

        $("body").on("click",".clear",function(){
            if(flag){
                layer.alert("课表已发布，无法清空教学班");
                return;
            }
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $("#grade").val();
            par.type = $(".jxbType").val();
            layer.confirm('确认清空？', {
                    btn: ['确定', '取消'] //按钮
                },function () {
                Common.getData('/virtualClass/clearAllJxb.do', par, function (rep) {
                    if(rep.code == 200){
                        layer.msg("操作成功")
                        jxbRelation.getVirturlClass();
                    }else{
                        layer.msg("服务器异常")
                    }
                });
            },function () {
            });
        });

        $("body").on("click",".qk",function(){
            if(flag){
                layer.alert("课表已发布，无法清空教学班");
                return;
            }
            var par = {};
            par.xuNiBanId = $(this).attr("ids");
            par.subId = $(this).attr("subId");
            par.jxbId = $(this).attr("jxbId");
            if(par.jxbId == "" || par.jxbId == undefined || par.jxbId == null){
                layer.msg("未设置教学班");
                return;
            }
            Common.getData('/virtualClass/clearJXB.do', par, function (rep) {
                if(rep.code == 200){
                    layer.msg("操作成功")
                }else{
                    layer.msg("服务器异常")
                }
            });
            jxbRelation.getVirturlClass();
            e.stopPropagation();
        });

        $("body").on("click", ".xzf", function () {
            if(flag){
                layer.alert("课表已发布，无法选择教学班");
                return;
            }
            var par = {};
            par.xqid = deXqid;
            par.subjectId = $(this).attr("subId");
            par.gradeId = $("#grade").val();
            if ($(".jxbType").val() == 0) {
                par.subType2 = 1;
            } else {
                par.subType2 = 2;
            }
            Common.getData('/n33_setJXB/getAllJXBList.do', par, function (rep) {
                Common.render({tmpl: $('#jxb_temp'), data: rep.message, context: '#jxb', overwrite: 1});

            })
            $("#qdjxb").attr("xuNiBanId", $(this).attr("ids"));
            $("#qdjxb").attr("subId", $(this).attr("subId"));
            if ($(this).attr("jxbId") == "" || $(this).attr("jxbId") == undefined || $(this).attr("jxbId") == null) {
                $("#qdjxb").attr("oJxbId", "*");
            } else {
                $("#qdjxb").attr("oJxbId", $(this).attr("jxbId"));
            }
            $("#jxb").val($(this).attr("jxbId"));
            $('.bg').show();
            $('.xz-popup').show();
            e.stopPropagation();
        });

        $("body").on("click", "#qdjxb", function () {
            var par = {};
            par.xuNiBanId = $(this).attr("xuNiBanId");
            par.subId = $(this).attr("subId");
            par.oJxbId = $(this).attr("oJxbId");
            par.jxbId = $("#jxb").val();
            if(par.jxbId == "" || par.jxbId == null || par.jxbId == undefined){
                layer.msg("请选择教学班");
                return;
            }
            Common.getData('/virtualClass/selectJXB.do', par, function (rep) {
                layer.msg(rep.message)
            });
            $('.bg').hide();
            $('.xz-popup').hide();
            jxbRelation.getVirturlClass();

        });

        $("body").on("click", ".szz", function () {
            $("#qdtea").attr("jxbId", $(this).attr("jxbId"));
            jxbRelation.getTeacherList($(this).attr("subId"));
            $("#teacher").val($(this).attr("tid"));
            $('.bg').show();
            $('.szz-popup').show();
        });

        $("#qdtea").click(function () {
            jxbRelation.setJxbTeacher($("#qdtea").attr("jxbId"));
            $('.bg').hide();
            $('.szz-popup').hide();
        });

        $("body").on("click", ".sz", function () {
            $("#qdclass").attr("jxbId", $(this).attr("jxbId"));
            jxbRelation.getClassRoomList();
            $("#room").val($(this).attr("roomId"));
            $('.bg').show();
            $('.sz-popup').show();
        });

        $("#qdclass").click(function () {
            jxbRelation.setJxbRoom($("#qdclass").attr("jxbId"));
            $('.bg').hide();
            $('.sz-popup').hide();
        });

        $('body').on('click','#xuniban li',function(){
            var ti = $(this).find('.ti');
            if(ti.attr("isBigger") == 1){
                layer.msg("该拼班组合人数大于教学班容量，点击确定进行拼班");
                $(this).css("border",'1px solid red');
            }
            ti.addClass('tii').parent().siblings().find('.ti').removeClass('tii');
            $("#qdpuzzle").attr("ids",$(this).attr("ids"));
        })

        //点击确定拼班按钮
        $('body').on("click","#qdpuzzle",function () {
            var par = {};
            par.pinId = $('.tdCur').attr("ids");
            par.pinById = $(this).attr("ids")
            par.xqid = deXqid;
            par.gradeId = $("#grade").val();
            Common.getData('/virtualClass/puzzleClass.do', par, function (rep) {

            });
            $('.bg').hide();
            $('.pb-popup').hide();
            jxbRelation.getVirturlClass();
        });

        //计算冲突
        $(".chatt-bt").click(function () {
//            $('.loading-p').show();
            var par = {};
            par.cid = deXqid;
            par.gradeId = $("#grade").val();
            Common.getData('/studentTag/conflictDetection.do', par, function (rep) {
                layer.msg(rep.message);
//                $('.loading-p').hide();
            })
        })
        //点击相同学科组合和教学班颜色相同
        $('body').on('click','.hcur',function(){
             var te = $(this).text();
            $('.hcur').each(function(){
                   if($(this).text()==te){
                       $(this).toggleClass('trCur');

                   }else{
                       $(this).removeClass('trCur')
                   }
               })
        })
        $('body').on('click','.bcur',function(){
            var te = $(this).text();

            $('.bcur').each(function(){
                if($(this).text()==te && $(this).attr('jxbId')!=""){
                    $(this).toggleClass('trCur');

                }else{
                    $(this).removeClass('trCur')
                }
            })
        })
        //点击拼班按钮
        $("body").on("click","#pinban",function () {
            if(flag){
                layer.alert("课表已发布，无法拼班");
                return;
            }
            var par = {};
            par.virtualClassId = $('.tdCur').attr("ids");
            if(par.virtualClassId == "" || par.virtualClassId == undefined || par.virtualClassId == null){
                layer.msg("请选择标签")
                return;
            }
            $('.bg').show();
            $('.pb-popup').show();
            par.type = $(".jxbType").val();
            par.xqid = deXqid;
            par.gradeId = $("#grade").val();
            Common.getData('/virtualClass/getVirtualClassForCombineClass.do', par, function (rep) {
                Common.render({tmpl: $('#xunibanList'), data: rep.message.retList, context: '#xuniban', overwrite: 1});
                if(rep.message.aware == undefined){
                    if(rep.message.retList.length==0){
                        $('#xuniban').append('<li class="end">无可拼学生标签组</li>')
                    }
                    $("#xuniban li").each(function(s,ot){
                        if($(this).attr("ids") == rep.message.id){
                            $(this).find("#tuijian").addClass("tjj")
                        }
                    });
                }else{
                    layer.msg(rep.message.aware);
                    $('.bg').hide();
                    $('.pb-popup').hide();
                }
            })
        });

    }

    //设置教室
    jxbRelation.setJxbRoom = function (jxbId) {
        var roomId = $("#room").val();
        Common.getData('/n33_fenbaninfoset/setJxbRoom.do', {"jxbId": jxbId, "roomId": roomId}, function (rep) {
            if (rep.code == 200) {
                jxbRelation.getVirturlClass();
            }
        })
    }

    //获取所有的教室供选择
    jxbRelation.getClassRoomList = function () {
        var gradeId = $("#grade").val();
        Common.getData('/n33_fenbaninfoset/getClassRoomList.do', {"gradeId": gradeId}, function (rep) {
            Common.render({tmpl: $('#room_temp'), data: rep, context: '#room', overwrite: 1});
        })
    }

    //设置教师
    jxbRelation.setJxbTeacher = function (jxbId) {
        var teacherId = $("#teacher").val();
        Common.getData('/n33_fenbaninfoset/setJxbTeacher.do', {"jxbId": jxbId, "teacherId": teacherId}, function (rep) {
            if (rep.code == 200) {
                jxbRelation.getVirturlClass();
            }
        })
    }

    //获取所有的老师供选择
    jxbRelation.getTeacherList = function (subjectId) {
        var gradeId = $("#grade").val();
        Common.getData('/n33_fenbaninfoset/getTeacherList.do', {
            "gradeId": gradeId,
            "subjectId": subjectId
        }, function (rep) {
            Common.render({tmpl: $('#teacher_temp'), data: rep, context: '#teacher', overwrite: 1});
        })
    }

    //查询所有的虚拟班
    jxbRelation.getVirturlClass = function () {
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $("#grade").val();
        par.zouBanType = $(".jxbType").val();
        if ($("#isHide").is(":checked")) {
            par.isHide = 1;
        } else {
            par.isHide = 0;
        }
        Common.getData('/virtualClass/getVirturlClass.do', par, function (rep) {
            if(rep.message.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            Common.render({tmpl: $('#jxbRelationList'), data: rep.message, context: '#jxbRelation', overwrite: 1});

        })

        $(".teaName").each(function (i, st) {
            if ($(this).attr("jxbId") == "" || $(this).attr("jxbId") == undefined || $(this).attr("jxbId") == null) {
                $(this).hide();
            }
        });
        $(".classRoom").each(function (i, st) {
            if ($(this).attr("jxbId") == "" || $(this).attr("jxbId") == undefined || $(this).attr("jxbId") == null) {
                $(this).hide();
            }
        });
        var flag = getCiIdIsFaBu();
        if(flag){
            $(".finish").prop("disabled","disabled");
        }
    }

    //将所有的标签初始化为虚拟班
    jxbRelation.initVirturlClass = function () {
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $("#grade").val();
        if(par.xqid != "" && par.xqid != undefined && par.xqid != null && par.gradeId != "" && par.gradeId != undefined && par.gradeId != null){
            Common.getData('/virtualClass/initVirturlClass.do', par, function (rep) {

            });
        }
    }

    //获取默认学期和默认排课次
    jxbRelation.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            deXqid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }

    //获取年级
    jxbRelation.getGrade = function () {
        if(deXqid != "" && deXqid != undefined && deXqid != null){
            Common.getData('/new33isolateMange/getGradList.do', {"xqid": deXqid}, function (rep) {
                Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade',overwrite : 1});
                //$("#grade em:eq(0)").addClass("cur");
            })
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

                        if(obj.key == "zouBanType"){
                            zouBanType = obj.value;
                        }
                    })
                    $("#grade").val(gradeId);
                    $("#grade").change();
                    $(".jxbType").val(zouBanType).change();
                    if($(".jxbType").val() == null){
                        $(".jxbType").val(0);
                        $(".jxbType").change();
                    }
                } else {

                }
            });
        } catch (x) {

        }
    }

    function getCiIdIsFaBu() {
        var flag = null;
        var xqid = $("#term .cur").attr("ids");
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId":deXqid}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }
    module.exports = jxbRelation;
})