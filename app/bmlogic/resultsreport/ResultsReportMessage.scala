package bmlogic.resultsreport

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/6/22.
  */
abstract class msg_ResultsReportCommand extends CommonMessage

object ResultsReportMessage {
    case class msg_LinkData(data : JsValue) extends msg_ResultsReportCommand
}
