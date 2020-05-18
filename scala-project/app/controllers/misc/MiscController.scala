package controllers.misc

import java.time.Instant

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Credentials, PasswordHasherRegistry}
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, SignUpEvent, Silhouette}
import com.mohiva.play.silhouette.impl.exceptions.{IdentityNotFoundException, InvalidPasswordException}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import javax.inject.{Inject, Singleton}
import modules.LoginInfoDao
import play.api.i18n.I18nSupport
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SignUpController @Inject()(cc: MessagesControllerComponents,
                                 silhouette: Silhouette[DefaultEnv],
                                 userService: UserService,
                                 loginInfoDao: LoginInfoDao,
                                 authInfoRepository: AuthInfoRepository,
                                 passwordHasherRegistry: PasswordHasherRegistry,
                                 credentialsProvider: CredentialsProvider)
                                (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc)
    with I18nSupport {

  case class SignUpForm(email: String, firstName: String, lastName: String, password: String)

  def restricted() = silhouette.SecuredAction { implicit request =>
    Ok(":)")
  }

  def signUp() = silhouette.UnsecuredAction(parse.json).async { implicit request =>
    implicit val signUpRead = (
      (JsPath \ "email").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "firstName").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "lastName").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "password").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
      ) (SignUpForm.apply _)

    val validation = request.body.validate[SignUpForm](signUpRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[SignUpForm] => {

        val data = s.value
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)
        val authInfo = passwordHasherRegistry.current.hash(data.password)


        val appUser = AppUser(
          email = s.value.email,
          firstName = s.value.firstName,
          lastName = s.value.lastName,
        )

        for {
          user <- userService.save(appUser)
          _ <- loginInfoDao.saveUserLoginInfo(user.id, loginInfo)
          _ <- authInfoRepository.add(loginInfo, authInfo)
          authenticator <- silhouette.env.authenticatorService.create(loginInfo)
          token <- silhouette.env.authenticatorService.init(authenticator)
          result <- silhouette.env.authenticatorService.embed(token, Ok(
            Json.obj(
              "email" -> s.value.firstName,
              "token" -> token
            )))
        } yield {
          silhouette.env.eventBus.publish(SignUpEvent(user, request))
          silhouette.env.eventBus.publish(LoginEvent(user, request))
          result
        }

      }
    }
  }

  case class SignInForm(email: String, password: String)

  def signIn() = silhouette.UnsecuredAction(parse.json).async { implicit request =>
    implicit val signInRead = (
      (JsPath \ "email").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "password").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
      ) (SignInForm.apply _)

    val validation = request.body.validate[SignInForm](signInRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[SignInForm] => {
        val data = s.value
        val credentials = Credentials(data.email, data.password)

        authenticateWithCredentials(data.email, data.password).flatMap {
          case Success(user) => {
            val loginInfo = LoginInfo(CredentialsProvider.ID, user.email)
            silhouette.env.authenticatorService.create(loginInfo).flatMap { authenticator =>
              silhouette.env.eventBus.publish(LoginEvent(user, request))
              silhouette.env.authenticatorService.init(authenticator).flatMap {token =>
                silhouette.env.authenticatorService.embed(token, Ok(Json.obj(
                  "firstName" -> data.email,
                  "token" -> token
                )))
              }
            }
          }
          case InvalidPassword =>
            Future.successful(Forbidden(Json.obj("errorCode" -> "InvalidPassword")))
          case UserNotFound =>
            Future.successful(Forbidden(Json.obj("errorCode" -> "UserNotFound")))
        }
      }
    }
  }

  def authenticateWithCredentials(email: String, password: String) = {
    val credentials = Credentials(email, password)
    credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
      userService.retrieve(loginInfo).map {
        case Some(user) => Success(user)
        case None => UserNotFound
      }
    }.recoverWith {
      case _: InvalidPasswordException => Future.successful(InvalidPassword)
      case _: IdentityNotFoundException => Future.successful(UserNotFound)
      case e => Future.failed(e)
    }
  }
}

sealed trait AuthenticateResult
case class Success(user: AppUser) extends AuthenticateResult
object InvalidPassword extends AuthenticateResult
object UserNotFound extends AuthenticateResult
