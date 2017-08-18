package bmlogic.company.CompanyData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by liwei on 2017/8/1.
  */
trait CompanyInfo {
    val condition : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        (js \ "company_id").asOpt[String].map (x => builder += "company_id" -> x).getOrElse(Unit)
        (js \ "company_name_en").asOpt[String].map (x => builder += "company_name_en" -> x).getOrElse(Unit)
        (js \ "company_name_ch").asOpt[String].map (x => builder += "company_name_ch" -> x).getOrElse(Unit)
        (js \ "short_name").asOpt[String].map (x => builder += "short_name" -> x).getOrElse(Unit)

        builder.result()
    }

    implicit val m2d : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        builder += "company_name_en" -> (js \ "company_name_en").asOpt[String].map (x => x).getOrElse(throw new Exception("company input js error"))
        builder += "company_name_ch" -> (js \ "company_name_ch").asOpt[String].map (x => x).getOrElse(throw new Exception("company input js error"))
        builder += "company_photo" -> (js \ "company_photo").asOpt[String].map (x => x).getOrElse("")
        builder += "short_name" -> (js \ "short_name").asOpt[String].map (x => x).getOrElse("")

        builder.result()
    }

    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "company_id" -> toJson(obj.getAs[String]("company_id").map (x => x).getOrElse(throw new Exception("company output error"))),
            "company_name_en" -> toJson(obj.getAs[String]("company_name_en").map (x => x).getOrElse(throw new Exception("company output error"))),
            "company_name_ch" -> toJson(obj.getAs[String]("company_name_ch").map (x => x).getOrElse(throw new Exception("company output error"))),
            "company_photo" -> toJson(obj.getAs[String]("company_photo").map (x => x).getOrElse(throw new Exception("company output error"))),
            "short_name" -> toJson(obj.getAs[String]("short_name").map (x => x).getOrElse(throw new Exception("company output error"))),
            "date" -> toJson(obj.getAs[Long]("date").map (x => x).getOrElse(throw new Exception("company output error")))
        )
    }
}
