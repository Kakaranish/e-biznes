package controllers

import java.util.UUID

import daos.{CartDao, CartItemDao, ProductDao, UserDao}
import javax.inject.{Inject, Singleton}
import models._
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

  def getAllPreviews() = Action.async { implicit request =>
    val cartsResult = cartDao.getAllPreviews()
    cartsResult.map(carts => {
      if(carts.isEmpty) Ok("There are no carts found")
      else Ok(views.html.carts.carts(carts))
    })
  }

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
        val cartItemsResult = cartItemDao.getAllForCart(cartId)
        cartItemsResult.map(cartItems =>
          Ok(views.html.carts.cart(cart, user, cartItems.map(
            cartItem => (cartItem._1._1, cartItem._2.get))))
        )
      }
    }
  }

  def getByUserId(userId: String) = Action.async { implicit request =>
    val cartResult = cartDao.getByUserId(userId)
    cartResult.map(cartTuple => {
      if(cartTuple == None) Ok(s"There is no cart for user $userId")
      else {
        val user = cartTuple.get._2.get
        val cart = cartTuple.get._1
        val cartItems = Await.result(cartItemDao.getAllForCart(cart.id), Duration.Inf)
        Ok(views.html.carts.cartForUser(cart, user,
          cartItems.map(cartItem => (cartItem._1._1, cartItem._2.get)))
        )
      }
    })
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
    val finalizationResult = Await.result(cartDao.getFinalizationStatus(cartId), Duration.Inf)
    if(finalizationResult == None) Future(Ok(s"Failed: There is no cart with id $cartId"))
    else if(finalizationResult.get) Future(Ok(s"Failed: Cart with id $cartId is already finalized"))
    else {
      val availableProducts = Await.result(productDao.getAllPreviews(), Duration.Inf)
      if (availableProducts.isEmpty) Future(Ok("There are no products found"))
      else {
        val formToPass = addItemToCartForm.fill(AddToCartForm(cartId, null, 0))
        val productsPreviews = availableProducts.map(product =>
          ProductPreview(product._1, product._2))
        Future(Ok(views.html.carts.addToCart(formToPass, productsPreviews)))
      }
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
    val cartItem = Await.result(cartItemDao.getById(cartItemId), Duration.Inf)
    if (cartItem == None) Future(Ok(s"There is no cart item with id $cartItemId"))
    else {
      val deleteResult = cartItemDao.delete(cartItemId)
      deleteResult.map(result => {
        if (result != 0) {
          cartDao.setUpdateDateToNow(cartItem.get._1._1.cartId)
          Ok(s"Cart item with id $cartItemId has been deleted")
        }
        else Ok(s"There is no cart item with id $cartItemId")
      })
    }
  }

  def setFinalized(cartId: String) = Action.async { implicit request =>
    val setResult = cartDao.setFinalized(cartId)
    setResult.map(result => {
      if (result != 0) Ok(s"Cart id $cartId has been finalized")
      else Ok(s"There is no cart with id $cartId")
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
        cartItemDao.create(cartItem).map(_ => {
          cartDao.setUpdateDateToNow(cartForm.cartId)
          Redirect(routes.CartController.addToCart(cartForm.cartId))
            .flashing("success" -> "Item added to cart.")
        })
      }
    )
  }
}

case class AddToCartForm(cartId: String,
                         productId: String,
                         quantity: Int)