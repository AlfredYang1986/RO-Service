package bmlogic.resultsreport

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/7/6.
  */

abstract class msg_BudgetOptCommand extends CommonMessage

object BudgetOptMessage {
    case class msg_BudgetResultReport(data : JsValue) extends msg_BudgetOptCommand
    case class msg_BudgetResultComparison(data : JsValue) extends msg_BudgetOptCommand
    case class msg_BudgetResultExport(data : JsValue) extends msg_BudgetOptCommand
}