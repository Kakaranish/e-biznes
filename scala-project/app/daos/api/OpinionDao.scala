package daos.api

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models.{Opinion, TableDefinitions}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class OpinionDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider)
                             (implicit ec: ExecutionContext)
  extends TableDefinitions {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getById(opinionId: String) = db.run {
    opinionTable.filter(_.id === opinionId)
      .result
      .headOption
  }

  def belongsToUser(opinionId: String, userId: String) = db.run {
    opinionTable.filter(o => o.id === opinionId && o.userId === userId)
      .exists
      .result
  }

  def create(opinion: Opinion) = {
    val id = UUID.randomUUID().toString()
    val toAdd = Opinion(id, opinion.userId, opinion.productId, opinion.content)
    db.run(opinionTable += toAdd).map(_ => toAdd)
  }

  def canBeAddedByUserToProduct(userId: String, productId: String) = db.run {
    for {
      exists <- opinionTable.filter(o => o.productId === productId && o.userId === userId)
          .exists
          .result
      wasBought <- {
        if(exists) DBIO.successful(true)
        else cartItemTable.filter(r => r.productId === productId)
          .joinLeft(cartTable.filter(_.isFinalized === true)).on((x, y) => x.cartId === y.id)
          .exists
          .result
      }
    } yield {
      if(exists) false
      else wasBought
    }
  }

  def delete(opinionId: String) = db.run {
    opinionTable.filter(_.id === opinionId)
      .delete
  }
}
