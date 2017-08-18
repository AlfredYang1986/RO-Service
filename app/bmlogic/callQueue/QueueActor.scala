package bmlogic.callQueue

import akka.actor._
import akka.agent.Agent
import bmlogic.callQueue.CallRJob.{CallFailed, CallR, CallSuccess}
import bmlogic.callQueue.XMPP.queueMessageProxy

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by clock on 17-7-20.
  */
object QueueActor {
    def props = Props(new QueueActor())

    case class PushToQueue(uname:String,arg: String)
    case class DropFormQueue(uname: String)
    case class Excution()

    val queue:Agent[List[(String,String)]] = Agent(Nil)
    val flag:Agent[Int] = Agent(0)
}

class QueueActor extends Actor with ActorLogging {
    import QueueActor._
    context.system.scheduler.schedule(0 seconds,1 seconds,self,Excution())

    def receive = {
        case PushToQueue(uname,arg) => pushToQueue(uname,arg)
        case DropFormQueue(arg) => dropFormQueue(arg)
        case Excution() => excution()
        case CallSuccess(uname,arg) => log2page(uname,arg)
        case CallFailed(uname,err) => log2page(uname,err)
        case _ => print("failed")
    }

    def pushToQueue(uname: String,arg:String) = {
        queue() match {
            case head::tail =>{
                new queueMessageProxy().sendMsg("前面"+queue().length+"人",uname, Map("type" -> "txt"))
            }
            case _ => new queueMessageProxy().sendMsg(s"开始优化",uname, Map("type" -> "txt"))
        }
        queue send  queue() :+ (uname,arg)
    }


    def dropFormQueue(uname: String) = {
        queue send queue().filter(!_._1.equals(uname))
        self ! PoisonPill
    }

    def excution() = {
        if (flag() == 0) {
            queue() match {
                case head::tail =>{
                    flag send 1
                    val callActor: ActorRef = context.actorOf(CallRJob.props, CallRJob.name)
                    callActor ! CallR(head._1,head._2)
                    new queueMessageProxy().sendMsg(s"开始优化",head._1, Map("type" -> "txt"))
                }
                case Nil => Unit
            }
        }
    }

    def log2page(uname:String,arg:String) = {
        sender() ! PoisonPill
        queue send queue().tail
        self ! PoisonPill

        new queueMessageProxy().sendMsg(arg,uname, Map("type" -> "txt"))
        otherLog2page()

        flag send 0
    }

    def otherLog2page() = {
        def calcOutrunner(uname:String): Int ={
            queue().map(x=>x._1).indexOf(uname)
        }
        queue().map { x =>
            val ql = queue().length
            val ns = calcOutrunner(x._1)+1
            new queueMessageProxy().sendMsg(s"正在:"+ ns +" / "+ql,x._1, Map("type" -> "txt"))
        }
    }
}
