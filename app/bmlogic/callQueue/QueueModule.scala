package bmlogic.callQueue

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.util.Timeout
import bmlogic.callQueue.QueueActor.{DropFormQueue, PushToQueue}
import bmlogic.callQueue.QueueCommand.{msg_Dequeue, msg_Queueup}
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import akka.pattern.ask
import akka.util.Timeout
import bmlogic.callQueue.XMPP.queueMessageProxy


object QueueModule extends ModuleTrait{

	def dispatchMsg(msg : MessageDefines)
                   (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_Queueup(data : JsValue) => queueup(data)(pr)
		case msg_Dequeue(data : JsValue) => dequeue(data)(pr)
		case _ => ???
	}

    def queueup(data : JsValue)(pr : Option[Map[String, JsValue]])
               (implicit cm : CommonModules) = {
        val as = cm.modules.get.get("as").map (x => x.asInstanceOf[ActorSystem]).getOrElse(throw new Exception("no find ActorSystem"))
        val user_name = (data \ "user_name").asOpt[String].map(x => x).getOrElse(throw new Exception("input args error"))
        val Rjson = pr.get("result").asOpt[String].map(x => x).getOrElse(throw new Exception("r run-time error"))
        val qa = as.actorOf(QueueActor.props)
        qa ! PushToQueue(user_name,Rjson)

        (Some(Map("method" -> toJson("queueup"), "result" -> toJson(Rjson))), None)
    }

    def dequeue(data : JsValue)(pr : Option[Map[String, JsValue]])
               (implicit cm : CommonModules) = {
        val as = cm.modules.get.get("as").map (x => x.asInstanceOf[ActorSystem]).getOrElse(throw new Exception("no find ActorSystem"))
        val user_name = (data \ "user_name").asOpt[String].get
        val qa = as.actorOf(QueueActor.props)//查找name=user_name的actor，并发送dropFormQueue消息
        qa ! DropFormQueue(user_name)

        (Some(Map("method" -> toJson("dequeue"), "result" -> toJson("未实现"))), None)
    }
}