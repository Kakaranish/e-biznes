package controllers

import daos.UserDao
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(cc: ControllerComponents, userDao: UserDao)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val usersResult = userDao.getAll()
    usersResult.map(users => {
      if(users.isEmpty) Ok("There are no users found")
      else Ok(views.html.users.users(users))
    })
  }

  def getById(userId: String) = Action.async { implicit request =>
    val userResult = userDao.getById(userId)
    userResult.map(user => {
      if(user == None) Ok(s"There is no user with id $userId")
      else Ok(views.html.users.user(user.get))
    })
  }

  def createUser = Action {
    Ok("")
  }

  def deleteUser(userId: String) = Action {
    Ok("")
  }

  def updateUser(userId: String) = Action {
    Ok("")
  }
}