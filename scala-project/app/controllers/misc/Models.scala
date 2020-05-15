package controllers.misc

import java.util.UUID

import com.mohiva.play.silhouette.api.Identity
import slick.jdbc.SQLiteProfile.api._

case class  AppUser(id: String = UUID.randomUUID.toString,
                   email: String,
                   firstName: String,
                   lastName: String,
                   password: String) extends Identity

class AppUserTable(tag: Tag) extends Table[AppUser](tag, "AppUser") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def email = column[String]("Email", O.Unique)

  def password = column[String]("Password")

  def firstName = column[String]("FirstName")

  def lastName = column[String]("LastName")

  override def * = (id, email, password, firstName, lastName) <> ((AppUser.apply _).tupled, AppUser.unapply)
}

case class LoginInfo(id: String,
                     providerId: String,
                     providerKey: String)

class LoginInfoTable(tag: Tag) extends Table[LoginInfo](tag, "LoginInfo") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def providerId = column[String]("ProviderId")

  def providerKey = column[String]("ProviderKey")

  override def * = (id, providerId, providerKey) <> ((LoginInfo.apply _).tupled, LoginInfo.unapply)
}

case class UserLoginInfo(userId: String, loginInfoId: String)

class UserLoginInfoTable(tag: Tag) extends Table[UserLoginInfo](tag, "UserLoginInfo") {
  def userId = column[String]("UserId")

  def loginInfoId = column[String]("LoginInfoId")

  override def * = (userId, loginInfoId) <> ((UserLoginInfo.apply _).tupled, UserLoginInfo.unapply)
}