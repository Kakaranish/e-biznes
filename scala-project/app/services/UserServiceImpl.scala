package services

import com.mohiva.play.silhouette.api.LoginInfo
import daos.AppUserDao
import javax.inject.Inject
import models.AppUser

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl @Inject()(appUserDao: AppUserDao)
                               (implicit ec: ExecutionContext) extends UserService {

  override def retrieve(loginInfo: LoginInfo): Future[Option[AppUser]] = {
    appUserDao.find(loginInfo)
  }

  def save(user: AppUser) = {
    appUserDao.save(user)
  }
}