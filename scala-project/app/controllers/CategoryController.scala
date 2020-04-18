package controllers

import daos.CategoryDao
import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryController @Inject()(categoryDao: CategoryDao, cc: MessagesControllerComponents)
                                  (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val createCategoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  def getAll = Action.async { implicit request =>
    val categories = categoryDao.getAll()
    categories.map(cats => Ok(views.html.categories.categories(cats)))
  }

  def getById(categoryId: String) = Action.async { implicit request =>
    val category = categoryDao.getWithId(categoryId)
    category.map(cat => {
      if (cat == None) Ok(s"There is no category with id $categoryId")
      else Ok(views.html.categories.category(cat.get))
    })
  }

  def create = Action { implicit request =>
    Ok(views.html.categories.createCategory(createCategoryForm))
  }

  def createHandler = Action.async { implicit request =>
    createCategoryForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categories.createCategory(errorForm))
        )
      },
      category => {
        categoryDao.create(category.name).map(_ => {
          Redirect(routes.CategoryController.create())
            .flashing("success" -> "category.created")
        })
      }
    )
  }

  def update(categoryId: String) = Action {
    Ok("")
  }

  def delete(categoryId: String) = Action {
    Ok("")
  }
}

case class CreateCategoryForm(name: String)