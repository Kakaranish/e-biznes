package controllers.misc

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, SignUpEvent, Silhouette}
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

        //        userService.save(appUser).map {user => Ok(user.email)}
        //        Future(Ok("XD"))
        for {
          user <- userService.save(appUser)
          _ <- loginInfoDao.saveUserLoginInfo(user.id, loginInfo)
//          authInfo <- authInfoRepository.add(loginInfo, authInfo)
          authenticator <- silhouette.env.authenticatorService.create(loginInfo)
          token <- silhouette.env.authenticatorService.init(authenticator)
          result <- silhouette.env.authenticatorService.embed(token, Ok(
            Json.obj(
              "firstName" -> s.value.firstName,
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

  def signIn() = silhouette.UnsecuredAction(parse.json).async { implicit request =>
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
        val providerId = CredentialsProvider.ID
        val providerKey = s.value.email

        val appUser = AppUser(
          email = s.value.email,
          firstName = s.value.firstName,
          lastName = s.value.lastName
        )

        val loginInfo = LoginInfo(providerId, providerKey)
        //        credentialsProvider.authenticate(Credentials("login", "password")).map()
        for {
          authenticator <- silhouette.env.authenticatorService.create(loginInfo)
          token <- silhouette.env.authenticatorService.init(authenticator)
          result <- silhouette.env.authenticatorService.embed(token, Ok(
            Json.obj(
              "firstName" -> s.value.firstName,
              "token" -> token
            )))
        } yield {
          silhouette.env.eventBus.publish(SignUpEvent(appUser, request))
          silhouette.env.eventBus.publish(LoginEvent(appUser, request))
          result
        }

      }
    }

  }
}