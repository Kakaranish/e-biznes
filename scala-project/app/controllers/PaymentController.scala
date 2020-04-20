package controllers

import daos.PaymentDao
import javax.inject._
import models.Payment
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class PaymentController @Inject()(cc: MessagesControllerComponents, paymentDao: PaymentDao)
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
    Ok(views.html.payments.createPayment(createForm))
  }

  def update(paymentId: String) = Action { implicit reqeust =>
    val paymentResult = Await.result(paymentDao.getById(paymentId), Duration.Inf)
    if(paymentResult == None) Ok(s"There is no payment with id $paymentId")
    else {
      val payment = paymentResult.get
      val updateFormToPass = updateForm.fill(UpdatePaymentForm(payment.id, payment.methodCode, payment.dateTime))
      Ok(views.html.payments.updatePayment(updateFormToPass))
    }
  }

  // Forms

  val createForm = Form {
    mapping(
      "methodCode" -> nonEmptyText
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updateForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "methodCode" -> nonEmptyText,
      "dateTime" -> nonEmptyText
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  //Handlers

  val createHandler = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.payments.createPayment(errorForm))
        )
      },
      createForm => {
        val dateTime = new DateTime().toString(DateTimeFormat
          .forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
        val payment = Payment(null, createForm.methodCode, dateTime)
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
        val payment = Payment(updateForm.id, updateForm.methodCode, updateForm.dateTime)
        paymentDao.update(payment).map(_ =>
          Redirect(routes.PaymentController.update(payment.id))
            .flashing("success" -> "Payment updated.")
        )
      }
    )
  }
}

case class CreatePaymentForm(methodCode: String)

case class UpdatePaymentForm(id: String,
                             methodCode: String,
                             dateTime: String)