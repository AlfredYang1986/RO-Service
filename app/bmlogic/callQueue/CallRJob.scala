package bmlogic.callQueue

import akka.actor.{Actor, ActorLogging, Props}
import bmlogic.callQueue.jri.JRIImpl

/**
  * Created by clock on 17-7-20.
  */
object CallRJob{
    def name = "RJobActor"
    def props = Props[CallRJob]

    case class CallR(uname:String,arg:String)
    case class CallSuccess(uname:String,arg:String)
    case class CallFailed(uname:String,err:String)
}

class CallRJob extends Actor with ActorLogging with JRIImpl{
    import CallRJob._
    override def receive = {
        case CallR(uname,arg) =>
            val result = callAI(arg).toString()
            result match {
                case "\"TRUE\"" => sender() ! CallSuccess(uname,"优化完成")
                case "\"FALSE\"" => sender() ! CallFailed(uname,"调用出错")
                case _ => sender() ! CallFailed(uname,result.toString())
            }
        case _ => Unit
    }
}
