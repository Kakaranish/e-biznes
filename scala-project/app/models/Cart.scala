package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Cart(id: String,
                isFinalized: Boolean,
                updateDate: String)

object Cart {
  implicit val cartFormat = Json.format[Cart]
}

class CartTable(tag: Tag) extends Table[Cart](tag, "Cart") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def isFinalized = column[Boolean]("IsFinalized")

  def updateDate = column[String]("UpdateDate")

  override def * = (id, isFinalized, updateDate) <> ((Cart.apply _).tupled, Cart.unapply)
}
