package controllers

import daos.CategoryDao
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class CategoryControllerApi @Inject()(cc: MessagesControllerComponents, categoryDao: CategoryDao)
                                     (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val categoriesResult = categoryDao.getAll()
    categoriesResult.map(categories => Ok(Json.toJson(categories)))
  }

  def getById(categoryId: String) = Action.async { implicit request =>
    val categoryResult = categoryDao.getById(categoryId)
    categoryResult.map(category => Ok(Json.toJson(category)))
  }
}