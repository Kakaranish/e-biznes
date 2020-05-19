package controllers.api.auth

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, SignUpEvent, Silhouette}
import com.mohiva.play.silhouette.impl.providers.{CommonSocialProfileBuilder, CredentialsProvider, SocialProvider, SocialProviderRegistry}
import daos.LoginInfoDao
import javax.inject.{Inject, Singleton}
import models.AppUser
import play.api.i18n.I18nSupport
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import services.UserService
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SignUpController @Inject()(cc: MessagesControllerComponents,
                                 silhouette: Silhouette[DefaultEnv],
                                 userService: UserService,
                                 loginInfoDao: LoginInfoDao,
                                 authInfoRepository: AuthInfoRepository,
                                 passwordHasherRegistry: PasswordHasherRegistry,
                                 socialProviderRegistry: SocialProviderRegistry)
                                (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc)
    with I18nSupport {

  def submit() = silhouette.UnsecuredAction(parse.json).async { implicit request =>
    implicit val signUpRead = (
      (JsPath \ "email").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "firstName").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "lastName").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "password").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
      ) (SignUpRequest.apply _)

    val validation = request.body.validate[SignUpRequest](signUpRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[SignUpRequest] => {
        val data = s.value
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)

        userService.retrieve(loginInfo).flatMap {
          case Some(user) => Future(Status(CONFLICT)(s"email '${user.email}' is already in use"))
          case None => {
            val userToCreate = AppUser(email = s.value.email, firstName = s.value.firstName,
              lastName = s.value.lastName, role = "USER")
            for {
              user <- userService.save(userToCreate)
              _ <- loginInfoDao.saveUserLoginInfo(user.id, loginInfo)
              authInfo = passwordHasherRegistry.current.hash(data.password)
              _ <- authInfoRepository.add(loginInfo, authInfo)
              authenticator <- silhouette.env.authenticatorService.create(loginInfo)
              token <- silhouette.env.authenticatorService.init(authenticator)
              result <- silhouette.env.authenticatorService.embed(
                token, Ok(Json.obj(
                  "token" -> token,
                  "email" -> data.email,
                  "expiryDatetime" -> authenticator.expirationDateTime.toString()
                )))
            } yield {
              silhouette.env.eventBus.publish(SignUpEvent(user, request))
              silhouette.env.eventBus.publish(LoginEvent(user, request))
              result
            }
          }
        }
      }
    }
  }

  case class SignUpRequest(email: String, firstName: String, lastName: String, password: String)

}