package controllers

import daos.PaymentDao
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class PaymentController @Inject()(cc: ControllerComponents, paymentDao: PaymentDao)
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val paymentsResult = paymentDao.getAll()
    paymentsResult.map(payments => {
      if(payments.isEmpty) Ok("There are no payments found")
      else Ok(views.html.payments.payments(payments))
    })
  }

  def getById(paymentId: String) = Action.async { implicit request =>
    val paymentResult = paymentDao.getById(paymentId)
    paymentResult.map(payment => {
      if(payment == None) Ok(s"There is no payment with id $paymentId")
      else Ok(views.html.payments.payment(payment.get))
    })
  }

  def create = Action {
    Ok("")
  }

  def update(paymentId: String) = Action {
    Ok("")
  }
}
