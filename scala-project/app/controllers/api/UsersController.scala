package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.api.UserDaoApi
import javax.inject.{Inject, Singleton}
import models.User
import play.api.libs.json.{JsError, JsPath, JsSuccess, Json, JsonValidationError}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import play.api.libs.functional.syntax._
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserControllerApi @Inject()(cc: MessagesControllerComponents,
                                  userDao: UserDaoApi,
                                  silhouette: Silhouette[DefaultEnv])
                                 (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def getAll() = silhouette.SecuredAction.async {implicit request =>
    if(request.identity.role != "ADMIN") Future(Status(UNAUTHORIZED))
    else {
      val usersResult = userDao.getAll()
      usersResult.map(users => Ok(Json.toJson(users)))
    }
  }

  def getById(userId: String) = silhouette.SecuredAction.async {implicit request =>
    if(request.identity.role != "ADMIN") Future(Status(UNAUTHORIZED))
    else {
      userDao.getById(userId).map(user => user match {
        case Some(u) => Ok(Json.toJson(u))
        case _ => Status(NOT_FOUND)(JsError.toJson(JsError("not found")))
      })
    }
  }

  def update() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    if(request.identity.role != "ADMIN") Future(Status(UNAUTHORIZED))
    else {
      implicit val userRead = (
        (JsPath \ "id").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
          (JsPath \ "email").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
          (JsPath \ "role").read[String].filter(JsonValidationError("invalid role"))(x => x != null && !x.isEmpty && List("USER", "ADMIN").contains(x)) and
          (JsPath \ "firstName").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
          (JsPath \ "lastName").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
        ) (UpdateUserRequest.apply _)

      val validation = request.body.validate[UpdateUserRequest](userRead)
      validation match {
        case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
        case s: JsSuccess[UpdateUserRequest] => {
          val user = s.value
          userDao.getById(user.id).flatMap(oById => oById match {
            case None => Future(Status(BAD_REQUEST)("no user with such id"))
            case Some(_) => {
              userDao.getByEmail(user.email).flatMap(oByEmail => oByEmail match {
                case Some(foundUser) => {
                  if(foundUser.id != user.id) Future(Status(BAD_REQUEST)("user with such email already exists"))
                  else {
                    val toUpdate = User(user.id, user.email, user.firstName, user.lastName, user.role)
                    userDao.update(toUpdate).flatMap(updatedUser => Future(Ok(Json.toJson(updatedUser))))
                  }
                }
                case None => {
                  val toUpdate = User(user.id, user.email, user.firstName, user.lastName, user.role)
                  userDao.update(toUpdate).flatMap(updatedUser => Future(Ok(Json.toJson(updatedUser))))
                }
              })
            }
          })
        }
      }
    }
  }

  case class UpdateUserRequest(id: String, email: String, role: String, firstName: String, lastName: String)
}