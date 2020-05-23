package daos.api

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Payment, TableDefinitions}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class PaymentDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def create(payment: Payment) = {
    val availableMethods = List("BLIK", "CARD", "TRANSFER")
    if (!availableMethods.contains(payment.methodCode)) null
    else {
      val id = UUID.randomUUID().toString()
      val now = new DateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
      val toAdd = Payment(id, payment.orderId, payment.methodCode, now, payment.amountOfMoney)
      val action = paymentTable += toAdd
      db.run(action).map(_ => toAdd)
    }
  }
}
