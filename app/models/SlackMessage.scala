package models

import play.api.libs.json._

/**
 * Created by elufimov on 25/02/16.
 */
trait SlackMessage {
  val icon_emoji: Option[String]
  val text: String
  val username: Option[String]
  val attachments: Option[List[Attachment]]
}
case class SlackMessageSingle(
  username: Option[String],
  icon_emoji: Option[String],
  text: String,
  channel: String,
  attachments: Option[List[Attachment]]
) extends SlackMessage

object SlackMessageSingle {
  implicit val attachmentFieldFormat = Json.format[AttachmentField]
  implicit val attachmentFormat = Json.format[Attachment]
  implicit val slackMessageSingleFormat = Json.format[SlackMessageSingle]
}

case class SlackMessageMultiple(
  username: Option[String],
  icon_emoji: Option[String],
  text: String,
  channels: Seq[String],
  attachments: Option[List[Attachment]]
) extends SlackMessage

object SlackMessageMultiple {
  implicit val attachmentFieldFormat = Json.format[AttachmentField]
  implicit val attachmentFormat = Json.format[Attachment]
  implicit val slackMessageMultipleFormat = Json.format[SlackMessageMultiple]
}

case class Attachment(
  fallback: Option[String],
  color: Option[String],
  pretext: Option[String],
  author_name: Option[String],
  author_link: Option[String],
  author_icon: Option[String],
  title: Option[String],
  title_link: Option[String],
  text: Option[String],
  fields: Option[Seq[AttachmentField]],
  image_url: Option[String],
  thumb_url: Option[String]
)

object Attachment {
  implicit val AttachmentFieldFormat = Json.format[AttachmentField]
  implicit val AttachmentFormat = Json.format[Attachment]
}

case class AttachmentField(
  title: String,
  value: String,
  short: Boolean
)

object AttachmentField {
  implicit val AttachmentFieldFormat = Json.format[AttachmentField]
}
