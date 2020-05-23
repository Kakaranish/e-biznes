package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Category(id: String,
                    name: String,
                    isDeleted: Boolean)

object Category {
  implicit val categoryFormat = Json.format[Category]
}

class CategoryTable(tag: Tag) extends Table[Category](tag, "Category") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def name = column[String]("Name", O.Unique)

  def isDeleted = column[Boolean]("IsDeleted");

  override def * = (id, name, isDeleted) <> ((Category.apply _).tupled, Category.unapply)
}