package controllers

import daos.{NotificationDao, UserDao}
import javax.inject._
import models.Notification
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class NotificationController @Inject()(cc: MessagesControllerComponents,
                                       notificationDao: NotificationDao,
                                       userDao: UserDao)
                                      (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def notifications(userId: String) = Action.async { implicit request =>
    val notificationsResult = notificationDao.getAllForUser(userId)
    notificationsResult.map(notifications => {
      if (notifications.isEmpty) Ok(s"There are no notificationsForUser for user with id $userId")
      else Ok(views.html.notifications.notificationsForUser(notifications))
    })
  }

  def notification(notificationId: String) = Action.async { implicit result =>
    val notificationResult = notificationDao.getById(notificationId)
    notificationResult.map(notification => {
      if (notification == None) Ok(s"There is no notification with id $notificationId")
      else Ok(views.html.notifications.notification(notification.get))
    })
  }

  def create = Action { implicit request =>
    val availableUsers = Await.result(userDao.getAll(), Duration.Inf)
    if(availableUsers.isEmpty) Ok("Cannot add notification - there are no users")
    else Ok(views.html.notifications.createNotification(createForm, availableUsers))
  }

  def update(notificationId: String) = Action {
    Ok("")
  }

  def delete(notificationId: String) = Action {
    Ok("")
  }

  // Forms
  val createForm = Form {
    mapping(
      "userId" -> nonEmptyText,
      "content" -> nonEmptyText
    )(CreateNotificationForm.apply)(CreateNotificationForm.unapply)
  }

  val updateForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "userId" -> nonEmptyText,
      "content" -> nonEmptyText,
      "isRead" -> number
    )(UpdateNotificationForm.apply)(UpdateNotificationForm.unapply)
  }

  // Handlers

  val createHandler = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        val availableUsers = Await.result(userDao.getAll(), Duration.Inf)
        Future.successful(
          BadRequest(views.html.notifications.createNotification(errorForm, availableUsers))
        )
      },
      createForm => {
        val notification = Notification(null, createForm.userId, createForm.content, 0)
        notificationDao.create(notification).map(_ =>
          Redirect(routes.NotificationController.create())
            .flashing("success" -> "Notification created.")
        )
      }
    )
  }

//  val updateHandler = Action.async { implicit request =>
//    updateForm.bindFromRequest().fold(
//      errorForm => {
//        Future.successful(
//          BadRequest(views.html.index)
//        )
//      },
//      updateForm => {
//        // Placeholder for logic
//        Future.successful(
//          BadRequest(views.html.index)
//        )
//      }
//    )
//  }
}

case class CreateNotificationForm(userId: String,
                                  content: String)

case class UpdateNotificationForm(id: String,
                                  userId: String,
                                  content: String,
                                  isRead: Int)