/**
 * Created by albin on 2018/5/14.
 */
define('zhuanXiang', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var zhuanXiang = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    Common = require('common');
    var xqid = ""
    var zxqid = ""
    var clsList = new Array();
    var clsNameList = "";
    var id = "";
    var StuList = new Array();
    var Cids = new Array();

    var isFaBu = true;
    var zid = "";
    zhuanXiang.getZhuanXiangList = function () {
        var par = {};
        par.xqid = xqid;
        par.gradeId = $("#grade").val();
        par.subId = $("#subject").val();
        if (par.xqid != "" && par.xqid != null && par.xqid != undefined && par.gradeId != "" && par.gradeId != undefined && par.gradeId != null && par.subId != "" && par.subId != null && par.subId != undefined) {
            Common.getData('/n33_setJXB/getZhuanXiangList.do', par, function (rep) {
                if(rep.message.length==0){
                    $("#none_png").show();
                    $("#content").hide();
                }
                else{
                    $("#none_png").hide();
                    $("#content").show();
                }
                Common.render({tmpl: $('#tb_temp'), data: rep.message, context: '#tb', overwrite: 1});
            })
        }

    }
    zhuanXiang.getZhuanXiangZuList = function () {
        var par = {};
        par.xqid = xqid;
        par.jxbId = id;
        Common.getData('/n33_setJXB/getZhuanXiangZuList.do', par, function (rep) {
            Common.render({tmpl: $('#zh_temp'), data: rep.message, context: '#zh', overwrite: 1});
        })
    }
    zhuanXiang.getStudentList = function () {
        var par = {};
        par.xqid = xqid;
        par.classId = $("#cl .ss-cur").attr("ids");
        par.sex = $('#selSex').val();
        par.combine = $('#selCom').val();
        par.level = $('#selLevel').val();
        Common.getData('/IsolateStudent/getStudentDtoByClassIdForZhuanXiang.do', par, function (rep) {
            Common.render({tmpl: $('#studentList'), data: rep.message, context: '#stuList', overwrite: 1});

        })
        if (isFaBu != false) {
            $("#quedingren").prop("disabled", "disabled")
            $("#quedingren").addClass("quedingren")
            $(".xuanrendea,#stuCk").prop("disabled", "disabled")
        }
    }

    zhuanXiang.getZhuan = function (id) {
        var par = {};
        par.id = id;
        Common.getData('/n33_setJXB/getZhuan.do', par, function (rep) {
            StuList = rep.message.ids;
            Cids = rep.message.cids;
            //Common.render({tmpl: $('#studentList'), data: rep.message, context: '#stuList', overwrite: 1});
        })
    }
    zhuanXiang.xuan = function () {
        $.each(StuList, function (i, obj) {
            $(".xuanrendea[ids=" + obj + "]").prop("checked", true)
        })
        $("#cl li").hide()
        $("#cl li").removeClass("ss-cur")
        $.each(Cids, function (i, obj) {
            if (i == 0) {
                $("#cl li[ids=" + obj + "]").addClass("ss-cur")
            }
            $("#cl li[ids=" + obj + "]").show()
        })
    }
    zhuanXiang.xuan1 = function () {
        $.each(StuList, function (i, obj) {
            $(".xuanrendea[ids=" + obj + "]").prop("checked", true)
        })
    }

    zhuanXiang.getStuSelectResultByClassForZhuanXiang = function () {
        var gradeId = $("#grade").val();
        var type = 3;
        if (!gradeId || gradeId == "" || !type || type == "") {
            return;
        }
        Common.getData('/new33school/set/getStuSelectResultByClassForZhuanXiang.do', {
            "xqid": xqid,
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

    zhuanXiang.getIsFaBu = function () {
        var par = {};
        par.ciId = xqid;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', par, function (rep) {
            isFaBu = rep.message;
        });
    }

    function getStatusPK() {
        $.ajax({
            type: "GET",
            url: '/n33_setJXB/getStatusPK.do',
            async: false,
            cache: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (rep) {
                if (rep.st == -1) {
                    $('.bg').hide();
                    $(".jhh").hide();
                    window.clearInterval(c);
                    zhuanXiang.getZhuanXiangZuList();
                    layer.msg(rep.result);
                } else {
                    $(".jhhSp").text("正在导入组合，请稍候...");
                }
            }
        });
    }

    zhuanXiang.init = function () {
        $('body').on('click', '.bjks', function () {
            if (isFaBu == false) {
                var ks = $(this).attr("ks")
                var par = {};
                par.id = $(this).attr("ids");
                layer.prompt({
                    formType: 0,
                    value: ks,
                    title: '请输入新的课时！'
                }, function (value, index, elem) {
                    par.ks = value;
                    layer.close(index);
                    Common.getData('/n33_setJXB/updateZhuanKS.do', par, function (rep) {
                        zhuanXiang.getZhuanXiangList();
                    })
                });
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })

        $('body').on("change",'#selSex',function () {
            zhuanXiang.getStudentList();
            zhuanXiang.xuan();
        });
        $('body').on("change",'#selCom',function () {
            zhuanXiang.getStudentList();
            zhuanXiang.xuan();
        });
        $('body').on("change",'#selLevel',function () {
            zhuanXiang.getStudentList();
            zhuanXiang.xuan();
        });

        $('.close,.qxx').click(function () {
            $('.bg').hide();
            $('.addn-popup').hide();
            $('.add-popup').hide();
            $(".imstu-popup1").hide();
            $(".drsb-popup1").hide();
            $(".imselect-popup1").hide();
        })
        $("body").on("change", "#file", function () {
            var _this = $(this);
            var nameArr = _this.val().split("\\");
            var filename = nameArr[nameArr.length - 1];
            $(".file-name").html(filename);
        });
        $("body").on("click", "#qddr", function () {
            var par = {};
            par.xqid = xqid;
            par.zuHeId = id;
            par.gradeId = $("#grade").val();
            if (!par.xqid || par.xqid == "" || par.xqid == null) {
                layer.msg("请选择一个学期");
                return;
            }
            if ($("#file").val() != "") {
                var value = $("#file").val();
                var result = /\.[^\.]+/.exec(value);
                if (result == ".xls" || result == ".xlsx") {
                    $(".jhh").show();
                    $('.bg').show();
                    $(".imstu-popup1").hide();
                    $(".imselect-popup1").hide();
                    $(".jhhSp").text("正在导入组合，请稍候...");
                    $.ajaxFileUpload({
                        url: '/n33_setJXB/addUserImpYu.do',
                        param: par,
                        secureuri: false,
                        fileElementId: 'file',
                        dataType: 'json',
                        async: true,
                        success: function (data) {
                            if (data.code == 500) {
                                $(".jhh").hide();
                                $('.bg').hide();
                                layer.msg("导入组合发生异常");
                            }
                        },
                        error: function (e) {
                            $(".jhh").hide();
                            $('.bg').hide();
                            var file = $("#file")
                            file.after(file.clone().val(""));
                            file.remove();
                            $(".file-name").html("");
                            $(".jhh").hide();
                            $('.bg').hide();
                            layer.msg("网络错误，请重试！！");
                        }
                    })
                    c = setInterval(getStatusPK, 2000);
                } else {
                    $(".jhh").hide();
                    $('.bg').hide();
                    var file = $("#file")
                    file.after(file.clone().val(""));
                    file.remove();
                    $(".file-name").html("");
                    layer.msg("请使用excel格式导入");
                }
            } else {
                $(".jhh").hide();
                $('.bg').hide();
                var file = $("#file")
                file.after(file.clone().val(""));
                file.remove();
                $(".file-name").html("");
                layer.msg("请选择文件");
            }
        })
        $("body").on("click", ".download", function () {
            window.location.href = "/n33_setJXB/exportTemplate.do?gradeId=" + $("#grade").val() + "&id=" + id;
        })
        $('body').on('click', '.bj', function () {
            if (isFaBu == false) {
                var name = $(this).attr("nm")
                var par = {};
                par.id = $(this).attr("ids");
                layer.prompt({
                    formType: 0,
                    value: name,
                    title: '请输入专项名！'
                }, function (value, index, elem) {
                    par.name = value;
                    layer.close(index);
                    Common.getData('/n33_setJXB/updateZhuanName.do', par, function (rep) {
                        zhuanXiang.getZhuanXiangList();
                    })
                });
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })

        $("#stuCk").click(function () {
            if ($(this).is(":checked")) {
                $(".xuanrendea").prop("checked", "checked")
                $(".xuanrendea").each(function (tt, ff) {
                    var a = 0;
                    $.each(StuList, function (i, obj) {
                        if ($(ff).attr("ids") == obj) {
                            a = 1;
                        }
                    })
                    if (a == 0) {
                        StuList.push($(ff).attr("ids"))
                    }
                })
            } else {
                $(".xuanrendea").prop("checked", "")
                var stu = new Array();
                $.each(StuList, function (i, obj) {
                    var a = 0;
                    $(".xuanrendea").each(function (tt, ff) {
                        if (obj == $(ff).attr("ids")) {
                            a = 1;
                        }
                    })
                    if (a == 0) {
                        stu.push(obj);
                    }
                })
                StuList = stu;
            }
        })

        $("#quan").click(function () {
            if ($(this).is(":checked")) {
                $(".rRight").addClass("dRight")
                $(".rRight").removeClass("rRight")
            } else {
                $(".dRight").addClass("rRight")
                $(".dRight").removeClass("dRight")
            }
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
        $(".chatt-bt").click(function () {
            if (isFaBu == false) {
                $(".jhh").show();
                $(".bg").show();
                $(".jhhSp").text("生成冲突中，请稍候...");
                var par = {};
                par.cid = xqid;
                par.gradeId = $("#grade").val();
                setTimeout(function () {
                    Common.getData('/studentTag/conflictDetection.do', par, function (rep) {
                        layer.msg(rep.message);
                        $(".jhh").hide();
                        $(".bg").hide();
                    })
                }, 500)
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })

        $('.chat-bt').click(function () {
            if (isFaBu == false) {
                $('.bg').show();
                $('.man-popup').show();
                clsList = new Array();
                clsNameList = ""
                $("#clasNameList").val("");
                $("#zhName").val("")
                $(".dRight").addClass("rRight")
                $(".dRight").removeClass("dRight")
                $("#sunName").html($("#subject option:selected").html())
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("body").on("click", ".delR", function () {
            if (isFaBu == false) {
                var par = {};
                par.id = $(this).attr("ids");
                layer.confirm('确认要删除么？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    layer.msg("删除中...");
                    Common.getData('/n33_setJXB/delZhanXu.do', par, function (rep) {
                        zhuanXiang.getZhuanXiangZuList();
                    })
                }, function () {
                });
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("body").on("click", ".delZu", function () {
            if (isFaBu == false) {
                var par = {};
                par.id = $(this).attr("ids");
                layer.confirm('确认要删除么？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    layer.msg("删除中...");
                    Common.getData('/n33_setJXB/delZu.do', par, function (rep) {
                        zhuanXiang.getZhuanXiangList();
                    })
                }, function () {
                });
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })

        $("#quedingren").click(function () {
            var map = {};
            map.id = $("#bnm").attr("ids");
            map.stu = StuList;
            Common.getPostBodyData('/n33_setJXB/upZhuan.do', map, function (rep) {
                layer.msg("操作成功!");
                $(".txz-popup,.bg").hide();
                zhuanXiang.getZhuanXiangZuList();
                zhuanXiang.getZhuanXiangList();
                zhuanXiang.getTeacherList();
            });
        })

        $("body").on("click", ".xuanrendea", function () {
            var ids = $(this).attr("ids");
            if ($(this).is(":checked")) {
                StuList.push(ids);
            } else {
                var stu = new Array();
                $.each(StuList, function (i, obj) {
                    if (obj != ids) {
                        stu.push(obj);
                    }
                })
                StuList = stu;
            }
        })

        $("body").on("click", "#cl li", function () {
            $("#cl li").removeClass("ss-cur");
            $(this).addClass("ss-cur");
            zhuanXiang.getStudentList();
            //zhuanXiang.getZhuan(zid)
            zhuanXiang.xuan1();
            var len = $('#stuList input:checked').length;
            if ($('#stuList em>input').length == len) {
                $('#stuCk').prop('checked', true)
            } else {
                $('#stuCk').prop('checked', false)
            }
        })
        $('.zx-bt').click(function () {
            if (isFaBu == false) {
                $('.bg').show();
                $('.mnew-popup').show();
                $("#jbName").val("")
                $("#addZhuan").attr("ids", "*");
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("body").on("click", ".gl", function () {
            $('.bg').show();
            $('.txz-popup').show();
            $("#bnm").html($(this).attr("nm"))
            $("#bnm").attr("ids", $(this).attr("ids"))
            zid = $(this).attr("ids");
            zhuanXiang.getStuSelectResultByClassForZhuanXiang();
            zhuanXiang.getZhuan($(this).attr("ids"));
            zhuanXiang.xuan();
            zhuanXiang.getStudentList();
            zhuanXiang.xuan();
            if (isFaBu != false) {
                $("#quedingren").prop("disabled", "disabled")
                $("#quedingren").addClass("quedingren")
                $(".xuanrendea,#stuCk").prop("disabled", "disabled")
            }
        })

        $("body").on("click", ".edt", function () {
            if (isFaBu == false) {
                $(".mnew-popup,.bg").show();
                $("#jbName").val($(this).attr("nm"))
                $("#room option[value=" + $(this).attr("room") + "]").prop("selected", true)
                $("#teacher option[value=" + $(this).attr("tea") + "]").prop("selected", true)
                $("#addZhuan").attr("ids", $(this).attr("ids"));
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("#addZhuan").click(function () {
            var par = {};
            par.xqid = xqid;
            par.jxbId = id;
            par.name = $("#jbName").val();
            par.roomId = $("#room").val();
            par.teacherId = $("#teacher").val();
            if ($(this).attr("ids") == "*") {
                Common.getData('/n33_setJXB/addZhuanXiangZu.do', par, function (rep) {
                    zhuanXiang.getZhuanXiangZuList();
                })
            } else {
                par.id = $(this).attr("ids");
                Common.getData('/n33_setJXB/upZhuanXiangZu.do', par, function (rep) {
                    zhuanXiang.getZhuanXiangZuList();
                })
            }
            $(".mnew-popup,.bg").hide();
        })
        $("body").on("click", ".manage", function () {
            id = $(this).attr("ids");
            $('.ma1').hide();
            $('.ma2').show();
            $("#zhm").html($(this).attr("nm"))
            zhuanXiang.getZhuanXiangZuList();
            zhuanXiang.getTeacherList1($(this).attr("subId"));
        })
        $("#subject").change(function () {
            zhuanXiang.getZhuanXiangList();
            zhuanXiang.getTeacherList();
        })
        $("#grade").change(function () {
            zhuanXiang.getGradeSubjectList();
            zhuanXiang.getClassList();
            zhuanXiang.getClassRoomList();
            zhuanXiang.getZhuanXiangList();
            zhuanXiang.getTeacherList();
        })
        zhuanXiang.getDefaultTerm()
        zhuanXiang.getGradeList()
        initGrade();
        zhuanXiang.getGradeSubjectList();
        zhuanXiang.getClassList();
        zhuanXiang.getClassRoomList();
        zhuanXiang.getZhuanXiangList();
        zhuanXiang.getTeacherList();

        $("#addJxb").click(function () {
            var map = {};
            map.xqid = xqid;
            map.gradeId = $("#grade").val();
            map.subjectId = $("#subject1").val();
            map.cls = clsList;
            map.name = $("#zhName").val();
            Common.getPostBodyData('/n33_setJXB/addZhuanXiang.do', map, function (rep) {
                layer.msg("操作成功!");
                $(".man-popup,.bg").hide();
                zhuanXiang.getZhuanXiangList();
            });
        })

        $(".xzz-popup .qr").click(function () {
            clsList = new Array();
            clsNameList = "";
            $(".xzz-popup").hide();
            $(".man-popup,.bg").show();
            $(".dRight").each(function (i, obj) {
                clsList.push($(obj).attr("ids"));
                if (i == 0) {
                    clsNameList += $(obj).attr("nm");
                } else {
                    clsNameList += "," + $(obj).attr("nm");
                }
            })
            $("#clasNameList").val(clsNameList);
        })
        $(".xzz-popup .qx,.xzz-popup .close").click(function () {
            $(".xzz-popup").hide();
            $(".man-popup,.bg").show();
        })

        $("body").on("click", ".dRight", function () {
            $(this).removeClass("dRight")
            $(this).addClass("rRight")
        })
        $('.xz').click(function () {
            $(".fks").addClass("rRight")
            $(".fks").removeClass("dRight")
            $("#quan").prop("checked", false);
            $('.xzz-popup').show();
            $.each(clsList, function (i, obj) {
                $(".fks[ids=" + obj + "]").removeClass("rRight")
                $(".fks[ids=" + obj + "]").addClass("dRight")
            })
        })
        $("body").on("click", ".rRight", function () {
            $(this).removeClass("rRight")
            $(this).addClass("dRight")
        })
        zhuanXiang.getIsFaBu();
    }
    zhuanXiang.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            xqid = rep.message.paikeci;
            zxqid = rep.message.paikexq;
        });
    }
    zhuanXiang.getGradeList = function () {
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
        })
    }
    zhuanXiang.getGradeSubjectList = function () {
        var par = {};
        par.xqid = xqid;
        par.gradeId = $("#grade").val();
        $(".subject").empty();
        if (par.gradeId != undefined) {
            Common.getData('/new33isolateMange/getSubjectByGradeId.do', par, function (rep) {
                $("#subject").append("<option value='*'>全部</option>")
                Common.render({tmpl: $('#subject_temp'), data: rep.message, context: '.subject'});
            })
        }
    }
    zhuanXiang.getClassRoomList = function () {
        var gradeId = $("#grade").val();
        Common.getData('/n33_fenbaninfoset/getClassRoomList.do', {"gradeId": gradeId}, function (rep) {
            Common.render({tmpl: $('#room_temp'), data: rep, context: '#room', overwrite: 1});
        })
    }
    zhuanXiang.getTeacherList = function () {
        var gradeId = $("#grade").val();
        var subjectId = $("#subject").val();
        if (subjectId != "*") {
            Common.getData('/n33_fenbaninfoset/getTeacherList.do', {
                "gradeId": gradeId,
                "subjectId": subjectId
            }, function (rep) {
                Common.render({tmpl: $('#teacher_temp'), data: rep, context: '#teacher', overwrite: 1});
            })
        }
    }
    zhuanXiang.getTeacherList1 = function (sub) {
        var gradeId = $("#grade").val();
        var subjectId = sub;
        if (subjectId != "*") {
            Common.getData('/n33_fenbaninfoset/getTeacherList.do', {
                "gradeId": gradeId,
                "subjectId": subjectId
            }, function (rep) {
                Common.render({tmpl: $('#teacher_temp'), data: rep, context: '#teacher', overwrite: 1});
            })
        }
    }
    zhuanXiang.getClassList = function () {
        var par = {};
        par.xqid = xqid;
        par.gradeId = $("#grade").val();
        if (par.gradeId != undefined) {
            Common.getData('/new33classManage/getClassList.do', par, function (rep) {
                Common.render({tmpl: $('#clList'), data: rep.message, context: '#csList', overwrite: 1});
                //    class="ss-cur"
                Common.render({tmpl: $('#cs_temp'), data: rep.message, context: '#cl', overwrite: 1});
                $("#cl li:eq(0)").addClass("ss-cur");
            })
        }
    }

    function initGrade() {
        try {
            Common.userData("n33", function (res) {
                var gradeId = "";
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                    });
                    if(gradeId != ""){
                        $("#grade").val(gradeId).change();
                    }
                } else {

                }
            });
        } catch (x) {

        }
    }

    module.exports = zhuanXiang;
})