package bmlogic.channel

import java.util.Date

import bminjection.db.DBTrait
import bmlogic.common.sercurity.Sercurity
import bmlogic.channel.ChannelMessage._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import bmlogic.channel.ChannelData.ChannelInfo
import bmlogic.channel.ChannelModule.condition
import bmlogic.common.page.Page._

import scala.collection.immutable.Map

/**
  * Created by liwei on 2017/6/19.
  */
object ChannelModule extends ModuleTrait with ChannelInfo{
    def dispatchMsg(msg : MessageDefines)
                   (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_PushChannel(data) => pushChannel(data)
        case msg_UpdateChannel(data) => updateChannel(data)
        case msg_DropChannel(data) => dropChannel(data)

        case msg_QueryChannel(data) => queryChannel(data)(pr)
        case msg_ListChannel(data) => listChannels(data)(pr)
        case msg_PageChannel(data) => pageChannels(data)(pr)
        case msg_ListChannelPr() => listChannelsPr(pr)

        case _ => ???
    }

    def pushChannel(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val str = """channel seed"""

            val o : DBObject = data
            val channel_ch = (data \ "channel_ch").asOpt[String].map (x => x).getOrElse(throw new Exception("channel input js error"))
            val channel_en = (data \ "channel_en").asOpt[String].map (x => x).getOrElse(throw new Exception("channel input js error"))
            val channel_description = (data \ "channel_description").asOpt[String].map (x => x).getOrElse(throw new Exception("channel input js error"))
            o += "channel_id" -> Sercurity.md5Hash(str + channel_ch + channel_en + channel_description + Sercurity.getTimeSpanWithMillSeconds)
            o += "date" -> new Date().getTime.asInstanceOf[Number]

            db.insertObject(o, "channel", "channel_id")
            (Some(Map("method" -> toJson("pushChannel"), "channel" -> toJson(o - "tmp"))),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateChannel(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data
            o += "channel_id" -> (data \ "channel_id").get.asOpt[String].get
            o += "date" -> (data \ "date").get.asOpt[String].get.toLong.asInstanceOf[Number]

            db.updateObject(o, "channel", "channel_id")

            (Some(Map("method" -> toJson("updateChannel"), "channel" -> toJson(o - "tmp"))),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropChannel(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)

            db.deleteObject(o, "channel", "channel_id")

            (Some(Map("method" -> toJson("dropChannel"), "result" -> toJson("tmp"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryChannel(data : JsValue)
                   (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)

            val result = db.queryObject(o, "channel")

            (Some(Map(
                "method" -> toJson("queryChannel"),
                "channel" -> toJson(result),
                "company" -> toJson(pr.get.get("company"))
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def listChannels(data : JsValue)
                    (pr : Option[Map[String, JsValue]])
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            val result = db.queryMultipleObject(condition = o, db_name = "channel", skip = 0, take = 1000)

            (Some(Map(
                "method" -> toJson("listChannels"),
                "channel" -> toJson(result)
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def pageChannels(data : JsValue)
                    (pr : Option[Map[String, JsValue]])
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val currentPage = (data \ "currentPage").asOpt[Int].map (x => x).getOrElse(1)
            val skip = SKIP(currentPage)

            val o = condition(data)
            lazy val count = db.queryCount(condition = o, db_name = "channel").get
            val result = db.queryMultipleObject(condition = o, db_name = "channel", skip = skip, take = TAKE)

            (Some(Map(
                "method" -> toJson("pageChannels"),
                "result" -> toJson(result),
                "page" -> Page(currentPage,count),
                "count" -> toJson(count)
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def listChannelsPr(pr : Option[Map[String, JsValue]])
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val user =  pr.get("user")
            val company = (user \ "company").get.asOpt[String].getOrElse(throw new Exception("channel input js error"))
            val department = (user \ "department").get.asOpt[String].getOrElse(throw new Exception("channel input js error"))
            val data = toJson(Map("company" -> toJson(company),"department" -> toJson(department)))
            val o = condition(data)
            val result = db.queryMultipleObject(condition = o, db_name = "channel", take = 200, skip = 0)

            (Some(Map(
                "method" -> toJson("listChannelsPr"),
                "user" -> toJson(user),
                "channel" -> toJson(result)
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
