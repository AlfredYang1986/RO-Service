package bmlogic.dataManage.dataManageData

import java.util.Date

import bmlogic.common.sercurity.Sercurity
import bmutil.dao._data_connection
import bmutil.excel.java.{ProductBudget, ProductOptimization}
import com.mongodb.casbah.Imports._
import com.pharbers.aqll.common.alFileHandler.alExcelOpt.scala.BaseExcel
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 2017/6/29.
  */
trait DataManageInfo {
    implicit val d2m: DBObject => Map[String, JsValue] = { obj =>
        Map(
            "company" -> toJson(obj.getAs[String]("company").map(x => x).getOrElse(throw new Exception("db prase error"))),
            "department" -> toJson(obj.getAs[String]("department").map(x => x).getOrElse(throw new Exception("db prase error"))),
            "scope_name_ch" -> toJson(obj.getAs[String]("scope_name_ch").map(x => x).getOrElse(throw new Exception("db prase error"))),
            "scope_name_en" -> toJson(obj.getAs[String]("scope_name_en").map(x => x).getOrElse(throw new Exception("db prase error")))
        )
    }

    implicit val l2m: List[BaseExcel] => DBObject = {
        case head::tail =>
            head match {
                case o: ProductOptimization =>
                    val builder = MongoDBObject.newBuilder
                    builder += "company" -> o.getCompany
                    builder += "department" -> o.getDepartment
                    builder += "year" -> o.getYear
                    builder.result()
                case p: ProductBudget =>
                    val builder = MongoDBObject.newBuilder
                    builder += "company" -> p.getCompany
                    builder += "department" -> p.getDepartment
                    builder += "year" -> p.getYear
                    builder.result()
                case _ => throw new Exception("data import file error")
            }
        case _ => throw new Exception("import data is null")
    }

    def importBudget(list:List[ProductBudget]): Unit ={
        list.foreach{x =>
            val builder = MongoDBObject.newBuilder
            builder += "product_ratio" -> x.getProduct_ratio.toDouble
            builder += "product_cost" -> x.getProduct_cost.toDouble
            builder += "region_northeast" -> x.getRegion_northeast.toDouble
            builder += "region_northchina" -> x.getRegion_northchina.toDouble
            builder += "region_eastchina" -> x.getRegion_eastchina.toDouble
            builder += "region_southchina" -> x.getRegion_southchina.toDouble
            builder += "region_westchina" -> x.getRegion_westchina.toDouble
            builder += "region_centralchina" -> x.getRegion_centralchina.toDouble
            builder += "region_beijingandtianjin" -> x.getRegion_beijingandtianjin.toDouble
            val year = x.getYear
            builder += "year" -> year.toInt
            builder += "budget_totle" -> x.getBudget_totle.toDouble
            val scope_name_ch = x.getScope_name_ch
            builder += "scope_name_ch" -> scope_name_ch
            val scope_name_en =  x.getScope_name_en
            builder += "scope_name_en" -> scope_name_en
            val company = x.getCompany
            builder += "company" -> company
            val department = x.getDepartment
            builder += "department" -> department
            builder += "cross_product_optimization" -> x.getCross_product_optimization
            builder += "sales_area_analysis" -> x.getSales_area_analysis
            builder += "last_year_sales" -> x.getLast_year_sales.toDouble
            builder += "date" -> new Date().getTime.asInstanceOf[Number]
            builder += "budget_id" -> Sercurity.md5Hash("budget seed" + scope_name_ch + scope_name_en + company + department + year + Sercurity.getTimeSpanWithMillSeconds)
            _data_connection.getCollection("budget") += builder.result()
        }
    }

    def importOptimization(list:List[ProductOptimization]):Unit = {
        list.foreach{x =>
            val builder = MongoDBObject.newBuilder
            builder += "need_optimization" -> x.getNeed_optimization
            builder += "now_budget" -> x.getNow_budget.toDouble
            builder += "now_budget_ratio" -> x.getNow_budget_ratio.toDouble
            builder += "measured_budget_ratio" -> x.getMeasured_budget_ratio.toDouble
            builder += "calculate_budget_values" -> x.getCalculate_budget_values.toDouble
            builder += "budget_variation" -> x.getBudget_variation
            builder += "budget_criteria" -> x.getBudget_criteria.toDouble
            builder += "income_standards" -> x.getIncome_standards.toDouble
            val channel_name_ch = x.getChannel_name_ch
            builder += "channel_name_ch" -> channel_name_ch
            val channel_name_en = x.getChannel_name_en
            builder += "channel_name_en" -> channel_name_en
            builder += "channel_description" -> x.getChannel_description
            val scope_name_ch = x.getScope_name_ch
            builder += "scope_name_ch" -> scope_name_ch
            val scope_name_en = x.getScope_name_en
            builder += "scope_name_en" -> scope_name_en
            val year = x.getYear
            builder += "year" -> year.toInt
            val company = x.getCompany
            builder += "company" -> company
            val department = x.getDepartment
            builder += "department" -> department
            builder += "date" -> new Date().getTime.asInstanceOf[Number]
            builder += "pro_opt_id" -> Sercurity.md5Hash("product optimization seed" + scope_name_ch + scope_name_en + channel_name_ch + channel_name_en + company + department + year + Sercurity.getTimeSpanWithMillSeconds)
            _data_connection.getCollection("productOptimization") += builder.result()
        }
    }
}