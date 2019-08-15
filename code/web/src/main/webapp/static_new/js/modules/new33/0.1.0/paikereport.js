/**
 * Created by albin on 2017/7/25.
 */

define('paikereport', ['jquery', 'doT', 'common','Rome', 'pagination', 'layer'], function (require, exports, module) {
    var paikereport = {};
    require('jquery');
    require('doT');
    require('layer');
    Common = require('common');

    var ciId = $("body").attr("ciId");
    paikereport.init=function(){
        getgrade();
        getReport();
        $("#grade").change(function(){
            getReport();
        });

    }
    function getReport(){
        var par = {};
        par.ciId = $("body").attr("ciId");
        par.gid = $("#grade").val();
        Common.getData('/n33PaikeReport/getReport.do', par, function (rep) {
           // console.log(rep);
            Common.render({tmpl: $('#zhengti1_temp'), data: rep.zhengti1, context: '#zhengti1',overwrite:1});

            $("#zhengti2 td[name='zixinum']").html(rep.zhengti2.zixinum);
            $("#zhengti2 td[name='zixihour']").html(rep.zhengti2.zixihour);
            $("#zhengti2 td[name='feizoubanhour']").html(rep.zhengti2.feizoubanhour);
            $("#zhengti2 td[name='feizoubannum']").html(rep.zhengti2.feizoubannum);
            $("#zhengti2 td[name='zoubannum']").html(rep.zhengti2.zoubannum);
            $("#zhengti2 td[name='zoubanhour']").html(rep.zhengti2.zoubanhour);

            $("#zouban1 td[name='zoubannum']").html(rep.zouban1.zoubannum);
            $("#zouban1 td[name='teanum']").html(rep.zouban1.teanum);
            $("#zouban1 td[name='ratio']").html(rep.zouban1.ratio);
            $("#zouban1 td[name='zoubanhour']").html(rep.zouban1.zoubanhour);

            Common.render({tmpl: $('#zouban2_temp'), data: rep.zouban2, context: '#zouban2',overwrite:1});

            Common.render({tmpl: $('#zouban3_temp'), data: rep.zouban3, context: '#zouban3',overwrite:1});

            Common.render({tmpl: $('#shiwu_temp'), data: rep.shiwu, context: '#shiwu',overwrite:1});

            $("#kebiao1 td[name='keyong']").html(rep.kebiao1.keyong);
            $("#kebiao1 td[name='swkeyong']").html(rep.kebiao1.swkeyong);
            $("#kebiao1 td[name='xwkeyong']").html(rep.kebiao1.xwkeyong);
            $("#kebiao1 td[name='wskeyong']").html(rep.kebiao1.wskeyong);

            $("#kebiao2 td[name='zoubanshangwu']").html(rep.kebiao1.zoubanshangwu);
            $("#kebiao2 td[name='feizoubanshangwu']").html(rep.kebiao1.feizoubanshangwu);
            $("#kebiao2 td[name='zoubanxiawu']").html(rep.kebiao1.zoubanxiawu);
            $("#kebiao2 td[name='feizoubanxiawu']").html(rep.kebiao1.feizoubanxiawu);
            $("#kebiao2 td[name='zoubanwanshang']").html(rep.kebiao1.zoubanwanshang);
            $("#kebiao2 td[name='feizoubanwanshang']").html(rep.kebiao1.feizoubanwanshang);

            Common.render({tmpl: $('#select_result_temp'), data: rep.selectresult, context: '#select_result',overwrite:1});

            Common.render({tmpl: $('#select_result2_temp'), data: rep.selectresult2, context: '#select_result2',overwrite:1});

            Common.render({tmpl: $('#roomkebiao_temp'), data: rep.roomkebiao, context: '#roomkebiao',overwrite:1});


            $("#title").html(rep.paikeciname+"  排课报告");
            // 设置图
            setEcharts(rep);
        });
    }
    function getgrade(){
        var xqid = $("body").attr("ciId");
        if(xqid != undefined && xqid != null && xqid != ""){
            Common.getData('/new33isolateMange/getGradList.do', {"xqid":xqid}, function (rep) {
                Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade',overwrite:1});
            })
        }
    }












    /**************************************echarts*******************************************************/
    function setEcharts(rep){
        setZhengti1(rep.zhengti1);
        setZhengti2(rep.zhengti2);
        setZouban3(rep.zouban3);
        setShiwu(rep.shiwu)
    }

    function setZhengti1(zhengti1){
        var myChart = echarts.init(document.getElementById('main1'));
        var xData = [];
        function filter(type){
            if(type=="等级考"||type=="合格考"){
                return true;
            }
            return false;
        }
        for(var i in zhengti1){
            if(filter(zhengti1[i].type)){
                xData.push(zhengti1[i].subname+"("+zhengti1[i].type+")")
            }

        }
        var jxbnum = [];
        for(var i in zhengti1){
            if(filter(zhengti1[i].type)) {
                jxbnum.push(zhengti1[i].jxbnum);
            }
        }
        var zongks = [];
        for(var i in zhengti1){
            if(filter(zhengti1[i].type)) {
                zongks.push(zhengti1[i].zongks)
            }
        }
        var zhengti1_option = {
            title : {
                text: ""
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['教学班数量','总课时']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            dataZoom : {
                show : true,
                realtime : true,
                start : 0,
                end : 100
            },
            xAxis : [
                {
                    type : 'category',
                    data : xData,
                    axisLabel: {
                        rotate: 0,
                        interval: 0
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'教学班数量',
                    type:'bar',
//                barWidth : 40,
                    itemStyle: {
                        normal:{
                            color: 'RGB(46,199,201)'
                        }
                    },
                    data:jxbnum,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    }
                },
                {
                    name:'总课时',
                    type:'bar',
//                barWidth : 40,
                    itemStyle: {
                        normal:{
                            color: 'RGB(182,162,222)'
                        }
                    },
                    data:zongks,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name : '平均值'}
                        ]
                    }
                }
            ]
        };
        myChart.setOption(zhengti1_option);
    }

    function setZhengti2(zhengti2) {
        var myChart = echarts.init(document.getElementById('main2'));
        var jxbnum = [zhengti2.feizoubannum,zhengti2.zoubannum,zhengti2.zixinum]
        var zongks = [zhengti2.feizoubanhour,zhengti2.zoubanhour,zhengti2.zixihour];
        var zhengti2_option = {
            title : {
                text: ""
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['教学班数量','课时总数']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            dataZoom : {
                show : true,
                realtime : true,
                start : 0,
                end : 100
            },
            xAxis : [
                {
                    type : 'category',
                    data : ["非走班","走班","自习课"],
                    axisLabel: {
                        rotate: 0,
                        interval: 0
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'教学班数量',
                    type:'bar',
//                barWidth : 40,
                    itemStyle: {
                        normal:{
                            color: 'RGB(46,199,201)'
                        }
                    },
                    data:jxbnum,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    }
                },
                {
                    name:'课时总数',
                    type:'bar',
//                barWidth : 40,
                    itemStyle: {
                        normal:{
                            color: 'RGB(182,162,222)'
                        }
                    },
                    data:zongks,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name : '平均值'}
                        ]
                    }
                }
            ]
        };
        myChart.setOption(zhengti2_option);
    }

    function setZouban3(zouban3){
        var myChart = echarts.init(document.getElementById('main3'));
        var xData = [];

        for(var i in zouban3){
                xData.push(zouban3[i].subname+"("+zouban3[i].type+")")
        }
        var stunum = [];
        for(var i in zouban3){
                stunum.push(zouban3[i].stunum);
        }
        var maxnum = [];
        for(var i in zouban3){
            maxnum.push(zouban3[i].maxnum)
        }
        var zouban3_option = {
            title : {
                text: ""
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['总人数','班级最多人数']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            dataZoom : {
                show : true,
                realtime : true,
                start : 0,
                end : 100
            },
            xAxis : [
                {
                    type : 'category',
                    data : xData,
                    axisLabel: {
                        rotate: 0,
                        interval: 0
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'总人数',
                    type:'bar',
//                barWidth : 40,
                    itemStyle: {
                        normal:{
                            color: 'RGB(46,199,201)'
                        }
                    },
                    data:stunum,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    }
                },
                {
                    name:'班级最多人数',
                    type:'bar',
//                barWidth : 40,
                    itemStyle: {
                        normal:{
                            color: 'RGB(182,162,222)'
                        }
                    },
                    data:maxnum,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name : '平均值'}
                        ]
                    }
                }
            ]
        };
        myChart.setOption(zouban3_option);
    }

    function setShiwu(content) {
        var myChart = echarts.init(document.getElementById('main4'));
        var xData = [];

        for(var i in content){
            xData.push(content[i].subname)
        }
        var teanum = [];
        for(var i in content){
            teanum.push(content[i].teanum);
        }
        var swnum = [];
        for(var i in content){
            swnum.push(content[i].swnum)
        }
        var option = {
            title : {
                text: ""
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['教师人数','事务数量']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            dataZoom : {
                show : true,
                realtime : true,
                start : 0,
                end : 100
            },
            xAxis : [
                {
                    type : 'category',
                    data : ["非走班","走班","自习课"],
                    axisLabel: {
                        rotate: 0,
                        interval: 0
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'教师人数',
                    type:'bar',
//                barWidth : 40,
                    itemStyle: {
                        normal:{
                            color: 'RGB(46,199,201)'
                        }
                    },
                    data:teanum,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    }
                },
                {
                    name:'事务数量',
                    type:'bar',
//                barWidth : 40,
                    itemStyle: {
                        normal:{
                            color: 'RGB(182,162,222)'
                        }
                    },
                    data:swnum,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name : '平均值'}
                        ]
                    }
                }
            ]
        };
        myChart.setOption(option);
    }




    module.exports = paikereport;
})