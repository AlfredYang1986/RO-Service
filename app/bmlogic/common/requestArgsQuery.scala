package bmlogic.common

import play.api.mvc._
import play.api.libs.json.JsValue
import play.api.libs.Files.TemporaryFile

import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.duration._
import scala.concurrent.Await

import bmmessages.MessageRoutes
import bmmessages.excute
import bmpattern.RoutesActor

import javax.inject._

object requestArgsQuery {
    def apply()(implicit akkasys : ActorSystem) = new requestArgsQuery()
}

class requestArgsQuery @Inject() (implicit akkasys : ActorSystem) extends Controller {
	implicit val t = Timeout(3000 seconds)

  	def requestArgs(request : Request[AnyContent])(func : JsValue => JsValue) : Result = {
  		try {
  			request.body.asJson.map { x =>
  				Ok(func(x))
  			}.getOrElse (BadRequest("Bad Request for input"))
  		} catch {
  			case _ : Exception => BadRequest("Bad Request for input")
  		}  		   
	}
	
  	def requestArgsV2(request : Request[AnyContent])(func : JsValue => MessageRoutes) : Result = {
  		try {

  			request.body.asJson.map { x => 
  				Ok(commonExcution(func(x)))
  			}.getOrElse (BadRequest("Bad Request for input"))
  		} catch {
  			case _ : Exception => BadRequest("Bad Request for input")
  		}  		   
	}
  	
  	def commonExcution(msr : MessageRoutes) : JsValue = {
		val act = akkasys.actorOf(Props[RoutesActor])
		val r = act ? excute(msr)
		Await.result(r.mapTo[JsValue], t.duration)
	}
 
  	def uploadRequestArgs(request : Request[AnyContent])(func : MultipartFormData[TemporaryFile] => JsValue) : Result = {
  		try {
   			request.body.asMultipartFormData.map { x => 
   				Ok(func(x))
  			}.getOrElse (BadRequest("Bad Request for input1"))
  		} catch {
  			case _ : Exception => BadRequest("Bad Request for input2")
  		}
  	}
}

object requestVerify {
	def apply() = new requestVerify()
}

class requestVerify extends Controller {
	def verifylogin(request : Request[AnyContent], page : String) : Result = {
		try {
			if(!request.cookies.isEmpty){
				val user_name = request.cookies.get("user_name").get.value
				val level = request.cookies.get("level").get.value
				if(!user_name.equals("") && !user_name.equals(null)){
					page match {
						case "goBusinessAnalysis" => {
							Ok(views.html.BusinessAnalysis(user_name, level, "BusinessAnalysis"))
						}
						case "goBudgetOptimization" => {
							Ok(views.html.BudgetOptimization(user_name, level, "BudgetOptimization"))
						}
						case "goResultsReport" => {
							Ok(views.html.ResultsReport(user_name, level, "ResultsReport"))
						}
						case "goResultsComparison" => {
							Ok(views.html.ResultsComparison(user_name, level, "ResultsComparison"))
						}
						case "goUserManage" => {
							Ok(views.html.UserManage(user_name, level, "UserManage"))
						}
						case "goCompanyManage" => {
							Ok(views.html.CompanyManage(user_name, level, "CompanyManage"))
						}
						case "goDepartmentManage" => {
							Ok(views.html.DepartmentManage(user_name, level, "DepartmentManage"))
						}
						case "goProductManage" => {
							Ok(views.html.ProductManage(user_name, level, "ProductManage"))
						}
						case "goChannelManage" => {
							Ok(views.html.ChannelManage(user_name, level, "ChannelManage"))
						}
						case "goDataManage" => {
							Ok(views.html.DataManage(user_name, level, "DataManage"))
						}
					}
				}else{
					Ok(views.html.login())
				}
			}else{
				Ok(views.html.login())
			}
		} catch {
			case _ : Exception => BadRequest("Bad Request for input")
		}
	}
}