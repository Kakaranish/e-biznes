package services

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.AppUser

import scala.concurrent.Future

trait UserService extends IdentityService[AppUser] {
  def save(user: AppUser, loginInfo: LoginInfo): Future[AppUser]

  def createOrUpdate(user: AppUser, loginInfo: LoginInfo): Future[AppUser]
}