package bmlogic.residual.ResidualData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 2017/7/1.
  */
trait ResidualInfo {
    val condition : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder
        (js \ "estimateId").asOpt[String].map (x => builder += "estimate_id" -> x).getOrElse(Unit)

        builder.result()
    }

    implicit val m2d : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        builder += "product_name" -> (js \ "productName").asOpt[String].map (x => x).getOrElse(throw new Exception("residual input js error"))
        builder += "channel_name" -> (js \ "channelName").asOpt[String].map (x => x).getOrElse(throw new Exception("residual input js error"))
        builder += "channel_effective_scope" -> (js \ "channelEffectiveScope").asOpt[String].map (x => x).getOrElse(throw new Exception("residual input js error"))
        builder += "competition" -> (js \ "competition").asOpt[String].map (x => x).getOrElse(throw new Exception("residual input js error"))
        builder += "gain_rate" -> (js \ "gainRate").asOpt[String].map (x => x).getOrElse(throw new Exception("residual input js error"))
        builder += "channel_invest_percentage" -> (js \ "channelInvestPercentage").asOpt[String].map (x => x).getOrElse(throw new Exception("residual input js error"))
        builder += "channel_effective" -> (js \ "channelEffect").asOpt[String].map (x => x).getOrElse(throw new Exception("residual input js error"))
        builder += "invest_floor" -> (js \ "investFloor").asOpt[String].map (x => x).getOrElse(throw new Exception("residual input js error"))
        builder += "invest_ceiling" -> (js \ "investCeiling").asOpt[String].map (x => x).getOrElse(throw new Exception("residual input js error"))

        builder.result()
    }

    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "estimateId" -> toJson(obj.getAs[String]("estimate_id").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "productName" -> toJson(obj.getAs[String]("product_name").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "channelName" -> toJson(obj.getAs[String]("channel_name").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "channelEffectiveScope" -> toJson(obj.getAs[String]("channel_effective_scope").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "competition" -> toJson(obj.getAs[String]("competition").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "gainRate" -> toJson(obj.getAs[String]("gain_rate").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "channelInvestPercentage" -> toJson(obj.getAs[String]("channel_invest_percentage").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "channelEffect" -> toJson(obj.getAs[String]("channel_effective").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "investFloor" -> toJson(obj.getAs[String]("invest_floor").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "investCeiling" -> toJson(obj.getAs[String]("invest_ceiling").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "createDate" -> toJson(obj.getAs[Long]("create_date").map(x => x).getOrElse(throw new Exception("residual output error"))),
            "userName" -> toJson(obj.getAs[String]("user_name").map(x => x).getOrElse(throw new Exception("residual output error")))
        )
    }
}
