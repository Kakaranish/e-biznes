package controllers

import daos.CartItemDao
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class CartItemController @Inject()(cc: ControllerComponents,
                                   orderedProductDao: CartItemDao)
                                  (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def getAll() = Action { implicit request =>
    Ok("")
  }

  def getAllForOrder(orderId: String) = Action { implicit request =>
    Ok("")
  }

  def getById(orderedProductId: String) = Action { implicit request =>
    Ok("")
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