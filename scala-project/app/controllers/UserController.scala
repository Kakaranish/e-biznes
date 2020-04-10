package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def users() = Action {
    Ok("")
  }

  def users(userId: String) = Action {
    Ok("")
  }

  def createUser = Action {
    Ok("")
  }

  def deleteUser(userId: String) = Action {
    Ok("")
  }

  def updateUser(userId: String) = Action {
    Ok("")
  }

}
