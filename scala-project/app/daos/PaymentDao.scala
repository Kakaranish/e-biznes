package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Payment, TableDefinitions}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class PaymentDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getAll() = db.run {
    paymentTable.result
  }

  def getById(paymentId: String) = db.run {
    paymentTable.filter(record => record.id === paymentId)
      .result
      .headOption
  }

  def create(payment: Payment) = db.run {
    val id = UUID.randomUUID().toString()
    val initStatus = "PENDING"
    paymentTable += Payment(id, payment.orderId, payment.methodCode, payment.dateCreated, payment.amountOfMoney, initStatus)
  }

  def update(paymentToUpdate: Payment) = db.run {
    paymentTable.filter(record => record.id === paymentToUpdate.id)
      .update(paymentToUpdate)
  }
}
