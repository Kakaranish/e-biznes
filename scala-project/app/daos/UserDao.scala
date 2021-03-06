package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{TableDefinitions, User, UserTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
extends TableDefinitions{
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

  def getAll() = db.run(userTable.result)

  def getById(userId: String) = db.run(
    userTable.filter(record => record.id === userId)
      .result
      .headOption
  )

  def getAllPreviews() = db.run {
    userTable.map(record => (record.id, record.email))
      .result
  }

  def create(user: User) = db.run {
    val id = UUID.randomUUID().toString()
    userTable += User(id, user.email, user.firstName, user.lastName, user.role)
  }

  def update(userToUpdate: User) = db.run {
    userTable.filter(record => record.id === userToUpdate.id)
      .map(record => (record.firstName, record.lastName))
      .update((userToUpdate.firstName, userToUpdate.lastName))
  }

  def delete(userId: String) = db.run {
    userTable.filter(record => record.id === userId)
      .delete
  }
}
