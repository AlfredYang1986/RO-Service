package bmlogic.common

import javax.inject._

import play.api.http.DefaultHttpErrorHandler
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing.Router
import scala.concurrent._
/**
  * Created by liwei on 2017/8/11.
  */
@Singleton
class ErrorHandler @Inject() (
         env: Environment,
         config: Configuration,
         sourceMapper: OptionalSourceMapper,
         router: Provider[Router]
     ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

    override def onProdServerError(request: RequestHeader, exception: UsefulException) = {
        Future.successful(Ok(views.html.InternalServerError()))
    }

    override def onNotFound(request: RequestHeader, message: String) = {
        Future.successful(Ok(views.html.NotFound()))
    }
}
