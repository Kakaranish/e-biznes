package daos

import javax.inject.{Inject, Singleton}
import models.{OrderTable, OrderedProductTable, ProductTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class OrderedProductDao @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                  orderDao: OrderDao, productDao: ProductDao)
                                 (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val orderedProductTable = TableQuery[OrderedProductTable]
  private val orderTable = TableQuery[OrderTable]
  private val productTable = TableQuery[ProductTable]

  def getAll() = db.run((for {
    ((orderedProduct, order), product) <- orderedProductTable joinLeft
      orderTable on ((x, y) => x.orderId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((orderedProduct, order), product))
    .result
  )

  def getAllForOrder(orderId: String) = db.run((for {
    ((orderedProduct, order), product) <- orderedProductTable joinLeft
      orderTable on ((x, y) => x.orderId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((orderedProduct, order), product))
    .filter(record => record._1._1.orderId === orderId)
    .result
  )

  def getById(orderedProductId: String) = db.run((for {
    ((orderedProduct, order), product) <- orderedProductTable joinLeft
      orderTable on ((x, y) => x.orderId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((orderedProduct, order), product))
    .filter(record => record._1._1.id === orderedProductId)
    .result
    .headOption
  )
}