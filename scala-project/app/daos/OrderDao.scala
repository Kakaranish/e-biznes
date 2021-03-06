package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Order, TableDefinitions}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class OrderDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                         userDao: UserDao,
                         shippingInfoDao: ShippingInfoDao)
                        (implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getAll() = db.run((for {
    ((order, user), shippingInfo) <- orderTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      shippingInfoTable on ((x, y) => x._1.shippingInfoId === y.id)
  } yield ((order, user), shippingInfo))
    .result
  )

  def getAllIds() = db.run {
    orderTable.map(record => (record.id))
      .result
  }

  def getIdsForAllWithoutShippingInfo() = db.run {
    orderTable.filter(record => record.shippingInfoId.isEmpty)
      .map(record => (record.id))
      .result
  }

  def getAllForUser(userId: String) = db.run((for {
    (order, shippingInfo) <- orderTable joinLeft
      shippingInfoTable on ((x, y) => x.shippingInfoId === y.id)
  } yield (order, shippingInfo))
    .filter(record => record._1.userId === userId)
    .result
  )

  def getById(orderId: String) = db.run((for {
    ((order, user), shippingInfo) <- orderTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      shippingInfoTable on ((x, y) => x._1.shippingInfoId === y.id)
  } yield ((order, user), shippingInfo))
    .filter(record => record._1._1.id === orderId)
    .result
    .headOption
  )

  def create(order: Order) = db.run {
    val id = UUID.randomUUID().toString()
    val nowIso = new DateTime().toString(DateTimeFormat
      .forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
    orderTable += Order(id, order.cartId, order.userId, null, nowIso)
  }

  def createWithId(order: Order) = db.run {
    val nowIso = new DateTime().toString(DateTimeFormat
      .forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
    orderTable += Order(order.id, order.cartId, order.userId, null, nowIso)
  }

  def updateShippingInfo(orderId: String, shippingInfoId: String) = db.run {
    orderTable.filter(record => record.id === orderId)
      .map(record => (record.shippingInfoId))
      .update((Option[String](shippingInfoId)))
  }
}