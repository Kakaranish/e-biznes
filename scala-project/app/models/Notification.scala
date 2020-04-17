package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Notification(id: String,
                        userId: String,
                        content: String,
                        isRead: Int)

object Notification {
  implicit val notificationFormat = Json.format[Notification]
}

class NotificationTable(tag: Tag) extends Table[Notification](tag, "Notification") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def userId = column[String]("UserId")

  def content = column[String]("Content")

  def isRead = column[Int]("IsRead")

  def user_fk = foreignKey("user_fk", userId, TableQuery[UserTable])(_.id)

  override def * = (id, userId, content, isRead) <> ((Notification.apply _).tupled, Notification.unapply)
}
