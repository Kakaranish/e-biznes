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

  val emptyStringMsg = "cannot be empty"

  def create() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val createPaymentRead = (
      (JsPath \ "orderId").read[String].filter(JsonValidationError(emptyStringMsg))(x => x != null && !x.isEmpty) and
        (JsPath \ "methodCode").read[String].filter(JsonValidationError(emptyStringMsg))(x => x != null && !x.isEmpty) and
        (JsPath \ "amountOfMoney").read[Float].filter(JsonValidationError(emptyStringMsg))(x => x > 0)
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
              val toAdd = Payment(null, data.orderId, data.methodCode, null, data.amountOfMoney, "PENDING")
              paymentDao.create(toAdd).flatMap(payment => Future(Ok(Json.toJson(payment))))
            }
          }
        })
      }
    }
  }

  def cancelPaymentStatus() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val paymentStatusRead = (JsPath \ "paymentId").read[String].filter(JsonValidationError(emptyStringMsg))(x => x != null && !x.isEmpty)
    val validation = request.body.validate[String](paymentStatusRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        paymentDao.belongsToUser(s.value, request.identity.id).flatMap(belongs => {
          if(!belongs) Future(Status(BAD_REQUEST)("payment does not belong to user"))
          else paymentDao.updateStatus(s.value, "CANCELLED").flatMap(_ => Future(Ok))
        })
      }
    }
  }

  def adminUpdatePaymentStatus() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    if(request.identity.role != "ADMIN") Future(Status(UNAUTHORIZED))
    else {
      implicit val paymentStatusRead = (
        (JsPath \ "paymentId").read[String].filter(JsonValidationError(emptyStringMsg))(x => x != null && !x.isEmpty) and
          (JsPath \ "status").read[String].filter(JsonValidationError(emptyStringMsg))(x => x != null && !x.isEmpty &&
            List("PENDING", "ACCEPTED", "REJECTED", "CANCELLED").contains(x))
        ) (UpdatePaymentRequest.apply _)

      val validation = request.body.validate[UpdatePaymentRequest](paymentStatusRead)
      validation match {
        case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
        case s: JsSuccess[UpdatePaymentRequest] => {
          paymentDao.existsWithId(s.value.paymentId).flatMap(exists =>
            if(!exists) Future(Status(NOT_FOUND)("payment with such id does not exist"))
            else paymentDao.updateStatus(s.value.paymentId, s.value.status).flatMap(_ => Future(Ok))
          )
        }
      }
    }
  }

  case class CreatePaymentRequest(orderId: String, methodCode: String, amountOfMoney: Float)

  case class UpdatePaymentRequest(paymentId: String, status: String)
}