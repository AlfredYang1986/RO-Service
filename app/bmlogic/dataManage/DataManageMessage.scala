package bmlogic.dataManage

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by clock on 2017/7/31.
  */
abstract class msg_DataManageCommand extends CommonMessage

object DataManageMessage {
    case class msg_DataImport(data : JsValue) extends msg_DataManageCommand
}