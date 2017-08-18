package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthMessage.{msg_AuthTokenParser, msg_CheckAuthTokenTest}
import bmlogic.common.requestArgsQuery
import bmlogic.residual.ResidualMessage._
import bmmessages.{CommonModules, MessageRoutes}
import bmpattern.LogMessage.msg_log
import bmpattern.ResultMessage.msg_CommonResultMessage
import play.api.libs.json.Json.toJson
import play.api.mvc.{Action, Controller}

/**
  * Created by clock on 2017/7/1.
  */
class ResidualController @Inject ()(as_inject : ActorSystem, dbt : DBTrait, att : AuthTokenTrait) extends Controller{
    implicit val as = as_inject

    def pushResidual = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push residual"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_PushResidual(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def dropResidual = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("drop residual"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_DropResidual(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def updateResidual = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("update residual"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_UpdateResidual(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def lstResidual = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("list residual"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_LstResidual(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
