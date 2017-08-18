/**
 * Created by liwei on 2017/7/27.
 */
$(function () {
    pageResult(1);
});

function pageResult(page){
    try{
        var dataMap = JSON.stringify({
            "token" : $.session.get("token")
        });

        AjaxData("/company/page", dataMap, "POST", function (data) {
            if(data.status === "ok"){
                var result = data.result;
                PetPage(result.result,result.page,"companyManage")
            }else{
                throw new PetException('company list request failed", "message');
            }
        }, function (e) {
            throw new PetException(e.name);
        });
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
}

var QueryCompany = function (id) {
    try {
        $("#companymanage-form").modal('show');
        $('#company_form')[0].reset();
        if(id !== ""){
            var dataMap = JSON.stringify({
                "company_id" : id
            });

            AjaxData("/company/query", dataMap, "POST", function (data) {
                if(data.status === "ok"){
                    var company = data.result.company;
                    $('#company_id').val(company.company_id);
                    $('#company_name_en').val(company.company_name_en);
                    $('#company_name_ch').val(company.company_name_ch);
                    $('#company_photo').val(company.company_photo);
                    $('#short_name').val(company.short_name);
                    $('#date').val(company.date)
                }else{
                    throw new PetException('the company information request failed');
                }
            }, function (e) {
                throw new PetException(e.name);
            });
        }
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var SaveCompany = function () {
    try {
        CompanyVerification()
        var company_id = $('#company_id').val();
        var url = "/company/push";
        if(company_id !== ""){
            url = "/company/update"
        }
        var dataMap = JSON.stringify($('#company_form').serializeJSON());
        AjaxData(url, dataMap, "POST", function (data) {
            if(data.status === "ok"){
                $("#companymanage-form").modal('hide');
                $.tooltip("提交成功",2500,true);
                pageResult(1)
            }else{
                throw new PetException('company information submission failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        })
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var RemoveCompany = function (id) {
    try {
        var dataMap = JSON.stringify({
            "company_id" : id
        });

        $.dialog('confirm','提示','您确认要删除么？',0,function(){
            AjaxData("/company/drop", dataMap, "POST", function (data) {
                if(data.status === "ok"){
                    $.tooltip("操作成功",2500,true);
                    pageResult(1)
                }else{
                    throw new PetException('company information deletion failed');
                }
                $.closeDialog();
            }, function (e) {
                throw new PetException(e.name);
            });
        });
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};