package daos

import java.util.UUID

import javax.inject.Inject
import models.{Category, CategoryTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext

class CategoryDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val categoryTable = TableQuery[CategoryTable]

  def getAll() = db.run {
    categoryTable.result
  }

  def getById(id: String) = db.run {
    categoryTable.filter(category => category.id === id)
      .result
      .headOption
  }

  def getByName(name: String) = db.run {
    categoryTable.filter(category => category.name === name)
      .result
      .headOption
  }

  def create(category: Category) = db.run {
    val id = UUID.randomUUID().toString()
    categoryTable += Category(id, category.name, false)
  }

  def update(categoryToUpdate: Category) = db.run {
    categoryTable.filter(record => record.id === categoryToUpdate.id)
      .update(categoryToUpdate)
  }

  def delete(categoryId: String) = db.run {
    categoryTable.filter(record => record.id === categoryId)
      .delete
  }
}