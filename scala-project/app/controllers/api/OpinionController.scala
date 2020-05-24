package controllers.api

import com.mohiva.play.silhouette.api.Silhouette
import daos.api.{OpinionDaoApi, ProductDaoApi}
import play.api.libs.functional.syntax._
import javax.inject.{Inject, Singleton}
import models.Opinion
import play.api.libs.json.{JsError, JsPath, JsSuccess, Json, JsonValidationError}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}
import silhouette.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OpinionControllerApi @Inject()(cc: MessagesControllerComponents,
                                  silhouette: Silhouette[DefaultEnv],
                                  productDao: ProductDaoApi,
                                  opinionDao: OpinionDaoApi)
                                 (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def create() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val createOpinionRead = (
        (JsPath \ "productId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty) and
          (JsPath \ "content").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)
        ) (CreateOpinionRequest.apply _)

    val validation = request.body.validate[CreateOpinionRequest](createOpinionRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[CreateOpinionRequest] => {
        val data = s.value
        productDao.getById(data.productId).flatMap(product => {
          product match {
            case Some(_) => {
              opinionDao.canBeAddedByUserToProduct(request.identity.id, data.productId).flatMap(canBeAdded => {
                if(!canBeAdded) Future(Status(BAD_REQUEST)("opinion cannot be added"))
                else {
                  val toAdd = Opinion(null, request.identity.id, data.productId, data.content)
                  opinionDao.create(toAdd).flatMap(opinion => Future(Ok(Json.toJson(opinion))))
                }
              })
            }
            case _ => Future(Status(NOT_FOUND)(JsError.toJson(JsError("no product with such id"))))
          }
        })
      }
    }
  }

  def delete() = silhouette.SecuredAction.async(parse.json) { implicit request =>
    implicit val deleteOpinionRead =
      (JsPath \ "opinionId").read[String].filter(JsonValidationError("cannot be empty"))(x => x != null && !x.isEmpty)

    val validation = request.body.validate[String](deleteOpinionRead)
    validation match {
      case e: JsError => Future(Status(BAD_REQUEST)(JsError.toJson(e)))
      case s: JsSuccess[String] => {
        val opinionId = s.value
        opinionDao.belongsToUser(opinionId, request.identity.id).flatMap(belongs => {
          if(!belongs) Future(Status(BAD_REQUEST)("there is no such opinion for given user"))
          else opinionDao.delete(opinionId).flatMap(_ => Future(Ok))
        })
      }
    }
  }

  case class CreateOpinionRequest(productId: String, content: String)
}