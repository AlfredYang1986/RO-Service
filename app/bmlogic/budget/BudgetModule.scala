package bmlogic.budget

import bminjection.db.DBTrait
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import play.api.libs.json.JsValue
import bmlogic.budget.BudgetMessage._
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import bmlogic.common.sercurity.Sercurity
import java.util.Date

import bmlogic.budget.BudgetData.BudgetInfo
import bmutil.dao._data_connection

/**
  * Created by liwei on 2017/6/14.
  */
object BudgetModule extends ModuleTrait with BudgetInfo{
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_PushBudget(data) => pushBudget(data)(pr)
        case msg_UpdateBudget(data) => batchUpdateBudget(data)(pr)
        case msg_DropBudget(data) => dropBudget(data)
        case msg_QueryBudget(data) => queryBudget(data)
        case msg_ListBudget(data) => listBudget(data)(pr)

        case _ => ???
    }

    def pushBudget(data : JsValue)
                  (pr : Option[Map[String, JsValue]])
                  (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val str = """budget seed"""

            (Some(Map("method" -> toJson("pushBudget"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateBudget(data : JsValue)
            (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o : DBObject = data
            o += "budget_id" -> (data \ "budget_id").get.asOpt[String].get

            db.updateObject(obj=o, db_name="budget", primary_key="budget_id")

            (Some(Map("method" -> toJson("updateBudget"), "result" -> toJson(o - "tmp"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def batchUpdateBudget(data : JsValue)
                      (pr : Option[Map[String, JsValue]])
                      (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val result = pr.get("result").asOpt[List[JsValue]].get
            val budget_id = (data \ "budget_id").get.asOpt[String].get

            result foreach { x =>
                val budget_id_pr = (x \ "budget_id").get.asOpt[String].get

                if(budget_id_pr.equals(budget_id)){
                    val o : DBObject = data
                    o += "budget_id" -> budget_id
                    update(o)
                }else{
                    val budget_totle = (data \ "budget_totle").get.asOpt[Double].get
                    val product_ratio = (x \ "product_ratio").get.asOpt[Double].get
                    val o : DBObject = x
                    o += "budget_id" -> budget_id_pr
                    o += "budget_totle" -> budget_totle.asInstanceOf[Number]
                    o += "product_cost" -> (budget_totle * product_ratio / 100).asInstanceOf[Number]
                    update(o)
                }
            }

            def update(o : DBObject) = db.updateObject(obj=o, db_name="budget", primary_key="budget_id")

            (Some(Map("method" -> toJson("updateBudgetPr"), "result" -> toJson("tmp"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropBudget(data : JsValue)
                  (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            (Some(Map("method" -> toJson("dropBudget"), "result" -> toJson("Ok"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryBudget(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val o = condition(data)

            val result = db.queryObject(condition=o, db_name="budget")

            if (result.isEmpty) throw new Exception("budget input js error")
            else (Some(Map("method" -> toJson("queryBudget"), "result" -> toJson(result.get))) , None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def listBudget(data : JsValue)
                  (pr : Option[Map[String, JsValue]])
                  (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val auth = pr.get("auth")
            val channel = (auth \ "channel").asOpt[List[Map[String,JsValue]]].map(x => x).get
            val scopes = channel.groupBy(x => x("scope").asOpt[String].map(x => x).get).map(x => x._1)

            val o = condition(auth)

            val result = db.queryMultipleObject(condition = o, db_name = "budget",take=0).filter(x => scopes.exists(y => y.equals(x("scope_name_ch").asOpt[String].get)))

            (Some(Map("method" -> toJson("listBudget"), "result" -> toJson(result))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
