package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class ShippingInfo(id: String,
                        userId: String,
                        country: String,
                        city: String,
                        address: String,
                        zipOrPostcode: String)

object ShippingInfo {
  implicit val shippingInfoFormat = Json.format[ShippingInfo]
}

class ShippingInfoTable(tag: Tag) extends Table[ShippingInfo](tag, "ShippingInfo") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def userId = column[String]("UserId")

  def country = column[String]("Country")

  def city = column[String]("City")

  def address = column[String]("Address")

  def zipOrPostcode = column[String]("ZipOrPostcode")

  def user_fk = foreignKey("user_fk", userId, TableQuery[UserTable])(_.id)

  override def * = (id, userId, country, city, address, zipOrPostcode) <> ((ShippingInfo.apply _).tupled, ShippingInfo.unapply)
}