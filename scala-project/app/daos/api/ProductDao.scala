package daos.api

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Product, TableDefinitions}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ProductDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider)
                             (implicit ec: ExecutionContext) extends TableDefinitions {
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

  def getProductPreview(productId: String, userId: String) = db.run {
    (for {
      product <- productTable.filter(_.id === productId).joinLeft(categoryTable).on((x, y) => x.categoryId === y.id).result.headOption
      wishlistItem <- if (product.isDefined) wishlistItemTable
        .filter(r => r.userId === userId && r.productId === productId).result.headOption else DBIO.successful(null)
      cart <- if(product.isDefined) cartTable
        .filter(r => r.userId === userId && r.isFinalized === false).result.headOption else DBIO.successful(null)
      cartItem <- {
        if (cart == null || !cart.isDefined) DBIO.successful(null)
        else cartItemTable.filter(r => r.cartId === cart.get.id && r.productId === productId).result.headOption
      }
    } yield (product, wishlistItem, cart, cartItem)).transactionally
  }

  def getById(productId: String) = db.run {
    productTable.filter(_.id === productId)
      .result
      .headOption
  }

  def getAllByCategoryId(categoryId: String) = db.run {
    (for {
      category <- categoryTable.filter(_.id === categoryId).result.headOption
      products <- productTable.filter(_.categoryId === categoryId).result
    } yield (category, products)).transactionally
  }

  def getPopulatedById(productId: String) = db.run((for {
    (product, category) <- productTable joinLeft
      categoryTable on ((x, y) => x.categoryId === y.id)
  } yield (product, category))
    .filter(record => record._1.id === productId)
    .result
    .headOption
  )

  def existsAnyWithCategoryId(categoryId: String) = db.run {
    productTable.filter(record => record.categoryId === categoryId).exists.result
  }

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