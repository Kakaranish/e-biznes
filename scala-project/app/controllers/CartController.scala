package controllers

import java.util.UUID
import daos.{CartDao, CartItemDao, ProductDao, UserDao}
import javax.inject.{Inject, Singleton}
import models.Cart
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class CartController @Inject()(cc: MessagesControllerComponents,
                               cartDao: CartDao,
                               cartItemDao: CartItemDao,
                               userDao: UserDao,
                               productDao: ProductDao)
                              (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def getById(cartId: String) = Action.async { implicit request =>
    val cartResult = Await.result(cartDao.getById(cartId), Duration.Inf)
    if (cartResult == None) Future(Ok(s"There is no cart with id $cartId"))

    else {
      val cart = cartResult.get._1

      val userResult = cartResult.get._2
      if (userResult == None) {
        Future.successful(Ok("There is no user"))
      } else {
        val user = userResult.get

        val orderedProductsResult = cartItemDao.getAllForCart(cartId)
        orderedProductsResult.map(orderedProducts =>
          Ok(views.html.carts.cart(cart, user, orderedProducts.map(
            orderedItem => (orderedItem._1._1, orderedItem._2.get))))
        )
      }
    }
  }

  def createForUser(userId: String) = Action {
    val id = UUID.randomUUID().toString()
    val nowIso = new DateTime().toString(DateTimeFormat
      .forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))

    val userResult = Await.result(userDao.getById(userId), Duration.Inf)
    if (userResult == None) {
      Ok(s"There is no user with id $userId")
    } else {
      val cart = Cart(id, userId, false, nowIso)
      val createResult = Await.result(cartDao.createWithId(cart), Duration.Inf)
      if (createResult == 0) Ok(s"Unable to create cart for user $userId")
      else Ok(s"Cart with id $id created.")
    }
  }
}