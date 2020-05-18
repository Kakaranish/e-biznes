package silhouette

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.AppUser

trait DefaultEnv extends Env {
  type I = AppUser
  type A = JWTAuthenticator
}
