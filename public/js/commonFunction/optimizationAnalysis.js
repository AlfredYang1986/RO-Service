/**
 * Created by clock on 2017/7/4.
 */
var loader = new SVGLoader(document.getElementById('loader'), {speedIn: 300, easingIn: mina.easeinout});

jQuery(document).ready(function () {
    $("#loading").hide();
    jQuery('#tabl1e').dataTable({"sPaginationType": "full_numbers"});
    initTable();
});

function initTable() {
    var d = JSON.stringify({
        "token": $.session.get('token')
    });

    $.ajax({
        contentType: "application/json,charset=utf-8",
        type: "POST",
        dataType: "json",
        data: d,
        url: "/common/optimized/loadOptimization",//要访问的后台地址
        success: function (data) {
            if (data.status == "error") {
                $.tooltip('数据获取失败');
            } else {
                var result = data.result.result
                var htmlsb = "";
                for (var i in result) {
                    htmlsb += "<tr class='odd gradeX'>";
                    htmlsb += "<td style='display: none;'>" + result[i].analysis_id + "</td>";
                    htmlsb += "<td class='center'>" + result[i].channel_name_en + "</td>";
                    htmlsb += "<td class='center'>" + result[i].opt_flag + "</td>";
                    htmlsb += "<td class='center'>" + result[i].min_spend + "</td>";
                    htmlsb += "<td class='center'>" + result[i].max_spend + "</td>";
                    htmlsb += "<td class='center'>" + result[i].marginal_cost + "</td>";
                    htmlsb += "<td style='display: none;'>" + result[i].create_date + "</td>";
                    htmlsb += "<td style='display: none;'>" + result[i].status + "</td>";
                    htmlsb += "<td style='display: none;'>" + result[i].user_name + "</td>";
                    htmlsb += "<td style='display: none;'>" + result[i].product_name + "</td>";
                    htmlsb += "<td class='center'>";
                    htmlsb += "<a href=\"javascript:void(0);\" onclick=\"editBut(this)\"><i class=\"fa fa-gear\"></i></a>";
                    htmlsb += "</td>";
                    htmlsb += "</tr>";
                }
                $('tbody[id="tbody"]').html(htmlsb);
            }
        },
        error: function(){
            $.tooltip('网络异常');
        }
    });
}

function changeOptimizationType() {
    var optimizationType = $("#optimizationType").val()
    if (optimizationType == "Fixed Budget") {
        $("#generalBudgetDiv").show();
        $("#targetReturnDiv").hide();
    } else if (optimizationType == "Target Revenue") {
        $("#generalBudgetDiv").hide();
        $("#targetReturnDiv").show();
    } else {
        $("#generalBudgetDiv").hide();
        $("#targetReturnDiv").hide();
    }
}

function changeTab(type) {
    if (type == 1) {
        $('#myTab a:first').tab('show');//初始化显示哪个tab
        $('#myTab a').click(function (e) {
            e.preventDefault();//阻止a链接的跳转行为
            $(this).tab('show');//显示当前选中的链接及关联的content
        })
    } else {
        $('#myTab a:last').tab('show');//初始化显示哪个tab
        $('#myTab a').click(function (e) {
            e.preventDefault();//阻止a链接的跳转行为
            $(this).tab('show');//显示当前选中的链接及关联的content
        })
    }
}


function editBut(obj) {
    var tds = $(obj).parent().parent().find("td");
    $("#analysisId").val(tds.eq(0).text());
    $("#channelName").val(tds.eq(1).text());
    $("#optFlag").val(tds.eq(2).text());
    $("#minSpend").val(tds.eq(3).text());
    $("#maxSpend").val(tds.eq(4).text());
    $("#marginalCost").val(tds.eq(5).text());
    $("#createDate").val(tds.eq(6).text());
    $("#status").val(tds.eq(7).text());
    $("#userName").val(tds.eq(8).text());
    $("#productName").val(tds.eq(9).text());
    $("#update").modal("show");
}


function editObject() {
    var analysisId = $("#analysisId").val();
    var optFlag = $("#optFlag").val();
    var minSpend = $("#minSpend").val();
    var maxSpend = $("#maxSpend").val();
    var marginalCost = $("#marginalCost").val();

    if (minSpend == '') {
        $.tooltip('最小花费还没填呢...');
        minSpend.focus();
        return false;
    } else if (maxSpend == '') {
        $.tooltip('最大花费还没填呢...');
        maxSpend.focus();
        return false;
    } else if (marginalCost == '') {
        $.tooltip('单价还没填呢...');
        marginalCost.focus();
        return false;
    } else {//submit
        var d = JSON.stringify({
            "token" : $.session.get('token'),
            "analysis_id" : analysisId,
            "opt_flag": optFlag,
            "min_spend": Number(minSpend),
            "max_spend": Number(maxSpend),
            "marginal_cost": Number(marginalCost),
            "create_date": Number($("#createDate").val()),
            "channel_name_en": $("#channelName").val(),
            "status": Number($("#status").val()),
            "product_name": $("#productName").val(),
            "user_name": $("#userName").val()
        });

        $.ajax({
            contentType: "application/json,charset=utf-8",
            dataType: "json",
            type: "POST",
            data: d,
            url: "/common/optimized/update",
            success: function (result) {
                if (result.status == "error") {
                    $.tooltip('数据修改失败');
                } else {
                    $.tooltip("数据修改成功", 2000, true, function () {
                        $.closeDialog(function () {
                            window.location = "/common/optimizedAnalysis"
                        });
                    });
                }
            }
        });
    }
}

function startAnalysis() {
    var data = {};
    data['token'] = $.session.get('token');
    var optimizationType = $('#optimizationType').val();
    data['optimizationType'] = optimizationType;
    var generalBudget = $('#generalBudget').val();
    var mainChannelNumber = $('#mainChannelNumber').val();
    var targetReturn = $('#targetReturn').val();

    if(optimizationType == "Fixed Budget"){
        if (generalBudget == '') {
            $.tooltip('总渠道还没填呢...');
            $('#generalBudget').focus();
            return false;
        } else if (mainChannelNumber == '') {
            $.tooltip('重要渠道数量还没填呢...');
            $('#mainChannelNumber').focus();
            return false;
        }
        data['generalBudget'] = Number(generalBudget);
        data['mainChannelNumber'] = Number(mainChannelNumber);
    }else if(optimizationType == "Target Revenue"){
        if (targetReturn == '') {
            $.tooltip('目标收益还没填呢...');
            $('#targetReturn').focus();
            return false;
        } else if (mainChannelNumber == '') {
            $.tooltip('重要渠道数量还没填呢...');
            $('#mainChannelNumber').focus();
            return false;
        }
        data['targetReturn'] = Number(targetReturn)
        data['mainChannelNumber'] = Number(mainChannelNumber);
    }else{
        if (mainChannelNumber == '') {
            $.tooltip('重要渠道数量还没填呢...');
            $('#mainChannelNumber').focus();
            return false;
        }
        data['mainChannelNumber'] = Number(mainChannelNumber);
    }

    var baseScalingFactor = $('#baseScalingFactor').val();
    var priceperUnit = $('#priceperUnit').val();
    var grossMargin = $('#grossMargin').val();

    if (baseScalingFactor == '') {
        $.tooltip('调整因子还没填呢...');
        $('#baseScalingFactor').focus();
        return false;
    }else if (priceperUnit == '') {
        $.tooltip('药品单价还没填呢...');
        $('#priceperUnit').focus();
        return false;
    }else if (grossMargin == '') {
        $.tooltip('毛利率还没填呢...');
        $('#grossMargin').focus();
        return false;
    }

    data['baseScalingFactor'] = Number(baseScalingFactor);
    data['priceperUnit'] = Number(priceperUnit);
    data['grossMargin'] = Number(grossMargin);

    loader.show();

    $.ajax({
        contentType: "application/json,charset=utf-8",
        dataType: "json",
        type: "POST",
        data: JSON.stringify(data),
        url: "/common/optimized/analysis",
        success: function (result) {
            if (result.status == "SUCCESS") {
                $.tooltip(result.statusMsg, 2000, true, function () {
                    $.closeDialog(function () {
                        loader.hide();
                        location = "/common/optimizedAnalysis";
                    });
                });
            } else {
                $.tooltip(result.statusMsg, 2000, true, function () {
                    $.closeDialog(function () {
                        loader.hide();
                    });
                });
                return false;
            }
        }
    });
}




