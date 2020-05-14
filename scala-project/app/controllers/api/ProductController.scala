package controllers.api

import daos.{CategoryDao, ProductDao}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{MessagesControllerComponents, _}

import scala.concurrent.ExecutionContext

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
          "category" -> prod._2.getOrElse(null)
        ))
      }
      case _ => Status(NOT_FOUND)(JsError.toJson(JsError("not found")))
    })
  }
}