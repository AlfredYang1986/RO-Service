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
                Ok(resultJson())
            }.getOrElse (BadRequest("Bad Request for input"))
        } catch {
            case _ : Exception => BadRequest("Bad Request for input")
        }
    )

    def resultJson() : JsValue = {
        toJson(List(
            toJson(Map(
                "id" -> toJson("1")
                , "hospital" -> toJson("人民医院")
                , "productA1" -> toJson("66,667")
                , "productA2" -> toJson("66,667")
                , "productA3" -> toJson("66,667")
                , "productB1" -> toJson("13,333")
                , "productB2" -> toJson("13,333")
                , "productB3" -> toJson("13,333")
                , "productC1" -> toJson("代表1")
                , "productC2" -> toJson("代表1")
                , "productC3" -> toJson("代表1")
            )),
            toJson(Map(
                "id" -> toJson("2")
                , "hospital" -> toJson("协和医院")
                , "productA1" -> toJson("66,667")
                , "productA2" -> toJson("66,667")
                , "productA3" -> toJson("66,667")
                , "productB1" -> toJson("13,333")
                , "productB2" -> toJson("13,333")
                , "productB3" -> toJson("13,333")
                , "productC1" -> toJson("代表1")
                , "productC2" -> toJson("代表1")
                , "productC3" -> toJson("代表1")
            )),
            toJson(Map(
                "id" -> toJson("3")
                , "hospital" -> toJson("肿瘤医院")
                , "productA1" -> toJson("66,667")
                , "productA2" -> toJson("66,667")
                , "productA3" -> toJson("66,667")
                , "productB1" -> toJson("13,333")
                , "productB2" -> toJson("13,333")
                , "productB3" -> toJson("13,333")
                , "productC1" -> toJson("代表1")
                , "productC2" -> toJson("代表1")
                , "productC3" -> toJson("代表1")
            )),
            toJson(Map(
                "id" -> toJson("4")
                , "hospital" -> toJson("妇产医院")
                , "productA1" -> toJson("66,667")
                , "productA2" -> toJson("66,667")
                , "productA3" -> toJson("66,667")
                , "productB1" -> toJson("13,333")
                , "productB2" -> toJson("13,333")
                , "productB3" -> toJson("13,333")
                , "productC1" -> toJson("代表1")
                , "productC2" -> toJson("代表1")
                , "productC3" -> toJson("代表1")
            )),
            toJson(Map(
                "id" -> toJson("5")
                , "hospital" -> toJson("社区医院")
                , "productA1" -> toJson("66,667")
                , "productA2" -> toJson("66,667")
                , "productA3" -> toJson("66,667")
                , "productB1" -> toJson("13,333")
                , "productB2" -> toJson("13,333")
                , "productB3" -> toJson("13,333")
                , "productC1" -> toJson("代表1")
                , "productC2" -> toJson("代表1")
                , "productC3" -> toJson("代表1")
            ))
//            ,
//            toJson(Map(
//                "id" -> toJson("1")
//                , "hospital" -> toJson("人民医院")
//                , "productA1" -> toJson("66,667")
//                , "productA2" -> toJson("66,667")
//                , "productA3" -> toJson("66,667")
//                , "productB1" -> toJson("13,333")
//                , "productB2" -> toJson("13,333")
//                , "productB3" -> toJson("13,333")
//                , "productC1" -> toJson("代表1")
//                , "productC2" -> toJson("代表1")
//                , "productC3" -> toJson("代表1")
//            )),
//            toJson(Map(
//                "id" -> toJson("6")
//                , "hospital" -> toJson("协和医院")
//                , "productA1" -> toJson("66,667")
//                , "productA2" -> toJson("66,667")
//                , "productA3" -> toJson("66,667")
//                , "productB1" -> toJson("13,333")
//                , "productB2" -> toJson("13,333")
//                , "productB3" -> toJson("13,333")
//                , "productC1" -> toJson("代表1")
//                , "productC2" -> toJson("代表1")
//                , "productC3" -> toJson("代表1")
//            )),
//            toJson(Map(
//                "id" -> toJson("7")
//                , "hospital" -> toJson("肿瘤医院")
//                , "productA1" -> toJson("66,667")
//                , "productA2" -> toJson("66,667")
//                , "productA3" -> toJson("66,667")
//                , "productB1" -> toJson("13,333")
//                , "productB2" -> toJson("13,333")
//                , "productB3" -> toJson("13,333")
//                , "productC1" -> toJson("代表1")
//                , "productC2" -> toJson("代表1")
//                , "productC3" -> toJson("代表1")
//            )),
//            toJson(Map(
//                "id" -> toJson("8")
//                , "hospital" -> toJson("妇产医院")
//                , "productA1" -> toJson("66,667")
//                , "productA2" -> toJson("66,667")
//                , "productA3" -> toJson("66,667")
//                , "productB1" -> toJson("13,333")
//                , "productB2" -> toJson("13,333")
//                , "productB3" -> toJson("13,333")
//                , "productC1" -> toJson("代表1")
//                , "productC2" -> toJson("代表1")
//                , "productC3" -> toJson("代表1")
//            )),
//            toJson(Map(
//                "id" -> toJson("9")
//                , "hospital" -> toJson("社区医院")
//                , "productA1" -> toJson("66,667")
//                , "productA2" -> toJson("66,667")
//                , "productA3" -> toJson("66,667")
//                , "productB1" -> toJson("13,333")
//                , "productB2" -> toJson("13,333")
//                , "productB3" -> toJson("13,333")
//                , "productC1" -> toJson("代表1")
//                , "productC2" -> toJson("代表1")
//                , "productC3" -> toJson("代表1")
//            )),
//            toJson(Map(
//                "id" -> toJson("10")
//                , "hospital" -> toJson("人民医院")
//                , "productA1" -> toJson("66,667")
//                , "productA2" -> toJson("66,667")
//                , "productA3" -> toJson("66,667")
//                , "productB1" -> toJson("13,333")
//                , "productB2" -> toJson("13,333")
//                , "productB3" -> toJson("13,333")
//                , "productC1" -> toJson("代表1")
//                , "productC2" -> toJson("代表1")
//                , "productC3" -> toJson("代表1")
//            )),
//            toJson(Map(
//                "id" -> toJson("11")
//                , "hospital" -> toJson("协和医院")
//                , "productA1" -> toJson("66,667")
//                , "productA2" -> toJson("66,667")
//                , "productA3" -> toJson("66,667")
//                , "productB1" -> toJson("13,333")
//                , "productB2" -> toJson("13,333")
//                , "productB3" -> toJson("13,333")
//                , "productC1" -> toJson("代表1")
//                , "productC2" -> toJson("代表1")
//                , "productC3" -> toJson("代表1")
//            ))

        ))
    }
}
