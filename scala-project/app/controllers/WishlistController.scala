package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

class WishlistController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def wishItems(userId: String) = Action {
    Ok("")
  }

  def wishItem(wishItemId: String) = Action {
    Ok("")
  }

  def create = Action {
    Ok("")
  }

  def update(wishItemId: String) = Action {
    Ok("")
  }

  def delete(wishItemId: String) = Action {
    Ok("")
  }
}
