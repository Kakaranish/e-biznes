package daos

import javax.inject.{Inject, Singleton}
import models.{ProductTable, UserTable, WishlistedProductTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class WishlistedProductDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                  userDao: UserDao,
                                  productDao: ProductDao)
                                 (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val wishlistedProductTable = TableQuery[WishlistedProductTable]
  private val userTable = TableQuery[UserTable]
  private val productTable = TableQuery[ProductTable]

  // Temp:
  def list() = db.run {
    wishlistedProductTable.result
  }
}