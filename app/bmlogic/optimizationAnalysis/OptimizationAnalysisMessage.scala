package bmlogic.optimizationAnalysis

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by clock on 2017/7/4.
  */
abstract class msg_OptimizationAnalysisCommand extends CommonMessage

object OptimizationAnalysisMessage {
    case class msg_ReportOptimization(data : JsValue) extends msg_OptimizationAnalysisCommand
    case class msg_AnalysisOptimization(data : JsValue) extends msg_OptimizationAnalysisCommand
    case class msg_LstOptimization(data : JsValue) extends msg_OptimizationAnalysisCommand
    case class msg_UpdateOptimization(data : JsValue) extends msg_OptimizationAnalysisCommand
}