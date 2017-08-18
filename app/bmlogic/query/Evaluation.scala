package bmlogic.query

import bminjection.db.DBTrait
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 17-7-13.
  */
case class Evaluation(condition:DBObject, db:DBTrait) extends queryTrait{
    val list:List[Map[String, JsValue]] = db.queryMultipleObject(condition, db_name = "evaluation", take = 0) { obj =>
        Map(
            "user_name" -> toJson(obj.getAs[String]("user_name").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "product_name" -> toJson(obj.getAs[String]("product_name").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "create_date" -> toJson(obj.getAs[Long]("create_date").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "channel_name_en" -> toJson(obj.getAs[String]("channel_name_en").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "channel_name_zh" -> toJson(obj.getAs[String]("channel_name_zh").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "effective" -> toJson(obj.getAs[String]("effective").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "effective_scope" -> toJson(obj.getAs[String]("effective_scope").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "competition" -> toJson(obj.getAs[String]("competition").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "gain_rate" -> toJson(obj.getAs[Int]("gain_rate").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "invest_percentage" -> toJson(obj.getAs[String]("invest_percentage").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "invest_effective_flag" -> toJson(obj.getAs[String]("invest_effective_flag").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "effective_gap" -> toJson(obj.getAs[String]("effective_gap").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "retention_rate" -> toJson(obj.getAs[Double]("retention_rate").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "final_retention" -> toJson(obj.getAs[Double]("final_retention").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "curve_type" -> toJson(obj.getAs[String]("curve_type").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "carry_over_range" -> toJson(obj.getAs[String]("carry_over_range").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "carry_over" -> toJson(obj.getAs[Double]("carry_over").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "cost_current" -> toJson(obj.getAs[Int]("cost_current").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "contribution" -> toJson(obj.getAs[Double]("contribution").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "roi" -> toJson(obj.getAs[Double]("roi").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "mroi" -> toJson(obj.getAs[Double]("mroi").map(x => x).getOrElse(throw new Exception("evaluation output error"))),
            "rSqure" -> toJson(obj.getAs[Double]("r_squre").map(x => x).getOrElse(throw new Exception("evaluation output error")))
        )
    }
}
