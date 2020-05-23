package controllers

import daos.{OrderDao, PaymentDao}
import javax.inject._
import models.Payment
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.floatFormat
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class PaymentController @Inject()(cc: MessagesControllerComponents,
                                  paymentDao: PaymentDao,
                                  orderDao: OrderDao)
                                 (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val paymentsResult = paymentDao.getAll()
    paymentsResult.map(payments => {
      if (payments.isEmpty) Ok("There are no payments found")
      else Ok(views.html.payments.payments(payments))
    })
  }

  def getById(paymentId: String) = Action.async { implicit request =>
    val paymentResult = paymentDao.getById(paymentId)
    paymentResult.map(payment => {
      if (payment == None) Ok(s"There is no payment with id $paymentId")
      else Ok(views.html.payments.payment(payment.get))
    })
  }

  def create = Action { implicit request =>
    val ordersIds = Await.result(orderDao.getAllIds(), Duration.Inf)
    if(ordersIds.isEmpty) Ok("Unable to create payment - there are no orders")
    Ok(views.html.payments.createPayment(createForm, ordersIds))
  }

  def update(paymentId: String) = Action { implicit reqeust =>
    val paymentResult = Await.result(paymentDao.getById(paymentId), Duration.Inf)
    if (paymentResult == None) Ok(s"There is no payment with id $paymentId")
    else {
      val payment = paymentResult.get
      val updateFormToPass = updateForm.fill(UpdatePaymentForm(
        payment.id, payment.orderId, payment.methodCode, payment.dateCreated, payment.amountOfMoney))
      Ok(views.html.payments.updatePayment(updateFormToPass))
    }
  }

  // Forms

  val createForm = Form {
    mapping(
      "orderId" -> nonEmptyText,
      "methodCode" -> nonEmptyText,
      "amountOfMoney" -> of(floatFormat)
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updateForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "orderId" -> nonEmptyText,
      "methodCode" -> nonEmptyText,
      "dateCreated" -> nonEmptyText,
      "amountOfMoney" -> of(floatFormat)
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  //Handlers

  val createHandler = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        val ordersIds = Await.result(orderDao.getAllIds(), Duration.Inf)
        Future.successful(
          BadRequest(views.html.payments.createPayment(errorForm, ordersIds))
        )
      },
      createForm => {
        val nowIso = new DateTime().toString(DateTimeFormat
          .forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
        val payment = Payment(null, createForm.orderId, createForm.methodCode,
         nowIso, createForm.amountOfMoney)

        paymentDao.create(payment).map(_ =>
          Redirect(routes.PaymentController.create())
            .flashing("success" -> "Payment created.")
        )
      }
    )
  }

  val updateHandler = Action.async { implicit request =>
    updateForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.payments.updatePayment(errorForm))
        )
      },
      updateForm => {
        val payment = Payment(updateForm.id, updateForm.orderId, updateForm.methodCode,
          updateForm.dateCreated, updateForm.amountOfMoney)

        paymentDao.update(payment).map(_ =>
          Redirect(routes.PaymentController.update(payment.id))
            .flashing("success" -> "Payment updated.")
        )
      }
    )
  }
}

case class CreatePaymentForm(orderId: String,
                             methodCode: String,
                             amountOfMoney: Float)

case class UpdatePaymentForm(id: String,
                             orderId: String,
                             methodCode: String,
                             dateCreated: String,
                             amountOfMoney: Float)