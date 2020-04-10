package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class OpinionController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def opinions(productId: String) = Action {
    Ok("")
  }

  def opinion(opinionId: String) = Action {
    Ok("")
  }

  def create = Action {
    Ok("")
  }

  def update(opinionId: String) = Action {
    Ok("")
  }

  def delete(opinionId: String) = Action {
    Ok("")
  }

}
