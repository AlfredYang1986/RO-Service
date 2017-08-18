package bmlogic.department

import java.util.Date

import bminjection.db.DBTrait
import bmlogic.common.{associationRemove,associationUpdate}
import bmlogic.department.DepartmentData.DepartmentInfo
import bmlogic.department.DepartmentMessage._
import bmlogic.common.sercurity.Sercurity
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import scala.collection.immutable.Map
import bmlogic.common.page.Page.{Page, SKIP, TAKE}

/**
  * Created by liwei on 2017/6/19.
  */
object DepartmentModule extends ModuleTrait with DepartmentInfo{
    def dispatchMsg(msg : MessageDefines)
                   (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_PushDepartment(data) => pushDepartment(data)
        case msg_UpdateDepartment(data) => updateDepartment(data)
        case msg_DropDepartment(data) => dropDepartment(data)

        case msg_QueryDepartment(data) => queryDepartment(data)(pr)
        case msg_ListDepartment(data) => listDepartment(data)
        case msg_PageDepartment(data) => pageDepartment(data)

        case msg_QueryDepartmentPr() => queryDepartmentPr(pr)


        case _ => ???
    }

    def pushDepartment(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val str = """department seed"""

            val o : DBObject = data
            val department_name = (data \ "department_name").asOpt[String].map (x => x).getOrElse(throw new Exception("department input js error"))
            val is_admin = (data \ "is_admin").get.asOpt[String].map (x => x.toInt.asInstanceOf[Number]).getOrElse(throw new Exception("department input js error"))
            o += "department_id" -> Sercurity.md5Hash(str + department_name + is_admin + Sercurity.getTimeSpanWithMillSeconds)
            o += "date" -> new Date().getTime.asInstanceOf[Number]

            db.insertObject(o, "department", "department_name")
            (Some(Map("method" -> toJson("pushDepartment"), "department" -> toJson(o - "tmp"))),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateDepartment(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data
            o += "department_id" -> (data \ "department_id").get.asOpt[String].get
            o += "date" -> (data \ "date").get.asOpt[String].get.toLong.asInstanceOf[Number]
            associationUpdate().apply(data,db,"department")
            db.updateObject(o, "department", "department_id")

            (Some(Map("method" -> toJson("updateDepartment"), "department" -> toJson(o - "tmp"))),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropDepartment(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            associationRemove().apply(data,db,"department")
            db.deleteObject(o, "department", "department_id")

            (Some(Map("method" -> toJson("dropDepartment"), "result" -> toJson("tmp"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryDepartment(data : JsValue)
                   (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val o = condition(data)

            val result = db.queryObject(o, "department")

            (Some(Map(
                "method" -> toJson("queryDepartment"),
                "department" -> toJson(result),
                "company" -> toJson(pr.get.get("company"))
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def listDepartment(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = condition(data)
            val result = db.queryMultipleObject(o, "department", skip = 0, take = 1000)

            (Some(Map("method" -> toJson("listDepartment"),"department" -> toJson(result))),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def pageDepartment(data : JsValue)
                      (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val currentPage = (data \ "currentPage").asOpt[Int].map (x => x).getOrElse(1)
            val skip = SKIP(currentPage)

            val o : DBObject = condition(data)
            lazy val count = db.queryCount(condition = o, db_name = "department").get
            val result = db.queryMultipleObject(o, "department", take = TAKE, skip = skip)

            (Some(Map(
                "method" -> toJson("pageDepartment"),
                "result" -> toJson(result),
                "page" -> Page(currentPage,count),
                "count" -> toJson(count)
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryDepartmentPr(pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val user = pr.get.get("user").get
            val company = (user \ "company").asOpt[String].map(x => x).getOrElse(throw new Exception("department input js error"))
            val department = (user \ "department").asOpt[String].map(x => x).getOrElse(throw new Exception("department input js error"))
            val result = db.queryObject($and("company" -> company, "department_name" -> department), "department")

            (Some(Map(
                "method" -> toJson("queryDepartmentPr"),
                "user" -> user,
                "channel" -> pr.get.get("channel").get,
                "department" -> toJson(result.get)
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
