package controllers

import play.api.mvc.{Action, Controller}

/**
  * Created by clock on 2017/6/28.
  */
class CommonPageController  extends Controller {
    def goNaturalSalesEstirmate = Action {
        Ok(views.html.naturalSalesEstirmate())
    }

    def goResidualEstirmate = Action {
        Ok(views.html.residualEstirmate())
    }

    def goUploadData = Action {
        Ok(views.html.uploadData())
    }

    def goEstirmateAnalysis = Action {
        Ok(views.html.estirmateAnalysis())
    }

    def goEstirmateReport = Action {
        Ok(views.html.estirmateReport())
    }

    def goOptimizedAnalysis = Action {
        Ok(views.html.optimizationAnalysis())
    }

    def goOptimizedReport = Action {
        Ok(views.html.optimizationReport())
    }
}