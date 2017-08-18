package bmlogic.department.DepartmentData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import bmlogic.common.AuthEnum._

import scala.collection.immutable.Map

/**
  * Created by liwei on 2017/6/19.
  */
trait DepartmentInfo {

    val condition : JsValue => DBObject = { js =>

        val builder = MongoDBObject.newBuilder

        (js \ "department_id").asOpt[String].map (x => builder += "department_id" -> x).getOrElse(Unit)
        (js \ "company").asOpt[String].map (x => builder += "company" -> x).getOrElse(Unit)
        (js \ "department_name").asOpt[String].map (x => builder += "department_name" -> x).getOrElse(Unit)
        (js \ "short_name").asOpt[String].map (x => builder += "short_name" -> x).getOrElse(Unit)
        (js \ "is_admin").asOpt[Int].map (x => builder += "is_admin" -> x).getOrElse(Unit)

        builder.result()
    }

    implicit val m2d : JsValue => DBObject = { js =>
        val builder = MongoDBObject.newBuilder

        builder += "company" -> (js \ "company").asOpt[String].map (x => x).getOrElse(throw new Exception("department input js error"))
        builder += "department_name" -> (js \ "department_name").asOpt[String].map (x => x).getOrElse(throw new Exception("department input js error"))
        builder += "short_name" -> (js \ "short_name").asOpt[String].map (x => x).getOrElse(throw new Exception("department input js error"))
        builder += "is_admin" -> (js \ "is_admin").get.asOpt[String].map (x => x.toInt.asInstanceOf[Number]).getOrElse(throw new Exception("department input js error"))

        builder.result()
    }

    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "department_id" -> toJson(obj.getAs[String]("department_id").map (x => x).getOrElse(throw new Exception("department output error"))),
            "company" -> toJson(obj.getAs[String]("company").map (x => x).getOrElse(throw new Exception("department output error"))),
            "department_name" -> toJson(obj.getAs[String]("department_name").map (x => x).getOrElse(throw new Exception("department output error"))),
            "short_name" -> toJson(obj.getAs[String]("short_name").map (x => x).getOrElse(throw new Exception("department output error"))),
            "is_admin" -> toJson(obj.getAs[Int]("is_admin").map (x => IntToString(x)).getOrElse(throw new Exception("department output error"))),
            "date" -> toJson(obj.getAs[Long]("date").map (x => x).getOrElse(throw new Exception("department output error")))
        )
    }
}
