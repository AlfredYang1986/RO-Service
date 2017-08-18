package bmlogic.resultsreport.ResultsReportData

import bmlogic.resultsreport._
import com.mongodb.casbah.Imports.DBObject
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by liwei on 2017/8/14.
  */

trait ReportComparisonTrait {

    def assemblyComparisonCondition(data : JsValue, pr : Option[Map[String, JsValue]], db_name : String) : (List[String], String, DBObject) = {
        val auth = pr.get.get("auth").get
        val channel = (auth \ "channel").asOpt[List[Map[String,JsValue]]].map(x => x).get

        val scopes = channel.groupBy(x => x.get("scope").get.asOpt[String].map(x => x).get).map(x => x._1).toList
        val scope = (data \ "scope_name_ch").get.asOpt[String].get

        val scope_sel = if(scope.equals("")) scopes.head else scope
        val jv = toJson(auth.asOpt[Map[String,JsValue]].get.++:(Map("scope_name_ch" -> toJson(scope_sel))))

        val condit = selCondition(jv, db_name)

        (scopes, scope_sel, condit)
    }

    def generateComparisonResult(data : JsValue, list : List[Map[String, JsValue]]) : (List[Int],Int,List[Map[String, JsValue]]) = {
        val times_list = list.map(x => x.get("times").get.asOpt[JsValue].get.asOpt[Int].map(x => x).get).distinct.sorted(Ordering.Int.reverse)

        val times = (data \ "times").asOpt[String].map(x => x).get
        val selected_times = if(times.equals("")) times_list.head else times.toInt
        val channels = list.filter(x => x.get("times").get.asOpt[Int].map(x => x).get.equals(selected_times))

        (times_list, selected_times, channels)
    }

    def assemblyExportCondition(data : JsValue, pr : Option[Map[String, JsValue]], db_name : String) : DBObject = {

        val scope_name_ch = (data \ "scope_name_ch").asOpt[String].map(x => x).get
        val jv = toJson(pr.get.get("auth").get.asOpt[Map[String,JsValue]].get.++:(Map("scope_name_ch" -> toJson(scope_name_ch))))

        selCondition(jv, db_name)
    }

    def generateExportResult(data : JsValue, list : List[Map[String, JsValue]]) : List[Map[String, JsValue]] = {
        list.filter(x => x.get("times").get.asOpt[Int].map(x => x).get.equals((data \ "times").asOpt[String].map(x => x).get.toInt))
    }

    def selCondition(jv : JsValue, db_name : String) : DBObject = db_name match {
        case "budget_opt" => BudgetOptModule.condition(jv)
        case "revenue_opt" => RevenueOptModule.condition(jv)
        case "profit_opt" => ProfitOptModule.condition(jv)
    }
}
