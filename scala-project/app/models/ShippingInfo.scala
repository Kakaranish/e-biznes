package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class ShippingInfo(id: String,
                        country: String,
                        city: String,
                        address: String,
                        zipOrPostcode: String)

object ShippingInfo {
  implicit val shippingInfoFormat = Json.format[ShippingInfo]
}

class ShippingInfoTable(tag: Tag) extends Table[ShippingInfo](tag, "ShippingInfo") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def country = column[String]("Country")

  def city = column[String]("City")

  def address = column[String]("Address")

  def zipOrPostcode = column[String]("ZipOrPostcode")

  override def * = (id, country, city, address, zipOrPostcode) <> ((ShippingInfo.apply _).tupled, ShippingInfo.unapply)
}