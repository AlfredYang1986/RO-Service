package bmlogic.callQueue.jri

import bmlogic.callQueue.CallTrait
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/6/21.
  */
trait JRITrait extends CallTrait {
    override def callAI(arg: String): JsValue = ???
}
