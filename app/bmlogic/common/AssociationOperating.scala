package bmlogic.common

import bminjection.db.DBTrait
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by liwei on 2017/8/9.
  */
case class associationRemove() extends AssociationOperating{
    def apply(data : JsValue, db : DBTrait,buss : String) = buss match {
        case "company" => {
            val o = condition(data)
            implicit val d2m: DBObject => Map[String, JsValue] = { o =>
                Map("company" -> toJson(o.getAs[String]("company_name_en").map(x => x).getOrElse(throw new Exception("company output error"))))
            }
            val company = db.queryObject(condition = o, db_name = "company").get
            db.deleteMultipleObject(condition = m2d(company,buss),db_name = "department")
            db.deleteMultipleObject(condition = m2d(company,buss),db_name = "product")
            db.deleteMultipleObject(condition = m2d(company,buss),db_name = "channel")
            db.deleteMultipleObject(condition = m2d(company,buss),db_name = "users")
        }
        case "department" => {
            val o = condition(data)
            implicit val d2m: DBObject => Map[String, JsValue] = { o =>
                Map(
                    "company" -> toJson(o.getAs[String]("company").map(x => x).getOrElse(throw new Exception("department output error"))),
                    "department" -> toJson(o.getAs[String]("department_name").map(x => x).getOrElse(throw new Exception("department output error")))
                )
            }
            val department = db.queryObject(condition = o, db_name = "department").get
            db.deleteMultipleObject(condition = m2d(department,buss),db_name = "product")
            db.deleteMultipleObject(condition = m2d(department,buss),db_name = "channel")
            db.deleteMultipleObject(condition = m2d(department,buss),db_name = "users")
        }
        case "product" => {
            val o = condition(data)
            implicit val d2m: DBObject => Map[String, JsValue] = { o =>
                Map(
                    "company" -> toJson(o.getAs[String]("company").map(x => x).getOrElse(throw new Exception("product output error"))),
                    "department" -> toJson(o.getAs[String]("department").map(x => x).getOrElse(throw new Exception("product output error"))),
                    "scope" -> toJson(o.getAs[String]("scope_name_ch").map(x => x).getOrElse(throw new Exception("product output error")))
                )
            }
            val scope = db.queryObject(condition = o, db_name = "product").get
            db.deleteMultipleObject(condition = m2d(scope,buss),db_name = "channel")
        }
    }
}

case class associationUpdate() extends AssociationOperating{
    def apply(data : JsValue, db : DBTrait,buss : String) = buss match {
        case "company" => {
            val o = condition(data)
            implicit val d2m: DBObject => Map[String, JsValue] = { o =>
                Map("company" -> toJson(o.getAs[String]("company_name_en").map(x => x).getOrElse(throw new Exception("company output error"))))
            }
            val company = db.queryObject(condition = o, db_name = "company").get
            db.updateMultipleObject(condition = m2d(company,buss),doc = j2d(data,buss),db_name = "department")
            db.updateMultipleObject(condition = m2d(company,buss),doc = j2d(data,buss),db_name = "product")
            db.updateMultipleObject(condition = m2d(company,buss),doc = j2d(data,buss),db_name = "channel")
            db.updateMultipleObject(condition = m2d(company,buss),doc = j2d(data,buss),db_name = "users")
        }
        case "department" => {
            val o = condition(data)
            implicit val d2m: DBObject => Map[String, JsValue] = { o =>
                Map(
                    "company" -> toJson(o.getAs[String]("company").map(x => x).getOrElse(throw new Exception("department output error"))),
                    "department" -> toJson(o.getAs[String]("department_name").map(x => x).getOrElse(throw new Exception("department output error")))
                )
            }
            val department = db.queryObject(condition = o, db_name = "department").get
            db.updateMultipleObject(condition = m2d(department,buss),doc = j2d(data,buss),db_name = "product")
            db.updateMultipleObject(condition = m2d(department,buss),doc = j2d(data,buss),db_name = "channel")
            db.updateMultipleObject(condition = m2d(department,buss),doc = j2d(data,buss),db_name = "users")
        }
        case "product" => {
            val o = condition(data)
            implicit val d2m: DBObject => Map[String, JsValue] = { o =>
                Map(
                    "company" -> toJson(o.getAs[String]("company").map(x => x).getOrElse(throw new Exception("product output error"))),
                    "department" -> toJson(o.getAs[String]("department").map(x => x).getOrElse(throw new Exception("product output error"))),
                    "scope" -> toJson(o.getAs[String]("scope_name_ch").map(x => x).getOrElse(throw new Exception("product output error")))
                )
            }
            val scope = db.queryObject(condition = o, db_name = "product").get
            db.updateMultipleObject(condition = m2d(scope,buss),doc = j2d(data,buss),db_name = "channel")
        }
    }
}

trait AssociationOperating {
    def condition(data : JsValue) : DBObject = {
        val builder = MongoDBObject.newBuilder

        (data \ "company_id").asOpt[String].map (x => builder += "company_id" -> x).getOrElse(Unit)
        (data \ "department_id").asOpt[String].map (x => builder += "department_id" -> x).getOrElse(Unit)
        (data \ "scope_id").asOpt[String].map(x => builder += "scope_id" -> x).getOrElse(Unit)

        builder.result()
    }

    def m2d(map : Map[String,JsValue],buss : String) : DBObject = {
        val builder = MongoDBObject.newBuilder

        builder += "company" -> map("company").asOpt[String].map(x => x).get
        if(buss.equals("department") || buss.equals("product")){
            builder += "department" -> map("department").asOpt[String].map(x => x).get
            if(buss.equals("product"))
                builder += "scope" -> map("scope").asOpt[String].map(x => x).get
        }

        builder.result()
    }

    def j2d(data : JsValue,buss : String) : DBObject = {
        val builder = MongoDBObject.newBuilder

        if(buss.equals("company")){
            builder += "company" -> (data \ "company_name_en").asOpt[String].map(x => x).get
        }else{
            if(buss.equals("department"))
                builder += "department" -> (data \ "department_name").asOpt[String].map(x => x).get
            else
                builder += "scope" -> (data \ "scope_name_ch").asOpt[String].map(x => x).get
        }

        builder.result()
    }
}