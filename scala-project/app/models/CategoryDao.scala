package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class CategoryDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "Category") {
    def id = column[String]("Id", O.PrimaryKey, O.Unique)

    def name = column[String]("Name", O.Unique)

    override def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  val categoryTable = TableQuery[CategoryTable]

  def create(name: String): Future[Category] = db.run {
    (categoryTable.map(c => c.name)
      returning categoryTable.map(_.id)
      into ((name, id) => Category(id, name))
      ) += (name);
  }

  def list(): Future[Seq[Category]] = db.run(categoryTable.result)
}