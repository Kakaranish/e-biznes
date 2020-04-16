package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NotificationDao @Inject()(dbConfigProvider: DatabaseConfigProvider, userDao: UserDao)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  import userDao.UserTable

  private class NotificationTable(tag: Tag) extends Table[Notification](tag, "Notification") {
    def id = column[String]("Id", O.PrimaryKey, O.Unique)

    def userId = column[String]("UserId")

    def content = column[String]("Content")

    def isRead = column[Int]("IsRead")

    def user_fk = foreignKey("user_fk", userId, userTable)(_.id)

    override def * = (id, userId, content, isRead) <> ((Notification.apply _).tupled, Notification.unapply)
  }

  private val userTable = TableQuery[UserTable]
  private val notificationTable = TableQuery[NotificationTable]

  def list(): Future[Seq[(Notification, Option[User])]] = db.run((for {
    (notification, user) <- notificationTable joinLeft userTable
  } yield (notification, user)).result)
}