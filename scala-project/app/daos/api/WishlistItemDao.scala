package daos.api

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{TableDefinitions, User, WishlistItem}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WishlistItemDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                productDao: ProductDaoApi)
                               (implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  def getAllWithProductsForUser(userId: String) = db.run((for {
    (wishlistItem, product) <- wishlistItemTable joinLeft
      productTable on ((x, y) => x.productId === y.id)
  } yield (wishlistItem, product))
    .filter(record => record._1.userId === userId)
    .result
  )

  def create(productId: String, userId: String) = {
    val findOtherAction = wishlistItemTable.filter(record => record.productId === productId
      && record.userId === userId).result.headOption
    db.run(findOtherAction).flatMap(other => other match {
      case Some(other) => Future(other)
      case None => {
        val id = UUID.randomUUID().toString()
        val toAdd = WishlistItem(id, userId, productId)
        db.run(wishlistItemTable += toAdd).map(_ => toAdd)
      }
    })
  }

  def deleteForUserAndProduct(productId: String, userId: String) = db.run {
    wishlistItemTable.filter(record => record.userId === userId
      && record.productId === productId)
      .delete
  }

  def isProductOnUserWishlist(productId: String, userId: String) = {
    val action = wishlistItemTable.filter(record => record.productId === productId
      && record.userId === userId).result.headOption
    db.run(action).map(result => result.isDefined)
  }
}