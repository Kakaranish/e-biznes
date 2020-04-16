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

  class UserTable(tag: Tag) extends Table[User](tag, "User") {
    def id = column[String]("Id", O.PrimaryKey, O.Unique)

    def email = column[String]("Email", O.Unique)

    def password = column[String]("Password")

    def firstName = column[String]("FirstName")

    def lastName = column[String]("LastName")

    def * = (id, email, password, firstName, lastName) <> ((User.apply _).tupled, User.unapply)
  }

  private val users = TableQuery[UserTable]

  def list(): Future[Seq[User]] = db.run(users.result)

  def createUser(email: String, password: String, firstName: String, lastName: String) = db.run {
    val id = UUID.randomUUID().toString()
    users += User(id, email, password, firstName, lastName)
  }
}
