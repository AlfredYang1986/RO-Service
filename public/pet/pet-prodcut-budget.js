/**
 * Created by liwei on 2017/7/19.
 */

/**
 * Product Budget Table Data
 */
var ProductBudgetTable = function () {
    try {
        var dataMap = JSON.stringify({
            "token" : $.session.get('token')
        });

        AjaxData("/astellas/budget/lst", dataMap, "POST", function(data){
            if(data.status == "error"){
                throw new PetException('product budget list request failed');
            }else{
                var result = data.result.result
                var htmlsb = "";
                $('h5[id="yearh"]').html(result[0].year+"营销预算");
                for (var i in result) {
                    htmlsb += "<tr>";
                    htmlsb += "<td>"+(parseInt(i)+1)+"</td>";
                    htmlsb += "<td>"+result[i].year+"</td>";
                    htmlsb += "<td>"+result[i].scope_name_ch+"</td>";
                    htmlsb += "<td>"+floattoFixedN(result[i].product_ratio,1)+"%</td>";
                    htmlsb += "<td>"+thousandBitSeparator(parseInt(result[i].product_cost))+"</td>";
                    if(false){
                        htmlsb += "<td>"+floattoFixedN(result[i].region_northeast,1)+"%</td>";
                        htmlsb += "<td>"+floattoFixedN(result[i].region_northchina,1)+"%</td>";
                        htmlsb += "<td>"+floattoFixedN(result[i].region_eastchina,1)+"%</td>";
                        htmlsb += "<td>"+floattoFixedN(result[i].region_southchina,1)+"%</td>";
                        htmlsb += "<td>"+floattoFixedN(result[i].region_westchina,1)+"%</td>";
                        htmlsb += "<td>"+floattoFixedN(result[i].region_centralchina,1)+"%</td>";
                        htmlsb += "<td>"+floattoFixedN(result[i].region_beijingandtianjin,1)+"%</td>";
                    }
                    htmlsb += "<td>"+thousandBitSeparator(parseInt(result[i].last_year_sales))+"</td>";
                    htmlsb += "<td>"+result[i].cross_product_optimization+"</td>";
                    htmlsb += "<td hidden>"+result[i].sales_area_analysis+"</td>";
                    htmlsb += "<td class=\"center\">";
                    htmlsb += "<a href=\"javascript:ProductBudgetModal('"+result[i].budget_id+"');\"><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
                    htmlsb += "</td>";
                    htmlsb += "</tr>";
                }
                $('tbody[id="product_budget_tbody"]').html(htmlsb);
            }
        }, function(e){
            throw new PetException(e.name);
        });
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

/**
 * Product Budget Modal Data
 * @param id
 */
var ProductBudgetModal = function (id) {
    try {
        var dataMap = JSON.stringify({
            "token" : $.session.get('token'),
            "budget_id" : id
        });

        AjaxData("/astellas/budget/query", dataMap, "POST", function(data){
            if(data.status == "error"){
                throw new PetException('product budget request failed');
            }else {
                var obj = data.result.result
                $("#budget-form").modal('show');
                $('#modal_form')[0].reset();

                $("#product_budget_last_year_sales").val(thousandBitSeparator(parseInt(obj.last_year_sales)));
                $("#product_budget_budget_id").val(obj.budget_id);
                $("#product_budget_company").val(obj.company);
                $("#product_budget_department").val(obj.department);
                $("#product_budget_date").val(obj.date);
                $("#product_budget_scope_name_en").val(obj.scope_name_en);
                $("#product_budget_scope_name_ch").val(obj.scope_name_ch);

                defaultSelected('product_budget_year',obj.year)

                $("#product_budget_product_ratio").val(floattoFixedN(obj.product_ratio, 1))
                $("#product_budget_budget_totle").val(obj.budget_totle);
                $("#product_budget_product_cost").val(thousandBitSeparator(parseInt(obj.product_cost)));

                defaultSelected('product_budget_cross_product_optimization',obj.cross_product_optimization)

                $("#product_budget_region_northeast").val(floattoFixedN(obj.region_northeast, 1));
                $("#product_budget_region_northchina").val(floattoFixedN(obj.region_northchina, 1));
                $("#product_budget_region_eastchina").val(floattoFixedN(obj.region_eastchina, 1));
                $("#product_budget_region_southchina").val(floattoFixedN(obj.region_southchina, 1));
                $("#product_budget_region_westchina").val(floattoFixedN(obj.region_westchina, 1));
                $("#product_budget_region_centralchina").val(floattoFixedN(obj.region_centralchina, 1));
                $("#product_budget_region_beijingandtianjin").val(floattoFixedN(obj.region_beijingandtianjin, 1));

                defaultSelected('product_budget_sales_area_analysis',obj.sales_area_analysis)
            }
        }, function(e){
            throw new PetException(e.name);
        });
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}



/**
 * Save Product Budget Info
 */
var ProductBudgetSave = function () {
    try {
        BudgetVerification();
        changeCost();
        var dataMap = JSON.stringify({
            "token": $.session.get('token'),
            "last_year_sales": reductionNumber($("#product_budget_last_year_sales").val()),
            "budget_id": $("#product_budget_budget_id").val(),
            "company": $("#product_budget_company").val(),
            "department": $("#product_budget_department").val(),
            "date": parseInt($("#product_budget_date").val()),
            "scope_name_en": $("#product_budget_scope_name_en").val(),
            "scope_name_ch": $("#product_budget_scope_name_ch").val(),
            "year": parseInt($("#product_budget_year").find("option:selected").text()),
            "product_ratio": parseFloat($("#product_budget_product_ratio").val()),
            "budget_totle": parseFloat($("#product_budget_budget_totle").val()),
            "product_cost": parseFloat(reductionNumber($("#product_budget_product_cost").val())),
            "cross_product_optimization": $("#product_budget_cross_product_optimization").find("option:selected").text(),
            "region_northeast": parseFloat($("#product_budget_region_northeast").val()),
            "region_northchina": parseFloat($("#product_budget_region_northchina").val()),
            "region_eastchina": parseFloat($("#product_budget_region_eastchina").val()),
            "region_southchina": parseFloat($("#product_budget_region_southchina").val()),
            "region_westchina": parseFloat($("#product_budget_region_westchina").val()),
            "region_centralchina": parseFloat($("#product_budget_region_centralchina").val()),
            "region_beijingandtianjin": parseFloat($("#product_budget_region_beijingandtianjin").val()),
            "sales_area_analysis": $("#product_budget_sales_area_analysis").find("option:selected").text()
        });

        AjaxData("/astellas/budget/update", dataMap, "POST", function(data){
            if(data.status == "error"){
                throw new PetException('product budget data failed to save');
            }else{
                ProductBudgetTable();
                $("#budget-form").modal('hide');
                $.tooltip('提交成功', 2500, true);
            }
        }, function(e){
            throw new PetException(e.name)
        });
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

var changeCost = function () {
    try {
        var product_ratio = $("#product_budget_product_ratio").val();
        var budget_totle = $("#product_budget_budget_totle").val();
        $("#product_budget_product_cost").val(thousandBitSeparator(parseInt(product_ratio / 100 * budget_totle)));
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

var ChangeAndSaveOptimizatType = function () {
    try {
        var coocie_optimizaOpt = $.cookie("optimizaOpt");
        if(coocie_optimizaOpt!="" && coocie_optimizaOpt!=null){
            $("#optimizaOpt option").each(function (){
                if($(this).text()==coocie_optimizaOpt){
                    $(this).attr('selected',true);
                }
            });
        }else{
            $("#optimizaOpt option").each(function (){
                if($(this).text()=="固定收益"){
                    $.cookie("optimizaOpt","固定收益");
                    $(this).attr('selected',true);
                }
            });
        }
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}

var ChangeOptimizatType = function () {
    try {
        $.cookie("optimizaOpt",$("#optimizaOpt").find("option:selected").text());
    } catch(err) {
        PetAlert(getMessageByName(err.name));
    }
}