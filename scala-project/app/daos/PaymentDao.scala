package daos

import javax.inject.{Inject, Singleton}
import models.PaymentTable
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
}
