package bmlogic.budget.BudgetData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by liwei on 2017/6/15.
  */
trait BudgetInfo {

    val condition : JsValue => DBObject = { js =>

        val builder = MongoDBObject.newBuilder

        (js \ "budget_id").asOpt[String].map (x => builder += "budget_id" -> x).getOrElse(Unit)
        (js \ "company").asOpt[String].map (x => builder += "company" -> x).getOrElse(Unit)
        (js \ "department").asOpt[String].map (x => builder += "department" -> x).getOrElse(Unit)
        (js \ "scope_name_ch").asOpt[String].map (x => builder += "scope_name_ch" -> x).getOrElse(Unit)
        (js \ "scope_name_en").asOpt[String].map (x => builder += "scope_name_en" -> x).getOrElse(Unit)

        builder.result()
    }

    implicit val m2d : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        builder += "product_ratio" -> (js \ "product_ratio").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "product_cost" -> (js \ "product_cost").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "region_northeast" -> (js \ "region_northeast").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "region_northchina" -> (js \ "region_northchina").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "region_eastchina" -> (js \ "region_eastchina").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "region_southchina" -> (js \ "region_southchina").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "region_westchina" -> (js \ "region_westchina").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "region_centralchina" -> (js \ "region_centralchina").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "region_beijingandtianjin" -> (js \ "region_beijingandtianjin").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "year" -> (js \ "year").asOpt[Int].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "budget_totle" -> (js \ "budget_totle").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "scope_name_ch" -> (js \ "scope_name_ch").asOpt[String].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "scope_name_en" -> (js \ "scope_name_en").asOpt[String].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "company" -> (js \ "company").asOpt[String].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "department" -> (js \ "department").asOpt[String].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "cross_product_optimization" -> (js \ "cross_product_optimization").asOpt[String].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "sales_area_analysis" -> (js \ "sales_area_analysis").asOpt[String].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "last_year_sales" -> (js \ "last_year_sales").asOpt[Double].map (x => x).getOrElse(throw new Exception("budget input js error"))
        builder += "date" -> (js \ "date").asOpt[Long].map (x => x).getOrElse(throw new Exception("budget input js error"))

        builder.result()
    }

    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(

            "year" -> toJson(obj.getAs[Int]("year").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "company" -> toJson(obj.getAs[String]("company").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "department" -> toJson(obj.getAs[String]("department").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "scope_name_ch" -> toJson(obj.getAs[String]("scope_name_ch").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "scope_name_en" -> toJson(obj.getAs[String]("scope_name_en").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "cross_product_optimization" -> toJson(obj.getAs[String]("cross_product_optimization").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "sales_area_analysis" -> toJson(obj.getAs[String]("sales_area_analysis").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "budget_totle" -> toJson(obj.getAs[Double]("budget_totle").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "product_ratio" -> toJson(obj.getAs[Double]("product_ratio").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "product_cost" -> toJson(obj.getAs[Double]("product_cost").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "region_northeast" -> toJson(obj.getAs[Double]("region_northeast").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "region_northchina" -> toJson(obj.getAs[Double]("region_northchina").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "region_eastchina" -> toJson(obj.getAs[Double]("region_eastchina").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "region_southchina" -> toJson(obj.getAs[Double]("region_southchina").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "region_westchina" -> toJson(obj.getAs[Double]("region_westchina").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "region_centralchina" -> toJson(obj.getAs[Double]("region_centralchina").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "region_beijingandtianjin" -> toJson(obj.getAs[Double]("region_beijingandtianjin").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "budget_id" -> toJson(obj.getAs[String]("budget_id").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "last_year_sales" -> toJson(obj.getAs[Double]("last_year_sales").map(x => x).getOrElse(throw new Exception("budget output error"))),
            "date" -> toJson(obj.getAs[Long]("date").map(x => x).getOrElse(throw new Exception("budget output error")))
        )
    }


}
