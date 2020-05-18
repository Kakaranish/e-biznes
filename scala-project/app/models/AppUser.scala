package models

import java.util.UUID

import com.mohiva.play.silhouette.api.Identity

case class AppUser(id: String = UUID.randomUUID.toString,
                   email: String,
                   firstName: String,
                   lastName: String) extends Identity
