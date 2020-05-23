package daos.api

package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider,
                           productDao: ProductDaoApi)
                          (implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getById(cartId: String) = db.run {
    cartTable.filter(_.id === cartId)
      .result
      .headOption
  }

  def getByUserId(userId: String) = db.run {
    cartTable.filter(record => record.userId === userId && record.isFinalized === false)
      .result
      .headOption
  }

  def getOrCreateByUserId(userId: String) = {
    val getCartQuery = cartTable.filter(record => record.userId === userId && record.isFinalized === false)
      .result
      .headOption
    db.run(getCartQuery).flatMap { c =>
      c match {
        case Some(cart) => Future(cart)
        case None => createForUser(userId)
      }
    }
  }

  def createForUser(userId: String) = {
    val id = UUID.randomUUID().toString()
    val nowIso = new DateTime().toString(DateTimeFormat
      .forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
    val cartToAdd = Cart(id, userId, false, nowIso)

    val action = cartTable += cartToAdd
    db.run(action).map(_ => cartToAdd)
  }

  def setFinalized(cartId: String) = db.run {
    (for {
      cartItems <- cartItemTable.filter(_.cartId === cartId).result
      _ <- DBIO.sequence(cartItems.map(ci => productDao.subtractAmt(ci.productId, ci.quantity)))
      _ <- cartTable.filter(record => record.id === cartId)
        .map(record => record.isFinalized)
        .update(true)
      now = new DateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
      _ <- cartTable.filter(record => record.id === cartId)
        .map(record => (record.updateDate))
        .update(now)
    } yield ()).transactionally
  }

  def setUpdateDateToNow(cartId: String) = db.run {
    val nowIso = new DateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
    cartTable.filter(record => record.id === cartId)
      .map(record => record.updateDate)
      .update(nowIso)
  }

  def update(cartToUpdate: Cart) = db.run {
    cartTable.filter(record => record.id === cartToUpdate.id)
      .update(cartToUpdate)
  }
}