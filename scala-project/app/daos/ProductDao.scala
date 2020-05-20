package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{CategoryTable, Product, ProductPreview, ProductTable, TableDefinitions}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ProductDao @Inject()(dbConfigProvider: DatabaseConfigProvider, categoryDao: CategoryDao)
                          (implicit ec: ExecutionContext) extends TableDefinitions{
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getAll() = db.run {
    (for {
      (product, category) <- productTable joinLeft
        categoryTable on ((x, y) => x.categoryId === y.id)
    } yield (product, category))
      .result
  }

  def getAllPreviews() = db.run {
    productTable.map(record => (record.id, record.name))
      .result
  }

  def getById(productId: String) = db.run {
    productTable.filter(_.id === productId)
      .result
      .headOption
  }

  def getPopulatedById(productId: String) = db.run((for {
    (product, category) <- productTable joinLeft
      categoryTable on ((x, y) => x.categoryId === y.id)
  } yield (product, category))
    .filter(record => record._1.id === productId)
    .result
    .headOption
  )

  def create(product: Product) = db.run {
    val id = UUID.randomUUID().toString()
    productTable += Product(id, product.name, product.description, product.price,
      product.quantity, product.categoryId)
  }

  def update(productToUpdate: Product) = db.run {
    productTable.filter(record => record.id === productToUpdate.id)
      .update(productToUpdate)
  }

  def delete(productId: String) = db.run {
    productTable.filter(record => record.id === productId)
      .delete
  }
}
