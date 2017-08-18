package bmlogic.products.ProductData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 17-6-14.
  */
trait ProductInfo {

    val condition: JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        (js \ "scope_id").asOpt[String].map(x => builder += "scope_id" -> x).getOrElse(Unit)
        (js \ "company").asOpt[String].map(x => builder += "company" -> x).getOrElse(Unit)
        (js \ "department").asOpt[String].map(x => builder += "department" -> x).getOrElse(Unit)
        (js \ "scope_name_ch").asOpt[String].map(x => builder += "scope_name_ch" -> x).getOrElse(Unit)
        (js \ "scope_name_en").asOpt[String].map(x => builder += "scope_name_en" -> x).getOrElse(Unit)
        builder.result()
    }

    implicit val m2d: JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        builder += "company" -> (js \ "company").asOpt[String].map(x => x).getOrElse(throw new Exception("product input js error"))
        builder += "department" -> (js \ "department").asOpt[String].map(x => x).getOrElse(throw new Exception("product input js error"))
        builder += "scope_name_ch" -> (js \ "scope_name_ch").asOpt[String].map(x => x).getOrElse(throw new Exception("product input js error"))
        builder += "scope_name_en" -> (js \ "scope_name_en").asOpt[String].map(x => x).getOrElse(throw new Exception("product input js error"))

        builder.result()
    }

    implicit val d2m: DBObject => Map[String, JsValue] = { o =>
        Map(
            "scope_id" -> toJson(o.getAs[String]("scope_id").map(x => x).getOrElse(throw new Exception("product output error"))),
            "company" -> toJson(o.getAs[String]("company").map(x => x).getOrElse(throw new Exception("product output error"))),
            "department" -> toJson(o.getAs[String]("department").map(x => x).getOrElse(throw new Exception("product output error"))),
            "scope_name_ch" -> toJson(o.getAs[String]("scope_name_ch").map(x => x).getOrElse(throw new Exception("product output error"))),
            "scope_name_en" -> toJson(o.getAs[String]("scope_name_en").map(x => x).getOrElse(throw new Exception("product output error"))),
            "date" -> toJson(o.getAs[Long]("date").map(x => x).getOrElse(throw new Exception("product output error")))
        )
    }
}
