/**
 * Created by liwei on 2017/7/19.
 */
var AjaxData = function(url, data, type, successfun, errorfun) {
    $.ajax({
        type: type,
        url: url,
        dataType: "json",
        cache: false,
        data: data,
        contentType: "application/json,charset=utf-8",
        success: function (data) {
            successfun(data)
        },
        error: function (e) {
            errorfun(e)
        }
    });
}
