package bmlogic.resultsreport

import bminjection.db.DBTrait
import bmlogic.resultsreport.ProfitOptMessage._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import bmlogic.resultsreport.FileExport.ProfitFileExportInfo
import bmutil.FileConfig.Suffix_Csv
import bmlogic.resultsreport.ResultsReportData.{ProfitOptInfo, ReportComparisonTrait}

/**
  * Created by liwei on 2017/7/6.
  */
object ProfitOptModule extends ModuleTrait
    with ProfitOptInfo
    with ProfitFileExportInfo
    with ReportComparisonTrait {

    val db_name = "profit_opt"

    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_ProfitResultReport(data) => profitResultReport(data)(pr)
        case msg_ProfitResultComparison(data) => profitResultComparison(data)(pr)
        case msg_ProfitResultExport(data) => profitResultExport(data)(pr)
        case _ => ???
    }

    def profitResultReport(data : JsValue)
                     (pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {

            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val optimizaOpt = (data \ "optimizaOpt").get.asOpt[String].get
            val condition = assemblyComparisonCondition(data, pr, db_name)
            val list = db.queryMultipleObject(condition = condition._3, db_name = db_name, sort = "times", take = 1000).groupBy(_.get("times")).maxBy(_._1.get.asOpt[Int].get)

            (Some(Map(
                "method" -> toJson("listBudgetOpt"),
                "channels" -> toJson(Map("key" -> list._1.get,"values" -> toJson(list._2))),
                "products" -> toJson(condition._1),
                "optimizaOpt" -> toJson(optimizaOpt),
                "scope_name_ch" -> toJson(condition._2)
            )), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def profitResultComparison(data : JsValue)
                       (pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val condition = assemblyComparisonCondition(data, pr, db_name)
            val list = db.queryMultipleObject(condition = condition._3, db_name = db_name, sort = "times", take = 1000)
            val result = generateComparisonResult(data, list)

            (Some(Map(
                "method" -> toJson("listProfitOptPr"),
                "channels" -> toJson(result._3),
                "products" -> toJson(condition._1),
                "scope_name_ch" -> toJson(condition._2),
                "times_lst" -> toJson(result._1),
                "times" -> toJson(result._2)
            )), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def profitResultExport(data : JsValue)
                       (pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = assemblyExportCondition(data, pr, db_name)
            val list = db.queryMultipleObject(condition = o, db_name = db_name, sort = "times", take = 1000)

            (Some(Map(
                "method" -> toJson("exportProfitOpt"),
                "result" -> toJson(FileExport(generateExportResult(data, list), Suffix_Csv))
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
