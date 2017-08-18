/**
 * Created by clock on 2017/6/28.
 */

//自定义日期格式
laydate({
    elem: '#launchDate',
    format: 'YYYY-MM-DD',
    festival: true, //显示节日
});

//切换tab动画
function resetTabs() {
    $("#content > div").hide(); //Hide all content
    $("#tabs a").attr("id", ""); //Reset id's
}

var myUrl = window.location.href; //get URL
var myUrlTab = myUrl.substring(myUrl.indexOf("#")); // For localhost/tabs.html#tab2, myUrlTab = #tab2
var myUrlTabName = myUrlTab.substring(0, 4); // For the above example, myUrlTabName = #tab

(function () {
    $("#content > div").hide(); // Initially hide all content
    $("#tabs li:first a").attr("id", "current"); // Activate first tab
    $("#content > div:first").fadeIn(); // Show first tab content
    $("#tabs a").on("click", function (e) {
        e.preventDefault();
        if ($(this).attr("id") == "current") { //detection for current tab
            return
        }
        else {
            resetTabs();
            $(this).attr("id", "current"); // Activate this
            $($(this).attr('name')).fadeIn(); // Show content for current tab
        }
    });

    for (i = 1; i <= $("#tabs li").length; i++) {
        if (myUrlTab == myUrlTabName + i) {
            resetTabs();
            $("a[name='" + myUrlTab + "']").attr("id", "current"); // Activate url tab
            $(myUrlTab).fadeIn(); // Show url tab content
        }
    }
})()

jQuery(document).ready(function () {
    jQuery('#table1').dataTable({"sPaginationType": "full_numbers"});
    laydate.skin('molv');//切换皮肤，请查看skins下面皮肤库
    //加载数据
    loadTable();
});

//提交并验证表单
function pushNaturalSales() {
    var productName = $("#productName").val();
    var launchDate = $("#launchDate").val();
    var treatmentArea = $("#treatmentArea").val();

    if (productName == '') {
        $.tooltip('产品名称还没填呢...');
        productName.focus();
        return false;
    } else if (launchDate == '') {
        $.tooltip('产品上市时间还没填呢...');
        launchDate.focus();
        return false;
    } else if (treatmentArea == '') {
        $.tooltip('产品属于哪个治疗领域还没填呢...');
        treatmentArea.focus();
        return false;
    } else {//submit
        var d = JSON.stringify({
            "token" : $.session.get('token'),
            "product_name": productName,
            "launch_date": launchDate,
            "otc_flag": $("#otcFlag").val(),
            "category": $("#category").val(),
            "indication": $("#indication").val(),
            "treatment_area": $("#treatmentArea").val(),
            "life_cycle": $("#lifeCycle").val(),
            "competition_level": $("#competitionLevel").val(),
            "last_year_sales_growth_rate": $("#lastYearSalesGrowthRate").val()
        });

        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/common/naturalSales/push",
            data: d,
            contentType: "application/json,charset=utf-8",
            success: function (result) {
                if (result.status == "error") {
                    $.tooltip('自然销量录入失败');
                } else {
                    $.tooltip("自然销量录入成功", 2000, true, function () {
                        $.closeDialog(function () {
                            window.location = "/common/naturalSalesEstirmate"
                        });
                    });
                }
            }
        })
    }
}

function loadTable(){
    var skip = arguments[0] ? arguments[0] : 0;
    var take = arguments[1] ? arguments[1] : 20;
    var d = JSON.stringify({
        "token" : $.session.get('token'),//$.cookie("token")
        "skip" : skip,
        "take" : take
    });

    $.ajax({
        contentType: "application/json,charset=utf-8",
        type: "POST",
        url: "/common/naturalSales/lst",
        data: d,
        success: function(data) {
            if(data.status == "error"){
                $.tooltip('数据获取失败');
            }else{
                var result = data.result.result
                var htmlsb = "";
                for (var i in result) {
                    htmlsb += "<tr class='odd gradeX'>";
                        htmlsb += "<td class='center'>" + result[i].product_name + "</td>";
                        htmlsb += "<td class='center'>" + result[i].launch_date + "</td>";
                        htmlsb += "<td class='center'>" + result[i].otc_flag + "</td>";
                        htmlsb += "<td class='center'>" + result[i].category + "</td>";
                        htmlsb += "<td class='center'>" + result[i].indication + "</td>";
                        htmlsb += "<td class='center'>" + result[i].treatment_area + "</td>";
                        htmlsb += "<td class='center'>" + result[i].life_cycle + "</td>";
                        htmlsb += "<td class='center'>" + result[i].competition_level + "</td>";
                        htmlsb += "<td class='center'>" + result[i].last_year_sales_growth_rate + "</td>";
                        htmlsb += "<td class='center'>" + L2Date(result[i].create_date) + "</td>";
                        htmlsb += "<td class='center'>";
                            htmlsb += "<a href=\"javascript:editBut(\'"+ result[i].estimate_id +"\');\"><i class=\"fa fa-edit\"></i></a>";
                            htmlsb += "<a href=\"javascript:delObject(\'"+ result[i].estimate_id +"\');\" class=\"delete-row\"><i class=\"fa fa-trash-o\"></i></a>";
                        htmlsb += "</td>";
                    htmlsb += "</tr>";
                }
                $('tbody[id="tbody"]').html(htmlsb);
            }
        }
    })
}

function L2Date(l){
    return new Date(parseInt(l)).toLocaleString().substr(0,9);
}

function editBut(id){
    var d = JSON.stringify({
        "token" : $.session.get('token'),//$.cookie("token")
        "estimate_id" : id
    });

    $.ajax({
        type: "POST",
        url: "/common/naturalSales/lst",
        data: d,
        contentType: "application/json,charset=utf-8",
        success: function (data) {
            if (data.status == "error") {
                $.tooltip('数据获取失败');
            } else {
                var result = data.result.result
                if(result.length == 1){
                    $("#productName").val(result[0].product_name);
                    $("#launchDate").val(result[0].launch_date);
                    $("#otcFlag").val(result[0].otc_flag);
                    $("#category").val(result[0].category);
                    $("#indication").val(result[0].indication);
                    $("#treatmentArea").val(result[0].treatment_area);
                    $("#lifeCycle").val(result[0].life_cycle);
                    $("#competitionLevel").val(result[0].competition_level);
                    $("#lastYearSalesGrowthRate").val(result[0].last_year_sales_growth_rate);
                    $("#createDate").val(result[0].create_date);
                    $("#submitBtn").attr("onclick","editObject('"+ id +"')");
                    $("#tabs a").eq(0).click();
                }else{
                    $.tooltip('数据获取出错');
                }
            }
        }
    });
}

function editObject(id) {
    var productName = $("#productName").val();
    var launchDate = $("#launchDate").val();
    var treatmentArea = $("#treatmentArea").val();

    if (productName == '') {
        $.tooltip('产品名称还没填呢...');
        productName.focus();
        return false;
    } else if (launchDate == '') {
        $.tooltip('产品上市时间还没填呢...');
        launchDate.focus();
        return false;
    } else if (treatmentArea == '') {
        $.tooltip('产品属于哪个治疗领域还没填呢...');
        treatmentArea.focus();
        return false;
    } else {//submit
        var d = JSON.stringify({
            "token" : $.session.get('token'),
            "estimate_id" : id,
            "product_name": productName,
            "launch_date": launchDate,
            "otc_flag": $("#otcFlag").val(),
            "category": $("#category").val(),
            "indication": $("#indication").val(),
            "treatment_area": $("#treatmentArea").val(),
            "life_cycle": $("#lifeCycle").val(),
            "competition_level": $("#competitionLevel").val(),
            "last_year_sales_growth_rate": $("#lastYearSalesGrowthRate").val(),
            "create_date": parseInt($("#createDate").val())
        });

        $.ajax({
            contentType: "application/json,charset=utf-8",
            dataType: "json",
            type: "POST",
            data: d,
            url: "/common/naturalSales/update",
            success: function (result) {
                if (result.status == "error") {
                    $.tooltip('数据修改失败');
                } else {
                    $.tooltip("数据修改成功", 2000, true, function () {
                        $.closeDialog(function () {
                            window.location = "/common/naturalSalesEstirmate"
                        });
                    });
                }
            }
        });
    }
}

function delObject(id) {
    $.dialog('confirm', '提示', '您确认要删除么？', 0, function () {
        var d = JSON.stringify({
            "token" : $.session.get('token'),//$.cookie("token")
            "estimate_id" : id
        });

        $.ajax({
            contentType: "application/json,charset=utf-8",
            dataType: "json",
            type: "POST",
            data: d,
            url: "/common/naturalSales/drop",
            success: function (result) {
                if (result.status == "error") {
                    $.tooltip(result.statusMsg, 2000, true, function () {
                        $.closeDialog(function () {
                        });
                    });
                    return false;
                } else {
                    $.tooltip("数据删除成功", 2000, true, function () {
                        $.closeDialog(function () {
                            window.location = "/common/naturalSalesEstirmate"
                        });
                    });
                }
            }
        })
    });
}

