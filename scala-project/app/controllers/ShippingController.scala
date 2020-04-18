package controllers

import daos.ShippingInfoDao
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class ShippingController @Inject()(cc: ControllerComponents, shippingInfoDao: ShippingInfoDao)
                                  (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val shippingInfosResult = shippingInfoDao.getAll()
    shippingInfosResult.map(shippingInfos => {
      if(shippingInfos.isEmpty) Ok("There are no shipping infos found")
      else Ok(views.html.shippingInfos.shippingInfos(shippingInfos))
    })
  }

  def getById(shippingInfoId: String) = Action.async { implicit request =>
    val shippingInfoResult = shippingInfoDao.getById(shippingInfoId)
    shippingInfoResult.map(shippingInfo => {
      if(shippingInfo == None) Ok(s"There is no shipping info with id $shippingInfoId")
      else Ok(views.html.shippingInfos.shippingInfo(shippingInfo.get))
    })
  }

  def create = Action {
    Ok("")
  }

  def update(shippingInfoId: String) = Action {
    Ok("")
  }

  def delete(shippingInfoId: String) = Action {
    Ok("")
  }
}
