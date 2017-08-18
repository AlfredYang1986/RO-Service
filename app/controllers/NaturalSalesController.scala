package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthMessage.{msg_AuthTokenParser, msg_CheckAuthTokenTest}
import bmlogic.common.requestArgsQuery
import bmlogic.naturalSales.NaturalSalesMessage._
import bmmessages.{CommonModules, MessageRoutes}
import bmpattern.LogMessage.msg_log
import bmpattern.ResultMessage.msg_CommonResultMessage
import play.api.libs.json.Json.toJson
import play.api.mvc.{Action, Controller}

/**
  * Created by clock on 2017/6/28.
  */
class NaturalSalesController @Inject ()(as_inject : ActorSystem, dbt : DBTrait, att : AuthTokenTrait) extends Controller{
    implicit val as = as_inject

    def pushNaturalSales = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push natural sales"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_PushNaturalSales(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def dropNaturalSales = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("drop natural sales"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_DropNaturalSales(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def updateNaturalSales = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("update natural sales"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_UpdateNaturalSales(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def lstNaturalSales = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("list natural sales"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_LstNaturalSales(jv)//执行的方法
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
