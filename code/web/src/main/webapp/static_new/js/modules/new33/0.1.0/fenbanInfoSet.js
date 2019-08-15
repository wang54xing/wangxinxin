/**
 * Created by albin on 2017/7/25.
 */

define('fenbanInfoSet', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var fenbanInfoSet = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    common = require('common');
    var paikeci = "";
    var gradeId = "";
    var subjectId = "";
    var subType = "";
    var jxbId = "";
    var pkxq = "";
    var stuArray = new Array();

    fenbanInfoSet.getDefaultTerm = function () {
        common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#defaultTerm").attr("ids", rep.message.xqid);
            paikeci = rep.message.paikeci;
            pkxq = rep.message.paikexq;
        });
    };

    fenbanInfoSet.getSubjectByType = function () {
        var par = {};
        //par.xqid = $("#defaultTerm").attr("ids");
        par.gradeId = $("#grade .cur").attr("val");
        par.subType = $("#subType").val();
        if (!par.gradeId || par.gradeId == "" || par.gradeId == null) {
            common.render({tmpl: $('#subject_temp'), data: [], context: '#subject', overwrite: 1});
            return;
        }
        common.getData('/n33_fenbaninfoset/getSubjectByType.do', par, function (rep) {
            common.render({tmpl: $('#subject_temp'), data: rep, context: '#subject', overwrite: 1});
            $("#subject em:eq(0)").addClass("cur");
        })
    };

    fenbanInfoSet.getStudentByName = function () {
        var par = {};
        par.studentName = $("#userName").val();
        par.gid = $("#grade .cur").attr("val");
        par.ciId = paikeci;
        if (par.studentName != "") {
            common.getData('/new33isolateMange/getStudentByStudentName.do', par, function (rep) {
                if (rep.message.classId != null) {
                    $("#sousuokk").attr("cid", rep.message.classId)
                    $("#sousuokk").attr("combiname", rep.message.combiname)
                    $("#sousuokk").attr("userId", rep.message.userId)
                    $("#userName").val(rep.message.userName)
                } else {
                    layer.msg("未查询到未分配教学班的学生。")
                }
            })
        } else {
            layer.msg("请输入学生姓名。")
        }
    };

    fenbanInfoSet.addStudentByXingZheng = function () {
        var par = {}
        par.gradeId = $("#grade .cur").attr("val");
        par.ciId = paikeci;
        par.cid = $("#sousuokk").attr("cid");
        par.userId = $("#sousuokk").attr("userId");
        if (par.cid != "" && par.cid != null && par.cid != undefined) {
            common.getData('/studentTag/addStudentByXingZheng.do', par, function (rep) {
                common.render({tmpl: $('#XingSub'), data: rep.message, context: '#xing', overwrite: 1});
            })
        }
    };

    fenbanInfoSet.tuiJianByType = function () {
        var par = {}
        par.gradeId = $("#grade .cur").attr("val");
        par.ciId = paikeci;
        par.cid = $("#sousuokk").attr("cid");
        par.combiname = $("#sousuokk").attr("combiname");
        if (par.cid != "" && par.cid != null && par.cid != undefined) {
            var flag = getCiIdIsFaBu();
            if (flag) {
                par.fabu = 0;
            } else {
                par.fabu = 1;
            }
            common.getData('/studentTag/getJxbByStudentSubject.do', par, function (rep) {
                if (rep.message != null) {
                    common.render({tmpl: $('#subS'), data: rep.message.subject, context: '#tab-zb', overwrite: 1});
                }
                $(".item").each(function (i, obj) {
                    var array = new Array();
                    $.each(rep.message.subjectJxbList, function (index, item) {
                        if ($(obj).attr("subid") == item[0].subId) {
                            $.each(item, function (tem, hehe) {
                                array.push(hehe)
                            })
                        }
                        if (item[0].DengHe == 1) {
                            $(".isy[subid=" + item[0].subId + "]").addClass("tCur")
                        }
                    })
                    common.render({
                        tmpl: $('#subJxb'),
                        data: array,
                        context: '.te' + $(obj).attr("subid"),
                        overwrite: 1
                    });
                })
            })
        }
    };

    fenbanInfoSet.addStudentZhuanXiangKe = function () {
        var par = {}
        par.gradeId = $("#grade .cur").attr("val");
        par.ciId = paikeci;
        par.cid = $("#sousuokk").attr("cid");
        if (par.cid != "" && par.cid != null && par.cid != undefined) {
            common.getData('/studentTag/addStudentZhuanXiangKe.do', par, function (rep) {
                if (rep.message != null) {
                    common.render({tmpl: $('#Zhuan'), data: rep.message.zhuan, context: '#tab-zxk', overwrite: 1});
                    $(".items").each(function (i, obj) {
                        var array = new Array();
                        $.each(rep.message.msg, function (index, item) {
                            if ($(obj).attr("zxId") == item[0].zxId) {
                                $.each(item, function (tem, hehe) {
                                    array.push(hehe)
                                })
                            }
                        })
                        common.render({
                            tmpl: $('#ZhuanList'),
                            data: array,
                            context: '.te' + $(obj).attr("zxId"),
                            overwrite: 1
                        });
                    })
                }
            })
        }
    };

    fenbanInfoSet.getClassListByJxbId = function () {
        var par = {};
        par.jxbId = jxbId;
        common.getData('/new33classManage/getClassListByJxbId.do', par, function (rep) {
            common.render({
                tmpl: $('#jxbClass'),
                data: rep.message,
                context: '#clsUl',
                overwrite: 1
            });
            $("#clsUl li:eq(0)").addClass("t-li")
        })
    };

    fenbanInfoSet.getClassStudentListByJxbId = function () {
        var par = {};
        par.jxbId = jxbId;
        par.classId = $("#clsUl .t-li").attr("ids");
        common.getData('/new33classManage/getClassStudentListByJxbId.do', par, function (rep) {
            common.render({
                tmpl: $('#STUClass'),
                data: rep.message,
                context: '#myList',
                overwrite: 1
            });
            $("#myList-nav").html("");
            $('#myList').listnav({
                includeOther: true,
                noMatchText: '',
                prefixes: ['the', 'a']
            });
        })
    };

    fenbanInfoSet.init = function () {
    	$('.c1a').hide();
    	
    	$('body').on('change', '#subType', function () {
    		var type = $('#subType').val();
    		if(type == 0){
    			$('.c1a').hide();
    		}else{
    			$('.c1a').show();
    		}
    	});

        $("#addjxb").click(function () {
            var par = {};
            par.jxbId = jxbId;
            par.stuIds = stuArray;
            if (stuArray.length > 0) {
                common.getPostBodyData('/new33classManage/addJXBStu.do', par, function (repp) {
                    fenbanInfoSet.getClassStudentListByJxbId();
                    fenbanInfoSet.getJxbInfo()
                    layer.msg("操作成功");
                    $('.ccxx-popup').hide();
                    var dto = {};
                    dto.id = jxbId;
                    dto.gid = $("#grade .cur").attr("val");
                    common.getData('/n33_fenbaninfoset/getjxbStudentByJxbId.do', dto, function (rep) {
                        common.render({tmpl: $('#room_xq'), data: rep.message, context: '#student', overwrite: 1});
                    })
                })
            } else {
                layer.msg("请选择学生!");
            }
        });

        $("body").on("click", "#myList .fuck", function () {
            if ($(this).find("i").hasClass("r-icon")) {
                $(this).find("i").removeClass("r-icon");
                var stuAr = new Array();
                var uid = $(this).attr("ids");
                $.each(stuArray, function (i, obj) {
                    if (obj != uid) {
                        stuAr.push(obj);
                    }
                })
                stuArray = stuAr;
            } else {
                $(this).find("i").addClass("r-icon");
                stuArray.push($(this).attr("ids"));
            }
        });

        $('.ccxx-popup .all-select').click(function () {//全选
            $.each($("#myList .fuck"),function (i,item) {
                var ids=$(item).attr("ids");
                if(stuArray.indexOf(ids)<0){
                    stuArray.push(ids);
                }
                $(item).find("i").addClass("r-icon");
            })
        });
        
        $('body').on('click', '.ttj', function () {
            stuArray = new Array();
            $('.ccxx-popup').show();
            fenbanInfoSet.getClassListByJxbId();
            fenbanInfoSet.getClassStudentListByJxbId();
        });

        $("body").on("click", "#clsUl li", function () {
            $("#clsUl li").removeClass("t-li")
            $(this).addClass("t-li")
            fenbanInfoSet.getClassStudentListByJxbId();
            $.each(stuArray, function (i, obj) {
                $("#myList .t5[ids=" + obj + "]").addClass("r-icon")
            })
        });

        $("#queDing").click(function () {
            var dto = {};
            dto.uid = $("#sousuokk").attr("userid");
            var jxbIds = new Array();
            $(".tii").each(function (i, obj) {
                jxbIds.push($(obj).attr("ids"));
            })
            dto.jxbIds = jxbIds;
            common.getPostBodyData('/studentTag/addStudentByJxb.do', dto, function (rep) {
                layer.msg("操作成功");
                $(".tj-popup,.bg").hide();
            })
        });

        $("body").on("click", "#tab-zb li", function () {
            if ($(this).find(".ti").attr("isfabu") == 0) {
                layer.msg("课表已发布，学生只能加入推荐教学班。")
            } else {
                $(this).find('.ti').addClass('tii').parent().siblings().find('.ti').removeClass('tii')
            }
        });

        $("body").on("click", "#tab-zxk .gUl li", function () {
            $(this).find('.ti').toggleClass('tii').parent().siblings().find('.ti').removeClass('tii');
        });

        $("#sousuokk").click(function () {
            fenbanInfoSet.getStudentByName();
            fenbanInfoSet.addStudentByXingZheng();
            fenbanInfoSet.tuiJianByType();
            fenbanInfoSet.addStudentZhuanXiangKe();
        });

        $('body').on('click', '#student .delStudentJ', function () {
            var par = {};
            par.studentId = $(this).attr("userid");
            par.jxbId = $(this).attr("jxbid");
            common.getData('/studentTag/delStudentByJxb.do', par, function (rep) {
                fenbanInfoSet.getJxbInfo();
                var dto = {};
                dto.id = par.jxbId;
                dto.gid = $("#grade .cur").attr("val");
                common.getData('/n33_fenbaninfoset/getjxbStudentByJxbId.do', dto, function (rep) {
                    common.render({tmpl: $('#room_xq'), data: rep.message, context: '#student', overwrite: 1});
                })
            })
        });

        $('body').on('click', '.cInfo', function () {
            $('.bg').show();
            $('.detail-popup').show();
            var par = {};
            par.id = $(this).attr("ids");
            jxbId = $(this).attr("ids");
            par.gid = $("#grade .cur").attr("val");
            var name = $(this).attr("nm");
            $("#className").html(name)
            common.getData('/n33_fenbaninfoset/getjxbStudentByJxbId.do', par, function (rep) {
                common.render({tmpl: $('#room_xq'), data: rep.message, context: '#student', overwrite: 1});
            });
        });

        fenbanInfoSet.getDefaultTerm();
        fenbanInfoSet.getgrade();
        fenbanInfoSet.getSubjectByType();
        fenbanInfoSet.getJxbInfo();
        initGrade();

        $("#subType").change(function () {
            fenbanInfoSet.getSubjectByType();
            fenbanInfoSet.getJxbInfo();
        });

        $("body").on("click", "#grade em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            fenbanInfoSet.getSubjectByType();
            fenbanInfoSet.getJxbInfo();
            fenbanInfoSet.getAcClassType();
        });

        $("body").on("click", "#subject em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            fenbanInfoSet.getJxbInfo();
        });

        $('.searchName').bind('input propertychange', function () {
            searchCity();
        });

        // 教师设置
        $("body").on("click", "#jxb .cla", function () {
            $("#set_teacher").attr("jxbid", $(this).attr("jxbid"));
            if($(this).attr("tid") != ""){
                var tid = $(this).attr("tid");
                var eid = $(this).attr("eid");
                layer.confirm('修改教师会撤销该教学班课表，确定修改？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    $("#teacher").val(tid);
                    $('.layui-layer').hide();
                    $('.layui-layer-shade').hide();
                    $('.bg').show();
                    $('.setp-popup').show();
                    fenbanInfoSet.getTeacherList(eid);
                },function () {
                });
            }else{
                fenbanInfoSet.getTeacherList($(this).attr("eid"));
                $("#teacher").val($(this).attr("tid"));
                $('.bg').show();
                $('.setp-popup').show();
            }
        });

        // 教室设置
        $("body").on("click", "#jxb .gra", function () {
            $("#set_room").attr("jxbid", $(this).attr("jxbid"));
            if($(this).attr("rid") != ""){
                var rid = $(this).attr("rid");
                layer.confirm('修改教室会撤销该教学班课表，确定修改？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    $('.layui-layer').hide();
                    $('.layui-layer-shade').hide();
                    fenbanInfoSet.getClassRoomList();
                    $("#room").val(rid);
                    $('.bg').show();
                    $('.selp-popup').show();
                },function () {
                });
            }else{
                fenbanInfoSet.getClassRoomList();
                $("#room").val($(this).attr("rid"));
                $('.bg').show();
                $('.selp-popup').show();
            }
        });

        // 昵称设置
        $("body").on("click", "#jxb .name", function () {
            $("#set_name").attr("jxbid", $(this).attr("jxbid"));
            $("#nickname").val($(this).attr("nick"));
            $('.bg').show();
            $('.name-popup').show();
        });

        $("#set_teacher").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            fenbanInfoSet.setJxbTeacher($("#set_teacher").attr("jxbid"));
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
            fenbanInfoSet.setJxbRoom($("#set_room").attr("jxbid"));
            $('.bg').hide();
            $('.setp-popup').hide();
            $('.selp-popup').hide();
        });

        $("#set_name").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            fenbanInfoSet.setNickName($("#set_name").attr("jxbid"));
            $('.bg').hide();
            $('.name-popup').hide();
        });

        $('.qxx,.close').click(function () {
            $('.bg').hide();
            $('.setp-popup').hide();
            $('.selp-popup').hide();
            $('.name-popup').hide();
            $('.tj-popup').hide();
        });

        $('.ccxx-popup .qxx,.ccxx-popup .close').click(function () {
            $(".ccxx-popup").hide();
            $('.bg').hide();
        });

        $("#export").click(function () {
            window.location.href = "/n33_fenbaninfoset/exportJXBinfo.do?gradeId=" + $("#grade em[class='cur']").attr("val");
        });

        $("#detect").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            fenbanInfoSet.detactConfliction();
        });
        
        //自动设置
        $("#autoset").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            var type = $('#subType').val();
            if(type == 3){
            	fenbanInfoSet.teaAutoSet();
            }else{
            	fenbanInfoSet.autoSetTeacherAndClassRoom();
            }
        });
        
        $("#clearSet").click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            clearSet();
        });

        //分班方案-弹窗
        $('#fbBtn').click(function () {
            var flag = getCiIdIsFaBu();
            if (flag) {
                layer.alert("课表已发布,不允许修改");
                return;
            }
            fenbanInfoSet.getZuoBanJxbInfo();
            $('.fbf-popup').show();
            $('.bg').show()
        });

        //教学班类型 切换
        $("#zbJxbType").change(function () {
            fenbanInfoSet.getZuoBanJxbInfo();
        });

        //分班方案-弹窗 - 关闭
        $('.fbf-close, .fbf-popup .save').click(function () {
            $("#zbJxbId").val("");
            $("#zbJxbSubId").val("");
            $("#zbJxbSerial").val("");
            $('.fbf-popup').hide();
            $('.bg').hide()
        });

        //分班方案-设置老师
        $(document).on('click','.fb_cs_teacher',function () {
            var teaId = $(this).attr("tid")||"";
            var jxbId = $(this).parent().parent().attr("jxbId")||"";
            var subId = $(this).parent().parent().attr("subId")||"";
            var serial = $(this).parent().parent().attr("serial")||"0";
            $("#zbJxbId").val(jxbId);
            $("#zbJxbSubId").val(subId);
            $("#zbJxbSerial").val(serial);
            if(teaId!= ""){
                layer.confirm('修改教师会撤销该教学班课表，确定修改？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    $('.layui-layer').hide();
                    $('.layui-layer-shade').hide();
                    $('.fbf-setp-popup').show();
                    fenbanInfoSet.getZbJxbSerialTeaList(subId, serial);
                    $("#zbJxbTeaList").val(teaId);
                },function () {
                });
            }else{
                fenbanInfoSet.getZbJxbSerialTeaList(subId, serial);
                $('.fbf-setp-popup').show();
            }
        });

        //分班方案-设置老师 - 保存
        $('.fbf-setp-popup .submit-btn').click(function () {
            fenbanInfoSet.setZbJxbSerialTea();
        });

        //分班方案-设置老师 - 关闭
        $('.setp-close').click(function () {
            $('.fbf-setp-popup').hide();
            $("#zbJxbId").val("");
            $("#zbJxbSubId").val("");
            $("#zbJxbSerial").val("");
        });

        //分班方案-选择教室
        $(document).on('click','.fb_cs_room',function () {
            var roomId = $(this).attr("rmid")||"";
            var jxbId = $(this).parent().parent().attr("jxbId")||"";
            var subId = $(this).parent().parent().attr("subId")||"";
            var serial = $(this).parent().parent().attr("serial")||"0";
            $("#zbJxbId").val(jxbId);
            $("#zbJxbSubId").val(subId);
            $("#zbJxbSerial").val(serial);
            if(roomId!= ""){
                layer.confirm('修改教室会撤销该教学班课表，确定修改？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    $('.layui-layer').hide();
                    $('.layui-layer-shade').hide();
                    $('.fbf-selp-popup').show();
                    fenbanInfoSet.getZbJxbSerialClsRmList(serial);
                    $("#zbJxbRoomList").val(roomId);
                },function () {
                });
            }else{
                fenbanInfoSet.getZbJxbSerialClsRmList(serial);
                $('.fbf-selp-popup').show();
            }
        });

        //分班方案-选择教室 - 关闭
        $('.selp-close').click(function () {
            $('.fbf-selp-popup').hide();
            $("#zbJxbId").val("");
            $("#zbJxbSubId").val("");
            $("#zbJxbSerial").val("");
        });

        //分班方案-选择教室 - 保存
        $('.fbf-selp-popup .submit-btn').click(function () {
            fenbanInfoSet.setZbJxbSerialClsRm();
        });


        //分班方案-查看学生
        $(document).on('click','.fb_cs_stu',function () {
            var jxbId = $(this).parent().parent().attr("jxbId")||"";
            var subId = $(this).parent().parent().attr("subId")||"";
            $("#zbJxbId").val(jxbId);
            $("#zbJxbSubId").val(subId);
            fenbanInfoSet.getJxbStuInfos();
            $('.fbf-detail-popup').show();
        });

        //分班方案-删除学生
        $('body').on('click', '#zbJxbStuList .delStudent', function () {
            var param = {};
            param.studentId = $(this).attr("userId");
            param.jxbId = $(this).attr("jxbId");
            common.getPostData('/studentTag/delStudentByJxb.do', param, function (rep) {
                if(rep.code=="200") {
                    var curSubId = $("#subject em[class='cur']").attr("val")||"";
                    var zbJxbSubId =$("#zbJxbSubId").val();
                    if(curSubId==zbJxbSubId){
                        fenbanInfoSet.getJxbInfo();
                    }
                    fenbanInfoSet.getJxbStuInfos();
                    fenbanInfoSet.getZuoBanJxbInfo();
                }
            });
        });

        //分班方案-查看学生 - 关闭
        $('.fbf-detail-close').click(function () {
            $('.fbf-detail-popup').hide();
            $("#zbJxbId").val("");
            $("#zbJxbSubId").val("");
        });

        //分班方案-查看学生-添加查询
        $('.fbf_detail_search').click(function () {
            stuArray = new Array();
            fenbanInfoSet.getZbJxbClassList();
            $('.fbf-ccxx-popup').show();
        });

        //选择班级
        $("body").on("click", "#zbJxbClassList li", function () {
            $(this).addClass("t-li").siblings().removeClass("t-li");
            fenbanInfoSet.getZbJxbClassStuList();
            $.each(stuArray, function (i, obj) {
                $("#zbJxbClsStus .t5[ids=" + obj + "]").addClass("r-icon");
            });
        });

        //全选 学生
        $('.fbf-ccxx-popup .all-select').click(function () {//全选
            $.each($("#zbJxbClsStus .fuck"),function (i,item) {
                var ids=$(item).attr("ids");
                if(stuArray.indexOf(ids)<0){
                    stuArray.push(ids);
                }
                $(item).find("i").addClass("r-icon");
            });
        });

        //选择学生
        $("body").on("click", "#zbJxbClsStus .fuck", function () {
            if ($(this).find("i").hasClass("r-icon")) {
                $(this).find("i").removeClass("r-icon");
                var stuAr = new Array();
                var uid = $(this).attr("ids");
                $.each(stuArray, function (i, obj) {
                    if (obj != uid) {
                        stuAr.push(obj);
                    }
                })
                stuArray = stuAr;
            } else {
                $(this).find("i").addClass("r-icon");
                stuArray.push($(this).attr("ids"));
            }
        });

        //分班方案-选择学生 - 关闭
        $('.fbf-ccxx-popup .fbf-ccxx-close').click(function () {
            $('.fbf-ccxx-popup').hide();
        });

        //分班方案-选择学生 - 保存
        $('.fbf-ccxx-popup .submit-btn').click(function () {
            fenbanInfoSet.jxbAddStu();
        });

        $(document).on('click','.fbf_add',function () {
            var serail = $(this).attr("serail");
            $("#zbJxbSerial").val(serail);
            fenbanInfoSet.getNoSerialZbJxbList();
            $('.fbf2-setp-popup').show();
        });

        $('.fbf2-setp-popup .submit-btn').click(function () {
            fenbanInfoSet.setZbJxbSerial();
        });

        $('.fbf2-setp-popup .setp2-close').click(function () {
            $("#zbJxbSerial").val("");
            $('.fbf2-setp-popup').hide();
        });
    };

    fenbanInfoSet.getAcClassType = function(){
        var data = {};
        data.gradeId = $("#grade em[class='cur']").attr("val");
        data.ciId = paikeci;
        common.getPostData('/n33_set/getAcClassType.do', data, function (rep) {
            if (rep.code == '200') {
                if(rep.message==1){
                    $("#fbBtn").show();
                }else{
                    $("#fbBtn").hide();
                }
            }
        });
    };

    fenbanInfoSet.getZuoBanJxbInfo = function(){
        var param = {};
        param.gradeId = $("#grade em[class='cur']").attr("val");
        param.jxbType = $("#zbJxbType").val();
        param.ciId = paikeci;
        common.getPostData('/n33_fenbaninfoset/getZuoBanJxbInfo.do', param, function (rep) {
            if(rep.code=="200"){
                var serailList = rep.message.serailList;
                var dataList = rep.message.dataList;
                var maxSerial = rep.message.maxSerial;
                $.each(dataList, function (i, data) {
                    data.maxSerial = maxSerial;
                });
                common.render({tmpl: $('#serailHead_temp1'), data: serailList, context: '#serailHead1', overwrite: 1});
                common.render({tmpl: $('#serailHead_temp2'), data: serailList, context: '#serailHead2', overwrite: 1});
                common.render({tmpl: $('#seraildata_temp'), data: dataList, context: '#serailDataList', overwrite: 1});
            }
        })
    };

    fenbanInfoSet.getJxbStuInfos = function(){
        var param = {};
        param.gid = $("#grade em[class='cur']").attr("val");
        param.id = $("#zbJxbId").val();
        common.getPostData('/n33_fenbaninfoset/getjxbStudentByJxbId.do', param, function (rep) {
            if(rep.code=="200") {
                common.render({
                    tmpl: $('#zbJxbStuList_temp'),
                    data: rep.message,
                    context: '#zbJxbStuList',
                    overwrite: 1
                });
            }
        });
    };

    fenbanInfoSet.getZbJxbClassList = function () {
        var param = {};
        param.jxbId = $("#zbJxbId").val();
        common.getPostData('/new33classManage/getZbJxbClassList.do', param, function (rep) {
            if(rep.code=="200") {
                common.render({
                    tmpl: $('#zbJxbClassList_temp'),
                    data: rep.message,
                    context: '#zbJxbClassList',
                    overwrite: 1
                });
                $("#zbJxbClassList li:eq(0)").addClass("t-li");
                fenbanInfoSet.getZbJxbClassStuList();
            }
        })
    };

    fenbanInfoSet.getZbJxbClassStuList = function(){
        var param = {};
        param.jxbId = $("#zbJxbId").val();
        param.classId = $("#zbJxbClassList .t-li").attr("ids")||"";
        if(param.classId==""){
            $('#zbJxbClsStus').html("");
            return false;
        }
        common.getData('/new33classManage/getZbJxbClassStuList.do', param, function (rep) {
            $('#zbJxbClsStus').html("");
            if(rep.code=="200") {
                common.render({
                    tmpl: $('#zbJxbClsStus_tmpl'),
                    data: rep.message,
                    context: '#zbJxbClsStus',
                    overwrite: 1
                });
                $("#zbJxbClsStus-nav").html("");
                $('#zbJxbClsStus').listnav({
                    includeOther: true,
                    noMatchText: '',
                    prefixes: ['the', 'a']
                });
            }
        })
    };

    fenbanInfoSet.jxbAddStu = function(){
        var param = {};
        param.jxbId = $("#zbJxbId").val();
        param.stuIds = stuArray;
        if (stuArray.length > 0) {
            common.getPostBodyData('/new33classManage/addJXBStu.do', param, function (rep) {
                if(rep.code=="200") {
                    var curSubId = $("#subject em[class='cur']").attr("val")||"";
                    var zbJxbSubId =$("#zbJxbSubId").val()
                    if(curSubId==zbJxbSubId){
                        fenbanInfoSet.getJxbInfo();
                    }
                    fenbanInfoSet.getJxbStuInfos();
                    fenbanInfoSet.getZuoBanJxbInfo();
                    layer.msg("操作成功");
                    $('.fbf-ccxx-popup').hide();
                }
            });
        } else {
            layer.msg("请选择学生!");
        }
    };

    fenbanInfoSet.getZbJxbSerialTeaList = function (subId, serial) {
        var param = {};
        param.gradeId = $("#grade em[class='cur']").attr("val")||"";
        param.subId = subId;
        param.ciId = paikeci;
        param.jxbType = $("#zbJxbType").val();
        param.serial = serial;
        param.currJxbId = $("#zbJxbId").val();
        common.getPostData('/n33_fenbaninfoset/getZbJxbSerialTeaList.do', param, function (rep) {
            if(rep.code=="200") {
                common.render({tmpl: $('#zbJxbTeas_tmpl'), data: rep.message, context: '#zbJxbTeaList', overwrite: 1});
            }
        })
    }

    fenbanInfoSet.setZbJxbSerialTea = function () {
        var param = {};
        param.jxbId = $("#zbJxbId").val();
        param.teacherId = $("#zbJxbTeaList").val();
        common.getPostData('/n33_fenbaninfoset/setJxbTeacher.do', param, function (rep) {
            if (rep.code == 200) {
                var curSubId = $("#subject em[class='cur']").attr("val")||"";
                var zbJxbSubId =$("#zbJxbSubId").val()
                if(curSubId==zbJxbSubId){
                    fenbanInfoSet.getJxbInfo();
                }
                fenbanInfoSet.getZuoBanJxbInfo();
                layer.msg("操作成功");
                $('.fbf-setp-popup').hide();
            }
        });
    };

    fenbanInfoSet.getZbJxbSerialClsRmList = function (serial) {
        var param = {};
        param.gradeId = $("#grade em[class='cur']").attr("val")||"";
        param.ciId = paikeci;
        param.jxbType = $("#zbJxbType").val();
        param.serial = serial;
        param.currJxbId = $("#zbJxbId").val();
        common.getPostData('/n33_fenbaninfoset/getZbJxbSerialClsRmList.do', param, function (rep) {
            if(rep.code=="200") {
                common.render({tmpl: $('#zbJxbRoom_temp'), data: rep.message, context: '#zbJxbRoomList', overwrite: 1});
            }
        })
    };

    fenbanInfoSet.setZbJxbSerialClsRm = function(){
        var param = {};
        param.jxbId = $("#zbJxbId").val();
        param.roomId = $("#zbJxbRoomList").val();
        common.getData('/n33_fenbaninfoset/setJxbRoom.do', param, function (rep) {
            if (rep.code == 200) {
                var curSubId = $("#subject em[class='cur']").attr("val")||"";
                var zbJxbSubId =$("#zbJxbSubId").val();
                if(curSubId==zbJxbSubId){
                    fenbanInfoSet.getJxbInfo();
                }
                fenbanInfoSet.getZuoBanJxbInfo();
                layer.msg("操作成功");
                $('.fbf-selp-popup').hide();
            }
        });
    };

    fenbanInfoSet.getNoSerialZbJxbList = function(){
        var param = {};
        param.gradeId = $("#grade em[class='cur']").attr("val");
        param.jxbType = $("#zbJxbType").val();
        param.ciId = paikeci;
        common.getData('/n33_fenbaninfoset/getNoSerialZbJxbList.do', param, function (rep) {
            if (rep.code == 200) {
                common.render({
                    tmpl: $('#noSerialJxbList_temp'),
                    data: rep.message,
                    context: '#noSerialJxbList',
                    overwrite: 1
                });
            }
        })
    };

    fenbanInfoSet.setZbJxbSerial = function(){
        var param = {};
        param.jxbId = $("#noSerialJxbList").val();
        param.serial = $("#zbJxbSerial").val();
        common.getData('/n33_fenbaninfoset/setZbJxbSerial.do', param, function (rep) {
            if (rep.code == 200) {
                var curSubId = $("#subject em[class='cur']").attr("val")||"";
                var zbJxbSubId =$("#noSerialJxbList option:selected").attr("subId");
                if(curSubId==zbJxbSubId){
                    fenbanInfoSet.getJxbInfo();
                }
                fenbanInfoSet.getZuoBanJxbInfo();
                layer.msg("操作成功");
                $('.fbf2-setp-popup').hide();
            }
        });
    };

    fenbanInfoSet.getgrade = function () {
        var par = {};
        par.xqid = paikeci;
        if (!par.xqid || par.xqid == "" || par.xqid == null) {
            common.render({tmpl: $('#grade_temp'), data: [], context: '#grade', overwrite: 1});
            return;
        }
        common.getData('/new33isolateMange/getGradList.do', par, function (rep) {
            common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            // $("#grade em:eq(0)").addClass("cur");
        })
    };

    fenbanInfoSet.getJxbInfo = function () {
        var gradeId = $("#grade em[class='cur']").attr("val");
        var subType = $("#subType").val();
        var subjectId = $("#subject em[class='cur']").attr("val");
        if (!gradeId || !subjectId || gradeId == "" || subjectId == "") {
            return;
        }
        common.getPostBodyData('/n33_fenbaninfoset/getJxbInfoExcptZX.do?gradeId=' + gradeId + "&subType=" + subType + "&subjectId=" + subjectId, {}, function (rep) {
            if(rep.list.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            common.render({tmpl: $('#jxb_temp'), data: rep.list, context: '#jxb', overwrite: 1});
        })
    };

    fenbanInfoSet.getTeacherList = function (subjectId) {
        var gradeId = $("#grade em[class='cur']").attr("val");
        common.getData('/n33_fenbaninfoset/getTeacherList.do', {
            "gradeId": gradeId,
            "subjectId": subjectId
        }, function (rep) {
            common.render({tmpl: $('#teacher_temp'), data: rep, context: '#teacher', overwrite: 1});
        })
    };

    fenbanInfoSet.getClassRoomList = function () {
        var gradeId = $("#grade em[class='cur']").attr("val");
        common.getData('/n33_fenbaninfoset/getClassRoomList.do', {"gradeId": gradeId}, function (rep) {
            common.render({tmpl: $('#room_temp'), data: rep, context: '#room', overwrite: 1});
        })
    };

    fenbanInfoSet.setJxbTeacher = function (jxbid) {
        var teacherId = $("#teacher").val();
        common.getData('/n33_fenbaninfoset/setJxbTeacher.do', {"jxbId": jxbid, "teacherId": teacherId}, function (rep) {
            if (rep.code == 200) {
                fenbanInfoSet.getJxbInfo();
            }
        })
    };

    fenbanInfoSet.setJxbRoom = function (jxbid) {
        var roomId = $("#room").val();
        common.getData('/n33_fenbaninfoset/setJxbRoom.do', {"jxbId": jxbid, "roomId": roomId}, function (rep) {
            if (rep.code == 200) {
                fenbanInfoSet.getJxbInfo();
            }
        })
    };

    fenbanInfoSet.setNickName = function (jxbid) {
        var nickname = $("#nickname").val();
        common.getData('/n33_fenbaninfoset/setNickName.do', {"jxbId": jxbid, "nickname": nickname}, function (rep) {
            if (rep.code == 200) {
                fenbanInfoSet.getJxbInfo();
            }
        })
    };

    fenbanInfoSet.detactConfliction = function () {
        var gradeId = $("#grade em[class='cur']").attr("val");
        var cid = paikeci;
        $(".jhh").show();
        setTimeout(function () {
            common.getData('/studentTag/conflictDetection.do', {"gradeId": gradeId, "cid": cid}, function (rep) {
                $(".jhh").hide();
                layer.msg("生成成功!");
            })
        }, 100)

    };

    fenbanInfoSet.autoSetTeacherAndClassRoom = function () {
        $('.bg').hide();
        $('.detail-popup').hide();
        $('.scla-popup').hide();
        // var subType = $("#subType").val();
        // if (subType != 1 && subType != 2) {
        //     layer.alert("仅支持等级或合格型");
        //     return;
        // }
        var data = {};
        data.indexes = [];
        $("#indexes input").each(function () {
            if ($(this).is(":checked")) {
                data.indexes.push(Number($(this).val()));
            }
        });
        data.subjectIds = [];
        $("#subject em").each(function () {
            data.subjectIds.push($(this).attr("val"));
        });
        var ciId = paikeci;
        var gid = $("#grade em[class='cur']").attr("val");
        common.getPostBodyData('/n33_fenbaninfoset/autoSetTeacherAndClassRoom.do?gid=' + gid + "&ciId=" + ciId, data, function (rep) {
            if (rep.message.status1 == 0 && rep.message.status2 == 0) {
                layer.msg("设置完成");
            }
            else if (rep.message.status1 == 1) {
                layer.alert(rep.message.reason1);
            }
            else if (rep.message.status2 == 1) {
                layer.alert(rep.message.reason2);
            }
            fenbanInfoSet.getJxbInfo();

        })
    };
    
    //行政班自动分教师
    fenbanInfoSet.teaAutoSet = function () {
        var param = {};
        param.xqid = $("#defaultTerm").attr("ids");
        //param.xqid = paikeci;
        param.gid = $("#grade em[class='cur']").attr("val");
        param.subid = $("#subject em[class='cur']").attr("val");
        param.name = '*';
        param.subType = $('#subType').val();
        common.getPostBodyData('/n33_fenbaninfoset/teaAutoSet.do', param, function (rep) {
            if (rep.code == 200) {
            	layer.msg("设置完成");
            	fenbanInfoSet.getJxbInfo();
            }
        });
    };
    
    // 清空设置
    function clearSet() {
        var subType = $("#subType").val();
        if (subType != 1 && subType != 2) {
            layer.alert("仅支持等级或合格型");
            return;
        }
        var data = {};
        data.jxbIds = [];
        $("#jxb tr").each(function () {
            data.jxbIds.push($(this).attr("ids"));
        });
        layer.confirm('确认清空？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            common.getPostBodyData('/n33_fenbaninfoset/clearSet.do', data, function (rep) {
                if (rep.code == 200) {
                    layer.msg("清空完成");
                }
                fenbanInfoSet.getJxbInfo();

            })
        }, function () {
        });

    }

    function initGrade() {
        try {
            common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                        if (obj.key == "subType") {
                            subType = obj.value;
                        }
                        if (obj.key == "subjectId") {
                            subjectId = obj.value;
                        }


                    });
                    $("#grade em").each(function () {
                        if (gradeId == $(this).attr("val")) {
                            $(this).click();
                        }
                    });
                    $("#subType").val(subType).change();
                    $("#subject em").each(function () {
                        if (subjectId == $(this).attr("val")) {
                            $(this).click();
                        }
                    });
                    if ($("#subType").val() == null) {
                        $("#subType").val(0);
                        $("#subType").change();
                    }

                } else {

                }
            });
        } catch (x) {

        }
    }

    function getCiIdIsFaBu() {
        var flag = null;
        common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId": paikeci}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }

    module.exports = fenbanInfoSet;
});