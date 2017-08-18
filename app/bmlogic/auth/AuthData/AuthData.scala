package bmlogic.auth.AuthData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by alfredyang on 01/06/2017.
  */
trait AuthData {

    val condition : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        (js \ "user_id").asOpt[String].map (x => builder += "user_id" -> x).getOrElse(Unit)
        (js \ "company").asOpt[String].map (x => builder += "company" -> x).getOrElse(Unit)
        (js \ "department").asOpt[String].map (x => builder += "department" -> x).getOrElse(Unit)

        builder.result()
    }
    
    implicit val m2d : JsValue => DBObject = { js =>
        val build = MongoDBObject.newBuilder
        val user_name = (js \ "user_name").asOpt[String].map (x => x).getOrElse(throw new Exception("users input js error"))
        val pwd = (js \ "pwd").asOpt[String].map (x => x).getOrElse(throw new Exception("users input js error"))
        build += "user_name" -> user_name
        build += "pwd" -> pwd

        build += "screen_name" -> (js \ "screen_name").asOpt[String].map (x => x).getOrElse("")
        build += "screen_photo" -> (js \ "screen_photo").asOpt[String].map (x => x).getOrElse("")
        build += "phoneNo" -> (js \ "phoneNo").asOpt[String].map (x => x).getOrElse("")
        build += "email" -> (js \ "email").asOpt[String].map (x => x).getOrElse("")

        build += "company" -> (js \ "company").asOpt[String].map (x => x).getOrElse(throw new Exception("users input js error"))
        build += "department" -> (js \ "department").asOpt[String].map (x => x).getOrElse(throw new Exception("users input js error"))

        build.result
    }

    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "user_id" -> toJson(obj.getAs[String]("user_id").map (x => x).getOrElse(throw new Exception("users output error"))),
            "user_name" -> toJson(obj.getAs[String]("user_name").map (x => x).getOrElse(throw new Exception("users output error"))),
            "pwd" -> toJson(obj.getAs[String]("pwd").map (x => x).getOrElse(throw new Exception("users output error"))),
            "phoneNo" -> toJson(obj.getAs[String]("phoneNo").map (x => x).getOrElse(throw new Exception("users output error"))),
            "email" -> toJson(obj.getAs[String]("email").map (x => x).getOrElse(throw new Exception("users output error"))),
            "screen_name" -> toJson(obj.getAs[String]("screen_name").map (x => x).getOrElse(throw new Exception("users output error"))),
            "screen_photo" -> toJson(obj.getAs[String]("screen_photo").map (x => x).getOrElse(throw new Exception("users output error"))),
            "company" -> toJson(obj.getAs[String]("company").map (x => x).getOrElse(throw new Exception("users output error"))),
            "department" -> toJson(obj.getAs[String]("department").map (x => x).getOrElse(throw new Exception("users output error"))),
            "date" -> toJson(obj.getAs[Long]("date").map (x => x).getOrElse(throw new Exception("users output error")))
        )
    }
}
