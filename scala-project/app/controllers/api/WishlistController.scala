package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.{ProductDao, UserDao, WishlistItemDao}
import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

class WishlistControllerApi @Inject()(cc: MessagesControllerComponents,
                                      silhouette: Silhouette[DefaultEnv],
                                      wishlistItemDao: WishlistItemDao,
                                      userDao: UserDao,
                                      productDao: ProductDao)
                                     (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def getAllForUser() = silhouette.SecuredAction.async { implicit request =>
    wishlistItemDao.getAllWithProductsForUser(request.identity.id).flatMap(items =>
      Future(Ok(Json.toJson(items.map(item => Json.obj(
        "wishItem" -> item._1,
        "product" -> item._2.get
      ))))))
  }

  def getProductStatus(productId: String) = silhouette.SecuredAction.async { implicit request =>
    if (productId == null || productId.isEmpty) Future(Status(BAD_REQUEST)(Json.toJson("cannot be empty")))
    wishlistItemDao.isProductOnUserWishlist(productId, request.identity.id).map { result => Ok(Json.toJson(result)) }
  }

  def addToWishlist() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val addToWishlistRead =
      (JsPath \ "productId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)

    val validation = request.body.validate[String](addToWishlistRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        wishlistItemDao.addToWishlist2(s.value, request.identity.id).flatMap { result =>
          Future(Ok(Json.toJson(result)))
        }
      }
    }
  }

  def deleteFromWishlist() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val deleteFromWishlistRead =
      (JsPath \ "productId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)

    val validation = request.body.validate[String](deleteFromWishlistRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        wishlistItemDao.deleteFromWishlist(s.value, request.identity.id).flatMap { result =>
          Future(Ok(Json.toJson(if (result > 0) true else false)))
        }
      }
    }
  }
}