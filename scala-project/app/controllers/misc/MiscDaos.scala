package controllers.misc

import com.mohiva.play.silhouette.api.LoginInfo
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext


//@Singleton
//class UserLoginInfoDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
//  private val dbConfig = dbConfigProvider.get[JdbcProfile]
//
//  import dbConfig._
//  import profile.api._
//
//  private val userLoginInfoTable = TableQuery[UserLoginInfoTable]
//  private val appUserTable = TableQuery[AppUserTable]
//  private val loginInfoTable = TableQuery[LoginInfoTable]
//
//  def find(userId: String, providerId: String) = db.run {
//    (for {
//      ((_, loginInfo), user) <- userLoginInfoTable.filter(r => r.userId === userId)
//        .join(loginInfoTable).on(_.loginInfoId === _.id)
//        .join(appUserTable).on(_._1.userId === _.id)
//      if loginInfo.providerId === providerId
//    } yield (user, loginInfo))
//      .result
//      .headOption
//  }
//}
//


//class AppUserDAOImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends TableDefinitions {
//  val dbConfig = dbConfigProvider.get[JdbcProfile]
//
//  import dbConfig._
//  import profile.api._
//
//  // Helper
//  def findLoginInfoQuery(loginInfo: LoginInfo) = {
//    loginInfoTable.filter(dbLoginInfo => dbLoginInfo.providerId === loginInfo.providerID &&
//      dbLoginInfo.providerKey === loginInfo.providerKey)
//  }
//
//  def find(loginInfo: LoginInfo) = {
//    val query = for {
//      dbLoginInfo <- findLoginInfoQuery(loginInfo)
//      dbUserLoginInfo <- userLoginInfoTable.filter(_.loginInfoId === dbLoginInfo.id)
//      dbUser <- appUserTable.filter(_.id === dbUserLoginInfo.userId)
//    } yield dbUser
//    db.run(query.result.headOption).map { dbUserOption =>
//      dbUserOption.map { user =>
//        AppUser(user.id, user.firstName, user.lastName, user.email)
//      }
//    }
//  }
//
//  def find(userId: String) = {
//    val query = appUserTable.filter(_.id === userId)
//    db.run(query.result.headOption).map { resultOption =>
//      resultOption.map { result =>
//        AppUser(result.id, result.email, result.firstName, result.lastName)
//      }
//    }
//  }
//
//  def save(user: AppUser) = db.run {
//    val dbUser = AppUserDb(user.id, user.email, user.firstName, user.lastName)
//    appUserTable.insertOrUpdate(dbUser).map(_ => user)
//  }
//
//  def findByEmail(email: String) = db.run {
//    appUserTable.filter(_.email === email).take(1).result.headOption.map(dbUser => dbUser.map { user =>
//      AppUser(user.id, user.firstName, user.lastName, user.email)
//    })
//  }
//}

