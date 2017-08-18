/**
 * Created by liwei on 2017/8/4.
 */

/**
 * 重置下拉框并赋值
 * @param id    组件ID
 * @param lst   结果集
 */
var resetSelect = function (id,lst) {
    $("#"+id+" option").remove();
    for(var i in lst) {
        $("#"+id+"").append("<option>"+lst[i]+"</option>");
    }
}

/**
 * 默认记忆选中
 * @param id    组件ID
 * @param arg   选中项
 */
var defaultSelected = function (id,arg) {
    $("#"+id+" option").each(function () {
        if ($(this).text() == arg) {
            $(this).attr('selected', true);
        }
    });
}

/**
 * 重置公司下拉框并赋值
 * @param company   公司结果集
 */
var resetCompanySelect = function (companys) {
    $("#company option").remove();
    for(var i in companys) {
        $("#company").append("<option>"+companys[i].company_name_en+"</option>");
    }
}

/**
 * 重置部门下拉框并赋值
 * @param department
 */
var resetDepartmentSelect = function (departments) {
    $("#department option").remove();
    for(var i in departments) {
        $("#department").append("<option>"+departments[i].department_name+"</option>");
    }
}

/**
 * 重置产品下拉框并赋值
 * @param scope
 */
var resetScopeSelect = function (scopes) {
    $("#scope option").remove();
    for(var i in scopes) {
        $("#scope").append("<option>"+scopes[i].scope_name_ch+"</option>");
    }
}

/**
 * 公司下拉框Change事件
 * @param obj
 * @param flag      是否需要添加部门下拉框change事件
 */
var changeCompany = function (obj,flag) {
    try {
        var dataMap = JSON.stringify({
            "company" : $("#company").find("option:selected").text()
        });

        AjaxData("/department/lst", dataMap, "POST", function (data) {
            if(data.status == "ok"){
                var result = data.result.department;
                resetDepartmentSelect(result);
                if(obj != ''){
                    defaultSelected('department',obj.department);
                    if(flag){
                        changeDepartment(obj);
                    }
                }else{
                    defaultSelected('department',result[0].department_name);
                    if(flag){
                        changeDepartment('');
                    }
                }
            }else{
                console.info("出错啦");
            }
        }, function (e) { console.info(e.message) });
    } catch (err) {
        $.tooltip(err.message);
    }
}

/**
 * 部门下拉框Change事件
 * @param object
 */
var changeDepartment = function (obj) {
    try {
        var dataMap = JSON.stringify({
            "company" : $("#company").find("option:selected").text(),
            "department" : $("#department").find("option:selected").text()
        })

        AjaxData("/product/lst", dataMap, "POST", function (data) {
            if(data.status == "ok"){
                var result = data.result.product;
                resetScopeSelect(result);

                if(obj != ''){
                    defaultSelected('scope',obj.scope);
                }else{
                    defaultSelected('scope',result[0].scope_name_ch);
                }
            }else{
                console.info("出错啦");
            }
        }, function (e) { console.info(e.message) });
    } catch (err) {
        $.tooltip(err.message)
    }
}