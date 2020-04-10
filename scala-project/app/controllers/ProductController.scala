package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def products = Action {
    Ok("")
  }

  def product(productId: String) = Action {
    Ok("")
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
