package daos.api

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Product, TableDefinitions}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ProductDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider)
                             (implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getAll() = db.run {
    (for {
      (product, category) <- productTable.filter(_.isDeleted === false) joinLeft
        categoryTable on ((x, y) => x.categoryId === y.id)
    } yield (product, category))
      .result
  }

  def getById(productId: String) = db.run {
    productTable.filter(p => p.id === productId && p.isDeleted === false)
      .result
      .headOption
  }

  def getAllByCategoryId(categoryId: String) = db.run {
    (for {
      category <- categoryTable.filter(_.id === categoryId).result.headOption
      products <- productTable.filter(p => p.categoryId === categoryId && p.isDeleted === false).result
    } yield (category, products))
      .transactionally
  }

  def getWhenNotLoggedInPopulatedProduct(productId: String) = db.run {
    for {
      productInfo <- productTable.filter(p => p.id === productId && p.isDeleted === false)
        .joinLeft(categoryTable).on((x, y) => x.categoryId === y.id)
        .result
        .headOption
      opinions <- {
        if (productInfo == null || !productInfo.isDefined) DBIO.successful(List())
        else opinionTable.filter(_.productId === productId)
          .joinLeft(userTable).on((x, y) => x.userId === y.id)
          .result
      }
    } yield (productInfo, opinions)
  }

  def getWhenLoggedInPopulatedProduct(productId: String, userId: String) = db.run {
    (for {
      product <- productTable.filter(p => p.id === productId && p.isDeleted === false)
        .joinLeft(categoryTable).on((x, y) => x.categoryId === y.id)
        .result
        .headOption
      wishlistItem <- {
        if (!product.isDefined) DBIO.successful(null)
        else wishlistItemTable.filter(r => r.userId === userId && r.productId === productId)
          .result
          .headOption
      }
      cart <- {
        if (!product.isDefined) DBIO.successful(None)
        else cartTable .filter(r => r.userId === userId && r.isFinalized === false)
          .result
          .headOption
      }
      cartItem <- {
        if (!cart.isDefined) DBIO.successful(null)
        else cartItemTable.filter(r => r.cartId === cart.get.id && r.productId === productId)
          .result
          .headOption
      }
      opinions <- {
        if (!product.isDefined) DBIO.successful(List())
        else opinionTable.filter(_.productId === productId)
          .joinLeft(userTable).on((x, y) => x.userId === y.id)
          .result
      }
      boughtByUser <- {
        if (!product.isDefined) DBIO.successful(false)
        else cartItemTable.filter(r => r.productId === productId)
          .joinLeft(cartTable).on((x, y) => x.cartId === y.id)
          .filter(c => c._2.map(_.userId) === userId && c._2.map(_.isFinalized) === true)
          .exists
          .result
      }
    } yield (product, wishlistItem, cart, cartItem, opinions, boughtByUser))
      .transactionally
  }

  def subtractAmt(productId: String, toSubtract: Int) = {
    (for {
      product <- productTable.filter(_.id === productId).result.headOption
      result <- {
        if (!product.isDefined) DBIO.failed(null)
        else if (product.get.quantity - toSubtract < 0) DBIO.failed(null)
        else productTable.filter(_.id === productId)
          .map(r => r.quantity)
          .update(product.get.quantity - toSubtract)
          .map(_ => Some(product.get.copy(quantity = product.get.quantity - toSubtract)))
      }
    } yield result).transactionally
  }

  def create(product: Product) = db.run {
    val id = UUID.randomUUID().toString()
    productTable += Product(id, product.name, product.description, product.price,
      product.quantity, product.categoryId, false)
  }

  def update(productToUpdate: Product) = db.run {
    productTable.filter(record => record.id === productToUpdate.id)
      .update(productToUpdate)
  }

  def delete(productId: String) = db.run {
    productTable.filter(record => record.id === productId)
      .map(r => (r.isDeleted))
      .update(true)
  }
}