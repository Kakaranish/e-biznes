package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
