package bmlogic.query

import bminjection.db.DBTrait
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 17-7-13.
  */
case class Retention(condition:DBObject,db:DBTrait) extends queryTrait{
    val list:List[Map[String, JsValue]] = db.queryMultipleObject(condition, db_name = "retention", take = 0) { obj =>
            Map(
                "retention_id" -> toJson(obj.getAs[Int]("retention_id").map(x => x).getOrElse(throw new Exception("retention output error"))),
                "user_name" -> toJson(obj.getAs[String]("user_name").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "product_name" -> toJson(obj.getAs[String]("product_name").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "create_date" -> toJson(obj.getAs[Long]("create_date").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "channel_name_en" -> toJson(obj.getAs[String]("channel_name_en").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "channel_name_zh" -> toJson(obj.getAs[String]("channel_name_zh").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "effective_scope" -> toJson(obj.getAs[String]("effective_scope").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "competition" -> toJson(obj.getAs[String]("competition").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "gain_rate" -> toJson(obj.getAs[Int]("gain_rate").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "invest_percentage" -> toJson(obj.getAs[String]("invest_percentage").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "effective" -> toJson(obj.getAs[String]("effective").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "invest_effective_flag" -> toJson(obj.getAs[String]("invest_effective_flag").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
                "effective_gap" -> toJson(obj.getAs[String]("effective_gap").map(x => x).getOrElse(throw new Exception("channel cost output error")))
            )
        }
}
