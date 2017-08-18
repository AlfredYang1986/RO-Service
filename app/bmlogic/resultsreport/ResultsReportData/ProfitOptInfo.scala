package bmlogic.resultsreport.ResultsReportData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import bmutil.DateUtils._

/**
  * Created by liwei on 2017/6/22.
  */
trait ProfitOptInfo {
    val condition : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        (js \ "department").asOpt[String].map (x => builder += "role" -> x).getOrElse(Unit)
        (js \ "scope_name_ch").asOpt[String].map (x => builder += "scope_name_ch" -> x).getOrElse(Unit)

        builder.result()
    }

    implicit val d2m1 : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "opt_type" -> toJson(obj.getAs[String]("opt_type").map (x => x).getOrElse(throw new Exception("profit opt output error"))),
            "role" -> toJson(obj.getAs[String]("role").map (x => x).getOrElse(throw new Exception("profit opt output error"))),
            "times" -> toJson(obj.getAs[Number]("times").map (x => x).getOrElse(throw new Exception("profit opt output error")).intValue()),
            "products" -> toJson(obj.getAs[String]("products").map (x => x).getOrElse(throw new Exception("profit opt output error"))),
            "current_cost_prod" -> toJson(obj.getAs[Number]("current_cost_prod").map (x => x).getOrElse(throw new Exception("profit opt output error")).doubleValue()),
            "current_profit" -> toJson(obj.getAs[Number]("current_profit").map (x => x).getOrElse(throw new Exception("profit opt output error")).doubleValue()),
            "max_profit" -> toJson(obj.getAs[Number]("max_profit").map (x => x).getOrElse(throw new Exception("profit opt output error")).doubleValue()),
            "channels" -> toJson(obj.getAs[String]("channels").map (x => x).getOrElse(throw new Exception("profit opt output error"))),
            "channel_current_cost" -> toJson(obj.getAs[Number]("channel_current_cost").map (x => x).getOrElse(throw new Exception("profit opt output error")).doubleValue()),
            "channel_opt_cost" -> toJson(obj.getAs[Number]("channel_opt_cost").map (x => x).getOrElse(throw new Exception("profit opt output error")).doubleValue()),
            "channel_current_cost_ratio" -> toJson(obj.getAs[Number]("channel_current_cost_ratio").map (x => x).getOrElse(throw new Exception("profit opt output error")).doubleValue()),
            "channel_opt_cost_ratio" -> toJson(obj.getAs[Number]("channel_opt_cost_ratio").map (x => x).getOrElse(throw new Exception("profit opt output error")).doubleValue()),
            "profit_uplift_prod" -> toJson(obj.getAs[Number]("profit_uplift_prod").map (x => x).getOrElse(throw new Exception("profit opt output error")).doubleValue()),
            "scope_name_ch" -> toJson(obj.getAs[String]("scope_name_ch").map (x => x).getOrElse(throw new Exception("profit opt output error"))),
            "channel_name_ch" -> toJson(obj.getAs[String]("channel_name_ch").map (x => x).getOrElse(throw new Exception("profit opt output error"))),
            "channel_description" -> toJson(obj.getAs[String]("channel_description").map (x => x).getOrElse(throw new Exception("profit opt output error"))),
            "date" -> toJson(timeStamp2yyyyMMddHHmmss(obj.getAs[Number]("date").map (x => x).getOrElse(throw new Exception("profit opt output error")).longValue()))
        )
    }
}
