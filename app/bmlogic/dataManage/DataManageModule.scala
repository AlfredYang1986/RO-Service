package bmlogic.dataManage

import bminjection.db.DBTrait
import bmlogic.dataManage.DataManageMessage.msg_DataImport
import bmlogic.dataManage.dataManageData.DataManageInfo
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import bmutil.excel.scala.DefaultData._
import com.mongodb.casbah.Imports.DBObject
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import scala.collection.immutable.Map

/**
  * Created by clock on 2017/7/31.
  */
object DataManageModule extends ModuleTrait with DataManageInfo{
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_DataImport(data) => dataImport(data)(pr)
        case _ => ???
    }

    def dataImport(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))

            val str = """auto import data"""

            val collection_name = (data \ "collection_name").asOpt[String].map (x => x).getOrElse(throw new Exception("auto import input js error"))
            val file_path = (data \ "file_path").asOpt[String].map (x => x).getOrElse(throw new Exception("auto import input js error"))

            collection_name match {
                case "budget" => {
                    val lst = productbudgetbase(file_path)
                    val result = db.queryObject(lst, collection_name)
                    if(result.isEmpty) importBudget(lst)
                }
                case "productOptimization" => {
                    val lst = productoptimizationbase(file_path)
                    val result = db.queryObject(lst, collection_name)
                    if(result.isEmpty) importOptimization(lst)
                }
                case _ => throw new Exception("auto import input js error")
            }

            (Some(Map(
                "method" -> toJson("dataImport"),
                "result" -> toJson("ok")
            )),None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
