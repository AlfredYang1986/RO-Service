package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthMessage.{msg_AuthTokenParser, msg_CheckAuthTokenTest}
import bmlogic.callQueue.XMPP.queueMessageProxy
import bmlogic.common.requestArgsQuery
import bmlogic.estirmateAnalysis.EstirmateAnalysisMessage._
import bmlogic.naturalSales.NaturalSalesMessage.msg_LstNaturalSales
import bmmessages.{CommonModules, MessageRoutes}
import bmpattern.LogMessage.msg_log
import bmpattern.ResultMessage.msg_CommonResultMessage
import play.api.libs.json.Json.toJson
import play.api.mvc.{Action, Controller}

/**
  * Created by clock on 2017/7/10.
  */
class EstirmateController @Inject ()(as_inject : ActorSystem, dbt : DBTrait, att : AuthTokenTrait) extends Controller{
    implicit val as = as_inject

    def analysis = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("update residual"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_AnalysisEstirmate(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def report = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("list residual"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_LstNaturalSales(jv)
            :: msg_ReportEstirmate(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
