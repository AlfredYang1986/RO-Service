package bmlogic.optimization

import bminjection.db.DBTrait
import bmlogic.optimization.OptimizationMessage._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import bmlogic.optimization.optimizationData.OptimizationInfo

/**
  * Created by liwei on 2017/6/20.
  */
object OptimizationModule extends ModuleTrait with OptimizationInfo{
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_PushOptimization(data) => pushOptimization(data)(pr)
        case msg_BatchUpdateOptimization(data) => batchUpdateOptimization(data)(pr)
        case msg_DropOptimization(data) => dropOptimization(data)
        case msg_QueryOptimization(data) => queryOptimization(data)
        case msg_ListOptimization(data) => listOptimization(data)(pr)

        case msg_QueryOptimizationByCDS(data) => queryOptimizationByCDS(data)(pr)
        case msg_UpdateOptimizationByBudget(data) => updateOptimizationByBudget(data)(pr)
        case msg_ListOptimizationByBudget(data) => listOptimizationByBudget(data)(pr)

        case _ => ???
    }

    def pushOptimization(data : JsValue)
                        (pr : Option[Map[String, JsValue]])
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val str = """product optimization seed"""

            (Some(Map("method" -> toJson("pushOptimization"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def batchUpdateOptimization(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val result = pr.get("result").asOpt[List[JsValue]].get
            val budget_criteria = (data \ "budget_criteria").get.asOpt[Double].get
            val income_standards = (data \ "income_standards").get.asOpt[Double].get
            val channel_name_ch_data = (data \ "channel_name_ch").get.asOpt[String].get

            result foreach{x =>
                val channel_name_ch = (x \ "channel_name_ch").get.asOpt[String].get
                val measured_budget_ratio = (x \ "measured_budget_ratio").get.asOpt[Double].get
                if(channel_name_ch_data.equals(channel_name_ch)){
                    val o: DBObject = data
                    o += "pro_opt_id" -> (data \ "pro_opt_id").get.asOpt[String].get
                    db.updateObject(o, "productOptimization", "pro_opt_id")
                }else{
                    val o: DBObject = x
                    o += "pro_opt_id" -> (x \ "pro_opt_id").get.asOpt[String].get
                    o += "budget_criteria" -> budget_criteria.asInstanceOf[Number]
                    o += "income_standards" -> income_standards.asInstanceOf[Number]
                    o += "calculate_budget_values" -> (measured_budget_ratio / 100 * budget_criteria).asInstanceOf[Number]
                    db.updateObject(o, "productOptimization", "pro_opt_id")
                }
            }

            (Some(Map("method" -> toJson("updateOptimization"),"result" -> toJson("tmp"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropOptimization(data : JsValue)
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            (Some(Map("method" -> toJson("dropOptimization"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryOptimization(data : JsValue)
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)
            val result = db.queryObject(condition=o, db_name="productOptimization")

            if (result.isEmpty) throw new Exception("product optimization input js error")
            else (result, None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def listOptimization(data : JsValue)
                        (pr : Option[Map[String, JsValue]])
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val auth = pr.get("auth")

            val channel = (auth \ "channel").asOpt[List[Map[String,JsValue]]].map(x => x).get
            val products = channel.groupBy(x => x("scope").asOpt[String].map(x => x).get).map(x => x._1)

            val company = (auth \ "company").get.asOpt[String].get
            val department = (auth \ "department").get.asOpt[String].get

            val scope_name_ch = (data \ "scope_name_ch").get.asOpt[String].get

            val selproduct = scope_name_ch match {
                case "" => products.head
                case _ => scope_name_ch
            }

            val args = toJson(Map("company" -> toJson(company),"department" -> toJson(department),"scope_name_ch" -> toJson(selproduct)))

            val o = condition(args)
            val result = db.queryMultipleObject(condition = o, db_name = "productOptimization", sort = "now_budget",take=0)
            (Some(
                Map(
                    "method" -> toJson("listOptimization"),
                    "result" -> toJson(result),
                    "products" -> toJson(products),
                    "selproduct" -> toJson(selproduct)
                )
            ), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }


    def queryOptimizationByCDS(data : JsValue)
                              (pr : Option[Map[String, JsValue]])
                              (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val auth = pr.get("auth")

            val company = (auth \ "company").asOpt[String].map(x => x).get
            val department = (auth \ "department").asOpt[String].map(x => x).get

            val o = condition(toJson(Map(
                "company" -> toJson(company),
                "department" -> toJson(department),
                "scope_name_ch" -> toJson((data \ "scope_name_ch").get.asOpt[String].get)
            )))
            val result = db.queryMultipleObject(condition = o, db_name = "productOptimization",sort="data",take=0)

            (Some(Map("method" -> toJson("queryOptimizationByCDS"),"result" -> toJson(result))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateOptimizationByBudget(data : JsValue)
                            (pr : Option[Map[String, JsValue]])
                            (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val result = pr.get("result").asOpt[List[JsValue]].get
            result foreach{ x =>
                val product_cost = (x \ "product_cost").get.asOpt[Double].get
                val o1 : DBObject = condition(x)
                val lst = db.queryMultipleObject(o1,"productOptimization")
                lst foreach { y =>
                    val o2 : DBObject = toJson(y)
                    o2 += "pro_opt_id" -> y("pro_opt_id").asOpt[String].map(x => x).get
                    o2 += "budget_criteria" -> product_cost.asInstanceOf[Number]
                    db.updateObject(o2, "productOptimization", "pro_opt_id")
                }
            }
            (Some(Map("method" -> toJson("updateOptimizationPr"),"result" -> toJson("tmp"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def listOptimizationByBudget(data : JsValue)
                           (pr : Option[Map[String, JsValue]])
                           (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val o = condition(pr.get("auth"))
            val result = db.queryMultipleObject(condition = o, db_name = "productOptimization",sort="data",skip=0,take=100)

            (Some(Map("method" -> toJson("listOptimizationPr2"),"result" -> toJson(result))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}