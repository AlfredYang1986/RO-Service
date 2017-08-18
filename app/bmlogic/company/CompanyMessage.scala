package bmlogic.company

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/8/1.
  */
abstract class msg_CompanysCommand extends CommonMessage

object CompanyMessage {
    /**
      * 公司信息
      */
    case class msg_PushCompany(data : JsValue) extends msg_CompanysCommand
    case class msg_UpdateCompany(data : JsValue) extends msg_CompanysCommand
    case class msg_DropCompany(data : JsValue) extends msg_CompanysCommand
    case class msg_QueryCompany(data : JsValue) extends msg_CompanysCommand
    case class msg_ListCompany(data : JsValue) extends msg_CompanysCommand
    case class msg_PageCompany(data : JsValue) extends msg_CompanysCommand
}
