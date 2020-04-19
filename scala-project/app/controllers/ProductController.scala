package controllers

import daos.{CategoryDao, ProductDao}
import javax.inject._
import models.{Category, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class ProductController @Inject()(cc: MessagesControllerComponents,
                                  productDao: ProductDao,
                                  categoryDao: CategoryDao)
                                 (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def products = Action.async { implicit request =>
    val productsResult = productDao.getAll()
    productsResult.map(products => {
      if (products.isEmpty) Ok("No products found")
      else Ok(views.html.products.products(products))
    })
  }

  def product(productId: String) = Action.async { implicit request =>
    val productResult = productDao.getById(productId)
    productResult.map(product => {
      if (product == None) Ok(s"There is no product with id $productId")
      else Ok(views.html.products.product(product.get))
    })
  }

  def create = Action { implicit request =>
    val availableCategories = Await.result(categoryDao.getAll(), Duration.Inf)
    Ok(views.html.products.createProduct(createForm, availableCategories))
  }

  def delete(productId: String) = Action {
    Ok("")
  }

  def update(productId: String) = Action {
    Ok("")
  }

  // Forms

  val createForm = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> of(floatFormat),
      "quantity" -> number,
      "categoryId" -> nonEmptyText
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  // Handlers

  val createHandler = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        val availableCategories = Await.result(categoryDao.getAll(), Duration.Inf)
        Future.successful(
          BadRequest(views.html.products.createProduct(errorForm, availableCategories))
        )
      },
      productForm => {
        val product = Product(null, productForm.name, productForm.description,
          productForm.price, productForm.quantity, productForm.categoryId)
        productDao.create(product).map(_ =>
          Redirect(routes.ProductController.create())
            .flashing("success" -> "Product created.")
        )
      }
    )
  }
}

case class CreateProductForm(name: String,
                             description: String,
                             price: Float,
                             quantity: Int,
                             categoryId: String)