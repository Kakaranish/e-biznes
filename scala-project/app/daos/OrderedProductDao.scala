package daos

import javax.inject.{Inject, Singleton}
import models.{OrderTable, OrderedProductTable, ProductTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class OrderedProductDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                  orderDao: OrderDao,
                                  productDao: ProductDao)
                                 (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val orderedProductTable = TableQuery[OrderedProductTable]
  private val orderTable = TableQuery[OrderTable]
  private val productTable = TableQuery[ProductTable]

  // TODO:
  def list() = db.run((for {
    ((orderedProduct, order), product) <- orderedProductTable joinLeft orderTable joinLeft productTable
  } yield ((orderedProduct, order), product)).result)
}