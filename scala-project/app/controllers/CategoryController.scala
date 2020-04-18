package controllers

import daos.CategoryDao
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class CategoryController @Inject()(categoryDao: CategoryDao, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def categories = Action.async { implicit request =>
    val categories = categoryDao.getAll()
    categories.map(cats => Ok(views.html.categories.categories(cats)))
  }

  def category(categoryId: String) = Action.async { implicit request =>
    val category = categoryDao.getWithId(categoryId)
    category.map(cat => {
      if(cat == None) Ok(s"There is no category with id $categoryId")
      else Ok(views.html.categories.category(cat.get))
    })
  }

  def create = Action {
    Ok("")
  }

  def update(categoryId: String) = Action {
    Ok("")
  }

  def delete(categoryId: String) = Action {
    Ok("")
  }

}
