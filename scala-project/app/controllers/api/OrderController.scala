package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.api.daos.CartDaoApi
import daos.api.{CartItemDaoApi, OrderDaoApi}
import javax.inject.{Inject, Singleton}
import models.Order
import play.api.libs.json._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderControllerApi @Inject()(cc: MessagesControllerComponents,
                                   silhouette: Silhouette[DefaultEnv],
                                   orderDao: OrderDaoApi,
                                   cartItemDao: CartItemDaoApi,
                                   cartDao: CartDaoApi)
                                  (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def create() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val createOrderRead = (JsPath \ "cartId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)

    val validation = request.body.validate[String](createOrderRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        val cartId = s.value
        cartDao.getById(cartId).flatMap { c =>
          c match {
            case Some(cart) => {
              if (cart.userId != request.identity.id) Future(Status(BAD_REQUEST)("cart does not belong to given user"))
              else if (cart.isFinalized) Future(Status(BAD_REQUEST)("cart is already finalized"))
              else {
                cartDao.setFinalized(cartId).flatMap(_ => {
                  orderDao.create(Order(null, cart.id, request.identity.id, null, null)).flatMap(order => {
                    Future(Ok(Json.toJson(order.id)))
                  })
                })
              }
            }
            case None => Future(Status(NOT_FOUND)(Json.toJson("there is no cart with given id")))
          }
        }
      }
    }
  }

  def getPreviewsForUser() = silhouette.SecuredAction.async { implicit request =>
    orderDao.getByUserId(request.identity.id).map(orders => Ok(Json.toJson(orders)))
  }

  def getPopulatedWithShippingInfoById(orderId: String) = silhouette.SecuredAction.async {implicit request =>
    if (orderId == null || orderId.isBlank || orderId.isEmpty)
      Future(Status(BAD_REQUEST)("orderId is invalid"))

    orderDao.getPopulatedWithShippingInfoById(orderId).flatMap(o => o match {
      case None => Future(Status(NOT_FOUND)("Order not found"))
      case Some(orderInfo) => Future(Ok(Json.obj(
        "order" -> orderInfo._1,
        "shippingInfo" -> orderInfo._2
      )))
    })
  }

  def getPopulatedById(orderId: String) = silhouette.SecuredAction.async { implicit request =>
    if (orderId == null || orderId.isBlank || orderId.isEmpty)
      Future(Status(BAD_REQUEST)("orderId is invalid"))

    orderDao.belongsToUser(orderId, request.identity.id).flatMap(belongs =>
      if (!belongs) Future(Status(BAD_REQUEST)("orderId does not belong to user"))
      else {
        orderDao.getPopulatedById(orderId).flatMap(oi => oi match {
          case Some(orderInfo) => {

            val cartItemsInfo = orderInfo._4.map(ci => Json.obj(
              "cartItem" -> ci._1,
              "product" -> ci._2
            ))
            var resultObj = Json.obj(
              "order" -> orderInfo._1,
              "cart" -> orderInfo._2,
              "cartItems" -> cartItemsInfo,
              "payments" -> Json.toJson(orderInfo._5)
            )
            if (orderInfo._3.isDefined) resultObj = resultObj + ("shippingInfo" -> Json.toJson(orderInfo._3.get))

            Future(Ok(Json.toJson(resultObj)))
          }
        })
      }
    )
  }
}