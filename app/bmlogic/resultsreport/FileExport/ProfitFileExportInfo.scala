package bmlogic.resultsreport.FileExport

import bmutil.FileConfig.Export_Field_Xml
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/7/21.
  */
trait ProfitFileExportInfo extends FileExportInfo {

    override def FileExport(list: List[Map[String, JsValue]], suffix: String): String = {
        val header = ((xml.XML.loadFile(Export_Field_Xml) \ "body" \ "profit_opt").map(x => x.text)).toList
        val content = list.map { x =>
            val channel_current_cost_ratio = (x.get("channel_current_cost_ratio").get.asOpt[JsValue].get.asOpt[Double].map(x => x).get * 100 + "%")
            val channel_opt_cost_ratio = (x.get("channel_opt_cost_ratio").get.asOpt[JsValue].get.asOpt[Double].map(x => x).get * 100 + "%")
            val profit_uplift_prod = (x.get("profit_uplift_prod").get.asOpt[JsValue].get.asOpt[Double].map(x => x).get * 100 + "%")
            (x("times") :: x("scope_name_ch")
                    :: x("channel_name_ch")
                    :: x("channel_description")
                    :: x("channel_current_cost")
                    :: channel_current_cost_ratio
                    :: x("channel_opt_cost")
                    :: channel_opt_cost_ratio
                    :: x("current_cost_prod")
                    :: x("current_profit")
                    :: x("max_profit")
                    :: profit_uplift_prod
                    :: x.get("date").get :: Nil)
        }
        WriterIntoFile(header, content, suffix)
    }
}
