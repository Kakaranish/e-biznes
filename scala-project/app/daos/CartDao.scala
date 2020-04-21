package daos

import java.util.UUID

import javax.inject.{Inject, Singleton}
import models._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class CartDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val cartTable = TableQuery[CartTable]
  private val userTable = TableQuery[UserTable]

  def getById(cartId: String) = db.run {
    (for {
      (cart, user) <- cartTable joinLeft
        userTable on ((x, y) => x.userId === y.id)
    } yield (cart, user))
      .filter(record => record._1.id === cartId)
      .result
      .headOption
  }

  def create(cart: Cart) = db.run {
    val id = UUID.randomUUID().toString()
    cartTable += Cart(id, cart.userId, cart.isFinalized, cart.updateDate)
  }

  def createWithId(cart: Cart) = db.run {
    cartTable += Cart(cart.id, cart.userId, cart.isFinalized, cart.updateDate)
  }

  def update(cartToUpdate: Cart) = db.run {
    cartTable.filter(record => record.id === cartToUpdate.id)
      .update(cartToUpdate)
  }

  def setUpdateDateToNow(cartId: String) = db.run {
    val nowIso = new DateTime().toString(DateTimeFormat
      .forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
    cartTable.filter(record => record.id === cartId)
      .map(record => (record.updateDate))
      .update(nowIso)
  }

  def setFinalized(cartId: String) = db.run {
    cartTable.filter(record => record.id === cartId)
      .map(record => (record.isFinalized))
      .update((true))
  }

    def delete(cartId: String) = db.run {
    cartTable.filter(record => record.id === cartId)
      .delete
  }
}