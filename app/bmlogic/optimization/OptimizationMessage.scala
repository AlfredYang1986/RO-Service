package bmlogic.optimization

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/6/20.
  */
abstract class msg_OptimizationCommand extends CommonMessage

object OptimizationMessage {
    case class msg_PushOptimization(data : JsValue) extends msg_OptimizationCommand
    case class msg_BatchUpdateOptimization(data : JsValue) extends msg_OptimizationCommand
    case class msg_DropOptimization(data : JsValue) extends msg_OptimizationCommand
    case class msg_QueryOptimization(data : JsValue) extends msg_OptimizationCommand
    case class msg_ListOptimization(data : JsValue) extends msg_OptimizationCommand

    case class msg_QueryOptimizationByCDS(data : JsValue) extends msg_OptimizationCommand
    case class msg_UpdateOptimizationByBudget(data : JsValue) extends msg_OptimizationCommand
    case class msg_ListOptimizationByBudget(data : JsValue) extends msg_OptimizationCommand
}