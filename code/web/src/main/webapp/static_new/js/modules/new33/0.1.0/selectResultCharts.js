define('selectResultCharts', ['jquery', 'doT', 'common','Rome', 'pagination','layer','echarts4'], function (require, exports, module) {
    var selectResultCharts = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('echarts4');
    common = require('common');

    selectResultCharts.init=function(){
        $(".top-nav .a1").addClass("active")
        selectResultCharts.getTermList();
        selectResultCharts.getDefaultCi();
        selectResultCharts.getgrade($("#term .cur").attr("val"));
        selectResultCharts.getClass();
        selectResultCharts.getStuSelectResultByClass();
        initGrade();
        $("body").on("click","#term em",function(){
            $(this).addClass('cur').siblings().removeClass('cur');
            selectResultCharts.getgrade($(this).attr("val"));
            selectResultCharts.getClass();
            selectResultCharts.getStuSelectResultByClass();
        });
        $("body").on("click","#grade em",function(){
            $(this).addClass('cur').siblings().removeClass('cur');
            selectResultCharts.getClass();
            selectResultCharts.getStuSelectResultByClass();
        });
        $("body").on("click","#type em",function(){
            $(this).addClass('cur').siblings().removeClass('cur');
            selectResultCharts.getStuSelectResultByClass();
        });
        $("#classList").change(function(){
            selectResultCharts.getStuSelectResultByClass();
        });

    }
    selectResultCharts.getTermList = function(){
        common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
            if(rep.code==200){
                common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term',overwrite:1});
                // $("#term em:eq(0)").addClass("cur");
            }
        });
    }
    selectResultCharts.getgrade = function(xqid){
        common.getData('/new33isolateMange/getGradList.do', {"xqid":xqid}, function (rep) {
            common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade',overwrite:1});
            // $("#grade em:eq(0)").addClass("cur");
        })
    }
    selectResultCharts.getClass = function(){
        var xqid = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");

        if(!xqid||!gradeId||xqid==""||gradeId==""){
            return;
        }
        common.getData('/new33classManage/getClassList.do', {"xqid":xqid,"gradeId":gradeId}, function (rep) {
            rep.message.unshift({"classId":"grade","class":"年级"});
            common.render({tmpl: $('#classList_temp'), data: rep.message, context: '#classList',overwrite:1});

        })
    }
    selectResultCharts.getStuSelectResultByClass = function(){
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");
        var type = $("#type em[class='cur']").attr("val");
        if(!termId||!gradeId||termId==""||gradeId==""||!type||type==""){
            return;
        }
        common.getData('/new33school/set/getStuSelectResultByClass.do', {"xqid":termId,"gradeId":gradeId,"type":type}, function (rep) {
            var classId =  $("#classList").val();
            var title = [];
            for(var i in rep.content){
                title.push(rep.content[i].name);
            }

            var dataArray = [];
            if(classId=="grade"){
                var temparr = [];

                for(var i in rep.content){
                    var temparr2 = [];
                    for(var j in rep.content[i].list){
                        temparr2.push(rep.content[i].list[j].num)
                    }
                    temparr.push(temparr2);
                }
                for(var i in temparr){
                    for(var j in temparr[i]){
                        if(!dataArray[i]){
                            dataArray[i] = 0;
                        }
                        dataArray[i]+=Number(temparr[i][j]);
                    }
                }
            }
            else{
                for(var i in rep.content){
                    for(var j in rep.content[i].list){
                        if(rep.content[i].list[j].id==classId){
                            dataArray.push(rep.content[i].list[j].num)
                        }

                    }
                }
            }
            setCharts(dataArray,title);
        })
    }
    function setCharts(dataArray,title){
        var myChart1 = echarts.init(document.getElementById('main1'), 'macarons');
        var option1 = {
            tooltip: {
                show: true,
                trigger: 'axis'
            },
            legend: {
                data: ['数量'],
                left: 'left'
            },
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            calculable: true,
            xAxis: [
                {
                    type: 'category',
                    data: title,
                    axisLabel: {
                        rotate: 50,
                        interval: 0
                    }

                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '数量',
                    type: 'bar',
                    data: dataArray,
                    itemStyle: {
                        normal: {
                            label: {
                                show: true
                            }
                        }
                    }
                }
            ]
        };
        myChart1.setOption(option1);
    }
    selectResultCharts.getDefaultCi = function () {
        common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term em").each(function () {
                if(rep.message.paikeci==$(this).attr("val")){
                    $(this).addClass("cur");
                }
            });
        });
    }
    function initGrade(){
        try {
            common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                        if (obj.key == "type") {
                            type = obj.value;
                        }

                    })
                    $("#grade em").each(function () {
                        if(gradeId==$(this).attr("val")){
                            $(this).click();
                        }
                    });
                    $("#type em").each(function () {
                        if(type==$(this).attr("val")){
                            $(this).click();
                        }
                    });
                } else {

                }
            });
        } catch (x) {

        }
    }
    module.exports = selectResultCharts;
})