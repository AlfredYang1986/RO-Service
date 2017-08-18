package bmlogic.naturalSales

import java.util.Date

import bminjection.db.DBTrait
import bmlogic.common.sercurity.Sercurity
import bmlogic.naturalSales.NaturalSalesMessage.{msg_PushNaturalSales, _}
import bmlogic.naturalSales.naturalSalesData.NaturalSalesInfo
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 2017/6/29.
  */
object NaturalSalesModule extends ModuleTrait with NaturalSalesInfo{
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_PushNaturalSales(data) => pushNaturalSales(data)(pr)
        case msg_DropNaturalSales(data) => dropNaturalSales(data)(pr)
        case msg_UpdateNaturalSales(data) => updateNaturalSales(data)(pr)
        case msg_LstNaturalSales(data) => lstNaturalSales(data)(pr)
        case _ => ???
    }

    def pushNaturalSales(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            var pr_auth = pr.get.get("auth").get
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val user_name = (pr_auth \ "user_name").get.asOpt[String].get
            val str = """natural sales"""

            var o : DBObject = data
            o += "estimate_id" -> Sercurity.md5Hash(str + user_name + Sercurity.getTimeSpanWithMillSeconds)
            o += "user_name" -> user_name
            o += "create_date" -> new Date().getTime.asInstanceOf[Number]

            db.insertObject(o, "naturalSalesEstimate", "estimate_id")

            (Some(Map("method" -> toJson("push atural sales"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropNaturalSales(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            db.deleteObject(o, "naturalSalesEstimate", "estimate_id")

            (Some(Map("method" -> toJson("drop atural sales"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateNaturalSales(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            var pr_auth = pr.get.get("auth").get
            val user_name = (pr_auth \ "user_name").get.asOpt[String].get
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val o : DBObject = data
            o += "estimate_id" -> (data \ "estimate_id").get.asOpt[String].get
            o += "create_date" -> (data \ "create_date").get.asOpt[Long]
            o += "user_name" -> user_name

            db.updateObject(o, "naturalSalesEstimate", "estimate_id")

            (Some(Map("method" -> toJson("update atural sales"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def lstNaturalSales(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val skip = (data \ "skip").asOpt[Int].map (x => x).getOrElse(0)
            val take = (data \ "take").asOpt[Int].map (x => x).getOrElse(20)

            val c = condition(data)
            val result = db.queryMultipleObject(c, db_name = "naturalSalesEstimate", take = take, skip = skip)

            (Some(Map("result" -> toJson(result))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
