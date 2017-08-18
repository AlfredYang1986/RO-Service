package bmlogic.callQueue

import akka.actor.ActorSystem
import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by clock on 2017/7/21.
  */
abstract class msg_QueueCommand extends CommonMessage

object QueueCommand {
    case class msg_Queueup(data : JsValue) extends msg_QueueCommand
    case class msg_Dequeue(data : JsValue) extends msg_QueueCommand
}
