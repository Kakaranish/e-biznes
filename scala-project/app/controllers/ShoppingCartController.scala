package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class ShoppingCartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def create = Action {
    Ok("")
  }

  def productsIn(cartId: String) = Action {
    Ok("")
  }

  def update(cartId: String) = Action {
    Ok("")
  }

  def delete(cartId: String) = Action {
    Ok("")
  }

}