package bmlogic.residual

import java.util.Date

import bminjection.db.DBTrait
import bmlogic.common.sercurity.Sercurity
import bmlogic.residual.ResidualData.ResidualInfo
import bmlogic.residual.ResidualMessage._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 2017/7/1.
  */
object ResidualModule extends ModuleTrait with ResidualInfo{
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_PushResidual(data) => pushResidual(data)(pr)
        case msg_DropResidual(data) => dropResidual(data)(pr)
        case msg_UpdateResidual(data) => updateResidual(data)(pr)
        case msg_LstResidual(data) => lstResidual(data)(pr)
        case _ => ???
    }

    def pushResidual(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            var pr_auth = pr.get.get("auth").get
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val user_name = (pr_auth \ "user_name").get.asOpt[String].get
            val str = """residual"""

            var o : DBObject = data
            o += "estimate_id" -> Sercurity.md5Hash(str + user_name + Sercurity.getTimeSpanWithMillSeconds)
            o += "user_name" -> user_name
            o += "create_date" -> new Date().getTime.asInstanceOf[Number]

            db.insertObject(o, "residualEstimate", "estimate_id")

            (Some(Map("method" -> toJson("push residual"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropResidual(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            db.deleteObject(o, "residualEstimate", "estimate_id")

            (Some(Map("method" -> toJson("drop residual"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateResidual(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            var pr_auth = pr.get.get("auth").get
            val user_name = (pr_auth \ "user_name").get.asOpt[String].get
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val o : DBObject = data
            o += "estimate_id" -> (data \ "estimateId").get.asOpt[String].get
            o += "create_date" -> (data \ "createDate").get.asOpt[Long]
            o += "user_name" -> user_name

            db.updateObject(o, "residualEstimate", "estimate_id")

            (Some(Map("method" -> toJson("update residual"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def lstResidual(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            var pr_auth = pr.get.get("auth").get
            val user_name = (pr_auth \ "user_name").get.asOpt[String].get
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val skip = (data \ "skip").asOpt[Int].map (x => x).getOrElse(0)
            val take = (data \ "take").asOpt[Int].map (x => x).getOrElse(20)

            val c = condition(data)
            c += "user_name" -> user_name
            val result = db.queryMultipleObject(c, db_name = "residualEstimate", take = take, skip = skip)

            (Some(Map("result" -> toJson(result))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
