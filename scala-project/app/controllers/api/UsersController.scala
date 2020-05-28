package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.api.UserDaoApi
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
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
}