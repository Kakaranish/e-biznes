package controllers

import daos.{OpinionDao, ProductDao}
import javax.inject._
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

@Singleton
class OpinionController @Inject()(cc: ControllerComponents,
                                  productDao: ProductDao,
                                  opinionDao: OpinionDao)
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAllForProduct(productId: String) = Action.async { implicit request =>
    val opinionsResult = opinionDao.getAllForProduct(productId)
    opinionsResult.map(opinions => {
      if (opinions.isEmpty) Ok(s"There are no opinions for product with id $productId")
      else {
        val product = Await.result(productDao.getById(productId), Duration.Inf).get
        Ok(views.html.opinions.opinionsForProduct(product._1, opinions))
      }
    })
  }

  def getById(opinionId: String) = Action.async { implicit request =>
    val opinionResult = opinionDao.getById(opinionId)
    opinionResult.map(opinion => {
      if (opinion == None) Ok(s"There is no opinion with id $opinionId")
      else Ok(views.html.opinions.opinion(opinion.get))
    })
  }

  def create = Action {
    Ok("")
  }

  def update(opinionId: String) = Action {
    Ok("")
  }

  def delete(opinionId: String) = Action {
    Ok("")
  }
}
