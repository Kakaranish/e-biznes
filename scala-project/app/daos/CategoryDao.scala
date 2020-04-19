package daos

import java.util.UUID
import javax.inject.Inject
import models.{Category, CategoryTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

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

  def create(name: String) = db.run {
    val id = UUID.randomUUID().toString()
    categoryTable += Category(id, name)
  }

  def update(categoryToUpdate: Category) = db.run {
    categoryTable.filter(record => record.id === categoryToUpdate.id)
      .update(categoryToUpdate)
  }
}