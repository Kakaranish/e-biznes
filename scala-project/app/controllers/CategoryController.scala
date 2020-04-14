package controllers

import daos.CategoryDao
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class CategoryController @Inject()(categoryDao: CategoryDao, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def categories = Action.async { implicit request =>
    val categories = categoryDao.list()
    categories.map(cats => Ok(views.html.categories(cats)))
  }

  def category(categoryId: String) = Action {
    Ok("")
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
