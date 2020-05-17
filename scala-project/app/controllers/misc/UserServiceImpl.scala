package controllers.misc

import java.util.UUID

import com.mohiva.play.silhouette.api
import com.mohiva.play.silhouette.api.LoginInfo
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl @Inject()(appUserDao: AppUserDAOImpl)
                               (implicit ec: ExecutionContext) extends UserService {

  override def retrieve(loginInfo: LoginInfo): Future[Option[AppUser]] = ???

  def save(user: AppUser) = {
    appUserDao.save(user)
  }
//  override def retrieve(loginInfo: api.LoginInfo) = {
//    Future(Option(null))
//  }
//
//  override def retrieveUserLoginInfo(id: String, providerID: String) = {
//    //    userLoginInfoDao.find(id, providerID)
//    Future(Option(null))
//  }

//  override def createOrUpdate(loginInfo: LoginInfo, email: String, firstName: String, lastName: String) = {
//    Future.sequence(Seq(
//      appUserDao.find(loginInfo),
//      appUserDao.findByEmail(email)
//    )).flatMap(users => users.flatten.headOption match {
//      case Some(user) => {
//        Future(user)
//      }
//      case None => {
//        appUserDao.save(AppUser(UUID.randomUUID().toString, email,
//          firstName, lastName
//        ))
//      }
//    })
//  }
//  override def createOrUpdate(loginInfo: LoginInfo, email: String, firstName: String, lastName: String): Unit = ???
}