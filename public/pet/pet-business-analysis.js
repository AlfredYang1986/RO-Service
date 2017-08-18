/**
 * Created by liwei on 2017/7/19.
 */
$(function () {
    $.cookie("optimizaOpt","固定预算");

    var department = $.cookie("department");

    if(department === "销售企划"){
        $('#pdf').show();
        $('#basicWizard').hide();
        PDFObject.embed("/assets/modules/pdf/PET_Report_Astellas_SalesOperation_tool.pdf", "#pdf");
    }else if(department === "感染"){
        $('#pdf').hide();
        $('#basicWizard').show();
        PDFObject.embed("/assets/modules/pdf/PET_Report_Astellas_HA&VC_tool.pdf", "#havc");
        PDFObject.embed("/assets/modules/pdf/PET_Report_Astellas_MYC&PD_tool.pdf", "#mycpd");
    }else if(department === "过敏"){
        $('#pdf').show();
        $('#basicWizard').hide();
        PDFObject.embed("/assets/modules/pdf/PET_Report_Astellas_ALK_tool.pdf", "#pdf");
    }else if(department === "移植"){
        $('#pdf').show();
        $('#basicWizard').hide();
        PDFObject.embed("/assets/modules/pdf/PET_Report_Astellas_PRG&GRF_tool.pdf", "#pdf");
    }else{
        $('#pdf').show();
        $('#basicWizard').hide();
        PDFObject.embed("/assets/modules/pdf/PET_Report_Astellas_0605.pdf", "#pdf");
    }

    $('#basicWizard').bootstrapWizard();
});