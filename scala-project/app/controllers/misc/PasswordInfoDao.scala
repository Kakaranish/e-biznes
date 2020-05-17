//package controllers.misc
//
//import com.mohiva.play.silhouette.api.LoginInfo
//import com.mohiva.play.silhouette.api.util.PasswordInfo
//import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
//import javax.inject.Inject
//import play.api.db.slick.DatabaseConfigProvider
//import slick.jdbc.JdbcProfile
//import scala.concurrent.{ExecutionContext, Future}
//import scala.reflect.ClassTag
//
//class PasswordInfoDao @Inject()(dbConfigProvider: DatabaseConfigProvider)
//                               (implicit ec: ExecutionContext, val classTag: ClassTag[PasswordInfo])
//  extends DelegableAuthInfoDAO[PasswordInfo] with TableDefinitions {
//
//  val dbConfig = dbConfigProvider.get[JdbcProfile]
//
//  import dbConfig._
//  import profile.api._
//
//  /////////////////////////////////////////////////////////////////////
//  // Helper queries
//
//  def findLoginInfoQuery(loginInfo: LoginInfo) =
//    loginInfoTable.filter(dbLoginInfo => dbLoginInfo.providerId === loginInfo.providerID &&
//      dbLoginInfo.providerKey === loginInfo.providerKey)
//
//  def findPasswordQuery(loginInfo: LoginInfo) = {
//    passwordInfoTable.filter(_.loginInfoId in findLoginInfoQuery(loginInfo).map(_.id))
//  }
//
//  def addAction(loginInfo: LoginInfo, authInfo: PasswordInfo) =
//    findLoginInfoQuery(loginInfo).result.head.flatMap { dbLoginInfo =>
//      passwordInfoTable += PasswordInfoDb(authInfo.hasher, authInfo.password, authInfo.salt, dbLoginInfo.id)
//    }.transactionally
//
//  def updateAction(loginInfo: LoginInfo, passwordInfo: PasswordInfo) = {
//    findPasswordQuery(loginInfo)
//      .map(dbPasswordInfo => (dbPasswordInfo.hasher, dbPasswordInfo.password, dbPasswordInfo.salt))
//      .update((passwordInfo.hasher, passwordInfo.password, passwordInfo.salt))
//  }
//
//  /////////////////////////////////////////////////////////////////////
//
//  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = db.run {
//    findPasswordQuery(loginInfo).result.headOption.map(dbPwdOption => dbPwdOption.map(dbPwd =>
//      PasswordInfo(dbPwd.hasher, dbPwd.password, dbPwd.salt)))
//  }
//
//  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = db.run {
//    addAction(loginInfo, authInfo).map(_ => authInfo)
//  }
//
//  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = db.run {
//    updateAction(loginInfo, authInfo).map(_ => authInfo)
//  }
//
//  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
//    val query = findLoginInfoQuery(loginInfo)
//      .joinLeft(passwordInfoTable).on(_.id === _.loginInfoId)
//    val action = query.result.head.flatMap {
//      case (dbLoginInfo, Some(dbPasswordInfo)) => updateAction(loginInfo, authInfo)
//      case (dbLoginInfo, None) => addAction(loginInfo, authInfo)
//    }
//    db.run(action).map(_ => authInfo)
//  }
//
//  override def remove(loginInfo: LoginInfo): Future[Unit] = db.run {
//    findPasswordQuery(loginInfo).delete.map(_ => ())
//  }
//}
//