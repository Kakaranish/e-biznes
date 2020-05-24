package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class NotificationDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                userDao: UserDao)
                               (implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

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

  def create(notification: Notification) = db.run {
    val id = UUID.randomUUID().toString()
    notificationTable += Notification(id, notification.userId, notification.content, notification.isRead)
  }

  def update(notificationToUpdate: Notification) = db.run {
    notificationTable.filter(record => record.id === notificationToUpdate.id)
      .update(notificationToUpdate)
  }

  def delete(notificationId: String) = db.run {
    notificationTable.filter(record => record.id === notificationId)
      .delete
  }
}