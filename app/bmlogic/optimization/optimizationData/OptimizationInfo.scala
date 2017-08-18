package bmlogic.optimization.optimizationData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by liwei on 2017/6/20.
  */
trait OptimizationInfo {
    val condition : JsValue => DBObject = { js =>

        val builder = MongoDBObject.newBuilder

        (js \ "pro_opt_id").asOpt[String].map (x => builder += "pro_opt_id" -> x).getOrElse(Unit)
        (js \ "company").asOpt[String].map (x => builder += "company" -> x).getOrElse(Unit)
        (js \ "department").asOpt[String].map (x => builder += "department" -> x).getOrElse(Unit)
        (js \ "scope_name_ch").asOpt[String].map (x => builder += "scope_name_ch" -> x).getOrElse(Unit)
        (js \ "scope_name_en").asOpt[String].map (x => builder += "scope_name_en" -> x).getOrElse(Unit)

        builder.result()
    }

    implicit val m2d : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        builder += "need_optimization" -> (js \ "need_optimization").asOpt[String].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "now_budget" -> (js \ "now_budget").asOpt[Double].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "now_budget_ratio" -> (js \ "now_budget_ratio").asOpt[Double].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "measured_budget_ratio" -> (js \ "measured_budget_ratio").asOpt[Double].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "calculate_budget_values" -> (js \ "calculate_budget_values").asOpt[Double].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "budget_variation" -> (js \ "budget_variation").asOpt[String].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "budget_criteria" -> (js \ "budget_criteria").asOpt[Double].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "income_standards" -> (js \ "income_standards").asOpt[Double].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "channel_name_ch" -> (js \ "channel_name_ch").asOpt[String].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "channel_name_en" -> (js \ "channel_name_en").asOpt[String].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "channel_description" -> (js \ "channel_description").asOpt[String].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "scope_name_ch" -> (js \ "scope_name_ch").asOpt[String].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "scope_name_en" -> (js \ "scope_name_en").asOpt[String].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "year" -> (js \ "year").asOpt[Int].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "company" -> (js \ "company").asOpt[String].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "department" -> (js \ "department").asOpt[String].map (x => x).getOrElse(throw new Exception("result report input js error"))
        builder += "date" -> (js \ "date").asOpt[Long].map (x => x).getOrElse(throw new Exception("result report input js error"))

        builder.result()
    }

    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "year" -> toJson(obj.getAs[Int]("year").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "company" -> toJson(obj.getAs[String]("company").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "department" -> toJson(obj.getAs[String]("department").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "scope_name_ch" -> toJson(obj.getAs[String]("scope_name_ch").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "scope_name_en" -> toJson(obj.getAs[String]("scope_name_en").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "channel_name_en" -> toJson(obj.getAs[String]("channel_name_en").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "channel_name_ch" -> toJson(obj.getAs[String]("channel_name_ch").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "channel_description" -> toJson(obj.getAs[String]("channel_description").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "need_optimization" -> toJson(obj.getAs[String]("need_optimization").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "now_budget" -> toJson(obj.getAs[Double]("now_budget").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "now_budget_ratio" -> toJson(obj.getAs[Double]("now_budget_ratio").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "measured_budget_ratio" -> toJson(obj.getAs[Double]("measured_budget_ratio").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "calculate_budget_values" -> toJson(obj.getAs[Double]("calculate_budget_values").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "budget_variation" -> toJson(obj.getAs[String]("budget_variation").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "budget_criteria" -> toJson(obj.getAs[Double]("budget_criteria").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "income_standards" -> toJson(obj.getAs[Double]("income_standards").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "pro_opt_id" -> toJson(obj.getAs[String]("pro_opt_id").map(x => x).getOrElse(throw new Exception("result report output error"))),
            "date" -> toJson(obj.getAs[Long]("date").map(x => x).getOrElse(throw new Exception("result report output error")))
        )
    }
}
