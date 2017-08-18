/**
 * Created by liwei on 2017/7/17.
 */
$(function(){
    querylist()
});

var querylist = function () {
    try {
        var optimizaOpt = $.cookie("optimizaOpt")

        var scope_name_ch = $("#form_productOpt").find("option:selected").text()
        var times = $("#form_times").find("option:selected").text()

        var dataMap = JSON.stringify({
            "token" : $.session.get('token'),
            "scope_name_ch" : scope_name_ch,
            "optimizaOpt" : optimizaOpt,
            "times" : times
        });

        if(optimizaOpt == "固定预算"){
            AjaxData("/astellas/resultsComparison/listBudgetOpt", dataMap, "POST", function(data){
                if (data.status == "error") {
                    throw new PetException('fixed budget result comparison request failed');
                } else {
                    createComponent(data)
                }
            }, function(e){
                throw new PetException(e.name);
            });
        }else if(optimizaOpt == "固定收益"){
            AjaxData("/astellas/resultsComparison/listRevenueOpt", dataMap, "POST", function(data){
                if (data.status == "error") {
                    throw new PetException('fixed income result comparison request failed');
                } else {
                    createComponent(data)
                }
            }, function(e){
                throw new PetException(e.name);
            });
        }else{
            AjaxData("/astellas/resultsComparison/listProfitOpt", dataMap, "POST", function(data){
                if (data.status == "error") {
                    throw new PetException('the profit maximization result comparison request failed');
                } else {
                    createComponent(data)
                }
            }, function(e){
                throw new PetException(e.ame);
            });
        }
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

var createComponent = function (data) {
    var result = data.result
    resetSelect('form_productOpt',result.products)
    defaultSelected('form_productOpt',result.scope_name_ch)
    resetSelect('form_times',result.times_lst)
    defaultSelected('form_times',result.times)
    echarts1_func(result.channels)
    echarts2_func(result.channels)
}

var echarts1_func = function (result) {
    var xAxis_data = []
    var series_data1 = []
    var series_data2 = []
    var echarts1title = ""
    var optimizaOpt = $.cookie("optimizaOpt")
    if(optimizaOpt == "利润最大化"){
        xAxis_data = ["当前利润", "增量", "优化利润"]
        var series_data11 = parseInt(result[0].max_profit) - parseInt(result[0].current_profit)
        series_data1 = [series_data11,parseInt(result[0].current_profit),0]
        series_data2 = [parseInt(result[0].current_profit),series_data11,parseInt(result[0].max_profit)]
        echarts1title = "当前利润 Vs 优化利润"
    }else{
        xAxis_data = ["当前收入", "增量", "优化收入"]
        var series_data11 = parseInt(result[0].opt_revenue_prod) - parseInt(result[0].current_revenue_prod)
        series_data1 = [series_data11,parseInt(result[0].current_revenue_prod),0]
        series_data2 = [parseInt(result[0].current_revenue_prod),series_data11,parseInt(result[0].opt_revenue_prod)]
        echarts1title = "当前收入 Vs 优化收入"
    }

    var echarts1 = echarts.init(document.getElementById('echarts1'));
    echarts1.showLoading({
        text : '数据获取中',
        effect: 'whirling'
    });
    echarts1.hideLoading();

    var option1 = {
        title: {
            text: echarts1title,
            left: 'center'
        },
        tooltip : {
            trigger: 'axis',
                axisPointer : {             // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'             // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter: function (params) {
                var tar = params[1];
                return tar.name + '<br/>' + tar.seriesName + ' : ' + thousandBitSeparator(tar.value);
            }
        },
        grid: {
            left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
        },
        xAxis: {
            name: '类型',
            type : 'category',
                splitLine: {show:false},
            data : xAxis_data
        },
        yAxis: {
            name:'金额',
            type : 'value'
        },
        series: [
            {
                name: '辅助',
                type: 'bar',
                stack:  '总金额',
                barWidth: '30%',
                itemStyle: {
                    normal: {
                        barBorderColor: 'rgba(0,0,0,0)',
                        color: 'rgba(0,0,0,0)'
                    },
                    emphasis: {
                        barBorderColor: 'rgba(0,0,0,0)',
                        color: 'rgba(0,0,0,0)'
                    }
                },
                data: series_data1
            },
            {
                name: "金额",
                type: 'bar',
                stack: '总金额',
                label: {
                    normal: {
                        show: true,
                        position: 'inside'
                    }
                },
                barWidth: '30%',
                itemStyle: {
                    normal: {
                        color: {
                            type: 'linear',
                            x: 0,
                            y: 0,
                            x2: 0,
                            y2: 1,
                            colorStops: [{
                                offset: 0, color: 'rgba(14,125,218,0.8)'
                            }, {
                                offset: 1, color: 'rgba(14,125,218,0.3)'
                            }],
                            globalCoord: false
                        }
                    }
                },
                data: series_data2
            }
        ]
    };
    echarts1.setOption(option1);
    window.addEventListener("resize", function() {
        echarts1.resize();
    });
}

var echarts2_func = function (result) {
    console.info(result)
    var xAxis_data = []
    var series_data1 = []
    var series_data2 = []
    for(var i in result){
        xAxis_data.push(result[i].channel_name_ch)
        series_data1.push(parseInt(result[i].channel_current_cost))
        series_data2.push(parseInt(result[i].channel_opt_cost))
    }
    var echarts2 = echarts.init(document.getElementById('echarts2'));
    echarts2.showLoading({
        text : '数据获取中',
        effect: 'whirling'
    });
    echarts2.hideLoading();
    var option = {
        title : {
            text: '当前成本花费 Vs 当前优化成本花费',
            //subtext: '该产品所有渠道',
            left: 'center'
        },
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            orient: 'vertical',      // 布局方式，默认为水平布局，可选为： 'horizontal' ¦ 'vertical'
            x: 'right',               // 水平安放位置，默认为全图居中，可选为：
            // 'center' ¦ 'left' ¦ 'right'
            // ¦ {number}（x坐标，单位px）
            y: 'center',                  // 垂直安放位置，默认为全图顶端，可选为：
            data:['当前成本花费','当前优化成本花费']
        },
        toolbox: {
            show : true,
            feature : {
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                name : '渠道',
                type : 'category',
                data : xAxis_data
            }
        ],
        yAxis : [
            {
                name : '金额',
                type : 'value',
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                }
            }
        ],
        series : [
            {
                name:'当前成本花费',
                type:'bar',
                data:series_data1,
                itemStyle: {
                    normal: {
                        color: '#26C0C0',
                        lineStyle : {
                            width:2,
                            type:'solid'  //'dotted'虚线 'solid'实线
                        }
                    }
                }
            },
            {
                name:'当前优化成本花费',
                type:'bar',
                data:series_data2,
                itemStyle: {
                    normal: {
                        color: '#F0805A',
                        lineStyle : {
                            width:2,
                            type:'solid'  //'dotted'虚线 'solid'实线
                        }
                    }
                }
            }
        ]
    }
    echarts2.setOption(option);
    window.addEventListener("resize", function() {
        echarts2.resize();
    });
}


var exportFile = function () {
    try {
        var optimizaOpt = $.cookie("optimizaOpt")

        var scope_name_ch = $("#form_productOpt").find("option:selected").text()
        var times = $("#form_times").find("option:selected").text()

        var dataMap = JSON.stringify({
            "token" : $.session.get('token'),
            "scope_name_ch" : scope_name_ch,
            "times" : times
        });

        if(optimizaOpt == "固定预算"){
            AjaxData("/astellas/resultsComparison/exportBudgetOpt", dataMap, "POST", function(data){
                if (data.status == "error") {
                    throw new PetException('fixed budget results comparison information export failed');
                } else {
                    location.href = "/common/downloadFile/"+ data.result.result
                }
            }, function(e){ throw  new PetException(e.name) });
        }else if(optimizaOpt == "固定收益"){
            AjaxData("/astellas/resultsComparison/exportRevenueOpt", dataMap, "POST", function(data){
                if (data.status == "error") {
                    throw new PetException('fixed income results comparison information export failed');
                } else {
                    location.href = "/common/downloadFile/"+ data.result.result
                }
            }, function(e){ throw  new PetException(e.name) });
        }else{
            AjaxData("/astellas/resultsComparison/exportProfitOpt", dataMap, "POST", function(data){
                if (data.status == "error") {
                    throw new PetException('profit maximization results comparison information export failure');
                } else {
                    location.href = "/common/downloadFile/"+ data.result.result
                }
            }, function(e){ throw  new PetException(e.name) });
        }
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}