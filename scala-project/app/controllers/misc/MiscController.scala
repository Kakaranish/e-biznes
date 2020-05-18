package controllers.misc

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import daos.LoginInfoDao
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import services.UserService
import silhouette.DefaultEnv

import scala.concurrent.ExecutionContext

@Singleton
class MiscController @Inject()(cc: MessagesControllerComponents,
                               silhouette: Silhouette[DefaultEnv],
                               userService: UserService,
                               loginInfoDao: LoginInfoDao,
                               authInfoRepository: AuthInfoRepository,
                               passwordHasherRegistry: PasswordHasherRegistry,
                               credentialsProvider: CredentialsProvider)
                              (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc)
    with I18nSupport {

  def restricted() = silhouette.SecuredAction { implicit request =>
    Ok(":)")
  }
}