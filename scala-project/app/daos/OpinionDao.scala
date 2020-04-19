package daos

import java.util.UUID
import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OpinionDao @Inject()(dbConfigProvider: DatabaseConfigProvider, userDao: UserDao, productDao: ProductDao)
                          (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val opinionTable = TableQuery[OpinionTable]
  private val userTable = TableQuery[UserTable]
  private val productTable = TableQuery[ProductTable]

  def getAllForProduct(productId: String) = db.run((for {
    ((opinion, user), product) <- opinionTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((opinion, user), product))
    .filter(record => record._1._1.productId === productId)
    .result
  )

  def getById(opinionId: String): Future[Option[((Opinion, Option[User]), Option[Product])]] = db.run((for {
    ((opinion, user), product) <- opinionTable joinLeft
      userTable on ((x, y) => x.userId === y.id) joinLeft
      productTable on ((x, y) => x._1.productId === y.id)
  } yield ((opinion, user), product))
    .filter(record => record._1._1.id === opinionId)
    .result
    .headOption
  )

  def create(opinion: Opinion) = db.run {
    val id = UUID.randomUUID().toString()
    opinionTable += Opinion(id, opinion.userId, opinion.productId, opinion.content)
  }

  def update(opinionToUpdate: Opinion) = db.run {
    opinionTable.filter(record => record.id === opinionToUpdate.id)
      .update(opinionToUpdate)
  }

  def delete(opinionId: String) = db.run {
    opinionTable.filter(record => record.id === opinionId)
      .delete
  }
}