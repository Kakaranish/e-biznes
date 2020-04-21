package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Order(id: String,
                 cartId: String,
                 userId: String,
                 shippingInfoId: String,
                 dateCreated: String)

object Order {
  implicit val orderFormat = Json.format[Order]
}

class OrderTable(tag: Tag) extends Table[Order](tag, "Order") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def cartId = column[String]("CartId")

  def userId = column[String]("UserId")

  def shippingInfoId = column[String]("ShippingInfoId")

  def dateCreated = column[String]("DateCreated")

  def cart_fk = foreignKey("cart_fk", userId, TableQuery[CartTable])(_.id)

  def user_fk = foreignKey("user_fk", userId, TableQuery[UserTable])(_.id)

  def shippingInfo_fk = foreignKey("shippingInfo_fk", shippingInfoId, TableQuery[ShippingInfoTable])(_.id)

  override def * = (id, cartId, userId, shippingInfoId, dateCreated) <> ((Order.apply _).tupled, Order.unapply)
}
