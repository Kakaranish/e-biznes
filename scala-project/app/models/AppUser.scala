package models

import java.util.UUID

import com.mohiva.play.silhouette.api.Identity
import play.api.libs.json.Json

case class AppUser(id: String = UUID.randomUUID.toString,
                   email: String,
                   firstName: String,
                   lastName: String,
                   role: String) extends Identity
object AppUser {
  implicit val appUserFormat = Json.format[AppUser]
}