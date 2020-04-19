package daos

import java.util.UUID
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

  def create(user: User) = db.run {
    val id = UUID.randomUUID().toString()
    // TODO: In the future add password encryption here
    userTable += User(id, user.email, user.password, user.firstName, user.lastName)
  }

  def update(userToUpdate: User) = db.run {
    userTable.filter(record => record.id === userToUpdate.id)
      .update(userToUpdate)
  }

  def delete(userId: String) = db.run {
    userTable.filter(record => record.id === userId)
      .delete
  }
}
