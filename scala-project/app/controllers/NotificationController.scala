package controllers

import daos.NotificationDao
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class NotificationController @Inject()(notificationDao: NotificationDao, cc: ControllerComponents)
                                      (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def notifications(userId: String) = Action.async { implicit request =>
    val notificationsResult = notificationDao.getAllForUser(userId)
    notificationsResult.map(notifications => {
      if(notifications.isEmpty) Ok(s"There are no notificationsForUser for user with id $userId")
      else Ok(views.html.notifications.notificationsForUser(notifications))
    })
  }

  def notification(notificationId: String) = Action.async { implicit result =>
    val notificationResult = notificationDao.getById(notificationId)
    notificationResult.map(notification => {
      if(notification == None) Ok(s"There is no notification with id $notificationId")
      else Ok(views.html.notifications.notification(notification.get))
    })
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
