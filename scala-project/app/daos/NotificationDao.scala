package daos

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class NotificationDao @Inject()(dbConfigProvider: DatabaseConfigProvider, userDao: UserDao)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val userTable = TableQuery[UserTable]
  private val notificationTable = TableQuery[NotificationTable]

  def getAllForUser(userId: String) = db.run((for {
    (notification, user) <- notificationTable joinLeft
      userTable on ((x, y) => x.userId === y.id)
  } yield (notification, user))
    .filter(record => record._1.userId === userId)
    .result
  )

  def getById(notificationId: String) = db.run((for {
    (notification, user) <- notificationTable joinLeft
      userTable on ((x, y) => x.userId === y.id)
  } yield (notification, user))
    .filter(record => record._1.id === notificationId)
    .result
    .headOption
  )
}