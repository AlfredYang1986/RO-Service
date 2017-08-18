package bmlogic.query

import bminjection.db.DBTrait
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 17-7-13.
  */
case class ResponseCurvePlot(condition:DBObject, db:DBTrait) extends queryTrait{
    val list:List[Map[String, JsValue]] = db.queryMultipleObject(condition,db_name = "responseCurvePlot",take=0){obj =>
        Map(
            "user_name" -> toJson(obj.getAs[String]("user_name").map(x => x).getOrElse(throw new Exception("responseCurvePlot output error"))),
            "product_name" -> toJson(obj.getAs[String]("product_name").map(x => x).getOrElse(throw new Exception("responseCurvePlot output error"))),
            "create_date" -> toJson(obj.getAs[Long]("create_date").map(x => x).getOrElse(throw new Exception("responseCurvePlot output error"))),
            "channel_name_en" -> toJson(obj.getAs[String]("channel_name_en").map(x => x).getOrElse(throw new Exception("responseCurvePlot output error"))),
            "cost" -> toJson(obj.getAs[Int]("cost").map(x => x).getOrElse(throw new Exception("responseCurvePlot output error"))),
            "response" -> toJson(obj.getAs[Double]("response").map(x => x).getOrElse(throw new Exception("responseCurvePlot output error")))
        )
    }
}
