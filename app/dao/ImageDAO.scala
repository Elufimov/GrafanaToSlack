package dao

import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

import models.ImageModel
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.PostgresDriver

import scala.concurrent.Future

/**
 * Created by elufimov on 11/02/16.
 */
class ImageDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[PostgresDriver] {
  import driver.api._

  private val Images = TableQuery[ImageTable]

  def insert(image: ImageModel) = db.run(Images returning Images.map(_.uuid) += image)

  def all(): Future[Seq[ImageModel]] = db.run(Images.result)

  def getByUUID(uuid: UUID) = db.run(Images.filter(_.uuid === uuid).result).map(i ⇒ i.head.array)

  private class ImageTable(tag: Tag) extends Table[ImageModel](tag, "IMAGES") {
    def uuid = column[UUID]("UUID", O.PrimaryKey, O.AutoInc)
    def binary = column[Array[Byte]]("IMAGE")
    def timestamp = column[Timestamp]("TIMESTAMP")

    def * = (uuid.?, binary, timestamp) <> (ImageModel.tupled, ImageModel.unapply)
  }

}
