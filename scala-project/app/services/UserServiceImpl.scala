package services

import com.mohiva.play.silhouette.api.LoginInfo
import daos.{AppUserDao, LoginInfoDao}
import javax.inject.Inject
import models.AppUser

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl @Inject()(appUserDao: AppUserDao,
                                loginInfoDao: LoginInfoDao)
                               (implicit ec: ExecutionContext) extends UserService {

  override def retrieve(loginInfo: LoginInfo): Future[Option[AppUser]] = {
    appUserDao.find(loginInfo)
  }

  def save(user: AppUser, loginInfo: LoginInfo) = {
    for {
      savedUser <- appUserDao.save(user)
      _ <- loginInfoDao.saveUserLoginInfo(savedUser.id, loginInfo)
    } yield savedUser
  }

  def createOrUpdate(user: AppUser, loginInfo: LoginInfo): Future[AppUser] = {
    appUserDao.find(loginInfo).flatMap {
      case Some(_) => appUserDao.update(user)
      case None => save(user, loginInfo)
    }
  }
}