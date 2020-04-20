package daos

import java.util.UUID
import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ShippingInfoDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val shippingInfoTable = TableQuery[ShippingInfoTable]

  def getAll() = db.run {
    shippingInfoTable.result
  }

  def getById(shippingInfoId: String) = db.run {
    shippingInfoTable.filter(record => record.id === shippingInfoId)
      .result
      .headOption
  }

  def create(shippingInfo: ShippingInfo) = db.run {
    val id = UUID.randomUUID().toString()
    shippingInfoTable += ShippingInfo(id, shippingInfo.country,
      shippingInfo.city, shippingInfo.address, shippingInfo.zipOrPostcode)
  }

  def update(shippingInfoToUpdate: ShippingInfo) = db.run {
    shippingInfoTable.filter(record => record.id === shippingInfoToUpdate.id)
      .update(shippingInfoToUpdate)
  }

  def delete(shippingInfoId: String) = db.run {
    shippingInfoTable.filter(record => record.id === shippingInfoId)
      .delete
  }
}
