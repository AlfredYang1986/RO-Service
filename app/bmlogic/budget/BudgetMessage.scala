package bmlogic.budget

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/6/14.
  */

abstract class msg_BudgetCommand extends CommonMessage

object BudgetMessage {
    case class msg_PushBudget(data : JsValue) extends msg_BudgetCommand
    case class msg_UpdateBudget(data : JsValue) extends msg_BudgetCommand
    case class msg_DropBudget(data : JsValue) extends msg_BudgetCommand
    case class msg_QueryBudget(data : JsValue) extends msg_BudgetCommand
    case class msg_ListBudget(data : JsValue) extends msg_BudgetCommand
}
