package controllers

import daos.OrderedProductDao
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class OrderedProductController @Inject()(cc: ControllerComponents, orderedProductDao: OrderedProductDao)
                                        (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val orderedProductsResult = orderedProductDao.getAll()
    orderedProductsResult.map(orderedProducts => {
      if (orderedProducts.isEmpty) Ok("There are no ordered products found")
      else Ok(views.html.orderedProducts.orderedProducts(orderedProducts))
    })
  }

  def getAllForOrder(orderId: String) = Action.async { implicit request =>
    val orderedProductsResult = orderedProductDao.getAllForOrder(orderId)
    orderedProductsResult.map(orderedProducts => {
      if (orderedProducts.isEmpty) Ok(s"There are no ordered products for order with id $orderId")
      else Ok(views.html.orderedProducts.orderedProductsForOrder(orderedProducts))
    })
  }

  def getById(orderedProductId: String) = Action.async { implicit request =>
    val orderedProductResult = orderedProductDao.getById(orderedProductId)
    orderedProductResult.map(orderedProduct => {
      if (orderedProduct == None) Ok(s"There are no ordered products with id $orderedProduct")
      else Ok(views.html.orderedProducts.orderedProduct(orderedProduct.get))
    })
  }

  def create = Action {
    Ok("")
  }

  def update(orderId: String) = Action {
    Ok("")
  }

  def delete(orderId: String) = Action {
    Ok("")
  }
}