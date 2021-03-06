package daos.api

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class CartItemDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider,
                               productDao: ProductDaoApi)
                              (implicit ec: ExecutionContext) extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getAllForCart(cartId: String) = db.run {
    cartItemTable.filter(_.cartId === cartId)
      .result
  }

  def getAllWithProductsForCart(cartId: String) = db.run((for {
    (cartItem, product) <- cartItemTable joinLeft
      productTable on ((x, y) => x.productId === y.id)
  } yield (cartItem, product))
    .filter(record => record._1.cartId === cartId)
    .result
  )

  def getCartOfCartItem(cartItemId: String) = {
    val action = for {
      cartItem <- cartItemTable.filter(_.id === cartItemId)
      result <- cartTable.filter(_.id === cartItem.id)
    } yield result

    db.run(action.result.headOption)
  }

  def getPopulatedWithProductById(cartItemId: String) = db.run((for {
    (cartItem, product) <- cartItemTable joinLeft
      productTable on ((x, y) => x.productId === y.id)
  } yield (cartItem, product))
    .filter(record => record._1.id === cartItemId)
    .result
    .headOption
  )

  def update(cartItem: CartItem) = db.run {
    cartItemTable.filter(_.id === cartItem.id)
      .map(record => (record.quantity, record.pricePerProduct))
      .update((cartItem.quantity, cartItem.pricePerProduct))
  }

  def belongsToUser(cartItemId: String, userId: String) = {
    val action = (for {
      (cartItem, cart) <- cartItemTable joinLeft
        cartTable on ((x, y) => x.cartId === y.id)
    } yield (cartItem, cart))
      .filter(_._1.id === cartItemId)
      .result
      .headOption

    db.run(action).map { c =>
      c match {
        case Some(cartPair) => cartPair._2.get.userId == userId
        case None => false
      }
    }
  }

  def create(cartItem: CartItem) = {
    val id = UUID.randomUUID().toString()
    val toAdd = cartItem.copy(id = id)
    db.run(cartItemTable += toAdd).map(_ => toAdd)
  }

  def delete(cartItemId: String) = db.run {
    cartItemTable.filter(record => record.id === cartItemId)
      .delete
  }
}