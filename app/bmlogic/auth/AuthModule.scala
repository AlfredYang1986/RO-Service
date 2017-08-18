package bmlogic.auth

import java.util.Date

import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson
import AuthMessage._
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthData.AuthData
import bmlogic.common.sercurity.Sercurity
import bmmessages.MessageDefines
import bmmessages.CommonModules
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import bmlogic.common.AuthEnum._
import bmlogic.common.page.Page.{Page, SKIP, TAKE}

import scala.collection.immutable.Map
import com.mongodb.casbah.Imports._

object AuthModule extends ModuleTrait with AuthData {

	def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_AuthPushUser(data) => authPushUser(data)
        case msg_AuthUpdateUser(data) => authUpdateUser(data)(pr)
        case msg_AuthDropUser(data) => authDropUser(data)(pr)

		case msg_AuthWithPassword(data) => authWithPassword(data)
        case msg_QueryUser(data) => queryUser(data)(pr)
        case msg_QueryMultipleUser(data) => queryMultipleUser(data)(pr)
        case msg_PageUser(data) => pageUser(data)(pr)

        case msg_AuthTokenParser(data) => authTokenParser(data)

        case msg_CheckAuthTokenTest(data) => checkAuthTokenTest(data)(pr)
        case msg_CheckTokenExpire(data) => checkAuthTokenExpire(data)(pr)
        case msg_CheckSuperAdministrator(data) => checkSuperAdmin(data)(pr)
        case msg_CheckAdministrator(data) => checkAdmin(data)(pr)

        case msg_GenerateToken() => generateToken(pr)

		case _ => ???
	}

    def authPushUser(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val att = cm.modules.get.get("att").map (x => x.asInstanceOf[AuthTokenTrait]).getOrElse(throw new Exception("no encrypt impl"))

            val date = new Date().getTime
            val o : DBObject = data
            val user_name = (data \ "user_name").asOpt[String].map (x => x).getOrElse(throw new Exception("users input js error"))
            val pwd = (data \ "pwd").asOpt[String].map (x => x).getOrElse(throw new Exception("users input js error"))
            o += "user_id" -> Sercurity.md5Hash(user_name + pwd + Sercurity.getTimeSpanWithMillSeconds)
            o += "date" -> date.asInstanceOf[Number]
            db.insertObject(o, "users", "user_name")

            val reVal = toJson(o - "user_id" - "pwd" - "phoneNo" - "email" - "date")

            (Some(Map("method" -> toJson("authPushUser"), "user" -> reVal)),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def authUpdateUser(data : JsValue)
                      (pr : Option[Map[String, JsValue]])
                      (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try{
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data

            o += "user_id" -> (data \ "user_id").get.asOpt[String].get
            o += "date" -> (data \ "date").get.asOpt[String].get.toLong.asInstanceOf[Number]
            db.updateObject(o, "users", "user_id")

            (Some(Map("method" -> toJson("authUpdateUser"), "company" -> toJson(o - "tmp"))),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def authDropUser(data : JsValue)
                    (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)

            db.deleteObject(o, "users", "user_id")

            (Some(Map("method" -> toJson("authDropUser"), "result" -> toJson("tmp"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def authWithPassword(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
		try {
			val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

			val user_name = (data \ "user_name").asOpt[String].map (x => x).getOrElse(throw new Exception("users input js error"))
			val pwd = (data \ "pwd").asOpt[String].map (x => x).getOrElse(throw new Exception("users input js error"))

			val result = db.queryObject($and("user_name" -> user_name, "pwd" -> pwd), "users")
            val reVal = toJson(result.get - "user_id" - "pwd" - "phoneNo" - "email" - "date")

            if (result.isEmpty)
                throw new Exception("no user found")
			else
                (Some(Map("method" -> toJson("authWithPassword"), "user" -> reVal)), None)
		} catch {
			case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
    }

    def queryUser(data : JsValue)
                 (pr : Option[Map[String, JsValue]])
                 (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            val result = db.queryObject(o, "users")

            (Some(Map("method" -> toJson("queryUser"), "user" -> toJson(result), "company" -> toJson(pr.get.get("company")))),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryMultipleUser(data : JsValue)
                         (pr : Option[Map[String, JsValue]])
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = condition(data)
            val result = db.queryMultipleObject(o, "users", skip = 0, take = 1000)

            (Some(Map("method" -> toJson("queryMultipleUser"), "result" -> toJson(result))),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def pageUser(data : JsValue)
                         (pr : Option[Map[String, JsValue]])
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val currentPage = (data \ "currentPage").asOpt[Int].map (x => x).getOrElse(1)

            val o = condition(data)
            lazy val count = db.queryCount(condition = o, db_name = "users").get
            val result = db.queryMultipleObject(condition = o, db_name = "users", skip = SKIP(currentPage), take = TAKE)

            (Some(Map(
                "method" -> toJson("pageUser"),
                "result" -> toJson(result),
                "page" -> Page(currentPage,count),
                "count" -> toJson(count)
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def authTokenParser(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val att = cm.modules.get.get("att").map (x => x.asInstanceOf[AuthTokenTrait]).getOrElse(throw new Exception("no encrypt impl"))

            val auth_token = (data \ "token").asOpt[String].map (x => x).getOrElse(throw new Exception("token input error"))
            val auth = att.decrypt2JsValue(auth_token)

            (Some(Map("method" -> toJson("authTokenParser"), "auth" -> toJson(auth))), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def checkAuthTokenTest(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
        (pr, None)
    }

    def checkAuthTokenExpire(data : JsValue)
                            (pr : Option[Map[String, JsValue]])
                            (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val auth = pr.map (x => x.get("auth").get).getOrElse(throw new Exception("token parse error"))
            val expire_in = (auth \ "expire_in").asOpt[Long].map (x => x).getOrElse(throw new Exception("token parse error"))
            if (new Date().getTime > expire_in) throw new Exception("token expired")
            else (pr, None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def checkSuperAdmin(value: JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val auth = pr.map (x => x.get("auth").get).getOrElse(throw new Exception("token parse error"))

            //println(auth) What are you doing?

            (pr, None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def checkAdmin(value: JsValue)
                  (pr : Option[Map[String, JsValue]])
                  (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {

            val auth = pr.map (x => x.get("auth").get).getOrElse(throw new Exception("token parse error"))

            (pr, None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def generateToken(pr : Option[Map[String, JsValue]])
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val att = cm.modules.get.get("att").map (x => x.asInstanceOf[AuthTokenTrait]).getOrElse(throw new Exception("no encrypt impl"))

            val user = pr.get.get("user").get
            val channel = pr.get.get("channel").get
            val department = pr.get.get("department").get

            val is_admin = (department \ "is_admin").asOpt[String].map(x => x).getOrElse(throw new Exception("user department error"))

            /**
              * generate token
              */
            def tokonCode: (JsValue, String) = {
                var usm = user.as[JsObject].value.toMap
                usm += "channel" -> channel
                usm += "is_admin" -> toJson(StringToInt(is_admin))
                usm += ("expire_in" -> toJson(new Date().getTime + 60 * 60 * 1000 * 24))
                (toJson(usm - "channel"), att.encrypt2Token(toJson(usm)))
            }
            val (u, t) = tokonCode

            (Some(Map("method" -> toJson("generateToken"), "user" -> u, "token" -> toJson(t))), None)

        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}