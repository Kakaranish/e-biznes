package daos

import javax.inject.{Inject, Singleton}
import models.{OrderTable, PaymentTable, ShippingInfoTable, UserTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class OrderDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                         userDao: UserDao,
                         shippingInfoDao: ShippingInfoDao,
                         paymentDao: PaymentDao)
                        (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val orderTable = TableQuery[OrderTable]
  private val userTable = TableQuery[UserTable]
  private val shippingInfoTable = TableQuery[ShippingInfoTable]
  private val paymentTable = TableQuery[PaymentTable]

  def list() = db.run((for {
    (product, category) <-
      orderTable joinLeft userTable joinLeft shippingInfoTable joinLeft paymentTable
  } yield (product, category)).result)
}
