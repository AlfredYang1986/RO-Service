package bmlogic.callQueue.XMPP

import play.api.libs.json.{JsValue, Json}

import scala.concurrent.stm.{Ref, atomic}
import io.swagger.client.api.{AuthenticationApi, MessagesApi}
import io.swagger.client.model.{Msg, Token}
import bmutil.emChatConfig._
import bmutil.errorcode.ErrorCode

/**
  * Created by clock on 2017/7/27.
  * copy qianpeng
  */

sealed trait alEmChat{
	val alOrgName = orgName

	val alAppName = appName

	val grant_type = grantType

	val client_id = clientId

	val client_secret = clientSecret

	val access = Ref(Map[String, Any]())

	val api = new MessagesApi

	def initTokenByProp() = {
		val map = Json.parse(new AuthenticationApi().
			orgNameAppNameTokenPost(alOrgName,
				alAppName,
				new Token().clientId(client_id).
					grantType(grant_type).
					clientSecret(client_secret))).as[Map[String, JsValue]]
		atomic {implicit tnx =>
			access() = access() ++ Map("access_token" -> (" Bearer " + map("access_token").asOpt[String].getOrElse(ErrorCode.getErrorMessageByName("input error"))),
				"expirdat" -> (System.currentTimeMillis() + map("expires_in").asOpt[Double].getOrElse(-1D)))
		}
	}

	protected def invokeEasemobAPI(payload: Msg): String = {
		api.orgNameAppNameMessagesPost(alOrgName, alAppName, getAccessToken, payload)
	}

	def getAccessToken(): String = {
		if(access.single.get.isEmpty ||
			System.currentTimeMillis() > access.single.get("expirdat").asInstanceOf[Double])
			initTokenByProp()
		access.single.get("access_token").toString
	}
}

trait alMessage extends alEmChat{
	def sendMsg(content: String, toUser: String, ext: Map[String, String] = Map.empty): Boolean

}
