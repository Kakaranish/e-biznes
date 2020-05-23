package daos.api

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Order, TableDefinitions}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class OrderDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider)
                           (implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def create(order: Order) = {
    val id = UUID.randomUUID().toString()
    val nowIso = new DateTime().toString(DateTimeFormat
      .forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))

    val toAdd = Order(id, order.cartId, order.userId, order.shippingInfoId, nowIso)
    db.run(orderTable += toAdd).map(_ => toAdd)
  }
  
  def getById(orderId: String) = db.run {
    (for {
      orderInfo <- orderTable.filter(_.id === orderId)
        .joinLeft(cartTable).on((x, y) => x.cartId === y.id)
        .joinLeft(shippingInfoTable).on((x, y) => x._1.shippingInfoId === y.id)
        .result
        .headOption
      cartItems <-
        if (orderInfo.isDefined) cartItemTable.filter(_.cartId === orderInfo.get._1._2.get.id)
          .joinLeft(productTable).on((x, y) => x.productId === y.id)
          .result
        else DBIO.successful(null)
    } yield {
      if (!orderInfo.isDefined) null
      else (orderInfo.get._1._1, orderInfo.get._1._2.get, orderInfo.get._2, cartItems)
    }).transactionally
  }
}
