package controllers

import daos.{CategoryDao, ProductDao}
import javax.inject._
import models.{Product, ProductPreview}
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

  def getAll() = Action.async { implicit request =>
    val productsResult = productDao.getAll()
    productsResult.map(products => {
      if (products.isEmpty) Ok("No products found")
      else Ok(views.html.products.products(products))
    })
  }

  def getAllPreviews() = Action.async { implicit request =>
    val productsResult = productDao.getAllPreviews()
    productsResult.map(products => {
      if (products.isEmpty) Ok("No products found")
      else Ok(views.html.products.productsPreviews(
        products.map(product => ProductPreview(product._1, product._2))))
    })
  }

  def getById(productId: String) = Action.async { implicit request =>
    val productResult = productDao.getPopulatedById(productId)
    productResult.map(product => {
      if (product == None) Ok(s"There is no product with id $productId")
      else Ok(views.html.products.product(product.get))
    })
  }

  def create() = Action { implicit request =>
    val availableCategories = Await.result(categoryDao.getAll(), Duration.Inf)
    Ok(views.html.products.createProduct(createForm, availableCategories))
  }

  def update(productId: String) = Action { implicit request =>
    val productResult = Await.result(productDao.getPopulatedById(productId), Duration.Inf)
    if(productResult == None) Ok(s"There is no product with id $productId to update")
    else {
      val availableCategories = Await.result(categoryDao.getAll(), Duration.Inf)
      val product = productResult.get
      val updateFormToPass = updateForm.fill(UpdateProductForm(product._1.id, product._1.name, product._1.description,
        product._1.price, product._1.quantity, product._1.categoryId))
      Ok(views.html.products.updateProduct(updateFormToPass, availableCategories))
    }
  }

  def delete(productId: String) = Action.async { implicit request =>
    val deleteResult = productDao.delete(productId)
    deleteResult.map(result => {
      if(result != 0) Ok(s"Product with id $productId has been deleted")
      else Ok(s"There is no product with id $productId")
    })
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

  val updateForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> of(floatFormat),
      "quantity" -> number,
      "categoryId" -> nonEmptyText
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
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

  val updateHandler = Action.async { implicit request =>
    val availableCategories = Await.result(categoryDao.getAll(), Duration.Inf)
    updateForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.products.updateProduct(errorForm, availableCategories))
        )
      },
      productForm => {
        val productToUpdate = Product(productForm.id, productForm.name, productForm.description,
          productForm.price, productForm.quantity, productForm.categoryId)
        productDao.update(productToUpdate).map(_ =>
          Redirect(routes.ProductController.update(productToUpdate.id))
            .flashing("success" -> "Product updated.")
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

case class UpdateProductForm(id: String,
                             name: String,
                             description: String,
                             price: Float,
                             quantity: Int,
                             categoryId: String)