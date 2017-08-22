package bmlogic.common

import play.api.mvc._

object requestVerify {
	def apply() = new requestVerify()
}

class requestVerify extends Controller {
	def verifylogin(request : Request[AnyContent], page : String) : Result = {
		try {
			page match {
				case "goBusinessIntroduction" => {
					Ok(views.html.BusinessIntroduction("BusinessIntroduction"))
				}
				case "goResourceManagement" => {
					Ok(views.html.ResourceManagement("ResourceManagement"))
				}
				case "goResourceOptimization" => {
					Ok(views.html.ResourceOptimization("ResourceOptimization"))
				}
				case "goRegionalManagement" => {
					Ok(views.html.RegionalManagement("RegionalManagement"))
				}
				case "goTaskManagement" => {
					Ok(views.html.TaskManagement("TaskManagement"))
				}
				case "goCustomerManagement" => {
					Ok(views.html.CustomerManagement("CustomerManagement"))
				}
				case "goResultsReport" => {
					Ok(views.html.ResultsReport("ResultsReport"))
				}
			}
		} catch {
			case _ : Exception => BadRequest("Bad Request for input")
		}
	}
}