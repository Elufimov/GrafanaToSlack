package models

import play.api.libs.json._

/**
 * Created by elufimov on 01/03/16.
 */
case class Response(transformed: List[Transformed], sended: List[Sended])

object Response {
  implicit val transformedFormat = Json.format[Transformed]
  implicit val sendedFormat = Json.format[Sended]
  implicit val responseFormat = Json.format[Response]
}
case class Transformed(transformed: String)
object Transformed {
  implicit val transformedFormat = Json.format[Transformed]
}
case class Sended(channel: String, code: Int, result: String)

object Sended {
  implicit val sendedFormat = Json.format[Sended]
}