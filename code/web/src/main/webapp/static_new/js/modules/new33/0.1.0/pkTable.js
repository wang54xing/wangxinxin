/**
 * Created by albin on 2018/3/19.
 */
define('pkTable', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var pkTable = {};
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
    var zlength = 5;
    var jxbId = "";
    var pkflg = false;
    var weekArg = new Array("周一", "周二", "周三", "周四", "周五", "周六", "周日");
    var wk = 1;
    var kejis = "";
    var x = "";
    var y = "";
    var colorFlag = true;

    var desc = "";

    function getStatus() {
        $.ajax({
            type: "GET",
            url: '/paike/getStatus.do',
            async: false,
            cache: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {
                if (rep.st == -1) {
                    $('.bg').hide();
                    $(".jhh").hide();
                    window.clearInterval(c);
                    pkTable.GetPositionsByWeek();
                    layer.msg("发布成功");
                } else {
                    if (rep.st == undefined || rep.st == 999) {
                        $(".jhhSp").text("正在保存当前数据为命名课表。");
                    } else {
                        $(".jhhSp").text("发布第" + rep.st + "周数据中,请稍候。。");
                    }
                }
            }
        });
    }

    function getStatusPK() {
        $.ajax({
            type: "GET",
            url: '/paike/getStatusPK.do',
            async: false,
            cache: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {
                if (rep.st == -1) {
                    $('.bg').hide();
                    $(".jhh").hide();
                    window.clearInterval(c);
                    if($("#classroom").is(":checked")){
                        pkTable.GetPositionsByWeek();
                        pkTable.getRoomEntryListByXqGrade();
                        pkTable.getList();
                        pkTable.getKeBiaoList(1);
                        pkTable.getZouBanTimeList(1);
                        $('.jxbs').empty();
                        $('.xkb').empty();
                        pkTable.getkpjxb();
                    }else if($("#teacher").is(":checked")){
                        pkTable.getTeacherList();
                        pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
                        pkTable.ChuShiByTea();
                        pkTable.getGuDingShiWu();
                        pkTable.getGDSX();
                        pkTable.getTeaShiWuByXqid();
                        pkTable.GetTeachersSettledPositions();
                        $('.jxbs').empty();
                        $('.xkb').empty();
                    }
                    layer.msg("自动排课成功");
                } else {
                    if(rep.zrenshu!=0&&rep.zrenshu!=""&&rep.zrenshu!=undefined) {
                        $(".jhhSp").text("自动排课中," + rep.renshu + "/" + rep.zrenshu+"人");
                    }else{
                        $(".jhhSp").text("正在自动化排课中,请稍后");
                    }
                }
            }
        });
    }

    pkTable.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            xqid = rep.message.paikeci;
            zhenXqid = rep.message.paikexq;
            $("#defaultTerm").attr("ids", rep.message.paikeci);
        });
    }

    pkTable.GetPositionsByWeek = function () {
        Common.getData('/paike/GetPositionsByWeek.do', {
            "xqid": zhenXqid,
            "gid": $(".gaozhong .active").attr("ids"),
            "week": wk
        }, function (rep) {
            if (rep.message >= 0) {
                $(".tag .b8").show();
                $(".tag .b7").show();
                $('#tagcheck').hide();
            } else {
                $(".tag .b7").show();
                $(".tag .b8").hide();
                $('#tagcheck').hide();
            }
        })
    }
    pkTable.getListByXq = function () {
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
    pkTable.getTagList = function (type) {
        var dto = {};
        dto.xqid = xqid;
        dto.gradeId = $(".gaozhong .active").attr("ids");
        var tagid = "";
        if (type == 1) {
            tagid = $(".classrooms .t-li").attr("ids");
        }
        Common.getData('/studentTag/getTagListKS.do', dto, function (rep) {
            $(".classrooms").empty();
            Common.render({tmpl: $('#tag_templ'), data: rep.message, context: '.classrooms'});
            if (type == 1) {
                $(".classrooms li").each(function () {
                    if ($(this).attr("ids") == tagid) {
                        $(this).addClass("t-li");
                    }
                });
            } else {
                $(".classrooms li:eq(0)").addClass("t-li");
                $('#clsrom').text($(".classrooms li:eq(0)").find("span").attr("tname"));
            }
            var ypks = $(".classrooms .t-li").find("span").attr("ypks");
            var ks = $(".classrooms .t-li").find("span").attr("ks");
            $("#ypks").text(ypks);
            $("#allks").text(ks + "课时)");
        })
    }
    pkTable.getkpjxbByTag = function () {
        var par = {};
        par.gid = $(".gaozhong .active").attr("ids");
        ;
        par.type = $('#jxbType').val();
        par.tagId = $(".classrooms .t-li").attr("ids")
        Common.getData('/paike/getkpjxbByTag.do', par, function (rep) {
            if (rep.code == '200') {
                $('#tagByTeaID').hide();
                $('.subject').hide();
                $('.kpjxblist').show();
                $('.kpjxblist').empty();
                Common.render({tmpl: $('#kpjxblistTempByTeaId'), data: rep.message, context: '.kpjxblist'});
            }

        })
    }

    pkTable.deleteRecord = function () {
        var par = {};
        par.xqid = xqid;
        Common.getData('/paike/deleteRecord.do', par, function (rep) {
        });
    }

    pkTable.init = function () {
        $("#tiaozheng").click(function(){
            pkTable.deleteRecord();
            setTimeout(function () {
                window.location.href="/newisolatepage/pkTableTiao.do"
            },500);

        })
        $(".b99").click(function () {
            //检测信息完善程度
            var par = {};
            par.gradeId = $(".gaozhong .active").attr("ids");
            par.ciId = xqid;
            Common.getData('/paike/checkJXBInfo.do', par, function (rep) {
                if (rep.message == 0) {
                    layer.confirm('您将清空所有的排课数据，然后自动排课？', {
                        btn: ['确定', '取消'] //按钮
                    }, function () {
                        layer.msg('清空中。。。', {time: 1000});
                        setTimeout(function(){
                            $(".jhh").show();
                            $('.bg').show();
                            $(".jhhSp").text("正在自动化排课中，请稍候...");
                            $.ajax({
                                type: "post",
                                data: JSON.stringify(par),
                                //url: '/paike/ZiDongPaiKe.do?gradeId=' + par.gradeId + "&ciId=" + xqid + "&xqid=" + zhenXqid,
                                url: '/paike/ZiDongPaiKeZuHe.do?gradeId=' + par.gradeId + "&ciId=" + xqid + "&xqid=" + zhenXqid,
                                async: true,
                                cache: false,
                                contentType: 'application/json',
                                success: function (rep) {
                                    if (rep.code == 500) {
                                        window.clearInterval(c);
                                        $(".jhh").hide();
                                        $('.bg').hide();
                                        // $('.jhh').show();
                                        layer.alert("自动排课发生异常");
                                    }
                                }
                            });
                            c = setInterval(getStatusPK, 2000);
                        },1000)
                    }, function () {
                    });
                }
                else {
                    layer.msg("请将所有教学班的学生、老师、教室信息设置完毕后重试;")
                }
            })
        })

        $("#tag").click(function () {
            $(".itt").html("")
            pkTable.getSubjectList();
            pkTable.ChuShiByTea();
            pkTable.getGuDingShiWu();
            pkTable.getGDSX();
            pkTable.getTagList();
            pkTable.getkpjxbByTag();
            pkTable.GetTeachersSettledPositionsTag();
            var len = zlength * 128;
            $('th>div').css('width', len + 'px');
            $('.fixedcont .ul1').css('width', '180');
            $('#tagcheck').hide();
            $('#gid').show();
            $('#tid').hide();
            $('#gid').addClass("one")
            //$('#gid').removeClass("one")
        })
        pkTable.getDefaultTerm();
        pkTable.getListByXq();
        $("#jxbType").change(function () {
            if ($("#classroom").is(":checked")) {
                // pkTable.getGradeSubjectList();
                pkTable.getkpjxb();
            } else if ($("#teacher").is(":checked")) {
                pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
            } else {
                pkTable.getkpjxbByTag();
            }

        });
        $(".queding").click(function () {
            var par = {};
            par.gid = $(".gaozhong .active").attr("ids");
            par.nid = $("#uu .bul-cur").attr("ids");
            if (par.nid != "" && par.nid != undefined) {
                $(".jhh").show();
                $(".bg").show();
                $(".bt-popup").hide()
                $(".jhhSp").text("加载课表中,请稍候..")
                setTimeout(function () {
                    Common.getData('/paike/getDtoByNid.do', par, function (rep) {
                    })
                    pkTable.getKeBiaoList(2);
                    // pkTable.getGradeSubjectList();
                    pkTable.getkpjxb();
                    $('.bg').hide();
                    $('.bt-popup').hide();
                    var len = zlength * 128;
                    $('th>div').css('width', len + 'px');
                    $(".jhh").hide();
                    $(".bg").hide();
                }, 500)
            }
        })


        $('.qxx,.close').click(function () {
            $('.bg').hide();
            $('.bf-popup').hide();
            $('.bt-popup').hide();
            $('.fb-popup').hide();
            $('.z-popup').hide();
            $('.z-popup1').hide();
        })
        $('.fb').click(function () {
            pkTable.getFaBuLogs();
        })
        $('body').on('click', '.bul li', function () {
            $(this).addClass('bul-cur').siblings().removeClass('bul-cur');
        });
        
        //跳转到排课规则页面
        $('body').on('click', '#pkRule', function () {
        	var gradeId = $('.gaozhong label[class="active"]').attr('ids');
        	window.location.href='/newisolatepage/pkRuleSet.do?gradeId=' + gradeId;
        });
        
        $('body').on('click', '.tb-clsrom', function () {
            if (pkflg) {
                if (!$(this).hasClass("gray")) {
                    var obj = $(this);
                    if ($("#classroom").is(":checked")) {
                        if(obj.hasClass("red")){
                            layer.confirm('排课时间存在事务，是否排课？', {
                                btn: ['确定', '取消'] //按钮
                            }, function () {
                                pkTable.saveKeBiaoInfo(obj.attr('ykbid'), jxbId);
                                obj.addClass("shi");
                                obj.addClass("pic");
                            },function () {
                            });
                        }else{
                            pkTable.saveKeBiaoInfo(obj.attr('ykbid'), jxbId);
                        }

                    } else if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                        if(obj.hasClass("red")){
                            layer.confirm('排课时间存在事务，是否排课？', {
                                btn: ['确定', '取消'] //按钮
                            }, function () {
                                pkTable.saveKeBiaoInfoByTea(obj.attr("x"), obj.attr("y"));
                            },function () {
                            });
                        }else{
                            pkTable.saveKeBiaoInfoByTea(obj.attr("x"), obj.attr("y"));
                        }
                    }
                }
            }
        });
        $('body').on('click', '.kpjxblist li', function (e) {
            e.stopPropagation();
            if ($(this).hasClass('jxbbac')) {
                $(this).removeClass('jxbbac');
                jxbId = "";
                pkflg = false;
                if ($("#classroom").is(":checked")) {
                    pkTable.getKeBiaoList(2);
                    pkTable.getZouBanTimeList(1);
                }
                if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                    $(".itt").html("")
                    pkTable.ChuShiByTea();
                    pkTable.getGuDingShiWu();
                    pkTable.getGDSX();
                    pkTable.getTeaShiWuByXqid();
                    if ($("#teacher").is(":checked")) {
                        pkTable.GetTeachersSettledPositions();
                    } else {
                        pkTable.GetTeachersSettledPositionsTag();
                    }
                }
            } else {
                $(this).addClass('jxbbac').siblings().removeClass('jxbbac');
                if ($("#classroom").is(":checked")) {
                    pkTable.getKeBiaoList(2);
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
                    pkTable.getConflictedSettledJXB($(this).attr('jxbid'));
                } else if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                    jxbId = $(this).attr('jxbid');
                    pkTable.getConflictedSettledJXBByTea($(this).attr('jxbid'));
                }
            }
        });

        $('body').on('click', '.classrooms li', function () {
            $(this).addClass('t-li').siblings().removeClass('t-li');
            if ($("#classroom").is(":checked")) {
                var string = $(".classrooms>.t-li").find("span").attr("tname");
                $('#clsrom').text(string);
                var ypks = $(".classrooms>.t-li").find("span").attr("ypks");
                var ks = $(".classrooms>.t-li").find("span").attr("ks");
                $("#ypks").text(ypks);
                $("#allks").text(ks);
                pkTable.getkpjxb();
                pkTable.getKeBiaoList(2);
                pkTable.getZouBanTimeList(1);
                $('.cont1').empty();
            } else if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                $(".itt").html("")
                pkTable.ChuShiByTea();
                pkTable.getGuDingShiWu();
                pkTable.getGDSX();
                pkTable.getTeaShiWuByXqid();
                if ($("#teacher").is(":checked")) {
                    $('#clsrom').text($(this).find("span").attr("tname"));
                    // var ypks = $(".classrooms>.t-li").find("span").attr("ypks");
                    // var ks = $(".classrooms>.t-li").find("span").attr("ks");
                    // $("#ypks").text(ypks);
                    // $("#allks").text(ks);
                    pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
                    pkTable.GetTeachersSettledPositions();
                    $('.cont1').empty();
                } else {
                    $('#clsrom').text($(this).find("span").attr("tname"));
                    var ypks = $(".classrooms>.t-li").find("span").attr("ypks");
                    var ks = $(".classrooms>.t-li").find("span").attr("ks");
                    $("#ypks").text(ypks);
                    $("#allks").text(ks);
                    pkTable.getkpjxbByTag();
                    pkTable.GetTeachersSettledPositionsTag();
                }
            }

        })
        $(document).ready(function () {
            // var len = $('.weeks th div div').length;
            var len = zlength * 128;
            $('th>div').css('width', len + 'px');
        })


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
            $("#fabName").html("发布")
            $(".stff").attr("iz", 1);
        })
        $(".tag .b8").click(function () {
            $('.bg').show();
            $("#fabName").html("取消发布")
            $('.bf-popup').show();
            $(".stff").attr("iz", 2);
        })
        $(".stff").click(function () {
            $('.bg').hide();
            $('.bf-popup').hide();
            var par = {};
            par.staWeek = $(".kaishi").val();
            par.endWeek = $(".jieshu").val();
            par.gid = $(".gaozhong .active").attr("ids");
            par.cid = xqid;
            par.xqid = zhenXqid;
            if ($(this).attr("iz") == 1) {
                $(".jhhSp").text("正在保存当前数据为命名课表。");
                $(".jhh").show();
                $('.bg').show();
                $.ajax({
                    type: "post",
                    data: JSON.stringify(par),
                    url: '/paike/faBuKeBiao.do?staWeek=' + par.staWeek + "&endWeek=" + par.endWeek + "&gid=" + par.gid + "&cid=" + par.cid,
                    async: true,
                    cache: false,
                    contentType: 'application/json',
                    success: function (rep) {
                        if (rep.code == 500) {
                            $(".jhh").hide();
                            $('.bg').hide();
                            window.clearInterval(c);
                            layer.alert("发布出现异常");
                        }
                    }
                });
                c = setInterval(getStatus, 1000);
            } else {
                layer.confirm('是否删除自习课？', {
                    btn: ['否', '是'], //按钮
                    success: function(layero){
                        layero.find('.layui-layer-btn').css({
                            'overflow': 'hidden',
                            'width': '65%',
                            'margin': '0 auto'
                        });
                        layero.find('.layui-layer-btn0').css('float', 'right');
                        layero.find('.layui-layer-btn1').css('float', 'left');
                        layero.find('.layui-layer-content').css('text-align', 'center');
                    }
                }, function () {
                    $('.layer-anim').hide();
                    $(".jhhSp").text("正在取消发布课表...请稍后");
                    $(".jhh").show();
                    $('.bg').show();
                    setTimeout(function () {
                        Common.getData('/paike/delZhouKeBiao.do', par, function (rep) {
                            layer.msg("取消成功");
                            $(".jhh").hide();
                            $('.bg').hide();
                            pkTable.GetPositionsByWeek();
                        })
                    })
                },function () {
                    $('.layer-anim').hide();
                    $(".jhhSp").text("正在取消发布课表...请稍后");
                    $(".jhh").show();
                    $('.bg').show();
                    var ciIds = new Array();
                    setTimeout(function () {
                        Common.getData('/paike/getCiIds.do', par, function (rep){
                            $(rep.message).each(function (i,st) {
                                ciIds.push(st);
                            });
                            if(ciIds.length > 0){
                                var delmap = {};
                                delmap.ciIds = ciIds;
                                delmap.gid = $(".gaozhong .active").attr("ids");
                                Common.getPostBodyData('/n33_zixi/removeZiXiBanGidAndCids.do',delmap,function (rep) {
                                    if(rep.code == 200){
                                        Common.getData('/paike/delZhouKeBiao.do', par, function (rep) {
                                            layer.msg("取消成功");
                                            $(".jhh").hide();
                                            $('.bg').hide();
                                            pkTable.GetPositionsByWeek();
                                        })
                                    }else{
                                        layer.msg("删除自习课失败，请联系管理员！");
                                    }
                                })
                            }else{
                                Common.getData('/paike/delZhouKeBiao.do', par, function (rep) {
                                    layer.msg("取消成功");
                                    $(".jhh").hide();
                                    $('.bg').hide();
                                    pkTable.GetPositionsByWeek();
                                })
                            }
                        });
                    },500)
                });
            }
        })

        $('body').on("mouseover",".citt",function () {
            var obj = $(this);
            var flag = false;
            $($(this).find("p")).each(function (i,st) {
                if($(this).hasClass("shi")){
                    flag = true;
                }
            })
            if(flag){
                pkTable.getTeaShiWuByYKB(obj);
            }
        });

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

        $('body').on("click",".cx",function () {
            $('.cont').removeClass('e9')
            if ($('.actv') == null || $('.actv').length == 0) {
                layer.alert("请选择撤销课！");
            } else {
                var ykbIds = "";
                if ($("#classroom").is(":checked")) {
                    $('.actv').each(function (i) {
                        if ($(this).attr('jxbid') != '') {
                            ykbIds += $(this).attr('ykbid') + ",";
                        }
                    });
                    if (ykbIds == '') {
                        layer.alert("请选择撤销课！");
                        return;
                    }
                    pkTable.undoYKB(ykbIds);
                    pkTable.getList();
                } else if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                    var count = 0;
                    $('.actv').each(function (i) {
                        if ($(this).attr('ykbid') != "" && $(this).attr('ykbid') != undefined && $(this).attr('ykbid') != null) {
                            count++;
                            ykbIds += $(this).attr('ykbid') + ",";
                        }
                    });
                    if (count != 0) {
                        pkTable.undoYKB(ykbIds);
                    } else {
                        layer.alert("该课节未排课！");
                    }
                }

            }
            var len = zlength * 128;
            $('th>div').css('width', len + 'px');
        });
        $(".b2").click(function () {
            if ($('.actv') == null || $('.actv').length == 0) {
                layer.alert("请选择撤销课！");
            } else {
                var ykbIds = "";
                if ($("#classroom").is(":checked")) {
                    $('.actv').each(function (i) {
                        if ($(this).attr('jxbid') != '') {
                            ykbIds += $(this).attr('ykbid') + ",";
                        }
                    });
                    if (ykbIds == '') {
                        layer.alert("请选择有课撤销！");
                        return;
                    }
                    pkTable.undoYKB(ykbIds);
                    pkTable.getList();
                } else if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                    var count = 0;
                    $('.actv').each(function (i) {
                        if ($(this).attr('ykbid') != "" && $(this).attr('ykbid') != undefined && $(this).attr('ykbid') != null) {
                            count++;
                            ykbIds += $(this).attr('ykbid') + ",";
                        }
                    });
                    if (count != 0) {
                        pkTable.undoYKB(ykbIds);
                    } else {
                        layer.alert("该课节未排课！");
                    }
                }

            }
            var len = zlength * 128;
            $('th>div').css('width', len + 'px');
        });

        $(".b3").click(function () {
            // layer.confirm('您将清空所有的排课数据，确认清空？', {
            //     btn: ['确定', '取消'] //按钮
            // }, function () {
            //     layer.msg('清空中。。。', {time: 1000});
            //     pkTable.clearKB();
            //     var len = zlength * 128;
            //     $('th>div').css('width', len + 'px');
            //     pkTable.checkYKB();
            // }, function () {
            // });
            if($("#classroom").is(":checked")){
                $('.z-popup').show();
                $('.bg').show();
            }else{
                $('.z-popup1').show();
                $('.bg').show();
            }
        })

        $('body').on("click",'#clearAll',function () {
            layer.confirm('您将清空所有的排课数据，确认清空？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('清空中。。。', {time: 1000});
                pkTable.clearKB();
                var len = zlength * 128;
                $('th>div').css('width', len + 'px');
                pkTable.checkYKB();
                $('.z-popup ').hide();
                $('.bg').hide();
            }, function () {
            });
        });
        $('body').on("click",'#clearAllTea',function () {
            layer.confirm('您将清空所有的排课数据，确认清空？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('清空中。。。', {time: 1000});
                pkTable.clearKB();
                var len = zlength * 128;
                $('th>div').css('width', len + 'px');
                pkTable.checkYKB();
                $('.z-popup1').hide();
                $('.bg').hide();
            }, function () {
            });
        });

        $('body').on("click",'#clearByCid',function () {
            layer.confirm('您将清空当前教室排课数据，确认清空？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('清空中。。。', {time: 1000});
                pkTable.clearKBByCid();
                // var len = zlength * 128;
                // $('th>div').css('width', len + 'px');
                pkTable.checkYKB();
                $('.z-popup ').hide();
                $('.bg').hide();
            }, function () {
            });
        });

        $('body').on("click",'#clearByTea',function () {
            layer.confirm('您将清空当前教师排课数据，确认清空？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                layer.msg('清空中。。。', {time: 1000});
                pkTable.clearKBByTea();
                // var len = zlength * 128;
                // $('th>div').css('width', len + 'px');
                pkTable.checkYKB();
                $('.z-popup1').hide();
                $('.bg').hide();
            }, function () {
            });
        });

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
            pkTable.getKeBiaoList(2);
        });
        pkTable.getGradeList();
        pkTable.getGradeWeekRangeByXqid();
        pkTable.GetPositionsByWeek();
        pkTable.getRoomEntryListByXqGrade();
        pkTable.getKeBiaoList();
        pkTable.getZouBanTimeList(1);
        // pkTable.getGradeSubjectList();
        pkTable.checkYKB();
        pkTable.getkpjxb();
        pkTable.getList();
        // pkTable.getDesc(1);
        // pkTable.initDesc();
        $("body").on("click", ".subject li", function () {
            $('#subjectId').val($(this).attr('subjectId'));
            subid = $(this).attr('subjectId');
            pkTable.getkpjxb();
        });

        //按照教师排课
        $("body").on("click", "#teacher", function () {
            $(".itt").html("")
            pkTable.getSubjectList();
            pkTable.getTeacherList();
            pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
            pkTable.ChuShiByTea();
            pkTable.getGuDingShiWu();
            pkTable.getGDSX();
            pkTable.getTeaShiWuByXqid();
            pkTable.GetTeachersSettledPositions();
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
            $('.cont1').empty();
        })
        $("body").on("click", "#classroom", function () {
            pkTable.getSubjectList();
            pkTable.getRoomEntryListByXqGrade();
            // pkTable.getGradeSubjectList();
            pkTable.getkpjxb();
            pkTable.getKeBiaoList();
            pkTable.getZouBanTimeList(1);
            pkTable.getList();
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
            $('.cont1').empty();
        })


        $("body").on("click", "#sub label", function () {
            $('#sub label').each(function (i) {
                $(this).removeClass('active');
            })
            $(this).addClass('active');
            $(".itt").html("")
            pkTable.ChuShiByTea();
            pkTable.getTeacherList();
            pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
            pkTable.getGuDingShiWu();
            pkTable.getGDSX();
            pkTable.getTeaShiWuByXqid();
            pkTable.GetTeachersSettledPositions();
        });


        $('.gaozhong label').click(function () {
            $('.gaozhong label').each(function (i) {
                $(this).removeClass('active');
            })
            $(this).addClass('active');
            pkTable.checkYKB();
            if ($("#classroom").is(":checked")) {
                pkTable.GetPositionsByWeek();
                pkTable.getRoomEntryListByXqGrade();
                pkTable.getKeBiaoList(2);
                pkTable.getZouBanTimeList(1);
                pkTable.getList();
                // pkTable.getGradeSubjectList();
                pkTable.getkpjxb();
            } else if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                $(".itt").html("")
                pkTable.ChuShiByTea();
                if ($("#teacher").is(":checked")) {
                    pkTable.getSubjectList();
                    pkTable.getTeacherList();
                    pkTable.getGuDingShiWu();
                    pkTable.getGDSX();
                    pkTable.getTeaShiWuByXqid();
                    pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
                    pkTable.GetTeachersSettledPositions();
                } else {
                    pkTable.getTagList();
                    pkTable.getkpjxbByTag();
                    pkTable.GetTeachersSettledPositionsTag();
                }
            }

        })
        $('body').on('click','.ls',function(){
            var text = $(this).find('.vjs').text();
            console.log(text);
            $('.ls').each(function(){
                if($(this).find('.vjs').text()==text){
                    $(this).find('.vjs').addClass('font');

                }else{
                    $(this).find('.vjs').removeClass('font')
                }
            })
        })
        //收起移动窗
        $('.fixedcont .info .p1 em').click(function () {
            $('.fixedcont .cont,#movebg').addClass('h0');
            $(this).hide();
            $('#move').css('width', '130px');
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
        $('body').on('click', '.tab2 td>div>div', function () {
            if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                if ($(this).hasClass('red')) {

                } else {
                    if ($(this).hasClass('active')) {
                        $(this).removeClass('actv');
                        $(this).removeClass('active');
                        $('.cont').removeClass('e9');
                        $('.jxbs').empty();
                        $('.xkb').empty();
                        $('.cont1').empty();
                        pkTable.getZhuanXiangKeList();
                    } else {
                        $('.tab2 td>div>div').each(function (i) {
                            $(this).removeClass('actv');
                            $(this).removeClass('active');
                            $('.cont').removeClass('e9');
                        });
                        if (!pkflg && $(this).attr("kbtype") != 0 && $(this).attr("kbtype") != undefined && $(this).attr("kbtype") != null) {
                            if ($(this).hasClass('actv')) {
                                $('.cont1').empty();
                                $(this).removeClass('actv');
                                $('.cont').removeClass('e9');
                            } else {
                                $(this).addClass('actv');
                                pkTable.getJXBById($(this).attr("ykbId"),$(this).attr("x"),$(this).attr("y"));
                            }
                            pkTable.getZhuanXiangKeList();
                            $(this).toggleClass('active');
                            if(colorFlag){
                                $('.cont').toggleClass('e9');
                            }
                        } else if (!pkflg && ($(this).attr("kbtype") == 0 || $(this).attr("kbtype") == undefined || $(this).attr("kbtype") == null)) {
                            if ($(this).hasClass('actv')) {
                                $(this).removeClass('actv');
                                $('.cont1').empty();
                                $('.cont').removeClass('e9');
                            } else {
                                $(this).addClass('actv');
                                pkTable.getJXBByXY($(this).attr("x"), $(this).attr("y"));
                            }
                            $(this).toggleClass('active');
                            if(colorFlag){
                                $('.cont').toggleClass('e9');
                            }
                        }
                    }
                }
            } else {
                if ($(this).hasClass('red')) {

                } else {
                    if ($(this).hasClass('active')) {
                        $(this).removeClass('actv');
                        $(this).removeClass('active');
                        $('.cont').removeClass('e9');
                        $('.jxbs').empty();
                        $('.xkb').empty();
                        $('.cont1').empty();
                        pkTable.getZhuanXiangKeList();
                    } else {
                        $('.tab2 td>div>div').each(function (i) {
                            $(this).removeClass('actv');
                            $(this).removeClass('active');
                            $('.cont').removeClass('e9');
                            $('.cont1').empty();
                        });
                        if (!pkflg && $(this).attr("kbtype") != 0 && $(this).attr("kbtype") != undefined && $(this).attr("kbtype") != null) {
                            if ($(this).hasClass('actv')) {
                                $(this).removeClass('actv');
                                $('.cont1').empty();
                                $('.cont').removeClass('e9');
                            } else {
                                $(this).addClass('actv');
                                pkTable.getJXBById($(this).attr('ykbid'),$(this).attr("x"),$(this).attr("y"));
                            }
                            pkTable.getZhuanXiangKeList();
                            $(this).toggleClass('active');
                            if(colorFlag){
                                $('.cont').toggleClass('e9');
                            }

                        }else if(!pkflg && ($(this).attr("kbtype") == 0 || $(this).attr("kbtype") == undefined || $(this).attr("kbtype") == null)){
                            if ($(this).hasClass('actv')) {
                                $(this).removeClass('actv');
                                $('.cont1').empty();
                                $('.cont').removeClass('e9');
                            } else {
                                $(this).addClass('actv');
                                    pkTable.getJXBByXY($(this).attr("x"),$(this).attr("y"));
                            }
                            pkTable.getZhuanXiangKeList();
                            if(colorFlag){
                                $('.cont').toggleClass('e9');
                            }
                            $(this).toggleClass('active');
                        }
                    }

                }
            }
        });

        //点击课表表格(按照教师课表)
        /*$('body').on('click', '.tab2 td>div>div', function () {
         if($("#teacher").is(":checked") || $("#tag").is(":checked")){
         if ($(this).hasClass('red')) {

         } else {
         if (!pkflg && $(this).attr("ykbId") != "" && $(this).attr("ykbId") != undefined && $(this).attr("ykbId") != null) {
         if ($(this).hasClass('actv')) {
         $(this).removeClass('actv');
         } else {
         $(this).addClass('actv');
         pkTable.getJXBById($(this).attr("ykbId"));
         }
         $(this).toggleClass('active');
         }
         }
         }
         });*/

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

        $('body').on("click","#swConflict",function () {
            pkTable.getShiWuConflict();
        });

        //自动排课
        $('.zdpk').click(function () {
            var flag = pkTable.getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许重新自动排课");
                return false;
            }
            layer.confirm('是否进行教学班冲突检测？', {
                btn: ['确定', '取消'] //按钮
            }, function (index) {
                layer.close(index);
                pkTable.conflictDetection();
            }, function () {
                pkTable.getAcClassType();
            });
        });

        var aotuPk = sessionStorage.getItem("ATCKB_aotuPk") || "";
        if(aotuPk=="Y"){
            pkTable.autoPaiKeFeiZouBan();
            sessionStorage.removeItem("ATCKB_aotuPk");
        }

        $('body').on("click", "#onlyNoZouBan",function () {
            $('.bg').hide();
            $('.zdpk-popup').hide();
            pkTable.autoPaiKeFeiZouBan();
        });

        /*$('body').on("click", "#zouAndNoZou",function () {

        });*/

        $('.zdpk_close').click(function () {
            $('.bg').hide();
            $('.zdpk-popup').hide();
        })

        $('#zbfzb').click(function () {
            var gradeId = $('.gaozhong .active').attr('ids')||"";
            Common.goTo('/newisolatepage/jxbAutoTimeCombine.do?gradeId='+gradeId);
        });
    }

    //计算冲突
    pkTable.conflictDetection = function() {
        var index = layer.load(2, {
            content: '冲突检测中...',
            success: function (layero) {
                layero.find('.layui-layer-content').css({
                    'paddingTop': '40px',
                    'width': '100px',
                    'textAlign': 'center',
                    'backgroundPositionX': 'center'
                });
            }
        });
        var par = {};
        par.gradeId = $('.gaozhong .active').attr('ids')||"";
        par.cid = xqid;
        setTimeout(function(){
            Common.getData('/studentTag/conflictDetection.do', par, function (rep) {
                layer.close(index);
                if (rep.code == '200') {
                    layer.msg("检测完成", {time:2000});

                    //$('.bg').show()
                    //$('.zdpk-popup').show();
                    pkTable.getAcClassType();

                }
            });
        }, "100");
    };


    pkTable.getCiIdIsFaBu = function() {
        var flag = null;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId": xqid}, function (rep) {
            flag = rep.message;
        });
        return flag;
    };

    pkTable.getAcClassType = function(){
        var data = {};
        data.gradeId = $('.gaozhong .active').attr('ids')||"";
        data.ciId = xqid;
        Common.getPostData('/n33_set/getAcClassType.do', data, function (rep) {
            if (rep.code == '200') {
                if(rep.message==1){
                    pkTable.autoPaiKeFeiZouBan();
                }else{
                    $('.bg').show()
                    $('.zdpk-popup').show();
                }
            }
        });
    };

    pkTable.autoPaiKeFeiZouBan = function(){
        var index = layer.load(2);
        var data = {};
        data.gradeId = $('.gaozhong .active').attr('ids')||"";
        data.xqId = zhenXqid;
        data.ciId = xqid;
        setTimeout(function(){
            Common.getPostData('/paike/autoPaiKeFeiZouBan.do', data, function (rep) {
                layer.close(index);
                if (rep.code == '200') {
                    layer.msg("自动排课完成");
                    pkTable.getKeBiaoList();
                    pkTable.getZouBanTimeList(1);
                    pkTable.checkYKB();
                    pkTable.getkpjxb();
                    pkTable.getList();
                }
            });
        }, "100");
    };

    pkTable.initDesc = function(){
        if($("#teacher").is(":checked")){
            $('.itt').each(function (i,st) {
                if(($(this).find('p').length == 0) && ($(this).text() == null || $(this).text().trim() == "")){
                    $(this).text(desc)
                }
            });
        }else{
            $('.citt').each(function (i,st) {
                if(($(this).find('p').length == 0) || $(this).find("div").length == 0){
                    $(this).text(desc)
                }
            });
        }
    }

    pkTable.getDesc = function (kbType) {
        var par = {};
        par.ciId = xqid;
        var gradeId = "";
        par.kbType = kbType;
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.type = 3;
        Common.getData('/n33_set/getDESCForNoCourse.do', par, function (rep) {
            desc = rep.message;
        });
    }

    pkTable.getShiWuConflict = function(){
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.paikeci = xqid;
        Common.getData('/gdsx/getShiWuConflict.do', par, function (rep) {
            if(rep.message != null && rep.message.length !=0){
                Common.render({tmpl: $('#conflict_temp'), data: rep.message, context: '#conflict',overwrite : 1});
                $('#tabDv1').hide();
                $('#tabDv2').show();
                $('.z-popup2').show();
                $('.bg').show();
            }else{
                $('.z-popup2').show();
                $('.bg').show();
                $('#tabDv1').show();
                $('#tabDv2').hide();
            }

        })
    }

    pkTable.getTeaShiWuByYKB = function(obj){
        var classRoomId = $(".t-li").attr("ids");
        var ykbId = obj.attr("ykbId");
        var x = obj.attr("x");
        var y = obj.attr("y");
        Common.getData('/paike/getTeaShiWu.do', {"ykbId" :ykbId,"classRoomId":classRoomId,"x":x,"y":y}, function (rep) {
            var string = "";
            $.each(rep.message,function (i,st) {
                string += st.desc;
                string += "\r\n";
            });
            obj.attr("title",string)
        })
    }

    pkTable.clearKBByTea = function(){
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.xqcid = xqid;
        var teacherId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                teacherId = $(this).attr('ids');
            }
        });
        par.teaId = teacherId;
        Common.getData('/paike/clearKBByTea.do', par, function (rep) {
            if (rep.code == '200') {
                layer.msg("清除成功！");
                pkTable.getTeacherList();
                pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
                pkTable.ChuShiByTea();
                pkTable.getGuDingShiWu();
                pkTable.getGDSX();
                pkTable.getTeaShiWuByXqid();
                pkTable.GetTeachersSettledPositions();
                $('.jxbs').empty();
                $('.xkb').empty();

            }
        });

    }

    pkTable.clearKBByCid = function(){
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.xqcid = xqid;
        var classRoomId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                classRoomId = $(this).attr('ids');
            }
        });
        par.classRoomId = classRoomId;
        Common.getData('/paike/clearKBByCid.do', par, function (rep) {
            if (rep.code == '200') {
                layer.msg("清除成功！");
                pkTable.getRoomEntryListByXqGrade();
                pkTable.getList();
                pkTable.getKeBiaoList(1);
                pkTable.getZouBanTimeList(1);
                $('.jxbs').empty();
                $('.xkb').empty();
                pkTable.getkpjxb();
            }
        });

    }

    //源课表格子所有课(根据xy)
    pkTable.getJXBByXY = function (x, y) {
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


    //源课表格子所有课
    pkTable.getJXBById = function (ykbId,x,y) {
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
    pkTable.getGradeWeekRangeByXqid = function () {
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
                if (rep != null) {
                    zlength = rep.end;
                }
            });
        }
    }
    pkTable.checkYKB = function () {
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
    pkTable.getRoomEntryListByXqGrade = function () {
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
                if (rep.code == '200') {
                    $(".classrooms").empty();
                    Common.render({tmpl: $('#classrooms_templ'), data: rep.message, context: '.classrooms'});
                    $(".classrooms li:eq(0)").addClass("t-li");
                    $('#clsrom').text($(".classrooms li:eq(0)").find("span").text());
                }
            });

            // if(par.xqid != "" && par.xqid != undefined && par.xqid != null && par.gradeId != "" && par.gradeId != undefined && par.gradeId != null){
            //     Common.getData('/paike/getRoomEntryListByXqGradeAndTerm.do', par, function (rep) {
            //         $(".jiaoshi").empty();
            //         $(".jiaoshi").append(" <span>教室 : </span>")
            //         $(".jiaoshi").append(" <label class='active all' js='all'>全部</label>")
            //         Common.render({tmpl: $('#jiaos'), data: rep.message, context: '.jiaoshi'});
            //     });
            // }
        }
    }


    pkTable.undoYKB = function (ykbIds) {
        var par = {};
        par.ykbIds = ykbIds;
        Common.getData('/paike/undoYKB.do', par, function (rep) {
            if (rep.code == '200') {
                layer.msg("撤销成功！");
                $('.jxbs').empty();
                $('.xkb').empty();
                if ($("#classroom").is(":checked")) {
                    pkTable.getKeBiaoList(2);
                    pkTable.getkpjxb();
                    pkTable.getZouBanTimeList(1);
                } else if ($("#teacher").is(":checked") || $("#tag").is(":checked")) {
                    $(".itt").html("")
                    pkTable.ChuShiByTea();
                    pkTable.getGuDingShiWu();
                    pkTable.getGDSX();
                    pkTable.getTeaShiWuByXqid();
                    if ($("#teacher").is(":checked")) {
                        pkTable.getTeacherList(1);
                        pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
                        pkTable.GetTeachersSettledPositions();
                    } else {
                        pkTable.getTagList(1);
                        pkTable.getkpjxbByTag();
                        pkTable.GetTeachersSettledPositionsTag();
                    }
                }
            } else {
                layer.msg("撤销失败！")
            }
        });
    }

    pkTable.clearKB = function () {
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
                if($("#classroom").is(":checked")){
                    pkTable.getRoomEntryListByXqGrade();
                    pkTable.getList();
                    pkTable.getKeBiaoList(1);
                    pkTable.getZouBanTimeList(1);
                    $('.jxbs').empty();
                    $('.xkb').empty();
                    pkTable.getkpjxb();
                }else{
                    pkTable.getTeacherList();
                    pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
                    pkTable.ChuShiByTea();
                    pkTable.getGuDingShiWu();
                    pkTable.getGDSX();
                    pkTable.getTeaShiWuByXqid();
                    pkTable.GetTeachersSettledPositions();
                    $('.jxbs').empty();
                    $('.xkb').empty();
                }
            }
        });
    }

    pkTable.getGradeList = function () {
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
            $(".gaozhong").append(" <span>年级 : </span>")
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.gaozhong'});
            // $(".gaozhong label:eq(0)").addClass("active");
            pkTable.initGrade();
        })
    }

    pkTable.getZhuanXiangKeList = function (){
        var map = {};
        map.ciId = xqid;
        var ykbIds = new Array();
        $('.actv').each(function (i,st) {
            if($(this).attr("kbType") == 4){
                ykbIds.push($(this).attr("ykbId"));
            }
        });
        map.zhuanXiangIds = ykbIds;
        if(ykbIds.length == 0){
            Common.render({tmpl: $('#zhuanxiang_temp'), data: [], context: '#zhuanxiang',overwrite:1});
        }else{
            Common.getPostBodyData("/paike/getZhuanXiangYKBIds.do",map,function (rep) {
                Common.render({tmpl: $('#zhuanxiang_temp'), data: rep.message, context: '#zhuanxiang',overwrite:1});
            })
        }
    }

    pkTable.getKeBiaoList = function (type) {
        // pkTable.getDesc(1);
        $('.cont').removeClass('e9');
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
        pkTable.getZhuanXiangKeList();
        pkTable.initDesc();
    }
    pkTable.showTable = function () {
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

    pkTable.getkpjxb = function () {
        var par = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        par.gradeId = gradeId;
        par.type = $('#jxbType').val();
        par.termId = xqid;
        var classRoomId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                classRoomId = $(this).attr('ids');
            }
        });
        par.classroomId = classRoomId;

        var map = {};
        map.xqid = xqid;
        map.gid = gradeId;
        map.roomId = classRoomId;
        map.type = $('#jxbType').val();
        var kpcount = "";
        Common.getData('/paike/getRoomJXBCount.do', map, function (rep) {
            kpcount = rep.message;
            $(".kpcount").text(kpcount);
        });

        // if (subjectId!='') {
        Common.getData('/paike/getkpjxbByCrmid.do', par, function (rep) {
            if (rep.code == '200') {
                $('.subject').hide();
                $('.kpjxblist').show();
                $('.kpjxblist').empty();
                $('.jxbs').empty();
                $('.xkb').empty();
                jxbId = "";
                pkflg = false;
                var count = rep.message.length;
                $.each(rep.message,function (i,st) {
                    if(st.ypksCount >= st.zksCount){
                        count--;
                    }
                })
                if((kpcount - count) < 0){
                    $(".ypcount").text(0);
                }else{
                    $(".ypcount").text(kpcount - count);
                }
                Common.render({tmpl: $('#kpjxblistTemp'), data: rep.message, context: '.kpjxblist'});
            }else{
                $(".ypcount").text(0);
            }
        })
        // }

    }

    // pkTable.getGradeSubjectList = function () {
    //     var data = {};
    //     var gradeId = "";
    //     $('.gaozhong label').each(function (i) {
    //         if ($(this).hasClass("active")) {
    //             gradeId = $(this).attr('ids');
    //         }
    //     })
    //     data.gid = gradeId;
    //     data.xqid = xqid;
    //     data.type = $('#jxbType').val();
    //     Common.getData('/new33isolateMange/getSubjectByGradeIdType.do', data, function (rep) {
    //         $("#tagByTeaID").show();
    //         $('.subject').show();
    //         $('.kpjxblist').hide();
    //         $('.subject').empty();
    //         Common.render({tmpl: $('#subjectTrScript'), data: rep.message, context: '.subject'});
    //     })
    // }


    pkTable.getTeaShiWuByXqid = function () {
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
                if(!$(".idx" + (obj.y - 1) + (obj.x - 1)).hasClass("red")){
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).html(obj.desc);
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).addClass("red")
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title",obj.desc);
                }else{
                    var string = $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title");
                    if(string || string == null){
                        string = obj.desc;
                    }else{
                        string += ",";
                        string += obj.desc;
                    }
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title",string);
                }
            })
        })
    }

    pkTable.getGuDingShiWu = function () {
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": zhenXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                if(!$(".idx" + (obj.y - 1) + (obj.x - 1)).hasClass("red")){
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).html(obj.desc);
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).addClass("red")
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title",obj.desc);
                }else{
                    var string = $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title");
                    if(string || string == null){
                        string = obj.desc;
                    }else{
                        string += ",";
                        string += obj.desc;
                    }
                    $(".idx" + (obj.y - 1) + (obj.x - 1)).attr("title",string);
                }
            })
        })
    }

    pkTable.getGDSX = function (){
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
                if($(".idx" + (obj.x) + (obj.y)).hasClass("red")){
                    var string = $(".idx" + (obj.x) + (obj.y)).attr("title");
                    if(string || string == null){
                        string = obj.desc;
                    }else{
                        string += ",";
                        string += obj.desc;
                    }
                    $(".idx" + (obj.x) + (obj.y)).attr("title",string);
                    $(".idx" + (obj.x) + (obj.y)).addClass("red")
                }else{
                    $(".idx" + (obj.x) + (obj.y)).attr("title",obj.desc);
                    $(".idx" + (obj.x) + (obj.y)).text(obj.desc);
                    $(".idx" + (obj.x) + (obj.y)).addClass("red")
                }

            })
        })
    }

    pkTable.ChuShiByTea = function () {
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

    pkTable.getkpjxbByTeaId = function (subjectId) {

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
        var teacherId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                teacherId = $(this).attr('ids');
            }
        });
        par.teaId = teacherId;

        var map = {};
        map.xqid = xqid;
        map.gid = gradeId;
        map.teaId = teacherId;
        map.type = $('#jxbType').val();
        var kpcount = "";
        Common.getData('/paike/getTeacherJXBCount.do', map, function (rep) {
            kpcount = rep.message;
            $(".kpcount").text(kpcount);
        });

        Common.getData('/paike/getkpjxbByTeaId.do', par, function (rep) {
            if (rep.code == '200') {
                $('#tagByTeaID').hide();
                $('.subject').hide();
                $('.kpjxblist').show();
                $('.kpjxblist').empty();
                var count = rep.message.length;
                $.each(rep.message,function (i,st) {
                    if(st.ypksCount >= st.zksCount){
                        count--;
                    }
                })
                jxbId = "";
                //alert(kpcount)
                $(".ypcount").text(kpcount - count);
                Common.render({tmpl: $('#kpjxblistTempByTeaId'), data: rep.message, context: '.kpjxblist'});
            }else{
                $(".ypcount").text(0);
            }
        })
    }

    pkTable.getTeacherList = function (type) {
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

    pkTable.getConflictedSettledJXBByTea = function (jxbId) {
        pkTable.lianTangs(jxbId);
        var data = {};
        data.jxbId = jxbId;
        data.termId = $("#defaultTerm").attr("ids");
        Common.getData('/paike/getConflictedSettledJXB.do', data, function (rep) {
            if (rep.code == '200') {
                $('.jxbs').empty();
                $('.xkb').empty();
                pkflg = true;
                // var sws = rep.message.swcts;
                // if (sws != null && sws.length != 0) {
                //     for (var i = 0; i < sws.length; i++) {
                //         var dt = "idx" + (sws[i].y - 1) + (sws[i].x - 1);
                //         $("." + dt).addClass("red");
                //         $("." + dt).text(sws[i].desc);
                //     }
                // }
                if (rep.message.jxbcts != null && rep.message.jxbcts.length != 0) {
                    for (var j = 0; j < rep.message.jxbcts.length; j++) {
                        var string = "";
                        var dt = "idx" + (rep.message.jxbcts[j].x) + (rep.message.jxbcts[j].y);
                        if($("." + dt).hasClass('red')){
                            $("." + dt).removeClass('red');
                            string = $("." + dt).attr("title")
                            $("." + dt).text("");
                        }

                        $("." + dt).addClass("gray");
                        if ($("." + dt + " p").hasClass('pic')) {
                            // $("."+ dt +" .ls").text(rep.message.jxbcts[j].remarks);
                        } else {
                            if ($(" ." + dt + " .ls").length == 0) {
                                var string = "";
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
                                $("." + dt).attr("title",string);
                            }
                        }

                    }
                }

            }
        });
    }

    pkTable.lianTangs = function (jxbId) {
        var data = {};
        data.jxbId = jxbId;
        Common.getData('/paike/lianTangs.do', data, function (rep) {
            if (rep.code == '200') {
                var three = rep.message.three;
                var four = rep.message.four;
                if (three != null && three.length != 0) {
                    for (var i = 0; i < three.length; i++) {
                        if($(".idx" + three[i].three + " .ls").length == 0){
                            $(".idx" + three[i].three).addClass("liantang");
                            $(".idx" + three[i].three).text("三连堂");
                            $(".idx" + three[i].three).attr("title",three[i].threeJxbName);
                        }else{
                            var string = $(".idx" + three[i].three).attr("title");
                            if(string || string == null){
                                string = "三连堂";
                                string += three[i].threeJxbName;
                            }else{
                                string += ",";
                                string += "三连堂";
                                string += three[i].threeJxbName;
                            }
                            $(".idx" + three[i].three).attr("title",string)
                        }
                    }
                }
                if (four != null && four.length != 0) {
                    for (var i = 0; i < four.length; i++) {
                        if($(".idx" + four[i].four + " .ls").length == 0) {
                            $(".idx" + four[i].four).addClass("liantang");
                            $(".idx" + four[i].four).text("四连堂");
                            $(".idx" + four[i].four).attr("title",four[i].fourJxbName);
                        }else{
                            var string = $(".idx" + four[i].four).attr("title");
                            if(string || string == null){
                                string = "四连堂";
                                string += four[i].fourJxbName;
                            }else{
                                string += ",";
                                string += "四连堂";
                                string += four[i].fourJxbName;
                            }
                            $(".idx" + four[i].four).attr("title",string)
                        }
                    }
                }
            }
        });
    }
    pkTable.getConflictedSettledJXB = function (jxbId) {
        pkTable.lianTangs(jxbId);
        var data = {};
        data.jxbId = jxbId;
        data.termId = $("#defaultTerm").attr("ids");
        var classRoomId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                classRoomId = $(this).attr('ids');
            }
        });
        data.classroomId = classRoomId;
        Common.getData('/paike/getConflictedSettledJXBByRoomId.do', data, function (rep) {
            if (rep.code == '200') {
                $('.jxbs').empty();
                $('.xkb').empty();
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
                            if ($("." + rep.message.jxbcts[j].classroomId + " ." + dt + " .ls").length == 0) {
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
                        }

                        //}
                    }
                }
                pkTable.getZouBanTimeList(1);
            }
        });
    }

    pkTable.GetTeachersSettledPositions = function () {
        // pkTable.getDesc(2);
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
                    if($(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").hasClass("red")){
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
                    str += "<div class='ls'><p class='whide' title=" + obj.JxbName + "(" + obj.count + "人)><em class='vxk'>" + obj.JxbName + "</em><em class='vrs'>(" + obj.count + "人)</em></p><p>" + obj.subName + "</p><p><em class='vjs'>" + obj.roomName + "</em></p></div>";
                    $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").html(str);
                    $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").attr("ykbId", obj.ykbId);
                    $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").attr("kbType", obj.type);
                    $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").attr("jxbId", obj.jxbId);
                })
                pkTable.getZouBanTimeList(2);
            })
        }
        pkTable.initDesc();
    }
    pkTable.GetTeachersSettledPositionsTag = function () {
        var par = {};
        var tagId = "";
        $('.classrooms li').each(function (i) {
            if ($(this).hasClass("t-li")) {
                tagId = $(this).attr('ids');
            }
        });
        par.tagId = tagId;
        Common.getData('/paike/GetTeachersSettledPositionsTag.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var str = "";
                if (obj.type == 1) {
                    str = "<p class='zb pic'></p>";
                } else if (obj.type == 2) {
                    str = "<p class='zbl pic'></p>";
                } else if (obj.type == 4) {
                    str = "<p class='zhuan pic'></p>";
                } else if (obj.type == 6) {
                    str = "<p class='dan pic'></p>";
                }
                str += "<div class='ls'><p class='whide' title=" + obj.JxbName + obj.count + "><em class='vxk'>" + obj.JxbName + "</em><em class='vrs'>(" + obj.count + ")</em></p><p>" + obj.subName + "</p><p><em class='vjs'>" + obj.roomName + "</em></p></div>";
                $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").html(str);
                $(".itt[x=" + (obj.x - 1) + "][y=" + (obj.y - 1) + "]").attr("ykbId", obj.ykbId);
            })
        })
    }


    pkTable.saveKeBiaoInfoByTea = function (x, y) {
        var data = {};
        data.xqid = xqid;
        data.x = x;
        data.y = y;
        data.jxbId = jxbId;
        Common.getData('/paike/saveKeBiaoInfoByTeacher.do', data, function (rep) {
            if (rep.code == '200') {
                layer.msg("放入成功！");
                $(".itt").html("")
                pkTable.ChuShiByTea();
                pkTable.getGuDingShiWu();
                pkTable.getGDSX();
                pkTable.getTeaShiWuByXqid();
                if ($("#teacher").is(":checked")) {
                    pkTable.getTeacherList(1);
                    pkTable.getkpjxbByTeaId($("#sub .active").attr("ids"));
                    pkTable.GetTeachersSettledPositions();
                } else {
                    pkTable.getTagList(1);
                    pkTable.getkpjxbByTag();
                    pkTable.GetTeachersSettledPositionsTag()
                }
                pkflg = false;
                colorFlag = false;
                jxbId = "";
            }
        });
    }
    pkTable.saveKeBiaoInfo = function (ykbId, jxbId) {
        var data = {};
        data.ykbId = ykbId;
        data.jxbId = jxbId;
        data.oykbId = "";
        Common.getData('/paike/saveKeBiaoInfo.do', data, function (rep) {
            if (rep.code == '200') {
                layer.msg("放入成功！");
                pkTable.getkpjxb();
                pkTable.getKeBiaoList(2);
                pkTable.getZouBanTimeList(1);
                pkTable.getList();
                pkflg = false;
                colorFlag = false;
                jxbId = "";
            }
        });
    }
    pkTable.getSubjectList = function () {
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

    pkTable.getList = function () {
        var gid = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gid = $(this).attr('ids');
            }
        })
        var xueqi = xqid;
        var gradeId = gid;
        Common.getData('/classset/getListByXqGrade.do', {"xqid": xueqi, "gradeId": gradeId}, function (rep) {
            $.each(rep, function (i, obj) {
                $(".wla[ids=" + obj.roomId + "]").text("（" + obj.arrangedTime + "/" + obj.classTime + "）");
            });
        });


    }
    pkTable.getFaBuLogs = function () {
        var par = {}
        var gid = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gid = $(this).attr('ids');
            }
        })
        par.termId = zhenXqid;
        par.gradeId = gid;
        par.ci = xqid;
        Common.getData('/paike/getFaBuLogs.do', par, function (rep) {
            if (rep.code=='200') {
                $('.bg').show();
                $('.fb-popup').show();
                $('.fblog').empty();
                Common.render({tmpl: $('#fblogTmpl'), data: rep.message, context: '.fblog'});
            }
        });
    }
    pkTable.getZouBanTimeList = function (type) {
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
    pkTable.initGrade = function () {
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

    module.exports = pkTable;
})