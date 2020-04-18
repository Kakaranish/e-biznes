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

  def getAllForUser(userId: String) = db.run((for {
    ((wishlistedProduct, user), product) <- wishlistedProductTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((wishlistedProduct, user), product))
    .filter(record => record._1._1.userId === userId)
    .result
  )

  def getById(wishlistedProductId: String) = db.run((for {
    ((wishlistedProduct, user), product) <- wishlistedProductTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((wishlistedProduct, user), product))
    .filter(record => record._1._1.id === wishlistedProductId)
    .result
    .headOption
  )
}