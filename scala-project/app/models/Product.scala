package models

import play.api.libs.json.Json
import slick.jdbc.SQLiteProfile.api._

case class Product(id: String,
                   name: String,
                   description: String,
                   price: Float,
                   quantity: Int,
                   categoryId: String,
                   isDeleted: Boolean)

case class ProductPreview(id: String, name: String)

object ProductPreview {
  implicit val productPreviewFormat = Json.format[ProductPreview]
}

object Product {
  implicit val productFormat = Json.format[Product]
}

class ProductTable(tag: Tag) extends Table[Product](tag, "Product") {
  def id = column[String]("Id", O.PrimaryKey, O.Unique)

  def name = column[String]("Name")

  def description = column[String]("Description")

  def price = column[Float]("Price")

  def quantity = column[Int]("Quantity")

  def categoryId = column[String]("CategoryId")

  def isDeleted = column[Boolean]("IsDeleted")

  def category_fk = foreignKey("category_fk", categoryId, TableQuery[CategoryTable])(_.id)

  override def * = (id, name, description, price, quantity, categoryId, isDeleted) <> ((Product.apply _).tupled, Product.unapply)
}
