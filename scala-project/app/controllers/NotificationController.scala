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

  def update(notificationId: String) = Action { implicit request =>
    val notificationResult = Await.result(notificationDao.getById(notificationId), Duration.Inf)
    if(notificationResult == None) Ok(s"There is no notification with id $notificationId")
    else {
      val notification = notificationResult.get._1
      val user = notificationResult.get._2.get
      val updateFormToPass = updateForm.fill(UpdateNotificationForm(
        notificationId, notification.userId, notification.content, notification.isRead != 0))
      Ok(views.html.notifications.updateNotification(updateFormToPass, user))
    }
  }

  def delete(notificationId: String) = Action.async { implicit request =>
    val deleteResult = notificationDao.delete(notificationId)
    deleteResult.map(result => {
      if (result != 0) Ok(s"Notification with id $notificationId has been deleted")
      else Ok(s"There is no notification with id $notificationId")
    })
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
      "isRead" -> boolean
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

  val updateHandler = Action.async { implicit request =>
    updateForm.bindFromRequest().fold(
      errorForm => {
        val user = Await.result(userDao.getById(errorForm("userId").value.get), Duration.Inf).get
        Future.successful(
          BadRequest(views.html.notifications.updateNotification(errorForm, user))
        )
      },
      updateForm => {
        val notification = Notification(updateForm.id, updateForm.userId, updateForm.content, if(updateForm.isRead) 1 else 0)
        notificationDao.update(notification).map(_ =>
          Redirect(routes.NotificationController.update(notification.id))
            .flashing("success" -> "Notification updated.")
        )
      }
    )
  }
}

case class CreateNotificationForm(userId: String,
                                  content: String)

case class UpdateNotificationForm(id: String,
                                  userId: String,
                                  content: String,
                                  isRead: Boolean)