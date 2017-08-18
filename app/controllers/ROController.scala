package controllers

import bmlogic.common.requestVerify
import play.api.mvc.{Action, Controller}

/**
  * Created by liwei on 2017/6/14.
  */
class ROController extends Controller{
    def goHome = Action {request =>
        requestVerify().verifylogin(request,"goHome")
    }

    def login = Action{Ok(views.html.login())}

    def goBusinessIntroduction = Action { request =>
        requestVerify().verifylogin(request,"goBusinessIntroduction")
    }

    def goResourceManagement = Action { request =>
        requestVerify().verifylogin(request,"goResourceManagement")
    }

    def goResourceOptimization = Action { request =>
        requestVerify().verifylogin(request,"goResourceOptimization")
    }
}
