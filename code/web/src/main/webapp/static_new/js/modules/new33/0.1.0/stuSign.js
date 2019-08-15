/**
 * Created by albin on 2018/3/29.
 */
define('stuSign', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var stuSign = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var tagId = "";
    var isFaBu = true;
    stuSign.init = function () {

        $("body").on("click", ".addStuIsTag", function () {
            var par = {};
            par.tagId = tagId;
            par.xqid = deXqid;
            var stuList = new Array();
            $(".addSt:checked").each(function (i, obj) {
                stuList.push($(obj).attr("ids"));
            })
            par.stuList = stuList;
            Common.getPostBodyData('/studentTag/addStuJinTag.do', par, function (rep) {
                $('.bg').show();
                $('.wdd-popup').hide();
                $('.wd-popup').show();
                layer.msg("操作成功!");
                var map = {}
                map.id = tagId;
                Common.getData('/studentTag/getStuSign.do', map, function (res) {
                    Common.render({tmpl: $('#st_temp'), data: res.message, context: '#stList', overwrite: 1});
                })
                stuSign.getClassStudent();
            });
        })

        $('body').on('click', '.xzBtn', function () {
            if (isFaBu == false) {
                $('.bg').show();
                $('.wdd-popup').show();
                $('.wd-popup').hide();
                var par = {};
                par.gradeId = $("#grade").val();
                par.xqid = deXqid;
                par.subName = $("#subList option:selected").attr("subname");
                Common.getData('/studentTag/getNotTagStudentBySubject.do', par, function (rep) {
                    Common.render({tmpl: $('#addStuTag'), data: rep.message, context: '#adTagStu', overwrite: 1});
                })
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("#zdbq").click(function () {
            if (isFaBu == false) {
                var par = {};
                par.gradeId = $("#grade").val();
                par.xqid = deXqid;
                $(".jhh").show();
                $(".bg").show();
                $(".jhhSp").text("自动创建标签中，请稍候...");
                setTimeout(function () {
                    Common.getPostData('/studentTag/AutoTagList.do', par, function (rep) {
                        if (rep.message == "ok") {
                            layer.msg("创建成功");
                            stuSign.getClassStudent();
                        } else {
                            layer.msg(rep.message)
                        }
                    })
                    $(".jhh").hide();
                    $(".bg").hide();
                }, 500);
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        });

        $("#qkbq").click(function () {
            if (isFaBu == false) {
                var par = {};
                par.gradeId = $("#grade").val();
                par.xqid = deXqid;
                layer.confirm('确认清除标签？', function () {
                    Common.getData('/studentTag/cleanUpTagInfos.do', par, function (rep) {
                        if (rep.code == "200") {
                            layer.msg("清除标签完成！！！");
                            stuSign.getClassStudent();
                        } else {
                            layer.msg(rep.message)
                        }
                    });
                });
            } else {
                layer.msg("当前次已经发布,不允许清除标签!")
            }
        });

        $("body").on("click", ".stuList", function () {
            if (isFaBu == false) {
                var par = {};
                par.tagId = $(this).attr("tgid");
                par.stuId = $(this).attr("ids");
                layer.confirm('确认释放该学生？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    layer.msg("删除中...");
                    Common.getData('/studentTag/delStudentByTagId.do', par, function (rep) {
                        var map = {}
                        map.id = par.tagId;
                        Common.getData('/studentTag/getStuSign.do', map, function (res) {
                            if(res.message.length>0) {
                                Common.render({
                                    tmpl: $('#st_temp'),
                                    data: res.message,
                                    context: '#stList',
                                    overwrite: 1
                                });
                            }else{
                                $(".wd-popup,.bg").hide();
                            }
                        })
                        stuSign.getClassStudent();
                    })
                }, function () {
                    $(".stuList[ids=" + par.stuId + "]").prop("checked", "checked")
                });
            } else {
                $(this).prop("checked", "checked")
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("body").on("click", ".auto-bt", function () {
            if (isFaBu == false) {
                $('.bg').show();
                $('.addb-popup').show()
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
        })
        $("body").on("click", ".auto-btt", function () {
            $('.bg').hide();
            $('.addb-popup').hide()
        })
        stuSign.getDefaultTerm();
        stuSign.getIsFaBu();

        $("body").on("click", ".rrow", function () {
            if (isFaBu == false) {
                var par = {};
                par.id = $(this).attr("ids");
                layer.confirm('确认删除？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    Common.getData('/studentTag/removeTag.do', par, function (rep) {
                        layer.msg(rep.message);
                        stuSign.getClassStudent();
                    });
                }, function () {
                });
            } else {
                layer.msg("当前次已经发布,不允许修改数据!")
            }
            e.stopPropagation();
        });

        $('body').on('click', '.sing-td', function () {
            if ($(this).html() > 0) {
                $(this).toggleClass('td-cur');
                if ($('.tab-jsks tbody tr td').hasClass('td-cur')) {
                    $('.bt-hh').removeClass('auto-btt').addClass('auto-bt')
                } else {
                    $('.bt-hh').removeClass('auto-bt').addClass('auto-btt')
                }
            }
        })
        $('body').on('click', '.wd', function () {
            tagId = $(this).attr("ids");
            $('.bg').show();
            $('.wd-popup').show();
            $("#tagName").html($(this).attr("name"))
            var par = {};
            par.id = $(this).attr("ids");
            Common.getData('/studentTag/getStuSign.do', par, function (rep) {
                Common.render({tmpl: $('#st_temp'), data: rep.message, context: '#stList', overwrite: 1});
            })
        })
        $(".ss").click(function () {
            stuSign.addStudentTag();
        })
        stuSign.getGrade();
        stuSign.getPartMaxFenDuan();
        stuSign.getStuSelectResultByClass();
        stuSign.getClassStudent();
        $("#grade").change(function () {
            stuSign.getPartMaxFenDuan();
            stuSign.getStuSelectResultByClass();
            stuSign.getClassStudent();
        })
        $("#ZuHeType").change(function () {
            stuSign.getStuSelectResultByClass();
            stuSign.getClassStudent();
        })
        $("#subList").change(function () {
            stuSign.getClassStudent();
        })
        $("#ceng").change(function () {
            stuSign.getClassStudent();
        })
        $("#duanCiSel").change(function () {
            stuSign.getClassStudent();
        })
        $(".vtl13").click(function () {
            stuSign.getClassStudent();
        })
    }
    stuSign.getIsFaBu = function () {
        var par = {};
        par.ciId = deXqid;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', par, function (rep) {
            isFaBu = rep.message;
        });
    }
    stuSign.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            deXqid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);

        });
    }

    stuSign.getPartMaxFenDuan = function(){
        var par = {};
        par.gradeId = $("#grade").val();
        par.ciId = deXqid;
        Common.getData('/autoPk/partClassSet/getUsePartMaxFenDuan.do', par, function (rep) {
            if(rep.code=='200'){
                var maxFenDuan = rep.message;
                var html = "<option value='-1'>全部</option>";
                for(var fenDuan = 1; fenDuan<=maxFenDuan;fenDuan++){
                    html+='<option value="'+fenDuan+'">段次'+fenDuan+'</option>';
                }
                $("#duanCiSel").html(html);
            }
        });
    };

    stuSign.addStudentTag = function () {
        var dto = {};
        dto.name = $(".inp-v").val();
        if (dto.name.length > 20) {
            $(".inp-v").val("");
            layer.msg("标签名称不能超过20个字符");
            return;
        }
        dto.gradeId = $("#grade").val();
        dto.xqid = deXqid;
        dto.view = 0;
        var studentInfos = new Array();
        $(".td-cur").each(function () {
            var cid = $(this).attr("cid");
            var cnm = $(this).attr("cnm");
            var tr = $(this).parent();
            $(tr).find(".userLit span[bj=0]").each(function (i, te) {
                var dto = {};
                dto.classId = cid;
                dto.className = cnm;
                dto.stuid = $(te).attr("ids");
                studentInfos.push(dto);
            })
        })
        dto.studentInfos = studentInfos;
        Common.getPostBodyData('/studentTag/addStudentTag.do', dto, function (rep) {
            stuSign.getClassStudent();
            $(".inp-v").val("");
            $(".addb-popup,.bg").hide();
        });
    }

    stuSign.getGrade = function () {
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade'});
        })
    }

    stuSign.getStuSelectResultByClass = function () {
        var gradeId = $("#grade").val();
        var type = $("#ZuHeType").val();
        ;
        if (!gradeId || gradeId == "" || !type || type == "") {
            return;
        }
        Common.getData('/new33school/set/getStuSelectResultByClass.do', {
            "xqid": deXqid,
            "gradeId": gradeId,
            "type": type
        }, function (rep) {
            if(rep.content.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            Common.render({tmpl: $('#content2_temp'), data: rep.content, context: '#subList', overwrite: 1});
        })
    }

    stuSign.getClassStudent = function () {
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $("#grade").val();
        par.sbt = $("#ZuHeType").val();
        par.sbn = $("#subList").val();
        par.lev = $("#ceng").val();
        par.fenDuan = $("#duanCiSel").val();
        par.type = 0;
        par.fei = 1;
        //if (!$(".weibiao").is(":checked") && !$(".biaole").is(":checked")) {
        //    par.type = 0;
        //}
        //if ($(".weibiao").is(":checked") && !$(".biaole").is(":checked")) {
        //    par.type = 1;
        //}
        //if (!$(".weibiao").is(":checked") && $(".biaole").is(":checked")) {
        //    par.type = 2;
        //}
        if ($(".fei0").is(":checked")) {
            par.fei = 0;
        }
        $('.bt-hh').removeClass('auto-bt').addClass('auto-btt');
        //e.stopPropagation();
        if (par.xqid != "" && par.gradeId != "" && par.sbn != "" && par.xqid != null && par.gradeId != null && par.sbn != null) {
            if (par.sbt == 2) {
                Common.getPostBodyData('/studentTag/getStudentListByDingErZouYi.do', par, function (rep) {
                    Common.render({tmpl: $('#studentListDing'), data: rep.message, context: '#tbd', overwrite: 1});
                });
            } else {
                Common.getPostBodyData('/studentTag/getStudentList.do', par, function (rep) {
                    Common.render({tmpl: $('#studentList'), data: rep.message, context: '#tbd', overwrite: 1});
                });
            }
        } else {
            $("#tbd").empty();
        }
    }
    module.exports = stuSign;
})