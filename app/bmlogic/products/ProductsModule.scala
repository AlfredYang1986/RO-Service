package bmlogic.products

import java.util.Date

import bminjection.db.DBTrait
import bmlogic.common.page.Page.{Page, SKIP, TAKE}
import bmlogic.common.sercurity.Sercurity
import bmlogic.products.ProductData.ProductInfo
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmlogic.products.ProductsMessage._
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson
import bmlogic.common.{associationRemove,associationUpdate}
import scala.collection.immutable.Map

/**
  * Created by jeorch on 17-6-14.
  */
object ProductsModule extends ModuleTrait with ProductInfo {

    def dispatchMsg(msg : MessageDefines)
                   (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_PushProduct(data) => pushProduct(data)
        case msg_UpdateProduct(data) => updateProduct(data)
        case msg_DropProduct(data) => dropProduct(data)

        case msg_QueryProduct(data) => queryProduct(data)(pr)
        case msg_ListProduct(data) => listProducts(data)
        case msg_PageProduct(data) => pageProducts(data)

        case msg_LinkProductWithDepartment(data) => link2Department(data)(pr)

        case _ => ???
    }

    def pushProduct(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val str = """product seed"""

            val o : DBObject = data
            val scope_name_ch = (data \ "scope_name_ch").asOpt[String].getOrElse(throw new Exception("product input js error"))
            val scope_name_en = (data \ "scope_name_en").asOpt[String].getOrElse(throw new Exception("product input js error"))
            o += "scope_id" -> Sercurity.md5Hash(str + scope_name_ch + scope_name_en + Sercurity.getTimeSpanWithMillSeconds)
            o += "date" -> new Date().getTime.asInstanceOf[Number]

            db.insertObject(o, "product", "scope_id")
            (Some(Map("method" -> toJson("pushProduct"), "product" -> toJson(o - "tmp"))),None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateProduct(data : JsValue)
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data
            o += "scope_id" -> (data \ "scope_id").get.asOpt[String].getOrElse(throw new Exception("product input js error"))
            o += "date" -> (data \ "date").get.asOpt[String].get.toLong.asInstanceOf[Number]
            associationUpdate().apply(data,db,"product")
            db.updateObject(o, "product", "scope_id")

            (Some(Map("method" -> toJson("update product"), "product" -> toJson(o - "tmp"))), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropProduct(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            associationRemove().apply(data,db,"product")
            db.deleteObject(o, "product", "scope_id")

            (Some(Map("method" -> toJson("dropProduct"), "product" -> toJson("tmp"))), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryProduct(data : JsValue)
                    (pr : Option[Map[String, JsValue]])
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            val result = db.queryObject(o, "product")
            (Some(Map(
                "method" -> toJson("queryProduct"),
                "product" -> toJson(result),
                "company" -> toJson(pr.get.get("company"))
            )),None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def listProducts(data : JsValue)
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            val result = db.queryMultipleObject(condition = o, db_name = "product", skip = 0, take = 1000)

            (Some(Map(
                "method" -> toJson("listProducts"),
                "product" -> toJson(result)
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def pageProducts(data : JsValue)
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val currentPage = (data \ "currentPage").asOpt[Int].map (x => x).getOrElse(1)

            val o = condition(data)
            lazy val count = db.queryCount(condition = o, db_name = "product").get
            val result = db.queryMultipleObject(condition = o, db_name = "product", take = TAKE, skip = SKIP(currentPage))

            (Some(Map(
                "method" -> toJson("pageProducts"),
                "result" -> toJson(result),
                "page" -> Page(currentPage,count),
                "count" -> toJson(count)
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def link2Department(data : JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val company = pr.get("company")
            val departments = (company \ "department").asOpt[List[JsValue]].map(x => x).getOrElse(throw new Exception("company output error"))

            val lks = (data \ "links").asOpt[List[String]].map (x => x).getOrElse(throw new Exception("product input js error"))

            val et = toJson(departments.filter(p => lks.contains((p \ "department_name").asOpt[String].get)).map { x =>
                var tmp = x.as[JsObject].value.toMap
                val lst  = (x \ "department_scope").asOpt[List[String]].get
                tmp += "department_scope" -> toJson((lst :+ (data \ "product_name").asOpt[String].get).distinct)
                toJson(tmp)
            } ::: departments.filterNot(p => lks.contains((p \ "department_name").asOpt[String].get)))

            var result = pr.get - "company"
            result += "company" -> toJson(company.as[JsObject].value.toMap + ("department" -> et))

            (Some(result), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
