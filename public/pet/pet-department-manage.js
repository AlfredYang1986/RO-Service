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
            "currentPage": currentPage,
            "company": getCompanyVal()
        });

        if($.cookie("level") !== '0'){
            dataMap = JSON.stringify({
                "token" : $.session.get("token"),
                "currentPage": currentPage,
                "company" : $.cookie("company")
            })
        }

        AjaxData("/department/page", dataMap, "POST", function (data) {
            if(data.status === "ok"){
                var result = data.result;
                PetPage(result.result,result.page,"departmentManage")
            }else{
                throw new PetException('department list request failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        });
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var QueryDepartment = function (id) {
    try {
        $("#departmentmanage-form").modal('show');
        $('#department_form')[0].reset();

        var object = new Object();
        if($.cookie("level") !== '0')
            object['company_name_en'] = $.cookie("company");
        if(id !== '')
            object['department_id'] = id;

        var dataMap = JSON.stringify(object);

        AjaxData("/department/query", dataMap, "POST", function (data) {
            if(data.status === "ok"){
                var department = data.result.department;
                var company = data.result.company;
                if(id !== ''){
                    $('#department_id').val(department.department_id);
                    $('#department_name').val(department.department_name);
                    $('#short_name').val(department.short_name);

                    defaultSelected('is_admin',department.is_admin);

                    $('#date').val(department.date);
                    resetCompanySelect(company);
                    defaultSelected('company',department.company)

                }else{
                    resetCompanySelect(company)
                }
            }else{
                throw new PetException('the department information request failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        })
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var SaveDepartment = function () {
    try {
        DepartmentVerification()
        var department_id = $('#department_id').val();
        var url = "/department/push";
        if(department_id !== ""){
            url = "/department/update"
        }
        var dataMap = JSON.stringify($('#department_form').serializeJSON());
        AjaxData(url, dataMap, "POST", function (data) {
            if(data.status === "ok"){
                $("#departmentmanage-form").modal('hide');
                $.tooltip("提交成功",2500,true);
                pageResult(1);
            }else{
                throw new PetException('department information submission failed');
            }
        }, function (e) {
            throw new PetException(e.name);
        });
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
};

var RemoveDepartment = function (id) {
    try {
        var dataMap = JSON.stringify({
            "department_id" : id
        });

        $.dialog('confirm','提示','您确认要删除么？',0,function(){
            AjaxData("/department/drop", dataMap, "POST", function (data) {
                if(data.status === "ok"){
                    $.tooltip("操作成功",2500,true);
                    pageResult(1);
                }else{
                    throw new PetException('department information deletion failed');
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