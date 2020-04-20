package controllers

import daos.ShippingInfoDao
import javax.inject.{Inject, Singleton}
import models.ShippingInfo
import play.api.mvc.{AbstractController, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class ShippingController @Inject()(cc: MessagesControllerComponents,
                                   shippingInfoDao: ShippingInfoDao)
                                  (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val shippingInfosResult = shippingInfoDao.getAll()
    shippingInfosResult.map(shippingInfos => {
      if (shippingInfos.isEmpty) Ok("There are no shipping infos found")
      else Ok(views.html.shippingInfos.shippingInfos(shippingInfos))
    })
  }

  def getById(shippingInfoId: String) = Action.async { implicit request =>
    val shippingInfoResult = shippingInfoDao.getById(shippingInfoId)
    shippingInfoResult.map(shippingInfo => {
      if (shippingInfo == None) Ok(s"There is no shipping info with id $shippingInfoId")
      else Ok(views.html.shippingInfos.shippingInfo(shippingInfo.get))
    })
  }

  def create = Action { implicit request =>
    Ok(views.html.shippingInfos.createShippingInfo(createForm))
  }

  def update(shippingInfoId: String) = Action { implicit request =>
    val shippingInfoResult = Await.result(shippingInfoDao.getById(shippingInfoId), Duration.Inf)
    if(shippingInfoResult == None) Ok(s"There is no shipping info with id $shippingInfoId")

    val shippingInfo = shippingInfoResult.get
    val updateFormToPass = updateForm.fill(UpdateShippingInfoForm(shippingInfo.id,
      shippingInfo.country, shippingInfo.city, shippingInfo.address,
      shippingInfo.zipOrPostcode))
    Ok(views.html.shippingInfos.updateShippingInfo(updateFormToPass))
  }

  def delete(shippingInfoId: String) = Action.async { implicit request =>
    val deleteResult = shippingInfoDao.delete(shippingInfoId)
    deleteResult.map(result => {
      if(result != 0) Ok(s"Shipping info with id $shippingInfoId has been deleted")
      else Ok(s"There is no shipping info with id $shippingInfoId")
    })
  }

  // Forms

  val createForm = Form {
    mapping(
      "country" -> nonEmptyText,
      "city" -> nonEmptyText,
      "address" -> nonEmptyText,
      "zipOrPostcode" -> nonEmptyText
    )(CreateShippingInfoForm.apply)(CreateShippingInfoForm.unapply)
  }

  val updateForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "country" -> nonEmptyText,
      "city" -> nonEmptyText,
      "address" -> nonEmptyText,
      "zipOrPostcode" -> nonEmptyText
    )(UpdateShippingInfoForm.apply)(UpdateShippingInfoForm.unapply)
  }

  // Handlers

  val createHandler = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.shippingInfos.createShippingInfo(errorForm))
        )
      },
      createForm => {
        val shippingInfo = ShippingInfo(null, createForm.country, createForm.city,
          createForm.address, createForm.zipOrPostcode)
        shippingInfoDao.create(shippingInfo).map(_ =>
          Redirect(routes.ShippingController.create())
            .flashing("success" -> "Shipping info created.")
        )
      }
    )
  }

  val updateHandler = Action.async { implicit request =>
    updateForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.shippingInfos.updateShippingInfo(errorForm))
        )
      },
      updateForm => {
        val shippingInfo = ShippingInfo(updateForm.id, updateForm.country,
        updateForm.city, updateForm.address, updateForm.zipOrPostcode)
        shippingInfoDao.update(shippingInfo).map(_ =>
          Redirect(routes.ShippingController.create())
            .flashing("success" -> "Shipping info updated.")
        )
      }
    )
  }
}

case class CreateShippingInfoForm(country: String,
                                  city: String,
                                  address: String,
                                  zipOrPostcode: String)

case class UpdateShippingInfoForm(id: String,
                                  country: String,
                                  city: String,
                                  address: String,
                                  zipOrPostcode: String)