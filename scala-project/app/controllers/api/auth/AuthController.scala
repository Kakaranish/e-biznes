package controllers.api.auth

import com.mohiva.play.silhouette.api.Silhouette
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(cc: MessagesControllerComponents,
                               silhouette: Silhouette[DefaultEnv])
                              (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc)
    with I18nSupport {

  def verify() = silhouette.SecuredAction.async { implicit request =>
    val user = request.identity
    Future.successful(Ok(Json.obj(
      "email" -> user.email,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "role" -> user.role
    )))
  }
}
