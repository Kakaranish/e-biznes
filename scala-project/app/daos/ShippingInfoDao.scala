package daos

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShippingInfoDao @Inject()(dbConfigProvider: DatabaseConfigProvider, userDao: UserDao)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val userTable = TableQuery[UserTable]
  private val shippingInfoTable = TableQuery[ShippingInfoTable]

  def list(): Future[Seq[(ShippingInfo, Option[User])]] = db.run((for {
    (shippingInfo, user) <- shippingInfoTable joinLeft userTable
  } yield (shippingInfo, user)).result)
}
