package controllers

import bmlogic.fop.SliceUpload
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import play.api.mvc._

class FopController extends Controller {
    implicit val f: String => JsValue = { name =>
        toJson(Map("status" -> toJson("error"), "result" -> toJson(name)))
    }

    def downloadFile(name: String) = Action {
        Ok(SliceUpload.downloadFile(name)).as("excel/csv")
    }

    def uploadFile = Action { request =>
        uploadRequestArgs(request)(SliceUpload.ManyFileSlice)
    }

    def uploadRequestArgs(request: Request[AnyContent])(func: MultipartFormData[TemporaryFile] => JsValue): Result = {
        try {
            request.body.asMultipartFormData.map { x =>
                Ok(func(x))
            }.getOrElse(BadRequest("Bad Request for input"))
        } catch {
            case _: Exception => BadRequest("Bad Request for input")
        }
    }
}