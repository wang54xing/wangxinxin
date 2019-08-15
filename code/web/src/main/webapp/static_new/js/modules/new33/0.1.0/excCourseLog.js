define('excCourseLog', ['jquery', 'doT', 'common', 'pagination', 'Rome', 'layer'], function (require, exports, module) {
    var excCourseLog = {};
    require('jquery');
    require('doT');
    require('layer');
    require('pagination');
    Common = require('common');
    var deXqid = "";
    var xqid = "";
    var l3 = 1;
    var l2 = 1;
    var l1 = 1;
    var nb = 0;
    var currentWeek = 0;
    var weekArg = new Array("周一","周二","周三","周四","周五","周六","周日");

    excCourseLog.init = function() {
        excCourseLog.getDefaultTerm();
        excCourseLog.getGradeList();
        $('.tkbg').click(function(){
            excCourseLog.getTKTotal();
            $('.mm1').hide();
            $('.mm2').show();
        });
        $('.prevK').click(function(){
            // $('.mm1').show();
            // $('.mm2').hide();
            window.location.reload();
        });

        $('.qUl li').click(function(){
            $(this).addClass('active').siblings().removeClass('active');
            var tt = $(this).attr('id');
            $('#' + "tab-" + tt).show().siblings("div").hide();
        });
        $("body").on("click", ".agree", function () {
            excCourseLog.tkChangeType($(this).attr('lid'),1);
        });
        $("body").on("click", ".export", function () {
            excCourseLog.export();
        });

        $("body").on("click", ".refuse", function () {
            excCourseLog.tkChangeType($(this).attr('lid'),2);
        });

        $('.search').click(function() {
                excCourseLog.getTkLogs(1);
        })
        $('.gaozhong label').click(function () {
            $('.gaozhong label').each(function (i) {
                $(this).removeClass('active');
            })
            $(this).addClass('active');
            excCourseLog.getTkLogs(1);
        });

        $(document).ready(function(){
            var len = ($('.zhou label').length-1)*100;
            $('th>div').css('width',len+'px');
        })

    }
    /**
     * @func chartInit
     * @desc 渲染图表
     * @example
     * chengJi.chartInit(id,{})
     */
    excCourseLog.chartInit = function(id,opt){
        var _chart = echarts.init(document.getElementById(id));
        _chart.setOption(opt);
    }

    excCourseLog.getTKTotal = function() {
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        data.gradeId = gradeId;
        data.termId = xqid;
        data.ci = deXqid;
        Common.getData('/paike/getTKTotal.do', data, function (rep) {
            var chardata1 = excCourseLog.buildChartData("myChart1",rep.message);
            excCourseLog.chartInit('D1',chardata1);
            var chardata2 = excCourseLog.buildChartData("myChart2",rep.message);
            excCourseLog.chartInit('D2',chardata2);
            var chardata3 = excCourseLog.buildChartData("myChart3",rep.message);
            excCourseLog.chartInit('D3',chardata3);
            var chardata4 = excCourseLog.buildChartData("myChart4",rep.message);
            excCourseLog.chartInit('D4',chardata4);
            var chardata5 = excCourseLog.buildChartData("myChart5",rep.message);
            excCourseLog.chartInit('D5',chardata5);
            var chardata6 = excCourseLog.buildChartData("myChart6",rep.message);
            excCourseLog.chartInit('D6',chardata6);
            var chardata7 = excCourseLog.buildChartData("myChart7",rep.message);
            excCourseLog.chartInit('D7',chardata7);
        });
    }
    excCourseLog.buildChartData = function (type, rawdata){
        var charData;
        var subjectNames=new Array();
        var subjectLongCnts=new Array();
        var subjectShortCnts=new Array();
        var subjectAllCnts=new Array();
        if (rawdata.subjectCntDTOs!=null && rawdata.subjectCntDTOs.length!=0) {
            for (var i=0;i<rawdata.subjectCntDTOs.length;i++) {
                subjectNames[i] = rawdata.subjectCntDTOs[i].subjectName;
                subjectShortCnts[i] = rawdata.subjectCntDTOs[i].shortCnt;
                subjectLongCnts[i] = rawdata.subjectCntDTOs[i].longCnt;
                subjectAllCnts[i] = rawdata.subjectCntDTOs[i].allCnt;
            }
        }
        if (type=="myChart1") {
            charData = {
                title : {
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    left: 'left',
                    data: ['调课数量','代课数量']
                },
                color:['#2ec7c9', '#b6a2de'],
                series : [
                    {
                        name: '',
                        type: 'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data:[
                            {value:rawdata.tkCnt, name:'调课数量'},
                            {value:rawdata.dkCnt, name:'代课数量'}
                        ],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            }
        }  else if (type=="myChart2") {
            charData = {
                title : {
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    left: 'left',
                    data: ['短期数量','长期数量']
                },
                color:['#2ec7c9', '#b6a2de'],
                series : [
                    {
                        name: '',
                        type: 'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data:[
                            {value:rawdata.shortCnt, name:'短期数量'},
                            {value:rawdata.longCnt, name:'长期数量'}
                        ],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            }
        }  else if (type=="myChart3") {

            charData = {
                title : {
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                xAxis: {
                    type: 'category',
                    data: subjectNames
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    type:'bar',
                    name:'调课数量',
                    data: subjectAllCnts,
                    type: 'bar',
                    itemStyle: {
                        //通常情况下：
                        normal:{
                            color:'#b6a2de'
                        }
                    }
                }]
            }
        }  else if (type=="myChart4") {
            charData = {
                title : {
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                xAxis: {
                    type: 'category',
                    data: subjectNames
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    type:'bar',
                    name:'调课数量',
                    data: subjectLongCnts,
                    type: 'bar',
                    itemStyle: {
                        //通常情况下：
                        normal:{
                            color:'#b6a2de'
                        }
                    }
                }]
            }
        }  else if (type=="myChart5") {
            charData = {
                title : {
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                xAxis: {
                    type: 'category',
                    data: subjectNames
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    type:'bar',
                    name:'调课数量',
                    data: subjectShortCnts,
                    type: 'bar',
                    itemStyle: {
                        //通常情况下：
                        normal:{
                            color:'#b6a2de'
                        }
                    }
                }]
            }
        }  else if (type=="myChart6") {
            charData = {
                title : {
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    left: 'left',
                    data: ['行政调课','等级调课','合格调课']
                },
                color:['#2ec7c9', '#b6a2de','#F48F49'],
                series : [
                    {
                        name: '',
                        type: 'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data:[
                            {value:rawdata.xzCnt, name:'行政调课'},
                            {value:rawdata.djCnt, name:'等级调课'},
                            {value:rawdata.hgCnt, name:'合格调课'}
                        ],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            }
        }  else if (type=="myChart7") {
            var weekCnt = new Array();
            if (rawdata.weekCntDTOs!=null && rawdata.weekCntDTOs.length!=0) {
                for (var i = 0; i < rawdata.weekCntDTOs.length; i++) {
                    weekCnt[i] = rawdata.weekCntDTOs[i].count;
                }
            }
            charData = {
                title : {
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                xAxis: {
                    type: 'category',
                    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    type:'bar',
                    name:'学生调课总数量',
                    data: weekCnt,
                    type: 'bar',
                    itemStyle: {
                        //通常情况下：
                        normal:{
                            color:'#b6a2de'
                        }
                    }
                }]
            }
        }
        return charData;
    }
    excCourseLog.getDefaultTerm = function(){
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            xqid = rep.message.xqid;
            deXqid = rep.message.paikeci;
            $("#defaultTerm").attr("ids",rep.message.paikeci);
        });
    }

    excCourseLog.export = function() {
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        window.location.href = "/paike/exportTKReport.do?gradeId=" + gradeId + "&termId=" + xqid + "&ci=" + deXqid + "&userName=" + $('#userName').val();
    }

    excCourseLog.tkChangeType = function(id,type) {
        Common.getData('/paike/tkChangeType.do', {"id": id,"type":type}, function (rep) {
            if (rep.code=='200') {
                if (type==1) {
                    layer.msg("通过成功！");
                } else {
                    layer.msg("拒绝成功！");
                }
                excCourseLog.getTkLogs(1);
            } else {
                if (type==1) {
                    layer.msg("通过失败！");
                } else {
                    layer.msg("拒绝失败！");
                }
            }
        });
    }

    excCourseLog.getGradeList = function () {
        if(xqid != null && xqid != undefined  && xqid != ""){
            Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": xqid}, function (rep) {
                $(".gaozhong").append(" <span>年级 : </span>")
                Common.render({tmpl: $('#grade_temp'), data: rep, context: '.gaozhong'});
                // $(".gaozhong label:eq(0)").addClass("active");
                excCourseLog.initGrade();
                excCourseLog.getTkLogs(1);
            })
        }
    }


    excCourseLog.getTkLogs = function (page) {
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        data.gradeId = gradeId;
        data.termId = xqid;
        data.ci = deXqid;
        data.userName = $('#userName').val();
        data.page = page;
        data.pageSize = 10;
        Common.getData('/paike/getTiaoKeLog.do', data, function (rep) {
            if (rep.code == '200') {
                if(rep.message.count==0){
                    $("#none_png").show();
                    $("#content").hide();
                }
                else{
                    $("#none_png").hide();
                    $("#content").show();
                }
                if (rep.message.logs != null) {
                    $('.tkLog').empty();
                    Common.render({tmpl: $('#tkLog_temp'), data: rep.message.logs, context: '.tkLog'});
                    $('.pageDiv').jqPaginator({
                        totalPages: Math.ceil(rep.message.count / 10) == 0 ? 1 : Math.ceil(rep.message.count / 10),
                        visiblePages: 5,//分多少页
                        currentPage: parseInt(page),//当前页数
                        first: '<li class="first"><a href="javascript:void(0);">首<\/a><\/li>',
                        prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                        next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                        last: '<li class="last"><a href="javascript:void(0);">末<\/a><\/li>',
                        page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                        onPageChange: function (n) { //回调函数
                            if (n != page) {
                                page = n;
                                excCourseLog.getTkLogs(page);
                            }
                        }
                    });
                }
            }
        })
    }
    excCourseLog.initGrade = function () {
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
    module.exports = excCourseLog;
});