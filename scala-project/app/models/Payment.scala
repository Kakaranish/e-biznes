package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Payment(id: String,
                   orderId: String,
                   methodCode: String,
                   dateCreated: String,
                   amountOfMoney: Float,
                   status: String)

object Payment {
  implicit val paymentFormat = Json.format[Payment]
}

class PaymentTable(tag: Tag) extends Table[Payment](tag, "Payment") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def orderId = column[String]("OrderId")

  def methodCode = column[String]("MethodCode")

  def dateCreated = column[String]("DateCreated")

  def amountOfMoney = column[Float]("AmountOfMoney")

  def status = column[String]("Status")

  def order_fk = foreignKey("order_fk", orderId, TableQuery[OrderTable])(_.id)

  override def * = ((id, orderId, methodCode, dateCreated, amountOfMoney, status)
    <> ((Payment.apply _).tupled, Payment.unapply))
}