package controllers.api

import daos.UserDao
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class UserControllerApi @Inject()(cc: MessagesControllerComponents, userDao: UserDao)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val usersResult = userDao.getAll()
    usersResult.map(users => Ok(Json.toJson(users)))
  }

  def getById(userId: String) = Action.async { implicit request =>
    userDao.getById(userId).map(user => user match {
      case Some(u) => Ok(Json.toJson(u))
      case _ => Status(NOT_FOUND)(JsError.toJson(JsError("not found")))
    })
  }
}