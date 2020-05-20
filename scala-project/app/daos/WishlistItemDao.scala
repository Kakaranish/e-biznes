package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{TableDefinitions, User, WishlistItem}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WishlistItemDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                userDao: UserDao,
                                productDao: ProductDao)
                               (implicit ec: ExecutionContext) extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getAllPopulatedForUser(userId: String) = db.run((for {
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

  def create(wishlistItem: WishlistItem) = {
    val findOtherAction = wishlistItemTable.filter(record => record.productId === wishlistItem.productId
      && record.userId === wishlistItem.userId).result.headOption
    db.run(findOtherAction).map(other => other match {
      case Some(other) => other
      case None => {
        val id = UUID.randomUUID().toString()
        val toAdd = WishlistItem(id, wishlistItem.userId, wishlistItem.productId)
        db.run(wishlistItemTable += toAdd).map(_ => toAdd)
      }
    })
  }
  
  def delete(wishlistItemId: String) = db.run {
    wishlistItemTable.filter(record => record.id === wishlistItemId)
      .delete
  }
}