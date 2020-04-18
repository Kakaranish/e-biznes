package daos

import javax.inject.{Inject, Singleton}
import models.{User, UserTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val userTable = TableQuery[UserTable]

  def getAll() = db.run(userTable.result)

  def getById(userId: String) = db.run(
    userTable.filter(record => record.id === userId)
      .result
      .headOption
  )
}
