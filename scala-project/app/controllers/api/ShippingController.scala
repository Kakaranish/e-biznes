package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.api.{OrderDaoApi, ShippingInfoDaoApi}
import javax.inject.{Inject, Singleton}
import models.ShippingInfo
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, JsonValidationError, _}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShippingControllerApi @Inject()(cc: MessagesControllerComponents,
                                      silhouette: Silhouette[DefaultEnv],
                                      shippingInfoDao: ShippingInfoDaoApi,
                                      orderDao: OrderDaoApi)
                                     (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def create() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val createShippingInfoRead = (
      (JsPath \ "orderId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "country").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "city").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "address").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "zipOrPostcode").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
      ) (CreateShippingInfoRequest.apply _)

    val validation = request.body.validate[CreateShippingInfoRequest](createShippingInfoRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[CreateShippingInfoRequest] => {
        val data = s.value
        orderDao.getById(data.orderId).flatMap(o => o match {
          case None => Future(Status(NOT_FOUND)("no such order"))
          case Some(order) => {
            if (order.userId != request.identity.id) Future(Status(BAD_REQUEST)("given order does not belong to user"))
            else {
              val shippingInfo = ShippingInfo(null, data.country, data.city, data.address, data.zipOrPostcode)
              shippingInfoDao.create(shippingInfo, data.orderId).flatMap(si => Future(Ok(Json.toJson(si))))
            }
          }
        })
      }
    }
  }

  def update() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val updateShippingInfoRead = (
      (JsPath \ "id").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "country").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "city").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "address").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "zipOrPostcode").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
      ) (UpdateShippingInfoRequest.apply _)

    val validation = request.body.validate[UpdateShippingInfoRequest](updateShippingInfoRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[UpdateShippingInfoRequest] => {
        val data = s.value
        shippingInfoDao.belongsToUser(data.id, request.identity.id).flatMap(belongs => {
          if (!belongs) Future(Status(BAD_REQUEST)("shipping info does not belong to user"))
          else {
            val shippingInfo = ShippingInfo(data.id, data.country, data.city, data.address, data.zipOrPostcode)
            shippingInfoDao.update(shippingInfo).flatMap(_ => Future(Ok))
          }
        })
      }
    }
  }

  def delete() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val deleteShippingRead = (
      (JsPath \ "shippingInfoId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty))

    val validation = request.body.validate[String](deleteShippingRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        val data = s.value
        shippingInfoDao.belongsToUser(data, request.identity.id).flatMap(belongs => {
          if (!belongs) Future(Status(BAD_REQUEST)("shipping info does not belong to user"))
          else shippingInfoDao.delete(data).flatMap(_ => Future(Ok))
        })
      }
    }
  }

  case class CreateShippingInfoRequest(orderId: String,
                                       country: String,
                                       city: String,
                                       address: String,
                                       zipOrPostcode: String)

  case class UpdateShippingInfoRequest(id: String,
                                       country: String,
                                       city: String,
                                       address: String,
                                       zipOrPostcode: String)

}