package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ProductDao @Inject()(dbConfigProvider: DatabaseConfigProvider, categoryDao: CategoryDao)
                          (implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val productTable = TableQuery[ProductTable]
  private val categoryTable = TableQuery[CategoryTable]

  def list() = db.run((for {
    (product, category) <- productTable joinLeft categoryTable
  } yield (product, category)).result)
}
