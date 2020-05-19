package controllers.api.auth

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.Clock
import com.mohiva.play.silhouette.api.{LoginEvent, Silhouette}
import com.mohiva.play.silhouette.impl.providers.{CommonSocialProfileBuilder, SocialProvider, SocialProviderRegistry}
import daos.LoginInfoDao
import javax.inject.Inject
import models.UserIdentity
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import services.UserService
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

class SocialController @Inject()(cc: MessagesControllerComponents,
                                 silhouette: Silhouette[DefaultEnv],
                                 configuration: Configuration,
                                 clock: Clock,
                                 userService: UserService,
                                 loginInfoDao: LoginInfoDao,
                                 authInfoRepository: AuthInfoRepository,
                                 socialProviderRegistry: SocialProviderRegistry)
                                (implicit ex: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def authenticate(provider: String) = Action.async { implicit request =>
    (socialProviderRegistry.get[SocialProvider](provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) => {
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => {
            p.retrieveProfile(authInfo).flatMap { profile =>
              loginInfoDao.getAuthenticationProviders(profile.email.get).flatMap { providers =>
                if (providers.contains(provider) || providers.isEmpty) {

                  val userToCreate = UserIdentity(email = profile.email.getOrElse(""),
                    firstName = profile.firstName.getOrElse(""),
                    lastName = profile.lastName.getOrElse(""), role = "USER")

                  for {
                    user <- userService.saveOrUpdate(userToCreate, profile.loginInfo)
                    _ <- authInfoRepository.add(profile.loginInfo, authInfo)

                    authenticator <- silhouette.env.authenticatorService.create(profile.loginInfo)
                    token <- silhouette.env.authenticatorService.init(authenticator)
                    queryParams = s"token=$token&email=${profile.email.get}&expiryDatetime=${authenticator.expirationDateTime.toString()}}"
                    result <- silhouette.env.authenticatorService.embed(
                      token, Redirect(s"http://localhost:3000/auth/successful?$queryParams"))
                  } yield {
                    silhouette.env.eventBus.publish(LoginEvent(user, request))
                    result
                  }
                } else {
                  val errorCode="XD403" // Email is bounded to other provider
                  Future.successful(Redirect(s"http://localhost:3000/auth/failure?errorCode=$errorCode"))
                }
              }
            }
          }
        }
      }
      case None => Future.successful(Status(BAD_REQUEST)(Json.obj("error" -> s"No '$provider' provider")))
    }).recover {
      case _: ProviderException => {
        val errorCode = "XD500" // Unknown error
        Redirect(s"http://localhost:3000/auth/failure?errorCode=$errorCode")
      }
    }
  }
}
