package bmlogic.channel

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/6/19.
  */
abstract class msg_ChannelCommand extends CommonMessage

object ChannelMessage {
    case class msg_PushChannel(data : JsValue) extends msg_ChannelCommand
    case class msg_UpdateChannel(data : JsValue) extends msg_ChannelCommand
    case class msg_DropChannel(data : JsValue) extends msg_ChannelCommand

    case class msg_QueryChannel(data : JsValue) extends msg_ChannelCommand
    case class msg_ListChannel(data : JsValue) extends msg_ChannelCommand
    case class msg_PageChannel(data : JsValue) extends msg_ChannelCommand
    case class msg_ListChannelPr() extends msg_ChannelCommand
}
