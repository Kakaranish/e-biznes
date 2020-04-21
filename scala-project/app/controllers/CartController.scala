package controllers

import daos.{CartDao, CartItemDao, ProductDao, UserDao}
import javax.inject.{Inject, Singleton}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

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
    if(cartResult == None)  Future(Ok(s"There is no cart with id $cartId"))

    else {
      val cart = cartResult.get._1

      val userResult = cartResult.get._2
      if(userResult == None) Future.successful(Ok("There is no user"))
      val user = userResult.get

      val orderedProductsResult = cartItemDao.getAllForCart(cartId)
      orderedProductsResult.map(orderedProducts =>
        Ok(views.html.carts.cart(cart, user, orderedProducts.map(
          orderedItem => (orderedItem._1._1, orderedItem._2.get))))
      )
    }
  }
}
