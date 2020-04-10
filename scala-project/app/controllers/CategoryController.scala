package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class CategoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def categories = Action {
    Ok("")
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
