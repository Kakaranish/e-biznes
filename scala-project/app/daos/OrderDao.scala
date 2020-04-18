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

  def getAll() = db.run((for {
    (((order, user), shippingInfo), payment) <- orderTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      shippingInfoTable on ((x, y) => x._1.shippingInfoId === y.id) joinLeft
      paymentTable on ((x, y) => x._1._1.paymentId === y.id)
  } yield (((order, user), shippingInfo), payment))
    .result
  )

  def getAllForUser(userId: String) = db.run((for {
    (((order, user), shippingInfo), payment) <- orderTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      shippingInfoTable on ((x, y) => x._1.shippingInfoId === y.id) joinLeft
      paymentTable on ((x, y) => x._1._1.paymentId === y.id)
  } yield (((order, user), shippingInfo), payment))
    .filter(record => record._1._1._1.userId === userId)
    .result
  )

  def getById(orderId: String) = db.run((for {
    (((order, user), shippingInfo), payment) <- orderTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      shippingInfoTable on ((x, y) => x._1.shippingInfoId === y.id) joinLeft
      paymentTable on ((x, y) => x._1._1.paymentId === y.id)
  } yield (((order, user), shippingInfo), payment))
    .filter(record => record._1._1._1.id === orderId)
    .result
    .headOption
  )
}
