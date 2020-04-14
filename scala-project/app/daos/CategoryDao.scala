package daos

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class CategoryDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "daos.Category") {
    def id = column[String]("id", O.PrimaryKey, O.Unique)

    def name = column[String]("name", O.Unique)

    override def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  lazy val category = TableQuery[CategoryTable]

  def create(name: String): Future[Category] = db.run {
    (category.map(c => c.name)
      returning category.map(_.id)
      into ((name, id) => Category(id, name))
      ) += (name);
  }

  def list(): Future[Seq[Category]] = db.run(category.result)
}