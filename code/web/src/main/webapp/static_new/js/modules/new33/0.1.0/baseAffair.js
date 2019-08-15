/**
 * Created by albin on 2018/3/13.
 */
define('baseAffair', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer', "zTreeCore", "zTreeExcheck", "zTreeExedit"], function (require, exports, module) {
    var baseAffair = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    require('zTreeCore');
    require('zTreeExcheck');
    require('zTreeExedit');
    var terrId = "";
    var pid = "";
    var xqid = ""
    Common = require('common');
    var teaList = new Array();
    var xIndexs = new Array();
    var yIndexs = new Array();
    var names = new Array();
    var cid = ""
    var isFaBu = true;

    baseAffair.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term").val(rep.message.paikeci);
            cid = rep.message.paikeci;
            xqid = rep.message.paikexq;
        });
    }

    baseAffair.getListBySchoolId = function () {
        if (cid != null && cid != undefined) {
            Common.getData('/courseset/getListBySchoolId.do', {"xqid": cid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#keShi', overwrite: 1});
            });
        }
    }
    baseAffair.getIndexList = function () {
        var kename = "";
        var count = 0;
        xIndexs = new Array();
        yIndexs = new Array();
        $("#keShi tr").each(function (ri, tr) {
            var td = $(this).find(".s-cur");
            $(td).each(function (di, ds) {
                if ($(ds).find("em").hasClass("set-cur")) {
                    xIndexs.push(ri + 1)
                    yIndexs.push(di + 1)
                    if (count < 3) {
                        kename += "周" + (di + 1) + "第" + (ri + 1) + "节,";
                        count += 1;
                    }
                }
            })
        })
        $(".shezhi").html(kename + "等" + yIndexs.length + "个课时")
        $(".set-popup").hide();
    }
    baseAffair.addShiWu = function () {
        var dto = {};
        dto.id = $("#baocunShiWu").attr("ids");
        dto.swlbId = $("#swTy").val();
        dto.termId = xqid;
        dto.desc = $(".inp4").val();
        dto.level = $(".inp9").val();
        dto.xindex = xIndexs;
        dto.yindex = yIndexs;
        dto.tealist = teaList;
        dto.sk = "0";
        if ($("#stuKe").is(":checked")) {
            dto.sk = "1";
        }
        if (dto.desc.length > 20) {
            layer.msg("名称不能超过20个字符");
            return;
        }
        if (dto.desc == "") {
            layer.msg("请将内容填写完整")
            return
        }
        if (dto.level == "") {
            layer.msg("请将内容填写完整")
            return
        }
        if (dto.yindex.length == 0) {
            layer.msg("请将内容填写完整")
            return
        }
        Common.getPostBodyData('/new33isolateMange/addSwDto.do', dto, function (rep) {
            baseAffair.getSwList();
            $('.sww-popup,.bg').fadeOut();
            layer.msg('保存成功')
        });
    }

    baseAffair.getSwList = function () {
        var par = {};
        par.xqid = xqid;
        par.typeId = $("#swType .cur").attr("ids");
        Common.getData('/new33isolateMange/getSwByXqidAndType.do', par, function (rep) {
            if(rep.message.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            Common.render({tmpl: $('#sw'), data: rep.message, context: '#swTbd', overwrite: 1});
        })
    }
    baseAffair.getIsFaBu = function () {
        var par = {};
        par.xqid = xqid;
        Common.getData('/new33isolateMange/getXqIdIsFaBu.do', par, function (rep) {
            isFaBu = rep.message;
        });
    }
    baseAffair.init = function () {
        $(document).on('click', '.detail', function () {
            $('.tc-popup').show();
            $('.bg').show()
            var par = {};
            par.id = $(this).attr("ids");
            $("#swta").empty();
            Common.getData('/new33isolateMange/getSwTeaList.do', par, function (rep) {
                Common.render({tmpl: $('#SWTEALIST'), data: rep.message, context: '#swta', overwrite: 1});
            })
        });
        baseAffair.getDefaultTerm();
        baseAffair.getIsFaBu();
        $('.t-tx').click(function () {
            $("#myList").empty();
            $('.bg').show();
            $('.qua-popup').show();
            baseAffair.getUserListBySubjectListAndGradeId();
            baseAffair.addUserList();
        })
        $("body").on("click", ".opde", function () {
            if (isFaBu == false) {
                var par = {};
                par.id = $(this).attr("ids");
                Common.getData('/new33isolateMange/delSwById.do', par, function (rep) {
                    layer.msg(rep.message);
                    baseAffair.getSwList();
                })
            } else {
                layer.msg("当前学期已经发布,不允许修改数据!")
            }
        })

        $("body").on("click", ".optt", function () {
            if (isFaBu == false) {
                teaList = new Array();
                xIndexs = new Array();
                yIndexs = new Array();
                names = new Array();
                var par = {};
                par.id = $(this).attr("ids");
                par.cid = cid;
                $(".sww-popup,.bg").show();
                $("#xb").html("编辑")
                $(".s-cur em").removeClass("set-cur");
                Common.getData('/new33isolateMange/getSwById.do', par, function (rep) {
                    teaList = rep.message.tids;
                    xIndexs.push(rep.message.x);
                    yIndexs.push(rep.message.y);
                    names = rep.message.names;
                    if (rep.message.sk == 0) {
                        $("#stuKe").prop("checked", "")
                    } else {
                        $("#stuKe").prop("checked", "checked")
                    }
                    $("#baocunShiWu").attr("ids", rep.message.id);
                    $(".shezhi").html(rep.message.xy)
                    $(".tongxun").html(rep.message.teaName)
                    $(".inp4").val(rep.message.desc)
                    $(".inp9").val(rep.message.level)
                    $("#swTy option[value=" + rep.message.swlbId + "]").prop("selected", true);
                    var y = 1;
                    var x = 1;
                    $(".s-cur").each(function () {
                        if (y == rep.message.y && x == rep.message.x) {
                            $(this).find("em").addClass("set-cur");
                        }
                        y += 1;
                        if (y == $("#keShi tr:eq(0) td").length) {
                            x += 1;
                            y = 1;
                        }
                    })
                })
            } else {
                layer.msg("当前学期已经发布,不允许修改数据!")
            }
        })
        //保存事务
        $("#baocunShiWu").click(function () {
            baseAffair.addShiWu();
        })

        $(".queDingRen").click(function () {
            var name = "";
            $.each(names, function (i, obj) {
                if (i < 3) {
                    name += obj + ",";
                }
            })
            $(".tongxun").html(name + "等" + names.length + "人");
            $('.qua-popup').hide();
            $('.sww-popup').show();
        })


        $('.sel-tea').click(function () {
            if (isFaBu == false) {
                teaList = new Array();
                xIndexs = new Array();
                yIndexs = new Array();
                names = new Array();
                $("#baocunShiWu").attr("ids","*");
                $("#xb").html("新增")
                $(".inp4").val("")
                $(".sww").html("")
                $(".tongxun").html("全体人员事务")
                $('.bg').show();
                $('.sww-popup').show();
                $("#stuKe").prop("checked")
                $("#myList em").removeClass("spe-cur")
                $("#keShi em").removeClass("set-cur")
                $("#swTy option[value=" + $("#swType .cur").attr("ids") + "]").prop("selected", true)
            } else {
                layer.msg("当前学期已经发布,不允许修改数据!")
            }
        })
        $("body").on("click", "#sou", function () {
            baseAffair.getUserListBySubjectListAndGradeId1();
            baseAffair.addUserList();
        })

        $("body").on("click", "#myList li", function () {
            if ($(this).find("em").hasClass("spe-cur")) {
                var newTeaList = new Array();
                var newTeaNameList = new Array();
                var uid = $(this).attr("uid");
                $.each(teaList, function (i, obj) {
                    if (obj != uid) {
                        newTeaList.push(obj)
                        newTeaNameList.push(names[i]);
                    }
                })
                //删除
                teaList = newTeaList;
                names = newTeaNameList;
            } else {
                var index = 0;
                //是否存在重复
                $.each(teaList, function (i, obj) {
                    if (obj == $(this).attr("uid")) {
                        index = 1;
                    }
                })
                if (index == 0) {
                    teaList.push($(this).attr("uid"));
                    names.push($(this).attr("nm"));
                }
            }
            $(this).find('em').toggleClass('spe-cur')
        })
        $(".qdIndex").click(function () {
            baseAffair.getIndexList();
        })
        baseAffair.getXueQi();
        baseAffair.getSwType();
        baseAffair.getTr();
        baseAffair.getListBySchoolId();
        baseAffair.getSwList();
        $("body").on("click", "#term em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            baseAffair.getSwType()
            baseAffair.getTr();
            baseAffair.getListBySchoolId();
            baseAffair.getSwList();
        })

        $("body").on("click", "#swType em", function () {
            $(this).addClass('cur').siblings().removeClass('cur');
            $(".c-bb").html($(this).html())
            baseAffair.getSwList();
        })
    }

    baseAffair.getXueQi = function () {
        //Common.getData('/new33isolateMange/getTermList.do', {}, function (rep) {
        //    if (rep.code == 200) {
        //        Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term', overwrite: 1});
        //        $("#term em:eq(0)").addClass("cur");
        //    }
        //});
    }
    baseAffair.getSwType = function () {
        var par = {};
        par.xqid = xqid;
        Common.getData('/new33isolateMange/getSwTypeListByXqid.do', par, function (rep) {
            Common.render({tmpl: $('#sw_temp'), data: rep.message, context: '#swType', overwrite: 1});
            $("#swType em:eq(0)").addClass("cur");
            Common.render({tmpl: $('#swtemp'), data: rep.message, context: '#swTy', overwrite: 1});

        })
    }
    baseAffair.zTreeOnClic = function (event, treeId, treeNode) {
        terrId = treeNode.id;
        pid = treeNode.parentId;
        baseAffair.getUserListBySubjectListAndGradeId();
        baseAffair.addUserList();
    };

    baseAffair.getTr = function () {
        $.fn.zTree.init($("#treeDemo"), {
            async: {
                enable: true,
                url: '/new33isolateMange/getSubjectListByXqId.do?xqid=' + xqid
            },
            data: {
                simpleData: {
                    enable: true,
                    pIdKey: 'parentId',
                    rootPId: 0
                }
            },
            callback: {
                onClick: baseAffair.zTreeOnClic
            }
        })

    }
    baseAffair.getUserListBySubjectListAndGradeId1 = function () {
        var par = {};
        par.gid = "*";
        par.subid = "*";
        par.xqid = xqid;
        par.name = $("#name").val();
        if (par.name == "") {
            par.name = "*";
        }
        if (pid != "") {
            Common.getData('/new33isolateMange/getTeaListByXqid.do', par, function (rep) {
                Common.render({tmpl: $('#TeaLi'), data: rep.message, context: '#myList', overwrite: 1});
            })
        } else if (par.name != "*") {
            par.gid = "*";
            Common.getData('/new33isolateMange/getTeaListByXqid.do', par, function (rep) {
                Common.render({tmpl: $('#TeaLi'), data: rep.message, context: '#myList', overwrite: 1});
            })
        }
    }
    baseAffair.getUserListBySubjectListAndGradeId = function () {
        var par = {};
        if (pid == 0) {
            par.gid = terrId;
            par.subid = "*";
        } else {
            par.gid = pid;
            par.subid = terrId;
        }
        par.xqid = xqid;
        par.name = $("#name").val();
        if (par.name == "") {
            par.name = "*";
        }
        if (pid != "") {
            Common.getData('/new33isolateMange/getTeaListByXqid.do', par, function (rep) {
                Common.render({tmpl: $('#TeaLi'), data: rep.message, context: '#myList', overwrite: 1});
            })
        } else if (par.name != "*") {
            par.gid = "*";
            Common.getData('/new33isolateMange/getTeaListByXqid.do', par, function (rep) {
                Common.render({tmpl: $('#TeaLi'), data: rep.message, context: '#myList', overwrite: 1});
            })
        }
    }
    baseAffair.addUserList = function () {
        $.each(teaList, function (i, obj) {
            $("#myList li[uid=" + obj + "]").find('em').addClass("spe-cur")
        })
        $("#myList-nav").html("");
        $("#myList-nav").append(" <div class='demo-search'> <input type='text'id='name'> <button id='sou'>搜索</button> </div>")
        $('#myList').listnav({
            includeOther: true,
            noMatchText: '',
            prefixes: ['the', 'a']
        });
    }
    module.exports = baseAffair;
})