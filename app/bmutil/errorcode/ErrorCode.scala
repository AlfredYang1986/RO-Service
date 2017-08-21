package bmutil.errorcode

import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.libs.json.JsValue

object ErrorCode {

    case class ErrorNode(name: String, code: Int, message: String)

    private def xls: List[ErrorNode] = List(
        ErrorNode("unknown error", -9999, "未知异常")
    )

    def getErrorCodeByName(name: String): Int = xls.find(x => x.name == name) match {
        case Some(y) => y.code
        case None => -9999
    }

    def getErrorMessageByName(name: String): String = xls.find(x => x.name == name) match {
        case Some(y) => y.message
        case None => "unknow error"
    }

    def errorToJson(name: String): JsValue =
        Json.toJson(Map("status" -> toJson("error"), "error" ->
                toJson(Map("code" -> toJson(this.getErrorCodeByName(name)), "message" -> toJson(this.getErrorMessageByName(name))))))

    def errorToMap(name: String): Map[String, JsValue] =
        Map("status" -> toJson("error"), "error" -> toJson(Map("code" -> toJson(this.getErrorCodeByName(name)), "message" -> toJson(this.getErrorMessageByName(name)))))
}