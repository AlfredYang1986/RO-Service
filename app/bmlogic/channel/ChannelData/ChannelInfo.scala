package bmlogic.channel.ChannelData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by liwei on 2017/6/19.
  */
trait ChannelInfo {

    val condition : JsValue => DBObject = { js =>

        val builder = MongoDBObject.newBuilder

        (js \ "channel_id").asOpt[String].map (x => builder += "channel_id" -> x).getOrElse(Unit)
        (js \ "company").asOpt[String].map (x => builder += "company" -> x).getOrElse(Unit)
        (js \ "department").asOpt[String].map (x => builder += "department" -> x).getOrElse(Unit)
        (js \ "scope").asOpt[String].map (x => builder += "scope" -> x).getOrElse(Unit)
        (js \ "channel_ch").asOpt[String].map (x => builder += "channel_ch" -> x).getOrElse(Unit)
        (js \ "channel_en").asOpt[String].map (x => builder += "channel_en" -> x).getOrElse(Unit)

        builder.result()
    }

    implicit val m2d : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        builder += "company" -> (js \ "company").asOpt[String].map (x => x).getOrElse(throw new Exception("channel input js error"))
        builder += "department" -> (js \ "department").asOpt[String].map (x => x).getOrElse(throw new Exception("channel input js error"))
        builder += "scope" -> (js \ "scope").asOpt[String].map (x => x).getOrElse(throw new Exception("channel input js error"))
        builder += "channel_ch" -> (js \ "channel_ch").asOpt[String].map (x => x).getOrElse(throw new Exception("channel input js error"))
        builder += "channel_en" -> (js \ "channel_en").asOpt[String].map (x => x).getOrElse(throw new Exception("channel input js error"))
        builder += "channel_description" -> (js \ "channel_description").asOpt[String].map (x => x).getOrElse(throw new Exception("channel input js error"))

        builder.result()
    }

    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "channel_id" -> toJson(obj.getAs[String]("channel_id").map (x => x).getOrElse(throw new Exception("channel output error"))),
            "company" -> toJson(obj.getAs[String]("company").map (x => x).getOrElse(throw new Exception("channel output error"))),
            "department" -> toJson(obj.getAs[String]("department").map (x => x).getOrElse(throw new Exception("channel output error"))),
            "scope" -> toJson(obj.getAs[String]("scope").map (x => x).getOrElse(throw new Exception("channel output error"))),
            "channel_ch" -> toJson(obj.getAs[String]("channel_ch").map (x => x).getOrElse(throw new Exception("channel output error"))),
            "channel_en" -> toJson(obj.getAs[String]("channel_en").map (x => x).getOrElse(throw new Exception("channel output error"))),
            "channel_description" -> toJson(obj.getAs[String]("channel_description").map (x => x).getOrElse(throw new Exception("channel output error"))),
            "date" -> toJson(obj.getAs[Long]("date").map (x => x).getOrElse(throw new Exception("channel output error")))
        )
    }
}
