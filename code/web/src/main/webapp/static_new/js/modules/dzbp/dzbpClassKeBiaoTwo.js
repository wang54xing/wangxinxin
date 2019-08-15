define('dzbpClassKeBiaoTwo', ['jquery', 'doT', 'common', 'layer'], function (require, exports, module) {
    var dzbpClassKeBiaoTwo = {};
    require('jquery');
    require('doT');
    require('layer');
    var Common = require('common');
    dzbpClassKeBiaoTwo.init = function () {
        dzbpClassKeBiaoTwo.shownCurrTime();
        dzbpClassKeBiaoTwo.getClassInfo();
        dzbpClassKeBiaoTwo.getZhouKeBiaoList();

        setInterval(dzbpClassKeBiaoTwo.shownCurrTime, 1000);
    };

    dzbpClassKeBiaoTwo.shownCurrTime = function(){
        var date=new Date();
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var seconds = date.getSeconds();
        if(hours<10){
            hours = '0'+hours;
        }
        var isRefresh = false;
        if(minutes%5==0&&seconds==0){
            isRefresh = true;
        }
        if(minutes<10){
            minutes = '0'+minutes;
        }
        if(seconds<10){
            seconds = '0'+seconds;
        }
        $('#time').text(hours+":"+minutes+":"+seconds);
        if(isRefresh){
            dzbpClassKeBiaoTwo.getClassInfo();
            dzbpClassKeBiaoTwo.shownCurrKeTea();
        }
    };

    dzbpClassKeBiaoTwo.shownCurrKeTea = function(){
        var courseRanges = dataMap.get("courseRanges")||[];
        var zkbList = dataMap.get("zkbList")||[];
        if(courseRanges.length==0||zkbList.length==0){
            dzbpClassKeBiaoTwo.getZhouKeBiaoList();
        }else{
            var keJie = dzbpClassKeBiaoTwo.getCurrKeJie(courseRanges);
            var currWeekIndex = $("#currWeekIndex").val();
            if(keJie==-1){
                $("#renKeTea").text("");
            }
            $.each(zkbList, function (i, obj) {
                var teaName = "";
                if(obj.teacherName != null&&obj.teacherName !=""){
                    teaName = obj.teacherName;
                }
                if(obj.x==currWeekIndex&&keJie==obj.y){
                    $("#renKeTea").text(teaName);
                }
            });
        }
    };

    dzbpClassKeBiaoTwo.getClassInfo = function(){
        var param = {};
        param.schoolId = $("#schoolId").val();
        param.classId = $("#classId").val();
        Common.getPostData("/n33App/getClassInfo.do", param, function (rep) {
            if(rep.code=='200'){
                var dto = rep.message;
                $("#className").text(dto.className);
            }
        });
    };

    var dataMap = Common.map();
    dzbpClassKeBiaoTwo.getZhouKeBiaoList = function(){
        var param = {};
        param.schoolId = $("#schoolId").val();
        param.classId = $("#classId").val();
        $("#renKeTea").text("");
        Common.getPostData("/n33App/getZhouKeBiaoList.do", param, function (rep) {
            if(rep.code=='200'){
                var msg = rep.message;
                Common.render({tmpl: $('#KeShiList_tmpl'), data: msg.courseRangeDTOs, context: '#KeShiList', overwrite: 1});
                dzbpClassKeBiaoTwo.initKeBiao();
                var keJie = dzbpClassKeBiaoTwo.getCurrKeJie(msg.courseRangeDTOs);
                if(keJie==-1){
                    $("#renKeTea").text("");
                }
                var currWeekIndex = $("#currWeekIndex").val();
                $.each(msg.zkbdto, function (i, obj) {
                    var tag = $(".itt[x=" + obj.x + "][y=" + obj.y + "]");
                    $(tag).css("background", "#FFF");
                    var teaName = "";
                    var subName = "";
                    if(obj.teacherName != null&&obj.teacherName !=""){
                        teaName = obj.teacherName;
                    }
                    if(obj.subjectName != null&&obj.subjectName !=""){
                        subName = obj.subjectName;
                    }
                    if(obj.x==currWeekIndex&&keJie==obj.y){
                        $("#renKeTea").text(teaName);
                    }
                    $(tag).html("<p>" + subName + "</p><p>" + teaName + "</p>");
                });
                dataMap.set("courseRanges", msg.courseRangeDTOs);
                dataMap.set("zkbList", msg.zkbdto);
            }
        });
    };

    dzbpClassKeBiaoTwo.initKeBiao = function () {
        /*$(".itt").each(function (i,td) {
            $(td).removeClass("red");
        });*/
        $("#KeShiList tr").each(function (i, tr) {
            var y = i;
            $(tr).find(".itt").each(function (j, td) {
                var x = j;
                $(td).attr("x", x);
                $(td).attr("y", y);
            });
        });
    };

    dzbpClassKeBiaoTwo.getCurrKeJie = function(list){
        var date = new Date();
        var curTime = date.getTime();
        var curDate = $("#currDate").val();
        var keJie = -1;
        $.each(list, function (i, obj) {
            var startDate = new Date(curDate+" "+obj.start+":00");
            var endDate = new Date(curDate+" "+obj.end+":00");
            var startTime = startDate.getTime()-10*60*1000;
            var endTime = endDate.getTime();
            if(curTime>=startTime&&curTime<endTime){
                keJie = i;
                return keJie;
            }
        });
        return keJie;
    };

    module.exports = dzbpClassKeBiaoTwo;
});