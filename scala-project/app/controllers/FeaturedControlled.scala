package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class FeaturedControlled @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def featured(productId: String) = Action {
    Ok("")
  }

}
