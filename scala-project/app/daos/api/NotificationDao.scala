package daos.api

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Notification, TableDefinitions}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class NotificationDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider)
                                  (implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def create(notification: Notification) = {
    val id = UUID.randomUUID().toString()
    val toAdd = notification.copy(id = id)
    db.run(notificationTable += toAdd).map(_ => toAdd)
  }

  def getAllForUser(userId: String) = db.run {
    notificationTable.filter(_.userId === userId)
      .result
  }

  def getAllUnreadForUser(userId: String) = db.run {
    notificationTable.filter(n => n.userId === userId && n.isRead === 0)
      .result
  }

  def makeAllReadForUser(userId: String) = db.run {
    notificationTable.filter(_.userId === userId)
      .map(n => n.isRead)
      .update(1)
  }
}