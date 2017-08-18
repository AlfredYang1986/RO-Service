/**
 * Created by liwei on 2017/7/25.
 */
$(function () {
    pageResult(1);
});

var pageResult = function (currentPage) {
    try {
        var dataMap = JSON.stringify({
            "token": $.session.get("token"),
            "currentPage": currentPage,
            "company": getCompanyVal(),
            "department": getDepartmentVal()
        });

        if($.cookie("level") !== '0'){
            dataMap = JSON.stringify({
                "token" : $.session.get("token"),
                "currentPage": currentPage,
                "company" : $.cookie("company"),
                "department": getDepartmentVal()
            });
        }

        AjaxData("/auth/page", dataMap, "POST", function (data) {
            if(data.status === "ok"){
                var result = data.result;
                PetPage(result.result,result.page,"userManage")
            }else{
                throw new PetException('user list request failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        });
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var UserManageModal = function (id) {
    try {

        $("#usermanage-form").modal('show');
        $('#user_form')[0].reset();

        var object = {};
        object['token'] = $.session.get('token');
        if ($.cookie("level") !== '0')
            object['company_name_en'] = $.cookie("company");
        if (id !== '')
            object['user_id'] = id;

        var dataMap = JSON.stringify(object);

        AjaxData("/auth/queryUser", dataMap, "POST", function (data) {
            if (data.status === "ok") {
                var user = data.result.user;
                var company = data.result.company;
                resetCompanySelect(company);
                $('input[id=user_name]').removeAttr("readonly");
                if (id !== '') {
                    $('input[id=user_name]').attr("readonly", "readonly");
                    $('#user_id').val(user.user_id);
                    $('#screen_photo').val(user.screen_photo);
                    $('#date').val(user.date);
                    $('#user_name').val(user.user_name);
                    $('#pwd').val(user.pwd);
                    $('#screen_name').val(user.screen_name);
                    $('#phoneNo').val(user.phoneNo);
                    $('#email').val(user.email);

                    defaultSelected('company',user.company);
                    changeCompany(user,false);
                }else{
                    defaultSelected('company',company[0].company_name_en);
                    changeCompany('',false);
                }
            } else {
                throw new PetException('the user information request failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        })
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var SaveUser = function () {
    try {
        UserVerification();
        var object = $('#user_form').serializeJSON();
        object['token'] = $.session.get('token');
        var dataMap = JSON.stringify(object);

        var url = "/auth/pushUser";
        var user_id = $('#user_id').val();
        if(user_id !== ""){
            url = "/auth/update";
        }
        AjaxData(url, dataMap, "POST", function(data){
            if(data.status === "ok"){
                pageResult(1);
                $("#usermanage-form").modal('hide');
                $.tooltip('提交成功', 2500, true);
            }else{
                throw new PetException("user information submission failed");
            }
        }, function(e){
            throw new PetException(e.name);
        });
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var RemoveUser = function (id) {
    try {
        var dataMap = JSON.stringify({
            "token": $.session.get('token'),
            "user_id": id
        });

        $.dialog('confirm','提示','您确认要删除么？',0,function(){
            AjaxData("/auth/dropUser", dataMap, "POST", function(data){
                console.info(data);
                if(data.status === "ok"){
                    pageResult(1);
                    $.tooltip('操作成功', 2500, true);
                }else{
                    throw new PetException("user information deletion failed");
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