package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Opinion(id: String,
                   userId: String,
                   productId: String,
                   content: String)

object Opinion {
  implicit val opinionFormat = Json.format[Opinion]
}

class OpinionTable(tag: Tag) extends Table[Opinion](tag, "Opinion") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)
  def userId = column[String]("UserId")
  def productId = column[String]("ProductId")
  def content = column[String]("Content")

  def userFK = foreignKey("user_fk", userId, TableQuery[UserTable])(_.id)
  def productFK = foreignKey("product_fk", productId, TableQuery[ProductTable])(_.id)

  override def * = (id, userId, productId, content) <> ((Opinion.apply _).tupled, Opinion.unapply)
}
