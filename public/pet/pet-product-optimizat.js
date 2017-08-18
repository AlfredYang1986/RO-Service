/**
 * Created by liwei on 2017/7/19.
 */

/**
 * 产品优化设置列表
 * @constructor
 */
var ProductOptimizatTable = function () {
    try {
        var dataMap = JSON.stringify({
            "token" : $.session.get('token'),
            "scope_name_ch" : $("#productOpt").find("option:selected").text()
        });

        AjaxData("/astellas/productOptimization/lst", dataMap, "POST", function(data){
            if(data.status == "error"){
                throw new PetException('product optimization list request failed');
            }else{

                $("#productOpt option").remove();
                var products = data.result.products;
                for(var i in products) {
                    $("#productOpt").append("<option>"+products[i]+"</option>");
                }

                var selproduct = data.result.selproduct
                $("#productOpt option").each(function (){
                    if($(this).text()==selproduct){
                        $(this).attr('selected',true);
                    }
                });

                var result = data.result.result
                var htmlsb = "";
                for (var i in result) {
                    htmlsb += "<tr>";
                    htmlsb += "<td>"+(parseInt(i)+1)+"</td>";
                    htmlsb += "<td>"+result[i].year+"</td>";
                    htmlsb += "<td>"+result[i].channel_name_ch+"</td>";
                    htmlsb += "<td>"+result[i].channel_description+"</td>";
                    htmlsb += "<td>"+result[i].need_optimization+"</td>";
                    htmlsb += "<td>"+floattoFixedN(result[i].now_budget_ratio,1)+"%</td>";
                    htmlsb += "<td>"+thousandBitSeparator(parseInt(result[i].now_budget))+"</td>";
                    htmlsb += "<td>"+floattoFixedN(result[i].measured_budget_ratio,1)+"%</td>";
                    htmlsb += "<td>"+thousandBitSeparator(parseInt(result[i].calculate_budget_values))+"</td>";
                    htmlsb += "<td>"+result[i].budget_variation+"</td>";
                    htmlsb += "<td class=\"center\">";
                    htmlsb += "<a href=\"javascript:ProductOptimizatModal('"+result[i].pro_opt_id+"');\"><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
                    htmlsb += "</td>";
                    htmlsb += "</tr>";
                }
                $('tbody[id="product_optimizat_tbody"]').html(htmlsb);
                //verificationPercentagePr(0)
            }
        }, function(e){
            throw new PetException(e.name);
        });
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }


}

/**
 * 根据ID查询产品优化设置信息
 * @param id
 * @constructor
 */
var ProductOptimizatModal = function (id) {
    try {
        var dataMap = JSON.stringify({
            "token" : $.session.get('token'),
            "pro_opt_id" : id
        });

        AjaxData("/astellas/productOptimization/query", dataMap, "POST", function(data){
            if(data.status == "error"){
                throw new PetException('product optimization request failed');
            }else{
                $("#productoptimization-form").modal('show');
                $('#modal_form')[0].reset();
                HideAndShowInputByOptimizatType();

                $("#product_optimizat_pro_opt_id").val(data.result.pro_opt_id);
                $("#product_optimizat_channel_name_en").val(data.result.channel_name_en);
                $("#product_optimizat_company").val(data.result.company);
                $("#product_optimizat_department").val(data.result.department);
                $("#product_optimizat_date").val(data.result.date);
                $("#product_optimizat_scope_name_en").val(data.result.scope_name_en);
                $("#product_optimizat_now_budget").val(data.result.now_budget);
                $("#product_optimizat_now_budget_ratio").val(floattoFixedN(data.result.now_budget_ratio,1));
                $("#product_optimizat_scope_name_ch").val(data.result.scope_name_ch);
                $("#product_optimizat_channel_name_ch").val(data.result.channel_name_ch);
                $("#product_optimizat_channel_description").val(data.result.channel_description);
                $("#product_optimizat_year").val(data.result.year);
                $("#product_optimizat_measured_budget_ratio").val(floattoFixedN(data.result.measured_budget_ratio,1));
                $("#product_optimizat_budget_criteria").val(data.result.budget_criteria);
                $("#product_optimizat_calculate_budget_values").val(parseInt(data.result.calculate_budget_values));

                defaultSelected('product_optimizat_budget_variation',data.result.budget_variation)

                defaultSelected('product_optimizat_need_optimization',data.result.need_optimization)

                $("#product_optimizat_income_standards").val(data.result.income_standards);
            }
        }, function(e){
            throw new PetException(e.name);
        });
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

/**
 * 保存产品优化设置信息
 * @constructor
 */
var ProductOptimizatSave = function () {
    try {
        OptimizatVerification()
        changeRatio()
        var dataMap = JSON.stringify({
            "token" : $.session.get('token'),
            "pro_opt_id" : $("#product_optimizat_pro_opt_id").val(),
            "channel_name_en" : $("#product_optimizat_channel_name_en").val(),
            "company" : $("#product_optimizat_company").val(),
            "department" : $("#product_optimizat_department").val(),
            "date" : parseInt($("#product_optimizat_date").val()),
            "scope_name_en" : $("#product_optimizat_scope_name_en").val(),
            "now_budget" : parseFloat($("#product_optimizat_now_budget").val()),
            "now_budget_ratio" : parseFloat($("#product_optimizat_now_budget_ratio").val()),
            "scope_name_ch" : $("#product_optimizat_scope_name_ch").val(),
            "channel_name_ch" : $("#product_optimizat_channel_name_ch").val(),
            "channel_description" : $("#product_optimizat_channel_description").val(),
            "year" : parseInt($("#product_optimizat_year").val()),
            "measured_budget_ratio" : parseFloat($("#product_optimizat_measured_budget_ratio").val()),
            "budget_criteria" : parseFloat($("#product_optimizat_budget_criteria").val()),
            "calculate_budget_values" : parseFloat($("#product_optimizat_calculate_budget_values").val()),
            "budget_variation" : $("#product_optimizat_budget_variation").find("option:selected").text(),
            "need_optimization" : $("#product_optimizat_need_optimization").find("option:selected").text(),
            "income_standards" : parseFloat($("#product_optimizat_income_standards").val())
        });

        AjaxData("/astellas/productOptimization/update", dataMap, "POST", function(data){
            if(data.status == "error"){
                throw new PetException('product optimization data failed to save');
            }else{
                ProductOptimizatTable();
                $("#productoptimization-form").modal('hide');
                $.tooltip('提交成功', 2500, true);
            }
        }, function(e){
            throw new PetException(e.name);
        });
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

var changeRatio = function () {
    try {
        var budget_criteria = $("#product_optimizat_budget_criteria").val();
        var measured_budget_ratio = $("#product_optimizat_measured_budget_ratio").val();
        $("#product_optimizat_calculate_budget_values").val(parseFloat(measured_budget_ratio / 100 * parseFloat(budget_criteria)).toFixed(2));
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

var HideAndShowInputByOptimizatType = function () {
    try {
        var optimizatType = $.cookie("optimizaOpt")
        if(optimizatType == "固定收益"){
            $("#income_standards_div").show()
        }else{
            $("#income_standards_div").hide()
        }
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

var UpdateProductOptimizatByProductBudget = function () {
    try {
        var dataMap = JSON.stringify({
            "token" : $.session.get('token'),
        });

        AjaxData("/astellas/productOptimization/updatepr", dataMap, "POST", function(data){
            if(data.status == "error"){
                throw new PetException('page initialization batch update data failed');
            }else{
                ProductOptimizatTable();
            }
        }, function(e){
            throw new PetException(e.name);
        });
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}