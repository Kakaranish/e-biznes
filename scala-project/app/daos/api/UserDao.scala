package daos.api

import javax.inject.{Inject, Singleton}
import models.TableDefinitions
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class UserDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends TableDefinitions{
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getAll() = {
    db.run(userTable.result)
  }

  def getById(userId: String) = db.run(
    userTable.filter(record => record.id === userId)
      .result
      .headOption
  )
}
