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
            "department": getDepartmentVal()
        });

        AjaxData("/product/page", dataMap, "POST", function (data) {
            if(data.status === "ok"){
                var result = data.result;
                PetPage(result.result,result.page,"productManage");
            }else{
                throw new PetException('product list request failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        });
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var QueryProduct = function (id) {
    try {
        $("#productmanage-form").modal('show');
        $('#product_form')[0].reset();

        var object = {};
        if($.cookie("level") !== '0')
            object['company_name_en'] = $.cookie("company");
        if(id !== '')
            object['scope_id'] = id;
        var dataMap = JSON.stringify(object);

        AjaxData("/product/query", dataMap, "POST", function (data) {
            if(data.status === "ok"){
                var product = data.result.product;
                var company = data.result.company;
                resetCompanySelect(company);
                if(id !== ''){
                    $('#scope_id').val(product.scope_id);
                    $('#scope_name_ch').val(product.scope_name_ch);
                    $('#scope_name_en').val(product.scope_name_en);
                    $('#date').val(product.date);

                    defaultSelected('company',product.company);
                    changeCompany(product,false);
                }else{
                    changeCompany('',false);
                }
            }else{
                throw new PetException('the product information request failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        })

    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var SaveProduct = function () {
    try {
        ProductVerification()
        var scope_id = $('#scope_id').val();
        var url = "/product/push";
        if(scope_id !== ""){
            url = "/product/update"
        }
        var dataMap = JSON.stringify($('#product_form').serializeJSON());
        AjaxData(url, dataMap, "POST", function (data) {
            if(data.status === "ok"){
                $("#productmanage-form").modal('hide');
                $.tooltip("提交成功",2500,true);
                pageResult(1);
            }else{
                throw new PetException('product information submission failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        })
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var RemoveProduct = function (id) {
    try {
        var dataMap = JSON.stringify({
            "scope_id" : id
        });

        $.dialog('confirm','提示','您确认要删除么？',0,function(){
            AjaxData("/product/drop", dataMap, "POST", function (data) {
                if(data.status === "ok"){
                    $.tooltip("操作成功",2500,true);
                    pageResult(1);
                }else{
                    throw new PetException('product information deletion failed');
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