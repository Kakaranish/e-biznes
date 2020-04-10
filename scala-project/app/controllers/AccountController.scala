package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class AccountController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def login = Action {
    Ok("")
  }

  def register = Action {
    Ok("")
  }
  
}
