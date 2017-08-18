package bmlogic.residual

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by clock on 2017/7/1.
  */
abstract class msg_ResidualCommand extends CommonMessage

object ResidualMessage {
    case class msg_PushResidual(data : JsValue) extends msg_ResidualCommand
    case class msg_DropResidual(data : JsValue) extends msg_ResidualCommand
    case class msg_UpdateResidual(data : JsValue) extends msg_ResidualCommand
    case class msg_LstResidual(data : JsValue) extends msg_ResidualCommand
}