package controllers.api

import daos.UserDao
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class UserControllerApi @Inject()(cc: MessagesControllerComponents, userDao: UserDao)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val usersResult = userDao.getAll()
    usersResult.map(users => Ok(Json.toJson(users)))
  }
}