package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartItemDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                            orderDao: OrderDao,
                            productDao: ProductDao)
                           (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val cartItemTable = TableQuery[CartItemTable]
  private val orderTable = TableQuery[OrderTable]
  private val productTable = TableQuery[ProductTable]

  def getAll() = db.run((for {
    ((cartItem, order), product) <- cartItemTable joinLeft
      orderTable on ((x, y) => x.cartId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((cartItem, order), product))
    .result
  )

  def getAllForCart(cartId: String): Future[Seq[((CartItem, Option[Order]), Option[Product])]] = db.run((for {
    ((cartItem, order), product) <- cartItemTable joinLeft
      orderTable on ((x, y) => x.cartId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((cartItem, order), product))
    .filter(record => record._1._1.cartId === cartId)
    .result
  )

  def getById(cartItemId: String) = db.run((for {
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
    cartItemTable += CartItem(id, cartItem.cartId, cartItem.productId, cartItem.quantity)
  }

  def delete(cartItemId: String) = db.run {
    cartItemTable.filter(record => record.id === cartItemId)
      .delete
  }
}