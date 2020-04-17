package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class WishlistedProduct(id: String,
                             userId: String,
                             productId: String)

object WishlistedProduct {
  implicit val wishlistedProductFormat = Json.format[WishlistedProduct]
}

class WishlistedProductTable(tag: Tag) extends Table[WishlistedProduct](tag, "WishlistedProduct") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def userId = column[String]("UserId")

  def productId = column[String]("ProductId")

  override def * = (id, userId, productId) <> ((WishlistedProduct.apply _).tupled, WishlistedProduct.unapply)
}