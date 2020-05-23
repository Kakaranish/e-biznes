package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Payment, PaymentTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class PaymentDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val paymentTable = TableQuery[PaymentTable]

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
    paymentTable += Payment(id, payment.orderId, payment.methodCode, payment.dateCreated, payment.amountOfMoney)
  }

  def update(paymentToUpdate: Payment) = db.run {
    paymentTable.filter(record => record.id === paymentToUpdate.id)
      .update(paymentToUpdate)
  }
}
