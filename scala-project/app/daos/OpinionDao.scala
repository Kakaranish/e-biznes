package daos

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class OpinionDao @Inject()(dbConfigProvider: DatabaseConfigProvider, userDao: UserDao, productDao: ProductDao)
                          (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val opinionTable = TableQuery[OpinionTable]
  private val userTable = TableQuery[UserTable]
  private val productTable = TableQuery[ProductTable]
}