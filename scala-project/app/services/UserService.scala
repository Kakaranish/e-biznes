package services

import com.mohiva.play.silhouette.api.services.IdentityService
import models.AppUser

import scala.concurrent.Future

trait UserService extends IdentityService[AppUser] {
  def save(user: AppUser): Future[AppUser]
}