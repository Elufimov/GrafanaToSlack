package controllers

import java.sql.Timestamp
import java.util.UUID
import javax.inject._

import dao.ImageDAO
import models._
import play.api.mvc._
import play.api.libs.json._
import services.{ GrafanaService, SlackService }
import utils.Config

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class Application @Inject() (
    grafana: GrafanaService,
    imageDAO: ImageDAO,
    slack: SlackService
) extends Controller {
  def forward() = Action.async(parse.json[SlackMessageMultiple]) { request ⇒
    request.body.attachments match {
      case None ⇒
        val listOfResponse = request.body.channels.map { channel ⇒
          slack.sendMessage(
            SlackMessageSingle(
              username = request.body.username,
              icon_emoji = request.body.icon_emoji,
              text = request.body.text,
              channel = channel,
              attachments = None
            )
          ).map { i ⇒
              Sended(
                channel = channel,
                code = i.status,
                result = i.body
              )
            }
        }.toList
        Future.sequence(listOfResponse).map { i ⇒
          Ok(
            Json.toJson(
              Response(
                transformed = List.empty[Transformed],
                sended = i
              )
            )
          )
        }

      case Some(e) ⇒
        val normalAttachs = e.filter(_.image_url.exists(!_.startsWith(Config.grafanaHost)))
        val transformedAttachs = e.filter(_.image_url.exists(_.startsWith(Config.grafanaHost))).map { attach ⇒
          grafana.downloadImage(attach.image_url.get).flatMap { downloadedFile ⇒
            imageDAO.insert(
              ImageModel(
                uuid = None,
                array = downloadedFile,
                timestamp = new Timestamp(new java.util.Date().getTime())
              )
            )
          }.map { uuid ⇒
            attach.copy(image_url = Some(s"${Config.url}:${Config.port}/image/${uuid.toString}"))
          }
        }

        Future.sequence(transformedAttachs).flatMap { list ⇒
          val listOfResponse = request.body.channels.map { channel ⇒
            slack.sendMessage(
              SlackMessageSingle(
                username = request.body.username,
                icon_emoji = request.body.icon_emoji,
                text = request.body.text,
                channel = channel,
                attachments = Some(normalAttachs ++ list)
              )
            ).map { i ⇒
                Sended(
                  channel = channel,
                  code = i.status,
                  result = i.body
                )
              }
          }.toList
          Future.sequence(listOfResponse).map { i ⇒
            Ok(
              Json.toJson(
                Response(
                  transformed = list.map { j ⇒
                    Transformed(
                      transformed = j.image_url.get
                    )
                  },
                  sended = i
                )
              )
            )
          }
        }
    }
  }

  def image(uuid: UUID) = Action.async { request ⇒
    imageDAO.getByUUID(uuid).map { image ⇒
      Ok(image).as("image/jpg")
    }
  }

}