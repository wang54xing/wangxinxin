define('autoTimeCombKeBiao', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var autoTimeCombKeBiao = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    var Common = require('common');
    var ciId = "";
    var xqId = "";

    autoTimeCombKeBiao.init = function () {

        autoTimeCombKeBiao.getDefaultCi();

        autoTimeCombKeBiao.getTimeCombineInfos();

        autoTimeCombKeBiao.getListBySchoolIdTime();

        autoTimeCombKeBiao.getGradeWeek();

        autoTimeCombKeBiao.initKeBiao();

        autoTimeCombKeBiao.getTimeCombKeBiaoList();

        //点击时段组合
        $("body").on("click", "#timeCombList li", function () {
            $(".jxbkj .gdsw").each(function(i, gdsw){
                if($(gdsw).hasClass("gdsx")){
                    var title = $(gdsw).attr("gdsx")||"";
                    $(gdsw).attr("title", title);
                }else{
                    $(gdsw).attr("title", "");
                    $(gdsw).css({"background":""});
                    if($(this).find(".ls").length == 0 ) {
                        $(gdsw).html("");
                    }
                }
                $(gdsw).removeClass("gdsw");
            });

            var type = $(this).attr("ty");
            var serial = $(this).attr("srl");

            if($(this).hasClass("finish")){
                layer.alert("该时段组合已完成，请重新选择时段组合！！！");
                autoTimeCombKeBiao.getTimeCombJxbCtList(type, serial);
                return false;
            }

            if($(this).hasClass("act")){
                $(this).removeClass('act');
            }else {
                $(this).addClass('act').siblings().removeClass('act');
                autoTimeCombKeBiao.getTimeCombJxbCtList(type, serial);
            }
        });

        $('body').on('click', '.jxbkj .itt', function () {
            if($(this).find(".ls").length > 0 ){
                if($(this).hasClass("act1")){
                    $(this).removeClass("act1");
                }else{
                    $(".jxbkj .itt").removeClass('act1');
                    $(this).addClass("act1");
                }
            }else{
                var totalTimeComb = $("#totalTimeComb").text();
                var finishTimeComb = $("#finishTimeComb").text();
                if(totalTimeComb==finishTimeComb){
                    layer.msg("时段组合排课已完成！！！");
                    return false;
                }
                if($("#timeCombList").find(".act").length > 0 ) {
                    var timeComb = $("#timeCombList .act");
                    var saveParam = {};
                    saveParam.gradeId = $('#gradeId').val();
                    saveParam.ciId = ciId;
                    saveParam.type = $(timeComb).attr("ty");
                    saveParam.serialName = $(timeComb).attr("nm");
                    saveParam.serial = $(timeComb).attr("srl");
                    saveParam.x = $(this).attr("x");
                    saveParam.y = $(this).attr("y");
                    autoTimeCombKeBiao.saveTimeCombKeBiao(saveParam);
                }else{
                    layer.alert("请选择时段组合！！！");
                }
            }
        });

        $("body").on("click", "#cancel", function () {
            if($(".jxbkj .act1").length == 0){
                layer.alert("请选择撤销的时段组合！！！");
                return;
            }
            var par = {};
            par.gradeId = $("#gradeId").val();
            par.ciId = ciId;
            par.x = $('.jxbkj .act1').attr("x");
            par.y = $('.jxbkj .act1').attr("y");
            if(par.ciId != null && par.ciId != "" && par.gradeId != null && par.gradeId != "") {
                Common.getPostData('/n33TimeCombKeBiao/cancelTimeCombKeBiao.do', par, function (rep) {
                    if (rep.code == '200') {
                        layer.msg("撤销成功！");
                        $('.jxbkj .act1').html("");
                        $('.jxbkj .act1').removeClass('act1');
                        autoTimeCombKeBiao.getTimeCombineInfos();
                    }
                });
            }
        });

        $("body").on("click", "#autoPaiKe", function(){
            autoTimeCombKeBiao.autoBuildTimeCombKeBiao();
        });

        $('#nextStop').click(function () {
            sessionStorage.setItem('ATCKB_aotuPk', "Y");
            Common.goTo('/newisolatepage/pkTable.do');
        });

        $('#prevStop').click(function () {
            var gradeId = $("#gradeId").val();
            Common.goTo('/newisolatepage/jxbAutoTimeCombine.do?gradeId='+gradeId);
        });
    };

    autoTimeCombKeBiao.getDefaultCi = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            ciId = rep.message.paikeci;
            xqId = rep.message.xqid;
            //$("#defaultTerm").text(rep.message.paikeciname);
            //$("#term").val(rep.message.paikeci);
        });
    };

    autoTimeCombKeBiao.getTimeCombineInfos = function () {
        var par = {};
        par.ciId = ciId;
        par.gradeId = $('#gradeId').val();
        Common.getPostData('/timeCombine/getTimeCombineInfos.do', par, function (rep) {
            if(rep.code=='200'){
                var map = rep.message;
                var timeComb = $("#timeCombList .act");
                var type = $(timeComb).attr("ty");
                var serial = $(timeComb).attr("srl");
                $("#totalTimeComb").text(map.totalTimeComb);
                $("#finishTimeComb").text(map.finishTimeComb);
                var timeCombList = map.timeCombList;
                Common.render({tmpl: '#timeCombList_templ', data: timeCombList, context: '#timeCombList', overwrite: 1});
                var tag = $("#timeCombList li[ty=" + type + "][srl=" + serial + "]");
                if(!$(tag).hasClass("finish")){
                    $(tag).addClass('act');
                }
            }
        });
    };

    autoTimeCombKeBiao.getListBySchoolIdTime = function () {
        var xqid = ciId;
        if (xqid && xqid != null) {
            Common.getPostData('/courseset/getListBySchoolId.do', {"xqid": xqid}, function (rep) {
                Common.render({tmpl: $('#ks_temp'), data: rep, context: '.kejie', overwrite: 1});
            });
        }
    };

    autoTimeCombKeBiao.getGradeWeek = function () {
        var par = {};
        par.ciId = ciId;
        par.gradeId = $("#gradeId").val();
        if(par.ciId != null && par.ciId != "" && par.gradeId != null && par.gradeId != ""){
            Common.getPostData('/n33PaikeZuHe/getGradeWeek.do', par, function (rep) {
                Common.render({tmpl: $('#weeks_temp'), data: rep.message, context: '.weeks', overwrite: 1});
            });
        }
    };

    autoTimeCombKeBiao.initKeBiao = function () {
        var array = new Array();
        var dayCount = $(".gradeweek").length;
        var kjCount = $(".kejiecount").length;
        for(var x=0; x<dayCount; x++){
            for(var y=0; y<kjCount; y++){
                var map = {};
                map.x = x;
                map.y = y;
                array.push(map);
            }
        }
        $(".jxbkj").empty();
        Common.render({tmpl: $('#kbByTea'), data: array, context: '.jxbkj'});
    };

    autoTimeCombKeBiao.saveTimeCombKeBiao = function(saveParam){
        Common.getPostBodyData('/n33TimeCombKeBiao/saveTimeCombKeBiao.do', saveParam, function (rep) {
            if (rep.code == '200') {
                if(rep.message=='1'){
                    layer.msg("冲突，该课节已被占用！！！");
                }
                if(rep.message=='2'){
                    layer.msg("该时段组合已完成，请重新选择时段组合！！！");
                }
                autoTimeCombKeBiao.getTimeCombineInfos();
                autoTimeCombKeBiao.getTimeCombKeBiaoList();
                autoTimeCombKeBiao.getTimeCombJxbCtList(saveParam.type, saveParam.serial);
            }
        });
    };

    autoTimeCombKeBiao.getTimeCombKeBiaoList = function(){
        var par = {};
        par.gradeId = $("#gradeId").val();
        par.ciId = ciId;
        if(par.ciId != null && par.ciId != "" && par.gradeId != null && par.gradeId != ""){
            Common.getPostData('/n33TimeCombKeBiao/getTimeCombKeBiaoList.do', par, function (rep) {
                if (rep.code == '200') {
                    $(".jxbkj .itt").html("");
                    $(".jxbkj .itt").removeClass("gdsx").removeClass("gdsw");
                    $(".jxbkj .itt").attr("title", "");
                    $(".jxbkj .itt").css({"background":""});
                    var list = rep.message;
                    $.each(list, function (i, kb) {
                        var html = "<div class='ls'>" + kb.serialName + "</div>";
                        var tag = $(".jxbkj .itt[x=" + kb.x + "][y=" + kb.y + "]");
                        $(tag).html(html);
                        /*if($(tag).hasClass("gdsx")||$(tag).hasClass("gdsw")){
                            $(tag).css({"background":""});
                        }*/
                    });
                    autoTimeCombKeBiao.getGDSXList();
                }
            });
        }
    };

    autoTimeCombKeBiao.autoBuildTimeCombKeBiao = function(){
        var index = layer.load(2);
        var par = {};
        par.gradeId = $("#gradeId").val();
        par.xqId = xqId;
        par.ciId = ciId;
        setTimeout(function(){
            Common.getPostData('/n33TimeCombKeBiao/autoBuildTimeCombKeBiao.do', par, function (rep) {
                layer.close(index);
                if (rep.code == '200') {
                    $(".jxbkj .itt").removeClass('act1');
                    autoTimeCombKeBiao.getTimeCombineInfos();
                    autoTimeCombKeBiao.getTimeCombKeBiaoList();
                    layer.msg("自动排课完成");
                }
            });
        }, "100");
    };

    autoTimeCombKeBiao.getGDSXList = function () {
        var par = {};
        par.gid = $("#gradeId").val();
        par.xqid = xqId;
        Common.getPostData('/gdsx/getGDSXByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var tag = $(".jxbkj .itt[x=" + obj.x + "][y=" + obj.y + "]");
                $(tag).addClass("gdsx");
                $(tag).attr("title", obj.desc);
                $(tag).attr("gdsx", obj.desc);
                if($(tag).find(".ls").length == 0 ) {
                    $(tag).html(obj.desc);
                    $(tag).css("background", "#FFB6C1");
                }
            });
        });
    };

    autoTimeCombKeBiao.getTimeCombJxbCtList = function (type, serial) {
        var par = {};
        par.gradeId = $("#gradeId").val();
        par.xqId = xqId;
        par.ciId = ciId;
        par.type = type;
        par.serial = serial;
        Common.getPostData('/n33TimeCombKeBiao/getTimeCombJxbCtList.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var tag = $(".jxbkj .itt[x=" + obj.x + "][y=" + obj.y + "]");
                $(tag).addClass("gdsw");
                var title = $(tag).attr("title")||"";
                var firstMsg = "";
                $.each(obj.msgs, function (j, msg) {
                    if(j==0){
                        firstMsg = msg;
                    }
                    if (title == "") {
                        title = msg;
                    } else {
                        title += "\r\n";
                        title += msg;
                    }
                });
                $(tag).attr("title", title);
                if(!$(tag).hasClass("gdsx")){
                    if($(tag).find(".ls").length == 0 ) {
                        $(tag).html(firstMsg);
                        $(tag).css("background", "#FFB6C1");
                    }
                }
            });
        });
    };

    module.exports = autoTimeCombKeBiao;
});