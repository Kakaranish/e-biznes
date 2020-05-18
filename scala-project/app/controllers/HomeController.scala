package controllers

import daos.CategoryDao
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: MessagesControllerComponents, categoryDao: CategoryDao)
                              (implicit exec: ExecutionContext) extends MessagesAbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action.async { implicit request =>

    Future {
      val categories = Await.result(categoryDao.getAll(), Duration.Inf);
      val categoriesAsJson = Json.toJson(categories);
      Ok(categoriesAsJson);
    }
  }

}
