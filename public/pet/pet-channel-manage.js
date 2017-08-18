/**
 * Created by liwei on 2017/7/27.
 */
$(function () {
    pageResult(1);
});

var pageResult = function (currentPage) {
    try {
        var dataMap = JSON.stringify({
            "token" : $.session.get("token"),
            "currentPage" : currentPage,
            "company": getCompanyVal(),
            "department": getDepartmentVal(),
            "scope": getProductVal()
        });

        AjaxData("/channel/page", dataMap, "POST", function (data) {
            if(data.status === "ok"){
                var result = data.result;
                PetPage(result.result,result.page,"channelManage");
            }else{
                throw new PetException('channel list request failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        });
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var QueryChannel = function (id) {
    try {
        $("#channelmanage-form").modal('show');
        $('#channel_form')[0].reset();

        var object = {};
        if($.cookie("level") !== '0')
            object['company_name_en'] = $.cookie("company");
        if(id !== '')
            object['channel_id'] = id;
        var dataMap = JSON.stringify(object);
        AjaxData("/channel/query", dataMap, "POST", function (data) {
            if(data.status === "ok"){
                var company = data.result.company;
                resetCompanySelect(company);
                if(id !== ''){
                    var channel = data.result.channel;
                    $('#channel_id').val(channel.channel_id);
                    $('#channel_ch').val(channel.channel_ch);
                    $('#channel_en').val(channel.channel_en);
                    $('#channel_description').val(channel.channel_description);
                    $('#date').val(channel.date);
                    defaultSelected('company',channel.company);
                    changeCompany(channel,true)
                }else{
                    defaultSelected('company',company[0].company_name_en);
                    changeCompany('',true)
                }
            }else{
                throw new PetException('the channel information request failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        });
    } catch (err) {
        PetAlert(PetException(err.name));
    }
};

var SaveChannel = function () {
    try {
        ChannelVerification()
        var channel_id = $('#channel_id').val();
        var url = "/channel/push";
        if(channel_id !== ""){
            url = "/channel/update";
        }
        var dataMap = JSON.stringify($('#channel_form').serializeJSON());
        AjaxData(url, dataMap, "POST", function (data) {
            if(data.status === "ok"){
                $("#channelmanage-form").modal('hide');
                $.tooltip("提交成功",2500,true);
                pageResult(1);
            }else{
                throw new PetException('channel information submission failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        });
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var RemoveChannel = function (id) {
    try {
        var dataMap = JSON.stringify({
            "channel_id" : id
        });

        $.dialog('confirm','提示','您确认要删除么？',0,function(){
            AjaxData("/channel/drop", dataMap, "POST", function (data) {
                if(data.status === "ok"){
                    $.tooltip("操作成功",2500,true);
                    pageResult(1);
                }else{
                    throw new PetException('channel information deletion failed');
                }
                $.closeDialog();
            }, function (e) {
                throw new PetException(e.name);
            });
        })
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};
