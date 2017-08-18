package controllers

import bmlogic.common.requestVerify
import play.api.mvc.{Action, Controller}

/**
  * Created by liwei on 2017/6/14.
  */
class AstellasController extends Controller{
    def goHome = Action {request => requestVerify().verifylogin(request,"goHome")}

    def login = Action{Ok(views.html.login())}

    def goBusinessAnalysis = Action { request =>
        requestVerify().verifylogin(request,"goBusinessAnalysis")
    }

    def goBudgetOptimization = Action { request =>
        requestVerify().verifylogin(request,"goBudgetOptimization")
    }

    def goResultsReport = Action { request =>
        requestVerify().verifylogin(request,"goResultsReport")
    }

    def goResultsComparison = Action { request =>
        requestVerify().verifylogin(request,"goResultsComparison")
    }

    def goUserManage = Action { request =>
        requestVerify().verifylogin(request,"goUserManage")
    }

    def goCompanyManage = Action { request =>
        requestVerify().verifylogin(request,"goCompanyManage")
    }

    def goDepartmentManage = Action { request =>
        requestVerify().verifylogin(request,"goDepartmentManage")
    }

    def goProductManage = Action { request =>
        requestVerify().verifylogin(request,"goProductManage")
    }

    def goChannelManage = Action { request =>
        requestVerify().verifylogin(request,"goChannelManage")
    }

    def goDataManage = Action { request =>
        requestVerify().verifylogin(request,"goDataManage")
    }
}
