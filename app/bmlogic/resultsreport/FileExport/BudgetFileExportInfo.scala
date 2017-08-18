package bmlogic.resultsreport.FileExport
import bmutil.FileConfig.Export_Field_Xml
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/7/21.
  */
trait BudgetFileExportInfo extends FileExportInfo {

    override def FileExport(list: List[Map[String, JsValue]], suffix: String): String = {
        val header = ((xml.XML.loadFile(Export_Field_Xml) \ "body" \ "budget_opt").map(x => x.text)).toList
        val content = list.map { x =>
            val channel_current_cost_ratio = (x.get("channel_current_cost_ratio").get.asOpt[Double].map(x =>x).get * 100 + "%")
            val channel_opt_cost_ratio = (x.get("channel_opt_cost_ratio").get.asOpt[Double].map(x =>x).get * 100 + "%")
            val revenue_uplift_prod = (x.get("revenue_uplift_prod").get.asOpt[Double].map(x => x).get * 100 + "%")
            (x("times") :: x("scope_name_ch")
                    :: x("channel_name_ch")
                    :: x("channel_description")
                    :: x("channel_current_cost")
                    :: channel_current_cost_ratio
                    :: x("channel_opt_cost")
                    :: channel_opt_cost_ratio
                    :: x("current_cost_prod")
                    :: x("current_revenue_prod")
                    :: x("opt_revenue_prod")
                    :: revenue_uplift_prod
                    :: x("date") :: Nil)
        }
        WriterIntoFile(header, content,suffix)
    }
}
