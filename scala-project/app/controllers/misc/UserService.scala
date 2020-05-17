package controllers.misc

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService

import scala.concurrent.Future

case class SignUp(email: String, firstName: String, lastName: String, password: String)

trait UserService extends IdentityService[AppUser] {
  def save(user: AppUser): Future[AppUser]

  //  def retrieveUserLoginInfo(id: String, providerID: String): Future[Option[(AppUser, LoginInfoDb)]]
//
//  def createOrUpdate(loginInfo: LoginInfo, email: String, firstName: String, lastName: String)
}


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
