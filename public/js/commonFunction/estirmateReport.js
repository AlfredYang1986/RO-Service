jQuery(document).ready(function(){
    var d = JSON.stringify({
        "token" : $.session.get('token')//$.cookie("token")
    });
    $.ajax({
        contentType: "application/json,charset=utf-8",
        type: "POST",
        url: "/common/estirmate/report",
        data: d,
        success: function (data) {
            var red_fonts = data.result.red_fonts;
            init_redFonts(red_fonts);
            var tables = data.result.tables;
            init_table(tables);
            var echarts = data.result.echarts;
            init_echarts(echarts);
        }
    });
});

function init_redFonts(red_fonts) {
    $("span[name='product_name']").html(red_fonts.product_name);
    $("#user_name").html(red_fonts.user_name);
    $("#create_date").html(red_fonts.create_date_str);

    $("#productFewYears").html(red_fonts.product_few_years);
    $("#productMatureStage").html(red_fonts.product_mature_stage);
    $("#otcFlag").html(red_fonts.otc_flag);
    $("#classification").html(red_fonts.category);
    $("#ta").html(red_fonts.treatment_area);
    $("#competition").html(red_fonts.competition_level);

    $("span[name='startyear']").html(red_fonts.start_year);
    $("#totalSales").html(red_fonts.total_sales);
    $("span[name='endyear']").html(red_fonts.end_year);
    $("#salesGrowth").html(red_fonts.sales_growth);//FIXME 没有值，原因是数据库也没有
    $("#totalSalesAdj").html(red_fonts.total_sales_adj);
    $("#carryOverRange").html(red_fonts.carry_over_range);


    $("span[name='channelName1']").html(red_fonts.channel_name1);
    $("#channelName1Cost").html(red_fonts.channel_name1_cost);
    $('span[name="channelName2"]').html(red_fonts.channel_name2);
    $("#channelName2Cost").html(red_fonts.channel_name2_cost);
    $('span[name="channelName3"]').html(red_fonts.channel_name3);
    $("#channelName3Cost").html(red_fonts.channel_name3_cost);
    $("#channelNameOther").html(red_fonts.channel_name_other);

    $("#channelName2investPercentage").html(red_fonts.channel_name2_invest_percentage);
    $("#channelName2roi").html(red_fonts.channel_name2_roi);
    $("#channelName3investPercentage").html(red_fonts.channel_name3_invest_percentage);
    $("#channelName3roi").html(red_fonts.channel_name3_roi);
    $("#channelName2gainRate").html(red_fonts.channel_name2_gain_rate);
    $("#channelName3gainRate").html(red_fonts.channel_name3_gain_rate);
    $("#channelName2finalRetention").html(red_fonts.channel_name2_final_retention);
    $("#channelName3finalRetention").html(red_fonts.channel_name3_final_retention);

    $('span[name="carryOver"]').html(red_fonts.carry_over);
    $('#total_sales_adj_carry_over').html(red_fonts.total_sales_adj_carry_over);

    $('#month').html(red_fonts.month);
    $('#rSqure').html(red_fonts.rSqure);
    $('#carry_overtotalsalesadj').html(red_fonts.carry_over_total_sales_adj);
    $('#carryOver1').html(red_fonts.carry_over1);
    $('#channelName71Contribution').html(red_fonts.channel_name71_contribution);
    $('#channelName72').html(red_fonts.channel_name72);
    $('#channelName72Contribution').html(red_fonts.channel_name72_contribution);
    $('#channelName7ContributionOther').html(red_fonts.channel_name7_contribution_other);


    $("span[name='channelName81']").html(red_fonts.channel_name81)
    $("span[name='finalRetention81']").html(red_fonts.final_retention81)
    $("span[name='channelName82']").html(red_fonts.channel_name82)
    $("span[name='finalRetention82']").html(red_fonts.final_retention82)
    $("span[name='channelName83']").html(red_fonts.channel_name83)
    $("span[name='finalRetention83']").html(red_fonts.final_retention83)
    $("#month81").html(red_fonts.month81);
    $("#month82").html(red_fonts.month82);

    $("#channelName91").html(red_fonts.channel_name91);
    $("#roi91").html(red_fonts.roi91);
    $("#mroi911").html(red_fonts.mroi911);
    $("#mroi912").html(red_fonts.mroi912);
    $("#mroi913").html(red_fonts.mroi913);

    $("#channelName101").html(red_fonts.channel_name101);
    $("#roi101").html(red_fonts.roi101);
    $("#mroi1011").html(red_fonts.mroi1011);
    $("#mroi1012").html(red_fonts.mroi1012);
    $("#mroi1013").html(red_fonts.mroi1013);

    $("#channelName111").html(red_fonts.channel_name111);
    $("#roi111").html(red_fonts.roi111);
    $("#mroi1111").html(red_fonts.mroi1111);
    $("#mroi1112").html(red_fonts.mroi1112);
    $("#mroi1113").html(red_fonts.mroi1113);

    $("#channelName121").html(red_fonts.channel_name121);
    $("#roi1121").html(red_fonts.roi121);
    $("#mroi1211").html(red_fonts.mroi1211);
    $("#mroi1212").html(red_fonts.mroi1212);
    $("#mroi1213").html(red_fonts.mroi1213);
}

function init_table(tables){
    init_channel_table(tables.channel);
    init_contribution_table(tables.contribution);
    init_roi_table(tables.channel);
}

function init_channel_table(channel_table){
    var htmlsb = "";
    for (var i in channel_table) {
        htmlsb += "<tr>";
        htmlsb += "<td width=134>";
        htmlsb += "<p align=center>";
        htmlsb += "<span>"+ channel_table[i].channel_name_en +"</span>";
        htmlsb += "</p>";
        htmlsb += "</td>";
        htmlsb += "</tr>";
    }
    $('tbody[id="channel_table"]').html(htmlsb);
}

function init_contribution_table(contribution_table){
    htmlsb = "";
    for (var i in contribution_table) {
        htmlsb += "<tr>";
        htmlsb += "<td width=134>";
        htmlsb += "<p>";
        htmlsb += "<span>"+ contribution_table[i].channel_name_en +"</span>";
        htmlsb += "</p>";
        htmlsb += "</td>";
        htmlsb += "<td width=136>";
        htmlsb += "<p align=right>";
        htmlsb += "<span>"+ parseFloat(contribution_table[i].current_cost*100).toFixed(2) +"%</span>";
        htmlsb += "</p>";
        htmlsb += "</td>";
        htmlsb += "<td width=136>";
        htmlsb += "<p align=right>";
        htmlsb += "<span>"+ parseFloat(contribution_table[i].current_contribution*100).toFixed(2) +"%</span>";
        htmlsb += "</p>";
        htmlsb += "</td>";
        htmlsb += "</tr>";
    }
    $('tbody[id="contribution_table"]').html(htmlsb);
}

function init_roi_table(roi_table){
    htmlsb = "";
    for (var i in roi_table) {
        htmlsb += "<tr>";
        htmlsb += "<td width=134>";
        htmlsb += "<p>";
        htmlsb += "<span>"+ roi_table[i].channel_name_en +"</span>";
        htmlsb += "</p>";
        htmlsb += "</td>";
        htmlsb += "<td width=136>";
        htmlsb += "<p align=right>";
        htmlsb += "<span>"+ roi_table[i].roi +"</span>";
        htmlsb += "</p>";
        htmlsb += "</td>";
        htmlsb += "</tr>";
    }
    $('tbody[id="roi_table"]').html(htmlsb);
}

function init_echarts(echarts){
    init_pie_echart(echarts.pie);
    init_line1_echart(echarts.line1);
    init_line2_echart(echarts.line2);
    init_bar_echart(echarts.bar);
    init_curve1_echart(echarts.curve1);
    init_curve2_echart(echarts.curve2);
    init_curve3_echart(echarts.curve3);
    init_curve4_echart(echarts.curve4);
}

var pie, line1, line2, bar, logarithm1, logarithm2, logarithm3, logarithm4;

function init_pie_echart(pie_echart){
    $("#pie").empty();
    pie = echarts.init(document.getElementById('pie'), "shine");
    pie.setOption({
        tooltip: {trigger: 'item', formatter: "{a}，{d}%"},
        legend: {orient: 'vertical', left: 'right', data: pie_echart.seriesdata},
        series: [{
            name: '访问来源',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: pie_echart.seriesdata,
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }]
    })
}

function init_line1_echart(line1_echart){
    $("#line1").empty();
    line1 = echarts.init(document.getElementById('line1'), "shine");
    line1.setOption({
        tooltip: {trigger: 'axis'},
        grid: {left: '2%', right: '2%', containLabel: true},
        xAxis: [{
            type: 'category',
            name: '年月',
            nameLocation: 'middle',
            nameGap: 25,
            boundaryGap: false,
            data: line1_echart.xAxisdata
        }],
        yAxis: [{
            type: 'value',
            name: '费用',
            axisLabel: {formatter: '{value}'},
            splitLine: {show: false},
            splitNumber: 5
        }],
        series: line1_echart.seriesdata
    });
}

function init_line2_echart(line2_echart){
    $("#line2").empty();
    line2 = echarts.init(document.getElementById('line2'), "shine");
    line2.setOption({
        tooltip: {trigger: 'axis'},
        xAxis: {
            type: 'category',
            name: '月份',
            nameLocation: 'middle',
            nameGap: 25,
            splitLine: {show: false},
            data: line2_echart.xAxisdata
        },
        grid: {left: '2%', right: '2%', containLabel: true},
        yAxis: [{
            type: 'value',
            name: '残留量',
            axisLabel: {formatter: '{value}'},
            splitLine: {show: false},
            splitNumber: 5
        }],
        series: line2_echart.seriesdata
    });
}

function init_bar_echart(bar_echart){
    $("#bar").empty();
    bar = echarts.init(document.getElementById('bar'), "shine");
    bar.setOption({
        color: ['#3398DB'],
        tooltip: {
            trigger: 'item', formatter: function (obj) {
                console.info(obj);
                return obj.seriesName + '：' + Number(obj.value).toFixed(2);
            }
        },
        xAxis: [{
            type: 'category',
            data: bar_echart.xAxisdata,
            axisLabel: {interval: 0, rotate: 45, margin: 2, textStyle: {color: "#222"}}
        }],
        grid: {left: '2%', right: '2%', containLabel: true, x: 40, x2: 100, y2: 150},
        yAxis: [{type: 'value', nameLocation: 'middle', splitLine: {show: false}}],
        series: [{
            name: 'ROI', type: 'bar', barWidth: '60%', data: bar_echart.seriesdata, itemStyle: {
                normal: {
                    label: {
                        show: true, position: 'top', formatter: function (obj) {
                            return Number(obj.value).toFixed(2)
                        }
                    }
                }
            }
        }]
    });
}

function init_curve1_echart(curve1){
    $("#logarithm1").empty();
    logarithm1 = echarts.init(document.getElementById('logarithm1'), "macarons");
    logarithm1.setOption({
        tooltip: {trigger: 'axis'},
        legend: {data: ['投入', '产出']},
        xAxis: {
            type: 'category',
            name: '费用(元)',
            nameLocation: 'middle',
            nameGap: 25,
            splitLine: {show: false},
            data: curve1.xAxisdata
        },
        grid: {left: '2%', right: '2%', containLabel: true},
        yAxis: [{type: 'value', name: '销量(元)', axisLabel: {formatter: '{value}'}, splitNumber: 10}],
        series: [{name: '投入', type: 'line', data: curve1.seriesdata1},
            {name: '产出', type: 'line', data: curve1.seriesdata2}]
    });
}

function init_curve2_echart(curve2) {
    $("#logarithm2").empty();
    logarithm2 = echarts.init(document.getElementById('logarithm2'), "macarons");
    logarithm2.setOption({
        tooltip: {trigger: 'axis'},
        legend: {data: ['投入', '产出']},
        xAxis: {
            type: 'category',
            name: '费用(元)',
            nameLocation: 'middle',
            nameGap: 25,
            splitLine: {show: false},
            data: curve2.xAxisdata
        },
        grid: {left: '2%', right: '2%', containLabel: true},
        yAxis: [{type: 'value', name: '销量(元)', axisLabel: {formatter: '{value}'}, splitNumber: 10}],
        series: [{name: '投入', type: 'line', data: curve2.seriesdata1},
            {name: '产出', type: 'line', data: curve2.seriesdata2}]
    });
}

function init_curve3_echart(curve3) {
    $("#logarithm3").empty();
    logarithm3 = echarts.init(document.getElementById('logarithm3'), "macarons");
    logarithm3.setOption({
        tooltip: {trigger: 'axis'},
        legend: {data: ['投入', '产出']},
        xAxis: {
            type: 'category',
            name: '费用(元)',
            nameLocation: 'middle',
            nameGap: 25,
            splitLine: {show: false},
            data: curve3.xAxisdata
        },
        grid: {left: '2%', right: '2%', containLabel: true},
        yAxis: [{type: 'value', name: '销量(元)', axisLabel: {formatter: '{value}'}, splitNumber: 10}],
        series: [{name: '投入', type: 'line', data: curve3.seriesdata1},
            {name: '产出', type: 'line', data: curve3.seriesdata2}]
    });
}

function init_curve4_echart(curve4) {
    $("#logarithm4").empty();
    logarithm4 = echarts.init(document.getElementById('logarithm4'), "macarons");
    logarithm4.setOption({
        tooltip: {trigger: 'axis'},
        legend: {data: ['投入', '产出']},
        xAxis: {
            type: 'category',
            name: '费用(元)',
            nameLocation: 'middle',
            nameGap: 25,
            splitLine: {show: false},
            data: curve4.xAxisdata
        },
        grid: {left: '2%', right: '2%', containLabel: true},
        yAxis: [{type: 'value', name: '销量(元)', axisLabel: {formatter: '{value}'}, splitNumber: 10}],
        series: [{name: '投入', type: 'line', data: curve4.seriesdata1},
            {name: '产出', type: 'line', data: curve4.seriesdata2}]
    });
}

/***
 * 设置自动调整Size
 */
window.addEventListener("resize", function () {
    pie.resize();
    line1.resize();
    line2.resize();
    bar.resize();
    logarithm1.resize();
    logarithm2.resize();
    logarithm3.resize();
    logarithm4.resize();
});
