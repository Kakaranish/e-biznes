package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.{CartDao, CartItemDao, CategoryDao, ProductDao}
import javax.inject.{Inject, Singleton}
import models.{CartItem, TableDefinitions}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartControllerApi @Inject()(cc: MessagesControllerComponents,
                                  silhouette: Silhouette[DefaultEnv],
                                  cartDao: CartDao,
                                  cartItemDao: CartItemDao,
                                  productDao: ProductDao,
                                  categoryDao: CategoryDao)
                                 (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) with TableDefinitions {

  def getCartItems() = silhouette.SecuredAction.async { implicit request =>
    val user = request.identity

    cartDao.getByUserId(user.id).flatMap { c =>
      c match {
        case None => Future(Ok(JsArray()))
        case Some(cart) => {
          cartItemDao.getAllForCart(cart.id).flatMap { cartItems =>
            Future(Ok(Json.toJson(cartItems.map(ci => Json.obj(
              "cartItem" -> ci._1,
              "product" -> ci._2.get
            )))))
          }
        }
      }
    }
  }

  def addToCart() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val addToCartRead = (
      (JsPath \ "productId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "quantity").read[Int].filter(JsonValidationError("must be positive integer"))(x => x > 0)
      ) (AddToCartRequest.apply _)

    val validation = request.body.validate[AddToCartRequest](addToCartRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[AddToCartRequest] => {
        productDao.getById(s.value.productId).flatMap { p =>
          p match {
            case Some(product) => {
              if (product.quantity < s.value.quantity) Future(Status(BAD_REQUEST)(s"max available product quantity is ${product.quantity}"))
              else {
                val user = request.identity
                cartDao.getOrCreateByUserId(user.id).flatMap { cart =>
                  val cartItemToAdd = CartItem(null, cart.id, s.value.productId, s.value.quantity)
                  cartItemDao.createWithReturn(cartItemToAdd).flatMap { cartItem =>
                    Future(Ok(Json.toJson(cartItem)))
                  }
                }
              }
            }
            case None => Future(Status(NOT_FOUND)("there is no product with such id"))
          }
        }
      }
    }
  }

  def deleteFromCart() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val addToCartRead =
      (JsPath \ "cartItemId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)

    val validation = request.body.validate[String](addToCartRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        val cartItemId = s.value
        cartItemDao.belongsToUser(cartItemId, request.identity.id).flatMap { belongs =>
          belongs match {
            case false => Future(Status(BAD_REQUEST)("cart item does not belong to user cart"))
            case true => {
              cartItemDao.delete(cartItemId).flatMap { _ =>
                Future(Ok)
              }
            }
          }
        }
      }
    }
  }


  def updateCartItemQuantity() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val updateItemRead = (
      (JsPath \ "cartItemId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "quantity").read[Int].filter(JsonValidationError("must be positive integer"))(x => x > 0)
      ) (UpdateCartItemQuantityRequest.apply _)

    val validation = request.body.validate[UpdateCartItemQuantityRequest](updateItemRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[UpdateCartItemQuantityRequest] => {
        cartItemDao.getCartItemCart(s.value.cartItemId).flatMap { c =>
          c match {
            case None => Future(Status(BAD_REQUEST)("cart item does not belong to any cart"))
            case Some(cart) => {
              if (cart.userId != request.identity.id)
                Future(Status(BAD_REQUEST)("cart item does not belong to user cart"))
              else {
                cartItemDao.getPopulatedWithProductById(s.value.cartItemId).flatMap { ciPair =>
                  val product = ciPair.get._2.get
                  if (product.quantity < s.value.quantity) Future(Status(BAD_REQUEST)(s"max available product quantity is ${product.quantity}"))
                  else {
                    cartItemDao.updateQuantity(s.value.cartItemId, s.value.quantity).flatMap { _ => Future(Ok) }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  case class AddToCartRequest(productId: String, quantity: Int)

  case class UpdateCartItemQuantityRequest(cartItemId: String, quantity: Int)

}