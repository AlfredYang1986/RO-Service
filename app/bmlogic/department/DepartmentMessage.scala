package bmlogic.department

import bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by liwei on 2017/6/19.
  */
abstract class msg_DepartmentCommand extends CommonMessage

object DepartmentMessage {
    case class msg_PushDepartment(data : JsValue) extends msg_DepartmentCommand
    case class msg_UpdateDepartment(data : JsValue) extends msg_DepartmentCommand
    case class msg_DropDepartment(data : JsValue) extends msg_DepartmentCommand

    case class msg_QueryDepartment(data : JsValue) extends msg_DepartmentCommand
    case class msg_ListDepartment(data : JsValue) extends msg_DepartmentCommand
    case class msg_PageDepartment(data : JsValue) extends msg_DepartmentCommand
    case class msg_QueryDepartmentPr() extends msg_DepartmentCommand
}
