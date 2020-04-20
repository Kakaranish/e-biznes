package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{ProductTable, UserTable, WishlistItem, WishlistItemTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class WishlistItemDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                userDao: UserDao,
                                productDao: ProductDao)
                               (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val wishlistItemTable = TableQuery[WishlistItemTable]
  private val userTable = TableQuery[UserTable]
  private val productTable = TableQuery[ProductTable]

  def getAllForUser(userId: String) = db.run((for {
    ((wishlistItem, user), product) <- wishlistItemTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((wishlistItem, user), product))
    .filter(record => record._1._1.userId === userId)
    .result
  )

  def getById(wishlistItemId: String) = db.run((for {
    ((wishlistItem, user), product) <- wishlistItemTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((wishlistItem, user), product))
    .filter(record => record._1._1.id === wishlistItemId)
    .result
    .headOption
  )

  def create(wishlistItem: WishlistItem) = db.run {
    val id = UUID.randomUUID().toString()
    wishlistItemTable += WishlistItem(id, wishlistItem.userId,
      wishlistItem.productId)
  }

  def delete(wishlistItemId: String) = db.run {
    wishlistItemTable.filter(record => record.id === wishlistItemId)
      .delete
  }
}