/**
 * Created by liwei on 2017/7/21.
 */

var reg = /^[0-9]+([.]{1}[0-9]+){0,1}$/; //只能输入整数或小数的正则表达式

var BudgetVerification = function () {
    try {
        var product_ratio = $('#product_budget_product_ratio').val();
        var budget_totle = $('#product_budget_budget_totle').val();
        var last_year_sales = reductionNumber($("#product_budget_last_year_sales").val());

        if(!reg.test(product_ratio)){
            $('#product_budget_product_ratio').focus();
            throw new PetException('the data type of the product you entered is not correct');
        }

        if(!reg.test(budget_totle)){
            $('#product_budget_budget_totle').focus();
            throw new PetException('the data type of the total marketing budget you entered is incorrect');
        }

        if(!reg.test(last_year_sales)){
            $('#product_budget_last_year_sales').focus();
            throw new PetException("the data type you entered last year's total sales is incorrect");
        }
    } catch(err) {
        throw new PetException(err.name);
    }
}

var OptimizatVerification = function () {
    try {
        var measured_budget_ratio = $('#product_optimizat_measured_budget_ratio').val();
        var budget_criteria = $('#product_optimizat_budget_criteria').val();
        var income_standards = $('#product_optimizat_income_standards').val();

        if(!reg.test(measured_budget_ratio)){
            $('#product_optimizat_measured_budget_ratio').focus();
            throw new PetException('the data type of the estimated budget ratio you entered is incorrect');
        }

        if(!reg.test(budget_criteria)){
            $('#product_optimizat_budget_criteria').focus();
            throw new PetException('the data type of the fixed budget you entered is not correct');
        }

        if(!reg.test(income_standards)){
            $('#product_optimizat_income_standards').focus();
            throw new PetException('the data type of the fixed income standard you entered is not correct');
        }
    } catch(err) {
        throw new PetException(err.name);
    }
}

var ChannelVerification = function () {
    try {
        var channel_ch = $('#channel_ch').val();
        var channel_en = $('#channel_en').val();
        var channel_description = $('#channel_description').val();

        if(channel_ch == null || channel_ch == ''){
            $('#channel_ch').focus();
            throw new PetException('channel chinese name is empty');
        }

        if(channel_en == null || channel_en == ''){
            $('#channel_en').focus();
            throw new PetException('channel english name is empty');
        }

        if(channel_description == null || channel_description == ''){
            $('#channel_description').focus();
            throw new PetException('channel description is empty');
        }
    } catch(err) {
        throw new PetException(err.name);
    }
}

var ProductVerification = function () {
    try {
        var scope_name_ch = $('#scope_name_ch').val();
        var scope_name_en = $('#scope_name_en').val();
        if(scope_name_ch == null || scope_name_ch == ''){
            $('#scope_name_ch').focus();
            throw new PetException('product chinese name is empty');
        }

        if(scope_name_en == null || scope_name_en == ''){
            $('#scope_name_en').focus();
            throw new PetException('product english name is empty');
        }
    } catch (err) {
        throw new PetException(err.name);
    }
}

var DepartmentVerification = function () {
    try {
        var department_name = $('#department_name').val();
        var short_name = $('#short_name').val();
        if(department_name == null || department_name == ''){
            $('#department_name').focus();
            throw new PetException('department name is empty');
        }

        if(short_name == null || short_name == ''){
            $('#short_name').focus();
            throw new PetException('department referred to as empty');
        }
    } catch (err) {
        throw new PetException(err.name);
    }
}

var CompanyVerification = function () {
    try {
        var company_name_ch = $('#company_name_ch').val();
        var company_name_en = $('#company_name_en').val();
        var short_name = $('#short_name').val();
        if(company_name_ch == null || company_name_ch == ''){
            $('#company_name_ch').focus();
            throw new PetException('company chinese name is empty');
        }

        if(company_name_en == null || company_name_en == ''){
            $('#company_name_en').focus();
            throw new PetException('company english name is empty');
        }

        if(short_name == null || short_name == ''){
            $('#short_name').focus();
            throw new PetException('company referred to as empty');
        }
    } catch (err) {
        throw new PetException(err.name);
    }
}

var UserVerification = function () {
    try {
        var user_name = $('#user_name').val();
        var pwd = $('#pwd').val();
        if(user_name == null || user_name == ''){
            $('#user_name').focus();
            throw new PetException('the user name is empty');
        }

        if(pwd == null || pwd == ''){
            $('#pwd').focus();
            throw new PetException('the password is empty');
        }
    } catch (err) {
        throw new PetException(err.name);
    }
}

var verificationProductRatioSum = function () {
    try {
        var trs = $('#product_budget_tbody').find("tr")
        var tds = trs.find('td:eq(3)').text().split("%")
        var sum = 0.0
        for (var i in tds) {
            if(i < (tds.length-1))
                sum += parseFloat(tds[i])
        }
        if(sum < 99 || sum > 101)
            throw new PetException('the proportion of each product to fill in the wrong');
    } catch(err) {
        throw new PetException(err.name);
    }
}

var verificatProductNowRatioSum = function () {
    try {
        var trs = $('#product_optimizat_tbody').find("tr")
        var tds = trs.find('td:eq(7)').text().split("%")
        var sum = 0.0;
        for (var i in tds) {
            if(i < (tds.length-1))
                sum += parseFloat(tds[i]);
        }
        if(sum < 99 || sum > 101)
            throw new PetException('the product estimates the budget ratio is incorrect');
    } catch(err) {
        throw new PetException(err.name);
    }
}

var verificatRegionRatioSum = function () {
    try {
        var arr = []
        arr.push(parseFloat($("#region_northeast").val()));
        arr.push(parseFloat($("#region_northchina").val()));
        arr.push(parseFloat($("#region_eastchina").val()));
        arr.push(parseFloat($("#region_southchina").val()));
        arr.push(parseFloat($("#region_westchina").val()));
        arr.push(parseFloat($("#region_centralchina").val()));
        arr.push(parseFloat($("#region_beijingandtianjin").val()));
        var totle = arr.sum()
        if(totle != 100)
            throw new PetException('the sum of the major regions is incorrect');
    } catch(err) {
        throw new PetException(err.name);
    }
}


