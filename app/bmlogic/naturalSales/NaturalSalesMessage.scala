package bmlogic.naturalSales

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by clock on 2017/6/29.
  */
abstract class msg_NaturalSalesCommand extends CommonMessage

object NaturalSalesMessage {
    case class msg_PushNaturalSales(data : JsValue) extends msg_NaturalSalesCommand
    case class msg_DropNaturalSales(data : JsValue) extends msg_NaturalSalesCommand
    case class msg_UpdateNaturalSales(data : JsValue) extends msg_NaturalSalesCommand
    case class msg_LstNaturalSales(data : JsValue) extends msg_NaturalSalesCommand
}