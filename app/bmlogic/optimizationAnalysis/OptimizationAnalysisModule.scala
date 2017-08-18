package bmlogic.optimizationAnalysis

import bminjection.db.DBTrait
import bmlogic.common.sercurity.Sercurity
import bmlogic.optimizationAnalysis.OptimizationAnalysisMessage._
import bmlogic.optimizationAnalysis.optimizationAnalysisData.optimizationAnalysisInfo
import bmlogic.query.{Optimization, Retention}
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.alDateOpt.{L2yyyy_MM, isOneDay}
import bmutil.errorcode.ErrorCode
import bmutil.precisionOpt.decimal2percent
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 2017/7/4.
  */
object OptimizationAnalysisModule extends ModuleTrait with optimizationAnalysisInfo with Optimization_JRImpl{
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_ReportOptimization(data) => reportOptimization(data)(pr)
        case msg_AnalysisOptimization(data) => analysisOptimization(data)(pr)
        case msg_LstOptimization(data) => lstOptimization(data)(pr)
        case msg_UpdateOptimization(data) => updateOptimization(data)(pr)
        case _ => ???
    }

    def reportOptimization(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val pr_auth = pr.get("auth")
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val user_name = (pr_auth \ "user_name").get.asOpt[String].get
            val sb: StringBuffer = new StringBuffer
            sb.append("{\"data\":[")
            sb.append("[150000,60000,\"A\",\"目前的投入产出\"],")
            sb.append("[150000,120000,\"B\", \"同样的投入，可以优化到的销售额\" ],")
            sb.append("[50000,60000,\"C\",\"同样的产出，可以优化到的总投入\"],")
            sb.append("[170000,90000,\"D\",\"如果要达到目标销售额，未优化的投入额\"],")
            sb.append("[100000,90000,\"E\",\"如果要达到目标销售额，优化后的投入额\"],")
            sb.append("[0,30000,\"F\",\"没有投入的自然销量\"]")
            sb.append("],")
            sb.append("\"maxx\":" + 200000 + ",")
            sb.append("\"maxy\":" + 150000 + ",")
            sb.append("\"minx\":" + 0 + ",")
            sb.append("\"miny\":" + 30000 + "}")

            val optimization_tb = Optimization(MongoDBObject("user_name" -> user_name),db)
            val optimization_list = optimization_tb.queryAll

            val product_name = optimization_list.head("product_name")
            val create_date = L2yyyy_MM(optimization_list.head("create_date").as[Long])
            var opt_type = optimization_list.head("opt_type").asOpt[String].get
            var uplift = optimization_list.head("uplift").asOpt[Double].get.toString

            //表格1
            if(opt_type == "Fixed Budget"){
                opt_type = "投入预算不变"
                uplift = "销量可以提升"+uplift+"%"
            }else if(opt_type.equals("Target Revenue")){
                opt_type = "达到目标收入"
                uplift = "成本可以降低"+uplift+"%"
            }else{
                opt_type = "最大化利润"
                uplift = "利润可以达到"+uplift+"%"
            }

            //精度处理//FIXME 还可以优化
            val table = optimization_list.map{ x =>
                Map(
                    "channel_name_en" -> toJson(x("channel_name_en").as[String]),
                    "current_cost" -> toJson(decimal2percent(x("current_cost").as[Double])),
                    "current_contribution" -> toJson(decimal2percent(x("current_contribution").as[Double])),
                    "opt_cost" -> toJson(decimal2percent(x("opt_cost").as[Double])),
                    "opt_contribution" -> toJson(decimal2percent(x("opt_contribution").as[Double]))
                )
            }

            (Some(Map(
                "method" -> toJson("优化报告"),
                "scatter" -> toJson(sb.toString),
                "user_name" -> toJson(user_name),
                "create_date" -> toJson(create_date),
                "product_name" -> product_name,
                "opt_type" -> toJson(opt_type),
                "uplift" -> toJson(uplift),
                "table" -> toJson(table))
            ), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def analysisOptimization(data : JsValue)
                            (pr : Option[Map[String, JsValue]])
                            (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val retention_tb = Retention(DBObject(),db)
            val retention = retention_tb.getHeadSortByDate

            val optimization_type = (data \ "optimizationType").asOpt[String].map (x => x).get
            val general_budget = (data \ "generalBudget").asOpt[Double].map (x => x).getOrElse(0D)
            val main_channel_number = (data \ "mainChannelNumber").asOpt[Double].map (x => x).get
            val base_scaling_factor = (data \ "baseScalingFactor").asOpt[Double].map (x => x).get
            val target_return = (data \ "targetReturn").asOpt[Double].map (x => x).getOrElse(0D)
            val priceper_unit = (data \ "priceperUnit").asOpt[Double].map (x => x).get
            val gross_margin = (data \ "grossMargin").asOpt[Double].map (x => x).get

            val arg = retention ++ Map(
                "optimization_type" -> toJson(optimization_type),
                "general_budget" -> toJson(general_budget),
                "main_channel_number" -> toJson(main_channel_number),
                "base_scaling_factor" -> toJson(base_scaling_factor),
                "target_return" -> toJson(target_return),
                "priceper_unit" -> toJson(priceper_unit),
                "gross_margin" -> toJson(gross_margin)

            )

            callAI(arg.toString)

            (Some(Map("method" -> toJson("优化分析"), "result" -> toJson("分析完成"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def lstOptimization(data : JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val pr_auth = pr.get("auth")
            val user_name = (pr_auth \ "user_name").get.asOpt[String].get
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            //取出该用户最后录入的渠道时间
            val retention_tb = Retention(DBObject(),db)
            val retention_list = retention_tb.queryAll
            val maxDate = retention_tb.getHeadSortByDate("create_date").as[Long]

            //删除该用户最近日期所有的垃圾数据
            val minTime = isOneDay(maxDate)(0)
            val maxTime = isOneDay(maxDate)(1)
            val condition1 = $and(
                "create_date" -> MongoDBObject("$gte" -> minTime),
                "create_date" -> MongoDBObject("$lte" -> maxTime),
                "user_name" -> user_name,
                "status" -> 0
            )
            val opt_result = db.queryMultipleObject(condition1,db_name = "optimizationInputTemplate")
            opt_result.foreach(x => db.deleteObject(condition(toJson(x)),"optimizationInputTemplate","analysis_id"))


            //获取该用户最近日期的所有渠道名称
            val retention_max_data_result = retention_list.filter(_("create_date").as[Long] > minTime).filter(_("create_date").as[Long] < maxTime)
            val str = """optimization input"""

            //对于优化表中没有的记录追加，有则不变
            retention_max_data_result.foreach(x => {
                val temp = db.queryObject(findByCondition(x,minTime,maxTime),db_name = "optimizationInputTemplate")
                if(temp.isEmpty){
                    var o : DBObject = toJson(x)
                    o += "analysis_id" -> Sercurity.md5Hash(str + user_name + Sercurity.getTimeSpanWithMillSeconds)
                    db.insertObject(o, "optimizationInputTemplate", "analysis_id")
                }
            })

            val result = db.queryMultipleObject(MongoDBObject("user_name"->user_name), db_name = "optimizationInputTemplate")

            (Some(Map("result" -> toJson(result))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateOptimization(data : JsValue)
                        (pr : Option[Map[String, JsValue]])
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val o : DBObject = data
            o += "analysis_id" -> (data \ "analysis_id").get.asOpt[String].get

            db.updateObject(o, "optimizationInputTemplate", "analysis_id")

            (Some(Map("method" -> toJson("update optimization analysis"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

}
