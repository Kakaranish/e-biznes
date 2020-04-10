package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class PaymentController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def payments(userId: String) = Action {
    Ok("")
  }

  def payment(paymentId: String) = Action {
    Ok("")
  }

  def create = Action {
    Ok("")
  }

  def update(paymentId: String) = Action {
    Ok("")
  }

}
