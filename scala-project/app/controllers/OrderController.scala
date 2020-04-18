package controllers

import daos.OrderDao
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class OrderController @Inject()(cc: ControllerComponents, orderDao: OrderDao)
                               (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val ordersResult = orderDao.getAll()
    ordersResult.map(orders => {
      if(orders.isEmpty) Ok("There are no orders found")
      else Ok(views.html.orders.orders(orders))
    })
  }

  def getAllForUser(userId: String) = Action.async { implicit request =>
    val ordersResult = orderDao.getAllForUser(userId)
    ordersResult.map(orders => {
      if(orders.isEmpty) Ok(s"There are no orders for user with id $userId")
      else Ok(views.html.orders.ordersForUser(orders))
    })
  }

  def getById(orderId: String) = Action.async { implicit request =>
    val orderResult = orderDao.getById(orderId)
    orderResult.map(order => {
      if(order == None) Ok(s"There is no order with id $orderId")
      else Ok(views.html.orders.order(order.get))
    })
  }

  def create = Action {
    Ok("")
  }

  def update(orderId: String) = Action {
    Ok("")
  }
}
