package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.api.{NotificationDaoApi, UserDaoApi}
import javax.inject.{Inject, Singleton}
import models.Notification
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NotificationControllerApi @Inject()(cc: MessagesControllerComponents,
                                          notificationDao: NotificationDaoApi,
                                          userDao: UserDaoApi,
                                          silhouette: Silhouette[DefaultEnv])
                                         (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val emptyStringMsg = "cannot be empty"

  def create() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    if (request.identity.role != "ADMIN") Future(Status(UNAUTHORIZED))
    else {
      implicit val createNotificationRead = (
        (JsPath \ "userId").read[String].filter(JsonValidationError(emptyStringMsg))(x => x != null && !x.isEmpty) and
          (JsPath \ "content").read[String].filter(JsonValidationError(emptyStringMsg))(x => x != null && !x.isEmpty)
        ) (CreateNotificationRequest.apply _)

      val validation = request.body.validate[CreateNotificationRequest](createNotificationRead)
      validation match {
        case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
        case s: JsSuccess[CreateNotificationRequest] => {
          userDao.getById(s.value.userId).flatMap(u => u match {
            case None => Future(Status(NOT_FOUND)("no user with such id"))
            case Some(_) => {
              val notification = Notification(null, s.value.userId, s.value.content, 0)
              notificationDao.create(notification).flatMap(_ => Future(Ok))
            }
          })
        }
      }
    }
  }

  def getAllForUser() = silhouette.SecuredAction.async { implicit request =>
    notificationDao.getAllForUser(request.identity.id).flatMap(notifs => Future(Ok(Json.toJson(notifs))))
  }

  def getUnreadForUser() = silhouette.SecuredAction.async { implicit request =>
    notificationDao.getAllUnreadForUser(request.identity.id).flatMap(notifs => Future(Ok(Json.toJson(notifs))))
  }

  def makeAllReadForUser() = silhouette.SecuredAction.async { implicit request =>
    notificationDao.makeAllReadForUser(request.identity.id).flatMap(_ => Future(Ok))
  }

  case class CreateNotificationRequest(userId: String, content: String)
}
