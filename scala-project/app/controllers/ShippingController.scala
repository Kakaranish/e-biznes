package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class ShippingController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAllShippingInfoForUser(userId: String) = Action {
    Ok("")
  }

  def getWithId(shippingInfoId: String) = Action {
    Ok("")
  }

  def create = Action {
    Ok("")
  }

  def update(shippingInfoId: String) = Action {
    Ok("")
  }

  def delete(shippingInfoId: String) = Action {
    Ok("")
  }
}
