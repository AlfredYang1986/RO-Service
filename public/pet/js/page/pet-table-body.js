/**
 * Created by liwei on 2017/8/8.
 */
var company_tbody = function (result,page) {
    var tbody = "";
    for(var i in result){
        tbody += "<tr>";
        tbody += "<td>" + (parseInt(page.ROW_START) + parseInt(i)) + "</td>";
        tbody += "<td>" + result[i].company_name_ch + "</td>";
        tbody += "<td>" + result[i].company_name_en + "</td>";
        tbody += "<td>" + result[i].short_name + "</td>";
        tbody += "<td class=\"center\">";
        tbody += "<a href=\"javascript:QueryCompany('" + result[i].company_id + "');\"><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
        tbody += "<a href=\"javascript:RemoveCompany('" + result[i].company_id + "');\"><i class=\"fa fa-trash-o\"></i></a>";
        tbody += "</td>";
        tbody += "</tr>";
    }
    return tbody;
}

var user_tbody = function (result,page) {
    var tbody = "";
    for(var i in result){
        tbody += "<tr>";
        tbody += "<td>" + (parseInt(page.ROW_START) + parseInt(i)) + "</td>";
        tbody += "<td>" + result[i].user_name + "</td>";
        tbody += "<td>" + result[i].screen_name + "</td>";
        tbody += "<td>" + result[i].phoneNo + "</td>";
        tbody += "<td>" + result[i].email + "</td>";
        tbody += "<td>" + result[i].company + "</td>";
        tbody += "<td>" + result[i].department + "</td>";
        tbody += "<td class=\"center\">";
        tbody += "<a href=\"javascript:UserManageModal('" + result[i].user_id + "');\"><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
        tbody += "<a href=\"javascript:RemoveUser('" + result[i].user_id + "');\"><i class=\"fa fa-trash-o\"></i></a>";
        tbody += "</td>";
        tbody += "</tr>";
    }
    return tbody;

}

var department_tbody = function (result,page) {
    var tbody = "";
    for(var i in result){
        tbody += "<tr>";
        tbody += "<td>" + (parseInt(page.ROW_START) + parseInt(i)) + "</td>";
        tbody += "<td>" + result[i].department_name + "</td>";
        tbody += "<td>" + result[i].short_name + "</td>";
        tbody += "<td>" + result[i].is_admin + "</td>";
        tbody += "<td>" + result[i].company + "</td>";
        tbody += "<td class=\"center\">";
        tbody += "<a href=\"javascript:QueryDepartment('" + result[i].department_id + "');\"><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
        tbody += "<a href=\"javascript:RemoveDepartment('" + result[i].department_id + "');\"><i class=\"fa fa-trash-o\"></i></a>";
        tbody += "</td>";
        tbody += "</tr>";
    }
    return tbody;
}

var product_tbody = function (result,page) {
    var tbody = "";
    for(var i in result){
        tbody += "<tr>";
        tbody += "<td>" + (parseInt(page.ROW_START) + parseInt(i)) + "</td>";
        tbody += "<td>" + result[i].scope_name_ch + "</td>";
        tbody += "<td>" + result[i].scope_name_en + "</td>";
        tbody += "<td>" + result[i].department + "</td>";
        tbody += "<td>" + result[i].company + "</td>";
        tbody += "<td class=\"center\">";
        tbody += "<a href=\"javascript:QueryProduct('" + result[i].scope_id + "');\"><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
        tbody += "<a href=\"javascript:RemoveProduct('" + result[i].scope_id + "');\"><i class=\"fa fa-trash-o\"></i></a>";
        tbody += "</td>";
        tbody += "</tr>";
    }
    return tbody;
}

var channel_tbody = function (result,page) {
    var tbody = "";
    for(var i in result){
        tbody += "<tr>";
        tbody += "<td>" + (parseInt(page.ROW_START) + parseInt(i)) + "</td>";
        tbody += "<td>" + result[i].channel_ch + "</td>";
        tbody += "<td>" + result[i].channel_en + "</td>";
        tbody += "<td>" + result[i].channel_description + "</td>";
        tbody += "<td>" + result[i].scope + "</td>";
        tbody += "<td>" + result[i].department + "</td>";
        tbody += "<td>" + result[i].company + "</td>";
        tbody += "<td class=\"center\">";
        tbody += "<a href=\"javascript:QueryChannel('" + result[i].channel_id + "');\"><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
        tbody += "<a href=\"javascript:RemoveChannel('" + result[i].channel_id + "');\"><i class=\"fa fa-trash-o\"></i></a>";
        tbody += "</td>";
        tbody += "</tr>";
    }
    return tbody;
}
