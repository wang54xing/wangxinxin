/**
 * Created by fulaan on 2018/6/8.
 */
// 基于准备好的dom，初始化echarts实例
$(function(){
    var myChart = echarts.init(document.getElementById('main1'));

    var option = {
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
                data : ['英语','语文','数学','政治','历史','地理','生物','化学','物理','语文','数学','政治']
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
                data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
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
                data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
                markPoint : {
                    data : [
                        {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183, symbolSize:18},
                        {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
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
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
})