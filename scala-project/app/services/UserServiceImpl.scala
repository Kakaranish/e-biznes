package services

import com.mohiva.play.silhouette.api.LoginInfo
import daos.{AppUserDao, LoginInfoDao}
import javax.inject.Inject
import models.UserIdentity

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl @Inject()(appUserDao: AppUserDao,
                                loginInfoDao: LoginInfoDao)
                               (implicit ec: ExecutionContext) extends UserService {

  override def retrieve(loginInfo: LoginInfo): Future[Option[UserIdentity]] = {
    appUserDao.find(loginInfo)
  }

  def saveOrUpdate(user: UserIdentity, loginInfo: LoginInfo): Future[UserIdentity] = {
    appUserDao.find(loginInfo).flatMap {
      case Some(_) => appUserDao.update(user)
      case None => save(user, loginInfo)
    }
  }

  def save(user: UserIdentity, loginInfo: LoginInfo) = {
    for {
      savedUser <- appUserDao.save(user)
      _ <- loginInfoDao.saveUserLoginInfo(savedUser.id, loginInfo)
    } yield savedUser
  }
}