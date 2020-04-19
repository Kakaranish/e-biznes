package controllers

import daos.CategoryDao
import javax.inject._
import models.Category
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class CategoryController @Inject()(categoryDao: CategoryDao, cc: MessagesControllerComponents)
                                  (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll = Action.async { implicit request =>
    val categories = categoryDao.getAll()
    categories.map(cats => Ok(views.html.categories.categories(cats)))
  }

  def getById(categoryId: String) = Action.async { implicit request =>
    val category = categoryDao.getById(categoryId)
    category.map(cat => {
      if (cat == None) Ok(s"There is no category with id $categoryId")
      else Ok(views.html.categories.category(cat.get))
    })
  }

  def create = Action { implicit request =>
    Ok(views.html.categories.createCategory(createCategoryForm))
  }

  def update(categoryId: String) = Action { implicit request =>
    val categoryResult = Await.result(categoryDao.getById(categoryId), Duration.Inf)
    if (categoryResult == None) Ok(s"There is no category with id $categoryId to update")
    else {
      val category = categoryResult.get
      val categoryFormToPass = updateCategoryForm.fill(UpdateCategoryForm(category.id, category.name))
      Ok(views.html.categories.updateCategory(categoryFormToPass))
    }
  }

  def delete(categoryId: String) = Action {
    Ok("")
  }

  // Forms

  val createCategoryForm = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateCategoryForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "name" -> nonEmptyText
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }

  // Handlers section

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

  def updateHandler = Action.async { implicit request =>
    updateCategoryForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categories.updateCategory(errorForm))
        )
      },
      categoryForm => {
        val categoryToUpdate = Category(categoryForm.id, categoryForm.name)
        categoryDao.update(categoryToUpdate).map { _ =>
          Redirect(routes.CategoryController.update(categoryToUpdate.id))
            .flashing("success" -> "Product updated.")
        }
      }
    )
  }
}

case class CreateCategoryForm(name: String)

case class UpdateCategoryForm(id: String, name: String)