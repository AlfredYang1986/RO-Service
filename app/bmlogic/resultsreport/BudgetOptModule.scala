package bmlogic.resultsreport

import bminjection.db.DBTrait
import bmlogic.resultsreport.BudgetOptMessage._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import play.api.libs.json.JsValue
import bmutil.errorcode.ErrorCode
import play.api.libs.json.Json.toJson
import bmlogic.resultsreport.FileExport.BudgetFileExportInfo
import bmutil.FileConfig.Suffix_Csv
import bmlogic.resultsreport.ResultsReportData.{BudgetOptInfo, ReportComparisonTrait}

/**
  * Created by liwei on 2017/7/6.
  */
object BudgetOptModule extends ModuleTrait
    with BudgetOptInfo
    with BudgetFileExportInfo
    with ReportComparisonTrait {

    val db_name = "budget_opt"

    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_BudgetResultReport(data) => budgetResultReport(data)(pr)
        case msg_BudgetResultComparison(data) => budgetResultComparison(data)(pr)
        case msg_BudgetResultExport(data) => budgetResultExport(data)(pr)
    }

    def budgetResultReport(data : JsValue)
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

    def budgetResultComparison(data : JsValue)
                       (pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {

            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val condition = assemblyComparisonCondition(data, pr, db_name)
            val list = db.queryMultipleObject(condition = condition._3, db_name = db_name, sort = "times", take = 1000)
            val result = generateComparisonResult(data, list)

            (Some(Map(
                "method" -> toJson("listBudgetOptPr"),
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

    def budgetResultExport(data : JsValue)
                       (pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = assemblyExportCondition(data, pr, db_name)
            val list = db.queryMultipleObject(condition = o, db_name = db_name, sort = "times", take = 1000)

            (Some(Map(
                "method" -> toJson("exportBudgetOpt"),
                "result" -> toJson(FileExport(generateExportResult(data, list), Suffix_Csv))
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
