package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthMessage.{msg_AuthTokenParser, msg_CheckAuthTokenTest}
import bmlogic.common.requestArgsQuery
import bmlogic.optimizationAnalysis.OptimizationAnalysisMessage._
import bmmessages.{CommonModules, MessageRoutes}
import bmpattern.LogMessage.msg_log
import bmpattern.ResultMessage.msg_CommonResultMessage
import play.api.libs.json.Json.toJson
import play.api.mvc.{Action, Controller}

/**
  * Created by clock on 2017/7/1.
  */
class OptimizedController @Inject ()(as_inject : ActorSystem, dbt : DBTrait, att : AuthTokenTrait) extends Controller{
    implicit val as = as_inject

    def analysis = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("analysis"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_AnalysisOptimization(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def report = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("report"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_ReportOptimization(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def lstOptimization = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("lstOptimization"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_LstOptimization(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def updateOptimization = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("updateOptimization"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_UpdateOptimization(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
