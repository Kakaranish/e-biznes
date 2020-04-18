package daos

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

  def create(name: String): Future[Category] = db.run {
    (categoryTable.map(c => c.name)
      returning categoryTable.map(_.id)
      into ((name, id) => Category(id, name))
      ) += (name);
  }

  def getAll() = db.run {
    categoryTable.result
  }

  def getWithId(id: String) = db.run {
    categoryTable.filter(category => category.id === id).result.headOption
  }
}