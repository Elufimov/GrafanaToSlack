package models

import java.sql.Timestamp
import java.util.UUID

/**
 * Created by elufimov on 11/02/16.
 */
case class ImageModel(
  uuid: Option[UUID],
  array: Array[Byte],
  timestamp: Timestamp
)
