package bmlogic.naturalSales.naturalSalesData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 2017/6/29.
  */
trait NaturalSalesInfo {
    val condition : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder
        (js \ "estimate_id").asOpt[String].map (x => builder += "estimate_id" -> x).getOrElse(Unit)

        builder.result()
    }

    implicit val m2d : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        builder += "product_name" -> (js \ "product_name").asOpt[String].map (x => x).getOrElse(throw new Exception("natural sales input js error"))
        builder += "launch_date" -> (js \ "launch_date").asOpt[String].map (x => x).getOrElse(throw new Exception("natural sales input js error"))
        builder += "otc_flag" -> (js \ "otc_flag").asOpt[String].map (x => x).getOrElse(throw new Exception("natural sales input js error"))
        builder += "category" -> (js \ "category").asOpt[String].map (x => x).getOrElse(throw new Exception("natural sales input js error"))
        builder += "indication" -> (js \ "indication").asOpt[String].map (x => x).getOrElse(throw new Exception("natural sales input js error"))
        builder += "treatment_area" -> (js \ "treatment_area").asOpt[String].map (x => x).getOrElse(throw new Exception("natural sales input js error"))
        builder += "life_cycle" -> (js \ "life_cycle").asOpt[String].map (x => x).getOrElse(throw new Exception("natural sales input js error"))
        builder += "competition_level" -> (js \ "competition_level").asOpt[String].map (x => x).getOrElse(throw new Exception("natural sales input js error"))
        builder += "last_year_sales_growth_rate" -> (js \ "last_year_sales_growth_rate").asOpt[String].map (x => x).getOrElse(throw new Exception("natural sales input js error"))

        builder.result()
    }

    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "user_name" -> toJson(obj.getAs[String]("user_name").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "estimate_id" -> toJson(obj.getAs[String]("estimate_id").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "product_name" -> toJson(obj.getAs[String]("product_name").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "launch_date" -> toJson(obj.getAs[String]("launch_date").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "otc_flag" -> toJson(obj.getAs[String]("otc_flag").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "category" -> toJson(obj.getAs[String]("category").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "indication" -> toJson(obj.getAs[String]("indication").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "treatment_area" -> toJson(obj.getAs[String]("treatment_area").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "life_cycle" -> toJson(obj.getAs[String]("life_cycle").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "competition_level" -> toJson(obj.getAs[String]("competition_level").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "last_year_sales_growth_rate" -> toJson(obj.getAs[String]("last_year_sales_growth_rate").map(x => x).getOrElse(throw new Exception("natural sales output error"))),
            "create_date" -> toJson(obj.getAs[Long]("create_date").map(x => x).getOrElse(throw new Exception("natural sales output error")))
        )
    }
}
