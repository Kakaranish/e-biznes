package controllers

import daos.{OpinionDao, ProductDao, UserDao}
import javax.inject._
import models.{Opinion, ProductPreview}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OpinionController @Inject()(cc: MessagesControllerComponents,
                                  opinionDao: OpinionDao,
                                  productDao: ProductDao,
                                  userDao: UserDao)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAllForProduct(productId: String) = Action.async { implicit request =>
    val opinionsResult = opinionDao.getAllForProduct(productId)
    opinionsResult.map(opinions => {
      if (opinions.isEmpty) Ok(s"There are no opinions for product with id $productId")
      else {
        val product = Await.result(productDao.getById(productId), Duration.Inf).get
        Ok(views.html.opinions.opinionsForProduct(product._1, opinions))
      }
    })
  }

  def getById(opinionId: String) = Action.async { implicit request =>
    val opinionResult = opinionDao.getById(opinionId)
    opinionResult.map(opinion => {
      if (opinion == None) Ok(s"There is no opinion with id $opinionId")
      else Ok(views.html.opinions.opinion(opinion.get))
    })
  }

  def create = Action { implicit request =>
    val availableUsers = Await.result(userDao.getAll(), Duration.Inf)
    if (availableUsers.isEmpty) Ok("Can't add opinion - there are no users in db.")

    val availableProductsResult = Await.result(productDao.getAllPreviews(), Duration.Inf)
    if (availableProductsResult.isEmpty) Ok("Can't add opinion - there are no products in db.")
    val availableProducts = availableProductsResult.map(product => ProductPreview(product._1, product._2))

    Ok(views.html.opinions.createOpinion(createForm, availableUsers, availableProducts))
  }

  def update(opinionId: String) = Action {
    Ok("")
  }

  def delete(opinionId: String) = Action {
    Ok("")
  }

  // Forms
  val createForm = Form {
    mapping(
      "userId" -> nonEmptyText,
      "productId" -> nonEmptyText,
      "content" -> nonEmptyText
    )(CreateOpinionForm.apply)(CreateOpinionForm.unapply)
  }

  val updateForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "userId" -> nonEmptyText,
      "productId" -> nonEmptyText,
      "content" -> nonEmptyText
    )(UpdateOpinionForm.apply)(UpdateOpinionForm.unapply)
  }

  // Handlers

  val createHandler = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        val availableUsers = Await.result(userDao.getAll(), Duration.Inf)
        val availableProducts = Await.result(productDao.getAllPreviews(), Duration.Inf)
          .map(product => ProductPreview(product._1, product._2))
        Future.successful(
          BadRequest(views.html.opinions.createOpinion(errorForm, availableUsers, availableProducts))
        )
      },
      createForm => {
        val opinion = Opinion(null, createForm.userId, createForm.productId, createForm.content)
        opinionDao.create(opinion).map(_ =>
          Redirect(routes.OpinionController.create())
            .flashing("success" -> "Opinion created.")
        )
      }
    )
  }


  //  val updateHandler = Action.async { implicit request =>
  //    updateForm.bindFromRequest().fold(
  //      errorForm => {
  //        Future.successful(
  //          BadRequest(views.html.index)
  //        )
  //      },
  //      updateForm => {
  //        // Placeholder for logic
  //        Future.successful(
  //          BadRequest(views.html.index)
  //        )
  //      }
  //    )
  //  }
}

case class CreateOpinionForm(userId: String,
                             productId: String,
                             content: String)

case class UpdateOpinionForm(id: String,
                             userId: String,
                             productId: String,
                             content: String)