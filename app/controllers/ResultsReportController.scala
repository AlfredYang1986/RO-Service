package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthMessage._
import bmlogic.common.requestArgsQuery
import bmlogic.optimization.OptimizationMessage._
import bmlogic.resultsreport.ResultsReportMessage._
import bmlogic.resultsreport.BudgetOptMessage._
import bmlogic.resultsreport.ProfitOptMessage._
import bmlogic.resultsreport.RevenueOptMessage._
import bmmessages.{CommonModules, MessageRoutes}
import bmpattern.LogMessage.msg_log
import play.api.libs.json.Json.toJson
import play.api.mvc._
import bmpattern.ParallelMessage
import bmlogic.budget.BudgetMessage._
import bmlogic.callQueue.QueueCommand.msg_Queueup
import bmpattern.ResultMessage.msg_CommonResultMessage

/**
  * Created by liwei on 2017/6/13.
  */
class ResultsReportController @Inject () (as_inject : ActorSystem, dbt : DBTrait, att : AuthTokenTrait) extends Controller{
    implicit val as = as_inject

    def executeRProcess = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmlogic.resultsreport.ResultsReportModule.serviceResultMerge
        import bmpattern.ResultMessage.lst_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("executeRProcess"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: ParallelMessage(
                MessageRoutes(msg_LinkData(jv) :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att)))) ::
                MessageRoutes(msg_ListBudget(jv) :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att)))) ::
                MessageRoutes(msg_ListOptimizationByBudget(jv) :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att)))) :: Nil, serviceResultMerge)
            :: msg_Queueup(jv)
            :: msg_CommonResultMessage()
            :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att, "as" -> as_inject))))
    })

    def listBudgetOpt = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("listBudgetOpt"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_BudgetResultReport(jv)
            :: msg_CommonResultMessage()
            :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def listRevenueOpt = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("listRevenueOpt"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_RevenueResultReport(jv)
            :: msg_CommonResultMessage()
            :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def listProfitOpt = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("listProfitOpt"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_ProfitResultReport(jv)
            :: msg_CommonResultMessage()
            :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
