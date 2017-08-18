package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.common.requestArgsQuery
import bmlogic.budget.BudgetMessage._
import bmlogic.auth.AuthMessage._
import bmmessages.{CommonModules, MessageRoutes}
import bmpattern.LogMessage.msg_log
import bmpattern.ResultMessage.msg_CommonResultMessage
import play.api.libs.json.Json.toJson
import play.api.mvc._
/**
  * Created by liwei on 2017/6/13.
  */
class BudgetController @Inject ()(as_inject : ActorSystem, dbt : DBTrait, att : AuthTokenTrait)  extends Controller{
    implicit val as = as_inject

    def pushBudget = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("pushBudget"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_PushBudget(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    });

    def updateBudget = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("updateBudget"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_ListBudget(jv)
            :: msg_UpdateBudget(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def dropBudget = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("dropBudget"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_DropBudget(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def queryBudget = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("queryBudget"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_QueryBudget(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def listBudget = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("listBudget"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_ListBudget(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
