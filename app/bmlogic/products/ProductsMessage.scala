package bmlogic.products

/**
  * Created by jeorch on 17-6-14.
  */
import play.api.libs.json.JsValue
import bmmessages.CommonMessage

abstract class msg_ProductCommand extends CommonMessage

object ProductsMessage {
	case class msg_PushProduct(data : JsValue) extends msg_ProductCommand
	case class msg_UpdateProduct(data : JsValue) extends msg_ProductCommand
	case class msg_DropProduct(data : JsValue) extends msg_ProductCommand

	case class msg_QueryProduct(data : JsValue) extends msg_ProductCommand
	case class msg_ListProduct(data : JsValue) extends msg_ProductCommand
	case class msg_PageProduct(data : JsValue) extends msg_ProductCommand

	case class msg_LinkProductWithDepartment(data : JsValue) extends msg_ProductCommand
}
