package bmlogic.callQueue

import play.api.libs.json.JsValue

/**
  * Created by clock on 17-7-20.
  */
trait CallTrait {
    def callAI(arg: String): JsValue
}
