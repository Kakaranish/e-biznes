package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.api.{CategoryDaoApi, ProductDaoApi}
import javax.inject.{Inject, Singleton}
import models.{Category, Product}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{MessagesControllerComponents, _}
import silhouette.DefaultEnv

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class ProductControllerApi @Inject()(cc: MessagesControllerComponents,
                                     silhouette: Silhouette[DefaultEnv],
                                     productDao: ProductDaoApi,
                                     categoryDao: CategoryDaoApi)
                                    (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    productDao.getAll().map(prods => Ok(Json.toJson(prods.map(p => Json.obj(
      "product" -> p._1,
      "category" -> p._2.get
    )))));
  }

  def getAllByCategory(categoryId: String) = Action.async {
    if (categoryId == null || categoryId.isEmpty) Future(Status(BAD_REQUEST)(Json.toJson(" categoryId cannot be empty")))
    productDao.getAllByCategoryId(categoryId).flatMap { result =>
      Future(Ok(Json.obj(
        "category" -> result._1.get,
        "products" -> Json.toJson(result._2)
      )))
    }
  }

  def getById(productId: String) = silhouette.UserAwareAction.async { implicit request =>

    request.identity match {
      case None => {
        productDao.getPopulatedById(productId).map(product => product match {
          case Some(prod) => {
            Ok(Json.obj(
              "product" -> prod._1,
              "category" -> prod._2.getOrElse(null),
              "wishlistItem" -> JsNull,
              "cartItem" -> JsNull
            ))
          }
          case _ => Status(NOT_FOUND)(JsError.toJson(JsError("not found")))
        })
      }
      case Some(_) => {
        productDao.getProductPreview(productId, request.identity.get.id).map(result =>
          if (!result._1.isDefined) Status(NOT_FOUND)(JsError.toJson(JsError("not found")))
          else {
            var resJson = Json.obj("product" -> result._1.get._1)
            if (result._1.get._2.isDefined) resJson = resJson + ("category" -> Json.toJson(result._1.get._2.get))
            if (result._2.isDefined) resJson = resJson + ("wishlistItem" -> Json.toJson(result._2.get))
            if (result._4 != null && result._4.isDefined) resJson = resJson + ("cartItem" -> Json.toJson(result._4.get))
            Ok(resJson)
          })
      }
    }
  }

  case class CreateProductDto(name: String, description: String, price: Float, quantity: Int, categoryId: String)

  def create() = Action.async(parse.json) { implicit request =>
    implicit val productRead = (
      (JsPath \ "name").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "description").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "price").read[Float].filter(JsonValidationError("must be greater > 0"))(x => x != null && x > 0) and
        (JsPath \ "quantity").read[Int].filter(JsonValidationError("must be positive integer"))(x => x != null && x > 0) and
        (JsPath \ "categoryId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
      ) (CreateProductDto.apply _)

    val validation = request.body.validate[CreateProductDto](productRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[CreateProductDto] => {
        categoryDao.getById(s.value.categoryId).map(category => {
          if (category.getOrElse(null) == null) {
            Status(BAD_REQUEST)(JsError.toJson(JsError("invalid category")))
          } else {
            val product = Product(null, s.value.name, s.value.description, s.value.price, s.value.quantity, s.value.categoryId, false)
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
    implicit val productRead = (
      (JsPath \ "id").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "name").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "description").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
        (JsPath \ "price").read[Float].filter(JsonValidationError("must be greater > 0"))(x => x != null && x > 0) and
        (JsPath \ "quantity").read[Int].filter(JsonValidationError("must be positive integer"))(x => x != null && x > 0) and
        (JsPath \ "categoryId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
      ) (UpdateProductDto.apply _)

    val validation = request.body.validate[UpdateProductDto](productRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[UpdateProductDto] => {
        val productResult: Option[(Product, Option[Category])] = Await.result(productDao.getPopulatedById(s.value.id), Duration.Inf)
        productResult match {
          case Some(_) => {
            val updatedProduct = Product(s.value.id, s.value.name, s.value.description, s.value.price, s.value.quantity, s.value.categoryId, false)
            productDao.update(updatedProduct)
            Future(Ok)
          }
          case _ => Future(Status(BAD_REQUEST)(JsError.toJson(JsError("no category with such id"))))
        }
      }
    }
  }

  def delete() = Action.async(parse.json) { implicit request =>
    implicit val productRead = (JsPath \ "id").read[String]
      .filter(JsonValidationError("must be non-empty"))(_.length > 0)
    val validation = request.body.validate[String](productRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        productDao.getPopulatedById(s.value).flatMap(product => {
          product match {
            case Some(_) => productDao.delete(s.value).flatMap(_ => Future(Ok))
            case _ => Future(Status(NOT_FOUND)(JsError.toJson(JsError("no product with such id"))))
          }
        })
      }
    }
  }
}