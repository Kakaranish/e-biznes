package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class OrderedProduct(id: String,
                          orderId: String,
                          productId: String,
                          quantity: Int)

object OrderedProduct {
  implicit val orderedProductFormat = Json.format[OrderedProduct]
}

class OrderedProductTable(tag: Tag) extends Table[OrderedProduct](tag, "OrderedProduct") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def orderId = column[String]("OrderId")

  def productId = column[String]("ProductId")

  def quantity = column[Int]("Quantity")

  def order_fk = foreignKey("order_fk", orderId, TableQuery[OrderTable])(_.id)

  def product_fk = foreignKey("product_fk", productId, TableQuery[ProductTable])(_.id)

  override def * = (id, orderId, productId, quantity) <> ((OrderedProduct.apply _).tupled, OrderedProduct.unapply)
}
