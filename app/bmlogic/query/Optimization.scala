package bmlogic.query

import bminjection.db.DBTrait
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 17-7-13.
  */
case class Optimization(condition:DBObject,db:DBTrait) extends queryTrait{
    val list:List[Map[String, JsValue]] = db.queryMultipleObject(condition, db_name = "optimizationTemplate", take = 0) { obj =>
        Map(
            "user_name" -> toJson(obj.getAs[String]("user_name").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "product_name" -> toJson(obj.getAs[String]("product_name").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "create_date" -> toJson(obj.getAs[Long]("create_date").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "channel_name_en" -> toJson(obj.getAs[String]("channel_name_en").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "current_cost" -> toJson(obj.getAs[Double]("current_cost").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "current_contribution" -> toJson(obj.getAs[Double]("current_contribution").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "current_value" -> toJson(obj.getAs[Double]("current_value").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "opt_cost" -> toJson(obj.getAs[Double]("opt_cost").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "opt_contribution" -> toJson(obj.getAs[Double]("opt_contribution").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "opt_type" -> toJson(obj.getAs[String]("opt_type").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "opt_value" -> toJson(obj.getAs[Double]("opt_value").map(x => x).getOrElse(throw new Exception("optimization output error"))),
            "uplift" -> toJson(obj.getAs[Double]("uplift").map(x => x).getOrElse(throw new Exception("optimization output error")))
        )
    }

    def groupByCC:List[Map[String, JsValue]] = {
        list.groupBy{x =>
            x.get("channel_name")
            x.get("current_cost")
        }.toList.sortBy{y => y._2.head("current_cost").asOpt[Double].get}.reverse.map(_._2.head)
    }
}
