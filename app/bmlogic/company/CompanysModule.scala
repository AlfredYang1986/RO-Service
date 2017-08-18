package bmlogic.company

import java.util.Date

import bmlogic.common.sercurity.Sercurity
import bminjection.db.DBTrait
import bmlogic.common.page.Page.{Page, SKIP, TAKE}
import bmlogic.company.CompanyMessage._
import bmpattern.ModuleTrait
import bmlogic.company.CompanyData.CompanyInfo
import bmmessages.{CommonModules, MessageDefines}
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import bmlogic.common.{associationRemove,associationUpdate}
import scala.collection.immutable.Map

/**
  * Created by liwei on 2017/8/1.
  */
object CompanysModule  extends ModuleTrait with CompanyInfo {
    def dispatchMsg(msg : MessageDefines)
                   (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_PushCompany(data) => pushCompany(data)
        case msg_UpdateCompany(data) => updateCompany(data)
        case msg_DropCompany(data) => dropCompany(data)
        case msg_QueryCompany(data) => queryCompany(data)
        case msg_ListCompany(data) => listCompanys(data)
        case msg_PageCompany(data) => pageCompanys(data)

        case _ => ???
    }

    def pushCompany(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val str = """company seed"""

            val o : DBObject = data
            val company_name_en = (data \ "company_name_en").asOpt[String].map (x => x).getOrElse(throw new Exception("company input js error"))
            val company_name_ch = (data \ "company_name_ch").asOpt[String].map (x => x).getOrElse(throw new Exception("company input js error"))
            val short_name = (data \ "short_name").asOpt[String].map (x => x).getOrElse(throw new Exception("company input js error"))
            o += "company_id" -> Sercurity.md5Hash(str + company_name_en + company_name_ch + short_name + Sercurity.getTimeSpanWithMillSeconds)
            o += "date" -> new Date().getTime.asInstanceOf[Number]

            db.insertObject(o, "company", "company_name_en")

            (Some(Map("method" -> toJson("pushCompany"), "company" -> toJson("tmp"))), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateCompany(data : JsValue)
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data
            o += "company_id" -> (data \ "company_id").get.asOpt[String].getOrElse(throw new Exception("company input js error"))
            o += "date" -> (data \ "date").get.asOpt[String].get.toLong.asInstanceOf[Number]
            associationUpdate().apply(data,db,"company")
            db.updateObject(o, "company", "company_id")

            (Some(Map("method" -> toJson("updateCompany"), "company" -> toJson(o - "tmp"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropCompany(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            associationRemove().apply(data,db,"company")
            db.deleteObject(o, "company", "company_id")

            (Some(Map("method" -> toJson("dropCompany"), "company" -> toJson("tmp"))), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryCompany(data : JsValue)
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            val result = db.queryObject(o, "company")

            if (result.isEmpty) throw new Exception("company input js error")
            else (Some(Map("method" -> toJson("queryCompany"), "company" -> toJson(result.get))), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def listCompanys(data : JsValue)
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            val result = db.queryMultipleObject(condition = o, db_name = "company", skip = 0, take = 1000)

            (Some(Map("method" -> toJson("listCompanys"), "company" -> toJson(result))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def pageCompanys(data : JsValue)
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val currentPage = (data \ "currentPage").asOpt[Int].map (x => x).getOrElse(1)

            val o = condition(data)
            lazy val count = db.queryCount(condition = o, db_name = "company").get
            val result = db.queryMultipleObject(condition = o, db_name = "company", skip = SKIP(currentPage), take = TAKE)

            (Some(Map(
                "method" -> toJson("pageCompanys"),
                "result" -> toJson(result),
                "page" -> Page(currentPage,count),
                "count" -> toJson(count)
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
