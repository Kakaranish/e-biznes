package models

import java.util.UUID

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val users = TableQuery[UserTable]

  def list(): Future[Seq[User]] = db.run(users.result)

  def createUser(email: String, password: String, firstName: String, lastName: String) = db.run {
    val id = UUID.randomUUID().toString()
    users += User(id, email, password, firstName, lastName)
  }
}
