package controllers

import daos.{ProductDao, UserDao, WishlistedProductDao}
import javax.inject.Inject
import models.{ProductPreview, UserPreview, WishlistedProduct}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class WishlistController @Inject()(cc: MessagesControllerComponents,
                                   wishlistedProductDao: WishlistedProductDao,
                                   userDao: UserDao,
                                   productDao: ProductDao)
                                  (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def wishItemsForUser(userId: String) = Action.async { implicit request =>
    val wishlistedProductsResult = wishlistedProductDao.getAllForUser(userId)
    wishlistedProductsResult.map(wishlistedProducts => {
      if (wishlistedProducts.isEmpty) Ok(s"No wishlisted items for user with id $userId")
      else Ok(views.html.wishlistedProducts.wishlistedProductsForUser(wishlistedProducts))
    })
  }

  def wishItem(wishItemId: String) = Action.async { implicit request =>
    val wishlistedProductResult = wishlistedProductDao.getById(wishItemId)
    wishlistedProductResult.map(wishlistedProduct => {
      if (wishlistedProduct == None) Ok(s"There is no wish item with id $wishItemId")
      else Ok(views.html.wishlistedProducts.wishlistedProduct(wishlistedProduct.get))
    })
  }

  def create = Action { implicit request =>
    val usersPreviews = Await.result(userDao.getAllPreviews(), Duration.Inf)
    if (usersPreviews.isEmpty) Ok("Unable to create wishlist product - there are no users")

    val productsPreviews = Await.result(productDao.getAllPreviews(), Duration.Inf)
    if (productsPreviews.isEmpty) Ok("Unable to create wishlist product - there are no products")

    Ok(views.html.wishlistedProducts.createWishlistedProduct(createForm,
      usersPreviews.map(userPreview => UserPreview(userPreview._1, userPreview._2)),
      productsPreviews.map(productPreview => ProductPreview(productPreview._1, productPreview._2)))
    )
  }

  def delete(wishItemId: String) = Action.async { implicit request =>
    val deleteResult = wishlistedProductDao.delete(wishItemId)
    deleteResult.map(result => {
      if (result != 0) Ok(s"Wishlist product with id $wishItemId has been deleted")
      else Ok(s"There is no wishlist product with id $wishItemId")
    })
  }

  // Forms

  val createForm = Form {
    mapping(
      "userId" -> nonEmptyText,
      "productId" -> nonEmptyText
    )(CreateWishlistedProductForm.apply)(CreateWishlistedProductForm.unapply)
  }

  // Handlers

  val createHandler = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        val usersPreviews = Await.result(userDao.getAllPreviews(), Duration.Inf)
        val productsPreviews = Await.result(productDao.getAllPreviews(), Duration.Inf)
        Future.successful(
          BadRequest(views.html.wishlistedProducts.createWishlistedProduct(
            errorForm, usersPreviews.map(userPreview => UserPreview(userPreview._1, userPreview._2)),
            productsPreviews.map(productPreview => ProductPreview(productPreview._1, productPreview._2))))
        )
      },
      createForm => {
        // TODO in future: it would be nice to validate if wishlist item already exists
        val wishlistProduct = WishlistedProduct(null, createForm.userId, createForm.productId)
        wishlistedProductDao.create(wishlistProduct).map(_ =>
          Redirect(routes.WishlistController.create())
            .flashing("success" -> "Wishlist product created.")
        )
      }
    )
  }
}

case class CreateWishlistedProductForm(userId: String,
                                       productId: String)