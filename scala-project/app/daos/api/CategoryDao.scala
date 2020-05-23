package daos.api

import java.util.UUID

import javax.inject.Inject
import models.{Category, TableDefinitions}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

class CategoryDaoApi @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends TableDefinitions {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  def getAll() = db.run {
    categoryTable.filter(_.isDeleted === false)
      .result
  }

  def getById(id: String) = db.run {
    categoryTable.filter(c => c.id === id && c.isDeleted === false)
      .result
      .headOption
  }

  def getByName(name: String) = db.run {
    categoryTable.filter(c => c.name === name && c.isDeleted === false)
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
    categoryTable.filter(_.id === categoryId)
      .map(record => (record.isDeleted))
      .update(true);
  }
}