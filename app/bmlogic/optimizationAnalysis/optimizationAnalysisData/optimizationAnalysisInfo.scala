package bmlogic.optimizationAnalysis.optimizationAnalysisData

import com.mongodb.casbah.Imports._
import play.api.libs.json.{JsString, JsValue}
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 2017/6/29.
  */
trait optimizationAnalysisInfo {
    val condition : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder
        (js \ "analysis_id").asOpt[String].map (x => builder += "analysis_id" -> x).getOrElse(Unit)

        builder.result()
    }

    //根据用户名、产品名称、渠道名称、最近日期查询是否有已操作数据存在
    def findByCondition(x: Map[String, JsValue],minTime:Long,maxTime:Long): DBObject = {
        $and(
            "create_date" -> MongoDBObject("$gte" -> minTime),
            "create_date" -> MongoDBObject("$lte" -> maxTime),
            "user_name" -> x("user_name").asInstanceOf[JsString].value,
            "channel_name_en" -> x("channel_name_en").asInstanceOf[JsString].value,
            "product_name" -> x("product_name").asInstanceOf[JsString].value
        )
    }

    implicit val m2d : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        builder += "user_name" -> (js \ "user_name").asOpt[String].map (x => x).getOrElse(throw new Exception("optimization analysis input js error"))
        builder += "product_name" -> (js \ "product_name").asOpt[String].map (x => x).getOrElse(throw new Exception("optimization analysis input js error"))
        builder += "create_date" -> (js \ "create_date").asOpt[Long].map (x => x).getOrElse(throw new Exception("optimization analysis input js error"))
        builder += "channel_name_en" -> (js \ "channel_name_en").asOpt[String].map (x => x).getOrElse(throw new Exception("optimization analysis input js error"))
        builder += "status" -> (js \ "status").asOpt[Int].map (x => x).getOrElse(0)
        builder += "opt_flag" -> (js \ "opt_flag").asOpt[String].map (x => x).getOrElse("Yes")
        builder += "min_spend" -> (js \ "min_spend").asOpt[Int].map (x => x).getOrElse(0)
        builder += "max_spend" -> (js \ "max_spend").asOpt[Int].map (x => x).getOrElse(100000)
        builder += "marginal_cost" -> (js \ "marginal_cost").asOpt[Int].map (x => x).getOrElse(0)

        builder.result()
    }

    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "analysis_id" -> toJson(obj.getAs[String]("analysis_id").map(x => x).getOrElse(throw new Exception("optimization analysis output error"))),
            "product_name" -> toJson(obj.getAs[String]("product_name").map(x => x).getOrElse(throw new Exception("optimization analysis output error"))),
            "channel_name_en" -> toJson(obj.getAs[String]("channel_name_en").map(x => x).getOrElse(throw new Exception("optimization analysis output error"))),
            "opt_flag" -> toJson(obj.getAs[String]("opt_flag").map(x => x).getOrElse(throw new Exception("optimization analysis output error"))),
            "min_spend" -> toJson(obj.getAs[Int]("min_spend").map(x => x).getOrElse(throw new Exception("optimization analysis output error"))),
            "max_spend" -> toJson(obj.getAs[Int]("max_spend").map(x => x).getOrElse(throw new Exception("optimization analysis output error"))),
            "marginal_cost" -> toJson(obj.getAs[Int]("marginal_cost").map(x => x).getOrElse(throw new Exception("optimization analysis output error"))),
            "user_name" -> toJson(obj.getAs[String]("user_name").map(x => x).getOrElse(throw new Exception("optimization analysis output error"))),
            "create_date" -> toJson(obj.getAs[Long]("create_date").map(x => x).getOrElse(throw new Exception("optimization analysis output error"))),
            "status" -> toJson(obj.getAs[Int]("status").map(x => x).getOrElse(throw new Exception("optimization analysis output error")))
        )
    }
}
