/**
 * Created by liwei on 2017/8/8.
 */
var PetPage = function (result,page,type) {
    switch (type) {
        case "userManage" :
            $('thead[id="thead"]').html(user_thead);
            BasicTable(result, page, user_tbody(result,page));
            break;
        case "companyManage" :
            $('thead[id="thead"]').html(company_thead);
            BasicTable(result, page, company_tbody(result,page));
            break;
        case "departmentManage" :
            $('thead[id="thead"]').html(department_thead);
            BasicTable(result, page, department_tbody(result,page));
            break;
        case "productManage" :
            $('thead[id="thead"]').html(product_thead);
            BasicTable(result, page, product_tbody(result,page));
            break;
        case "channelManage" :
            $('thead[id="thead"]').html(channel_thead);
            BasicTable(result, page, channel_tbody(result,page));
            break;
        default :
            console.log("error");
            break;
    }
}

var BasicTable = function (result,page,tbody) {
    if(result != null && result.length != 0){
        $('tbody[id="tbody"]').html(tbody);
    }else{
        $('tbody[id="tbody"]').html("<tr><td>没有匹配的记录</td></tr>");
    }
    Pagination(result,page);
}