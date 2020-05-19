package services

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.AppUser

import scala.concurrent.Future

trait UserService extends IdentityService[AppUser] {
  def saveOrUpdate(user: AppUser, loginInfo: LoginInfo): Future[AppUser]
}