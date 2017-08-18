/***
 * 设置自动调整Size
 */
window.addEventListener("resize", function () {
    scatter.resize();
});

//绘制图表
(function () {
    $.ajax({
        contentType: "application/json,charset=utf-8",
        dataType: "json",
        type: "POST",
        data: JSON.stringify({
            "token" : $.session.get('token')
        }),
        url: "/common/optimized/report",//要访问的后台地址
        success: function (data) {
            $("#productName").html(data.result.product_name);
            $("#userName").html(data.result.user_name);
            $("#createDate").html(data.result.create_date);
            $("#optimizationType").html(data.result.opt_type);
            $("#uplift").html(data.result.uplift);
            $("#scatter").empty();
            var scatterJson = eval("(" + data.result.scatter + ")");
            scatter = echarts.init(document.getElementById('scatter'), "roma");
            scatter.setOption({
                grid: {left: '2%', right: '2%', containLabel: true},
                tooltip: {
                    padding: 10,
                    backgroundColor: '#222',
                    borderColor: '#777',
                    borderWidth: 1,
                    formatter: function (obj) {
                        var value = obj.value;
                        return '<div style="border-bottom: 1px solid rgba(255,255,255,.3); font-size: 18px;padding-bottom: 7px;margin-bottom: 7px">'
                            + value[2] + '  ' + value[3] + '</div>'
                            + '销售额(RMB)：' + value[0] + '<br>'
                            + '总推广预算(RMB)：' + value[1] + '<br>';
                    }
                },
                xAxis: [{
                    name: '总推广预算(RMB)',
                    nameLocation: 'middle',
                    nameGap: 25,
                    gridIndex: 0,
                    min: 0,
                    max: scatterJson.maxx
                }],
                yAxis: [{name: '销售额(RMB)', gridIndex: 0, min: 0, max: scatterJson.maxy}],
                series: [
                    {
                        name: 'I',
                        type: 'scatter',
                        xAxisIndex: 0,
                        yAxisIndex: 0,
                        data: scatterJson.data,
                        markLine: {
                            animation: false,
                            label: {normal: {formatter: '最佳投入产出组合线', textStyle: {align: 'right'}}},
                            lineStyle: {normal: {type: 'solid'}},
                            tooltip: {formatter: '最佳投入产出组合线'},
                            data: [[{
                                coord: [scatterJson.minx, scatterJson.miny],
                                symbol: 'none'
                            }, {coord: [scatterJson.maxx, scatterJson.maxy], symbol: 'none'}]]
                        }
                    }
                ]
            });
            var table = data.result.table;
            var htmlsb = "";
            for (var i in table) {
                htmlsb += "<tr>";
                    htmlsb += "<td width=134>";
                        htmlsb += "<p>";
                            htmlsb += "<span>"+ table[i].channel_name_en +"</span>";
                        htmlsb += "</p>";
                    htmlsb += "</td>";

                    htmlsb += "<td width=136>";
                        htmlsb += "<p align=right>";
                            htmlsb += "<span lang=EN-US>"+ table[i].current_cost +"%</span>";
                        htmlsb += "</p>";
                    htmlsb += "</td>";

                    htmlsb += "<td width=136>";
                        htmlsb += "<p align=right>";
                            htmlsb += "<span lang=EN-US>"+ table[i].current_contribution +"%</span>";
                        htmlsb += "</p>";
                    htmlsb += "</td>";

                    htmlsb += "<td width=136>";
                        htmlsb += "<p align=right>";
                            htmlsb += "<span lang=EN-US>"+ table[i].opt_cost +"%</span>";
                        htmlsb += "</p>";
                    htmlsb += "</td>";

                    htmlsb += "<td width=136>";
                        htmlsb += "<p align=right>";
                            htmlsb += "<span lang=EN-US maxFractionDigits='2'>"+ table[i].opt_contribution +"%</span>";
                        htmlsb += "</p>";
                    htmlsb += "</td>";

                htmlsb += "</tr>";
            }

            $('tbody[id="tbody"]').html(htmlsb);
        }
    });
})()