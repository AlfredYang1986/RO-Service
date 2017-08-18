package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthMessage.{msg_AuthTokenParser, msg_CheckTokenExpire}
import bmlogic.common.requestArgsQuery
import bmlogic.resultsreport.BudgetOptMessage._
import bmlogic.resultsreport.ProfitOptMessage._
import bmlogic.resultsreport.RevenueOptMessage._
import bmmessages.{CommonModules, MessageRoutes}
import bmpattern.LogMessage.msg_log
import bmpattern.ResultMessage.msg_CommonResultMessage
import play.api.libs.json.Json.toJson
import play.api.mvc._
/**
  * Created by liwei on 2017/7/19.
  */
class ResultsComparisonController @Inject () (as_inject : ActorSystem, dbt : DBTrait, att : AuthTokenTrait) extends Controller {
    implicit val as = as_inject

    def listBudgetOptPr = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("listBudgetOpt"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_BudgetResultComparison(jv)
            :: msg_CommonResultMessage()
            :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def listRevenueOptPr = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("listRevenueOpt"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_RevenueResultComparison(jv)
            :: msg_CommonResultMessage()
            :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def listProfitOptPr = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("listProfitOpt"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_ProfitResultComparison(jv)
            :: msg_CommonResultMessage()
            :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })


    def exportBudgetOpt = Action(
        request => requestArgsQuery().requestArgsV2(request) { jv =>
            import bmpattern.LogMessage.common_log
            import bmpattern.ResultMessage.common_result
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("listBudgetOpt"))), jv)
                :: msg_AuthTokenParser(jv)
                :: msg_CheckTokenExpire(jv)
                :: msg_BudgetResultExport(jv)
                :: msg_CommonResultMessage()
                :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def exportRevenueOpt = Action(
        request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("listRevenueOpt"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_RevenueResultExport(jv)
            :: msg_CommonResultMessage()
            :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def exportProfitOpt = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("listProfitOpt"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_ProfitResultExport(jv)
            :: msg_CommonResultMessage()
            :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
