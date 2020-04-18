package controllers

import daos.WishlistedProductDao
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class WishlistController @Inject()(cc: ControllerComponents, wishlistedProductDao: WishlistedProductDao)
                                  (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def wishItemsForUser(userId: String) = Action.async { implicit request =>
    val wishItems = wishlistedProductDao.getAllForUser(userId)
    wishItems.map(items => {
      if(items.isEmpty) Ok(s"No wishlisted items for user with id $userId")
      else Ok(views.html.wishlistedProducts.wishlistedProductsForUser(items))
    })
  }

  def wishItem(wishItemId: String) = Action.async { implicit request =>
    val wishItem = wishlistedProductDao.getById(wishItemId)
    wishItem.map(item => {
      if(item == None) Ok(s"There is no wish item with id $wishItemId")
      else Ok(views.html.wishlistedProducts.wishlistedProduct(item.get))
    })
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
