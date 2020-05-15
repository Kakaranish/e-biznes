package controllers.misc

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService

import scala.concurrent.Future

case class SignUp(email: String, firstName: String, lastName: String, password: String)

trait UserService extends IdentityService[AppUser]

// class UserService extends IdentityService[AppUser] {
//   override def retrieve(loginInfo: LoginInfo): Future[Option[AppUser]] = {
//     Ok
//   }

//   def create(loginInfo: LoginInfo, signUp: SignUp) = {
//     Future.successful(
//       AppUser(
//         email = signUp.email,
//         firstName = signUp.firstName,
//         lastName = signUp.lastName,
//         password = signUp.password,
//         loginInfo = loginInfo)
//     )
//   }
// }
