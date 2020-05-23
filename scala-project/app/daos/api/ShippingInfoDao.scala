package daos.api

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{ShippingInfo, TableDefinitions}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ShippingInfoDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                   orderDao: OrderDaoApi)
                                  (implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def create(shippingInfo: ShippingInfo, orderId: String) = {
    val id = UUID.randomUUID().toString()
    val toAdd = shippingInfo.copy(id = id)

    val action = (for {
      _ <- shippingInfoTable += toAdd
      _ <- DBIO.successful(orderDao.assignShippingInfoId(orderId, id))
    } yield ()).transactionally

    db.run(action).map(_ => toAdd)
  }

  def belongsToUser(shippingInfoId: String, userId: String) = db.run {
    shippingInfoTable.filter(_.id === shippingInfoId)
      .joinRight(orderTable).on((x, y) => x.id === y.shippingInfoId)
      .filter(_._2.userId === userId)
      .exists
      .result
  }

  def delete(shippingInfoId: String) = {
    val action = (for {
      _ <- shippingInfoTable.filter(_.id === shippingInfoId).delete
      _ <- DBIO.successful(orderDao.deassignShippingInfoId(shippingInfoId))
    } yield ()).transactionally

    db.run(action)
  }

  def update(shippingInfo: ShippingInfo) = db.run {
    shippingInfoTable.filter(_.id === shippingInfo.id)
      .update(shippingInfo)
  }
}