package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.api.{OrderDaoApi, PaymentDaoApi}
import javax.inject.{Inject, Singleton}
import models.Payment
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}
import scala.math.BigDecimal.RoundingMode

@Singleton
class PaymentControllerApi @Inject()(cc: MessagesControllerComponents,
                                     silhouette: Silhouette[DefaultEnv],
                                     paymentDao: PaymentDaoApi,
                                     orderDao: OrderDaoApi)
                                    (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def create() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val createPaymentRead = (
      (JsPath \ "orderId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "methodCode").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "amountOfMoney").read[Float].filter(JsonValidationError("cannot be empty"))(x => x > 0)
      ) (CreatePaymentRequest.apply _)

    val validation = request.body.validate[CreatePaymentRequest](createPaymentRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[CreatePaymentRequest] => {
        val data = s.value
        orderDao.getById(data.orderId).flatMap(o => o match {
          case None => Future(Status(NOT_FOUND)("no such order"))
          case Some(order) => {
            if (order.userId != request.identity.id) Future(Status(BAD_REQUEST)("given order does not belong to user"))
            else {
              val toAdd = Payment(null, data.orderId, data.methodCode, null, data.amountOfMoney)
              paymentDao.create(toAdd).flatMap(payment => Future(Ok(Json.toJson(payment))))
            }
          }
        })
      }
    }
  }

  case class CreatePaymentRequest(orderId: String, methodCode: String, amountOfMoney: Float)
}