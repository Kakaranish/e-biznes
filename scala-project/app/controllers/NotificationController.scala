package controllers

import javax.inject._
import models.{Notification, NotificationDao}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NotificationController @Inject()(notificationDao: NotificationDao, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def allNotifications = Action.async { implicit request =>
    val notifications = notificationDao.list()
    notifications.map(notifs => Ok(views.html.notifications(notifs)))
  }

  def notifications(userId: String) = Action {
    Ok("")
  }

  def notification(notificationId: String) = Action {
    Ok("")
  }

  def create = Action {
    Ok("")
  }

  def update(notificationId: String) = Action {
    Ok("")
  }

  def delete(notificationId: String) = Action {
    Ok("")
  }
}
