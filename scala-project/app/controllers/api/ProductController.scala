package controllers.api

import daos.{CategoryDao, ProductDao}
import javax.inject.{Inject, Singleton}
import models.{Category, Product}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{MessagesControllerComponents, _}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class ProductControllerApi @Inject()(cc: MessagesControllerComponents,
                                     productDao: ProductDao,
                                     categoryDao: CategoryDao)
                                    (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getById(productId: String) = Action.async { implicit request =>
    productDao.getById(productId).map(product => product match {
      case Some(prod) => {
        Ok(Json.obj(
          "product" -> prod._1,
          "category" -> prod._2
        ))
      }
      case _ => Status(NOT_FOUND)(JsError.toJson(JsError("not found")))
    })
  }

  case class CreateProductDto(name: String, description: String, price: Float, quantity: Int, categoryId: String)

  def create() = Action.async(parse.json) { implicit request =>
    implicit val categoryRead = (
      (JsPath \ "name").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "description").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "price").read[Float].filter(JsonValidationError("must be greater > 0"))(x => x != null && x > 0) and
        (JsPath \ "quantity").read[Int].filter(JsonValidationError("must be positive integer"))(x => x != null && x > 0) and
        (JsPath \ "categoryId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
      ) (CreateProductDto.apply _)

    val validation = request.body.validate[CreateProductDto](categoryRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[CreateProductDto] => {
        categoryDao.getById(s.value.categoryId).map(category => {
          if (category.getOrElse(null) == null) {
            Status(BAD_REQUEST)(JsError.toJson(JsError("invalid category")))
          } else {
            val product = Product(null, s.value.name, s.value.description, s.value.price, s.value.quantity, s.value.categoryId)
            val createResult = Await.result(productDao.create(product), Duration.Inf)
            createResult match {
              case 0 => Status(INTERNAL_SERVER_ERROR)(JsError.toJson(JsError("internal error")))
              case 1 => Ok
            }
          }
        })
      }
    }
  }

  case class UpdateProductDto(id: String, name: String, description: String, price: Float, quantity: Int, categoryId: String)

  def update() = Action.async(parse.json) { implicit request =>
    implicit val categoryRead = (
      (JsPath \ "id").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "name").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "description").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "price").read[Float].filter(JsonValidationError("must be greater > 0"))(x => x != null && x > 0) and
        (JsPath \ "quantity").read[Int].filter(JsonValidationError("must be positive integer"))(x => x != null && x > 0) and
        (JsPath \ "categoryId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
      ) (UpdateProductDto.apply _)

    val validation = request.body.validate[UpdateProductDto](categoryRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[UpdateProductDto] => {
        val productResult: Option[(Product, Option[Category])] = Await.result(productDao.getById(s.value.id), Duration.Inf)
        productResult match {
          case Some(prod) => {
            val updatedProduct = Product(s.value.id, s.value.name, s.value.description, s.value.price, s.value.quantity, s.value.categoryId)
            productDao.update(updatedProduct)
            Future(Ok)
          }
          case _ => Future(Status(BAD_REQUEST)(JsError.toJson(JsError("no category with such id"))))
        }
      }
    }
  }
}