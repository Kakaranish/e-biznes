package daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import javax.inject.{Inject, Singleton}
import models.{LoginInfoDb, TableDefinitions, UserLoginInfoDb}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class LoginInfoDaoImpl @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends LoginInfoDao with TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  override def saveUserLoginInfo(userID: String, loginInfo: LoginInfo) = {
    val id = UUID.randomUUID().toString
    val dbLoginInfo = LoginInfoDb(id, loginInfo.providerID, loginInfo.providerKey)

    val actions = for {
      _ <- loginInfoTable += dbLoginInfo
      userLoginInfo = UserLoginInfoDb(userID, dbLoginInfo.id)
      _ <- userLoginInfoTable += userLoginInfo
    } yield ()
    db.run(actions)
  }
}