package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class WishlistItem(id: String,
                        userId: String,
                        productId: String)

object WishlistItem {
  implicit val wishlistItemFormat = Json.format[WishlistItem]
}

class WishlistItemTable(tag: Tag) extends Table[WishlistItem](tag, "WishlistItem") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def userId = column[String]("UserId")

  def productId = column[String]("ProductId")

  override def * = (id, userId, productId) <> ((WishlistItem.apply _).tupled, WishlistItem.unapply)
}