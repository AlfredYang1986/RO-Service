/**
 * Created by liwei on 2017/8/18.
 */
$(function () {
    document.onkeydown = function (e) {
        var theEvent = window.event || e;
        var code = theEvent.keyCode || theEvent.which;
        if (code === 13) {
            $("#sub").click();
        }
    };

    $("#sub").click(function(){
        window.location="/ro/businessIntroduction"
    });
});