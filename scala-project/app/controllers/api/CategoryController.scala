package controllers.api

import daos.api.{CategoryDaoApi, ProductDaoApi}
import javax.inject._
import models.Category
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class CategoryControllerApi @Inject()(cc: MessagesControllerComponents,
                                      categoryDao: CategoryDaoApi,
                                      productDao: ProductDaoApi)
                                     (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val categoriesResult = categoryDao.getAll()
    categoriesResult.map(categories => Ok(Json.toJson(categories)))
  }

  def getById(categoryId: String) = Action.async { implicit request =>
    val categoryResult = categoryDao.getById(categoryId)
    categoryResult.map(category => Ok(Json.toJson(category)))
  }

  def create() = Action.async(parse.json) { implicit request =>
    implicit val categoryRead: Reads[String] = (JsPath \ "name").read[String]
      .filter(JsonValidationError("must be non-empty"))(_.length > 0)

    val validation = request.body.validate[String](categoryRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        categoryDao.getByName(s.value).flatMap(exists => {
          exists match {
            case Some(_) => Future(Status(BAD_REQUEST)(JsError.toJson(JsError("category with such name exists"))))
            case _ => categoryDao.create(Category(null, s.value, false)).flatMap(_ => Future(Ok))
          }
        })
      }
    }
  }

  case class CategoryUpdateDto(id: String, name: String)

  def update() = Action.async(parse.json) { implicit request =>
    implicit val categoryUpdateRead = (
      (JsPath \ "id").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "name").read[String].filter(JsonValidationError("must be non-empty"))(_.length > 0)
      ) (CategoryUpdateDto.apply _)

    val validation = request.body.validate[CategoryUpdateDto](categoryUpdateRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[CategoryUpdateDto] => {
        categoryDao.getById(s.value.id).flatMap(category => {
          if (category.getOrElse(null) == null) Future(Status(BAD_REQUEST)(JsError.toJson(JsError("no category with such id"))))
          else if (category.get.name == s.value.name) Future(Ok)
          else {
            val otherCategoryResult = Await.result(categoryDao.getByName(s.value.name), Duration.Inf)
            otherCategoryResult match {
              case Some(_) => Future(Status(BAD_REQUEST)(JsError.toJson(JsError("other category has such name"))))
              case None => categoryDao.update(Category(s.value.id, s.value.name, false)).flatMap(_ => Future(Ok))
            }
          }
        })
      }
    }
  }

  def delete() = Action.async(parse.json) { implicit request =>
    implicit val categoryRead: Reads[String] = (JsPath \ "id").read[String]
      .filter(JsonValidationError("must be non-empty"))(_.length > 0)
    val validation = request.body.validate[String](categoryRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        categoryDao.getById(s.value).flatMap(category => {
          if (category.getOrElse(null) == null) Future(Status(BAD_REQUEST)(JsError.toJson(JsError("no category with such id"))))
          else {
            productDao.existsAnyWithCategoryId(s.value).flatMap(exists => {
              if(exists) Future(Status(BAD_REQUEST)(JsError.toJson(JsError("cannot remove because category is assigned to at least one product"))))
              else categoryDao.delete(s.value).flatMap(_ => Future(Ok))
            })
          }
        })
      }
    }
  }
}