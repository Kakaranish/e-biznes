package controllers.misc

import com.mohiva.play.silhouette.api
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl @Inject()(implicit ec: ExecutionContext) extends UserService {
  override def retrieve(loginInfo: api.LoginInfo): Future[Option[AppUser]] = {
    Future(Option(null))
  }
}