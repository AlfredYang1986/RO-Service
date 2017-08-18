/**
 * Created by clock on 2017/8/7.
 */
/**
 * 下拉框通用事件
 */
$(function () {
    initCompanySlt();

    $("#companySlt").change(function () {
        if (getCompanyVal() === null) {
            $("#departmentSlt").empty();
            $("#productSlt").empty();
        } else {
            $("#departmentSlt").empty();
            $("#productSlt").empty();
            changeDepartmentSlt()
        }
        pageResult(1);
    });

    $("#departmentSlt").change(function () {
        if (getDepartmentVal() === null) {
            $("#productSlt").empty();
        } else {
            $("#productSlt").empty();
            changeProductSlt()
        }
        pageResult(1)
    });

    $("#productSlt").change(function () {
        pageResult(1)
    });
});

/**
 * 获取当前页面下拉框的值
 */
var getCompanyVal = function (sltId) {
    sltId = typeof sltId !== 'undefined' ? sltId : "companySlt";
    var company = $("#" + sltId).val();
    if (company === "--请选择--" || company === null)
        return null;
    else
        return company;
};

var getDepartmentVal = function (sltId) {
    sltId = typeof sltId !== 'undefined' ? sltId : "departmentSlt";
    var department = $("#" + sltId).val();
    if (department === "--请选择--" || department === null)
        return null;
    else
        return department;
};

var getProductVal = function (sltId) {
    sltId = typeof sltId !== 'undefined' ? sltId : "productSlt";
    var product = $("#" + sltId).val();
    if (product === "--请选择--" || product === null)
        return null;
    else
        return product;
};

/**
 * 重置下拉框并赋值
 * @param id    组件ID
 * @param lst   结果集
 */
var resetSelectNoDefault = function (id, lst) {
    $("#" + id + " option").remove();
    if (lst === []) {
        $("#" + id).append("<option>--未查询到结果--</option>");
    } else {
        $("#" + id).append("<option>--请选择--</option>");
        for (var i in lst) {
            $("#" + id + "").append("<option>" + lst[i] + "</option>");
        }
    }
};

/**
 * 初始化公司下拉框
 */
var initCompanySlt = function (sltId) {
    try {
        sltId = typeof sltId !== 'undefined' ? sltId : "companySlt";

        var dataMap = JSON.stringify({
            "token": $.session.get("token")
        });

        AjaxData("/company/lst", dataMap, "POST", function (data) {
            if (data.status === "ok") {
                $("#" + sltId + " option").remove();
                var companys = data.result.company;
                for (var i in companys) {
                    companys[i] = companys[i].company_name_en;
                }
                resetSelectNoDefault(sltId, companys);
            } else {
                console.info("出错啦")
            }
        }, function (e) {
            console.info(e.message)
        })
    } catch (err) {
        $.tooltip(err.message)
    }
};

/**
 * 根据公司改变部门下拉框
 * @param sltId
 */
var changeDepartmentSlt = function(sltId){
    try {
        sltId = typeof sltId !== 'undefined' ? sltId : "departmentSlt";

        var dataMap = JSON.stringify({
            "token": $.session.get("token"),
            "company": getCompanyVal()
        });

        AjaxData("/department/lst", dataMap, "POST", function (data) {
            if (data.status === "ok") {
                var departmentList = data.result.department;
                for (var i in departmentList) {
                    departmentList[i] = departmentList[i].department_name;
                }
                resetSelectNoDefault(sltId, departmentList);
            } else {
                console.info("出错啦")
            }
        }, function (e) {
            console.info(e.message)
        })
    } catch (err) {
        $.tooltip(err.message)
    }
};

/**
 * 根据部门改变产品下拉框
 * @param sltId
 */
var changeProductSlt = function(sltId) {
    try {
        sltId = typeof sltId !== 'undefined' ? sltId : "productSlt";

        var dataMap = JSON.stringify({
            "token": $.session.get("token"),
            "company": getCompanyVal(),
            "department": getDepartmentVal()
        });

        AjaxData("/product/lst", dataMap, "POST", function (data) {
            if (data.status === "ok") {
                var productList = data.result.product;
                for (var i in productList) {
                    productList[i] = productList[i].scope_name_ch;
                }
                resetSelectNoDefault(sltId, productList);
            } else {
                console.info("出错啦")
            }
        }, function (e) {
            console.info(e.message)
        })
    } catch (err) {
        $.tooltip(err.message)
    }
};