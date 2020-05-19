package daos

import com.mohiva.play.silhouette.api.LoginInfo

import scala.concurrent.Future

trait LoginInfoDao {
  def saveUserLoginInfo(userID: String, loginInfo: LoginInfo): Future[Unit]

  def getAuthenticationProviders(email: String): Future[Seq[String]]
}
