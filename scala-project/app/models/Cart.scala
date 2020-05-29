package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Cart(id: String,
                userId: String,
                isFinalized: Boolean,
                updateDate: String)

object Cart {
  implicit val cartFormat = Json.format[Cart]
}

class CartTable(tag: Tag) extends Table[Cart](tag, "Cart") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def userId = column[String]("UserId")

  def isFinalized = column[Boolean]("IsFinalized")

  def updateDate = column[String]("UpdateDate")

  def userFK = foreignKey("user_fk", userId, TableQuery[UserTable])(_.id)

  override def * = (id, userId, isFinalized, updateDate) <> ((Cart.apply _).tupled, Cart.unapply)
}