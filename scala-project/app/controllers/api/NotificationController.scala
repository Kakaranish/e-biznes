package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.api.NotificationDaoApi
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NotificationControllerApi @Inject()(cc: MessagesControllerComponents,
                                          notificationDao: NotificationDaoApi,
                                          silhouette: Silhouette[DefaultEnv])
                                         (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAllForUser() = silhouette.SecuredAction.async { implicit request =>
    notificationDao.getAllForUser(request.identity.id).flatMap(notifs => Future(Ok(Json.toJson(notifs))))
  }

  def getUnreadForUser() = silhouette.SecuredAction.async { implicit request =>
    notificationDao.getAllUnreadForUser(request.identity.id).flatMap(notifs => Future(Ok(Json.toJson(notifs))))
  }

  def makeAllReadForUser() = silhouette.SecuredAction.async { implicit request =>
    notificationDao.makeAllReadForUser(request.identity.id).flatMap(_ => Future(Ok))
  }
}
