package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class User(id: String, email: String, password: String, firstName: String, lastName: String)

object User {
  implicit val userFormat = Json.format[User]
}

class UserTable(tag: Tag) extends Table[User](tag, "User") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def email = column[String]("Email", O.Unique)

  def password = column[String]("Password")

  def firstName = column[String]("FirstName")

  def lastName = column[String]("LastName")

  def * = (id, email, password, firstName, lastName) <> ((User.apply _).tupled, User.unapply)
}
