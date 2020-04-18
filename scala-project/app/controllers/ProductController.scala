package controllers

import daos.ProductDao
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class ProductController @Inject()(cc: ControllerComponents, productDao: ProductDao)
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def products = Action.async { implicit request =>
    val productsResult = productDao.getAll()
    productsResult.map(products => {
      if(products.isEmpty) Ok("No products found")
      else Ok(views.html.products.products(products))
    })
  }

  def product(productId: String) = Action.async { implicit request =>
    val productResult = productDao.getById(productId)
    productResult.map(product => {
      if(product == None) Ok(s"There is no product with id $productId")
      else Ok(views.html.products.product(product.get))
    })
  }

  def create = Action {
    Ok("")
  }

  def delete(productId: String) = Action {
    Ok("")
  }

  def update(productId: String) = Action {
    Ok("")
  }
}
