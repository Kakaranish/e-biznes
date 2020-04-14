package daos

import play.api.libs.json.Json

case class Category(id: String, name: String)

object Category {
  implicit val categoryFormat = Json.format[Category]
}
