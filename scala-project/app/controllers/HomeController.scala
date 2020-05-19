package controllers

import daos.CategoryDao
import javax.inject._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsError, JsPath, JsSuccess, JsonValidationError}
import play.api.mvc._

import scala.concurrent.ExecutionContext

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


  def index = Action(parse.json) { implicit request =>
    val read1 = (JsPath \ "name").read[String] and
      (JsPath \ "age").read[Long]

    implicit val read2 = ((JsPath \ "name").read[String].filter(JsonValidationError("length must be > 2"))(_.length > 2) and
      (JsPath \ "age").read[Long]) (SomeClass.apply _)

    val validation = request.body.validate[SomeClass](read2)

    validation match {
      case s: JsSuccess[SomeClass] => Ok(s.value.name)
      case e: JsError => Ok(JsError.toJson(e))
    }
  }
}
case class SomeClass(name: String, age: Long)

