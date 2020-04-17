package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Payment(id: String,
                   methodCode: String,
                   dateTime: String)

object Payment {
  implicit val paymentFormat = Json.format[Category]
}

class PaymentTable(tag: Tag) extends Table[Payment](tag, "Payment") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def methodCode = column[String]("MethodCode")

  def dateTime = column[String]("DateTime")

  override def * = (id, methodCode, dateTime) <> ((Payment.apply _).tupled, Payment.unapply)
}
