package daos

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NotificationDao @Inject()(dbConfigProvider: DatabaseConfigProvider, userDao: UserDao)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val userTable = TableQuery[UserTable]
  private val notificationTable = TableQuery[NotificationTable]

  def list(): Future[Seq[(Notification, Option[User])]] = db.run((for {
    (notification, user) <- notificationTable joinLeft userTable
  } yield (notification, user)).result)
}