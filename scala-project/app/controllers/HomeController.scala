package controllers

import java.util.concurrent.TimeUnit
import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import javax.inject._
import play.api.libs.json.{JsObject, JsString, Json}
import play.api.mvc._
import com.github.t3hnar.bcrypt._

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  case class Person(firstName: String,
                    lastName: String,
                    age: Int)
  object Person {
    implicit val personFormat = Json.format[Person]
  }

  // Auth section

  private val tokenExpiryPeriodInDays = 1
  private val secretKey = "awesomeSecretKey"
  private val header = JwtHeader("HS256")

  def setClaims(username: String, expiryPeriodInDays: Long) = JwtClaimsSet(
    Map(
      "user" -> username,
      "expiredAt" -> (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(expiryPeriodInDays))
    )
  )

  def index = Action(parse.json) { implicit request =>
    val jwt = JsonWebToken(header, setClaims("sgruz", 1), secretKey)
    Ok(JsObject(
      Seq(
        "jwt" -> JsString(jwt)
      )
    ))
  }

  def index2 = Action(parse.json) { implicit request =>
    val message = (request.body \ "message").asOpt[String]
    val password = (request.body \ "password").asOpt[String]

    val salt = "$2a$10$8K1p/a0dL1LXMIgoEDFrwO"
    val encryptedPassword = password.get.bcrypt(salt)

    val validPassword = "testPassword"
    val passwordValid = validPassword.bcrypt(salt) == encryptedPassword

    val response = JsObject(
      Seq(
        "message" -> JsString("Hello world"),
        "person" -> Json.toJson(Person("Stanislaw", "Gruz", 22)),
        "encryptedPassword" -> JsString(encryptedPassword),
        "isPasswordValid" -> Json.toJson(passwordValid)
      )
    )
    Ok(response)
  }
}
