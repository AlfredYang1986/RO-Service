/**
 * Created by liwei on 2017/6/22.
 */
$(function () {
    querylist()
})

var querylist = function () {
    try {
        $("#form1").hide()
        $("#form2").hide()
        $("#form3").hide()
        var optimizaOpt = $.cookie("optimizaOpt")
        var scope_name_ch = ""
        if(optimizaOpt == "固定预算"){
            scope_name_ch = $("#budget_productOpt").find("option:selected").text()
        }else if(optimizaOpt == "固定收益"){
            scope_name_ch = $("#revenue_productOpt").find("option:selected").text()
        }else{
            scope_name_ch = $("#profit_productOpt").find("option:selected").text()
        }
        var dataMap = JSON.stringify({
            "token" : $.session.get('token'),
            "scope_name_ch" : scope_name_ch,
            "optimizaOpt" : optimizaOpt
        });

        if(optimizaOpt == "固定预算"){
            AjaxData("/astellas/resultsReport/listBudgetOpt", dataMap, "POST", function(data){
                if (data.status == "error") {
                    listBudgetOpt([])
                    listBudgetOpt_Top([])
                } else {
                    var result = data.result.channels.values
                    listBudgetOpt(result)
                    listBudgetOpt_Top(result)

                    resetSelect('budget_productOpt', data.result.products);
                    defaultSelected('budget_productOpt', data.result.scope_name_ch);
                }
            }, function(e){
                throw new PetAlert('fixed budget results report failed');
            });
        }else if(optimizaOpt == "固定收益"){
            AjaxData("/astellas/resultsReport/listRevenueOpt", dataMap, "POST", function(data){
                if (data.status == "error") {
                    listRevenueOpt([])
                    listRevenueOpt_Top([])
                } else {
                    var result = data.result.channels.values
                    listRevenueOpt(result)
                    listRevenueOpt_Top(result)

                    resetSelect('revenue_productOpt', data.result.products);
                    defaultSelected('revenue_productOpt', data.result.scope_name_ch);
                }
            }, function(e){
                throw new PetAlert('fixed Revenue Report Report Request failed');
            });
        }else{
            AjaxData("/astellas/resultsReport/listProfitOpt", dataMap, "POST", function(data){
                if (data.status == "error") {
                    listProfitOpt([])
                    listProfitOpt_Top([])
                } else {
                    var result = data.result.channels.values
                    listProfitOpt(result)
                    listProfitOpt_Top(result)


                    resetSelect('profit_productOpt', data.result.products);
                    defaultSelected('profit_productOpt', data.result.scope_name_ch);
                }
            }, function(e){
                throw new PetAlert('profit Maximization Report request failed');
            });
        }
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

/**
 * 固定预算 Table
 * @param result
 */
var listBudgetOpt = function (result) {
    var htmlsb = "";
    htmlsb += "<thead>"
    htmlsb += "<tr>"
    htmlsb += "<th>#</th>"
    htmlsb += "<th>渠道名称</th>"
    htmlsb += "<th>渠道说明</th>"
    htmlsb += "<th>当期成本花费</th>"
    htmlsb += "<th>当前成本花费比例</th>"
    htmlsb += "<th>优化成本花费</th>"
    htmlsb += "<th>优化成本花费比例</th>"
    htmlsb += "<th>时间</th>"
    htmlsb += "</tr>"
    htmlsb += "</thead>"
    htmlsb += "<tbody id=\"tbody\">"
    if(result.length != 0){
        $.cookie("times",result[0].times)
        for (var i in result) {
            htmlsb += "<tr>";
            htmlsb += "<td>"+(parseInt(i)+1)+"</td>";
            htmlsb += "<td>"+result[i].channel_name_ch+"</td>";
            htmlsb += "<td>"+result[i].channel_description+"</td>";
            htmlsb += "<td>"+thousandBitSeparatorParseInt(result[i].channel_current_cost)+"</td>";
            htmlsb += "<td>"+doubleToPercentage(result[i].channel_current_cost_ratio)+"</td>";
            htmlsb += "<td>"+thousandBitSeparatorParseInt(result[i].channel_opt_cost)+"</td>";
            htmlsb += "<td>"+doubleToPercentage(result[i].channel_opt_cost_ratio)+"</td>";
            htmlsb += "<td>"+result[i].date+"</td>";
            htmlsb += "</tr>";
        }
    }else{
        $.cookie("times",0)
        htmlsb += "<tr><td>没有匹配的记录</td></tr>";
    }
    htmlsb += "</tbody>"

    $('table[id="table2"]').html(htmlsb);
}

/**
 * 固定收益 Table
 * @param result
 */
var listRevenueOpt = function (result) {
    var htmlsb = "";
    htmlsb += "<thead>"
    htmlsb += "<tr>"
    htmlsb += "<th>#</th>"
    htmlsb += "<th>渠道名称</th>"
    htmlsb += "<th>渠道说明</th>"
    htmlsb += "<th>优化成本花费</th>"
    htmlsb += "<th>优化成本花费比例</th>"
    htmlsb += "<th>时间</th>"
    htmlsb += "</tr>"
    htmlsb += "</thead>"
    htmlsb += "<tbody id=\"tbody\">"
    if(result.length != 0){
        $.cookie("times",result[0].times)
        for (var i in result) {
            htmlsb += "<tr>";
            htmlsb += "<td>"+(parseInt(i)+1)+"</td>";
            htmlsb += "<td>"+result[i].channel_name_ch+"</td>";
            htmlsb += "<td>"+result[i].channel_description+"</td>";
            htmlsb += "<td>"+thousandBitSeparatorParseInt(result[i].channel_opt_cost)+"</td>";
            htmlsb += "<td>"+doubleToPercentage(result[i].channel_opt_cost_ratio)+"</td>";
            htmlsb += "<td>"+result[i].date+"</td>";
            htmlsb += "</tr>";
        }
    }else{
        $.cookie("times",0)
        htmlsb += "<tr><td>没有匹配的记录</td></tr>";
    }
    htmlsb += "</tbody>"

    $('table[id="table2"]').html(htmlsb);
}

/**
 * 利润最大化 Table
 * @param result
 */
var listProfitOpt = function (result) {
    var htmlsb = "";
    htmlsb += "<thead>"
    htmlsb += "<tr>"
    htmlsb += "<th>#</th>"
    htmlsb += "<th>渠道名称</th>"
    htmlsb += "<th>渠道说明</th>"
    htmlsb += "<th>当期成本花费</th>"
    htmlsb += "<th>当前成本花费比例</th>"
    htmlsb += "<th>优化成本花费</th>"
    htmlsb += "<th>优化成本花费比例</th>"
    htmlsb += "<th>时间</th>"
    htmlsb += "</tr>"
    htmlsb += "</thead>"
    htmlsb += "<tbody id=\"tbody\">"
    if(result.length != 0){
        $.cookie("times",result[0].times)
        for (var i in result) {
            htmlsb += "<tr>";
            htmlsb += "<td>"+(parseInt(i)+1)+"</td>";
            htmlsb += "<td>"+result[i].channel_name_ch+"</td>";
            htmlsb += "<td>"+result[i].channel_description+"</td>";
            htmlsb += "<td>"+thousandBitSeparatorParseInt(result[i].channel_current_cost)+"</td>";
            htmlsb += "<td>"+doubleToPercentage(result[i].channel_current_cost_ratio)+"</td>";
            htmlsb += "<td>"+thousandBitSeparatorParseInt(result[i].channel_opt_cost)+"</td>";
            htmlsb += "<td>"+doubleToPercentage(result[i].channel_opt_cost_ratio)+"</td>";
            htmlsb += "<td>"+result[i].date+"</td>";
            htmlsb += "</tr>";
        }
    }else{
        $.cookie("times",0)
        htmlsb += "<tr><td>没有匹配的记录</td></tr>";
    }
    htmlsb += "</tbody>"

    $('table[id="table2"]').html(htmlsb);
}

/**
 * 固定预算 Top
 * @param result
 */
var listBudgetOpt_Top = function (result) {
    $("#form1").show()
    if(result.length != 0){
        $("#budget_current_cost_prod").text(thousandBitSeparatorParseInt(result[0].current_cost_prod))
        $("#budget_current_revenue_prod").text(thousandBitSeparatorParseInt(result[0].current_revenue_prod))
        $("#budget_opt_revenue_prod").text(thousandBitSeparatorParseInt(result[0].opt_revenue_prod))
        $("#budget_revenue_uplift_prod").text(doubleToPercentage(result[0].revenue_uplift_prod))
    }
}

/**
 * 固定收益 Top
 * @param result
 */
var listRevenueOpt_Top = function (result) {
    $("#form2").show()
    if(result.length != 0){
        $("#revenue_target_revenue").text(thousandBitSeparatorParseInt(result[0].target_revenue))
        $("#revenue_opt_revenue_prod").text(thousandBitSeparatorParseInt(result[0].opt_revenue_prod))
        $("#revenue_opt_cost_prod").text(thousandBitSeparatorParseInt(result[0].opt_cost_prod))
    }
}

/**
 * 利润最大化 Top
 * @param result
 */
var listProfitOpt_Top = function (result) {
    $("#form3").show()
    if(result.length != 0){
        $("#profit_current_cost_prod").text(thousandBitSeparatorParseInt(result[0].current_cost_prod))
        $("#profit_current_profit").text(thousandBitSeparatorParseInt(result[0].current_profit))
        $("#profit_max_profit").text(thousandBitSeparatorParseInt(result[0].max_profit))
        $("#profit_profit_uplift_prod").text(doubleToPercentage(result[0].profit_uplift_prod))
    }
}

/**
 * 调R接口，生成报告
 */
var generateReport = function () {
    try {
        var optimizaOpt = $.cookie("optimizaOpt")
        var dataMap = JSON.stringify({
            "token" : $.session.get('token'),
            "optimizaOpt" : $.cookie("optimizaOpt"),
            "times" : parseInt($.cookie("times")),
            "user_name" : $.cookie("user_name")
        });

        AjaxData("/astellas/resultsReport/executerprocess", dataMap, "POST", function(data){
            if (data.result.endsWith('.json')) {
                load_im();
                $('#queueLoding').modal('show');
            } else {
                $('#queueLoding').modal('hide');
                throw new PetException('r run-time error');
            }
        }, function(e){
            throw new PetException(e.name);
        });
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}