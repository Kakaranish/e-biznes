package daos

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
}
