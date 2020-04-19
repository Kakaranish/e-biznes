package controllers

import daos.UserDao
import javax.inject._
import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(cc: MessagesControllerComponents, userDao: UserDao)
                              (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll() = Action.async { implicit request =>
    val usersResult = userDao.getAll()
    usersResult.map(users => {
      if (users.isEmpty) Ok("There are no users found")
      else Ok(views.html.users.users(users))
    })
  }

  def getById(userId: String) = Action.async { implicit request =>
    val userResult = userDao.getById(userId)
    userResult.map(user => {
      if (user == None) Ok(s"There is no user with id $userId")
      else Ok(views.html.users.user(user.get))
    })
  }

  def create = Action { implicit request =>
    Ok(views.html.users.createUser(createForm, Seq.empty[String]))
  }

  def delete(userId: String) = Action {
    Ok("")
  }

  def update(userId: String) = Action {
    Ok("")
  }

  // Forms

  val createForm = Form {
    mapping(
      "email" -> email,
      "password" -> nonEmptyText,
      "confirmedPassword" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText
    )(CreateUserForm.apply)(CreateUserForm.unapply).verifying(
      "Password are different",
      fields =>
        fields match {
          case createFormData => createFormData.password == createFormData.confirmedPassword
        }
    )
  }

  val updateForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "email" -> email,
      "password" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText
    )(UpdateUserForm.apply)(UpdateUserForm.unapply)
  }

  // Handlers

  val createHandler = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.users.createUser(errorForm, errorForm.errors.map(x => x.message)))
        )
      },
      createForm => {
        val user = User(null, createForm.email, createForm.password,
          createForm.firstName, createForm.lastName)
        userDao.create(user).map(_ =>
          Redirect(routes.UserController.create())
            .flashing("success" -> "User created.")
        )
      }
    )
  }

  //  val updateHandler = Action.async { implicit request =>
  //    updateForm.bindFromRequest().fold(
  //      errorForm => {
  //        Future.successful(
  //          BadRequest(views.html.index)
  //        )
  //      },
  //      updateForm => {
  //        // Placeholder for logic
  //        Future.successful(
  //          BadRequest(views.html.index)
  //        )
  //      }
  //    )
  //  }
}

case class CreateUserForm(email: String,
                          password: String,
                          confirmedPassword: String,
                          firstName: String,
                          lastName: String)

case class UpdateUserForm(id: String,
                          email: String,
                          password: String,
                          firstName: String,
                          lastName: String)