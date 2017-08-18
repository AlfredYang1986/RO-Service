package bmlogic.estirmateAnalysis

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by clock on 2017/7/4.
  */
abstract class msg_EstirmateAnalysisCommand extends CommonMessage

object EstirmateAnalysisMessage {
    case class msg_ReportEstirmate(data : JsValue) extends msg_EstirmateAnalysisCommand
    case class msg_AnalysisEstirmate(data : JsValue) extends msg_EstirmateAnalysisCommand
}