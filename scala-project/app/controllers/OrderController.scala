package controllers

import java.util.UUID

import daos.{CartDao, CartItemDao, OrderDao, UserDao}
import javax.inject._
import models.Order
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OrderController @Inject()(cc: MessagesControllerComponents,
                                orderDao: OrderDao,
                                userDao: UserDao,
                                cartItemDao: CartItemDao,
                                cartDao: CartDao)
                               (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val ordersResult = orderDao.getAll()
    ordersResult.map(orders => {
      if (orders.isEmpty) Ok("There are no orders found")
      else Ok(views.html.orders.orders(orders))
    })
  }

  def getAllForUser(userId: String) = Action.async { implicit request =>
    val userResult = userDao.getById(userId)
    userResult.map(user => {
      if (user == None) Ok(s"There is no user with id $userId")
      else {
        val orders = Await.result(orderDao.getAllForUser(userId), Duration.Inf)
        if (orders.isEmpty) Ok(s"There are no orders for user with id $userId")
        else Ok(views.html.orders.ordersForUser(orders, user.get))
      }
    })
  }

  def getById(orderId: String) = Action.async { implicit request =>
    val orderResult = Await.result(orderDao.getById(orderId), Duration.Inf)
    if (orderResult == None) Future(Ok(s"There is no order with id $orderId"))
    else {
      val order = orderResult.get
      val cartId = order._1._1.cartId
      val orderedProductsResult = cartItemDao.getAllForCart(cartId)

      orderedProductsResult.map(orderedProducts =>
        Ok(views.html.orders.order(order, orderedProducts.map(
          orderedItem => (orderedItem._1, orderedItem._2.get))))
      )
    }
  }

  def create(cartId: String) = Action.async { implicit request =>
    val cartResult = Await.result(cartDao.getById(cartId), Duration.Inf)
    if (cartResult == None) {
      Future(Ok(s"There is no cart with id $cartId"))
    } else {
      val cart = cartResult.get._1
      if (cart.isFinalized) Future(Ok(s"Failed: Cart with id $cartId is already finalized"))
      else {
        val userId = cartResult.get._2.get.id
        val orderId = UUID.randomUUID().toString()
        val order = Order(orderId, cart.id, userId, null, null)
        orderDao.createWithId(order).map(createResult => {
          if (createResult != 0) {
            cartDao.setFinalized(cartId)
            Ok(s"Success. Order with id $orderId has been created")
          }
          else Ok("Creating order failed")
        })
      }
    }
  }

  def update(orderId: String) = Action {
    Ok("")
  }
}
