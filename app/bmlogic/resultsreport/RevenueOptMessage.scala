package bmlogic.resultsreport

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/7/6.
  */

abstract class msg_RevenueOptCommand extends CommonMessage

object RevenueOptMessage {
    case class msg_RevenueResultReport(data : JsValue) extends msg_RevenueOptCommand
    case class msg_RevenueResultComparison(data : JsValue) extends msg_RevenueOptCommand
    case class msg_RevenueResultExport(data : JsValue) extends msg_RevenueOptCommand
}
