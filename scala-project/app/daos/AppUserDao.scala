package daos

import com.mohiva.play.silhouette.api.LoginInfo
import models.{AppUser, AppUserDb}

import scala.concurrent.Future

trait AppUserDao {
  def save(user: AppUser): Future[AppUser]

  def update(user: AppUser): Future[AppUser]

  def find(loginInfo: LoginInfo): Future[Option[AppUser]]
}
