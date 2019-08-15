/**
 * Created by albin on 2018/3/24.
 */
define('classTable', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var classTable = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var deCid="";
    var tid = '';
    classTable.GetTeachersSettledPositions = function () {
        var par = {};
        par.classId = $("#Cls .t-li").attr("ids");
        par.xqid = deXqid;
        par.gradeId = $(".xueke .active").attr("ids");
        // par.week = $("#week").val();
        Common.getData('/paike/GetClassSettledPositions.do', par, function (rep) {
            $("#ban").html($("#Cls .t-li").find("span").html())
            $(".itt").css("background", "#FFF")
            $(".itt").css("color","#000")
            $.each(rep.message, function (i, obj) {
                if(obj.type==1) {
                    if(obj.teaName != undefined){
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html("<p>" + obj.jList + "</p><p>" + obj.teaName + "</p>")
                    }else{
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html("<p>" + obj.jList + "</p><p>" + "</p>")
                    }
                }else{
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html("走班")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").attr("title",obj.jList)
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").css("color","green")
                }
            })
        })
    }

    classTable.getGDSX = function () {
        var par = {};
        par.xqid = tid;
        par.gid = $(".xueke .active").attr("ids");
        Common.getData('/gdsx/getGDSXByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var x =obj.y + 1;
                var y =obj.x + 1;
                $(".itt[x=" + x + "][y=" + y + "]").html(obj.desc)
                $(".itt[x=" + x + "][y=" + y + "]").css("background", "#ccc")
            })
        })
    }

    classTable.init = function () {
        $('body').on('click','#printGDG',function(){
            $('#printGDG').hide();
            var headstr = "<html><head><title></title></head><body>";
            var footstr = "</body>";
            var newstr = $('#GDGContent').html();
            var oldstr = document.body.innerHTML;
            document.body.innerHTML = headstr+newstr+footstr;
            window.print();
            console.log('老页面：'+oldstr);
            console.log('新页面：'+newstr);
            document.body.innerHTML = oldstr;
            $('#printGDG').show()
            return false;
        })
        classTable.getDefaultTerm();
        // $("#week").change(function(){
        //     classTable.GetTeachersSettledPositions();
        //     classTable.getGuDingShiWu()
        // })
        $("body").on("click",".xueke label",function(){
            $(".xueke label").removeClass("active");
            $(this).addClass("active");
            $(".itt").html("");
            $(".itt").removeClass("gray");
            $(".itt").removeClass("red");
            classTable.getClassList();
            classTable.GetTeachersSettledPositions();
            classTable.getGuDingShiWu()
            classTable.getClassCount();
            classTable.getGDSX();
        })

        $("body").on("click",".b9",function(){
            var par = {};
            par.classId = $("#Cls .t-li").attr("ids");
            par.xqid = deXqid;
            par.gradeId = $(".xueke .active").attr("ids");
            par.week = $("#week").val();
            window.location.href = "/paike/exportXZBKB.do?classId="+par.classId + "&xqid=" + par.xqid + "&gradeId=" + par.gradeId + "&week=" + par.week;

        });

        classTable.getListBySchoolId();
        classTable.getGrade();
        // classTable.getListByXq();
        $("body").on("click","#Cls li",function(){
            $("#Cls li").removeClass("t-li");
            $(this).addClass("t-li");
            $(".itt").html("")
            classTable.GetTeachersSettledPositions();
            classTable.getGuDingShiWu()
            classTable.getGDSX();
        })
        classTable.getChushi();
        classTable.GetTeachersSettledPositions();
        classTable.getGuDingShiWu()
        classTable.getClassCount();
        classTable.getGDSX();
    }

    classTable.getDefaultTerm = function(){
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            deXqid = rep.message.paikeci;
            deCid = rep.message.paikeci;
            tid = rep.message.paikexq;
            $("#defaultTerm").attr("ids",rep.message.xqid);
        });
    }

    // classTable.getListByXq = function () {
    //     var wk = 1;
    //     Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
    //         wk = rep.message;
    //     })
    //     Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
    //         Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
    //         $("#week option[value=" + wk + "]").prop("selected", true)
    //     })
    // }
    classTable.getChushi = function () {
        var y = 1;
        var x = 1;
        $(".itt").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            y += 1;
            if (y == 8) {
                x += 1;
                y = 1;
            }
        })
    }

    classTable.getGuDingShiWu = function () {
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": tid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
            })
        })
    }
    classTable.getListBySchoolId = function () {
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolId.do', {"xqid": deXqid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }

    classTable.getGrade = function () {
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": deXqid}, function (rep) {
            $(".xueke").append(" <span>年级: </span>")
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.xueke'});
            $(".xueke label:eq(0)").addClass("active");
            classTable.getClassList();
        })
    }

    classTable.getClassList = function () {
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $(".xueke .active").attr("ids");
        Common.getData('/new33classManage/getClassList.do',par, function (rep) {
            Common.render({tmpl: $('#class_temp'), data: rep.message, context: '#Cls',overwrite: 1});
            $("#Cls li:eq(0)").addClass("t-li")
        })
    }

    classTable.getClassCount = function(){
        var count = 0;
        $("#Cls li").each(function () {
            count ++;
        });
        if(count != 0){
            $(".classCount").text("（班级数：" + count + "）");
        }
    }

    module.exports = classTable;
})