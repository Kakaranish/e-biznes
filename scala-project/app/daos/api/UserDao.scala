package daos.api

import javax.inject.{Inject, Singleton}
import models.{TableDefinitions, User}
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

  def getByEmail(email: String) = db.run {
    userTable.filter(r => r.email === email)
      .result
      .headOption
  }

  def update(user: User) = {
    val action = userTable.filter(_.id === user.id)
      .map(r => (r.firstName, r.lastName, r.role))
      .update((user.firstName, user.lastName, user.role))
    db.run(action).map(_ => user)
  }
}
