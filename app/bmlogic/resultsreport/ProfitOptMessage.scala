package bmlogic.resultsreport

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/7/6.
  */

abstract class msg_ProfitOptCommand extends CommonMessage

object ProfitOptMessage {
    case class msg_ProfitResultReport(data : JsValue) extends msg_ProfitOptCommand
    case class msg_ProfitResultComparison(data : JsValue) extends msg_ProfitOptCommand
    case class msg_ProfitResultExport(data : JsValue) extends msg_ProfitOptCommand
}
