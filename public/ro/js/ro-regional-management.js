/**
 * Created by liwei on 2017/8/21.
 */
layui.use('table', function(){
    var table = layui.table;
});

$(function () {
    var dataMap = JSON.stringify({});
    AjaxData("/regionalManagement/list", dataMap, "POST", function (data) {
        console.info(data);
    }, function (err) {
        console.log(err.name);
    })
});