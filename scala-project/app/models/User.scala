package models

import play.api.libs.json.Json

case class User(id: String, email: String, password: String, firstName: String, lastName: String)

object User {
  implicit val userFormat = Json.format[User]
}
