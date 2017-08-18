package controllers

import javax.inject._

import akka.actor.ActorSystem
import bminjection.db.DBTrait
import bminjection.token.AuthTokenTrait
import bmlogic.auth.AuthMessage._
import bmlogic.channel.ChannelMessage._
import bmlogic.common.requestArgsQuery
import bmlogic.company.CompanyMessage.msg_ListCompany
import bmlogic.department.DepartmentMessage.msg_QueryDepartmentPr
import bmmessages._
import bmpattern.LogMessage.msg_log
import bmpattern.ResultMessage.msg_CommonResultMessage
import play.api.libs.json.Json.toJson
import play.api.mvc._

class AuthController @Inject () (as_inject : ActorSystem, dbt : DBTrait, att : AuthTokenTrait) extends Controller {
    implicit val as = as_inject

    def authPushUser = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("authPushUser"))), jv)
            :: msg_AuthPushUser(jv)
            :: msg_ListChannelPr()
            :: msg_QueryDepartmentPr()
            :: msg_GenerateToken()
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
        })

    def authWithPassword = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("authWithPassword"))), jv)
            :: msg_AuthWithPassword(jv)
            :: msg_ListChannelPr()
            :: msg_QueryDepartmentPr()
            :: msg_GenerateToken()
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
        })

    def authTokenCheckTest = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("authTokenCheckTest"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckAuthTokenTest(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
        })

    def queryUser = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("queryUser"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_ListCompany(jv)
            :: msg_QueryUser(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def queryMultipleUser = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("queryMultipleUser"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_QueryMultipleUser(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def pageUser = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("pageUser"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_PageUser(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def authChangePwd = Action (Ok(""))


    def authUpdate = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("authUpdate"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_AuthUpdateUser(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def authDropUser = Action (request => requestArgsQuery().requestArgsV2(request) { jv =>
        import bmpattern.LogMessage.common_log
        import bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("authDropUser"))), jv)
            :: msg_AuthTokenParser(jv)
            :: msg_CheckTokenExpire(jv)
            :: msg_AuthDropUser(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def authLogout = Action (Ok(""))
}