package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class CartItemDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                            orderDao: OrderDao,
                            productDao: ProductDao)
                           (implicit ec: ExecutionContext) extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getAllWithProductsForCart(cartId: String) = db.run((for {
    (cartItem, product) <- cartItemTable joinLeft
      productTable on ((x, y) => x.productId === y.id)
  } yield (cartItem, product))
    .filter(record => record._1.cartId === cartId)
    .result
  )

  def getPopulatedById(cartItemId: String) = db.run((for {
    ((cartItem, order), product) <- cartItemTable joinLeft
      orderTable on ((x, y) => x.cartId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((cartItem, order), product))
    .filter(record => record._1._1.id === cartItemId)
    .result
    .headOption
  )

  def create(cartItem: CartItem) = db.run {
    val id = UUID.randomUUID().toString()
    val toAdd = CartItem(id, cartItem.cartId, cartItem.productId, cartItem.quantity)
    cartItemTable += toAdd
  }

  def delete(cartItemId: String) = db.run {
    cartItemTable.filter(record => record.id === cartItemId)
      .delete
  }
}