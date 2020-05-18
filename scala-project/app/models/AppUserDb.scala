package models

import java.util.UUID

import slick.jdbc.SQLiteProfile.api._
import slick.lifted.Tag

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