package models

import play.api.libs.json.Json

case class Notification(id: String, userId: String, content: String, isRead: Int)

object Notification {
  implicit val notificationFormat = Json.format[Notification]
}
