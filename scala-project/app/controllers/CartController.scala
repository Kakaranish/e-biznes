package controllers

import java.util.UUID

import daos.{CartDao, CartItemDao, ProductDao, UserDao}
import javax.inject.{Inject, Singleton}
import models.{Cart, CartItem, ProductPreview}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.Form
import play.api.data.Forms._
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

  def addToCart(cartId: String) = Action.async { implicit request =>
    val availableProducts = Await.result(productDao.getAllPreviews(), Duration.Inf)
    if (availableProducts.isEmpty) Future(Ok("There are no products found"))
    else {
      val formToPass = addItemToCartForm.fill(AddToCartForm(cartId, null, 0))
      val productsPreviews = availableProducts.map(product =>
        ProductPreview(product._1, product._2))
      Future(Ok(views.html.carts.addToCart(formToPass, productsPreviews)))
    }
  }

  def delete(cartId: String) = Action {
    val cartResult = Await.result(cartDao.getById(cartId), Duration.Inf)
    if (cartResult == None) {
      Ok(s"There is no cart with id $cartId")
    } else {
      val cart = cartResult.get
      if (cart._1.isFinalized) {
        Ok(s"Cart with id $cartId is already finalized so it can't be removed")
      } else {
        val deleteResult = Await.result(cartDao.delete(cartId), Duration.Inf)
        if (deleteResult != 0) Ok(s"Cart with id $cartId has been deleted")
        else Ok(s"Unable to remove cart with id $cartId")
      }
    }
  }

  def deleteCartItem(cartItemId: String) = Action.async { implicit request =>
    val deleteResult = cartItemDao.delete(cartItemId)
    deleteResult.map(result => {
      if (result != 0) Ok(s"Cart item with id $cartItemId has been deleted")
      else Ok(s"There is no cart item with id $cartItemId")
    })
  }

  // Forms

  val addItemToCartForm = Form {
    mapping(
      "cartId" -> nonEmptyText,
      "productId" -> nonEmptyText,
      "quantity" -> number(1, 20)
    )(AddToCartForm.apply)(AddToCartForm.unapply)
  }

  // Handlers

  val addToCartHandler = Action.async { implicit request =>
    addItemToCartForm.bindFromRequest().fold(
      errorForm => {
        val availableProducts = Await.result(productDao.getAllPreviews(), Duration.Inf)
        Future.successful(
          BadRequest(views.html.carts.addToCart(errorForm, availableProducts.map(
            tuple => ProductPreview(tuple._1, tuple._2))))
        )
      },
      cartForm => {
        val cartItem = CartItem(null, cartForm.cartId, cartForm.productId, cartForm.quantity)
        cartItemDao.create(cartItem).map(_ =>
          Redirect(routes.CartController.addToCart(cartForm.cartId))
            .flashing("success" -> "Item added to cart.")
        )
      }
    )
  }
}

case class AddToCartForm(cartId: String,
                         productId: String,
                         quantity: Int)