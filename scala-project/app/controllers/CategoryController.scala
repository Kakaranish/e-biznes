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
class CategoryController @Inject()(cc: MessagesControllerComponents, categoryDao: CategoryDao)
                                  (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
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

  def create() = Action { implicit request =>
    Ok(views.html.categories.createCategory(createForm))
  }

  def update(categoryId: String) = Action { implicit request =>
    val categoryResult = Await.result(categoryDao.getById(categoryId), Duration.Inf)
    if (categoryResult == None) Ok(s"There is no category with id $categoryId to update")
    else {
      val category = categoryResult.get
      val categoryFormToPass = updateForm.fill(UpdateCategoryForm(category.id, category.name))
      Ok(views.html.categories.updateCategory(categoryFormToPass))
    }
  }

  def delete(categoryId: String) = Action.async { implicit reqeust =>
    val deleteResult = categoryDao.delete(categoryId)
    deleteResult.map(result => {
      if(result != 0) Ok(s"Category with id $categoryId has been deleted")
      else Ok(s"There is no category with id $categoryId")
    })
  }

  // Forms

  val createForm = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "name" -> nonEmptyText
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }

  // Handlers section

  def createHandler() = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categories.createCategory(errorForm))
        )
      },
      categoryForm => {
        val category = Category(null, categoryForm.name, false)
        categoryDao.create(category).map(_ =>
          Redirect(routes.CategoryController.create())
            .flashing("success" -> "Category created.")
        )
      }
    )
  }

  def updateHandler() = Action.async { implicit request =>
    updateForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categories.updateCategory(errorForm))
        )
      },
      categoryForm  => {
        val categoryToUpdate = Category(categoryForm.id, categoryForm.name, false)
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