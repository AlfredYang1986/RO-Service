package bmlogic.callQueue.XMPP

import bmutil.errorcode.ErrorCode
import io.swagger.client.ApiException
import io.swagger.client.model.{Msg, MsgContent, UserName}

/**
  * Created by clock on 2017/7/27.
  * copy qianpeng
  */
class queueMessageProxy extends alMessage {

	override def sendMsg(content: String, toUser: String, ext: Map[String, String] = Map.empty): Boolean = {
		try {
			val userName = new UserName()
			userName.add(toUser)
			invokeEasemobAPI(new Msg().from("project").target(userName).targetType("users").msg(new MsgContent().`type`(MsgContent.TypeEnum.TXT).msg(content)).ext(ext))
			true
		} catch {
			case e: ApiException =>
				e.getCode match {
					case 401 => println(ErrorCode.errorToJson("error client secre").toString())
					case 500 => println(ErrorCode.errorToJson("error server").toString())
					case _ => println(ErrorCode.errorToJson("unknow error").toString())
				}
				false
		}
	}
}
