package bmlogic.resultsreport

import bmlogic.resultsreport.ResultsReportMessage._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import play.api.libs.json.Json.toJson
import play.api.libs.json.JsValue
import java.io._
import java.util.UUID
import bmutil.errorcode.ErrorCode._
import bmutil.JRConfig._
import bmutil.alFileOpt._

/**
  * Created by liwei on 2017/6/22.
  */
object ResultsReportModule extends ModuleTrait {
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_LinkData(data) => linkData(data)
        case _ => ???
    }

    def linkData(data : JsValue)(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        (Some(Map("method" -> toJson("linkData"), "result" -> data)), None)
    }

    def serviceResultMerge(rst : List[Map[String, JsValue]]) : Map[String, JsValue] = {
        try {
            var optimi_type : Option[String] = None     //优化类型
            var batchs_time : Option[Int] = None        //数据批次
            var budget_list : Option[JsValue] = None    //产品预算
            var optimi_list : Option[JsValue] = None    //产品优化

            rst foreach{x =>
                x("method").asOpt[String] match {
                    case Some("linkData") => {
                        val data = x("result").asOpt[JsValue].get
                        optimi_type = (data \ "optimizaOpt").get.asOpt[String]
                        batchs_time = Some((data \ "times").get.asOpt[Int].get+1)
                    }
                    case Some("listBudget") => {
                        budget_list = x("result").asOpt[JsValue]
                    }
                    case Some("listOptimizationPr2") => {
                        optimi_list = x("result").asOpt[JsValue]
                    }
                }
            }

            val arg = toJson(Map(
                "optimizationType" -> toJson(optimi_type.get),
                "productBudgets" -> budget_list.get,
                "productOptimizations" -> optimi_list.get,
                "times" -> toJson(batchs_time.get)
            ))

            //将JSON写入文件，提供路径给R，R根据路径自动读取Json内容。
            val json_name = s"${UUID.randomUUID().toString}.json"

            write2File(R_Json_Path)(json_name)(pw => pw.write(arg.toString))

            Map("result" -> toJson(json_name))
        } catch {
            case e: Exception => errorToMap(e.getMessage)
        }
    }
}
