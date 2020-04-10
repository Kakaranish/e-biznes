package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class NotificationController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

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
