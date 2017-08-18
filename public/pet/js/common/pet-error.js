/**
 * Created by liwei on 2017/8/10.
 */
var PetException = function(name) {
    this.name = name;
}

var PetAlert = function (content) {
    $.tooltip(content.message);
}

var PetErrors = [
    {"code" : -1000, "name" : "the user name is empty", "message" : "用户名为空"},
    {"code" : -1001, "name" : "the password is empty", "message" : "密码为空"},
    {"code" : -1002, "name" : "user information submission failed", "message" : "用户信息保存失败"},
    {"code" : -1003, "name" : "user information deletion failed", "message" : "用户信息删除失败"},
    {"code" : -1004, "name" : "the user information request failed", "message" : "用户信息请求失败"},
    {"code" : -1005, "name" : "user list request failed", "message" : "用户列表请求失败"},
    {"code" : -1006, "name" : "login account and password do not match", "message" : "登录帐号和密码不匹配"},
    {"code" : -1007, "name" : "logout failed", "message" : "登出失败"},

    {"code" : -1021, "name" : "company chinese name is empty", "message" : "公司中文名称为空"},
    {"code" : -1022, "name" : "company english name is empty", "message" : "公司英文名称为空"},
    {"code" : -1023, "name" : "company referred to as empty", "message" : "公司简称为空"},
    {"code" : -1024, "name" : "company information submission failed", "message" : "公司信息保存失败"},
    {"code" : -1025, "name" : "company information deletion failed", "message" : "公司信息删除失败"},
    {"code" : -1026, "name" : "the company information request failed", "message" : "公司信息请求失败"},
    {"code" : -1027, "name" : "company list request failed", "message" : "公司列表请求失败"},

    {"code" : -1041, "name" : "department name is empty", "message" : "部门名称为空"},
    {"code" : -1042, "name" : "department referred to as empty", "message" : "部门简称为空"},
    {"code" : -1043, "name" : "department information submission failed", "message" : "部门信息保存失败"},
    {"code" : -1044, "name" : "department information deletion failed", "message" : "部门信息删除失败"},
    {"code" : -1045, "name" : "the department information request failed", "message" : "部门信息请求失败"},
    {"code" : -1046, "name" : "department list request failed", "message" : "部门列表请求失败"},

    {"code" : -1061, "name" : "product chinese name is empty", "message" : "产品中文名称为空"},
    {"code" : -1062, "name" : "product english name is empty", "message" : "产品英文名称为空"},
    {"code" : -1063, "name" : "product information submission failed", "message" : "产品信息保存失败"},
    {"code" : -1064, "name" : "product information deletion failed", "message" : "产品信息删除失败"},
    {"code" : -1065, "name" : "the product information request failed", "message" : "产品信息请求失败"},
    {"code" : -1066, "name" : "product list request failed", "message" : "产品列表请求失败"},

    {"code" : -1081, "name" : "channel chinese name is empty", "message" : "渠道中文名称为空"},
    {"code" : -1082, "name" : "channel english name is empty", "message" : "渠道英文名称为空"},
    {"code" : -1082, "name" : "channel description is empty", "message" : "渠道说明为空"},
    {"code" : -1083, "name" : "channel information submission failed", "message" : "渠道信息保存失败"},
    {"code" : -1084, "name" : "channel information deletion failed", "message" : "渠道信息删除失败"},
    {"code" : -1085, "name" : "the channel information request failed", "message" : "渠道信息请求失败"},
    {"code" : -1086, "name" : "channel list request failed", "message" : "渠道列表请求失败"},

    {"code" : -1101, "name" : "product budget template download failed", "message" : "产品预算模板下载失败"},
    {"code" : -1102, "name" : "product optimization template failed to download", "message" : "产品优化模板下载失败"},
    {"code" : -1103, "name" : "product budget data import failed", "message" : "产品预算数据导入失败"},
    {"code" : -1104, "name" : "product optimization data import failed", "message" : "产品优化数据导入失败"},

    {"code" : -1121, "name" : "the proportion of each product to fill in the wrong", "message" : "各产品比例填写有误"},
    {"code" : -1122, "name" : "the product estimates the budget ratio is incorrect", "message" : "该产品测算预算比例填写有误"},
    {"code" : -1123, "name" : "the sum of the major regions is incorrect", "message" : "各大区占比总和有误"},
    {"code" : -1124, "name" : "product budget data failed to save", "message" : "产品预算数据保存失败"},
    {"code" : -1125, "name" : "product budget request failed", "message" : "产品预算信息请求失败"},
    {"code" : -1126, "name" : "product budget list request failed", "message" : "产品预算列表请求失败"},
    {"code" : -1127, "name" : "page initialization batch update data failed", "message" : "页面初始化批量更新数据失败"},
    {"code" : -1128, "name" : "product optimization data failed to save", "message" : "产品优化数据保存失败"},
    {"code" : -1129, "name" : "product optimization request failed", "message" : "产品优化信息请求失败"},
    {"code" : -1130, "name" : "product optimization list request failed", "message" : "产品优化列表请求失败"},

    {"code" : -1131, "name" : "the data type of the product you entered is not correct", "message" : "您输入的该产品比例的数据类型不正确"},
    {"code" : -1132, "name" : "the data type of the total marketing budget you entered is incorrect", "message" : "您输入的营销预算总额的数据类型不正确"},
    {"code" : -1133, "name" : "the data type you entered last year's total sales is incorrect", "message" : "您输入的去年销售总额的数据类型不正确"},
    {"code" : -1134, "name" : "the data type of the estimated budget ratio you entered is incorrect", "message" : "您输入的测算预算比例的数据类型不正确"},
    {"code" : -1135, "name" : "the data type of the fixed budget you entered is not correct", "message" : "您输入的固定预算标准的数据类型不正确"},
    {"code" : -1136, "name" : "the data type of the fixed income standard you entered is not correct", "message" : "您输入的固定收益标准的数据类型不正确"},

    {"code" : -1141, "name" : "fixed budget results report failed", "message" : "固定预算结果报告请求失败"},
    {"code" : -1142, "name" : "fixed Revenue Report Report Request failed", "message" : "固定收益结果报告请求失败"},
    {"code" : -1143, "name" : "profit Maximization Report request failed", "message" : "利润最大化结果报告请求失败"},
    {"code" : -1144, "name" : "r run-time error", "message" : "R运行时错误"},

    {"code" : -1161, "name" : "fixed budget result comparison request failed", "message" : "固定预算结果比较请求失败"},
    {"code" : -1162, "name" : "fixed income result comparison request failed", "message" : "固定收益结果比较请求失败"},
    {"code" : -1163, "name" : "the profit maximization result comparison request failed", "message" : "利润最大化结果比较请求失败"},
    {"code" : -1164, "name" : "fixed budget results comparison information export failed", "message" : "固定预算结果比较信息导出失败"},
    {"code" : -1165, "name" : "fixed income results comparison information export failed", "message" : "固定收益结果比较信息导出失败"},
    {"code" : -1166, "name" : "profit maximization results comparison information export failure", "message" : "利润最大化结果比较信息导出失败"},

    {"code" : -9999, "name" : "unknown error", "message" : "unknown error"}
]

var getMessageByName = function (name) {
    for(var i in PetErrors){
        if(PetErrors[i].name == name){
            this.code = PetErrors[i].code;
            this.name = name;
            this.message = PetErrors[i].message;
            return this;
        }
    }
}