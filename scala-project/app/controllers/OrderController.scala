package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class OrderController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def orders(userId: String) = Action {
    Ok("")
  }

  def order(orderId: String) = Action {
    Ok("")
  }

  def create = Action {
    Ok("")
  }

  def update(orderId: String) = Action {
    Ok("")
  }

}
