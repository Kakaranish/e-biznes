package controllers.misc

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class LoginInfoDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val loginInfoTable = TableQuery[LoginInfoTable]
}

@Singleton
class UserLoginInfoDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val userLoginInfoTable = TableQuery[UserLoginInfoTable]
  private val appUserTable = TableQuery[AppUserTable]
  private val loginInfoTable = TableQuery[LoginInfoTable]

  def find(userId: String, providerId: String) = db.run {
    (for {
      ((_, loginInfo), user) <- userLoginInfoTable.filter(r => r.userId === userId)
        .join(loginInfoTable).on(_.loginInfoId === _.id)
        .join(appUserTable).on(_._1.userId === _.id)
      if loginInfo.providerId === providerId
    } yield (user, loginInfo))
      .result
      .headOption
  }
}
