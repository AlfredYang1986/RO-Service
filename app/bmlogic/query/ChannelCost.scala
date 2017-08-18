package bmlogic.query

import bminjection.db.DBTrait
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 17-7-13.
  */
case class ChannelCost(condition:DBObject, db:DBTrait) extends queryTrait{
    val list:List[Map[String, JsValue]] = db.queryMultipleObject(condition, db_name="channelCost",take=0){obj =>
        Map(
            "user_name" -> toJson(obj.getAs[String]("user_name").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "product_name" -> toJson(obj.getAs[String]("product_name").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "create_date" -> toJson(obj.getAs[Long]("create_date").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "date" -> toJson(obj.getAs[String]("date").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "channel_name_en" -> toJson(obj.getAs[String]("channel_name_en").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "sales" -> toJson(obj.getAs[Int]("sales").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "cost" -> toJson(obj.getAs[Int]("cost").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "year" -> toJson(obj.getAs[Int]("year").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "month" -> toJson(obj.getAs[Int]("month").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "mth_cnt" -> toJson(obj.getAs[Int]("mth_cnt").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "total_sales" -> toJson(obj.getAs[Int]("total_sales").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "total_sales_adj" -> toJson(obj.getAs[Int]("total_sales_adj").map(x => x).getOrElse(throw new Exception("channel cost output error"))),
            "sales_growth" -> toJson(obj.getAs[Double]("sales_growth").map(x => x).getOrElse(0D))
        )
    }
}
