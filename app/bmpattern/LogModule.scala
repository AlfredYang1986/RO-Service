package bmpattern

import bmpattern.LogMessage._
import bmutil.errorcode.ErrorCode
import play.api.libs.json.JsValue

object LogModule extends ModuleTrait {
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case cmd : msg_log => cmd.l(cmd.ls, cmd.data)
        case _ => (None, Some(ErrorCode.errorToJson("can not parse result")))
    }
}
