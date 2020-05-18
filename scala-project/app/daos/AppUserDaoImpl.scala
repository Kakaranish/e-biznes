package daos

import com.mohiva.play.silhouette.api.LoginInfo
import javax.inject.{Inject, Singleton}
import models.{AppUser, AppUserDb, TableDefinitions}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class AppUserDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                              (implicit ec: ExecutionContext)
  extends AppUserDao with TableDefinitions {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def save(user: AppUser) = db.run {
    val dbUser = AppUserDb(user.id, user.email, user.firstName, user.lastName)
    appUserTable.insertOrUpdate(dbUser).map(_ => user)
  }

  def find(loginInfo: LoginInfo) = {
    val query = for {
      dbLoginInfo <- findLoginInfoQuery(loginInfo)
      dbUserLoginInfo <- userLoginInfoTable.filter(_.loginInfoId === dbLoginInfo.id)
      dbUser <- appUserTable.filter(_.id === dbUserLoginInfo.userId)
    } yield dbUser
    db.run(query.result.headOption).map { dbUserOption =>
      dbUserOption.map { user =>
        AppUser(user.id, user.email, user.firstName, user.lastName)
      }
    }
  }

  def findLoginInfoQuery(loginInfo: LoginInfo) = {
    loginInfoTable.filter(dbLoginInfo => dbLoginInfo.providerId === loginInfo.providerID &&
      dbLoginInfo.providerKey === loginInfo.providerKey)
  }
}