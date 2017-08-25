package controllers

import play.api.mvc._
import play.api.libs.json.Json.toJson
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/8/21.
  */
class RegionalManagementController  extends Controller {
    def queryMultipleRegionalManagement = Action(request =>
        try {
            request.body.asJson.map { x =>
                Ok(succesJSON(resultJson()))
            }.getOrElse (BadRequest("Bad Request for input"))
        } catch {
            case _ : Exception => BadRequest("Bad Request for input")
        }
    )

    def resultJson() : JsValue = {
        toJson(List(
            toJson(Map(
                "id" -> toJson("1")
                ,"hospital" -> toJson("人民医院")
                ,"productA1" -> toJson("66,667")
                ,"productA2" -> toJson("66,667")
                ,"productA3" -> toJson("66,667")
                ,"productB1" -> toJson("13,333")
                ,"productB2" -> toJson("13,333")
                ,"productB3" -> toJson("13,333")
                ,"productC1" -> toJson("代表1")
                ,"productC2" -> toJson("代表1")
                ,"productC3" -> toJson("代表1")
            ))
        ))
    }

    def succesJSON(data : JsValue) : JsValue = {
        toJson(Map("code" -> toJson(0),
            "msg" -> toJson("成功"),
            "count" -> toJson(10),
            "data" -> data))
    }

    def failedJSON(data : JsValue) : JsValue = {
        toJson(Map("code" -> toJson(1),
            "msg" -> toJson("失败"),
            "count" -> toJson(10),
            "data" -> data))
    }
}
