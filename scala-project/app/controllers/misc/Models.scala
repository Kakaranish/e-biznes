package controllers.misc

import java.util.UUID

import com.mohiva.play.silhouette.api.Identity
import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._
import slick.lifted.{TableQuery, Tag}


case class AppUser(id: String = UUID.randomUUID.toString,
                   email: String,
                   firstName: String,
                   lastName: String) extends Identity

// ---  DB MODELS  -----------------------------------------------------------------

case class AppUserDb(id: String = UUID.randomUUID.toString,
                   email: String,
                   firstName: String,
                   lastName: String)

class AppUserTable(tag: Tag) extends Table[AppUserDb](tag, "AppUser") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def email = column[String]("Email", O.Unique)

  def firstName = column[String]("FirstName")

  def lastName = column[String]("LastName")

  override def * = (id, email, firstName, lastName) <> ((AppUserDb.apply _).tupled, AppUserDb.unapply)
}

case class LoginInfoDb(id: String,
                       providerId: String,
                       providerKey: String)

class LoginInfoTable(tag: Tag) extends Table[LoginInfoDb](tag, "LoginInfo") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def providerId = column[String]("ProviderId")

  def providerKey = column[String]("ProviderKey")

  override def * = (id, providerId, providerKey) <> ((LoginInfoDb.apply _).tupled, LoginInfoDb.unapply)
}

case class UserLoginInfoDb(userId: String, loginInfoId: String)

class UserLoginInfoTable(tag: Tag) extends Table[UserLoginInfoDb](tag, "AppUserLoginInfo") {
  def userId = column[String]("UserId")

  def loginInfoId = column[String]("LoginInfoId")

  override def * = (userId, loginInfoId) <> ((UserLoginInfoDb.apply _).tupled, UserLoginInfoDb.unapply)
}

case class PasswordInfoDb(hasher: String, password: String, salt: Option[String], loginInfoId: String)

object PasswordInfoDb {
  implicit val passwordInfoFormat = Json.format[PasswordInfoDb]
}

class PasswordInfoTable(tag: Tag) extends Table[PasswordInfoDb](tag, "PasswordInfo") {
  def hasher = column[String]("Hasher")

  def password = column[String]("Password")

  def salt = column[Option[String]]("Salt")

  def loginInfoId = column[String]("LoginInfoId")

  def login_info_fk = foreignKey("login_info_fk", loginInfoId, TableQuery[LoginInfoTable])(_.id)

  def * = (hasher, password, salt, loginInfoId) <> ((PasswordInfoDb.apply _).tupled, PasswordInfoDb.unapply)
}

case class TokenDb(id: String, userId: String, expiry: String)

object TokenDb {
  implicit val tokenFormat = Json.format[TokenDb]
}

class TokenTable(tag: Tag) extends Table[TokenDb](tag, "PasswordInfo") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def userId = column[String]("UserId")

  def expiry = column[String]("Expiry")

  def user_fk = foreignKey("user_fk", userId, TableQuery[AppUserTable])(_.id)

  def * = (id, userId, expiry) <> ((TokenDb.apply _).tupled, TokenDb.unapply)
}

trait TableDefinitions {
  val tokenTable = TableQuery[TokenTable]
  val appUserTable = TableQuery[AppUserTable]
  val passwordInfoTable = TableQuery[PasswordInfoTable]
  val loginInfoTable = TableQuery[LoginInfoTable]
  val userLoginInfoTable = TableQuery[UserLoginInfoTable]
}