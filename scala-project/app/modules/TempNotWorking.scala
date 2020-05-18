package modules

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import controllers.misc._
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait LoginInfoDao {
  def saveUserLoginInfo(userID: String, loginInfo: LoginInfo): Future[Unit]
}

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

trait AppUserDAO {
  def save(user: AppUser): Future[AppUser]

  def find(loginInfo: LoginInfo): Future[Option[AppUser]]
}

@Singleton
class AppUserDAOImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends AppUserDAO with TableDefinitions {
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

//override def saveUserLoginInfo(userID: String, loginInfo: LoginInfo) = {
//  val id = UUID.randomUUID().toString
//  val dbLoginInfo = LoginInfoDb(id, loginInfo.providerKey, loginInfo.providerID)
//
//  val loginInfoAction = {
//  val retrieveLoginInfo = loginInfoTable.filter(info =>
//  info.providerId === loginInfo.providerID && info.providerKey === loginInfo.providerKey)
//  .result.headOption
//  val insertLoginInfo = loginInfoTable.returning(loginInfoTable.map(_.id))
//  .into((info, id) => info.copy(id)) += dbLoginInfo
//  //      .into((info, id) => info.copy(id = Some(id))) NOT SURE ABOUT THIS  LINE
//  for {
//  loginInfoOption <- retrieveLoginInfo
//  _ <- loginInfoOption.map(DBIO.successful(_)).getOrElse(insertLoginInfo)
//} yield {}
//}
//
//  val actions = (for {
//  _ <- loginInfoAction
//  userLoginInfo = UserLoginInfoDb(userID, dbLoginInfo.id)
//  _ <- userLoginInfoTable += userLoginInfo
//} yield ()).transactionally
//
//  db.run(actions)
//}