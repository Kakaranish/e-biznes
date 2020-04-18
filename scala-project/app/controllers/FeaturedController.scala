package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class FeaturedController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def featured(productId: String) = Action {
    Ok("")
  }
}
