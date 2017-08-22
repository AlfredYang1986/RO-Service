package controllers

import play.api.mvc._
import play.api.libs.json.Json.toJson

/**
  * Created by liwei on 2017/8/21.
  */
class RegionalManagementController  extends Controller {
    def queryMultipleRegionalManagement = Action { request =>
        try {
            request.body.asJson.map { x =>
                Ok(toJson(Map(
                    "code" -> toJson(0),
                    "msg" -> toJson(""),
                    "count" -> toJson(1),
                    "data" -> toJson(List(
                        toJson(Map(
                            "id" -> toJson("10001")
                            ,"username" -> toJson("杜甫")
                            ,"email" -> toJson("xianxin@layui.com")
                            ,"sex" -> toJson("男")
                            ,"city" -> toJson("浙江杭州")
                            ,"sign" -> toJson("点击此处，显示更多。当内容超出时，点击单元格会自动显示更多内容。")
                            ,"experience" -> toJson("116")
                            ,"ip" -> toJson("192.168.0.8")
                            ,"logins" -> toJson("108")
                            ,"joinTime" -> toJson("2016-10-14")
                        ))
                    ))
                )))
            }.getOrElse (BadRequest("Bad Request for input"))
        } catch {
            case _ : Exception => BadRequest("Bad Request for input")
        }
    }
}
