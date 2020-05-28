package controllers

import daos.{ProductDao, UserDao, WishlistItemDao}
import javax.inject.Inject
import models.{ProductPreview, UserPreview, WishlistItem}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class WishlistController @Inject()(cc: MessagesControllerComponents,
                                   wishlistItemDao: WishlistItemDao,
                                   userDao: UserDao,
                                   productDao: ProductDao)
                                  (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def wishItemsForUser(userId: String) = Action.async { implicit request =>
    val wishlistItemResult = wishlistItemDao.getAllPopulatedForUser(userId)
    wishlistItemResult.map(wishlistItem => {
      if (wishlistItem.isEmpty) Ok(s"No wishlist items for user with id $userId")
      else Ok(views.html.wishlistItems.wishlisItemsForUser(wishlistItem))
    })
  }

  def wishItem(wishItemId: String) = Action.async { implicit request =>
    val wishlistItemResult = wishlistItemDao.getById(wishItemId)
    wishlistItemResult.map(wishlistItem => {
      if (wishlistItem == None) Ok(s"There is no wishlist item with id $wishItemId")
      else Ok(views.html.wishlistItems.wishlistItem(wishlistItem.get))
    })
  }

  def create = Action { implicit request =>
    val usersPreviews = Await.result(userDao.getAllPreviews(), Duration.Inf)
    if (usersPreviews.isEmpty) Ok("Unable to create wishlist item - there are no users")

    val productsPreviews = Await.result(productDao.getAllPreviews(), Duration.Inf)
    if (productsPreviews.isEmpty) Ok("Unable to create wishlist item - there are no products")

    Ok(views.html.wishlistItems.createWishlistItem(createForm,
      usersPreviews.map(userPreview => UserPreview(userPreview._1, userPreview._2)),
      productsPreviews.map(productPreview => ProductPreview(productPreview._1, productPreview._2, productPreview._3)))
    )
  }

  def delete(wishItemId: String) = Action.async { implicit request =>
    val deleteResult = wishlistItemDao.delete(wishItemId)
    deleteResult.map(result => {
      if (result != 0) Ok(s"Wishlist item with id $wishItemId has been deleted")
      else Ok(s"There is no wishlist item with id $wishItemId")
    })
  }

  // Forms

  val createForm = Form {
    mapping(
      "userId" -> nonEmptyText,
      "productId" -> nonEmptyText
    )(CreateWishlistItemForm.apply)(CreateWishlistItemForm.unapply)
  }

  // Handlers

  val createHandler = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        val usersPreviews = Await.result(userDao.getAllPreviews(), Duration.Inf)
        val productsPreviews = Await.result(productDao.getAllPreviews(), Duration.Inf)
        Future.successful(
          BadRequest(views.html.wishlistItems.createWishlistItem(
            errorForm, usersPreviews.map(userPreview => UserPreview(userPreview._1, userPreview._2)),
            productsPreviews.map(productPreview => ProductPreview(productPreview._1, productPreview._2, productPreview._3))))
        )
      },
      createForm => {
        // TODO in future: it would be nice to validate if wishlist item already exists
        val wishlistProduct = WishlistItem(null, createForm.userId, createForm.productId)
        wishlistItemDao.create(wishlistProduct).map(_ =>
          Redirect(routes.WishlistController.create())
            .flashing("success" -> "Wishlist item created.")
        )
      }
    )
  }
}

case class CreateWishlistItemForm(userId: String,
                                  productId: String)