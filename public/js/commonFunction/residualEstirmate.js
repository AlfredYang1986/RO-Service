/**
 * Created by clock on 2017/7/1.
 */

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
    //加载数据
    loadTable();
});

//提交并验证表单
function pushNaturalSales() {
    var productName = $("#productName").val();
    var channelName = $("#channelName").val();
    var gainRate = $("#gainRate").val();

    if (productName == '') {
        $.tooltip('产品名称还没填呢...');
        productName.focus();
        return false;
    } else if (channelName == '') {
        $.tooltip('渠道名称还没填呢...');
        channelName.focus();
        return false;
    } else if (gainRate == '') {
        $.tooltip('信息传达率还没填呢...');
        gainRate.focus();
        return false;
    } else {//submit
        var d = JSON.stringify({
            "token" : $.session.get('token'),
            "productName": productName,
            "channelName": channelName,
            "channelEffectiveScope": $("#channelEffectiveScope").val(),
            "competition": $("#competition").val(),
            "gainRate": gainRate,
            "channelInvestPercentage": $("#channelInvestPercentage").val(),
            "channelEffect": $("#channelEffect").val(),
            "investFloor": $("#investFloor").val(),
            "investCeiling": $("#investCeiling").val(),
        });

        $.ajax({
            contentType: "application/json,charset=utf-8",
            dataType: "json",
            type: "POST",
            data: d,
            url: "/common/residual/push",
            success: function (result) {
                if (result.status == "error") {
                    $.tooltip('残留量录入失败');
                } else {
                    $.tooltip("残留量录入成功", 2000, true, function () {
                        $.closeDialog(function () {
                            window.location = "/common/residualEstirmate"
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
        data: d,
        url: "/common/residual/lst",
        success: function(data) {
            if(data.status == "error"){
                $.tooltip('数据获取失败');
            }else{
                var result = data.result.result
                var htmlsb = "";
                for (var i in result) {
                    htmlsb += "<tr class='odd gradeX'>";
                        htmlsb += "<td class='center'>" + result[i].productName + "</td>";
                        htmlsb += "<td class='center'>" + result[i].channelName + "</td>";
                        htmlsb += "<td class='center'>" + result[i].channelEffectiveScope + "</td>";
                        htmlsb += "<td class='center'>" + result[i].competition + "</td>";
                        htmlsb += "<td class='center'>" + result[i].gainRate + "</td>";
                        htmlsb += "<td class='center'>" + result[i].channelInvestPercentage + "</td>";
                        htmlsb += "<td class='center'>" + result[i].channelEffect + "</td>";
                        htmlsb += "<td class='center'>" + result[i].investFloor + "</td>";
                        htmlsb += "<td class='center'>" + result[i].investCeiling + "</td>";
                        htmlsb += "<td class='center'>" + L2Date(result[i].createDate) + "</td>";
                        htmlsb += "<td class='center'>";
                            htmlsb += "<a href=\"javascript:editBut(\'"+ result[i].estimateId +"\');\"><i class=\"fa fa-edit\"></i></a>";
                            htmlsb += "<a href=\"javascript:delObject(\'"+ result[i].estimateId +"\');\" class=\"delete-row\"><i class=\"fa fa-trash-o\"></i></a>";
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
        "estimateId" : id
    });

    $.ajax({
        contentType: "application/json,charset=utf-8",
        type: "POST",
        data: d,
        url: "/common/residual/lst",
        success: function (data) {
            if (data.status == "error") {
                $.tooltip('数据获取失败');
            } else {
                var result = data.result.result
                if(result.length == 1){
                    $("#productName").val(result[0].productName);
                    $("#channelName").val(result[0].channelName);
                    $("#channelEffectiveScope").val(result[0].channelEffectiveScope);
                    $("#competition").val(result[0].competition);
                    $("#gainRate").val(result[0].gainRate);
                    $("#channelInvestPercentage").val(result[0].channelInvestPercentage);
                    $("#channelEffect").val(result[0].channelEffect);
                    $("#investFloor").val(result[0].investFloor);
                    $("#investCeiling").val(result[0].investCeiling);
                    $("#createDate").val(result[0].createDate);
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
    var channelName = $("#channelName").val();
    var gainRate = $("#gainRate").val();

    if (productName == '') {
        $.tooltip('产品名称还没填呢...');
        productName.focus();
        return false;
    } else if (channelName == '') {
        $.tooltip('渠道名称还没填呢...');
        channelName.focus();
        return false;
    } else if (gainRate == '') {
        $.tooltip('信息传达率还没填呢...');
        gainRate.focus();
        return false;
    } else {//submit
        var d = JSON.stringify({
            "token" : $.session.get('token'),
            "estimateId" : id,
            "productName": productName,
            "channelName": channelName,
            "channelEffectiveScope": $("#channelEffectiveScope").val(),
            "competition": $("#competition").val(),
            "gainRate": gainRate,
            "channelInvestPercentage": $("#channelInvestPercentage").val(),
            "channelEffect": $("#channelEffect").val(),
            "investFloor": $("#investFloor").val(),
            "investCeiling": $("#investCeiling").val(),
            "createDate": parseInt($("#createDate").val())
        });

        $.ajax({
            contentType: "application/json,charset=utf-8",
            dataType: "json",
            type: "POST",
            data: d,
            url: "/common/residual/update",
            success: function (result) {
                if (result.status == "error") {
                    $.tooltip('数据修改失败');
                } else {
                    $.tooltip("数据修改成功", 2000, true, function () {
                        $.closeDialog(function () {
                            window.location = "/common/residualEstirmate"
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
            "estimateId" : id
        });

        $.ajax({
            contentType: "application/json,charset=utf-8",
            dataType: "json",
            type: "POST",
            data: d,
            url: "/common/residual/drop",
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
                            window.location = "/common/residualEstirmate"
                        });
                    });
                }
            }
        })
    });
}

