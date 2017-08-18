/**
 * Created by clock on 2017/7/4.
 */
var loader = new SVGLoader( document.getElementById( 'loader' ), { speedIn : 300, easingIn : mina.easeinout } );
jQuery(document).ready(function(){
    $('#analysisBtn').click(function() {
        var d = JSON.stringify({
            "token" : $.session.get('token')
        });

        loader.show();
        $.ajax({
            type: "POST",
            dataType: "json",
            data: d,
            url : "/common/estirmate/analysis",//要访问的后台地址
            contentType: "application/json,charset=utf-8",
            success : function(result) {
                if(result.status == "SUCCESS"){
                    $.tooltip(result.statusMsg,2000,true,function(){
                        $.closeDialog(function(){
                            loader.hide();
                            location = "/common/estirmateAnalysis";
                        });
                    });
                }else{
                    $.tooltip(result.statusMsg,2000,true,function(){
                        $.closeDialog(function(){
                            loader.hide();
                        });
                    });
                    return false;
                }
            }
        });
    });
});
