package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class CartItem(id: String,
                    cartId: String,
                    productId: String,
                    quantity: Int,
                    pricePerProduct: Float)

object CartItem {
  implicit val cartItemFormat = Json.format[CartItem]
}

class CartItemTable(tag: Tag) extends Table[CartItem](tag, "CartItem") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def cartId = column[String]("CartId")

  def productId = column[String]("ProductId")

  def quantity = column[Int]("Quantity")

  def pricePerProduct = column[Float]("PricePerProduct")

  def cart_fk = foreignKey("cart_fk", cartId, TableQuery[CartTable])(_.id)

  def product_fk = foreignKey("product_fk", productId, TableQuery[ProductTable])(_.id)

  override def * = (id, cartId, productId, quantity, pricePerProduct) <> ((CartItem.apply _).tupled, CartItem.unapply)
}